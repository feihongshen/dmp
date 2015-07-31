package cn.explink.b2c.vipshop.oxo;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;



import net.sf.json.JSONObject;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.explink.b2c.tools.DataImportDAO_B2c;
import cn.explink.b2c.tools.DataImportService_B2c;
import cn.explink.b2c.tools.JiontDAO;
import cn.explink.b2c.tools.JointEntity;
import cn.explink.b2c.tools.JointService;
import cn.explink.b2c.vipshop.ReaderXMLHandler;
import cn.explink.b2c.vipshop.VipShop;
import cn.explink.b2c.vipshop.VipShopConfig;
import cn.explink.b2c.vipshop.VipShopExceptionHandler;
import cn.explink.b2c.vipshop.oxo.response.TpsOxoPickStateVo;
import cn.explink.dao.CustomerDAO;
import cn.explink.dao.CwbDAO;
import cn.explink.service.CwbOrderService;
import cn.explink.service.DataImportService;
import cn.explink.util.DateTimeUtil;
import cn.explink.util.XmlUtil;
import cn.explink.util.MD5.MD5Util;

@Service
public class VipShopOXOGetPickStateService {
	
	@Autowired
	JiontDAO jiontDAO;
	@Autowired
	ReaderXMLHandler readXMLHandler;
	@Autowired
	DataImportService_B2c dataImportService_B2c;
	@Autowired
	DataImportDAO_B2c dataImportDAO_B2c;
	@Autowired
	JointService jointService;
	@Autowired
	CustomerDAO customerDAO;
	@Autowired
	DataImportService dataImportService;
	@Autowired
	CwbDAO cwbDAO;

	@Autowired
	CwbOrderService cwbOrderService;

	private Logger logger = LoggerFactory.getLogger(VipShopOXOGetPickStateService.class);

	public VipShop getVipShop(int key) {
		JointEntity obj = this.jiontDAO.getJointEntity(key);
		if(obj == null){
			return null;
		}
		JSONObject jsonObj = JSONObject.fromObject(obj.getJoint_property());
		VipShop vipshop = (VipShop) JSONObject.toBean(jsonObj, VipShop.class);
		return vipshop;
	}


	/**
	 * 获取唯品会订单信息
	 */
	public long getOrdersByVipShopOXO(int vipshop_key) {
		VipShop vipshop = this.getVipShop(vipshop_key);
		int isOpenFlag = this.jointService.getStateForJoint(vipshop_key);
		if (isOpenFlag == 0) {
			this.logger.info("未开启VipShop_OXO[" + vipshop_key + "]对接！");
			return -1;
		}

		// 构建请求，解析返回信息
		TpsOxoPickStateVo responseVo = this.requestHttpAndCallBackAnaly(vipshop);

		if ((responseVo == null)) {
			this.logger.error("VipShop_OXO提货状态下发接口返回xml字符串为空或解析xml失败！");
			return -1;
		}
		
		String sys_response_code = responseVo.getHead().getSysResponseCode() == null ? ""  : responseVo.getHead().getSysResponseCode(); // 返回码
		String sys_response_msg = responseVo.getHead().getSysRespnoseMsg() == null ? ""  : responseVo.getHead().getSysRespnoseMsg(); // 返回说明
		try {
			VipShopExceptionHandler.respValidateMessage(sys_response_code, sys_response_msg, vipshop);
		} catch (Exception e) {
			this.logger.error("返回VipShop_OXO提货状态下发接口响应信息验证失败！异常原因:", e);
			return -1;
		}
		if (!"S00".equals(sys_response_code)) {
			this.logger.info("当前唯品会返回信息异常，sys_response_code={}", sys_response_code);
			return -1;
		}

		this.logger.info("请求VipShop_OXO提货状态下发信息-返回码：[S00],success ,sys_response_msg={}", sys_response_msg);
			
		if(responseVo.getBinds() == null || CollectionUtils.isEmpty(responseVo.getBinds().getBind())){
			this.logger.info("请求VipShop_OXO提货状态下发接口-没有获取到提货状态信息！,当前SEQ={}", vipshop.getVipshop_seq());
			return -1;
		}
		
		updateCwbPickState(responseVo.getBinds().getBind(),vipshop);

		return 1;

	}
	

	
	
	/**
	 * 更新揽收状态
	 * @param stateRecords
	 * @param vipshop
	 */
	@Transactional
	public void updateCwbPickState(List<TpsOxoPickStateVo.Binds.Bind> stateRecords,VipShop vipshop){
		
		long customerid = Long.valueOf(vipshop.getCustomerids()); //客户id
		
		for(TpsOxoPickStateVo.Binds.Bind pickState : stateRecords){
			//TODO 更新揽收状态
		}
	}
	

	/**
	 * 构建请求，解析返回
	 *
	 * @param vipshop
	 * @return
	 */
	private TpsOxoPickStateVo requestHttpAndCallBackAnaly(VipShop vipshop) {
		String request_time = DateTimeUtil.getNowTime();
		String requestXML = this.StringXMLRequest(vipshop, request_time);
		String MD5Str = vipshop.getPrivate_key() + VipShopConfig.version + request_time + vipshop.getShipper_no();
		String sign = MD5Util.md5(MD5Str, "UTF-8").toLowerCase();
		String endpointUrl = vipshop.getSendCwb_URL();
		String response_XML = null;

		this.logger.info("获取VipShop_OXO提货状态XML={}", requestXML);

		try {
			response_XML = this.HTTPInvokeWs(endpointUrl, VipShopOXOConfig.nameSpace, VipShopOXOConfig.requestMethodName, requestXML, sign);
		} catch (Exception e) {
			this.logger.error("处理唯品会OXO提货状态下发接口请求异常！返回信息：" + response_XML + ",异常原因：" + e.getMessage(), e);
			return null;
		}

		String orderXML = this.readXMLHandler.parseOXORspSOAP(ReaderXMLHandler.parseBack(response_XML));
		this.logger.info("当前下载唯品会XML={}", orderXML);
		TpsOxoPickStateVo obj = null;
		try {
			obj = XmlUtil.toObject(TpsOxoPickStateVo.class, orderXML);
		} catch (Exception e1) {
			e1.printStackTrace();
			this.logger.error("转换唯品会OXO提货状态下发接口响应报文为TpsOxoPickStateVo实体类异常。响应报文内容为："+orderXML ,e1);

		}
		
		return obj;
		
	}

	private String StringXMLRequest(VipShop vipshop, String request_time) {
		StringBuffer sub = new StringBuffer();
		sub.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		sub.append("<request>");
		sub.append("<head>");
		sub.append("<version>" + VipShopConfig.version + "</version>");
		sub.append("<request_time>" + request_time + "</request_time>");
		sub.append("<cust_code>" + vipshop.getShipper_no() + "</cust_code>");
		sub.append("</head>");
		sub.append("</request>");
		return sub.toString();
	}

	private String HTTPInvokeWs(String endpointUrl, String nameSpace, String methodName, String requestXML, String sign) throws Exception {
		StringBuffer result = null;
		OutputStream out = null;
		BufferedReader in = null;
		try {
			String soapActionString = nameSpace + "/" + methodName;
			StringBuffer paramXml = new StringBuffer();
			paramXml.append("<arg0>" + readXMLHandler.parse(requestXML) + "</arg0>");
			paramXml.append("<arg1>" + sign.toLowerCase() + "</arg1>");
			paramXml.append("<arg2>S203</arg2>");

			String soap = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\"><soapenv:Body><" + methodName + " xmlns:tps=\"" + nameSpace + "\">" + paramXml + "</" + methodName
					+ "></soapenv:Body></soapenv:Envelope>";
			logger.info("soap方式请求格式：" + soap);
			URL url = new URL(endpointUrl);
			HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
			httpConn.setRequestProperty("Content-Length", String.valueOf(soap.getBytes("UTF-8").length));
			httpConn.setRequestProperty("Content-Type", "text/xml; charset=UTF-8");
			httpConn.setRequestProperty("soapActionString", soapActionString);
			
			httpConn.setRequestMethod("POST");
			httpConn.setReadTimeout(60000);
			httpConn.setConnectTimeout(60000);
			httpConn.setDoOutput(true);
			httpConn.setDoInput(true);
			out = httpConn.getOutputStream();
			out.write(soap.getBytes("UTF-8"));
			out.flush();

			InputStreamReader isr = new InputStreamReader(httpConn.getInputStream(), "UTF-8");

			in = new BufferedReader(isr);
			result = new StringBuffer("");
			String inputLine;
			while ((inputLine = in.readLine()) != null) {
				result.append(inputLine);
			}

		} catch (Throwable e) {
			e.printStackTrace();
			throw new Exception("WebService服务链路异常:" + e.getMessage(), e);
		} finally {
			if (out != null) {
				out.close();
			}
			if (in != null) {
				in.close();
			}

		}
		return result.toString();
	}
}

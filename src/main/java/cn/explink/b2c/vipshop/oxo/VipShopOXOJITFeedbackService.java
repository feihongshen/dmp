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
import cn.explink.b2c.tools.JiontDAO;
import cn.explink.b2c.tools.JointEntity;
import cn.explink.b2c.tools.JointService;
import cn.explink.b2c.vipshop.ReaderXMLHandler;
import cn.explink.b2c.vipshop.VipShop;
import cn.explink.b2c.vipshop.VipShopExceptionHandler;
import cn.explink.b2c.vipshop.oxo.request.OXOJITFeedbackVo;
import cn.explink.b2c.vipshop.oxo.response.OXOJITFeedbackResponseVo;
import cn.explink.service.SystemInstallService;
import cn.explink.util.DateTimeUtil;
import cn.explink.util.XmlUtil;
import cn.explink.util.MD5.MD5Util;

@Service
public class VipShopOXOJITFeedbackService {
	
	@Autowired
	JiontDAO jiontDAO;
	@Autowired
	ReaderXMLHandler readXMLHandler;
	@Autowired
	DataImportDAO_B2c dataImportDAO_B2c;
	@Autowired
	JointService jointService;
	@Autowired
	SystemInstallService systemInstallService;

	private Logger logger = LoggerFactory.getLogger(VipShopOXOJITFeedbackService.class);

	private VipShop getVipShop(int key) {
		JointEntity obj = this.jiontDAO.getJointEntity(key);
		if(obj == null){
			return null;
		}
		JSONObject jsonObj = JSONObject.fromObject(obj.getJoint_property());
		VipShop vipshop = (VipShop) JSONObject.toBean(jsonObj, VipShop.class);
		return vipshop;
	}

	
	/**
	 * 生成请求实体
	 * @param costomerid
	 * @return
	 */
	private OXOJITFeedbackVo buildRequestVo(VipShop vipshop){
		String	pickPerson = this.systemInstallService.getParameter("OXOJITFeedback.pickPerson");
		String	pickPersonTel = this.systemInstallService.getParameter("OXOJITFeedback.pickPersonTel");
		String	pickCarNo = this.systemInstallService.getParameter("OXOJITFeedback.pickCarNo");
		String	pickQty = this.systemInstallService.getParameter("OXOJITFeedback.pickQty");
		String	expectedPickTime = this.systemInstallService.getParameter("OXOJITFeedback.expectedPickTime");
		String	picUrl = this.systemInstallService.getParameter("OXOJITFeedback.picUrl");
		String	idNumber = this.systemInstallService.getParameter("OXOJITFeedback.idNumber");

		OXOJITFeedbackVo feedbackVo = new OXOJITFeedbackVo();
		
		OXOJITFeedbackVo.Head head = new OXOJITFeedbackVo.Head();
		head.setCustCode(vipshop.getShipper_no());
		head.setRequestTime(DateTimeUtil.getNowTime());
		head.setVersion("1.0");
		
		feedbackVo.setHead(head);
		
		OXOJITFeedbackVo.Binds binds = new OXOJITFeedbackVo.Binds();
		
		List<String> trancwbList = dataImportDAO_B2c.getUnfeedbackOXOJITOrders(100, Long.valueOf(vipshop.getCustomerids()));
		if(CollectionUtils.isNotEmpty(trancwbList)){
			for(String trancwb : trancwbList){
				OXOJITFeedbackVo.Binds.Bind bind = new OXOJITFeedbackVo.Binds.Bind();
				bind.setCustDataId(trancwb);
				bind.setExpectedPickTime(expectedPickTime);
				bind.setIdNumber(idNumber);
				bind.setOrderSn(trancwb);
				bind.setPickCarNo(pickCarNo);
				bind.setPickPerson(pickPerson);
				bind.setPickPersonTel(pickPersonTel);
				bind.setPickQty(pickQty);
				bind.setPicUrl(picUrl);
				binds.getBind().add(bind);
			}
		}
		
		feedbackVo.setBinds(binds);
		
		return feedbackVo;
	}
	
	/**
	 * 生成需要 加密的字符
	 * @param feedbackVo
	 * @return
	 */
	private String generateSign(OXOJITFeedbackVo feedbackVo){
		StringBuilder sb = new StringBuilder();
		sb.append(feedbackVo.getHead().getVersion())
				.append(feedbackVo.getHead().getRequestTime())
				.append(feedbackVo.getHead().getCustCode());		
		for(OXOJITFeedbackVo.Binds.Bind bind : feedbackVo.getBinds().getBind()){
			sb.append(bind.getCustDataId()).append(bind.getOrderSn())
					.append(bind.getPickPerson())
					.append(bind.getPickPersonTel())
					.append(bind.getPickCarNo()).append(bind.getPickQty())
					.append(bind.getExpectedPickTime())
					.append(bind.getPicUrl()).append(bind.getIdNumber());
		}
		
		return sb.toString();
		
	}

	/**
	 * 获取唯品会OXO订单状态(由定时器调用)
	 */
	public long sendVipShopOXOJITFeedback(int vipshop_key) {
		VipShop vipshop = this.getVipShop(vipshop_key);
		int isOpenFlag = this.jointService.getStateForJoint(vipshop_key);
		if (isOpenFlag == 0) {
			this.logger.info("未开启VipShop_OXO[" + vipshop_key + "]对接！");
			return -1;
		}
		
		// 构建请求，解析返回信息
		OXOJITFeedbackResponseVo responseVo = this.requestHttpAndCallBackAnaly(vipshop);

		if ((responseVo == null)) {
			this.logger.error("VipShop_OXO提货任务反馈接口返回xml字符串为空或解析xml失败！");
			return -1;
		}
		
		String sys_response_code = responseVo.getHead().getSysResponseCode() == null ? ""  : responseVo.getHead().getSysResponseCode(); // 返回码
		String sys_response_msg = responseVo.getHead().getSysRespnoseMsg() == null ? ""  : responseVo.getHead().getSysRespnoseMsg(); // 返回说明
		try {
			VipShopExceptionHandler.respValidateMessage(sys_response_code, sys_response_msg, vipshop);
		} catch (Exception e) {
			this.logger.error("返回VipShop_OXO提货任务反馈接口响应信息验证失败！异常原因:", e);
			return -1;
		}
		if (!"S00".equals(sys_response_code)) {
			this.logger.info("当前唯品会返回信息异常，sys_response_code={}", sys_response_code);
			return -1;
		}

		this.logger.info("请求VipShop_OXO提货任务反馈接口信息-返回码：[S00],success ,sys_response_msg={}", sys_response_msg);
			
		if(responseVo.getBinds() == null || CollectionUtils.isEmpty(responseVo.getBinds().getBind())){
			this.logger.info("请求VipShop_OXO提货任务反馈接口-没有获取到响应信息！");
			return -1;
		}
		
		updateOxojitfeedbackflag(responseVo.getBinds().getBind(),vipshop);

		return 1;

	}
	

	
	/**
	 * 更新 Oxojitfeedbackflag = 1
	 * @param binds
	 * @param vipshop
	 */
	@Transactional
	public void updateOxojitfeedbackflag(List<OXOJITFeedbackResponseVo.Binds.Bind> binds,VipShop vipshop){
		
		long customerid = Long.valueOf(vipshop.getCustomerids()); //客户id
				
		StringBuilder transcwbs = new StringBuilder();
		for(OXOJITFeedbackResponseVo.Binds.Bind bind : binds){
			try{
				VipShopExceptionHandler.validateBizResponseCode(bind.getBizResponseCode(), bind.getBizRespnoseMsg(), vipshop);
			}catch(Exception e){
				this.logger.error("解析VipShop_OXO提货任务反馈接口响应信息验证失败！失败原因:", e);
			}
			transcwbs.append("'").append(bind.getCustDataId()).append("',");
		}
		if(transcwbs.length() > 0){
			dataImportDAO_B2c.updateOXOJITfeedbackflag(transcwbs.substring(0, transcwbs.length() - 1), customerid);
		}

	}
	

	/**
	 * 构建请求，解析返回
	 *
	 * @param vipshop
	 * @return
	 */
	private OXOJITFeedbackResponseVo requestHttpAndCallBackAnaly(VipShop vipshop) {
		
		OXOJITFeedbackVo feedbackVo = this.buildRequestVo(vipshop);
		if(CollectionUtils.isEmpty(feedbackVo.getBinds().getBind())){
			logger.info("在系统中未找到需要进行OXO提货任务反馈的数据,放弃本次请求VipShop_OXO提货任务反馈接口。同时返回null作为结果。");
			return null;
		}
		String MD5Str = vipshop.getPrivate_key() + this.generateSign(feedbackVo);
		String sign = MD5Util.md5(MD5Str, "UTF-8").toLowerCase();
		String endpointUrl = vipshop.getSendCwb_URL();
		String requestXML = null;
		String response_XML = null;
		
		try{
			requestXML = XmlUtil.toXml(OXOJITFeedbackVo.class, feedbackVo);
		}catch(Exception e){
			this.logger.error("VipShop_OXO提货任务反馈实体转xml报文异常",e);
			return null;
		}
		this.logger.info("请求VipShop_OXO提货任务反馈接口XML={}", requestXML);

		try {
			response_XML = this.HTTPInvokeWs(endpointUrl, VipShopOXOConfig.nameSpace, VipShopOXOConfig.requestMethodName, requestXML, sign);
		} catch (Exception e) {
			this.logger.error("处理VipShop_OXO提货任务反馈接口请求异常！返回信息：" + response_XML + ",异常原因：" + e.getMessage(), e);
			return null;
		}

		String orderXML = this.readXMLHandler.parseOXORspSOAP(ReaderXMLHandler.parseBack(response_XML));
		this.logger.info("当前下载VipShop_OXO提货任务反馈接口响应报文XML={}", orderXML);
		OXOJITFeedbackResponseVo obj = null;
		try {
			obj = XmlUtil.toObject(OXOJITFeedbackResponseVo.class, orderXML);
		} catch (Exception e1) {
			e1.printStackTrace();
			this.logger.error("转换VipShop_OXO提货任务反馈接口响应报文为OXOJITFeedbackResponseVo实体类异常。响应报文内容为："+orderXML ,e1);

		}
		
		return obj;
		
	}

	private String HTTPInvokeWs(String endpointUrl, String nameSpace, String methodName, String requestXML, String sign) throws Exception {
		StringBuffer result = null;
		OutputStream out = null;
		BufferedReader in = null;
		try {
			String soapActionString = nameSpace + "/" + methodName;
			StringBuffer paramXml = new StringBuffer();
			paramXml.append("<content>" + readXMLHandler.parse(requestXML) + "</content>");
			paramXml.append("<sign>" + sign.toLowerCase() + "</sign>");
			paramXml.append("<serviceCode>S202</serviceCode>");

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

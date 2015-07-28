package cn.explink.b2c.vipshop.oxo;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.explink.b2c.tools.DataImportDAO_B2c;
import cn.explink.b2c.tools.DataImportService_B2c;
import cn.explink.b2c.tools.JiontDAO;
import cn.explink.b2c.tools.JointEntity;
import cn.explink.b2c.tools.JointService;
import cn.explink.b2c.vipshop.ReaderXMLHandler;
import cn.explink.b2c.vipshop.VipShop;
import cn.explink.b2c.vipshop.VipShopConfig;
import cn.explink.b2c.vipshop.VipShopExceptionHandler;
import cn.explink.b2c.vipshop.oxo.response.TpsOrderVo;
import cn.explink.controller.CwbOrderDTO;
import cn.explink.dao.CustomWareHouseDAO;
import cn.explink.dao.CustomerDAO;
import cn.explink.dao.CwbDAO;
import cn.explink.dao.OrderGoodsDAO;
import cn.explink.dao.UserDAO;
import cn.explink.domain.EmailDate;
import cn.explink.domain.User;
import cn.explink.enumutil.CwbOrderTypeIdEnum;
import cn.explink.enumutil.PaytypeEnum;
import cn.explink.service.CwbOrderService;
import cn.explink.service.DataImportService;
import cn.explink.util.DateTimeUtil;
import cn.explink.util.XmlUtil;
import cn.explink.util.MD5.MD5Util;

@Service
public class VipShopOXOGetCwbDataService {
	
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
	CustomWareHouseDAO customWarehouseDAO;
	@Autowired
	OrderGoodsDAO orderGoodsDAO;
	@Autowired
	UserDAO userDAO;
	@Autowired
	DataImportService dataImportService;
	@Autowired
	ReaderXMLHandler readXML;
	@Autowired
	CwbDAO cwbDAO;
	
	@Autowired
	CwbOrderService cwbOrderService;

	private Logger logger = LoggerFactory.getLogger(VipShopOXOGetCwbDataService.class);

	public VipShop getVipShop(int key) {
		JointEntity obj = this.jiontDAO.getJointEntity(key);
		if(obj == null){
			return null;
		}
		JSONObject jsonObj = JSONObject.fromObject(obj.getJoint_property());
		VipShop vipshop = (VipShop) JSONObject.toBean(jsonObj, VipShop.class);
		return vipshop;
	}

	public void edit(HttpServletRequest request, int joint_num) {
		VipShop vipshop = new VipShop();
		vipshop.setShipper_no(request.getParameter("shipper_no"));
		vipshop.setPrivate_key(request.getParameter("private_key"));
		vipshop.setGetMaxCount(Integer.parseInt(request.getParameter("getMaxCount")));
		if(StringUtils.isNotBlank(request.getParameter("sendMaxCount"))){
			vipshop.setSendMaxCount(Integer.parseInt(request.getParameter("sendMaxCount")));
		}
		vipshop.setWarehouseid(Long.parseLong(request.getParameter("warehouseid")));
		vipshop.setGetCwb_URL(request.getParameter("getCwb_URL"));
		vipshop.setSendCwb_URL(request.getParameter("sendCwb_URL"));
		vipshop.setCustomerids(request.getParameter("customerids"));
		vipshop.setVipshop_seq(Long.parseLong(request.getParameter("vipshop_seq")));
		String customerids = request.getParameter("customerids");
			
		String oldCustomerids = "";

		JSONObject jsonObj = JSONObject.fromObject(vipshop);
		JointEntity jointEntity = this.jiontDAO.getJointEntity(joint_num);
		if (jointEntity == null) {
			jointEntity = new JointEntity();
			jointEntity.setJoint_num(joint_num);
			jointEntity.setJoint_property(jsonObj.toString());
			this.jiontDAO.Create(jointEntity);
		} else {
			try {
				oldCustomerids = this.getVipShop(joint_num).getCustomerids();
			} catch (Exception e) {
			}
			jointEntity.setJoint_num(joint_num);
			jointEntity.setJoint_property(jsonObj.toString());
			this.jiontDAO.Update(jointEntity);
		}
		// 保存 枚举到供货商表中
		this.customerDAO.updateB2cEnumByJoint_num(customerids, oldCustomerids, joint_num);
	}

	public void updateMaxSEQ(int joint_num, VipShop vipshop) {
		VipShop vip = new VipShop();
		vip.setShipper_no(vipshop.getShipper_no());
		vip.setPrivate_key(vipshop.getPrivate_key());
		vip.setGetMaxCount(vipshop.getGetMaxCount());
		vip.setSendMaxCount(vipshop.getSendMaxCount());
		vip.setGetCwb_URL(vipshop.getGetCwb_URL());
		vip.setSendCwb_URL(vipshop.getSendCwb_URL());
		vip.setCustomerids(vipshop.getCustomerids());
		vip.setVipshop_seq(vipshop.getVipshop_seq());
		vip.setWarehouseid(vipshop.getWarehouseid());

		JSONObject jsonObj = JSONObject.fromObject(vip);
		JointEntity jointEntity = this.jiontDAO.getJointEntity(joint_num);
		jointEntity.setJoint_num(joint_num);
		jointEntity.setJoint_property(jsonObj.toString());
		this.jiontDAO.Update(jointEntity);

	}

	public void update(int joint_num, int state) {
		this.jiontDAO.UpdateState(joint_num, state);
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
		TpsOrderVo responseVo = this.requestHttpAndCallBackAnaly(vipshop);

		if ((responseVo == null)) {
			this.logger.error("系统返回xml字符串为空或解析xml失败！");
			return -1;
		}
		
		String sys_response_code = responseVo.getHead().getSysResponseCode() == null ? ""  : responseVo.getHead().getSysResponseCode(); // 返回码
		String sys_response_msg = responseVo.getHead().getSysRespnoseMsg() == null ? ""  : responseVo.getHead().getSysRespnoseMsg(); // 返回说明
		try {
			VipShopExceptionHandler.respValidateMessage(sys_response_code, sys_response_msg, vipshop);
		} catch (Exception e) {
			this.logger.error("返回VipShop_OXO订单查询信息验证失败！异常原因:", e);
			return -1;
		}
		if (!"S00".equals(sys_response_code)) {
			this.logger.info("当前唯品会返回信息异常，sys_response_code={}", sys_response_code);
			return -1;
		}

		this.logger.info("请求VipShop_OXO订单信息-返回码：[S00],success ,sys_response_msg={}", sys_response_msg);
			
		if(responseVo.getOrders() == null || CollectionUtils.isEmpty(responseVo.getOrders().getOrder())){
			this.logger.info("请求VipShop_OXO订单信息-没有获取到订单或者订单信息重复！,当前SEQ={}", vipshop.getVipshop_seq());
			return -1;
		}
		
		extractedDataImport(responseVo.getOrders().getOrder(),vipshop);

		return 1;

	}
	
	private void convertOrderVoToCwbOrderDTO(CwbOrderDTO cwbOrder,TpsOrderVo.Orders.Order order,VipShop vipshop){
		if(StringUtils.isNotBlank(order.getBusinessType())){
			if(order.getBusinessType().equals("20")){
				cwbOrder.setCwbordertypeid(CwbOrderTypeIdEnum.OXO_JIT.getValue());
			}
			else if(order.getBusinessType().equals("40")){
				cwbOrder.setCwbordertypeid(CwbOrderTypeIdEnum.OXO.getValue());
			}
			else if(order.getBusinessType().equals("30")){//保留类型
				//TODO保留类型
			}
		}
		cwbOrder.setCwbordertypeid(Long.valueOf(order.getBusinessType()));
		cwbOrder.setRemark1(order.getBrandName()+"&"+order.getStoreName()+"&"+order.getStoreContacts()+"&"+order.getStoreTel());//品牌商&提货门店&提货联系人&提货联系方式
		cwbOrder.setRemark2(order.getCustomerName());//订单客户，还不确定存到哪个字段 
		cwbOrder.setRemark3(order.getValuationValue());//保价价值 ,新增字段
		cwbOrder.setRemark4(order.getCnorProv()+"&"+order.getCnorCity()+"&"+order.getCnorRegion()+"&"+order.getCnorAddr());//发货省，市，区，详细地址
		cwbOrder.setRemark5( order.getWarehouse()+"&"+order.getWarehouseAddr());//仓库+仓库地址 
	
		cwbOrder.setCustomercommand(order.getRequiredTime());//要求提货时间
		cwbOrder.setTranscwb(order.getCustOrderNo());//TMS订单号
		cwbOrder.setCwb(order.getOrderSn()); //订单号，tps运单号
		cwbOrder.setConsigneename( order.getBuyerName());//收件人
		cwbOrder.setConsigneeaddress(order.getBuyerAddress());//收件人地址
		cwbOrder.setConsigneephone(order.getTel());//收件人电话
		cwbOrder.setConsigneemobile(order.getMobile());//收件人手机
		cwbOrder.setCargoamount(order.getGoodsMoney());//商品价格
		cwbOrder.setReceivablefee(order.getMoney());
		if(StringUtils.isNotBlank(order.getExtPayType())){
			if(order.getExtPayType().equals("-1")){ //非货到付款
				cwbOrder.setPaywayid(PaytypeEnum.Qita.getValue());
			}
			
			if(order.getExtPayType().equals("0")){//货到付款现金支付
				cwbOrder.setPaywayid(PaytypeEnum.Xianjin.getValue());
			}
			
			if(order.getExtPayType().equals("1")){//货到付款刷卡支付
				cwbOrder.setPaywayid(PaytypeEnum.Pos.getValue());
			}
			
			if(order.getExtPayType().equals("2")){//货到付款支付宝 支付
				cwbOrder.setPaywayid(PaytypeEnum.CodPos.getValue());
			}
		}
		cwbOrder.setConsigneepostcode(order.getPostCode());//收件人邮政编码
		cwbOrder.setCargorealweight(order.getOriginalWeight());//重量
		cwbOrder.setCargovolume(order.getOriginalVolume()); //体积
		
		cwbOrder.setSendcargonum(order.getTotalPack());//箱数
		
		//orderMap.put("", order.getSalesPlatform());//销售平台，不确定要不要存
		cwbOrder.setCargotype(order.getTransportType());//品类
		
		cwbOrder.setShouldfare(order.getFreight());//运费
		//orderMap.put("", order.getOrgName()); //提货站点名称
		//orderMap.put("", order.getOrgCode());//提货站点编码
		cwbOrder.setCustomerid(Long.valueOf(vipshop.getCustomerids())); //客户id
		cwbOrder.setStartbranchid(vipshop.getWarehouseid());
				
	}

	/**
	 * 插入到临时表
	 * @param orders
	 */
	private void extractedDataImport(List<TpsOrderVo.Orders.Order> orders,VipShop vipshop){
	
		long warehouseid = vipshop.getWarehouseid(); // 获取虚拟库房
		
		long customerid = Long.valueOf(vipshop.getCustomerids()); //客户id

		EmailDate ed = dataImportService.getOrCreateEmailDate_B2C(Long.valueOf(vipshop.getCustomerids()), 0, warehouseid, 0); //生成批次
		User user = new User();
		user.setUserid(1);
		user.setBranchid(warehouseid);
		
		for(TpsOrderVo.Orders.Order order : orders){
			try{
				CwbOrderDTO cwbOrder = new CwbOrderDTO();
				convertOrderVoToCwbOrderDTO(cwbOrder,order,vipshop);
				if("new".equalsIgnoreCase(order.getCmdType())){
					//cwb 对应 order_sn是否已经存在 continue
					if(dataImportDAO_B2c.getCwbByCwbB2ctemp(cwbOrder.getCwb()) != null){
						continue;
					}
					dataImportDAO_B2c.insertCwbOrder_toTempTable(cwbOrder, customerid, warehouseid, user, ed);
	
				}
				else if("edit".equalsIgnoreCase(order.getCmdType())){
					//判断揽件状态是否 是 揽件成功 不允许编辑 continue
					//cwbDAO.get
					//dataImportDAO_B2c.update
				}
				else if("cancel".equalsIgnoreCase(order.getCmdType())){
					
					//TODO 判断订单是否已经揽收成功，如果揽收成功则不允许取消。如果未揽收成功，则执行取消操作
					dataImportDAO_B2c.dataLoseB2ctempByCwb(cwbOrder.getCwb());
					this.cwbDAO.dataLoseByCwb(cwbOrder.getCwb());
					cwbOrderService.datalose_vipshop(cwbOrder.getCwb());
				}
			
			}catch(Exception e){
				logger.error("VIP_OXO数据插入临时表发生未知异常cwb=" + order.getOrderSn(), e);
			}
			

		}
	}
	

	/**
	 * 构建请求，解析返回
	 *
	 * @param vipshop
	 * @return
	 */
	private TpsOrderVo requestHttpAndCallBackAnaly(VipShop vipshop) {
		String request_time = DateTimeUtil.getNowTime();
		String requestXML = this.StringXMLRequest(vipshop, request_time);
		String MD5Str = vipshop.getPrivate_key() + VipShopConfig.version + request_time + vipshop.getShipper_no() + vipshop.getVipshop_seq() + vipshop.getGetMaxCount();
		String sign = MD5Util.md5(MD5Str, "UTF-8").toLowerCase();
		String endpointUrl = vipshop.getGetCwb_URL();
		String response_XML = null;

		this.logger.info("获取VipShop_OXO订单XML={}", requestXML);

		try {
			response_XML = this.HTTPInvokeWs(endpointUrl, VipShopOXOConfig.nameSpace, VipShopOXOConfig.requestMethodName, requestXML, sign);
		} catch (Exception e) {
			this.logger.error("处理唯品会OXO订单请求异常！返回信息：" + response_XML + ",异常原因：" + e.getMessage(), e);
			return null;
		}

		String orderXML = this.readXMLHandler.parseOXORspSOAP(ReaderXMLHandler.parseBack(response_XML));
		this.logger.info("当前下载唯品会XML={}", orderXML);
		TpsOrderVo obj = null;
		try {
			obj = XmlUtil.toObject(TpsOrderVo.class, orderXML);
		} catch (Exception e1) {
			e1.printStackTrace();
			this.logger.error("转换唯品会OXO响应报文为TpsOrderVo实体类异常。响应报文内容为："+orderXML ,e1);

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
		sub.append("<seq>" + vipshop.getVipshop_seq() + "</seq>");
		sub.append("<count>" + vipshop.getGetMaxCount() + "</count>");
		sub.append("</head>");
		sub.append("</request>");
		return sub.toString();
	}

	/**
	 * 验证返回的sign
	 *
	 * @param paseSign
	 * @return
	 */
	public boolean checkSignResponseInfo(String paseSign, String MD5Str) {
		if (paseSign.equals(MD5Str)) {
			return true;
		}
		return true;
	}

	
	private String HTTPInvokeWs(String endpointUrl, String nameSpace, String methodName, String requestXML, String sign) throws Exception {
		StringBuffer result = null;
		OutputStream out = null;
		BufferedReader in = null;
		try {
			String soapActionString = nameSpace + "/" + methodName;
			StringBuffer paramXml = new StringBuffer();
			readXML = readXML == null? new ReaderXMLHandler():readXML;
			paramXml.append("<arg0>" + readXML.parse(requestXML) + "</arg0>");
			paramXml.append("<arg1>" + sign.toLowerCase() + "</arg1>");
			paramXml.append("<arg2>S201</arg2>");

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
	public static void main(String[] args) {
		//String[] seq_arrs={"110000011787834","110000011787835","110000011787838","110000011787840"};
		
		//System.out.println(getMaxSEQ(seq_arrs));
		VipShop vipshop = new VipShop();
		vipshop.setGetCwb_URL("http://test.tps.vip.com:7172/ws/tpsCarrierService");
		vipshop.setPrivate_key("30113");
		vipshop.setShipper_no("NHLDP089");
		vipshop.setVipshop_seq(0);
		vipshop.setGetMaxCount(10);
		VipShopOXOGetCwbDataService service = new VipShopOXOGetCwbDataService();
		service.requestHttpAndCallBackAnaly(vipshop);
		
	}
	
	
	
}

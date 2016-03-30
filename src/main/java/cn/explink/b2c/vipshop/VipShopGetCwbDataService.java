package cn.explink.b2c.vipshop;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import cn.explink.b2c.tools.B2cEnum;
import cn.explink.b2c.tools.DataImportDAO_B2c;
import cn.explink.b2c.tools.DataImportService_B2c;
import cn.explink.b2c.tools.JiontDAO;
import cn.explink.b2c.tools.JointEntity;
import cn.explink.b2c.tools.JointService;
import cn.explink.controller.CwbOrderDTO;
import cn.explink.dao.CustomWareHouseDAO;
import cn.explink.dao.CustomerDAO;
import cn.explink.dao.CwbDAO;
import cn.explink.dao.OrderGoodsDAO;
import cn.explink.dao.SystemInstallDAO;
import cn.explink.dao.UserDAO;
import cn.explink.domain.Customer;
import cn.explink.domain.OrderGoods;
import cn.explink.domain.SystemInstall;
import cn.explink.enumutil.CwbOrderTypeIdEnum;
import cn.explink.enumutil.IsmpsflagEnum;
import cn.explink.enumutil.MPSAllArrivedFlagEnum;
import cn.explink.enumutil.MpsTypeEnum;
import cn.explink.enumutil.MpsswitchTypeEnum;
import cn.explink.enumutil.PaytypeEnum;
import cn.explink.service.CwbOrderService;
import cn.explink.service.DataImportService;
import cn.explink.support.transcwb.TransCwbDao;
import cn.explink.util.DateTimeUtil;

@Service
public class VipShopGetCwbDataService {

	@Autowired
	JiontDAO jiontDAO;
	@Autowired
	SOAPHandler soapHandler;
	@Autowired
	ReaderXMLHandler readXMLHandler;
	@Autowired
	CwbDAO cwbDAO;
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
	CwbOrderService cwbOrderService;
	@Autowired
	TransCwbDao transCwbDao;
	@Autowired
	VipshopInsertCwbDetailTimmer vipshopInsertCwbDetailTimmer;
	@Autowired
	DataImportService dataImportService;
	@Autowired
	SystemInstallDAO systemInstallDAO;

	private Logger logger = LoggerFactory.getLogger(VipShopGetCwbDataService.class);

	public String getObjectMethod(int key) {
		JointEntity obj = this.jiontDAO.getJointEntity(key);
		return obj == null ? null : obj.getJoint_property();
	}

	public VipShop getVipShop(int key) {
		if (this.getObjectMethod(key) == null) {
			return null;
		}
		JSONObject jsonObj = JSONObject.fromObject(this.getObjectMethod(key));
		VipShop vipshop = (VipShop) JSONObject.toBean(jsonObj, VipShop.class);
		return vipshop;
	}

	@Transactional
	public void edit(HttpServletRequest request, int joint_num) {
		VipShop vipshop = new VipShop();
		vipshop.setDaysno((request.getParameter("daysno")==null||("".equals(request.getParameter("daysno"))))?5:(Integer.valueOf(request.getParameter("daysno"))));
		vipshop.setSelb2cnum((request.getParameter("selb2cnum")==null||("".equals(request.getParameter("selb2cnum"))))?200:(Long.valueOf(request.getParameter("selb2cnum"))));
		vipshop.setShipper_no(request.getParameter("shipper_no"));
		vipshop.setPrivate_key(request.getParameter("private_key"));
		vipshop.setGetMaxCount(Integer.parseInt(request.getParameter("getMaxCount")));
		vipshop.setSendMaxCount(Integer.parseInt(request.getParameter("sendMaxCount")));
		vipshop.setGetCwb_URL(request.getParameter("getCwb_URL"));
		vipshop.setSendCwb_URL(request.getParameter("sendCwb_URL"));
		vipshop.setCustomerids(request.getParameter("customerids"));
		vipshop.setWarehouseid(Long.parseLong(request.getParameter("warehouseid")));
		vipshop.setVipshop_seq(Long.parseLong(request.getParameter("vipshop_seq")));
		String customerids = request.getParameter("customerids");
		vipshop.setIsopendownload(Integer.parseInt(request.getParameter("isopendownload")));
		vipshop.setForward_hours(Integer.parseInt(request.getParameter("forward_hours")));
		vipshop.setIsTuoYunDanFlag(Integer.parseInt(request.getParameter("isTuoYunDanFlag")));
		vipshop.setIsShangmentuiFlag(Integer.parseInt(request.getParameter("isShangmentuiFlag").equals("") ? "0" : request.getParameter("isShangmentuiFlag")));
		
		String cancelOrIntercept=request.getParameter("cancelOrIntercept").equals("")?"0":request.getParameter("cancelOrIntercept");
		vipshop.setCancelOrIntercept(Integer.parseInt(cancelOrIntercept));
		
		String isOpenLefengflag=request.getParameter("isOpenLefengflag").equals("")?"0":request.getParameter("isOpenLefengflag");
		vipshop.setIsOpenLefengflag(Integer.parseInt(isOpenLefengflag));

		String resuseReasonFlag=request.getParameter("resuseReasonFlag").equals("")?"0":request.getParameter("resuseReasonFlag");
		vipshop.setResuseReasonFlag(Integer.parseInt(resuseReasonFlag));
		String lefengCustomerid=request.getParameter("lefengCustomerid");
		vipshop.setLefengCustomerid(lefengCustomerid);
		String isCreateTimeToEmaildateFlag=request.getParameter("isCreateTimeToEmaildateFlag").equals("")?"0":request.getParameter("isCreateTimeToEmaildateFlag");
		vipshop.setIsCreateTimeToEmaildateFlag(Integer.parseInt(isCreateTimeToEmaildateFlag));
		/*String daysno=request.getParameter("daysno").equals("")?"3":request.getParameter("daysno");
		String selb2cnum=request.getParameter("selb2cnum").equals("")?"0":request.getParameter("selb2cnum");
		vipshop.setSelb2cnum(Integer.parseInt(selb2cnum));
		vipshop.setDaysno(Integer.parseInt(daysno));*/
		
		vipshop.setOpenmpspackageflag(Integer.valueOf((request.getParameter("openmpspackageflag")==null||("".equals(request.getParameter("openmpspackageflag"))))?0:(Integer.valueOf(request.getParameter("openmpspackageflag")))));
		vipshop.setTransflowUrl(request.getParameter("transflowUrl"));
		
		String oldLefengCustomerids = ""; //乐蜂customerid
		
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
				oldLefengCustomerids = this.getVipShop(joint_num).getLefengCustomerid();
			} catch (Exception e) {
			}
			jointEntity.setJoint_num(joint_num);
			jointEntity.setJoint_property(jsonObj.toString());
			this.jiontDAO.Update(jointEntity);
		}
		// 保存 枚举到供货商表中
		this.customerDAO.updateB2cEnumByJoint_num(customerids, oldCustomerids, joint_num);
		this.customerDAO.updateB2cEnumByJoint_num(lefengCustomerid, oldLefengCustomerids, joint_num);
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
		vip.setIsopendownload(vipshop.getIsopendownload());
		vip.setForward_hours(vipshop.getForward_hours());
		vip.setIsTuoYunDanFlag(vipshop.getIsTuoYunDanFlag());
		vip.setIsShangmentuiFlag(vipshop.getIsShangmentuiFlag());
		vip.setIsOpenLefengflag(vipshop.getIsOpenLefengflag());
		vip.setLefengCustomerid(vipshop.getLefengCustomerid());
		vip.setIsCreateTimeToEmaildateFlag(vipshop.getIsCreateTimeToEmaildateFlag());
		vip.setOpenmpspackageflag(vipshop.getOpenmpspackageflag());
		vip.setTransflowUrl(vipshop.getTransflowUrl());
		vip.setSelb2cnum(vipshop.getSelb2cnum());
		vip.setDaysno(vipshop.getDaysno());
		
		
		JSONObject jsonObj = JSONObject.fromObject(vip);
		JointEntity jointEntity = this.jiontDAO.getJointEntity(joint_num);
		jointEntity.setJoint_num(joint_num);
		jointEntity.setJoint_property(jsonObj.toString());
		this.jiontDAO.Update(jointEntity);

	}

	public void update(int joint_num, int state) {
		this.jiontDAO.UpdateState(joint_num, state);
	}

	public int getStateForVipShop(int key) {
		JointEntity obj = null;
		int state = 0;
		try {
			obj = this.jiontDAO.getJointEntity(key);
			state = obj.getState();
		} catch (Exception e) {
			// TODO: handle exception
		}
		return state;
	}

	/**
	 * 获取唯品会订单信息
	 */
	@Transactional
	public long getOrdersByVipShop(int vipshop_key) {
		
		Date dt1 = new Date();
		
		VipShop vipshop = this.getVipShop(vipshop_key);
		int isOpenFlag = this.jointService.getStateForJoint(vipshop_key);
		if (isOpenFlag == 0) {
			this.logger.info("未开启vipshop[" + vipshop_key + "]对接！");
			return -1;
		}
		if (vipshop.getIsopendownload() == 0) {
			this.logger.info("未开启vipshop[" + vipshop_key + "]订单下载接口");
			return -1;
		}
		
		SystemInstall systemInstall = systemInstallDAO.getSystemInstall("feedbackOrderResult");
		// 是否开启反馈订单结果接口
		boolean feedbackOrderResult = false;
		if(systemInstall != null && "1".equals(systemInstall.getValue())){
			feedbackOrderResult = true;
		}

		// 构建请求，解析返回信息
		Map<String, Object> parseMap = this.requestHttpAndCallBackAnaly(vipshop, feedbackOrderResult);

		if ((parseMap == null) || (parseMap.size() == 0)) {
			this.logger.error("系统返回xml字符串为空或解析xml失败！");
			return -1;
		}

		String sys_response_code = parseMap.get("sys_response_code") != null ? parseMap.get("sys_response_code").toString() : ""; // 返回码
		String sys_response_msg = parseMap.get("sys_respnose_msg") != null ? parseMap.get("sys_respnose_msg").toString() : ""; // 返回说明
		try {
			VipShopExceptionHandler.respValidateMessage(sys_response_code, sys_response_msg, vipshop);
		} catch (Exception e) {
			this.logger.error("返回vipshop订单查询信息验证失败！异常原因:", e);
			return -1;
		}
		if (!"S00".equals(sys_response_code)) {
			this.logger.info("当前唯品会返回信息异常，sys_response_code={}", sys_response_code);
			return -1;
		}

		this.logger.info("请求Vipshop订单信息-返回码：[S00],success ,sys_response_msg={}", sys_response_msg);

		String parseMD5Str = VipShopMD5Util.parseJonitMD5Str(parseMap, vipshop.getPrivate_key());
		boolean checkRespSign = this.checkSignResponseInfo(parseMap.get("sign").toString(), VipShopMD5Util.MD5(parseMD5Str));
		if (!checkRespSign) {
			this.logger.error("请求Vipshop订单信息-返回签名验证失败！本地签名：[" + VipShopMD5Util.MD5(parseMD5Str) + "],返回签名：[" + parseMap.get("sign") + "]");
			return -1;
		}

		// 订单处理结果 
		Map<String, Boolean> resultMap = parseResultMap(parseMap);
		List<Map<String, String>> orderlist = this.parseXmlDetailInfo(parseMap, vipshop, resultMap);
		// 带反馈接口如果返回有数据如果全部是重复数据，也要去反馈。
		if (CollectionUtils.isEmpty(orderlist) && resultMap.size() == 0) {
			this.updateMaxSEQ(vipshop_key, vipshop);
			this.logger.info("请求Vipshop订单信息-没有获取到订单或者订单信息重复！,当前SEQ={}", vipshop.getVipshop_seq());
			return -1;
		}

		if (vipshop.getIsTuoYunDanFlag() == 0) {
			for (Map<String, String> dataMap : orderlist) {
				extractedDataImport(vipshop_key, vipshop, orderlist, dataMap, resultMap);
			}			
		} else {
			for (Map<String, String> dataMap : orderlist) {
				extractedDataImportByEmaildate(vipshop_key, vipshop, dataMap, resultMap);
			}
		}
		
		if(feedbackOrderResult && resultMap.size() >= 1){
			feedbackOrderResult(vipshop, resultMap);
		}
		return 1;
	}
	
	public void extractedDataImportByEmaildate(int vipshop_key,
			VipShop vipshop, Map<String, String> dataMap, Map<String, Boolean> resultMap) {
		List<Map<String, String>> onelist = new ArrayList<Map<String, String>>();
		onelist.add(dataMap);
		long customerid = Long.valueOf(dataMap.get("customerid"));
		try {
			// modify by bruce shangguan 20160329  托运单开启取,emaildate取rec_create_time　
			String emaildate = dataMap.get("rec_create_time").toString();			
			   //Added by leoliao at 2016-03-09 如果传过来的出仓时间为空，则使用当前日期作为批次时间
			if(emaildate == null || emaildate.trim().equals("")){
				emaildate = DateTimeUtil.getNowDate() + " 00:00:00";				
				dataMap.put("rec_create_time", emaildate);
			}
			   //Added end
			// modify end 
			long warehouseid = vipshop.getWarehouseid();
			this.dataImportService_B2c.Analizy_DataDealByB2cByEmaildate(customerid, B2cEnum.VipShop_beijing.getMethod(), onelist, warehouseid, true, emaildate, 0);

			this.updateMaxSEQ(vipshop_key, vipshop);
			this.logger.info("请求Vipshop订单信息导入成功cwb={}-更新了最大的SEQ!{}", dataMap.get("cwb").toString(), vipshop.getVipshop_seq());
		} catch (Exception e) {
			markResultMap(dataMap, resultMap, false);
			this.logger.error("vipshop调用数据导入接口异常!cwb=" + dataMap.get("cwb").toString(), e);
		}
	}
	
	public void extractedDataImport(int vipshop_key, VipShop vipshop,
			List<Map<String, String>> orderlist, Map<String, String> dataMap, Map<String, Boolean> resultMap) {
		List<Map<String, String>> onelist = new ArrayList<Map<String, String>>();
		onelist.add(dataMap);
		long customerid = Long.valueOf(dataMap.get("customerid"));
		try {
			long warehouseid = vipshop.getWarehouseid();
			if(vipshop.getIsCreateTimeToEmaildateFlag()==0){
				this.dataImportService_B2c.Analizy_DataDealByB2c(customerid, B2cEnum.VipShop_beijing.getMethod(), onelist, warehouseid, true);
			}else{
				String emaildate = dataMap.get("remark2").toString();
				//Added by leoliao at 2016-03-09 如果传过来的出仓时间为空，则使用当前日期作为批次时间
				if(emaildate == null || emaildate.trim().equals("")){
					emaildate = DateTimeUtil.getNowDate() + " 00:00:00";
					dataMap.put("remark2", emaildate);
				}
				//Added end
				this.dataImportService_B2c.Analizy_DataDealByB2cByEmaildate(customerid, B2cEnum.VipShop_beijing.getMethod(), onelist, warehouseid, true, emaildate, 0);
			}
			this.logger.info("请求Vipshop订单信息-插入数据库处理成功！");
			
			this.updateMaxSEQ(vipshop_key, vipshop);
			this.logger.info("请求Vipshop订单信息-更新了最大的SEQ!{}", vipshop.getVipshop_seq());
		} catch (Exception e) {
			markResultMap(dataMap, resultMap, false);
			this.logger.error("vipshop调用数据导入接口异常!,订单List信息:" + orderlist + "message:", e);			
		}
	}

	/**
	 * 构建请求，解析返回
	 *
	 * @param vipshop
	 * @return
	 */
	private Map<String, Object> requestHttpAndCallBackAnaly(VipShop vipshop, boolean feedbackOrderResult) {
		
		String request_time = DateTimeUtil.getNowTime();
		String requestXML = this.StringXMLRequest(vipshop, request_time, feedbackOrderResult);
		String MD5Str = null;
		if(feedbackOrderResult){
			MD5Str = vipshop.getPrivate_key() + VipShopConfig.version + request_time + vipshop.getShipper_no() + vipshop.getGetMaxCount();
		}else{
			MD5Str = vipshop.getPrivate_key() + VipShopConfig.version + request_time + vipshop.getShipper_no() + vipshop.getVipshop_seq() + vipshop.getGetMaxCount();
			
		}
		String sign = VipShopMD5Util.MD5(MD5Str);
		String endpointUrl = vipshop.getGetCwb_URL();
		String response_XML = null;

		this.logger.info("获取vipshop订单XML={}", requestXML);

		try {
			if(feedbackOrderResult){
				response_XML = this.soapHandler.HTTPInvokeWs(endpointUrl, VipShopConfig.nameSpace, VipShopConfig.requestMethodName, requestXML, sign, VipShopConfig.CODE_S131);
			} else {
				response_XML = this.soapHandler.HTTPInvokeWs(endpointUrl, VipShopConfig.nameSpace, VipShopConfig.requestMethodName, requestXML, sign, VipShopConfig.CODE_S101);
			}
		} catch (Exception e) {
			this.logger.error("处理唯品会订单请求异常！返回信息：" + response_XML + ",异常原因：" + e.getMessage(), e);
			return null;
		}

		String orderXML = this.readXMLHandler.subStringSOAP(ReaderXMLHandler.parseBack(response_XML));
		this.logger.info("当前下载唯品会XML={}", orderXML);

		Map<String, Object> parseMap = null;
		try {
			parseMap = this.readXMLHandler.parserXmlToJSONObjectByArray(response_XML);
			// logger.info("解析后的XML-Map："+parseMap);
		} catch (Exception e) {
			this.logger.error("解析vipshop返回订单信息异常!,异常原因：" + e.getMessage(), e);
			return null;
		}
		return parseMap;
	}

	private String StringXMLRequest(VipShop vipshop, String request_time, boolean feedbackOrderResult) {

		String business_type = "";
		if (vipshop.getIsShangmentuiFlag() == 1) { // 只上门退
			business_type = "<business_type>1</business_type>";
		}
		if (vipshop.getIsShangmentuiFlag() == 2) { // 全部
			business_type = "<business_type>0,1</business_type>";
		}

		StringBuffer sub = new StringBuffer();
		sub.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		sub.append("<request>");
		sub.append("<head>");
		sub.append("<version>" + VipShopConfig.version + "</version>");
		sub.append("<request_time>" + request_time + "</request_time>");
		sub.append("<cust_code>" + vipshop.getShipper_no() + "</cust_code>");
		if(!feedbackOrderResult){
			sub.append("<seq>" + vipshop.getVipshop_seq() + "</seq>");
		}
		sub.append("<count>" + vipshop.getGetMaxCount() + "</count>");
		sub.append(business_type);
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

	/**
	 * 返回的xml信息解析拼接。 20120514
	 *
	 * @param orderlist
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, String>> parseXmlDetailInfo(Map paseXmlMap, VipShop vipshop, Map<String, Boolean> resultMap) {
		List<Map<String, Object>> orderlist = (List<Map<String, Object>>) paseXmlMap.get("orderlist");
		List<Map<String, String>> paraList = new ArrayList<Map<String, String>>();
		Customer customer=customerDAO.getCustomerById(Long.valueOf(vipshop.getCustomerids()));
		String seq_arrs = "";
		if ((orderlist != null) && (orderlist.size() > 0)) {
			Set<String> lantuiNeWSet = new HashSet<String>();
			for (Map<String, Object> datamap : orderlist) {
				seq_arrs = this.saveMapDataAndGetMaxSEQ(vipshop, paraList, seq_arrs, datamap,customer.getMpsswitch(), resultMap, lantuiNeWSet);
			}
		}
		long maxSEQ = this.getMaxSEQ(seq_arrs.split(","));
		if (maxSEQ != 0) {
			vipshop.setVipshop_seq(maxSEQ); // 赋值最大的seq
		}
		return paraList;
	}

	private String saveMapDataAndGetMaxSEQ(VipShop vipshop, List<Map<String, String>> paraList, String seq_arrs, Map<String, Object> datamap,int mpsswitch, Map<String, Boolean> resultMap, Set<String> lantuiNeWSet) {	
		String order_sn = null;
		try {
			Map<String, String> dataMap = new HashMap<String, String>();
			String id = VipShopGetCwbDataService.convertEmptyString("id", datamap);
			String seq = VipShopGetCwbDataService.convertEmptyString("seq", datamap);
			order_sn = VipShopGetCwbDataService.convertEmptyString("order_sn", datamap);
			// String box_id = convertEmptyString("box_id", datamap);
			String buyer_name = VipShopGetCwbDataService.convertEmptyString("buyer_name", datamap);
			String buyer_address = VipShopGetCwbDataService.convertEmptyString("buyer_address", datamap);
			String tel = VipShopGetCwbDataService.convertEmptyString("tel", datamap);
			String mobile = VipShopGetCwbDataService.convertEmptyString("mobile", datamap);
			String post_code = VipShopGetCwbDataService.convertEmptyString("post_code", datamap);
			String transport_day = VipShopGetCwbDataService.convertEmptyString("transport_day", datamap);
			String money = VipShopGetCwbDataService.convertEmptyString("money", datamap);
			String order_batch_no = VipShopGetCwbDataService.convertEmptyString("order_batch_no", datamap);
			String add_time = VipShopGetCwbDataService.convertEmptyString("add_time", datamap);
			String customer_name = VipShopGetCwbDataService.convertEmptyString("customer_name", datamap); // 客户
			String service_type = VipShopGetCwbDataService.convertEmptyString("service_type", datamap); // 服务类型：服务类型：1.
			
			String original_weight = "".equals(VipShopGetCwbDataService.convertEmptyString("original_weight", datamap)) ? "0" : VipShopGetCwbDataService.convertEmptyString("original_weight", datamap); // 重量
			String ext_pay_type = "".equals(VipShopGetCwbDataService.convertEmptyString("ext_pay_type", datamap)) ? "0" : VipShopGetCwbDataService.convertEmptyString("ext_pay_type", datamap); // 支付方式
			int paywayid = ext_pay_type.equals("1") ? PaytypeEnum.Pos.getValue() : PaytypeEnum.Xianjin.getValue();
			String attemper_no = VipShopGetCwbDataService.convertEmptyString("attemper_no", datamap); // 托运单号，根据此字段生成批次.
			String created_dtm_loc = VipShopGetCwbDataService.convertEmptyString("created_dtm_loc", datamap); // 批次时间，绑定托运单号
			String rec_create_time = VipShopGetCwbDataService.convertEmptyString("rec_create_time", datamap); // 生成时间
			String order_delivery_batch = choseOrderDeliveryBatch(datamap);
			String freight = VipShopGetCwbDataService.convertEmptyString("freight", datamap); // 上门退运费
			String business_type = VipShopGetCwbDataService.convertEmptyString("business_type", datamap); // 0：唯品会出仓派送件(默认)，1：客退上门揽收件
			String cwbordertype = business_type.equals("1") ? String.valueOf(CwbOrderTypeIdEnum.Shangmentui.getValue()) : String.valueOf(CwbOrderTypeIdEnum.Peisong.getValue());
			String warehouse_addr = VipShopGetCwbDataService.convertEmptyString("warehouse_addr", datamap); // 仓库地址
			String cmd_type = VipShopGetCwbDataService.convertEmptyString("cmd_type", datamap); // 操作指令new
			String go_get_return_time = VipShopGetCwbDataService.convertEmptyString("go_get_return_time", datamap); //预约上门揽退时间
			String is_gatherpack = VipShopGetCwbDataService.convertEmptyString("is_gatherpack", datamap); //1：表示此订单需要承运商站点集包 0：表示唯品会仓库整单出仓
			String is_gathercomp = VipShopGetCwbDataService.convertEmptyString("is_gathercomp", datamap); //最后一箱:1最后一箱 ，0默认 
			String pack_nos = VipShopGetCwbDataService.convertEmptyString("pack_nos", datamap); // 箱号会随着集单次数追加
			String total_pack = VipShopGetCwbDataService.convertEmptyString("total_pack", datamap); // 新增箱数
			CwbOrderDTO cwbOrderDTO = dataImportDAO_B2c.getCwbByCwbB2ctemp(order_sn);
			
			String cargotype = choseCargotype(service_type);
			created_dtm_loc = choseCreateDtmLoc(created_dtm_loc);
			String transcwb=pack_nos!=null&&!pack_nos.isEmpty()?pack_nos:order_sn;
			//团购标志
			String vip_club = VipShopGetCwbDataService.convertEmptyString("vip_club", datamap);
			
			if(vipshop.getIsOpenLefengflag()==1){//开启乐蜂网
				if((customer_name==null||customer_name.isEmpty()||!customer_name.contains("乐蜂"))&&!cwbordertype.equals(String.valueOf(CwbOrderTypeIdEnum.Shangmentui.getValue()))){
					return getSeq(seq_arrs, seq);
				}
			}
			
			dataMap = addOrderDtoMap(vipshop, order_sn, dataMap, buyer_name,
					buyer_address, tel, mobile, post_code, transport_day,
					money, order_batch_no, add_time, customer_name, cargotype,
					original_weight, paywayid, attemper_no, created_dtm_loc,
					rec_create_time, order_delivery_batch, freight,
					cwbordertype, warehouse_addr, go_get_return_time,
					is_gatherpack, is_gathercomp, total_pack, transcwb,mpsswitch,vip_club);
			
			
			//集包相关代码处理
			String mspAllPackge = mpsallPackage(vipshop, order_sn, is_gatherpack, is_gathercomp,pack_nos, total_pack, cwbOrderDTO,mpsswitch,paraList,dataMap);
			if(mspAllPackge!=null){
				return getSeq(seq_arrs, seq);
			}
			
			if(dataMap==null){
				return getSeq(seq_arrs, seq);
			}
						
			if (cwbordertype.equals(String.valueOf(CwbOrderTypeIdEnum.Shangmentui.getValue()))) {
				if("new".equalsIgnoreCase(cmd_type)){
					lantuiNeWSet.add(order_sn);
				}
				// 如果是order_sn对应订单不存在，标识不处理取消
				// 因为取消跟修改会去重，所以当包括新增的时候，不需要处理取消。
				if(!lantuiNeWSet.contains(order_sn) && cwbOrderDTO == null && "cancel".equalsIgnoreCase(cmd_type) && !"".equals(seq)){
					resultMap.put(seq, false);
				}
				
				seq_arrs = interceptShangmentui(vipshop, paraList, seq_arrs,order_sn, dataMap, seq, cmd_type);
				//防止多次取消订单导致出现有效订单的情况 Added by leoliao at 2013-03-02
				if ("cancel".equalsIgnoreCase(cmd_type)) {
					return seq_arrs;
				}
			}			
			
			if (cwbOrderDTO!= null) {
				this.logger.info("获取唯品会订单有重复,已过滤...cwb={},更新SEQ={}", order_sn, seq);
				seq_arrs = getSeq(seq_arrs, seq);
				return seq_arrs;
			}

			if (cwbordertype.equals(String.valueOf(CwbOrderTypeIdEnum.Shangmentui.getValue()))) {
				if ("new".equalsIgnoreCase(cmd_type)) {
					this.insertOrderGoods(datamap, order_sn);// 插入商品列表,try防止异常
				}
			}

			this.logger.info("唯品会订单cwb={},seq={}", order_sn, seq);			
			
			if (dataMap.get("cwb").isEmpty()) { // 若订单号为空，则继续。
				seq_arrs = getSeq(seq_arrs, seq);
				return seq_arrs;
			}
			seq_arrs = getSeq(seq_arrs, seq);			
			
			paraList.add(dataMap);		

		} catch (Exception e) {
			this.logger.error("唯品会订单下载处理单条信息异常,cwb=" + order_sn, e);
		}
		return seq_arrs;
	}

	private String choseCargotype(String service_type) {
		// B2C，
		// 2.
		// 仓配服务，3.
		// 配送服务
		String cargotype = "";
		if ("1".equals(service_type)) {
			cargotype = "B2C";
		} else if ("2".equals(service_type)) {
			cargotype = "仓配服务";
		} else if ("3".equals(service_type)) {
			cargotype = "配送服务";
		}
		return cargotype;
	}

	private String choseCreateDtmLoc(String created_dtm_loc) {
		if ((created_dtm_loc == null) || created_dtm_loc.isEmpty()) {
			created_dtm_loc = DateTimeUtil.getNowDate() + " 00:00:00";
		}
		return created_dtm_loc;
	}

	private Map<String,String> addOrderDtoMap(VipShop vipshop, String order_sn,
			Map<String, String> dataMap, String buyer_name,
			String buyer_address, String tel, String mobile, String post_code,
			String transport_day, String money, String order_batch_no,
			String add_time, String customer_name, String cargotype,
			String original_weight, int paywayid, String attemper_no,
			String created_dtm_loc, String rec_create_time,
			String order_delivery_batch, String freight, String cwbordertype,
			String warehouse_addr, String go_get_return_time,
			String is_gatherpack, String is_gathercomp, String total_pack,
			String transcwb,int mpsswitch,String vip_club) {
		String sendcarnum=total_pack.isEmpty() ? "1" : total_pack;
		
		dataMap.put("cwb", order_sn);
		dataMap.put("transcwb", transcwb);
		
		dataMap.put("consigneename", buyer_name);
		dataMap.put("sendcarnum", sendcarnum);
		dataMap.put("consigneemobile", mobile);
		dataMap.put("consigneephone", tel);
		dataMap.put("consigneepostcode", post_code);
		dataMap.put("consigneeaddress", buyer_address);
		dataMap.put("receivablefee", money);
		
		dataMap.put("customercommand", "送货时间要求:" + transport_day + ",订单配送批次:" + order_delivery_batch + "," + choseFreightRemark(freight,cwbordertype) + ",预约揽收时间："+go_get_return_time);
		
		
		dataMap.put("sendcargoname", "[发出商品]");
		dataMap.put("customerid", choseCustomerId(vipshop, customer_name));
		dataMap.put("remark1", order_batch_no); // 交接单号
		dataMap.put("remark2", (vipshop.getIsCreateTimeToEmaildateFlag()==1?add_time:rec_create_time)); // 如果开启生成批次，则remark2是出仓时间，否则是订单生成时间

		dataMap.put("cargorealweight", original_weight); // 重量
		dataMap.put("paywayid", String.valueOf(paywayid)); // 支付方式
		dataMap.put("remark3", "托运单号:" + attemper_no); // 托运单号
		dataMap.put("remark4", created_dtm_loc); // 批次时间
        dataMap.put("rec_create_time" , rec_create_time); // 订单创建时间
		dataMap.put("cargotype", cargotype); // 服务类别
		dataMap.put("remark5", customer_name+"/"+warehouse_addr); // 仓库地址

		dataMap.put("cwbordertypeid", cwbordertype);
		dataMap.put("shouldfare", freight.isEmpty() ? "0" : freight);
		dataMap.put("cwbordertypeid", cwbordertype);
		
		dataMap.put("ismpsflag", choseIsmpsflag(is_gatherpack,is_gathercomp,sendcarnum,mpsswitch));
		dataMap.put("mpsallarrivedflag", choseMspallarrivedflag(is_gathercomp,is_gatherpack,sendcarnum,mpsswitch));
		//团购标志
		dataMap.put("vipclub",vip_club.equals("3")?"1":"0");
		return dataMap;
		
	}

	private String choseMspallarrivedflag(String is_gathercomp,String is_gatherpack,String sendcarnum,int mpsswitch) {
		
		if(mpsswitch!=MpsswitchTypeEnum.WeiKaiQiJiDan.getValue()){
			//集包并且是最后一箱并且箱号大于1
			if("1".equals(is_gatherpack)&&"1".equals(is_gathercomp)&&Long.valueOf(sendcarnum)>1){ 
				return String.valueOf(VipGathercompEnum.Last.getValue());
			}
		}
		
		return String.valueOf(VipGathercompEnum.Default.getValue());
		
		
	}

	private String choseOrderDeliveryBatch(Map<String, Object> datamap) {
		String order_delivery_batch = VipShopGetCwbDataService.convertEmptyString("order_delivery_batch", datamap); // 1（默认）-一配订单：2-二配订单
		if ("1".equals(order_delivery_batch)) {
			order_delivery_batch = "一配订单";
		} else if ("2".equals(order_delivery_batch)) {
			order_delivery_batch = "二配订单";
		} else {
			order_delivery_batch = "普通订单";
		}
		return order_delivery_batch;
	}

	private String choseIsmpsflag(String is_gatherpack,String is_gathercomp,String sendcarnum,int mpsswitch) {
		String ismpsflag=String.valueOf(IsmpsflagEnum.no.getValue()); //'是否一票多件(集包模式)：0默认；1是一票多件'; 
		
		//开启集单，并且运单为多个，则默认为一票多件
		if(mpsswitch!=MpsswitchTypeEnum.WeiKaiQiJiDan.getValue()){
			
			//拦截 开启集单模式，总件数只有一件的数据 is_gatherpack=1，is_gathercomp=1，total_pack=1  --->这就是个单包裹(罗冬确认)
			if("1".equals(is_gatherpack)&&"1".equals(is_gathercomp)&&"1".equals(sendcarnum)){
				return String.valueOf(IsmpsflagEnum.no.getValue());
			}
			if("1".equals(is_gatherpack)&&Long.valueOf(sendcarnum)>1){
				return String.valueOf(IsmpsflagEnum.yes.getValue());
			}
			if("1".equals(is_gatherpack)&&"0".equals(is_gathercomp)){
				return String.valueOf(IsmpsflagEnum.yes.getValue());
			}
			
		}
		
		
		return ismpsflag;
	}

	private String choseFreightRemark(String freight,String cwbordertype) {
		 String remarkFreight="";
		if (cwbordertype.equals(String.valueOf(CwbOrderTypeIdEnum.Shangmentui.getValue()))) {
			double freight_d = Double.valueOf((freight != null) && !freight.isEmpty() ? freight : "0");
			if (freight_d > 0) {
				remarkFreight = "现付";
			} else {
				remarkFreight = "到付";
			}
		}
		return remarkFreight;
	}

	private String choseCustomerId(VipShop vipshop, String customer_name) {
		String customerid=vipshop.getCustomerids();  //默认选择唯品会customerid
		
		if((customer_name!=null&&customer_name.contains("乐蜂")))
		{
			customerid=vipshop.getLefengCustomerid()==null||vipshop.getLefengCustomerid().isEmpty()?vipshop.getCustomerids():vipshop.getLefengCustomerid();
		}
		return customerid;
	}

	private String  mpsallPackage(VipShop vipshop, String order_sn,
			String is_gatherpack, String is_gathercomp, String pack_nos,
			String total_pack, CwbOrderDTO cwbOrderDTO,int mpsswitch,List<Map<String, String>> paraList,Map<String, String> paraMap) {
		
		if(mpsswitch==MpsswitchTypeEnum.WeiKaiQiJiDan.getValue()){
			return null;
		}
		if(!"1".equals(is_gatherpack)){ //是否集包模式
			return null;
		}
		/**Commented by leoliao at 2016-03-01
		//拦截 开启集单模式，总件数只有一件的数据 is_gatherpack=1，is_gathercomp=1，total_pack=1  --->这就是个单包裹(罗冬确认)
		if("1".equals(is_gatherpack)&&"1".equals(is_gathercomp)&&"1".equals(total_pack)){
			return null;
		}
		*/
		
		filterMpsPackageOrderDto(paraList, paraMap);
		
		//订单不存在，则不需要处理
		if(cwbOrderDTO==null){
			return null;
		}
		
		/**更新发货时间 leoliao at 2016-03-01
		 * 产品层面要改成的是这样的：
		 * 如果TMS推过来的数据有最后一件标志了就把发货时间写入订单表里面&运单表，
		 * 如果TMS推过来的数据没有最后一件标志那就把发货时间写到运单表里面
		 */
		String emaildate = ""; //paraMap.get("remark2"); //发货时间
		if(paraMap != null){
			emaildate = paraMap.get("remark2");
		}else{
			for(Map<String, String> mapPara : paraList){
				if(!order_sn.equals(mapPara.get("cwb"))){
					continue;
				}
				
				emaildate = mapPara.get("remark2");
				break;
			}
		}
		
		if("1".equals(is_gatherpack) && "1".equals(is_gathercomp)){
			//把发货时间写入订单表
			cwbDAO.updateEmaildate(order_sn, emaildate);
			
			//把发货时间写入运单表
			String[] arrTranscwb = pack_nos.split(",");
			if(arrTranscwb != null && arrTranscwb.length > 0){
				dataImportService.updateEmaildate(Arrays.asList(arrTranscwb), emaildate);
			}
		}else if("1".equals(is_gatherpack) && "0".equals(is_gathercomp)){
			//把发货时间写入运单表
			String[] arrTranscwb = pack_nos.split(",");
			if(arrTranscwb != null && arrTranscwb.length > 0){
				dataImportService.updateEmaildate(Arrays.asList(arrTranscwb), emaildate);
			}
		}
		//更新发货时间结束
		
		//一票多件，并且到齐了，排重returen
		if(cwbOrderDTO !=null && cwbOrderDTO.getMpsallarrivedflag() == MPSAllArrivedFlagEnum.YES.getValue()){
			return null;
		}
		
		/**Comment by leoliao at 2016-03-01
		     兼容以下情况，故不做拦截而是改为一票一件：
	            先产生is_gatherpack=1,is_gathercomp=0,total_pack=1的订单数据。
	           后面又产生一条is_gatherpack=1,is_gathercomp=1,total_pack=1的订单数据
	   **/
		if("1".equals(is_gatherpack)&&"1".equals(is_gathercomp)&&"1".equals(total_pack)){
			dataImportDAO_B2c.updateTmsPackageCondition(order_sn, pack_nos, Integer.valueOf(total_pack), MPSAllArrivedFlagEnum.NO.getValue(), MpsTypeEnum.PuTong.getValue());
			if(cwbOrderDTO.getGetDataFlag() != 0){
				cwbDAO.updateTmsPackageCondition(order_sn, pack_nos, Integer.valueOf(total_pack), MPSAllArrivedFlagEnum.NO.getValue(),MpsTypeEnum.PuTong.getValue());
			}
			//是否需要删除express_ops_transcwb、express_ops_transcwb_detail表的数据?
			return null;
		}
		
		int mpsallarrivedflag=0;
		if(Integer.valueOf(is_gathercomp)==VipGathercompEnum.Last.getValue()){ //到齐
			mpsallarrivedflag = MPSAllArrivedFlagEnum.YES.getValue();
		}
		
		//Added by leoliao at 2016-03-21 使用锁的方式解决集包一票多件同时修改临时表订单的问题
		long b2cTempOpscwbid = cwbOrderDTO.getOpscwbid();
		dataImportDAO_B2c.updateTmsPackageCondition(b2cTempOpscwbid, pack_nos, Integer.valueOf(total_pack), mpsallarrivedflag, IsmpsflagEnum.yes.getValue());
		dataImportDAO_B2c.update_CwbDetailTempByCwb(0, b2cTempOpscwbid); //更新临时表的getDataFlag为0以重新转业务	
		//Added end
		
		/**Commented by leoliao at 2016-03-21 这部分的逻辑已改为在临时表转正式表定时器里	
		//需要集包 ？风险，线程不安全?
		if(cwbOrderDTO.getGetDataFlag()==0){
			dataImportDAO_B2c.updateTmsPackageCondition(order_sn, pack_nos, Integer.valueOf(total_pack), mpsallarrivedflag,MpsTypeEnum.YiPiaoDuoJian.getValue());
			//dataImportService.insertTransCwbDetail(cwbOrderDTO);
			
		}else{			
			CwbOrder co =cwbDAO.getCwbByCwb(order_sn);
			if(co==null){
				return null;
			}
			dataImportDAO_B2c.updateTmsPackageCondition(order_sn, pack_nos, Integer.valueOf(total_pack), mpsallarrivedflag,MpsTypeEnum.YiPiaoDuoJian.getValue());
			cwbDAO.updateTmsPackageCondition(order_sn, pack_nos, Integer.valueOf(total_pack), mpsallarrivedflag,MpsTypeEnum.YiPiaoDuoJian.getValue());
			for(String pack_no:pack_nos.split(",")){
				String selectCwb=transCwbDao.getCwbByTransCwb(pack_no);
				if(selectCwb==null){
					transCwbDao.saveTranscwb(pack_no, order_sn);
					cwbOrderDTO.setTranscwb(pack_no);
					dataImportService.insertTransCwbDetail(cwbOrderDTO,co.getEmaildate());
				}
			}
		}
		**/
		
		return "SUCCESS";
	}
	
	
	/**
	 * 在集合中去掉cwb相同，保留 运单号最多的一条。
	 * true : 按照currentMap存储
	 * false: 
	 */
	private void filterMpsPackageOrderDto(List<Map<String, String>> paraList,Map<String,String> currentMap){
		if(paraList==null||paraList.size()==0){
			return ;
		}
		for(Map<String,String> oldMap:paraList){
			if(!oldMap.get("cwb").equals(currentMap.get("cwb"))){
				continue;
			}
			String oldTranscwb = oldMap.get("transcwb");
			String currentTranscwb = currentMap.get("transcwb");
			//后者大于前者，移除前者
			if(oldTranscwb.split(",").length<currentTranscwb.split(",").length){
				paraList.remove(oldMap);
				return;
			}
			//后者小于前者，移除后者
			if(oldTranscwb.split(",").length>currentTranscwb.split(",").length){
				currentMap=null;
				return;
			}
			//后者==前者，移除不是最后一箱的
			if(oldTranscwb.split(",").length==currentTranscwb.split(",").length){
				if(Integer.valueOf(currentMap.get("mpsallarrivedflag"))==VipGathercompEnum.Default.getValue()
						&&Integer.valueOf(oldMap.get("mpsallarrivedflag"))==VipGathercompEnum.Default.getValue()){
					paraList.remove(oldMap);
					return; //Added by leoliao at 2016-03-01
				}
				if(Integer.valueOf(currentMap.get("mpsallarrivedflag"))==VipGathercompEnum.Last.getValue()){
					paraList.remove(oldMap);
					return;
				}
			}
			
		}
	}

	private String interceptShangmentui(VipShop vipshop,
			List<Map<String, String>> paraList, String seq_arrs,
			String order_sn, Map<String, String> dataMap, String seq,
			String cmd_type) {
		if ("edit".equalsIgnoreCase(cmd_type)) {
			this.dataImportDAO_B2c.updateBycwb(dataMap);
			this.cwbDAO.updateBycwb(dataMap);
			
			//Added by leoliao at 2016-03-21 去掉重复
			filterRepeatCwbs(paraList, order_sn);
			
			return getSeq(seq_arrs, seq);
		}
		// 订单取消
		if ("cancel".equalsIgnoreCase(cmd_type)) {			
			if(vipshop.getCancelOrIntercept()==0){ //取消
				this.dataImportDAO_B2c.dataLoseB2ctempByCwb(order_sn);
				this.cwbDAO.dataLoseByCwb(order_sn);
				orderGoodsDAO.loseOrderGoods(order_sn);
				cwbOrderService.datalose_vipshop(order_sn);
			}else{ //拦截
				//cwbOrderService.auditToTuihuo(userDAO.getAllUserByid(1), order_sn, order_sn, FlowOrderTypeEnum.DingDanLanJie.getValue(),1);
				cwbOrderService.tuihuoHandleVipshop(userDAO.getAllUserByid(1), order_sn, order_sn,0);
			}
			
			filterRepeatCwbs(paraList, order_sn);
		
			return getSeq(seq_arrs, seq);
		}
		
		return seq_arrs;
	}

	private String getSeq(String seq_arrs, String seq) {
		seq_arrs += seq + ",";
		return seq_arrs;
	}

	private void filterRepeatCwbs(List<Map<String, String>> paraList,
			String order_sn) {
		try {
			if(paraList!=null&&paraList.size()>0){
				for(int i=0;i<paraList.size();i++){
					Map<String,String> data =paraList.get(i);
					if(data.get("cwb").toString().equals(order_sn)){
						paraList.remove(data);
					}
				}
			}
		} catch (Exception e) {
			logger.error("订单过滤异常order_sn"+order_sn,e);
		}
	}
	
	
	
//	private void filterMpsPackCwbs(List<Map<String, String>> paraList,
//			String order_sn,String transcwb,Map<String, String> paraMap) {
//		try {
//			if(paraList!=null&&paraList.size()>0){
//				for(int i=0;i<paraList.size();i++){
//					Map<String,String> data =paraList.get(i);
//					if(data.get("cwb").toString().equals(order_sn)){
//						String oldtranscwb=data.get("transcwb");
//						String newtranscwb=transcwb;
//						//两个数组合并
//						String mergeTranscwb="";
//						Set<String> set = new HashSet<String>();
//						for(String old:oldtranscwb.split(",")){
//							set.add(old);
//						}
//						for(String new1:newtranscwb.split(",")){
//							set.add(new1);
//						}
//						
//						//遍历set，放入mergeTranscwb
//						
//						paraList.remove(i);
//						
//						paraMap.put("transcwb", mergeTranscwb);
//					}
//				}
//			}
//		} catch (Exception e) {
//			logger.error("订单过滤异常order_sn"+order_sn,e);
//		}
//	}

	private void insertOrderGoods(Map<String, Object> datamap, String order_sn) {
		try {
			List<Map<String, Object>> goodslist = (List<Map<String, Object>>) datamap.get("goods");
			if ((goodslist != null) && (goodslist.size() > 0)) {
				for (Map<String, Object> good : goodslist) {
					OrderGoods ordergoods = new OrderGoods();
					ordergoods.setCwb(order_sn);
					ordergoods.setCretime(DateTimeUtil.getNowTime());
					ordergoods.setGoods_brand(good.get("goods_brand").toString());
					ordergoods.setGoods_code(good.get("goods_code").toString());
					ordergoods.setGoods_name(good.get("goods_name").toString());
					ordergoods.setGoods_num(good.get("goods_num").toString());
					ordergoods.setGoods_pic_url(good.get("goods_pic_url").toString());
					ordergoods.setGoods_spec(good.get("goods_spec").toString());
					ordergoods.setReturn_reason(good.get("return_reason").toString());
					ordergoods.setCretime(DateTimeUtil.getNowTime());
					this.orderGoodsDAO.CreateOrderGoods(ordergoods);

				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			this.logger.error("获取商品列表异常,单号=" + order_sn, e);
		}
	}

	private static String convertEmptyString(String str, Map m) {
		String returnStr = m.get(str) == null ? "" : m.get(str).toString();
		return returnStr;
	}

	// 得到最大的值。
	public static long  getMaxSEQ(String[] seq_arrs) {
		
		
		
		long max = 0;
		if ((seq_arrs != null) && (seq_arrs.length > 0)) {
			for (int i = 0; i < seq_arrs.length; i++) {

				if (max < Long.valueOf((seq_arrs[i] == null) || "".equals(seq_arrs[i]) ? "0" : seq_arrs[i])) {
					max = Long.valueOf(seq_arrs[i]);
				}
			}
		}
		return max;
	}
	public static void main(String[] args) {
		String[] seq_arrs={"110000011787834","110000011787835","110000011787838","110000011787840"};
		
		System.out.println(getMaxSEQ(seq_arrs));
		
		List<Map<String,String>> list=new ArrayList<Map<String,String>>();
		Map<String,String> map1=new HashMap<String, String>();
		map1.put("cwb", "111");
		map1.put("transcwb", "aaa");
		
		Map<String,String> map2=new HashMap<String, String>();
		map2.put("cwb", "222");
		map2.put("transcwb", "mmmmm");
		
		Map<String,String> map3=new HashMap<String, String>();
		map3.put("cwb", "333");
		map3.put("transcwb", "aaa,bbb");
		
		list.add(map1);
		list.add(map2);
		list.add(map3);

		for(Map<String,String> mapA:list){
			if(mapA.get("cwb").equals("222")){
				list.remove(mapA);
			}
		}
		
		for(Map<String,String> mapR:list){
			System.out.println("cwb="+mapR.get("cwb")+",transcwb="+mapR.get("transcwb"));
		}
		
	}
	
	/**
	 * 把处理好的订单结果反馈给取订单方
	 * @param vipshop
	 */
	private void feedbackOrderResult(VipShop vipshop, Map<String, Boolean> result){
		String request_time = DateTimeUtil.getNowTime();
		String requestXML = this.StringXMLFeedback(vipshop, request_time, result);
		String MD5Str = vipshop.getPrivate_key() + VipShopConfig.version + request_time + vipshop.getShipper_no();
		String sign = VipShopMD5Util.MD5(MD5Str);
		String endpointUrl = vipshop.getGetCwb_URL();
		String response_XML = null;

		this.logger.info("反馈vipshop订单XML={}", requestXML);

		try {
			response_XML = this.soapHandler.HTTPInvokeWs(endpointUrl, VipShopConfig.nameSpace, VipShopConfig.requestMethodName, requestXML, sign, VipShopConfig.CODE_S141);
		} catch (Exception e) {
			this.logger.error("反馈唯品会订单请求异常！返回信息：" + response_XML + ",异常原因：" + e.getMessage(), e);
		}
	}
	
	/**
	 * 反馈订单结果报文
	 * @param vipshop
	 * @return
	 */
	private String StringXMLFeedback(VipShop vipshop, String request_time, Map<String, Boolean> result){
		StringBuilder sub = new StringBuilder();
		sub.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		sub.append("<request>");
		sub.append("<head>");
		sub.append("<version>" + VipShopConfig.version + "</version>");
		sub.append("<request_time>" + request_time + "</request_time>");
		sub.append("<cust_code>" + vipshop.getShipper_no() + "</cust_code>");
		sub.append("</head>");
		sub.append("<orders>");
		Set<Entry<String, Boolean>> set = result.entrySet();
		for(Entry<String, Boolean> entry : set){
			sub.append("<order>");
			sub.append("<id>");
			sub.append(entry.getKey());
			sub.append("</id>");			
			if(entry.getValue()){
				sub.append("<biz_response_code>");
				sub.append("B00");
				sub.append("</biz_response_code>");
				sub.append("<biz_response_msg>");
				sub.append("SUCCESS");
				sub.append("</biz_response_msg>");
			} else {
				sub.append("<biz_response_code>");
				sub.append("S99");
				sub.append("</biz_response_code>");
				sub.append("<biz_response_msg>");
				sub.append("FAIL");
				sub.append("</biz_response_msg>");
			}
			sub.append("</order>");
		}
		sub.append("</orders>");
		sub.append("</request>");
		return sub.toString();
		
	}
	
	/**
	 * 把处理结果标识成默认成功
	 * @param paseXmlMap
	 * @return
	 */
	private Map<String, Boolean> parseResultMap(Map paseXmlMap) {
		Map<String, Boolean> result = new HashMap<String, Boolean>();
		List<Map<String, Object>> orderlist = (List<Map<String, Object>>) paseXmlMap.get("orderlist");
		if (CollectionUtils.isEmpty(orderlist)) {
			return result;
		}
		String seq = null;
		for (Map<String, Object> datamap : orderlist) {
			seq = VipShopGetCwbDataService.convertEmptyString("seq", datamap);
			// 不添加为空的seq
			if("".equals(seq)){
				continue;
			}
			result.put(seq, true);
		}
		return result;
	}
	
	/**
	 * 标志结果
	 */
	private void markResultMap(Map<String, String> dataMap, Map<String, Boolean> resultMap, boolean result) {
		String seq = VipShopGetCwbDataService.convertEmptyString("seq", dataMap);
		if(!"".equals(seq)){
			resultMap.put(seq, result);
		}
	}

}

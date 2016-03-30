package cn.explink.b2c.telecomsc;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.explink.b2c.tools.B2cEnum;
import cn.explink.b2c.tools.DataImportService_B2c;
import cn.explink.b2c.tools.JiontDAO;
import cn.explink.b2c.tools.JointEntity;
import cn.explink.dao.CustomerDAO;
import cn.explink.enumutil.CwbOrderTypeIdEnum;
import cn.explink.enumutil.PaytypeEnum;
import cn.explink.pos.tools.JacksonMapper;
import cn.explink.service.CustomerService;
import cn.explink.util.EncryptData;
import cn.explink.util.MD5.MD5Util;

@Service
public class TelecomshopService {
	private static Logger logger = LoggerFactory.getLogger(TelecomshopService.class);

	@Autowired
	JiontDAO jiontDAO;
	@Autowired
	CustomerDAO customerDAO;
	@Autowired
	DataImportService_B2c dataImportInterface;
	@Autowired
	CustomerService customerService;

	protected static ObjectMapper jacksonmapper = JacksonMapper.getInstance();

	public String getObjectMethod(int key) {
		JointEntity obj = jiontDAO.getJointEntity(key);
		return obj == null ? null : obj.getJoint_property();
	}

	public Telecomshop getTelecomShop(int key) {
		if (getObjectMethod(key) == null) {
			return null;
		}
		JSONObject jsonObj = JSONObject.fromObject(getObjectMethod(key));
		Telecomshop dangdang = (Telecomshop) JSONObject.toBean(jsonObj, Telecomshop.class);
		return dangdang;
	}

	@Transactional
	public void edit(HttpServletRequest request, int joint_num) {
		Telecomshop lt = new Telecomshop();
		String customerid = request.getParameter("customerid");
		lt.setCustomerid(customerid);
		lt.setPrivate_key(request.getParameter("private_key"));
		lt.setReceiver_url(request.getParameter("receiver_url"));
		lt.setSender_url(request.getParameter("sender_url"));
		lt.setMaxCount(Integer.valueOf(request.getParameter("maxCount")));
		lt.setWarehouseid(Long.valueOf(request.getParameter("warehouseid")));

		JSONObject jsonObj = JSONObject.fromObject(lt);

		JointEntity jointEntity = jiontDAO.getJointEntity(joint_num);
		String oldCustomerids = "";
		if (jointEntity == null) {
			jointEntity = new JointEntity();
			jointEntity.setJoint_num(joint_num);
			jointEntity.setJoint_property(jsonObj.toString());
			jiontDAO.Create(jointEntity);
		} else {
			try {
				oldCustomerids = getTelecomShop(joint_num).getCustomerid();

			} catch (Exception e) {
			}
			jointEntity.setJoint_num(joint_num);
			jointEntity.setJoint_property(jsonObj.toString());
			jiontDAO.Update(jointEntity);
		}
		// 保存 枚举到供货商表中
		customerDAO.updateB2cEnumByJoint_num(customerid, oldCustomerids, joint_num);
		this.customerService.initCustomerList();
	}

	public void update(int joint_num, int state) {
		jiontDAO.UpdateState(joint_num, state);
	}

	public TelecomParms loadingTelecomParms(HttpServletRequest request) {
		TelecomParms telecomParms = new TelecomParms();
		String DISTRIBUTION_ID = request.getParameter("DISTRIBUTION_ID");
		String ORDER_ID = request.getParameter("ORDER_ID");
		String RECEIVER = request.getParameter("RECEIVER");
		String LINK_PHONE = request.getParameter("LINK_PHONE");
		String PHONE = request.getParameter("PHONE");
		String ADDRESS = request.getParameter("ADDRESS");
		String PRODUCTINFO = request.getParameter("PRODUCTINFO");
		String SEND_CONTEN = request.getParameter("SEND_CONTEN");
		String SHOULD_COLLECTION = request.getParameter("SHOULD_COLLECTION");
		String PAY_CHANNEL_ID = request.getParameter("PAY_CHANNEL_ID");
		String SIGN = request.getParameter("SIGN");
		String SIGNTYPE = request.getParameter("SIGNTYPE");

		String RECEIVER_IDCARD_TYPE = request.getParameter("RECEIVER_IDCARD_TYPE") == null ? "" : request.getParameter("RECEIVER_IDCARD_TYPE");
		String RECEIVER_IDCARD_CODE = request.getParameter("RECEIVER_IDCARD_CODE") == null ? "" : request.getParameter("RECEIVER_IDCARD_CODE");
		String NETWORK_NAME = request.getParameter("NETWORK_NAME") == null ? "" : request.getParameter("NETWORK_NAME");
		String NETWORK_PHONE = request.getParameter("NETWORK_PHONE") == null ? "" : request.getParameter("NETWORK_PHONE");
		String NETWORK_IDCARD_TYPE = request.getParameter("NETWORK_IDCARD_TYPE") == null ? "" : request.getParameter("NETWORK_IDCARD_TYPE");
		String NETWORK_IDCARD_CODE = request.getParameter("NETWORK_IDCARD_CODE") == null ? "" : request.getParameter("NETWORK_IDCARD_CODE");
		String olId = request.getParameter("olId") == null ? "" : request.getParameter("olId");

		// logger.info("电信请求原始===olId={}",olId);
		// logger.info("电信请求解密后===olId={}",EncryptData.decrypt(olId));

		telecomParms.setReceiver_idcard_type(EncryptData.decrypt(RECEIVER_IDCARD_TYPE));
		telecomParms.setReceiver_idcard_code(EncryptData.decrypt(RECEIVER_IDCARD_CODE));
		telecomParms.setNetwork_name(EncryptData.decrypt(NETWORK_NAME));
		telecomParms.setNetwork_phone(EncryptData.decrypt(NETWORK_PHONE));
		telecomParms.setNetwork_idcard_type(EncryptData.decrypt(NETWORK_IDCARD_TYPE));
		telecomParms.setNetwork_idcard_code(EncryptData.decrypt(NETWORK_IDCARD_CODE));
		telecomParms.setOlId(EncryptData.decrypt(olId));

		telecomParms.setDistribution_Id(EncryptData.decrypt(DISTRIBUTION_ID));
		telecomParms.setOrder_Id(EncryptData.decrypt(ORDER_ID));
		telecomParms.setReceiver(EncryptData.decrypt(RECEIVER));
		telecomParms.setLink_phone(EncryptData.decrypt(LINK_PHONE));
		telecomParms.setPhone(EncryptData.decrypt(PHONE));
		telecomParms.setAddress(EncryptData.decrypt(ADDRESS));
		telecomParms.setProductinfo(EncryptData.decrypt(PRODUCTINFO));
		telecomParms.setSend_conten(EncryptData.decrypt(SEND_CONTEN));
		telecomParms.setShould_collection(EncryptData.decrypt(SHOULD_COLLECTION));
		telecomParms.setPay_channel_id(EncryptData.decrypt(PAY_CHANNEL_ID));
		telecomParms.setSign(SIGN);
		telecomParms.setSigntype(SIGNTYPE);

		logger.info("0电信商城0请求参数:DISTRIBUTION_ID=" + telecomParms.getDistribution_Id() + ",ORDER_ID=" + telecomParms.getOrder_Id() + ",RECEIVER=" + telecomParms.getReceiver() + ",LINK_PHONE="
				+ telecomParms.getLink_phone() + ",PHONE=" + telecomParms.getPhone() + ",ADDRESS=" + telecomParms.getAddress() + ",PRODUCTINFO=" + telecomParms.getProductinfo() + ",SEND_CONTEN="
				+ telecomParms.getSend_conten() + ",SHOULD_COLLECTION=" + telecomParms.getShould_collection() + ",PAY_CHANNEL_ID=" + telecomParms.getPay_channel_id() + ",SIGN="
				+ telecomParms.getSign() + ",SIGNTYPE=" + telecomParms.getSigntype() + ",RECEIVER_IDCARD_TYPE=" + telecomParms.getReceiver_idcard_type() + ",RECEIVER_IDCARD_CODE="
				+ telecomParms.getReceiver_idcard_code() + ",NETWORK_NAME=" + telecomParms.getNetwork_name() + ",NETWORK_PHONE=" + telecomParms.getNetwork_phone() + ",NETWORK_IDCARD_TYPE="
				+ telecomParms.getNetwork_idcard_type() + ",NETWORK_IDCARD_CODE=" + telecomParms.getNetwork_idcard_code() + ",olId=" + telecomParms.getOlId());

		return telecomParms;
	}

	public TelecomParms loadingTelecomParms_test(HttpServletRequest request) {
		TelecomParms telecomParms = new TelecomParms();
		String DISTRIBUTION_ID = request.getParameter("DISTRIBUTION_ID");
		String ORDER_ID = request.getParameter("ORDER_ID");
		String RECEIVER = request.getParameter("RECEIVER");
		String LINK_PHONE = request.getParameter("LINK_PHONE");
		String PHONE = request.getParameter("PHONE");
		String ADDRESS = request.getParameter("ADDRESS");
		String PRODUCTINFO = request.getParameter("PRODUCTINFO");
		String SEND_CONTEN = request.getParameter("SEND_CONTEN");
		String SHOULD_COLLECTION = request.getParameter("SHOULD_COLLECTION");
		String PAY_CHANNEL_ID = request.getParameter("PAY_CHANNEL_ID");
		String SIGN = request.getParameter("SIGN");
		String SIGNTYPE = request.getParameter("SIGNTYPE");
		// 0电信商城0请求参数:DISTRIBUTION_ID=1001201311071403319202,ORDER_ID=100000000659382,RECEIVER=测试,LINK_PHONE=13223423423,
		// PHONE=13311530093,ADDRESS=北京北京市东城区北京市区,
		// PRODUCTINFO=iPhone5|32G黑色|预存话费送手机|老用户专享并赠送精美礼包-每月最低消费上网版-49元-Lumia800C合约计划（话费补贴）-乐享3G上网版-上网版T8-49-新号码:13311530093-普通卡;,
		// SEND_CONTEN=13311530093;,SHOULD_COLLECTION=603800,PAY_CHANNEL_ID=17,SIGN=ac20cb5d47672af7b0813f74b0b2d445,SIGNTYPE=MD5

		telecomParms.setDistribution_Id("1001201311071403319202");
		telecomParms.setOrder_Id("100000000659382");
		telecomParms.setReceiver("测试");
		telecomParms.setLink_phone("13223423423");
		telecomParms.setPhone("13311530093");
		telecomParms.setAddress("北京北京市东城区北京市区");
		telecomParms.setProductinfo("iPhone5|32G黑色|预存话费送手机|老用户专享并赠送精美礼包-每月最低消费上网版-49元-Lumia800C合约计划（话费补贴）-乐享3G上网版-上网版T8-49-新号码:13311530093-普通卡;");
		telecomParms.setSend_conten("13311530093;");
		telecomParms.setShould_collection("603800");
		telecomParms.setPay_channel_id("17");
		telecomParms.setSign("ac20cb5d47672af7b0813f74b0b2d445");
		telecomParms.setSigntype("MD5");

		logger.info("0电信商城0请求参数:DISTRIBUTION_ID=" + telecomParms.getDistribution_Id() + ",ORDER_ID=" + telecomParms.getOrder_Id() + ",RECEIVER=" + telecomParms.getReceiver() + ",LINK_PHONE="
				+ telecomParms.getLink_phone() + ",PHONE=" + telecomParms.getPhone() + ",ADDRESS=" + telecomParms.getAddress() + ",PRODUCTINFO=" + telecomParms.getProductinfo() + ",SEND_CONTEN="
				+ telecomParms.getSend_conten() + ",SHOULD_COLLECTION=" + telecomParms.getShould_collection() + ",PAY_CHANNEL_ID=" + telecomParms.getPay_channel_id() + ",SIGN="
				+ telecomParms.getSign() + ",SIGNTYPE=" + telecomParms.getSigntype());

		return telecomParms;
	}

	/**
	 * 接收电信商城请求 一单一单请求
	 * 
	 * @param telecom
	 * @param telecomParms
	 * @return
	 * @throws Exception
	 */
	public String receivedCwbOrderExport(Telecomshop telecom, TelecomParms telecomParms) throws Exception {

		telecomParmsValidate(telecomParms); // 参数验证

		ValidateSecuritySignMD5(telecom, telecomParms);

		List<Map<String, String>> parmslist = parseCwbArrByOrderDto(telecomParms);

		long warehouseid = telecom.getWarehouseid(); // 订单导入的库房Id
		dataImportInterface.Analizy_DataDealByB2c(Long.parseLong(telecom.getCustomerid()), B2cEnum.Telecomshop.getMethod(), parmslist, warehouseid, true);
		logger.info("{电信商城}下载订单信息调用数据导入接口-插入数据库成功!");

		return "success";

	}

	/**
	 * 返回一个转化为导入接口可识别的对象
	 */
	private List<Map<String, String>> parseCwbArrByOrderDto(TelecomParms parm) {
		List<Map<String, String>> cwbList = new ArrayList<Map<String, String>>();
		double receivablefee = Double.valueOf(parm.getShould_collection()) / 100;
		Map<String, String> cwbMap = new HashMap<String, String>();
		cwbMap.put("cwb", parm.getDistribution_Id());
		cwbMap.put("transcwb", parm.getOrder_Id());
		cwbMap.put("consigneename", parm.getReceiver());
		cwbMap.put("consigneemobile", parm.getPhone());
		cwbMap.put("consigneephone", parm.getLink_phone());
		cwbMap.put("consigneeaddress", parm.getAddress());
		cwbMap.put("sendcarname", parm.getProductinfo());
		cwbMap.put("receivablefee", String.valueOf(receivablefee));
		cwbMap.put("paywayid", getPayWayId(parm.getPay_channel_id()));
		cwbMap.put("cwbordertypeid", String.valueOf(CwbOrderTypeIdEnum.Peisong.getValue()));

		String remark2 = "收件人证件类型：" + parm.getReceiver_idcard_type() + "&收件人证件号码：" + parm.getReceiver_idcard_code();
		String remark3 = "开户人姓名：" + parm.getNetwork_name() + "&开户人电话：" + parm.getNetwork_phone();
		String remark4 = "开户人证件类型：" + parm.getNetwork_idcard_type() + "&开户人证件号码：" + parm.getNetwork_idcard_code();

		cwbMap.put("remark1", parm.getOlId()); // 购物车ID
		cwbMap.put("remark2", remark2);
		cwbMap.put("remark3", remark3);
		cwbMap.put("remark4", remark4);

		cwbList.add(cwbMap);

		return cwbList;
	}

	private String getPayWayId(String paywayid) {
		if (paywayid != null && (paywayid.equals("2") || paywayid.equals("6") || paywayid.equals("9") || paywayid.equals("11") || paywayid.equals("12") || paywayid.equals("22"))) {
			return PaytypeEnum.Pos.getValue() + "";
		} else {
			return PaytypeEnum.Xianjin.getValue() + "";
		}
	}

	private void ValidateSecuritySignMD5(Telecomshop telecom, TelecomParms telecomParms) throws UnsupportedEncodingException {

		String sign = telecomParms.getSign();

		LinkedHashMap<String, String> paramsMap = buildParmsMap(telecomParms);
		String linkStr = createLinkString(paramsMap);

		logger.info("验证签名字符串linkStr=" + linkStr);
		String local_sign = MD5Util.md5(linkStr);
		logger.info("电信sign={},本地sign={}", sign, local_sign);
		if (!local_sign.equalsIgnoreCase(sign)) {
			// throw new
			// RuntimeException("电信请求参数{sign}错误,local_sign="+local_sign);
		}
	}

	private LinkedHashMap<String, String> buildParmsMap(TelecomParms telecomParms) throws UnsupportedEncodingException {

		LinkedHashMap<String, String> parmsMap = new LinkedHashMap<String, String>();
		parmsMap.put("DISTRIBUTION_ID", URLEncoder.encode(telecomParms.getDistribution_Id(), "UTF-8"));
		parmsMap.put("ORDER_ID", URLEncoder.encode(telecomParms.getOrder_Id(), "UTF-8"));
		parmsMap.put("RECEIVER", URLEncoder.encode(telecomParms.getReceiver(), "UTF-8"));
		parmsMap.put("LINK_PHONE", URLEncoder.encode(telecomParms.getLink_phone(), "UTF-8"));
		parmsMap.put("PHONE", URLEncoder.encode(telecomParms.getPhone(), "UTF-8"));
		parmsMap.put("ADDRESS", URLEncoder.encode(telecomParms.getAddress(), "UTF-8"));
		parmsMap.put("PRODUCTINFO", URLEncoder.encode(telecomParms.getProductinfo(), "UTF-8"));
		parmsMap.put("SEND_CONTEN", URLEncoder.encode(telecomParms.getSend_conten(), "UTF-8"));
		parmsMap.put("SHOULD_COLLECTION", URLEncoder.encode(telecomParms.getShould_collection(), "UTF-8"));
		parmsMap.put("PAY_CHANNEL_ID", URLEncoder.encode(telecomParms.getPay_channel_id(), "UTF-8"));

		if (telecomParms.getReceiver_idcard_type() != null || !telecomParms.getReceiver_idcard_type().isEmpty()) {
			parmsMap.put("RECEIVER_IDCARD_TYPE", URLEncoder.encode(telecomParms.getReceiver_idcard_type(), "UTF-8"));
		}

		if (telecomParms.getReceiver_idcard_code() != null || !telecomParms.getReceiver_idcard_code().isEmpty()) {
			parmsMap.put("RECEIVER_IDCARD_CODE", URLEncoder.encode(telecomParms.getReceiver_idcard_code(), "UTF-8"));
		}

		if (telecomParms.getNetwork_name() != null || !telecomParms.getNetwork_name().isEmpty()) {
			parmsMap.put("NETWORK_NAME", URLEncoder.encode(telecomParms.getNetwork_name(), "UTF-8"));
		}

		if (telecomParms.getNetwork_phone() != null || !telecomParms.getNetwork_phone().isEmpty()) {
			parmsMap.put("NETWORK_PHONE", URLEncoder.encode(telecomParms.getNetwork_phone(), "UTF-8"));
		}
		if (telecomParms.getNetwork_idcard_type() != null || !telecomParms.getNetwork_idcard_type().isEmpty()) {
			parmsMap.put("NETWORK_IDCARD_TYPE", URLEncoder.encode(telecomParms.getNetwork_idcard_type(), "UTF-8"));
		}
		if (telecomParms.getNetwork_idcard_code() != null || !telecomParms.getNetwork_idcard_code().isEmpty()) {
			parmsMap.put("NETWORK_IDCARD_CODE", URLEncoder.encode(telecomParms.getNetwork_idcard_code(), "UTF-8"));
		}
		if (telecomParms.getOlId() != null || !telecomParms.getOlId().isEmpty()) {
			parmsMap.put("olId", URLEncoder.encode(telecomParms.getOlId(), "UTF-8"));
		}

		return parmsMap;

	}

	/**
	 * 把数组所有元素排序，并按照“参数=参数值”的模式用“&”字符拼接成字符串
	 * 
	 * @param params
	 *            需要排序并参与字符拼接的参数组
	 * @return 拼接后字符串
	 */
	public static String createLinkString(Map<String, String> params) {

		List<String> keys = new ArrayList<String>(params.keySet());
		// Collections.sort(keys);

		String prestr = "";

		for (int i = 0; i < keys.size(); i++) {
			String key = keys.get(i);
			String value = params.get(key);

			if (i == keys.size() - 1) {// 拼接时，不包括最后一个&字符
				prestr = prestr + key + "=" + value;
			} else {
				prestr = prestr + key + "=" + value + "&";
			}
		}

		return prestr;
	}

	private void telecomParmsValidate(TelecomParms telecomParms) {
		if (telecomParms.getDistribution_Id() == null || telecomParms.getDistribution_Id().isEmpty()) {
			throw new RuntimeException("电信请求参数{distribution_Id}为空");
		}
		if (telecomParms.getOrder_Id() == null || telecomParms.getOrder_Id().isEmpty()) {
			throw new RuntimeException("电信请求参数{order_Id}为空");
		}
		if (telecomParms.getReceiver() == null || telecomParms.getReceiver().isEmpty()) {
			throw new RuntimeException("电信请求参数{receiver}为空");
		}
		if (telecomParms.getAddress() == null || telecomParms.getAddress().isEmpty()) {
			throw new RuntimeException("电信请求参数{address}为空");
		}
		if (telecomParms.getSign() == null || telecomParms.getSign().isEmpty()) {
			throw new RuntimeException("电信请求参数{sign}为空");
		}
	}

	public static void main(String[] args) {
		String strs = "DISTRIBUTION_ID=1001201310151420362376&ORDER_ID=100000000494201&RECEIVER=%E6%B5%8B%E8%AF%95&RECEIVER_IDCARD_TYPE=2&RECEIVER_IDCARD_CODE=123456&LINK_PHONE=13223423423&PHONE=13311530093&ADDRESS=%E5%8C%97%E4%BA%AC%E5%8C%97%E4%BA%AC%E5%B8%82%E4%B8%9C%E5%9F%8E%E5%8C%BA%E5%8C%97%E4%BA%AC%E5%B8%82%E5%8C%BA&PRODUCTINFO=iPhone5%7C32G%E9%BB%91%E8%89%B2%7C%E9%A2%84%E5%AD%98%E8%AF%9D%E8%B4%B9%E9%80%81%E6%89%8B%E6%9C%BA%7C%E8%80%81%E7%94%A8%E6%88%B7%E4%B8%93%E4%BA%AB%E5%B9%B6%E8%B5%A0%E9%80%81%E7%B2%BE%E7%BE%8E%E7%A4%BC%E5%8C%85-%E6%AF%8F%E6%9C%88%E6%9C%80%E4%BD%8E%E6%B6%88%E8%B4%B9%E4%B8%8A%E7%BD%91%E7%89%88-49%E5%85%83-Lumia800C%E5%90%88%E7%BA%A6%E8%AE%A1%E5%88%92%EF%BC%88%E8%AF%9D%E8%B4%B9%E8%A1%A5%E8%B4%B4%EF%BC%89-%E4%B9%90%E4%BA%AB3G%E4%B8%8A%E7%BD%91%E7%89%88-%E4%B8%8A%E7%BD%91%E7%89%88T8-49-%E6%96%B0%E5%8F%B7%E7%A0%81%3A13311530093-%E6%99%AE%E9%80%9A%E5%8D%A1%3B&SEND_CONTEN=13311530093%3B&SHOULD_COLLECTION=603800&PAY_CHANNEL_ID=17&NETWORK_NAME=%E6%9D%A8%E5%B9%82&NETWORK_PHONE=13552274074&NETWORK_IDCARD_TYPE=1&NETWORK_IDCARD_CODE=110101191501016812&olId=123456789";
		logger.info(MD5Util.md5(strs));

	}

	// /**
	// * 获取参数串
	// * @param requestList
	// * @return
	// */
	// public static String getParams(List<RequestModel> requestList){
	// String params = "";
	// if (null!=requestList && requestList.size()>0){
	// int count = requestList.size();
	// for(int i=0;i<count;i++){
	// RequestModel temp = requestList.get(i);
	// String strURLEncoder=temp.getParamValue();
	// try {
	// if(strURLEncoder != null && strURLEncoder != ""){
	// strURLEncoder = URLEncoder.encode(temp.getParamValue(),"UTF-8");
	// }
	// } catch (UnsupportedEncodingException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	// if(strURLEncoder == null){
	// strURLEncoder = "";//将null参数统一处理成""
	// }
	// if (params.equals("")){
	// params = temp.getParamName() + "=" +strURLEncoder;
	// }else{
	// params = params+"&"+temp.getParamName() + "=" +strURLEncoder;
	//
	// }
	// }
	// }
	// return params;
	// }

}

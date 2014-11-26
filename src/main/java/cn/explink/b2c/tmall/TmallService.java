package cn.explink.b2c.tmall;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.type.TypeReference;
import org.jdom.JDOMException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.explink.b2c.dpfoss.waybill.QueryWayBillResponse;
import cn.explink.b2c.tools.B2cEnum;
import cn.explink.b2c.tools.DataImportDAO_B2c;
import cn.explink.b2c.tools.DataImportService_B2c;
import cn.explink.b2c.tools.JiontDAO;
import cn.explink.b2c.tools.JointEntity;
import cn.explink.dao.CustomWareHouseDAO;
import cn.explink.dao.CustomerDAO;
import cn.explink.dao.SystemInstallDAO;
import cn.explink.domain.CustomWareHouse;
import cn.explink.domain.SystemInstall;
import cn.explink.pos.tools.JacksonMapper;
import cn.explink.util.DateTimeUtil;

@Service
public class TmallService {

	@Autowired
	JiontDAO jiontDAO;
	@Autowired
	TmallDAO tmallDAO;
	@Autowired
	CustomerDAO customerDAO;
	@Autowired
	DataImportService_B2c dataImportInterface;
	@Autowired
	CustomWareHouseDAO customWarehouseDAO;
	@Autowired
	DataImportDAO_B2c dataImportDAO_B2c;
	@Autowired
	DataImportService_B2c dataImportService_B2c;
	@Autowired
	SystemInstallDAO systemInstallDAO;

	private Logger logger = LoggerFactory.getLogger(TmallService.class);

	public String getObjectMethod(int key) {
		JointEntity obj = jiontDAO.getJointEntity(key);
		return obj == null ? null : obj.getJoint_property();
	}

	public Tmall getTmall(int key) {
		if (getObjectMethod(key) == null) {
			return null;
		}
		JSONObject jsonObj = JSONObject.fromObject(getObjectMethod(key));
		Tmall tmall = (Tmall) JSONObject.toBean(jsonObj, Tmall.class);
		return tmall;
	}

	public void edit(HttpServletRequest request, int joint_num) {
		Tmall tmall = new Tmall();
		tmall.setPartner(request.getParameter("partner"));
		tmall.setTms_service_code(request.getParameter("tms_service_code"));
		tmall.setOut_biz_code(request.getParameter("out_biz_code"));
		tmall.setService_code(request.getParameter("service_code"));
		tmall.setPrivate_key(request.getParameter("private_key"));
		tmall.setPost_url(request.getParameter("post_url"));
		tmall.setGetInfo_url(request.getParameter("getInfo_url"));
		tmall.setCustomerids(request.getParameter("customerids"));
		tmall.setWarehouseid(Long.parseLong(request.getParameter("warehouseid")));
		String customerids = request.getParameter("customerids");
		tmall.setServiceTel(request.getParameter("serviceTel"));

		tmall.setData_format(request.getParameter("data_format"));
		tmall.setIsCallBackAccept(Integer.valueOf(request.getParameter("isCallBackAccept")));

		tmall.setAcceptflag(Integer.valueOf(request.getParameter("acceptflag")));
		tmall.setIsCallBackError(Integer.valueOf(request.getParameter("isCallBackError")));
		tmall.setExpt_url(request.getParameter("expt_url"));

		tmall.setB2c_enum(joint_num); // 存入枚举到实体对象
		String oldCustomerids = "";

		JSONObject jsonObj = JSONObject.fromObject(tmall);
		JointEntity jointEntity = jiontDAO.getJointEntity(joint_num);
		if (jointEntity == null) {
			jointEntity = new JointEntity();
			jointEntity.setJoint_num(joint_num);
			jointEntity.setJoint_property(jsonObj.toString());
			jiontDAO.Create(jointEntity);
		} else {
			try {
				oldCustomerids = getTmall(joint_num).getCustomerids();

			} catch (Exception e) {
			}
			jointEntity.setJoint_num(joint_num);
			jointEntity.setJoint_property(jsonObj.toString());
			jiontDAO.Update(jointEntity);
		}
		// 保存 枚举到供货商表中
		customerDAO.updateB2cEnumByJoint_num(customerids, oldCustomerids, joint_num);

	}

	public void update(int joint_num, int state) {
		jiontDAO.UpdateState(joint_num, state);
	}

	public int getStateForTmall(int key) {
		JointEntity obj = null;
		int state = 0;
		try {
			obj = jiontDAO.getJointEntity(key);
			state = obj.getState();
		} catch (Exception e) {
			// TODO: handle exception
		}
		return state;
	}

	/**
	 * 处理tmall请求 被动接收订单数据接口
	 * 
	 * @throws IOException
	 * @throws JDOMException
	 */
	public String requestMethod(Tmall tmall, String partner, String signtype, String sign, String notify_id, String notify_type, String notify_time, String content) throws Exception {
		String nowtime = DateTimeUtil.getNowTime();

		// 验证基础信息是否通过
		try {
			validateBaseInfo(partner, signtype, sign, notify_id, notify_type, content, tmall);
		} catch (Exception e) {
			logger.error("验证tmall基础信息失败:" + e.getMessage());
			// return response_XML("failed",e.getMessage());
		}

		Map<String, String> xmlMap = null;
		// 解析方式
		if (tmall.getData_format().equals("XML")) {
			xmlMap = TmallConfig.Analyz_XmlDocByRequestXML(content);
		} else {
			xmlMap = TmallConfig.Analyz_XmlDocByRequestJSON(content);
		}

		// 验证Xml解析后是否符合业务
		try {
			ValidateXMLRight(xmlMap, tmall);
		} catch (RuntimeException e) {
			logger.error("验证Xml解析后业务逻辑错误:" + e);
			return response_XML("failed", e.getMessage());
		}

		// 插入临时表的反馈结果
		return InsertTmallCwb_temp(nowtime, xmlMap, notify_id, tmall);

	}

	private void ValidateXMLRight(Map<String, String> xmlMap, Tmall tmall) {
		String wms_code = xmlMap.get("wms_code") != null && !"".equals(xmlMap.get("wms_code").toString()) ? xmlMap.get("wms_code").toString() : "";
		if (!customWarehouseDAO.isExistsWarehouFlag(wms_code, tmall.getCustomerids())) {
			// if(switchDAO.getSwitchBySwitchname(SwitchEnum.DaoRuShuJuChuangJianFaHuoCangKu.getText()).getState().equals(SwitchEnum.DaoRuShuJuChuangJianFaHuoCangKu.getInfo())){
			CustomWareHouse custwarehouse = new CustomWareHouse();
			custwarehouse.setCustomerid(Long.valueOf(tmall.getCustomerids()));
			custwarehouse.setCustomerwarehouse(xmlMap.get("wms_code").toString());
			custwarehouse.setWarehouse_no(xmlMap.get("wms_code").toString());
			custwarehouse.setWarehouseremark(xmlMap.get("wms_address").toString());
			custwarehouse.setIfeffectflag(1);
			customWarehouseDAO.creCustomer(custwarehouse);
		}

		// if
		// (customWarehouseDAO.isIntoStorageFlag(xmlMap.get("tms_order_code"),
		// tmall.getCustomerids())) {// 确认单子没有入库
		//
		// throw new RuntimeException("Tmall 该单子已入库不能合单！[" +
		// xmlMap.get("tms_order_code") + "]");
		// }
		else if (tmallDAO.isExistsOver24HCwbInfo(xmlMap.get("tms_order_code"), tmall.getCustomerids())) {
			throw new RuntimeException("Tmall 该单子已超过24小时不能合单！[" + xmlMap.get("tms_order_code") + "]");
		}
	}

	/**
	 * 插入临时表，然后启动个定时器 获取临时表信息
	 * 
	 * @param nowtime
	 * @param xmlMap
	 * @param notify_id
	 * @param tmall
	 */
	private String InsertTmallCwb_temp(String nowtime, Map<String, String> xmlMap, String notify_id, Tmall tmall) {
		String emaildate = DateTimeUtil.getNowDate() + " 00:00:00";
		List<Map<String, String>> xmlList = getDetailParamList(emaildate, tmall.getCustomerids(), nowtime, xmlMap, notify_id);
		long warehouseid = getTmall(tmall.getB2c_enum()).getWarehouseid(); // 订单入库库房

		try {
			dataImportInterface.Analizy_DataDealByB2c(Long.valueOf(tmall.getCustomerids()), B2cEnum.Tmall.getMethod(), xmlList, warehouseid, true);
			logger.info("处理Tmall导入后的订单信息成功");
		} catch (Exception e) {
			logger.error("tmall调用数据导入接口异常!,订单号:" + xmlMap.get("tms_order_code") + "Exception_Message:", e);
			return response_XML("failed", "插入数据库处理异常,可能存在notify_id重复!");
		}
		logger.info("tmall数据插入临时表成功,订单号：{},运单号：{}", xmlMap.get("tms_order_code"), xmlMap.get("order_code"));
		return response_XML("success", "");
	}

	/**
	 * 验证基础的配置是否正确
	 * 
	 * @param partner
	 * @param signtype
	 * @param sign
	 * @param notify_id
	 * @param notify_type
	 * @param content
	 * @return
	 */
	private void validateBaseInfo(String partner, String signtype, String sign, String notify_id, String notify_type, String content, Tmall tmall) {
		if (!TmallConfig.notify_type.equals(notify_type)) {
			throw new RuntimeException("Tmall消息类型不正确！[" + notify_type + "]");
		}

		if (!tmall.getPartner().equals(partner)) {
			throw new RuntimeException("Tmall-TP唯一指定TMS ID 不正确![" + partner + "]");
		}
		if (!TmallConfig.sign_type.equals(signtype)) {
			throw new RuntimeException("签名方式不正确![" + signtype + "]");
		}
		if (sign != null) {
			String sign_str = null;
			try {
				sign_str = TmallConfig.encryptSign_Method(content, tmall.getPrivate_key());
			} catch (Exception e) {
				throw new RuntimeException("签名异常," + e);
			}
			if (sign_str != null && !sign_str.equals(sign)) {
				// throw new RuntimeException("私钥：[" + tmall.getPrivate_key() +
				// "],本地生成签名：[" + sign_str+ "],tmall签名验证错误[" + sign + "]");
			}
		}
	}

	/**
	 * 反馈给tmall信息
	 * 
	 * @param log_result
	 * @param log_event
	 * @return
	 */
	public String response_XML(String log_result, String log_event) {
		String strs = "";
		if ("success".equals(log_result)) {
			strs = "<wlb><is_success>T</is_success></wlb>";
		} else {
			strs = "<wlb><is_success>F</is_success><error>" + log_event + "</error></wlb>";
		}
		logger.info(strs);
		return strs;

	}

	/**
	 * 解析xml转化为list 把map转换为list 参数
	 * 
	 * @param customer_id
	 * @param nowtime
	 * @param xmlMap
	 * @param notify_id
	 * @return
	 */
	public List<Map<String, String>> getDetailParamList(String emaildate, String customer_id, String nowtime, Map<String, String> xmlMap, String notify_id) {
		List<Map<String, String>> paralist = new ArrayList<Map<String, String>>();
		String shipcwb = xmlMap.get("order_code") == null ? "" : xmlMap.get("order_code").toString();
		String cwb = xmlMap.get("tms_order_code") == null ? "" : xmlMap.get("tms_order_code").toString();
		String order_flag = xmlMap.get("order_flag") == null ? "" : xmlMap.get("order_flag").toString();
		order_flag = order_flag.trim();

		String receivablefee = xmlMap.get("total_amount") == null ? "0" : xmlMap.get("total_amount").toString();

		// 客户要求
		String schedule_start = xmlMap.get("schedule_start") == null ? "" : xmlMap.get("schedule_start").toString();
		String schedule_end = xmlMap.get("schedule_end") == null ? "" : xmlMap.get("schedule_end").toString();
		String schedule_type = xmlMap.get("schedule_type") == null ? "" : xmlMap.get("schedule_type").toString();
		String customercommand = Analyz_CustomerCommand(schedule_type);

		// 获取天猫预约派送的描述
		customercommand = getTmallYuYueDescribe(cwb, schedule_start, schedule_end, schedule_type, customercommand);

		String consigneename = xmlMap.get("receiver_name") == null ? "" : xmlMap.get("receiver_name").toString();
		String consigneepostcode = xmlMap.get("receiver_zip") == null ? "" : xmlMap.get("receiver_zip").toString();
		String cwbprovince = xmlMap.get("receiver_province") == null ? "" : xmlMap.get("receiver_province").toString();
		String cwbcity = xmlMap.get("receiver_city") == null ? "" : xmlMap.get("receiver_city").toString();
		String cwbcounty = xmlMap.get("receiver_district") == null ? "" : xmlMap.get("receiver_district").toString();
		String consigneeaddress = xmlMap.get("receiver_address") == null ? "" : xmlMap.get("receiver_address").toString();
		String consigneemobile = xmlMap.get("receiver_mobile") == null ? "" : xmlMap.get("receiver_mobile").toString();
		String consigneephone = xmlMap.get("receiver_phone") == null ? "" : xmlMap.get("receiver_phone").toString();
		String cargorealweight = xmlMap.get("package_weight") == null ? "0" : xmlMap.get("package_weight").toString();
		String cargovolume = xmlMap.get("package_volume") == null ? "0" : xmlMap.get("package_volume").toString();

		String tms_service_code = xmlMap.get("tms_service_code") == null ? "0" : xmlMap.get("tms_service_code").toString(); // 20130522
																															// 新增服务编码标识
		// 仓库编码
		String wms_code = xmlMap.get("wms_code") == null ? "" : xmlMap.get("wms_code").toString();
		String warhouseid = dataImportService_B2c.getCustomerWarehouseNo(wms_code, customer_id);

		String wms_address = xmlMap.get("wms_address") == null ? "" : xmlMap.get("wms_address").toString();

		String user_id = xmlMap.get("user_id") == null ? "" : xmlMap.get("user_id").toString(); // 20130609
																								// 新增标识
																								// 淘宝商家标识
		String user_nick = xmlMap.get("user_nick") == null ? "" : xmlMap.get("user_nick").toString();

		String sendcargoname = "[发出商品]";
		String cwbordertypeid = "1";
		String cwbdelivertypeid = "1";

		// / 地址是省市区之和
		String consigneeaddress_all = cwbprovince + cwbcity + cwbcounty + consigneeaddress;

		// 重量
		double cargorealweight_a = (Double.parseDouble(cargorealweight)) / 1000;

		double receivablefee_a = (Double.parseDouble(receivablefee)) / 100;
		if (!order_flag.equals("1")) { // cod类型，有代收款
			if (order_flag.equals("8")) { // 上门换
				cwbordertypeid = "3";
			} else if (order_flag.equals("9")) { // 上门退
				cwbordertypeid = "2";
			}
			receivablefee_a = 0;
		}

		String cwbremark = Analyz_tmallOrderFlag(order_flag);
		double cargoamount = (Double.parseDouble(receivablefee)) / 100; // 货物金额

		// 插入订单入库库房
		String branch_id = "0";

		Map<String, String> dataMap = new HashMap<String, String>();
		dataMap.put("cwb", cwb);
		dataMap.put("shipcwb", shipcwb);
		dataMap.put("order_flag", order_flag);
		dataMap.put("emaildate", emaildate);
		dataMap.put("nowtime", nowtime);
		dataMap.put("consigneename", consigneename);
		dataMap.put("consigneepostcode", consigneepostcode);
		dataMap.put("consigneeaddress", consigneeaddress_all);
		dataMap.put("consignoraddress", wms_address); // tmall仓库地址，这个要存.
		dataMap.put("cwbprovince", cwbprovince);
		dataMap.put("cwbcity", cwbcity);

		dataMap.put("cwbcounty", cwbcounty);
		dataMap.put("consigneemobile", consigneemobile);
		dataMap.put("consigneephone", consigneephone);
		dataMap.put("cargorealweight", cargorealweight_a + "");
		dataMap.put("cargovolume", cargovolume); // 单位，立方厘米
		dataMap.put("customercommand", customercommand);
		dataMap.put("cargoamount", cargoamount + ""); // 货物金额
		dataMap.put("receivablefee", receivablefee_a + ""); // 代收款
		dataMap.put("sendcargoname", sendcargoname); // 发货商品
		dataMap.put("sendcarnum", "1"); // 发货商品
		dataMap.put("customerwarehouseid", warhouseid); // 发货仓库
		dataMap.put("cwbordertypeid", cwbordertypeid); // 订单类型
		dataMap.put("cwbdelivertypeid", cwbdelivertypeid); // 配送方式
		dataMap.put("startbranchid", branch_id); // 导入库房id
		dataMap.put("customerid", customer_id); // 配送方式
		dataMap.put("tmall_notify_id", notify_id); // tmall唯一标示，控制不重复。

		dataMap.put("tmall_service_code", tms_service_code); // tmall服务编码标识
		dataMap.put("multi_shipcwb", shipcwb);
		dataMap.put("transcwb", shipcwb);
		dataMap.put("cwbremark", cwbremark);
		dataMap.put("remark1", user_id + "," + user_nick); // 把商家标识暂时放在remark1中。
		paralist.add(dataMap);

		return paralist;
	}

	private String getTmallYuYueDescribe(String cwb, String schedule_start, String schedule_end, String schedule_type, String customercommand) {

		try {
			List<TmallTimeRole> rolelist = null;
			if ("104".equals(schedule_type)) {
				SystemInstall systimes = systemInstallDAO.getSystemInstall("tmall.yuyue.role");
				String jsonValue = systimes.getValue();
				rolelist = JacksonMapper.getInstance().readValue(jsonValue, new TypeReference<List<TmallTimeRole>>() {
				});
			}

			int startHours = 0;
			int endHours = 0;
			if (!"".equals(schedule_start)) {
				startHours = Integer.valueOf(schedule_start.substring(11, 13)); // 获取小时数
			}
			if (!"".equals(schedule_end)) {
				endHours = Integer.valueOf(schedule_end.substring(11, 13)); // 获取小时数
			}
			String mappingName = getMappingName(rolelist, startHours, endHours);
			if (!mappingName.isEmpty()) {
				customercommand += "," + mappingName + schedule_start + "~" + schedule_end;
			} else {
				customercommand += "," + schedule_start + "~" + schedule_end;
			}

		} catch (Exception e) {
			logger.error("tmall预约派送-时间处理异常" + cwb, e);
			return customercommand += "," + schedule_start + "~" + schedule_end;
		}
		return customercommand;
	}

	private String getMappingName(List<TmallTimeRole> rolelist, int startHours, int endHours) {

		if (rolelist != null && rolelist.size() > 0) {
			for (TmallTimeRole tmallrole : rolelist) {
				if (startHours >= tmallrole.getStime() && endHours <= tmallrole.getEtime()) {
					return tmallrole.getRole();
				}
			}
		}
		return "";
	}

	private String Analyz_CustomerCommand(String schedule_type) {
		String customercommand = "";
		if ("1".equals(schedule_type)) {
			customercommand = "工作日送";
		} else if ("2".equals(schedule_type)) {
			customercommand = "节假日送";
		} else if ("101".equals(schedule_type)) {
			customercommand = "当日达";
		} else if ("102".equals(schedule_type)) {
			customercommand = "次晨达";
		} else if ("103".equals(schedule_type)) {
			customercommand = "次日达";
		} else if ("104".equals(schedule_type)) {
			customercommand = "预约派送";
		} else if ("105".equals(schedule_type)) {
			customercommand = "三日达";
		} else if ("106".equals(schedule_type)) {
			customercommand = "迟到免单";
		} else if ("111".equals(schedule_type)) {
			customercommand = "活动标";
		}

		return customercommand;
	}

	private String Analyz_tmallOrderFlag(String order_flag) {
		String customercommand = "";
		if (order_flag.contains("1")) {
			customercommand += "cod –货到付款";
		}
		if (order_flag.contains("2")) {
			customercommand += "limit-限时配送";
		}
		if (order_flag.contains("8")) {
			customercommand += "换货";
		}
		if (order_flag.contains("9")) {
			customercommand += "上门";
		}
		return customercommand;
	}

}

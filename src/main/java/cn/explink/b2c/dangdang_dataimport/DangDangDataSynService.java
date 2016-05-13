package cn.explink.b2c.dangdang_dataimport;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.explink.b2c.tools.B2cEnum;
import cn.explink.b2c.tools.DataImportDAO_B2c;
import cn.explink.b2c.tools.DataImportService_B2c;
import cn.explink.b2c.tools.JiontDAO;
import cn.explink.b2c.tools.JointEntity;
import cn.explink.dao.CustomWareHouseDAO;
import cn.explink.dao.CustomerDAO;
import cn.explink.dao.CwbDAO;
import cn.explink.domain.CustomWareHouse;
import cn.explink.domain.CwbOrder;
import cn.explink.enumutil.CwbOrderTypeIdEnum;
import cn.explink.enumutil.PaytypeEnum;
import cn.explink.pos.tools.JacksonMapper;
import cn.explink.util.DateTimeUtil;
import cn.explink.util.MD5.MD5Util;

@Service
public class DangDangDataSynService {
	private Logger logger = LoggerFactory.getLogger(DangDangDataSynService.class);

	@Autowired
	JiontDAO jiontDAO;
	@Autowired
	CustomerDAO customerDAO;
	@Autowired
	CwbDAO cwbDAO;
	@Autowired
	CustomWareHouseDAO customWarehouseDAO;
	@Autowired
	DataImportService_B2c dataImportService_B2c;
	@Autowired
	DataImportDAO_B2c dataImportDAO_B2c;

	protected static ObjectMapper jacksonmapper = JacksonMapper.getInstance();

	public String getObjectMethod(int key) {
		JointEntity obj = jiontDAO.getJointEntity(key);
		return obj == null ? null : obj.getJoint_property();
	}

	public DangDangDataSyn getDangDang(int key) {
		if (getObjectMethod(key) == null) {
			return null;
		}
		JSONObject jsonObj = JSONObject.fromObject(getObjectMethod(key));
		DangDangDataSyn dangdang = (DangDangDataSyn) JSONObject.toBean(jsonObj, DangDangDataSyn.class);
		return dangdang;
	}

	public void edit(HttpServletRequest request, int joint_num) {
		DangDangDataSyn dangdang = new DangDangDataSyn();
		dangdang.setExpress_id(request.getParameter("express_id"));
		dangdang.setPrivate_key(request.getParameter("private_key"));
		dangdang.setOwn_url(request.getParameter("own_url"));
		dangdang.setOwn_url_test(request.getParameter("own_url_test"));

		dangdang.setCustomerid_tushu(request.getParameter("customerid_tushu"));
		dangdang.setCustomerid_baihuo(request.getParameter("customerid_baihuo"));
		dangdang.setCustomerid_dangrida(request.getParameter("customerid_dangrida"));
		dangdang.setCustomerid_zhaoshang(request.getParameter("customerid_zhaoshang"));
		dangdang.setCharcode(request.getParameter("charcode"));
		dangdang.setBranchid(Integer.valueOf(request.getParameter("branchid")));
		dangdang.setIsopen_drdflag(Integer.valueOf(request.getParameter("isopen_drdflag")));

		dangdang.setDrd_starttime(Long.valueOf(request.getParameter("drd_starttime")));
		dangdang.setDrd_endtime(Long.valueOf(request.getParameter("drd_endtime")));
		dangdang.setRuleEmaildateHours(Long.valueOf(request.getParameter("ruleEmaildateHours"))); // 按照时间点生成批次,此时间段之前来的数据作为昨天的批次.

		JSONObject jsonObj = JSONObject.fromObject(dangdang);

		JointEntity jointEntity = jiontDAO.getJointEntity(joint_num);
		if (jointEntity == null) {
			jointEntity = new JointEntity();
			jointEntity.setJoint_num(joint_num);
			jointEntity.setJoint_property(jsonObj.toString());
			jiontDAO.Create(jointEntity);
		} else {

			jointEntity.setJoint_num(joint_num);
			jointEntity.setJoint_property(jsonObj.toString());
			jiontDAO.Update(jointEntity);
		}

	}

	public void update(int joint_num, int state) {
		jiontDAO.UpdateState(joint_num, state);
	}

	/**
	 * 当当数据导入接口
	 * 
	 * @param dangdang
	 * @param version
	 * @param action
	 * @param express_id
	 * @param request_time
	 * @param token
	 * @param order_list
	 * @return
	 * @throws Exception
	 */
	public String AnalizyDangDangRequestInfo(DangDangDataSyn dangdangsyn, String version, String action, String express_id, String request_time, String token, String order_list) throws Exception {

		try {
			ValidateParamsDangDang(version, action, express_id, request_time, token, order_list, dangdangsyn);
		} catch (DangDangSynException e1) {
			logger.error("验证当当请求基础信息异常" + e1.getExpt_code() + e1.getExpt_msg());
			return respJSONInfo(e1.getExpt_code());
		}

		/**
		 * 解析JSON 保存在hashmap中
		 */
		if ("put".equals(action)) {
			List<Map<String, Object>> datalist = parseOrderListJSONByDangDang(order_list);
			if (datalist == null || datalist.size() == 0) {
				return respJSONInfo(DangDangExptEnum.OtherExpt.getExpt_code());
			}
			DangDangResp ddresp = new DangDangResp();
			List<Map<String, String>> orderlist = DealWithOrderlist_put(ddresp, datalist, dangdangsyn);

			for (Map<String, String> orderMap : orderlist) {
				try {
					List<Map<String, String>> ordermapList = new ArrayList<Map<String, String>>();
					ordermapList.add(orderMap);
					dataImportService_B2c.Analizy_DataDealByB2c(Long.valueOf(orderMap.get("customerid").toString()), B2cEnum.DangDang_daoru.getMethod(), ordermapList, dangdangsyn.getBranchid(), true);
					logger.info("处理dangdang导入后的订单信息成功,cwb={}", orderMap.get("cwb"));
				} catch (Exception e) {
					logger.error("当当调用数据导入接口异常!,order_list=" + order_list, e);
					return respJSONInfo(DangDangExptEnum.OtherExpt.getExpt_code());
				}

			}

			String returnjson = jacksonmapper.writeValueAsString(ddresp);

			logger.info("当前返回当当信息json={}", returnjson);
			return returnjson;

		} else {
			return DealWithDDInterface_del(order_list);
		}

	}

	private void ValidateParamsDangDang(String version, String action, String express_id, String request_time, String token, String order_list, DangDangDataSyn dangdang) {

		if (version == null || "".equals(version)) {
			throw new DangDangSynException(DangDangExptEnum.ParamsExpt.getExpt_code(), "版本号为空");
		}
		if (!"put".equals(action) && !"del".equals(action)) {
			throw new DangDangSynException(DangDangExptEnum.ParamsExpt.getExpt_code(), "请求action异常" + action);
		}
		if (token == null || "".equals(token)) {
			throw new DangDangSynException(DangDangExptEnum.ParamsExpt.getExpt_code(), "请求token为空");
		}
		if (order_list == null || "".equals(order_list)) {
			throw new DangDangSynException(DangDangExptEnum.ParamsExpt.getExpt_code(), "请求order_list为空");
		}
		// 配送公司编号匹配正确
		if (!express_id.equals(dangdang.getExpress_id())) {
			throw new DangDangSynException(DangDangExptEnum.ParamsExpt.getExpt_code(), "配送公司id不匹配,本地express_id=" + dangdang.getExpress_id() + ",当当发送express_id=" + express_id);
		}

		// 加密
		String MD5Data = MD5Util.getMD5String32Bytes(request_time + express_id, dangdang.getPrivate_key());

		if (!MD5Data.equals(token)) {
			throw new DangDangSynException(DangDangExptEnum.SignValidateFailed.getExpt_code(), "签名验证失败,本地=" + MD5Data + ",当当=" + token);
		}

	}

	/**
	 * 对当当订单信息格式进行处理
	 * 
	 * @param customerid
	 * @param datalist
	 * @param dangdang
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, String>> DealWithOrderlist_put(DangDangResp ddresp, List<Map<String, Object>> datalist, DangDangDataSyn dangdang) throws Exception {

		List<DangDangError> errlist = new ArrayList<DangDangError>();

		List<Map<String, String>> resporderlist = new ArrayList<Map<String, String>>();

		for (Map<String, Object> dataMap : datalist) {
			String order_id = "";
			Map<String, String> dataMapresp = new HashMap<String, String>();

			order_id = getParamsString(dataMap, "order_id"); // 订单号
			CwbOrder cwbOrder = cwbDAO.getCwbByCwb(order_id);
			if (cwbOrder != null) {
				ddresp.setError_code("21");
				DangDangError derr = new DangDangError();
				derr.setOrder_id(order_id);
				derr.setError_code(DangDangExptEnum.CwbExists.getExpt_code());
				errlist.add(derr);
				logger.warn("获取[当当]订单中含有重复数据cwb={}", order_id);
				continue;
			}

			String warehouse = getParamsString(dataMap, "warehouse"); // 发货仓库

			// 发货仓库 id 如果不存在则自动创建
			CustomWareHouse customerwarehouse = dataImportService_B2c.getCustomerWarehouseByName(warehouse);
			String customerwarehouseid = "";
			if (customerwarehouse == null) {
				ddresp.setError_code("21");
				DangDangError derr = new DangDangError();
				derr.setOrder_id(order_id);
				derr.setError_code(DangDangExptEnum.OtherExpt.getExpt_code());
				errlist.add(derr);
				logger.warn("该发货仓库在系统中不存在，需设置，warehouse=" + warehouse);
				continue;
			}
			customerwarehouseid = customerwarehouse.getWarehouseid() + "";

			String rcv_name = getParamsString(dataMap, "rcv_name"); // 收货人
			String rcv_province = getParamsString(dataMap, "rcv_province"); // 省
			String rcv_city = getParamsString(dataMap, "rcv_city");// 市
			String rcv_town = getParamsString(dataMap, "rcv_town"); // 区
			String rcv_address = getParamsString(dataMap, "rcv_address"); // 地址
			String rcv_zip = getParamsString(dataMap, "rcv_zip"); // 邮编
			String rcv_fix_tel = getParamsString(dataMap, "rcv_fix_tel"); // 联系电话
			String rcv_mobile_tel = getParamsString(dataMap, "rcv_mobile_tel"); // 移动电话
			String send_date = getParamsString(dataMap, "send_date"); // 发货时间

			String goods_payment = getParamsforIntStr(dataMap, "goods_payment"); // 货款
			double should_receive_payment = Double.valueOf(getParamsforIntStr(dataMap, "should_receive_payment")); // 应收款
			String cust_message = getParamsString(dataMap, "cust_message"); // 客户要求
			String package_num = dataMap.get("package_num") != null && !"".equals(dataMap.get("package_num").toString()) ? dataMap.get("package_num").toString() : "1"; // 件数
			String goods_weight = getParamsforIntStr(dataMap, "goods_weight"); // 货物重量
			String package_size = getParamsString(dataMap, "package_size"); // 货物尺寸
			double should_refund_payment = Double.parseDouble(dataMap.get("should_refund_payment") != null && !"".equals(dataMap.get("should_refund_payment").toString()) ? dataMap.get(
					"should_refund_payment").toString() : "0");
			if (should_refund_payment < 0 || should_receive_payment < 0) { // 应退款
				should_refund_payment = (-should_refund_payment);
				should_receive_payment = 0;
			}

			String product_items = parseCargonameDetail(dataMap); // 货物详情
			String expensive_items = parseExpensiveItems(dataMap, product_items);// 是否贵重物品
																					// (放在
																					// 商品名称
																					// 里面)

			// ////////////////////////////////////////////////////////////////////
			String client_type = getParamsforIntStr(dataMap, "client_type");// 运单来源=======
			String client_order_type = dataMap.get("client_order_type") != null && !"".equals(dataMap.get("client_order_type").toString()) ? dataMap.get("client_order_type").toString() : "1"; // 运单类型
			String shipment_type = dataMap.get("shipment_type") != null && !"".equals(dataMap.get("shipment_type").toString()) ? dataMap.get("shipment_type").toString() : "1"; // 配送方式
			String pay_way = getParamsforIntStr(dataMap, "pay_way"); // 支付方式
			int paywayid = PaytypeEnum.Xianjin.getValue();
			if (pay_way.equals("2")) { // 刷卡
				paywayid = PaytypeEnum.Pos.getValue();
			}

			// String
			// is_multi_package=getParamsString(dataMap,"is_multi_package"); //
			// 是否一单多件
			String best_arrive_date = getParamsforIntStr(dataMap, "best_arrive_date"); // 送货时间
			// customer_type=jo.get("customer_type").toString(); // 顾客类型（是否VIP）
			// ////////////////////////////////////////////////////////////////////
			String customer_id = "";
			String cwbordertypeid = parseCwbOrderTypeId(client_order_type); // 运单类型
			String cwbdelivertypeid = parseCwbDeliveryTypeId(shipment_type);// 配送方式

			// 如果是dangdang自营且是当日递的
			if ("1".equals(client_type) && Integer.valueOf(cwbdelivertypeid) == DangDangDelivertypeEnum.DangRidi.getValue()) {
				customer_id = dangdang.getCustomerid_dangrida();
				cwbdelivertypeid = "2"; // 再改回加急里面
			} else if ("1".equals(client_type)) { // 只判断自营中的百货和图书的类别
				customer_id = customerwarehouse.getCustomerid() + "";
			} else { // 默认招商
				customer_id = dangdang.getCustomerid_zhaoshang();
			}

			/**
			 * 按照当日递时间段来卡 当日递订单，海外环球要求，时间段：早上8点到下午3点
			 */
			if (dangdang.getIsopen_drdflag() == 1) {
				long drd_starttime = dangdang.getDrd_starttime(); // 08
				long drd_endtime = dangdang.getDrd_endtime(); // 15
				long nowHours = DateTimeUtil.getNowHours(); // 获取当前的小时数
				if (nowHours >= drd_starttime && nowHours <= drd_endtime) {
					customer_id = dangdang.getCustomerid_dangrida();
				}
			}

			// ruleEmaildateHours //
			String ruleEmaildateHours = "0";
			if (customer_id.equals(dangdang.getCustomerid_baihuo()) || customer_id.equals(dangdang.getCustomerid_tushu())) {
				if (dangdang.getRuleEmaildateHours() > 0) {
					ruleEmaildateHours = String.valueOf(dangdang.getRuleEmaildateHours());
				}
			}

			// 货款
			String cargoamount = "0"; // 发货货物金额
			String backcaramount = "0"; // 取货货物金额
			if ("1".equals(cwbordertypeid)) { // 配送
				cargoamount = goods_payment;
			} else if ("2".equals(cwbordertypeid)) { // 上门退
				backcaramount = goods_payment;
			} else if ("3".equals(cwbordertypeid)) { // 上门换
				cargoamount = goods_payment;
			}

			// 发货数量
			String sendcargonum = "1";
			// 取货数量
			String backcargonum = "0";
			if ("1".equals(cwbordertypeid)) {
				sendcargonum = package_num;
			} else if ("2".equals(cwbordertypeid)) { // 上门退
				backcargonum = package_num;
			}

			// 客户要求
			String customercommand = best_arrive_date + ",配送要求：" + getCustomerCommand(shipment_type);
			if (cust_message != null && !cust_message.equals("")) {
				customercommand = cust_message + ";" + best_arrive_date + ",配送要求：" + getCustomerCommand(shipment_type);
			}

			// 商品信息 发出，取回
			String sendcargoname = warehouse;
			String backcargoname = "取回商品";
			if ("1".equals(cwbordertypeid)) { // 配送 发出商品
				sendcargoname += product_items + "^" + expensive_items;
				backcargoname = "";
			} else if ("2".equals(cwbordertypeid)) {
				backcargoname += product_items + "^" + expensive_items;
				sendcargoname = "";
			}

			// 邮件时间 当前时间
			String emaildate = DateTimeUtil.getNowDate() + " 00:00:00";
			// String csremark="配送方式:"+getCustomerCommand(cwbdelivertypeid);
			// //配送方式

			dataMapresp.put("cwb", order_id);
			dataMapresp.put("consigneename", rcv_name);
			dataMapresp.put("cwbprovince", rcv_province);
			dataMapresp.put("cwbcity", rcv_city);
			dataMapresp.put("cwbcounty", rcv_town);
			dataMapresp.put("consigneeaddress", rcv_address);
			dataMapresp.put("consigneepostcode", rcv_zip);
			dataMapresp.put("consigneephone", rcv_fix_tel);
			dataMapresp.put("consigneemobile", rcv_mobile_tel);
			dataMapresp.put("shiptime", send_date);
			dataMapresp.put("cargoamount", goods_payment); // 货物金额
			dataMapresp.put("receivablefee", should_receive_payment + ""); // 代收款
			dataMapresp.put("customercommand", customercommand);
			dataMapresp.put("sendcarname", sendcargoname); // 发货商品
			dataMapresp.put("sendcarnum", package_num); // 发货 件数
			dataMapresp.put("backcargoname", backcargoname); // 取回商品
			dataMapresp.put("cargorealweight", goods_weight); // 重量
			dataMapresp.put("cargotype", package_size); // 货物类别
			dataMapresp.put("paybackfee", should_refund_payment + ""); // 应退款
			dataMapresp.put("cwbordertypeid", cwbordertypeid); // 订单类型
			dataMapresp.put("cwbdelivertypeid", cwbdelivertypeid); // 配送方式
			dataMapresp.put("caramount", cargoamount); // 发货货物金额
			dataMapresp.put("backcaramount", backcaramount); // 取回货物金额
			dataMapresp.put("cwbremark", customercommand); // 备注
			dataMapresp.put("paywayid", paywayid + ""); // 原支付方式
			dataMapresp.put("newpaywayid", paywayid + ""); // 原支付方式
			dataMapresp.put("startbranchid", "0"); // 默认0
			dataMapresp.put("customerwarehouseid", customerwarehouseid); // 发货仓库
			dataMapresp.put("customerid", customer_id); // customerid
			dataMapresp.put("ruleEmaildateHours", ruleEmaildateHours); // customerid

			resporderlist.add(dataMapresp);

		}

		if (ddresp.getError_code() == null) {
			ddresp.setError_code(DangDangExptEnum.Success.getExpt_code());
		}
		ddresp.setError_list(errlist);

		return resporderlist;
	}

	/**
	 * 根据配送方式 返回一个desc
	 * 
	 * @return
	 */
	private String getCustomerCommand(String cwbdeliverytypeid) {
		for (DeliveryTypeEnum em : DeliveryTypeEnum.values()) {
			if (String.valueOf(em.getMsg_code()).equals(cwbdeliverytypeid)) {
				return em.getMsg_des();
			}
		}
		return "普通配送";
	}

	private String parseCwbDeliveryTypeId(String shipment_type) {
		String cwbdelivertypeid = "";
		if ("1".equals(shipment_type)) {
			cwbdelivertypeid = "1";
		} else if ("5".equals(shipment_type) || "7".equals(shipment_type)) {
			cwbdelivertypeid = "2";
		} else if (DangDangDelivertypeEnum.DangRidi.getValue() == Integer.valueOf(shipment_type)) {
			cwbdelivertypeid = DangDangDelivertypeEnum.DangRidi.getValue() + "";
		} else {
			cwbdelivertypeid = "1";
		}
		return cwbdelivertypeid;
	}

	private String parseCwbOrderTypeId(String client_order_type) {
		int cwbordertypeid = CwbOrderTypeIdEnum.Peisong.getValue();
		if (DangDangOrdertypeEnum.PuTongPeiSong.getValue() == Integer.valueOf(client_order_type)) {
			cwbordertypeid = CwbOrderTypeIdEnum.Peisong.getValue();
		} else if (DangDangOrdertypeEnum.YouJiHuanHuoDan.getValue() == Integer.valueOf(client_order_type) || DangDangOrdertypeEnum.ShangMenHuanHuoDan.getValue() == Integer.valueOf(client_order_type)) {
			cwbordertypeid = CwbOrderTypeIdEnum.Shangmenhuan.getValue();
		} else if (DangDangOrdertypeEnum.ShangMenTuiHuoDan.getValue() == Integer.valueOf(client_order_type)) {
			cwbordertypeid = CwbOrderTypeIdEnum.Shangmentui.getValue(); // 上门退
		} else {
			cwbordertypeid = CwbOrderTypeIdEnum.Peisong.getValue(); // 配送
		}
		return cwbordertypeid + "";
	}

	private String parseCargonameDetail(Map<String, Object> dataMap) {
		/**
		 * 订单详细信息
		 */
		String product_items = dataMap.get("product_items") != null ? dataMap.get("product_items").toString() : "";
		if (product_items != null && !"".equals(product_items)) {
			List json1 = (List) dataMap.get("product_items");
			if (json1 != null && json1.size() > 0) {
				for (int k = 0; k < json1.size(); k++) {
					Map jo1 = (Map) json1.get(k);
					String product_id = jo1.get("product_id") != null ? jo1.get("product_id").toString() : ""; // 商品号
					String product_name = jo1.get("product_name") != null ? jo1.get("product_name").toString() : "";// 商品名称
					String product_num = jo1.get("product_num") != null ? jo1.get("product_num").toString() : "";// 商品数量
					String product_price = jo1.get("product_price") != null ? jo1.get("product_price").toString() : "";// 商品单价
					String product_total = jo1.get("product_total") != null ? jo1.get("product_total").toString() : "";// 小计
					// product_items = product_id + "," + product_name + "," +
					// product_num + "," + product_price + "," + product_total +
					// "/";
					product_items = product_name;
				}

			}
		}
		return product_items;
	}

	private String parseExpensiveItems(Map<String, Object> dataMap, String product_items) {
		String expensive_items = dataMap.get("expensive_items") != null ? dataMap.get("expensive_items").toString() : "";
		if (!"".equals(product_items)) {
			List json2 = (List) dataMap.get("expensive_items");
			if (json2 != null && json2.size() > 0) {
				for (int k = 0; k < json2.size(); k++) {
					Map jo1 = (Map) json2.get(k);
					String sub_order_id = jo1.get("sub_order_id") != null ? jo1.get("sub_order_id").toString() : ""; // 子单号
					String goods_weight1 = jo1.get("goods_weight") != null ? jo1.get("goods_weight").toString() : "";// 商品重量
					String cover_code = jo1.get("cover_code") != null ? jo1.get("cover_code").toString() : "";// 封签号
					expensive_items = sub_order_id + "," + goods_weight1 + "," + cover_code + "/";
				}
			}
		}
		return expensive_items;
	}

	private List<Map<String, Object>> parseOrderListJSONByDangDang(String json) {
		try {
			return jacksonmapper.readValue(json, List.class);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("order_list转化为List<Map<String, Object>>发生异常", e);
			return null;
		}

	}

	public String respJSONInfo(String expt_code) {
		try {

			DangDangResp dd = new DangDangResp();
			dd.setError_code(expt_code);
			return jacksonmapper.writeValueAsString(dd);
		} catch (Exception e) {
			logger.error("转化为JSON格式异常", e);
			return "转化为JSON格式异常";
		}
	}

	public String getParamsString(Map<String, Object> dataMap, String params) {
		return dataMap.get(params) != null ? dataMap.get(params).toString() : "";
	}

	public String getParamsforIntStr(Map<String, Object> dataMap, String params) {
		return dataMap.get(params) != null ? dataMap.get(params).toString() : "0";
	}

	private String DealWithDDInterface_del(String order_list) throws Exception {
		List<DangDangError> errlist = new ArrayList<DangDangError>();

		DangDangResp ddresp = new DangDangResp();
		ddresp.setError_code("0");
		JSONArray arrays = JSONArray.fromObject(order_list);

		int j = 0;
		for (int i = 0; i < arrays.size(); i++) {
			String order_id = arrays.get(i).toString();

			CwbOrder cwbOrder = cwbDAO.getCwbByCwb(order_id);
			if (cwbOrder == null) {
				ddresp.setError_code("21");
				DangDangError derr = new DangDangError();
				derr.setOrder_id(order_id);
				derr.setError_code("101");
				logger.error("del失败！该订单不存在！cwb=" + order_id);
				errlist.add(derr);
			} else {
				cwbDAO.dataLoseByCwb(order_id);
				dataImportDAO_B2c.dataLoseB2ctempByCwb(order_id);
				logger.info("del成功！当前失效订单cwb={}", order_id);
			}

		}
		ddresp.setError_list(errlist);

		return JacksonMapper.getInstance().writeValueAsString(ddresp);
	}

	public static void main(String[] args) {
		String nowdate = DateTimeUtil.formatDate(DateTimeUtil.getPreviousDate(new Date()));
		System.out.println(nowdate);
	}

}

package cn.explink.b2c.vipshop;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.explink.b2c.tools.B2cEnum;
import cn.explink.b2c.tools.DataImportDAO_B2c;
import cn.explink.b2c.tools.DataImportService_B2c;
import cn.explink.b2c.tools.JiontDAO;
import cn.explink.b2c.tools.JointEntity;
import cn.explink.b2c.tools.JointService;
import cn.explink.dao.CustomWareHouseDAO;
import cn.explink.dao.CustomerDAO;
import cn.explink.dao.CwbDAO;
import cn.explink.dao.OrderGoodsDAO;
import cn.explink.domain.OrderGoods;
import cn.explink.enumutil.CwbOrderTypeIdEnum;
import cn.explink.enumutil.PaytypeEnum;
import cn.explink.service.CwbOrderService;
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
	CwbOrderService cwbOrderService;

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

	public void edit(HttpServletRequest request, int joint_num) {
		VipShop vipshop = new VipShop();
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
		vip.setIsopendownload(vipshop.getIsopendownload());
		vip.setForward_hours(vipshop.getForward_hours());
		vip.setIsTuoYunDanFlag(vipshop.getIsTuoYunDanFlag());
		vip.setIsShangmentuiFlag(vipshop.getIsShangmentuiFlag());

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
	public long getOrdersByVipShop(int vipshop_key) {
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

		// 构建请求，解析返回信息
		Map<String, Object> parseMap = this.requestHttpAndCallBackAnaly(vipshop);

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

		List<Map<String, String>> orderlist = this.parseXmlDetailInfo(parseMap, vipshop);
		if ((orderlist == null) || (orderlist.size() == 0)) {
			this.updateMaxSEQ(vipshop_key, vipshop);
			this.logger.info("请求Vipshop订单信息-没有获取到订单或者订单信息重复！,当前SEQ={}", vipshop.getVipshop_seq());
			return -1;
		}

		if (vipshop.getIsTuoYunDanFlag() == 0) {
			try {
				long warehouseid = this.getVipShop(vipshop_key).getWarehouseid();
				this.dataImportService_B2c.Analizy_DataDealByB2c(Long.parseLong(vipshop.getCustomerids()), B2cEnum.VipShop_beijing.getMethod(), orderlist, warehouseid, true);
				this.logger.info("请求Vipshop订单信息-插入数据库处理成功！");

				this.updateMaxSEQ(vipshop_key, vipshop);
				this.logger.info("请求Vipshop订单信息-更新了最大的SEQ!{}", vipshop.getVipshop_seq());
			} catch (Exception e) {
				this.logger.error("vipshop调用数据导入接口异常!,订单List信息:" + orderlist + "message:", e);
				e.printStackTrace();
			}
		} else {
			for (Map<String, String> dataMap : orderlist) {
				List<Map<String, String>> onelist = new ArrayList<Map<String, String>>();
				onelist.add(dataMap);
				try {
					String emaildate = dataMap.get("remark4").toString();
					long warehouseid = this.getVipShop(vipshop_key).getWarehouseid();
					this.dataImportService_B2c
							.Analizy_DataDealByB2cByEmaildate(Long.parseLong(vipshop.getCustomerids()), B2cEnum.VipShop_beijing.getMethod(), onelist, warehouseid, true, emaildate, 0);

					this.updateMaxSEQ(vipshop_key, vipshop);
					this.logger.info("请求Vipshop订单信息导入成功cwb={}-更新了最大的SEQ!{}", dataMap.get("cwb").toString(), vipshop.getVipshop_seq());
				} catch (Exception e) {
					this.logger.error("vipshop调用数据导入接口异常!cwb=" + dataMap.get("cwb").toString(), e);

				}

			}
		}

		return 1;

	}

	/**
	 * 构建请求，解析返回
	 *
	 * @param vipshop
	 * @return
	 */
	private Map<String, Object> requestHttpAndCallBackAnaly(VipShop vipshop) {
		String request_time = DateTimeUtil.getNowTime();
		String requestXML = this.StringXMLRequest(vipshop, request_time);
		String MD5Str = vipshop.getPrivate_key() + VipShopConfig.version + request_time + vipshop.getShipper_no() + vipshop.getVipshop_seq() + vipshop.getGetMaxCount();
		String sign = VipShopMD5Util.MD5(MD5Str);
		String endpointUrl = vipshop.getGetCwb_URL();
		String response_XML = null;

		this.logger.info("获取vipshop订单XML={}", requestXML);

		try {
			response_XML = this.soapHandler.HTTPInvokeWs(endpointUrl, VipShopConfig.nameSpace, VipShopConfig.requestMethodName, requestXML, sign);
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

	private String StringXMLRequest(VipShop vipshop, String request_time) {

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
		sub.append("<seq>" + vipshop.getVipshop_seq() + "</seq>");
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
	public List<Map<String, String>> parseXmlDetailInfo(Map paseXmlMap, VipShop vipshop) {
		List<Map<String, Object>> orderlist = (List<Map<String, Object>>) paseXmlMap.get("orderlist");
		List<Map<String, String>> paraList = new ArrayList<Map<String, String>>();
		String seq_arrs = "";
		if ((orderlist != null) && (orderlist.size() > 0)) {
			for (Map<String, Object> datamap : orderlist) {
				seq_arrs = this.SaveMapDataAndGetMaxSEQ(vipshop, paraList, seq_arrs, datamap);
			}
		}
		long maxSEQ = this.getMaxSEQ(seq_arrs.split(","));
		if (maxSEQ != 0) {
			vipshop.setVipshop_seq(maxSEQ); // 赋值最大的seq
		}
		return paraList;
	}

	private String SaveMapDataAndGetMaxSEQ(VipShop vipshop, List<Map<String, String>> paraList, String seq_arrs, Map<String, Object> datamap) {
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

			String total_pack = VipShopGetCwbDataService.convertEmptyString("total_pack", datamap); // 新增箱数

			String customer_name = VipShopGetCwbDataService.convertEmptyString("customer_name", datamap); // 客户

			String service_type = VipShopGetCwbDataService.convertEmptyString("service_type", datamap); // 服务类型：服务类型：1.
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

			/**
			 * 新增参数
			 */
			String original_weight = "".equals(VipShopGetCwbDataService.convertEmptyString("original_weight", datamap)) ? "0" : VipShopGetCwbDataService.convertEmptyString("original_weight", datamap); // 重量
			String ext_pay_type = "".equals(VipShopGetCwbDataService.convertEmptyString("ext_pay_type", datamap)) ? "0" : VipShopGetCwbDataService.convertEmptyString("ext_pay_type", datamap); // 支付方式
			int paywayid = ext_pay_type.equals("1") ? PaytypeEnum.Pos.getValue() : PaytypeEnum.Xianjin.getValue();
			String attemper_no = VipShopGetCwbDataService.convertEmptyString("attemper_no", datamap); // 托运单号，根据此字段生成批次.
			String created_dtm_loc = VipShopGetCwbDataService.convertEmptyString("created_dtm_loc", datamap); // 批次时间，绑定托运单号

			String rec_create_time = VipShopGetCwbDataService.convertEmptyString("rec_create_time", datamap); // 生成时间

			String order_delivery_batch = VipShopGetCwbDataService.convertEmptyString("order_delivery_batch", datamap); // 1（默认）-一配订单：2-二配订单
			if ("1".equals(order_delivery_batch)) {
				order_delivery_batch = "一配订单";
			} else if ("2".equals(order_delivery_batch)) {
				order_delivery_batch = "二配订单";
			} else {
				order_delivery_batch = "普通订单";
			}

			String freight = VipShopGetCwbDataService.convertEmptyString("freight", datamap); // 上门退运费

			String remarkFreight = "";

			String business_type = VipShopGetCwbDataService.convertEmptyString("business_type", datamap); // 0：唯品会出仓派送件(默认)，1：客退上门揽收件

			String cwbordertype = business_type.equals("1") ? String.valueOf(CwbOrderTypeIdEnum.Shangmentui.getValue()) : String.valueOf(CwbOrderTypeIdEnum.Peisong.getValue());

			String warehouse_addr = VipShopGetCwbDataService.convertEmptyString("warehouse_addr", datamap); // 仓库地址

			String cmd_type = VipShopGetCwbDataService.convertEmptyString("cmd_type", datamap); // 操作指令new
			
			String pack_nos = VipShopGetCwbDataService.convertEmptyString("pack_nos", datamap); // 箱号
			
			// : 新建
			// edit:修改
			// cancel
			// : 删除
			// 客退上门揽件会有修改/删除

			if (cwbordertype.equals(String.valueOf(CwbOrderTypeIdEnum.Shangmentui.getValue()))) {
				double freight_d = Double.valueOf((freight != null) && !freight.isEmpty() ? freight : "0");
				if (freight_d > 0) {
					remarkFreight = "现付";
				} else {
					remarkFreight = "到付";
				}
			}

			if ((created_dtm_loc == null) || created_dtm_loc.isEmpty()) {
				created_dtm_loc = DateTimeUtil.getNowDate() + " 00:00:00";
			}
			String transcwb=pack_nos!=null&&!pack_nos.isEmpty()?pack_nos:order_sn;
			int sendcarnum=1;
			try {
				if(pack_nos!=null&&!pack_nos.isEmpty()){
					if(pack_nos.split(",").length>1){
						sendcarnum=pack_nos.split(",").length;
					}else{
						sendcarnum=1;
					}
				}
			} catch (Exception e) {}
			
			dataMap.put("cwb", order_sn);
			dataMap.put("transcwb", transcwb);
			dataMap.put("consigneename", buyer_name);
			dataMap.put("sendcarnum", (total_pack.isEmpty() ? "1" : total_pack));
			dataMap.put("consigneemobile", mobile);
			dataMap.put("consigneephone", tel);
			dataMap.put("consigneepostcode", post_code);
			dataMap.put("consigneeaddress", buyer_address);
			dataMap.put("receivablefee", money);
			dataMap.put("customercommand", transport_day + "," + order_delivery_batch + "," + remarkFreight);
			dataMap.put("sendcarnum", sendcarnum+"");
			dataMap.put("sendcargoname", "[发出商品]");
			dataMap.put("customerid", vipshop.getCustomerids());
			dataMap.put("remark1", order_batch_no); // 交接单号
			dataMap.put("remark2", rec_create_time); // 2015-01-08修改为生成时间

			dataMap.put("cargorealweight", original_weight); // 重量
			dataMap.put("paywayid", String.valueOf(paywayid)); // 支付方式
			dataMap.put("remark3", "托运单号:" + attemper_no); // 托运单号
			dataMap.put("remark4", created_dtm_loc); // 批次时间

			dataMap.put("cargotype", cargotype); // 服务类别
			dataMap.put("remark5", warehouse_addr); // 仓库地址

			dataMap.put("cwbordertypeid", cwbordertype);
			dataMap.put("shouldfare", freight.isEmpty() ? "0" : freight);
			dataMap.put("cwbordertypeid", cwbordertype);

			if (cwbordertype.equals(String.valueOf(CwbOrderTypeIdEnum.Shangmentui.getValue()))) {

				if ("edit".equalsIgnoreCase(cmd_type)) {
					this.dataImportDAO_B2c.updateBycwb(dataMap);
					this.cwbDAO.updateBycwb(dataMap);
					seq_arrs += seq + ",";
					return seq_arrs;
				}
				// 订单取消
				if ("cancel".equalsIgnoreCase(cmd_type)) {
					this.dataImportDAO_B2c.dataLoseB2ctempByCwb(order_sn);
					this.cwbDAO.dataLoseByCwb(order_sn);
					orderGoodsDAO.loseOrderGoods(order_sn);
					cwbOrderService.datalose_vipshop(order_sn);
					seq_arrs += seq + ",";
					return seq_arrs;
				}

			}

			if ((this.cwbDAO.getCwbByCwb(order_sn) != null)) {
				this.logger.info("获取唯品会订单有重复,已过滤...cwb={},更新SEQ={}", order_sn, seq);
				seq_arrs += seq + ",";
				return seq_arrs;
			}

			if (cwbordertype.equals(String.valueOf(CwbOrderTypeIdEnum.Shangmentui.getValue()))) {

				if ("new".equalsIgnoreCase(cmd_type)) {
					// 插入商品列表,try防止异常
					this.insertOrderGoods(datamap, order_sn);
				}

			}

			this.logger.info("唯品会订单cwb={},seq={}", order_sn, seq);

			if ("".equals(dataMap.get("cwb").toString())) { // 若订单号为空，则继续。
				seq_arrs += seq + ",";
				return seq_arrs;
			}
			paraList.add(dataMap);
			seq_arrs += seq + ",";

		} catch (Exception e) {
			this.logger.error("唯品会订单下载处理单条信息异常,cwb=" + order_sn, e);
		}
		return seq_arrs;
	}

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
			this.logger.error("获取商品列表异常,单号=" + order_sn, e);
		}
	}

	private static String convertEmptyString(String str, Map m) {
		String returnStr = m.get(str) == null ? "" : m.get(str).toString();
		return returnStr;
	}

	// 得到最大的值。
	public long getMaxSEQ(String[] seq_arrs) {
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

}

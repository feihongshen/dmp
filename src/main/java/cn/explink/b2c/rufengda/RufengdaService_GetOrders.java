package cn.explink.b2c.rufengda;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import cn.explink.b2c.tools.B2cEnum;
import cn.explink.controller.CwbOrderDTO;
import cn.explink.enumutil.PaytypeEnum;
import cn.explink.pos.tools.JacksonMapper;
import cn.explink.util.DateTimeUtil;
import cn.explink.util.WebServiceHandler;
import cn.explink.util.MD5.DES3Utils;
import cn.explink.util.MD5.RSAUtils;

/**
 * 如风达 获取订单数据的接口
 * 
 * @author Administrator
 *
 */
@Service
public class RufengdaService_GetOrders extends RufengdaService {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	public String getParamsString(Map<String, Object> dataMap, String params) {
		return dataMap.get(params) != null ? dataMap.get(params).toString() : "";
	}

	public String getParamsforIntStr(Map<String, Object> dataMap, String params) {
		return dataMap.get(params) != null ? dataMap.get(params).toString() : "0";
	}

	ObjectMapper objectMapper = JacksonMapper.getInstance();

	/**
	 * 解析order_list
	 * 
	 * @param json
	 * @throws Exception
	 */
	private List<Map<String, Object>> parseOrdersJsonList(String json) {
		try {
			return objectMapper.readValue(json, List.class);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("如风达转化为List<Map<String, Object>>发生异常", e);
		}
		return null;
	}

	/***
	 * 获取订单detail的接口
	 * ============================================================
	 * ======================
	 * 
	 * @throws Exception
	 */

	public long GetOrders(int rfd_key, int loopcount) {

		Rufengda rfd = getRufengda(rfd_key);
		int isOpenFlag = jointService.getStateForJoint(rfd_key);
		if (isOpenFlag == 0) {
			logger.warn("未开启[如风达]获取订单接口");
			return -1;
		}

		// 获取订单
		try {
			String lcId = rfd.getLcId();
			if (lcId.contains("55df-4c35-95bb-65e4d02949ca")) {
				lcId = lcId + "-lixuepeng";
			}

			String returnValue = (String) WebServiceHandler.invokeWs(rfd.getWs_url(), "GetOrders", lcId);
			if (returnValue == null || returnValue.equals("") || returnValue.indexOf(",") <= -1) {
				logger.error("获取如风达订单返回空,loopcount=" + loopcount);
				return 0;
			}
			String invokeReturn = returnValue.substring(0, returnValue.lastIndexOf(","));
			String crc = returnValue.substring(returnValue.lastIndexOf(",") + 1);
			if (rfd.getIsopensignflag() == 1) {
				boolean ret = RSAUtils.verify(invokeReturn, crc, rfd);
				if (!ret) {
					logger.error("获取订单信息验证签名异常,crc=" + crc);
					return 0;
				}
			}

			if (invokeReturn == null || invokeReturn.trim().equals("")) {
				logger.warn("获取[如风达]订单信息 为空！");
				return 0;
			}
			logger.info("获取[如风达]loopcount=" + loopcount + ",订单信息={},签名字符串={}", invokeReturn, crc);

			String emaildate = DateTimeUtil.getNowDate() + " 00:00:00";

			List<Map<String, Object>> orderlist = parseOrdersJsonList(invokeReturn); // jackson转化为List

			List<Map<String, String>> xmlList = getDetailParamList(rfd, orderlist, emaildate); // 构建一个插入数据库的对象

			try {
				long warehouseid = getRufengda(rfd_key).getWarehouseid();
				List<CwbOrderDTO> extractss = dataImportService_B2c.Analizy_DataDealByB2c(Long.parseLong(rfd.getCustomerid()), B2cEnum.Rufengda.getMethod(), xmlList, warehouseid, true);
				logger.debug("处理[如风达]后的订单信息=" + extractss.toString());
				return xmlList.size();
			} catch (Exception e) {
				logger.error("[如风达]调用数据导入接口异常!,Json=" + invokeReturn, e);
				return 0;
			}

		} catch (Exception e) {
			logger.error("获取[如风达]订单出现未知异常", e);
			e.printStackTrace();
			return 0;
		}

	}

	private List<Map<String, String>> getDetailParamList(Rufengda rfd, List<Map<String, Object>> orderlist, String emaildate) throws Exception {

		String nowtime = DateTimeUtil.getNowTime();
		List<Map<String, String>> paraList = new ArrayList<Map<String, String>>();
		String key = rfd.getDes_key() + orderlist.size() + rfd.getLcId();
		if (orderlist != null && orderlist.size() > 0) {
			for (Map<String, Object> order : orderlist) {
				Map<String, String> dataMap = buildOrderMap(rfd, emaildate, nowtime, key, order);
				if (dataMap == null) {
					continue;
				}
				paraList.add(dataMap);
			}
		}
		return paraList;
	}

	// +PD4xvd8TwBinMMb6g6RtwenjbHxZgLrt9QB8Eh2SnHA5H90vvxN+Q==

	private Map<String, String> buildOrderMap(Rufengda rfd, String emaildate, String nowtime, String key, Map<String, Object> order) throws Exception {
		String OrderNO = getParamsString(order, "OrderNO"); // 订单号
		String CustomerOrder = getParamsString(order, "CustomerOrder"); // 商家单号

		String MerchantName = getParamsString(order, "MerchantName"); // 商家名称

		String cwbordertypeid = getCwbOrderType(getParamsString(order, "OrderType")); // 订单类型
		String UserName = getParamsString(order, "UserName"); // 收件人
		String PostalCode = getParamsString(order, "PostalCode"); // 收件人邮编
		String Province = getParamsString(order, "Province"); // 收件人所在省
		String City = getParamsString(order, "City"); // 收件人 收件市
		String Area = getParamsString(order, "Area"); // 收件人 区
		String Weight = getParamsforIntStr(order, "Weight"); // 重量
		String Address = "";
		String MobilePhone = ""; // 手机
		String ReceiveTel = ""; // 座机

		String address_des = getParamsString(order, "Address");
		Address = DES3Utils.decryptMode(address_des, key);
		String mobilephone_des = getParamsString(order, "MobilePhone");
		MobilePhone = DES3Utils.decryptMode(mobilephone_des, key);
		String receiveTel_des = getParamsString(order, "ReceiveTel");
		ReceiveTel = DES3Utils.decryptMode(receiveTel_des, key);

		Address = Province + City + Area + Address;// 收件人详细地址 拼接省市区

		String WaybillSourse = getParamsString(order, "WaybillSourse"); // 订单来源
																		// id
		String WaybillSourseRemark = "运单来源：";
		if ("0".equals(WaybillSourse)) {
			WaybillSourseRemark += "Vancl";
		} else if ("1".equals(WaybillSourse)) {
			WaybillSourseRemark += "VJIA";
		} else if ("2".equals(WaybillSourse)) {
			WaybillSourseRemark += "如风达";
		}

		WaybillSourseRemark = WaybillSourseRemark + "," + MerchantName;

		String CallBefore = getParamsString(order, "CallBefore"); // 是否送前提示
		String OutTime = getParamsString(order, "OutTime"); // 出库如风达 时间
		String NeedFund = getParamsforIntStr(order, "NeedFund"); // 应收金额
		String BackFund = getParamsforIntStr(order, "BackFund"); // 应退金额
		String WareHouseName = getParamsString(order, "WareHouseName"); // 仓库名称

		WaybillSourseRemark += (getParamsString(order, "OrderType").equals("3") ? " 签收返单" : "");

		String PaymentType = getParamsString(order, "PaymentType"); // 付款方式
																	// ,放在customercommand列
		int paywayid = "0".equals(PaymentType) ? PaytypeEnum.Xianjin.getValue() : PaytypeEnum.Pos.getValue(); // 支付类型

		// 凡客的发货仓库，需提前在系统中设置
		String warhouseid = dataImportService_B2c.getCustomerWarehouseIdByName(WareHouseName, rfd.getCustomerid());

		// 商品信息
		List orderGoods = order.get("Goods") != null && !"".equals(order.get("Goods").toString()) ? (List) order.get("Goods") : new ArrayList();
		String ProductName = "";
		String ProductCode = "";
		String OrderAmount = "1";
		String SellPrice = "0";
		String Size = "";
		if (orderGoods != null && orderGoods.size() > 0) {
			Map goodsMap = (Map) orderGoods.get(0);
			ProductName = goodsMap.get("ProductName") != null ? goodsMap.get("ProductName").toString() : ""; // 商品名称
			ProductCode = goodsMap.get("ProductCode") != null ? goodsMap.get("ProductCode").toString() : ""; // 商品编号
			// OrderAmount=goodsMap.get("OrderAmount")!=null?goodsMap.get("OrderAmount").toString():"1";
			// //商品数量
			SellPrice = goodsMap.get("SellPrice") != null ? goodsMap.get("SellPrice").toString() : "0"; // 商品单价
			Size = goodsMap.get("Size") != null ? goodsMap.get("Size").toString() : ""; // 商品尺寸
		}
		String sendcargoname = "发出商品-" + ProductName; // 商品信息 发出，取回
		String backcargoname = "";
		if ("2".equals(cwbordertypeid)) {
			backcargoname = "取回商品";
			sendcargoname = "";
		} else if ("3".equals(cwbordertypeid)) {
			backcargoname = "取回商品";
		}

		CwbOrderDTO cwbOrder = dataImportDAO_B2c.getCwbByCwbB2ctemp(OrderNO);
		if (cwbOrder != null) {
			logger.warn("获取[如风达]临时表订单中含有重复数据cwb={}", OrderNO);
			return null;
		}

		Map<String, String> dataMap = new HashMap<String, String>();
		dataMap.put("cwb", OrderNO);
		dataMap.put("transcwb", CustomerOrder);
		dataMap.put("emaildate", emaildate);
		dataMap.put("nowtime", nowtime);
		dataMap.put("consigneename", UserName);
		dataMap.put("consigneepostcode", PostalCode);
		dataMap.put("consigneeaddress", Address);
		dataMap.put("consignoraddress", WaybillSourseRemark); // 发件地址
		dataMap.put("cwbprovince", Province);
		dataMap.put("cwbcity", City);
		dataMap.put("cwbcounty", Area);
		dataMap.put("consigneemobile", MobilePhone);
		dataMap.put("consigneephone", ReceiveTel);
		dataMap.put("cargorealweight", Weight);
		dataMap.put("customercommand", CallBefore.equals("1") ? "送前需提示" : "");
		dataMap.put("cargoamount", SellPrice + ""); // 货物金额 暂定为0
		dataMap.put("receivablefee", NeedFund); // 代收款
		dataMap.put("paybackfee", BackFund); // 应退金额
		dataMap.put("sendcarname", sendcargoname); // 发货商品
		dataMap.put("sendcarnum", OrderAmount); // 发货数量
		dataMap.put("backcargoname", backcargoname); // 发货商品
		dataMap.put("customerwarehouseid", warhouseid); // 发货仓库
		dataMap.put("cwbordertypeid", cwbordertypeid); // 订单类型
		dataMap.put("cwbdelivertypeid", "1"); // 配送方式
		dataMap.put("startbranchid", "0"); // 当前库房id
		dataMap.put("customerid", rfd.getCustomerid()); // 供货商Id
		dataMap.put("cwbremark", WaybillSourseRemark); // 供货商Id
		dataMap.put("paywayid", String.valueOf(paywayid)); // 支付方式
		return dataMap;
	}

	// 获取订单类型
	public String getCwbOrderType(String cwbOrdertype) {
		if (cwbOrdertype.equals("1")) { // 1上门换 2，上门退货单， 0，普通订单，3，为签收返单
			return "3";
		}
		if (cwbOrdertype.equals("2")) {
			return "2";
		}
		if (cwbOrdertype.equals("3")) {
			return "1";
		}
		if (cwbOrdertype.equals("0")) {
			return "1";
		}
		return "1";
	}

	public static void main(String[] args) throws Exception {

		String returnValue = (String) WebServiceHandler.invokeWs("http://edi.wuliusys.com/SynOrderService.svc?wsdl", "GetOrders", "d920b446-b749-4310-b49e-4a4837e3c0a3");

		System.out.println("结果：" + returnValue);
	}

}

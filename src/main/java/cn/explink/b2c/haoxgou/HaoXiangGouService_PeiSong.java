package cn.explink.b2c.haoxgou;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.explink.b2c.explink.ExplinkService;
import cn.explink.b2c.tools.B2cEnum;
import cn.explink.b2c.tools.DataImportService_B2c;
import cn.explink.b2c.tools.JiontDAO;
import cn.explink.b2c.tools.JointService;
import cn.explink.dao.CustomWareHouseDAO;
import cn.explink.dao.CustomerDAO;
import cn.explink.dao.CwbDAO;
import cn.explink.dao.DeliveryStateDAO;
import cn.explink.dao.OrderFlowDAO;
import cn.explink.domain.CustomWareHouse;
import cn.explink.domain.CwbOrder;
import cn.explink.enumutil.CwbOrderTypeIdEnum;
import cn.explink.pos.tools.JacksonMapper;
import cn.explink.util.DateTimeUtil;
import cn.explink.util.WebServiceHandler;

/**
 * 配送,上门换类型订单
 * 
 * @author Administrator
 *
 */
@Service
public class HaoXiangGouService_PeiSong extends HaoXiangGouService {
	private Logger logger = LoggerFactory.getLogger(HaoXiangGouService_PeiSong.class);

	@Autowired
	JiontDAO jiontDAO;
	@Autowired
	CustomerDAO customerDAO;
	@Autowired
	OrderFlowDAO orderFlowDAO;
	@Autowired
	ExplinkService explinkService;
	@Autowired
	CwbDAO cwbDAO;

	@Autowired
	DeliveryStateDAO deliveryStateDAO;
	@Autowired
	CustomWareHouseDAO customWarehouseDAO;

	@Autowired
	DataImportService_B2c dataImportService_B2c;
	@Autowired
	JointService jointService;

	/**
	 * 订单下载接口 配送 上门换货
	 */
	public long GetOrdWayBillInfoForD2D() {
		HaoXiangGou hxg = getHaoXiangGou(B2cEnum.HaoXiangGou.getKey());
		int isOpenFlag = jointService.getStateForJoint(B2cEnum.HaoXiangGou.getKey());
		if (isOpenFlag == 0) {
			logger.warn("未开启0好享购0获取订单接口");
			return -1;
		}

		try {

			String starttime = DateTimeUtil.getDateBeforeHours(hxg.getSelectHours(), "yyyyMMddHHmmss");
			String endtime = DateTimeUtil.getDateBeforeHours(0, "yyyyMMddHHmmss");

			if (hxg.getIsopentestflag() == 1) {
				starttime = hxg.getStarttime();
				endtime = hxg.getEndtime();
			}
			logger.info("请求好享购参数:starttime={},endtime={},password=" + hxg.getPassword(), starttime, endtime);

			String starttime_DES = DESUtil.encrypt(starttime, hxg.getDes_key());
			String endtime_DES = DESUtil.encrypt(endtime, hxg.getDes_key());

			Object parms[] = { starttime_DES, endtime_DES, hxg.getPassword() };

			String rtn_data_DES = (String) WebServiceHandler.invokeWs(hxg.getRequst_url(), "GetOrdWayBillInfoForD2D", parms);

			if (rtn_data_DES == null) {
				logger.info("当前没有下载到好享购的数据:starttime={},endtime={}", starttime, endtime);
				return 0;
			}

			String rtn_data = DESUtil.decrypt(rtn_data_DES, hxg.getDes_key()); // DES解密，JSON格式字符串

			logger.info("请求好享购返回订单信息:rtn_data={},password={}", rtn_data, hxg.getPassword());

			List<Map<String, Object>> datalist = parseOrderListfromJSON(rtn_data); // JSON转化为集合

			if (datalist == null || datalist.size() == 0) {
				logger.info("当前没有下载到待处理的订单0好享购0-配送单");
				return 0;
			}

			List<Map<String, String>> orderlist = getPamarsListByDataList(datalist, hxg);

			dataImportService_B2c.Analizy_DataDealByB2c(Long.valueOf(hxg.getCustomerids()), B2cEnum.HaoXiangGou.getMethod(), orderlist, hxg.getWarehouseid(), true);

			logger.info("处理好享购-配送导入后的订单信息成功,starttime={},endtime={}", starttime, endtime);

			return orderlist.size();
		} catch (Exception e) {
			logger.error("调用0好享购0webservice服务器异常" + e.getMessage(), e);
			return 0;
		}

	}

	private List<Map<String, Object>> parseOrderListfromJSON(String json) {
		try {
			return JacksonMapper.getInstance().readValue(json, List.class);
		} catch (Exception e) {
			logger.error("rtn_data转化为List<Map<String, Object>>发生异常", e);
			return null;
		}

	}

	public List<Map<String, String>> getPamarsListByDataList(List<Map<String, Object>> datalist, HaoXiangGou hxg) throws Exception {
		List<Map<String, String>> resporderlist = new ArrayList<Map<String, String>>();

		for (Map<String, Object> dataMap : datalist) {
			Map<String, String> dataMapresp = buildOrderMap(hxg, dataMap);
			if (dataMapresp != null) {
				resporderlist.add(dataMapresp);
			}
		}
		return resporderlist;
	}

	private Map<String, String> buildOrderMap(HaoXiangGou hxg, Map<String, Object> dataMap) {
		Map<String, String> dataMapresp = new HashMap<String, String>();
		String invcId = getParamsString(dataMap, "invcId"); // 运单号
		String ordId = getParamsString(dataMap, "ordId"); // 订单号
		String orgOrdId = getParamsString(dataMap, "orgOrdId"); // 原始订单号
		String rcverName = getParamsString(dataMap, "rcverName"); // 收货人姓名
		String rcvAddr = getParamsString(dataMap, "rcvAddr"); // 收货地址
		String codAmt = getParamsforIntStr(dataMap, "codAmt"); // 代收货款
		String goodName = getParamsString(dataMap, "goodName"); // 商品名称
		String goodQty = getParamsforIntStr(dataMap, "goodQty"); // 件数
		String ordType = getParamsString(dataMap, "ordType"); // 订单类型 01：正常交货
																// 03：换货
		String siteNo = getParamsString(dataMap, "siteNo"); // 仓库号
		String goodClass = getParamsString(dataMap, "goodClass"); // 商品类型 01:普品
																	// 02：赠品
																	// 03：贵品
		String delverCD = getParamsString(dataMap, "delverCD"); // 配送公司

		CwbOrder cwbOrder = cwbDAO.getCwbByCwb(invcId);
		if (cwbOrder != null) {
			logger.warn("获取0好享购0订单中含有重复数据cwb={}", invcId);
			return null;
		}
		if (!customWarehouseDAO.isExistsWarehouFlag(siteNo, hxg.getCustomerids())) {
			CustomWareHouse custwarehouse = new CustomWareHouse();
			custwarehouse.setCustomerid(Long.valueOf(hxg.getCustomerids()));
			custwarehouse.setCustomerwarehouse(siteNo);
			custwarehouse.setWarehouse_no(siteNo);
			custwarehouse.setWarehouseremark("");
			custwarehouse.setIfeffectflag(1);
			customWarehouseDAO.creCustomer(custwarehouse);
		}
		String warhouseid = dataImportService_B2c.getCustomerWarehouseNo(siteNo, hxg.getCustomerids());
		dataMapresp.put("cwb", invcId);
		dataMapresp.put("transcwb", ordId);
		dataMapresp.put("consigneename", rcverName);
		dataMapresp.put("consigneeaddress", rcvAddr);
		dataMapresp.put("receivablefee", codAmt); // 代收款
		dataMapresp.put("paybackfee", "0"); // 应退款
		dataMapresp.put("sendcarname", goodName); // 发货商品
		dataMapresp.put("sendcarnum", goodQty); // 发货 件数
		dataMapresp.put("backcargoname", "");
		dataMapresp.put("backcargonum", "0");
		dataMapresp.put("cwbordertypeid", "01".equals(ordType) ? CwbOrderTypeIdEnum.Peisong.getValue() + "" : CwbOrderTypeIdEnum.Shangmenhuan.getValue() + ""); // 订单类型
		dataMapresp.put("customerwarehouseid", warhouseid); // 发货仓库
		dataMapresp.put("cargotype", getGoodClassType(goodClass)); // 货物类别
		dataMapresp.put("customerid", hxg.getCustomerids()); // customerid
		dataMapresp.put("cwbremark", (!orgOrdId.equals("") ? ("原始订单号:" + orgOrdId) : "") + " " + delverCD); // 备注
		dataMapresp.put("consigneephone", "");
		dataMapresp.put("shipcwb", ""); // 退货号
		dataMapresp.put("remark1", ""); // 备注

		return dataMapresp;
	}

	private String getGoodClassType(String goodClass) {
		if (goodClass.contains("01")) {
			return "普品";
		}
		if (goodClass.contains("02")) {
			return "赠品";
		}
		if (goodClass.contains("03")) {
			return "贵品";
		}
		return "普品";
	}

	public String getParamsString(Map<String, Object> dataMap, String params) {
		return dataMap.get(params) != null ? dataMap.get(params).toString() : "";
	}

	public String getParamsforIntStr(Map<String, Object> dataMap, String params) {
		return dataMap.get(params) != null ? dataMap.get(params).toString() : "0";
	}

}

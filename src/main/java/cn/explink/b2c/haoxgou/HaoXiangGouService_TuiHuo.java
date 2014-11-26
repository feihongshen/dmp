package cn.explink.b2c.haoxgou;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
import cn.explink.domain.DeliveryState;
import cn.explink.enumutil.CwbOrderTypeIdEnum;
import cn.explink.enumutil.DeliveryStateEnum;
import cn.explink.pos.tools.JacksonMapper;
import cn.explink.util.DateTimeUtil;
import cn.explink.util.WebServiceHandler;

/**
 * 获取订单，退货单
 * 
 * @author Administrator
 *
 */
@Service
public class HaoXiangGouService_TuiHuo extends HaoXiangGouService {
	private Logger logger = LoggerFactory.getLogger(HaoXiangGouService_TuiHuo.class);

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
	 * 订单下载接口 上门取件单
	 * */
	public long GetRtnWayBillInfoForD2D() {
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

			starttime = DESUtil.encrypt(starttime, hxg.getDes_key());
			endtime = DESUtil.encrypt(endtime, hxg.getDes_key());

			Object parms[] = { starttime, endtime, hxg.getPassword() };
			String rtn_data_DES = (String) WebServiceHandler.invokeWs(hxg.getRequst_url(), "GetRtnWayBillInfoForD2D", parms);

			String rtn_data = DESUtil.decrypt(rtn_data_DES, hxg.getDes_key()); // DES解密，JSON格式字符串

			logger.info("请求好享购返回订单信息(上门退):rtn_data={},password={}", rtn_data, hxg.getPassword());

			List<Map<String, Object>> datalist = parseOrderListfromJSON(rtn_data); // JSON转化为集合

			if (datalist == null || datalist.size() == 0) {
				logger.info("当前没有下载到待处理的订单0好享购0-上门退");
				return 0;
			}

			List<Map<String, String>> orderlist = getPamarsListByDataList(datalist, hxg);

			dataImportService_B2c.Analizy_DataDealByB2c(Long.valueOf(hxg.getCustomerids()), B2cEnum.HaoXiangGou.getMethod(), orderlist, hxg.getWarehouseid(), true);

			logger.info("处理好享购-上门退导入后的订单信息成功,starttime={},endtime={}", starttime, endtime);

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
			e.printStackTrace();
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

		String invcId_str = getParamsString(dataMap, "invcId"); // 运单号

		String ordId = getParamsString(dataMap, "ordId"); // 订单号
		String rtnId = getParamsString(dataMap, "rtnId"); // 退货号
		String ninvcId = getParamsString(dataMap, "ninvcId"); // 换货新运单号
		String rcverName = getParamsString(dataMap, "rcverName"); // 收货人姓名
		String rcvAddr = getParamsString(dataMap, "rcvAddr"); // 收货地址
		String rcvTel = getParamsString(dataMap, "rcvTel"); // 收货人电话
		String rtnAmt = getParamsforIntStr(dataMap, "rtnAmt"); // 代退货款
		String goodName = getParamsString(dataMap, "goodName"); // 商品名称
		String goodQty = getParamsString(dataMap, "goodQty"); // 件数
		String siteNo = getParamsString(dataMap, "siteNo"); // 仓库号
		String goodClass = getParamsString(dataMap, "goodClass"); // 商品类型 01:普品
																	// 02：赠品
																	// 03：贵品
		String rtnType = getParamsString(dataMap, "rtnType"); // 退出类型：01：退货不退款
																// 02：退货退款 03:
																// 换货 04：拒收
		String delverCD = getParamsString(dataMap, "delverCD"); // 配送公司

		String rtnTypeStr = getReturnType(rtnType); // 退货要求
		String cwbremark = delverCD + " " + rtnTypeStr + ",退货号:" + rtnId;

		CwbOrder cwbOrder = cwbDAO.getCwbByCwb(invcId);
		if (cwbOrder != null) {
			invcId = IfTuiHuoDanDealWithFlag(invcId, rtnId, goodName, goodClass, rtnType, rtnTypeStr);
			if (invcId == null) {
				return null;
			}
		} else {
			if ("04".equals(rtnType) || "03".equals(rtnType)) { // 排除拒收和换货
				logger.warn("非上门退货类型订单无法插入数据库中,接口=v2.5,cwb={}", invcId);
				return null;
			} else {
				invcId = rtnId;
				logger.info("获取到好享购退货单(历史无配送)需新增，退货号={}", invcId);
			}
		}

		String cwbremark1 = "原运单号:" + invcId_str + ",原订单号:" + ordId; // 存入上门退的原始运单号和订单号,状态反馈的时候会调用

		dataMapresp.put("cwb", invcId);
		dataMapresp.put("transcwb", ordId);
		dataMapresp.put("consigneename", rcverName);
		dataMapresp.put("consigneeaddress", rcvAddr);
		dataMapresp.put("consigneephone", rcvTel);
		dataMapresp.put("receivablefee", "0"); // 代收款
		dataMapresp.put("paybackfee", rtnAmt);
		dataMapresp.put("sendcarname", ""); // 发货商品
		dataMapresp.put("sendcarnum", "0"); // 发货 件数
		dataMapresp.put("backcargoname", goodName);
		dataMapresp.put("backcargonum", goodQty);
		dataMapresp.put("cwbordertypeid", CwbOrderTypeIdEnum.Shangmentui.getValue() + ""); // 订单类型
		dataMapresp.put("customerwarehouseid", getWarehouseId(hxg, siteNo)); // 发货仓库
		dataMapresp.put("cargotype", getGoodClassType(goodClass)); // 货物类别
		dataMapresp.put("customerid", hxg.getCustomerids()); // customerid
		dataMapresp.put("cwbremark", cwbremark); // 备注
		dataMapresp.put("shipcwb", ordId); // 退货号
		dataMapresp.put("remark1", cwbremark1);

		return dataMapresp;
	}

	private String IfTuiHuoDanDealWithFlag(String invcId, String rtnId, String goodName, String goodClass, String rtnType, String rtnTypeStr) {
		logger.warn("获取0好享购0订单中含有重复数据cwb={}，该数据为退货指令插入或修改", invcId);
		DeliveryState delivery = deliveryStateDAO.getActiveDeliveryStateByCwb(invcId);

		if (delivery == null) {
			logger.warn("获取好享购2.5接口订单号={}状态无效,历史状态未配送完毕,过滤...", invcId);
			return null;
		}
		long deliverystate = delivery.getDeliverystate();
		// /如果退货指令中，原始单号是配送成功，则现在单号就 插入，退货号 rtnId 转化为cwb插入数据库.
		if ((deliverystate == DeliveryStateEnum.PeiSongChengGong.getValue()) && !"04".equals(rtnType) && !"03".equals(rtnType)) {
			invcId = rtnId;
			logger.info("获取到好享购退货单(历史状态为签收)，退货号={}", invcId);
			return invcId;
		}

		if (deliverystate == DeliveryStateEnum.JuShou.getValue() && "04".equals(rtnType)) { // 如果是拒收的话，则追加，修改备注等操作。
			String remarkAppend = "退货指令,取回商品:" + goodName + " " + getGoodClassType(goodClass) + " " + rtnTypeStr;
			logger.info("获取到好享购退货单(历史状态为拒收,需追加备注)，退货号={}", invcId);
			CwbOrder co = cwbDAO.getCwbByCwb(invcId);

			String oldcwbremark = co.getCwbremark().length() > 0 ? co.getCwbremark() + "\n" : "";
			// 修改人为admin，加上了备注的修改日期
			String newcwbremark = oldcwbremark + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + "[admin]" + remarkAppend;
			cwbDAO.updateCwbRemark(invcId, newcwbremark);
			return null;
		}

		return null;

	}

	private String getWarehouseId(HaoXiangGou hxg, String siteNo) {
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
		return warhouseid;
	}

	private String getReturnType(String rtnType) {
		// 01：退货不退款 02：退货退款 03: 换货 04：拒收
		if (rtnType.contains("01")) {
			return "退货不退款";
		}
		if (rtnType.contains("02")) {
			return "退货退款";
		}
		if (rtnType.contains("03")) {
			return "换货";
		}
		return "拒收";
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

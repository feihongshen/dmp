package cn.explink.b2c.maisike;

/**
 * 发送二级站方法
 */
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.type.TypeReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.explink.b2c.explink.xmldto.OrderDto;
import cn.explink.b2c.maisike.sendback_json.OrderReturn;
import cn.explink.b2c.maisike.sendback_json.OrderReturnShell;
import cn.explink.b2c.maisike.senddata_json.Order;
import cn.explink.b2c.maisike.senddata_json.OrderPackage;
import cn.explink.b2c.maisike.senddata_json.OrderShell;
import cn.explink.b2c.maisike.senddata_json.OrderStatus;
import cn.explink.b2c.maisike.senddata_json.RespOrder;
import cn.explink.b2c.tools.B2cDataOrderFlowDetail;
import cn.explink.b2c.tools.B2cEnum;
import cn.explink.b2c.tools.B2cTools;
import cn.explink.b2c.tools.JacksonMapper;
import cn.explink.dao.GetDmpDAO;
import cn.explink.dao.WarehouseCommenDAO;
import cn.explink.domain.Branch;
import cn.explink.domain.WarehouseToCommen;
import cn.explink.enumutil.CwbOrderTypeIdEnum;
import cn.explink.enumutil.PaytypeEnum;
import cn.explink.util.DateTimeUtil;
import cn.explink.util.RestHttpServiceHanlder;
import cn.explink.util.MD5.MD5Util;

@Service
public class MaisikeService_Send2LvBranch {
	private Logger logger = LoggerFactory.getLogger(MaisikeService_Send2LvBranch.class);
	@Autowired
	private GetDmpDAO getDmpDAO;
	@Autowired
	B2cDataOrderFlowDetail orderFlowDetail;
	@Autowired
	private B2cTools b2ctools;

	@Autowired
	WarehouseCommenDAO WarehouseCommenDAO;

	public Maisike getSmileSettingMethod(int key) {
		Maisike maisike = null;
		if (b2ctools.getObjectMethod(key) != null) {
			JSONObject jsonObj = JSONObject.fromObject(b2ctools.getObjectMethod(key).getJoint_property());
			maisike = (Maisike) JSONObject.toBean(jsonObj, Maisike.class);
		} else {
			maisike = new Maisike();
		}
		return maisike;
	}

	/**
	 * 推送出库二级站信息
	 */
	public long sendTwoLeavelBranch() {
		long calcCount = 0;
		int b2cenum = B2cEnum.Maisike.getKey();

		if (!b2ctools.isB2cOpen(b2cenum)) {
			logger.info("未开出库0迈思可0的对接!");
			return -1;
		}
		Maisike maisike = getSmileSettingMethod(b2cenum);

		long maxCount = maisike.getMaxCount();

		for (int i = 0; i < 20; i++) {

			try {
				long loopcount = maisike.getLoopcount() == 0 ? 5 : maisike.getLoopcount(); // 重发次数5

				List<WarehouseToCommen> datalist = WarehouseCommenDAO.getCommenCwbListByCommon(b2cenum + "", maxCount, loopcount); // 查询所有未推送数据
				if (datalist == null || datalist.size() == 0) {
					logger.info("当前没有待发送下游的数据-迈思可");
					return 0;
				}
				calcCount += datalist.size();
				String requestCwbs = getRequestDMPCwbArrs(datalist); // 封装为-上游oms请求dmp的参数.
				String responseJson = getDmpDAO.getDMPOrdersByCwbs(requestCwbs); // 根据订单号批量
																					// 请求dmp，返回订单集合

				List<OrderDto> respOrders = JacksonMapper.getInstance().readValue(responseJson, new TypeReference<List<OrderDto>>() {
				});

				if (respOrders == null || respOrders.size() == 0) {
					logger.info("当前没有待发送迈思可数据");
					return 0;
				}

				excuteSendMaisike_forward(maisike, datalist, respOrders); // 正向物流
																			// （配送）

				excuteSendMaisike_backing(maisike, datalist, respOrders); // 逆向物流
																			// （上门退换）

			} catch (Exception e) {
				logger.error("处理发送迈思可数据发生未知异常", e);
			}
		}

		return calcCount;

	}

	/**
	 * 执行配送类型订单的推送 正向物流 （包括配送）
	 * 
	 * @param maisike
	 * @param datalist
	 * @param respOrders
	 * @throws UnsupportedEncodingException
	 * @throws IOException
	 * @throws JsonGenerationException
	 * @throws JsonMappingException
	 * @throws JsonParseException
	 */
	private void excuteSendMaisike_forward(Maisike maisike, List<WarehouseToCommen> datalist, List<OrderDto> respOrders) {
		try {

			String fn = "order.arrival.set";

			List<Order> orders = buildOutLv2BranchList_forward(respOrders, datalist); // 构建请求List
			if (orders == null || orders.size() == 0) {
				logger.info("正向-当前没有推送迈思可订单信息");
				return;
			}
			OrderShell orderShell = new OrderShell();
			orderShell.setOrders(orders);

			String appdata = JacksonMapper.getInstance().writeValueAsString(orderShell); // 转化JSON
																							// 字符串

			Map<String, String> params = buildRequestMaps(maisike, appdata, fn); // 构建请求的参数

			String responseData = RestHttpServiceHanlder.sendHttptoServer(params, maisike.getSend_url()); // 请求并返回

			logger.info("正向-迈思可返回信息={}", responseData);

			dealwithResponse(datalist, appdata, responseData); // 返回处理
		} catch (Exception e) {
			logger.error("处理发送迈思可正向物流异常", e);
		}

	}

	/**
	 * 执行配送类型订单的推送 逆向物流 （包括上门退换货）
	 * 
	 * @param maisike
	 * @param datalist
	 * @param respOrders
	 * @throws UnsupportedEncodingException
	 * @throws IOException
	 * @throws JsonGenerationException
	 * @throws JsonMappingException
	 * @throws JsonParseException
	 */
	private void excuteSendMaisike_backing(Maisike maisike, List<WarehouseToCommen> datalist, List<OrderDto> respOrders) {
		try {

			String fn = "order.return.set";

			List<OrderReturn> orders = buildOutLv2BranchList_backing(respOrders, datalist); // 构建请求List
			if (orders == null || orders.size() == 0) {
				logger.info("逆向-当前没有推送迈思可订单信息");
				return;
			}

			OrderReturnShell orderReturnShell = new OrderReturnShell();
			orderReturnShell.setOrders(orders);

			String appdata = JacksonMapper.getInstance().writeValueAsString(orderReturnShell); // 转化JSON
																								// 字符串

			Map<String, String> params = buildRequestMaps(maisike, appdata, fn); // 构建请求的参数

			String responseData = RestHttpServiceHanlder.sendHttptoServer(params, maisike.getSend_url()); // 请求并返回

			logger.info("逆向-迈思可返回信息={}", responseData);

			dealwithResponse(datalist, appdata, responseData); // 返回处理
		} catch (Exception e) {
			logger.error("处理发送迈思可逆向物流异常", e);
		}

	}

	/**
	 * 处理迈思可返回的信息节点
	 * 
	 * @param datalist
	 * @param appdata
	 * @param responseData
	 * @throws IOException
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 */
	private void dealwithResponse(List<WarehouseToCommen> datalist, String appdata, String responseData) throws IOException, JsonParseException, JsonMappingException {

		if (responseData == null || responseData.isEmpty()) {
			logger.info("推送迈思可订单信息返回空,原请求data={}", appdata);
			for (WarehouseToCommen wcomm : datalist) {
				WarehouseCommenDAO.updateCommenCwbListBycwb(wcomm.getCwb(), "2", "请求返回空");
			}

			// continue;
		}

		RespOrder respOrder = JacksonMapper.getInstance().readValue(responseData, RespOrder.class); // 解析

		if (!respOrder.getCode().equals(MaisikeExpEmum.Success.getErrCode())) {
			logger.info("推送迈思可订单信息-返回未知异常,code={},msg={}", respOrder.getCode(), respOrder.getMsg());
			for (WarehouseToCommen wcomm : datalist) {
				WarehouseCommenDAO.updateCommenCwbListBycwb(wcomm.getCwb(), "2", "code返回失败");
			}
			// continue;
		}

		// 处理返回结果
		List<String> successList = respOrder.getOsnlist();
		List<String> failList = respOrder.getErr_osnlist();
		if (successList != null && successList.size() > 0) {
			for (String cwb : successList) {
				WarehouseCommenDAO.updateCommenCwbListBycwb(cwb, DateTimeUtil.getNowTime(), "成功");
			}
		}
		if (failList != null && failList.size() > 0) {
			for (String cwb : failList) {
				WarehouseCommenDAO.updateCommenCwbListBycwb(cwb, "2", "失败");
			}
		}
	}

	private Map<String, String> buildRequestMaps(Maisike maisike, String appdata, String fn) throws UnsupportedEncodingException {
		Map<String, String> params = new HashMap<String, String>();

		String appname = maisike.getAppname();
		String apptime = DateTimeUtil.getNowTime();
		String appkey = MD5Util.md5(appname + maisike.getApp_key() + apptime); // 签名

		params.put("fn", fn);
		params.put("appname", appname);
		params.put("apptime", apptime);
		params.put("appkey", appkey);
		// params.put("appdata",URLEncoder.encode(appdata,"UTF-8") );
		params.put("appdata", appdata);
		return params;
	}

	private int getBranchById(long bindBranchid, List<Branch> branchlist) {
		for (Branch branch : branchlist) {
			if (branch.getBranchid() == bindBranchid) {
				return branch.getBindmsksid();
			}
		}
		return 0;
	}

	private String getBranchNameById(long branchid, List<Branch> branchlist) {
		for (Branch branch : branchlist) {
			if (branch.getBranchid() == branchid) {
				return branch.getBranchname();
			}
		}
		return "未知";
	}

	private List<Order> buildOutLv2BranchList_forward(List<OrderDto> respOrders, List<WarehouseToCommen> commondataList) throws UnsupportedEncodingException {

		List<Order> orderList = new ArrayList<Order>();
		List<Branch> branchlist = getDmpDAO.getAllBranchs();
		for (OrderDto ot : respOrders) {
			if (ot.getCwbordertypeid() == CwbOrderTypeIdEnum.Peisong.getValue()) { // 只限于操作正向配送的订单数据
				try {
					Order order = new Order();
					order.setOsn(ot.getCwb());
					order.setSid(getMaisikesid(commondataList, branchlist, ot)); // 迈思可站点唯一标识
					order.setUname(URLEncoder.encode(ot.getConsigneename(), "UTF-8"));
					String phone = (ot.getConsigneemobile() == null ? "" : ot.getConsigneemobile().trim()) + " " + (ot.getConsigneephone() == null ? "" : ot.getConsigneephone().trim());
					phone = phone.length() > 1 ? phone : "0";
					order.setUphone(phone);
					order.setOarea(URLEncoder.encode(ot.getCwbprovince() + "-" + ot.getCwbcity() + "-" + ot.getCwbcounty(), "UTF-8"));
					order.setOaddress(URLEncoder.encode(ot.getConsigneeaddress(), "UTF-8"));
					order.setOweight(ot.getCargorealweight().floatValue());
					order.setOpaytype(getPayType(ot.getPaywayid())); // 支付方式
																		// 需确认后再修改
					order.setOpaymoney(ot.getCargoamount().floatValue());
					order.setOischarge(ot.getReceivablefee().floatValue() > 0 ? 1 : 0);
					order.setOchargemoney(ot.getReceivablefee().floatValue());
					order.setOtime(DateTimeUtil.getNowTime());

					order.setOpackagelist(getOrderPackageList(ot)); // 订单包裹集合

					order.setOstatuslist(buildOrderStatus(commondataList, branchlist, ot)); // 订单流转集合
					orderList.add(order);
				} catch (Exception e) {
					logger.error("构建单个对象异常cwb=" + ot.getCwb(), e);
				}
			}

		}

		return orderList;

	}

	private List<OrderReturn> buildOutLv2BranchList_backing(List<OrderDto> respOrders, List<WarehouseToCommen> commondataList) throws UnsupportedEncodingException {

		List<OrderReturn> orderList = new ArrayList<OrderReturn>();
		List<Branch> branchlist = getDmpDAO.getAllBranchs();
		for (OrderDto ot : respOrders) {
			if (ot.getCwbordertypeid() == CwbOrderTypeIdEnum.Peisong.getValue()) { // 只支持逆向物流
																					// -上门退换货的数据构建。
				continue;
			}
			try {
				OrderReturn orderReturn = new OrderReturn();
				orderReturn.setOsn(ot.getCwb());
				orderReturn.setSid(getMaisikesid(commondataList, branchlist, ot)); // 迈思可站点唯一标识
				orderReturn.setUname(URLEncoder.encode(ot.getConsigneename() == null ? "未知" : ot.getConsigneename(), "UTF-8"));
				String phone = (ot.getConsigneemobile() == null ? "" : ot.getConsigneemobile().trim()) + " " + (ot.getConsigneephone() == null ? "" : ot.getConsigneephone().trim());
				phone = phone.length() > 1 ? phone : "0";
				orderReturn.setUphone(phone);
				orderReturn.setOarea(URLEncoder.encode(ot.getCwbprovince() + "-" + ot.getCwbcity() + "-" + ot.getCwbcounty(), "UTF-8"));
				orderReturn.setOaddress(URLEncoder.encode(ot.getConsigneeaddress(), "UTF-8"));
				orderReturn.setOweight(ot.getCargorealweight().floatValue());

				orderReturn.setOpaytype(getPayType(ot.getPaywayid())); // 支付方式
																		// 需确认后再修改
				orderReturn.setOpaymoney(ot.getCargoamount().floatValue());
				orderReturn.setOischarge(ot.getReceivablefee().floatValue() > 0 ? 1 : 0);
				orderReturn.setOchargemoney(ot.getReceivablefee().floatValue());
				orderReturn.setChgosn(""); // 换货单号暂无

				orderReturn.setReturnproname(URLEncoder.encode(ot.getBackcargoname() == null || ot.getBackcargoname().isEmpty() ? "未知" : ot.getBackcargoname(), "UTF-8"));
				orderReturn.setReturnpronum(ot.getBackcargonum() == 0 ? 1 : ot.getBackcargonum());
				orderReturn.setReturnpaytype(ot.getPaybackfee().doubleValue() > 0 ? MskRetPayTypeEmum.CASH.toString() : MskRetPayTypeEmum.NOPAY.toString());
				orderReturn.setIsvisit(0); // 默认全部非上门
				orderReturn.setReturntime(DateTimeUtil.getNowTime());

				orderReturn.setOpackagelist(getOrderPackageList(ot)); // 订单包裹集合
				orderReturn.setOstatuslist(buildOrderStatus(commondataList, branchlist, ot)); // 订单流转集合

				String returntype = ot.getCwbordertypeid() == CwbOrderTypeIdEnum.Shangmenhuan.getValue() ? "REPLACE" : "RETUEN"; // 退货货类型
				orderReturn.setReturntype(returntype);

				orderList.add(orderReturn);
			} catch (Exception e) {
				logger.error("构建单个对象异常cwb=" + ot.getCwb(), e);
			}

		}

		return orderList;

	}

	private List<OrderStatus> buildOrderStatus(List<WarehouseToCommen> commondataList, List<Branch> branchlist, OrderDto ot) throws UnsupportedEncodingException {
		List<OrderStatus> ostatuslist = new ArrayList<OrderStatus>();
		OrderStatus orderStatus = new OrderStatus();
		orderStatus.setOstatus(URLEncoder.encode("站点出库-已发货", "UTF-8"));
		orderStatus.setOtime(DateTimeUtil.getNowTime());
		orderStatus.setOremark(URLEncoder.encode(getOremark(commondataList, branchlist, ot), "UTF-8"));
		ostatuslist.add(orderStatus);
		return ostatuslist;
	}

	/**
	 * 获取迈思可的订单流转信息
	 * 
	 * @param commondataList
	 * @param branchlist
	 * @param ot
	 * @return
	 */
	private String getOremark(List<WarehouseToCommen> commondataList, List<Branch> branchlist, OrderDto ot) {
		for (WarehouseToCommen tocom : commondataList) {
			if (tocom.getCwb().equals(ot.getCwb())) {
				String startbranch = getBranchNameById(tocom.getStartbranchid(), branchlist); // 查询迈思可站点信息的id
				String nextbranch = getBranchNameById(tocom.getNextbranchid(), branchlist); // 查询迈思可站点信息的id
				String remark = "货物由一级站[" + startbranch + "]出库至二级站[" + nextbranch + "]";
				return remark;
			}
		}
		return "";
	}

	private List<OrderPackage> getOrderPackageList(OrderDto ot) {
		List<OrderPackage> opackagelist = new ArrayList<OrderPackage>();
		String PsnCwbs = "";
		if (ot.getTranscwb() == null || ot.getTranscwb().isEmpty()) {
			PsnCwbs = ot.getCwb();
		} else {
			PsnCwbs = ot.getTranscwb();
		}
		for (String psn : PsnCwbs.split(",")) {
			OrderPackage orderPackage = new OrderPackage();
			orderPackage.setPsn(psn);
			opackagelist.add(orderPackage);
		}
		return opackagelist;
	}

	private int getMaisikesid(List<WarehouseToCommen> commondataList, List<Branch> branchlist, OrderDto ot) {
		for (WarehouseToCommen tocom : commondataList) {
			if (tocom.getCwb().equals(ot.getCwb())) {
				long bindmskid = getBranchById(tocom.getNextbranchid(), branchlist); // 查询迈思可站点信息的id
				Stores stores = getDmpDAO.getStoresById(bindmskid);
				return Integer.valueOf(stores.getSid());
			}
		}
		return 0;
	}

	private String getPayType(long paytype) {
		if (paytype == PaytypeEnum.Xianjin.getValue()) {
			return "DELIVERY";
		}
		if (paytype == PaytypeEnum.Pos.getValue()) {
			return "DELIVERY";
		}
		return "";
	}

	private String getRequestDMPCwbArrs(List<WarehouseToCommen> datalist) {
		JSONArray jsonArr = new JSONArray();
		for (WarehouseToCommen common : datalist) {
			JSONObject jsonobj = new JSONObject();
			jsonobj.put("cwb", common.getCwb());
			jsonArr.add(jsonobj);
		}
		String requestCwbs = jsonArr.toString();
		return requestCwbs;
	}

}

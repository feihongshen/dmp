package cn.explink.b2c.smiled;

/**
 * 发送二级站方法
 */
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
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
import cn.explink.b2c.maisike.Maisike;
import cn.explink.b2c.maisike.MaisikeExpEmum;
import cn.explink.b2c.maisike.MskRetPayTypeEmum;
import cn.explink.b2c.maisike.Stores;
import cn.explink.b2c.maisike.sendback_json.OrderReturn;
import cn.explink.b2c.maisike.senddata_json.Order;
import cn.explink.b2c.maisike.senddata_json.OrderPackage;
import cn.explink.b2c.maisike.senddata_json.OrderStatus;
import cn.explink.b2c.maisike.senddata_json.RespOrder;
import cn.explink.b2c.smiled.xmldto.SmiledOrder;
import cn.explink.b2c.smiled.xmldto.SmiledResponse;
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
public class SmiledService_SendBranch {
	private Logger logger = LoggerFactory.getLogger(SmiledService_SendBranch.class);
	@Autowired
	private GetDmpDAO getDmpDAO;
	@Autowired
	B2cDataOrderFlowDetail orderFlowDetail;
	@Autowired
	private B2cTools b2ctools;

	@Autowired
	WarehouseCommenDAO WarehouseCommenDAO;

	public Smiled getSmileSettingMethod(int key) {
		Smiled maisike = null;
		if (b2ctools.getObjectMethod(key) != null) {
			JSONObject jsonObj = JSONObject.fromObject(b2ctools.getObjectMethod(key).getJoint_property());
			maisike = (Smiled) JSONObject.toBean(jsonObj, Smiled.class);
		} else {
			maisike = new Smiled();
		}
		return maisike;
	}

	/**
	 * 推送出库二级站信息
	 */
	public void sendNextBranch() {
		int b2cenum = B2cEnum.Smiled.getKey();

		if (!b2ctools.isB2cOpen(b2cenum)) {
			logger.info("未开出库0思迈下游0的对接!");
			return;
		}
		Smiled smiled = getSmileSettingMethod(b2cenum);

		long maxCount = smiled.getMaxCount();

		long loopcount = smiled.getLoopcount();

		for (int i = 0; i < loopcount; i++) {

			try {

				long resendcount = smiled.getResendcount() == 0 ? 2 : smiled.getResendcount();

				List<WarehouseToCommen> datalist = WarehouseCommenDAO.getCommenCwbListByCommon(b2cenum + "", maxCount, resendcount); // 查询所有未推送数据
				if (datalist == null || datalist.size() == 0) {
					logger.info("当前没有待发送下游的数据-思迈下游");
					return;
				}

				String requestCwbs = getRequestDMPCwbArrs(datalist); // 封装为-上游oms请求dmp的参数.
				String responseJson = getDmpDAO.getDMPOrdersByCwbs(requestCwbs); // 根据订单号批量
																					// 请求dmp，返回订单集合

				List<OrderDto> respOrders = JacksonMapper.getInstance().readValue(responseJson, new TypeReference<List<OrderDto>>() {
				});

				if (respOrders == null || respOrders.size() == 0) {
					logger.info("当前没有待发送思迈下游数据");
					return;
				}

				excuteSendMaisike_forward(smiled, datalist, respOrders); // 正向物流
																			// （配送）

			} catch (Exception e) {
				logger.error("处理发送思迈下游数据发生未知异常", e);
			}
		}

	}

	/**
	 * 执行配送类型订单的推送 正向物流 （包括配送）
	 * 
	 * @param smiled
	 * @param datalist
	 * @param respOrders
	 * @throws UnsupportedEncodingException
	 * @throws IOException
	 * @throws JsonGenerationException
	 * @throws JsonMappingException
	 * @throws JsonParseException
	 */
	private void excuteSendMaisike_forward(Smiled smiled, List<WarehouseToCommen> datalist, List<OrderDto> respOrders) {

		// List<Branch> branchlist=getDmpDAO.getAllBranchs();

		for (OrderDto ot : respOrders) {
			SmiledOrder order = new SmiledOrder();
			order.setWorkCode(ot.getCwb());
			order.setSubCode(ot.getTranscwb());
			order.setReplCost(ot.getReceivablefee());
			order.setFreight(BigDecimal.ZERO);
			order.setGoodsNum(ot.getSendcargonum());
			order.setGoodsSize(ot.getCargosize());
			order.setGoodsWeight(ot.getCargorealweight());
			order.setGetPerson(ot.getConsigneename());
			order.setGetPhone(ot.getConsigneemobile());
			order.setPayType(ot.getPaywayid() == PaytypeEnum.Pos.getValue() ? "1" : "0");
			order.setSettlementway("4");
			order.setRefundableAmount(ot.getPaybackfee());
			order.setDeliveryReuir(ot.getCustomercommand());
			order.setCilienStorage("");

			try {

				String requestXML = SmiledUnmarchal.marchal(order);

				logger.info("当前请求思迈下游推送xml={}", requestXML);

				String responseXML = RestHttpServiceHanlder.sendHttptoServer(requestXML, smiled.getFeedback_url());

				logger.info("当前思迈下游返回xml={}", responseXML);

				SmiledResponse response = SmiledUnmarchal.Unmarchal(responseXML);

				String is_success = null;
				if (response.getIs_success().equals("T")) {
					is_success = DateTimeUtil.getNowTime();
				} else {
					is_success = "2";
				}
				WarehouseCommenDAO.updateCommenCwbListBycwb(ot.getCwb(), is_success, response.getError());

			} catch (Exception e) {
				logger.error("推送下游思迈发生未知异常" + ot.getCwb(), e);
			}

		}

	}

	/**
	 * 处理思迈下游返回的信息节点
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
			logger.info("推送思迈下游订单信息返回空,原请求data={}", appdata);
			for (WarehouseToCommen wcomm : datalist) {
				WarehouseCommenDAO.updateCommenCwbListBycwb(wcomm.getCwb(), "2", "请求返回空");
			}

			// continue;
		}

		RespOrder respOrder = JacksonMapper.getInstance().readValue(responseData, RespOrder.class); // 解析

		if (!respOrder.getCode().equals(MaisikeExpEmum.Success.getErrCode())) {
			logger.info("推送思迈下游订单信息-返回未知异常,code={},msg={}", respOrder.getCode(), respOrder.getMsg());
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

	/**
	 * 获取思迈下游的订单流转信息
	 * 
	 * @param commondataList
	 * @param branchlist
	 * @param ot
	 * @return
	 */
	private String getOremark(List<WarehouseToCommen> commondataList, List<Branch> branchlist, OrderDto ot) {
		for (WarehouseToCommen tocom : commondataList) {
			if (tocom.getCwb().equals(ot.getCwb())) {
				String startbranch = getBranchNameById(tocom.getStartbranchid(), branchlist); // 查询思迈下游站点信息的id
				String nextbranch = getBranchNameById(tocom.getNextbranchid(), branchlist); // 查询思迈下游站点信息的id
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
				long bindmskid = getBranchById(tocom.getNextbranchid(), branchlist); // 查询思迈下游站点信息的id
				Stores stores = getDmpDAO.getStoresById(bindmskid);
				return Integer.valueOf(stores.getSid());
			}
		}
		return 0;
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

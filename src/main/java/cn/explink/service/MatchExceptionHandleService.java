package cn.explink.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.explink.dao.BranchDAO;
import cn.explink.dao.CwbDAO;
import cn.explink.dao.OrderFlowDAO;
import cn.explink.dao.UserDAO;
import cn.explink.domain.CwbOrder;
import cn.explink.domain.MatchExceptionOrder;
import cn.explink.domain.User;
import cn.explink.domain.orderflow.OrderFlow;
import cn.explink.enumutil.FlowOrderTypeEnum;
import cn.explink.util.SqlBuilder;

@Service
public class MatchExceptionHandleService {

	@Autowired
	private CwbDAO cwbDAO;

	@Autowired
	private OrderFlowDAO flowDAO;

	@Autowired
	private SecurityContextHolderStrategy securityContextHolderStrategy;

	@Autowired
	private CwbOrderService cwbOrderService;

	@Autowired
	private UserDAO userDAO;

	@Autowired
	private BranchDAO branchDAO;

	@Transactional
	public JSONObject redistributionBranch(String cwb, long newBranchId) {
		CwbOrder cwbOrder = this.queryOrder(cwb);
		if (cwbOrder == null) {
			return this.createResult(false, "订单号[" + cwb + "]不存在", null);
		}
		OrderFlow orderFlow = this.queryOrderCurrentFlow(cwb);
		JSONObject obj = this.validateReDisOrder(cwbOrder, orderFlow);
		if (!obj.getBoolean("successed")) {
			return obj;
		}
		// 删除订单流程.
		this.resetOrderFlow(cwbOrder);
		// 更新订单状态.
		this.updateOrderDeliverBranch(cwbOrder, newBranchId);
		// 插入异常匹配已处理流程.
		this.insertExceptionMatchHandledFlow(cwb);
		// 执行分站到货逻辑.
		this.exeArriveBranch(cwb, newBranchId);
		// 添加订单最后流程时间和是否为超区订单.
		this.fillExtraData(obj, cwbOrder, orderFlow);

		return obj;
	}

	private void fillExtraData(JSONObject obj, CwbOrder cwbOrder, OrderFlow orderFlow) {
		Date todayZeroTime = this.getTodayZeroTime();
		obj.put("today", todayZeroTime.compareTo(orderFlow.getCredate()) <= 0);
		obj.put("outarea", cwbOrder.getOutareaflag() != 0);
		obj.put("message", "处理完成");
	}

	private void exeArriveBranch(String cwb, long newBranchId) {
		User branchMaster = this.getBranchMaster(newBranchId);
		if (branchMaster == null) {
			return;
		}
		this.cwbOrderService.substationGoods(branchMaster, cwb, cwb, 0, 0, "", "", false);
	}

	private User getBranchMaster(long branchId) {
		List<User> userList = this.userDAO.getUserByRole("2,4", branchId);
		if (userList.isEmpty()) {
			return null;
		}
		return userList.get(0);
	}

	private void resetOrderFlow(CwbOrder cwbOrder) {
		if (cwbOrder.getOutareaflag() == 0) {
			return;
		}
		FlowOrderTypeEnum fzdh = FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao;
		FlowOrderTypeEnum dch = FlowOrderTypeEnum.FenZhanDaoHuoYouHuoWuDanSaoMiao;
		// 删除原有流程.
		this.flowDAO.deleteOrderFlow(cwbOrder.getCwb(), fzdh, dch);
		// 设置导入数据流程为当前流程.
		this.flowDAO.setOrderCurrentFlow(cwbOrder.getCwb(), FlowOrderTypeEnum.DaoRuShuJu);
	}

	private void updateOrderDeliverBranch(CwbOrder cwbOrder, long newBranchId) {
		String cwb = cwbOrder.getCwb();
		boolean isOutArea = cwbOrder.getOutareaflag() != 0;
		this.cwbDAO.mehUpdateOutAreaOrderData(cwb, newBranchId, isOutArea);
	}

	private void insertExceptionMatchHandledFlow(String cwb) {
		OrderFlow outAreaHandledFlow = this.createOutAreaHandledFlow(cwb);
		Calendar cal = Calendar.getInstance();
		String strTime = new SimpleDateFormat("yyyy-MM-hh HH:mm:ss").format(cal.getTime());
		this.flowDAO.mehCreOrderFlow(outAreaHandledFlow, strTime);
	}

	private OrderFlow createOutAreaHandledFlow(String cwb) {
		OrderFlow flow = new OrderFlow();
		flow.setCwb(cwb);
		flow.setBranchid(this.getCurrentBranchId());
		flow.setFlowordertype(FlowOrderTypeEnum.YiChangPiPeiYiChuLi.getValue());
		flow.setCredate(new Date());
		flow.setUserid(this.getUserId());
		flow.setIsnow(0);
		flow.setFloworderdetail("");

		return flow;
	}

	private JSONObject validateReDisOrder(CwbOrder order, OrderFlow orderFlow) {
		if (!this.isCurrentBranchOrder(order)) {
			return this.createResult(false, "非本站订单无法操作", this.transfer(order));
		}
		if (!this.isSmtOrder(order)) {
			this.createResult(false, "非上门退订单", this.transfer(order));
		}
		return this.validateOrderFlow(order, orderFlow);
	}

	private boolean isSmtOrder(CwbOrder order) {
		return order.getCwbordertypeid() == 2;
	}

	private boolean isCurrentBranchOrder(CwbOrder order) {
		long orderBranchId = order.getCurrentbranchid();
		long curBranchId = this.getCurrentBranchId();
		if (orderBranchId == curBranchId) {
			return true;
		}
		return false;
	}

	private JSONObject validateOrderFlow(CwbOrder order, OrderFlow orderFlow) {
		FlowOrderTypeEnum flowOrderType = FlowOrderTypeEnum.getText(orderFlow.getFlowordertype());
		if (order.getOutareaflag() == 1) {
			return this.validateOutAreaOrderFlow(order, flowOrderType);
		}
		return this.validateNormalOrderFlow(order, flowOrderType);
	}

	private JSONObject validateNormalOrderFlow(CwbOrder order, FlowOrderTypeEnum flowOrderType) {
		if (FlowOrderTypeEnum.DaoRuShuJu.equals(flowOrderType)) {
			return this.createResult(true, null, null);
		}
		String message = "新单状态为[" + flowOrderType.getText() + "]非[" + FlowOrderTypeEnum.DaoRuShuJu.getText() + "]无法匹配.";
		return this.createResult(false, message, this.transfer(order));
	}

	private MatchExceptionOrder transfer(CwbOrder order) {
		MatchExceptionOrder meo = new MatchExceptionOrder();
		meo.setCwb(order.getCwb());
		meo.setCustomerName(order.getConsigneename());
		meo.setCustomerPhone(order.getConsigneephone());
		meo.setCustomerAddress(order.getConsigneeaddress());
		meo.setMatchBranchName(order.getExcelbranch());
		meo.setReceivedFee(order.getShouldfare().doubleValue());

		return meo;
	}

	private JSONObject validateOutAreaOrderFlow(CwbOrder order, FlowOrderTypeEnum currentFlow) {
		boolean fzdhCond = FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.equals(currentFlow);
		boolean dchCond = FlowOrderTypeEnum.FenZhanDaoHuoYouHuoWuDanSaoMiao.equals(currentFlow);
		if (fzdhCond || dchCond) {
			return this.createResult(true, null, this.queyMatchExceptionOrderOutAreaByCwb(order.getCwb()));
		}
		return this.createFlowWrongResult(order, currentFlow);
	}

	private JSONObject createFlowWrongResult(CwbOrder order, FlowOrderTypeEnum orderFlow) {
		String message = "当前订单流程为[" + orderFlow.getText() + "]无法进行异常匹配处理";
		MatchExceptionOrder meo = this.queyMatchExceptionOrderOutAreaByCwb(order.getCwb());

		return this.createResult(false, message, meo);
	}

	private OrderFlow queryOrderCurrentFlow(String cwb) {
		return this.flowDAO.getOrderCurrentFlowByCwb(cwb);
	}

	private MatchExceptionOrder queyMatchExceptionOrderOutAreaByCwb(String cwb) {
		String sql = this.getMEOutAreaQuerySql(cwb);
		List<MatchExceptionOrder> meoList = this.cwbDAO.queryMatchExceptionOrder(sql, false);
		this.fillMatchExceptionOrder(meoList);

		return meoList.get(0);
	}

	private String getMEOutAreaQuerySql(String cwb) {
		SqlBuilder sql = new SqlBuilder();
		sql.appendSelectPart(this.getSelectCwbInnerJoinFlowPart());
		sql.appendCondition("d.cwb = '" + cwb + "' ");
		sql.appendCondition("d.outareaflag = 1");
		sql.appendCondition("f.flowordertype = 60");
		sql.appendExtraPart("order by f.credate desc");
		sql.appendExtraPart("limit 1");

		return sql.getSql();
	}

	private String getSelectCwbInnerJoinFlowPart() {
		String qryFields = this.getQueryFields();
		return "select " + qryFields + " from express_ops_cwb_detail d inner join express_ops_order_flow f on d.cwb = f.cwb";
	}

	private String getQueryFields() {
		return "f.credate,f.branchid,f.userid,d.cwb,d.deliverybranchid,d.consigneename,d.consigneephone,d.shouldfare,d.consigneeaddress,d.outareaflag";
	}

	private JSONObject createResult(boolean successed, String message, MatchExceptionOrder meo) {
		JSONObject result = new JSONObject();
		result.put("successed", Boolean.valueOf(successed));
		result.put("message", message);
		result.put("meo", meo);

		return result;
	}

	private CwbOrder queryOrder(String cwb) {
		return this.cwbDAO.getCwbByCwb(cwb);
	}

	private long getCurrentBranchId() {
		return this.getSessionUser().getBranchid();
	}

	private long getUserId() {
		return this.getSessionUser().getUserid();
	}

	private User getSessionUser() {
		ExplinkUserDetail userDetail = (ExplinkUserDetail) this.securityContextHolderStrategy.getContext().getAuthentication().getPrincipal();
		return userDetail.getUser();
	}

	private Date getTodayZeroTime() {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);

		return cal.getTime();
	}

	private void fillMatchExceptionOrder(List<MatchExceptionOrder> meoList) {
		if (meoList.isEmpty()) {
			return;
		}
		this.fillBranchName(meoList);
		this.fillUserName(meoList);
	}

	private void fillBranchName(List<MatchExceptionOrder> meoList) {
		Set<Long> branchIdSet = this.getBranchIdSet(meoList);
		if (branchIdSet.isEmpty()) {
			return;
		}
		Map<Long, String> branchNameMap = this.branchDAO.getBranchNameMap(branchIdSet);
		String tmp = null;
		for (MatchExceptionOrder meo : meoList) {
			tmp = branchNameMap.get(meo.getReportOutAreaBranchId());
			if (tmp == null) {
				meo.setReportOutAreaBranchName("");
			} else {
				meo.setReportOutAreaBranchName(tmp);
			}
			tmp = branchNameMap.get(meo.getMatchBranchId());
			if (tmp == null) {
				meo.setMatchBranchName("");
			} else {
				meo.setMatchBranchName(tmp);
			}
		}
	}

	private void fillUserName(List<MatchExceptionOrder> meoList) {
		Set<Long> userIdSet = this.getUserIdSet(meoList);
		Map<Long, String> userNameMap = this.userDAO.getUserNameMap(new ArrayList<Long>(userIdSet));
		for (MatchExceptionOrder meo : meoList) {
			String name = userNameMap.get(meo.getReportOutAreaUserId());
			meo.setReportOutAreaUserName(name == null ? "" : name);
		}
	}

	private Set<Long> getUserIdSet(List<MatchExceptionOrder> meoList) {
		Set<Long> userIdSet = new HashSet<Long>();
		for (MatchExceptionOrder meo : meoList) {
			if (meo.getReportOutAreaUserId() != 0) {
				userIdSet.add(Long.valueOf(meo.getReportOutAreaUserId()));
			}
		}
		return userIdSet;
	}

	private Set<Long> getBranchIdSet(List<MatchExceptionOrder> meoList) {
		Set<Long> branchIdSet = new HashSet<Long>();
		for (MatchExceptionOrder meo : meoList) {
			if (meo.getMatchBranchId() != 0) {
				branchIdSet.add(Long.valueOf(meo.getMatchBranchId()));
			}
			if (meo.getReportOutAreaBranchId() != 0) {
				branchIdSet.add(Long.valueOf(meo.getReportOutAreaBranchId()));
			}
		}
		return branchIdSet;
	}

}

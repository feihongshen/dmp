package cn.explink.controller;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.explink.dao.CwbDAO;
import cn.explink.dao.UserDAO;
import cn.explink.domain.SmtOrderContainer;
import cn.explink.domain.User;
import cn.explink.enumutil.FlowOrderTypeEnum;
import cn.explink.service.ExplinkUserDetail;

@Controller
@RequestMapping("/smt")
public class SmtController {

	@Autowired
	private CwbDAO cwbDAO = null;

	@Autowired
	private UserDAO userDAO = null;

	@Autowired
	SecurityContextHolderStrategy securityContextHolderStrategy;

	private enum OrderTypeEnum {
		Normal("normal"), Transfer("transfer"), All("all");

		OrderTypeEnum(String name) {
			this.name = name;
		}

		private static Map<String, OrderTypeEnum> nameMap = null;

		String name;

		static {
			OrderTypeEnum.nameMap = new HashMap<String, OrderTypeEnum>();
			for (OrderTypeEnum dataTypeEnum : OrderTypeEnum.values()) {
				OrderTypeEnum.nameMap.put(dataTypeEnum.getName(), dataTypeEnum);
			}
		}

		public String getName() {
			return this.name;
		}

		public static OrderTypeEnum getDataType(String name) {
			return OrderTypeEnum.nameMap.get(name);
		}

	}

	private enum OptTimeTypeEnum {
		Today("today"), History("history");

		OptTimeTypeEnum(String name) {
			this.name = name;
		}

		private static Map<String, OptTimeTypeEnum> nameMap = null;

		String name;

		static {
			OptTimeTypeEnum.nameMap = new HashMap<String, OptTimeTypeEnum>();
			for (OptTimeTypeEnum dataTypeEnum : OptTimeTypeEnum.values()) {
				OptTimeTypeEnum.nameMap.put(dataTypeEnum.getName(), dataTypeEnum);
			}
		}

		public String getName() {
			return this.name;
		}

		public static OptTimeTypeEnum getDataType(String name) {
			return OptTimeTypeEnum.nameMap.get(name);
		}

	}

	@RequestMapping("/smtorderdispatch")
	public String smtOrderDispatch(Model model) {
		this.addBranchDelvierToModel(model);
		this.addTodayNotDispatchedData(model);
		this.addHistoryNotDispatchedData(model);

		return "smt/smtorderdispatch";
	}

	@RequestMapping("/smtorderoutarea")
	public @ResponseBody String smtOrderOutArea(HttpServletRequest request) {
		return "";
	}

	@RequestMapping("/querysmtorder")
	public SmtOrderContainer querySmtOrder(HttpServletRequest request) {
		OrderTypeEnum dataType = this.getDataType(request);
		OptTimeTypeEnum timeType = this.getTimeType(request);
		int page = this.getQueryPage(request);
		boolean dispatched = this.getDipatched(request);

		return this.querySmtOrder(dataType, timeType, page, dispatched);
	}

	@RequestMapping("/querytodayoutareaorder")
	public SmtOrderContainer queryTodayOutAreaOrder() {
		return null;
	}

	private void addTodayNotDispatchedData(Model model) {
		int tNorNotDisCnt = this.querySmtOrderCount(OrderTypeEnum.Normal, OptTimeTypeEnum.Today, false);
		int tTraNotDisCnt = this.querySmtOrderCount(OrderTypeEnum.Normal, OptTimeTypeEnum.Today, false);
		model.addAttribute("tNorNotDisCnt", tNorNotDisCnt);
		model.addAttribute("tTraNotDisCnt", tTraNotDisCnt);

		SmtOrderContainer tNotDisData = this.querySmtOrder(OrderTypeEnum.All, OptTimeTypeEnum.Today, 1, false);
		model.addAttribute("tNotDisData", tNotDisData);
	}

	private void addHistoryNotDispatchedData(Model model) {
		int hNorNotDisCnt = this.querySmtOrderCount(OrderTypeEnum.Normal, OptTimeTypeEnum.History, false);
		int hTraNotDisCnt = this.querySmtOrderCount(OrderTypeEnum.Normal, OptTimeTypeEnum.History, false);
		model.addAttribute("hNorNotDisCnt", hNorNotDisCnt);
		model.addAttribute("hTraNotDisCnt", hTraNotDisCnt);
	}

	private SmtOrderContainer querySmtOrder(OrderTypeEnum dataType, OptTimeTypeEnum timeType, int page, boolean dispatched) {
		String sql = this.getOrderListQuerySql(dataType, timeType, page, dispatched);

		SmtOrderContainer container = new SmtOrderContainer();
		container.setSmtOrderList(this.cwbDAO.querySmtOrder(sql));

		return container;
	}

	private int querySmtOrderCount(OrderTypeEnum dataType, OptTimeTypeEnum timeType, boolean dispatched) {
		String sql = this.getOrderCountQuerySql(dataType, timeType, dispatched);

		return this.cwbDAO.querySmtOrderCount(sql);
	}

	private OrderTypeEnum getDataType(HttpServletRequest request) {
		return OrderTypeEnum.getDataType(request.getParameter("dataType"));
	}

	private OptTimeTypeEnum getTimeType(HttpServletRequest request) {
		return OptTimeTypeEnum.getDataType(request.getParameter("timeType"));
	}

	private int getQueryPage(HttpServletRequest request) {
		String strPage = request.getParameter("page");
		return strPage == null ? 0 : Integer.valueOf(strPage).intValue() - 1;
	}

	private boolean getDipatched(HttpServletRequest request) {
		String dispatched = request.getParameter("dispatched");

		return Boolean.valueOf(dispatched);
	}

	private void addBranchDelvierToModel(Model model) {
		List<User> delivers = this.getCurrentBranchDeliver();
		model.addAttribute("deliverList", delivers);
	}

	private List<User> getCurrentBranchDeliver() {
		String roleids = "2,4";

		return this.userDAO.getUserByRolesAndBranchid(roleids, this.getSessionUser().getBranchid());
	}

	private String getOrderCountQuerySql(OrderTypeEnum dataType, OptTimeTypeEnum timeType, boolean dispatched) {
		StringBuilder sql = new StringBuilder();
		sql.append("select count(1) from express_ops_cwb_detail where ");
		this.appendOrderQueryWhereCond(sql, dataType, timeType, dispatched);

		return sql.toString();
	}

	private String getOrderListQuerySql(OrderTypeEnum dataType, OptTimeTypeEnum timeType, int page, boolean dispatched) {
		StringBuilder sql = new StringBuilder();
		sql.append("select " + this.getSmtOrderQryFields() + " from express_ops_cwb_detail where ");
		this.appendOrderQueryWhereCond(sql, dataType, timeType, dispatched);
		this.appendLimit(sql, page);

		return sql.toString();
	}

	private void appendLimit(StringBuilder sql, int page) {
		int start = (page - 1) * 100;
		int end = page * 100;
		sql.append("limit " + start + "," + end);
	}

	private void appendOrderQueryWhereCond(StringBuilder sql, OrderTypeEnum dataType, OptTimeTypeEnum timeType, boolean dispatched) {
		// 站点查询条件.
		this.appendBranchWhereCond(sql);
		// 上门退订单查询条件.
		this.appendSmtOrderTypeWhereCond(sql);
		// 未领货查询条件.
		this.appendInStationWhereCond(sql);
		// 数据类型过滤条件.
		this.appendDataTypeWhereCond(sql, dataType);
		// 是否分派小件员.
		this.appendDispatchedWhereCond(sql, dispatched);
		// 时间过滤条件.
		this.appendTimeTypeWhereCond(sql, timeType);
	}

	private void appendBranchWhereCond(StringBuilder sql) {
		long branchId = this.getCurrentBranchId();
		sql.append("deliverybranchid=" + branchId + " ");
	}

	private void appendSmtOrderTypeWhereCond(StringBuilder sql) {
		sql.append("and cwbordertypeid=" + 2 + " ");
	}

	private void appendDispatchedWhereCond(StringBuilder sql, boolean dispatched) {
		if (dispatched) {
			sql.append("and deliverid != ''");
		} else {
			sql.append("and deliverid ='' ");
		}
	}

	private void appendInStationWhereCond(StringBuilder sql) {
		// 流程类型为分到到货和到错误或者配送类型分站滞留.
		sql.append("and (flowordertype IN(7,8) or deliverystate=6) ");
	}

	private void appendDataTypeWhereCond(StringBuilder sql, OrderTypeEnum dataType) {

	}

	private void appendTimeTypeWhereCond(StringBuilder sql, OptTimeTypeEnum timeType) {
		sql.append("and cwb in(");
		sql.append(this.getOptTimeCwbInSqlParam(timeType) + ") ");
	}

	private String getOptTimeCwbInSqlParam(OptTimeTypeEnum timeType) {
		StringBuilder sql = new StringBuilder();
		sql.append("select cwb from express_ops_order_flow where ");
		this.appendBranchWhereCond(sql);
		// 流程状态为分站到货和到错货状态或者配送状态为分站滞留.
		this.appendFlowTypeWhereCond(sql, FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao, FlowOrderTypeEnum.FenZhanDaoHuoYouHuoWuDanSaoMiao);
		// 操作时间条件.
		this.appendOrderOptTimeWhereCond(sql, timeType);

		return sql.toString();
	}

	private void appendFlowTypeWhereCond(StringBuilder sql, FlowOrderTypeEnum... types) {
		if (types.length == 1) {
			sql.append("and flowordertype = " + types[0].getValue() + " ");
		} else {
			sql.append("and flowordertype in (" + this.getOrderFlowTypes(types) + ") ");
		}
	}

	private void appendOrderOptTimeWhereCond(StringBuilder sql, OptTimeTypeEnum timeType) {
		sql.append("and credate");
		if (OptTimeTypeEnum.Today.equals(timeType)) {
			sql.append(" >= ");
		} else {
			sql.append(" < ");
		}
		// 处理时间.
		String credate = this.getTodayZeroTimeString();
		sql.append("'" + credate + "'");

	}

	private String getOrderFlowTypes(FlowOrderTypeEnum... typeEnums) {
		StringBuilder flowTypes = new StringBuilder();
		for (FlowOrderTypeEnum typeEnum : typeEnums) {
			flowTypes.append(typeEnum.getValue());
			flowTypes.append(",");
		}
		return flowTypes.substring(0, flowTypes.length() - 1);
	}

	private String getSmtOrderQryFields() {
		return "opscwbid,cwb,consigneename,consigneeaddress,consigneephone,shouldfare,excelbranch,deliverid";
	}

	private long getCurrentBranchId() {
		return this.getSessionUser().getBranchid();
	}

	private User getSessionUser() {
		ExplinkUserDetail userDetail = (ExplinkUserDetail) this.securityContextHolderStrategy.getContext().getAuthentication().getPrincipal();
		return userDetail.getUser();
	}

	private String getTodayZeroTimeString() {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		Date date = new Date(cal.getTimeInMillis());
		return new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(date);
	}

}

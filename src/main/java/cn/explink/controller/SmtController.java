package cn.explink.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.explink.dao.CwbDAO;
import cn.explink.dao.DeliveryStateDAO;
import cn.explink.dao.ExceptionCwbDAO;
import cn.explink.dao.OrderFlowDAO;
import cn.explink.dao.SystemInstallDAO;
import cn.explink.dao.UserDAO;
import cn.explink.domain.DeliveryState;
import cn.explink.domain.SmtOrder;
import cn.explink.domain.SmtOrderContainer;
import cn.explink.domain.User;
import cn.explink.domain.orderflow.OrderFlow;
import cn.explink.enumutil.ExceptionCwbErrorTypeEnum;
import cn.explink.enumutil.FlowOrderTypeEnum;
import cn.explink.exception.ExplinkException;
import cn.explink.pos.tools.JacksonMapper;
import cn.explink.service.CwbOrderService;
import cn.explink.service.CwbOrderWithDeliveryState;
import cn.explink.service.ExplinkUserDetail;
import cn.explink.service.SmtService;
import cn.explink.util.ExcelUtils;

@Controller
@RequestMapping("/smt")
public class SmtController {
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	private static final String TODAY_OUT_AREA_FN = "今日超区.xlsx";

	private static final String EXCEPTION_DATA_FN = "异常数据.xlsx";

	@Autowired
	private CwbDAO cwbDAO = null;

	@Autowired
	private UserDAO userDAO = null;

	@Autowired
	private OrderFlowDAO orderFlowDAO = null;

	@Autowired
	private CwbOrderService cwborderService;

	@Autowired
	private SystemInstallDAO systemInstallDAO;

	@Autowired
	private ExceptionCwbDAO exceptionCwbDAO;

	@Autowired
	SecurityContextHolderStrategy securityContextHolderStrategy;

	@Autowired
	private SmtService smtService = null;

	@Autowired
	DeliveryStateDAO deliveryStateDAO;

	private ObjectMapper om = JacksonMapper.getInstance();

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

	private enum ColumnEnum {
		OrderNo("订单号"), MatchStation("匹配站点"), ReceivableFee("应收运费"), Delvier("小件员"), CustomerName("退件人姓名"), Phone("联系方式"), Address("取件地址");

		String name;

		ColumnEnum(String name) {
			this.name = name;
		}

		public String getName() {
			return this.name;
		}

	}

	@RequestMapping("/smtorderdispatch")
	public String smtOrderDispatch(Model model,
			@RequestParam(value = "deliverid", required = false, defaultValue = "0") long deliverid) {
		this.addBranchDelvierToModel(model);
		// this.addTodayNotDispatchedData(model);
		// 采用异步加载策略.
		// this.addHistoryNotDispatchedData(model);
		// this.addTodayDispatchData(model);
		// this.addTodayOutAreaData(model);
		// 主页面新增关联小件员查询 add by chunlei05.li 2016/8/16
		model.addAttribute("deliverid", deliverid);
		return "smt/smtorderdispatch";
	}

	@RequestMapping("/smtorderoutarea")
	public @ResponseBody
	JSONObject smtOrderOutArea(HttpServletRequest request) {
		String[] cwbs = this.getCwbs(request).split(",");
		long curBranchId = this.getCurrentBranchId();
		long curUserId = this.getCurrentUserId();
		return this.getSmtService().smtOrderOutArea(cwbs, curBranchId, curUserId);

	}

	@SuppressWarnings("unused")
	private String getOutAreaFlowErrorMsg(FlowOrderTypeEnum flowOrderType) {
		StringBuilder msg = new StringBuilder();
		msg.append("订单状态为[");
		msg.append(flowOrderType.getText());
		msg.append("]非[分站到货]或[到错货]状态无法超区.");

		return msg.toString();
	}

	@RequestMapping("/querysmtorder")
	public @ResponseBody SmtOrderContainer querySmtOrder(HttpServletRequest request,
			@RequestParam(value = "deliverid", required = false, defaultValue = "0") long deliverid, String tableId) {
		OrderTypeEnum dataType = this.getDataType(request);
		OptTimeTypeEnum timeType = this.getTimeType(request);
		int page = this.getQueryPage(request);
		boolean dispatched = this.getDipatched(request);
		// 今日和历史新单派送，关联小件员 add by chunlei05.li 2016/8/16
		if (StringUtils.equals(tableId, "today_table") || StringUtils.equals(tableId, "history_table")) {
			return this.querySmtOrder(dataType, timeType, page, dispatched, deliverid);
		}
		return this.querySmtOrder(dataType, timeType, page, dispatched);
	}

	@RequestMapping("/querytodayoutareaorder")
	public @ResponseBody
	SmtOrderContainer queryTodayOutAreaOrder() {
		return this.queryTodayOutAreaOrder(1);
	}

	@RequestMapping("/exportdata")
	public void exportData(HttpServletRequest request, HttpServletResponse response) throws Exception {
		OrderTypeEnum dataType = this.getDataType(request);
		OptTimeTypeEnum timeType = this.getTimeType(request);
		boolean dispatched = this.getDipatched(request);
		SmtOrderContainer ctn = this.querySmtOrder(dataType, timeType, -1, dispatched);

		this.exportData(response, ctn, dataType, timeType, dispatched);
	}

	@RequestMapping("/exporttodayoutareadata")
	public void exportOutAreaData(HttpServletRequest request, HttpServletResponse response) throws Exception {
		SmtOrderContainer order = this.queryTodayOutAreaOrder(-1);
		this.exportTodayOutAreaData(response, order, SmtController.TODAY_OUT_AREA_FN);
	}

	@RequestMapping("/exportexceptiondata")
	public void exportExceptionData(HttpServletRequest request, HttpServletResponse response) throws Exception {
		SmtOrderContainer order = this.queryExceptionData(request);
		this.exportTodayOutAreaData(response, order, SmtController.EXCEPTION_DATA_FN);
	}

	@RequestMapping("/querysmthistoryordercount")
	@ResponseBody
	public JSONObject querySmtHistoryOrderCount(HttpServletRequest request,
			@RequestParam(value = "deliverid", required = false, defaultValue = "0") long deliverid) {
		/** 订单查询关联小件员 modfiy by chunlei05.li 2016/8/16 **/
		int hNorNotDisCnt = this.querySmtOrderCount(OrderTypeEnum.Normal, OptTimeTypeEnum.History, false, deliverid);
		int hTraNotDisCnt = this.querySmtOrderCount(OrderTypeEnum.Transfer, OptTimeTypeEnum.History, false, deliverid);
		/** end **/
		JSONObject jsonObj = new JSONObject();
		jsonObj.put("hNorNotDisCnt", hNorNotDisCnt);
		jsonObj.put("hTraNotDisCnt", hTraNotDisCnt);

		return jsonObj;
	}

	@RequestMapping("/querysmttodaynotdisordercount")
	@ResponseBody
	public JSONObject querySmtTodayNotDisOrderCount(HttpServletRequest request,
			@RequestParam(value = "deliverid", required = false, defaultValue = "0") long deliverid) {
		/** 订单查询关联小件员 modfiy by chunlei05.li 2016/8/16 **/
		int tNorNotDisCnt = this.querySmtOrderCount(OrderTypeEnum.Normal, OptTimeTypeEnum.Today, false, deliverid);
		int tTraNotDisCnt = this.querySmtOrderCount(OrderTypeEnum.Transfer, OptTimeTypeEnum.Today, false, deliverid);
		SmtOrderContainer tNotDisData = this.querySmtOrder(OrderTypeEnum.All, OptTimeTypeEnum.Today, 1, false, deliverid);
		/** end **/
		JSONObject jsonObj = new JSONObject();
		jsonObj.put("tNorNotDisCnt", tNorNotDisCnt);
		jsonObj.put("tTraNotDisCnt", tTraNotDisCnt);
		jsonObj.put("tNotDisData", tNotDisData);

		return jsonObj;
	}

	@RequestMapping("/querysmttodaydisordercount")
	@ResponseBody
	public JSONObject querySmtTodayDisOrderCount(HttpServletRequest request,
			@RequestParam(value = "deliverid", required = false, defaultValue = "0") long deliverid) {
		/** 订单查询关联小件员 modfiy by chunlei05.li 2016/8/16 **/
		int tNorDisCnt = this.querySmtOrderCount(OrderTypeEnum.Normal, OptTimeTypeEnum.Today, true, deliverid);
		int tTraDisCnt = this.querySmtOrderCount(OrderTypeEnum.Transfer, OptTimeTypeEnum.Today, true, deliverid);
		/** end **/
		JSONObject jsonObj = new JSONObject();
		jsonObj.put("tNorDisCnt", tNorDisCnt);
		jsonObj.put("tTraDisCnt", tTraDisCnt);

		return jsonObj;
	}

	@RequestMapping("/querysmttodayoutareacount")
	@ResponseBody
	public JSONObject queryTodayOutAreaCount() {
		String sql = this.getTodayOutAreaCountSql();
		int count = this.cwbDAO.querySmtOrderCount(sql);

		JSONObject jsonObj = new JSONObject();
		jsonObj.put("tOutAreaCnt", count);

		return jsonObj;
	}

	private SmtOrderContainer queryTodayOutAreaOrder(int page) {
		String sql = this.getTodayOutAreaOrderSql(page);

		List<SmtOrder> orderList = this.cwbDAO.querySmtOrder(sql);
		this.fillUserName(orderList);
		SmtOrderContainer ctn = new SmtOrderContainer();
		ctn.setSmtOrderList(orderList);

		return ctn;
	}

	private String getTodayOutAreaCountSql() {
		StringBuilder sql = new StringBuilder();
		sql.append(this.getSelectTodayOutAreaCountPart());
		this.appendTodayOutAreaWhereCond(sql);

		return sql.toString();
	}

	private String getTodayOutAreaOrderSql(int page) {
		StringBuilder sql = new StringBuilder();
		sql.append(this.getSelectTodayOutAreaPart());
		this.appendTodayOutAreaWhereCond(sql);
		this.appendLimit(sql, page);

		return sql.toString();
	}

	private void appendTodayOutAreaWhereCond(StringBuilder sql) {
		sql.append("f.branchid  = " + this.getCurrentBranchId() + " ");
		sql.append("and f.credate >= '" + this.getTodayOutAreaZeroTimeString() + "' ");
		sql.append("and f.flowordertype = " + FlowOrderTypeEnum.ChaoQu.getValue() + " ");
		sql.append("and d.state = 1 ");
	}

	private String getCwbs(HttpServletRequest request) {
		return request.getParameter("cwbs");
	}

	private SmtOrderContainer queryExceptionData(HttpServletRequest request) {
		String cwbs = request.getParameter("cwbs");
		SmtOrderContainer ctn = new SmtOrderContainer();
		if (cwbs.length() == 0) {
			ctn.setSmtOrderList(new ArrayList<SmtOrder>());
		} else {
			ctn.setSmtOrderList(this.cwbDAO.querySmtOrder(this.getOrderQuerySql(cwbs)));
		}
		return ctn;
	}

	private String getOrderQuerySql(String cwbs) {
		StringBuilder sql = new StringBuilder();
		sql.append("select " + this.getSmtOrderQryFields() + " from express_ops_cwb_detail where ");
		this.appendBranchWhereCond(sql);
		this.appendCwbsWhereCond(sql, cwbs);

		return sql.toString();
	}

	private void exportTodayOutAreaData(HttpServletResponse response, SmtOrderContainer ctn, String fileName) throws Exception {
		ExportHandler handler = new ExportHandler(ctn);
		handler.excel(response, this.getColumnNames(), "sheet1", fileName);
	}

	private void exportData(HttpServletResponse response, SmtOrderContainer ctn, OrderTypeEnum dataType, OptTimeTypeEnum timeType, boolean dispatched) throws Exception {
		ExportHandler handler = new ExportHandler(ctn);
		String fileName = this.getFileName(dataType, timeType, dispatched);
		handler.excel(response, this.getColumnNames(), "sheet1", fileName);
	}

	private String getFileName(OrderTypeEnum dataType, OptTimeTypeEnum timeType, boolean dispatched) {
		StringBuilder fileName = new StringBuilder();
		this.appendTimeTypeToFileName(fileName, timeType);
		this.appendOrderTypeToFileName(fileName, dataType);
		this.appendDispatchToFileName(fileName, dispatched);
		fileName.append(".xlsx");

		return fileName.toString();
	}

	private void appendTimeTypeToFileName(StringBuilder fileName, OptTimeTypeEnum timeType) {
		if (OptTimeTypeEnum.Today.equals(timeType)) {
			fileName.append("今日");
		} else {
			fileName.append("历史");
		}
	}

	private void appendOrderTypeToFileName(StringBuilder fileName, OrderTypeEnum dataType) {
		if (OrderTypeEnum.Normal.equals(dataType)) {
			fileName.append("新单");
		} else if (OrderTypeEnum.Transfer.equals(dataType)) {
			fileName.append("转单");
		} else {
		}
	}

	private void appendDispatchToFileName(StringBuilder fileName, boolean dispatched) {
		if (dispatched) {
			fileName.append("已分派");
		} else {
			fileName.append("未分派");
		}
	}

	private String[] getColumnNames() {
		List<String> colNameList = new ArrayList<String>();
		for (ColumnEnum colEnum : ColumnEnum.values()) {
			colNameList.add(colEnum.getName());
		}
		return colNameList.toArray(new String[0]);
	}

	@SuppressWarnings("unused")
	private void addTodayNotDispatchedData(Model model) {
		int tNorNotDisCnt = this.querySmtOrderCount(OrderTypeEnum.Normal, OptTimeTypeEnum.Today, false);
		int tTraNotDisCnt = this.querySmtOrderCount(OrderTypeEnum.Transfer, OptTimeTypeEnum.Today, false);
		model.addAttribute("tNorNotDisCnt", tNorNotDisCnt);
		model.addAttribute("tTraNotDisCnt", tTraNotDisCnt);

		SmtOrderContainer tNotDisData = this.querySmtOrder(OrderTypeEnum.All, OptTimeTypeEnum.Today, 1, false);
		model.addAttribute("tNotDisData", tNotDisData);
	}

	@SuppressWarnings("unused")
	private void addHistoryNotDispatchedData(Model model) {
		int hNorNotDisCnt = this.querySmtOrderCount(OrderTypeEnum.Normal, OptTimeTypeEnum.History, false);
		int hTraNotDisCnt = this.querySmtOrderCount(OrderTypeEnum.Transfer, OptTimeTypeEnum.History, false);
		model.addAttribute("hNorNotDisCnt", hNorNotDisCnt);
		model.addAttribute("hTraNotDisCnt", hTraNotDisCnt);
	}

	@SuppressWarnings("unused")
	private void addTodayDispatchData(Model model) {
		int tNorDisCnt = this.querySmtOrderCount(OrderTypeEnum.Normal, OptTimeTypeEnum.Today, true);
		int tTraDisCnt = this.querySmtOrderCount(OrderTypeEnum.Transfer, OptTimeTypeEnum.Today, true);
		model.addAttribute("tNorDisCnt", tNorDisCnt);
		model.addAttribute("tTraDisCnt", tTraDisCnt);
	}
	
	/**
	 * 兼容旧方法
	 * @author chunlei05.li
	 * @date 2016年8月16日 下午3:54:28
	 * @param dataType
	 * @param timeType
	 * @param page
	 * @param dispatched
	 * @param deliverid
	 * @return
	 */
	private SmtOrderContainer querySmtOrder(OrderTypeEnum dataType, OptTimeTypeEnum timeType, int page, boolean dispatched) {
		return this.querySmtOrder(dataType, timeType, page, dispatched, 0);
	}
	
	/**
	 * 关联小件员查询
	 * @author chunlei05.li
	 * @date 2016年8月16日 下午5:03:14
	 * @param dataType
	 * @param timeType
	 * @param page
	 * @param dispatched
	 * @param deliverid
	 * @return
	 */
	private SmtOrderContainer querySmtOrder(OrderTypeEnum dataType, OptTimeTypeEnum timeType, int page, boolean dispatched, long deliverid) {
		String sql = this.getOrderListQuerySql(dataType, timeType, page, dispatched, deliverid);

		List<SmtOrder> orderList = this.cwbDAO.querySmtOrder(sql);
		this.fillUserName(orderList);

		SmtOrderContainer container = new SmtOrderContainer();
		container.setSmtOrderList(orderList);

		return container;
	}

	private void fillUserName(List<SmtOrder> orderList) {
		if (orderList.isEmpty()) {
			return;
		}
		List<Long> deliverIdList = this.getDeliverIdList(orderList);
		if (deliverIdList.isEmpty()) {
			return;
		}
		Map<Long, String> nameMap = this.userDAO.getUserNameMap(deliverIdList);
		for (SmtOrder order : orderList) {
			order.setStrDeliver(nameMap.get(Long.valueOf(order.getDeliver())));
		}
	}

	private List<Long> getDeliverIdList(List<SmtOrder> orderList) {
		List<Long> deliverIdList = new ArrayList<Long>();
		for (SmtOrder order : orderList) {
			if (order.getDeliver() == 0) {
				continue;
			}
			deliverIdList.add(Long.valueOf(order.getDeliver()));
		}
		return deliverIdList;
	}
	
	/**
	 * 兼容旧方法
	 * @author chunlei05.li
	 * @date 2016年8月16日 下午3:53:15
	 * @param dataType
	 * @param timeType
	 * @param dispatched
	 * @return
	 */
	private int querySmtOrderCount(OrderTypeEnum dataType, OptTimeTypeEnum timeType, boolean dispatched) {
		return this.querySmtOrderCount(dataType, timeType, dispatched, 0);
	}
	
	/**
	 * 关联小件员查询
	 * @author chunlei05.li
	 * @date 2016年8月16日 下午5:02:56
	 * @param dataType
	 * @param timeType
	 * @param dispatched
	 * @param deliverid
	 * @return
	 */
	private int querySmtOrderCount(OrderTypeEnum dataType, OptTimeTypeEnum timeType, boolean dispatched, long deliverid) {
		String sql = this.getOrderCountQuerySql(dataType, timeType, dispatched, deliverid);

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
		return strPage == null ? 1 : Integer.valueOf(strPage).intValue();
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

	private String getOrderCountQuerySql(OrderTypeEnum dataType, OptTimeTypeEnum timeType, boolean dispatched, long deliverid) {
		StringBuilder sql = new StringBuilder();
		sql.append(this.getSelectCountPart());
		this.appendOrderQueryWhereCond(sql, dataType, timeType, dispatched, deliverid);

		return sql.toString();
	}

	private String getOrderListQuerySql(OrderTypeEnum dataType, OptTimeTypeEnum timeType, int page, boolean dispatched, long deliverid) {
		StringBuilder sql = new StringBuilder();
		sql.append(this.getSelectOrderPart());
		this.appendOrderQueryWhereCond(sql, dataType, timeType, dispatched, deliverid);
		this.appendLimit(sql, page);

		return sql.toString();
	}

	private void appendLimit(StringBuilder sql, int page) {
		if (page == -1) {
			return;
		}
		int start = (page - 1) * 100;
		int end = page * 100;
		sql.append("limit " + start + "," + end);
	}

	private void appendOrderQueryWhereCond(StringBuilder sql, OrderTypeEnum dataType, OptTimeTypeEnum timeType, boolean dispatched, long deliverid) {
		// 站点查询条件.
		this.appendBranchWhereCond(sql);
		// 上门退订单查询条件.
		this.appendSmtOrderTypeWhereCond(sql);
		// 订单状态查询条件.
		this.appendInStationWhereCond(sql, dispatched);
		// 数据类型过滤条件.
		this.appendDataTypeWhereCond(sql, dataType);
		// 时间过滤条件.
		this.appendTimeTypeWhereCond(sql, timeType);
		// 转单数据可能存在多次分站到货.
		// this.appendFlowNowWhereCond(sql, dataType);
		// 小件员查询
		this.appendDeliveridWhereCond(sql, deliverid);
		// 加入订单失效条件.
		this.appendEffectiveWhereCond(sql);
	}

	private void appendEffectiveWhereCond(StringBuilder sql) {
		sql.append("and d.state = 1 ");
	}

	private void appendBranchWhereCond(StringBuilder sql) {
		long branchId = this.getCurrentBranchId();
		sql.append("f.branchid = " + branchId + " ");
	}

	private void appendCwbsWhereCond(StringBuilder sql, String cwbs) {
		sql.append("and cwb in(" + cwbs + ") ");
	}

	private void appendSmtOrderTypeWhereCond(StringBuilder sql) {
		sql.append("and d.cwbordertypeid = 2 ");
	}

	private void appendInStationWhereCond(StringBuilder sql, boolean dispatch) {
		if (dispatch) {
			int pickingFlow = FlowOrderTypeEnum.FenZhanLingHuo.getValue();
			sql.append("and d.flowordertype= " + pickingFlow + " ");

		} else {
			// 流程类型为分到到货和到错误或者配送类型分站滞留.
			sql.append("and (d.flowordertype IN(7,8) or d.deliverystate=6) ");
		}
	}

	private void appendDataTypeWhereCond(StringBuilder sql, OrderTypeEnum dataType) {
		if (OrderTypeEnum.Transfer.equals(dataType)) {
			sql.append("and d.outareaflag = 2 ");
		} else if (OrderTypeEnum.Normal.equals(dataType)) {
			sql.append("and d.outareaflag = 0 ");
		} else {
			sql.append("and d.outareaflag != 1 ");
		}
	}

	private void appendTimeTypeWhereCond(StringBuilder sql, OptTimeTypeEnum timeType) {
		sql.append("and f.credate ");
		if (OptTimeTypeEnum.Today.equals(timeType)) {
			sql.append(">=");
		} else {
			sql.append("<");
		}
		sql.append(this.getTodayZeroTimeString() + " ");
	}
	
	/**
	 * 增加小件员查询条件
	 * @author chunlei05.li
	 * @date 2016年8月16日 下午3:45:36
	 * @param sql
	 * @param timeType
	 */
	private void appendDeliveridWhereCond(StringBuilder sql, long deliverid) {
		if (deliverid == 0) { // 没有选取小件员，则查询全部的
			return;
		}
		// 选取了小件员，则查询匹配改小件员和未匹配小件员的
		sql.append("and d.exceldeliverid in(0,").append(deliverid).append(") ");
	}

	@SuppressWarnings("unused")
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
		sql.append(credate + " ");

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
		return "d.opscwbid,d.cwb,d.consigneename,d.consigneeaddress,d.consigneephone,d.consigneemobile,d.shouldfare,d.excelbranch,d.deliverid";
	}

	private String getSelectOrderPart() {
		return "select " + this.getSmtOrderQryFields() + " from express_ops_cwb_detail d inner join express_ops_operation_time f on d.cwb = f.cwb where ";
	}

	private String getSelectCountPart() {
		return "select count(1) from express_ops_cwb_detail d inner join express_ops_operation_time f on d.cwb = f.cwb where ";
	}

	private String getSelectTodayOutAreaCountPart() {
		return "select count(1) from express_ops_cwb_detail d inner join express_ops_order_flow f on d.cwb = f.cwb where ";
	}

	private String getSelectTodayOutAreaPart() {
		return "select " + this.getSmtOrderQryFields() + " from express_ops_cwb_detail d inner join express_ops_order_flow f on d.cwb = f.cwb where ";
	}

	private long getCurrentUserId() {
		return this.getSessionUser().getUserid();
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
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);

		return Long.toString(cal.getTimeInMillis());
	}

	private String getTodayOutAreaZeroTimeString() {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);

		return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(cal.getTimeInMillis());
	}

	private class ExportHandler extends ExcelUtils {

		private SmtOrderContainer contianer = null;

		public ExportHandler(SmtOrderContainer container) {
			this.contianer = container;
		}

		@Override
		public void fillData(Sheet sheet, CellStyle style) {
			int rowNum = 1;
			List<SmtOrder> orderList = this.getOrderList();
			for (SmtOrder order : orderList) {
				this.createRow(sheet, order, rowNum++, style);
			}
		}

		private void createRow(Sheet sheet, SmtOrder order, int rowNum, CellStyle style) {
			Row row = sheet.createRow(rowNum);
			this.handleSheet(sheet, style);
			this.createStringCell(row, 0, order.getCwb(), style);
			this.createStringCell(row, 1, order.getMatchBranch(), style);
			this.createNumberCell(row, 2, order.getReceivedFee(), style);
			this.createStringCell(row, 3, order.getStrDeliver(), style);
			this.createStringCell(row, 4, order.getCustomerName(), style);
			this.createStringCell(row, 5, order.getPhone(), style);
			this.createStringCell(row, 6, order.getAddress(), style);
		}

		private void handleSheet(Sheet sheet, CellStyle style) {
			style.setAlignment(CellStyle.ALIGN_CENTER);
			sheet.setColumnWidth(0, 15 * 256);
			sheet.setColumnWidth(1, 15 * 256);
			sheet.setColumnWidth(2, 15 * 256);
			sheet.setColumnWidth(3, 15 * 256);
			sheet.setColumnWidth(4, 15 * 256);
			sheet.setColumnWidth(5, 25 * 256);
			sheet.setColumnWidth(6, 50 * 256);
		}

		private void createStringCell(Row row, int column, String value, CellStyle style) {
			Cell cell = row.createCell(column);
			cell.setCellStyle(style);
			cell.setCellValue(value);
		}

		private void createNumberCell(Row row, int column, double value, CellStyle style) {
			Cell cell = row.createCell(column);
			cell.setCellStyle(style);
			cell.setCellValue(value);
		}

		private List<SmtOrder> getOrderList() {
			return this.getContianer().getSmtOrderList();
		}

		private SmtOrderContainer getContianer() {
			return this.contianer;
		}
	}

	public SmtService getSmtService() {
		return this.smtService;
	}

}

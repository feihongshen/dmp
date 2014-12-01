package cn.explink.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.explink.b2c.huitongtx.addressmatch.MatchTypeEnum;
import cn.explink.dao.BranchDAO;
import cn.explink.dao.CwbDAO;
import cn.explink.dao.UserDAO;
import cn.explink.domain.Branch;
import cn.explink.domain.MatchExceptionOrder;
import cn.explink.enumutil.FlowOrderTypeEnum;
import cn.explink.util.ExcelUtils;
import cn.explink.util.SqlBuilder;

@Controller
@RequestMapping("/meh")
public class MatchExceptionController {

	private enum ColumnEnum {

		OrderNo("订单号"), ReportOutAraaTime("上报超区时间"), ReportOutAreaBranch("上报超区站点"), ReportOutAreaUser("上报超区人"), MatchBranch("分派站点"), CustomerName("客户姓名"), CustmerPhone("联系方式"), ReceivedFee("应收运费"), CustomerAddress(
				"客户地址");
		String columnName;

		ColumnEnum(String columnName) {
			this.columnName = columnName;
		}

		public String getColumnName() {
			return this.columnName;
		}

	}

	@Autowired
	private CwbDAO cwbDAO;

	@Autowired
	private BranchDAO branchDAO;

	@Autowired
	private UserDAO userDAO;

	@RequestMapping("/matchexceptionhandle")
	public String matchExpectionHandle(Model model) {
		// 站点列表.
		this.addBranchList(model);
		// 今日待处理转单.
		this.addTWaitTransferOrderCnt(model);
		// 今日待匹配订单.
		this.addTWaitMatchOrderCnt(model);
		// 历史待处理转单.
		this.addHWaitTransferOrderCnt(model);
		// 历史待匹配订单.
		this.addHWaitMatchOrderCnt(model);
		// 今日已转单.
		this.addTTransferOrderCnt(model);
		// 今日已匹配.
		this.addTMatchOrderCnt(model);
		// 今日待处理订单.
		this.addTodayWaitHandleOrder(model);

		return "meh/matchexceptionhandle";
	}

	@RequestMapping("/querymatchexceptionorder")
	public @ResponseBody List<MatchExceptionOrder> queryMatchExceptionOrder(HttpServletRequest request) {
		return this.queryOrder(request);
	}

	@RequestMapping("/exportdata")
	public @ResponseBody void exportData(HttpServletRequest request, HttpServletResponse response) throws Exception {
		boolean today = this.isToday(request);
		Boolean transfer = this.getBooleanValue(request, "transfer");
		Boolean match = this.getBooleanValue(request, "match");
		List<MatchExceptionOrder> meoList = this.queryOrder(today, transfer, match, 1);
		ExportHandler handler = new ExportHandler(meoList);
		handler.exportExcel(response, this.getExportFileName(today, transfer, match));
	}

	@RequestMapping("/exportexceptiondata")
	public @ResponseBody void exportExceptionData(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String data = request.getParameter("data");
		JSONArray array = JSONArray.fromObject(data);
		List<MatchExceptionOrder> meoList = this.transfer(array);
		ExportHandler handler = new ExportHandler(meoList);
		handler.exportExcel(response, "异常订单.xlsx");
	}

	private List<MatchExceptionOrder> transfer(JSONArray data) {
		List<MatchExceptionOrder> meoList = new ArrayList<MatchExceptionOrder>();
		int length = data.size();
		for (int i = 0; i < length; i++) {
			JSONArray row = data.getJSONArray(i);
			meoList.add(this.transferRow(row));
		}
		return meoList;
	}

	private MatchExceptionOrder transferRow(JSONArray row) {
		MatchExceptionOrder meo = new MatchExceptionOrder();
		meo.setCwb(row.getString(0));
		meo.setReportOutAreaTime(row.getString(1));
		meo.setReportOutAreaBranchName(row.getString(2));
		meo.setReportOutAreaUserName(row.getString(3));
		meo.setMatchBranchName(row.getString(4));
		meo.setCustomerName(row.getString(5));
		meo.setCustomerPhone(row.getString(6));
		meo.setReceivedFee(row.getDouble(7));
		meo.setCustomerAddress(row.getString(8));

		return meo;
	}

	private String getExportFileName(boolean today, Boolean transfer, Boolean match) {
		StringBuilder fileName = new StringBuilder();
		this.appendTimeFileName(fileName, today);
		this.appendTypeFileName(fileName, transfer, match);
		fileName.append(".xlsx");

		return fileName.toString();
	}

	private void appendTimeFileName(StringBuilder fileName, boolean today) {
		if (today) {
			fileName.append("今日");
		} else {
			fileName.append("历史");
		}
	}

	private void appendTypeFileName(StringBuilder fileName, Boolean transfer, Boolean match) {
		if ((transfer != null) && (match != null)) {
			if (transfer.booleanValue() && match.booleanValue()) {
				fileName.append("已处理");
			} else {
				fileName.append("未处理");
			}
		} else {
			if ((transfer != null)) {
				if (transfer.booleanValue()) {
					fileName.append("已转单");
				} else {
					fileName.append("未转单");
				}
			}
			if ((match != null)) {
				if (match.booleanValue()) {
					fileName.append("已匹配");
				} else {
					fileName.append("未匹配");
				}
			}
		}
	}

	private List<MatchExceptionOrder> queryOrder(HttpServletRequest request) {
		boolean today = this.isToday(request);
		Boolean transfer = this.getBooleanValue(request, "transfer");
		Boolean match = this.getBooleanValue(request, "match");

		return this.queryOrder(today, transfer, match, 1);
	}

	private List<MatchExceptionOrder> queryOrder(boolean today, Boolean transfer, Boolean match, int page) {
		if ((transfer == null) && (match == null)) {
			return new ArrayList<MatchExceptionOrder>();
		}
		if (transfer == null) {
			return this.queryMatchOrder(today, match.booleanValue(), page);
		}
		if (match == null) {
			return this.queryTransferOrder(today, transfer, page);
		}
		return this.queryWaitHandleOrder(today, page);
	}

	private boolean isToday(HttpServletRequest request) {
		return Boolean.valueOf(request.getParameter("today")).booleanValue();
	}

	private Boolean getBooleanValue(HttpServletRequest request, String name) {
		String parameter = request.getParameter(name);
		if (parameter == null) {
			return null;
		}
		return Boolean.valueOf(parameter);
	}

	private void addBranchList(Model model) {
		List<Branch> branchList = this.branchDAO.getAllBranches();
		model.addAttribute("branchList", branchList);
	}

	private void addTWaitTransferOrderCnt(Model model) {
		int tWaitTraOrdCnt = this.queryTransferOrderCount(true, false);
		model.addAttribute("tWaitTraOrdCnt", Integer.valueOf(tWaitTraOrdCnt));
	}

	private void addHWaitTransferOrderCnt(Model model) {
		int tWaitTraOrdCnt = this.queryTransferOrderCount(false, false);
		model.addAttribute("hWaitTraOrdCnt", Integer.valueOf(tWaitTraOrdCnt));
	}

	private void addTWaitMatchOrderCnt(Model model) {
		int tWaitMatOrdCnt = this.queryMatchOrderCount(true, false);
		model.addAttribute("tWaitMatOrdCnt", Integer.valueOf(tWaitMatOrdCnt));
	}

	private void addHWaitMatchOrderCnt(Model model) {
		int hWaitMatOrdCnt = this.queryMatchOrderCount(false, false);
		model.addAttribute("hWaitMatOrdCnt", Integer.valueOf(hWaitMatOrdCnt));
	}

	private void addTTransferOrderCnt(Model model) {
		int tTraOrdCnt = this.queryTransferOrderCount(true, true);
		model.addAttribute("tTraOrdCnt", Integer.valueOf(tTraOrdCnt));
	}

	private void addTMatchOrderCnt(Model model) {
		int tMatOrdCnt = this.queryMatchOrderCount(true, true);
		model.addAttribute("tTraOrdCnt", Integer.valueOf(tMatOrdCnt));
	}

	private void addTodayWaitHandleOrder(Model model) {
		List<MatchExceptionOrder> tWaitHanOrdList = this.queryWaitHandleOrder(true, 1);
		model.addAttribute("tWaitHanOrdList", tWaitHanOrdList);
	}

	private int queryTransferOrderCount(boolean today, boolean transfer) {
		return this.cwbDAO.queryMatchExceptionOrderCount(this.getQueryTransferCountSql(today, transfer));
	}

	private List<MatchExceptionOrder> queryTransferOrder(boolean today, boolean transfer, int page) {
		List<MatchExceptionOrder> meoList = this.cwbDAO.queryMatchExceptionOrder(this.getQueryTransferOrderSql(today, transfer, page));
		this.fillMatchExceptionOrder(meoList);

		return meoList;
	}

	private int queryMatchOrderCount(boolean today, boolean match) {
		return this.cwbDAO.queryMatchExceptionOrderCount(this.getMatchOrderCountSql(today, match));
	}

	private List<MatchExceptionOrder> queryMatchOrder(boolean today, boolean match, int page) {
		List<MatchExceptionOrder> meoList = this.cwbDAO.queryMatchExceptionOrder(this.getMatchOrderSql(today, match, page));
		this.fillMatchExceptionOrder(meoList);

		return meoList;
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

	private List<MatchExceptionOrder> queryWaitHandleOrder(boolean today, int page) {
		String sql = this.getQuerywaitHandleOrderSql(today, page);
		List<MatchExceptionOrder> meoList = this.cwbDAO.queryMatchExceptionOrder(sql);
		this.fillMatchExceptionOrder(meoList);

		return meoList;
	}

	private String getQuerywaitHandleOrderSql(boolean today, int page) {
		StringBuilder sql = new StringBuilder();
		sql.append(this.getQueryTransferOrderSql(today, false, -1));
		sql.append(" union ");
		sql.append(this.getMatchOrderSql(today, false, -1));
		sql.append(" ");
		sql.append(this.getLimitSql(page));

		return sql.toString();
	}

	private String getQueryTransferOrderSql(boolean today, boolean transfer, int page) {
		SqlBuilder sql = new SqlBuilder();
		sql.appendSelectPart(this.getSelectOrderPart());
		this.appendTransferSqlWhereCond(sql, today, transfer);
		sql.appendExtraPart(this.getLimitSql(page));

		return sql.getSql();
	}

	private String getQueryTransferCountSql(boolean today, boolean transfer) {
		SqlBuilder sql = new SqlBuilder();
		sql.appendSelectPart(this.getSelectCountPart());
		this.appendTransferSqlWhereCond(sql, today, transfer);

		return sql.getSql();
	}

	private String getMatchOrderCountSql(boolean today, boolean match) {
		SqlBuilder sql = new SqlBuilder();
		sql.appendSelectPart(this.getSelectCountPart());
		this.appendMatchSqlWhereCond(sql, today, match);

		return sql.getSql();

	}

	private String getMatchOrderSql(boolean today, boolean match, int page) {
		SqlBuilder sql = new SqlBuilder();
		sql.appendSelectPart(this.getSelectOrderPart());
		this.appendMatchSqlWhereCond(sql, today, match);
		sql.appendExtraPart(this.getLimitSql(page));

		return sql.getSql();
	}

	private String getLimitSql(int page) {
		if (page == -1) {
			return "";
		}
		int start = (page - 1) * 100;
		int end = page * 100;

		return "limit " + start + "," + end;
	}

	private void appendMatchSqlWhereCond(SqlBuilder sql, boolean today, boolean match) {
		// 增加前导列的命中率.
		if (match) {
			sql.appendCondition(this.getTimeWhereCond(today));
			sql.appendCondition(this.getOrderStatusWhereCond(FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao));
			sql.appendCondition(this.getMatchWhereCond(match));
			sql.appendCondition(this.getManualMatchWhereCond());
		} else {
			sql.appendCondition(this.getMatchWhereCond(match));
			sql.appendCondition(this.getOrderStatusWhereCond(FlowOrderTypeEnum.DaoRuShuJu));
			sql.appendCondition(this.getTimeWhereCond(today));
		}
	}

	private String getManualMatchWhereCond() {
		return "d.addresscodeedittype = " + MatchTypeEnum.RenGong.getValue();
	}

	private void appendTransferSqlWhereCond(SqlBuilder sql, boolean today, boolean transfer) {
		// 转单条件.
		sql.appendCondition(this.getTransferWhereCond(transfer));
		// 时间条件.
		sql.appendCondition(this.getTimeWhereCond(today));
	}

	private String getTransferWhereCond(boolean transfer) {
		int outAreaFlow = FlowOrderTypeEnum.ChaoQu.getValue();
		if (transfer) {
			return "f.flowordertype = " + outAreaFlow + " and d.flowordertype between 7 and 59";
		} else {
			return "d.flowordertype = " + outAreaFlow + " and f.flowordertype = " + outAreaFlow;
		}
	}

	private String getOrderStatusWhereCond(FlowOrderTypeEnum orderStatus) {
		StringBuilder cond = new StringBuilder();
		int fzdh = orderStatus.getValue();
		cond.append("d.flowordertype = ");
		cond.append(fzdh);

		return cond.toString();
	}

	private String getMatchWhereCond(boolean match) {
		StringBuilder cond = new StringBuilder();
		if (match) {
			cond.append("d.nextbranchid != 0 ");
		} else {
			cond.append("d.nextbranchid = 0 ");
		}
		return cond.toString();
	}

	private String getTimeWhereCond(boolean today) {
		StringBuilder whereCond = new StringBuilder();
		whereCond.append("f.credate ");
		if (today) {
			whereCond.append(" >= ");
		} else {
			whereCond.append(" < ");
		}
		whereCond.append("'" + this.getTodayZeroTimeString() + "' ");

		return whereCond.toString();
	}

	private String getSelectOrderPart() {
		return "select " + this.getQueryFields() + " from express_ops_cwb_detail d inner join express_ops_order_flow f on d.cwb = f.cwb";
	}

	private String getSelectCountPart() {
		return "select count(1) from express_ops_cwb_detail d inner join express_ops_order_flow f on d.cwb = f.cwb";
	}

	private String getQueryFields() {
		return "f.credate,f.branchid,f.userid,d.cwb,d.nextbranchid,d.consigneename,d.consigneephone,d.shouldfare,d.consigneeaddress";
	}

	private String getTodayZeroTimeString() {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);

		return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(cal.getTime());
	}

	private class ExportHandler extends ExcelUtils {

		private List<MatchExceptionOrder> meoList = null;

		public ExportHandler(List<MatchExceptionOrder> meoList) {
			this.meoList = meoList;
		}

		@Override
		public void fillData(Sheet sheet, CellStyle style) {
			sheet.setColumnWidth(6, 25 * 256);
			sheet.setColumnWidth(8, 50 * 256);
			int rownum = 1;
			for (MatchExceptionOrder meo : this.getMeoList()) {
				this.createRow(sheet, style, meo, rownum++);
			}
		}

		public void exportExcel(HttpServletResponse response, String fileName) throws Exception {
			super.excel(response, this.getColumns(), "sheet1", fileName);
		}

		private void createRow(Sheet sheet, CellStyle style, MatchExceptionOrder order, int rownum) {
			Row row = sheet.createRow(rownum);
			this.createCell(row, 0, order.getCwb(), style);
			this.createCell(row, 1, order.getReportOutAreaTime(), style);
			this.createCell(row, 2, order.getReportOutAreaBranchName(), style);
			this.createCell(row, 3, order.getReportOutAreaUserName(), style);
			this.createCell(row, 4, order.getMatchBranchName(), style);
			this.createCell(row, 5, order.getCustomerName(), style);
			this.createCell(row, 6, order.getCustomerPhone(), style);
			this.createCell(row, 7, order.getReceivedFee(), style);
			this.createCell(row, 8, order.getCustomerAddress(), style);
		}

		private void createCell(Row row, int column, String value, CellStyle style) {
			Cell cell = row.createCell(column);
			cell.setCellStyle(style);
			cell.setCellValue(value);
		}

		private void createCell(Row row, int column, double value, CellStyle style) {
			Cell cell = row.createCell(column);
			cell.setCellStyle(style);
			cell.setCellValue(value);
		}

		private String[] getColumns() {
			List<String> nameList = new ArrayList<String>();
			for (ColumnEnum col : ColumnEnum.values()) {
				nameList.add(col.getColumnName());
			}
			return nameList.toArray(new String[0]);
		}

		private List<MatchExceptionOrder> getMeoList() {
			return this.meoList;
		}

	}
}

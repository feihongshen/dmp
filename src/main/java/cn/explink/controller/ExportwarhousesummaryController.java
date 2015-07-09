package cn.explink.controller;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ColumnMapRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import cn.explink.dao.BranchDAO;
import cn.explink.dao.CommonDAO;
import cn.explink.dao.ComplaintDAO;
import cn.explink.dao.CustomWareHouseDAO;
import cn.explink.dao.CustomerDAO;
import cn.explink.dao.CwbDAO;
import cn.explink.dao.DeliveryStateDAO;
import cn.explink.dao.ExportmouldDAO;
import cn.explink.dao.ExportwarhousesummaryDAO;
import cn.explink.dao.OutWarehouseGroupDAO;
import cn.explink.dao.ReasonDao;
import cn.explink.dao.RemarkDAO;
import cn.explink.dao.SystemInstallDAO;
import cn.explink.dao.TuihuoRecordDAO;
import cn.explink.dao.UserDAO;
import cn.explink.domain.Branch;
import cn.explink.domain.Common;
import cn.explink.domain.Complaint;
import cn.explink.domain.CustomWareHouse;
import cn.explink.domain.Customer;
import cn.explink.domain.CwbDetailView;
import cn.explink.domain.CwbOrder;
import cn.explink.domain.DeliveryState;
import cn.explink.domain.Exportmould;
import cn.explink.domain.Exportwarhousesummary;
import cn.explink.domain.Reason;
import cn.explink.domain.Remark;
import cn.explink.domain.SetExportField;
import cn.explink.domain.TuihuoRecord;
import cn.explink.domain.User;
import cn.explink.service.CwbRouteService;
import cn.explink.service.DataStatisticsService;
import cn.explink.service.ExplinkUserDetail;
import cn.explink.service.ExportService;
import cn.explink.util.DateTimeUtil;
import cn.explink.util.ExcelUtils;
import cn.explink.util.Page;
import cn.explink.util.StreamingStatementCreator;

@RequestMapping("/exportwarhousesummary")
@Controller
public class ExportwarhousesummaryController {

	@Autowired
	ExportwarhousesummaryDAO exDAO;
	@Autowired
	SystemInstallDAO systemInstallDAO;
	@Autowired
	CustomerDAO customerDAO;
	@Autowired
	DataStatisticsService dataStatisticsService;
	@Autowired
	ExportmouldDAO exportmouldDAO;
	@Autowired
	SecurityContextHolderStrategy securityContextHolderStrategy;

	@Autowired
	UserDAO userDAO;
	@Autowired
	CwbDAO cwbDAO;
	@Autowired
	BranchDAO branchDAO;

	@Autowired
	ReasonDao reasonDAO;
	@Autowired
	OutWarehouseGroupDAO outWarehouseGroupDAO;
	@Autowired
	DeliveryStateDAO deliveryStateDAO;
	@Autowired
	CommonDAO commonDAO;
	@Autowired
	JdbcTemplate jdbcTemplate;

	@Autowired
	CwbRouteService cwbRouteService;

	@Autowired
	ExportService exportService;
	@Autowired
	CustomWareHouseDAO customWareHouseDAO;

	@Autowired
	RemarkDAO remarkDAO;
	@Autowired
	TuihuoRecordDAO tuihuoRecordDAO;
	@Autowired
	ComplaintDAO complaintDAO;

	@RequestMapping("/list")
	public String getList(Model model, HttpServletRequest request) {

		List<Branch> warhouseList = this.branchDAO.getAllBranchBySiteType(1);
		List<Branch> zhandianes = this.branchDAO.getAllBranchBySiteType(2);
		model.addAttribute("warhouseList", warhouseList);
		model.addAttribute("zhandianes", zhandianes);

		return "/exportwarhousesummary/list";
	}

	@RequestMapping("/search")
	public String search(Model model, HttpServletRequest request, @RequestParam(value = "warhouseid", required = false, defaultValue = "") String warhouseid,
			@RequestParam(value = "strtime", required = false, defaultValue = "") String strtime, @RequestParam(value = "endtime", required = false, defaultValue = "") String endtime) {
		if ((strtime == "") || (strtime == null)) {
			strtime = DateTimeUtil.formatDate(new Date());
		}
		List<Branch> warhouseList = this.branchDAO.getAllBranchBySiteType(1);
		List<Branch> zhandianes = this.branchDAO.getAllBranchBySiteType(2);
		model.addAttribute("warhouseList", warhouseList);
		model.addAttribute("zhandianes", zhandianes);
		String startbranchid = "'" + warhouseid.replace(",", "','") + "'";
		model.addAttribute("warhouseids", warhouseid);
		String branchids = "";
		for (Branch branch : zhandianes) {
			branchids += branch.getBranchid() + ",";
		}
		branchids.substring(0, branchids.length() - 1);
		model.addAttribute("branchids", branchids);
		branchids = "'" + branchids.replace(",", "','") + "'";
		List<Exportwarhousesummary> exs = this.exDAO.getBranchSum(branchids, startbranchid, strtime, endtime);
		List<Exportwarhousesummary> exsum = this.exDAO.getBranchAllSum(branchids, startbranchid, strtime, endtime);

		Set<String> credates = new TreeSet<String>();// 所有日期
		Map<String, Map<Integer, Integer>> sum = new HashMap<String, Map<Integer, Integer>>();// 所有的站点所有数据
		Map<String, Integer> allsum = new HashMap<String, Integer>();// 所有站点每天的总计
		for (Exportwarhousesummary ex : exsum) {
			allsum.put(ex.getCredate(), ex.getBranchSum());
		}
		for (Exportwarhousesummary ex : exs) {
			credates.add(ex.getCredate());
		}

		for (String credate : credates) {
			Map<Integer, Integer> everday = new HashMap<Integer, Integer>();
			for (Exportwarhousesummary ex : exs) {
				if (credate.equals(ex.getCredate())) {
					everday.put(ex.getBranchId(), ex.getBranchSum());
				}
				sum.put(credate, everday);
			}
		}
		model.addAttribute("credates", credates);
		model.addAttribute("exs", exs);
		model.addAttribute("sum", sum);
		model.addAttribute("allsum", allsum);
		model.addAttribute("start", strtime);
		model.addAttribute("end", endtime);

		List<Exportwarhousesummary> warhousesum = this.exDAO.getWarhouseSum(startbranchid, strtime, endtime);
		int warhouseAllsum = this.exDAO.getWarhouseAllSum(startbranchid, strtime, endtime);// 所有库房入库总计
		Map<String, Integer> warhousemap = new HashMap<String, Integer>();// 所有库房每天的总计
		for (Exportwarhousesummary ex : warhousesum) {
			warhousemap.put(ex.getCredate(), ex.getBranchSum());
		}
		model.addAttribute("warhousemap", warhousemap);
		model.addAttribute("warhouseAllsum", warhouseAllsum);

		List<Exportwarhousesummary> branchallday = this.exDAO.getBranchAllDay(branchids, startbranchid, strtime, endtime);
		int all = this.exDAO.getAllBranchAllDay(branchids, startbranchid, strtime, endtime);
		Map<Integer, Integer> branchmap = new HashMap<Integer, Integer>();// 所有库房每天的总计
		for (Exportwarhousesummary ex : branchallday) {
			branchmap.put(ex.getBranchId(), ex.getBranchSum());
		}
		model.addAttribute("branchmap", branchmap);
		model.addAttribute("all", all);
		return "/exportwarhousesummary/search";
	}

	@RequestMapping("/detail/{page}")
	public String detail(Model model, HttpServletRequest request, @PathVariable("page") long page, @RequestParam(value = "detail_branchids", required = false, defaultValue = "") String branchids,
			@RequestParam(value = "detail_startime", required = false, defaultValue = "") String startime, @RequestParam(value = "detail_endtime", required = false, defaultValue = "") String endtime,
			@RequestParam(value = "flag", required = false, defaultValue = "") String flag) {
		model.addAttribute("branchids", branchids);
		model.addAttribute("startime", startime);
		model.addAttribute("endtime", endtime);
		model.addAttribute("flag", flag);
		branchids = "'" + branchids.replace(",", "','") + "'";
		List<CwbOrder> cwbOrders = null;
		int count = 0;
		if (flag.equals("1")) {
			cwbOrders = this.exDAO.getCwbsByeveryday(page, branchids, startime);
			count = this.exDAO.getCwbsByeverydaycount(branchids, startime);
		} else if (flag.equals("2")) {
			cwbOrders = this.exDAO.getCwbsByBrancheveryday(page, branchids, startime);
			count = this.exDAO.getCwbsByBrancheverydaycount(branchids, startime);
		} else if (flag.equals("3")) {
			cwbOrders = this.exDAO.getCwbsByAlleveryday(page, branchids, startime, endtime);
			count = this.exDAO.getCwbsByAlleverydaycount(branchids, startime, endtime);
		} else if (flag.equals("4")) {
			cwbOrders = this.exDAO.getCwbsByALLBrancheveryday(page, branchids, startime, endtime);
			count = this.exDAO.getCwbsByALLBrancheverydaycount(branchids, startime, endtime);
		} else if (flag.equals("5")) {
			cwbOrders = this.exDAO.getCwbsByBranchAllday(page, branchids, startime, endtime);
			count = this.exDAO.getCwbsByBranchAlldaycount(branchids, startime, endtime);
		}
		String cwbs = "'";
		for (CwbOrder cwb : cwbOrders) {
			cwbs += cwb.getCwb() + "','";
		}
		cwbs = cwbs.substring(0, cwbs.length() - 2);
		List<CwbOrder> cOrders = this.exDAO.getCwbsDetail(cwbs);

		List<Customer> cList = this.customerDAO.getAllCustomers();
		String showCustomer = this.systemInstallDAO.getSystemInstall("showCustomer").getValue();
		JSONArray showCustomerjSONArray = JSONArray.fromObject("[" + showCustomer + "]");
		List<CwbDetailView> weirukuViewlist = this.getcwbDetail(cOrders, cList, showCustomerjSONArray, null, 0);
		model.addAttribute("cOrders", weirukuViewlist);

		List<Exportmould> exportmouldlist = this.exportmouldDAO.getAllExportmouldByUser(this.getSessionUser().getRoleid());
		model.addAttribute("exportmouldlist", exportmouldlist);
		model.addAttribute("cwbs", cwbs);
		model.addAttribute("page", page);
		model.addAttribute("p", null);
		model.addAttribute("page_obj", new Page(count, page, Page.ONE_PAGE_NUMBER));
		return "/exportwarhousesummary/detail";
	}

	@RequestMapping("/exportExcle_warhouse")
	public void exportExcle_warhouse(HttpServletResponse response, @RequestParam(value = "warhouseid", required = false, defaultValue = "") String warhouseid,
			@RequestParam(value = "strtime", required = false, defaultValue = "") String strtime, @RequestParam(value = "endtime", required = false, defaultValue = "") String endtime) {
		final List<Branch> zhandianes = this.branchDAO.getAllBranchBySiteType(2);
		String startbranchid = "'" + warhouseid.replace(",", "','") + "'";
		String branchids = "";
		for (Branch branch : zhandianes) {
			branchids += branch.getBranchid() + ",";
		}
		branchids.substring(0, branchids.length() - 1);
		branchids = "'" + branchids.replace(",", "','") + "'";
		final List<Exportwarhousesummary> exsum = this.exDAO.getBranchAllSum(branchids, startbranchid, strtime, endtime);
		final List<Exportwarhousesummary> exs = this.exDAO.getBranchSum(branchids, startbranchid, strtime, endtime);
		final Map<String, Map<Integer, Integer>> sum = new HashMap<String, Map<Integer, Integer>>();// 所有的站点所有数据
		Set<String> credates = new TreeSet<String>();// 所有日期
		for (Exportwarhousesummary ex : exs) {
			credates.add(ex.getCredate());
		}
		for (String credate : credates) {
			Map<Integer, Integer> everday = new HashMap<Integer, Integer>();
			for (Exportwarhousesummary ex : exs) {
				if (credate.equals(ex.getCredate())) {
					everday.put(ex.getBranchId(), ex.getBranchSum());
				}
				sum.put(credate, everday);
			}
		}
		final List<Exportwarhousesummary> warhousesum = this.exDAO.getWarhouseSum(startbranchid, strtime, endtime);
		final Map<String, Integer> warhousemap = new HashMap<String, Integer>();// 所有库房每天的总计
		for (Exportwarhousesummary ex : warhousesum) {
			warhousemap.put(ex.getCredate(), ex.getBranchSum());
		}
		final int warhouseAllsum = this.exDAO.getWarhouseAllSum(startbranchid, strtime, endtime);
		final List<Exportwarhousesummary> branchallday = this.exDAO.getBranchAllDay(branchids, startbranchid, strtime, endtime);
		final Map<Integer, Integer> branchmap = new HashMap<Integer, Integer>();// 所有库房每天的总计
		for (Exportwarhousesummary ex : branchallday) {
			branchmap.put(ex.getBranchId(), ex.getBranchSum());
		}
		final int all = this.exDAO.getAllBranchAllDay(branchids, startbranchid, strtime, endtime);
		String[] cloumnName1 = new String[zhandianes.size() + 3]; // 导出的列名
		String[] cloumnName2 = new String[zhandianes.size() + 3]; // 导出的英文列名
		this.SetWarhousesummary(cloumnName1, cloumnName2, zhandianes);
		final String[] cloumnName = cloumnName1;
		final String[] cloumnName3 = cloumnName2;
		String sheetName = "出库汇总"; // sheet的名称
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
		String fileName = "Order_" + df.format(new Date()) + ".xlsx"; // 文件名
		try {
			ExcelUtils excelUtil = new ExcelUtils() { // 生成工具类实例，并实现填充数据的抽象方法
				@SuppressWarnings("static-access")
				@Override
				public void fillData(Sheet sheet, CellStyle style) {

					style.setAlignment(CellStyle.ALIGN_CENTER);
					for (int k = 0; k <= exsum.size(); k++) {
						Row row = sheet.createRow(k + 1);
						row.setHeightInPoints(15);
						for (int i = 0; i < cloumnName3.length; i++) {
							sheet.setColumnWidth(i, 15 * 256);
							Cell cell = row.createCell((short) i);
							cell.setCellStyle(style);
							Object a = null;
							// 给导出excel赋值
							a = this.setWarhousesummaryObject(cloumnName3, a, i, k);

							cell.setCellValue(a == null ? "" : a.toString());
						}
					}
				}

				private Object setWarhousesummaryObject(String[] cloumnName3, Object a, int i, int k) {

					if (cloumnName3[i].equals("date")) {
						if (k == exsum.size()) {
							a = "合计";
						} else {
							a = exsum.get(k).getCredate();
						}
					}
					if (cloumnName3[i].equals("inputnumber")) {
						if (k == exsum.size()) {
							a = warhouseAllsum + "";
						} else {
							String credate = exsum.get(k).getCredate();
							if(warhousemap.get(credate)!=null){
								a = warhousemap.get(credate);
							}
							else {
								a="0";
							}
						}
					}
					for (Branch branch : zhandianes) {
						Integer branchid = Integer.parseInt(branch.getBranchid() + "");
						if (cloumnName3[i].equals(branch.getBranchid() + "")) {
							if (k == exsum.size()) {
								a = branchmap.get(branchid) == null ? 0 : branchmap.get(branchid);
							} else {
								String credate = exsum.get(k).getCredate();
								if (sum.get(credate).get(branchid) != null) {
									a = sum.get(credate).get(branchid);
								} else {
									a = 0;
								}
							}
						}
					}
					if (cloumnName3[i].equals("count")) {
						if (k == exsum.size()) {
							a = all;
						} else {
							a = exsum.get(k).getBranchSum();
						}
					}
					return a;
				}
			};
			excelUtil.excel(response, cloumnName, sheetName, fileName);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void SetWarhousesummary(String[] cloumnName1, String[] cloumnName2, List<Branch> zhandianes) {
		cloumnName1[0] = "日期";
		cloumnName2[0] = "date";
		cloumnName1[1] = "入库数量";
		cloumnName2[1] = "inputnumber";
		for (int i = 0; i < zhandianes.size(); i++) {
			cloumnName1[i + 2] = zhandianes.get(i).getBranchname();
			cloumnName2[i + 2] = zhandianes.get(i).getBranchid() + "";
		}
		cloumnName1[zhandianes.size() + 2] = "合计";
		cloumnName2[zhandianes.size() + 2] = "count";
	}

	@RequestMapping("/exportExcle")
	public void exportExcle(Model model, HttpServletResponse response, /*
																		 * @
																		 * RequestParam
																		 * (
																		 * value
																		 * =
																		 * "cwbs"
																		 * ,
																		 * required
																		 * =
																		 * false
																		 * ,
																		 * defaultValue
																		 * = "")
																		 * final
																		 * String
																		 * cwbs0
																		 * ,
																		 */
			@RequestParam(value = "exportmould", required = false, defaultValue = "") final String exportmould, @RequestParam(value = "p", required = false, defaultValue = "") long p,
			@RequestParam(value = "detail_branchids", required = false, defaultValue = "") String branchids,
			@RequestParam(value = "detail_startime", required = false, defaultValue = "") String startime, @RequestParam(value = "detail_endtime", required = false, defaultValue = "") String endtime,
			@RequestParam(value = "flag", required = false, defaultValue = "") String flag) {
		branchids = "'" + branchids.replace(",", "','") + "'";

		int count = 0;
		String str = "";
		if (flag.equals("1")) {
			str += " SELECT a.* FROM express_ops_cwb_detail a," + "express_ops_order_intowarhouse b " + " WHERE a.cwb=b.cwb and a.state=1 AND b.branchid in (" + branchids + ") "
					+ " and   b.flowordertype='4' " + " and   b.state='1' " + " and   b.credate >= '" + startime + " 00:00:00" + "'" + " and   b.credate <= '" + startime + " 23:59:59" + "'"
					+ " limit " + ((p - 1) * Page.EXCEL_PAGE_NUMBER) + " ," + Page.EXCEL_PAGE_NUMBER;

			count = this.exDAO.getCwbsByeverydaycount(branchids, startime);
		} else if (flag.equals("2")) {
			str += " SELECT a.* FROM express_ops_cwb_detail a,express_ops_warehouse_to_branch b  WHERE a.cwb=b.cwb and a.state=1 AND" + " b.nextbranchid in (" + branchids + ") "
					+ " and   b.state='1' " + " and   b.credate >= '" + startime + " 00:00:00" + "'" + " and   b.credate <= '" + startime + " 23:59:59" + "'" + " limit " + ((p - 1)
					* Page.EXCEL_PAGE_NUMBER) + " ," + Page.EXCEL_PAGE_NUMBER;

			count = this.exDAO.getCwbsByBrancheverydaycount(branchids, startime);
		} else if (flag.equals("3")) {
			str += " SELECT a.* FROM express_ops_cwb_detail a,express_ops_order_intowarhouse b  WHERE a.cwb=b.cwb and a.state=1 AND" + " b.branchid in (" + branchids + ") "
					+ " and   b.flowordertype='4' " + " and   b.state='1' " + " and b.credate >= '" + startime + "'" + " and b.credate <= '" + endtime + "'" + " limit " + ((p - 1)
					* Page.EXCEL_PAGE_NUMBER) + " ," + Page.EXCEL_PAGE_NUMBER;

			count = this.exDAO.getCwbsByAlleverydaycount(branchids, startime, endtime);
		} else if (flag.equals("4")) {
			str += " SELECT a.* FROM express_ops_cwb_detail a,express_ops_warehouse_to_branch b  WHERE a.cwb=b.cwb and a.state=1 " + " and   b.nextbranchid in (" + branchids + ")  "
					+ " and   b.type='1' " + " and   b.state='1' " + " and b.credate >= '" + startime + "'" + " and b.credate <= '" + endtime + "'" + " limit " + ((p - 1) * Page.EXCEL_PAGE_NUMBER)
					+ " ," + Page.EXCEL_PAGE_NUMBER;

			count = this.exDAO.getCwbsByALLBrancheverydaycount(branchids, startime, endtime);
		} else if (flag.equals("5")) {
			str += " SELECT a.* FROM express_ops_cwb_detail a,express_ops_warehouse_to_branch b  WHERE a.cwb=b.cwb and a.state=1 " + " and   b.nextbranchid in (" + branchids + ")  "
					+ " and   b.type='1' " + " and   b.state='1' " + " and b.credate >= '" + startime + "'" + " and b.credate <= '" + endtime + "'" + " limit " + ((p - 1) * Page.EXCEL_PAGE_NUMBER)
					+ " ," + Page.EXCEL_PAGE_NUMBER;

			count = this.exDAO.getCwbsByBranchAlldaycount(branchids, startime, endtime);
		}
		p++;
		model.addAttribute("p", p);
		String[] cloumnName1 = {}; // 导出的列名
		String[] cloumnName2 = {}; // 导出的英文列名
		String[] cloumnName3 = {}; // 导出的数据类型

		List<SetExportField> listSetExportField = this.exportmouldDAO.getSetExportFieldByStrs(exportmould);
		cloumnName1 = new String[listSetExportField.size()];
		cloumnName2 = new String[listSetExportField.size()];
		cloumnName3 = new String[listSetExportField.size()];
		for (int k = 0, j = 0; j < listSetExportField.size(); j++, k++) {
			cloumnName1[k] = listSetExportField.get(j).getFieldname();
			cloumnName2[k] = listSetExportField.get(j).getFieldenglishname();
			cloumnName3[k] = listSetExportField.get(j).getExportdatatype();
		}
		final String[] cloumnName4 = cloumnName1;
		final String[] cloumnName5 = cloumnName2;
		final String[] cloumnName6 = cloumnName3;
		String sheetName = "订单信息"; // sheet的名称
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
		String fileName = "Order_" + df.format(new Date()) + ".xlsx"; // 文件名
		try {
			// 查询出数据

			final String sql = str;

			ExcelUtils excelUtil = new ExcelUtils() { // 生成工具类实例，并实现填充数据的抽象方法
				@Override
				public void fillData(final Sheet sheet, final CellStyle style) {
					final List<User> uList = ExportwarhousesummaryController.this.userDAO.getAllUser();
					final Map<Long, Customer> cMap = ExportwarhousesummaryController.this.customerDAO.getAllCustomersToMap();
					final List<Branch> bList = ExportwarhousesummaryController.this.branchDAO.getAllBranches();
					final List<Common> commonList = ExportwarhousesummaryController.this.commonDAO.getAllCommons();
					final List<CustomWareHouse> cWList = ExportwarhousesummaryController.this.customWareHouseDAO.getAllCustomWareHouse();
					List<Remark> remarkList = new ArrayList<Remark>();// remarkDAO.getRemarkByCwbs(cwbs);
					final Map<String, Map<String, String>> remarkMap = ExportwarhousesummaryController.this.exportService.getInwarhouseRemarks(remarkList);
					final List<Reason> reasonList = ExportwarhousesummaryController.this.reasonDAO.getAllReason();
					ExportwarhousesummaryController.this.jdbcTemplate.query(new StreamingStatementCreator(sql), new ResultSetExtractor<Object>() {
						private int count = 0;
						ColumnMapRowMapper columnMapRowMapper = new ColumnMapRowMapper();
						private List<Map<String, Object>> recordbatch = new ArrayList<Map<String, Object>>();

						public void processRow(ResultSet rs) throws SQLException {

							Map<String, Object> mapRow = this.columnMapRowMapper.mapRow(rs, this.count);
							this.recordbatch.add(mapRow);
							this.count++;
							if ((this.count % 100) == 0) {
								this.writeBatch();
							}

						}

						private void writeSingle(Map<String, Object> mapRow, TuihuoRecord tuihuoRecord, DeliveryState ds, Map<String, String> allTime, int rownum, Map<String, String> cwbspayupidMap,
								Map<String, String> complaintMap) throws SQLException {
							Row row = sheet.createRow(rownum + 1);
							row.setHeightInPoints(15);
							for (int i = 0; i < cloumnName4.length; i++) {
								Cell cell = row.createCell((short) i);
								cell.setCellStyle(style);
								// sheet.setColumnWidth(i, (short)
								// (5000));
								// //设置列宽
								Object a = ExportwarhousesummaryController.this.exportService.setObjectA(cloumnName5, mapRow, i, uList, cMap, bList, commonList, tuihuoRecord, ds, allTime, cWList, remarkMap, reasonList, cwbspayupidMap,
										complaintMap);
								if (cloumnName6[i].equals("double")) {
									cell.setCellValue(a == null ? BigDecimal.ZERO.doubleValue() : a.equals("") ? BigDecimal.ZERO.doubleValue() : Double.parseDouble(a.toString()));
								} else {
									cell.setCellValue(a == null ? "" : a.toString());
								}
							}
						}

						@Override
						public Object extractData(ResultSet rs) throws SQLException, DataAccessException {
							while (rs.next()) {
								this.processRow(rs);
							}
							this.writeBatch();
							return null;
						}

						public void writeBatch() throws SQLException {
							if (this.recordbatch.size() > 0) {
								List<String> cwbs = new ArrayList<String>();
								for (Map<String, Object> mapRow : this.recordbatch) {
									cwbs.add(mapRow.get("cwb").toString());
								}
								Map<String, DeliveryState> deliveryStates = this.getDeliveryListByCwbs(cwbs);
								Map<String, TuihuoRecord> tuihuorecoredMap = this.getTuihuoRecoredMap(cwbs);
								Map<String, String> cwbspayupMsp = this.getcwbspayupidMap(cwbs);
								Map<String, String> complaintMap = this.getComplaintMap(cwbs);
								Map<String, Map<String, String>> orderflowList = ExportwarhousesummaryController.this.dataStatisticsService.getOrderFlowByCredateForDetailAndExportAllTime(cwbs, bList);
								int size = this.recordbatch.size();
								for (int i = 0; i < size; i++) {
									String cwb = this.recordbatch.get(i).get("cwb").toString();
									this.writeSingle(this.recordbatch.get(i), tuihuorecoredMap.get(cwb), deliveryStates.get(cwb), orderflowList.get(cwb), (this.count - size) + i, cwbspayupMsp, complaintMap);
								}
								this.recordbatch.clear();
							}
						}

						private Map<String, TuihuoRecord> getTuihuoRecoredMap(List<String> cwbs) {
							Map<String, TuihuoRecord> map = new HashMap<String, TuihuoRecord>();
							for (TuihuoRecord tuihuoRecord : ExportwarhousesummaryController.this.tuihuoRecordDAO.getTuihuoRecordByCwbs(cwbs)) {
								map.put(tuihuoRecord.getCwb(), tuihuoRecord);
							}
							return map;
						}

						private Map<String, DeliveryState> getDeliveryListByCwbs(List<String> cwbs) {
							Map<String, DeliveryState> map = new HashMap<String, DeliveryState>();
							for (DeliveryState deliveryState : ExportwarhousesummaryController.this.deliveryStateDAO.getActiveDeliveryStateByCwbs(cwbs)) {
								map.put(deliveryState.getCwb(), deliveryState);
							}
							return map;
						}

						private Map<String, String> getComplaintMap(List<String> cwbs) {
							Map<String, String> complaintMap = new HashMap<String, String>();
							for (Complaint complaint : ExportwarhousesummaryController.this.complaintDAO.getActiveComplaintByCwbs(cwbs)) {
								complaintMap.put(complaint.getCwb(), complaint.getContent());
							}
							return complaintMap;
						}

						private Map<String, String> getcwbspayupidMap(List<String> cwbs) {
							Map<String, String> cwbspayupidMap = new HashMap<String, String>();

							return cwbspayupidMap;
						}
					});

				}
			};
			excelUtil.excel(response, cloumnName4, sheetName, fileName);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public List<CwbDetailView> getcwbDetail(List<CwbOrder> cwbList, List<Customer> customerList, JSONArray showCustomerjSONArray, List<Branch> branchList, long sign) {
		Map<String, Map<String, String>> allTime = this.dataStatisticsService.getOrderFlowByCredateForDetailAndExportAllTime(this.getcwbs(cwbList), branchList);
		List<CwbDetailView> cwbViewlist = new ArrayList<CwbDetailView>();
		if (cwbList.size() > 0) {
			for (CwbOrder wco : cwbList) {
				CwbDetailView view = new CwbDetailView();
				Map<String, String> cwbMap = allTime.isEmpty() ? new HashMap<String, String>() : (allTime.get(wco.getCwb()));

				view.setOpscwbid(wco.getOpscwbid());
				view.setCwb(wco.getCwb());
				view.setPackagecode(wco.getPackagecode());
				view.setConsigneeaddress(wco.getConsigneeaddress());
				view.setConsigneename(wco.getConsigneename());
				view.setReceivablefee(wco.getReceivablefee());
				view.setEmaildate(wco.getEmaildate());
				view.setTranscwb(wco.getTranscwb());
				view.setCustomerid(wco.getCustomerid());
				view.setNextbranchid(wco.getNextbranchid());

				view.setRemarkView(this.getShowCustomer(showCustomerjSONArray, wco));
				view.setCustomername(this.dataStatisticsService.getQueryCustomerName(customerList, wco.getCustomerid()));
				view.setInSitetime(cwbMap == null ? "" : (cwbMap.get("InSitetime") == null ? "" : cwbMap.get("InSitetime")));
				view.setPickGoodstime(cwbMap == null ? "" : (cwbMap.get("PickGoodstime") == null ? "" : cwbMap.get("PickGoodstime")));
				view.setOutstoreroomtime(cwbMap == null ? "" : (cwbMap.get("Outstoreroomtime") == null ? "" : cwbMap.get("Outstoreroomtime")));
				cwbViewlist.add(view);
			}
		}

		List<CwbDetailView> views = new ArrayList<CwbDetailView>();
		Map<String, CwbDetailView> map = new HashMap<String, CwbDetailView>();
		for (CwbDetailView weirukuView : cwbViewlist) {
			if (sign == 1) {// 按出库时间排序
				map.put(weirukuView.getOutstoreroomtime() + weirukuView.getOpscwbid() + "_" + cwbViewlist.indexOf(weirukuView), weirukuView);
			} else if (sign == 2) {// 按到货时间排序
				map.put(weirukuView.getInSitetime() + weirukuView.getOpscwbid() + "_" + cwbViewlist.indexOf(weirukuView), weirukuView);
			} else if (sign == 3) {// 按领货时间排序
				map.put(weirukuView.getPickGoodstime() + weirukuView.getOpscwbid() + "_" + cwbViewlist.indexOf(weirukuView), weirukuView);
			}
		}
		List<String> keys = new ArrayList<String>(map.keySet());
		Collections.sort(keys, Collections.reverseOrder());
		for (int i = 0; i < keys.size(); i++) {
			views.add(map.get(keys.get(i)));
		}
		return sign == 0 ? cwbViewlist : views;
	}

	public List<String> getcwbs(List<CwbOrder> cwbList) {
		List<String> cwbsList = new ArrayList<String>();
		if (cwbList.size() > 0) {
			for (CwbOrder wco : cwbList) {
				cwbsList.add(wco.getCwb());
			}
		}
		return cwbsList;
	}

	public Object getShowCustomer(JSONArray jSONArray, CwbOrder co) {
		Object remark = "";
		try {
			for (int i = 0; i < jSONArray.size(); i++) {
				String a = jSONArray.getJSONObject(i).getString("customerid");
				String b[] = a.split(",");
				for (String s : b) {
					if (String.valueOf(co.getCustomerid()).equals(s)) {
						remark = co.getClass().getMethod("get" + jSONArray.getJSONObject(i).getString("remark")).invoke(co);
						break;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return remark;
	}

	private User getSessionUser() {
		ExplinkUserDetail userDetail = (ExplinkUserDetail) this.securityContextHolderStrategy.getContext().getAuthentication().getPrincipal();
		return userDetail.getUser();
	}
}

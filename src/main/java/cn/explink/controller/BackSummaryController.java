package cn.explink.controller;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

import cn.explink.dao.BackMiddleDAO;
import cn.explink.dao.BackSummaryDAO;
import cn.explink.dao.BranchDAO;
import cn.explink.dao.CommonDAO;
import cn.explink.dao.ComplaintDAO;
import cn.explink.dao.CustomWareHouseDAO;
import cn.explink.dao.CustomerDAO;
import cn.explink.dao.CwbDAO;
import cn.explink.dao.DeliveryStateDAO;
import cn.explink.dao.ExportmouldDAO;
import cn.explink.dao.ReasonDao;
import cn.explink.dao.RemarkDAO;
import cn.explink.dao.TuihuoRecordDAO;
import cn.explink.dao.UserDAO;
import cn.explink.domain.BackMiddle;
import cn.explink.domain.BackSummary;
import cn.explink.domain.Branch;
import cn.explink.domain.Common;
import cn.explink.domain.Complaint;
import cn.explink.domain.CustomWareHouse;
import cn.explink.domain.Customer;
import cn.explink.domain.CwbOrder;
import cn.explink.domain.DeliveryState;
import cn.explink.domain.Reason;
import cn.explink.domain.Remark;
import cn.explink.domain.SetExportField;
import cn.explink.domain.TuihuoRecord;
import cn.explink.domain.User;
import cn.explink.service.BackSummaryService;
import cn.explink.service.DataStatisticsService;
import cn.explink.service.ExplinkUserDetail;
import cn.explink.service.ExportService;
import cn.explink.util.ExcelUtils;
import cn.explink.util.Page;
import cn.explink.util.StreamingStatementCreator;

import com.pjbest.splitting.aspect.DataSource;
import com.pjbest.splitting.routing.DatabaseType;

/**
 * 退货中心出入库跟踪表
 * 
 * @author zs
 */

@Controller
@RequestMapping("/backsummary")
public class BackSummaryController {
	
	private static Logger logger = LoggerFactory.getLogger(BackSummaryController.class);
	
	@Autowired
	SecurityContextHolderStrategy securityContextHolderStrategy;
	@Autowired
	BackSummaryDAO backSummaryDAO;
	@Autowired
	BackSummaryService backSummaryService;
	@Autowired
	BackMiddleDAO backMiddleDAO;
	@Autowired
	CustomWareHouseDAO customWareHouseDAO;
	@Autowired
	BranchDAO branchDAO;
	@Autowired
	CustomerDAO customerDAO;
	@Autowired
	CwbDAO cwbDAO;
	@Autowired
	UserDAO userDAO;
	@Autowired
	ExportmouldDAO exportmouldDAO;
	@Autowired
	CommonDAO commonDAO;
	@Autowired
	RemarkDAO remarkDAO;
	@Autowired
	ReasonDao reasonDAO;
	@Autowired
	JdbcTemplate jdbcTemplate;
	@Autowired
	ExportService exportService;
	@Autowired
	DataStatisticsService dataStatisticsService;
	@Autowired
	TuihuoRecordDAO tuihuoRecordDAO;
	@Autowired
	DeliveryStateDAO deliveryStateDAO;
	@Autowired
	ComplaintDAO complaintDAO;

	private User getSessionUser() {
		ExplinkUserDetail userDetail = (ExplinkUserDetail) securityContextHolderStrategy.getContext().getAuthentication().getPrincipal();
		return userDetail.getUser();
	}

	/**
	 * 退货中心出入库跟踪表 列表
	 * 
	 * @param model
	 * @param time
	 * @return
	 */
	@RequestMapping("/list")
	public String list(Model model, @RequestParam(value = "time", required = false, defaultValue = "") String time) {
		if (!"".equals(time)) {
			String starttime = time + " 00:00:00";
			String endtime = time + " 23:59:59";
			List<BackSummary> backSummary = backSummaryDAO.getBackSummaryList(starttime, endtime);
			model.addAttribute("backSummary", backSummary);
		}
		return "/backsummary/list";
	}

	/**
	 * 根据汇总信息查 订单明细
	 * 
	 * @param model
	 * @param summaryid
	 * @param type
	 * @param time
	 * @param page
	 * @return
	 */
	@RequestMapping("/detail/{summaryid}/{type}/{time}/{page}")
	public String detail(Model model, @PathVariable("summaryid") long summaryid, @PathVariable("type") int type, @PathVariable("time") String time, @PathVariable("page") long page) {
		model.addAttribute("customerMap", customerDAO.getAllCustomersToMap());

		Map<Long, CustomWareHouse> customerWarehouseMap = new HashMap<Long, CustomWareHouse>();
		for (CustomWareHouse customWareHouse : customWareHouseDAO.getAllCustomWareHouse()) {
			customerWarehouseMap.put(customWareHouse.getWarehouseid(), customWareHouse);
		}
		model.addAttribute("customerWarehouseMap", customerWarehouseMap);

		Map<Long, Branch> branchMap = new HashMap<Long, Branch>();
		for (Branch branch : branchDAO.getAllBranches()) {
			branchMap.put(branch.getBranchid(), branch);
		}
		model.addAttribute("branchMap", branchMap);

		// 从退货出站统计_中间表获得订单号
		List<BackMiddle> backList = backMiddleDAO.getBackMiddleList(page, summaryid, type);
		StringBuffer sb = new StringBuffer("");
		List<CwbOrder> cwbList = new ArrayList<CwbOrder>();
		if (backList != null && !backList.isEmpty()) {
			for (BackMiddle backMiddle : backList) {
				sb.append("'").append(backMiddle.getCwb()).append("',");
			}
			cwbList = cwbDAO.getCwbByCwbs(sb.toString().substring(0, sb.toString().lastIndexOf(",")));
		}

		model.addAttribute("exportmouldlist", exportmouldDAO.getAllExportmouldByUser(this.getSessionUser().getRoleid()));// 导出模板
		model.addAttribute("summaryid", summaryid);
		model.addAttribute("type", type);
		model.addAttribute("time", time);
		model.addAttribute("cwbList", cwbList);
		model.addAttribute("page_obj", new Page(backMiddleDAO.getBackMiddleCount(summaryid, type), page, Page.ONE_PAGE_NUMBER));
		model.addAttribute("page", page);
		return "backsummary/detail";
	}

	/**
	 * 导出汇总列表Excel
	 * 
	 * @param model
	 * @param response
	 * @param request
	 * @param exceltime
	 */
	@RequestMapping("/exportExcleList")
	@DataSource(DatabaseType.REPLICA)
	public void exportExcleList(Model model, HttpServletResponse response, HttpServletRequest request, @RequestParam(value = "exceltime", required = false, defaultValue = "") String exceltime) {
		if (!"".equals(exceltime)) {
			String[] cloumnName1 = new String[6]; // 导出的列名
			String[] cloumnName2 = new String[6]; // 导出的英文列名
			exportService.SetBackSummaryFields(cloumnName1, cloumnName2);
			final String[] cloumnName = cloumnName1;
			final String[] cloumnName3 = cloumnName2;
			final HttpServletRequest request1 = request;
			String sheetName = "退货中心出入库跟踪表"; // sheet的名称
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
			String fileName = "Back_" + df.format(new Date()) + ".xlsx"; // 文件名
			try {
				String starttime = exceltime + " 00:00:00";
				String endtime = exceltime + " 59:59:59";
				List<BackSummary> backList = backSummaryDAO.getBackSummaryList(starttime, endtime);
				final List<BackSummary> list = backSummaryService.getBackSummaryByExcelView(backList);
				ExcelUtils excelUtil = new ExcelUtils() { // 生成工具类实例，并实现填充数据的抽象方法
					@Override
					public void fillData(Sheet sheet, CellStyle style) {
						for (int k = 0; k < list.size(); k++) {
							Row row = sheet.createRow(k + 1);
							row.setHeightInPoints((float) 15);
							for (int i = 0; i < cloumnName.length; i++) {
								Cell cell = row.createCell((short) i);
								cell.setCellStyle(style);
								Object a = null;
								// 给导出excel赋值
								a = exportService.setBackSummary(cloumnName3, request1, list, a, i, k);
								cell.setCellValue(a == null ? "" : a.toString());
							}
						}
					}
				};
				excelUtil.excel(response, cloumnName, sheetName, fileName);
			} catch (Exception e) {
				logger.error("", e);
			}
		}
	}

	/**
	 * 导出订单明细
	 * 
	 * @param model
	 * @param response
	 * @param request
	 * @param summaryid
	 *            汇总ID
	 * @param type
	 *            类型
	 * @param mouldfieldids2
	 *            导出模板
	 */
	@RequestMapping("/exportExcle")
	@DataSource(DatabaseType.REPLICA)
	public void exportExcle(Model model, HttpServletResponse response, HttpServletRequest request, @RequestParam(value = "summaryid", required = false, defaultValue = "0") long summaryid,
			@RequestParam(value = "type", required = false, defaultValue = "0") int type, @RequestParam(value = "exportmould", required = false, defaultValue = "") String exportmould) {
		String[] cloumnName1 = {}; // 导出的列名
		String[] cloumnName2 = {}; // 导出的英文列名
		String[] cloumnName3 = {}; // 导出的数据类型

		if (exportmould != null && !"0".equals(exportmould)) { // 选择模板
			List<SetExportField> listSetExportField = exportmouldDAO.getSetExportFieldByStrs(exportmould);
			cloumnName1 = new String[listSetExportField.size()];
			cloumnName2 = new String[listSetExportField.size()];
			cloumnName3 = new String[listSetExportField.size()];
			for (int k = 0, j = 0; j < listSetExportField.size(); j++, k++) {
				cloumnName1[k] = listSetExportField.get(j).getFieldname();
				cloumnName2[k] = listSetExportField.get(j).getFieldenglishname();
				cloumnName3[k] = listSetExportField.get(j).getExportdatatype();
			}
		} else {
			List<SetExportField> listSetExportField = exportmouldDAO.getSetExportFieldByStrs("0");
			cloumnName1 = new String[listSetExportField.size()];
			cloumnName2 = new String[listSetExportField.size()];
			cloumnName3 = new String[listSetExportField.size()];
			for (int k = 0, j = 0; j < listSetExportField.size(); j++, k++) {
				cloumnName1[k] = listSetExportField.get(j).getFieldname();
				cloumnName2[k] = listSetExportField.get(j).getFieldenglishname();
				cloumnName3[k] = listSetExportField.get(j).getExportdatatype();
			}
		}
		final String[] cloumnName4 = cloumnName1;
		final String[] cloumnName5 = cloumnName2;
		final String[] cloumnName6 = cloumnName3;
		String sheetName = "订单信息"; // sheet的名称
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
		String fileName = "Order_" + df.format(new Date()) + ".xlsx"; // 文件名

		try {
			final String sql = backMiddleDAO.getCwbByExcel(0, summaryid, type);
			ExcelUtils excelUtil = new ExcelUtils() { // 生成工具类实例，并实现填充数据的抽象方法
				@Override
				public void fillData(final Sheet sheet, final CellStyle style) {
					final List<User> uList = userDAO.getAllUserByuserDeleteFlag();
					final Map<Long, Customer> cMap = customerDAO.getAllCustomersToMap();
					final List<Branch> bList = branchDAO.getAllBranches();
					final List<Common> commonList = commonDAO.getAllCommons();
					final List<CustomWareHouse> cWList = customWareHouseDAO.getAllCustomWareHouse();
					List<Remark> remarkList = remarkDAO.getAllRemark();
					final Map<String, Map<String, String>> remarkMap = exportService.getInwarhouseRemarks(remarkList);
					final List<Reason> reasonList = reasonDAO.getAllReason();
					jdbcTemplate.query(new StreamingStatementCreator(sql), new ResultSetExtractor<Object>() {
						private int count = 0;
						ColumnMapRowMapper columnMapRowMapper = new ColumnMapRowMapper();
						private List<Map<String, Object>> recordbatch = new ArrayList<Map<String, Object>>();

						public void processRow(ResultSet rs) throws SQLException {
							Map<String, Object> mapRow = columnMapRowMapper.mapRow(rs, count);
							recordbatch.add(mapRow);
							count++;
							if (count % 100 == 0) {
								writeBatch();
							}
						}

						private void writeSingle(Map<String, Object> mapRow, TuihuoRecord tuihuoRecord, DeliveryState ds, Map<String, String> allTime, int rownum, Map<String, String> cwbspayupidMap,
								Map<String, String> complaintMap) throws SQLException {
							Row row = sheet.createRow(rownum + 1);
							row.setHeightInPoints((float) 15);
							for (int i = 0; i < cloumnName4.length; i++) {
								Cell cell = row.createCell((short) i);
								cell.setCellStyle(style);
								// sheet.setColumnWidth(i, (short) (5000));
								// //设置列宽
								Object a = exportService.setObjectA(cloumnName5, mapRow, i, uList, cMap, bList, commonList, tuihuoRecord, ds, allTime, cWList, remarkMap, reasonList, cwbspayupidMap,
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
							writeBatch();
							return null;
						}

						public void writeBatch() throws SQLException {
							if (recordbatch.size() > 0) {
								List<String> cwbs = new ArrayList<String>();
								for (Map<String, Object> mapRow : recordbatch) {
									cwbs.add(mapRow.get("cwb").toString());
								}
								Map<String, DeliveryState> deliveryStates = getDeliveryListByCwbs(cwbs);
								Map<String, TuihuoRecord> tuihuorecoredMap = getTuihuoRecoredMap(cwbs);
								Map<String, String> cwbspayupMsp = getcwbspayupidMap(cwbs);
								Map<String, String> complaintMap = getComplaintMap(cwbs);
								Map<String, Map<String, String>> orderflowList = dataStatisticsService.getOrderFlowByCredateForDetailAndExportAllTime(cwbs, bList);
								int size = recordbatch.size();
								for (int i = 0; i < size; i++) {
									String cwb = recordbatch.get(i).get("cwb").toString();
									writeSingle(recordbatch.get(i), tuihuorecoredMap.get(cwb), deliveryStates.get(cwb), orderflowList.get(cwb), count - size + i, cwbspayupMsp, complaintMap);
								}
								recordbatch.clear();
							}
						}

						private Map<String, TuihuoRecord> getTuihuoRecoredMap(List<String> cwbs) {
							Map<String, TuihuoRecord> map = new HashMap<String, TuihuoRecord>();
							for (TuihuoRecord tuihuoRecord : tuihuoRecordDAO.getTuihuoRecordByCwbs(cwbs)) {
								map.put(tuihuoRecord.getCwb(), tuihuoRecord);
							}
							return map;
						}

						private Map<String, DeliveryState> getDeliveryListByCwbs(List<String> cwbs) {
							Map<String, DeliveryState> map = new HashMap<String, DeliveryState>();
							for (DeliveryState deliveryState : deliveryStateDAO.getActiveDeliveryStateByCwbs(cwbs)) {
								map.put(deliveryState.getCwb(), deliveryState);
							}
							return map;
						}

						private Map<String, String> getComplaintMap(List<String> cwbs) {
							Map<String, String> complaintMap = new HashMap<String, String>();
							for (Complaint complaint : complaintDAO.getActiveComplaintByCwbs(cwbs)) {
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
			logger.error("", e);
		}
	}

}

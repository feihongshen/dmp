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

import cn.explink.dao.BranchDAO;
import cn.explink.dao.CommonDAO;
import cn.explink.dao.ComplaintDAO;
import cn.explink.dao.CustomWareHouseDAO;
import cn.explink.dao.CustomerDAO;
import cn.explink.dao.CwbDAO;
import cn.explink.dao.DeliveryStateDAO;
import cn.explink.dao.ExportmouldDAO;
import cn.explink.dao.MonitorDAO;
import cn.explink.dao.ReasonDao;
import cn.explink.dao.RemarkDAO;
import cn.explink.dao.TuihuoRecordDAO;
import cn.explink.dao.UserDAO;
import cn.explink.domain.Branch;
import cn.explink.domain.Common;
import cn.explink.domain.Complaint;
import cn.explink.domain.CustomWareHouse;
import cn.explink.domain.Customer;
import cn.explink.domain.DeliveryState;
import cn.explink.domain.Reason;
import cn.explink.domain.Remark;
import cn.explink.domain.SetExportField;
import cn.explink.domain.TuihuoRecord;
import cn.explink.domain.User;
import cn.explink.enumutil.BranchEnum;
import cn.explink.service.DataStatisticsService;
import cn.explink.service.ExplinkUserDetail;
import cn.explink.service.ExportService;
import cn.explink.service.MonitorLogService;
import cn.explink.util.ExcelUtils;
import cn.explink.util.Page;
import cn.explink.util.StreamingStatementCreator;

@Controller
@RequestMapping("/monitorlog")
public class MonitorLogController {

	@Autowired
	MonitorLogService monitorLogService;
	@Autowired
	UserDAO userDAO;
	@Autowired
	CwbDAO cwbDAO;
	@Autowired
	BranchDAO branchDAO;
	@Autowired
	CustomerDAO customerDAO;
	@Autowired
	ExportService exportService;
	@Autowired
	ExportmouldDAO exportmouldDAO;
	@Autowired
	CommonDAO commonDAO;
	@Autowired
	CustomWareHouseDAO customWareHouseDAO;
	@Autowired
	RemarkDAO remarkDAO;
	@Autowired
	ReasonDao reasonDao;
	@Autowired
	TuihuoRecordDAO tuihuoRecordDAO;
	@Autowired
	DeliveryStateDAO deliveryStateDAO;
	@Autowired
	ComplaintDAO complaintDAO;
	@Autowired
	MonitorDAO monitorDAO;
	@Autowired
	SecurityContextHolderStrategy securityContextHolderStrategy;
	@Autowired
	DataStatisticsService dataStatisticsService;
	@Autowired
	private JdbcTemplate jdbcTemplate;
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	private User getSessionUser() {
		ExplinkUserDetail userDetail = (ExplinkUserDetail) this.securityContextHolderStrategy.getContext().getAuthentication().getPrincipal();
		return userDetail.getUser();
	}

	/**
	 * 查看订单生命周期监控
	 *
	 * @param model
	 * @param cwb
	 * @param abnormaltypeid
	 * @return
	 */
	@RequestMapping("/monitorloglist")
	public String monitorloglist(Model model,@RequestParam(value = "customerid", required = false, defaultValue = "") String[] customeridStr,
			HttpServletRequest request) {
		
		List<Customer> clist =  this.customerDAO.getAllCustomers();
		model.addAttribute("customerlist",clist);
		Map<Long,String> cmap =new HashMap<Long , String>();
		for (Customer cut : clist) {
			cmap.put(cut.getCustomerid(), cut.getCustomername());
		}
		model.addAttribute("customerMap",cmap);
		List<String> customeridList = this.dataStatisticsService.getList(customeridStr);
		model.addAttribute("customeridStr", customeridList);
		List<Branch>  blist = branchDAO.getBranchAllzhandian(BranchEnum.KuFang.getValue()+"");
		String branchids ="-1";
		if(blist != null && blist.size()>0){
			for (Branch branch : blist) {
				branchids += ","+branch.getBranchid();
			}
		}
		String customerids =getStrings(customeridStr);
		List<MonitorLogDTO> monitorList =  new ArrayList<MonitorLogDTO>();
		if(request.getParameter("isnow") != null && request.getParameter("isnow").equals("1") ){
			monitorList = monitorLogService.getMonitorLogByBranchid(branchids,customerids);
		}		
		
		model.addAttribute("monitorList", monitorList);
		return "/monitor/monitorlog";
	}
	
	
	@RequestMapping("/show/{customerid}/{type}/{page}")
	public String showMonitor(Model model,@PathVariable("customerid") long customerid,
			@PathVariable("type") String type,
			@PathVariable("page") long page,
			HttpServletRequest request) {
		List<Customer> clist =  this.customerDAO.getAllCustomers();
		model.addAttribute("customerlist",clist);
		Map<Long,String> cmap =new HashMap<Long , String>();
		for (Customer cut : clist) {
			cmap.put(cut.getCustomerid(), cut.getCustomername());
		}
		model.addAttribute("customerMap",cmap);
		List<Branch>  blist = branchDAO.getBranchAllzhandian(BranchEnum.KuFang.getValue()+"");
		String branchids ="-1";
		if(blist != null && blist.size()>0){
			for (Branch branch : blist) {
				branchids += ","+branch.getBranchid();
			}
		}
		List<CwbOrderView>  cwborderList =   monitorLogService.getMonitorLogByType(branchids,customerid,type,page);
		
		model.addAttribute("cwborderList", cwborderList);
		
		long count = monitorLogService.getMonitorLogByTypeCount(branchids,customerid,type);
		Page pageparm = new Page(count, page, Page.ONE_PAGE_NUMBER);
		
		model.addAttribute("page_obj", pageparm);
		model.addAttribute("page", page);
		model.addAttribute("customerid", customerid);
		model.addAttribute("type", type);
		model.addAttribute("exportmouldlist", this.exportmouldDAO.getAllExportmouldByUser(this.getSessionUser().getRoleid()));
		return "/monitor/monitorlogshow";
	}
	
	@RequestMapping("/exportExcel")
	public void exportExcel(Model model, HttpServletResponse response, @RequestParam(value = "customerid", required = false, defaultValue = "0") long customerid,
			@RequestParam(value = "type", required = false, defaultValue = "") String type,
			@RequestParam(value = "exportmould", required = false, defaultValue = "0") String exportEume) {
		String[] cloumnName1 = {}; // 导出的列名
		String[] cloumnName2 = {}; // 导出的英文列名
		String[] cloumnName3 = {}; // 导出的数据类型

		List<SetExportField> listSetExportField = this.exportmouldDAO.getSetExportFieldByStrs(exportEume);
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
		String sheetName = "订单明细"; // sheet的名称
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
		String fileName = "monitor_" + df.format(new Date()) + ".xlsx"; // 文件名
		try {
			List<Branch>  blist = branchDAO.getBranchAllzhandian(BranchEnum.KuFang.getValue()+"");
			String branchids ="-1";
			if(blist != null && blist.size()>0){
				for (Branch branch : blist) {
					branchids += ","+branch.getBranchid();
				}
			}
			
			String sql1 = "";
			if("weidaohuo".equals(type)){
				sql1 =   monitorDAO.getMonitorLogByTypeSql("1", customerid);
			}
			
			if("tihuo".equals(type)){
				sql1 =   monitorDAO.getMonitorLogByTypeSql("2", customerid);
			}
			if("ruku".equals(type)){
				sql1 =   monitorDAO.getMonitorLogByTypeSql("4", customerid);
			}
			if("chuku".equals(type)){
				sql1 =   monitorDAO.getMonitorLogByTypeSql("6", branchids,customerid);
			}
			if("daozhan".equals(type)){
				sql1 =   monitorDAO.getMonitorLogByTypeSql("7,8,9,35,36", branchids,customerid);
			}
			if("zaizhanziji".equals(type)){
				sql1 =  "select * from express_ops_operation_time where id=-1";
			}
			if("yichuzhan".equals(type)){
				sql1 =   monitorDAO.getMonitorLogByTypeSql("6,14,40", branchids,customerid);
			}
			if("Zhongzhanruku".equals(type)){
				sql1 =   monitorDAO.getMonitorLogByTypeSql("12",customerid);
			}
			if("tuihuoruku".equals(type)){
				sql1 =   monitorDAO.getMonitorLogByTypeSql("15", customerid);
			}
			if("tuigonghuoshang".equals(type)){
				sql1 =   monitorDAO.getMonitorLogByTypeSql("27",customerid);
			}
			if("tuikehuweishoukuan".equals(type)){
				sql1 =   "select * from express_ops_operation_time where id=-1";
			}
			if("all".equals(type)){
				sql1 =   monitorDAO.getMonitorLogByTypeSql("1,2,4,6,7,8,9,12,14,15,27,35,36,40",customerid);
			}
			
			final String sql =sql1;

			ExcelUtils excelUtil = new ExcelUtils() { // 生成工具类实例，并实现填充数据的抽象方法
				@Override
				public void fillData(final Sheet sheet, final CellStyle style) {
					final List<User> uList = userDAO.getAllUser();
					final Map<Long, Customer> cMap = customerDAO.getAllCustomersToMap();
					final List<Branch> bList = branchDAO.getAllBranches();
					final List<Common> commonList = commonDAO.getAllCommons();
					final List<CustomWareHouse> cWList = customWareHouseDAO.getAllCustomWareHouse();
					List<Remark> remarkList = remarkDAO.getAllRemark();
					final Map<String, Map<String, String>> remarkMap = exportService.getInwarhouseRemarks(remarkList);
					final List<Reason> reasonList = reasonDao.getAllReason();
					jdbcTemplate.query(new StreamingStatementCreator(sql), new ResultSetExtractor<Object>() {
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
								// sheet.setColumnWidth(i, (short) (5000));
								// //设置列宽
								Object a = exportService.setObjectA(cloumnName5, mapRow, i, uList, cMap, bList, commonList, tuihuoRecord, ds, allTime, cWList, remarkMap,
										reasonList, cwbspayupidMap, complaintMap);
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
								Map<String, Map<String, String>> orderflowList = dataStatisticsService.getOrderFlowByCredateForDetailAndExportAllTime(cwbs, bList);
								int size = this.recordbatch.size();
								for (int i = 0; i < size; i++) {
									String cwb = this.recordbatch.get(i).get("cwb").toString();
									this.writeSingle(this.recordbatch.get(i), tuihuorecoredMap.get(cwb), deliveryStates.get(cwb), orderflowList.get(cwb), (this.count - size) + i, cwbspayupMsp,
											complaintMap);
								}
								this.recordbatch.clear();
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
							for (DeliveryState deliveryState :deliveryStateDAO.getActiveDeliveryStateByCwbs(cwbs)) {
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
							/*
							 * for(DeliveryState deliveryState:deliveryStateDAO.
							 * getActiveDeliveryStateByCwbs(cwbs)){ String
							 * ispayup = "否"; GotoClassAuditing goclass =
							 * gotoClassAuditingDAO
							 * .getGotoClassAuditingByGcaid(deliveryState
							 * .getGcaid());
							 * 
							 * if(goclass!=null&&goclass.getPayupid()!=0){
							 * ispayup = "是"; }
							 * cwbspayupidMap.put(deliveryState.getCwb(),
							 * ispayup); }
							 */
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
	
	
	private String getStrings(String[] strArr) {
		String strs = "";
		if (strArr.length > 0) {
			for (String str : strArr) {
				strs += str + ",";
			}
		}

		if (strs.length() > 0) {
			strs = strs.substring(0, strs.length() - 1);
		}
		return strs;
	}
	@RequestMapping("/monitorcunhuolist")
	public String monitorcunhuolist(Model model,@RequestParam(value = "dispatchbranchid", required = false, defaultValue = "") String[] dispatchbranchidStr) {
		
		List<Branch> branchList = this.branchDAO.getBranchToUser(this.getSessionUser().getUserid());
		List<String> dispatchbranchidlist = this.dataStatisticsService.getList(dispatchbranchidStr);
		model.addAttribute("dispatchbranchidStr", dispatchbranchidlist);
		model.addAttribute("branchList", branchList);
		return "/monitor/monitorcunhuo";
	}
	
	
}
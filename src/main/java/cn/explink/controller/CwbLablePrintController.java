package cn.explink.controller;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
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
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.explink.b2c.saohuobang.SaohuobangDao;
import cn.explink.b2c.saohuobang.Saohuobanginfo;
import cn.explink.b2c.tools.B2cEnum;
import cn.explink.dao.BaleDao;
import cn.explink.dao.BranchDAO;
import cn.explink.dao.BranchRouteDAO;
import cn.explink.dao.CommonDAO;
import cn.explink.dao.ComplaintDAO;
import cn.explink.dao.CustomWareHouseDAO;
import cn.explink.dao.CustomerDAO;
import cn.explink.dao.CwbDAO;
import cn.explink.dao.DeliveryStateDAO;
import cn.explink.dao.EmailDateDAO;
import cn.explink.dao.ExceedFeeDAO;
import cn.explink.dao.ExceptionCwbDAO;
import cn.explink.dao.ExportmouldDAO;
import cn.explink.dao.GotoClassAuditingDAO;
import cn.explink.dao.GroupDetailDao;
import cn.explink.dao.MenuDAO;
import cn.explink.dao.OrderFlowDAO;
import cn.explink.dao.OutWarehouseGroupDAO;
import cn.explink.dao.ReasonDao;
import cn.explink.dao.RemarkDAO;
import cn.explink.dao.StockResultDAO;
import cn.explink.dao.SwitchDAO;
import cn.explink.dao.SystemInstallDAO;
import cn.explink.dao.TruckDAO;
import cn.explink.dao.TuihuoRecordDAO;
import cn.explink.dao.UserDAO;
import cn.explink.dao.YpdjHandleRecordDAO;
import cn.explink.domain.Branch;
import cn.explink.domain.Common;
import cn.explink.domain.Complaint;
import cn.explink.domain.CustomWareHouse;
import cn.explink.domain.Customer;
import cn.explink.domain.CwbOrder;
import cn.explink.domain.DeliveryState;
import cn.explink.domain.EmailDate;
import cn.explink.domain.PrintOrderLabelVo;
import cn.explink.domain.Reason;
import cn.explink.domain.Remark;
import cn.explink.domain.SetExportField;
import cn.explink.domain.TuihuoRecord;
import cn.explink.domain.User;
import cn.explink.enumutil.CwbOrderPDAEnum;
import cn.explink.exception.CwbException;
import cn.explink.service.CwbOrderService;
import cn.explink.service.CwbRouteService;
import cn.explink.service.DataStatisticsService;
import cn.explink.service.ExplinkUserDetail;
import cn.explink.service.ExportService;
import cn.explink.util.ExcelUtils;
import cn.explink.util.Page;
import cn.explink.util.ServiceUtil;
import cn.explink.util.StreamingStatementCreator;

@RequestMapping("/cwbLablePrint")
@Controller
public class CwbLablePrintController {
	private Logger logger = LoggerFactory.getLogger(CwbLablePrintController.class);
	@Autowired
	CwbDAO cwbDAO;
	@Autowired
	BranchDAO branchDAO;
	@Autowired
	OrderFlowDAO orderFlowDAO;
	@Autowired
	ReasonDao reasonDAO;
	@Autowired
	CwbOrderService cwborderService;
	@Autowired
	OutWarehouseGroupDAO outWarehouseGroupDAO;
	@Autowired
	CustomerDAO customerDAO;
	@Autowired
	UserDAO userDAO;
	@Autowired
	StockResultDAO stockResultDAO;
	@Autowired
	TruckDAO truckDAO;
	@Autowired
	GroupDetailDao groupdetailDAO;
	@Autowired
	MenuDAO menuDAO;
	@Autowired
	DeliveryStateDAO deliveryStateDAO;
	@Autowired
	ExceptionCwbDAO exceptionCwbDAO;
	@Autowired
	GroupDetailDao groupDetailDAO;
	@Autowired
	BaleDao baleDAO;
	@Autowired
	SwitchDAO switchDAO;
	@Autowired
	CommonDAO commonDAO;
	@Autowired
	EmailDateDAO emaildateDAO;
	@Autowired
	JdbcTemplate jdbcTemplate;

	@Autowired
	CwbRouteService cwbRouteService;
	@Autowired
	BranchRouteDAO branchRouteDAO;
	@Autowired
	ExceedFeeDAO exceedFeeDAO;
	@Autowired
	EmailDateDAO emailDateDAO;
	@Autowired
	ExportmouldDAO exportmouldDAO;
	@Autowired
	ExportService exportService;
	@Autowired
	CustomWareHouseDAO customWareHouseDAO;
	@Autowired
	SystemInstallDAO systemInstallDAO;
	@Autowired
	RemarkDAO remarkDAO;
	@Autowired
	YpdjHandleRecordDAO ypdjHandleRecordDAO;

	@Autowired
	DataStatisticsService dataStatisticsService;
	@Autowired
	GotoClassAuditingDAO gotoClassAuditingDAO;

	@Autowired
	SecurityContextHolderStrategy securityContextHolderStrategy;
	@Autowired
	TuihuoRecordDAO tuihuoRecordDAO;
	@Autowired
	SaohuobangDao saohuobangDao;
	@Autowired
	ComplaintDAO complaintDAO;

	private User getSessionUser() {
		ExplinkUserDetail userDetail = (ExplinkUserDetail) this.securityContextHolderStrategy.getContext().getAuthentication().getPrincipal();
		return userDetail.getUser();
	}

	/**
	 * 标签打印功能列表
	 *
	 * @param model
	 * @param cwbs
	 * @param emaildateid
	 * @return
	 */
	@RequestMapping("/cwblableprint")
	public String cwblableprint(Model model, @RequestParam(value = "cwbs", required = false, defaultValue = "") String cwbs,
			@RequestParam(value = "emaildateid", required = true, defaultValue = "0") long emaildateid) {
		List<Customer> customerlist = this.customerDAO.getAllIsAutoProductcwbCustomers();

		String logo = this.systemInstallDAO.getSystemInstall("Logo") == null ? "XHM_LOGO.jpg" : this.systemInstallDAO.getSystemInstall("Logo").getValue();

		String printtemplet = logo.split("_")[0].toLowerCase();

		if (printtemplet.equals("hwhq")) {
			List<Customer> saohuobangList = this.customerDAO.getCustomerByCustomernameCheck(B2cEnum.saohuobang.getText());
			customerlist.addAll(saohuobangList);
		}
		if (printtemplet.equals("xjdcb")) {
			model.addAttribute("xjdcb", printtemplet);
		}
		model.addAttribute("customerList", customerlist);
		model.addAttribute("branchList", this.branchDAO.getAllEffectBranches());
		List<CwbOrder> clist = new ArrayList<CwbOrder>();
		if ("dd".equals(printtemplet)) {
			model.addAttribute("dd", printtemplet);
		}
		if ((cwbs.length() > 0) || (emaildateid != 0)) {
			if (cwbs.length() > 0) {
				String quot = "'", quotAndComma = "',";
				StringBuffer cwbstr = new StringBuffer();
				for (String cwbStr : cwbs.split("\r\n")) {
					if (cwbStr.trim().length() == 0) {
						continue;
					}
					cwbStr = cwbStr.trim();
					cwbstr = cwbstr.append(quot).append(cwbStr).append(quotAndComma);
				}
				clist = this.cwbDAO.getCwbByCwbs(cwbstr.substring(0, cwbstr.length() - 1));
			}
			if (emaildateid != 0) {
				clist = this.cwbDAO.getCwbsByEmailDateId(emaildateid);
			}
		}

		model.addAttribute("clist", clist);
		return "cwblableprint/cwblableprintlist";
	}

	/**
	 * 标签打印-打印列表中的订单
	 *
	 * @param model
	 * @param request
	 * @param isprint
	 * @return
	 */
	@RequestMapping("/cwblableprint_xhm")
	public String cwblableprint_xhm(Model model, HttpServletRequest request, @RequestParam(value = "isprint", defaultValue = "", required = true) String[] isprint,
			@RequestParam(value = "moudle", defaultValue = "", required = false) String moudle) {
		String cwbs = "";
		for (int i = 0; i < isprint.length; i++) {
			cwbs += "'" + isprint[i] + "',";
		}

		if (cwbs.length() > 0) {
			cwbs = cwbs.substring(0, cwbs.length() - 1);
		}
		List<CwbOrder> cwbList = this.cwbDAO.getCwbByCwbs(cwbs);
		String logo = this.systemInstallDAO.getSystemInstall("Logo") == null ? "EXPLINK_LOGO.png" : this.systemInstallDAO.getSystemInstall("Logo").getValue();
		if (moudle.length() > 0) {
			logo = moudle;
		}
		String printtemplet = logo.split("_")[0].toLowerCase();
		if (printtemplet.equals("hwhq")) {
			return this.lableprintforhwhqsaohuobang(model, cwbs);
		}
		model.addAttribute("logo", logo);
		model.addAttribute("localbranchname", this.branchDAO.getBranchByBranchid(this.getSessionUser().getBranchid()).getBranchname());
		model.addAttribute("customerlist", this.customerDAO.getAllCustomers());
		model.addAttribute("branchlist", this.branchDAO.getAllEffectBranches());
		model.addAttribute("userlist", this.userDAO.getAllUser());

		model.addAttribute("cwbList", cwbList);
		return "cwblableprint/cwblableprint_" + printtemplet;
	}

	private String lableprintforhwhqsaohuobang(Model model, String cwbs) {
		List<Saohuobanginfo> saolist = this.saohuobangDao.getSaohuobangOrderByCwbs(cwbs);

		model.addAttribute("saolist", saolist);
		return "cwblableprint/cwblableprint_hwhq";
	}

	/**
	 * 小标签打印功能
	 *
	 * @param model
	 * @param cwbs
	 * @param emaildateid
	 * @return
	 */
	@RequestMapping("/cwblittlelableprintlist")
	public String cwblittlelableprintlist(Model model, @RequestParam(value = "cwbs", required = false, defaultValue = "") String cwbs,
			@RequestParam(value = "emaildateid", required = true, defaultValue = "0") long emaildateid) {
		model.addAttribute("customerList", this.customerDAO.getCustomersByQuanKai());
		List<CwbOrder> clist = new ArrayList<CwbOrder>();
		if ((cwbs.length() > 0) || (emaildateid != 0)) {
			if (cwbs.length() > 0) {

				String quot = "'", quotAndComma = "',";
				StringBuffer cwbstr = new StringBuffer();
				for (String cwb : cwbs.split("\r\n")) {
					if (cwb.trim().length() == 0) {
						continue;
					}

					cwb = this.cwborderService.translateCwb(cwb);
					cwbstr = cwbstr.append(quot).append(cwb).append(quotAndComma);
				}
				clist = this.cwbDAO.getCwbByCwbs(cwbstr.substring(0, cwbstr.length() - 1));
			}
			if (emaildateid != 0) {
				clist = this.cwbDAO.getCwbsByEmailDateId(emaildateid);
			}
		}
		model.addAttribute("clist", clist);
		return "cwblableprint/cwblittlelableprintlist";
	}

	/**
	 * 小标签打印-打印列表中的订单
	 *
	 * @param model
	 * @param request
	 * @param isprint
	 * @return
	 */
	@RequestMapping("/cwblittlelableprint_xhm")
	public String cwblittlelableprint_xhm(Model model, HttpServletRequest request, @RequestParam(value = "isprint", defaultValue = "", required = true) String[] isprint) {
		String cwbs = "";
		for (int i = 0; i < isprint.length; i++) {
			cwbs += "'" + isprint[i] + "',";
		}

		if (cwbs.length() > 0) {
			cwbs = cwbs.substring(0, cwbs.length() - 1);
		}
		List<CwbOrder> cwbList = this.cwbDAO.getCwbByCwbs(cwbs);

		model.addAttribute("cwbList", cwbList);
		return "cwblableprint/cwblittlelableprint_xhm";
	}

	/**
	 * 条码打印功能
	 *
	 * @param model
	 * @param cwbs
	 * @param emaildateid
	 * @return
	 */
	@RequestMapping("/cwbbarcodeprintlist")
	public String cwbbarcodeprintlist(Model model, @RequestParam(value = "cwbs", required = false, defaultValue = "") String cwbs,
			@RequestParam(value = "emaildateid", required = true, defaultValue = "0") long emaildateid) {
		model.addAttribute("customerList", this.customerDAO.getCustomersByQuanKai());
		List<CwbOrder> clist = new ArrayList<CwbOrder>();
		if ((cwbs.length() > 0) || (emaildateid != 0)) {
			if (cwbs.length() > 0) {

				String quot = "'", quotAndComma = "',";
				StringBuffer cwbstr = new StringBuffer();
				for (String cwb : cwbs.split("\r\n")) {
					if (cwb.trim().length() == 0) {
						continue;
					}

					cwb = this.cwborderService.translateCwb(cwb);
					cwbstr = cwbstr.append(quot).append(cwb).append(quotAndComma);
				}
				clist = this.cwbDAO.getCwbByCwbs(cwbstr.substring(0, cwbstr.length() - 1));
			}
			if (emaildateid != 0) {
				clist = this.cwbDAO.getCwbsByEmailDateId(emaildateid);
			}
		}
		model.addAttribute("clist", clist);
		return "cwblableprint/cwbbarcodeprintlist";
	}

	/**
	 * 条码打印-打印列表中的订单
	 *
	 * @param model
	 * @param request
	 * @param isprint
	 * @return
	 */
	@RequestMapping("/cwbbarcodeprint_xhm")
	public String cwbbarcodeprint_xhm(Model model, HttpServletRequest request, @RequestParam(value = "isprint", defaultValue = "", required = true) String[] isprint) {
		String cwbs = "";
		for (int i = 0; i < isprint.length; i++) {
			cwbs += "'" + isprint[i] + "',";
		}

		if (cwbs.length() > 0) {
			cwbs = cwbs.substring(0, cwbs.length() - 1);
		}
		List<CwbOrder> cwbList = this.cwbDAO.getCwbByCwbs(cwbs);

		model.addAttribute("cwbList", cwbList);
		return "cwblableprint/cwbbarcodeprint_xhm";
	}

	/**
	 * 根据供货商切换供货商对应的发货批次
	 *
	 * @param model
	 * @param customerid
	 * @return
	 */
	@RequestMapping("/updateEmaildateid")
	public @ResponseBody List<EmailDate> updateEmaildateid(Model model, @RequestParam(value = "customerid", defaultValue = "0") long customerid) {
		return this.emaildateDAO.getEmailDateByCustomerid(customerid);
	}

	@ExceptionHandler(CwbException.class)
	public @ResponseBody ExplinkResponse handleCwbException(CwbException ex, HttpServletRequest request) {
		this.logger.error("系统异常", ex);
		ExplinkResponse explinkResponse = new ExplinkResponse(ex.getError().getValue() + "", ex.getMessage(), null);
		if (explinkResponse.getStatuscode().equals(CwbOrderPDAEnum.OK.getCode())) {
			explinkResponse.setWavPath(request.getContextPath() + ServiceUtil.waverrorPath + CwbOrderPDAEnum.OK.getVediourl());
		} else {
			explinkResponse.setWavPath(request.getContextPath() + ServiceUtil.waverrorPath + CwbOrderPDAEnum.SYS_ERROR.getVediourl());
		}
		return explinkResponse;
	}

	@ExceptionHandler(Exception.class)
	public @ResponseBody ExplinkResponse handleException(Exception ex, HttpServletRequest request) {
		this.logger.error("系统异常", ex);
		ExplinkResponse explinkResponse = new ExplinkResponse("000001", ex.getMessage(), null);
		if (explinkResponse.getStatuscode().equals(CwbOrderPDAEnum.OK.getCode())) {
			explinkResponse.setWavPath(request.getContextPath() + ServiceUtil.waverrorPath + CwbOrderPDAEnum.OK.getVediourl());
		} else {
			explinkResponse.setWavPath(request.getContextPath() + ServiceUtil.waverrorPath + CwbOrderPDAEnum.SYS_ERROR.getVediourl());
		}
		return explinkResponse;
	}

	/**
	 * 入库、到货（明细）、领货（明细）功能的导出数据功能
	 *
	 * @param model
	 * @param response
	 * @param request
	 * @param cwbs
	 */

	@RequestMapping("/exportExcle")
	public void exportExcle(Model model, HttpServletResponse response, HttpServletRequest request, @RequestParam(value = "cwbs", required = false, defaultValue = "") final String cwbs) {

		String[] cloumnName1 = {}; // 导出的列名
		String[] cloumnName2 = {}; // 导出的英文列名
		String[] cloumnName3 = {}; // 导出的数据类型

		List<SetExportField> listSetExportField = this.exportmouldDAO.getSetExportFieldByStrs("0");
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

			final String sql = this.cwbDAO.getSQLExportKeFu(cwbs);

			ExcelUtils excelUtil = new ExcelUtils() { // 生成工具类实例，并实现填充数据的抽象方法
				@Override
				public void fillData(final Sheet sheet, final CellStyle style) {
					final List<User> uList = CwbLablePrintController.this.userDAO.getAllUser();
					final Map<Long, Customer> cMap = CwbLablePrintController.this.customerDAO.getAllCustomersToMap();
					final List<Branch> bList = CwbLablePrintController.this.branchDAO.getAllBranches();
					final List<Common> commonList = CwbLablePrintController.this.commonDAO.getAllCommons();
					final List<CustomWareHouse> cWList = CwbLablePrintController.this.customWareHouseDAO.getAllCustomWareHouse();
					List<Remark> remarkList = CwbLablePrintController.this.remarkDAO.getRemarkByCwbs(cwbs);
					final Map<String, Map<String, String>> remarkMap = CwbLablePrintController.this.exportService.getInwarhouseRemarks(remarkList);
					final List<Reason> reasonList = CwbLablePrintController.this.reasonDAO.getAllReason();
					CwbLablePrintController.this.jdbcTemplate.query(new StreamingStatementCreator(sql), new ResultSetExtractor<Object>() {
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

						private void writeSingle(Map<String, Object> mapRow, TuihuoRecord tuihuoRecord, DeliveryState ds, Map<String, String> allTime, int rownum, Map<String, String> cwbspayupMsp,
								Map<String, String> complaintMap) throws SQLException {
							Row row = sheet.createRow(rownum + 1);
							row.setHeightInPoints(15);
							for (int i = 0; i < cloumnName4.length; i++) {
								Cell cell = row.createCell((short) i);
								cell.setCellStyle(style);
								// sheet.setColumnWidth(i, (short) (5000));
								// //设置列宽
								Object a = CwbLablePrintController.this.exportService.setObjectA(cloumnName5, mapRow, i, uList, cMap, bList, commonList, tuihuoRecord, ds, allTime, cWList, remarkMap,
										reasonList, cwbspayupMsp, complaintMap);
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

								Map<String, Map<String, String>> orderflowList = CwbLablePrintController.this.dataStatisticsService.getOrderFlowByCredateForDetailAndExportAllTime(cwbs, bList);
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
							for (TuihuoRecord tuihuoRecord : CwbLablePrintController.this.tuihuoRecordDAO.getTuihuoRecordByCwbs(cwbs)) {
								map.put(tuihuoRecord.getCwb(), tuihuoRecord);
							}
							return map;
						}

						private Map<String, DeliveryState> getDeliveryListByCwbs(List<String> cwbs) {
							Map<String, DeliveryState> map = new HashMap<String, DeliveryState>();
							for (DeliveryState deliveryState : CwbLablePrintController.this.deliveryStateDAO.getActiveDeliveryStateByCwbs(cwbs)) {
								map.put(deliveryState.getCwb(), deliveryState);
							}
							return map;
						}

						private Map<String, String> getComplaintMap(List<String> cwbs) {
							Map<String, String> complaintMap = new HashMap<String, String>();
							for (Complaint complaint : CwbLablePrintController.this.complaintDAO.getActiveComplaintByCwbs(cwbs)) {
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
					/*
					 * jdbcTemplate.query(new StreamingStatementCreator(sql),
					 * new RowCallbackHandler(){ private int count=0;
					 *
					 * @Override public void processRow(ResultSet rs) throws
					 * SQLException { Row row = sheet.createRow(count + 1);
					 * row.setHeightInPoints((float) 15);
					 *
					 * DeliveryState ds = getDeliveryByCwb(rs.getString("cwb"));
					 * Map<String,String> allTime =
					 * getOrderFlowByCredateForDetailAndExportAllTime
					 * (rs.getString("cwb"));
					 *
					 * for (int i = 0; i < cloumnName4.length; i++) { Cell cell
					 * = row.createCell((short) i); cell.setCellStyle(style);
					 * Object a = exportService.setObjectA(cloumnName5, rs, i ,
					 * uList
					 * ,cMap,bList,commonList,ds,allTime,cWList,remarkMap,reasonList
					 * ); if(cloumnName6[i].equals("double")){
					 * cell.setCellValue(a == null ?
					 * BigDecimal.ZERO.doubleValue() :
					 * a.equals("")?BigDecimal.ZERO
					 * .doubleValue():Double.parseDouble(a.toString())); }else{
					 * cell.setCellValue(a == null ? "" : a.toString()); } }
					 * count++;
					 *
					 * }});
					 */

				}
			};
			excelUtil.excel(response, cloumnName4, sheetName, fileName);

		} catch (Exception e) {
			logger.error("", e);
		}
	}

	/**
	 * 查询订单的配送结果
	 *
	 * @param cwb
	 * @return
	 */
	public DeliveryState getDeliveryByCwb(String cwb) {
		List<DeliveryState> delvieryList = this.deliveryStateDAO.getDeliveryStateByCwb(cwb);
		return delvieryList.size() > 0 ? delvieryList.get(delvieryList.size() - 1) : new DeliveryState();
	}

	/**
	 * 条形码打印
	 *
	 * @return
	 */
	@RequestMapping("/barcodeprint")
	public String BarcodePrint(Model model, @RequestParam(value = "textfield", required = true, defaultValue = "0") String cwbs,
			@RequestParam(value = "isshow", required = true, defaultValue = "0") long ishow, @RequestParam(value = "typeid", required = true, defaultValue = "cwb") String typeid) {
		if (ishow == 1) {
			List<String> list = new ArrayList<String>();
			int right = 0;
			int left = 0;
			if (cwbs.length() > 0) {
				for (String cwb : cwbs.split("\r\n")) {
					if (right >= 1000) {
						break;
					}
					left++;
					if (typeid.equals("cwb")) {
						if ((cwb.trim().length() == 0) || (cwb.trim().length() > 9)) {
							continue;
						}
					} else if (typeid.equals("baleno")) {
						if ((cwb.trim().length() == 0) || (cwb.trim().length() > 21)) {
							continue;
						}
					}
					right++;
					list.add(cwb);
				}
			}
			model.addAttribute("typeid", typeid);
			model.addAttribute("list", list);
			model.addAttribute("right", right);
			model.addAttribute("left", left);
		}
		return "cwblableprint/cwbbarcodeprint";
	}

	/**
	 * 随机打印条码
	 */
	@RequestMapping("/randomcodeprint")
	public String RandomcodePrint(Model model, HttpServletResponse response, HttpServletRequest request, @RequestParam(value = "textfield", required = false, defaultValue = "0") final String num,
			@RequestParam(value = "isshow", required = true, defaultValue = "0") long ishow, @RequestParam(value = "typeid", required = true, defaultValue = "cwb") String typeid) {
		if (ishow > 0) {
			List<String> slist = new ArrayList<String>();
			if (!num.equals("") && (num.trim().length() > 0)) {

				for (int i = 0; i < Integer.valueOf(num.trim()); i++) {
					if (Integer.valueOf(num.trim()) > 1000) {
						break;
					}
					if (typeid.equals("cwb")) {
						long a = System.currentTimeMillis();
						int cwb = (int) (Math.random() * 1000);
						long ss = a + cwb;
						String re = String.valueOf(ss).substring(5);
						String cwbs = "Z" + String.valueOf(re);
						if (!slist.contains(cwbs)) {
							slist.add(cwbs);
						} else {
							String recwb = cwbs.replace("Z", "G");
							slist.add(recwb);
						}
					} else if (typeid.equals("baleno")) {
						try {
							Thread.sleep(1);
						} catch (InterruptedException e) {
							logger.error("", e);
						}
						slist.add(new Date().getTime() + "");
					}

				}
			}
			model.addAttribute("slist", slist);
		}
		model.addAttribute("typeid", typeid);
		model.addAttribute("textfield", num);
		return "cwblableprint/cwbrandomCode";
	}

	/**
	 * 打印条码
	 */
	@RequestMapping("/printExcle")
	public String printExcle(Model model, HttpServletResponse response, HttpServletRequest request, @RequestParam(value = "cwbs", required = false, defaultValue = "") final String cwbs) {
		List<String> c = new ArrayList<String>();
		String[] a = cwbs.split(",");
		for (String b : a) {
			c.add(b);
		}
		model.addAttribute("cwbs", c);
		return "cwblableprint/printTiaoxingma";
	}

	@RequestMapping("/printBranchcode")
	public String printBranchcode(Model model, HttpServletResponse response, HttpServletRequest request, @RequestParam(value = "branchids", required = false, defaultValue = "") final String branchids) {
		List<String> c = new ArrayList<String>();
		String[] a = branchids.split(",");
		for (String b : a) {
			c.add(b);
		}
		model.addAttribute("branchids", c);
		return "cwblableprint/printBranchcode";
	}

	@RequestMapping("/branchcodeprint/{page}")
	public String list(@PathVariable("page") long page, Model model, @RequestParam(value = "branchname", required = false, defaultValue = "") String branchname,
			@RequestParam(value = "branchaddress", required = false, defaultValue = "") String branchaddress, @RequestParam(value = "sitetype", required = false, defaultValue = "0") int sitetype,
			@RequestParam(value = "pagesize", required = false, defaultValue = "10") int pagesize) {

		model.addAttribute("branches", this.branchDAO.getBranchByPage(page, branchname, branchaddress, sitetype, pagesize));
		model.addAttribute("page_obj", new Page(this.branchDAO.getBranchCount(branchname, branchaddress, sitetype, pagesize), page, pagesize));
		model.addAttribute("page", page);
		model.addAttribute("sitetype", sitetype);
		model.addAttribute("pagesize", pagesize);
		return "cwblableprint/branchcodeprint";
	}
	
	/**
	 * 面单打印
	 * 2016年5月23日 上午11:01:30
	 * @return
	 * @throws ParseException 
	 */
	@RequestMapping("/printOrderLabel")
	public String printOrderLabel(Model model, @RequestParam(value = "cwb") List<String> cwbList) throws ParseException {
		List<CwbOrder> cwbOrderList = this.cwborderService.getCwbOrderListByCwbsWithoutState(cwbList);
		List<PrintOrderLabelVo> printOrderLabelVoList = new ArrayList<PrintOrderLabelVo>();
		for(CwbOrder cwbOrder : cwbOrderList) {
			PrintOrderLabelVo vo = new PrintOrderLabelVo();
			// 日期格式转换
			if(StringUtils.isNotBlank(cwbOrder.getEmaildate())) {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				Date emaildate = sdf.parse(cwbOrder.getEmaildate());
				sdf = new SimpleDateFormat("yyyy-M-d");
				String emaildateStr = sdf.format(emaildate);
				cwbOrder.setEmaildate(emaildateStr);
			}
			// 费用合计 = 运费+包装+保价
			BigDecimal totalfee = BigDecimal.ZERO;
			if(cwbOrder.getShouldfare() != null) {
				totalfee = totalfee.add(cwbOrder.getShouldfare());
			}
			if(cwbOrder.getInsuredfee() != null) {
				totalfee = totalfee.add(cwbOrder.getInsuredfee());
			}
			if(cwbOrder.getPackagefee() != null) {
				totalfee = totalfee.add(cwbOrder.getPackagefee());
			}
			cwbOrder.setTotalfee(totalfee);
			vo.setCwbOrder(cwbOrder);
			Branch branch = null;
			if(cwbOrder.getDeliverybranchid() != 0) {
				branch = this.branchDAO.getBranchByBranchid(cwbOrder.getDeliverybranchid());
			}
			if(branch == null) {
				branch = new Branch();
			}
			vo.setBranch(branch);
			printOrderLabelVoList.add(vo);
		}
		model.addAttribute("printOrderLabelVoList", printOrderLabelVoList);
		return "cwblableprint/printOrderLabel";
	}
}

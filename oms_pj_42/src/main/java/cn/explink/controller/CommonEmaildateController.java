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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
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
import cn.explink.dao.CommonEmaildateDAO;
import cn.explink.dao.CustomerDAO;
import cn.explink.dao.CwbDAO;
import cn.explink.dao.ExportmouldDAO;
import cn.explink.dao.GetDmpDAO;
import cn.explink.dao.WarehouseCommenDAO;
import cn.explink.domain.Branch;
import cn.explink.domain.Common;
import cn.explink.domain.CommonEmaildate;
import cn.explink.domain.CustomWareHouse;
import cn.explink.domain.Customer;
import cn.explink.domain.CwbOrder;
import cn.explink.domain.CwbOrderAll;

import cn.explink.domain.Reason;
import cn.explink.domain.Remark;
import cn.explink.domain.SetExportField;
import cn.explink.domain.User;
import cn.explink.domain.WarehouseToCommen;
import cn.explink.service.CommenService;
import cn.explink.service.ExportExcelService;
import cn.explink.util.ExcelUtils;
import cn.explink.util.Page;
import cn.explink.util.StreamingStatementCreator;

@RequestMapping("/commonemail")
@Controller
public class CommonEmaildateController {

	@Autowired
	CommonEmaildateDAO commonEmaildateDAO;
	@Autowired
	GetDmpDAO getDmpDAO;
	@Autowired
	BranchDAO branchDAO;
	@Autowired
	WarehouseCommenDAO warehouseCommenDAO;
	@Autowired
	CwbDAO cwbDAO;
	@Autowired
	CustomerDAO customerDAO;
	@Autowired
	CommenService commenService;
	@Autowired
	CommonDAO commonDAO;
	@Autowired
	ExportmouldDAO exportmouldDAO;
	@Autowired
	ExportExcelService exportExcelService;
	@Autowired
	JdbcTemplate jdbcTemplate;

	@RequestMapping("/list/{page}")
	public String list(@PathVariable("page") long page, Model model, @RequestParam(value = "isshow", required = false, defaultValue = "0") long isshow,
			@RequestParam(value = "commonnumber", required = false, defaultValue = "") String commoncode, @RequestParam(value = "startdate", required = false, defaultValue = "") String startdate,
			@RequestParam(value = "enddate", required = false, defaultValue = "") String enddate) {
		List<CommonEmaildate> commonEmaildateList = new ArrayList<CommonEmaildate>();
		long count = 0;
		if (isshow == 1) {
			commonEmaildateList = commonEmaildateDAO.getAllCommonEmaildatePage(commoncode, startdate, enddate, page);
			count = commonEmaildateDAO.getAllCommonEmaildateCount(commoncode, startdate, enddate);
		}
		List<Common> commonList = getDmpDAO.getAllCommons();
		Map<String, String> comMap = new HashMap<String, String>();
		if (commonList != null && commonList.size() > 0) {
			for (Common common : commonList) {
				comMap.put(common.getCommonnumber(), common.getCommonname());
			}
		}
		String dmpURl = getDmpDAO.getDmpurl().substring(getDmpDAO.getDmpurl().lastIndexOf("/"), getDmpDAO.getDmpurl().length());
		model.addAttribute("dmpUrl", dmpURl);
		model.addAttribute("comMap", comMap);
		model.addAttribute("commList", commonList);
		model.addAttribute("commonEmaildateList", commonEmaildateList);
		model.addAttribute("page_obj", new Page(count, page, Page.ONE_PAGE_NUMBER));
		model.addAttribute("page", page);
		return "common/showlist";
	}

	@RequestMapping("/show/{emaildateid}/{page}")
	public String show(HttpServletRequest request, Model model, @PathVariable("emaildateid") long emaildateid, @PathVariable("page") long page) {
		String dmpid = request.getSession().getAttribute("dmpid") == null ? "" : request.getSession().getAttribute("dmpid").toString();
		User user = getDmpDAO.getLogUser(dmpid);
		List<Branch> branchs = getDmpDAO.getAllBranchs();
		List<Customer> customers = getDmpDAO.getAllCustomers();
		List<CustomWareHouse> customWareHouses = getDmpDAO.getCustomWareHouse();
		List<Common> commonList = getDmpDAO.getAllCommons();
		String commoncode = "";
		CommonEmaildate ce = commonEmaildateDAO.getAllCommonEmaildateByEmaildateid(emaildateid);
		if (ce != null) {
			commoncode = ce.getCommoncode();
		}
		List<CwbOrder> cList = cwbDAO.getCwbByEmaildate(emaildateid, page);
		List<WarehouseToCommen> wList = warehouseCommenDAO.getCountByEmaildateId(emaildateid);

		Map<String, String> stateTimeMap = getMapForStateTime(wList);
		List<CwbOrderAll> cwbOrderView = new ArrayList<CwbOrderAll>();
		cwbOrderView = commenService.getCwborderView(cList, customers, branchs, commonList, customWareHouses, commoncode);
		long count = 0;
		count = warehouseCommenDAO.getCountByEmaildateIdAndStatetime(emaildateid);

		Map<String, String> comMap = new HashMap<String, String>();
		if (commonList != null && commonList.size() > 0) {
			for (Common common : commonList) {
				comMap.put(common.getCommonnumber(), common.getCommonname());
			}
		}
		model.addAttribute("emaildateid", emaildateid);
		model.addAttribute("stateTimeMap", stateTimeMap);
		model.addAttribute("comMap", comMap);
		model.addAttribute("exportmouldlist", getDmpDAO.getExportmoulds(user, dmpid));
		model.addAttribute("page_obj", new Page(count, page, Page.ONE_PAGE_NUMBER));
		model.addAttribute("page", page);
		model.addAttribute("cwborderList", cwbOrderView);
		String dmpURl = getDmpDAO.getDmpurl().substring(getDmpDAO.getDmpurl().lastIndexOf("/"), getDmpDAO.getDmpurl().length());
		model.addAttribute("dmpUrl", dmpURl);
		return "/common/showDetail";
	}

	@RequestMapping("/showdetailbycwbs/{page}")
	public String showdetailbycwbs(HttpServletRequest request, @RequestParam(value = "cwbs", required = false, defaultValue = "") String cwbs, Model model, @PathVariable("page") long page,
			@RequestParam(value = "isshow", required = false, defaultValue = "0") long isshow) {
		long count = 0l;
		if (isshow > 0) {
			String[] cwbArray = cwbs.trim().split("\r\n");
			String s = "'";
			String s1 = "',";
			StringBuffer cwbsSqlBuffer = new StringBuffer();
			for (String cwb : cwbArray) {
				if (cwb.trim().length() == 0) {
					continue;
				}
				if (cwb.length() > 0) {
					cwbsSqlBuffer = cwbsSqlBuffer.append(s).append(cwb).append(s1);
				}
			}
			if (cwbsSqlBuffer.length() == 0) {
				return "redirect:/commonemail/list/1";
			}

			String cwbsString = cwbsSqlBuffer.substring(0, cwbsSqlBuffer.length() - 1);
			String dmpid = request.getSession().getAttribute("dmpid") == null ? "" : request.getSession().getAttribute("dmpid").toString();
			User user = getDmpDAO.getLogUser(dmpid);
			List<Branch> branchs = getDmpDAO.getAllBranchs();
			List<Customer> customers = getDmpDAO.getAllCustomers();
			List<CustomWareHouse> customWareHouses = getDmpDAO.getCustomWareHouse();
			List<Common> commonList = getDmpDAO.getAllCommons();
			List<CwbOrder> cList = cwbDAO.getCwbByCwbsAndEmaildate(cwbsString, page);
			List<WarehouseToCommen> wList = warehouseCommenDAO.getCountByCwbs(cwbsString);
			Map<String, String> stateTimeMap = getMapForStateTime(wList);
			// 订单 跟承运商 map
			Map<String, String> cwbForcommonMap = getMapForCode(wList);
			List<CwbOrderAll> cwbOrderView = new ArrayList<CwbOrderAll>();
			cwbOrderView = commenService.getCwborderViewForCwbs(cList, customers, branchs, commonList, customWareHouses, cwbForcommonMap);
			// 承运商 map
			Map<String, String> comMap = new HashMap<String, String>();
			if (commonList != null && commonList.size() > 0) {
				for (Common common : commonList) {
					comMap.put(common.getCommonnumber(), common.getCommonname());
				}
			}

			count = wList.size();
			model.addAttribute("stateTimeMap", stateTimeMap);
			model.addAttribute("comMap", comMap);
			model.addAttribute("exportmouldlist", getDmpDAO.getExportmoulds(user, dmpid));
			model.addAttribute("cwborderList", cwbOrderView);

		}
		String dmpURl = getDmpDAO.getDmpurl().substring(getDmpDAO.getDmpurl().lastIndexOf("/"), getDmpDAO.getDmpurl().length());
		model.addAttribute("dmpUrl", dmpURl);
		model.addAttribute("page_obj", new Page(count, page, Page.ONE_PAGE_NUMBER));
		model.addAttribute("page", page);
		return "common/showdetailbycwbs";
	}

	private Map<String, String> getMapForCode(List<WarehouseToCommen> wList) {
		Map<String, String> map = new HashMap<String, String>();
		for (WarehouseToCommen warehouseToCommen : wList) {
			map.put(warehouseToCommen.getCwb(), warehouseToCommen.getCommencode());
		}
		return map;
	}

	private Map<String, String> getMapForStateTime(List<WarehouseToCommen> wList) {
		Map<String, String> mapForStateTime = new HashMap<String, String>();
		for (WarehouseToCommen w : wList) {
			mapForStateTime.put(w.getCwb(), w.getStatetime());
		}
		return mapForStateTime;
	}

	@RequestMapping("/exportForcwbs")
	public void exportForcwbs(HttpServletResponse response, HttpServletRequest request, @RequestParam(value = "exportmould2") String mouldfieldids2, @RequestParam(value = "cwbs") String cwbs) {

		String[] cwbArray = cwbs.trim().split("\r\n");
		String s = "'";
		String s1 = "',";
		StringBuffer cwbsSqlBuffer = new StringBuffer();
		for (String cwb : cwbArray) {
			if (cwb.trim().length() == 0) {
				continue;
			}
			if (cwb.length() > 0) {
				cwbsSqlBuffer = cwbsSqlBuffer.append(s).append(cwb).append(s1);
			}
		}
		if (cwbsSqlBuffer.length() == 0) {
			return;
		}
		cwbs = cwbsSqlBuffer.substring(0, cwbsSqlBuffer.length() - 1);
		// String mouldfieldids2 =request.getParameter("exportmould2"); // 导出模板
		String[] cloumnName1 = {}; // 导出的列名
		String[] cloumnName2 = {}; // 导出的英文列名
		String[] cloumnName3 = {}; // 导出的数据类型

		if (mouldfieldids2 != null && !"0".equals(mouldfieldids2)) { // 选择模板
			List<SetExportField> listSetExportField = getDmpDAO.getSetExportFieldByExportstate(mouldfieldids2);
			cloumnName1 = new String[listSetExportField.size()];
			cloumnName2 = new String[listSetExportField.size()];
			cloumnName3 = new String[listSetExportField.size()];
			for (int k = 0, j = 0; j < listSetExportField.size(); j++, k++) {
				cloumnName1[k] = listSetExportField.get(j).getFieldname();
				cloumnName2[k] = listSetExportField.get(j).getFieldenglishname();
				cloumnName3[k] = listSetExportField.get(j).getExportdatatype();
			}
		} else {
			List<SetExportField> listSetExportField = getDmpDAO.getSetExportFieldByExportstate("0");
			cloumnName1 = new String[listSetExportField.size()];
			cloumnName2 = new String[listSetExportField.size()];
			cloumnName3 = new String[listSetExportField.size()];
			for (int k = 0, j = 0; j < listSetExportField.size(); j++, k++) {
				cloumnName1[k] = listSetExportField.get(j).getFieldname();
				cloumnName2[k] = listSetExportField.get(j).getFieldenglishname();
				cloumnName3[k] = listSetExportField.get(j).getExportdatatype();
			}
		}
		String sheetName = "订单信息";
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
		String fileName = "Order_" + df.format(new Date()) + ".xlsx";
		final String[] cloumnName4 = cloumnName1;
		final String[] cloumnName5 = cloumnName2;
		final String[] cloumnName6 = cloumnName3;
		final String sql = cwbDAO.getSQLExportCommonBycwbs(cwbs);
		try {
			ExcelUtils excelUtil = new ExcelUtils() {
				@Override
				public void fillData(final Sheet sheet, final CellStyle style) {
					final List<User> uList = getDmpDAO.getAllUsers();
					final Map<Long, Customer> cMap = getDmpDAO.getAllCustomersToMap();
					final List<Branch> bList = getDmpDAO.getAllBranchs();
					final List<Common> commonList = getDmpDAO.getAllCommons();
					final List<CustomWareHouse> cWList = getDmpDAO.getCustomWareHouse();
					List<Remark> remarkList = getDmpDAO.getAllRemark();
					final Map<String, Map<String, String>> remarkMap = exportExcelService.getInwarhouseRemarks(remarkList);
					final List<Reason> reasonList = getDmpDAO.getAllReason();
					final List<String> cwbList = new ArrayList<String>();

					jdbcTemplate.query(new StreamingStatementCreator(sql), new ResultSetExtractor<Object>() {
						private int count = 0;

						public void processRow(ResultSet rs) throws SQLException {
							if (cwbList.indexOf(rs.getString("cwb")) > -1) {
								return;
							}
							cwbList.add(rs.getString("cwb"));

							Row row = sheet.createRow(count + 1);
							row.setHeightInPoints((float) 15);
							for (int i = 0; i < cloumnName4.length; i++) {
								Cell cell = row.createCell((short) i);
								cell.setCellStyle(style);
								Object a = exportExcelService.setObjectAForCommon(cloumnName5, rs, i, uList, cMap, bList, commonList, cWList, remarkMap, reasonList);
								if (cloumnName6[i].equals("double")) {
									cell.setCellValue(a == null ? BigDecimal.ZERO.doubleValue() : a.equals("") ? BigDecimal.ZERO.doubleValue() : Double.parseDouble(a.toString()));
								} else {
									cell.setCellValue(a == null ? "" : a.toString());
								}
							}
							count++;
						}

						@Override
						public Object extractData(ResultSet rs) throws SQLException, DataAccessException {
							while (rs.next()) {
								this.processRow(rs);
							}
							return null;
						}

					});
				}
			};
			excelUtil.excelLikeDmp(response, cloumnName4, sheetName, fileName);
		} catch (Exception e) {
			e.getMessage();
		}

	}

	@RequestMapping("/exportExcel")
	public void exportExcel(HttpServletResponse response, HttpServletRequest request, @RequestParam(value = "emaildateid") long emaildateid) {
		String mouldfieldids2 = request.getParameter("exportmould2"); // 导出模板
		String[] cloumnName1 = {}; // 导出的列名
		String[] cloumnName2 = {}; // 导出的英文列名
		String[] cloumnName3 = {}; // 导出的数据类型

		if (mouldfieldids2 != null && !"0".equals(mouldfieldids2)) { // 选择模板
			List<SetExportField> listSetExportField = getDmpDAO.getSetExportFieldByExportstate(mouldfieldids2);
			cloumnName1 = new String[listSetExportField.size()];
			cloumnName2 = new String[listSetExportField.size()];
			cloumnName3 = new String[listSetExportField.size()];
			for (int k = 0, j = 0; j < listSetExportField.size(); j++, k++) {
				cloumnName1[k] = listSetExportField.get(j).getFieldname();
				cloumnName2[k] = listSetExportField.get(j).getFieldenglishname();
				cloumnName3[k] = listSetExportField.get(j).getExportdatatype();
			}
		} else {
			List<SetExportField> listSetExportField = getDmpDAO.getSetExportFieldByExportstate("0");
			cloumnName1 = new String[listSetExportField.size()];
			cloumnName2 = new String[listSetExportField.size()];
			cloumnName3 = new String[listSetExportField.size()];
			for (int k = 0, j = 0; j < listSetExportField.size(); j++, k++) {
				cloumnName1[k] = listSetExportField.get(j).getFieldname();
				cloumnName2[k] = listSetExportField.get(j).getFieldenglishname();
				cloumnName3[k] = listSetExportField.get(j).getExportdatatype();
			}
		}
		String sheetName = "订单信息";
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
		String fileName = "Order_" + df.format(new Date()) + ".xlsx";
		final String[] cloumnName4 = cloumnName1;
		final String[] cloumnName5 = cloumnName2;
		final String[] cloumnName6 = cloumnName3;
		final String sql = cwbDAO.getSQLExportCommon(emaildateid);
		try {
			ExcelUtils excelUtil = new ExcelUtils() {
				@Override
				public void fillData(final Sheet sheet, final CellStyle style) {
					final List<User> uList = getDmpDAO.getAllUsers();
					final Map<Long, Customer> cMap = getDmpDAO.getAllCustomersToMap();
					final List<Branch> bList = getDmpDAO.getAllBranchs();
					final List<Common> commonList = getDmpDAO.getAllCommons();
					final List<CustomWareHouse> cWList = getDmpDAO.getCustomWareHouse();
					List<Remark> remarkList = getDmpDAO.getAllRemark();
					final Map<String, Map<String, String>> remarkMap = exportExcelService.getInwarhouseRemarks(remarkList);
					final List<Reason> reasonList = getDmpDAO.getAllReason();
					final List<String> cwbList = new ArrayList<String>();

					jdbcTemplate.query(new StreamingStatementCreator(sql), new ResultSetExtractor<Object>() {
						private int count = 0;

						public void processRow(ResultSet rs) throws SQLException {
							if (cwbList.indexOf(rs.getString("cwb")) > -1) {
								return;
							}
							cwbList.add(rs.getString("cwb"));

							Row row = sheet.createRow(count + 1);
							row.setHeightInPoints((float) 15);
							for (int i = 0; i < cloumnName4.length; i++) {
								Cell cell = row.createCell((short) i);
								cell.setCellStyle(style);
								Object a = exportExcelService.setObjectAForCommon(cloumnName5, rs, i, uList, cMap, bList, commonList, cWList, remarkMap, reasonList);
								if (cloumnName6[i].equals("double")) {
									cell.setCellValue(a == null ? BigDecimal.ZERO.doubleValue() : a.equals("") ? BigDecimal.ZERO.doubleValue() : Double.parseDouble(a.toString()));
								} else {
									cell.setCellValue(a == null ? "" : a.toString());
								}
							}
							count++;
						}

						@Override
						public Object extractData(ResultSet rs) throws SQLException, DataAccessException {
							while (rs.next()) {
								this.processRow(rs);
							}
							return null;
						}

					});
				}
			};
			excelUtil.excelLikeDmp(response, cloumnName4, sheetName, fileName);
		} catch (Exception e) {
			e.getMessage();
		}

	}

}

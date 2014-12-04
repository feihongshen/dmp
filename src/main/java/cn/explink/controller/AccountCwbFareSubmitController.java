package cn.explink.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import cn.explink.dao.AccountCwbFareDetailDAO;
import cn.explink.dao.BranchDAO;
import cn.explink.dao.CustomerDAO;
import cn.explink.dao.UserDAO;
import cn.explink.domain.AccountCwbFareDetail;
import cn.explink.domain.Branch;
import cn.explink.domain.Customer;
import cn.explink.domain.User;
import cn.explink.service.DataStatisticsService;
import cn.explink.service.ExplinkUserDetail;
import cn.explink.service.ExportService;
import cn.explink.util.ExcelUtils;

/**
 * 运费结算管理
 *
 */

@Controller
@RequestMapping("/accountcwbfaresubmit")
public class AccountCwbFareSubmitController {
	@Autowired
	UserDAO userDAO;

	// @Autowired
	// AccountCwbFareDetailService accountCwbFareDetailService;
	@Autowired
	SecurityContextHolderStrategy securityContextHolderStrategy;

	@Autowired
	BranchDAO branchDAO;

	@Autowired
	AccountCwbFareDetailDAO accountCwbFareDetailDAO;

	@Autowired
	DataStatisticsService dataStatisticsService;

	@Autowired
	CustomerDAO customerDAO;

	@Autowired
	ExportService exportService;

	private User getSessionUser() {
		ExplinkUserDetail userDetail = (ExplinkUserDetail) this.securityContextHolderStrategy.getContext().getAuthentication().getPrincipal();
		return userDetail.getUser();
	}

	/**
	 * 先付：显示信息
	 *
	 * @param model
	 * @param branchid
	 * @return
	 */
	@RequestMapping("/accountfaresubmitlist/{page}")
	public String accountfarelist(Model model, @PathVariable(value = "page") long page, @RequestParam(value = "begindate", required = false, defaultValue = "") String begindate,
			@RequestParam(value = "enddate", required = false, defaultValue = "") String enddate, @RequestParam(value = "cwbordertypeid", required = false, defaultValue = "0") long cwbordertypeid,
			@RequestParam(value = "faretypeid", required = false, defaultValue = "0") long faretypeid, @RequestParam(value = "userid", required = false, defaultValue = "0") long userid) {

		Branch branch = this.branchDAO.getBranchByBranchid(this.getSessionUser().getBranchid());
		model.addAttribute("branch", branch);
		List<User> userList = this.userDAO.getUserListByBranchid(branch.getBranchid(), 0);
		model.addAttribute("userList", userList);
		model.addAttribute("user", this.getSessionUser());
		List<Branch> branchList = this.branchDAO.getAllBranches();
		model.addAttribute("branchList", branchList);
		List<AccountCwbFareDetail> acfdList = new ArrayList<AccountCwbFareDetail>();
		if (page > 1) {
			acfdList = this.accountCwbFareDetailDAO.getAccountCwbFareDetailBySubmit(branch.getBranchid(), begindate, enddate, cwbordertypeid, faretypeid, userid);
		}
		model.addAttribute("acfdList", acfdList);
		model.addAttribute("faretypeid", faretypeid);
		model.addAttribute("cwbordertypeid", cwbordertypeid);
		List<Customer> customerList = this.customerDAO.getAllCustomers();
		model.addAttribute("customerlist", customerList);
		Double shouldfare = new Double(0);
		Double infactfare = new Double(0);
		for (AccountCwbFareDetail acfd : acfdList) {
			shouldfare += acfd.getShouldfare().doubleValue();
			infactfare += acfd.getInfactfare().doubleValue();
		}
		model.addAttribute("shouldfare", shouldfare);
		model.addAttribute("infactfare", infactfare);
		return "/accountfare/accountfaresubmitlist";
	}

	/**
	 * 配送结果结算记录导出Excel
	 *
	 * @param model
	 */
	@RequestMapping("/exportExcle")
	public void exportExcle(Model model, HttpServletResponse response, HttpServletRequest request, @RequestParam(value = "begindate", required = false, defaultValue = "") String begindate,
			@RequestParam(value = "enddate", required = false, defaultValue = "") String enddate, @RequestParam(value = "cwbordertypeid", required = false, defaultValue = "0") long cwbordertypeid,
			@RequestParam(value = "faretypeid", required = false, defaultValue = "0") long faretypeid, @RequestParam(value = "userid", required = false, defaultValue = "0") long userid) {
		String[] cloumnName1 = new String[10]; // 导出的列名
		String[] cloumnName2 = new String[10]; // 导出的英文列名
		this.exportService.SetAccountCwbFareDetailFields(cloumnName1, cloumnName2);
		final String[] cloumnName = cloumnName1;
		final String[] cloumnName3 = cloumnName2;
		final HttpServletRequest request1 = request;
		String sheetName = "上门退运费结算记录"; // sheet的名称
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
		String fileName = "AccountCwbFareDetail_" + df.format(new Date()) + ".xlsx"; // 文件名
		try {
			final Map<Long, Customer> cMap = this.customerDAO.getAllCustomersToMap();
			final List<Branch> bList = this.branchDAO.getAllBranches();
			final List<AccountCwbFareDetail> list = this.accountCwbFareDetailDAO.getAccountCwbFareDetailBySubmit(this.getSessionUser().getBranchid(), begindate, enddate, cwbordertypeid, faretypeid,
					userid);

			ExcelUtils excelUtil = new ExcelUtils() { // 生成工具类实例，并实现填充数据的抽象方法
				@Override
				public void fillData(Sheet sheet, CellStyle style) {
					for (int k = 0; k < list.size(); k++) {
						Row row = sheet.createRow(k + 1);
						row.setHeightInPoints(15);
						for (int i = 0; i < cloumnName.length; i++) {
							Cell cell = row.createCell((short) i);
							cell.setCellStyle(style);
							Object a = null;
							// 给导出excel赋值
							a = AccountCwbFareSubmitController.this.exportService.setAccountCwbFareDetailObject(cloumnName3, list, request1, a, i, k, bList, cMap);
							cell.setCellValue(a == null ? "" : a.toString());
						}
					}
				}
			};
			excelUtil.excel(response, cloumnName, sheetName, fileName);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

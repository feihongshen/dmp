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
import cn.explink.enumutil.BranchEnum;
import cn.explink.service.DataStatisticsService;
import cn.explink.service.ExplinkUserDetail;
import cn.explink.service.ExportService;
import cn.explink.util.ExcelUtils;
import cn.explink.util.Page;

/**
 * 运费结算审核
 * 
 */

@Controller
@RequestMapping("/accountcwbfaredetailVerify")
public class AccountCwbFareDetailVerifyController {
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
	 * 查询已交款的信息
	 * 
	 * @param model
	 * @param branchid
	 * @return
	 */
	@RequestMapping("/accountfarelist/{page}")
	public String accountfarelist(Model model, @PathVariable(value = "page") long page, @RequestParam(value = "customerid", required = false, defaultValue = "") String[] customerid,
			@RequestParam(value = "verifyflag", required = false, defaultValue = "0") int verifyflag, @RequestParam(value = "verifytime", required = false, defaultValue = "1") long verifytime,
			@RequestParam(value = "begindate", required = false, defaultValue = "") String begindate, @RequestParam(value = "enddate", required = false, defaultValue = "") String enddate,
			@RequestParam(value = "deliverybranchid", required = false, defaultValue = "0") long deliverybranchid,
			@RequestParam(value = "deliverystate", required = false, defaultValue = "0") long deliverystate,
			@RequestParam(value = "shoulefarefeesign", required = false, defaultValue = "1") long shoulefarefeesign,
			@RequestParam(value = "pageNumber", required = false, defaultValue = "100") long pageNumber) {

		Branch branch = this.branchDAO.getBranchByBranchid(this.getSessionUser().getBranchid());
		List<Branch> branchnameList = this.branchDAO.getQueryBranchByBranchsiteAndUserid(this.getSessionUser().getUserid(), BranchEnum.ZhanDian.getValue() + "," + BranchEnum.TuiHuo.getValue() + ","
				+ BranchEnum.ZhongZhuan.getValue());

		if (branch.getSitetype() == BranchEnum.ZhanDian.getValue()) {
			if (branchnameList.size() == 0) {
				branchnameList.add(branch);
			} else {
				if (!this.dataStatisticsService.checkBranchRepeat(branchnameList, branch)) {
					branchnameList.add(branch);
				}
			}
		}
		Page pageparm = new Page();
		List<AccountCwbFareDetail> acfdList = new ArrayList<AccountCwbFareDetail>();
		AccountCwbFareDetail accountCwbFareDetailSum = new AccountCwbFareDetail();
		if ((begindate.length() > 0) && (enddate.length() > 0)) {
			String customerids = this.dataStatisticsService.getStrings(customerid);
			acfdList = this.accountCwbFareDetailDAO.getAccountCwbFareDetailByQKVerify(page, customerids, verifyflag, verifytime, begindate, enddate, deliverybranchid, deliverystate,
					shoulefarefeesign, pageNumber);
			pageparm = new Page(this.accountCwbFareDetailDAO.getAccountCwbFareDetailCountByQKVerify(customerids, verifyflag, verifytime, begindate, enddate, deliverybranchid, deliverystate,
					shoulefarefeesign), page, pageNumber);
			accountCwbFareDetailSum = this.accountCwbFareDetailDAO.getAccountCwbFareDetailSumByQKVerify(customerids, verifyflag, verifytime, begindate, enddate, deliverybranchid, deliverystate,
					shoulefarefeesign);
		}

		model.addAttribute("acfdList", acfdList);
		model.addAttribute("accountCwbFareDetailSum", accountCwbFareDetailSum);
		model.addAttribute("branchList", branchnameList);
		List<String> customeridList = this.dataStatisticsService.getList(customerid);
		model.addAttribute("customeridStr", customeridList);
		List<Customer> customerList = this.customerDAO.getAllCustomers();
		model.addAttribute("customerlist", customerList);
		model.addAttribute("page_obj", pageparm);
		model.addAttribute("page", page);
		return "/accountfare/accountfareVerifylist";
	}

	/**
	 * 配送结果结算记录导出Excel
	 * 
	 * @param model
	 */
	@RequestMapping("/exportExcle")
	public void exportExcle(Model model, HttpServletResponse response, HttpServletRequest request, @RequestParam(value = "customerid", required = false, defaultValue = "") String[] customerid,
			@RequestParam(value = "verifyflag", required = false, defaultValue = "0") int verifyflag, @RequestParam(value = "verifytime", required = false, defaultValue = "1") long verifytime,
			@RequestParam(value = "begindate", required = false, defaultValue = "") String begindate, @RequestParam(value = "enddate", required = false, defaultValue = "") String enddate,
			@RequestParam(value = "deliverybranchid", required = false, defaultValue = "0") long deliverybranchid,
			@RequestParam(value = "deliverystate", required = false, defaultValue = "0") long deliverystate,
			@RequestParam(value = "shoulefarefeesign", required = false, defaultValue = "1") long shoulefarefeesign) {
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
			String customerids = this.dataStatisticsService.getStrings(customerid);
			final List<AccountCwbFareDetail> list = this.accountCwbFareDetailDAO.getExportAccountCwbFareDetailByQKVerify(customerids, verifyflag, verifytime, begindate, enddate, deliverybranchid,
					deliverystate, shoulefarefeesign);
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
							a = AccountCwbFareDetailVerifyController.this.exportService.setAccountCwbFareDetailObject(cloumnName3, list, request1, a, i, k, bList, cMap);
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

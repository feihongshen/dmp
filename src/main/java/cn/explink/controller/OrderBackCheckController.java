package cn.explink.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.explink.b2c.tools.B2cEnum;
import cn.explink.b2c.tools.JointService;
import cn.explink.dao.BranchDAO;
import cn.explink.dao.CustomerDAO;
import cn.explink.dao.OrderBackCheckDAO;
import cn.explink.dao.SystemInstallDAO;
import cn.explink.dao.UserDAO;
import cn.explink.domain.Branch;
import cn.explink.domain.Customer;
import cn.explink.domain.OrderBackCheck;
import cn.explink.domain.User;
import cn.explink.enumutil.BranchEnum;
import cn.explink.exception.CwbException;
import cn.explink.service.ExplinkUserDetail;
import cn.explink.service.ExportService;
import cn.explink.service.OrderBackCheckService;
import cn.explink.util.ExcelUtils;

/**
 * 退款审核
 * 
 * @author zs
 * 
 */
@Controller
@RequestMapping("/orderBackCheck")
public class OrderBackCheckController {
	@Autowired
	SecurityContextHolderStrategy securityContextHolderStrategy;
	@Autowired
	OrderBackCheckService orderBackCheckService;
	@Autowired
	OrderBackCheckDAO orderBackCheckDAO;
	@Autowired
	ExportService exportService;
	@Autowired
	CustomerDAO customerDAO;
	@Autowired
	UserDAO userDAO;
	@Autowired
	BranchDAO branchDAO;
	@Autowired
	JointService jointService;
	@Autowired
	SystemInstallDAO systemInstallDAO;

	private User getSessionUser() {
		ExplinkUserDetail userDetail = (ExplinkUserDetail) this.securityContextHolderStrategy.getContext().getAuthentication().getPrincipal();
		return userDetail.getUser();
	}

	/**
	 * 审核为允许退货出站
	 * 
	 * @param model
	 * @param request
	 * @param cwb
	 * @return
	 */
	@RequestMapping("/toTuiHuoCheck")
	public String toTuiHuoCheck(Model model, HttpServletRequest request, @RequestParam(value = "cwb", defaultValue = "", required = false) String cwb,
			@RequestParam(value = "searchType", defaultValue = "0", required = false) String searchType) {
		List<Branch> branchList = this.branchDAO.getQueryBranchByBranchidAndUserid(this.getSessionUser().getUserid(), BranchEnum.ZhanDian.getValue());
		String branchids = "";
		int isOpenFlag = this.jointService.getStateForJoint(B2cEnum.Amazon.getKey());
		model.addAttribute("amazonIsOpen", isOpenFlag);
		String isUseAuditTuiHuo = this.systemInstallDAO.getSystemInstall("isUseAuditTuiHuo") == null ? "no" : this.systemInstallDAO.getSystemInstall("isUseAuditTuiHuo").getValue();
		model.addAttribute("isUseAuditTuiHuo", isUseAuditTuiHuo);
		String isUseAuditZhongZhuan = this.systemInstallDAO.getSystemInstall("isUseAuditZhongZhuan") == null ? "no" : this.systemInstallDAO.getSystemInstall("isUseAuditTuiHuo").getValue();
		model.addAttribute("isUseAuditZhongZhuan", isUseAuditZhongZhuan);
		// 如果为选择站点则匹配用户权限
		if ((branchList != null) && !branchList.isEmpty()) {
			for (Branch listb : branchList) {
				branchids += listb.getBranchid() + ",";
			}
		}
		if (branchids.indexOf(",") > -1) {
			branchids = branchids.substring(0, branchids.lastIndexOf(","));
		}
		// 根据订单号查询
		if (!"".equals(cwb.trim()) && "0".equals(searchType)) {
			List<OrderBackCheck> list = new ArrayList<OrderBackCheck>();
			for (String cwbStr : cwb.split("\r\n")) {
				if ("".equals(cwbStr.trim())) {
					continue;
				}
				OrderBackCheck o = this.orderBackCheckDAO.getOrderBackCheckByCwbAndBranch(cwbStr, branchids);
				if (o != null) {
					list.add(o);
				}
			}
			List<Customer> customerList = this.customerDAO.getAllCustomers();
			List<User> userList = this.userDAO.getAllUser();
			List<OrderBackCheck> orderbackList = this.orderBackCheckService.getOrderBackCheckList(list, customerList, userList);
			model.addAttribute("orderbackList", orderbackList);
		}
		// 查询全部
		if ("1".equals(searchType)) {
			List<OrderBackCheck> list = this.orderBackCheckDAO.getOrderBackCheckListByBranch(branchids);
			List<Customer> customerList = this.customerDAO.getAllCustomers();
			List<User> userList = this.userDAO.getAllUser();
			List<OrderBackCheck> orderbackList = this.orderBackCheckService.getOrderBackCheckList(list, customerList, userList);
			model.addAttribute("orderbackList", orderbackList);
		}

		return "auditorderstate/toTuiHuoCheck";
	}

	/**
	 * 提交审核
	 * 
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping("/save")
	public @ResponseBody
	String save(Model model, HttpServletRequest request, @RequestParam(value = "ids", required = false, defaultValue = "") String ids) {
		try {
			this.orderBackCheckService.save(ids, this.getSessionUser());
			return "{\"errorCode\":0,\"error\":\"审核成功\"}";
		} catch (CwbException e) {
			return "{\"errorCode\":1,\"error\":\"" + e.getMessage() + "\"}";
		}
	}

	/**
	 * 导出Excel
	 * 
	 * @param model
	 * @param response
	 * @param cwb
	 * @param request
	 */
	@RequestMapping("/export")
	public void export(Model model, HttpServletResponse response, @RequestParam(value = "cwb", required = false, defaultValue = "") String cwb,
			@RequestParam(value = "searchType", defaultValue = "0", required = false) String searchType, HttpServletRequest request) {
		List<Branch> branchList = this.branchDAO.getQueryBranchByBranchidAndUserid(this.getSessionUser().getUserid(), BranchEnum.ZhanDian.getValue());
		String branchids = "";
		// 如果为选择站点则匹配用户权限
		if ((branchList != null) && !branchList.isEmpty()) {
			for (Branch listb : branchList) {
				branchids += listb.getBranchid() + ",";
			}
		}
		branchids = branchids.substring(0, branchids.lastIndexOf(","));

		List<Customer> customerList = this.customerDAO.getAllCustomers();
		List<User> userList = this.userDAO.getAllUser();
		String[] cloumnName1 = new String[9]; // 导出的列名
		String[] cloumnName2 = new String[9]; // 导出的英文列名
		this.exportService.SetOrderBackCheckFields(cloumnName1, cloumnName2);
		final String[] cloumnName = cloumnName1;
		final String[] cloumnName3 = cloumnName2;
		final HttpServletRequest request1 = request;
		String sheetName = "退货审核"; // sheet的名称
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
		String fileName = "Complaint_" + df.format(new Date()) + ".xlsx"; // 文件名

		List<OrderBackCheck> list = new ArrayList<OrderBackCheck>();
		// 根据订单号导出
		if (!"".equals(cwb.trim()) && "0".equals(searchType)) {
			for (String cwbStr : cwb.split("\r\n")) {
				if ("".equals(cwbStr.trim())) {
					continue;
				}
				OrderBackCheck o = this.orderBackCheckDAO.getOrderBackCheckByCwbAndBranch(cwbStr, branchids);
				if (o != null) {
					list.add(o);
				}
			}

		}// 根据权限导出全部
		if ("1".equals(searchType)) {
			list = this.orderBackCheckDAO.getOrderBackCheckListByBranch(branchids);
		}

		final List<OrderBackCheck> list1 = this.orderBackCheckService.getOrderBackCheckList(list, customerList, userList);
		try {
			ExcelUtils excelUtil = new ExcelUtils() { // 生成工具类实例，并实现填充数据的抽象方法
				@Override
				public void fillData(Sheet sheet, CellStyle style) {
					for (int k = 0; k < list1.size(); k++) {
						Row row = sheet.createRow(k + 1);
						row.setHeightInPoints(15);
						for (int i = 0; i < cloumnName.length; i++) {
							Cell cell = row.createCell((short) i);
							cell.setCellStyle(style);
							Object a = null;
							// 给导出excel赋值
							a = OrderBackCheckController.this.exportService.setOrderBackCheck(cloumnName3, request1, list1, a, i, k);
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

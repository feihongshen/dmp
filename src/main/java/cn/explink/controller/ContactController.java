package cn.explink.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import cn.explink.dao.BranchDAO;
import cn.explink.dao.RoleDAO;
import cn.explink.dao.UserDAO;
import cn.explink.domain.Branch;
import cn.explink.domain.Role;
import cn.explink.domain.User;
import cn.explink.enumutil.UserEmployeestatusEnum;
import cn.explink.service.ExportService;
import cn.explink.service.UserService;
import cn.explink.util.ExcelUtils;
import cn.explink.util.Page;

@RequestMapping("/contact")
@Controller
public class ContactController {

	private static Logger logger = LoggerFactory.getLogger(ContactController.class);
	
	@Autowired
	UserDAO userDAO;
	@Autowired
	RoleDAO roleDAO;
	@Autowired
	BranchDAO branchDAO;
	@Autowired
	UserService userService;

	@Autowired
	ExportService exportService;

	@RequestMapping("/list/{page}")
	private String list(@PathVariable("page") long page, Model model, @RequestParam(value = "workstate", required = false, defaultValue = "0") long workstate,
			@RequestParam(value = "sosStr", required = false, defaultValue = "") String sosString, @RequestParam(value = "branchid", required = false, defaultValue = "-1") long branchid,
			@RequestParam(value = "roleid", required = false, defaultValue = "-1") long roleid) {

		List<Role> roleList = roleDAO.getRoles();
		List<Branch> branchList = branchDAO.getAllEffectBranches();
		List<User> userList = userDAO.getUserForContact(sosString, branchid, roleid, workstate, page);
		List<UserView> userViews = userService.getUserView(userList, roleList, branchList);
		if (workstate == 0) {
			model.addAttribute("count", userDAO.getAllUserByWorkState(UserEmployeestatusEnum.GongZuo.getValue()));
			model.addAttribute("offline", userDAO.getAllUserByWorkState(UserEmployeestatusEnum.LiZhi.getValue()));
			model.addAttribute("holiday", userDAO.getAllUserByWorkState(UserEmployeestatusEnum.XiuJia.getValue()));
		}
		model.addAttribute("userList", userViews);
		model.addAttribute("branches", branchList);
		model.addAttribute("roles", roleList);
		model.addAttribute("page_obj", new Page(userDAO.getUserCountForContact(sosString, branchid, roleid, workstate), page, Page.ONE_PAGE_NUMBER));
		model.addAttribute("page", page);
		return "contact/list";
	}

	@RequestMapping("/exportuser")
	private void exportuser(HttpServletResponse response, @RequestParam(value = "workstate", required = false, defaultValue = "0") long workstate,
			@RequestParam(value = "sosStr", required = false, defaultValue = "") String sosString, @RequestParam(value = "branchid", required = false, defaultValue = "-1") long branchid,
			@RequestParam(value = "roleid", required = false, defaultValue = "-1") long roleid) {
		List<Role> roleList = roleDAO.getRoles();
		List<Branch> branchList = branchDAO.getAllBranches();
		List<User> userList = userDAO.getUserForContactExport(sosString, branchid, roleid, workstate);
		final List<UserView> userViews = userService.getUserView(userList, roleList, branchList);

		String[] cloumnName1 = new String[8]; // 导出的列名
		String[] cloumnName2 = new String[8]; // 导出的英文列名

		exportService.SetUserFields(cloumnName1, cloumnName2);
		final String[] cloumnName = cloumnName1;
		final String[] cloumnName3 = cloumnName2;
		String sheetName = "联系人"; // sheet的名称
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
		String fileName = "联系人_" + df.format(new Date()) + ".xlsx"; // 文件名
		ExcelUtils excelUtil = new ExcelUtils() { // 生成工具类实例，并实现填充数据的抽象方法
			@Override
			public void fillData(Sheet sheet, CellStyle style) {
				for (int k = 0; k < userViews.size(); k++) {
					Row row = sheet.createRow(k + 1);
					row.setHeightInPoints((float) 15);
					for (int i = 0; i < cloumnName.length; i++) {
						Cell cell = row.createCell((short) i);
						cell.setCellStyle(style);
						Object a = null;
						// 给导出excel赋值
						a = exportService.setUserObject(cloumnName3, userViews, a, i, k);
						cell.setCellValue(a == null ? "" : a.toString());
					}
				}
			}
		};
		try {
			excelUtil.excel(response, cloumnName, sheetName, fileName);
		} catch (Exception e) {
			logger.error("", e);
		}

	}

}

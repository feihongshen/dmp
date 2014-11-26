package cn.explink.controller;

import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.explink.dao.CustomerDAO;
import cn.explink.dao.CwbDAO;
import cn.explink.dao.DeleteCwbRecordDAO;
import cn.explink.dao.SystemInstallDAO;
import cn.explink.dao.UserDAO;
import cn.explink.dao.UserDeleteCwbDAO;
import cn.explink.domain.Customer;
import cn.explink.domain.CwbOrder;
import cn.explink.domain.DeleteCwbRecord;
import cn.explink.domain.SystemInstall;
import cn.explink.domain.User;
import cn.explink.domain.UserDeleteCwb;
import cn.explink.service.DeleteCwbService;
import cn.explink.service.ExplinkUserDetail;
import cn.explink.util.Page;

@RequestMapping("/deletecwb")
@Controller
public class DeleteCwbController {
	private Logger logger = LoggerFactory.getLogger(DeleteCwbController.class);
	@Autowired
	CwbDAO cwbDAO;
	@Autowired
	CustomerDAO customerDAO;
	@Autowired
	UserDAO userDAO;

	@Autowired
	UserDeleteCwbDAO userDeleteCwbDAO;
	@Autowired
	SystemInstallDAO systemInstallDAO;
	@Autowired
	DeleteCwbRecordDAO deleteCwbRecordDAO;

	@Autowired
	DeleteCwbService deleteCwbService;

	@Autowired
	SecurityContextHolderStrategy securityContextHolderStrategy;

	private User getSessionUser() {
		ExplinkUserDetail userDetail = (ExplinkUserDetail) securityContextHolderStrategy.getContext().getAuthentication().getPrincipal();
		return userDetail.getUser();
	}

	/**
	 * 订单删除功能
	 * 
	 * @param model
	 * @param cwbs
	 * @return
	 */
	@RequestMapping("/deletecwbs")
	public String deletecwbs(Model model, @RequestParam(value = "cwbs", defaultValue = "") String cwbs) {
		List<UserDeleteCwb> userDeleteCwbList = userDeleteCwbDAO.getUsersByUserid(getSessionUser().getUserid());
		String isusedelete = userDeleteCwbList.size() > 0 ? "yes" : "no";
		long allnum = 0;
		long successnum = 0;

		if (cwbs.length() > 0 && isusedelete.equals("yes")) {
			for (String cwb : cwbs.split("\r\n")) {
				if (cwb.trim().length() == 0) {
					continue;
				}
				allnum++;
				CwbOrder co = cwbDAO.getCwbByCwbLock(cwb);
				if (co != null) {
					deleteCwbService.deletecwb(co, getSessionUser());
					successnum++;
					logger.info("数据删除功能:操作人：{}，订单号：{}", getSessionUser().getRealname(), cwb);
				}
			}
			model.addAttribute("msg", "总共：" + allnum + "单，" + "成功：" + successnum + "单");
		}
		model.addAttribute("isusedelete", isusedelete);
		return "deletecwb/deletecwbs";
	}

	/**
	 * 查看该用户是否有删除数据的权限
	 * 
	 * @param checkcode
	 * @return
	 */
	@RequestMapping("/getuserdeleteright")
	public @ResponseBody JSONObject getuserdeleteright(@RequestParam(value = "checkcode", defaultValue = "") String checkcode) {
		JSONObject object = new JSONObject();
		try {
			List<UserDeleteCwb> userDeleteCwbList = userDeleteCwbDAO.getUsersByUserid(getSessionUser().getUserid());
			String isusedelete = userDeleteCwbList.size() > 0 ? "yes" : "no";

			object.put("msg", isusedelete);
		} catch (Exception e) {
			object.put("msg", "no");
		}
		return object;
	}

	/**
	 * 用户输入验证码后校验是否正确
	 * 
	 * @param checkcode
	 * @return
	 */
	@RequestMapping("/checkuserdeleteright")
	public @ResponseBody JSONObject checkuserdeleteright(@RequestParam(value = "checkcode", defaultValue = "") String checkcode) {
		JSONObject object = new JSONObject();
		try {
			SystemInstall deletecwb = systemInstallDAO.getSystemInstall("deletecwbcheckcode");
			String deletecwbcheckcode = deletecwb == null ? "" : deletecwb.getValue();

			// 判断用户输入的验证码是否和系统设置的一致
			if (checkcode.equals(deletecwbcheckcode)) {
				UserDeleteCwb userDeleteCwb = new UserDeleteCwb();
				userDeleteCwb.setUserid(getSessionUser().getUserid());
				userDeleteCwb.setCheckcode(checkcode);
				userDeleteCwbDAO.creUserDeleteCwb(userDeleteCwb);

				object.put("errorcode", 0);
				logger.info("数据删除校验验证码是否正确功能:操作人：{}，验证码：{}", getSessionUser().getRealname(), checkcode);
			} else {
				object.put("errorcode", 1);
				object.put("msg", "验证码错误，您尚未被管理员授权删除订单功能的操作！");
			}
		} catch (Exception e) {
			object.put("errorcode", 2);
			object.put("msg", e);
		}
		return object;
	}

	/**
	 * 订单删除查询
	 * 
	 * @param model
	 * @param page
	 * @param cwbs
	 * @param begindate
	 * @param enddate
	 * @return
	 */
	@RequestMapping("/deletecwbslist/{page}")
	public String deletecwbslist(Model model, @PathVariable(value = "page") long page, @RequestParam(value = "cwbs", defaultValue = "") String cwbs,
			@RequestParam(value = "begindate", required = false, defaultValue = "") String begindate, @RequestParam(value = "enddate", required = false, defaultValue = "") String enddate) {
		List<Customer> customerlist = customerDAO.getAllCustomers();
		List<User> userlist = userDAO.getAllUser();

		List<DeleteCwbRecord> deleteCwbRecordlist = new ArrayList<DeleteCwbRecord>();
		Page pageparm = new Page();
		long count = 0;
		if (cwbs.length() > 0 || (begindate.length() > 0 && enddate.length() > 0)) {
			StringBuffer cwbStr = new StringBuffer();
			for (String cwb : cwbs.split("\r\n")) {
				if (cwb.trim().length() == 0) {
					continue;
				}
				cwbStr = cwbStr.append("'").append(cwb).append("',");
			}

			if (cwbs.length() > 0) {
				cwbStr.deleteCharAt(cwbStr.length() - 1);
			}

			deleteCwbRecordlist = deleteCwbRecordDAO.getDeleteCwbRecordByDeleteTimeAndCwbsPage(page, begindate, enddate, cwbStr.toString());
			count = deleteCwbRecordDAO.getDeleteCwbRecordByDeleteTimeAndCwbsCount(begindate, enddate, cwbStr.toString());
			pageparm = new Page(count, page, Page.ONE_PAGE_NUMBER);
		}
		model.addAttribute("customerlist", customerlist);
		model.addAttribute("userlist", userlist);
		model.addAttribute("deleteCwbRecordlist", deleteCwbRecordlist);
		model.addAttribute("page_obj", pageparm);
		model.addAttribute("page", page);

		return "deletecwb/deletecwbslist";
	}
}

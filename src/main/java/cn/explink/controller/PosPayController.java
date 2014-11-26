package cn.explink.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.explink.dao.BranchDAO;
import cn.explink.dao.CustomerDAO;
import cn.explink.dao.PosPayMoneyDAO;
import cn.explink.dao.UserDAO;
import cn.explink.domain.Branch;
import cn.explink.domain.User;
import cn.explink.enumutil.BranchEnum;
import cn.explink.pos.tools.PosTradeDetail;
import cn.explink.service.ExplinkUserDetail;
import cn.explink.service.PosPayMoneyService;
import cn.explink.util.Page;

@RequestMapping("/pospay")
@Controller
public class PosPayController {

	@Autowired
	PosPayMoneyService posPayMoneyService;
	@Autowired
	PosPayMoneyDAO posPayMoneyDAO;
	@Autowired
	UserDAO userDAO;
	@Autowired
	CustomerDAO customerDAO;
	@Autowired
	BranchDAO branchDAO;
	@Autowired
	SecurityContextHolderStrategy securityContextHolderStrategy;

	private User getSessionUser() {
		ExplinkUserDetail userDetail = (ExplinkUserDetail) securityContextHolderStrategy.getContext().getAuthentication().getPrincipal();
		return userDetail.getUser();
	}

	@RequestMapping("/list/{page}")
	public String list(Model model, @PathVariable("page") long page, @RequestParam(value = "cwb", required = false, defaultValue = "") String cwb,
			@RequestParam(value = "pos_code", required = false, defaultValue = "") String pos_code, @RequestParam(value = "starttime", required = false, defaultValue = "") String starttime,
			@RequestParam(value = "endtime", required = false, defaultValue = "") String endtime, @RequestParam(value = "branchid", required = false, defaultValue = "-1") long branchid,
			@RequestParam(value = "deliveryid", required = false, defaultValue = "-1") long deliveryid,
			@RequestParam(value = "isSuccessFlag", required = false, defaultValue = "0") long isSuccessFlag, @RequestParam(value = "isshow", required = false, defaultValue = "0") long isshow,
			HttpServletRequest request) {

		List<PosTradeDetail> pospaylist = new ArrayList<PosTradeDetail>();
		Page peramPage = new Page();

		List<User> userListByBranchid = userDAO.getUserListByBranchid(branchid, deliveryid);
		String deliveryids = posPayMoneyService.getDeliveryids(userListByBranchid);

		Branch nowBranch = branchDAO.getBranchByBranchid(getSessionUser().getBranchid());
		List<Branch> branchlist = branchDAO.getQueryBranchByBranchidAndUserid(getSessionUser().getUserid(), BranchEnum.ZhanDian.getValue());

		if (nowBranch.getSitetype() == BranchEnum.ZhanDian.getValue() || nowBranch.getSitetype() == BranchEnum.KuFang.getValue()) {
			model.addAttribute("nowbranch", nowBranch);
		}

		if (isshow != 0) {
			pospaylist = posPayMoneyDAO.getPosPayByPage(page, cwb, pos_code, starttime, endtime, deliveryids, isSuccessFlag);
			peramPage = new Page(posPayMoneyDAO.getPosPayCount(cwb, pos_code, starttime, endtime, deliveryids, isSuccessFlag), page, Page.ONE_PAGE_NUMBER);
		}

		model.addAttribute("pospaylist", pospaylist);
		model.addAttribute("page_obj", peramPage);
		model.addAttribute("page", page);
		model.addAttribute("pos_code", pos_code);
		model.addAttribute("userList", userDAO.getAllUser());
		model.addAttribute("customerList", customerDAO.getAllCustomers());
		model.addAttribute("deliverlist", userDAO.getUserListByBranchid(branchid <= -1 ? nowBranch.getBranchid() : branchid, 0));
		model.addAttribute("branchlist", branchlist);
		this.saveConditionsToRequest(request, cwb, pos_code, starttime, endtime, deliveryid, deliveryids, isSuccessFlag);

		return "funds/pospay/pospaylist";
	}

	@RequestMapping("/save_excel")
	public void saveAsExcel(Model model, HttpServletRequest request, HttpServletResponse response) {
		List<User> userList = userDAO.getAllUser();
		posPayMoneyService.PosPayRecord_selectSaveAsExcel(userList, request, response);
	}

	@RequestMapping("/updatebranch")
	public @ResponseBody String updateCustomerwarehouse(Model model, @RequestParam("branchid") long branchid) {
		if (branchid > 0) {
			List<User> list = userDAO.getUserByBranchid(branchid);
			return JSONArray.fromObject(list).toString();
		} else {
			return "[]";
		}

	}

	private void saveConditionsToRequest(HttpServletRequest request, String cwb, String pos_code, String starttime, String endtime, long deliveryid, String deliveryids, long isSuccessFlag) {

		request.getSession().setAttribute("cwb", cwb);
		request.getSession().setAttribute("pos_code", pos_code);
		request.getSession().setAttribute("starttime", starttime);
		request.getSession().setAttribute("endtime", endtime);
		request.getSession().setAttribute("deliveryid", deliveryid);
		request.getSession().setAttribute("deliveryids", deliveryids);
		request.getSession().setAttribute("isSuccessFlag", isSuccessFlag);
	}

}

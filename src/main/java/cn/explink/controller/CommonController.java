package cn.explink.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.explink.dao.BranchDAO;
import cn.explink.dao.CommonDAO;
import cn.explink.dao.UserDAO;
import cn.explink.domain.Branch;
import cn.explink.domain.Common;
import cn.explink.domain.User;
import cn.explink.enumutil.BranchEnum;
import cn.explink.service.CommonService;
import cn.explink.util.Page;

@RequestMapping("/common")
@Controller
public class CommonController {

	@Autowired
	CommonDAO commonDAO;
	@Autowired
	CommonService commonService;
	@Autowired
	BranchDAO branchDAO;
	@Autowired
	UserDAO userDAO;

	@RequestMapping("/list/{page}")
	public String list(Model model, @PathVariable("page") long page, @RequestParam(value = "commonname", required = false, defaultValue = "") String commonname,
			@RequestParam(value = "commonnumber", required = false, defaultValue = "") String commonnumber) {
		List<Common> commonlist = commonDAO.getCommonByPage(page, commonname, commonnumber);
		model.addAttribute("commonList", commonlist);
		model.addAttribute("page_obj", new Page(commonDAO.getCommonCount(commonname, commonnumber), page, Page.ONE_PAGE_NUMBER));
		model.addAttribute("page", page);
		List<Branch> branchList = branchDAO.getBranchBySiteType(BranchEnum.ZhanDian.getValue());
		List<User> uList = userDAO.getAllUser();
		model.addAttribute("uList", uList);
		model.addAttribute("branchList", branchList);
		return "/common/list";
	}

	@RequestMapping("/add")
	public String add(Model model) {
		List<Branch> branchList = branchDAO.getBranchBySiteType(BranchEnum.ZhanDian.getValue());
		model.addAttribute("branchList", branchList);
		commonService.initCommonList();
		return "/common/add";
	}

	@RequestMapping("/create")
	public @ResponseBody String create(Model model, @RequestParam(value = "commonname", required = true) String commonname, @RequestParam(value = "commonnumber", required = true) String commonnumber,
			@RequestParam(value = "orderprefix", required = false) String orderprefix, @RequestParam(value = "branchid", required = false) long branchid,
			@RequestParam(value = "userid", required = false) long userid, @RequestParam(value = "pageSize", defaultValue = "0", required = false) String pageSize,
			@RequestParam(value = "private_key", required = false) String private_key, @RequestParam(value = "isopenflag", required = false) long isopenflag,
			@RequestParam(value = "feedback_url", required = false) String feedback_url, @RequestParam(value = "phone", required = false) String phone,
			@RequestParam(value = "loopcount", defaultValue = "10", required = false) long loopcount,
			@RequestParam(value = "isasynchronous", defaultValue = "10", required = false) long isasynchronous, HttpServletRequest request) {

		List<Common> listCommon = commonDAO.getCommonByCommonname(commonname, commonnumber);
		if (listCommon.size() > 0) {
			if (listCommon.get(0).getCommonname().equals(commonname)) {
				return "{\"errorCode\":1,\"error\":\"该承运商已存在\"}";
			} else {
				return "{\"errorCode\":1,\"error\":\"该承运商的承运商编码已存在\"}";
			}
		}
		if (branchid <= 0) {

			commonDAO.CreateCommon(commonname, commonnumber.trim().toUpperCase(), orderprefix.trim(), 0, 0, pageSize, private_key, isopenflag, feedback_url, phone, loopcount, isasynchronous);
			commonService.initCommonList();
			return "{\"errorCode\":0,\"error\":\"新建成功\"}";
		}
		List<Common> commonList = commonDAO.getCommonByBranchid(branchid);
		if (commonList != null && commonList.size() > 0) {
			return "{\"errorCode\":1,\"error\":\"所选站点已被别的承运商关联\"}";
		} else {
			commonDAO.CreateCommon(commonname, commonnumber.trim().toUpperCase(), orderprefix.trim(), branchid, userid, pageSize, private_key, isopenflag, feedback_url, phone, loopcount,
					isasynchronous);

			commonService.initCommonList();
			return "{\"errorCode\":0,\"error\":\"新建成功\"}";
		}

	}

	@RequestMapping("/edit/{id}")
	public String edit(Model model, @PathVariable("id") long id) {
		Common comm = commonDAO.getCommonById(id);
		model.addAttribute("common", comm);
		List<Branch> branchList = branchDAO.getBranchBySiteType(BranchEnum.ZhanDian.getValue());
		model.addAttribute("branchList", branchList);
		if (comm != null && comm.getBranchid() > 0) {
			List<User> list = userDAO.getAllUserByRolesAndBranchid("4", comm.getBranchid());
			model.addAttribute("nextBranchUserlist", list);
		}
		return "/common/edit";
	}

	@RequestMapping("/save/{id}")
	public @ResponseBody String save(Model model, HttpServletRequest request, @PathVariable("id") long id, @RequestParam(value = "commonname", required = true) String commonname,
			@RequestParam(value = "commonnumber", required = true) String commonnumber, @RequestParam(value = "orderprefix", required = false) String orderprefix,
			@RequestParam(value = "branchid", required = false) long branchid, @RequestParam(value = "userid", required = false) long userid,
			@RequestParam(value = "pageSize", required = false, defaultValue = "0") String pageSize, @RequestParam(value = "private_key", required = false) String private_key,
			@RequestParam(value = "isopenflag", required = false) long isopenflag, @RequestParam(value = "feedback_url", required = false) String feedback_url,
			@RequestParam(value = "phone", required = false) String phone, @RequestParam(value = "loopcount", defaultValue = "10", required = false) long loopcount,
			@RequestParam(value = "isasynchronous", defaultValue = "0", required = false) long isasynchronous) {
		List<Common> listCommon = commonDAO.getCommonByCommonname(commonname, commonnumber);
		if (listCommon.size() > 0 && listCommon.get(0).getId() != id) {
			if (listCommon.get(0).getCommonname().equals(commonname)) {
				return "{\"errorCode\":1,\"error\":\"该承运商已存在\"}";
			} else {
				return "{\"errorCode\":1,\"error\":\"该承运商的承运商编码已存在\"}";
			}
		}
		if (branchid <= 0) {
			commonDAO.saveCommon(commonname, commonnumber.trim().toUpperCase(), orderprefix.trim(), 0, 0, id, pageSize, private_key, isopenflag, feedback_url, phone, loopcount, isasynchronous);
			commonService.initCommonList();
			return "{\"errorCode\":0,\"error\":\"修改成功\"}";
		}
		List<Common> commonList = commonDAO.getCommonByBranchidAndNotID(branchid, id);
		if (commonList != null && commonList.size() > 0 && commonList.get(0).getId() != id) {
			return "{\"errorCode\":1,\"error\":\"所选站点已被别的承运商关联\"}";
		} else {
			commonDAO.saveCommon(commonname, commonnumber.trim().toUpperCase(), orderprefix.trim(), branchid, userid, id, pageSize, private_key, isopenflag, feedback_url, phone, loopcount,
					isasynchronous);
			commonService.initCommonList();
			return "{\"errorCode\":0,\"error\":\"修改成功\"}";
		}

	}

	@RequestMapping("/del/{id}")
	public @ResponseBody String del(Model model, @PathVariable("id") long id) {
		commonDAO.delCommon(id);
		commonService.initCommonList();
		return "{\"errorCode\":0,\"error\":\"操作成功\"}";
	}

	@RequestMapping("/updateDeliver")
	public @ResponseBody String updateDeliver(Model model, @RequestParam("branchid") long branchid) {
		if (branchid > 0) {
			List<User> list = userDAO.getAllUserByRolesAndBranchid("4", branchid);

			return JSONArray.fromObject(list).toString();
		} else {
			return "[]";
		}

	}

}

package cn.explink.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import cn.explink.dao.AccountAreaDAO;
import cn.explink.dao.BranchDAO;
import cn.explink.dao.FunctionDAO;
import cn.explink.domain.Branch;
import cn.explink.service.BranchService;
import cn.explink.util.Page;
import cn.explink.util.StringUtil;

@Controller
@RequestMapping("/branch")
public class BranchController {

	@Autowired
	BranchDAO branchDAO;
	@Autowired
	FunctionDAO functionDAO;
	@Autowired
	JdbcTemplate jdbcTemplate;
	@Autowired
	BranchService branchService;
	@Autowired
	AccountAreaDAO accountareaDAO;

	@RequestMapping("/toNextStopPage")
	public String toNextStopPage(Model model, HttpServletRequest request) {
		model.addAttribute("branches", branchDAO.getAllBranches());
		model.addAttribute("functiones", functionDAO.getFunctionListForType(FunctionDAO.BRANCH_FUNCTION_TYPE_NO));
		return "branch/nextStop";
	}

	@RequestMapping("/saveNextStop")
	public String save(Model model, @RequestParam("branchid") long branchid, @RequestParam("cwbtobranchid") String cwbtobranchid, @RequestParam("functionids") String functionids) throws Exception {
		branchDAO.saveBranchCwbtobranchid(branchid, cwbtobranchid, functionids);
		model.addAttribute("branches", branchDAO.getAllBranches());
		model.addAttribute("functiones", functionDAO.getFunctionListForType(FunctionDAO.BRANCH_FUNCTION_TYPE_NO));
		model.addAttribute("errorState", "保存成功");
		return "branch/nextStop";
	}

	@RequestMapping("/add")
	public String add(Model model) {
		model.addAttribute("accontareaList", accountareaDAO.getAllAccountArea());
		return "branch/add";
	}

	@RequestMapping("/createFile")
	public @ResponseBody String createFile(@RequestParam(value = "Filedata", required = false) MultipartFile file, Model model, HttpServletRequest request) {
		String branchname = StringUtil.nullConvertToEmptyString(request.getParameter("branchname"));
		List<Branch> list = branchDAO.getBranchByBranchnameCheck(branchname);
		if (list.size() > 0) {
			return "{\"errorCode\":1,\"error\":\"机构名称已存在\"}";
		} else {
			Branch bh = branchService.loadFormForBranch(request, file);
			branchDAO.creBranch(bh);
			return "{\"errorCode\":0,\"error\":\"创建成功\",\"type\":\"add\"}";
		}
	}

	@RequestMapping("/create")
	public @ResponseBody String create(Model model, HttpServletRequest request) {
		String branchname = StringUtil.nullConvertToEmptyString(request.getParameter("branchname"));
		List<Branch> list = branchDAO.getBranchByBranchnameCheck(branchname);
		if (list.size() > 0) {
			return "{\"errorCode\":1,\"error\":\"机构名称已存在\"}";
		} else {
			Branch bh = branchService.loadFormForBranch(request, null);
			branchDAO.creBranch(bh);
			return "{\"errorCode\":0,\"error\":\"创建成功\"}";
		}
	}

	@RequestMapping("/branchnamecheck")
	public @ResponseBody boolean branchnamecheck(@RequestParam("branchname") String branchname) throws Exception {
		branchname = new String(branchname.getBytes("ISO8859-1"), "utf-8");
		List<Map<String, Object>> list = jdbcTemplate.queryForList("SELECT * from express_set_branch where branchname=?", branchname);
		if (list.size() == 0) {
			return true;
		} else {
			return false;
		}
	}

	@RequestMapping("/saveFile/{id}")
	public @ResponseBody String saveFile(@PathVariable("id") long branchid, @RequestParam(value = "wavh", required = false) String wavh,
			@RequestParam(value = "Filedata", required = false) MultipartFile file, Model model, HttpServletRequest request) {
		String branchname = StringUtil.nullConvertToEmptyString(request.getParameter("branchname"));
		List<Branch> list = branchDAO.getBranchByBranchnameCheck(branchname);
		if (list.size() > 0 && list.get(0).getBranchid() != branchid) {
			return "{\"errorCode\":1,\"error\":\"站点名称已存在\"}";
		} else {
			Branch branch = branchService.loadFormForBranch(request, file, wavh);
			branch.setBranchid(branchid);
			branchDAO.saveBranch(branch);
			return "{\"errorCode\":0,\"error\":\"保存成功\",\"type\":\"edit\"}";
		}
	}

	@RequestMapping("/save/{id}")
	public @ResponseBody String save(@PathVariable("id") long branchid, @RequestParam(value = "wavh", required = false) String wavh, Model model, HttpServletRequest request) {
		String branchname = StringUtil.nullConvertToEmptyString(request.getParameter("branchname"));
		List<Branch> list = branchDAO.getBranchByBranchnameCheck(branchname);
		if (list.size() > 0 && list.get(0).getBranchid() != branchid) {
			return "{\"errorCode\":1,\"error\":\"站点名称已存在\"}";
		} else {
			Branch branch = branchService.loadFormForBranch(request, null, wavh);
			branch.setBranchid(branchid);
			branchDAO.saveBranch(branch);
			return "{\"errorCode\":0,\"error\":\"保存成功\"}";
		}
	}

	@RequestMapping("/edit/{id}")
	public String edit(@PathVariable("id") long branchid, Model model) {
		model.addAttribute("b", branchDAO.getBranchById(branchid));
		model.addAttribute("accontareaList", accountareaDAO.getAllAccountArea());
		return "branch/edit";
	}

	@RequestMapping("/list/{page}")
	public String list(@PathVariable("page") long page, Model model, @RequestParam(value = "branchname", required = false, defaultValue = "") String branchname,
			@RequestParam(value = "branchaddress", required = false, defaultValue = "") String branchaddress) {

		model.addAttribute("branches", branchDAO.getBranchByPage(page, branchname, branchaddress));
		model.addAttribute("page_obj", new Page(branchDAO.getBranchCount(branchname, branchaddress), page, Page.ONE_PAGE_NUMBER));
		model.addAttribute("page", page);
		return "branch/list";
	}

}

package cn.explink.controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import cn.explink.b2c.maisike.stores.StoresDAO;
import cn.explink.dao.AccountAreaDAO;
import cn.explink.dao.BranchDAO;
import cn.explink.dao.CwbDAO;
import cn.explink.dao.MenuDAO;
import cn.explink.dao.PaiFeiRuleDAO;
import cn.explink.dao.SystemInstallDAO;
import cn.explink.domain.Branch;
import cn.explink.domain.SystemInstall;
import cn.explink.domain.User;
import cn.explink.enumutil.BranchEnum;
import cn.explink.enumutil.PaiFeiRuleTypeEnum;
import cn.explink.schedule.Constants;
import cn.explink.service.BranchService;
import cn.explink.service.ExplinkUserDetail;
import cn.explink.service.ScheduledTaskService;
import cn.explink.service.SystemInstallService;
import cn.explink.util.JSONReslutUtil;
import cn.explink.util.Page;
import cn.explink.util.StringUtil;

@Controller
@RequestMapping("/branch")
public class BranchController {

	@Autowired
	BranchDAO branchDAO;
	@Autowired
	JdbcTemplate jdbcTemplate;
	@Autowired
	BranchService branchService;
	@Autowired
	AccountAreaDAO accountareaDAO;
	@Autowired
	MenuDAO menuDAO;
	@Autowired
	CwbDAO cwbDAO;
	@Autowired
	SystemInstallDAO systemInstallDAO;
	@Autowired
	SecurityContextHolderStrategy securityContextHolderStrategy;
	@Autowired
	StoresDAO storesDAO;
	@Autowired
	ScheduledTaskService scheduledTaskService;
	@Autowired
	SystemInstallService systemInstallService;
	@Autowired
	PaiFeiRuleDAO pfFeiRuleDAO;

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	private User getSessionUser() {
		ExplinkUserDetail userDetail = (ExplinkUserDetail) this.securityContextHolderStrategy.getContext().getAuthentication().getPrincipal();
		return userDetail.getUser();
	}

	@RequestMapping("/toNextStopPage")
	public String toNextStopPage(Model model, HttpServletRequest request) {
		model.addAttribute("branches", this.branchDAO.getBranchesByKuFangAndZhanDian());
		return "branch/nextStop";
	}

	@RequestMapping("/saveNextStop")
	public String save(Model model, @RequestParam("branchid") long branchid, @RequestParam("cwbtobranchid") String cwbtobranchid) throws Exception {
		this.branchDAO.saveBranchCwbtobranchid(branchid, cwbtobranchid);
		model.addAttribute("branches", this.branchDAO.getBranchesByKuFangAndZhanDian());
		model.addAttribute("errorState", "保存成功");
		this.logger.info("operatorUser={},机构管理->saveNextStop", this.getSessionUser().getUsername());
		return "branch/nextStop";
	}

	@RequestMapping("/add")
	public String add(Model model) {
		model.addAttribute("accontareaList", this.accountareaDAO.getAllAccountArea());
		model.addAttribute("PDAmenu", this.menuDAO.getPDAMenus());
		model.addAttribute("zhongzhuanList", this.branchDAO.getBranchBySiteType(BranchEnum.ZhongZhuan.getValue()));
		model.addAttribute("tuihuoList", this.branchDAO.getBranchBySiteType(BranchEnum.TuiHuo.getValue()));
		model.addAttribute("caiwuList", this.branchDAO.getBranchBySiteType(BranchEnum.CaiWu.getValue()));

		model.addAttribute("bindmsksid", this.systemInstallDAO.getSystemInstallByName("maisike_id_flag"));
		model.addAttribute("mskbranchlist", this.storesDAO.getMaisiBranchList());
		model.addAttribute("pfrulelist", this.pfFeiRuleDAO.getPaiFeiRuleByType(PaiFeiRuleTypeEnum.Franchisee.getValue()));

		// 站点结算对象设置
		String accountBranch = "";
		accountBranch = String.valueOf(BranchEnum.CaiWu.getValue() + "," + BranchEnum.ZhanDian.getValue());
		model.addAttribute("accountbranchList", this.branchDAO.getBanchByBranchidForStock(accountBranch));

		return "branch/add";
	}

	@RequestMapping("/createFile")
	public @ResponseBody
	String createFile(@RequestParam(value = "Filedata", required = false) MultipartFile file, @RequestParam(value = "functionids", required = false) List<String> functionids, Model model,
			HttpServletRequest request) {
		String branchname = StringUtil.nullConvertToEmptyString(request.getParameter("branchname"));
		String branchcode = StringUtil.nullConvertToEmptyString(request.getParameter("branchcode"));
		List<Branch> list = this.branchDAO.getBranchByBranchnameCheck(branchname);
		List<Branch> codeList = this.branchDAO.getBranchByBranchcodeCheck(branchcode);
		if (list.size() > 0) {
			return "{\"errorCode\":1,\"error\":\"机构名称已存在\"}";
		} else if ((codeList.size() > 0) && (codeList != null)) {
			return "{\"errorCode\":1,\"error\":\"机构编号已存在\"}";
		} else {
			Branch bh = this.branchService.loadFormForBranch(request, file, functionids);
			if (bh.getSitetype() == BranchEnum.ZhanDian.getValue()) {
				bh.setCheckremandtype(BranchEnum.YuYinTiXing.getValue());
			}
			long branchid = this.branchDAO.creBranch(bh);
			if (bh.getSitetype() == BranchEnum.ZhanDian.getValue()) {
				this.branchService.addzhandianToAddress(branchid, bh);
				// TODO 增加同步代码
				String adressenabled = this.systemInstallService.getParameter("newaddressenabled");
				if ((adressenabled != null) && adressenabled.equals("1")) {
					this.scheduledTaskService.createScheduledTask(Constants.TASK_TYPE_SYN_ADDRESS_BRANCH_CREATE, Constants.REFERENCE_TYPE_BRANCH_ID, String.valueOf(branchid), true);
				}
			}

			try {
				//this.resendOmsPutBranchMap();
			} catch (Exception e) {
				this.logger.info("当机构信息变更时，重新请求oms中的获取站点列表的方法pushBranchMap");
				e.printStackTrace();
			}
			try {
				//this.resendAccountPutBranchMap(request);
			} catch (Exception e) {
				this.logger.info("当机构信息变更时，重新请求account中的获取站点列表的方法pushBranchMap");
				e.printStackTrace();
			}

			this.logger.info("operatorUser={},机构管理->createFile", this.getSessionUser().getUsername());
			return "{\"errorCode\":0,\"error\":\"创建成功\",\"type\":\"add\"}";
		}
	}

	@RequestMapping("/create")
	public @ResponseBody
	String create(Model model, HttpServletRequest request, @RequestParam(value = "functionids", required = false) List<String> functionids) {
		String branchname = StringUtil.nullConvertToEmptyString(request.getParameter("branchname"));
		String branchcode = StringUtil.nullConvertToEmptyString(request.getParameter("branchcode"));
		List<Branch> list = this.branchDAO.getBranchByBranchnameCheck(branchname);
		List<Branch> codeList = this.branchDAO.getBranchByBranchcodeCheck(branchcode);
		if (list.size() > 0) {
			return "{\"errorCode\":1,\"error\":\"机构名称已存在\"}";
		} else if ((codeList.size() > 0) && (codeList != null)) {
			return "{\"errorCode\":1,\"error\":\"机构编号已存在\"}";
		} else {
			Branch bh = this.branchService.loadFormForBranch(request, null, functionids);

			if (bh.getSitetype() == BranchEnum.ZhanDian.getValue()) {
				bh.setCheckremandtype(BranchEnum.YuYinTiXing.getValue());
			}
			long branchid = this.branchDAO.creBranch(bh);
			if (bh.getSitetype() == BranchEnum.ZhanDian.getValue()) {
				this.branchService.addzhandianToAddress(branchid, bh);
				// TODO 增加同步代码
				String adressenabled = this.systemInstallService.getParameter("newaddressenabled");
				if ((adressenabled != null) && adressenabled.equals("1")) {
					this.scheduledTaskService.createScheduledTask(Constants.TASK_TYPE_SYN_ADDRESS_BRANCH_CREATE, Constants.REFERENCE_TYPE_BRANCH_ID, String.valueOf(branchid), true);
				}
			}

			try {
				//this.resendOmsPutBranchMap();
			} catch (Exception e) {
				this.logger.info("当机构信息变更时，重新请求oms中的获取站点列表的方法pushBranchMap");
				e.printStackTrace();
			}
			try {
				this.resendAccountPutBranchMap(request);
			} catch (Exception e) {
				this.logger.info("当机构信息变更时，重新请求account中的获取站点列表的方法pushBranchMap");
				e.printStackTrace();
			}
			this.logger.info("operatorUser={},机构管理->create,站点名称：{}", this.getSessionUser().getUsername(), branchname);
			return "{\"errorCode\":0,\"error\":\"创建成功\"}";
		}
	}

	@RequestMapping("/branchnamecheck")
	public @ResponseBody
	boolean branchnamecheck(@RequestParam("branchname") String branchname) throws Exception {
		branchname = new String(branchname.getBytes("ISO8859-1"), "utf-8");
		List<Map<String, Object>> list = this.jdbcTemplate.queryForList("SELECT * from express_set_branch where branchname=?", branchname);
		if (list.size() == 0) {
			return true;
		} else {
			return false;
		}
	}

	@RequestMapping("/saveFile/{id}")
	public @ResponseBody
	String saveFile(@PathVariable("id") long branchid, @RequestParam(value = "functionids", required = false) List<String> functionids, @RequestParam(value = "wavh", required = false) String wavh,
			@RequestParam(value = "Filedata", required = false) MultipartFile file, Model model, HttpServletRequest request) {

		String branchname = StringUtil.nullConvertToEmptyString(request.getParameter("branchname"));
		String branchcode = StringUtil.nullConvertToEmptyString(request.getParameter("branchcode"));
		List<Branch> list = this.branchDAO.getBranchByBranchnameCheck(branchname);
		List<Branch> codeList = this.branchDAO.getBranchByBranchcodeCheck(branchcode);
		String oldbranchname = this.branchDAO.getBranchById(branchid).getBranchname();
		if ((list.size() > 0) && (list.get(0).getBranchid() != branchid)) {
			return "{\"errorCode\":1,\"error\":\"机构名称已存在\"}";
		} else if ((codeList.size() > 0) && (codeList != null) && (codeList.get(0).getBranchid() != branchid)) {
			return "{\"errorCode\":1,\"error\":\"机构编号已存在\"}";
		} else {
			Branch branch = this.branchService.loadFormForBranch(request, file, wavh, functionids);
			branch.setBranchid(branchid);
			if (branch.getSitetype() == BranchEnum.ZhanDian.getValue()) {
				branch.setCheckremandtype(BranchEnum.YuYinTiXing.getValue());
			}
			this.branchDAO.saveBranch(branch);
			if (branch.getSitetype() == BranchEnum.ZhanDian.getValue()) {
				this.branchService.addzhandianToAddress(branchid, branch);
				// TODO 增加同步代码
				String adressenabled = this.systemInstallService.getParameter("newaddressenabled");
				if ((adressenabled != null) && adressenabled.equals("1")) {
					if (!oldbranchname.equals(branch.getBranchname())) {
						this.scheduledTaskService.createScheduledTask(Constants.TASK_TYPE_SYN_ADDRESS_BRANCH_MODIFY, Constants.REFERENCE_TYPE_BRANCH_ID, String.valueOf(branchid), true);
					}
				}
			}

			try {
				this.resendOmsPutBranchMap();
			} catch (Exception e) {
				this.logger.info("当机构信息变更时，重新请求oms中的获取站点列表的方法pushBranchMap");
				e.printStackTrace();
			}
			try {
				this.resendAccountPutBranchMap(request);
			} catch (Exception e) {
				this.logger.info("当机构信息变更时，重新请求account中的获取站点列表的方法pushBranchMap");
				e.printStackTrace();
			}

			this.logger.info("operatorUser={},机构管理->saveFile", this.getSessionUser().getUsername());
			return "{\"errorCode\":0,\"error\":\"保存成功\",\"type\":\"edit\"}";
		}
	}

	@RequestMapping("/save/{id}")
	public @ResponseBody
	String save(@PathVariable("id") long branchid, @RequestParam(value = "functionids", required = false) List<String> functionids, @RequestParam(value = "wavh", required = false) String wavh,
			Model model, HttpServletRequest request) {
		String branchname = StringUtil.nullConvertToEmptyString(request.getParameter("branchname"));
		String branchcode = StringUtil.nullConvertToEmptyString(request.getParameter("branchcode"));
		List<Branch> list = this.branchDAO.getBranchByBranchnameCheck(branchname);
		List<Branch> codeList = this.branchDAO.getBranchByBranchcodeCheck(branchcode);
		String oldbranchname = this.branchDAO.getBranchById(branchid).getBranchname();
		if ((list.size() > 0) && (list.get(0).getBranchid() != branchid)) {
			return "{\"errorCode\":1,\"error\":\"机构名称已存在\"}";
		} else if ((codeList.size() > 0) && (codeList != null) && (codeList.get(0).getBranchid() != branchid)) {
			return "{\"errorCode\":1,\"error\":\"机构编号已存在\"}";
		} else {
			Branch branch = this.branchService.loadFormForBranch(request, null, wavh, functionids);
			branch.setBranchid(branchid);
			if (branch.getSitetype() == BranchEnum.ZhanDian.getValue()) {
				branch.setCheckremandtype(BranchEnum.YuYinTiXing.getValue());
			}
			if (!branchname.equals(this.branchDAO.getBranchByBranchid(branchid).getBranchname())) {
				this.cwbDAO.saveCwbOrderByExcelbranch(branchname, branchid);
			}

			this.branchDAO.saveBranchNoFile(branch);
			if (branch.getSitetype() == BranchEnum.ZhanDian.getValue()) {
				this.branchService.addzhandianToAddress(branchid, branch);
				// TODO 增加同步代码
				String adressenabled = this.systemInstallService.getParameter("newaddressenabled");
				if ((adressenabled != null) && adressenabled.equals("1")) {
					if (!oldbranchname.equals(branch.getBranchname())) {
						this.scheduledTaskService.createScheduledTask(Constants.TASK_TYPE_SYN_ADDRESS_BRANCH_MODIFY, Constants.REFERENCE_TYPE_BRANCH_ID, String.valueOf(branchid), true);
					}
				}
			}

			try {
				this.resendOmsPutBranchMap();
			} catch (Exception e) {
				this.logger.info("当机构信息变更时，重新请求oms中的获取站点列表的方法pushBranchMap");
				e.printStackTrace();
			}
			try {
				this.resendAccountPutBranchMap(request);
			} catch (Exception e) {
				this.logger.info("当机构信息变更时，重新请求account中的获取站点列表的方法pushBranchMap");
				e.printStackTrace();
			}
			this.logger.info("operatorUser={},机构管理->save,站点名称：{}", this.getSessionUser().getUsername(), branch.getBranchname());
			return "{\"errorCode\":0,\"error\":\"保存成功\"}";
		}
	}

	@RequestMapping("/edit/{id}")
	public String edit(@PathVariable("id") long branchid, Model model) {
		model.addAttribute("b", this.branchDAO.getBranchById(branchid));
		model.addAttribute("accontareaList", this.accountareaDAO.getAllAccountArea());
		model.addAttribute("PDAmenu", this.menuDAO.getPDAMenus());
		model.addAttribute("zhongzhuanList", this.branchDAO.getBranchBySiteType(BranchEnum.ZhongZhuan.getValue()));
		model.addAttribute("tuihuoList", this.branchDAO.getBranchBySiteType(BranchEnum.TuiHuo.getValue()));
		model.addAttribute("caiwuList", this.branchDAO.getBranchBySiteType(BranchEnum.CaiWu.getValue()));
		model.addAttribute("pfrulelist", this.pfFeiRuleDAO.getPaiFeiRuleByType(PaiFeiRuleTypeEnum.Franchisee.getValue()));

		model.addAttribute("bindmsksid", this.systemInstallDAO.getSystemInstallByName("maisike_id_flag"));
		model.addAttribute("mskbranchlist", this.storesDAO.getMaisiBranchList());

		// 站点结算对象设置
		String accountBranch = "";
		accountBranch = String.valueOf(BranchEnum.CaiWu.getValue() + "," + BranchEnum.ZhanDian.getValue());
		model.addAttribute("accountbranchList", this.branchDAO.getBanchByBranchidForStock(accountBranch));
		return "branch/edit";
	}

	@RequestMapping("/list/{page}")
	public String list(@PathVariable("page") long page, Model model, @RequestParam(value = "branchname", required = false, defaultValue = "") String branchname,
			@RequestParam(value = "branchaddress", required = false, defaultValue = "") String branchaddress) {

		model.addAttribute("branches", this.branchDAO.getBranchByPage(page, branchname, branchaddress));
		model.addAttribute("page_obj", new Page(this.branchDAO.getBranchCount(branchname, branchaddress), page, Page.ONE_PAGE_NUMBER));
		model.addAttribute("page", page);
		return "branch/list";
	}

	/**
	 * 当机构信息变更时，重新请求oms中的获取站点列表的方法pushBranchMap
	 */
	public void resendOmsPutBranchMap() {
		SystemInstall omsPathUrl = this.systemInstallDAO.getSystemInstallByName("omsPathUrl");
		SystemInstall omsUrl = this.systemInstallDAO.getSystemInstallByName("omsUrl");
		String url1 = "";
		if ((omsPathUrl != null) && (omsUrl != null)) {
			url1 = omsPathUrl.getValue() + omsUrl.getValue();
		} else {
			url1 = "http://127.0.0.1:8080/oms/";
		}
		final String url = url1;
		try {
			String result = JSONReslutUtil.getResultMessageChangeLog(url + "OMSChange/pushBranchMap", "a=1", "POST").toString();
			if ((result == null) || result.equals("")) {
				this.logger.info("msg", "请求oms的站点异常");
			} else if (result.indexOf("01") > -1) {
				this.logger.info("msg", "oms的站点列表为空");
			}
			this.logger.info("保存oms的branchMap返回：{}", result);
		} catch (IOException e1) {
			this.logger.info("ip请求路径错误！请修改一下系统设置中的omsPathUrl");
		}
	}

	/**
	 * 当机构信息变更时，重新请求account中的获取站点列表的方法pushBranchMap
	 */
	public void resendAccountPutBranchMap(HttpServletRequest request) {
		SystemInstall omsPathUrl = this.systemInstallDAO.getSystemInstallByName("omsPathUrl");
		SystemInstall accountUrl = this.systemInstallDAO.getSystemInstallByName("accountUrl");
		String url1 = "";
		if ((omsPathUrl != null) && (accountUrl != null)) {
			url1 = omsPathUrl.getValue() + accountUrl.getValue();
		} else {
			url1 = "http://127.0.0.1:8080/account/";
		}
		final String url = url1;
		try {
			String dmpid = request.getSession().getId() == null ? "" : request.getSession().getId();
			String result = JSONReslutUtil.getResultMessageChangeLog(url + "jmsCenter/pushBranchMap", "dmpid=" + dmpid, "POST",1).toString();
			if ((result == null) || result.equals("")) {
				this.logger.info("msg", "请求account的站点异常");
			} else if (result.indexOf("01") > -1) {
				this.logger.info("msg", "account的站点列表为空");
			}
			this.logger.info("保存account的branchMap返回：{}", result);
		} catch (IOException e1) {
			this.logger.info("ip请求路径错误！请修改一下系统设置中的omsPathUrl");
		}
	}

	@RequestMapping("/del/{id}")
	public @ResponseBody
	String del(@PathVariable("id") long branchid) {
		this.branchDAO.delBranch(branchid);
		Branch branch = this.branchDAO.getBranchByBranchid(branchid);
		this.logger.info("operatorUser={},机构管理->del,站点id：{}", this.getSessionUser().getUsername(), branchid);
		if (branch.getBrancheffectflag().equals("1")) {
			this.branchService.addzhandianToAddress(branchid, branch);
		} else {
			this.branchService.delBranch(branchid);
		}
		// TODO 增加同步代码
		String adressenabled = this.systemInstallService.getParameter("newaddressenabled");
		if ((adressenabled != null) && adressenabled.equals("1")) {
			String brancheffectflag = this.branchDAO.getBranchById(branchid).getBrancheffectflag();
			if ("0".equals(brancheffectflag)) {
				this.scheduledTaskService.createScheduledTask(Constants.TASK_TYPE_SYN_ADDRESS_BRANCH_DELETE, Constants.REFERENCE_TYPE_BRANCH_ID, String.valueOf(branchid), true);
			} else {
				this.scheduledTaskService.createScheduledTask(Constants.TASK_TYPE_SYN_ADDRESS_BRANCH_CREATE, Constants.REFERENCE_TYPE_BRANCH_ID, String.valueOf(branchid), true);
			}
		}
		return "{\"errorCode\":0,\"error\":\"操作成功\"}";
	}

}

package cn.explink.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import cn.explink.b2c.maisike.branchsyn_json.Stores;
import cn.explink.b2c.maisike.stores.StoresDAO;
import cn.explink.core.utils.PoiExcelUtils;
import cn.explink.core.utils.PoiExcelUtils.ColDef;
import cn.explink.dao.AccountAreaDAO;
import cn.explink.dao.BranchDAO;
import cn.explink.dao.CwbDAO;
import cn.explink.dao.MenuDAO;
import cn.explink.dao.PaiFeiRuleDAO;
import cn.explink.dao.SystemInstallDAO;
import cn.explink.domain.Branch;
import cn.explink.domain.BranchSyncResultVo;
import cn.explink.domain.Menu;
import cn.explink.domain.PaiFeiRule;
import cn.explink.domain.SystemInstall;
import cn.explink.domain.User;
import cn.explink.enumutil.BranchEnum;
import cn.explink.enumutil.PaiFeiRuleTypeEnum;
import cn.explink.schedule.Constants;
import cn.explink.service.BankService;
import cn.explink.service.BranchInfService;
import cn.explink.service.BranchService;
import cn.explink.service.ExplinkUserDetail;
import cn.explink.service.ExportService;
import cn.explink.service.ScheduledTaskService;
import cn.explink.service.SystemInstallService;
import cn.explink.util.AjaxResult;
import cn.explink.util.ExcelUtils;
import cn.explink.util.JSONReslutUtil;
import cn.explink.util.JSR303ValidationManager;
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
	@Autowired
	private BankService bankService;
	@Autowired
	ExportService exportService;
	@Autowired
	BranchInfService branchInfService;
	
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
		this.initOrganizationControllData(model);
		return "branch/add";
	}

	@RequestMapping("/createFile")
	public @ResponseBody String createFile(@RequestParam(value = "Filedata", required = false) MultipartFile file, @RequestParam(value = "functionids", required = false) List<String> functionids, Model model, HttpServletRequest request) {
		String branchname = StringUtil.nullConvertToEmptyString(request.getParameter("branchname"));
		String branchcode = StringUtil.nullConvertToEmptyString(request.getParameter("branchcode"));
		String tpsbranchcode = StringUtil.nullConvertToEmptyString(request.getParameter("tpsbranchcode")==null?null:request.getParameter("tpsbranchcode").trim());
		List<Branch> list = this.branchDAO.getBranchByBranchnameCheck(branchname);
		List<Branch> codeList = this.branchDAO.getBranchByBranchcodeCheck(branchcode);
		List<Branch> tpscodeList = this.branchDAO.getBranchByTpsBranchcodeCheck(tpsbranchcode);
		if (list.size() > 0) {
			return "{\"errorCode\":1,\"error\":\"机构名称已存在\"}";
		} else if ((codeList.size() > 0) && (codeList != null)) {
			return "{\"errorCode\":1,\"error\":\"分拣码已存在\"}";
		} else if ((tpscodeList != null) && (tpscodeList.size() > 0)) {
			return "{\"errorCode\":1,\"error\":\"机构编码已存在\"}";
		} else {
			Branch bh = this.branchService.loadFormForBranch(request, file, functionids);
			AjaxResult rs =JSR303ValidationManager.getInstance().doValidateAsAjaxResult(bh);
			if (!rs.isResult()) {
				return "{\"errorCode\":1,\"error\":\"" + rs.getMessage() +"\"}";
			}
			if (bh.getSitetype() == BranchEnum.ZhanDian.getValue()) {
				bh.setCheckremandtype(BranchEnum.YuYinTiXing.getValue());
			}
			long branchid;
			try {
				branchid = branchService.creBranchAndSyncOsp(bh);
			} catch (Exception e) {
				String errorMessage = "操作失败，无法保存到本地或者同步到机构服务，原因：" + e.getMessage();
				return "{\"errorCode\":1,\"error\":\"" + errorMessage +"\"}";
			}
			bh = branchDAO.getBranchByBranchid(branchid);
			if (bh.getSitetype() == BranchEnum.ZhanDian.getValue()) {
				if(!branchInfService.isCloseOldInterface()){					
					this.branchService.addzhandianToAddress(branchid, bh,null);
				}
				branchInfService.saveBranchInf(bh, getSessionUser());
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
				this.logger.error("", e);
			}
			try {
				//this.resendAccountPutBranchMap(request);
			} catch (Exception e) {
				this.logger.info("当机构信息变更时，重新请求account中的获取站点列表的方法pushBranchMap");
				this.logger.error("", e);
			}

			this.logger.info("operatorUser={},机构管理->createFile", this.getSessionUser().getUsername());
			return "{\"errorCode\":0,\"error\":\"创建成功\",\"type\":\"add\"}";
		}
	}

	@RequestMapping("/create")
	public @ResponseBody String create(Model model, HttpServletRequest request, @RequestParam(value = "functionids", required = false) List<String> functionids) {
		String branchname = StringUtil.nullConvertToEmptyString(request.getParameter("branchname"));
		String branchcode = StringUtil.nullConvertToEmptyString(request.getParameter("branchcode"));
		String tpsbranchcode = StringUtil.nullConvertToEmptyString(request.getParameter("tpsbranchcode")==null?null:request.getParameter("tpsbranchcode").trim());
		List<Branch> list = this.branchDAO.getBranchByBranchnameCheck(branchname);
		List<Branch> codeList = this.branchDAO.getBranchByBranchcodeCheck(branchcode);
		List<Branch> tpscodeList = this.branchDAO.getBranchByTpsBranchcodeCheck(tpsbranchcode);
		
		if (list.size() > 0) {
			return "{\"errorCode\":1,\"error\":\"机构名称已存在\"}";
		} else if ((codeList.size() > 0) && (codeList != null)) {
			return "{\"errorCode\":1,\"error\":\"分拣码已存在\"}";
		} else if ((tpscodeList != null) && (tpscodeList.size() > 0)) {
			return "{\"errorCode\":1,\"error\":\"机构编码已存在\"}";
		}else {
			Branch bh = this.branchService.loadFormForBranch(request, null, functionids);
			AjaxResult rs =JSR303ValidationManager.getInstance().doValidateAsAjaxResult(bh);
			if (!rs.isResult()) {
				return "{\"errorCode\":1,\"error\":\"" + rs.getMessage() +"\"}";
			}
			if (bh.getSitetype() == BranchEnum.ZhanDian.getValue()) {
				bh.setCheckremandtype(BranchEnum.YuYinTiXing.getValue());
			}
			long branchid;
			try {
				branchid = branchService.creBranchAndSyncOsp(bh);
			} catch (Exception e) {
				String errorMessage = "操作失败，无法保存到本地或者同步到机构服务，原因：" + e.getMessage();
				return "{\"errorCode\":1,\"error\":\"" + errorMessage +"\"}";
			}
			bh = this.branchDAO.getBranchByBranchid(branchid);
			if (bh.getSitetype() == BranchEnum.ZhanDian.getValue()) {
				if(!branchInfService.isCloseOldInterface()){
					this.branchService.addzhandianToAddress(branchid, bh,null);
				}
				// TODO 增加同步代码
				String adressenabled = this.systemInstallService.getParameter("newaddressenabled");
				if ((adressenabled != null) && adressenabled.equals("1")) {
					this.scheduledTaskService.createScheduledTask(Constants.TASK_TYPE_SYN_ADDRESS_BRANCH_CREATE, Constants.REFERENCE_TYPE_BRANCH_ID, String.valueOf(branchid), true);
				}
				// add by jian_xie
				branchInfService.saveBranchInf(bh, getSessionUser());
			}

			try {
				//this.resendOmsPutBranchMap();
			} catch (Exception e) {
				this.logger.info("当机构信息变更时，重新请求oms中的获取站点列表的方法pushBranchMap");
				this.logger.error("", e);
			}
			try {
				this.resendAccountPutBranchMap(request);
			} catch (Exception e) {
				this.logger.info("当机构信息变更时，重新请求account中的获取站点列表的方法pushBranchMap");
				this.logger.error("", e);
			}
			this.logger.info("operatorUser={},机构管理->create,站点名称：{}", this.getSessionUser().getUsername(), branchname);
			return "{\"errorCode\":0,\"error\":\"创建成功\"}";
		}
	}

	@RequestMapping("/branchnamecheck")
	public @ResponseBody boolean branchnamecheck(@RequestParam("branchname") String branchname) throws Exception {
		branchname = new String(branchname.getBytes("ISO8859-1"), "utf-8");
		List<Map<String, Object>> list = this.jdbcTemplate.queryForList("SELECT * from express_set_branch where branchname=?", branchname);
		if (list.size() == 0) {
			return true;
		} else {
			return false;
		}
	}

	@RequestMapping("/saveFile/{id}")
	public @ResponseBody String saveFile(@PathVariable("id") long branchid, @RequestParam(value = "functionids", required = false) List<String> functionids, @RequestParam(value = "wavh", required = false) String wavh, @RequestParam(value = "Filedata", required = false) MultipartFile file, Model model, HttpServletRequest request) {

		String branchname = StringUtil.nullConvertToEmptyString(request.getParameter("branchname"));
		String branchcode = StringUtil.nullConvertToEmptyString(request.getParameter("branchcode"));
		String tpsbranchcode = StringUtil.nullConvertToEmptyString(request.getParameter("tpsbranchcode")==null?null:request.getParameter("tpsbranchcode").trim());
		List<Branch> list = this.branchDAO.getBranchByBranchnameCheck(branchname);
		List<Branch> codeList = this.branchDAO.getBranchByBranchcodeCheck(branchcode);
		List<Branch> tpscodeList = this.branchDAO.getBranchByTpsBranchcodeCheck(tpsbranchcode);
		Branch branchVO=this.branchDAO.getBranchById(branchid);
		String oldbranchname = branchVO.getBranchname();
		if ((list.size() > 0) && (list.get(0).getBranchid() != branchid)) {
			return "{\"errorCode\":1,\"error\":\"机构名称已存在\"}";
		} else if ((codeList.size() > 0) && (codeList != null) && (codeList.get(0).getBranchid() != branchid)) {
			return "{\"errorCode\":1,\"error\":\"分拣码已存在\"}";
		}else if ((tpscodeList != null) && (tpscodeList.size() > 0) && (tpscodeList.get(0).getBranchid() != branchid)) {
			return "{\"errorCode\":1,\"error\":\"机构编码已存在\"}";
		} else {
			Branch branch = this.branchService.loadFormForBranch(request, file, wavh, functionids);
			AjaxResult rs =JSR303ValidationManager.getInstance().doValidateAsAjaxResult(branch);
			if (!rs.isResult()) {
				return "{\"errorCode\":1,\"error\":\"" + rs.getMessage() +"\"}";
			}
			branch.setBranchid(branchid);
			if (branch.getSitetype() == BranchEnum.ZhanDian.getValue()) {
				branch.setCheckremandtype(BranchEnum.YuYinTiXing.getValue());
			}
			Branch oldBranch =this.branchDAO.getBranchByBranchid(branchid);
			try {
				branchService.saveBranchAndSyncOsp(branch);
			} catch (Exception e) {
				String errorMessage = "操作失败，无法保存到本地或者同步到机构服务，原因：" + e.getMessage();
				return "{\"errorCode\":1,\"error\":\"" + errorMessage +"\"}";
			}
			branch = branchDAO.getBranchByBranchid(branchid);
			if (branch.getSitetype() == BranchEnum.ZhanDian.getValue()) {
				if(!branchInfService.isCloseOldInterface()){
					this.branchService.addzhandianToAddress(branchid, branch,oldBranch.getTpsbranchcode());
				}
				// TODO 增加同步代码
				String adressenabled = this.systemInstallService.getParameter("newaddressenabled");
				if ((adressenabled != null) && adressenabled.equals("1")) {
					if (!oldbranchname.equals(branch.getBranchname())) {
						this.scheduledTaskService.createScheduledTask(Constants.TASK_TYPE_SYN_ADDRESS_BRANCH_MODIFY, Constants.REFERENCE_TYPE_BRANCH_ID, String.valueOf(branchid), true);
					}
				}
				branchInfService.saveBranchInf(branch, getSessionUser());
			}

			try {
				this.resendOmsPutBranchMap();
			} catch (Exception e) {
				this.logger.info("当机构信息变更时，重新请求oms中的获取站点列表的方法pushBranchMap");
				this.logger.error("", e);
			}
			try {
				this.resendAccountPutBranchMap(request);
			} catch (Exception e) {
				this.logger.info("当机构信息变更时，重新请求account中的获取站点列表的方法pushBranchMap");
				this.logger.error("", e);
			}

			this.logger.info("operatorUser={},机构管理->saveFile", this.getSessionUser().getUsername());
			return "{\"errorCode\":0,\"error\":\"保存成功\",\"type\":\"edit\"}";
		}
	}

	@RequestMapping("/save/{id}")
	public @ResponseBody String save(@PathVariable("id") long branchid, @RequestParam(value = "functionids", required = false) List<String> functionids, @RequestParam(value = "wavh", required = false) String wavh, Model model, HttpServletRequest request) {
		String branchname = StringUtil.nullConvertToEmptyString(request.getParameter("branchname"));
		String branchcode = StringUtil.nullConvertToEmptyString(request.getParameter("branchcode"));
		String tpsbranchcode = StringUtil.nullConvertToEmptyString(request.getParameter("tpsbranchcode")==null?null:request.getParameter("tpsbranchcode").trim());
		List<Branch> list = this.branchDAO.getBranchByBranchnameCheck(branchname);
		List<Branch> codeList = this.branchDAO.getBranchByBranchcodeCheck(branchcode);
		List<Branch> tpscodeList = this.branchDAO.getBranchByTpsBranchcodeCheck(tpsbranchcode);
		Branch branchVO=this.branchDAO.getBranchById(branchid);
		String oldbranchname = branchVO.getBranchname();
		if ((list.size() > 0) && (list.get(0).getBranchid() != branchid)) {
			return "{\"errorCode\":1,\"error\":\"机构名称已存在\"}";
		} else if ((codeList.size() > 0) && (codeList != null) && (codeList.get(0).getBranchid() != branchid)) {
			return "{\"errorCode\":1,\"error\":\"分拣码已存在\"}";
		}else if ((tpscodeList != null) && (tpscodeList.size() > 0) && (tpscodeList.get(0).getBranchid() != branchid)) {
			return "{\"errorCode\":1,\"error\":\"机构编码已存在\"}";
		}else {
			Branch branch = this.branchService.loadFormForBranch(request, null, wavh, functionids);
			branch.setBranchid(branchid);
			if (branch.getSitetype() == BranchEnum.ZhanDian.getValue()) {
				branch.setCheckremandtype(BranchEnum.YuYinTiXing.getValue());
			}
			Branch oldBranch =this.branchDAO.getBranchByBranchid(branchid);
			if (!branchname.equals(oldBranch.getBranchname())) {
				this.cwbDAO.saveCwbOrderByExcelbranch(branchname, branchid);
			}
			AjaxResult rs =JSR303ValidationManager.getInstance().doValidateAsAjaxResult(branch);
			if (!rs.isResult()) {
				return "{\"errorCode\":1,\"error\":\"" + rs.getMessage() +"\"}";
			}
			try {
				branchService.saveBranchAndSyncOsp(branch);
			} catch (Exception e) {
				String errorMessage = "操作失败，无法保存到本地或者同步到机构服务，原因：" + e.getMessage();
				return "{\"errorCode\":1,\"error\":\"" + errorMessage +"\"}";
			}
			branch = this.branchDAO.getBranchByBranchid(branchid);
			if (branch.getSitetype() == BranchEnum.ZhanDian.getValue()) {
				if(!branchInfService.isCloseOldInterface()){
					this.branchService.addzhandianToAddress(branchid, branch,oldBranch.getTpsbranchcode());
				}
				// TODO 增加同步代码
				String adressenabled = this.systemInstallService.getParameter("newaddressenabled");
				if ((adressenabled != null) && adressenabled.equals("1")) {
					if (!oldbranchname.equals(branch.getBranchname())) {
						this.scheduledTaskService.createScheduledTask(Constants.TASK_TYPE_SYN_ADDRESS_BRANCH_MODIFY, Constants.REFERENCE_TYPE_BRANCH_ID, String.valueOf(branchid), true);
					}
				}
				// 同步站点新接口 add by jian_xie
				branchInfService.saveBranchInf(branch, getSessionUser());
			}

			try {
				this.resendOmsPutBranchMap();
			} catch (Exception e) {
				this.logger.info("当机构信息变更时，重新请求oms中的获取站点列表的方法pushBranchMap");
				this.logger.error("", e);
			}
			try {
				this.resendAccountPutBranchMap(request);
			} catch (Exception e) {
				this.logger.info("当机构信息变更时，重新请求account中的获取站点列表的方法pushBranchMap");
				this.logger.error("", e);
			}
			this.logger.info("operatorUser={},机构管理->save,站点名称：{}", this.getSessionUser().getUsername(), branch.getBranchname());
			return "{\"errorCode\":0,\"error\":\"保存成功\"}";
		}
	}

	@RequestMapping("/edit/{id}")
	public String edit(@PathVariable("id") long branchid, Model model) {
		model = this.initOrganizationControllData(model); 
		model.addAttribute("b", this.branchDAO.getBranchById(branchid));
		return "branch/edit";
	}

	/**
	 * 初始化机构管理
	 */
	private Model initOrganizationControllData(Model model){
	
//		List<AccountArea> accontareaList = this.accountareaDAO.getAllAccountArea() ;
		List<Menu> PDAmenu = this.menuDAO.getPDAMenus() ;
		List<Branch> zhongzhuanList = this.branchDAO.getBranchBySiteType(BranchEnum.ZhongZhuan.getValue()) ;
		List<Branch> tuihuoList = this.branchDAO.getBranchBySiteType(BranchEnum.TuiHuo.getValue()) ;
		List<Branch> caiwuList = this.branchDAO.getBranchBySiteType(BranchEnum.CaiWu.getValue()) ;
		List<PaiFeiRule> pfrulelist = this.pfFeiRuleDAO.getPaiFeiRuleByType(PaiFeiRuleTypeEnum.Franchisee.getValue()) ;
		SystemInstall bindmsksid = this.systemInstallDAO.getSystemInstallByName("maisike_id_flag") ;
		List<Stores> mskbranchlist = this.storesDAO.getMaisiBranchList() ;
		List<Branch> accountbranchList = this.branchDAO.getBanchByBranchidForStock(String.valueOf(BranchEnum.CaiWu.getValue() + "," + BranchEnum.ZhanDian.getValue())); 
		List<JSONObject> tlBankList = this.bankService.getTlBankList() ;
		List<JSONObject> cftBankList = this.bankService.getCftBankList() ;
		
//		model.addAttribute("accontareaList", accontareaList);
		model.addAttribute("PDAmenu", PDAmenu);
		model.addAttribute("zhongzhuanList", zhongzhuanList);
		model.addAttribute("tuihuoList", tuihuoList);
		model.addAttribute("caiwuList", caiwuList);

		model.addAttribute("bindmsksid", bindmsksid);
		model.addAttribute("mskbranchlist", mskbranchlist);
		model.addAttribute("pfrulelist", pfrulelist);
		model.addAttribute("accountbranchList", accountbranchList);
		model.addAttribute("tlBankList", tlBankList);
		model.addAttribute("cftBankList", cftBankList);
		return model ;
	}
	
	@RequestMapping("/list/{page}")
	public String list(@PathVariable("page") long page, Model model, @RequestParam(value = "branchname", required = false, defaultValue = "") String branchname, @RequestParam(value = "branchaddress", required = false, defaultValue = "") String branchaddress) {

		model.addAttribute("branches", this.branchDAO.getBranchByPage(page, branchname, branchaddress));
		model.addAttribute("page_obj", new Page(this.branchDAO.getBranchCount(branchname, branchaddress), page, Page.ONE_PAGE_NUMBER));
		model.addAttribute("page", page);
		User user=this.getSessionUser();
		String userloginname=user.getUsername();
		model.addAttribute("flag", userloginname.equals("admin") ? true : false);
		return "branch/list";
	}

	/**
	 * 当机构信息变更时，重新请求oms中的获取站点列表的方法pushBranchMap
	 */
	public void resendOmsPutBranchMap() {
		//SystemInstall omsPathUrl = this.systemInstallDAO.getSystemInstallByName("omsPathUrl");
		SystemInstall omsUrl = this.systemInstallDAO.getSystemInstallByName("omsUrl");
		String url1 = "";
		if (omsUrl != null) {
			url1 = omsUrl.getValue();
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
			String result = JSONReslutUtil.getResultMessageChangeLog(url + "jmsCenter/pushBranchMap", "dmpid=" + dmpid, "POST", 1).toString();
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
	public @ResponseBody String del(@PathVariable("id") long branchid) {
		try {
			branchService.delBranchAndSyncOsp(branchid);
		} catch (Exception e) {
			String errorMessage = "操作失败，无法保存到本地或者同步到机构服务，原因：" + e.getMessage();
			return "{\"errorCode\":1,\"error\":\"" + errorMessage +"\"}";
		}
		Branch branch = this.branchDAO.getBranchByBranchid(branchid);
		this.logger.info("operatorUser={},机构管理->del,站点id：{}", this.getSessionUser().getUsername(), branchid);
		if (!branchInfService.isCloseOldInterface()) {
			if (branch.getBrancheffectflag().equals("1")) {
				this.branchService.addzhandianToAddress(branchid, branch, null);
			} else {
				this.branchService.delBranch(branchid);
			}
		}
		// TODO 增加同步代码
		String adressenabled = this.systemInstallService.getParameter("newaddressenabled");
		if ((adressenabled != null) && adressenabled.equals("1")) {
			String brancheffectflag = this.branchDAO.getBranchByBranchid(branchid).getBrancheffectflag();
			if ("0".equals(brancheffectflag)) {
				this.scheduledTaskService.createScheduledTask(Constants.TASK_TYPE_SYN_ADDRESS_BRANCH_DELETE, Constants.REFERENCE_TYPE_BRANCH_ID, String.valueOf(branchid), true);
			} else {
				this.scheduledTaskService.createScheduledTask(Constants.TASK_TYPE_SYN_ADDRESS_BRANCH_CREATE, Constants.REFERENCE_TYPE_BRANCH_ID, String.valueOf(branchid), true);
			}
		}
		// 同步站点机构，使用新接口
		if (branch.getSitetype() == BranchEnum.ZhanDian.getValue()) {
			branchInfService.saveBranchInf(branch, getSessionUser());
		}
		
		return "{\"errorCode\":0,\"error\":\"操作成功\"}";
	}
	
	@RequestMapping(value="/exportbranchinfo",method=RequestMethod.POST)
	public void exportbranchinfo(HttpServletRequest request,HttpServletResponse response){
		String[] cloumnName1 = new String[8]; // 导出的列名
		String[] cloumnName2 = new String[8]; // 导出的英文列名
		exportService.setBranchInfo(cloumnName1, cloumnName2);
		final String[] cloumnName = cloumnName1;
		final String[] cloumnName3 = cloumnName2;
		String sheetName = "机构信息"; // sheet的名称
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
		String fileName = "BranchInfo_" + df.format(new Date()) + ".xlsx"; // 文件名
		
	try{		
		String branchname=request.getParameter("branchname");
		String branchaddress=request.getParameter("branchaddress");
		final List<Branch> branchList=branchDAO.getBranchByBranchnameAndBranchaddress(branchname, branchaddress);
		ExcelUtils excelUtil = new ExcelUtils() { // 生成工具类实例，并实现填充数据的抽象方法
			@Override
			public void fillData(Sheet sheet, CellStyle style) {
				for (int k = 0; k < branchList.size(); k++) {
					Row row = sheet.createRow(k + 1);
					row.setHeightInPoints(15);
					for (int i = 0; i < cloumnName.length; i++) {
						Cell cell = row.createCell((short) i);
						cell.setCellStyle(style);
						Object a = null;
						// 给导出excel赋值
						a = exportService.setBranchInfoObject(cloumnName3,branchList,a, i, k);
						cell.setCellValue(a == null ? "" : a.toString());
					}
				}
			}
		};
		
		excelUtil.excel(response, cloumnName, sheetName, fileName);
	} catch (Exception e) {
		this.logger.error("", e);
	}
	
	}

	@RequestMapping("/syncAllBranchToOsp")
	public void syncAllBranchToOsp(HttpServletRequest request, HttpServletResponse response) throws Exception {
		List<BranchSyncResultVo> result = branchService.batchSyncBranchOsp();
		List<ColDef> colDefs = getBranchSyncResultColDefs();
		Workbook workbook = PoiExcelUtils.createExcelSheet(colDefs, (List) result);
		PoiExcelUtils.setExcelResponseHeader(response, "batch_sync_branch_result.xlsx");
		OutputStream out = response.getOutputStream();
		workbook.write(out);
		out.flush();
	}
	
	private List<ColDef> getBranchSyncResultColDefs() {
		List<ColDef> colDefs = new ArrayList<ColDef>();
		colDefs.add(new ColDef("branchName", "机构名称", 100));
		colDefs.add(new ColDef("carrierCode", "承运商编码", 100));
		colDefs.add(new ColDef("carrierSiteCode", "机构编码", 100));
		colDefs.add(new ColDef("result", "结果", 200));
		colDefs.add(new ColDef("message", "原因", 500));
		return colDefs;
	}
}

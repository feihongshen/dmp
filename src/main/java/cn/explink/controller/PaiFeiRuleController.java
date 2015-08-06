/**
 *
 */
package cn.explink.controller;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.explink.dao.AreaDAO;
import cn.explink.dao.BranchDAO;
import cn.explink.dao.CustomerDAO;
import cn.explink.dao.PFAreaDAO;
import cn.explink.dao.PFbasicDAO;
import cn.explink.dao.PFbusinessDAO;
import cn.explink.dao.PFcollectionDAO;
import cn.explink.dao.PFinsertionDAO;
import cn.explink.dao.PFoverareaDAO;
import cn.explink.dao.PFoverbigDAO;
import cn.explink.dao.PFoverweightDAO;
import cn.explink.dao.PaiFeiRuleDAO;
import cn.explink.dao.UserDAO;
import cn.explink.domain.Area;
import cn.explink.domain.Branch;
import cn.explink.domain.Customer;
import cn.explink.domain.PFarea;
import cn.explink.domain.PaiFeiRule;
import cn.explink.domain.User;
import cn.explink.enumutil.BranchTypeEnum;
import cn.explink.enumutil.PaiFeiRuleStateEnum;
import cn.explink.enumutil.PaiFeiRuleTypeEnum;
import cn.explink.service.ExplinkUserDetail;
import cn.explink.service.PaiFeiRuleService;
import cn.explink.util.Page;

/**
 * @author Administrator
 *
 */
@RequestMapping("paifeirule")
@Controller
public class PaiFeiRuleController {
	@Autowired
	BranchDAO branchDAO;
	@Autowired
	UserDAO userDAO;
	@Autowired
	CustomerDAO customerDAO;
	@Autowired
	SecurityContextHolderStrategy securityContextHolderStrategy;
	@Autowired
	PaiFeiRuleDAO paiFeiRuleDAO;
	@Autowired
	PaiFeiRuleService paiFeiRuleService;
	@Autowired
	PFbasicDAO pFbasicDAO;
	@Autowired
	PFcollectionDAO pFcollectionDAO;
	@Autowired
	PFinsertionDAO pFinsertionDAO;
	@Autowired
	PFbusinessDAO pFbusinessDAO;
	@Autowired
	PFoverareaDAO pFoverareaDAO;
	@Autowired
	PFAreaDAO pFareaDAO;
	@Autowired
	PFoverbigDAO pFoverbigDAO;
	@Autowired
	PFoverweightDAO pFoverweightDAO;
	@Autowired
	AreaDAO areaDAO;

	// private Logger logger = LoggerFactory.getLogger(this.getClass());
	User getSessionUser() {
		ExplinkUserDetail userDetail = (ExplinkUserDetail) this.securityContextHolderStrategy.getContext().getAuthentication().getPrincipal();
		return userDetail.getUser();
	}

	@RequestMapping("/list/{page}")
	public String list(@PathVariable("page") long page, Model model, @RequestParam(value = "name", required = false, defaultValue = "") String name, @RequestParam(
			value = "remark",
			required = false,
			defaultValue = "") String remark, @RequestParam(value = "type", required = false, defaultValue = "0") int type,
			@RequestParam(value = "state", required = false, defaultValue = "0") int state, @RequestParam(value = "orderby", required = false, defaultValue = "") String orderby, @RequestParam(
					value = "orderbyType",
					required = false,
					defaultValue = "") String orderbyType, @RequestParam(value = "pfruleNO", required = false, defaultValue = "") String pfruleNO, @RequestParam(
					value = "edit_ruleid",
					required = false,
					defaultValue = "0") long pfruleid) {
		List<Customer> customerList = this.customerDAO.getAllCustomers();
		String contractflag = BranchTypeEnum.JiaMeng.getValue() + "," + BranchTypeEnum.JiaMengErJi.getValue() + "," + BranchTypeEnum.JiaMengSanJi.getValue();
		List<Branch> branchs_Franchisee = this.branchDAO.getBranchsBycontractflag(contractflag);
		if ((null != pfruleNO) && (pfruleNO.length() > 0)) {
			PaiFeiRule rule = this.paiFeiRuleDAO.getPaiFeiRuleByNO(pfruleNO);
			model.addAttribute("rule", rule);
			model.addAttribute("save", 1);
		}
		if (pfruleid > 0) {
			this.paiFeiRuleService.getEditData(model, pfruleid);
		}
		List<Area> AreaList = this.areaDAO.getAllArea();
		List<PaiFeiRule> paiFeiRules = this.paiFeiRuleDAO.getPaiFeiRules(page, name, state, type, remark, orderby, orderbyType);
		int count = this.paiFeiRuleDAO.getPaiFeiRulesCounts(name, state, type, remark, orderby, orderbyType);
		Page page_obj = new Page(count, page, Page.ONE_PAGE_NUMBER);
		model.addAttribute("page", page);
		model.addAttribute("page_obj", page_obj);
		model.addAttribute("paiFeiRules", paiFeiRules);
		model.addAttribute("AreaList", AreaList);

		model.addAttribute("customerList", customerList);
		model.addAttribute("branchs_Franchisee", branchs_Franchisee);
		model.addAttribute("PaiFeiRuleStateEnum", PaiFeiRuleStateEnum.values());
		model.addAttribute("PaiFeiRuleTypeEnum", PaiFeiRuleTypeEnum.values());

		model.addAttribute("name", name);
		model.addAttribute("state", state);
		model.addAttribute("type", type);
		model.addAttribute("remark", remark);
		model.addAttribute("orderby", orderby);
		model.addAttribute("orderbyType", orderbyType);

		return "paifeiCount/rule/list";
	}

	@RequestMapping("/save")
	public @ResponseBody
	String save(Model model, @RequestParam(value = "json", required = false, defaultValue = "") String json,
			@RequestParam(value = "pfruletypeid", required = false, defaultValue = "0") int pfruletypeid, @RequestParam(value = "pfruleid", required = false, defaultValue = "0") long pfruleid) {
		JSONObject object = JSONObject.fromObject(json);
		try {
			this.paiFeiRuleService.save(object, pfruletypeid, pfruleid);
		} catch (Exception e) {
			return "{\"errorcode\":0,\"error\":\"增加异常！\"}";
		}
		return "{\"errorcode\":1,\"error\":\"增加成功！\"}";
	}

	@RequestMapping("/credata")
	public String credata(PaiFeiRule rule, Model model) {
		if (rule != null) {
			String pfruleNO = PaiFeiRuleService.createPaifeiNo("PF");
			rule.setPfruleNO(pfruleNO);
			rule.setCreuserid(this.getSessionUser().getUserid());
			this.paiFeiRuleDAO.crePaiFeiRule(rule);
			return "redirect:list/1?pfruleNO=" + pfruleNO;
		}
		return "redirect:list/1";
	}

	@RequestMapping("/delete")
	public @ResponseBody
	Map<String, Long> delete(@RequestParam(value = "ids", required = false, defaultValue = "") String ids, Model model) {
		String[] pfnos = ids.split(",");
		long counts = 0;
		for (String no : pfnos) {
			long pfruleid = Long.parseLong(no);
			List<Customer> customers = this.customerDAO.getCustomerByPFruleId(pfruleid);
			List<User> users = this.userDAO.getUserByPFruleId(pfruleid);
			List<Branch> branchs = this.branchDAO.getBanchByPFruleId(pfruleid);
			if (((customers == null) && (users == null) && (branchs == null)) || ((customers.size() == 0) && (users.size() == 0) && (branchs.size() == 0))) {
				int i = this.paiFeiRuleService.deletePaifeiRule(pfruleid);
				counts += i;
			}
		}
		Map<String, Long> map = new HashMap<String, Long>();
		map.put("counts", counts);
		return map;
	}

	@RequestMapping("/edittype")
	public @ResponseBody
	String edittype(@RequestParam(value = "json", required = false, defaultValue = "{}") String json, @RequestParam(value = "rulejson", required = false, defaultValue = "{}") String rulejson,
			@RequestParam(value = "areaid", required = false, defaultValue = "0") long areaid, @RequestParam(value = "type", required = false, defaultValue = "") String type, Model model,
			HttpServletRequest request) {

		int count = this.paiFeiRuleService.editType(json, rulejson, type, areaid, model);
		if (count > 0) {
			return "{\"errorcode\":1,\"error\":\"修改成功！\"}";
		} else {
			return "{\"errorcode\":0,\"error\":\"修改失败！\"}";
		}
	}

	@RequestMapping("/saveArea")
	public @ResponseBody
	String saveArea(@RequestParam(value = "rulejson", required = false, defaultValue = "{}") String rulejson, @RequestParam(value = "areajson", required = false, defaultValue = "{}") String areajson,
			@RequestParam(value = "tab", required = false, defaultValue = "") String tab, Model model, HttpServletRequest request) {

		long count = this.paiFeiRuleService.saveArea(areajson, rulejson, tab);
		if (count > 0) {
			return "{\"errorcode\":1,\"error\":\"修改成功！\"}";
		} else {
			return "{\"errorcode\":0,\"error\":\"修改失败！\"}";
		}
	}

	@RequestMapping("/check")
	public @ResponseBody
	Map<String, Object> check(@RequestParam(value = "name", required = false, defaultValue = "") String name, Model model) {
		Map<String, Object> map = new HashMap<String, Object>();

		PaiFeiRule rule = this.paiFeiRuleDAO.getPaiFeiRuleByName(name);
		String error = "";
		int errorcode = 0;
		if (rule != null) {
			error = "派费规则名称已存在！";
			errorcode = 1;
		}
		map.put("error", error);
		map.put("errorcode", errorcode);
		return map;
	}

	@RequestMapping("/updateArea")
	public @ResponseBody
	Map<String, Object> updateArea(@RequestParam(value = "areaid", required = false, defaultValue = "0") long areaid,
			@RequestParam(value = "areafee", required = false, defaultValue = "") String areafee, @RequestParam(value = "overbigflag", required = false, defaultValue = "-1") int overbigflag,
			Model model) {
		Map<String, Object> map = new HashMap<String, Object>();
		PFarea area = this.pFareaDAO.getPFareaById(areaid);
		int count = 0;
		if (area != null) {
			if ((areafee != null) && (areafee.length() > 0)) {
				area.setAreafee(new BigDecimal(areafee));
			}
			if (overbigflag >= -1) {
				area.setOverbigflag(overbigflag);
			}
			count=this.pFareaDAO.updatePFarea(area);
		}
		String error = "";
		int errorcode = 0;
		if (count >0) {
			error = "修改成功";
			errorcode = 1;
		}
		map.put("error", error);
		map.put("errorcode", errorcode);
		return map;
	}
}

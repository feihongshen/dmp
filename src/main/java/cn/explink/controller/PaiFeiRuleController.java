/**
 *
 */
package cn.explink.controller;

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
import cn.explink.domain.Branch;
import cn.explink.domain.Customer;
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
		List<PaiFeiRule> paiFeiRules = this.paiFeiRuleDAO.getPaiFeiRules(page, name, state, type, remark, orderby, orderbyType);
		int count = this.paiFeiRuleDAO.getPaiFeiRulesCounts(name, state, type, remark, orderby, orderbyType);
		Page page_obj = new Page(count, page, Page.ONE_PAGE_NUMBER);
		model.addAttribute("page", page);
		model.addAttribute("page_obj", page_obj);
		model.addAttribute("paiFeiRules", paiFeiRules);

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
			return "{\"errorCode\":0,\"error\":\"增加异常！\"}";
		}
		return "{\"errorCode\":1,\"error\":\"增加成功！\"}";
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
			List<Customer> customer = null;
			List<User> user = null;
			if ((customer == null) && (user == null)) {
				int i = this.paiFeiRuleDAO.deletePaiFeiRuleByPfRuleNO(no);
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
			@RequestParam(value = "type", required = false, defaultValue = "") String type, Model model, HttpServletRequest request) {

		int count = this.paiFeiRuleService.editType(json, rulejson, type, model);
		if (count > 0) {
			return "{\"errorCode\":1,\"error\":\"修改成功！\"}";
		} else {
			return "{\"errorCode\":0,\"error\":\"修改失败！\"}";
		}
	}

}

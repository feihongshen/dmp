package cn.explink.controller;

import java.util.ArrayList;
import java.util.List;

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

import cn.explink.dao.PenalizeOutDAO;
import cn.explink.dao.PenalizeTypeDAO;
import cn.explink.dao.UserDAO;
import cn.explink.domain.PenalizeOut;
import cn.explink.domain.PenalizeType;
import cn.explink.domain.User;
import cn.explink.service.ExplinkUserDetail;
import cn.explink.service.UserService;
import cn.explink.util.Page;

@Controller
@RequestMapping("/penalizeType")
public class PenalizeTypeController {

	@Autowired
	UserService userService;
	@Autowired
	UserDAO userDAO;
	@Autowired
	PenalizeTypeDAO penalizeTypeDAO;
	@Autowired
	PenalizeOutDAO penalizeOutDAO;
	@Autowired
	SecurityContextHolderStrategy securityContextHolderStrategy;

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	private User getSessionUser() {
		ExplinkUserDetail userDetail = (ExplinkUserDetail) this.securityContextHolderStrategy.getContext().getAuthentication().getPrincipal();
		return userDetail.getUser();
	}

	@RequestMapping("/list/{page}")
	public String list(@PathVariable("page") long page, Model model, @RequestParam(value = "text", required = false, defaultValue = "") String text) {
		model.addAttribute("penalizeTypeList", this.penalizeTypeDAO.getPenalizeTypeByText(page, text));
		model.addAttribute("page_obj", new Page(this.penalizeTypeDAO.getPenalizeTypeCount(text), page, Page.ONE_PAGE_NUMBER));
		model.addAttribute("page", page);
		return "/penalizeType/list";
	}

	@RequestMapping("/add")
	public String add(Model model) throws Exception {
		List<PenalizeType> penalizeTypeList = this.penalizeTypeDAO.getPenalizeTypeByType(1);
		model.addAttribute("penalizeTypeList", penalizeTypeList);
		return "/penalizeType/add";
	}

	@RequestMapping("/create")
	public @ResponseBody
	String create(PenalizeType type) throws Exception {
		if ((null != type) && (type.getText().length() <= 0)) {
			return "{\"errorCode\":1,\"error\":\"赔付类型不能为空\"}";
		}
		String text = type.getText();
		if(text.trim().length()==0){
			return "{\"errorCode\":1,\"error\":\"该赔付类型不能为空\"}";
		}
		if(text.trim().length()>20){
			return "{\"errorCode\":1,\"error\":\"该赔付类型不能超过20字\"}";
		}
		PenalizeType type1 = this.penalizeTypeDAO.getPenalizeTypeByText(text);
		if (type1 != null) {
			return "{\"errorCode\":1,\"error\":\"该赔付类型已存在\"}";
		} else {
			this.penalizeTypeDAO.crePenalizeType(type);
			this.logger.info("operatorUser={},赔付类型->create", this.getSessionUser().getUsername());
			return "{\"errorCode\":0,\"error\":\"创建成功\"}";
		}
	}

	@RequestMapping("/edit/{id}")
	public String edit(Model model, @PathVariable("id") int id) {
		List<PenalizeType> penalizeTypeList = this.penalizeTypeDAO.getPenalizeTypeByType(1);
		model.addAttribute("penalizeType", this.penalizeTypeDAO.getPenalizeTypeById(id));
		model.addAttribute("penalizeTypeList", penalizeTypeList);
		return "/penalizeType/edit";
	}

	@RequestMapping("/save")
	public @ResponseBody
	String save(PenalizeType type) throws Exception {
		if (null == type) {
			return "{\"errorCode\":1,\"error\":\"修改失败！\"}";
		}
		PenalizeType penalizeType = this.penalizeTypeDAO.getPenalizeTypeByTextAndOne(type.getText(), type.getId());
		List<PenalizeOut> penalizeOut = this.penalizeOutDAO.getPenalizeOutByPenalizeTypeId(type.getId());
		if ((penalizeOut != null) && (penalizeOut.size() > 0)) {
			return "{\"errorCode\":1,\"error\":\"该赔付类型正在使用中，不能修改！\"}";
		}
		if (penalizeType != null) {
			return "{\"errorCode\":1,\"error\":\"该赔付类型已存在\"}";
		} else {
			if(type.getText().trim().length()==0){
				return "{\"errorCode\":1,\"error\":\"该赔付类型不能为空\"}";
			}
			if(type.getText().trim().length()>20){
				return "{\"errorCode\":1,\"error\":\"该赔付类型不能超过20字\"}";
			}
			this.penalizeTypeDAO.savePenalizeType(type);
			this.logger.info("operatorUser={},赔付类型设置->save", this.getSessionUser().getUsername());
			return "{\"errorCode\":0,\"error\":\"保存成功\"}";
		}
	}

	@RequestMapping("/del/{id} ")
	public @ResponseBody
	String del(@PathVariable("id") int id, @RequestParam(value = "state") int state) throws Exception {
		List<PenalizeType> penalizeTypes = this.penalizeTypeDAO.getPenalizeTypeByParent(id);
		if (state == 1) {
			state = 0;
		} else if (state == 0) {
			state = 1;
		}
		if (state == 0) {
			if ((penalizeTypes != null) && ((penalizeTypes.size() > 0))) {
				return "{\"errorCode\":1,\"error\":\"该赔付类型正在使用中，不能修改！\"}";
			}
		}
		this.penalizeTypeDAO.delPenalizeType(id, state);
		this.logger.info("operatorUser={},赔付类型设置->del", this.getSessionUser().getUsername());
		return "{\"errorCode\":0,\"error\":\"保存成功\"}";
	}

	@RequestMapping("/delData/{id} ")
	public @ResponseBody
	String delData(@PathVariable("id") int id) throws Exception {
		List<PenalizeType> penalizeTypes = this.penalizeTypeDAO.getPenalizeTypeByParent(id);
		if ((penalizeTypes != null) && (penalizeTypes.size() > 0)) {
			return "{\"errorCode\":1,\"error\":\"该赔付类型正在使用中，不能删除！\"}";
		}
		this.logger.info("operatorUser={},系统 设置->del", this.getSessionUser().getUsername());
		List<PenalizeOut> penalizeOut = this.penalizeOutDAO.getPenalizeOutByPenalizeTypeId(id);
		if ((penalizeOut != null) && (penalizeOut.size() > 0)) {
			return "{\"errorCode\":1,\"error\":\"该赔付类型正在使用中，不能删除！\"}";
		} else {
			this.penalizeTypeDAO.delPenalizeTypeData(id);
		}
		return "{\"errorCode\":0,\"error\":\"删除成功\"}";
	}

	@RequestMapping("/findsmall")
	public @ResponseBody
	List<PenalizeType> findsmall(@RequestParam(value = "id", defaultValue = "0", required = false) int id) throws Exception {
		List<PenalizeType> penalizeTypeList = new ArrayList<PenalizeType>();
		if (id == 0) {
			penalizeTypeList = this.penalizeTypeDAO.getPenalizeTypeByType(2);
		} else {
			PenalizeType type = this.penalizeTypeDAO.getPenalizeTypeById(id);
			penalizeTypeList = this.penalizeTypeDAO.getPenalizeTypeByParent(type.getId());
		}
		return penalizeTypeList;
	}

	/*
	 * @RequestMapping("/findbig") public @ResponseBody List<Branch>
	 * findbig(@RequestParam(value = "id", defaultValue = "0", required = false)
	 * int id) throws Exception {
	 *
	 * List<Branch> branch = new ArrayList<Branch>(); if (branchname.length() ==
	 * 0) { branch = this.branchDAO.getAllBranches(); } else { branch =
	 * this.branchDAO.getBranchByBranchnameMoHu(branchname); } return branch; }
	 */
}
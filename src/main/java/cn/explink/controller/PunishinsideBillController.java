package cn.explink.controller;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.explink.dao.BranchDAO;
import cn.explink.dao.PenalizeTypeDAO;
import cn.explink.dao.PunishInsideDao;
import cn.explink.dao.PunishinsideBillDAO;
import cn.explink.dao.UserDAO;
import cn.explink.domain.Branch;
import cn.explink.domain.ExpressOpsPunishinsideBill;
import cn.explink.domain.PenalizeInside;
import cn.explink.domain.PenalizeType;
import cn.explink.domain.User;
import cn.explink.domain.VO.ExpressOpsPunishinsideBillVO;
import cn.explink.enumutil.CwbStateEnum;
import cn.explink.enumutil.PunishBillStateEnum;
import cn.explink.service.ExplinkUserDetail;
import cn.explink.service.PunishinsideBillService;
import cn.explink.util.DateTimeUtil;

@Controller
@RequestMapping("/punishinsideBill")
public class PunishinsideBillController {

	@Autowired
	PunishinsideBillDAO punishinsideBillDAO;
	@Autowired
	PunishinsideBillService punishinsideBillService;
	@Autowired
	JdbcTemplate jdbcTemplate;
	@Autowired
	SecurityContextHolderStrategy securityContextHolderStrategy;
	@Autowired
	BranchDAO branchDAO;
	@Autowired
	UserDAO userDAO;
	@Autowired
	PenalizeTypeDAO penalizeTypeDAO;
	@Autowired
	PunishInsideDao punishInsideDao;

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	private User getSessionUser() {
		ExplinkUserDetail userDetail = (ExplinkUserDetail) this.securityContextHolderStrategy
				.getContext().getAuthentication().getPrincipal();
		return userDetail.getUser();
	}

	// @RequestMapping("/CallerArchivalRepository/{page}")
	// public String CallerArchivalRepository(Model model,CsConsigneeInfo
	// cci,@PathVariable(value="page") long page){
	// model.addAttribute("page", page);
	// model.addAttribute("page_obj", new
	// Page(workorderdao.getCsConsigneeInfocount(cci.getName(),cci.getPhoneonOne(),cci.getConsigneeType()),
	// page, Page.ONE_PAGE_NUMBER));
	// model.addAttribute("ccilist",
	// workorderdao.queryAllCsConsigneeInfo(page,cci.getName(),cci.getPhoneonOne(),cci.getConsigneeType()));
	// return "workorder/CallerArchivalRepository";
	// }

	@RequestMapping("/punishinsideBillList")
	public String punishinsideBillList(Model model,
			ExpressOpsPunishinsideBillVO punishinsideBillVO) {
		// public String punishinsideBillList(Model
		// model,ExpressOpsPunishinsideBill
		// punishinsideBill,@PathVariable(value="page") long page){
		// model.addAttribute("page", page);
		// model.addAttribute("page_obj", new
		// Page(workorderdao.getCsConsigneeInfocount(cci.getName(),cci.getPhoneonOne(),cci.getConsigneeType()),
		// page, Page.ONE_PAGE_NUMBER));
		// model.addAttribute("ccilist",
		// workorderdao.queryAllCsConsigneeInfo(page,cci.getName(),cci.getPhoneonOne(),cci.getConsigneeType()));
		List<Branch> branchList = this.branchDAO.getAllEffectBranches();
		List<User> userList = this.userDAO.getAllUser();
		List<User> dutyPersonList = this.userDAO.getAllUserOrderByBranchid();
		Map<Integer, String> billStateMap = PunishBillStateEnum.getMap();
		List<ExpressOpsPunishinsideBill> list = this.punishinsideBillDAO
				.queryPunishinsideBill(punishinsideBillVO);
		List<PenalizeType> punishbigsortList = this.penalizeTypeDAO
				.getPenalizeTypeByType(1);
		List<PenalizeType> punishsmallsortList = this.penalizeTypeDAO
				.getPenalizeTypeByType(2);

		model.addAttribute("userList", userList);
		model.addAttribute("branchList", branchList);
		model.addAttribute("dutyPersonList", dutyPersonList);
		model.addAttribute("punishinsideBillList", list);
		model.addAttribute("punishinsideBillVO", punishinsideBillVO);
		model.addAttribute("billStateMap", billStateMap);
		model.addAttribute("punishbigsortList", punishbigsortList);
		model.addAttribute("punishsmallsortList", punishsmallsortList);

		return "punishinsideBill/punishinsideBillList";
	}

	@RequestMapping("/addPunishinsideBill")
	@ResponseBody
	public String addPunishinsideBill(
			ExpressOpsPunishinsideBill punishinsideBill) {

		User user = getSessionUser();
		if (user != null) {
			long userId = user.getUserid();
			punishinsideBill.setCreator(new Long(userId).intValue());
		}
		punishinsideBill.setCreateDate(DateTimeUtil.getNowDate());
		String billBatch = this.punishinsideBillService.generateBillBatch();
		punishinsideBill.setBillBatch(billBatch);
		punishinsideBill.setBillState(PunishBillStateEnum.WeiShenHe.getValue());
		this.punishinsideBillService.createPunishinsideBill(punishinsideBill);
		return "{\"errorCode\":0,\"error\":\"添加成功\"}";
	}

	@RequestMapping("/updatePunishinsideBillPage")
	public String updatePunishinsideBillPage(int id, Model model) {

		List<Branch> branchList = this.branchDAO.getAllEffectBranches();
		List<User> userList = this.userDAO.getAllUser();
		List<User> dutyPersonList = this.userDAO.getAllUserOrderByBranchid();
		Map<Integer, String> billStateMap = PunishBillStateEnum.getMap();
		Map<Integer, String> cwbStateMap = CwbStateEnum.getMap();
		List<ExpressOpsPunishinsideBill> list = this.punishinsideBillDAO
				.queryPunishinsideBill(new ExpressOpsPunishinsideBillVO());
		List<PenalizeType> punishbigsortList = this.penalizeTypeDAO
				.getPenalizeTypeByType(1);
		List<PenalizeType> punishsmallsortList = this.penalizeTypeDAO
				.getPenalizeTypeByType(2);
		ExpressOpsPunishinsideBillVO punishinsideBillVO = this.punishinsideBillService
				.getPunishinsideBillVO(id);

		model.addAttribute("userList", userList);
		model.addAttribute("branchList", branchList);
		model.addAttribute("dutyPersonList", dutyPersonList);
		model.addAttribute("punishinsideBillList", list);
		model.addAttribute("billStateMap", billStateMap);
		model.addAttribute("cwbStateMap", cwbStateMap);
		model.addAttribute("punishbigsortList", punishbigsortList);
		model.addAttribute("punishsmallsortList", punishsmallsortList);
		model.addAttribute("updatePage", 1);
		model.addAttribute("punishinsideBillVO", punishinsideBillVO);
		return "punishinsideBill/punishinsideBillList";
	}

	@RequestMapping("/penalizeInsidePage")
	public String penalizeInsidePage(int id, Model model) {

		List<Branch> branchList = this.branchDAO.getAllEffectBranches();
		List<User> userList = this.userDAO.getAllUser();
		List<User> dutyPersonList = this.userDAO.getAllUserOrderByBranchid();
		Map<Integer, String> billStateMap = PunishBillStateEnum.getMap();
		Map<Integer, String> cwbStateMap = CwbStateEnum.getMap();
		List<ExpressOpsPunishinsideBill> list = this.punishinsideBillDAO
				.queryPunishinsideBill(new ExpressOpsPunishinsideBillVO());
		List<PenalizeType> punishbigsortList = this.penalizeTypeDAO
				.getPenalizeTypeByType(1);
		List<PenalizeType> punishsmallsortList = this.penalizeTypeDAO
				.getPenalizeTypeByType(2);
		ExpressOpsPunishinsideBillVO punishinsideBillVO = this.punishinsideBillService
				.getPunishinsideBillVO(id);
		ExpressOpsPunishinsideBill punishinsideBill = this.punishinsideBillDAO
				.getPunishinsideBillListById(id);

		model.addAttribute("userList", userList);
		model.addAttribute("branchList", branchList);
		model.addAttribute("dutyPersonList", dutyPersonList);
		model.addAttribute("punishinsideBillList", list);
		model.addAttribute("punishinsideBillVO", punishinsideBillVO);
		model.addAttribute("billStateMap", billStateMap);
		model.addAttribute("cwbStateMap", cwbStateMap);
		model.addAttribute("punishbigsortList", punishbigsortList);
		model.addAttribute("punishsmallsortList", punishsmallsortList);
		model.addAttribute("updatePage", 1);
		model.addAttribute("penalizeInsidePage", 1);
		model.addAttribute("punishinsideBill", punishinsideBill);
		return "punishinsideBill/punishinsideBillList";
	}

	@RequestMapping("/penalizeInsideList")
	public String penalizeInsideList(ExpressOpsPunishinsideBillVO billVO,
			Model model) {

		List<Branch> branchList = this.branchDAO.getAllEffectBranches();
		List<User> userList = this.userDAO.getAllUser();
		List<User> dutyPersonList = this.userDAO.getAllUserOrderByBranchid();
		Map<Integer, String> billStateMap = PunishBillStateEnum.getMap();
		Map<Integer, String> cwbStateMap = CwbStateEnum.getMap();
		List<ExpressOpsPunishinsideBill> list = this.punishinsideBillDAO
				.queryPunishinsideBill(new ExpressOpsPunishinsideBillVO());
		List<PenalizeType> punishbigsortList = this.penalizeTypeDAO
				.getPenalizeTypeByType(1);
		List<PenalizeType> punishsmallsortList = this.penalizeTypeDAO
				.getPenalizeTypeByType(2);
		ExpressOpsPunishinsideBillVO punishinsideBillVO = this.punishinsideBillService
				.getPunishinsideBillVO(billVO.getId());
		ExpressOpsPunishinsideBill punishinsideBill = this.punishinsideBillDAO
				.getPunishinsideBillListById(billVO.getId());
		List<PenalizeInside> penalizeInsideList = this.punishInsideDao
				.findByCondition(1, billVO.getCwbs(), 0, 0, 0, 0,
						billVO.getPunishbigsort(), billVO.getPunishsmallsort(),
						billVO.getPunishNoCreateBeginDate(),
						billVO.getPunishNoCreateEndDate());

		model.addAttribute("userList", userList);
		model.addAttribute("branchList", branchList);
		model.addAttribute("dutyPersonList", dutyPersonList);
		model.addAttribute("punishinsideBillList", list);
		model.addAttribute("punishinsideBillVO", punishinsideBillVO);
		model.addAttribute("billStateMap", billStateMap);
		model.addAttribute("cwbStateMap", cwbStateMap);
		model.addAttribute("punishbigsortList", punishbigsortList);
		model.addAttribute("punishsmallsortList", punishsmallsortList);
		model.addAttribute("updatePage", 1);
		model.addAttribute("penalizeInsidePage", 1);
		model.addAttribute("punishinsideBill", punishinsideBill);
		model.addAttribute("penalizeInsideList", penalizeInsideList);
		return "punishinsideBill/punishinsideBillList";
	}
	
	@RequestMapping("/addPenalizeInsideList")
	public String addPenalizeInsideList(ExpressOpsPunishinsideBillVO billVO,
			Model model) {
		
		this.punishinsideBillDAO.updatePunishinsideBill(billVO.getPunishNos(), billVO.getId());
		
		List<Branch> branchList = this.branchDAO.getAllEffectBranches();
		List<User> userList = this.userDAO.getAllUser();
		List<User> dutyPersonList = this.userDAO.getAllUserOrderByBranchid();
		Map<Integer, String> billStateMap = PunishBillStateEnum.getMap();
		Map<Integer, String> cwbStateMap = CwbStateEnum.getMap();
		List<ExpressOpsPunishinsideBill> list = this.punishinsideBillDAO
				.queryPunishinsideBill(new ExpressOpsPunishinsideBillVO());
		List<PenalizeType> punishbigsortList = this.penalizeTypeDAO
				.getPenalizeTypeByType(1);
		List<PenalizeType> punishsmallsortList = this.penalizeTypeDAO
				.getPenalizeTypeByType(2);
		ExpressOpsPunishinsideBillVO punishinsideBillVO = this.punishinsideBillService
				.getPunishinsideBillVO(billVO.getId());
		
		model.addAttribute("userList", userList);
		model.addAttribute("branchList", branchList);
		model.addAttribute("dutyPersonList", dutyPersonList);
		model.addAttribute("punishinsideBillList", list);
		model.addAttribute("punishinsideBillVO", punishinsideBillVO);
		model.addAttribute("billStateMap", billStateMap);
		model.addAttribute("cwbStateMap", cwbStateMap);
		model.addAttribute("punishbigsortList", punishbigsortList);
		model.addAttribute("punishsmallsortList", punishsmallsortList);
		model.addAttribute("updatePage", 1);
		return "punishinsideBill/punishinsideBillList";
	}

	@RequestMapping("/updatePunishinsideBill")
	@ResponseBody
	public String updatePunishinsideBill(
			ExpressOpsPunishinsideBillVO punishinsideBillVO) {
		User user = getSessionUser();
		/*
		 * if (user != null) { long userId = user.getUserid();
		 * punishinsideBillVO.setModifyPerson(new Long(userId).intValue()); }
		 * punishinsideBillVO.setModifyTime(DateTimeUtil.getNowTime());
		 */
		this.punishinsideBillService
				.updatePunishinsideBillVO(punishinsideBillVO);
		return "{\"errorCode\":0,\"error\":\"修改成功\"}";
	}

	@RequestMapping("/deletePunishinsideBill")
	@ResponseBody
	public String deletePunishinsideBill(
			@RequestParam(value = "id", defaultValue = "", required = true) int id) {
		this.punishinsideBillService.deletePunishinsideBill(id);
		return "{\"errorCode\":0,\"error\":\"删除成功\"}";
	}

}

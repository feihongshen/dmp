package cn.explink.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
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

import cn.explink.dao.BranchDAO;
import cn.explink.dao.PenalizeTypeDAO;
import cn.explink.dao.PunishInsideDao;
import cn.explink.dao.PunishinsideBillDAO;
import cn.explink.dao.RoleDAO;
import cn.explink.dao.UserDAO;
import cn.explink.domain.Branch;
import cn.explink.domain.ExpressOpsPunishinsideBill;
import cn.explink.domain.PenalizeInside;
import cn.explink.domain.PenalizeType;
import cn.explink.domain.Role;
import cn.explink.domain.User;
import cn.explink.domain.VO.ExpressOpsPunishinsideBillVO;
import cn.explink.enumutil.CwbStateEnum;
import cn.explink.enumutil.FlowOrderTypeEnum;
import cn.explink.enumutil.PunishBillStateEnum;
import cn.explink.enumutil.PunishInsideStateEnum;
import cn.explink.service.ExplinkUserDetail;
import cn.explink.service.PunishinsideBillService;
import cn.explink.util.DateTimeUtil;
import cn.explink.util.Page;
import cn.explink.util.StringUtil;

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
	RoleDAO roleDAO;
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

	@RequestMapping("/punishinsideBillList/{page}")
	public String punishinsideBillList(@PathVariable("page") long page, Model model,
			ExpressOpsPunishinsideBillVO queryConditionVO) {
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
				.queryPunishinsideBill(page, queryConditionVO);
		int count = this.punishinsideBillDAO
				.queryPunishinsideBillCount(queryConditionVO);
		List<PenalizeType> punishbigsortList = this.penalizeTypeDAO
				.getPenalizeTypeByType(1);
		List<PenalizeType> punishsmallsortList = this.penalizeTypeDAO
				.getPenalizeTypeByType(2);
		Page page_obj = new Page(count, page, Page.ONE_PAGE_NUMBER);
		
		model.addAttribute("page", page);
		model.addAttribute("page_obj", page_obj);
		model.addAttribute("userList", userList);
		model.addAttribute("branchList", branchList);
		model.addAttribute("dutyPersonList", dutyPersonList);
		model.addAttribute("punishinsideBillList", list);
		model.addAttribute("queryConditionVO", queryConditionVO);
		model.addAttribute("billStateMap", billStateMap);
		model.addAttribute("punishbigsortList", punishbigsortList);
		model.addAttribute("punishsmallsortList", punishsmallsortList);
		model.addAttribute("weiShenHeState", PunishBillStateEnum.WeiShenHe.getValue());

		return "punishinsideBill/punishinsideBillList";
	}

	@RequestMapping("/addPunishinsideBill")
	public String addPunishinsideBill(
			ExpressOpsPunishinsideBill punishinsideBill, Model model) {

		User user = getSessionUser();
		if (user != null) {
			long userId = user.getUserid();
			punishinsideBill.setCreator(new Long(userId).intValue());
		}
		int id = this.punishinsideBillService.createPunishinsideBill(punishinsideBill);
		
		List<Branch> branchList = this.branchDAO.getAllEffectBranches();
		List<User> userList = this.userDAO.getAllUser();
		List<User> dutyPersonList = this.userDAO.getAllUserOrderByBranchid();
		Map<Integer, String> billStateMap = PunishBillStateEnum.getMap();
		Map<Integer, String> cwbStateMap = FlowOrderTypeEnum.getMap();
		List<ExpressOpsPunishinsideBill> list = this.punishinsideBillDAO
				.queryPunishinsideBill(1, new ExpressOpsPunishinsideBillVO());
		int count = this.punishinsideBillDAO
				.queryPunishinsideBillCount(new ExpressOpsPunishinsideBillVO());
		List<PenalizeType> punishbigsortList = this.penalizeTypeDAO
				.getPenalizeTypeByType(1);
		List<PenalizeType> punishsmallsortList = this.penalizeTypeDAO
				.getPenalizeTypeByType(2);
		ExpressOpsPunishinsideBillVO punishinsideBillVO = this.punishinsideBillService
				.getPunishinsideBillVO(id);
		int jiesuanAuthority = 0;
		int jiesuanAdvanceAuthority = 0;
		long userid = 0;
		String realname = "";
		if(user != null){
			long roleid = user.getRoleid();
			Role role = this.roleDAO.getRolesByRoleid(roleid);
			if(role != null){
				if("结算".equals(role.getRolename())){
					jiesuanAuthority = 1;
					jiesuanAdvanceAuthority = 1;
				}
			}
			userid = user.getUserid();
			realname = user.getRealname();
		}
		String nowDate = DateTimeUtil.getNowDate();
		Page page_obj = new Page(count, 1, Page.ONE_PAGE_NUMBER);
		
		model.addAttribute("page", 1);
		model.addAttribute("page_obj", page_obj);
		model.addAttribute("jiesuanAuthority", jiesuanAuthority);
		model.addAttribute("jiesuanAdvanceAuthority", jiesuanAdvanceAuthority);
		model.addAttribute("weiShenHeState", PunishBillStateEnum.WeiShenHe.getValue());
		model.addAttribute("yiShenHeState", PunishBillStateEnum.YiShenHe.getValue());
		model.addAttribute("yiHeXiaoState", PunishBillStateEnum.YiHeXiao.getValue());
		model.addAttribute("userid", userid);
		model.addAttribute("realname", realname);
		model.addAttribute("nowDate", nowDate);
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

	@RequestMapping("/updatePunishinsideBillPage")
	public String updatePunishinsideBillPage(int id, Model model) {

		List<Branch> branchList = this.branchDAO.getAllEffectBranches();
		List<User> userList = this.userDAO.getAllUser();
		List<User> dutyPersonList = this.userDAO.getAllUserOrderByBranchid();
		Map<Integer, String> billStateMap = PunishBillStateEnum.getMap();
		Map<Integer, String> cwbStateMap = FlowOrderTypeEnum.getMap();
		List<ExpressOpsPunishinsideBill> list = this.punishinsideBillDAO
				.queryPunishinsideBill(1, new ExpressOpsPunishinsideBillVO());
		int count = this.punishinsideBillDAO
				.queryPunishinsideBillCount(new ExpressOpsPunishinsideBillVO());
		List<PenalizeType> punishbigsortList = this.penalizeTypeDAO
				.getPenalizeTypeByType(1);
		List<PenalizeType> punishsmallsortList = this.penalizeTypeDAO
				.getPenalizeTypeByType(2);
		ExpressOpsPunishinsideBillVO punishinsideBillVO = this.punishinsideBillService
				.getPunishinsideBillVO(id);
		User user = getSessionUser();
		int jiesuanAuthority = 0;
		int jiesuanAdvanceAuthority = 0;
		long userid = 0;
		String realname = "";
		if(user != null){
			long roleid = user.getRoleid();
			Role role = this.roleDAO.getRolesByRoleid(roleid);
			if(role != null){
				if("结算".equals(role.getRolename())){
					jiesuanAuthority = 1;
					jiesuanAdvanceAuthority = 1;
				}
			}
			userid = user.getUserid();
			realname = user.getRealname();
		}
		String nowDate = DateTimeUtil.getNowDate();
		Page page_obj = new Page(count, 1, Page.ONE_PAGE_NUMBER);
		
		model.addAttribute("page", 1);
		model.addAttribute("page_obj", page_obj);
		model.addAttribute("jiesuanAuthority", jiesuanAuthority);
		model.addAttribute("jiesuanAdvanceAuthority", jiesuanAdvanceAuthority);
		model.addAttribute("weiShenHeState", PunishBillStateEnum.WeiShenHe.getValue());
		model.addAttribute("yiShenHeState", PunishBillStateEnum.YiShenHe.getValue());
		model.addAttribute("yiHeXiaoState", PunishBillStateEnum.YiHeXiao.getValue());
		model.addAttribute("userid", userid);
		model.addAttribute("realname", realname);
		model.addAttribute("nowDate", nowDate);
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
		Map<Integer, String> cwbStateMap = FlowOrderTypeEnum.getMap();
		List<ExpressOpsPunishinsideBill> list = this.punishinsideBillDAO
				.queryPunishinsideBill(1, new ExpressOpsPunishinsideBillVO());
		int count = this.punishinsideBillDAO
				.queryPunishinsideBillCount(new ExpressOpsPunishinsideBillVO());
		List<PenalizeType> punishbigsortList = this.penalizeTypeDAO
				.getPenalizeTypeByType(1);
		List<PenalizeType> punishsmallsortList = this.penalizeTypeDAO
				.getPenalizeTypeByType(2);
		ExpressOpsPunishinsideBillVO punishinsideBillVO = this.punishinsideBillService
				.getPunishinsideBillVO(id);
		User user = getSessionUser();
		int jiesuanAuthority = 0;
		int jiesuanAdvanceAuthority = 0;
		long userid = 0;
		String realname = "";
		if(user != null){
			long roleid = user.getRoleid();
			Role role = this.roleDAO.getRolesByRoleid(roleid);
			if(role != null){
				if("结算".equals(role.getRolename())){
					jiesuanAuthority = 1;
					jiesuanAdvanceAuthority = 1;
				}
			}
			userid = user.getUserid();
			realname = user.getRealname();
		}
		String nowDate = DateTimeUtil.getNowDate();
		Page page_obj = new Page(count, 1, Page.ONE_PAGE_NUMBER);
		
		model.addAttribute("page", 1);
		model.addAttribute("page_obj", page_obj);
		model.addAttribute("jiesuanAuthority", jiesuanAuthority);
		model.addAttribute("jiesuanAdvanceAuthority", jiesuanAdvanceAuthority);
		model.addAttribute("weiShenHeState", PunishBillStateEnum.WeiShenHe.getValue());
		model.addAttribute("yiShenHeState", PunishBillStateEnum.YiShenHe.getValue());
		model.addAttribute("yiHeXiaoState", PunishBillStateEnum.YiHeXiao.getValue());
		model.addAttribute("userid", userid);
		model.addAttribute("realname", realname);
		model.addAttribute("nowDate", nowDate);
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
		model.addAttribute("penalizeInsideQueryConditionVO", punishinsideBillVO);
		return "punishinsideBill/punishinsideBillList";
	}

	@RequestMapping("/penalizeInsideList/{page}")
	public String penalizeInsideList(@PathVariable("page") long page, ExpressOpsPunishinsideBillVO billVO,
			Model model) {

		List<Branch> branchList = this.branchDAO.getAllEffectBranches();
		List<User> userList = this.userDAO.getAllUser();
		List<User> dutyPersonList = this.userDAO.getAllUserOrderByBranchid();
		Map<Integer, String> billStateMap = PunishBillStateEnum.getMap();
		Map<Integer, String> cwbStateMap = CwbStateEnum.getMap();
		List<ExpressOpsPunishinsideBill> list = this.punishinsideBillDAO
				.queryPunishinsideBill(1, new ExpressOpsPunishinsideBillVO());
		int count = this.punishinsideBillDAO
				.queryPunishinsideBillCount(new ExpressOpsPunishinsideBillVO());
		List<PenalizeType> punishbigsortList = this.penalizeTypeDAO
				.getPenalizeTypeByType(1);
		List<PenalizeType> punishsmallsortList = this.penalizeTypeDAO
				.getPenalizeTypeByType(2);
		ExpressOpsPunishinsideBillVO punishinsideBillVO = this.punishinsideBillService
				.getPunishinsideBillVO(billVO.getId());
		String cwbs = "";
		if(StringUtils.isNotBlank(billVO.getCwbs())){
			List<String> cwbList = Arrays.asList(billVO.getCwbs().split(","));
			cwbs = StringUtil.getStringsByStringList(cwbList);
		}
		String existedPunishNos = this.punishinsideBillService.getExistedPunishNos(billVO.getId());
		if(StringUtils.isNotBlank(existedPunishNos)){
			existedPunishNos = StringUtil.getStringsByStringList(Arrays.asList(existedPunishNos.split(",")));
		}
		List<PenalizeInside> penalizeInsideList = this.punishinsideBillDAO
				.findByCondition(page, cwbs, 0, PunishInsideStateEnum.koufachengli.getValue(), 0, 0,
						billVO.getPunishbigsort(), billVO.getPunishsmallsort(),
						billVO.getPunishNoCreateBeginDate(),
						billVO.getPunishNoCreateEndDate(), existedPunishNos);
		int penalizeInsideCount = this.punishinsideBillDAO
				.findByConditionSum(cwbs, 0, PunishInsideStateEnum.koufachengli.getValue(), 0, 0,
						billVO.getPunishbigsort(), billVO.getPunishsmallsort(),
						billVO.getPunishNoCreateBeginDate(),
						billVO.getPunishNoCreateEndDate(), existedPunishNos);
		User user = getSessionUser();
		int jiesuanAuthority = 0;
		int jiesuanAdvanceAuthority = 0;
		long userid = 0;
		String realname = "";
		if(user != null){
			long roleid = user.getRoleid();
			Role role = this.roleDAO.getRolesByRoleid(roleid);
			if(role != null){
				if("结算".equals(role.getRolename())){
					jiesuanAuthority = 1;
					jiesuanAdvanceAuthority = 1;
				}
			}
			userid = user.getUserid();
			realname = user.getRealname();
		}
		String nowDate = DateTimeUtil.getNowDate();
		Page page_obj = new Page(count, 1, Page.ONE_PAGE_NUMBER);
		Page page_obj_penalize = new Page(penalizeInsideCount, page, Page.ONE_PAGE_NUMBER);
		
		model.addAttribute("page", 1);
		model.addAttribute("page_obj", page_obj);
		model.addAttribute("page_penalize", page);
		model.addAttribute("page_obj_penalize", page_obj_penalize);
		model.addAttribute("jiesuanAuthority", jiesuanAuthority);
		model.addAttribute("jiesuanAdvanceAuthority", jiesuanAdvanceAuthority);
		model.addAttribute("weiShenHeState", PunishBillStateEnum.WeiShenHe.getValue());
		model.addAttribute("yiShenHeState", PunishBillStateEnum.YiShenHe.getValue());
		model.addAttribute("yiHeXiaoState", PunishBillStateEnum.YiHeXiao.getValue());
		model.addAttribute("userid", userid);
		model.addAttribute("realname", realname);
		model.addAttribute("nowDate", nowDate);
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
		model.addAttribute("penalizeInsideList", penalizeInsideList);
		model.addAttribute("penalizeInsideQueryConditionVO", billVO);
		return "punishinsideBill/punishinsideBillList";
	}
	
	@RequestMapping("/addPenalizeInsideList")
	public String addPenalizeInsideList(ExpressOpsPunishinsideBillVO billVO,
			Model model) {
		
		this.punishinsideBillService.updatePunishinsideBill(billVO);
		
		List<Branch> branchList = this.branchDAO.getAllEffectBranches();
		List<User> userList = this.userDAO.getAllUser();
		List<User> dutyPersonList = this.userDAO.getAllUserOrderByBranchid();
		Map<Integer, String> billStateMap = PunishBillStateEnum.getMap();
		Map<Integer, String> cwbStateMap = FlowOrderTypeEnum.getMap();
		List<ExpressOpsPunishinsideBill> list = this.punishinsideBillDAO
				.queryPunishinsideBill(1, new ExpressOpsPunishinsideBillVO());
		List<PenalizeType> punishbigsortList = this.penalizeTypeDAO
				.getPenalizeTypeByType(1);
		List<PenalizeType> punishsmallsortList = this.penalizeTypeDAO
				.getPenalizeTypeByType(2);
		ExpressOpsPunishinsideBillVO punishinsideBillVO = this.punishinsideBillService
				.getPunishinsideBillVO(billVO.getId());
		User user = getSessionUser();
		int jiesuanAuthority = 0;
		int jiesuanAdvanceAuthority = 0;
		long userid = 0;
		String realname = "";
		if(user != null){
			long roleid = user.getRoleid();
			Role role = this.roleDAO.getRolesByRoleid(roleid);
			if(role != null){
				if("结算".equals(role.getRolename())){
					jiesuanAuthority = 1;
					jiesuanAdvanceAuthority = 1;
				}
			}
			userid = user.getUserid();
			realname = user.getRealname();
		}
		String nowDate = DateTimeUtil.getNowDate();

		model.addAttribute("jiesuanAuthority", jiesuanAuthority);
		model.addAttribute("jiesuanAdvanceAuthority", jiesuanAdvanceAuthority);
		model.addAttribute("weiShenHeState", PunishBillStateEnum.WeiShenHe.getValue());
		model.addAttribute("yiShenHeState", PunishBillStateEnum.YiShenHe.getValue());
		model.addAttribute("yiHeXiaoState", PunishBillStateEnum.YiHeXiao.getValue());
		model.addAttribute("userid", userid);
		model.addAttribute("realname", realname);
		model.addAttribute("nowDate", nowDate);
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
			@RequestParam(value = "ids", defaultValue = "", required = true) String ids) {
		this.punishinsideBillService.deletePunishinsideBill(ids);
		return "{\"errorCode\":0,\"error\":\"删除成功\"}";
	}

}

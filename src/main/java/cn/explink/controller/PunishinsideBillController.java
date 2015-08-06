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

	@RequestMapping("/punishinsideBillList/{page}")
	public String punishinsideBillList(@PathVariable("page") long page, Model model,
			ExpressOpsPunishinsideBillVO queryConditionVO) {
		// 获取所有责任机构
		List<Branch> branchList = this.branchDAO.getAllEffectBranches();
		// 获取所有用户
		List<User> userList = this.userDAO.getAllUser();
		// 获取所有责任人
		List<User> dutyPersonList = this.userDAO.getAllUserOrderByBranchid();
		// 获取对内扣罚对外赔付账单状态枚举
		Map<Integer, String> billStateMap = PunishBillStateEnum.getMap();
		// 获取所有对内扣罚账单列表
		List<ExpressOpsPunishinsideBill> list = this.punishinsideBillDAO
				.queryPunishinsideBill(page, queryConditionVO);
		// 获取所有对内扣罚账单列表条数
		int count = this.punishinsideBillDAO
				.queryPunishinsideBillCount(queryConditionVO);
		// 获取所有扣罚大类
		List<PenalizeType> punishbigsortList = this.penalizeTypeDAO
				.getPenalizeTypeByType(1);
		// 获取所有扣罚小类
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
		// 获取当前登录用户
		User user = getSessionUser();
		if (user != null) {
			long userId = user.getUserid();
			punishinsideBill.setCreator(new Long(userId).intValue());
		}
		int id = this.punishinsideBillService.createPunishinsideBill(punishinsideBill);
		
		// 责任机构
		List<Branch> branchList = this.branchDAO.getAllEffectBranches();
		// 用户
		List<User> userList = this.userDAO.getAllUser();
		// 责任人
		List<User> dutyPersonList = this.userDAO.getAllUserOrderByBranchid();
		// 账单状态枚举
		Map<Integer, String> billStateMap = PunishBillStateEnum.getMap();
		// 订单状态枚举
		Map<Integer, String> cwbStateMap = FlowOrderTypeEnum.getMap();
		// 账单列表
		List<ExpressOpsPunishinsideBill> list = this.punishinsideBillDAO
				.queryPunishinsideBill(1, new ExpressOpsPunishinsideBillVO());
		// 账单列表条数
		int count = this.punishinsideBillDAO
				.queryPunishinsideBillCount(new ExpressOpsPunishinsideBillVO());
		// 扣罚大类
		List<PenalizeType> punishbigsortList = this.penalizeTypeDAO
				.getPenalizeTypeByType(1);
		// 扣罚小类
		List<PenalizeType> punishsmallsortList = this.penalizeTypeDAO
				.getPenalizeTypeByType(2);
		// 当前创建的账单信息
		ExpressOpsPunishinsideBillVO punishinsideBillVO = this.punishinsideBillService
				.getPunishinsideBillVO(id);
		// 普通权限
		int jiesuanAuthority = 0;
		// 高级权限
		int jiesuanAdvanceAuthority = 0;
		// 当前登录用户id
		long userid = 0;
		// 当前登录用户真实姓名
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
		// 当前日期
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
		// 责任机构
		List<Branch> branchList = this.branchDAO.getAllEffectBranches();
		// 用户
		List<User> userList = this.userDAO.getAllUser();
		// 责任人
		List<User> dutyPersonList = this.userDAO.getAllUserOrderByBranchid();
		// 账单状态枚举
		Map<Integer, String> billStateMap = PunishBillStateEnum.getMap();
		// 订单状态枚举
		Map<Integer, String> cwbStateMap = FlowOrderTypeEnum.getMap();
		// 账单列表
		List<ExpressOpsPunishinsideBill> list = this.punishinsideBillDAO
				.queryPunishinsideBill(1, new ExpressOpsPunishinsideBillVO());
		// 账单列表条数
		int count = this.punishinsideBillDAO
				.queryPunishinsideBillCount(new ExpressOpsPunishinsideBillVO());
		// 扣罚大类
		List<PenalizeType> punishbigsortList = this.penalizeTypeDAO
				.getPenalizeTypeByType(1);
		// 扣罚小类
		List<PenalizeType> punishsmallsortList = this.penalizeTypeDAO
				.getPenalizeTypeByType(2);
		// 要修改或者查看的账单信息
		ExpressOpsPunishinsideBillVO punishinsideBillVO = this.punishinsideBillService
				.getPunishinsideBillVO(id);
		User user = getSessionUser();
		// 普通权限
		int jiesuanAuthority = 0;
		// 高级权限
		int jiesuanAdvanceAuthority = 0;
		// 当前登录用户id
		long userid = 0;
		// 当前登录用户的真实姓名
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
		// 当前日期
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
		// 责任机构
		List<Branch> branchList = this.branchDAO.getAllEffectBranches();
		// 用户
		List<User> userList = this.userDAO.getAllUser();
		// 责任人
		List<User> dutyPersonList = this.userDAO.getAllUserOrderByBranchid();
		// 账单状态枚举
		Map<Integer, String> billStateMap = PunishBillStateEnum.getMap();
		// 订单状态枚举
		Map<Integer, String> cwbStateMap = FlowOrderTypeEnum.getMap();
		// 账单列表
		List<ExpressOpsPunishinsideBill> list = this.punishinsideBillDAO
				.queryPunishinsideBill(1, new ExpressOpsPunishinsideBillVO());
		// 账单列表条数
		int count = this.punishinsideBillDAO
				.queryPunishinsideBillCount(new ExpressOpsPunishinsideBillVO());
		// 扣罚大类
		List<PenalizeType> punishbigsortList = this.penalizeTypeDAO
				.getPenalizeTypeByType(1);
		// 扣罚小类
		List<PenalizeType> punishsmallsortList = this.penalizeTypeDAO
				.getPenalizeTypeByType(2);
		// 修改或者查看的账单信息
		ExpressOpsPunishinsideBillVO punishinsideBillVO = this.punishinsideBillService
				.getPunishinsideBillVO(id);
		// 当前登录用户
		User user = getSessionUser();
		// 普通权限
		int jiesuanAuthority = 0;
		// 高级权限
		int jiesuanAdvanceAuthority = 0;
		// 当前登录用户id
		long userid = 0;
		// 当前登陆用户的真实姓名
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
		// 当前日期
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
		// 责任机构
		List<Branch> branchList = this.branchDAO.getAllEffectBranches();
		// 用户
		List<User> userList = this.userDAO.getAllUser();
		// 责任人
		List<User> dutyPersonList = this.userDAO.getAllUserOrderByBranchid();
		// 账单状态枚举
		Map<Integer, String> billStateMap = PunishBillStateEnum.getMap();
		// 订单状态枚举
		Map<Integer, String> cwbStateMap = CwbStateEnum.getMap();
		// 账单列表
		List<ExpressOpsPunishinsideBill> list = this.punishinsideBillDAO
				.queryPunishinsideBill(1, new ExpressOpsPunishinsideBillVO());
		// 账单列表条数
		int count = this.punishinsideBillDAO
				.queryPunishinsideBillCount(new ExpressOpsPunishinsideBillVO());
		// 扣罚大类
		List<PenalizeType> punishbigsortList = this.penalizeTypeDAO
				.getPenalizeTypeByType(1);
		// 扣罚小类
		List<PenalizeType> punishsmallsortList = this.penalizeTypeDAO
				.getPenalizeTypeByType(2);
		// 修改或者查看的账单信息
		ExpressOpsPunishinsideBillVO punishinsideBillVO = this.punishinsideBillService
				.getPunishinsideBillVO(billVO.getId());
		// 对内扣罚弹出层页面，查询条件:订单号
		String cwbs = "";
		if(StringUtils.isNotBlank(billVO.getCwbs())){
			List<String> cwbList = Arrays.asList(billVO.getCwbs().split(","));
			cwbs = StringUtil.getStringsByStringList(cwbList);
		}
		// 对内扣罚账单表中已经存在的扣罚单号
		String existedPunishNos = this.punishinsideBillService.getExistedPunishNos(billVO.getId());
		if(StringUtils.isNotBlank(existedPunishNos)){
			existedPunishNos = StringUtil.getStringsByStringList(Arrays.asList(existedPunishNos.split(",")));
		}
		// 对内扣罚列表
		List<PenalizeInside> penalizeInsideList = this.punishinsideBillDAO
				.findByCondition(page, cwbs, 0, PunishInsideStateEnum.koufachengli.getValue(), 0, 0,
						billVO.getPunishbigsort(), billVO.getPunishsmallsort(),
						billVO.getPunishNoCreateBeginDate(),
						billVO.getPunishNoCreateEndDate(), existedPunishNos);
		// 对内扣罚列表条数
		int penalizeInsideCount = this.punishinsideBillDAO
				.findByConditionSum(cwbs, 0, PunishInsideStateEnum.koufachengli.getValue(), 0, 0,
						billVO.getPunishbigsort(), billVO.getPunishsmallsort(),
						billVO.getPunishNoCreateBeginDate(),
						billVO.getPunishNoCreateEndDate(), existedPunishNos);
		// 当前登录用户
		User user = getSessionUser();
		// 普通权限
		int jiesuanAuthority = 0;
		// 高级权限
		int jiesuanAdvanceAuthority = 0;
		// 当前登录用户id
		long userid = 0;
		// 当前登录用户的真实姓名
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
		// 当前日期
		String nowDate = DateTimeUtil.getNowDate();
		// 对内扣罚账单列表分页信息
		Page page_obj = new Page(count, 1, Page.ONE_PAGE_NUMBER);
		// 对内扣罚列表分页信息
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
		// 修改对内扣罚账单表信息
		this.punishinsideBillService.updatePunishinsideBill(billVO);
		// 责任机构
		List<Branch> branchList = this.branchDAO.getAllEffectBranches();
		// 用户
		List<User> userList = this.userDAO.getAllUser();
		// 责任人
		List<User> dutyPersonList = this.userDAO.getAllUserOrderByBranchid();
		// 账单状态枚举
		Map<Integer, String> billStateMap = PunishBillStateEnum.getMap();
		// 订单状态枚举
		Map<Integer, String> cwbStateMap = FlowOrderTypeEnum.getMap();
		// 账单列表
		List<ExpressOpsPunishinsideBill> list = this.punishinsideBillDAO
				.queryPunishinsideBill(1, new ExpressOpsPunishinsideBillVO());
		// 账单列表条数
		int count = this.punishinsideBillDAO
				.queryPunishinsideBillCount(new ExpressOpsPunishinsideBillVO());
		// 扣罚大类
		List<PenalizeType> punishbigsortList = this.penalizeTypeDAO
				.getPenalizeTypeByType(1);
		// 扣罚小类
		List<PenalizeType> punishsmallsortList = this.penalizeTypeDAO
				.getPenalizeTypeByType(2);
		// 所修改或者查看的账单信息
		ExpressOpsPunishinsideBillVO punishinsideBillVO = this.punishinsideBillService
				.getPunishinsideBillVO(billVO.getId());
		// 当前登录用户
		User user = getSessionUser();
		// 普通权限
		int jiesuanAuthority = 0;
		// 高级权限
		int jiesuanAdvanceAuthority = 0;
		// 当前登录用户id
		long userid = 0;
		// 当前登录用户的真实姓名
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
		// 当前日期
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

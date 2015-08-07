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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.explink.dao.CustomerDAO;
import cn.explink.dao.CwbDAO;
import cn.explink.dao.ExceedSubsidyApplyDAO;
import cn.explink.dao.PenalizeTypeDAO;
import cn.explink.dao.RoleDAO;
import cn.explink.dao.UserDAO;
import cn.explink.domain.CwbOrder;
import cn.explink.domain.ExpressSetExceedSubsidyApply;
import cn.explink.domain.Role;
import cn.explink.domain.User;
import cn.explink.domain.VO.ExpressSetExceedSubsidyApplyVO;
import cn.explink.enumutil.DeliveryFeeBillCwbTypeEnum;
import cn.explink.enumutil.DeliveryFeeBillDateTypeEnum;
import cn.explink.enumutil.ExceedSubsidyApplyStateEnum;
import cn.explink.enumutil.FlowOrderTypeEnum;
import cn.explink.service.ExceedSubsidyApplyService;
import cn.explink.service.ExplinkUserDetail;
import cn.explink.service.ExportService;
import cn.explink.util.DateTimeUtil;
import cn.explink.util.Page;

@Controller
@RequestMapping("/exceedSubsidyApply")
public class ExceedSubsidyApplyController {

	@Autowired
	ExceedSubsidyApplyDAO exceedSubsidyApplyDAO;
	@Autowired
	ExceedSubsidyApplyService exceedSubsidyApplyService;
	@Autowired
	JdbcTemplate jdbcTemplate;
	@Autowired
	SecurityContextHolderStrategy securityContextHolderStrategy;
	@Autowired
	CwbDAO cwbDAO;
	@Autowired
	UserDAO userDAO;
	@Autowired
	RoleDAO roleDAO;
	@Autowired
	PenalizeTypeDAO penalizeTypeDAO;
	@Autowired
	CustomerDAO customerDAO;
	@Autowired
	ExportService exportService;

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	private User getSessionUser() {
		ExplinkUserDetail userDetail = (ExplinkUserDetail) this.securityContextHolderStrategy
				.getContext().getAuthentication().getPrincipal();
		return userDetail.getUser();
	}

	@RequestMapping("/exceedSubsidyApplyList/{page}")
	public String exceedSubsidyApplyList(@PathVariable("page") long page, Model model,
			ExpressSetExceedSubsidyApplyVO queryConditionVO) {
		// 当前登录用户
		User user = getSessionUser();
		// 配送员权限
		int deliveryAuthority = 0;
		// 站长权限
		int masterAuthority = 0;
		// 结算权限
		int advanceAuthority = 0;
		// 当前登录用户所在部门id
		long branchid = 0;
		if(user != null){
			long roleid = user.getRoleid();
			Role role = this.roleDAO.getRolesByRoleid(roleid);
			if(role != null){
				if("结算".equals(role.getRolename())){
					advanceAuthority = 1;
					queryConditionVO.setIsAdvanceAuthority(advanceAuthority);
				} else if("小件员".equals(role.getRolename())){
					deliveryAuthority = 1;
					queryConditionVO.setDeliveryPerson(new Long(user.getUserid()).intValue());
				} else if("站长".equals(role.getRolename())){
					deliveryAuthority = 1;
					masterAuthority = 1;
					branchid = user.getBranchid();
//					queryConditionVO.setDeliveryPerson(new Long(user.getUserid()).intValue());
				}
			}
		}
		// 配送员列表
		List<User> deliveryUserList = this.exceedSubsidyApplyService.getDeliveryUserList(branchid);
		// 补助申请列表
		List<ExpressSetExceedSubsidyApply> list = this.exceedSubsidyApplyDAO
				.queryExceedSubsidyApply(page, queryConditionVO);
		// 补助申请列表条数
		int count = this.exceedSubsidyApplyDAO
				.queryExceedSubsidyApplyCount(queryConditionVO);
//		List<User> userList = this.userDAO.getAllUser();
		// 补助申请状态枚举
		Map<Integer, String> applyStateMap = ExceedSubsidyApplyStateEnum.getMap();
		// 订单状态枚举
		Map<Integer, String> cwbStateMap = FlowOrderTypeEnum.getMap();
		Page page_obj = new Page(count, page, Page.ONE_PAGE_NUMBER);
		
		model.addAttribute("page", page);
		model.addAttribute("page_obj", page_obj);
		model.addAttribute("deliveryAuthority", deliveryAuthority);
		model.addAttribute("masterAuthority", masterAuthority);
		model.addAttribute("advanceAuthority", advanceAuthority);
		model.addAttribute("xinJianState", ExceedSubsidyApplyStateEnum.XinJian.getValue());
		model.addAttribute("weiShenHeState", ExceedSubsidyApplyStateEnum.WeiShenHe.getValue());
		model.addAttribute("queryConditionVO", queryConditionVO);
		model.addAttribute("exceedSubsidyApplyList", list);
		model.addAttribute("deliveryUserList", deliveryUserList);
		model.addAttribute("applyStateMap", applyStateMap);
		model.addAttribute("cwbStateMap", cwbStateMap);
		model.addAttribute("currentUser", user);
		return "exceedSubsidyApply/exceedSubsidyApplyList";
	}

	@RequestMapping("/addExceedSubsidyApply")
	public String addExceedSubsidyApply(
			ExpressSetExceedSubsidyApply exceedSubsidyApply, Model model) {
		int id = this.exceedSubsidyApplyService.createExceedSubsidyApply(exceedSubsidyApply);
		// 查询条件VO
		ExpressSetExceedSubsidyApplyVO queryConditionVO = new ExpressSetExceedSubsidyApplyVO();
		// 当前登录用户
		User user = getSessionUser();
		// 小件员权限
		int deliveryAuthority = 0;
		// 站长权限
		int masterAuthority = 0;
		// 结算权限
		int advanceAuthority = 0;
		// 当前登录用户所在部门id
		long branchid = 0;
		if(user != null){
			long roleid = user.getRoleid();
			Role role = this.roleDAO.getRolesByRoleid(roleid);
			if(role != null){
				if("小件员".equals(role.getRolename())){
					deliveryAuthority = 1;
					queryConditionVO.setDeliveryPerson(new Long(user.getUserid()).intValue());
				} else if("站长".equals(role.getRolename())){
					deliveryAuthority = 1;
					masterAuthority = 1;
					branchid = user.getBranchid();
				} else if("结算".equals(role.getRolename())){
					advanceAuthority = 1;
					queryConditionVO.setIsAdvanceAuthority(advanceAuthority);
				}
			}
		}
		// 补助申请状态枚举
		Map<Integer, String> applyStateMap = ExceedSubsidyApplyStateEnum.getMap();
		// 订单状态枚举
		Map<Integer, String> cwbStateMap = FlowOrderTypeEnum.getMap();
		// 补助申请列表
		List<ExpressSetExceedSubsidyApply> list = this.exceedSubsidyApplyDAO
				.queryExceedSubsidyApply(1, queryConditionVO);
		// 补助申请列表条数
		int count = this.exceedSubsidyApplyDAO
				.queryExceedSubsidyApplyCount(queryConditionVO);
//		List<User> userList = this.userDAO.getAllUser();
		// 配送员列表
		List<User> deliveryUserList = this.exceedSubsidyApplyService.getDeliveryUserList(branchid);
		// 创建的补助申请信息
		ExpressSetExceedSubsidyApplyVO exceedSubsidyApplyVO = this.exceedSubsidyApplyService
				.getExceedSubsidyApplyVO(id);
		if(exceedSubsidyApplyVO != null){
			if(ExceedSubsidyApplyStateEnum.XinJian.getValue() == exceedSubsidyApplyVO.getApplyState()){
				// 补助申请状态：新建，对应页面
				model.addAttribute("xinJianStatePage", 1);
			} else if(ExceedSubsidyApplyStateEnum.WeiShenHe.getValue() == exceedSubsidyApplyVO.getApplyState()){
				// 补助申请状态：未审核，对应页面
				model.addAttribute("weiShenHeStatePage", 1);
			}
		}
		Page page_obj = new Page(count, 1, Page.ONE_PAGE_NUMBER);
		
		model.addAttribute("page", 1);
		model.addAttribute("page_obj", page_obj);
		model.addAttribute("deliveryAuthority", deliveryAuthority);
		model.addAttribute("masterAuthority", masterAuthority);
		model.addAttribute("advanceAuthority", advanceAuthority);
		model.addAttribute("weiShenHeState", ExceedSubsidyApplyStateEnum.WeiShenHe.getValue());
		model.addAttribute("exceedSubsidyApplyList", list);
		model.addAttribute("applyStateMap", applyStateMap);
		model.addAttribute("cwbStateMap", cwbStateMap);
		model.addAttribute("exceedSubsidyApplyVO", exceedSubsidyApplyVO);
		model.addAttribute("deliveryUserList", deliveryUserList);
		return "exceedSubsidyApply/exceedSubsidyApplyList";
	}

	@RequestMapping("/updateExceedSubsidyApplyPage")
	public String updateExceedSubsidyApplyPage(int id, Model model) {
		// 查询条件VO
		ExpressSetExceedSubsidyApplyVO applyVO = new ExpressSetExceedSubsidyApplyVO();
		// 当前登录用户
		User user = getSessionUser();
		// 小件员权限
		int deliveryAuthority = 0;
		// 站长权限
		int masterAuthority = 0;
		// 结算权限
		int advanceAuthority = 0;
		// 当前登录用户所在的部门id
		long branchid = 0;
		if(user != null){
			long roleid = user.getRoleid();
			Role role = this.roleDAO.getRolesByRoleid(roleid);
			if(role != null){
				if("小件员".equals(role.getRolename())){
					deliveryAuthority = 1;
					applyVO.setDeliveryPerson(new Long(user.getUserid()).intValue());
				} else if("站长".equals(role.getRolename())){
					deliveryAuthority = 1;
					masterAuthority = 1;
					branchid = user.getBranchid();
				} else if("结算".equals(role.getRolename())){
					advanceAuthority = 1;
					applyVO.setIsAdvanceAuthority(advanceAuthority);
				}
			}
		}
		// 补助申请状态枚举
		Map<Integer, String> applyStateMap = ExceedSubsidyApplyStateEnum.getMap();
		// 订单状态枚举
		Map<Integer, String> cwbStateMap = FlowOrderTypeEnum.getMap();
		// 补助申请列表
		List<ExpressSetExceedSubsidyApply> list = this.exceedSubsidyApplyDAO
				.queryExceedSubsidyApply(1, applyVO);
		// 补助申请列表条数
		int count = this.exceedSubsidyApplyDAO
				.queryExceedSubsidyApplyCount(applyVO);
		// 所要编辑或者查看的补助申请信息
		ExpressSetExceedSubsidyApplyVO exceedSubsidyApplyVO = this.exceedSubsidyApplyService
				.getExceedSubsidyApplyVO(id);
//		List<User> userList = this.userDAO.getAllUser();
		// 配送员列表
		List<User> deliveryUserList = this.exceedSubsidyApplyService.getDeliveryUserList(branchid);
		
		if(exceedSubsidyApplyVO != null){
			if(ExceedSubsidyApplyStateEnum.XinJian.getValue() == exceedSubsidyApplyVO.getApplyState()){
				// 补助申请状态：新建，对应页面
				model.addAttribute("xinJianStatePage", 1);
			} else if(ExceedSubsidyApplyStateEnum.WeiShenHe.getValue() == exceedSubsidyApplyVO.getApplyState()){
				// 补助申请状态：未审核，对应页面
				model.addAttribute("weiShenHeStatePage", 1);
			} else if(ExceedSubsidyApplyStateEnum.YiShenHe.getValue() == exceedSubsidyApplyVO.getApplyState()){
				// 补助申请状态：已审核，对应页面
				model.addAttribute("yiShenHeStatePage", 1);
			} else if(ExceedSubsidyApplyStateEnum.YiJuJue.getValue() == exceedSubsidyApplyVO.getApplyState()){
				// 补助申请状态：已拒绝，对应页面
				model.addAttribute("yiJuJueStatePage", 1);
			}
		}
		Page page_obj = new Page(count, 1, Page.ONE_PAGE_NUMBER);
		
		model.addAttribute("page", 1);
		model.addAttribute("page_obj", page_obj);
		model.addAttribute("deliveryAuthority", deliveryAuthority);
		model.addAttribute("masterAuthority", masterAuthority);
		model.addAttribute("advanceAuthority", advanceAuthority);
		model.addAttribute("weiShenHeState", ExceedSubsidyApplyStateEnum.WeiShenHe.getValue());
		model.addAttribute("yiShenHeState", ExceedSubsidyApplyStateEnum.YiShenHe.getValue());
		model.addAttribute("yiJuJueState", ExceedSubsidyApplyStateEnum.YiJuJue.getValue());
		model.addAttribute("exceedSubsidyApplyList", list);
		model.addAttribute("applyStateMap", applyStateMap);
		model.addAttribute("cwbStateMap", cwbStateMap);
		model.addAttribute("exceedSubsidyApplyVO", exceedSubsidyApplyVO);
		model.addAttribute("deliveryUserList", deliveryUserList);
		model.addAttribute("currentUser", user);
		model.addAttribute("currentTime", DateTimeUtil.getNowTime());
		return "exceedSubsidyApply/exceedSubsidyApplyList";
	}

	@RequestMapping("/updateExceedSubsidyApply")
	@ResponseBody
	public String updateExceedSubsidyApply(
			ExpressSetExceedSubsidyApplyVO exceedSubsidyApplyVO) {
		exceedSubsidyApplyVO.setUpdateTime(DateTimeUtil.getNowTime());
		this.exceedSubsidyApplyService.updateExceedSubsidyApply(exceedSubsidyApplyVO);
		return "{\"errorCode\":0}";
	}

	@RequestMapping("/deleteExceedSubsidyApply")
	@ResponseBody
	public String deleteExceedSubsidyApply(
			@RequestParam(value = "ids", defaultValue = "", required = true) String ids) {
		this.exceedSubsidyApplyService.deleteExceedSubsidyApply(ids);
		return "{\"errorCode\":0,\"error\":\"删除成功\"}";
	}
	
	@RequestMapping("/validateCwbOrder")
	@ResponseBody
	public String validateCwbOrder(
			@RequestParam(value = "cwb", defaultValue = "", required = true) String cwb, @RequestParam(value = "id", defaultValue = "", required = false) Integer id) {
		// 判断订单号是否存在，若存在，则返回订单状态和收货地址
		String rtnStr = "{\"isExist\":0}";
		int count = this.exceedSubsidyApplyDAO.getExceedSubsidyApplyByCwbOrderCount(id, cwb);
		if(count > 0){
			// 订单号重复
			rtnStr = "{\"isExist\":2}";
		} else {
			CwbOrder cwbOrder = this.cwbDAO.getCwbByCwb(cwb);
			if(cwbOrder != null){
				rtnStr = "{\"isExist\":1,\"cwbOrderState\":" + cwbOrder.getFlowordertype() + ",\"receiveAddress\":\"" + cwbOrder.getConsigneeaddress() +"\"}";
			}
		}
		return rtnStr;
	}
}

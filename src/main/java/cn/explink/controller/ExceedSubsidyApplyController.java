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

	@RequestMapping("/exceedSubsidyApplyList")
	public String exceedSubsidyApplyList(Model model,
			ExpressSetExceedSubsidyApplyVO queryConditionVO) {
		
		User user = getSessionUser();
		int deliveryAuthority = 0;
		int advanceAuthority = 0;
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
				}
			}
		}
		List<ExpressSetExceedSubsidyApply> list = this.exceedSubsidyApplyDAO
				.queryExceedSubsidyApply(queryConditionVO);
		List<User> userList = this.userDAO.getAllUser();
		Map<Integer, String> applyStateMap = ExceedSubsidyApplyStateEnum.getMap();
		// 订单状态枚举
		Map<Integer, String> cwbStateMap = FlowOrderTypeEnum.getMap();
		
		model.addAttribute("deliveryAuthority", deliveryAuthority);
		model.addAttribute("advanceAuthority", advanceAuthority);
		model.addAttribute("xinJianState", ExceedSubsidyApplyStateEnum.XinJian.getValue());
		model.addAttribute("weiShenHeState", ExceedSubsidyApplyStateEnum.WeiShenHe.getValue());
		model.addAttribute("queryConditionVO", queryConditionVO);
		model.addAttribute("exceedSubsidyApplyList", list);
		model.addAttribute("userList", userList);
		model.addAttribute("applyStateMap", applyStateMap);
		model.addAttribute("cwbStateMap", cwbStateMap);
		model.addAttribute("currentUser", user);
		return "exceedSubsidyApply/exceedSubsidyApplyList";
	}

	@RequestMapping("/addExceedSubsidyApply")
	public String addExceedSubsidyApply(
			ExpressSetExceedSubsidyApply exceedSubsidyApply, Model model) {
		
		int id = this.exceedSubsidyApplyService.createExceedSubsidyApply(exceedSubsidyApply);
		
		ExpressSetExceedSubsidyApplyVO applyVO = new ExpressSetExceedSubsidyApplyVO();
		User user = getSessionUser();
		int deliveryAuthority = 0;
		int advanceAuthority = 0;
		if(user != null){
			long roleid = user.getRoleid();
			Role role = this.roleDAO.getRolesByRoleid(roleid);
			if(role != null){
				if("小件员".equals(role.getRolename())){
					deliveryAuthority = 1;
					applyVO.setDeliveryPerson(new Long(user.getUserid()).intValue());
				} else if("结算".equals(role.getRolename())){
					advanceAuthority = 1;
					applyVO.setIsAdvanceAuthority(advanceAuthority);
				}
			}
		}
		
		Map<Integer, String> applyStateMap = ExceedSubsidyApplyStateEnum.getMap();
		Map<Integer, String> cwbStateMap = FlowOrderTypeEnum.getMap();
		List<ExpressSetExceedSubsidyApply> list = this.exceedSubsidyApplyDAO
				.queryExceedSubsidyApply(applyVO);
		List<User> userList = this.userDAO.getAllUser();
		ExpressSetExceedSubsidyApplyVO exceedSubsidyApplyVO = this.exceedSubsidyApplyService
				.getExceedSubsidyApplyVO(id);
		
		if(exceedSubsidyApplyVO != null){
			if(ExceedSubsidyApplyStateEnum.XinJian.getValue() == exceedSubsidyApplyVO.getApplyState()){
				model.addAttribute("xinJianStatePage", 1);
			} else if(ExceedSubsidyApplyStateEnum.WeiShenHe.getValue() == exceedSubsidyApplyVO.getApplyState()){
				model.addAttribute("weiShenHeStatePage", 1);
			}
		}
		model.addAttribute("deliveryAuthority", deliveryAuthority);
		model.addAttribute("advanceAuthority", advanceAuthority);
		model.addAttribute("weiShenHeState", ExceedSubsidyApplyStateEnum.WeiShenHe.getValue());
		model.addAttribute("exceedSubsidyApplyList", list);
		model.addAttribute("applyStateMap", applyStateMap);
		model.addAttribute("cwbStateMap", cwbStateMap);
		model.addAttribute("exceedSubsidyApplyVO", exceedSubsidyApplyVO);
		model.addAttribute("userList", userList);
		return "exceedSubsidyApply/exceedSubsidyApplyList";
	}

	@RequestMapping("/updateExceedSubsidyApplyPage")
	public String updateExceedSubsidyApplyPage(int id, Model model) {

		ExpressSetExceedSubsidyApplyVO applyVO = new ExpressSetExceedSubsidyApplyVO();
		User user = getSessionUser();
		int deliveryAuthority = 0;
		int advanceAuthority = 0;
		if(user != null){
			long roleid = user.getRoleid();
			Role role = this.roleDAO.getRolesByRoleid(roleid);
			if(role != null){
				if("小件员".equals(role.getRolename())){
					deliveryAuthority = 1;
					applyVO.setDeliveryPerson(new Long(user.getUserid()).intValue());
				} else if("结算".equals(role.getRolename())){
					advanceAuthority = 1;
					applyVO.setIsAdvanceAuthority(advanceAuthority);
				}
			}
		}
		Map<Integer, String> applyStateMap = ExceedSubsidyApplyStateEnum.getMap();
		Map<Integer, String> cwbStateMap = FlowOrderTypeEnum.getMap();
		List<ExpressSetExceedSubsidyApply> list = this.exceedSubsidyApplyDAO
				.queryExceedSubsidyApply(applyVO);
		ExpressSetExceedSubsidyApplyVO exceedSubsidyApplyVO = this.exceedSubsidyApplyService
				.getExceedSubsidyApplyVO(id);
		List<User> userList = this.userDAO.getAllUser();
		
		if(exceedSubsidyApplyVO != null){
			if(ExceedSubsidyApplyStateEnum.XinJian.getValue() == exceedSubsidyApplyVO.getApplyState()){
				model.addAttribute("xinJianStatePage", 1);
			} else if(ExceedSubsidyApplyStateEnum.WeiShenHe.getValue() == exceedSubsidyApplyVO.getApplyState()){
				model.addAttribute("weiShenHeStatePage", 1);
			} else if(ExceedSubsidyApplyStateEnum.YiShenHe.getValue() == exceedSubsidyApplyVO.getApplyState()){
				model.addAttribute("yiShenHeStatePage", 1);
			} else if(ExceedSubsidyApplyStateEnum.YiJuJue.getValue() == exceedSubsidyApplyVO.getApplyState()){
				model.addAttribute("yiJuJueStatePage", 1);
			}
		}
		
		model.addAttribute("deliveryAuthority", deliveryAuthority);
		model.addAttribute("advanceAuthority", advanceAuthority);
		model.addAttribute("weiShenHeState", ExceedSubsidyApplyStateEnum.WeiShenHe.getValue());
		model.addAttribute("yiShenHeState", ExceedSubsidyApplyStateEnum.YiShenHe.getValue());
		model.addAttribute("yiJuJueState", ExceedSubsidyApplyStateEnum.YiJuJue.getValue());
		model.addAttribute("exceedSubsidyApplyList", list);
		model.addAttribute("applyStateMap", applyStateMap);
		model.addAttribute("cwbStateMap", cwbStateMap);
		model.addAttribute("exceedSubsidyApplyVO", exceedSubsidyApplyVO);
		model.addAttribute("userList", userList);
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
			@RequestParam(value = "cwb", defaultValue = "", required = true) String cwb) {
		String rtnStr = "{\"isExist\":0}";
		CwbOrder cwbOrder = this.cwbDAO.getCwbByCwb(cwb);
		if(cwbOrder != null){
			rtnStr = "{\"isExist\":1,\"cwbOrderState\":" + cwbOrder.getFlowordertype() + ",\"receiveAddress\":\"" + cwbOrder.getConsigneeaddress() +"\"}";
		}
		return rtnStr;
	}
}

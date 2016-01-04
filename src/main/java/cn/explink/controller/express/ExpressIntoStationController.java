package cn.explink.controller.express;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.explink.dao.RoleDAO;
import cn.explink.dao.UserDAO;
import cn.explink.domain.Role;
import cn.explink.domain.User;
import cn.explink.domain.VO.express.ExpressIntoStationCountVO;
import cn.explink.domain.VO.express.ExpressIntoStationVO;
import cn.explink.domain.VO.express.ExpressOpeAjaxResult;
import cn.explink.domain.VO.express.ExpressParamsVO;
import cn.explink.service.express.ExpressIntoStationService;
import cn.explink.util.Page;
import cn.explink.util.Tools;

/**
 * 揽件入站
 * @author jiangyu 2015年7月30日
 *
 */
@RequestMapping("/expressIntoStation")
@Controller
public class ExpressIntoStationController extends ExpressCommonController {

	@Autowired
	UserDAO userDAO;
	@Autowired
	RoleDAO roleDAO;
	@Autowired
	HttpSession session;
	@Autowired
	ExpressIntoStationService expressIntoStationService;

	@SuppressWarnings("unchecked")
	@RequestMapping("/expressQueryList/{page}")
	public String expressQueryList(@PathVariable("page") long page, Model model, ExpressParamsVO params, HttpServletRequest request) {
		//获取当前毫秒
		long startTime = System.currentTimeMillis();
		//completed
		
		List<User> uList = this.getRoleUsers();
		//获取当前用户，查询用户的机构
		Long branchId = this.getSessionUser().getBranchid();
		//封装params的branchid站点
		params.setBranchId(branchId);
		if (params.getDeliveryId() == null) {
			if (uList.size() != 0) {
				params.setDeliveryId(uList.get(0).getUserid());
			}
		}
		//综合查询记录数
		Map<String, Object> resultMap = this.expressIntoStationService.queryExpressListByPage(page, params, uList);

		//获取满足条件的记录
		List<ExpressIntoStationVO> list = (List<ExpressIntoStationVO>) resultMap.get("orders");
		//获取查询记录总数
		Long recordCount = (Long) resultMap.get("recordCount");
		//分页的控制显示
		Page page_obj = new Page(recordCount.intValue(), page, Page.ONE_PAGE_NUMBER);
		model.addAttribute("userList", uList);

		model.addAttribute("processState", params.getProcessState());
		model.addAttribute("payType", params.getPayType());
		model.addAttribute("deliveryManId", params.getDeliveryId() + "");
		this.session.setAttribute("pageDeliver", params.getDeliveryId());
		model.addAttribute("page", page);
		model.addAttribute("page_obj", page_obj);
		for (int i = 0; i < list.size(); i++) {
			if (params.getProcessState() == 1) {
				list.get(i).setProcessState("未处理");
			} else {
				list.get(i).setProcessState("已处理");
			}
		}
		model.addAttribute("expresseList", list);
		logger.info("进入揽收交接页面的时间共：" + (System.currentTimeMillis() - startTime) + "毫秒");
		return "express/intoStation/expressIntoStation";
	}

	/**
	 * 执行确认入站操作
	 * @param model
	 * @param params
	 * @return
	 */
	@RequestMapping("/executeIntoStation")
	@ResponseBody
	public ExpressOpeAjaxResult expressIntoStationExecute(Model model, ExpressParamsVO params) {
		long startTime = System.currentTimeMillis();
		ExpressOpeAjaxResult result = new ExpressOpeAjaxResult();
		User user = this.getSessionUser();
		if (!Tools.isEmpty(params.getIds())) {
			result = this.expressIntoStationService.executeIntoStationOpe(user, params);
		}
		this.logger.info("揽收交接页面、执行确认交货操作的时间共：" + (System.currentTimeMillis() - startTime) + "毫秒");
		return result;
	}

	/**
	 * 查询记录并汇总
	 * @param model
	 * @param params
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/querySelectIdsTotalRecord")
	@ResponseBody
	public ExpressOpeAjaxResult querySelectIdsTotalRecord(Model model, ExpressParamsVO params) {
		ExpressOpeAjaxResult result = new ExpressOpeAjaxResult();
		Map<String, Object> backResult = new HashMap<String, Object>();
		if (!Tools.isEmpty(params.getIds())) {
			//查询记录分组
			Map<String, Object> resultMap = this.expressIntoStationService.getSummaryRecord(params.getIds());
			/*
			Long totalCount =0L;
			BigDecimal totalFee = BigDecimal.ZERO;
			ExpressIntoStationCountVO nowPay = resultMap.get("nowPayMap");
			ExpressIntoStationCountVO monthPay = resultMap.get("monthPayMap");
			ExpressIntoStationCountVO arrivePay = resultMap.get("arrivePayMap");
			totalCount = nowPay.getCount()+monthPay.getCount()+arrivePay.getCount();
			totalFee = nowPay.getSumFee().add(monthPay.getSumFee()).add(arrivePay.getSumFee());

			backResult.put("totalOrderCount", totalCount);
			backResult.put("totalTransFee", totalFee);
			backResult.put("nowPayOrderCount", nowPay.getCount());
			backResult.put("nowPayTransFee", nowPay.getSumFee());
			backResult.put("arrivePayOrderCount", arrivePay.getCount());
			backResult.put("arrivePayTransFee", arrivePay.getSumFee());
			backResult.put("monthPayOrderCount", monthPay.getCount());
			backResult.put("monthPayTransFee", monthPay.getSumFee());*/

			ExpressIntoStationCountVO totalPay = (ExpressIntoStationCountVO) resultMap.get("totalRecord");
			String deliverManStr = (String) resultMap.get("deliverNameStr");

			backResult.put("totalOrderCount", totalPay.getCount());
			backResult.put("totalTransFee", totalPay.getSumFee());
			backResult.put("deliverNameStr", deliverManStr);
			result.setAttributes(backResult);
		}
		return result;
	}

	/**
	 * 获取用户
	 * @return
	 */
	public List<User> getRoleUsers() {
		String roleids = "2,4";
		List<Role> roles = this.roleDAO.getRolesByIsdelivery();
		if ((roles != null) && (roles.size() > 0)) {
			for (Role r : roles) {
				roleids += "," + r.getRoleid();
			}
		}
		List<User> uList = this.userDAO.getUserByRolesAndBranchid(roleids, this.getSessionUser().getBranchid());
		return uList;
	}

}

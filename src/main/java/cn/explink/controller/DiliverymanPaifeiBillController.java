/**
 *
 */
package cn.explink.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.explink.dao.BranchDAO;
import cn.explink.dao.DiliverymanPaifeiBillDAO;
import cn.explink.dao.RoleDAO;
import cn.explink.dao.UserDAO;
import cn.explink.domain.DiliverymanPaifeiBill;
import cn.explink.domain.Role;
import cn.explink.domain.User;
import cn.explink.enumutil.BranchEnum;
import cn.explink.enumutil.DateTypeEnum;
import cn.explink.enumutil.FlowOrderTypeEnum;
import cn.explink.enumutil.OrderTypeEnum;
import cn.explink.enumutil.PaytypeEnum;
import cn.explink.enumutil.PunishBillStateEnum;
import cn.explink.service.DiliverymanPaifeiBillService;
import cn.explink.service.ExplinkUserDetail;
import cn.explink.util.Page;

/**
 * @author wangqiang
 */
@RequestMapping("/diliverymanpaifeibill")
@Controller
public class DiliverymanPaifeiBillController {
	@Autowired
	private DiliverymanPaifeiBillService diliverymanPaifeiBillService;
	@Autowired
	private BranchDAO branchDAO;
	@Autowired
	private UserDAO userDAO;
	@Autowired
	SecurityContextHolderStrategy securityContextHolderStrategy;
	@Autowired
	private DiliverymanPaifeiBillDAO diliverymanPaifeiBillDAO;
	@Autowired
	RoleDAO roleDAO;

	private User getSessionUser() {
		ExplinkUserDetail userDetail = (ExplinkUserDetail) this.securityContextHolderStrategy.getContext().getAuthentication().getPrincipal();
		return userDetail.getUser();
	}

	/**
	 * 查询出所有订单
	 *
	 * @param model
	 * @param bill
	 * @param page
	 * @return
	 */
	@RequestMapping("/diliverymanpaifeibillPage/{page}")
	public String queryDiliverymanPaifeiBill(Model model, DiliverymanPaifeiBill bill, @PathVariable("page") long page) {
		List<DiliverymanPaifeiBill> DiliverymanPaifeiBillList = this.diliverymanPaifeiBillService.queryDiliverymanPaifeiBill(bill, page);
		int count = this.diliverymanPaifeiBillService.queryDiliverymanPaifeiBillCount(bill);
		Page page_obj = new Page(count, page, Page.ONE_PAGE_NUMBER);
		model.addAttribute("page", page);
		model.addAttribute("page_obj", page_obj);

		Map<Long, String> orgMap = this.getOrgMap();
		model.addAttribute("orgMap", orgMap);

		model.addAttribute("DiliverymanPaifeiBillList", DiliverymanPaifeiBillList);
		model.addAttribute("PunishBillStateEnum", PunishBillStateEnum.values());
		model.addAttribute("OrderTypeEnum", OrderTypeEnum.values());
		model.addAttribute("DateTypeEnum", DateTypeEnum.values());
		model.addAttribute("diliverymanName", this.userDAO.getAllUserByuserDeleteFlag());
		model.addAttribute("billbatch", bill.getBillbatch());
		model.addAttribute("billstate", bill.getBillstate());
		model.addAttribute("billCreationStartDate", bill.getBillCreationStartDate());
		model.addAttribute("billCreationEndDate", bill.getBillCreationEndDate());
		model.addAttribute("billVerificationStrartDate", bill.getBillVerificationStrartDate());
		model.addAttribute("billVerificationEndDate", bill.getBillVerificationEndDate());
		model.addAttribute("theirsite", bill.getTheirsite());
		model.addAttribute("ordertype", bill.getOrdertype());
		model.addAttribute("diliveryman", bill.getDiliveryman());
		model.addAttribute("sortField", bill.getSortField());
		model.addAttribute("orderingMethod", bill.getOrderingMethod());
		return "diliverymanpaifeibill/diliverymanpaifeibillPage";
	}

	/**
	 * 新增配送员账单
	 */
	@RequestMapping("/addDilivermanBill")
	@ResponseBody
	public int addDilivermanBill(@RequestParam(value = "site", required = false) Integer site, @RequestParam(value = "orderType", required = false) Integer orderType,
			@RequestParam(value = "diliveryman", required = false) String[] diliveryman, @RequestParam(value = "dateType", required = false) Integer dateType,
			@RequestParam(value = "startDate", required = false) String startDate, @RequestParam(value = "endDate", required = false) String endDate,
			@RequestParam(value = "explain", required = false) String explain) {
		int count = this.diliverymanPaifeiBillService.createBill(site, orderType, diliveryman, dateType, startDate, endDate, explain);
		return count;
	}

	/**
	 * 查询指定账单
	 */
	@RequestMapping("/queryById/{page}")
	public String queryById(Model model, @PathVariable("page") long page, @RequestParam(value = "id", required = false) Integer id) {
		DiliverymanPaifeiBill bill = this.diliverymanPaifeiBillService.queryById(id, page);
		int ordercount = this.diliverymanPaifeiBillService.queryByIdCount(id);
		model.addAttribute("bill", bill);
		Page page_ob = new Page(ordercount, page, Page.ONE_PAGE_NUMBER);
		model.addAttribute("pag", page);
		model.addAttribute("page_ob", page_ob);

		long pag = 1;
		// 重载页面所需要的数据
		List<DiliverymanPaifeiBill> DiliverymanPaifeiBillList = this.diliverymanPaifeiBillService.queryDiliverymanPaifeiBill(new DiliverymanPaifeiBill(), pag);
		int count = this.diliverymanPaifeiBillService.queryDiliverymanPaifeiBillCount(new DiliverymanPaifeiBill());
		Page page_obj = new Page(count, pag, Page.ONE_PAGE_NUMBER);
		model.addAttribute("page", pag);
		model.addAttribute("page_obj", page_obj);
		// 查询条件默认显示的条件
		// SimpleDateFormat df = new SimpleDateFormat("yyyy-MM");
		// Date myDate = new Date();
		// // 当月的第一天
		// String date = df.format(myDate);
		// String creationStartDate = date + "-01";
		// // 当前时间
		// String creationEndDate = DateTimeUtil.getNowDate();
		int jiesuanAuthority = 0;
		int jiesuanAdvanceAuthority = 0;
		User user = this.getSessionUser();
		if (user != null) {
			long roleid = user.getRoleid();
			Role role = this.roleDAO.getRolesByRoleid(roleid);
			if (role != null) {
				if ("结算".equals(role.getRolename())) {
					jiesuanAuthority = 1;
					jiesuanAdvanceAuthority = 1;
				}
			}
		}

		model.addAttribute("jiesuanAuthority", jiesuanAuthority);
		model.addAttribute("jiesuanAdvanceAuthority", jiesuanAdvanceAuthority);
		model.addAttribute("weiShenHeState", PunishBillStateEnum.WeiShenHe.getValue());
		model.addAttribute("yiShenHeState", PunishBillStateEnum.YiShenHe.getValue());
		model.addAttribute("yiHeXiaoState", PunishBillStateEnum.YiHeXiao.getValue());
		Map<Long, String> orgMap = this.getOrgMap();
		model.addAttribute("orgMap", orgMap);
		model.addAttribute("DiliverymanPaifeiBillList", DiliverymanPaifeiBillList);
		model.addAttribute("PunishBillStateEnum", PunishBillStateEnum.values());
		model.addAttribute("OrderTypeEnum", OrderTypeEnum.values());
		model.addAttribute("DateTypeEnum", DateTypeEnum.values());
		model.addAttribute("FlowOrderTypeEnum", FlowOrderTypeEnum.values());
		model.addAttribute("PaytypeEnum", PaytypeEnum.values());

		model.addAttribute("diliverymanName", this.userDAO.getAllUserByuserDeleteFlag());
		model.addAttribute("updatePage", 1);

		return "diliverymanpaifeibill/diliverymanpaifeibillPage";
	}

	private Map<Long, String> getOrgMap() {
		return this.branchDAO.getBranchNameMap(this.getSessionUser().getUserid(), BranchEnum.ZhanDian.getValue());
	}

	/**
	 * 修改账单信息
	 */
	@RequestMapping("/updateDilivermanBill")
	@ResponseBody
	public String updateDilivermanBill(DiliverymanPaifeiBill bill) {
		this.diliverymanPaifeiBillService.updateDilivermanBill(bill);
		return "{\"errorCode\":0,\"error\":\"修改成功\"}";
	}

	/**
	 * 删除账单
	 */
	@RequestMapping("/deletePaiFeiBill")
	@ResponseBody
	public int deletePaiFeiBill(@RequestParam(value = "id", required = false) String id) {
		int count = this.diliverymanPaifeiBillService.deletePaiFeiBill(id);
		return count;
	}

	/**
	 * 移除指定订单
	 */
	@RequestMapping("/deleteorder")
	@ResponseBody
	public String deleteorder(@RequestParam(value = "ordernumber", required = false) String ordernumber, @RequestParam(value = "id", required = false) Integer id) {
		this.diliverymanPaifeiBillService.deleteorder(ordernumber, id);
		return "{\"errorCode\":0,\"error\":\"移除成功\"}";
	}
	//修改账单状态
	@RequestMapping("/updateBillState")
	@ResponseBody
	public String updateBillState(@RequestParam(value = "id", required = false)Integer id,@RequestParam(value = "state", required = false)Integer state){
		this.diliverymanPaifeiBillDAO.updateBillState(id,state);
		return "{\"errorCode\":0,\"error\":\"修改状态成功\"}";
	}
	@RequestMapping("/getstationdeliver")
	@ResponseBody
	public Map<Long, String> getStationDeliver(HttpServletRequest request) {
		String strStationId = request.getParameter("stationId");
		long stationId = Long.valueOf(strStationId);

		return this.userDAO.getDeliverNameMapByBranchid(stationId);
	}
	
}

/**
 *
 */
package cn.explink.controller;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

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
import cn.explink.dao.PenalizeOutBillDAO;
import cn.explink.dao.PenalizeTypeDAO;
import cn.explink.dao.RoleDAO;
import cn.explink.dao.UserDAO;
import cn.explink.domain.Branch;
import cn.explink.domain.Customer;
import cn.explink.domain.PenalizeOut;
import cn.explink.domain.PenalizeType;
import cn.explink.domain.Role;
import cn.explink.domain.User;
import cn.explink.domain.penalizeOutBill;
import cn.explink.enumutil.CwbStateEnum;
import cn.explink.enumutil.PunishBillStateEnum;
import cn.explink.service.ExplinkUserDetail;
import cn.explink.service.PenalizeOutBillService;
import cn.explink.util.Page;

/**
 * @author wangqiang
 */
@Controller
@RequestMapping("/penalizeOutBill")
public class PenalizeOutBillController {
	@Autowired
	SecurityContextHolderStrategy securityContextHolderStrategy;
	@Autowired
	private PenalizeOutBillService penalizeOutBillService;
	@Autowired
	private CustomerDAO customerDao;
	@Autowired
	private PenalizeTypeDAO penalizeTypeDAO;
	@Autowired
	UserDAO userDAO;
	@Autowired
	BranchDAO branchDAO;
	@Autowired
	RoleDAO roleDAO;
	@Autowired
	private PenalizeOutBillDAO PenalizeOutBilldao;

	private User getSessionUser() {
		ExplinkUserDetail userDetail = (ExplinkUserDetail) this.securityContextHolderStrategy.getContext().getAuthentication().getPrincipal();
		return userDetail.getUser();
	}

	/**
	 * 查询账单数据
	 *
	 * @param model
	 * @param bill
	 * @param billCreationStartDate
	 * @param billCreationEndDate
	 * @param billVerificationStrartDate
	 * @param billVerificationEndDate
	 * @param sort
	 * @param method
	 * @return
	 */
	@RequestMapping("/penalizeOutBillPage/{page}")
	public String queryAllBenalizeOutBill(Model model, penalizeOutBill bill, @PathVariable("page") long page,
			@RequestParam(value = "billCreationStartDate", required = false) String billCreationStartDate, @RequestParam(value = "billCreationEndDate", required = false) String billCreationEndDate,
			@RequestParam(value = "billVerificationStrartDate", required = false) String billVerificationStrartDate,
			@RequestParam(value = "billVerificationEndDate", required = false) String billVerificationEndDate, @RequestParam(value = "sort", required = false) String sort,
			@RequestParam(value = "method", required = false) String method, @RequestParam(value = "query", required = false) Integer query) {
		List<penalizeOutBill> list = this.penalizeOutBillService.queryAll(bill, billCreationStartDate, billCreationEndDate, billVerificationStrartDate, billVerificationEndDate, sort, method, page);
		model.addAttribute("billList", list);
		int count = this.PenalizeOutBilldao.queryAllCount(bill, billCreationStartDate, billCreationEndDate, billVerificationStrartDate, billVerificationEndDate, sort, method, page);
		Page page_ob = new Page(count, page, Page.ONE_PAGE_NUMBER);
		model.addAttribute("page", page);
		model.addAttribute("page_ob", page_ob);
		List<Branch> branchList = this.branchDAO.getAllEffectBranches();
		List<Customer> customerList = this.customerDao.getAllCustomerss();
		List<PenalizeType> penalizebigList = this.penalizeTypeDAO.getPenalizeTypeByType(1);
		List<PenalizeType> penalizesmallList = this.penalizeTypeDAO.getPenalizeTypeByType(2);
		List<User> userList = this.userDAO.getAllUser();

		model.addAttribute("userList", userList);
		model.addAttribute("branchList", branchList);
		model.addAttribute("penalizebigList", penalizebigList);
		model.addAttribute("penalizesmallList", penalizesmallList);
		model.addAttribute("customerList", customerList);
		model.addAttribute("PunishBillStateEnum", PunishBillStateEnum.values());
		// 查询条件回显
		model.addAttribute("billbatches", bill.getBillbatches());
		model.addAttribute("billstate", bill.getBillstate());
		model.addAttribute("billCreationStartDate", billCreationStartDate);
		model.addAttribute("billCreationEndDate", billCreationEndDate);
		model.addAttribute("billVerificationStrartDate", billVerificationStrartDate);
		model.addAttribute("billVerificationEndDate", billVerificationEndDate);
		model.addAttribute("customerid", bill.getCustomerid());
		model.addAttribute("compensatebig", bill.getCompensatebig());
		model.addAttribute("sort", sort);
		model.addAttribute("method", method);
		return "penalizeOutBill/penalizeOutBillPage";
	}

	/**
	 * 增加赔付账单
	 */
	@RequestMapping("/addPenalizeOutBill")
	@ResponseBody
	public Long addPenalizeOutBill(@RequestParam(value = "compensatebig", required = false) Integer compensatebig, @RequestParam(value = "compensatesmall", required = false) Integer compensatesmall,
			@RequestParam(value = "compensateodd", required = false) String compensateodd, @RequestParam(value = "customerid", required = false) String customerid,
			@RequestParam(value = "creationStartDate", required = false) String creationStartDate, @RequestParam(value = "creationEndDate", required = false) String creationEndDate,
			@RequestParam(value = "compensateexplain", required = false) String compensateexplain, @RequestParam(value = "batchstate", required = false) String batchstate,
			@RequestParam(value = "dutypersonid", required = false) String dutypersonid, @RequestParam(value = "sumPrice", required = false) BigDecimal sumPrice,
			@RequestParam(value = "punishInsideRemark", required = false) String punishInsideRemark, @RequestParam(value = "checkbox1", required = false) String checkbox1) {
		Long id = this.penalizeOutBillService.addPenalizeOutBill(compensatebig, compensatesmall, compensateodd, customerid, creationStartDate, creationEndDate, compensateexplain);
		if ((checkbox1 != "") && (checkbox1 != null)) {
			this.penalizeOutBillService.addpunishinsideBill(batchstate, dutypersonid, sumPrice, punishInsideRemark, compensateodd);
		}
		return id;
	}

	/**
	 * 通过id查询所有记录
	 */
	@RequestMapping("/queryById/{page}")
	public String queryById(Model model, @RequestParam(value = "id", required = false) Integer id, @PathVariable("page") long page,
			@RequestParam(value = "billCreationStartDate", required = false) String billCreationStartDate, @RequestParam(value = "billCreationEndDate", required = false) String billCreationEndDate,
			@RequestParam(value = "billVerificationStrartDate", required = false) String billVerificationStrartDate,
			@RequestParam(value = "billVerificationEndDate", required = false) String billVerificationEndDate, @RequestParam(value = "sort", required = false) String sort,
			@RequestParam(value = "method", required = false) String method) {
		List<penalizeOutBill> list = this.penalizeOutBillService.queryAll(new penalizeOutBill(), billCreationStartDate, billCreationEndDate, billVerificationStrartDate, billVerificationEndDate, sort,
				method, page);
		model.addAttribute("billList", list);
		int coun = this.PenalizeOutBilldao.queryAllCount(new penalizeOutBill(), billCreationStartDate, billCreationEndDate, billVerificationStrartDate, billVerificationEndDate, sort, method, page);
		Page page_ob = new Page(coun, page, Page.ONE_PAGE_NUMBER);
		model.addAttribute("page", page);
		model.addAttribute("page_ob", page_ob);
		penalizeOutBill bill = this.penalizeOutBillService.queryById(id, page);
		int sum = this.penalizeOutBillService.queryByIdcount(id);
		List<Customer> customerList = this.customerDao.getAllCustomerss();
		List<PenalizeType> penalizebigList = this.penalizeTypeDAO.getPenalizeTypeByType(1);
		List<PenalizeType> penalizesmallList = this.penalizeTypeDAO.getPenalizeTypeByType(2);
		List<User> userList = this.userDAO.getAllUser();
		int jiesuanAuthority = 0;
		int jiesuanAdvanceAuthority = 0;
		long userid = 0;
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
			userid = user.getUserid();
		}

		Map<Integer, String> cwbStateMap = CwbStateEnum.getMap();
		Page page_o = new Page(sum, page, Page.ONE_PAGE_NUMBER);
		model.addAttribute("page", page);
		model.addAttribute("page_o", page_o);
		model.addAttribute("jiesuanAuthority", jiesuanAuthority);
		model.addAttribute("jiesuanAdvanceAuthority", jiesuanAdvanceAuthority);
		model.addAttribute("weiShenHeState", PunishBillStateEnum.WeiShenHe.getValue());
		model.addAttribute("yiShenHeState", PunishBillStateEnum.YiShenHe.getValue());
		model.addAttribute("yiHeXiaoState", PunishBillStateEnum.YiHeXiao.getValue());
		model.addAttribute("userid", userid);
		model.addAttribute("cwbStateMap", cwbStateMap);
		model.addAttribute("bill", bill);
		model.addAttribute("userList", userList);
		model.addAttribute("penalizebigList", penalizebigList);
		model.addAttribute("penalizesmallList", penalizesmallList);
		model.addAttribute("customerList", customerList);
		model.addAttribute("PunishBillStateEnum", PunishBillStateEnum.values());
		model.addAttribute("updatePage", 1);
		return "penalizeOutBill/penalizeOutBillPage";
	}

	/**
	 * 打开赔付账单明细
	 */
	@RequestMapping("/penalizeOutBillDedail")
	public String penalizeOutBillDedail(Model model, @RequestParam(value = "id", required = false) Integer id,
			@RequestParam(value = "billCreationStartDate", required = false) String billCreationStartDate, @RequestParam(value = "billCreationEndDate", required = false) String billCreationEndDate,
			@RequestParam(value = "billVerificationStrartDate", required = false) String billVerificationStrartDate,
			@RequestParam(value = "billVerificationEndDate", required = false) String billVerificationEndDate, @RequestParam(value = "sort", required = false) String sort,
			@RequestParam(value = "method", required = false) String method) {
		long page = 1;
		List<penalizeOutBill> list = this.penalizeOutBillService.queryAll(new penalizeOutBill(), billCreationStartDate, billCreationEndDate, billVerificationStrartDate, billVerificationEndDate, sort,
				method, page);
		model.addAttribute("billList", list);
		penalizeOutBill bill = this.penalizeOutBillService.queryById(id, page);

		List<Customer> customerList = this.customerDao.getAllCustomerss();
		List<PenalizeType> penalizebigList = this.penalizeTypeDAO.getPenalizeTypeByType(1);
		List<PenalizeType> penalizesmallList = this.penalizeTypeDAO.getPenalizeTypeByType(2);
		List<User> userList = this.userDAO.getAllUser();
		Map<Integer, String> cwbStateMap = CwbStateEnum.getMap();

		int jiesuanAuthority = 0;
		int jiesuanAdvanceAuthority = 0;
		long userid = 0;
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
			userid = user.getUserid();
		}

		model.addAttribute("jiesuanAuthority", jiesuanAuthority);
		model.addAttribute("jiesuanAdvanceAuthority", jiesuanAdvanceAuthority);
		model.addAttribute("weiShenHeState", PunishBillStateEnum.WeiShenHe.getValue());
		model.addAttribute("yiShenHeState", PunishBillStateEnum.YiShenHe.getValue());
		model.addAttribute("yiHeXiaoState", PunishBillStateEnum.YiHeXiao.getValue());
		model.addAttribute("userid", userid);
		model.addAttribute("cwbStateMap", cwbStateMap);
		model.addAttribute("bill", bill);
		model.addAttribute("userList", userList);
		model.addAttribute("penalizebigList", penalizebigList);
		model.addAttribute("penalizesmallList", penalizesmallList);
		model.addAttribute("customerList", customerList);
		model.addAttribute("PunishBillStateEnum", PunishBillStateEnum.values());
		model.addAttribute("updatePage", 1);
		model.addAttribute("updateDedailPage", 1);
		return "penalizeOutBill/penalizeOutBillPage";
	}

	/**
	 * 查询出指定订单的明细
	 */
	@RequestMapping("/penalizeOutBillList/{page}")
	public String addpenalizeOutBillList(Model model, @RequestParam(value = "id", required = false) Integer id, @PathVariable("page") long page,
			@RequestParam(value = "billCreationStartDate", required = false) String billCreationStartDate, @RequestParam(value = "billCreationEndDate", required = false) String billCreationEndDate,
			@RequestParam(value = "billVerificationStrartDate", required = false) String billVerificationStrartDate,
			@RequestParam(value = "billVerificationEndDate", required = false) String billVerificationEndDate, @RequestParam(value = "sort", required = false) String sort,
			@RequestParam(value = "method", required = false) String method, @RequestParam(value = "compensatebig", required = false) Integer compensatebig,
			@RequestParam(value = "compensatesmall", required = false) Integer compensatesmall, @RequestParam(value = "compensateodd", required = false) String compensateodd,
			@RequestParam(value = "customerid", required = false) Integer customerid, @RequestParam(value = "creationStartDate", required = false) String creationStartDate,
			@RequestParam(value = "creationEndDate", required = false) String creationEndDate) {
		List<penalizeOutBill> list = this.penalizeOutBillService.queryAll(new penalizeOutBill(), billCreationStartDate, billCreationEndDate, billVerificationStrartDate, billVerificationEndDate, sort,
				method, page);
		model.addAttribute("billList", list);
		int coun = this.PenalizeOutBilldao.queryAllCount(new penalizeOutBill(), billCreationStartDate, billCreationEndDate, billVerificationStrartDate, billVerificationEndDate, sort, method, page);
		// 主页面的分页
		Page page_ob = new Page(coun, page, Page.ONE_PAGE_NUMBER);
		model.addAttribute("page", page);
		model.addAttribute("page_ob", page_ob);
		penalizeOutBill bill = this.penalizeOutBillService.queryById(id, page);
		int sum = this.penalizeOutBillService.queryByIdcount(id);
		List<Customer> customerList = this.customerDao.getAllCustomerss();
		List<PenalizeType> penalizebigList = this.penalizeTypeDAO.getPenalizeTypeByType(1);
		List<PenalizeType> penalizesmallList = this.penalizeTypeDAO.getPenalizeTypeByType(2);
		List<User> userList = this.userDAO.getAllUser();
		Map<Integer, String> cwbStateMap = CwbStateEnum.getMap();
		List<PenalizeOut> penalizeOut = this.penalizeOutBillService.penalizeOutBillList(id, compensatebig, compensatesmall, compensateodd, customerid, creationStartDate, creationEndDate, page);
		int count = this.penalizeOutBillService.queryByOddDetailsum(id, compensatebig, compensatesmall, compensateodd, customerid, creationStartDate, creationEndDate, page);
		int jiesuanAuthority = 0;
		int jiesuanAdvanceAuthority = 0;
		long userid = 0;
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
			userid = user.getUserid();
		}
		// 账单中的订单明细分页
		Page page_o = new Page(sum, page, Page.ONE_PAGE_NUMBER);
		model.addAttribute("page", page);
		model.addAttribute("page_o", page_o);
		// 添加订单时所需要的分页
		Page page_obj = new Page(count, page, Page.ONE_PAGE_NUMBER);
		model.addAttribute("page", page);
		model.addAttribute("page_obj", page_obj);
		model.addAttribute("jiesuanAuthority", jiesuanAuthority);
		model.addAttribute("jiesuanAdvanceAuthority", jiesuanAdvanceAuthority);
		model.addAttribute("weiShenHeState", PunishBillStateEnum.WeiShenHe.getValue());
		model.addAttribute("yiShenHeState", PunishBillStateEnum.YiShenHe.getValue());
		model.addAttribute("yiHeXiaoState", PunishBillStateEnum.YiHeXiao.getValue());
		model.addAttribute("userid", userid);

		model.addAttribute("penalizeOut", penalizeOut);
		model.addAttribute("cwbStateMap", cwbStateMap);
		model.addAttribute("bill", bill);
		model.addAttribute("userList", userList);
		model.addAttribute("penalizebigList", penalizebigList);
		model.addAttribute("penalizesmallList", penalizesmallList);
		model.addAttribute("customerList", customerList);
		model.addAttribute("PunishBillStateEnum", PunishBillStateEnum.values());
		model.addAttribute("updatePage", 1);
		model.addAttribute("updateDedailPage", 1);

		model.addAttribute("creationStartDate", creationStartDate);
		model.addAttribute("creationEndDate", creationEndDate);
		model.addAttribute("compensatebig", compensatebig);
		model.addAttribute("compensatesmall", compensatesmall);
		compensateodd = compensateodd.replace(",", "\r\n");
		model.addAttribute("compen", compensateodd);

		return "penalizeOutBill/penalizeOutBillPage";
	}

	/**
	 * 添加赔付订单到赔付账单中
	 *
	 * @param model
	 * @param id
	 * @param billCreationStartDate
	 * @param billCreationEndDate
	 * @param billVerificationStrartDate
	 * @param billVerificationEndDate
	 * @param sort
	 * @param method
	 * @return
	 */
	@RequestMapping("/addpenalizeOutBillList")
	public String addpenalizeOutBillList(Model model, @RequestParam(value = "id", required = false) Integer id, @RequestParam(value = "compensateodd", required = false) String compensateodd,
			@RequestParam(value = "billCreationStartDate", required = false) String billCreationStartDate, @RequestParam(value = "billCreationEndDate", required = false) String billCreationEndDate,
			@RequestParam(value = "billVerificationStrartDate", required = false) String billVerificationStrartDate,
			@RequestParam(value = "billVerificationEndDate", required = false) String billVerificationEndDate, @RequestParam(value = "sort", required = false) String sort,
			@RequestParam(value = "method", required = false) String method) {
		this.penalizeOutBillService.addpenalizeOutBillList(id, compensateodd);
		long page = 1;
		List<penalizeOutBill> list = this.penalizeOutBillService.queryAll(new penalizeOutBill(), billCreationStartDate, billCreationEndDate, billVerificationStrartDate, billVerificationEndDate, sort,
				method, page);
		model.addAttribute("billList", list);
		penalizeOutBill bill = this.penalizeOutBillService.queryById(id, page);
		int sum = this.penalizeOutBillService.queryByIdcount(id);
		List<Customer> customerList = this.customerDao.getAllCustomerss();
		List<PenalizeType> penalizebigList = this.penalizeTypeDAO.getPenalizeTypeByType(1);
		List<PenalizeType> penalizesmallList = this.penalizeTypeDAO.getPenalizeTypeByType(2);
		List<User> userList = this.userDAO.getAllUser();
		Map<Integer, String> cwbStateMap = CwbStateEnum.getMap();
		int jiesuanAuthority = 0;
		int jiesuanAdvanceAuthority = 0;
		long userid = 0;
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
			userid = user.getUserid();
		}

		int coun = this.PenalizeOutBilldao.queryAllCount(new penalizeOutBill(), billCreationStartDate, billCreationEndDate, billVerificationStrartDate, billVerificationEndDate, sort, method, page);
		// 主页面的分页
		Page page_ob = new Page(coun, page, Page.ONE_PAGE_NUMBER);
		model.addAttribute("page", page);
		model.addAttribute("page_ob", page_ob);

		// 账单中的订单明细分页
		Page page_o = new Page(sum, page, Page.ONE_PAGE_NUMBER);
		model.addAttribute("page", page);
		model.addAttribute("page_o", page_o);
		model.addAttribute("jiesuanAuthority", jiesuanAuthority);
		model.addAttribute("jiesuanAdvanceAuthority", jiesuanAdvanceAuthority);
		model.addAttribute("weiShenHeState", PunishBillStateEnum.WeiShenHe.getValue());
		model.addAttribute("yiShenHeState", PunishBillStateEnum.YiShenHe.getValue());
		model.addAttribute("yiHeXiaoState", PunishBillStateEnum.YiHeXiao.getValue());
		model.addAttribute("userid", userid);
		model.addAttribute("cwbStateMap", cwbStateMap);
		model.addAttribute("bill", bill);
		model.addAttribute("userList", userList);
		model.addAttribute("penalizebigList", penalizebigList);
		model.addAttribute("penalizesmallList", penalizesmallList);
		model.addAttribute("customerList", customerList);
		model.addAttribute("PunishBillStateEnum", PunishBillStateEnum.values());
		model.addAttribute("updatePage", 1);
		model.addAttribute("msg", "添加成功");
		return "penalizeOutBill/penalizeOutBillPage";
	}

	/**
	 * 修改指定账单信息
	 */
	@RequestMapping("/penalizeOutBillUpdate")
	public String penalizeOutBillUpdate(Model model, penalizeOutBill bill1, @RequestParam(value = "id", required = false) Integer id,
			@RequestParam(value = "billCreationStartDate", required = false) String billCreationStartDate, @RequestParam(value = "billCreationEndDate", required = false) String billCreationEndDate,
			@RequestParam(value = "billVerificationStrartDate", required = false) String billVerificationStrartDate,
			@RequestParam(value = "billVerificationEndDate", required = false) String billVerificationEndDate, @RequestParam(value = "sort", required = false) String sort,
			@RequestParam(value = "method", required = false) String method) {
		this.penalizeOutBillService.penalizeOutBillUpdate(bill1);
		long page = 1;
		List<penalizeOutBill> list = this.penalizeOutBillService.queryAll(new penalizeOutBill(), billCreationStartDate, billCreationEndDate, billVerificationStrartDate, billVerificationEndDate, sort,
				method, page);
		model.addAttribute("billList", list);
		penalizeOutBill bill = this.penalizeOutBillService.queryById(id, page);
		int sum = this.penalizeOutBillService.queryByIdcount(id);
		List<Customer> customerList = this.customerDao.getAllCustomerss();
		List<PenalizeType> penalizebigList = this.penalizeTypeDAO.getPenalizeTypeByType(1);
		List<PenalizeType> penalizesmallList = this.penalizeTypeDAO.getPenalizeTypeByType(2);
		List<User> userList = this.userDAO.getAllUser();
		Map<Integer, String> cwbStateMap = CwbStateEnum.getMap();
		int jiesuanAuthority = 0;
		int jiesuanAdvanceAuthority = 0;
		long userid = 0;
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
			userid = user.getUserid();
		}
		int coun = this.PenalizeOutBilldao.queryAllCount(new penalizeOutBill(), billCreationStartDate, billCreationEndDate, billVerificationStrartDate, billVerificationEndDate, sort, method, page);
		// 主页面的分页
		Page page_ob = new Page(coun, page, Page.ONE_PAGE_NUMBER);
		model.addAttribute("page", page);
		model.addAttribute("page_ob", page_ob);

		// 账单中的订单明细分页
		Page page_o = new Page(sum, page, Page.ONE_PAGE_NUMBER);
		model.addAttribute("page", page);
		model.addAttribute("page_o", page_o);
		model.addAttribute("jiesuanAuthority", jiesuanAuthority);
		model.addAttribute("jiesuanAdvanceAuthority", jiesuanAdvanceAuthority);
		model.addAttribute("weiShenHeState", PunishBillStateEnum.WeiShenHe.getValue());
		model.addAttribute("yiShenHeState", PunishBillStateEnum.YiShenHe.getValue());
		model.addAttribute("yiHeXiaoState", PunishBillStateEnum.YiHeXiao.getValue());
		model.addAttribute("userid", userid);
		model.addAttribute("cwbStateMap", cwbStateMap);
		model.addAttribute("bill", bill);
		model.addAttribute("userList", userList);
		model.addAttribute("penalizebigList", penalizebigList);
		model.addAttribute("penalizesmallList", penalizesmallList);
		model.addAttribute("customerList", customerList);
		model.addAttribute("PunishBillStateEnum", PunishBillStateEnum.values());
		model.addAttribute("updatemsg", "修改成功");
		return "penalizeOutBill/penalizeOutBillPage";

	}

	// 删除指定的账单
	@RequestMapping("/deletePenalizeOutBill")
	@ResponseBody
	public String deletePenalizeOutBill(@RequestParam(value = "id", required = false) Integer id) {
		this.penalizeOutBillService.deletePenalizeOutBill(id);
		return "{\"errorCode\":0,\"error\":\"删除成功\"}";
	}
}
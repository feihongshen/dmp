package cn.explink.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.explink.dao.AccountCwbDetailDAO;
import cn.explink.dao.AccountCwbSummaryDAO;
import cn.explink.dao.AccountFeeDetailDAO;
import cn.explink.dao.BranchDAO;
import cn.explink.dao.UserDAO;
import cn.explink.domain.AccountCwbDetail;
import cn.explink.domain.AccountCwbDetailView;
import cn.explink.domain.AccountCwbSummary;
import cn.explink.domain.AccountFeeDetail;
import cn.explink.domain.Branch;
import cn.explink.domain.User;
import cn.explink.enumutil.AccountFlowOrderTypeEnum;
import cn.explink.enumutil.AccountTongjiEnum;
import cn.explink.enumutil.BranchEnum;
import cn.explink.exception.CwbException;
import cn.explink.service.AccountCwbDetailService;
import cn.explink.service.AccountCwbSummaryService;
import cn.explink.service.ExplinkUserDetail;
import cn.explink.service.ExportService;
import cn.explink.util.ExcelUtils;
import cn.explink.util.Page;

/**
 * 结算管理
 * 
 * @author zs
 *
 */

@Controller
@RequestMapping("/accountcwbdetail")
public class AccountCwbDetailController {
	@Autowired
	UserDAO userDAO;

	@Autowired
	AccountCwbDetailService accountCwbDetailService;
	@Autowired
	AccountCwbSummaryService accountCwbSummaryService;
	@Autowired
	SecurityContextHolderStrategy securityContextHolderStrategy;

	@Autowired
	BranchDAO branchDAO;

	@Autowired
	AccountCwbSummaryDAO accountCwbSummaryDAO;

	@Autowired
	AccountCwbDetailDAO accountCwbDetailDAO;

	@Autowired
	ExportService exportService;

	@Autowired
	AccountFeeDetailDAO accountFeeDetailDAO;

	private User getSessionUser() {
		ExplinkUserDetail userDetail = (ExplinkUserDetail) securityContextHolderStrategy.getContext().getAuthentication().getPrincipal();
		return userDetail.getUser();
	}

	/**
	 * 先付：显示信息
	 * 
	 * @param model
	 * @param branchid
	 * @return
	 */
	@RequestMapping("/outwarehouseInfo/{branchid}")
	public String outwarehouseInfo(Model model, @PathVariable("branchid") long branchid) {
		// 当前站点按权限 先付类型过滤
		model.addAttribute("branchList", branchDAO.getBranchByBranchidAccounttype(getSessionUser().getBranchid(), 1, String.valueOf(BranchEnum.ZhanDian.getValue()), getSessionUser().getUserid()));
		model.addAttribute("branchid", branchid);
		// 所选择的站点
		Branch selectBranch = new Branch();
		selectBranch = branchDAO.getBranchByBranchid(branchid);
		model.addAttribute("selectBranch", selectBranch);
		if (branchid > 0) {
			Map typeMap = new HashMap();
			// 中转退款、退货退款信息、POS退款
			typeMap = accountCwbDetailService.getAccountCwbByZZTHMap(branchid);
			// 加减款
			List<AccountFeeDetail> jiajianList = accountFeeDetailDAO.getAccountCustomFee(branchid);
			typeMap = accountCwbDetailService.getAccountCustomFee(typeMap, jiajianList);
			// 出库ID
			String chukuIds = AccountFlowOrderTypeEnum.KuFangChuKu.getValue() + "," + AccountFlowOrderTypeEnum.ZhongZhuanChuKu.getValue() + "," + AccountFlowOrderTypeEnum.TuiHuoChuKu.getValue();
			// 应交货款List
			List<AccountCwbDetail> yjList = accountCwbDetailDAO.getAccountCwbByYJList(branchid, chukuIds);
			typeMap = accountCwbDetailService.getAccountCwbByYJQKMap(yjList, typeMap, 0);

			// 二级单位应交货款List
			List<AccountCwbDetail> yjejList = accountCwbDetailDAO.getAccountCwbDetailByYTTEJ(branchid, chukuIds);
			typeMap = accountCwbDetailService.getAccountCwbByYJQKMap(yjejList, typeMap, 2);

			// 欠款List
			List<AccountCwbDetail> qkList = accountCwbDetailDAO.getAccountCwbByQKList(branchid, chukuIds);
			typeMap = accountCwbDetailService.getAccountCwbByYJQKMap(qkList, typeMap, 1);

			model.addAttribute("typeMap", typeMap);
			model.addAttribute("yjList", yjList);
			model.addAttribute("qkList", qkList);
			model.addAttribute("jiajianList", jiajianList);
			model.addAttribute("yjejList", yjejList);
		}
		return "/accountcwbdetail/outwarehouseList";
	}

	/**
	 * 先付:新建
	 * 
	 * @param model
	 * @param branchid
	 * @param summaryid
	 * @param request
	 * @return
	 */
	@RequestMapping("/createOutwarehouse/{branchid}")
	public @ResponseBody String createOutwarehouse(Model model, @PathVariable("branchid") long branchid, HttpServletRequest request) {
		long summaryid = 0;
		try {
			summaryid = accountCwbDetailService.createoutwarehouse(request, getSessionUser(), branchid);
			return "{\"errorCode\":0,\"error\":\"提交成功\",\"summaryid\":\"" + summaryid + "\"}";
		} catch (CwbException e) {
			return "{\"errorCode\":1,\"error\":\"" + e.getMessage() + "\"}";
		}
	}

	/**
	 * 先付：修改
	 * 
	 * @param model
	 * @param summaryid
	 * @param checkoutstate
	 * @param request
	 * @return
	 */
	@RequestMapping("/saveOutwarehouse/{summaryid}")
	public @ResponseBody String saveOutwarehouse(Model model, @PathVariable("summaryid") long summaryid,
			@RequestParam(value = "checkoutstate", required = false, defaultValue = "0") long checkoutstate, HttpServletRequest request) {
		try {
			accountCwbDetailService.updateoutwarehouse(request, getSessionUser(), checkoutstate, summaryid);
			if (checkoutstate == 1) {
				accountCwbDetailService.updateDelivery(request, getSessionUser());
			}
			return "{\"errorCode\":0,\"error\":\"交款成功\"}";
		} catch (Exception e) {
			e.printStackTrace();
			// logger.info("先付修改异常:"+e);
			return "{\"errorCode\":1,\"error\":\"交款失败\"}";
		}
	}

	/**
	 * 先付： 撤销
	 * 
	 * @param model
	 * @param summaryid
	 * @param branchid
	 * @return
	 */
	@RequestMapping("/outwarehouseDelete")
	public @ResponseBody String outwarehouseDelete(Model model, @RequestParam(value = "summaryid", required = false, defaultValue = "0") long summaryid,
			@RequestParam(value = "branchid", required = false, defaultValue = "0") long branchid) {
		try {
			accountCwbDetailService.outwarehouseDelete(summaryid, getSessionUser());
			return "{\"errorCode\":0,\"error\":\"撤销成功\"}";
		} catch (Exception e) {
			e.printStackTrace();
			return "{\"errorCode\":1,\"error\":\"撤销失败\"}";
		}
	}

	/**
	 * 先付：列表
	 * 
	 * @param model
	 * @param page
	 * @param branchname
	 * @param checkoutstate
	 * @param request
	 * @return
	 */
	@RequestMapping("/listOutwarehouse/{page}")
	public String listOutwarehouse(Model model, @PathVariable("page") long page, @RequestParam(value = "checkoutstate", required = false, defaultValue = "0") long checkoutstate,
			@RequestParam(value = "branchid", required = false, defaultValue = "0") long branchid, HttpServletRequest request) {
		List<Branch> branchList = branchDAO.getBranchByBranchidAccounttype(getSessionUser().getBranchid(), 1, String.valueOf(BranchEnum.ZhanDian.getValue()), getSessionUser().getUserid());
		List<User> userList = userDAO.getAllUser();
		String branchids = "";
		// 如果为选择站点则匹配用户权限
		if (branchid == 0) {
			if (branchList != null && !branchList.isEmpty()) {
				for (Branch listb : branchList) {
					branchids += listb.getBranchid() + ",";
				}
			}
			if (!"".equals(branchids)) {
				branchids = branchids.substring(0, branchids.lastIndexOf(","));
			}
		} else {
			branchids = String.valueOf(branchid);
		}

		model.addAttribute("summaryList", accountCwbSummaryService.getAccountCwbSummaryListByBranchAndUser(branchList, userList, page, checkoutstate, branchids, 1, "", ""));
		model.addAttribute("page_obj", new Page(accountCwbSummaryDAO.getAccountCwbSummaryCount(checkoutstate, branchids, 1, "", ""), page, Page.ONE_PAGE_NUMBER));
		model.addAttribute("page", page);
		model.addAttribute("branchList", branchList);
		return "/accountcwbdetail/outwarehouseCheck";
	}

	/**
	 * 先付：得到一条汇总信息
	 * 
	 * @param model
	 * @param summaryid
	 * @param request
	 * @return
	 */
	@RequestMapping("/getOutwarehouse/{summaryid}")
	public String getOutwarehouse(Model model, @PathVariable("summaryid") long summaryid, HttpServletRequest request) {
		AccountCwbSummary accountCwbSummary = new AccountCwbSummary();
		accountCwbSummary = accountCwbSummaryDAO.getAccountCwbSummaryById(summaryid);
		model.addAttribute("accountCwbSummary", accountCwbSummary);
		return "/accountcwbdetail/outwarehouseSummary";
	}

	/**
	 * 先付：显示信息--明细(根据ids)
	 * 
	 * @param model
	 * @param page
	 * @param ids
	 * @return
	 */
	@RequestMapping("/outwarehouseDetail/{page}")
	public String outwarehouseDetail(Model model, @PathVariable("page") long page, @RequestParam(value = "ids", required = false, defaultValue = "") String ids) {
		long total = 0;
		AccountCwbDetail accountCwbDetail = new AccountCwbDetail();
		accountCwbDetail = accountCwbDetailDAO.getReceivablefeeSum(ids);
		total = accountCwbDetailDAO.getAccountCwbDetailIdsCount(ids);
		model.addAttribute("detailList", accountCwbDetailDAO.getAccountCwbDetailIds(page, ids));
		model.addAttribute("page_obj", new Page(total, page, Page.ONE_PAGE_NUMBER));
		model.addAttribute("page", page);
		model.addAttribute("accountCwbDetail", accountCwbDetail);
		model.addAttribute("total", total);
		return "/accountcwbdetail/outwarehouseDetail";
	}

	/**
	 * 先付：明细(根据汇总summaryid)
	 * 
	 * @param model
	 * @param page
	 * @param ids
	 * @param state
	 * @return
	 */
	@RequestMapping("/outwarehousecwbs/{page}")
	public String outwarehousecwbs(Model model, @PathVariable("page") long page, @RequestParam("summaryid") long summaryid,
			@RequestParam(value = "flowOrderType", required = false, defaultValue = "") long flowOrderType) {
		String chukuIds = AccountFlowOrderTypeEnum.KuFangChuKu.getValue() + "," + AccountFlowOrderTypeEnum.ZhongZhuanChuKu.getValue() + "," + AccountFlowOrderTypeEnum.TuiHuoChuKu.getValue();
		long total = 0;
		AccountCwbDetail accountCwbDetail = new AccountCwbDetail();

		List<AccountCwbDetail> list = new ArrayList<AccountCwbDetail>();
		// 中转
		if (flowOrderType == AccountFlowOrderTypeEnum.ZhongZhuanRuKu.getValue()) {
			String zzId = AccountFlowOrderTypeEnum.ZhongZhuanRuKu.getValue() + "," + AccountFlowOrderTypeEnum.GaiZhanChongKuan.getValue();

			list = accountCwbDetailDAO.getAccountCwbDetailByType(page, zzId, summaryid);
			total = accountCwbDetailDAO.getCountAccountCwbDetailByType(zzId, summaryid);
			accountCwbDetail = accountCwbDetailDAO.getDetailByTypeSum(zzId, summaryid);
		}
		// 退货、POS
		if (flowOrderType == AccountFlowOrderTypeEnum.TuiHuoRuKu.getValue() || flowOrderType == AccountFlowOrderTypeEnum.Pos.getValue()) {
			list = accountCwbDetailDAO.getAccountCwbDetailByType(page, String.valueOf(flowOrderType), summaryid);
			total = accountCwbDetailDAO.getCountAccountCwbDetailByType(String.valueOf(flowOrderType), summaryid);
			accountCwbDetail = accountCwbDetailDAO.getDetailByTypeSum(String.valueOf(flowOrderType), summaryid);
		}
		// 欠款
		if (flowOrderType == AccountTongjiEnum.QianKuan.getValue()) {
			list = accountCwbDetailDAO.getAccountCwbDetailByQK(page, chukuIds, summaryid);
			total = accountCwbDetailDAO.getCountAccountCwbDetailByQK(chukuIds, summaryid);
			accountCwbDetail = accountCwbDetailDAO.getDetailByQKSum(chukuIds, summaryid);
		}
		// 已交
		if (flowOrderType == AccountTongjiEnum.YingJiao.getValue()) {
			list = accountCwbDetailDAO.getAccountCwbDetailByType(page, chukuIds, summaryid);
			total = accountCwbDetailDAO.getCountAccountCwbDetailByType(chukuIds, summaryid);
			accountCwbDetail = accountCwbDetailDAO.getDetailByTypeSum(chukuIds, summaryid);
		}
		// 未交
		if (flowOrderType == AccountTongjiEnum.WeiJiao.getValue()) {
			list = accountCwbDetailDAO.getAccountCwbDetailByWj(page, chukuIds, summaryid);
			total = accountCwbDetailDAO.getCountAccountCwbDetailByWj(chukuIds, summaryid);
			accountCwbDetail = accountCwbDetailDAO.getDetailByWjSum(chukuIds, summaryid);
		}

		// 总计应交
		if (flowOrderType == AccountTongjiEnum.ToYingJiao.getValue()) {
			list = accountCwbDetailDAO.getAccountCwbDetailByTo(page, chukuIds, summaryid);
			total = accountCwbDetailDAO.getCountAccountCwbDetailByTo(chukuIds, summaryid);
			accountCwbDetail = accountCwbDetailDAO.getDetailByToSum(chukuIds, summaryid);
		}

		model.addAttribute("detailList", list);
		model.addAttribute("page_obj", new Page(total, page, Page.ONE_PAGE_NUMBER));
		model.addAttribute("page", page);
		model.addAttribute("total", total);
		model.addAttribute("accountCwbDetail", accountCwbDetail);
		return "/accountcwbdetail/outwarehouseCwbs";
	}

	/**
	 * 后付：显示信息
	 * 
	 * @param model
	 * @param selectDS
	 *            代收货款
	 * @return
	 */
	@RequestMapping("/deliveryInfo")
	public String deliveryInfo(Model model, @RequestParam(value = "selectDS", required = false, defaultValue = "0") String selectDS,
			@RequestParam(value = "branchid", required = false, defaultValue = "0") long branchid) {
		Branch selectBranch = new Branch();
		// 判断当前操作用户类型为站点
		Branch userbranch = branchDAO.getBranchByBranchid(getSessionUser().getBranchid());
		if (userbranch.getSitetype() == BranchEnum.ZhanDian.getValue()) {
			selectBranch = branchDAO.getBranchByBranchid(getSessionUser().getBranchid());
		} else {// 财务登录
				// 当前站点按权限 配送结果结算
			model.addAttribute("branchList", branchDAO.getBranchByBranchidAccounttype(getSessionUser().getBranchid(), 2, String.valueOf(BranchEnum.ZhanDian.getValue()), getSessionUser().getUserid()));
			if (branchid > 0) {
				selectBranch = branchDAO.getBranchByBranchid(branchid);
			}
		}
		model.addAttribute("selectBranch", selectBranch);

		if (selectBranch.getBranchid() > 0) {
			Map typeMap = new HashMap();
			// 加减款
			List<AccountFeeDetail> jiajianList = accountFeeDetailDAO.getAccountCustomFee(selectBranch.getBranchid());
			typeMap = accountCwbDetailService.getAccountCustomFee(typeMap, jiajianList);
			// 已妥投交款List
			List<AccountCwbDetail> yjList = accountCwbDetailDAO.getAccountCwbDetailByYTT(selectBranch.getBranchid(), AccountFlowOrderTypeEnum.GuiBanShenHe.getValue(), selectDS);
			typeMap = accountCwbDetailService.getAccountCwbByYTTMap(yjList, typeMap, 0);
			// 欠款List
			List<AccountCwbDetail> qkList = accountCwbDetailDAO.getDeliveryByQKList(selectBranch.getBranchid(), String.valueOf(AccountFlowOrderTypeEnum.GuiBanShenHe.getValue()));
			typeMap = accountCwbDetailService.getAccountCwbByYTTMap(qkList, typeMap, 1);
			// 二级单位已妥投交款List
			List<AccountCwbDetail> yjejList = accountCwbDetailDAO.getDeliveryByYTTEJ(selectBranch.getBranchid(), String.valueOf(AccountFlowOrderTypeEnum.GuiBanShenHe.getValue()));
			typeMap = accountCwbDetailService.getAccountCwbByYTTMap(yjejList, typeMap, 2);

			model.addAttribute("typeMap", typeMap);
			model.addAttribute("yjList", yjList);
			model.addAttribute("yjejList", yjejList);
			model.addAttribute("qkList", qkList);
		}
		return "/accountcwbdetail/deliveryList";
	}

	/**
	 * 后付：新建
	 * 
	 * @param model
	 * @param branchid
	 * @param request
	 * @return
	 */
	@RequestMapping("/createdelivery/{branchid}")
	public @ResponseBody String createDelivery(Model model, @PathVariable("branchid") long branchid, HttpServletRequest request) {
		long summaryid = 0;
		try {
			summaryid = accountCwbDetailService.createDelivery(request, getSessionUser(), branchid);
			return "{\"errorCode\":0,\"error\":\"交款成功\"}";
		} catch (CwbException e) {
			return "{\"errorCode\":1,\"error\":\"" + e.getMessage() + "\"}";
		}

	}

	/**
	 * 后付：修改
	 * 
	 * @param model
	 * @param summaryid
	 * @param checkoutstate
	 * @param request
	 * @return
	 */
	@RequestMapping("/savedelivery/{summaryid}")
	public @ResponseBody String savedelivery(Model model, @PathVariable("summaryid") long summaryid, @RequestParam(value = "checkoutstate", required = false, defaultValue = "0") long checkoutstate,
			HttpServletRequest request) {
		try {
			accountCwbDetailService.updateoutwarehouse(request, getSessionUser(), checkoutstate, summaryid);
			if (checkoutstate == 1) {
				accountCwbDetailService.updateDelivery(request, getSessionUser());
			}
			return "{\"errorCode\":0,\"error\":\"交款成功\"}";
		} catch (Exception e) {
			e.printStackTrace();
			// logger.info("库房先付审核异常:"+e);
			return "{\"errorCode\":1,\"error\":\"交款失败\"}";
		}
	}

	/**
	 * 后付：列表
	 * 
	 * @param model
	 * @param page
	 * @param branchname
	 * @param checkoutstate
	 * @param request
	 * @return
	 */
	@RequestMapping("/listDelivery/{page}")
	public String listDelivery(Model model, @PathVariable("page") long page, @RequestParam(value = "branchid", required = false, defaultValue = "0") long branchid,
			@RequestParam(value = "checkoutstate", required = false, defaultValue = "0") long checkoutstate, @RequestParam(value = "starttime", required = false, defaultValue = "") String starttime,
			@RequestParam(value = "endtime", required = false, defaultValue = "") String endtime, HttpServletRequest request) {
		List<User> userList = userDAO.getAllUser();
		List<Branch> branchList = branchDAO.getBranchByBranchidAccounttype(getSessionUser().getBranchid(), 2, String.valueOf(BranchEnum.ZhanDian.getValue()), getSessionUser().getUserid());
		Branch userbranch = branchDAO.getBranchByBranchid(getSessionUser().getBranchid());

		// 判断当前操作用户类型为站点
		if (userbranch.getSitetype() == BranchEnum.ZhanDian.getValue()) {
			branchList.add(branchDAO.getBranchByBranchid(getSessionUser().getBranchid()));
		}

		String branchids = "";
		// 如果为选择站点则匹配用户权限
		if (branchid == 0) {
			if (branchList != null && !branchList.isEmpty()) {
				for (Branch listb : branchList) {
					branchids += listb.getBranchid() + ",";
				}
			}
			if (!"".equals(branchids)) {
				branchids = branchids.substring(0, branchids.lastIndexOf(","));
			}
		} else {
			branchids = String.valueOf(branchid);
		}
		model.addAttribute("summaryList", accountCwbSummaryService.getAccountCwbSummaryListByBranchAndUser(branchList, userList, page, checkoutstate, branchids, 2, starttime, endtime));
		model.addAttribute("page_obj", new Page(accountCwbSummaryDAO.getAccountCwbSummaryCount(checkoutstate, branchids, 2, starttime, endtime), page, Page.ONE_PAGE_NUMBER));
		model.addAttribute("page", page);
		model.addAttribute("branchList", branchList);
		return "/accountcwbdetail/deliveryCheck";
	}

	/**
	 * 后付：得到一条汇总信息(根据summaryid)
	 * 
	 * @param model
	 * @param summaryid
	 * @param request
	 * @return
	 */
	@RequestMapping("/getDelivery/{summaryid}")
	public String getDelivery(Model model, @PathVariable("summaryid") long summaryid, HttpServletRequest request) {
		AccountCwbSummary accountCwbSummary = new AccountCwbSummary();
		accountCwbSummary = accountCwbSummaryDAO.getAccountCwbSummaryById(summaryid);
		model.addAttribute("accountCwbSummary", accountCwbSummary);
		return "/accountcwbdetail/deliverySummary";
	}

	/**
	 * 后付:订单明细(根据汇总id)
	 * 
	 * @param model
	 * @param page
	 * @param ids
	 * @param state
	 * @return
	 */
	@RequestMapping("/deliverycwbs/{page}")
	public String deliveryCwbs(Model model, @PathVariable("page") long page, @RequestParam("summaryid") long summaryid,
			@RequestParam(value = "flowOrderType", required = false, defaultValue = "") long flowOrderType) {
		List<AccountCwbDetail> list = new ArrayList<AccountCwbDetail>();
		long total = 0;
		String flowtypeId = String.valueOf(AccountFlowOrderTypeEnum.GuiBanShenHe.getValue());
		AccountCwbDetail accountCwbDetail = new AccountCwbDetail();
		// 欠款
		if (flowOrderType == AccountTongjiEnum.QianKuan.getValue()) {
			list = accountCwbDetailDAO.getAccountCwbDetailByQK(page, flowtypeId, summaryid);
			total = accountCwbDetailDAO.getCountAccountCwbDetailByQK(flowtypeId, summaryid);
			accountCwbDetail = accountCwbDetailDAO.getDetailByQKSum(flowtypeId, summaryid);
		}
		// 已交
		if (flowOrderType == AccountTongjiEnum.YingJiao.getValue()) {
			list = accountCwbDetailDAO.getAccountCwbDetailByType(page, flowtypeId, summaryid);
			total = accountCwbDetailDAO.getCountAccountCwbDetailByType(flowtypeId, summaryid);
			accountCwbDetail = accountCwbDetailDAO.getDetailByTypeSum(flowtypeId, summaryid);
		}
		// 未交
		if (flowOrderType == AccountTongjiEnum.WeiJiao.getValue()) {
			list = accountCwbDetailDAO.getAccountCwbDetailByWj(page, flowtypeId, summaryid);
			total = accountCwbDetailDAO.getCountAccountCwbDetailByWj(flowtypeId, summaryid);
			accountCwbDetail = accountCwbDetailDAO.getDetailByWjSum(flowtypeId, summaryid);
		}

		// 总计应交
		if (flowOrderType == AccountTongjiEnum.ToYingJiao.getValue()) {
			list = accountCwbDetailDAO.getAccountCwbDetailByTo(page, flowtypeId, summaryid);
			total = accountCwbDetailDAO.getCountAccountCwbDetailByTo(flowtypeId, summaryid);
			accountCwbDetail = accountCwbDetailDAO.getDetailByToSum(flowtypeId, summaryid);
		}
		model.addAttribute("detailList", list);
		model.addAttribute("page_obj", new Page(total, page, Page.ONE_PAGE_NUMBER));
		model.addAttribute("page", page);
		model.addAttribute("total", total);
		model.addAttribute("accountCwbDetail", accountCwbDetail);
		return "/accountcwbdetail/deliveryCwbs";
	}

	/**
	 * 先付：导出Excel(根据ids)
	 * 
	 * @param model
	 * @param response
	 * @param request
	 * @param ids
	 */
	@RequestMapping("/exportOutwarehouseDetail")
	public void exportOutwarehouseDetail(Model model, HttpServletResponse response, HttpServletRequest request, @RequestParam(value = "ids", required = false, defaultValue = "") String ids) {
		String[] cloumnName1 = new String[7]; // 导出的列名
		String[] cloumnName2 = new String[7]; // 导出的英文列名
		exportService.SetAccountOutwarehouseFields(cloumnName1, cloumnName2);
		final String[] cloumnName = cloumnName1;
		final String[] cloumnName3 = cloumnName2;
		final HttpServletRequest request1 = request;
		String sheetName = "订单信息"; // sheet的名称
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
		String fileName = "Complaint_" + df.format(new Date()) + ".xlsx"; // 文件名
		try {
			List<AccountCwbDetail> clist = accountCwbDetailDAO.getAccountCwbByDeliveryList(ids);
			final List<AccountCwbDetailView> list = accountCwbDetailService.getViewByOutwarehouse(clist);
			ExcelUtils excelUtil = new ExcelUtils() { // 生成工具类实例，并实现填充数据的抽象方法
				@Override
				public void fillData(Sheet sheet, CellStyle style) {
					for (int k = 0; k < list.size(); k++) {
						Row row = sheet.createRow(k + 1);
						row.setHeightInPoints((float) 15);
						for (int i = 0; i < cloumnName.length; i++) {
							Cell cell = row.createCell((short) i);
							cell.setCellStyle(style);
							Object a = null;
							// 给导出excel赋值
							a = exportService.setAccountDelivery(cloumnName3, request1, list, a, i, k);
							cell.setCellValue(a == null ? "" : a.toString());
						}
					}
				}
			};
			excelUtil.excel(response, cloumnName, sheetName, fileName);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 先付:导出Excel(根据summaryid)
	 * 
	 * @param model
	 * @param response
	 * @param branchid
	 * @param selectDS
	 * @param request
	 */
	@RequestMapping("/exportByOutwarehouse")
	public void exportByOutwarehouse(Model model, HttpServletResponse response, @RequestParam("summaryid") long summaryid,
			@RequestParam(value = "flowOrderType", required = false, defaultValue = "") long flowOrderType, HttpServletRequest request) {
		String[] cloumnName1 = new String[7]; // 导出的列名
		String[] cloumnName2 = new String[7]; // 导出的英文列名

		exportService.SetAccountOutwarehouseFields(cloumnName1, cloumnName2);
		final String[] cloumnName = cloumnName1;
		final String[] cloumnName3 = cloumnName2;
		final HttpServletRequest request1 = request;
		String sheetName = "应交货款"; // sheet的名称
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
		String fileName = "Complaint_" + df.format(new Date()) + ".xlsx"; // 文件名

		try {
			// 查询出数据
			String chukuIds = AccountFlowOrderTypeEnum.KuFangChuKu.getValue() + "," + AccountFlowOrderTypeEnum.ZhongZhuanChuKu.getValue() + "," + AccountFlowOrderTypeEnum.TuiHuoChuKu.getValue();
			List<AccountCwbDetail> clist = new ArrayList<AccountCwbDetail>();
			// 中转
			if (flowOrderType == AccountFlowOrderTypeEnum.ZhongZhuanRuKu.getValue()) {
				String zzId = AccountFlowOrderTypeEnum.ZhongZhuanRuKu.getValue() + "," + AccountFlowOrderTypeEnum.GaiZhanChongKuan.getValue();
				clist = accountCwbDetailDAO.getAccountCwbDetailByType(0, zzId, summaryid);
			}
			// 退货
			if (flowOrderType == AccountFlowOrderTypeEnum.TuiHuoRuKu.getValue() || flowOrderType == AccountFlowOrderTypeEnum.Pos.getValue()) {
				clist = accountCwbDetailDAO.getAccountCwbDetailByType(0, String.valueOf(flowOrderType), summaryid);
			}
			// 欠款
			if (flowOrderType == AccountTongjiEnum.QianKuan.getValue()) {
				clist = accountCwbDetailDAO.getAccountCwbDetailByQK(0, chukuIds, summaryid);
			}
			// 已交
			if (flowOrderType == AccountTongjiEnum.YingJiao.getValue()) {
				clist = accountCwbDetailDAO.getAccountCwbDetailByType(0, chukuIds, summaryid);
			}
			// 未交
			if (flowOrderType == AccountTongjiEnum.WeiJiao.getValue()) {
				clist = accountCwbDetailDAO.getAccountCwbDetailByWj(0, chukuIds, summaryid);
			}
			// 总计应交
			if (flowOrderType == AccountTongjiEnum.ToYingJiao.getValue()) {
				clist = accountCwbDetailDAO.getAccountCwbDetailByTo(0, chukuIds, summaryid);
			}
			final List<AccountCwbDetailView> list = accountCwbDetailService.getViewByOutwarehouse(clist);
			ExcelUtils excelUtil = new ExcelUtils() { // 生成工具类实例，并实现填充数据的抽象方法
				@Override
				public void fillData(Sheet sheet, CellStyle style) {
					for (int k = 0; k < list.size(); k++) {
						Row row = sheet.createRow(k + 1);
						row.setHeightInPoints((float) 15);
						for (int i = 0; i < cloumnName.length; i++) {
							Cell cell = row.createCell((short) i);
							cell.setCellStyle(style);
							Object a = null;
							// 给导出excel赋值
							a = exportService.setAccountDelivery(cloumnName3, request1, list, a, i, k);
							cell.setCellValue(a == null ? "" : a.toString());
						}
					}
				}
			};
			excelUtil.excel(response, cloumnName, sheetName, fileName);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 后付：导出Excel(根据汇总summaryid)
	 * 
	 * @param model
	 * @param response
	 * @param branchid
	 * @param selectDS
	 * @param request
	 */
	@RequestMapping("/exportByDelivery")
	public void exportByDelivery(Model model, HttpServletResponse response, @RequestParam("summaryid") long summaryid,
			@RequestParam(value = "flowOrderType", required = false, defaultValue = "") long flowOrderType, HttpServletRequest request) {
		String[] cloumnName1 = new String[9]; // 导出的列名
		String[] cloumnName2 = new String[9]; // 导出的英文列名

		exportService.SetAccountDeliveryFields(cloumnName1, cloumnName2);
		final String[] cloumnName = cloumnName1;
		final String[] cloumnName3 = cloumnName2;
		final HttpServletRequest request1 = request;
		String sheetName = "已妥投交款"; // sheet的名称
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
		String fileName = "Complaint_" + df.format(new Date()) + ".xlsx"; // 文件名
		try {
			// 查询出数据
			String flowtypeId = String.valueOf(AccountFlowOrderTypeEnum.GuiBanShenHe.getValue());

			List<AccountCwbDetail> clist = new ArrayList<AccountCwbDetail>();
			// 欠款
			if (flowOrderType == AccountTongjiEnum.QianKuan.getValue()) {
				clist = accountCwbDetailDAO.getAccountCwbDetailByQK(0, flowtypeId, summaryid);
			}
			// 已交
			if (flowOrderType == AccountTongjiEnum.YingJiao.getValue()) {
				clist = accountCwbDetailDAO.getAccountCwbDetailByType(0, flowtypeId, summaryid);
			}
			// 未交
			if (flowOrderType == AccountTongjiEnum.WeiJiao.getValue()) {
				clist = accountCwbDetailDAO.getAccountCwbDetailByWj(0, flowtypeId, summaryid);
			}
			// 总计应交
			if (flowOrderType == AccountTongjiEnum.ToYingJiao.getValue()) {
				clist = accountCwbDetailDAO.getAccountCwbDetailByTo(0, flowtypeId, summaryid);
			}

			final List<AccountCwbDetailView> list = accountCwbDetailService.getViewByDelivery(clist);
			ExcelUtils excelUtil = new ExcelUtils() { // 生成工具类实例，并实现填充数据的抽象方法
				@Override
				public void fillData(Sheet sheet, CellStyle style) {
					for (int k = 0; k < list.size(); k++) {
						Row row = sheet.createRow(k + 1);
						row.setHeightInPoints((float) 15);
						for (int i = 0; i < cloumnName.length; i++) {
							Cell cell = row.createCell((short) i);
							cell.setCellStyle(style);
							Object a = null;
							// 给导出excel赋值
							a = exportService.setAccountDelivery(cloumnName3, request1, list, a, i, k);
							cell.setCellValue(a == null ? "" : a.toString());
						}
					}
				}
			};
			excelUtil.excel(response, cloumnName, sheetName, fileName);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 配送结果结算记录导出Excel
	 * 
	 * @param model
	 */
	@RequestMapping("/exportByDeliverySummary")
	public void exportByDeliverySummary(Model model, HttpServletResponse response, @RequestParam(value = "branchidExcel", required = false, defaultValue = "0") long branchid,
			@RequestParam(value = "checkoutstateExcel", required = false, defaultValue = "0") long checkoutstate,
			@RequestParam(value = "starttimeExcel", required = false, defaultValue = "") String starttime, @RequestParam(value = "endtimeExcel", required = false, defaultValue = "") String endtime,
			HttpServletRequest request) {
		List<User> userList = userDAO.getAllUser();
		List<Branch> branchList = branchDAO.getBranchByBranchidAccounttype(getSessionUser().getBranchid(), 2, String.valueOf(BranchEnum.ZhanDian.getValue()), getSessionUser().getUserid());
		Branch userbranch = branchDAO.getBranchByBranchid(getSessionUser().getBranchid());
		// 判断当前操作用户类型为站点
		if (userbranch.getSitetype() == BranchEnum.ZhanDian.getValue()) {
			branchList.add(branchDAO.getBranchByBranchid(getSessionUser().getBranchid()));
		}
		String branchids = "";
		// 如果为选择站点则匹配用户权限
		if (branchid == 0) {
			if (branchList != null && !branchList.isEmpty()) {
				for (Branch listb : branchList) {
					branchids += listb.getBranchid() + ",";
				}
			}
			if (!"".equals(branchids)) {
				branchids = branchids.substring(0, branchids.lastIndexOf(","));
			}
		} else {
			branchids = String.valueOf(branchid);
		}

		String[] cloumnName1 = new String[26]; // 导出的列名
		String[] cloumnName2 = new String[26]; // 导出的英文列名
		exportService.SetAccountDeliverySummaryFields(cloumnName1, cloumnName2);
		final String[] cloumnName = cloumnName1;
		final String[] cloumnName3 = cloumnName2;
		final HttpServletRequest request1 = request;
		String sheetName = "配送结果结算记录"; // sheet的名称
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
		String fileName = "Complaint_" + df.format(new Date()) + ".xlsx"; // 文件名
		try {
			final List<AccountCwbSummary> list = accountCwbSummaryService.getAccountCwbSummaryListByBranchAndUser(branchList, userList, -1, checkoutstate, branchids, 2, starttime, endtime);
			ExcelUtils excelUtil = new ExcelUtils() { // 生成工具类实例，并实现填充数据的抽象方法
				@Override
				public void fillData(Sheet sheet, CellStyle style) {
					for (int k = 0; k < list.size(); k++) {
						Row row = sheet.createRow(k + 1);
						row.setHeightInPoints((float) 15);
						for (int i = 0; i < cloumnName.length; i++) {
							Cell cell = row.createCell((short) i);
							cell.setCellStyle(style);
							Object a = null;
							// 给导出excel赋值
							a = exportService.setAccountCwbSummary(cloumnName3, request1, list, a, i, k);
							cell.setCellValue(a == null ? "" : a.toString());
						}
					}
				}
			};
			excelUtil.excel(response, cloumnName, sheetName, fileName);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

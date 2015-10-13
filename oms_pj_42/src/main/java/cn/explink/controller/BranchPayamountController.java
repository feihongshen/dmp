package cn.explink.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import cn.explink.dao.BranchDAO;
import cn.explink.dao.BranchPayamountDAO;
import cn.explink.dao.CustomerDAO;
import cn.explink.dao.CwbDAO;
import cn.explink.dao.GetDmpDAO;
import cn.explink.dao.OrderFlowDAO;
import cn.explink.dao.PayantDAO;
import cn.explink.domain.Branch;
import cn.explink.domain.BranchPayamount;
import cn.explink.domain.Customer;
import cn.explink.domain.CwbOrder;
import cn.explink.domain.DeliveryState;
import cn.explink.enumutil.BranchEnum;
import cn.explink.service.BranchPayamountService;
import cn.explink.util.DateDayUtil;
import cn.explink.util.Page;

@RequestMapping("/funds")
@Controller
public class BranchPayamountController {

	@Autowired
	BranchPayamountDAO branchPayamountDao;
	@Autowired
	BranchDAO branchDao;
	@Autowired
	PayantDAO payantDAO;
	@Autowired
	OrderFlowDAO orderFlowDAO;
	@Autowired
	GetDmpDAO getDmpDAO;
	@Autowired
	BranchPayamountService branchPayamountService;
	@Autowired
	CustomerDAO customerDao;
	@Autowired
	CwbDAO cwbDAO;
	@Autowired
	SecurityContextHolderStrategy securityContextHolderStrategy;
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

	@RequestMapping("/paymentCheack")
	public String list(Model model, @RequestParam(value = "branchid", required = false, defaultValue = "-1") long branchid,
			@RequestParam(value = "strateBranchpaydatetime", required = false, defaultValue = "") String strateBranchpaydatetime,
			@RequestParam(value = "endBranchpaydatetime", required = false, defaultValue = "") String endBranchpaydatetime,
			@RequestParam(value = "strateDeliverpaydate", required = false, defaultValue = "") String strateDeliverpaydate,
			@RequestParam(value = "endDeliverpaydate", required = false, defaultValue = "") String endDeliverpaydate,
			@RequestParam(value = "paytype", required = false, defaultValue = "-1") long paytype, @RequestParam(value = "flag", required = false, defaultValue = "-1") long flag,
			@RequestParam(value = "fromType", required = false, defaultValue = "-1") long fromType, @RequestParam(value = "upstate", required = false, defaultValue = "0") int upstate,
			HttpServletResponse response, HttpServletRequest request) {
		// 查询当前站点
		String dmpid = request.getSession().getAttribute("dmpid") == null ? "" : request.getSession().getAttribute("dmpid").toString();
		long branchidSession = getDmpDAO.getNowBrancheId(dmpid);
		setModels(model, branchid, strateBranchpaydatetime, endBranchpaydatetime, strateDeliverpaydate, endDeliverpaydate, paytype, flag, fromType, upstate, branchidSession);
		setSetions(request, branchid, strateBranchpaydatetime, endBranchpaydatetime, strateDeliverpaydate, endDeliverpaydate, paytype, flag, fromType, upstate, branchidSession);
		return "funds/payamount/view";
	}

	// <td align="center"
	// valign="middle"><%=b.getPaywayname().equals("1")?"银行转账":"现金" %></td>
	// <td align="center"
	// valign="middle"><%=b.getPayproveid()==null?"":b.getPayproveid() %></td>
	@RequestMapping("/update")
	public String update(Model model, @RequestParam(value = "controlStr", required = false, defaultValue = "") String controlStr,
			@RequestParam(value = "mackStr", required = false, defaultValue = "") String mackStr, @RequestParam(value = "upstate", required = false, defaultValue = "0") int upstate,
			HttpServletRequest request) {

		String strateBranchpaydatetime = request.getSession().getAttribute("strateBranchpaydatetime").toString();
		String endBranchpaydatetime = request.getSession().getAttribute("endBranchpaydatetime").toString();
		String strateDeliverpaydate = request.getSession().getAttribute("strateDeliverpaydate").toString();
		String endDeliverpaydate = request.getSession().getAttribute("endDeliverpaydate").toString();
		long branchid = new Long(request.getSession().getAttribute("branchid").toString());
		long paytype = new Long(request.getSession().getAttribute("paytype").toString());
		long flag = new Long(request.getSession().getAttribute("flag").toString());
		long fromType = new Long(request.getSession().getAttribute("fromType").toString());
		long branchidSession = new Long(request.getSession().getAttribute("branchidSession").toString());
		String[] mStr = null;
		String dmpid = request.getSession().getAttribute("dmpid") == null ? "" : request.getSession().getAttribute("dmpid").toString();
		if (controlStr.length() > 0) {
			String[] conStr = controlStr.split(";");
			if (mackStr != null && mackStr.length() > 0 && mackStr.indexOf("P_P") > -1) {
				mStr = mackStr.split("P_P");
			}
			for (int i = 0; i < conStr.length; i++) {
				branchPayamountService.updatePay(conStr[i], mStr == null ? "" : mStr[i], dmpid);
			}

		}
		setModels(model, branchid, strateBranchpaydatetime, endBranchpaydatetime, strateDeliverpaydate, endDeliverpaydate, paytype, flag, fromType, upstate, branchidSession);
		return "funds/payamount/view";
	}

	@RequestMapping("/back")
	public String back(Model model, HttpServletRequest request) {

		String strateBranchpaydatetime = request.getSession().getAttribute("strateBranchpaydatetime").toString();
		String endBranchpaydatetime = request.getSession().getAttribute("endBranchpaydatetime").toString();
		String strateDeliverpaydate = request.getSession().getAttribute("strateDeliverpaydate").toString();
		String endDeliverpaydate = request.getSession().getAttribute("endDeliverpaydate").toString();
		long branchid = new Long(request.getSession().getAttribute("branchid").toString());
		long paytype = new Long(request.getSession().getAttribute("paytype").toString());
		long flag = new Long(request.getSession().getAttribute("flag").toString());
		long fromType = new Long(request.getSession().getAttribute("fromType").toString());
		int upstate = new Integer(request.getSession().getAttribute("upstate").toString());
		long branchidSession = new Long(request.getSession().getAttribute("branchidSession").toString());

		setModels(model, branchid, strateBranchpaydatetime, endBranchpaydatetime, strateDeliverpaydate, endDeliverpaydate, paytype, flag, fromType, upstate, branchidSession);
		return "funds/payamount/view";
	}

	@RequestMapping("/paymentShow/{page}")
	public String show(Model model, @PathVariable("page") long page, @RequestParam(value = "controlStr") String controlStr, HttpServletRequest request) {
		String dmpid = request.getSession().getAttribute("dmpid") == null ? "" : request.getSession().getAttribute("dmpid").toString();
		int showphoneflag = getDmpDAO.getNowUserShowPhoneFlag(dmpid);
		model.addAttribute("usershowphoneflag", showphoneflag);
		List<CwbOrder> list = branchPayamountService.show(controlStr);
		model.addAttribute("cwborderList", branchPayamountService.show(controlStr));
		model.addAttribute("page_obj", new Page(branchPayamountService.getCount(controlStr), page, Page.ONE_PAGE_NUMBER));
		model.addAttribute("page", page);
		model.addAttribute("CwbOrderList", list);
		model.addAttribute("controlStr", controlStr);
		return "funds/payamount/show";
	}

	@RequestMapping("/deliveryStateShow/{controlStr}/{page}")
	public String showDeliveryState(Model model, @PathVariable("controlStr") String controlStr, @PathVariable("page") long page, HttpServletRequest request) {
		String dmpid = request.getSession().getAttribute("dmpid") == null ? "" : request.getSession().getAttribute("dmpid").toString();
		int showphoneflag = getDmpDAO.getNowUserShowPhoneFlag(dmpid);
		model.addAttribute("usershowphoneflag", showphoneflag);

		String goClassid = branchPayamountService.getGoClassIds(controlStr);
		String deliveryIds = branchPayamountService.getDeliveIds(goClassid);
		model.addAttribute("deliveryStatelist", branchPayamountService.showDeliveryState(deliveryIds, page));
		model.addAttribute("page_obj", new Page(branchPayamountService.getDeliveryCountStr(deliveryIds), page, Page.ONE_PAGE_NUMBER));
		model.addAttribute("page", page);
		model.addAttribute("controlStr", controlStr);
		request.getSession().setAttribute("controlStr", controlStr);
		return "funds/payamount/DeliveryView";
	}

	/**
	 * 查询各款项 未审核的
	 * 
	 * @param model
	 * @param controlStr
	 * @param page
	 * @param request
	 * @return
	 */
	@RequestMapping("/deliveryStateTypeShow/{controlStr}/{type}/{page}")
	public String deliveryStateTypeShow(Model model, @PathVariable("controlStr") String controlStr, @PathVariable("type") long type, @PathVariable("page") long page, HttpServletRequest request) {
		String dmpid = request.getSession().getAttribute("dmpid") == null ? "" : request.getSession().getAttribute("dmpid").toString();
		int showphoneflag = getDmpDAO.getNowUserShowPhoneFlag(dmpid);
		model.addAttribute("usershowphoneflag", showphoneflag);

		String goClassid = branchPayamountService.getGoClassIds(controlStr);
		String deliveryIds = branchPayamountService.getDeliveIds(goClassid);
		model.addAttribute("deliveryStatelist", branchPayamountService.showDeliveryStateType(deliveryIds, type, page));
		model.addAttribute("page_obj", new Page(branchPayamountService.getDeliveryCountStrType(deliveryIds, type), page, Page.ONE_PAGE_NUMBER));
		model.addAttribute("page", page);
		model.addAttribute("controlStr", controlStr);
		model.addAttribute("type", type);
		request.getSession().setAttribute("controlStr", controlStr);
		request.getSession().setAttribute("type", type);
		return "funds/payamount/DeliveryView";
	}

	@RequestMapping("/deliverybackStateShow/{controlStr}/{page}")
	public String showDeliverybackState(Model model, @PathVariable("controlStr") String controlStr, @PathVariable("page") long page, HttpServletRequest request) {
		String dmpid = request.getSession().getAttribute("dmpid") == null ? "" : request.getSession().getAttribute("dmpid").toString();
		int showphoneflag = getDmpDAO.getNowUserShowPhoneFlag(dmpid);
		model.addAttribute("usershowphoneflag", showphoneflag);

		String goClassid = "";
		goClassid = branchPayamountService.getBackGoClassIds(controlStr);
		String deliveryIds = "";
		if (!goClassid.equals("")) {
			deliveryIds = branchPayamountService.getDeliveIds(goClassid);
		}

		List<DeliveryState> list = null;
		long count = 0;
		if (!"".equals(deliveryIds)) {
			list = branchPayamountService.showDeliveryState(deliveryIds, page);
			count = branchPayamountService.getDeliveryCountStr(deliveryIds);
		}
		model.addAttribute("deliveryStatelist", list);
		model.addAttribute("page_obj", new Page(count, page, Page.ONE_PAGE_NUMBER));
		model.addAttribute("page", page);
		model.addAttribute("controlStr", controlStr);
		request.getSession().setAttribute("controlStr", controlStr);
		return "funds/payamount/DeliverybackView";
	}

	/**
	 * 查询各款项 已审核的
	 * 
	 * @param model
	 * @param controlStr
	 * @param page
	 * @param request
	 * @return
	 */
	@RequestMapping("/deliverybackStateTypeShow/{controlStr}/{type}/{page}")
	public String deliverybackStateTypeShow(Model model, @PathVariable("controlStr") String controlStr, @PathVariable("type") long type, @PathVariable("page") long page, HttpServletRequest request) {
		String dmpid = request.getSession().getAttribute("dmpid") == null ? "" : request.getSession().getAttribute("dmpid").toString();
		int showphoneflag = getDmpDAO.getNowUserShowPhoneFlag(dmpid);
		model.addAttribute("usershowphoneflag", showphoneflag);

		String goClassid = "";
		goClassid = branchPayamountService.getBackGoClassIds(controlStr);
		String deliveryIds = "";
		if (!goClassid.equals("")) {
			deliveryIds = branchPayamountService.getDeliveIds(goClassid);
		}

		List<DeliveryState> list = null;
		long count = 0;
		if (!"".equals(deliveryIds)) {
			list = branchPayamountService.showDeliveryStateType(deliveryIds, type, page);
			count = branchPayamountService.getDeliveryCountStrType(deliveryIds, type);
		}
		model.addAttribute("deliveryStatelist", list);
		model.addAttribute("page_obj", new Page(count, page, Page.ONE_PAGE_NUMBER));
		model.addAttribute("page", page);
		model.addAttribute("controlStr", controlStr);
		model.addAttribute("type", type);
		request.getSession().setAttribute("controlStr", controlStr);
		request.getSession().setAttribute("type", type);
		return "funds/payamount/DeliverybackView";
	}

	@RequestMapping("/showExp/{page}")
	public void showDeliveryStateExp(Model model, @PathVariable("page") long page, HttpServletResponse response, HttpServletRequest request) {
		String controlStr = request.getSession().getAttribute("controlStr") == null ? "" : request.getSession().getAttribute("controlStr").toString();
		long type = request.getSession().getAttribute("type") == null ? -1 : Long.parseLong(request.getSession().getAttribute("type").toString());
		String dmpid = request.getSession().getAttribute("dmpid") == null ? "" : request.getSession().getAttribute("dmpid").toString();
		int showphoneflag = getDmpDAO.getNowUserShowPhoneFlag(dmpid);
		model.addAttribute("usershowphoneflag", showphoneflag);
		String goClassid = branchPayamountService.getGoClassIds(controlStr);
		String deliveryIds = branchPayamountService.getDeliveIds(goClassid);
		// model.addAttribute("deliveryStatelist",
		// branchPayamountService.showDeliveryState(deliveryIds,-1));
		branchPayamountService.getCwbDetailByFlowOrder_payment(response, branchPayamountService.showDeliveryStateType(deliveryIds, type, -1), showphoneflag);
	}

	@RequestMapping("/showbackExp/{page}")
	public void showDeliverybackStateExp(Model model, @PathVariable("page") long page, HttpServletResponse response, HttpServletRequest request) {
		String controlStr = request.getSession().getAttribute("controlStr") == null ? "" : request.getSession().getAttribute("controlStr").toString();
		long type = request.getSession().getAttribute("type") == null ? -1 : Long.parseLong(request.getSession().getAttribute("type").toString());
		String dmpid = request.getSession().getAttribute("dmpid") == null ? "" : request.getSession().getAttribute("dmpid").toString();
		int showphoneflag = getDmpDAO.getNowUserShowPhoneFlag(dmpid);
		model.addAttribute("usershowphoneflag", showphoneflag);
		String goClassid = branchPayamountService.getBackGoClassIds(controlStr);
		String deliveryIds = branchPayamountService.getDeliveIds(goClassid);
		// model.addAttribute("deliveryStatelist",
		// branchPayamountService.showDeliveryState(deliveryIds,-1));
		branchPayamountService.getCwbDetailByFlowOrder_payment(response, branchPayamountService.showDeliveryStateType(deliveryIds, type, -1), showphoneflag);
	}

	@RequestMapping("/paymentShowExp")
	public String showExp(Model model, @RequestParam(value = "controlStr") String controlStr, HttpServletRequest request) {
		String dmpid = request.getSession().getAttribute("dmpid") == null ? "" : request.getSession().getAttribute("dmpid").toString();
		int showphoneflag = getDmpDAO.getNowUserShowPhoneFlag(dmpid);
		model.addAttribute("usershowphoneflag", showphoneflag);
		List<CwbOrder> list = branchPayamountService.show(controlStr);
		model.addAttribute("cwborderList", branchPayamountService.show(controlStr));
		model.addAttribute("CwbOrderList", list);
		return "funds/payamount/showExpo";
	}

	private void setModels(Model model, long branchid, String strateBranchpaydatetime, String endBranchpaydatetime, String strateDeliverpaydate, String endDeliverpaydate, long paytype, long flag,
			long fromType, int upstate, long branchidSession) {

		Branch nowBranch = getDmpDAO.getNowBranch(branchidSession); // 获取当前站点的类型
		int branchtype = nowBranch.getSitetype();
		if (branchtype != BranchEnum.CaiWu.getValue()) {
			model.addAttribute("nowbranch", nowBranch);

			model.addAttribute("branchList", null);
			model.addAttribute("nowdayNochackList", null);
			model.addAttribute("nowdayChackList", null);
			model.addAttribute("nowdayNochackSum", 0);
			model.addAttribute("allNochackSum", 0);
			model.addAttribute("upstate", upstate);

		} else {

			List<Branch> branchList = getDmpDAO.getBranchListByCawWu(branchidSession);

			List<BranchPayamount> nowdayNochackList = null;
			List<BranchPayamount> nowdayChackList = null;

			long nowdayNochackSum = 0;
			long allNochackSum = 0;

			String branchidStr = "";
			if (branchid < 0) {
				if (branchList != null && branchList.size() > 0) {
					for (Branch branch : branchList) {
						branchidStr += branch.getBranchid() + ",";
					}
					branchidStr = branchidStr.substring(0, branchidStr.length() - 1);
				} else {
					model.addAttribute("nowbranch", nowBranch);
					model.addAttribute("branchList", null);
					model.addAttribute("nowdayNochackList", null);
					model.addAttribute("nowdayChackList", null);
					model.addAttribute("nowdayNochackSum", 0);
					model.addAttribute("allNochackSum", 0);
					model.addAttribute("upstate", upstate);
					return;
				}
			}

			// List<BranchPayamount> nowdayNochackList1 =
			// branchPayamountDao.getAllNochack(-1, "", "", "", "",
			// upstate,branchidSession);
			List<BranchPayamount> nowdayNochackList2 = branchPayamountDao.getAllNochack(-1, "", "", "", "", 0, branchidSession);
			allNochackSum = nowdayNochackList2 == null ? 0 : nowdayNochackList2.size();
			List<BranchPayamount> allNochackList1 = branchPayamountDao.getAllNochack(-1, sdf.format(new Date()) + " 00:00:00", sdf.format(new Date()) + " 23:59:59", sdf.format(new Date())
					+ " 00:00:00", sdf.format(new Date()) + " 23:59:59", upstate, branchidSession);
			nowdayNochackSum = allNochackList1 == null ? 0 : allNochackList1.size();
			if (flag == 1) {
				nowdayNochackList = branchPayamountDao.getAllNochack(-1, sdf.format(new Date()) + " 00:00:00", sdf.format(new Date()) + " 23:59:59", sdf.format(new Date()) + " 00:00:00",
						sdf.format(new Date()) + " 23:59:59", upstate, branchidSession);
				nowdayChackList = branchPayamountDao.getAllNochack(-1, sdf.format(new Date()) + " 00:00:00", sdf.format(new Date()) + " 23:59:59", sdf.format(new Date()) + " 00:00:00",
						sdf.format(new Date()) + " 23:59:59", upstate, branchidSession);
			} else if (flag == 0) {
				nowdayNochackList = branchPayamountDao.getAllNochack(-1, "", "", "", "", 0, branchidSession);
				nowdayChackList = branchPayamountDao.getAllNochack(-1, sdf.format(new Date()) + " 00:00:00", sdf.format(new Date()) + " 23:59:59", sdf.format(new Date()) + " 00:00:00",
						sdf.format(new Date()) + " 23:59:59", upstate, branchidSession);
			} else {
				if ("".equals(strateBranchpaydatetime) && "".equals(endBranchpaydatetime) && "".equals(strateDeliverpaydate) && "".equals(endDeliverpaydate) && fromType == -1) {
					nowdayNochackList = branchPayamountDao.getAllNochack(branchid, sdf.format(new Date()) + " 00:00:00", sdf.format(new Date()) + " 23:59:59", sdf.format(new Date()) + " 00:00:00",
							sdf.format(new Date()) + " 23:59:59", upstate, branchidSession);
					nowdayChackList = branchPayamountDao.getAllNochack(branchid, sdf.format(new Date()) + " 00:00:00", sdf.format(new Date()) + " 23:59:59", sdf.format(new Date()) + " 00:00:00",
							sdf.format(new Date()) + " 23:59:59", upstate, branchidSession);
				} else {
					nowdayNochackList = branchPayamountDao.getAllNochack(branchid, strateBranchpaydatetime, endBranchpaydatetime, strateDeliverpaydate, endDeliverpaydate, upstate, branchidSession);
					nowdayChackList = branchPayamountDao.getAllNochack(branchid, strateBranchpaydatetime, endBranchpaydatetime, strateDeliverpaydate, endDeliverpaydate, upstate, branchidSession);
				}
			}
			// List<Branch> branchnameList = branchDao.getAllBranches();
			// List<Branch> branchnameList = getDmpDAO.getBranchByZhanDian();
			model.addAttribute("branchList", branchList);
			model.addAttribute("nowdayNochackList", nowdayNochackList);
			model.addAttribute("nowdayChackList", nowdayChackList);
			model.addAttribute("nowdayNochackSum", nowdayNochackSum);
			model.addAttribute("allNochackSum", allNochackSum);
			model.addAttribute("upstate", upstate);
		}

	}

	// 保存基本查询条件的session
	private void setSetions(HttpServletRequest request, long branchid, String strateBranchpaydatetime, String endBranchpaydatetime, String strateDeliverpaydate, String endDeliverpaydate,
			long paytype, long flag, long fromType, int upstate, long branchidSession) {
		request.getSession().setAttribute("branchid", branchid);
		request.getSession().setAttribute("strateBranchpaydatetime", strateBranchpaydatetime);
		request.getSession().setAttribute("endBranchpaydatetime", endBranchpaydatetime);
		request.getSession().setAttribute("strateDeliverpaydate", strateDeliverpaydate);
		request.getSession().setAttribute("endDeliverpaydate", endDeliverpaydate);
		request.getSession().setAttribute("paytype", paytype);
		request.getSession().setAttribute("flag", flag);
		request.getSession().setAttribute("fromType", fromType);
		request.getSession().setAttribute("upstate", upstate);
		request.getSession().setAttribute("branchidSession", branchidSession);
	}

	// //===========货款管理===================、、、、
	@RequestMapping("/payment")
	public String payment(Model model, @RequestParam(value = "emailStartTime", required = false, defaultValue = "") String emailStartTime,
			@RequestParam(value = "eamilEndTime", required = false, defaultValue = "") String eamilEndTime, @RequestParam(value = "customerid", required = false, defaultValue = "-1") long customerid,
			@RequestParam(value = "exportType", required = false, defaultValue = "0") long exportType, HttpServletResponse response, HttpServletRequest request) {

		if (!"1".equals(request.getParameter("isshow"))) {// 搜索的条件
			emailStartTime = (emailStartTime == null || emailStartTime.equals("")) ? (DateDayUtil.getDateBefore("", -6) + " 00:00:00") : emailStartTime;
			eamilEndTime = (eamilEndTime == null || eamilEndTime.equals("")) ? (DateDayUtil.getDateBefore("", 0) + " 23:59:59") : eamilEndTime;
		}

		List<Customer> cumstrListAll = getDmpDAO.getAllCustomers();
		Customer customer = null;
		setSetionsExp(request, emailStartTime, eamilEndTime, customerid, exportType);
		setModelsExp(model, emailStartTime, eamilEndTime, customerid, exportType);

		model.addAttribute("customer", customer);
		model.addAttribute("customerid", customerid);
		model.addAttribute("cumstrListAll", cumstrListAll);

		model.addAttribute("emailStartTime", emailStartTime);
		model.addAttribute("eamilEndTime", eamilEndTime);

		return "funds/payment/paymentMonitor";
	}

	@RequestMapping("/paymentExp/{page}")
	public String paymentExp(Model model, @PathVariable("page") long page, HttpServletResponse response, HttpServletRequest request) {

		String emailStartTime = request.getSession().getAttribute("emailStartTime").toString();
		String eamilEndTime = request.getSession().getAttribute("eamilEndTime").toString();
		long customerid = new Long(request.getSession().getAttribute("customerid").toString());
		long exportType = new Long(request.getSession().getAttribute("exportType").toString());

		setModelsExp(model, emailStartTime, eamilEndTime, customerid, exportType);
		Customer customer = getDmpDAO.getCustomer(customerid);
		model.addAttribute("customer", customer);
		model.addAttribute("emailStartTime", emailStartTime);
		model.addAttribute("eamilEndTime", eamilEndTime);
		int updatecount = payantDAO.CargoAmountStatistics_update(emailStartTime, eamilEndTime, customerid, page);

		return "funds/payment/paymentExpo";
	}

	// //===========退货款管理===================、、、、
	@RequestMapping("/paymentBack")
	public String paymentBack(Model model, @RequestParam(value = "emailStartTime", required = false, defaultValue = "") String emailStartTime,
			@RequestParam(value = "eamilEndTime", required = false, defaultValue = "") String eamilEndTime,
			@RequestParam(value = "startaudittime", required = false, defaultValue = "") String startaudittime,
			@RequestParam(value = "endaudittime", required = false, defaultValue = "") String endaudittime, @RequestParam(value = "customerid", required = false, defaultValue = "-1") long customerid,
			@RequestParam(value = "exportType", required = false, defaultValue = "0") long exportType, HttpServletResponse response, HttpServletRequest request) {

		if (!"1".equals(request.getParameter("isshow"))) {// 搜索的条件
			emailStartTime = (emailStartTime == null || emailStartTime.equals("")) ? (DateDayUtil.getDateBefore("", -6) + " 00:00:00") : emailStartTime;
			eamilEndTime = (eamilEndTime == null || eamilEndTime.equals("")) ? (DateDayUtil.getDateBefore("", 0) + " 23:59:59") : eamilEndTime;
			startaudittime = (startaudittime == null || startaudittime.equals("")) ? (DateDayUtil.getDateBefore("", -6) + " 00:00:00") : startaudittime;
			endaudittime = (endaudittime == null || endaudittime.equals("")) ? (DateDayUtil.getDateBefore("", 0) + " 23:59:59") : endaudittime;
		}

		List<Customer> cumstrListAll = getDmpDAO.getAllCustomers();
		setbackSetionsExp(request, emailStartTime, eamilEndTime, customerid, exportType, startaudittime, endaudittime);
		setModelsExpBack(model, emailStartTime, eamilEndTime, customerid, exportType, startaudittime, endaudittime);

		model.addAttribute("customerid", customerid);
		model.addAttribute("cumstrListAll", cumstrListAll);

		model.addAttribute("emailStartTime", emailStartTime);
		model.addAttribute("eamilEndTime", eamilEndTime);

		model.addAttribute("startaudittime", startaudittime);
		model.addAttribute("endaudittime", endaudittime);

		return "funds/payment/paymentBackMonitor";
	}

	@RequestMapping("/paymentBackExp/{page}")
	public String paymentBackExp(Model model, @PathVariable("page") long page, HttpServletResponse response, HttpServletRequest request) {

		String emailStartTime = request.getSession().getAttribute("emailStartTime").toString();
		String eamilEndTime = request.getSession().getAttribute("eamilEndTime").toString();
		long customerid = new Long(request.getSession().getAttribute("customerid").toString());
		long exportType = new Long(request.getSession().getAttribute("exportType").toString());

		setModelsExp(model, emailStartTime, eamilEndTime, customerid, exportType);
		Customer customer = getDmpDAO.getCustomer(customerid);
		model.addAttribute("customer", customer);
		model.addAttribute("emailStartTime", emailStartTime);
		model.addAttribute("eamilEndTime", eamilEndTime);
		int updatecount = payantDAO.CargoAmountStatisticsBack_update(emailStartTime, eamilEndTime, customerid, page);
		return "funds/payment/paymentBackExpo";
	}

	@RequestMapping("/searchdetail/{type}/{customerid}/{page}")
	public String searchdetail(Model model, @PathVariable("type") long type, @PathVariable("customerid") long customerid, @PathVariable("page") long page, HttpServletRequest request) {
		String dmpid = request.getSession().getAttribute("dmpid") == null ? "" : request.getSession().getAttribute("dmpid").toString();
		int showphoneflag = getDmpDAO.getNowUserShowPhoneFlag(dmpid);
		model.addAttribute("usershowphoneflag", showphoneflag);

		String emailStartTime = (String) request.getSession().getAttribute("emailStartTime");
		String eamilEndTime = (String) request.getSession().getAttribute("eamilEndTime");
		List<CwbOrder> cwbdetaillist = branchPayamountService.searchDetailByFlowType(type, customerid, page, emailStartTime, eamilEndTime);
		model.addAttribute("cwbdetaillist", cwbdetaillist);
		request.getSession().setAttribute("flowordertype", type);
		request.getSession().setAttribute("customerid", customerid);
		model.addAttribute("page_obj", new Page(cwbDAO.searchCountFlowOrdertype(type, emailStartTime, eamilEndTime, customerid, page), page, Page.ONE_PAGE_NUMBER));
		return "funds/payment/paymentSearchDetail";
	}

	@RequestMapping("/searchdetail_excel")
	public void paymentDetail_excel(Model model, HttpServletResponse response, HttpServletRequest request) {
		String emailStartTime = request.getSession().getAttribute("emailStartTime").toString();
		String eamilEndTime = request.getSession().getAttribute("eamilEndTime").toString();
		long customerid = new Long(request.getSession().getAttribute("customerid").toString());
		long flowordertype = new Long(request.getSession().getAttribute("flowordertype").toString());

		branchPayamountService.getCwbDetailByFlowOrder_SaveAsExcel(response, flowordertype, emailStartTime, eamilEndTime, customerid);

	}

	@RequestMapping("/searchdetail_back_excel")
	public void paymentDetail_back_excel(Model model, HttpServletResponse response, HttpServletRequest request) {
		String emailStartTime = request.getSession().getAttribute("emailStartTime").toString();
		String eamilEndTime = request.getSession().getAttribute("eamilEndTime").toString();
		String startaudittime = (String) request.getSession().getAttribute("startaudittime");
		String endaudittime = (String) request.getSession().getAttribute("endaudittime");
		long customerid = new Long(request.getSession().getAttribute("customerid").toString());
		String flowordertype = (String) request.getSession().getAttribute("flowordertype");

		branchPayamountService.getCwbDetailByFlowOrder_back_SaveAsExcel(response, flowordertype, emailStartTime, eamilEndTime, customerid, startaudittime, endaudittime);

	}

	@RequestMapping("/payment_return")
	public String payment_return(HttpServletRequest request, Model model) {
		String emailStartTime = request.getSession().getAttribute("emailStartTime").toString();
		String eamilEndTime = request.getSession().getAttribute("eamilEndTime").toString();
		long customerid = new Long(request.getSession().getAttribute("customerid").toString());
		long flowordertype = new Long(request.getSession().getAttribute("flowordertype").toString());
		long exportType = new Long(request.getSession().getAttribute("exportType").toString());
		model.addAttribute("datalist", payantDAO.CargoAmountStatistics(emailStartTime, eamilEndTime, customerid, exportType));

		model.addAttribute("emailStartTime", emailStartTime);
		model.addAttribute("eamilEndTime", eamilEndTime);
		model.addAttribute("flowordertype", flowordertype);
		model.addAttribute("customerid", customerid);
		model.addAttribute("exportType", exportType);
		return "funds/payment/paymentMonitor";
	}

	@RequestMapping("/searchdetail_back/{type}/{customerid}/{page}")
	public String searchdetail_back(Model model, @PathVariable("type") String type, @PathVariable("customerid") long customerid, @PathVariable("page") long page, HttpServletRequest request) {
		String dmpid = request.getSession().getAttribute("dmpid") == null ? "" : request.getSession().getAttribute("dmpid").toString();
		int showphoneflag = getDmpDAO.getNowUserShowPhoneFlag(dmpid);

		String emailStartTime = (String) request.getSession().getAttribute("emailStartTime");
		String eamilEndTime = (String) request.getSession().getAttribute("eamilEndTime");
		String startaudittime = (String) request.getSession().getAttribute("startaudittime");
		String endaudittime = (String) request.getSession().getAttribute("endaudittime");
		List<CwbOrder> cwbdetaillist = branchPayamountService.searchDetailByFlowTypeback(type, customerid, page, emailStartTime, eamilEndTime, startaudittime, endaudittime);
		model.addAttribute("cwbdetaillist", cwbdetaillist);
		request.getSession().setAttribute("flowordertype", type);
		request.getSession().setAttribute("customerid", customerid);
		model.addAttribute("page_obj", new Page(cwbDAO.searchCountFlowOrdertypeback(type, emailStartTime, eamilEndTime, customerid, page, startaudittime, endaudittime), page, Page.ONE_PAGE_NUMBER));
		return "funds/payment/paymentSearchDetail_back";
	}

	@RequestMapping("/payment_back_return")
	public String payment_back_return(HttpServletRequest request, Model model) {
		String emailStartTime = request.getSession().getAttribute("emailStartTime").toString();
		String eamilEndTime = request.getSession().getAttribute("eamilEndTime").toString();
		String startaudittime = request.getSession().getAttribute("startaudittime").toString();
		String endaudittime = request.getSession().getAttribute("endaudittime").toString();
		long customerid = new Long(request.getSession().getAttribute("customerid").toString());
		String flowordertype = request.getSession().getAttribute("flowordertype").toString();
		long exportType = new Long(request.getSession().getAttribute("exportType").toString());
		model.addAttribute("datalist", payantDAO.CargoAmountStatisticsBack(emailStartTime, eamilEndTime, customerid, exportType, startaudittime, endaudittime));

		model.addAttribute("emailStartTime", emailStartTime);
		model.addAttribute("eamilEndTime", eamilEndTime);
		model.addAttribute("startaudittime", startaudittime);
		model.addAttribute("endaudittime", endaudittime);
		model.addAttribute("flowordertype", flowordertype);
		model.addAttribute("customerid", customerid);
		model.addAttribute("exportType", exportType);
		return "funds/payment/paymentBackMonitor";
	}

	// 保存基本查询条件的session
	private void setSetionsExp(HttpServletRequest request, String emailStartTime, String eamilEndTime, long customerid, long exportType) {
		request.getSession().setAttribute("emailStartTime", emailStartTime);
		request.getSession().setAttribute("eamilEndTime", eamilEndTime);
		request.getSession().setAttribute("customerid", customerid);
		request.getSession().setAttribute("exportType", exportType);
	}

	// 保存基本查询条件的session
	private void setbackSetionsExp(HttpServletRequest request, String emailStartTime, String eamilEndTime, long customerid, long exportType, String startaudittime, String endaudittime) {
		request.getSession().setAttribute("emailStartTime", emailStartTime);
		request.getSession().setAttribute("eamilEndTime", eamilEndTime);
		request.getSession().setAttribute("customerid", customerid);
		request.getSession().setAttribute("exportType", exportType);
		request.getSession().setAttribute("startaudittime", startaudittime);
		request.getSession().setAttribute("endaudittime", endaudittime);
	}

	private void setModelsExp(Model model, String emailStartTime, String eamilEndTime, long customerid, long exportType) {
		model.addAttribute("datalist", payantDAO.CargoAmountStatistics(emailStartTime, eamilEndTime, customerid, exportType));
	}

	private void setModelsExpBack(Model model, String emailStartTime, String eamilEndTime, long customerid, long exportType, String startaudittime, String endaudittime) {
		model.addAttribute("datalist", payantDAO.CargoAmountStatisticsBack(emailStartTime, eamilEndTime, customerid, exportType, startaudittime, endaudittime));
	}

}

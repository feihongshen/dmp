package cn.explink.controller;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.explink.dao.AccountCwbFareDetailDAO;
import cn.explink.dao.AppearWindowDao;
import cn.explink.dao.ApplyEditDeliverystateDAO;
import cn.explink.dao.BranchDAO;
import cn.explink.dao.CustomerDAO;
import cn.explink.dao.CwbDAO;
import cn.explink.dao.DeliveryStateDAO;
import cn.explink.dao.ExceptionCwbDAO;
import cn.explink.dao.ReasonDao;
import cn.explink.dao.SystemInstallDAO;
import cn.explink.dao.UserDAO;
import cn.explink.domain.AccountCwbFareDetail;
import cn.explink.domain.ApplyEditDeliverystate;
import cn.explink.domain.Customer;
import cn.explink.domain.CwbOrder;
import cn.explink.domain.DeliveryState;
import cn.explink.domain.EdtiCwb_DeliveryStateDetail;
import cn.explink.domain.Reason;
import cn.explink.domain.SystemInstall;
import cn.explink.domain.User;
import cn.explink.enumutil.ApplyEditDeliverystateIshandleEnum;
import cn.explink.enumutil.FlowOrderTypeEnum;
import cn.explink.enumutil.ReasonTypeEnum;
import cn.explink.exception.CwbException;
import cn.explink.exception.ExplinkException;
import cn.explink.pos.tools.SignTypeEnum;
import cn.explink.service.CwbOrderService;
import cn.explink.service.CwbOrderWithDeliveryState;
import cn.explink.service.EditCwbService;
import cn.explink.service.ExplinkUserDetail;
import cn.explink.util.DateTimeUtil;
import cn.explink.util.Page;

@Controller
@RequestMapping("/applyeditdeliverystate")
public class ApplyEditDeliverystateController {

	@Autowired
	UserDAO userDAO;
	@Autowired
	ApplyEditDeliverystateDAO applyEditDeliverystateDAO;
	@Autowired
	CwbDAO cwbDAO;
	@Autowired
	BranchDAO branchDAO;
	@Autowired
	CustomerDAO customerDAO;
	@Autowired
	DeliveryStateDAO deliveryStateDAO;
	@Autowired
	ReasonDao reasonDAO;
	@Autowired
	CwbOrderService cwborderService;
	@Autowired
	ExceptionCwbDAO exceptionCwbDAO;
	@Autowired
	SecurityContextHolderStrategy securityContextHolderStrategy;
	@Autowired
	AccountCwbFareDetailDAO accountCwbFareDetailDAO;
	@Autowired
	AppearWindowDao appearWindowDao;
	@Autowired
	SystemInstallDAO systemInstallDAO;
	@Autowired
	EditCwbService editCwbService;
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	private User getSessionUser() {
		ExplinkUserDetail userDetail = (ExplinkUserDetail) this.securityContextHolderStrategy.getContext().getAuthentication().getPrincipal();
		return userDetail.getUser();
	}

	/**
	 * 进入申请修改订单配送结果页面
	 * 
	 * @param page
	 * @param model
	 * @param cwb
	 * @return
	 */

	@RequestMapping("/toCreateApplyEditDeliverystate/{page}")
	public String toCreateApplyEditDeliverystate(@PathVariable("page") long page, Model model, @RequestParam(value = "cwb", defaultValue = "", required = false) String cwb) {
		String quot = "'", quotAndComma = "',";

		long count = 0;

		if (cwb.length() > 0) {
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date date = new Date();
			String nowtime = df.format(date);

			StringBuffer cwbs = new StringBuffer();
			StringBuffer errorCwbs = new StringBuffer();

			for (String cwbStr : cwb.split("\r\n")) {
				if (cwbStr.trim().length() == 0) {
					continue;
				}
				// 判断是否符合申请条件：1.未反馈给电商 2.未交款
				DeliveryState deliverystate = this.deliveryStateDAO.getActiveDeliveryStateByCwb(cwbStr);
				if ((deliverystate == null) || (deliverystate.getDeliverystate() == 0)) {
					errorCwbs.append(cwbStr + ":未反馈的订单不能申请修改反馈状态！");
				} else if ((deliverystate != null) && (deliverystate.getPayupid() == 0) && (deliverystate.getIssendcustomer() == 0)) {
					cwbs = cwbs.append(quot).append(cwbStr).append(quotAndComma);
					CwbOrder co = this.cwbDAO.getCwbByCwbLock(cwbStr);
					List<ApplyEditDeliverystate> aedsList = this.applyEditDeliverystateDAO.getApplyEditDeliverystateByCwb(cwbStr, ApplyEditDeliverystateIshandleEnum.WeiChuLi.getValue());
					if ((co != null) && (aedsList.size() == 0)) {
						DeliveryState ds = this.deliveryStateDAO.getDeliveryByCwb(cwbStr);
						ApplyEditDeliverystate aeds = new ApplyEditDeliverystate();
						aeds.setCwb(cwbStr);
						aeds.setOpscwbid(co.getOpscwbid());
						aeds.setDeliverystateid(ds.getId());
						aeds.setCwbordertypeid(co.getCwbordertypeid());
						aeds.setNowdeliverystate(ds.getDeliverystate());
						aeds.setNopos(ds.getCash().add(ds.getCheckfee()).add(ds.getOtherfee()));
						aeds.setPos(ds.getPos());
						aeds.setDeliverid(ds.getDeliveryid());
						aeds.setApplyuserid(this.getSessionUser().getUserid());
						aeds.setApplybranchid(this.getSessionUser().getBranchid());
						aeds.setApplytime(nowtime);
						this.applyEditDeliverystateDAO.creApplyEditDeliverystate(aeds);
					}
				} else {
					if (deliverystate.getPayupid() > 0) {
						errorCwbs.append(cwbStr + ":已上交款不能申请修改反馈状态！");
					} else {
						errorCwbs.append(cwbStr + ":状态已推送给电商不能申请修改反馈状态！");
					}
				}
			}
			String cwbs1 = cwbs.length() > 0 ? cwbs.substring(0, cwbs.length() - 1) : "'--'";
			count = this.cwbDAO.getCwbOrderCwbsCount(cwbs1);

			model.addAttribute("cwbList", this.cwbDAO.getCwbByCwbsPage(page, cwbs1));
			model.addAttribute("applyEditDeliverystateList", this.applyEditDeliverystateDAO.getApplyEditDeliverystateByCwbsPage(page, cwbs1, ApplyEditDeliverystateIshandleEnum.WeiChuLi.getValue()));
			model.addAttribute("branchList", this.branchDAO.getAllEffectBranches());
			model.addAttribute("userList", this.userDAO.getAllUser());
			model.addAttribute("errorCwbs", errorCwbs.toString());
		}
		model.addAttribute("page_obj", new Page(count, page, Page.ONE_PAGE_NUMBER));

		return "applyeditdeliverystate/createApplyEditDeliverystate";
	}

	/**
	 * 提交申请的每个订单的修改后的配送结果和原因
	 * 
	 * @param id
	 * @param model
	 * @param editnowdeliverystate
	 * @param editreason
	 * @return
	 */
	@RequestMapping("/submitCreateApplyEditDeliverystate/{id}")
	public @ResponseBody
	String submitCreateApplyEditDeliverystate(@PathVariable("id") long id, Model model, @RequestParam(value = "editnowdeliverystate", defaultValue = "0", required = true) long editnowdeliverystate,
			@RequestParam(value = "editreason", defaultValue = "", required = true) String editreason) {
		try {
			// 判断是否符合申请条件：1.未反馈给电商 2.未交款
			ApplyEditDeliverystate applyEditDeliverystate = this.applyEditDeliverystateDAO.getApplyEditDeliverystateById(id);
			DeliveryState deliverystate = this.deliveryStateDAO.getActiveDeliveryStateByCwb(applyEditDeliverystate.getCwb());
			if ((deliverystate != null) && (deliverystate.getPayupid() == 0) && (deliverystate.getIssendcustomer() == 0)) {
				this.applyEditDeliverystateDAO.saveApplyEditDeliverystateById(id, editnowdeliverystate, editreason);
				this.appearWindowDao.creWindowTime("订单申请修改cwb=" + applyEditDeliverystate.getCwb(), 5, 0, 1);
				return "{\"errorCode\":0,\"error\":\"提交成功\"}";
			} else {
				if ((deliverystate == null) || (deliverystate.getPayupid() > 0)) {
					return "{\"errorCode\":1,\"error\":\"提交失败,此单已经上交款，不能再申请修改状态\"}";
				} else if ((deliverystate == null) || (deliverystate.getIssendcustomer() > 0)) {
					return "{\"errorCode\":1,\"error\":\"提交失败,此单状态已推送给电商，不能再申请修改状态\"}";
				}
				return "{\"errorCode\":1,\"error\":\"提交失败\"}";
			}
		} catch (Exception e) {

			return "{\"errorCode\":1,\"error\":\"提交失败\"}";
		}
	}

	/**
	 * 修改订单配送结果的查询功能
	 * 
	 * @param page
	 * @param model
	 * @param begindate
	 * @param enddate
	 * @param applybranchid
	 * @param ishandle
	 * @return
	 */
	@RequestMapping("/getApplyEditDeliverystateList/{page}")
	public String getApplyEditDeliverystateList(@PathVariable("page") long page, Model model, @RequestParam(value = "begindate", required = false, defaultValue = "") String begindate,
			@RequestParam(value = "enddate", required = false, defaultValue = "") String enddate, @RequestParam(value = "ishandle", defaultValue = "-1", required = false) long ishandle) {
		boolean isFinancial = false;

		isFinancial = this.IsRole("FinancialID");// 判断是不是财务角色

		model.addAttribute("applyEditDeliverystateList",
				this.applyEditDeliverystateDAO.getApplyEditDeliverystateByWherePage(page, begindate, enddate, this.getSessionUser().getBranchid(), ishandle, "", isFinancial, -1));
		model.addAttribute("branchList", this.branchDAO.getAllEffectBranches());
		model.addAttribute("userList", this.userDAO.getAllUser());
		model.addAttribute("page_obj",
				new Page(this.applyEditDeliverystateDAO.getApplyEditDeliverystateByWhereCount(begindate, enddate, this.getSessionUser().getBranchid(), ishandle, "", isFinancial, -1), page,
						Page.ONE_PAGE_NUMBER));

		return "applyeditdeliverystate/applyEditDeliverystatelist";
	}

	/**
	 * 客服看到的修改订单配送结果的功能
	 * 
	 * @param page
	 * @param model
	 * @param cwb
	 * @param begindate
	 * @param enddate
	 * @param applybranchid
	 * @param ishandle
	 * @return
	 */
	@RequestMapping("/tohandleApplyEditDeliverystateList/{page}")
	public String tohandleApplyEditDeliverystateList(@PathVariable("page") long page, Model model, @RequestParam(value = "cwb", required = false, defaultValue = "") String cwb,
			@RequestParam(value = "begindate", required = false, defaultValue = "") String begindate, @RequestParam(value = "enddate", required = false, defaultValue = "") String enddate,
			@RequestParam(value = "applybranchid", defaultValue = "0", required = false) long applybranchid, @RequestParam(value = "ishandle", defaultValue = "-1", required = false) long ishandle,
			@RequestParam(value = "audit", defaultValue = "-1", required = false) long audit, @RequestParam(value = "isnow", defaultValue = "0", required = false) long isnow) {
		boolean isFinancial = false;
		boolean isService = false;

		isFinancial = this.IsRole("FinancialID");// 判断是不是财务角色
		isService = this.IsRole("ServiceID");// 判断是不是客服角色
		model.addAttribute("isFinancial", isFinancial);
		model.addAttribute("isService", isService);
		List<ApplyEditDeliverystate> applyEditDeliverystates = new ArrayList<ApplyEditDeliverystate>();
		Page pageobj = new Page();
		if (isnow > 0) {
			applyEditDeliverystates = this.applyEditDeliverystateDAO.getApplyEditDeliverystateByWherePage(page, begindate, enddate, applybranchid, ishandle, cwb, isFinancial, audit);
			pageobj = new Page(this.applyEditDeliverystateDAO.getApplyEditDeliverystateByWhereCount(begindate, enddate, applybranchid, ishandle, cwb, isFinancial, audit), page, Page.ONE_PAGE_NUMBER);
		}
		model.addAttribute("applyEditDeliverystateList", applyEditDeliverystates);

		model.addAttribute("branchList", this.branchDAO.getAllEffectBranches());
		model.addAttribute("userList", this.userDAO.getAllUser());
		model.addAttribute("page_obj", pageobj);

		return "applyeditdeliverystate/handleApplyEditDeliverystatelist";
	}

	/**
	 * 客服进入到某个订单的修改功能
	 * 
	 * @param id
	 * @param model
	 * @return
	 */
	@RequestMapping("/handleApplyEditDeliverystateByid/{id}")
	public String handleApplyEditDeliverystateByid(@PathVariable("id") long id, Model model) {
		ApplyEditDeliverystate applyEditDeliverystate = this.applyEditDeliverystateDAO.getApplyEditDeliverystateById(id);
		DeliveryState ds = this.deliveryStateDAO.getActiveDeliveryStateByCwb(applyEditDeliverystate.getCwb());
		List<Reason> backreasonlist = this.reasonDAO.getAllReasonByReasonType(ReasonTypeEnum.ReturnGoods.getValue());
		List<Reason> leavedreasonlist = this.reasonDAO.getAllReasonByReasonType(ReasonTypeEnum.BeHelpUp.getValue());
		List<Reason> podremarkreasonlist = this.reasonDAO.getAllReasonByReasonType(ReasonTypeEnum.GiveResult.getValue());

		CwbOrder co = this.cwbDAO.getCwbByCwb(applyEditDeliverystate.getCwb());

		List<Customer> customerList = this.customerDAO.getAllCustomers();
		List<User> userList = this.userDAO.getAllUser();

		model.addAttribute("cwborder", co);
		model.addAttribute("deliverystate", this.getDeliveryStateView(ds, customerList, userList, null));

		if ((co.getBackreason() != null) && (co.getBackreason().length() > 0)) {
			model.addAttribute("backreasonid", this.reasonDAO.getReasonByReasonid(co.getBackreasonid()) == null ? 0 : this.reasonDAO.getReasonByReasonid(co.getBackreasonid()).getReasonid());
		}
		if ((co.getLeavedreason() != null) && (co.getLeavedreason().length() > 0)) {
			model.addAttribute("leavedreasonid", this.reasonDAO.getReasonByReasonid(co.getLeavedreasonid()) == null ? 0 : this.reasonDAO.getReasonByReasonid(co.getLeavedreasonid()).getReasonid());
		}

		model.addAttribute("backreasonlist", backreasonlist);
		model.addAttribute("leavedreasonlist", leavedreasonlist);
		model.addAttribute("podremarkreasonlist", podremarkreasonlist);
		model.addAttribute("applyEditDeliverystate", applyEditDeliverystate);
		model.addAttribute("userList", this.userDAO.getAllUser());

		return "applyeditdeliverystate/handleApplyEditDeliverystate";
	}

	/**
	 * 客服对选择的订单进行修改的功能
	 * 
	 * @param model
	 * @param id
	 * @param deliveryid
	 * @param podresultid
	 * @param backreasonid
	 * @param leavedreasonid
	 * @param podremarkid
	 * @param returnedfee
	 * @param receivedfeecash
	 * @param receivedfeepos
	 * @param posremark
	 * @param receivedfeecheque
	 * @param receivedfeeother
	 * @param checkremark
	 * @param deliverstateremark
	 * @return
	 */
	@RequestMapping("/agreeEditDeliveryState/{id}/{deliveryid}")
	public @ResponseBody
	String agreeEditDeliveryState(Model model, @PathVariable("id") long id, @PathVariable("deliveryid") long deliveryid,
			@RequestParam(value = "podresultid", required = false, defaultValue = "0") long podresultid, @RequestParam(value = "backreasonid", required = false, defaultValue = "0") long backreasonid,
			@RequestParam(value = "leavedreasonid", required = false, defaultValue = "0") long leavedreasonid,
			@RequestParam(value = "podremarkid", required = false, defaultValue = "0") long podremarkid, @RequestParam("returnedfee") BigDecimal returnedfee,
			@RequestParam("receivedfeecash") BigDecimal receivedfeecash, @RequestParam("receivedfeepos") BigDecimal receivedfeepos, @RequestParam("posremark") String posremark,
			@RequestParam("receivedfeecheque") BigDecimal receivedfeecheque, @RequestParam("receivedfeeother") BigDecimal receivedfeeother,
			@RequestParam(value = "checkremark", required = false, defaultValue = "") String checkremark,
			@RequestParam(value = "deliverstateremark", required = false, defaultValue = "") String deliverstateremark) {

		this.logger.info("web--进入单票反馈");

		ApplyEditDeliverystate applyEditDeliverystate = this.applyEditDeliverystateDAO.getApplyEditDeliverystateById(id);
		try {
			CwbOrder co = this.cwbDAO.getCwbByCwb(applyEditDeliverystate.getCwb());
			Map<String, AccountCwbFareDetail> accountCwbFareDetailMap = this.accountCwbFareDetailDAO.getAccountCwbFareDetailMapByCwbs("'" + applyEditDeliverystate.getCwb() + "'");

			// 判断订单当前状态为36 已审核状态的订单才能重置审核状态
			if (co.getFlowordertype() == FlowOrderTypeEnum.YiShenHe.getValue()) {
				// 判断订单号是否为POS刷卡 posremark=POS刷卡 POS刷卡的订单不允许重置审核状态
				DeliveryState ds = this.deliveryStateDAO.getDeliveryStateByCwb(co.getCwb()).get(0);
				if ((co.getInfactfare().compareTo(BigDecimal.ZERO) > 0) && ((accountCwbFareDetailMap.get(co.getCwb()) == null ? 0 : accountCwbFareDetailMap.get(co.getCwb()).getFareid()) > 0)) {
					// 暂借对象中的备注1字段输出一些提示语
					co.setRemark1("当前订单运费已交款，不可重置审核状态");
					return "{\"errorCode\":1,\"error\":\" 当前订单运费已交款，不可重置审核状态  \"}";
				} else if (ds.getPosremark().indexOf("POS刷卡") == -1) {

				} else {
					// 暂借对象中的备注1字段输出一些提示语
					co.setRemark1("POS刷卡签收的订单审核后不允许重置审核状态");
					return "{\"errorCode\":1,\"error\":\" POS刷卡签收的订单审核后不允许重置审核状态  \"}";
				}
				List<User> userList = this.userDAO.getUserByid(deliveryid);
				if (userList != null) {
					this.logger.info("重置订单审核状态功能 [{}] cwb: {}", this.getSessionUser().getRealname());
					List<EdtiCwb_DeliveryStateDetail> ecList = new ArrayList<EdtiCwb_DeliveryStateDetail>();
					List<String> errorList = new ArrayList<String>();

					try {
						EdtiCwb_DeliveryStateDetail ec_dsd = this.editCwbService.analysisAndSaveByChongZhiShenHe(applyEditDeliverystate.getCwb(), deliveryid, this.getSessionUser().getUserid());
						ecList.add(ec_dsd);
					} catch (ExplinkException ee) {
						errorList.add(applyEditDeliverystate.getCwb() + "_" + ee.getMessage());
						return "{\"errorCode\":1,\"error\":\"" + ee.getMessage() + "\"}";
					} catch (Exception e) {
						errorList.add(applyEditDeliverystate.getCwb() + "_" + FlowOrderTypeEnum.YiShenHe.getValue() + "_系统内部报错！");
						return "{\"errorCode\":1,\"error\":\"" + e.getMessage() + "\"}";
					}
				}

			}
		} catch (Exception e) {
			return "{\"errorCode\":1,\"error\":\"" + e.getMessage() + "\"}";
		}
		// 暂借对象中的备注1字段输出一些提示语

		try {
			// 判断是否符合申请条件：1.未反馈给电商 2.未交款
			DeliveryState deliverystate = this.deliveryStateDAO.getActiveDeliveryStateByCwb(applyEditDeliverystate.getCwb());
			if ((deliverystate != null) && (deliverystate.getPayupid() == 0) && (deliverystate.getIssendcustomer() == 0)) {
				Map<String, Object> parameters = new HashMap<String, Object>();
				parameters.put("deliverid", deliveryid);
				parameters.put("podresultid", podresultid);
				parameters.put("backreasonid", backreasonid);
				parameters.put("leavedreasonid", leavedreasonid);
				parameters.put("receivedfeecash", receivedfeecash);
				parameters.put("receivedfeepos", receivedfeepos);
				parameters.put("receivedfeecheque", receivedfeecheque);
				parameters.put("receivedfeeother", receivedfeeother);
				parameters.put("paybackedfee", returnedfee);
				parameters.put("podremarkid", podremarkid);
				parameters.put("posremark", posremark);
				parameters.put("checkremark", checkremark);
				parameters.put("deliverstateremark", deliverstateremark + "-客服改单");
				parameters.put("owgid", 0);
				parameters.put("sessionbranchid", deliverystate.getDeliverybranchid());
				parameters.put("sessionuserid", this.getSessionUser().getUserid());
				parameters.put("sign_typeid", SignTypeEnum.BenRenQianShou.getValue());
				parameters.put("sign_man", "");
				parameters.put("sign_time", DateTimeUtil.getNowTime());
				this.cwborderService.deliverStatePod(this.getSessionUser(), applyEditDeliverystate.getCwb(), applyEditDeliverystate.getCwb(), parameters);
				CwbOrder cwbOrder = this.cwbDAO.getCwbByCwb(applyEditDeliverystate.getCwb());
				DeliveryState deliveryState = this.deliveryStateDAO.getActiveDeliveryStateByCwb(applyEditDeliverystate.getCwb());

				CwbOrderWithDeliveryState cwbOrderWithDeliveryState = new CwbOrderWithDeliveryState();
				cwbOrderWithDeliveryState.setCwbOrder(cwbOrder);
				cwbOrderWithDeliveryState.setDeliveryState(deliveryState);

				try {
					this.applyEditDeliverystateDAO.agreeSaveApplyEditDeliverystateById(id, receivedfeecash.add(receivedfeecheque).add(receivedfeeother), receivedfeepos, this.getSessionUser()
							.getUserid(), DateTimeUtil.getNowTime(), new ObjectMapper().writeValueAsString(cwbOrderWithDeliveryState).toString());
				} catch (Exception e) {
					this.logger.error("error while saveing applyEditDeliverystate", e);
				}
			} else {
				if ((deliverystate != null) && (deliverystate.getPayupid() > 0)) {
					return "{\"errorCode\":1,\"error\":\"订单已经上交款，不能修改状态\"}";
				} else {
					return "{\"errorCode\":1,\"error\":\"订单状态已经推送给电商，不能修改状态\"}";
				}
			}

		} catch (CwbException ce) {
			CwbOrder cwbOrder = this.cwbDAO.getCwbByCwb(applyEditDeliverystate.getCwb());
			this.exceptionCwbDAO.createExceptionCwb(applyEditDeliverystate.getCwb(), ce.getFlowordertye(), ce.getMessage(), this.getSessionUser().getBranchid(), this.getSessionUser().getUserid(),
					cwbOrder == null ? 0 : cwbOrder.getCustomerid(), 0, 0, 0, "");

			return "{\"errorCode\":1,\"error\":\"" + ce.getMessage() + "\"}";
		}
		this.appearWindowDao.creWindowTime("订单修改受理处理cwb=" + applyEditDeliverystate.getCwb(), 6, applyEditDeliverystate.getApplyuserid(), 1);
		return "{\"errorCode\":0,\"error\":\"修改成功\"}";

	}

	/**
	 * 得到订单的一些信息功能
	 * 
	 * @param ds
	 * @param customerList
	 * @param userList
	 * @param clist
	 * @return
	 */
	private DeliveryStateView getDeliveryStateView(DeliveryState ds, List<Customer> customerList, List<User> userList, List<CwbOrder> clist) {
		DeliveryStateView sdv = new DeliveryStateView();
		sdv.setId(ds.getId());
		sdv.setCwb(ds.getCwb());
		sdv.setDeliveryid(ds.getDeliveryid());
		sdv.setReceivedfee(ds.getReceivedfee());
		sdv.setReturnedfee(ds.getReturnedfee());
		sdv.setBusinessfee(ds.getBusinessfee());
		sdv.setDeliverystate(ds.getDeliverystate());
		sdv.setCash(ds.getCash());
		sdv.setPos(ds.getPos());
		sdv.setPosremark(ds.getPosremark());
		sdv.setMobilepodtime(ds.getMobilepodtime());
		sdv.setCheckfee(ds.getCheckfee());
		sdv.setCheckremark(ds.getCheckremark());
		sdv.setReceivedfeeuser(ds.getReceivedfeeuser());
		sdv.setCreatetime(ds.getCreatetime());
		sdv.setOtherfee(ds.getOtherfee());
		sdv.setPodremarkid(ds.getPodremarkid());
		sdv.setDeliverstateremark(ds.getDeliverstateremark());
		sdv.setIsout(ds.getIsout());
		sdv.setPos_feedback_flag(ds.getPos_feedback_flag());
		sdv.setUserid(ds.getUserid());
		sdv.setGcaid(ds.getGcaid());
		sdv.setSign_typeid(ds.getSign_typeid());
		sdv.setSign_man(ds.getSign_man());
		sdv.setSign_time(ds.getSign_time());

		CwbOrder cwbOrder = null;
		if (clist == null) {
			cwbOrder = this.cwbDAO.getCwbByCwb(ds.getCwb());
		} else {
			for (CwbOrder c : clist) {
				if (c.getCwb().equals(ds.getCwb())) {
					cwbOrder = c;
					break;
				}
			}
		}
		if (cwbOrder == null) {
			this.logger.warn("cwborder {} not exist" + ds.getCwb());
			return null;
		}
		sdv.setCustomerid(cwbOrder.getCustomerid());
		for (Customer c : customerList) {
			if (cwbOrder.getCustomerid() == c.getCustomerid()) {
				sdv.setCustomername(c.getCustomername());
			}
		}
		sdv.setEmaildate(cwbOrder.getEmaildate());
		sdv.setConsigneename(cwbOrder.getConsigneename());
		sdv.setConsigneemobile(cwbOrder.getConsigneemobile());
		sdv.setConsigneephone(cwbOrder.getConsigneephone());
		sdv.setConsigneeaddress(cwbOrder.getConsigneeaddress());
		sdv.setBackcarname(cwbOrder.getBackcarname());
		sdv.setSendcarname(cwbOrder.getSendcarname());
		String realname = "";
		for (User u : userList) {
			if (u.getUserid() == ds.getDeliveryid()) {
				realname = u.getRealname();
			}
		}
		sdv.setDeliverealname(realname);
		sdv.setFlowordertype(cwbOrder.getFlowordertype());
		sdv.setCwbordertypeid(cwbOrder.getCwbordertypeid());
		sdv.setCwbremark(cwbOrder.getCwbremark());
		sdv.setBackreason(cwbOrder.getBackreason());
		sdv.setLeavedreason(cwbOrder.getLeavedreason());
		return sdv;
	}

	private boolean IsRole(String tip) {
		try {
			long roleid = this.getSessionUser().getRoleid();
			SystemInstall sys = this.systemInstallDAO.getSystemInstall(tip);
			sys = sys == null ? new SystemInstall() : sys;
			if (sys.getValue().contains(",")) {
				String[] ids = sys.getValue().split(",");
				for (String id : ids) {
					if (roleid == Integer.parseInt(id.trim())) {
						return true;
					}
				}
			}
		} catch (Exception e) {
			return false;
		}
		return false;
	}

	@RequestMapping("/audit")
	public @ResponseBody
	int audit(Model model, @RequestParam(value = "id", required = false, defaultValue = "0") long id, @RequestParam(value = "flag", required = false, defaultValue = "0") long flag) {
		int count = this.applyEditDeliverystateDAO.updateAudit(id, flag, this.getSessionUser().getUserid());
		return count;
	}

}
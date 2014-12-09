package cn.explink.controller;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.ObjectReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.explink.b2c.amazon.AmazonService;
import cn.explink.b2c.tools.B2cEnum;
import cn.explink.b2c.tools.JointService;
import cn.explink.dao.BranchDAO;
import cn.explink.dao.CustomerDAO;
import cn.explink.dao.CwbDAO;
import cn.explink.dao.DeliveryStateDAO;
import cn.explink.dao.ExceptionCwbDAO;
import cn.explink.dao.GotoClassAuditingDAO;
import cn.explink.dao.GotoClassOldDAO;
import cn.explink.dao.OrderFlowDAO;
import cn.explink.dao.ReasonDao;
import cn.explink.dao.SwitchDAO;
import cn.explink.dao.SystemInstallDAO;
import cn.explink.dao.UserDAO;
import cn.explink.domain.Customer;
import cn.explink.domain.CwbOrder;
import cn.explink.domain.CwbSearchDelivery;
import cn.explink.domain.DeliveryState;
import cn.explink.domain.GotoClassAuditing;
import cn.explink.domain.GotoClassOld;
import cn.explink.domain.Reason;
import cn.explink.domain.SystemInstall;
import cn.explink.domain.User;
import cn.explink.domain.orderflow.OrderFlow;
import cn.explink.enumutil.B2cPushStateEnum;
import cn.explink.enumutil.DeliveryStateEnum;
import cn.explink.enumutil.DeliveryTongjiEnum;
import cn.explink.enumutil.ExceptionCwbErrorTypeEnum;
import cn.explink.enumutil.FlowOrderTypeEnum;
import cn.explink.enumutil.ReasonTypeEnum;
import cn.explink.enumutil.switchs.SwitchEnum;
import cn.explink.exception.CwbException;
import cn.explink.exception.ExplinkException;
import cn.explink.pos.tools.SignTypeEnum;
import cn.explink.service.CwbOrderService;
import cn.explink.service.ExplinkUserDetail;
import cn.explink.service.ExportService;
import cn.explink.util.DateTimeUtil;
import cn.explink.util.ExcelUtils;
import cn.explink.util.Page;
import cn.explink.util.StringUtil;

@Controller
@RequestMapping("/delivery")
public class DeliveryController {
	private Logger logger = LoggerFactory.getLogger(DeliveryController.class);

	@Autowired
	DeliveryStateDAO deliveryStateDAO;

	@Autowired
	UserDAO userDAO;

	@Autowired
	CustomerDAO customerDAO;

	@Autowired
	BranchDAO branchDAO;

	@Autowired
	GotoClassAuditingDAO gotoClassAuditingDAO;

	@Autowired
	GotoClassOldDAO gotoClassOldDAO;

	@Autowired
	ReasonDao reasonDao;

	@Autowired
	CwbDAO cwbDAO;

	@Autowired
	CwbOrderService cwborderService;
	@Autowired
	OrderFlowDAO orderFlowDAO;
	@Autowired
	SwitchDAO switchDAO;

	@Autowired
	SystemInstallDAO systemInstallDAO;
	@Autowired
	ExceptionCwbDAO exceptionCwbDAO;

	@Autowired
	JointService jointService;

	@Autowired
	SecurityContextHolderStrategy securityContextHolderStrategy;
	@Autowired
	ExportService exportService;
	@Autowired
	AmazonService amazonService;
	@Autowired
	HttpSession session;

	private SimpleDateFormat df_d = new SimpleDateFormat("yyyy-MM-dd");
	private ObjectMapper objectMapper = new ObjectMapper();
	private ObjectReader dmpOrderFlowMapper = this.objectMapper.reader(CwbOrder.class);

	private User getSessionUser() {
		ExplinkUserDetail userDetail = (ExplinkUserDetail) this.securityContextHolderStrategy.getContext().getAuthentication().getPrincipal();
		return userDetail.getUser();
	}

	@RequestMapping("/auditView")
	public String auditView(Model model, @RequestParam(value = "deliveryId", defaultValue = "0", required = false) long deliveryId) {
		SystemInstall siteDayLogTime = this.systemInstallDAO.getSystemInstallByName("useAudit");
		if (siteDayLogTime != null) {
			model.addAttribute("useAudit", siteDayLogTime.getValue());
		}
		// 退货原因
		List<Reason> returnlist = this.reasonDao.getAllReasonByReasonType(ReasonTypeEnum.ReturnGoods.getValue());
		// 滞留原因
		List<Reason> staylist = this.reasonDao.getAllReasonByReasonType(ReasonTypeEnum.BeHelpUp.getValue());
		model.addAttribute("returnlist", returnlist);
		model.addAttribute("staylist", staylist);

		List<JSONObject> dList = null;
		// 如果当前登录用户为小件员
		if (2 == this.getSessionUser().getRoleid()) {
			dList = this.deliveryStateDAO.getDeliverByDeliveryStateNoZeroUserid(this.getSessionUser().getBranchid(), this.getSessionUser().getUserid());
			SystemInstall deliveryxiaojianyuan = this.systemInstallDAO.getSystemInstall("deliveryxiaojianyuan");
			model.addAttribute("deliveryxiaojianyuan", deliveryxiaojianyuan == null ? "no" : deliveryxiaojianyuan.getValue());
		} else {
			dList = this.deliveryStateDAO.getDeliverByDeliveryStateNoZero(this.getSessionUser().getBranchid());
		}
		model.addAttribute("deliverList", dList);
		model.addAttribute("userroleid", this.getSessionUser().getRoleid());

		/* model.addAttribute("customers",customerDAO.getAllCustomers()); */
		int width = 1400;
		if (deliveryId > 0) {
			List<DeliveryState> deliveryStateList = this.deliveryStateDAO.getDeliveryStateByDeliver(deliveryId);
			List<DeliveryStateView> cwbOrderWithDeliveryState = this.getDeliveryStateViews(deliveryStateList, null);
			DeliveryStateDTO deliveryStateDTO = new DeliveryStateDTO();
			deliveryStateDTO.analysisDeliveryStateList(cwbOrderWithDeliveryState);
			model.addAttribute("deliveryStateDTO", deliveryStateDTO);
		} else {// 没有选小件员的情况下 显示所有小件员当天的对应数据数据的
			List<Customer> customerList = this.customerDAO.getAllCustomers();
			// <小件员,<供货商,<类型，单数>>>
			// 领货 = 今日未反馈+ 今日已反馈 + 昨日未反馈
			Map<Long, Map<Long, Map<Long, Long>>> deliveryInCountMap = this.getDMap(dList, customerList, this.getSessionUser().getBranchid());
			Map<Long, Map<Long, Long>> huizongMap = new HashMap<Long, Map<Long, Long>>();
			if ((dList != null) && (dList.size() > 0) && (customerList != null) && (customerList.size() > 0)) {
				for (Customer customer : customerList) {
					long yifankui = 0;
					long jinriweifankui = 0;
					long zuoriweifankui = 0;
					long linghuo = 0;
					Map<Long, Long> typeMap = new HashMap<Long, Long>();
					for (JSONObject d : dList) {
						Map<Long, Long> map = deliveryInCountMap.get(d.getLong("deliveryid")).get(customer.getCustomerid());
						yifankui += map.get(DeliveryTongjiEnum.JinriYIfankui.getValue());
						jinriweifankui += map.get(DeliveryTongjiEnum.JinriWeifankui.getValue());
						zuoriweifankui += map.get(DeliveryTongjiEnum.zuoriWeifankui.getValue());
						linghuo += map.get(DeliveryTongjiEnum.LingHuo.getValue());
					}
					typeMap.put(DeliveryTongjiEnum.JinriYIfankui.getValue(), yifankui);
					typeMap.put(DeliveryTongjiEnum.JinriWeifankui.getValue(), jinriweifankui);
					typeMap.put(DeliveryTongjiEnum.zuoriWeifankui.getValue(), zuoriweifankui);
					typeMap.put(DeliveryTongjiEnum.LingHuo.getValue(), linghuo);
					huizongMap.put(customer.getCustomerid(), typeMap);
				}
			}
			model.addAttribute("customerList", customerList);
			model.addAttribute("deliveryMap", deliveryInCountMap);
			model.addAttribute("huizongMap", huizongMap);
			if (customerList.size() > 3) {
				width = width + (300 * (customerList.size() - 3));
			}
		}

		SystemInstall showposandqita = this.systemInstallDAO.getSystemInstall("showposandqita");
		SystemInstall isGuiBanUseZanBuChuLi = this.systemInstallDAO.getSystemInstall("isGuiBanUseZanBuChuLi");
		SystemInstall isReasonRequired = this.systemInstallDAO.getSystemInstall("isReasonRequired");
		model.addAttribute("isReasonRequired", isReasonRequired == null ? "no" : isReasonRequired.getValue());
		model.addAttribute("width", width);
		model.addAttribute("pl_switch", this.switchDAO.getSwitchBySwitchname(SwitchEnum.PiLiangFanKuiPOS.getText()));
		model.addAttribute("showposandqita", showposandqita == null ? "no" : showposandqita.getValue());
		model.addAttribute("isGuiBanUseZanBuChuLi", isGuiBanUseZanBuChuLi == null ? "no" : isGuiBanUseZanBuChuLi.getValue());
		model.addAttribute("isGuiBanUseZanBuChuLi", isGuiBanUseZanBuChuLi == null ? "no" : isGuiBanUseZanBuChuLi.getValue());
		return "delivery/auditView";
	}

	/**
	 * 封装前台需要的map
	 *
	 * @param dList
	 * @param customerList
	 * @return
	 */
	private Map<Long, Map<Long, Map<Long, Long>>> getDMap(List<JSONObject> dList, List<Customer> customerList, long branchid) {
		Map<Long, Map<Long, Map<Long, Long>>> deliveryInCountMap = new HashMap<Long, Map<Long, Map<Long, Long>>>();
		// 获取该站点所有的未归班的订单
		List<DeliveryState> delList = this.deliveryStateDAO.getDeliverByBranchid(branchid);
		if ((dList != null) && (dList.size() > 0) && (customerList != null) && (customerList.size() > 0)) {
			String nowtime = new SimpleDateFormat("yyyy-MM-dd").format(new Date()) + " 00:00:00";
			for (JSONObject dObject : dList) {
				if ((delList != null) && (delList.size() > 0)) {
					Map<Long, Map<Long, Long>> customerIdCountMap = new HashMap<Long, Map<Long, Long>>();
					for (Customer customer : customerList) {
						Map<Long, Long> typeMap = new HashMap<Long, Long>();
						long yifankui = 0;
						long jinriweifankui = 0;
						long zuoriweifankui = 0;
						long linghuo = 0;
						for (DeliveryState deliveryState : delList) {
							if (deliveryState.getDeliveryid() != dObject.getLong("deliveryid")) {
								continue;
							}
							if (deliveryState.getCustomerid() == customer.getCustomerid()) {
								long cratetimeL = 0;
								long nowTimeL = 0;
								try {
									cratetimeL = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(deliveryState.getCreatetime()).getTime();
									nowTimeL = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(nowtime).getTime();
								} catch (ParseException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								// 已反馈
								if (deliveryState.getDeliverystate() > 0) {
									yifankui++;
									linghuo++;
									continue;
								}
								// 今日未反馈
								if ((deliveryState.getDeliverystate() <= 0) && (cratetimeL > nowTimeL)) {
									jinriweifankui++;
									linghuo++;
									continue;
								}
								// 昨日未反馈
								if ((deliveryState.getDeliverystate() <= 0) && (cratetimeL <= nowTimeL)) {
									zuoriweifankui++;
									linghuo++;
									continue;
								}
							}

						}
						typeMap.put(DeliveryTongjiEnum.JinriYIfankui.getValue(), yifankui);
						typeMap.put(DeliveryTongjiEnum.JinriWeifankui.getValue(), jinriweifankui);
						typeMap.put(DeliveryTongjiEnum.zuoriWeifankui.getValue(), zuoriweifankui);
						typeMap.put(DeliveryTongjiEnum.LingHuo.getValue(), linghuo);

						customerIdCountMap.put(customer.getCustomerid(), typeMap);
					}
					deliveryInCountMap.put(dObject.getLong("deliveryid"), customerIdCountMap);
				}
			}

		}
		return deliveryInCountMap;
	}

	/**
	 * 暂不处理
	 *
	 * @param cwb
	 */
	@RequestMapping("/noSub/{cwb}")
	public @ResponseBody void noSub(@PathVariable("cwb") String cwb) {
		this.deliveryStateDAO.noSubSave(cwb);
	}

	/**
	 * 暂不处理恢复
	 *
	 * @param cwb
	 */
	@RequestMapping("/reSub/{cwb}")
	public @ResponseBody void reSub(@PathVariable("cwb") String cwb) {
		this.deliveryStateDAO.reSubSave(cwb);
	}

	/**
	 * 确定审核
	 *
	 * @param model
	 * @param cwbs
	 * @param nocwbs
	 */
	@RequestMapping("/sub")
	public String sub(Model model, @RequestParam("zanbuchuliTrStr") String zanbuchuliTrStr, @RequestParam("subTrStr") String subTrStr, @RequestParam("nocwbs") String nocwbs,
			@RequestParam("deliveryId") long deliveryId) {
		DeliveryStateDTO dsDTO = new DeliveryStateDTO();
		String cwbs = zanbuchuliTrStr;
		cwbs += (cwbs.length() > 0) && (subTrStr.length() > 0) ? "," + subTrStr : subTrStr;
		cwbs += (cwbs.length() > 0) && (nocwbs.length() > 0) ? "," + nocwbs : nocwbs;
		List<DeliveryState> dlist = this.deliveryStateDAO.getDeliveryStateByCwbs(cwbs);

		if (dlist != null) {
			List<DeliveryStateView> deliveryStateViews = this.getDeliveryStateViews(dlist, cwbs);
			dsDTO.analysisDeliveryStateList(deliveryStateViews);
		}

		model.addAttribute("deliveryStateDTO", dsDTO);
		User u = this.userDAO.getUserByUserid(deliveryId);
		model.addAttribute("branch", this.branchDAO.getBranchByBranchid(u.getBranchid()));
		model.addAttribute("deliver", u);
		SystemInstall usedeliverpay = this.systemInstallDAO.getSystemInstallByName("usedeliverpayup");
		model.addAttribute("usedeliverpayup", usedeliverpay == null ? "no" : usedeliverpay.getValue());
		return "delivery/sub";
	}

	/**
	 * 获得前端需要的界面视图变量
	 *
	 * @param dsList
	 * @param cwbs
	 *            'ABD123','BCD321' 如果为null 会自动从dsList中获取订单号
	 * @return
	 */
	public List<DeliveryStateView> getDeliveryStateViews(List<DeliveryState> dsList, String cwbs) {
		List<DeliveryStateView> deliveryStateViewList = new ArrayList<DeliveryStateView>();
		List<Customer> customerList = this.customerDAO.getAllCustomersNew();
		List<User> userList = this.userDAO.getAllUser();

		if (dsList.size() > 0) {
			if ((cwbs == null) || cwbs.equals("")) {
				StringBuffer cwbBuffer = new StringBuffer();
				for (DeliveryState ds : dsList) {
					cwbBuffer = cwbBuffer.append("'").append(ds.getCwb()).append("',");
				}
				cwbs = cwbBuffer.substring(0, cwbBuffer.length() - 1);
			}

			List<CwbOrder> clist = this.cwbDAO.getCwbByCwbs(cwbs);

			for (DeliveryState ds : dsList) {
				DeliveryStateView sdv = this.getDeliveryStateView(ds, customerList, userList, clist);
				if (sdv != null) { // 数据不正确时会返回null
					deliveryStateViewList.add(sdv);
				}
			}

		}

		return deliveryStateViewList;
	}

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
		sdv.setCodpos(ds.getCodpos());
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
		sdv.setSign_man(StringUtil.nullConvertToEmptyString(ds.getSign_man()));
		sdv.setSign_time(StringUtil.nullConvertToEmptyString(ds.getSign_time()));
		sdv.setDeliverytime(ds.getDeliverytime());
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
		sdv.setShouldfare(ds.getShouldfare());
		sdv.setInfactfare(ds.getInfactfare());
		return sdv;
	}

	/**
	 * 历史审核记录详情
	 *
	 * @param model
	 * @param cwbs
	 * @param nocwbs
	 */
	@RequestMapping("/viewOldSub/{id}")
	public String viewOldSub(Model model, @PathVariable("id") long id) {
		GotoClassAuditing gca = this.gotoClassAuditingDAO.getGotoClassAuditingByGcaid(id);
		model.addAttribute("gotoClassAuditing", gca);
		model.addAttribute("branch", this.branchDAO.getBranchByBranchid(gca.getBranchid()));
		model.addAttribute("deliverealuser", this.userDAO.getUserByUserid(gca.getDeliverealuser()));
		model.addAttribute("gotoClassOld", this.gotoClassOldDAO.getGotoClassOld(id).get(0));

		SystemInstall usedeliverpay = this.systemInstallDAO.getSystemInstallByName("usedeliverpayup");
		model.addAttribute("usedeliverpayup", usedeliverpay == null ? "no" : usedeliverpay.getValue());
		return "delivery/viewOldSub";
	}

	/**
	 * 打印归班汇总详情
	 *
	 * @param model
	 * @param cwbs
	 * @param nocwbs
	 */
	@RequestMapping("/printSub")
	public String printSub(Model model, @RequestParam("branchid") long branchid, @RequestParam("userid") long userid) {
		model.addAttribute("branch", this.branchDAO.getBranchByBranchid(branchid));
		model.addAttribute("user", this.userDAO.getUserByUserid(userid));
		return "delivery/printSub";
	}

	/**
	 * 提交审核
	 */
	@RequestMapping("/ok")
	public @ResponseBody String ok(Model model, HttpServletRequest request, @RequestParam("zanbuchuliTrStr") String zanbuchuliTrStr, @RequestParam("subTrStr") String subTrStr,
			@RequestParam("nocwbs") String nocwbs, @RequestParam("okTime") String okTime, @RequestParam("subAmount") String subAmount, @RequestParam("subAmountPos") String subAmountPos,
			@RequestParam("subAmountCodPos") String subAmountCodPos, @RequestParam("subSmtFare") String subSmtFare, @RequestParam("deliverealuser") long deliverealuser,
			@RequestParam(value = "deliverpayuptype", required = false, defaultValue = "0") int deliverpayuptype,
			@RequestParam(value = "deliverpayupamount", required = false, defaultValue = "0") BigDecimal deliverpayupamount,
			@RequestParam(value = "deliverpayupbanknum", required = false, defaultValue = "") String deliverpayupbanknum,
			@RequestParam(value = "deliverpayupaddress", required = false, defaultValue = "") String deliverpayupaddress,
			@RequestParam(value = "deliverpayupamount_pos", required = false, defaultValue = "0") BigDecimal deliverpayupamount_pos,
			@RequestParam(value = "deliverpayupamount_codpos", required = false, defaultValue = "0") BigDecimal deliverpayupamount_codpos,
			@RequestParam(value = "deliverAccount", required = false, defaultValue = "0") BigDecimal deliverAccount,
			@RequestParam(value = "deliverPosAccount", required = false, defaultValue = "0") BigDecimal deliverPosAccount) {
		try {
			if (subTrStr.trim().length() == 0) {
				return "{\"errorCode\":1,\"error\":\"没有关联订单\"}";
			}
			// 将支付宝COD扫码的支付合并到pos上作为归班
			BigDecimal subAmountPosAndCodPos = new BigDecimal(subAmountPos).add(new BigDecimal(subAmountCodPos));
			subAmountPos = subAmountPosAndCodPos + "";
			deliverpayupamount_pos = deliverpayupamount_pos.add(deliverpayupamount_codpos);
			GotoClassOld gotoClassOld = this.loadFormForGotoClass(request);
			return this.cwborderService.deliverAuditok(this.getSessionUser(), subTrStr, okTime, subAmount, subAmountPos, deliverealuser, gotoClassOld, deliverpayuptype, deliverpayupamount,
					deliverpayupbanknum, deliverpayupaddress, deliverpayupamount_pos, deliverAccount, deliverPosAccount);

		} catch (CwbException e) {
			String[] cwbs = subTrStr.split(",");
			for (String cwb : cwbs) {
				if (cwb.trim().length() == 0) {
					continue;
				}
				cwb = cwb.replaceAll("'", "");
				CwbOrder cwbOrder = this.cwbDAO.getCwbByCwb(cwb);
				this.exceptionCwbDAO.createExceptionCwb(cwb, e.getFlowordertye(), e.getMessage(), this.getSessionUser().getBranchid(), this.getSessionUser().getUserid(), cwbOrder == null ? 0
						: cwbOrder.getCustomerid(), 0, 0, 0, "");
			}

			e.printStackTrace();
			return "{\"errorCode\":1,\"error\":\"" + e.getMessage() + "\"}";
		} catch (ExplinkException e) {
			e.printStackTrace();
			return "{\"errorCode\":1,\"error\":\"" + e.getMessage() + "\"}";
		} catch (Exception e) {
			e.printStackTrace();
			return "{\"errorCode\":1,\"error\":\"系统错误\"}";
		}
	}

	private GotoClassOld loadFormForGotoClass(HttpServletRequest request) {
		GotoClassOld gco = new GotoClassOld();
		gco.setNownumber(Long.parseLong(request.getParameter("nownumber")));
		gco.setYiliu(Long.parseLong(request.getParameter("yiliu")));
		gco.setLishi_weishenhe(Long.parseLong(request.getParameter("lishi_weishenhe")));
		gco.setZanbuchuli(Long.parseLong(request.getParameter("zanbuchuli")));
		gco.setPeisong_chenggong(Long.parseLong(request.getParameter("peisong_chenggong")));
		gco.setPeisong_chenggong_amount(BigDecimal.valueOf(Double.parseDouble(request.getParameter("peisong_chenggong_amount"))));
		gco.setPeisong_chenggong_pos_amount(BigDecimal.valueOf(Double.parseDouble(request.getParameter("peisong_chenggong_pos_amount"))));
		gco.setTuihuo(Long.parseLong(request.getParameter("tuihuo")));
		gco.setTuihuo_amount(BigDecimal.valueOf(Double.parseDouble(request.getParameter("tuihuo_amount"))));
		gco.setBufentuihuo(Long.parseLong(request.getParameter("bufentuihuo")));
		gco.setBufentuihuo_amount(BigDecimal.valueOf(Double.parseDouble(request.getParameter("bufentuihuo_amount"))));
		gco.setBufentuihuo_pos_amount(BigDecimal.valueOf(Double.parseDouble(request.getParameter("bufentuihuo_pos_amount"))));
		gco.setZhiliu(Long.parseLong(request.getParameter("zhiliu")));
		gco.setZhiliu_amount(BigDecimal.valueOf(Double.parseDouble(request.getParameter("zhiliu_amount"))));
		gco.setShangmentui_chenggong(Long.parseLong(request.getParameter("shangmentui_chenggong")));
		gco.setShangmentui_chenggong_amount(BigDecimal.valueOf(Double.parseDouble(request.getParameter("shangmentui_chenggong_amount"))));
		gco.setShangmentui_chenggong_fare(BigDecimal.valueOf(Double.parseDouble(request.getParameter("shangmentui_chenggong_fare"))));
		gco.setShangmentui_jutui(Long.parseLong(request.getParameter("shangmentui_jutui")));
		gco.setShangmentui_jutui_amount(BigDecimal.valueOf(Double.parseDouble(request.getParameter("shangmentui_jutui_amount"))));
		gco.setShangmentui_jutui_fare(BigDecimal.valueOf(Double.parseDouble(request.getParameter("shangmentui_jutui_fare"))));
		gco.setShangmenhuan_chenggong(Long.parseLong(request.getParameter("shangmenhuan_chenggong")));
		gco.setShangmenhuan_chenggong_amount(BigDecimal.valueOf(Double.parseDouble(request.getParameter("shangmenhuan_chenggong_amount"))));
		gco.setShangmenhuan_chenggong_pos_amount(BigDecimal.valueOf(Double.parseDouble(request.getParameter("shangmenhuan_chenggong_pos_amount"))));
		gco.setDiushi(Long.parseLong(request.getParameter("diushi")));
		gco.setDiushi_amount(BigDecimal.valueOf(Double.parseDouble(request.getParameter("diushi_amount"))));
		return gco;
	}

	@RequestMapping("/editDeliveryState/{cwb}/{deliveryid}")
	public @ResponseBody String editDeliveryState(Model model, @PathVariable("cwb") String cwb, @PathVariable("deliveryid") long deliveryid, @RequestParam("podresultid") long podresultid,
			@RequestParam("backreasonid") long backreasonid, @RequestParam("leavedreasonid") long leavedreasonid,
			@RequestParam(value = "podremarkid", required = false, defaultValue = "") long podremarkid, @RequestParam("returnedfee") BigDecimal returnedfee,
			@RequestParam("receivedfeecash") BigDecimal receivedfeecash, @RequestParam("receivedfeepos") BigDecimal receivedfeepos, @RequestParam("posremark") String posremark,
			@RequestParam("receivedfeecheque") BigDecimal receivedfeecheque, @RequestParam("receivedfeeother") BigDecimal receivedfeeother,
			@RequestParam(value = "checkremark", required = false, defaultValue = "") String checkremark,
			@RequestParam(value = "deliverstateremark", required = false, defaultValue = "") String deliverstateremark, @RequestParam("weishuakareasonid") long weishuakareasonid,
			@RequestParam("losereasonid") long losereasonid, @RequestParam(value = "deliverytime", required = false, defaultValue = "") String deliverytime,
			@RequestParam(value = "signman", required = false, defaultValue = "") String signman, @RequestParam("infactfare") BigDecimal infactfare) {

		this.logger.info("web--进入单票反馈");
		try {
			String scancwb = cwb;
			cwb = this.cwborderService.translateCwb(cwb);

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
			parameters.put("deliverstateremark", deliverstateremark);
			parameters.put("owgid", 0);
			parameters.put("sessionbranchid", this.getSessionUser().getBranchid());
			parameters.put("sessionuserid", this.getSessionUser().getUserid());
			parameters.put("sign_typeid", SignTypeEnum.BenRenQianShou.getValue());
			parameters.put("sign_man", signman);
			parameters.put("sign_time", DateTimeUtil.getNowTime());
			parameters.put("weishuakareasonid", weishuakareasonid);
			parameters.put("losereasonid", losereasonid);
			parameters.put("deliverytime_now", deliverytime);
			parameters.put("infactfare", infactfare);

			this.cwborderService.deliverStatePod(this.getSessionUser(), cwb, scancwb, parameters);
		} catch (CwbException ce) {
			CwbOrder cwbOrder = this.cwbDAO.getCwbByCwb(cwb);
			this.exceptionCwbDAO.createExceptionCwb(cwb, ce.getFlowordertye(), ce.getMessage(), this.getSessionUser().getBranchid(), this.getSessionUser().getUserid(),
					cwbOrder == null ? 0 : cwbOrder.getCustomerid(), 0, 0, 0, "");
			return "{\"errorCode\":1,\"error\":\"" + ce.getMessage() + "\"}";
		}
		return "{\"errorCode\":0,\"error\":\"修改成功\"}";

	}

	@RequestMapping("/getnowDeliveryState/{cwb}")
	public String getnowDeliveryState(Model model, @PathVariable("cwb") String cwb) {
		DeliveryState ds = this.deliveryStateDAO.getActiveDeliveryStateByCwb(cwb);
		// model.addAttribute("deliverystate", ds.getDeliverystate());
		List<Reason> backreasonlist = this.reasonDao.getAllReasonByReasonType(ReasonTypeEnum.ReturnGoods.getValue());
		List<Reason> leavedreasonlist = this.reasonDao.getAllReasonByReasonType(ReasonTypeEnum.BeHelpUp.getValue());
		List<Reason> podremarkreasonlist = this.reasonDao.getAllReasonByReasonType(ReasonTypeEnum.GiveResult.getValue());
		List<Reason> weishuakareasonlist = this.reasonDao.getAllReasonByReasonType(ReasonTypeEnum.WeiShuaKa.getValue());
		List<Reason> losereasonlist = this.reasonDao.getAllReasonByReasonType(ReasonTypeEnum.DiuShi.getValue());

		CwbOrder co = this.cwbDAO.getCwbByCwb(cwb);

		List<Customer> customerList = this.customerDAO.getAllCustomers();
		List<User> userList = this.userDAO.getAllUser();
		// 是否开启了亚马逊对接
		int isOpenFlag = this.jointService.getStateForJoint(B2cEnum.Amazon.getKey());
		model.addAttribute("isOpenFlag", isOpenFlag);
		model.addAttribute("cwborder", co);
		this.session.setAttribute("deliveryStateType", ds.getDeliverystate());
		model.addAttribute("deliverystate", this.getDeliveryStateView(ds, customerList, userList, null));
		// model.addAttribute("deliveryStateType", ds.getDeliverystate());

		Reason backreason = this.reasonDao.getReasonByReasonid(co.getBackreasonid());
		Reason leavedreason = this.reasonDao.getReasonByReasonid(co.getLeavedreasonid());
		Reason weishuakareason = this.reasonDao.getReasonByReasonid(co.getWeishuakareasonid());
		Reason losereason = this.reasonDao.getReasonByReasonid(co.getLosereasonid());

		if ((co.getBackreason() != null) && (co.getBackreason().length() > 0)) {
			model.addAttribute("backreasonid", backreason == null ? 0 : backreason.getReasonid());
		}
		if ((co.getLeavedreason() != null) && (co.getLeavedreason().length() > 0)) {
			model.addAttribute("leavedreasonid", leavedreason == null ? 0 : leavedreason.getReasonid());
		}
		if ((co.getLeavedreason() != null) && (co.getLeavedreason().length() > 0)) {
			model.addAttribute("weishuakareasonid", weishuakareason == null ? 0 : weishuakareason.getReasonid());
		}
		if ((co.getLeavedreason() != null) && (co.getLeavedreason().length() > 0)) {
			model.addAttribute("losereasonid", losereason == null ? 0 : losereason.getReasonid());
		}

		SystemInstall isReasonRequired = this.systemInstallDAO.getSystemInstall("isReasonRequired");
		SystemInstall showposandqita = this.systemInstallDAO.getSystemInstall("showposandqita");
		SystemInstall isShowZLZDLH = this.systemInstallDAO.getSystemInstall("isShowZLZDLH");
		// 是否允许反馈为部分拒收
		SystemInstall partReject = this.systemInstallDAO.getSystemInstall("partReject");

		model.addAttribute("backreasonlist", backreasonlist);
		model.addAttribute("leavedreasonlist", leavedreasonlist);
		model.addAttribute("podremarkreasonlist", podremarkreasonlist);
		model.addAttribute("weishuakareasonlist", weishuakareasonlist);
		model.addAttribute("losereasonlist", losereasonlist);
		model.addAttribute("isReasonRequired", isReasonRequired == null ? "no" : isReasonRequired.getValue());
		model.addAttribute("showposandqita", showposandqita == null ? "no" : showposandqita.getValue());
		model.addAttribute("isShowZLZDLH", isShowZLZDLH == null ? "no" : isShowZLZDLH.getValue());
		model.addAttribute("partReject", partReject == null ? "yes" : partReject.getValue());

		return "delivery/editDeliveryState";
	}

	/**
	 * 配送订单批量反馈功能
	 *
	 * @param model
	 * @param request
	 * @param cwbs
	 * @param deliverystate
	 * @param backreasonid
	 * @param paytype
	 * @param leavedreasonid
	 * @param resendtime
	 * @param zhiliuremark
	 * @return
	 */
	@RequestMapping("/batchEditDeliveryState")
	public String batchEditDeliveryState(Model model, HttpServletRequest request, @RequestParam(value = "cwbs", required = false, defaultValue = "") String cwbs,
			@RequestParam(defaultValue = "0", required = false, value = "deliverystate") long deliverystate,
			@RequestParam(defaultValue = "0", required = false, value = "backreasonid") long backreasonid, @RequestParam(defaultValue = "1", required = false, value = "paytype") long paytype,
			@RequestParam(defaultValue = "0", required = false, value = "leavedreasonid") long leavedreasonid,
			@RequestParam(defaultValue = "", required = false, value = "resendtime") String resendtime, @RequestParam(defaultValue = "", required = false, value = "zhiliuremark") String zhiliuremark,
			@RequestParam(defaultValue = "", required = false, value = "deliverstateremark") String deliverstateremark) {
		List<Reason> backreasonlist = this.reasonDao.getAllReasonByReasonType(ReasonTypeEnum.ReturnGoods.getValue());
		List<Reason> leavedreasonlist = this.reasonDao.getAllReasonByReasonType(ReasonTypeEnum.BeHelpUp.getValue());

		List<User> deliverList = this.userDAO.getDeliveryUserByRolesAndBranchid("2,4", this.getSessionUser().getBranchid());

		List<Customer> customerList = this.customerDAO.getAllCustomers();// 获取供货商列表

		List<JSONObject> objList = new ArrayList<JSONObject>();

		long successcount = 0;
		if (cwbs.split("\r\n").length < 501) {
			for (String cwb : cwbs.split("\r\n")) {
				if (cwb.trim().length() == 0) {
					continue;
				}

				JSONObject obj = new JSONObject();
				String scancwb = cwb;
				obj.put("cwb", cwb);
				this.logger.info("反馈(批量)-配送订单,cwb:{}", cwb);
				try {

					// 成功订单
					Map<String, Object> parameters = new HashMap<String, Object>();
					parameters.put("podresultid", deliverystate);
					parameters.put("backreasonid", backreasonid);
					parameters.put("leavedreasonid", leavedreasonid);
					parameters.put("podremarkid", 0l);
					parameters.put("posremark", "");
					parameters.put("checkremark", "");
					parameters.put("deliverstateremark", "");
					parameters.put("owgid", 0);
					parameters.put("sessionbranchid", this.getSessionUser().getBranchid());
					parameters.put("sessionuserid", this.getSessionUser().getUserid());
					parameters.put("sign_typeid", SignTypeEnum.BenRenQianShou.getValue());
					parameters.put("sign_time", DateTimeUtil.getNowTime());
					parameters.put("isbatch", true);
					parameters.put("paywayid", paytype);
					parameters.put("resendtime", resendtime);
					parameters.put("zhiliuremark", zhiliuremark);
					parameters.put("deliverstateremark", deliverstateremark);

					this.cwborderService.deliverStatePod(this.getSessionUser(), cwb, scancwb, parameters);
					obj.put("cwbOrder", JSONObject.fromObject(this.cwbDAO.getCwbByCwb(cwb)));
					obj.put("errorcode", "000000");
					successcount++;

				} catch (CwbException ce) {// 出现验证错误
					CwbOrder cwbOrder = this.cwbDAO.getCwbByCwb(cwb);
					this.exceptionCwbDAO.createExceptionCwb(cwb, ce.getFlowordertye(), ce.getMessage(), this.getSessionUser().getBranchid(), this.getSessionUser().getUserid(), cwbOrder == null ? 0
							: cwbOrder.getCustomerid(), 0, 0, 0, "");
					obj.put("cwbOrder", cwbOrder);
					obj.put("errorcode", ce.getError().getValue());
					obj.put("errorinfo", ce.getMessage());
					if (cwbOrder == null) {// 如果无此订单
						obj.put("customer", "");
						obj.put("rukuTime", "");
						obj.put("daohuoTime", "");
					} else {
						for (Customer c : customerList) {
							if (c.getCustomerid() == cwbOrder.getCustomerid()) {
								obj.put("customer", c.getCustomername());
								break;
							}
						}

						List<OrderFlow> ofList = this.orderFlowDAO.getOrderFlowByCwb(cwb);
						OrderFlow of = this.cwborderService.getOrderFlowByOrderFlowListCwbAndTypes(ofList, FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao, FlowOrderTypeEnum.FenZhanDaoHuoYouHuoWuDanSaoMiao);
						obj.put("daohuoTime", of == null ? "" : DateTimeUtil.formatDate(of.getCredate()));
					}

				}
				objList.add(obj);
			}
		}

		List<Reason> weishuakareasonlist = this.reasonDao.getAllReasonByReasonType(ReasonTypeEnum.WeiShuaKa.getValue());

		model.addAttribute("successcount", successcount);
		model.addAttribute("backreasonid", backreasonid);
		model.addAttribute("leavedreasonid", leavedreasonid);
		model.addAttribute("paytype", paytype);
		model.addAttribute("objList", objList);
		model.addAttribute("deliverList", deliverList);
		model.addAttribute("deliverystate", DeliveryStateEnum.getByValue((int) deliverystate).getText());
		model.addAttribute("deliverystateid", deliverystate);
		model.addAttribute("backreasonlist", backreasonlist);
		model.addAttribute("leavedreasonlist", leavedreasonlist);
		model.addAttribute("showposandqita", this.systemInstallDAO.getSystemInstall("showposandqita") == null ? "no" : this.systemInstallDAO.getSystemInstall("showposandqita").getValue());
		// model.addAttribute("pl_switch",
		// switchDAO.getSwitchBySwitchname(SwitchEnum.PiLiangFanKuiPOS.getText()));
		model.addAttribute("pl_switch", this.systemInstallDAO.getSystemInstall("feedbackpos") == null ? "no" : this.systemInstallDAO.getSystemInstall("feedbackpos").getValue());
		model.addAttribute("weishuakareasonlist", weishuakareasonlist);
		model.addAttribute("deliverstateremark", deliverstateremark);
		model.addAttribute("batchEditDeliveryStateisUseCash",
				this.systemInstallDAO.getSystemInstall("batchEditDeliveryStateisUseCash") == null ? "no" : this.systemInstallDAO.getSystemInstall("batchEditDeliveryStateisUseCash").getValue());

		return "delivery/batchEditDeliveryState";
	}

	/**
	 * 上门换订单批量反馈功能
	 *
	 * @param model
	 * @param request
	 * @param cwbs
	 * @param deliverystate
	 * @param backreasonid
	 * @param paytype
	 * @param leavedreasonid
	 * @param resendtime
	 * @param zhiliuremark
	 * @return
	 */
	@RequestMapping("/batchEditSMHDeliveryState")
	public String batchEditSMHDeliveryState(Model model, HttpServletRequest request, @RequestParam(value = "cwbs", required = false, defaultValue = "") String cwbs,
			@RequestParam(defaultValue = "0", required = false, value = "deliverystate") long deliverystate, @RequestParam(defaultValue = "1", required = false, value = "paytype") long paytype) {
		List<User> deliverList = this.userDAO.getDeliveryUserByRolesAndBranchid("2,4", this.getSessionUser().getBranchid());
		List<Customer> customerList = this.customerDAO.getAllCustomers();// 获取供货商列表

		List<JSONObject> objList = new ArrayList<JSONObject>();

		long successcount = 0;
		if (cwbs.split("\r\n").length < 501) {
			for (String cwb : cwbs.split("\r\n")) {
				if (cwb.trim().length() == 0) {
					continue;
				}

				JSONObject obj = new JSONObject();
				String scancwb = cwb;
				obj.put("cwb", cwb);
				this.logger.info("反馈(批量)-上门换订单,cwb:{}", cwb);
				try {// 成功订单
					Map<String, Object> parameters = new HashMap<String, Object>();
					parameters.put("podresultid", deliverystate);
					parameters.put("backreasonid", 0l);
					parameters.put("leavedreasonid", 0l);
					parameters.put("podremarkid", 0l);
					parameters.put("posremark", "");
					parameters.put("checkremark", "");
					parameters.put("deliverstateremark", "");
					parameters.put("owgid", 0);
					parameters.put("sessionbranchid", this.getSessionUser().getBranchid());
					parameters.put("sessionuserid", this.getSessionUser().getUserid());
					parameters.put("sign_typeid", SignTypeEnum.BenRenQianShou.getValue());
					parameters.put("sign_time", DateTimeUtil.getNowTime());
					parameters.put("isbatch", true);
					parameters.put("paywayid", paytype);
					parameters.put("resendtime", "");
					parameters.put("zhiliuremark", "");

					this.cwborderService.deliverStatePod(this.getSessionUser(), cwb, scancwb, parameters);
					obj.put("cwbOrder", JSONObject.fromObject(this.cwbDAO.getCwbByCwb(cwb)));
					obj.put("errorcode", "000000");
					successcount++;

				} catch (CwbException ce) {// 出现验证错误
					CwbOrder cwbOrder = this.cwbDAO.getCwbByCwb(cwb);
					this.exceptionCwbDAO.createExceptionCwb(cwb, ce.getFlowordertye(), ce.getMessage(), this.getSessionUser().getBranchid(), this.getSessionUser().getUserid(), cwbOrder == null ? 0
							: cwbOrder.getCustomerid(), 0, 0, 0, "");
					obj.put("cwbOrder", cwbOrder);
					obj.put("errorcode", ce.getError().getValue());
					obj.put("errorinfo", ce.getMessage());
					if (cwbOrder == null) {// 如果无此订单
						obj.put("customer", "");
						obj.put("rukuTime", "");
						obj.put("daohuoTime", "");
					} else {
						for (Customer c : customerList) {
							if (c.getCustomerid() == cwbOrder.getCustomerid()) {
								obj.put("customer", c.getCustomername());
								break;
							}
						}

						List<OrderFlow> ofList = this.orderFlowDAO.getOrderFlowByCwb(cwb);
						OrderFlow of = this.cwborderService.getOrderFlowByOrderFlowListCwbAndTypes(ofList, FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao, FlowOrderTypeEnum.FenZhanDaoHuoYouHuoWuDanSaoMiao);
						obj.put("daohuoTime", of == null ? "" : DateTimeUtil.formatDate(of.getCredate()));
					}

				}
				objList.add(obj);
			}
		}

		model.addAttribute("successcount", successcount);
		model.addAttribute("objList", objList);
		model.addAttribute("deliverList", deliverList);
		model.addAttribute("deliverystate", DeliveryStateEnum.getByValue((int) deliverystate).getText());
		model.addAttribute("deliverystateid", deliverystate);
		model.addAttribute("paytype", paytype);
		model.addAttribute("showposandqita", this.systemInstallDAO.getSystemInstall("showposandqita") == null ? "no" : this.systemInstallDAO.getSystemInstall("showposandqita").getValue());
		model.addAttribute("pl_switch", this.switchDAO.getSwitchBySwitchname(SwitchEnum.PiLiangFanKuiPOS.getText()));

		return "delivery/batchEditSMHDeliveryState";
	}

	/**
	 * 上门退订单批量反馈功能
	 *
	 * @param model
	 * @param request
	 * @param cwbs
	 * @param deliverystate
	 * @param backreasonid
	 * @param paytype
	 * @param leavedreasonid
	 * @param resendtime
	 * @param zhiliuremark
	 * @return
	 */
	@RequestMapping("/batchEditSMTDeliveryState")
	public String batchEditSMTDeliveryState(Model model, HttpServletRequest request, @RequestParam(value = "cwbs", required = false, defaultValue = "") String cwbs,
			@RequestParam(defaultValue = "0", required = false, value = "deliverystate") long deliverystate,
			@RequestParam(defaultValue = "", required = false, value = "deliverstateremark") String deliverstateremark,
			@RequestParam(defaultValue = "0", required = true, value = "backreasonid") long backreasonid) {
		List<Reason> backreasonlist = this.reasonDao.getAllReasonByReasonType(ReasonTypeEnum.ReturnGoods.getValue());

		List<User> deliverList = this.userDAO.getDeliveryUserByRolesAndBranchid("2,4", this.getSessionUser().getBranchid());

		List<Customer> customerList = this.customerDAO.getAllCustomers();// 获取供货商列表

		List<JSONObject> objList = new ArrayList<JSONObject>();

		long successcount = 0;
		if (cwbs.split("\r\n").length < 501) {
			for (String cwb : cwbs.split("\r\n")) {
				if (cwb.trim().length() == 0) {
					continue;
				}

				JSONObject obj = new JSONObject();
				String scancwb = cwb;
				obj.put("cwb", cwb);
				this.logger.info("反馈(批量)-上门退订单,cwb:{}", cwb);
				DeliveryState deliveryState = this.deliveryStateDAO.getActiveDeliveryStateByCwb(cwb);
				try {// 成功订单
					Map<String, Object> parameters = new HashMap<String, Object>();
					parameters.put("podresultid", deliverystate);
					parameters.put("backreasonid", backreasonid);
					parameters.put("leavedreasonid", 0l);
					parameters.put("podremarkid", 0l);
					parameters.put("posremark", "");
					parameters.put("checkremark", "");
					parameters.put("deliverstateremark", deliverstateremark);
					parameters.put("owgid", 0);
					parameters.put("sessionbranchid", this.getSessionUser().getBranchid());
					parameters.put("sessionuserid", this.getSessionUser().getUserid());
					parameters.put("sign_typeid", SignTypeEnum.BenRenQianShou.getValue());
					parameters.put("sign_time", DateTimeUtil.getNowTime());
					parameters.put("isbatch", true);
					parameters.put("resendtime", "");
					parameters.put("zhiliuremark", "");
					if (DeliveryStateEnum.ShangMenJuTui.getValue() == deliverystate) {
						parameters.put("isjutui", true);
					}
					if ((deliveryState != null) && (DeliveryStateEnum.ShangMenJuTui.getValue() != deliverystate)) {
						parameters.put("infactfare", deliveryState.getShouldfare());
					}

					this.cwborderService.deliverStatePod(this.getSessionUser(), cwb, scancwb, parameters);
					obj.put("cwbOrder", JSONObject.fromObject(this.cwbDAO.getCwbByCwb(cwb)));
					obj.put("errorcode", "000000");
					successcount++;

				} catch (CwbException ce) {// 出现验证错误
					CwbOrder cwbOrder = this.cwbDAO.getCwbByCwb(cwb);
					this.exceptionCwbDAO.createExceptionCwb(cwb, ce.getFlowordertye(), ce.getMessage(), this.getSessionUser().getBranchid(), this.getSessionUser().getUserid(), cwbOrder == null ? 0
							: cwbOrder.getCustomerid(), 0, 0, 0, "");
					obj.put("cwbOrder", cwbOrder);
					obj.put("errorcode", ce.getError().getValue());
					obj.put("errorinfo", ce.getMessage());
					if (cwbOrder == null) {// 如果无此订单
						obj.put("customer", "");
						obj.put("rukuTime", "");
						obj.put("daohuoTime", "");
					} else {
						for (Customer c : customerList) {
							if (c.getCustomerid() == cwbOrder.getCustomerid()) {
								obj.put("customer", c.getCustomername());
								break;
							}
						}

						List<OrderFlow> ofList = this.orderFlowDAO.getOrderFlowByCwb(cwb);
						OrderFlow of = this.cwborderService.getOrderFlowByOrderFlowListCwbAndTypes(ofList, FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao, FlowOrderTypeEnum.FenZhanDaoHuoYouHuoWuDanSaoMiao);
						obj.put("daohuoTime", of == null ? "" : DateTimeUtil.formatDate(of.getCredate()));
					}

				}
				objList.add(obj);
			}
		}
		model.addAttribute("backreasonid", backreasonid);
		model.addAttribute("deliverstateremark", deliverstateremark);
		model.addAttribute("successcount", successcount);
		model.addAttribute("objList", objList);
		model.addAttribute("backreasonlist", backreasonlist);
		model.addAttribute("deliverList", deliverList);
		model.addAttribute("deliverystate", DeliveryStateEnum.getByValue((int) deliverystate).getText());
		model.addAttribute("deliverystateid", deliverystate);
		model.addAttribute("showposandqita", this.systemInstallDAO.getSystemInstall("showposandqita") == null ? "no" : this.systemInstallDAO.getSystemInstall("showposandqita").getValue());
		model.addAttribute("pl_switch", this.switchDAO.getSwitchBySwitchname(SwitchEnum.PiLiangFanKuiPOS.getText()));
		model.addAttribute("isReasonRequired", this.systemInstallDAO.getSystemInstall("isReasonRequired") == null ? "no" : this.systemInstallDAO.getSystemInstall("isReasonRequired").getValue());

		return "delivery/batchEditSMTDeliveryState";
	}

	/**
	 * 配送订单再次反馈功能
	 *
	 * @param model
	 * @param cwbdetails
	 * @param deliverystate
	 * @param backreasonid
	 * @param leavedreasonid
	 * @param paytype
	 * @return
	 */
	@RequestMapping("/rebatchEditDeliveryState")
	public String rebatchEditDeliveryState(Model model, @RequestParam(value = "cwbdetails", required = true, defaultValue = "") String cwbdetails,
			@RequestParam(defaultValue = "0", required = true, value = "deliverystate") long deliverystate,
			@RequestParam(defaultValue = "0", required = true, value = "backreasonid") long backreasonid,
			@RequestParam(defaultValue = "0", required = true, value = "leavedreasonid") long leavedreasonid,
			@RequestParam(defaultValue = "", required = false, value = "resendtime") String resendtime, @RequestParam(defaultValue = "1", required = false, value = "paytype") long paytype,
			@RequestParam(defaultValue = "", required = false, value = "deliverstateremark") String deliverstateremark) {
		List<Reason> backreasonlist = this.reasonDao.getAllReasonByReasonType(ReasonTypeEnum.ReturnGoods.getValue());
		List<Reason> leavedreasonlist = this.reasonDao.getAllReasonByReasonType(ReasonTypeEnum.BeHelpUp.getValue());

		List<User> deliverList = this.userDAO.getUserByRoleAndBranchid(2, this.getSessionUser().getBranchid());

		List<Customer> customerList = this.customerDAO.getAllCustomers();// 获取供货商列表

		List<JSONObject> objList = new ArrayList<JSONObject>();

		JSONArray rJson = JSONArray.fromObject(cwbdetails);
		long successcount = 0;

		for (int i = 0; i < rJson.size(); i++) {
			String cwbdetail = rJson.getString(i);
			if (cwbdetail.equals("") || (cwbdetail.indexOf("_s_") == -1)) {
				continue;
			}
			String[] cwb_reasonid = cwbdetail.split("_s_");
			JSONObject obj = new JSONObject();
			if (cwb_reasonid.length == 2) {
				String scancwb = cwb_reasonid[0] == null ? "0" : cwb_reasonid[0];
				obj.put("cwb", scancwb);
				try {// 成功订单
					Map<String, Object> parameters = new HashMap<String, Object>();
					parameters.put("deliverid", Long.parseLong(cwb_reasonid[1] == null ? "0" : cwb_reasonid[1].indexOf("w") > -1 ? "0" : cwb_reasonid[1]));
					parameters.put("podresultid", deliverystate);
					parameters.put("backreasonid", backreasonid);
					parameters.put("leavedreasonid", leavedreasonid);
					parameters.put("podremarkid", 0l);
					parameters.put("posremark", "");
					parameters.put("checkremark", "");
					parameters.put("deliverstateremark", "");
					parameters.put("owgid", 0);
					parameters.put("sessionbranchid", this.getSessionUser().getBranchid());
					parameters.put("sessionuserid", this.getSessionUser().getUserid());
					parameters.put("sign_typeid", SignTypeEnum.BenRenQianShou.getValue());
					parameters.put("sign_time", DateTimeUtil.getNowTime());
					parameters.put("isbatch", true);
					parameters.put("paywayid", paytype);
					parameters.put("weishuakareasonid", Long.parseLong(cwb_reasonid[1] == null ? "0" : cwb_reasonid[1].indexOf("w") > -1 ? cwb_reasonid[1].replaceAll("w", "") : "0"));
					parameters.put("resendtime", resendtime);
					parameters.put("deliverstateremark", deliverstateremark);

					this.cwborderService.deliverStatePod(this.getSessionUser(), scancwb, scancwb, parameters);
					obj.put("cwbOrder", JSONObject.fromObject(this.cwbDAO.getCwbByCwb(scancwb)));
					obj.put("errorcode", "000000");
					successcount++;
				} catch (CwbException ce) {// 出现验证错误
					CwbOrder cwbOrder = this.cwbDAO.getCwbByCwb(scancwb);
					this.exceptionCwbDAO.createExceptionCwb(scancwb, ce.getFlowordertye(), ce.getMessage(), this.getSessionUser().getBranchid(), this.getSessionUser().getUserid(),
							cwbOrder == null ? 0 : cwbOrder.getCustomerid(), 0, 0, 0, "");
					obj.put("cwbOrder", cwbOrder);
					obj.put("errorcode", ce.getError().getValue());
					obj.put("errorinfo", ce.getMessage());
					if (cwbOrder == null) {// 如果无此订单
						obj.put("customer", "");
						obj.put("daohuoTime", "");
					} else {
						for (Customer c : customerList) {
							if (c.getCustomerid() == cwbOrder.getCustomerid()) {
								obj.put("customer", c.getCustomername());
								break;
							}
						}
						List<OrderFlow> ofList = this.orderFlowDAO.getOrderFlowByCwb(scancwb);

						OrderFlow of = this.cwborderService.getOrderFlowByOrderFlowListCwbAndTypes(ofList, FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao, FlowOrderTypeEnum.FenZhanDaoHuoYouHuoWuDanSaoMiao);
						obj.put("daohuoTime", of == null ? "" : DateTimeUtil.formatDate(of.getCredate()));
					}
				}
			} else {
				CwbOrder cwbOrder = this.cwbDAO.getCwbByCwb(cwb_reasonid[0]);
				for (Customer c : customerList) {
					if (c.getCustomerid() == cwbOrder.getCustomerid()) {
						obj.put("customer", c.getCustomername());
						break;
					}
				}

				List<OrderFlow> ofList = this.orderFlowDAO.getOrderFlowByCwb(cwb_reasonid[0]);
				obj.put("daohuoTime",
						DateTimeUtil.formatDate(this.cwborderService.getOrderFlowByOrderFlowListCwbAndTypes(ofList, FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao,
								FlowOrderTypeEnum.FenZhanDaoHuoYouHuoWuDanSaoMiao).getCredate()));
				obj.put("cwbOrder", JSONObject.fromObject(cwbOrder));
				if (cwb_reasonid[1].indexOf("w") > -1) {
					obj.put("errorcode", ExceptionCwbErrorTypeEnum.Wei_Shua_Ka_Yuan_Yin.getValue());
					obj.put("errorinfo", ExceptionCwbErrorTypeEnum.Wei_Shua_Ka_Yuan_Yin.getText());
				} else {
					obj.put("errorcode", "555555");
					obj.put("errorinfo", "请检查小件员是否选择");
				}
			}
			objList.add(obj);
		}
		List<Reason> weishuakareasonlist = this.reasonDao.getAllReasonByReasonType(ReasonTypeEnum.WeiShuaKa.getValue());

		model.addAttribute("successcount", successcount);
		model.addAttribute("backreasonid", backreasonid);
		model.addAttribute("leavedreasonid", leavedreasonid);
		model.addAttribute("paytype", paytype);
		model.addAttribute("objList", objList);
		model.addAttribute("deliverList", deliverList);
		model.addAttribute("deliverystate", DeliveryStateEnum.getByValue((int) deliverystate).getText());
		model.addAttribute("deliverystateid", deliverystate);
		model.addAttribute("backreasonlist", backreasonlist);
		model.addAttribute("leavedreasonlist", leavedreasonlist);
		model.addAttribute("weishuakareasonlist", weishuakareasonlist);
		model.addAttribute("deliverstateremark", deliverstateremark);
		model.addAttribute("batchEditDeliveryStateisUseCash",
				this.systemInstallDAO.getSystemInstall("batchEditDeliveryStateisUseCash") == null ? "no" : this.systemInstallDAO.getSystemInstall("batchEditDeliveryStateisUseCash").getValue());

		return "delivery/batchEditDeliveryState";
	}

	/**
	 * 上门换订单再次批量反馈功能
	 *
	 * @param model
	 * @param cwbdetails
	 * @param deliverystate
	 * @param paytype
	 * @return
	 */
	@RequestMapping("/rebatchEditSMHDeliveryState")
	public String rebatchEditSMHDeliveryState(Model model, @RequestParam(value = "cwbdetails", required = true, defaultValue = "") String cwbdetails,
			@RequestParam(defaultValue = "0", required = true, value = "deliverystate") long deliverystate, @RequestParam(defaultValue = "1", required = false, value = "paytype") long paytype,
			@RequestParam(defaultValue = "0", required = false, value = "backreasonid") long backreasonid) {

		List<User> deliverList = this.userDAO.getUserByRoleAndBranchid(2, this.getSessionUser().getBranchid());

		List<Customer> customerList = this.customerDAO.getAllCustomers();// 获取供货商列表

		List<JSONObject> objList = new ArrayList<JSONObject>();

		JSONArray rJson = JSONArray.fromObject(cwbdetails);
		long successcount = 0;

		for (int i = 0; i < rJson.size(); i++) {
			String cwbdetail = rJson.getString(i);
			if (cwbdetail.equals("") || (cwbdetail.indexOf("_s_") == -1)) {
				continue;
			}
			String[] cwb_reasonid = cwbdetail.split("_s_");
			JSONObject obj = new JSONObject();
			if (cwb_reasonid.length == 2) {
				String scancwb = cwb_reasonid[0] == null ? "0" : cwb_reasonid[0];
				obj.put("cwb", scancwb);
				try {// 成功订单
					Map<String, Object> parameters = new HashMap<String, Object>();
					parameters.put("deliverid", Long.parseLong(cwb_reasonid[1] == null ? "0" : cwb_reasonid[1].indexOf("w") > -1 ? "0" : cwb_reasonid[1]));
					parameters.put("podresultid", deliverystate);
					parameters.put("backreasonid", backreasonid);
					parameters.put("leavedreasonid", 0l);
					parameters.put("podremarkid", 0l);
					parameters.put("posremark", "");
					parameters.put("checkremark", "");
					parameters.put("deliverstateremark", "");
					parameters.put("owgid", 0);
					parameters.put("sessionbranchid", this.getSessionUser().getBranchid());
					parameters.put("sessionuserid", this.getSessionUser().getUserid());
					parameters.put("sign_typeid", SignTypeEnum.BenRenQianShou.getValue());
					parameters.put("sign_time", DateTimeUtil.getNowTime());
					parameters.put("isbatch", true);
					parameters.put("paywayid", paytype);
					parameters.put("weishuakareasonid", 0l);

					this.cwborderService.deliverStatePod(this.getSessionUser(), scancwb, scancwb, parameters);
					obj.put("cwbOrder", JSONObject.fromObject(this.cwbDAO.getCwbByCwb(scancwb)));
					obj.put("errorcode", "000000");
					successcount++;
				} catch (CwbException ce) {// 出现验证错误
					CwbOrder cwbOrder = this.cwbDAO.getCwbByCwb(scancwb);
					this.exceptionCwbDAO.createExceptionCwb(scancwb, ce.getFlowordertye(), ce.getMessage(), this.getSessionUser().getBranchid(), this.getSessionUser().getUserid(),
							cwbOrder == null ? 0 : cwbOrder.getCustomerid(), 0, 0, 0, "");
					obj.put("cwbOrder", cwbOrder);
					obj.put("errorcode", ce.getError().getValue());
					obj.put("errorinfo", ce.getMessage());
					if (cwbOrder == null) {// 如果无此订单
						obj.put("customer", "");
						obj.put("daohuoTime", "");
					} else {
						for (Customer c : customerList) {
							if (c.getCustomerid() == cwbOrder.getCustomerid()) {
								obj.put("customer", c.getCustomername());
								break;
							}
						}
						List<OrderFlow> ofList = this.orderFlowDAO.getOrderFlowByCwb(scancwb);

						OrderFlow of = this.cwborderService.getOrderFlowByOrderFlowListCwbAndTypes(ofList, FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao, FlowOrderTypeEnum.FenZhanDaoHuoYouHuoWuDanSaoMiao);
						obj.put("daohuoTime", of == null ? "" : DateTimeUtil.formatDate(of.getCredate()));
					}
				}
			} else {
				CwbOrder cwbOrder = this.cwbDAO.getCwbByCwb(cwb_reasonid[0]);
				for (Customer c : customerList) {
					if (c.getCustomerid() == cwbOrder.getCustomerid()) {
						obj.put("customer", c.getCustomername());
						break;
					}
				}

				List<OrderFlow> ofList = this.orderFlowDAO.getOrderFlowByCwb(cwb_reasonid[0]);
				obj.put("daohuoTime",
						DateTimeUtil.formatDate(this.cwborderService.getOrderFlowByOrderFlowListCwbAndTypes(ofList, FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao,
								FlowOrderTypeEnum.FenZhanDaoHuoYouHuoWuDanSaoMiao).getCredate()));
				obj.put("cwbOrder", JSONObject.fromObject(cwbOrder));
				obj.put("errorcode", "555555");
				obj.put("errorinfo", "请检查小件员是否选择");
			}
			objList.add(obj);
		}

		model.addAttribute("successcount", successcount);
		model.addAttribute("paytype", paytype);
		model.addAttribute("objList", objList);
		model.addAttribute("deliverList", deliverList);
		model.addAttribute("deliverystate", DeliveryStateEnum.getByValue((int) deliverystate).getText());
		model.addAttribute("deliverystateid", deliverystate);
		return "delivery/batchEditSMHDeliveryState";
	}

	/**
	 * 上门退订单再次批量反馈
	 *
	 * @param model
	 * @param cwbdetails
	 * @param deliverystate
	 * @param backreasonid
	 * @param leavedreasonid
	 * @param paytype
	 * @return
	 */
	@RequestMapping("/rebatchEditSMTDeliveryState")
	public String rebatchEditSMTDeliveryState(Model model, @RequestParam(value = "cwbdetails", required = true, defaultValue = "") String cwbdetails,
			@RequestParam(defaultValue = "0", required = true, value = "deliverystate") long deliverystate,
			@RequestParam(defaultValue = "0", required = true, value = "backreasonid") long backreasonid,
			@RequestParam(defaultValue = "", required = true, value = "deliverstateremark") String deliverstateremark) {
		List<User> deliverList = this.userDAO.getUserByRoleAndBranchid(2, this.getSessionUser().getBranchid());

		List<Customer> customerList = this.customerDAO.getAllCustomers();// 获取供货商列表

		List<JSONObject> objList = new ArrayList<JSONObject>();

		JSONArray rJson = JSONArray.fromObject(cwbdetails);
		long successcount = 0;

		for (int i = 0; i < rJson.size(); i++) {
			String cwbdetail = rJson.getString(i);
			if (cwbdetail.equals("") || (cwbdetail.indexOf("_s_") == -1)) {
				continue;
			}
			String[] cwb_reasonid = cwbdetail.split("_s_");
			JSONObject obj = new JSONObject();
			if (cwb_reasonid.length == 2) {
				String scancwb = cwb_reasonid[0] == null ? "0" : cwb_reasonid[0];
				obj.put("cwb", scancwb);
				try {// 成功订单
					Map<String, Object> parameters = new HashMap<String, Object>();
					parameters.put("deliverid", Long.parseLong(cwb_reasonid[1] == null ? "0" : cwb_reasonid[1].indexOf("w") > -1 ? "0" : cwb_reasonid[1]));
					parameters.put("podresultid", deliverystate);
					parameters.put("backreasonid", backreasonid);
					parameters.put("leavedreasonid", 0l);
					parameters.put("podremarkid", 0l);
					parameters.put("posremark", "");
					parameters.put("checkremark", "");
					parameters.put("deliverstateremark", deliverstateremark);
					parameters.put("owgid", 0);
					parameters.put("sessionbranchid", this.getSessionUser().getBranchid());
					parameters.put("sessionuserid", this.getSessionUser().getUserid());
					parameters.put("sign_typeid", SignTypeEnum.BenRenQianShou.getValue());
					parameters.put("sign_time", DateTimeUtil.getNowTime());
					parameters.put("isbatch", true);
					parameters.put("weishuakareasonid", 0l);
					if (DeliveryStateEnum.ShangMenJuTui.getValue() == deliverystate) {
						parameters.put("isjutui", true);
					}
					this.cwborderService.deliverStatePod(this.getSessionUser(), scancwb, scancwb, parameters);
					obj.put("cwbOrder", JSONObject.fromObject(this.cwbDAO.getCwbByCwb(scancwb)));
					obj.put("errorcode", "000000");
					successcount++;
				} catch (CwbException ce) {// 出现验证错误
					CwbOrder cwbOrder = this.cwbDAO.getCwbByCwb(scancwb);
					this.exceptionCwbDAO.createExceptionCwb(scancwb, ce.getFlowordertye(), ce.getMessage(), this.getSessionUser().getBranchid(), this.getSessionUser().getUserid(),
							cwbOrder == null ? 0 : cwbOrder.getCustomerid(), 0, 0, 0, "");
					obj.put("cwbOrder", cwbOrder);
					obj.put("errorcode", ce.getError().getValue());
					obj.put("errorinfo", ce.getMessage());
					if (cwbOrder == null) {// 如果无此订单
						obj.put("customer", "");
						obj.put("daohuoTime", "");
					} else {
						for (Customer c : customerList) {
							if (c.getCustomerid() == cwbOrder.getCustomerid()) {
								obj.put("customer", c.getCustomername());
								break;
							}
						}
						List<OrderFlow> ofList = this.orderFlowDAO.getOrderFlowByCwb(scancwb);

						OrderFlow of = this.cwborderService.getOrderFlowByOrderFlowListCwbAndTypes(ofList, FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao, FlowOrderTypeEnum.FenZhanDaoHuoYouHuoWuDanSaoMiao);
						obj.put("daohuoTime", of == null ? "" : DateTimeUtil.formatDate(of.getCredate()));
					}
				}
			} else {
				CwbOrder cwbOrder = this.cwbDAO.getCwbByCwb(cwb_reasonid[0]);
				for (Customer c : customerList) {
					if (c.getCustomerid() == cwbOrder.getCustomerid()) {
						obj.put("customer", c.getCustomername());
						break;
					}
				}

				List<OrderFlow> ofList = this.orderFlowDAO.getOrderFlowByCwb(cwb_reasonid[0]);
				obj.put("daohuoTime",
						DateTimeUtil.formatDate(this.cwborderService.getOrderFlowByOrderFlowListCwbAndTypes(ofList, FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao,
								FlowOrderTypeEnum.FenZhanDaoHuoYouHuoWuDanSaoMiao).getCredate()));
				obj.put("cwbOrder", JSONObject.fromObject(cwbOrder));
				obj.put("errorcode", "555555");
				obj.put("errorinfo", "请检查小件员是否选择");
			}
			objList.add(obj);
		}
		List<Reason> backreasonlist = this.reasonDao.getAllReasonByReasonType(ReasonTypeEnum.ReturnGoods.getValue());

		model.addAttribute("successcount", successcount);
		model.addAttribute("deliverstateremark", deliverstateremark);
		model.addAttribute("backreasonid", backreasonid);
		model.addAttribute("backreasonlist", backreasonlist);
		model.addAttribute("objList", objList);
		model.addAttribute("deliverList", deliverList);
		model.addAttribute("deliverystate", DeliveryStateEnum.getByValue((int) deliverystate).getText());
		model.addAttribute("deliverystateid", deliverystate);
		model.addAttribute("isReasonRequired", this.systemInstallDAO.getSystemInstall("isReasonRequired") == null ? "no" : this.systemInstallDAO.getSystemInstall("isReasonRequired").getValue());
		return "delivery/batchEditSMTDeliveryState";
	}

	@RequestMapping("/list/{page}")
	public String list(@PathVariable("page") long page, Model model, @RequestParam(value = "deliverealuser", required = false, defaultValue = "0") long deliverealuser,
			@RequestParam(value = "begindate", required = false, defaultValue = "") String begindate, @RequestParam(value = "enddate", required = false, defaultValue = "") String enddate) {

		if (begindate.length() == 0) {
			begindate = this.df_d.format(new Date());
		}
		if (enddate.length() == 0) {
			enddate = this.df_d.format(new Date());
		}
		begindate += " 00:00:00";
		enddate += " 23:59:59";

		List<GotoClassAuditing> gotoClassAuditingByPage = this.gotoClassAuditingDAO.getGotoClassAuditingByPage(page, deliverealuser, begindate, enddate, this.getSessionUser().getBranchid());
		List<gotoClassAuditingView> gotoClassAuditingViews = new ArrayList<gotoClassAuditingView>();
		for (GotoClassAuditing gotoClassAuditing : gotoClassAuditingByPage) {
			gotoClassAuditingView view = new gotoClassAuditingView();
			BeanUtils.copyProperties(gotoClassAuditing, view);
			view.setDeliverealuser_name(this.userDAO.getUserByUserid(gotoClassAuditing.getDeliverealuser()).getRealname());
			view.setReceivedfeeuser_name(this.userDAO.getUserByUserid(gotoClassAuditing.getReceivedfeeuser()).getRealname());
			gotoClassAuditingViews.add(view);
		}
		model.addAttribute("gcas", gotoClassAuditingViews);
		model.addAttribute("page_obj", new Page(this.gotoClassAuditingDAO.getGotoClassAuditingCount(deliverealuser, begindate, enddate, this.getSessionUser().getBranchid()), page,
				Page.ONE_PAGE_NUMBER));
		model.addAttribute("page", page);
		model.addAttribute("us", this.userDAO.getUserByRole("2,4", this.getSessionUser().getBranchid()));
		SystemInstall usedeliverpay = this.systemInstallDAO.getSystemInstallByName("usedeliverpayup");
		model.addAttribute("usedeliverpayup", usedeliverpay == null ? "no" : usedeliverpay.getValue());
		return "delivery/list";
	}

	@RequestMapping("/B2CInterface")
	public @ResponseBody String B2CInterface(@RequestParam(value = "pushtime", required = false, defaultValue = "") String pushtime,
			@RequestParam(value = "pushstate", required = false, defaultValue = "0") long pushstate, @RequestParam(value = "pushremarks", required = false, defaultValue = "") String pushremarks,
			@RequestParam(value = "deliverystate", required = false, defaultValue = "0") long deliverystate, @RequestParam(value = "cwb", required = false, defaultValue = "") String cwb) {
		try {
			JSONObject obj = new JSONObject();
			obj.put("cwb", cwb);
			obj.put("deliverystate", deliverystate);
			if (pushstate != B2cPushStateEnum.TuiSongChengGong.getValue()) {
				pushtime = "";
			}
			this.deliveryStateDAO.saveDeliveryStateForB2C(pushtime, pushstate, pushremarks, cwb, deliverystate);
			return "{\"errorCode\":0,\"message\":\"" + cwb + "推送结果处理成功" + pushstate + "\"}";
		} catch (Exception e) {
			return "{\"errorCode\":1,\"message\":\"系统错误:\"" + e.getMessage() + "}";
		}
	}

	@ExceptionHandler(Exception.class)
	public @ResponseBody String onException(Exception e) {
		e.printStackTrace();
		return "{\"errorCode\":1,\"error\":\"系统错误:\"" + e.getMessage() + "}";
	}

	@ExceptionHandler(CwbException.class)
	public @ResponseBody String onCwbException(CwbException e) {
		e.printStackTrace();
		return "{\"errorCode\":1,\"cwb\":\"" + e.getCwb() + "\",\"error\":\"系统错误:\"" + e.getMessage() + "}";
	}

	/**
	 * 小件员领货查询 1.小件员自己能看见自己的 2.
	 */
	@RequestMapping("/searchDeliveryLead/{page}")
	public String searchDeliveryLead(@PathVariable("page") long page, Model model, @RequestParam(value = "deliverystate", required = false, defaultValue = "-1") long deliverystate,
			@RequestParam(value = "startid", required = false, defaultValue = "") String begindate, @RequestParam(value = "endid", required = false, defaultValue = "") String enddate) {
		long roleid = this.getSessionUser().getRoleid();
		long deliveryid = -1;
		long branchid = -1;
		if (roleid == 4) {// 站长
			branchid = this.getSessionUser().getBranchid();
		}
		if (roleid == 2) {// 小件员
			deliveryid = this.getSessionUser().getUserid();
		}
		List<CwbSearchDelivery> conSoleList = new ArrayList<CwbSearchDelivery>();
		if ((begindate.length() > 0) && (enddate.length() > 0)) {
			List<CwbOrder> cwbList = this.cwbDAO.getCwbByDeliveryStateAndBranchid(deliveryid, deliverystate);
			List<DeliveryState> desList = this.deliveryStateDAO.getCwbAndDeliveryByToPagedeliverystate(page, branchid, deliveryid, begindate, enddate, deliverystate);
			// 两表关联
			conSoleList = this.getJointCwbAndDelivery(cwbList, desList);
			model.addAttribute("page_obj", new Page(this.deliveryStateDAO.getCountBydeliverystate(branchid, deliverystate, begindate, enddate, deliveryid), page, Page.ONE_PAGE_NUMBER));
			model.addAttribute("customers", this.customerDAO.getAllCustomers());
		} else {
			model.addAttribute("page_obj", new Page());
		}
		model.addAttribute("deliverylist1", conSoleList);
		model.addAttribute("startid", begindate);
		model.addAttribute("endid", enddate);
		model.addAttribute("deliverystate", deliverystate);
		model.addAttribute("page", page);
		return "delivery/deliverylistshow";

	}

	private List<CwbSearchDelivery> getJointCwbAndDelivery(List<CwbOrder> cwbList, List<DeliveryState> desList) {
		List<CwbSearchDelivery> list = new ArrayList<CwbSearchDelivery>();
		for (CwbOrder clist : cwbList) {
			for (DeliveryState dlist : desList) {
				CwbSearchDelivery csd = new CwbSearchDelivery();
				if (clist.getCwb().equals(dlist.getCwb())) {
					csd.setCwb(dlist.getCwb());
					csd.setCustomerid(dlist.getCustomerid());
					csd.setConsigneeaddress(clist.getConsigneeaddress());
					csd.setConsigneename(clist.getConsigneename());
					csd.setCreatetime(dlist.getCreatetime());
					csd.setReceivablefee(clist.getReceivablefee());
					for (DeliveryStateEnum ds : DeliveryStateEnum.values()) {
						if ((ds.getValue() == dlist.getDeliverystate()) && (dlist.getDeliverystate() != 0)) {
							csd.setStatus(ds.getText());
							break;
						} else {
							csd.setStatus("已领货");
						}
					}
					list.add(csd);
					break;
				}
			}
		}
		return list;
	}

	@RequestMapping("/exportweifankui")
	public void exportweifankui(HttpServletRequest request, HttpServletResponse response, @RequestParam(value = "deliveryId", defaultValue = "0", required = false) long deliveryId) {

		String[] cloumnName1 = new String[9]; // 导出的列名
		String[] cloumnName2 = new String[9]; // 导出的英文列名

		this.exportService.SetAuditFields(cloumnName1, cloumnName2);
		final String[] cloumnName = cloumnName1;
		final String[] cloumnName3 = cloumnName2;
		String sheetName = "订单信息"; // sheet的名称
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
		String fileName = "order_" + df.format(new Date()) + ".xlsx"; // 文件名
		try {
			List<DeliveryState> deliveryStateList = this.deliveryStateDAO.getDeliveryStateByDeliver(deliveryId);
			List<DeliveryStateView> cwbOrderWithDeliveryState = this.getDeliveryStateViews(deliveryStateList, null);
			DeliveryStateDTO deliveryStateDTO = new DeliveryStateDTO();
			deliveryStateDTO.analysisDeliveryStateList(cwbOrderWithDeliveryState);
			final List<DeliveryStateView> views = deliveryStateDTO.getWeifankuiList();
			ExcelUtils excelUtil = new ExcelUtils() { // 生成工具类实例，并实现填充数据的抽象方法
				@Override
				public void fillData(Sheet sheet, CellStyle style) {
					for (int k = 0; k < views.size(); k++) {
						Row row = sheet.createRow(k + 1);
						row.setHeightInPoints(15);
						for (int i = 0; i < cloumnName.length; i++) {
							Cell cell = row.createCell((short) i);
							cell.setCellStyle(style);
							Object a = null;
							// 给导出excel赋值
							a = DeliveryController.this.exportService.setAuditWeifankuiObject(cloumnName3, views, a, i, k);
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

	@RequestMapping("/exportyifankui")
	public void exportyifankui(HttpServletResponse response, @RequestParam(value = "deliveryId", defaultValue = "0", required = false) long deliveryId) {

		String[] cloumnName1 = new String[15]; // 导出的列名
		String[] cloumnName2 = new String[15]; // 导出的英文列名

		this.exportService.SetAuditFieldsYiFanKui(cloumnName1, cloumnName2);
		final String[] cloumnName = cloumnName1;
		final String[] cloumnName3 = cloumnName2;
		String sheetName = "订单信息"; // sheet的名称
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
		String fileName = "order_" + df.format(new Date()) + ".xlsx"; // 文件名
		try {
			List<DeliveryState> deliveryStateList = this.deliveryStateDAO.getDeliveryStateByDeliver(deliveryId);
			List<DeliveryStateView> cwbOrderWithDeliveryState = this.getDeliveryStateViews(deliveryStateList, null);
			DeliveryStateDTO deliveryStateDTO = new DeliveryStateDTO();
			deliveryStateDTO.analysisDeliveryStateList(cwbOrderWithDeliveryState);
			cwbOrderWithDeliveryState.removeAll(deliveryStateDTO.getWeifankuiList());
			final List<DeliveryStateView> views = cwbOrderWithDeliveryState;
			ExcelUtils excelUtil = new ExcelUtils() { // 生成工具类实例，并实现填充数据的抽象方法
				@Override
				public void fillData(Sheet sheet, CellStyle style) {
					for (int k = 0; k < views.size(); k++) {
						Row row = sheet.createRow(k + 1);
						row.setHeightInPoints(15);
						for (int i = 0; i < cloumnName.length; i++) {
							Cell cell = row.createCell((short) i);
							cell.setCellStyle(style);
							Object a = null;
							// 给导出excel赋值
							a = DeliveryController.this.exportService.setAuditYifankuiObject(cloumnName3, views, a, i, k);
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
	 * 修改签收人
	 *
	 * @param model
	 * @param request
	 * @param cwbs
	 * @param deliverystate
	 * @param backreasonid
	 * @param paytype
	 * @param leavedreasonid
	 * @param resendtime
	 * @param zhiliuremark
	 * @return
	 */
	@RequestMapping("/batchEditXGSJRDeliveryState")
	public String batchEditXGSJRDeliveryState(Model model, HttpServletRequest request, @RequestParam(value = "cwbs", required = false, defaultValue = "") String cwbs,
			@RequestParam(defaultValue = "", required = false, value = "consignName") String consignName) {

		List<User> deliverList = this.userDAO.getDeliveryUserByRolesAndBranchid("2,4", this.getSessionUser().getBranchid());

		long successcount = 0;
		this.logger.info("反馈(批量)-修改收件人,cwb:{}，收件人改为{}", cwbs, consignName);
		try {// 成功订单
			int cwbCount = this.cwbDAO.countDeliverystateAndCwb(cwbs);
			if (cwbCount > 0) {
				this.deliveryStateDAO.updateDeliveryStateConsigneenameByCwb(consignName, cwbs);
				this.logger.info("修改收件人完毕,cwb:{}，收件人改为{}", cwbs, consignName);
				successcount++;
			}
		} catch (CwbException ce) {// 出现验证错误
			CwbOrder cwbOrder = this.cwbDAO.getCwbByCwb(cwbs);
			this.exceptionCwbDAO.createExceptionCwb(cwbs, ce.getFlowordertye(), ce.getMessage(), this.getSessionUser().getBranchid(), this.getSessionUser().getUserid(), cwbOrder == null ? 0
					: cwbOrder.getCustomerid(), 0, 0, 0, "");

		}
		model.addAttribute("successcount", successcount);
		model.addAttribute("deliverList", deliverList);
		return "delivery/batchEditXGSJRDeliveryState";
	}

	/**
	 * 触发的ajax
	 *
	 * @param model
	 * @param request
	 * @param cwb
	 * @return
	 */
	@RequestMapping("/checkBoxForff")
	public @ResponseBody int checkBoxFor(Model model, HttpServletRequest request, @RequestParam(value = "cwbs", required = false, defaultValue = "") String cwb) {
		int flag = 0;
		CwbOrder cwbOrder = this.cwbDAO.getCwbByCwb(cwb);
		if (cwbOrder == null) {// 如果无此订单
			flag = 2;
			return flag;
		}
		int cwbCount = this.cwbDAO.countDeliverystateAndCwb(cwb);// 判断是不是反馈为配送成功状态的订单
		if (cwbCount == 0) {
			return flag = 1;
		}
		return flag;
	}
}
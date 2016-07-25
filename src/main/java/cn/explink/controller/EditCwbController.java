package cn.explink.controller;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.HashSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.explink.b2c.tools.DataImportDAO_B2c;
import cn.explink.core.utils.StringUtils;
import cn.explink.dao.AccountCwbFareDetailDAO;
import cn.explink.dao.AppearWindowDao;
import cn.explink.dao.BranchDAO;
import cn.explink.dao.CwbDAO;
import cn.explink.dao.DeliveryStateDAO;
import cn.explink.dao.EditCwbDAO;
import cn.explink.dao.EmailDateDAO;
import cn.explink.dao.OperationTimeDAO;
import cn.explink.dao.OrderAddressReviseDao;
import cn.explink.dao.OrderFlowDAO;
import cn.explink.dao.UserDAO;
import cn.explink.dao.ZhiFuApplyDao;
import cn.explink.dao.searchEditCwbInfoDao;
import cn.explink.domain.Branch;
import cn.explink.domain.CwbOrder;
import cn.explink.domain.CwbOrderBranchMatchVo;
import cn.explink.domain.EdtiCwb_DeliveryStateDetail;
import cn.explink.domain.EmailDate;
import cn.explink.domain.FeeWayTypeRemark;
import cn.explink.domain.OrderAddressRevise;
import cn.explink.domain.SearcheditInfo;
import cn.explink.domain.User;
import cn.explink.domain.WindowShow;
import cn.explink.domain.ZhiFuApplyView;
import cn.explink.enumutil.ApplyEnum;
import cn.explink.enumutil.BranchEnum;
import cn.explink.enumutil.CwbOrderAddressCodeEditTypeEnum;
import cn.explink.enumutil.CwbOrderTypeIdEnum;
import cn.explink.enumutil.EditCwbTypeEnum;
import cn.explink.enumutil.FlowOrderTypeEnum;
import cn.explink.exception.ExplinkException;
import cn.explink.pos.tools.JacksonMapper;
import cn.explink.service.AdjustmentRecordService;
import cn.explink.service.BranchService;
import cn.explink.service.CwbOrderService;
import cn.explink.service.CwbOrderWithDeliveryState;
import cn.explink.service.DataImportService;
import cn.explink.service.EditCwbService;
import cn.explink.service.ExplinkUserDetail;
import cn.explink.service.ExportService;
import cn.explink.service.OrgBillAdjustmentRecordService;
import cn.explink.service.UserService;
import cn.explink.service.addressmatch.AddressMatchService;
import cn.explink.support.transcwb.TransCwbDao;
import cn.explink.util.DateTimeUtil;
import cn.explink.util.ExcelUtils;
import cn.explink.util.JSONReslutUtil;
import cn.explink.util.Page;
import cn.explink.util.SecurityUtil;
import cn.explink.util.StringUtil;

@RequestMapping("/editcwb")
@Controller
public class EditCwbController {
	@Autowired
	AddressMatchService addressMatchService;
	@Autowired
	CwbDAO cwbDAO;
	@Autowired
	ExportService exportService;
	@Autowired
	AppearWindowDao appearWindowDao;
	@Autowired
	DataImportDAO_B2c dataImportDAO_B2c;
	@Autowired
	DeliveryStateDAO deliveryStateDAO;
	@Autowired
	UserDAO userDAO;
	@Autowired
	TransCwbDao transCwbDao;
	@Autowired
	BranchDAO branchDAO;
	@Autowired
	CwbOrderService cwborderService;
	@Autowired
	OrderFlowDAO orderFlowDAO;
	@Autowired
	DataImportService dataImportService;
	@Autowired
	EditCwbService editCwbService;

	@Autowired
	EditCwbDAO editCwbDAO;
	@Autowired
	CwbOrderService cwbOrderService;
	@Autowired
	EmailDateDAO emaildateDAO;
	@Autowired
	OperationTimeDAO operationTimeDAO;

	@Autowired
	searchEditCwbInfoDao cwbInfoDao;
	@Autowired
	AccountCwbFareDetailDAO accountCwbFareDetailDAO;
	@Autowired
	AdjustmentRecordService adjustmentRecordService;
	@Autowired
	OrgBillAdjustmentRecordService orgBillAdjustmentRecordService;
	@Autowired
	ZhiFuApplyDao zhiFuApplyDao;

	@Autowired
	SecurityContextHolderStrategy securityContextHolderStrategy;
	@Autowired
	OrderAddressReviseDao orderAddressReviseDao;
	
	@Autowired
	private BranchService branchService;
	
	@Autowired
	private UserService userService;
	
	private Logger logger = LoggerFactory.getLogger(EditCwbController.class);

	private User getSessionUser() {
		ExplinkUserDetail userDetail = (ExplinkUserDetail) this.securityContextHolderStrategy.getContext().getAuthentication().getPrincipal();
		return userDetail.getUser();
	}

	protected static ObjectMapper jacksonmapper = JacksonMapper.getInstance();

	@RequestMapping("/start")
	public String start(Model model, @RequestParam(value = "cwbs", required = false, defaultValue = "") String cwbs, @RequestParam(value = "type", required = false, defaultValue = "0") int type) {
		this.logger.info("修改订单功能 [" + type + "][{}] cwb: {}", this.getSessionUser().getRealname(), cwbs);
		// 整理sql要读取的cwb start
		String[] cwbArray = cwbs.trim().split("\r\n");
		String s = "'";
		String s1 = "',";
		StringBuffer cwbsSqlBuffer = new StringBuffer();
		for (String cwb : cwbArray) {
			if (cwb.trim().length() == 0) {
				continue;
			}
			cwb = cwb.trim();
			if (cwb.length() > 0) {
				cwbsSqlBuffer = cwbsSqlBuffer.append(s).append(cwb).append(s1);
			}
		}
		if (cwbsSqlBuffer.length() == 0) {
			return "editcwb/start";
		}
		// 整理sql要读取的cwb end
		model.addAttribute("cwbArray", cwbArray);
		String cwbsSql = cwbsSqlBuffer.substring(0, cwbsSqlBuffer.length() - 1);
		List<CwbOrder> cwbList = this.cwbDAO.getCwbByCwbs(cwbsSql);
		//	Map<String, AccountCwbFareDetail> accountCwbFareDetailMap = this.accountCwbFareDetailDAO.getAccountCwbFareDetailMapByCwbs(cwbsSql);

		/*// 做重置审核状态更改的操作 start
		if (type == EditCwbTypeEnum.ChongZhiShenHeZhuangTai.getValue()) {
			List<CwbOrder> allowCwb = new ArrayList<CwbOrder>();// 允许更改订单
			List<CwbOrder> prohibitedCwb = new ArrayList<CwbOrder>(); // 禁止更改的订单
			for (CwbOrder co : cwbList) {

				// 判断订单当前状态为36 已审核状态的订单才能重置审核状态
				if (co.getFlowordertype() == FlowOrderTypeEnum.YiShenHe.getValue()) {
					// 判断订单号是否为POS刷卡 posremark=POS刷卡 POS刷卡的订单不允许重置审核状态
					DeliveryState ds = this.deliveryStateDAO.getDeliveryStateByCwb(co.getCwb()).get(0);
					if ((co.getInfactfare().compareTo(BigDecimal.ZERO) > 0) && ((accountCwbFareDetailMap.get(co.getCwb()) == null ? 0 : accountCwbFareDetailMap.get(co.getCwb()).getFareid()) > 0)) {
						// 暂借对象中的备注1字段输出一些提示语
						co.setRemark1("当前订单运费已交款，不可重置审核状态");
						prohibitedCwb.add(co);
					} else if (ds.getPosremark().indexOf("POS刷卡") == -1) {
						allowCwb.add(co);
					} else {
						// 暂借对象中的备注1字段输出一些提示语
						co.setRemark1("POS刷卡签收的订单审核后不允许重置审核状态");
						prohibitedCwb.add(co);
					}
				} else {
					// 暂借对象中的备注1字段输出一些提示语
					co.setRemark1("当前订单状态为[" + FlowOrderTypeEnum.getText(co.getFlowordertype()).getText() + "],不允许重置审核状态");
					prohibitedCwb.add(co);
				}

			}
			model.addAttribute("allowCwb", allowCwb);
			model.addAttribute("prohibitedCwb", prohibitedCwb);
			return "editcwb/ChongZhiShenHe";
			// 做重置审核状态更改的操作 end
		}*/
		if (type == EditCwbTypeEnum.XiuGaiJinE.getValue()) {// 修改订单金额更改操作
															// Start
			List<CwbOrderWithDeliveryState> allowCods = new ArrayList<CwbOrderWithDeliveryState>();
			List<CwbOrderWithDeliveryState> prohibitedCods = new ArrayList<CwbOrderWithDeliveryState>();
			for (CwbOrder co : cwbList) {
				CwbOrderWithDeliveryState cods = new CwbOrderWithDeliveryState();
				cods.setCwbOrder(co);
				cods.setDeliveryState(this.deliveryStateDAO.getDeliveryByCwbAndDeliverystate(co.getCwb()));
				// 存储订单表记录和反馈表记录，用于前端判断
				String completedCwb = editCwbService.getCompletedCwbByCwb(co);//已经审核过的订单不能修改订单支付信息
				if (completedCwb != null && !completedCwb.isEmpty()) { 
					cods.setError(completedCwb);
					prohibitedCods.add(cods);
				} else {
					allowCods.add(cods);
				}
			}
			model.addAttribute("allowCods", allowCods);
			model.addAttribute("prohibitedCods", prohibitedCods);
			return "editcwb/XiuGaiJinE";
			// 修改订单金额更改操作 end
		} else if (type == EditCwbTypeEnum.XiuGaiZhiFuFangShi.getValue()) {// 修改订单支付方式更改操作
																			// Start
			List<CwbOrderWithDeliveryState> allowCods = new ArrayList<CwbOrderWithDeliveryState>();
			List<CwbOrderWithDeliveryState> prohibitedCods = new ArrayList<CwbOrderWithDeliveryState>();
			for (CwbOrder co : cwbList) {
				CwbOrderWithDeliveryState cods = new CwbOrderWithDeliveryState();
				cods.setCwbOrder(co);
				cods.setDeliveryState(this.deliveryStateDAO.getDeliveryByCwbAndDeliverystate(co.getCwb()));
				String completedCwb = editCwbService.getCompletedCwbByCwb(co);
				if (completedCwb != null && !completedCwb.isEmpty()) { //已经审核过的订单不能修改订单支付信息
				    cods.setError(completedCwb);
					prohibitedCods.add(cods);
				} else if (co.getReceivablefee().compareTo(BigDecimal.ZERO) <= 0 || co.getDeliverystate() == 0) { // 存储订单表记录和反馈表记录，用于前端判断 如果代收金额
					cods.setError("代收货款应收金额少于等于0或者未反馈的订单不允许修改订单支付方式");
					prohibitedCods.add(cods);
				} else {
					allowCods.add(cods);
				}
			}
			model.addAttribute("allowCods", allowCods);
			model.addAttribute("prohibitedCods", prohibitedCods);
			return "editcwb/XiuGaiZhiFuFangShi";
			// 修改订单支付方式更改操作 end
		} else if (type == EditCwbTypeEnum.XiuGaiDingDanLeiXing.getValue()) {// 修改订单类型更改操作
																				// Start
			List<CwbOrderWithDeliveryState> allowCods = new ArrayList<CwbOrderWithDeliveryState>();
			List<CwbOrderWithDeliveryState> prohibitedCods = new ArrayList<CwbOrderWithDeliveryState>();
			for (CwbOrder co : cwbList) {
				CwbOrderWithDeliveryState cods = new CwbOrderWithDeliveryState();
				cods.setCwbOrder(co);
				cods.setDeliveryState(this.deliveryStateDAO.getDeliveryByCwbAndDeliverystate(co.getCwb()));
				String completedCwb = editCwbService.getCompletedCwbByCwb(co);
				if (completedCwb != null && !completedCwb.isEmpty()) {
					cods.setError(completedCwb); //已审核过的订单不允许修改订单类型
					prohibitedCods.add(cods);
				} else if ((cods.getDeliveryState() != null) && (co.getCwbordertypeid() == CwbOrderTypeIdEnum.Shangmentui.getValue()) && (cods.getDeliveryState().getInfactfare()
						.compareTo(BigDecimal.ZERO) > 0)) {
					cods.setError("上门退有应收运费的订单不允许修改订单类型");
					prohibitedCods.add(cods);
				} else if ((cods.getDeliveryState() != null) && (cods.getDeliveryState().getGcaid() > 0)) { // 已经归班的订单不能修改订单类型，必须充值审核状态才能修改
					cods.setError("已审核的订单不允许修改订单类型，若要修改，请重置审核状态后再试");
					prohibitedCods.add(cods);
				} else {
					allowCods.add(cods);
				}
			}
			model.addAttribute("allowCods", allowCods);
			model.addAttribute("prohibitedCods", prohibitedCods);
			return "editcwb/XiuGaiDingDanLeiXing";
			// 修改订单订单类型更改操作 end
		} else if (type == EditCwbTypeEnum.XiuGaiKaiDiYunFeiJinE.getValue()) {// 修改快递单运费金额操作
			List<CwbOrderWithDeliveryState> allowCods = new ArrayList<CwbOrderWithDeliveryState>();
			for (CwbOrder co : cwbList) {
				// 存储订单表记录和反馈表记录，用于前端判断
				CwbOrderWithDeliveryState cods = new CwbOrderWithDeliveryState();
				cods.setCwbOrder(co);
				cods.setDeliveryState(this.deliveryStateDAO.getDeliveryByCwbAndDeliverystate(co.getCwb()));
				String completedCwb = editCwbService.getCompletedCwbByCwb(co); // 已经审核过的订单不允许修改支付信息
				if (completedCwb == null || completedCwb.isEmpty()) {
					//将非快递单过滤掉
					if (co.getCwbordertypeid() == CwbOrderTypeIdEnum.Express.getValue()) {
						allowCods.add(cods);
					}
				}
			}
			model.addAttribute("allowCods", allowCods);
			return "editcwb/updateExpressFee";
		}

		// 如果不属于任何操作 则回到开始页面
		return "editcwb/start";
	}

	@RequestMapping("/getUserForAutoComplete")
	public @ResponseBody List<JSONObject> getUserForAutoComplete(@RequestParam(value = "username", required = false, defaultValue = "") String username) {
		List<User> userList = new ArrayList<User>();
		List<JSONObject> jsonlist = new ArrayList<JSONObject>();
		if (username.length() > 0) {
			userList = this.userDAO.getUserForAutoComplete(username);

		}
		if (userList.size() > 0) {
			for (User user : userList) {
				JSONObject obj = new JSONObject();
				obj.put("userid", user.getUserid());
				obj.put("username", user.getRealname());
				jsonlist.add(obj);
			}
		}
		return jsonlist;
	}

	@RequestMapping("/editChongZhiShenHeZhuangTai")
	public String editChongZhiShenHeZhuangTai(Model model, @RequestParam(value = "requestUser", required = false, defaultValue = "0") Long requestUser, @RequestParam(value = "cwbs", required = false, defaultValue = "") String[] cwbs) {
		List<User> userList = this.userDAO.getUserByid(requestUser);
		if (userList != null) {
			this.logger.info("重置订单审核状态功能 [{}] cwb: {}", this.getSessionUser().getRealname(), StringUtil.getStringsToString(cwbs));
			List<EdtiCwb_DeliveryStateDetail> ecList = new ArrayList<EdtiCwb_DeliveryStateDetail>();
			List<String> errorList = new ArrayList<String>();
			for (String cwb : cwbs) {
				try {
					EdtiCwb_DeliveryStateDetail ec_dsd = this.editCwbService.analysisAndSaveByChongZhiShenHe(cwb, requestUser, this.getSessionUser());
					ecList.add(ec_dsd);
				} catch (ExplinkException ee) {
					errorList.add(cwb + "_" + ee.getMessage());
				} catch (Exception e) {
					errorList.add(cwb + "_" + FlowOrderTypeEnum.YiShenHe.getValue() + "_系统内部报错！");
					this.logger.error("", e);
				}
			}
			model.addAttribute("ecList", ecList);
			model.addAttribute("errorList", errorList);

		}
		return "editcwb/ChongZhiShenHeResult";
	}

	@RequestMapping("/ChongZhiShenHeZhuangTai")
	public String ChongZhiShenHeZhuangTai(Model model, @RequestParam(value = "cwbs", required = false, defaultValue = "") String[] cwbs) {
		this.logger.info("重置订单审核状态功能 [{}] cwb: {}", this.getSessionUser().getRealname(), StringUtil.getStringsToString(cwbs));
		List<EdtiCwb_DeliveryStateDetail> ecList = new ArrayList<EdtiCwb_DeliveryStateDetail>();
		List<String> errorList = new ArrayList<String>();
		for (String cwb : cwbs) {
			try {
				EdtiCwb_DeliveryStateDetail ec_dsd = this.editCwbService.analysisAndSaveByChongZhiShenHe(cwb, (long) 0, this.getSessionUser());
				ecList.add(ec_dsd);
			} catch (ExplinkException ee) {
				errorList.add(cwb + "_" + FlowOrderTypeEnum.YiShenHe.getValue() + "_" + ee.getMessage());
			} catch (Exception e) {
				errorList.add(cwb + "_" + FlowOrderTypeEnum.YiShenHe.getValue() + "_系统内部报错！");
				this.logger.error("", e);
			}
		}
		model.addAttribute("ecList", ecList);
		model.addAttribute("errorList", errorList);

		return "editcwb/chongzhiResult";
	}

	@RequestMapping("/editXiuGaiJinE2")
	public String editXiuGaiJinE2(Model model, HttpServletRequest request, @RequestParam(value = "requestUser", required = true, defaultValue = "0") Long requestUser, @RequestParam(value = "cwbs", required = false, defaultValue = "") String[] cwbs) {
		List<User> userList = this.userDAO.getUserByid(requestUser);
		if (userList.size() > 0) {
			this.logger.info("修改订单金额功能 [{}] cwb: {}", this.getSessionUser().getRealname(), StringUtil.getStringsToString(cwbs));
			List<EdtiCwb_DeliveryStateDetail> ecList = new ArrayList<EdtiCwb_DeliveryStateDetail>();
			List<String> errorList = new ArrayList<String>();
			for (String cwb : cwbs) {
				String isDeliveryState = request.getParameter("isDeliveryState_" + cwb);
				BigDecimal receivablefee = request.getParameter("Receivablefee_" + cwb) == null ? BigDecimal.ZERO : new BigDecimal(request.getParameter("Receivablefee_" + cwb));
				BigDecimal cash = request.getParameter("Receivablefee_cash_" + cwb) == null ? BigDecimal.ZERO : new BigDecimal(request.getParameter("Receivablefee_cash_" + cwb));
				BigDecimal pos = request.getParameter("Receivablefee_pos_" + cwb) == null ? BigDecimal.ZERO : new BigDecimal(request.getParameter("Receivablefee_pos_" + cwb));
				BigDecimal checkfee = request.getParameter("Receivablefee_checkfee_" + cwb) == null ? BigDecimal.ZERO : new BigDecimal(request.getParameter("Receivablefee_checkfee_" + cwb));
				BigDecimal otherfee = request.getParameter("Receivablefee_otherfee_" + cwb) == null ? BigDecimal.ZERO : new BigDecimal(request.getParameter("Receivablefee_otherfee_" + cwb));
				BigDecimal Paybackfee = request.getParameter("Paybackfee_" + cwb) == null ? BigDecimal.ZERO : new BigDecimal(request.getParameter("Paybackfee_" + cwb));
				//added by jiangyu begin
				//页面修改后的应退金额
				BigDecimal paybackfee = request.getParameter("Paybackfee_" + cwb) == null ? BigDecimal.ZERO : new BigDecimal(request.getParameter("Paybackfee_" + cwb));
				CwbOrder cwbOrder = new CwbOrder();
				cwbOrder = this.cwbDAO.getCwbByCwb(cwb);
				User user = new User();
				if (userList.size() > 0) {
					user = userList.get(0);
				}

				// 先判断是有账单 获取到修改订单金额的值,进行判断插入到数据库中
				if ((receivablefee != null) && !receivablefee.equals(cwbOrder.getReceivablefee())) {
					//					this.adjustmentRecordService.createAdjustmentRecode(cwb, cwbOrder.getCustomerid(), cwbOrder.getReceivablefee(), paybackfee, receivablefee, "", user.getUsername(),cwbOrder.getCwbordertypeid());
					//客户调整单逻辑入口
					this.adjustmentRecordService.processAdjusRecordByMoney(cwbOrder, paybackfee, receivablefee, "", user.getUsername());
					//站内调整单逻辑入口
					this.orgBillAdjustmentRecordService.createOrgBillAdjustRecord(cwbOrder, user, receivablefee, paybackfee);
				}

				//added by jiangyu end

				try {
					EdtiCwb_DeliveryStateDetail ec_dsd = this.editCwbService
							.analysisAndSaveByXiuGaiJinE(cwb, isDeliveryState, receivablefee, cash, pos, checkfee, otherfee, Paybackfee, requestUser, this.getSessionUser().getUserid());
					ecList.add(ec_dsd);
				} catch (ExplinkException ee) {
					errorList.add(cwb + "_" + ee.getMessage());
				} catch (Exception e) {
					errorList.add(cwb + "_" + FlowOrderTypeEnum.YiShenHe.getValue() + "_系统内部报错！");
					this.logger.error("", e);
				}
			}
			model.addAttribute("ecList", ecList);
			model.addAttribute("errorList", errorList);

		}
		return "editcwb/XiuGaiJinEResult";
	}

	//修改后的修改金额处理方式
	@RequestMapping("/editXiuGaiJinE")
	public String editXiuGaiJinE(Model model, HttpServletRequest request, @RequestParam(value = "requestUser", required = true, defaultValue = "0") Long requestUser, @RequestParam(value = "cwbs", required = false, defaultValue = "") String[] cwbs) throws Exception {
		//存储在订单修改确认时所需要的参数(在金额修改时需要使用)
		List<FeeWayTypeRemark> frlist = new ArrayList<FeeWayTypeRemark>();
		//	long editcwbnum = 0;
		if ((cwbs != null) && (cwbs.length > 0)) {
			for (String cwb : cwbs) {
				FeeWayTypeRemark fr = new FeeWayTypeRemark();
				String isDeliveryState = request.getParameter("isDeliveryState_" + cwb);
				//修改为代收金额
				BigDecimal receivablefee = request.getParameter("Receivablefee_" + cwb) == null ? BigDecimal.ZERO : new BigDecimal(request.getParameter("Receivablefee_" + cwb));
				//反馈代收金额
				BigDecimal cash = request.getParameter("Receivablefee_cash_" + cwb) == null ? BigDecimal.ZERO : new BigDecimal(request.getParameter("Receivablefee_cash_" + cwb));
				//反馈代收pos
				BigDecimal pos = request.getParameter("Receivablefee_pos_" + cwb) == null ? BigDecimal.ZERO : new BigDecimal(request.getParameter("Receivablefee_pos_" + cwb));
				//反馈代收支票
				BigDecimal checkfee = request.getParameter("Receivablefee_checkfee_" + cwb) == null ? BigDecimal.ZERO : new BigDecimal(request.getParameter("Receivablefee_checkfee_" + cwb));
				//反馈代收其他
				BigDecimal otherfee = request.getParameter("Receivablefee_otherfee_" + cwb) == null ? BigDecimal.ZERO : new BigDecimal(request.getParameter("Receivablefee_otherfee_" + cwb));
				//修改为代退金额
				BigDecimal Paybackfee = request.getParameter("Paybackfee_" + cwb) == null ? BigDecimal.ZERO : new BigDecimal(request.getParameter("Paybackfee_" + cwb));
				fr.setCwb(cwb);
				fr.setIsDeliveryState(isDeliveryState);
				fr.setReceivablefee(receivablefee);
				fr.setCash(cash);
				fr.setPos(pos);
				fr.setCheckfee(checkfee);
				fr.setOtherfee(otherfee);
				fr.setPaybackfee(Paybackfee);
				fr.setRequestUser(requestUser);
				frlist.add(fr);
			}

			List<User> userList = this.userDAO.getUserByid(requestUser);
			if (userList.size() > 0) {
				this.logger.info("修改订单金额功能 [{}] cwb: {}", this.getSessionUser().getRealname(), StringUtil.getStringsToString(cwbs));
				//5.28新加支付申请-审核-确认流程
				List<ZhiFuApplyView> list = new ArrayList<ZhiFuApplyView>();
				String cwbss = "";
				for (String cwb : cwbs) {
					cwbss += "'" + cwb + "',";
				}
				List<CwbOrder> cwblist = this.cwbDAO.getcwborderList(cwbss.substring(0, cwbss.lastIndexOf(",")));
				for (CwbOrder co : cwblist) {
					long count = this.zhiFuApplyDao.getApplystateCount(co.getCwb(), 1);
					if (count == 0) {
						for (FeeWayTypeRemark fre : frlist) {
							if (co.getCwb().equals(fre.getCwb())) {
								ZhiFuApplyView zfav = new ZhiFuApplyView();
								zfav.setCwb(co.getCwb());
								zfav.setCustomerid((int) co.getCustomerid());
								zfav.setCwbordertypeid(co.getCwbordertypeid());
								zfav.setApplycwbordertypeid(0);
								zfav.setFlowordertype((int) co.getFlowordertype());
								zfav.setBranchid((int) co.getCurrentbranchid());
								zfav.setPaywayid((int) co.getPaywayid());
								zfav.setApplypaywayid(0);
								BigDecimal bdformer = BigDecimal.ZERO;
								BigDecimal bdafter = BigDecimal.ZERO;
								BigDecimal cwbreceivfee = co.getReceivablefee();//订单代收款
								BigDecimal cwbpaybackfee = co.getPaybackfee();//订单待退款
								if (co.getCwbordertypeid() == 1) {
									bdformer = cwbreceivfee;
									bdafter = fre.getReceivablefee();
								} else if (co.getCwbordertypeid() == 2) {
									bdformer = cwbpaybackfee;
									bdafter = fre.getPaybackfee();
								} else {
									if (fre.getReceivablefee().compareTo(bdformer) > 0) {
										bdformer = cwbreceivfee;
										bdafter = fre.getReceivablefee();
									} else {
										bdformer = cwbpaybackfee;
										bdafter = fre.getPaybackfee();
									}
								}
								zfav.setReceivablefee(bdformer);
								zfav.setApplyreceivablefee(bdafter);
								zfav.setApplyway(1);
								zfav.setApplystate(1);
								zfav.setApplyresult(0);
								zfav.setUserid(Integer.parseInt(String.valueOf(requestUser)));
								zfav.setFeewaytyperemark(JacksonMapper.getInstance().writeValueAsString(fre));
								SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
								String dateStr = sdf.format(new Date());
								zfav.setApplytime(dateStr);
								list.add(zfav);
								//							editcwbnum+=1;
							}
						}
					}
				}
				for (ZhiFuApplyView za : list) {
					this.zhiFuApplyDao.creZhiFuApplyView(za);
				}
			}
		}
		//model.addAttribute("auditcwbnum",editcwbnum);
		return "editcwb/start";
	}

	//修改快递金额处理方法
	@RequestMapping("/editXiuGaiKuaiDiJinE")
	public String editXiuGaiKuaiDiJinE(Model model, HttpServletRequest request, @RequestParam(value = "requestUser", required = true, defaultValue = "0") Long requestUser, @RequestParam(value = "cwbs", required = false, defaultValue = "") String[] cwbs) throws Exception {
		//存储在订单修改确认时所需要的参数(在金额修改时需要使用)
		List<FeeWayTypeRemark> frlist = new ArrayList<FeeWayTypeRemark>();
		//	long editcwbnum = 0;
		if ((cwbs != null) && (cwbs.length > 0)) {
			for (String cwb : cwbs) {
				FeeWayTypeRemark fr = new FeeWayTypeRemark();
				String isDeliveryState = request.getParameter("isDeliveryState_" + cwb);
				//修改为代退金额
				BigDecimal shouldfare = request.getParameter("Shouldfare_" + cwb) == null ? BigDecimal.ZERO : new BigDecimal(request.getParameter("Shouldfare_" + cwb));
				fr.setCwb(cwb);
				fr.setIsDeliveryState(isDeliveryState);
				fr.setShouldfare(shouldfare);
				fr.setRequestUser(requestUser);
				frlist.add(fr);
			}

			List<User> userList = this.userDAO.getUserByid(requestUser);
			if (userList.size() > 0) {
				this.logger.info("修改快递运费金额功能 [{}] cwb: {}", this.getSessionUser().getRealname(), StringUtil.getStringsToString(cwbs));
				//5.28新加支付申请-审核-确认流程
				List<ZhiFuApplyView> list = new ArrayList<ZhiFuApplyView>();
				String cwbss = "";
				for (String cwb : cwbs) {
					cwbss += "'" + cwb + "',";
				}
				List<CwbOrder> cwblist = this.cwbDAO.getcwborderList(cwbss.substring(0, cwbss.lastIndexOf(",")));
				for (CwbOrder co : cwblist) {
					long count = this.zhiFuApplyDao.getApplystateCount(co.getCwb(), 1);
					if (count == 0) {
						for (FeeWayTypeRemark fre : frlist) {
							if (co.getCwb().equals(fre.getCwb())) {
								ZhiFuApplyView zfav = new ZhiFuApplyView();
								zfav.setCwb(co.getCwb());
								zfav.setCustomerid((int) co.getCustomerid());
								zfav.setCwbordertypeid(co.getCwbordertypeid());
								zfav.setApplycwbordertypeid(0);
								zfav.setFlowordertype((int) co.getFlowordertype());
								zfav.setBranchid((int) co.getCurrentbranchid());
								zfav.setPaywayid((int) co.getPaywayid());
								zfav.setApplypaywayid(0);//快递应该填这个吗？实体里面没有注释
								/*BigDecimal bdformer = BigDecimal.ZERO;
								BigDecimal bdafter = BigDecimal.ZERO;
								BigDecimal cwbreceivfee = co.getReceivablefee();//订单代收款
								BigDecimal cwbpaybackfee = co.getPaybackfee();//订单待退款
								if (co.getCwbordertypeid() == 1) {
									bdformer = cwbreceivfee;
									bdafter = fre.getReceivablefee();
								} else if (co.getCwbordertypeid() == 2) {
									bdformer = cwbpaybackfee;
									bdafter = fre.getPaybackfee();
								} else {
									if (fre.getReceivablefee().compareTo(bdformer) > 0) {
										bdformer = cwbreceivfee;
										bdafter = fre.getReceivablefee();
									} else {
										bdformer = cwbpaybackfee;
										bdafter = fre.getPaybackfee();
									}
								}*/
								zfav.setReceivablefee(BigDecimal.ZERO);
								zfav.setApplyreceivablefee(BigDecimal.ZERO);
								zfav.setShouldfare(fre.getShouldfare());
								zfav.setApplyway(ApplyEnum.kuaidiyunfeijine.getValue());
								zfav.setApplystate(1);//快递应该填这个吗？实体里面没有注释
								zfav.setApplyresult(0);//快递应该填这个吗？实体里面没有注释
								zfav.setUserid(Integer.parseInt(String.valueOf(requestUser)));
								zfav.setFeewaytyperemark(JacksonMapper.getInstance().writeValueAsString(fre));
								SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
								String dateStr = sdf.format(new Date());
								zfav.setApplytime(dateStr);
								list.add(zfav);
								//							editcwbnum+=1;
							}
						}
					}
				}
				for (ZhiFuApplyView za : list) {
					this.zhiFuApplyDao.creZhiFuApplyView(za);
				}
			}
		}
		//model.addAttribute("auditcwbnum",editcwbnum);
		return "editcwb/start";
	}

	@RequestMapping("/editXiuGaiZhiFuFangShi")
	public String editXiuGaiZhiFuFangShi(Model model, HttpServletRequest request, @RequestParam(value = "requestUser", required = false, defaultValue = "0") Long requestUser, @RequestParam(value = "cwbs", required = false, defaultValue = "") String[] cwbs) throws Exception {

		List<FeeWayTypeRemark> frlist = new ArrayList<FeeWayTypeRemark>();
		if ((cwbs != null) && (cwbs.length > 0)) {
			//5.28新加支付申请-审核-确认流程

			for (String cwb : cwbs) {
				FeeWayTypeRemark fr = new FeeWayTypeRemark();
				fr.setCwb(cwb);
				fr.setPaywayid(request.getParameter("paywayid_" + cwb) == null ? 0 : Integer.valueOf(request.getParameter("paywayid_" + cwb)));
				fr.setNewpaywayid(request.getParameter("Newpaywayid_" + cwb) == null ? 0 : Integer.valueOf(request.getParameter("Newpaywayid_" + cwb)));
				fr.setRequestUser(requestUser);
				frlist.add(fr);
			}

			List<User> userList = this.userDAO.getUserByid(requestUser);
			if (userList.size() > 0) {
				this.logger.info("修改订单支付方式功能 [{}] cwb: {}", this.getSessionUser().getRealname(), StringUtil.getStringsToString(cwbs));
				//5.28新加支付申请-审核-确认流程
				List<ZhiFuApplyView> list = new ArrayList<ZhiFuApplyView>();
				String cwbss = "";
				for (String cwb : cwbs) {
					cwbss += "'" + cwb + "',";
				}
				List<CwbOrder> cwblist = this.cwbDAO.getcwborderList(cwbss.substring(0, cwbss.lastIndexOf(",")));

				for (CwbOrder co : cwblist) {
					long count = this.zhiFuApplyDao.getApplystateCount(co.getCwb(), 2);
					if (count == 0) {
						for (FeeWayTypeRemark ftr : frlist) {
							if (ftr.getCwb().equals(co.getCwb())) {
								ZhiFuApplyView zfav = new ZhiFuApplyView();
								zfav.setCwb(co.getCwb());
								zfav.setCustomerid((int) co.getCustomerid());
								zfav.setCwbordertypeid(co.getCwbordertypeid());
								zfav.setApplycwbordertypeid(0);
								zfav.setFlowordertype((int) co.getFlowordertype());
								zfav.setBranchid((int) co.getCurrentbranchid());
								zfav.setPaywayid((int) co.getPaywayid());
								zfav.setApplypaywayid(ftr.getNewpaywayid());
								zfav.setReceivablefee(co.getReceivablefee());
								zfav.setApplyreceivablefee(BigDecimal.ZERO);
								zfav.setApplyway(2);
								zfav.setApplystate(1);
								zfav.setApplyresult(0);
								zfav.setUserid(Integer.parseInt(String.valueOf(requestUser)));
								zfav.setFeewaytyperemark(JacksonMapper.getInstance().writeValueAsString(ftr));
								SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
								String dateStr = sdf.format(new Date());
								zfav.setApplytime(dateStr);
								list.add(zfav);
							}
						}
					}
				}
				for (ZhiFuApplyView za : list) {
					this.zhiFuApplyDao.creZhiFuApplyView(za);
				}
			}
		}
		return "editcwb/start";
	}

	@RequestMapping("/editXiuGaiDingDanLeiXing")
	public String editXiuGaiDingDanLeiXing(Model model, HttpServletRequest request, @RequestParam(value = "requestUser", required = false, defaultValue = "0") Long requestUser, @RequestParam(value = "cwbs", required = false, defaultValue = "") String[] cwbs) throws Exception {
		List<FeeWayTypeRemark> frlist = new ArrayList<FeeWayTypeRemark>();
		if ((cwbs != null) && (cwbs.length > 0)) {
			for (String cwb : cwbs) {
				FeeWayTypeRemark fr = new FeeWayTypeRemark();
				fr.setCwb(cwb);
				fr.setCwbordertypeid(request.getParameter("cwbordertypeid_" + cwb) == null ? 0 : Integer.valueOf(request.getParameter("cwbordertypeid_" + cwb)));
				fr.setNewcwbordertypeid(request.getParameter("Newcwbordertypeid_" + cwb) == null ? 0 : Integer.valueOf(request.getParameter("Newcwbordertypeid_" + cwb)));
				frlist.add(fr);
			}
			List<User> userList = this.userDAO.getUserByid(requestUser);
			if (userList.size() > 0) {
				this.logger.info("修改订单类型功能 [{}] cwb: {}", this.getSessionUser().getRealname(), StringUtil.getStringsToString(cwbs));
				//5.28新加支付申请-审核-确认流程
				List<ZhiFuApplyView> list = new ArrayList<ZhiFuApplyView>();
				String cwbss = "";
				for (String cwb : cwbs) {
					cwbss += "'" + cwb + "',";
				}

				List<CwbOrder> cwblist = this.cwbDAO.getcwborderList(cwbss.substring(0, cwbss.lastIndexOf(",")));
				for (CwbOrder co : cwblist) {
					long count = this.zhiFuApplyDao.getApplystateCount(co.getCwb(), 3);
					if (count == 0) {
						for (FeeWayTypeRemark ftr : frlist) {
							if (ftr.getCwb().equals(co.getCwb())) {
								ZhiFuApplyView zfav = new ZhiFuApplyView();
								zfav.setCwb(co.getCwb());
								zfav.setCustomerid((int) co.getCustomerid());
								zfav.setCwbordertypeid(co.getCwbordertypeid());
								zfav.setApplycwbordertypeid(request.getParameter("Newcwbordertypeid_" + co.getCwb()) == null ? 0 : Integer.valueOf(request.getParameter("Newcwbordertypeid_" + co
										.getCwb())));
								zfav.setFlowordertype((int) co.getFlowordertype());
								zfav.setBranchid((int) co.getCurrentbranchid());
								zfav.setPaywayid((int) co.getPaywayid());
								zfav.setApplypaywayid(0);
								zfav.setReceivablefee(co.getReceivablefee());
								zfav.setApplyreceivablefee(BigDecimal.ZERO);
								zfav.setApplyway(3);
								zfav.setApplystate(1);
								zfav.setApplyresult(0);
								zfav.setUserid(Integer.parseInt(String.valueOf(requestUser)));
								zfav.setFeewaytyperemark(JacksonMapper.getInstance().writeValueAsString(ftr));
								SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
								String dateStr = sdf.format(new Date());
								zfav.setApplytime(dateStr);
								list.add(zfav);
							}
						}
					}
				}
				for (ZhiFuApplyView za : list) {
					this.zhiFuApplyDao.creZhiFuApplyView(za);
				}
			}
		}
		return "editcwb/start";
	}

	/**
	 * 返回修改订单明细列表
	 *
	 * @param model
	 * @param fd_payup_detail_id
	 *            小件员交款审核表finance_deliver_pay_up_detail的 id
	 * @param f_payup_audit_id
	 *            站点交款审核表finance_pay_up_audit 的id
	 * @param finance_audit_id
	 *            与供货商结算审核表finance_audit 的id
	 * @param payupid
	 *            站点交款表 express_ops_pay_up id
	 * @param gcaid
	 *            归班表 express_ops_goto_class_auditing id
	 * @return
	 */
	@RequestMapping("/getList")
	public String getList(Model model, @RequestParam(value = "fd_payup_detail_id", required = false, defaultValue = "0") Long fd_payup_detail_id, @RequestParam(value = "f_payup_audit_id", required = false, defaultValue = "0") Long f_payup_audit_id, @RequestParam(value = "finance_audit_id", required = false, defaultValue = "0") Long finance_audit_id, @RequestParam(value = "payupid", required = false, defaultValue = "") String payupids, @RequestParam(value = "gcaid", required = false, defaultValue = "0") Long gcaid) {
		model.addAttribute("userList", this.userDAO.getAllUserByuserDeleteFlag());
		if (fd_payup_detail_id > 0) {
			model.addAttribute("ecList", this.editCwbDAO.getEditCwbListByFdPayupDetailId(fd_payup_detail_id));
		} else if (finance_audit_id > 0) {
			model.addAttribute("ecList", this.editCwbDAO.getEditCwbListByFinanceAuditId(finance_audit_id));
		} else if (f_payup_audit_id > 0) {
			model.addAttribute("ecList", this.editCwbDAO.getEditCwbListByFPayupAuditId(f_payup_audit_id));
		} else if (gcaid > 0) {
			model.addAttribute("ecList", this.editCwbDAO.getEditCwbListByGcaid(gcaid));
		} else if (payupids.length() > 0) {
			List<EdtiCwb_DeliveryStateDetail> ecList = new ArrayList<EdtiCwb_DeliveryStateDetail>();
			for (String payupid : payupids.split(",")) {
				ecList.addAll(this.editCwbDAO.getEditCwbListByPayupid(payupid));
			}

			model.addAttribute("ecList", ecList);
		}

		return "editcwb/list";
	}

	/**
	 * 订单信息修改
	 * @throws Exception 
	 */
	@RequestMapping("/editCwbInfo")
	public String editCwbInfo(Model model, @RequestParam(value = "cwb", required = false, defaultValue = "") String cwb,
			@RequestParam(value = "isshow", defaultValue = "0", required = false) long isshow // 是否显示,
	) throws Exception {
		if (isshow > 0) { // 查询
			List<CwbOrder> cwborderlist = new ArrayList<CwbOrder>();
			List<String> cwbList = new ArrayList<String>();
			for (String cwbStr : cwb.split("\r\n")) {
				if (cwbStr.trim().length() == 0) {
					continue;
				}
				cwbStr = cwbStr.trim();
				if (StringUtils.isNotBlank(cwbStr)) {
					cwbList.add(cwbStr);
				}
				CwbOrder co = this.cwbDAO.getCwbByCwb(cwbStr);
				if(co!=null) {
					co.setConsigneemobileOfkf(SecurityUtil.getInstance().decrypt(co.getConsigneemobileOfkf()));
				}
				if (co != null) {
					cwborderlist.add(co);
				}
			}
			List<CwbOrderBranchMatchVo> cwbOrderBranchMatchVoList = this.cwbOrderService.getCwbBranchMatchByCwbs(cwbList);
			List<Branch> branchs = this.branchDAO.getBranchAllzhandian(String.valueOf(BranchEnum.ZhanDian.getValue()));
			// 匹配站点名称
			Map<Long, String> branchnameMap = new HashMap<Long, String>();
			for (Branch branch : branchs) {
				branchnameMap.put(branch.getBranchid(), branch.getBranchname());
			}
			for (CwbOrderBranchMatchVo vo : cwbOrderBranchMatchVoList) {
				vo.getCwbOrder().setExcelbranch(branchnameMap.get(vo.getCwbOrder().getDeliverybranchid()));
			}
			model.addAttribute("cwbOrderBranchMatchVoList", cwbOrderBranchMatchVoList);
			model.addAttribute("branchs", branchs);
			if (cwbOrderBranchMatchVoList.size() > 0) {
				Branch branch = this.branchService.getZhanDianByBranchId(cwbOrderBranchMatchVoList.get(0).getCwbOrder().getDeliverybranchid());
				if(branch != null) {
					model.addAttribute("destinationName", branch.getBranchname());
				}
			}
		}
		return "editcwb/editInfo";

	}

	@RequestMapping("/searchCwbInfo/{cwb}")
	public @ResponseBody String searchCwbInfo(Model model, @PathVariable("cwb") String cwb, @RequestParam(value = "editname", required = false, defaultValue = "") String editname, @RequestParam(value = "editmobile", required = false, defaultValue = "0") Long editmobile, @RequestParam(value = "editcommand", defaultValue = "", required = false) String editcommand, // 是否显示,
			@RequestParam(value = "editshow", defaultValue = "0", required = false) long editshow, // 是否显示,
			@RequestParam(value = "editaddress", required = false, defaultValue = "") String editaddress) {
		cwb = cwb.trim();
		CwbOrderDTO co = this.dataImportDAO_B2c.getCwbFromCwborder(cwb);// 运单号
		// 删除订单，然后insert进来
		if (co == null) {
			return "{\"errorCode\":1,\"error\":\"订单号不存在\"}";
		}
		if (this.orderFlowDAO.getOrderFlowByCwbAndFlowordertype(FlowOrderTypeEnum.FenZhanLingHuo.getValue(), cwb).size() > 0) {
			return "{\"errorCode\":1,\"error\":\"领货的不许修改地址\"}";
		}
		return "{\"errorCode\":0,\"error\":\"修改\"}";
	}

	@RequestMapping("/updateCwbInfo/{cwb}")
	public @ResponseBody String updateCwbInfo(Model model, @PathVariable("cwb") String cwb,
			@RequestParam(value = "editname", required = false, defaultValue = "") String editname, // 修改的姓名
			@RequestParam(value = "editmobile", required = false, defaultValue = "") String editmobile, // 修改的电话
			@RequestParam(value = "editcommand", defaultValue = "", required = false) String editcommand, // 需求
			@RequestParam(value = "editshow", defaultValue = "0", required = false) long editshow, // 是否显示,
			@RequestParam(value = "remark", defaultValue = "", required = false) String remark, // 订单备注
			@RequestParam(value = "matchaddress", defaultValue = "", required = false) String branchname, // 匹配后站点
			String courierName, // 配送员ID
			@RequestParam(value = "begindate", defaultValue = "", required = false) String begindate,
			@RequestParam(value = "editaddress", required = false, defaultValue = "") String editaddress,
			@RequestParam(value = "checkeditaddress", required = false, defaultValue = "") String checkeditaddress,
			@RequestParam(value = "checkeditname", required = false, defaultValue = "") String checkeditname,
			@RequestParam(value = "checkeditmobile", required = false, defaultValue = "") String checkeditmobile,
			@RequestParam(value = "checkbranchname", required = false, defaultValue = "") String checkbranchname,
			@RequestParam(value = "checkeditcommand", required = false, defaultValue = "") String checkeditcommand) {// 地址
		// 1.修改后的信息赋值
		final ExplinkUserDetail userDetail = (ExplinkUserDetail) this.securityContextHolderStrategy.getContext().getAuthentication().getPrincipal();
		cwb = cwb.trim();
		this.logger.info("修改订单号：{}开始,editname" + editname + "editmobile" + editmobile + "editcommand" + editcommand + "editaddress" + editaddress, cwb);
		CwbOrder old = this.cwbDAO.getCwbByCwb(cwb);
		
		//已经审核过的订单不允许修改
		String completedCwb = editCwbService.getCompletedCwbByCwb(old);
		if (completedCwb != null && !completedCwb.isEmpty()) {
			return "{\"errorCode\":1,\"error\":\"修改失败，"+completedCwb+"订单信息！\"}";
		}
		
		// 删除后新增，插入新增查询表中
		this.cwbInfoDao.deleteEditInfo(cwb);
		
		// 构建新的订单信息
		CwbOrderDTO co = this.dataImportDAO_B2c.getCwbFromCwborder(cwb);// 运单号
		long checkExceldeliverid = co.getExceldeliverid();
		co.setConsigneename(editname);
		co.setCustomercommand(editcommand);
		if(!StringUtil.isEmpty(editmobile)) {
			co.setConsigneemobile(SecurityUtil.getInstance().encrypt(editmobile));
		}
		co.setConsigneeaddress(editaddress);
		co.setCwbremark(remark);

		// 3.匹配地址库
		try {
			co.setExcelbranch(null);
			if ((co.getExcelbranch() == null) || (co.getExcelbranch().length() == 0) || (co.getDeliverybranchid() == 0)) {
				this.logger.info("地址库-------");
				if (!old.getConsigneeaddress().equals(co.getConsigneeaddress())) {
					this.addressMatchService.doMatchAddress(this.getSessionUser().getUserid(), co.getCwb());
				}
			}
			// 修改匹配站
			Branch branch = this.branchDAO.getBranchByBranchname(branchname);
			User deliver = null;
			if ((co != null) && (branch.getBranchid() > 0)) {
				deliver = this.userService.getBranchDeliverByDeliverName(branch.getBranchid(), courierName);
				if(deliver != null) {
					co.setExcelbranchid(deliver.getUserid());
					co.setExceldeliver(deliver.getRealname());
				}
				CwbOrderAddressCodeEditTypeEnum addressCodeEditType = CwbOrderAddressCodeEditTypeEnum.WeiPiPei;
				if ((co.getAddresscodeedittype() == CwbOrderAddressCodeEditTypeEnum.DiZhiKu.getValue()) || (co.getAddresscodeedittype() == CwbOrderAddressCodeEditTypeEnum.XiuGai.getValue())) {// 如果修改的数据原来是地址库匹配的或者是后来修改的
					addressCodeEditType = CwbOrderAddressCodeEditTypeEnum.XiuGai;
				} else if ((co.getAddresscodeedittype() == CwbOrderAddressCodeEditTypeEnum.WeiPiPei.getValue()) || (co.getAddresscodeedittype() == CwbOrderAddressCodeEditTypeEnum.RenGong.getValue())) {// 如果修改的数据原来是为匹配的
																																																			// //
																																																			// 都将匹配状态变更为人工修改
					addressCodeEditType = CwbOrderAddressCodeEditTypeEnum.RenGong;
				}
				try {
					this.cwbOrderService.updateDeliveryBranchAndCourier(this.getSessionUser(), old, branch, addressCodeEditType, deliver);
					this.logger.info("客服管理--订单信息修改--地址库匹配订单号:{},站点{}", cwb, branchname);
				} catch (Exception e) {
					return "{\"errorCode\":1,\"error\":\"" + e.getMessage() + "\"}";
				}
			}

			// 4.成功后插入消息表express_ops_window

			List<User> userlist = this.userDAO.getAllUserbybranchid(old.getDeliverybranchid() == 0 ? old.getNextbranchid() : old.getDeliverybranchid());
			if ((userlist != null) && (userlist.size() > 0)) {

				String jsonInfo = co.getCwb();
				WindowShow a = this.appearWindowDao.getObjectWindowByState(userlist.get(0).getUserid());
				if (a != null) {// 存在,update
					this.logger.info("更新定时器表 --订单号{}，类型为2，用户为{}", co.getCwb(), userlist.get(0).getUserid());
					this.appearWindowDao.updateByStateAndUserid(a.getJsoninfo() + "," + jsonInfo, userlist.get(0).getUserid());
				} else {
					this.logger.info("新增定时器表 --订单号{}，类型为2，用户为{}", co.getCwb(), userlist.get(0).getUserid());
					this.appearWindowDao.creWindowTime(jsonInfo, "2", userlist.get(0).getUserid(), "1");
				}
			}
//			Branch branch2 = this.branchDAO.getBranchByBranchname(checkbranchname);
//			long branchid = branch2.getBranchid();
			long branchid = branch.getBranchid();
			long exceldeliverid = deliver == null ? 0 : deliver.getUserid();
			String exceldeliver = deliver == null ? null : deliver.getRealname();
			this.cwbInfoDao.createEditInfo(old, branchid, editname, editmobile, editcommand, editaddress, begindate, userDetail.getUser().getUserid(), remark, exceldeliverid, exceldeliver);
			/**
			 * 修改云订单信息  add gordon.zhou 2016/5/26
			 */
			try {
				CwbOrder order  = this.cwbDAO.getCwbByCwb(completedCwb);
				long newDeliveryBranchid = (order == null ? branchid : order.getDeliverybranchid());
				String omsUrl = this.editCwbService.omsUrl();
				JSONReslutUtil.getResultMessageChangeLog(omsUrl + "/OMSChange/editcwb", "type=5&cwb=" + co.getCwb() + "&branchid=" + newDeliveryBranchid + "&consigneename="
						+ editname +"&consigneemobile=" + editmobile + "&consigneeaddress=" + editaddress
						+ "&customercommand=" + editcommand , "POST");
			} catch (Exception e1) {
				this.logger.error("修改云订单信息异常:"+ co.getCwb(), e1);
			}
			
			long count = this.orderAddressReviseDao.countReviseAddress(cwb);
			if (count == 0) {
				this.orderAddressReviseDao
						.createReviseAddressInfo(cwb, old.getConsigneeaddress(), old.getEmaildate(), "系统导入", old.getConsigneenameOfkf(), old.getConsigneemobileOfkf(), "", checkbranchname, old
								.getCustomercommand(), old.getExceldeliver());
			}

			if ((exceldeliverid != checkExceldeliverid) || (!checkeditaddress.equals(editaddress)) || (!editname.equals(checkeditname)) || (!editmobile.equals(checkeditmobile)) || (!editcommand.equals(checkeditcommand)) || (!branchname
					.equals(""))) {
				/**
				 * 关于站点的一点判断
				 */
				String revisebranchName = checkbranchname;
				if ((!branchname.equals(checkbranchname)) && (!branchname.equals("请选择"))) {
					if (!branchname.equals("")) {
						revisebranchName = branchname;
					}
					this.orderAddressReviseDao
							.createReviseAddressInfo(cwb, editaddress, DateTimeUtil.getNowTime(), userDetail.getUser().getRealname(), editname, editmobile, begindate, revisebranchName, editcommand, exceldeliver);

				} else {
					if ((exceldeliverid != checkExceldeliverid) || (!checkeditaddress.equals(editaddress)) || (!editname.equals(checkeditname)) || (!editmobile.equals(checkeditmobile)) || (!editcommand.equals(checkeditcommand))) {

						this.orderAddressReviseDao
								.createReviseAddressInfo(cwb, editaddress, DateTimeUtil.getNowTime(), userDetail.getUser().getRealname(), editname, editmobile, begindate, revisebranchName, editcommand, exceldeliver);

					}

				}

			}

			// 2.更新到主表
			EmailDate ed = this.dataImportService.getEmailDate_B2CByEmaildate(co.getCustomerid(), co.getCustomerwarehouseid(), co.getCustomerwarehouseid(), co.getEmaildate());
			userDetail.getUser().setBranchid(Long.valueOf(ed.getWarehouseid()));
			this.emaildateDAO.editEditEmaildateForCwbcountAdd(ed.getEmaildateid());
			this.cwbOrderService.updateExcelCwb(co, co.getCustomerid(), (old == null || "".equals(old.getCarwarehouse()))?0:Long.valueOf(old.getCarwarehouse()), userDetail.getUser(), ed, true);
			return "{\"errorCode\":0,\"error\":\"修改成功\"}";
		} catch (Exception e) {
			this.logger.error("调用地址库异常", e);
			return "{\"errorCode\":1,\"error\":\"失败：调用地址库异常\"}";
		}
	}

	@RequestMapping("/toSearchCwb/{page}")
	public String toSerchCwb(Model model, @PathVariable("page") long page, @RequestParam(value = "begindate", required = false, defaultValue = "") String begindate, @RequestParam(value = "enddate", defaultValue = "", required = false) String endtime, // 是否显示,
			@RequestParam(value = "isshow", defaultValue = "0", required = false) long isshow // 是否显示,
	) {
		List<Branch> branchs = this.branchDAO.getAllBranches();
		Page pageobj = new Page();
		if (isshow > 0) {
			List<SearcheditInfo> slist = this.cwbInfoDao.getInfoByCretime(page, begindate, endtime);
			List<User> ulist = this.userDAO.getAllUser();
			pageobj = new Page(this.cwbInfoDao.countEditInfo(begindate, endtime), page, Page.ONE_PAGE_NUMBER);
			model.addAttribute("slist", slist);
			model.addAttribute("ulist", ulist);
		}
		model.addAttribute("page", page);
		model.addAttribute("page_obj", pageobj);
		model.addAttribute("branchs", branchs);
		return "editcwb/searchInfo";
	}

	/**
	 * 订单信息修改导出
	 */
	@RequestMapping("/exportExcle")
	public void exportExcle(Model model, HttpServletResponse response, HttpServletRequest request) {

		String[] cloumnName1 = new String[14]; // 导出的列名
		String[] cloumnName2 = new String[14]; // 导出的英文列名

		this.exportService.SetEditOrderFields(cloumnName1, cloumnName2);
		final String[] cloumnName = cloumnName1;
		final String[] cloumnName3 = cloumnName2;
		String sheetName = "订单修改信息"; // sheet的名称
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
		String fileName = "editcwbOrder_" + df.format(new Date()) + ".xlsx"; // 文件名
		try {
			// 查询出数据
			String begindate = request.getParameter("begindate") == null ? "" : request.getParameter("begindate");
			String enddate = request.getParameter("enddate") == null ? "" : request.getParameter("enddate");

			final List<SearcheditInfo> views = this.cwbInfoDao.getAllInfoByCretime(begindate, enddate);
			final List<User> userlist = this.userDAO.getAllUser();
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
							a = EditCwbController.this.exportService.setEditOrderObject(cloumnName3, views, userlist, a, i, k);
							cell.setCellValue(a == null ? "" : a.toString());
						}
					}
				}
			};
			excelUtil.excel(response, cloumnName, sheetName, fileName);
		} catch (Exception e) {
			this.logger.error("", e);
		}
	}

	public String getStrings(List<String> strArr) {
		String strs = "0,";
		if (strArr.size() > 0) {
			for (String str : strArr) {
				strs += "'" + str + "',";
			}
		}

		if (strs.length() > 0) {
			strs = strs.substring(0, strs.length() - 1);
		}
		return strs;
	}

	public String getString(List<Long> list) {
		String str = "0,";
		if (list.size() > 0) {
			for (Long num : list) {
				str += num + ",";
			}

		}
		if (str.length() > 0) {
			str = str.substring(0, str.length() - 1);
		}
		return str;

	}

	@RequestMapping("/matchaddress")
	public @ResponseBody JSONObject matchaddress(Model model, @RequestParam(value = "cwb", required = false, defaultValue = "") String cwb, @RequestParam(value = "address", defaultValue = "", required = false) String address // 是否显示,
	) {
		JSONObject json = this.addressMatchService.matchAddressByInterface(cwb, address,0);
		this.logger.info("客服管理--订单信息修改--地址库匹配订单号:{},站点{}", cwb, json.getString("netpoint"));
		return json;
	}

	@RequestMapping("/findbranch")
	public @ResponseBody List<Branch> findbranch(Model model, @RequestParam(value = "branchname", defaultValue = "", required = false) String branchname // 是否显示,
	) {
		List<Branch> branches = this.branchDAO.getBranchByBranchnameMoHu(branchname.trim());

		return branches;
	}

	@RequestMapping("/findCwbDetail/{cwb}")
	public String FindCwbDetail(Model model, @PathVariable String cwb) {
		List<OrderAddressRevise> orderAddressRevise = this.orderAddressReviseDao.getAddressReviseDetails(cwb);
		model.addAttribute("orderAddressRevises", orderAddressRevise);
		return "editcwb/addressReviseDetail";
	}
	
	/**
	 * 添加验证，如果存在未审核的修改申请，则不允许申请。
	 * @author jian.xie
	 * @date 2016-07-14
	 */
	@RequestMapping("/checkIsExist")
	@ResponseBody
	public String checkIsExist(@RequestParam(value="cwbs", defaultValue="-1") String cwbs){
		if(StringUtils.isEmpty(cwbs)){
			return "";
		}
		List<ZhiFuApplyView> list = zhiFuApplyDao.getNotAudiByCwbs(cwbs);
		StringBuilder result = new StringBuilder();
		Set<String> setCwb = new HashSet<String>();
		if(!CollectionUtils.isEmpty(list)){
			ZhiFuApplyView view = null;
			for(int i = 0, size = list.size(); i < size; i++){
				view = list.get(i);
				// 去重
				if(setCwb.contains(view.getCwb())){
					continue;
				}
				if(i != 0){
					result.append(",");
				}
				result.append(view.getCwb());
				setCwb.add(view.getCwb());
			}
		}
		return result.toString(); 
	}
}

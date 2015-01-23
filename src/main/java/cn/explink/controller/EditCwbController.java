package cn.explink.controller;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.explink.b2c.tools.DataImportDAO_B2c;
import cn.explink.dao.AccountCwbFareDetailDAO;
import cn.explink.dao.AppearWindowDao;
import cn.explink.dao.AdjustmentRecordDAO;
import cn.explink.dao.AppearWindowDao;
import cn.explink.dao.BranchDAO;
import cn.explink.dao.CwbDAO;
import cn.explink.dao.DeliveryStateDAO;
import cn.explink.dao.EditCwbDAO;
import cn.explink.dao.EmailDateDAO;
import cn.explink.dao.OrderFlowDAO;
import cn.explink.dao.UserDAO;
import cn.explink.dao.searchEditCwbInfoDao;
import cn.explink.domain.AccountCwbFareDetail;
import cn.explink.domain.AdjustmentRecord;
import cn.explink.domain.Branch;
import cn.explink.domain.CwbOrder;
import cn.explink.domain.DeliveryState;
import cn.explink.domain.EdtiCwb_DeliveryStateDetail;
import cn.explink.domain.EmailDate;
import cn.explink.domain.SearcheditInfo;
import cn.explink.domain.User;
import cn.explink.domain.WindowShow;
import cn.explink.enumutil.BranchEnum;
import cn.explink.enumutil.CwbOrderAddressCodeEditTypeEnum;
import cn.explink.enumutil.CwbOrderTypeIdEnum;
import cn.explink.enumutil.EditCwbTypeEnum;
import cn.explink.enumutil.FlowOrderTypeEnum;
import cn.explink.exception.ExplinkException;
import cn.explink.pos.tools.JacksonMapper;
import cn.explink.service.AdjustmentRecordService;
import cn.explink.service.CwbOrderService;
import cn.explink.service.CwbOrderWithDeliveryState;
import cn.explink.service.DataImportService;
import cn.explink.service.EditCwbService;
import cn.explink.service.ExplinkUserDetail;
import cn.explink.service.ExportService;
import cn.explink.service.addressmatch.AddressMatchService;
import cn.explink.support.transcwb.TransCwbDao;
import cn.explink.util.ExcelUtils;
import cn.explink.util.Page;
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
	@Produce(uri = "jms:topic:addressmatch")
	ProducerTemplate addressmatch;
	@Autowired
	EditCwbDAO editCwbDAO;
	@Autowired
	CwbOrderService cwbOrderService;
	@Autowired
	EmailDateDAO emaildateDAO;

	@Autowired
	searchEditCwbInfoDao cwbInfoDao;
	@Autowired
	AccountCwbFareDetailDAO accountCwbFareDetailDAO;
	@Autowired
	AdjustmentRecordService adjustmentRecordService;

	@Autowired
	SecurityContextHolderStrategy securityContextHolderStrategy;
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
		Map<String, AccountCwbFareDetail> accountCwbFareDetailMap = this.accountCwbFareDetailDAO.getAccountCwbFareDetailMapByCwbs(cwbsSql);

		// 做重置审核状态更改的操作 start
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
		} else if (type == EditCwbTypeEnum.XiuGaiJinE.getValue()) {// 修改订单金额更改操作
																	// Start
			List<CwbOrderWithDeliveryState> allowCods = new ArrayList<CwbOrderWithDeliveryState>();
			for (CwbOrder co : cwbList) {
				CwbOrderWithDeliveryState cods = new CwbOrderWithDeliveryState();
				cods.setCwbOrder(co);
				cods.setDeliveryState(this.deliveryStateDAO.getDeliveryByCwbAndDeliverystate(co.getCwb()));
				// 存储订单表记录和反馈表记录，用于前端判断
				allowCods.add(cods);
			}
			model.addAttribute("allowCods", allowCods);
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
				// 存储订单表记录和反馈表记录，用于前端判断 如果代收金额
				if (co.getReceivablefee().compareTo(BigDecimal.ZERO) <= 0) {
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
				// 已经归班的订单不能修改订单类型，必须充值审核状态才能修改
				if ((cods.getDeliveryState() != null) && (cods.getDeliveryState().getGcaid() > 0)) {
					cods.setError("已审核的订单不允许修改订单类型，若要修改，请重置审核状态后再试");
					prohibitedCods.add(cods);
				} else if ((cods.getDeliveryState() != null) && (co.getCwbordertypeid() == CwbOrderTypeIdEnum.Shangmentui.getValue())
						&& (cods.getDeliveryState().getInfactfare().compareTo(BigDecimal.ZERO) > 0)) {
					cods.setError("上门退有应收运费的订单不允许修改订单类型");
					prohibitedCods.add(cods);
				} else {
					allowCods.add(cods);
				}
			}
			model.addAttribute("allowCods", allowCods);
			model.addAttribute("prohibitedCods", prohibitedCods);
			return "editcwb/XiuGaiDingDanLeiXing";
			// 修改订单订单类型更改操作 end
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
	public String editChongZhiShenHeZhuangTai(Model model, @RequestParam(value = "requestUser", required = false, defaultValue = "0") Long requestUser,
			@RequestParam(value = "cwbs", required = false, defaultValue = "") String[] cwbs) {
		List<User> userList = this.userDAO.getUserByid(requestUser);
		if (userList != null) {
			this.logger.info("重置订单审核状态功能 [{}] cwb: {}", this.getSessionUser().getRealname(), StringUtil.getStringsToString(cwbs));
			List<EdtiCwb_DeliveryStateDetail> ecList = new ArrayList<EdtiCwb_DeliveryStateDetail>();
			List<String> errorList = new ArrayList<String>();
			for (String cwb : cwbs) {
				try {
					EdtiCwb_DeliveryStateDetail ec_dsd = this.editCwbService.analysisAndSaveByChongZhiShenHe(cwb, requestUser, this.getSessionUser().getUserid());
					ecList.add(ec_dsd);
				} catch (ExplinkException ee) {
					errorList.add(cwb + "_" + ee.getMessage());
				} catch (Exception e) {
					errorList.add(cwb + "_" + FlowOrderTypeEnum.YiShenHe.getValue() + "_系统内部报错！");
					e.printStackTrace();
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
				EdtiCwb_DeliveryStateDetail ec_dsd = this.editCwbService.analysisAndSaveByChongZhiShenHe(cwb, (long) 0, this.getSessionUser().getUserid());
				ecList.add(ec_dsd);
			} catch (ExplinkException ee) {
				errorList.add(cwb + "_" + FlowOrderTypeEnum.YiShenHe.getValue() + "_" + ee.getMessage());
			} catch (Exception e) {
				errorList.add(cwb + "_" + FlowOrderTypeEnum.YiShenHe.getValue() + "_系统内部报错！");
				e.printStackTrace();
			}
		}
		model.addAttribute("ecList", ecList);
		model.addAttribute("errorList", errorList);

		return "editcwb/chongzhiResult";
	}

	@RequestMapping("/editXiuGaiJinE")
	public String editXiuGaiJinE(Model model, HttpServletRequest request, @RequestParam(value = "requestUser", required = true, defaultValue = "0") Long requestUser,
			@RequestParam(value = "cwbs", required = false, defaultValue = "") String[] cwbs) {
		List<User> userList = this.userDAO.getUserByid(requestUser);
		if (userList.size() > 0) {
			this.logger.info("修改订单金额功能 [{}] cwb: {}", this.getSessionUser().getRealname(), StringUtil.getStringsToString(cwbs));
			List<EdtiCwb_DeliveryStateDetail> ecList = new ArrayList<EdtiCwb_DeliveryStateDetail>();
			List<String> errorList = new ArrayList<String>();
			//定义一个调整单List
			List<AdjustmentRecord> adjustmentRecords=new ArrayList<AdjustmentRecord>();
			for (String cwb : cwbs) {
				String isDeliveryState = request.getParameter("isDeliveryState_" + cwb);
				BigDecimal Receivablefee = request.getParameter("Receivablefee_" + cwb) == null ? BigDecimal.ZERO : new BigDecimal(request.getParameter("Receivablefee_" + cwb));
				BigDecimal cash = request.getParameter("Receivablefee_cash_" + cwb) == null ? BigDecimal.ZERO : new BigDecimal(request.getParameter("Receivablefee_cash_" + cwb));
				BigDecimal pos = request.getParameter("Receivablefee_pos_" + cwb) == null ? BigDecimal.ZERO : new BigDecimal(request.getParameter("Receivablefee_pos_" + cwb));
				BigDecimal checkfee = request.getParameter("Receivablefee_checkfee_" + cwb) == null ? BigDecimal.ZERO : new BigDecimal(request.getParameter("Receivablefee_checkfee_" + cwb));
				BigDecimal otherfee = request.getParameter("Receivablefee_otherfee_" + cwb) == null ? BigDecimal.ZERO : new BigDecimal(request.getParameter("Receivablefee_otherfee_" + cwb));
				BigDecimal Paybackfee = request.getParameter("Paybackfee_" + cwb) == null ? BigDecimal.ZERO : new BigDecimal(request.getParameter("Paybackfee_" + cwb));
				CwbOrder cwbOrder=new CwbOrder();
				cwbOrder=cwbDAO.getCwbByCwb(cwb);
				User user=new User();
				if(userList.size()>0){
					user=userList.get(0);
				}
				
				//先判断是有账单 获取到修改订单金额的值,进行判断插入到数据库中
				
				
				if(Receivablefee!=null&&!Receivablefee.equals(cwbOrder.getReceivablefee())){
					adjustmentRecordService.createAdjustmentRecode(cwb, cwbOrder.getCustomerid(), cwbOrder.getReceivablefee(), Paybackfee, Receivablefee, "", user.getUsername(), cwbOrder.getCwbordertypeid());
					
				}
				
				
				
				
				try {
					EdtiCwb_DeliveryStateDetail ec_dsd = this.editCwbService.analysisAndSaveByXiuGaiJinE(cwb, isDeliveryState, Receivablefee, cash, pos, checkfee, otherfee, Paybackfee, requestUser,
							this.getSessionUser().getUserid());
					ecList.add(ec_dsd);
				} catch (ExplinkException ee) {
					errorList.add(cwb + "_" + ee.getMessage());
				} catch (Exception e) {
					errorList.add(cwb + "_" + FlowOrderTypeEnum.YiShenHe.getValue() + "_系统内部报错！");
					e.printStackTrace();
				}
				
				
			}
			model.addAttribute("ecList", ecList);
			model.addAttribute("errorList", errorList);

		}
		return "editcwb/XiuGaiJinEResult";
	}

	@RequestMapping("/editXiuGaiZhiFuFangShi")
	public String editXiuGaiZhiFuFangShi(Model model, HttpServletRequest request, @RequestParam(value = "requestUser", required = false, defaultValue = "0") Long requestUser,
			@RequestParam(value = "cwbs", required = false, defaultValue = "") String[] cwbs) {
		List<User> userList = this.userDAO.getUserByid(requestUser);
		if (userList.size() > 0) {
			this.logger.info("修改订单支付方式功能 [{}] cwb: {}", this.getSessionUser().getRealname(), StringUtil.getStringsToString(cwbs));
			List<EdtiCwb_DeliveryStateDetail> ecList = new ArrayList<EdtiCwb_DeliveryStateDetail>();
			List<String> errorList = new ArrayList<String>();
			for (String cwb : cwbs) {
				int paywayid = request.getParameter("paywayid_" + cwb) == null ? 0 : Integer.valueOf(request.getParameter("paywayid_" + cwb));
				int newpaywayid = request.getParameter("Newpaywayid_" + cwb) == null ? 0 : Integer.valueOf(request.getParameter("Newpaywayid_" + cwb));
				try {
					EdtiCwb_DeliveryStateDetail ec_dsd = this.editCwbService.analysisAndSaveByXiuGaiZhiFuFangShi(cwb, paywayid, newpaywayid, requestUser, this.getSessionUser().getUserid());
					ecList.add(ec_dsd);
				} catch (ExplinkException ee) {
					errorList.add(cwb + "_" + ee.getMessage());
				} catch (Exception e) {
					errorList.add(cwb + "_不确定_系统内部报错！");
					e.printStackTrace();
				}
			}
			model.addAttribute("ecList", ecList);
			model.addAttribute("errorList", errorList);

		}
		return "editcwb/XiuGaiZhiFuFangShiResult";
	}

	@RequestMapping("/editXiuGaiDingDanLeiXing")
	public String editXiuGaiDingDanLeiXing(Model model, HttpServletRequest request, @RequestParam(value = "requestUser", required = false, defaultValue = "0") Long requestUser,
			@RequestParam(value = "cwbs", required = false, defaultValue = "") String[] cwbs) {
		List<User> userList = this.userDAO.getUserByid(requestUser);
		if (userList.size() > 0) {
			this.logger.info("修改订单类型功能 [{}] cwb: {}", this.getSessionUser().getRealname(), StringUtil.getStringsToString(cwbs));
			List<EdtiCwb_DeliveryStateDetail> ecList = new ArrayList<EdtiCwb_DeliveryStateDetail>();
			List<String> errorList = new ArrayList<String>();
			for (String cwb : cwbs) {
				int cwbordertypeid = request.getParameter("cwbordertypeid_" + cwb) == null ? 0 : Integer.valueOf(request.getParameter("cwbordertypeid_" + cwb));
				int Newcwbordertypeid = request.getParameter("Newcwbordertypeid_" + cwb) == null ? 0 : Integer.valueOf(request.getParameter("Newcwbordertypeid_" + cwb));
				try {
					EdtiCwb_DeliveryStateDetail ec_dsd = this.editCwbService.analysisAndSaveByXiuGaiDingDanLeiXing(cwb, cwbordertypeid, Newcwbordertypeid, requestUser, this.getSessionUser()
							.getUserid());
					ecList.add(ec_dsd);
				} catch (ExplinkException ee) {
					errorList.add(cwb + "_" + ee.getMessage());
				} catch (Exception e) {
					errorList.add(cwb + "_不确定_系统内部报错！");
					e.printStackTrace();
				}
			}
			model.addAttribute("ecList", ecList);
			model.addAttribute("errorList", errorList);

		}
		return "editcwb/XiuGaiDingDanLeiXingResult";
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
	public String getList(Model model, @RequestParam(value = "fd_payup_detail_id", required = false, defaultValue = "0") Long fd_payup_detail_id,
			@RequestParam(value = "f_payup_audit_id", required = false, defaultValue = "0") Long f_payup_audit_id,
			@RequestParam(value = "finance_audit_id", required = false, defaultValue = "0") Long finance_audit_id,
			@RequestParam(value = "payupid", required = false, defaultValue = "") String payupids, @RequestParam(value = "gcaid", required = false, defaultValue = "0") Long gcaid) {
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
	 */
	@RequestMapping("/editCwbInfo")
	public String editCwbInfo(Model model, @RequestParam(value = "cwb", required = false, defaultValue = "") String cwb,
			@RequestParam(value = "isshow", defaultValue = "0", required = false) long isshow // 是否显示,
	) {

		if (isshow > 0) {// 查询
			List<CwbOrder> cwborderlist = new ArrayList<CwbOrder>();

			for (String cwbStr : cwb.split("\r\n")) {
				if (cwbStr.trim().length() == 0) {
					continue;
				}
				CwbOrder co = this.cwbDAO.getCwbByCwb(cwbStr);
				if (co != null) {
					cwborderlist.add(co);
				}
			}
			List<Branch> branchs = this.branchDAO.getBranchAllzhandian(BranchEnum.ZhanDian.getValue() + "");
			model.addAttribute("cwbList", cwborderlist);
			model.addAttribute("branchs", branchs);
		}
		return "editcwb/editInfo";

	}

	@RequestMapping("/searchCwbInfo/{cwb}")
	public @ResponseBody String searchCwbInfo(Model model, @PathVariable("cwb") String cwb, @RequestParam(value = "editname", required = false, defaultValue = "") String editname,
			@RequestParam(value = "editmobile", required = false, defaultValue = "0") Long editmobile, @RequestParam(value = "editcommand", defaultValue = "", required = false) String editcommand, // 是否显示,
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
	public @ResponseBody String updateCwbInfo(Model model, @PathVariable("cwb") String cwb, @RequestParam(value = "editname", required = false, defaultValue = "") String editname,// 修改的姓名
			@RequestParam(value = "editmobile", required = false, defaultValue = "") String editmobile,// 修改的电话
			@RequestParam(value = "editcommand", defaultValue = "", required = false) String editcommand, // 需求
			@RequestParam(value = "editshow", defaultValue = "0", required = false) long editshow, // 是否显示,
			@RequestParam(value = "remark", defaultValue = "", required = false) String remark, // 订单备注
			@RequestParam(value = "matchaddress", defaultValue = "", required = false) String branchname, // 匹配后站点
			@RequestParam(value = "begindate", defaultValue = "", required = false) String begindate, @RequestParam(value = "editaddress", required = false, defaultValue = "") String editaddress) {// 地址
		// 1.修改后的信息赋值
		final ExplinkUserDetail userDetail = (ExplinkUserDetail) this.securityContextHolderStrategy.getContext().getAuthentication().getPrincipal();
		cwb = cwb.trim();
		this.logger.info("修改订单号：{}开始,editname" + editname + "editmobile" + editmobile + "editcommand" + editcommand + "editaddress" + editaddress, cwb);
		CwbOrder old = this.cwbDAO.getCwbByCwb(cwb);
		// 删除后新增，插入新增查询表中
		this.cwbInfoDao.deleteEditInfo(cwb);
		this.cwbInfoDao.createEditInfo(old, editname, editmobile, editcommand, editaddress, begindate, userDetail.getUser().getUserid(), remark);
		// 构建新的订单信息
		CwbOrderDTO co = this.dataImportDAO_B2c.getCwbFromCwborder(cwb);// 运单号
		co.setConsigneename(editname);
		co.setCustomercommand(editcommand);
		co.setConsigneemobile(editmobile);
		co.setConsigneeaddress(editaddress);
		co.setCwbremark(remark);
		// 2.更新到主表
		EmailDate ed = this.dataImportService.getEmailDate_B2CByEmaildate(co.getCustomerid(), co.getCustomerwarehouseid(), co.getCustomerwarehouseid(), co.getEmaildate());
		userDetail.getUser().setBranchid(Long.valueOf(ed.getWarehouseid()));
		this.emaildateDAO.editEditEmaildateForCwbcountAdd(ed.getEmaildateid());
		this.cwbOrderService.updateExcelCwb(co, co.getCustomerid(), ed.getWarehouseid(), userDetail.getUser(), ed, true);
		// 3.匹配地址库
		try {
			co.setExcelbranch(null);
			if ((co.getExcelbranch() == null) || (co.getExcelbranch().length() == 0) || (co.getDeliverybranchid() == 0)) {
				this.logger.info("地址库-------");
				if (!old.getConsigneeaddress().equals(co.getConsigneeaddress())) {
					this.addressMatchService.matchAddress(this.getSessionUser().getUserid(), co.getCwb());
				}
			}
			// 修改匹配站
			Branch branch = this.branchDAO.getBranchByBranchname(branchname);
			if ((co != null) && (branch.getBranchid() > 0)) {

				CwbOrderAddressCodeEditTypeEnum addressCodeEditType = CwbOrderAddressCodeEditTypeEnum.WeiPiPei;
				if ((co.getAddresscodeedittype() == CwbOrderAddressCodeEditTypeEnum.DiZhiKu.getValue()) || (co.getAddresscodeedittype() == CwbOrderAddressCodeEditTypeEnum.XiuGai.getValue())) {// 如果修改的数据原来是地址库匹配的或者是后来修改的
					addressCodeEditType = CwbOrderAddressCodeEditTypeEnum.XiuGai;
				} else if ((co.getAddresscodeedittype() == CwbOrderAddressCodeEditTypeEnum.WeiPiPei.getValue()) || (co.getAddresscodeedittype() == CwbOrderAddressCodeEditTypeEnum.RenGong.getValue())) {// 如果修改的数据原来是为匹配的
																																																			// //
																																																			// 都将匹配状态变更为人工修改
					addressCodeEditType = CwbOrderAddressCodeEditTypeEnum.RenGong;
				}
				try {
					this.cwbOrderService.updateDeliveryBranch(this.getSessionUser(), old, branch, addressCodeEditType);
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
			return "{\"errorCode\":0,\"error\":\"修改成功\"}";
		} catch (Exception e) {
			this.logger.error("调用地址库异常", e);
			e.printStackTrace();
			return "{\"errorCode\":1,\"error\":\"失败：调用地址库异常\"}";
		}
	}

	@RequestMapping("/toSearchCwb/{page}")
	public String toSerchCwb(Model model, @PathVariable("page") long page, @RequestParam(value = "begindate", required = false, defaultValue = "") String begindate,
			@RequestParam(value = "enddate", defaultValue = "", required = false) String endtime, // 是否显示,
			@RequestParam(value = "isshow", defaultValue = "0", required = false) long isshow // 是否显示,
	) {
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
		return "editcwb/searchInfo";
	}

	/**
	 * 订单信息修改导出
	 */
	@RequestMapping("/exportExcle")
	public void exportExcle(Model model, HttpServletResponse response, HttpServletRequest request) {

		String[] cloumnName1 = new String[16]; // 导出的列名
		String[] cloumnName2 = new String[16]; // 导出的英文列名

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
			e.printStackTrace();
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
	public @ResponseBody JSONObject matchaddress(Model model, @RequestParam(value = "cwb", required = false, defaultValue = "") String cwb,
			@RequestParam(value = "address", defaultValue = "", required = false) String address // 是否显示,
	) {
		JSONObject json = this.addressMatchService.matchAddressByInterface(cwb, address);
		this.logger.info("客服管理--订单信息修改--地址库匹配订单号:{},站点{}", cwb, json.getString("netpoint"));
		return json;
	}

	@RequestMapping("/findbranch")
	public @ResponseBody List<Branch> findbranch(Model model, @RequestParam(value = "branchname", defaultValue = "", required = false) String branchname // 是否显示,
	) {
		List<Branch> branches = this.branchDAO.getBranchByBranchnameMoHu(branchname);
		/*
		 * CwbOrder co = this.cwbDAO.getCwbByCwb(cwb); if (co != null) {
		 * CwbOrderAddressCodeEditTypeEnum addressCodeEditType =
		 * CwbOrderAddressCodeEditTypeEnum.WeiPiPei; if
		 * ((co.getAddresscodeedittype() ==
		 * CwbOrderAddressCodeEditTypeEnum.DiZhiKu.getValue()) ||
		 * (co.getAddresscodeedittype() ==
		 * CwbOrderAddressCodeEditTypeEnum.XiuGai.getValue())) {//
		 * 如果修改的数据原来是地址库匹配的或者是后来修改的 addressCodeEditType =
		 * CwbOrderAddressCodeEditTypeEnum.XiuGai; } else if
		 * ((co.getAddresscodeedittype() ==
		 * CwbOrderAddressCodeEditTypeEnum.WeiPiPei.getValue()) ||
		 * (co.getAddresscodeedittype() ==
		 * CwbOrderAddressCodeEditTypeEnum.RenGong.getValue())) {//
		 * 如果修改的数据原来是为匹配的 // 都将匹配状态变更为人工修改 addressCodeEditType =
		 * CwbOrderAddressCodeEditTypeEnum.RenGong; } try {
		 * this.cwbOrderService.updateDeliveryBranch(this.getSessionUser(), co,
		 * branch, addressCodeEditType);
		 * this.logger.info("客服管理--订单信息修改--地址库匹配订单号:{},站点{}", cwb, branchname);
		 * } catch (Exception e) { return "{\"errorCode\":1,\"error\":\"" +
		 * e.getMessage() + "\"}"; } } return
		 * "{\"errorCode\":0,\"error\":\"修改成功\"}";
		 */
		return branches;
	}
}

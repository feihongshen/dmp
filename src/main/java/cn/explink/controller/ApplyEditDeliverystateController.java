package cn.explink.controller;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

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
import cn.explink.b2c.tools.ObjectUnMarchal;
import cn.explink.dao.ApplyEditDeliverystateDAO;
import cn.explink.dao.BranchDAO;
import cn.explink.dao.CustomerDAO;
import cn.explink.dao.CwbDAO;
import cn.explink.dao.DeliveryStateDAO;
import cn.explink.dao.ExceptionCwbDAO;
import cn.explink.dao.ExportmouldDAO;
import cn.explink.dao.ReasonDao;
import cn.explink.dao.SystemInstallDAO;
import cn.explink.dao.UserDAO;
import cn.explink.dao.ZhiFuApplyDao;
import cn.explink.domain.AccountCwbFareDetail;
import cn.explink.domain.ApplyEditDeliverystate;
import cn.explink.domain.Branch;
import cn.explink.domain.Customer;
import cn.explink.domain.CwbOrder;
import cn.explink.domain.DeliveryState;
import cn.explink.domain.EdtiCwb_DeliveryStateDetail;
import cn.explink.domain.Exportmould;
import cn.explink.domain.FeeWayTypeRemark;
import cn.explink.domain.Reason;
import cn.explink.domain.SystemInstall;
import cn.explink.domain.User;
import cn.explink.domain.ZhiFuApplyView;
import cn.explink.enumutil.ApplyEditDeliverystateIshandleEnum;
import cn.explink.enumutil.ApplyEnum;
import cn.explink.enumutil.BranchEnum;
import cn.explink.enumutil.CwbOrderTypeIdEnum;
import cn.explink.enumutil.EditCwbTypeEnum;
import cn.explink.enumutil.FlowOrderTypeEnum;
import cn.explink.enumutil.ReasonTypeEnum;
import cn.explink.exception.CwbException;
import cn.explink.exception.ExplinkException;
import cn.explink.pos.tools.SignTypeEnum;
import cn.explink.service.AdjustmentRecordService;
import cn.explink.service.CwbOrderService;
import cn.explink.service.CwbOrderWithDeliveryState;
import cn.explink.service.EditCwbService;
import cn.explink.service.ExplinkUserDetail;
import cn.explink.service.OrgBillAdjustmentRecordService;
import cn.explink.util.DateTimeUtil;
import cn.explink.util.JsonUtil;
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
	AppearWindowDao appearWindowDao;
	@Autowired
	SystemInstallDAO systemInstallDAO;
	@Autowired
	AccountCwbFareDetailDAO accountCwbFareDetailDAO;
	@Autowired
	ZhiFuApplyDao zhiFuApplyDao;
	@Autowired
	CustomerDAO customerDao;
	@Autowired
	ExportmouldDAO exportmouldDAO;
	@Autowired
	EditCwbService editCwbService;
	@Autowired
	AdjustmentRecordService adjustmentRecordService;
	@Autowired
	OrgBillAdjustmentRecordService orgBillAdjustmentRecordService;
	

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	private User getSessionUser() {
		ExplinkUserDetail userDetail = (ExplinkUserDetail) this.securityContextHolderStrategy.getContext().getAuthentication().getPrincipal();
		return userDetail.getUser();
	}

	
	
	/**
	 * 重置反馈
	 *//*
	@RequestMapping("/resetFeedback")
	public String resetFeedback(Model model, @RequestParam(value = "cwbs", required = false, defaultValue = "") String cwbs, @RequestParam(value = "type", required = false, defaultValue = "0") int type) {
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
			return "applyeditdeliverystate/resetFeedback";
		}
		// 整理sql要读取的cwb end
		model.addAttribute("cwbArray", cwbArray);
		String cwbsSql = cwbsSqlBuffer.substring(0, cwbsSqlBuffer.length() - 1);
		List<CwbOrder> cwbList = cwbDAO.getCwbByCwbs(cwbsSql);
		Map<String, AccountCwbFareDetail> accountCwbFareDetailMap = accountCwbFareDetailDAO.getAccountCwbFareDetailMapByCwbs(cwbsSql);

		// 做重置审核状态更改的操作 start
		if (type == EditCwbTypeEnum.ChongZhiShenHeZhuangTai.getValue()) {
			List<CwbOrder> allowCwb = new ArrayList<CwbOrder>();// 允许更改订单
			List<CwbOrder> prohibitedCwb = new ArrayList<CwbOrder>(); // 禁止更改的订单
			for (CwbOrder co : cwbList) {

				// 判断订单当前状态为36 已审核状态的订单才能重置审核状态
				if (co.getFlowordertype() == FlowOrderTypeEnum.YiShenHe.getValue()) {
					// 判断订单号是否为POS刷卡 posremark=POS刷卡 POS刷卡的订单不允许重置审核状态
					DeliveryState ds = deliveryStateDAO.getDeliveryStateByCwb(co.getCwb()).get(0);
					if (co.getInfactfare().compareTo(BigDecimal.ZERO) > 0 && (accountCwbFareDetailMap.get(co.getCwb()) == null ? 0 : accountCwbFareDetailMap.get(co.getCwb()).getFareid()) > 0) {
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
				cods.setDeliveryState(deliveryStateDAO.getDeliveryByCwbAndDeliverystate(co.getCwb()));
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
				cods.setDeliveryState(deliveryStateDAO.getDeliveryByCwbAndDeliverystate(co.getCwb()));
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
				cods.setDeliveryState(deliveryStateDAO.getDeliveryByCwbAndDeliverystate(co.getCwb()));
				// 已经归班的订单不能修改订单类型，必须充值审核状态才能修改
				if (cods.getDeliveryState() != null && cods.getDeliveryState().getGcaid() > 0) {
					cods.setError("已审核的订单不允许修改订单类型，若要修改，请重置审核状态后再试");
					prohibitedCods.add(cods);
				} else if (cods.getDeliveryState() != null && co.getCwbordertypeid() == CwbOrderTypeIdEnum.Shangmentui.getValue()
						&& cods.getDeliveryState().getInfactfare().compareTo(BigDecimal.ZERO) > 0) {
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

		return "applyeditdeliverystate/resetFeedback";
	}*/
	/**
	 * 重置反馈并显示查询列表
	 */
	@RequestMapping("/resetFeedbackList")
	public String resetFeedbackList(Model model,HttpServletRequest request,
			@RequestParam(value = "cwb",defaultValue = "",required = false) String cwbs,
			@RequestParam(value = "cwbtypeid",defaultValue = "",required = false) Integer cwbtypeid,
			@RequestParam(value = "cwbresultid",defaultValue = "",required = false) Long cwbresultid,
			@RequestParam(value = "isdo",defaultValue = "",required = false) Integer isdo,
			@RequestParam(value = "cwbstate",defaultValue = "",required = false) Long cwbstate,
			@RequestParam(value = "feedbackbranchid",defaultValue = "",required = false) Long feedbackbranchid,
			@RequestParam(value = "begindate",defaultValue = "",required = false) String begindate,
			@RequestParam(value = "enddate",defaultValue = "",required = false) String enddate
			){
		
		List<Branch> branchList = branchDAO.getAllBranches();
		Map<Long, String> branchMap = new HashMap<Long, String>();
		for(Branch br:branchList){
			branchMap.put(br.getBranchid(), br.getBranchname());
		}
		if(cwbs==null){
			List<ApplyEditDeliverystate> applyeditlist = new ArrayList<ApplyEditDeliverystate>();
			if(cwbs.length()>0&&!"".equals(cwbs.trim())){
				String[] cwbss = cwbs.split("\r\n");
				StringBuffer sb = new StringBuffer("");
				for(String cwb:cwbss){
					sb.append("'").append(cwb).append("',");
				}
				applyeditlist = applyEditDeliverystateDAO.getApplyEditBycwbs(sb.substring(0, sb.length()-1).toString());
			}else{
				applyeditlist = applyEditDeliverystateDAO.getAppliedEditDeliverystateByOthers(cwbtypeid,cwbresultid,isdo,cwbstate,feedbackbranchid );
			}
			model.addAttribute("applyeditlist",applyeditlist);
		}
		model.addAttribute("branchList", branchList);
		model.addAttribute("branchMap", branchMap);
		return "applyeditdeliverystate/resetFeedbackList";
	}
	
	//处理为审核通过状态
	@RequestMapping("/getCheckboxDealPass")
	public @ResponseBody String getCheckboxDealPass(
			@RequestParam(value="cwbdata",defaultValue="",required = false) String cwbdata
			){
		if(cwbdata.equals("")){
			return null;
		}else{
			String cwbs = cwbdata.substring(0,cwbdata.length()-1);
			//StringBuffer sb = new StringBuffer();
			for(String cwb:cwbs.split(",")){
				applyEditDeliverystateDAO.updateShenheStatePass(cwb);
				ApplyEditDeliverystate aeds = applyEditDeliverystateDAO.getApplyED(cwb);
				//重置审核的最终方法
				EdtiCwb_DeliveryStateDetail ec_dsd = editCwbService.analysisAndSaveByChongZhiShenHe(cwb, aeds.getApplyuserid(), getSessionUser().getUserid());
			}
			//List<ApplyEditDeliverystate> list = applyEditDeliverystateDAO.getApplyEditBycwbs(sb.substring(0,sb.length()-1));
			
		}
		return null;
	}
	//处理为审核通过状态
	@RequestMapping("/getCheckboxDealNoPass")
	public @ResponseBody String getCheckboxDealNoPass(
			@RequestParam(value="cwbdata",defaultValue="",required = false) String cwbdata
			){
		if(cwbdata.equals("")){
			return null;
		}else{
			String cwbs = cwbdata.substring(0,cwbdata.length()-1);
			//StringBuffer sb = new StringBuffer();
			for(String cwb:cwbs.split(",")){
				applyEditDeliverystateDAO.updateShenheStateNoPass(cwb);
			}
			
		}
		return null;
	}
	
	
	
	//获取需要审核的订单并进行处理成通过审核
	@RequestMapping("/editPaywayInfoModifyCheckpass")
	public  @ResponseBody String editPaywayInfoModifyCheckpass(HttpServletRequest request){
		String applyids = request.getParameter("applyids");
		for(String applyid:applyids.split(",")){
			int applyidint = Integer.parseInt(applyid);
			zhiFuApplyDao.updateStatePassByCwb(applyidint);//更改状态为通过审核
		}
		return "{\"code\":1,\"msg\":\"true\"}";
	}

	/**
	 * 支付信息修改确认
	 */
	@RequestMapping("/paywayInfoModifyConfirm")
	public String paywayInfoModifyConfirm(Model model,HttpServletRequest request,
		@RequestParam(value = "exportmould", defaultValue = "", required = true) String exportmould,
		@RequestParam(value = "cwb", defaultValue = "", required = true) String cwbs,
		@RequestParam(value = "cwbtypeid", defaultValue = "0", required = true) int cwbtypeid,
		@RequestParam(value = "applypeople", defaultValue = "0", required = true) long applypeople,
		@RequestParam(value = "applytype", defaultValue = "0", required = true) int applytype,
		@RequestParam(value = "userid", defaultValue = "0", required = true) int userid,
		@RequestParam(value = "shenhestate", defaultValue = "0", required = true) int shenhestate,
		@RequestParam(value = "shenheresult", defaultValue = "0", required = true) int shenheresult
			) {
		List<ZhiFuApplyView> zflist = zhiFuApplyDao.getAllZFAVBycwbs();
		List<User> uslist = userDAO.getAllUser();
		Map<Long, String> userMap = new HashMap<Long, String>();
		for(ZhiFuApplyView zf:zflist){
			for(User us:uslist){
				if(us.getUserid()==zf.getUserid()){
					userMap.put(us.getUserid(),us.getUsername());
				}
			}
		}
		model.addAttribute("userMap", userMap);
		
		List<Customer> customerList = customerDao.getAllCustomers();
		Map<Long, String> customerMap = new HashMap<Long, String>();
		for(Customer cus:customerList){
			customerMap.put(cus.getCustomerid(),cus.getCustomername());
		}
		model.addAttribute("customerMap",customerMap);
		List<Branch> branchList = branchDAO.getQueryBranchByBranchsiteAndUserid(getSessionUser().getUserid(), String.valueOf(BranchEnum.ZhanDian.getValue()));
		Map<Long, String> braMap = new HashMap<Long, String>();
		for(Branch bra : branchList){
			braMap.put(bra.getBranchid(), bra.getBranchname());
		}
		model.addAttribute("bramap",braMap);
		
		List<Exportmould> exportmouldlist = exportmouldDAO.getAllExportmouldByUser(getSessionUser().getRoleid());
		model.addAttribute("exportmouldlist", exportmouldlist);
		model.addAttribute("customerList", customerList);
		model.addAttribute("branchList", branchList);
		List<ZhiFuApplyView> zhifulist = null;
		if (cwbs.length() > 0) {
			StringBuffer sb = new StringBuffer("");
			for (String cwbStr : cwbs.split("\r\n")) {
				if (cwbStr.trim().length() == 0) {
					continue;
				}
				
				sb.append("'"+cwbStr+"',");
			}
			zhifulist = zhiFuApplyDao.getZFAVBycwbss(sb.toString().substring(0,sb.lastIndexOf(",")));
			//request.getSession().setAttribute("exportcwbs", cwbs.substring(0, cwbs.length() - 1));
			model.addAttribute("zhifulist", zhifulist);
		}else if(cwbs==null||"".equals(cwbs.trim())){
			zhifulist = zhiFuApplyDao.getapplycwbss(cwbtypeid,applytype,userid,shenhestate,shenheresult);
			model.addAttribute("zhifulist", zhifulist);
		}


		return "applyeditdeliverystate/paywayInfoModifyConfirm";
	}
	
	//通过最终结算确认
	@RequestMapping("/editPaywayInfoModifyConfirmpass")
	public @ResponseBody String editPaywayInfoModifyConfirmpass(Model model,HttpServletRequest request) throws Exception{
		String applyids = request.getParameter("applyids");
		
		List<EdtiCwb_DeliveryStateDetail> ecList = new ArrayList<EdtiCwb_DeliveryStateDetail>();
		List<String> errorList = new ArrayList<String>();
		
		for(String applyid:applyids.split(",")){
			zhiFuApplyDao.updateStateConfirmPassByCwb(applyid);//更改状态为确认通过
			ZhiFuApplyView zfav = zhiFuApplyDao.getZhiFuViewByApplyid(applyid);
			FeeWayTypeRemark fwtr = JsonUtil.readValue(zfav.getFeewaytyperemark(),FeeWayTypeRemark.class);
			if(zfav.getApplyway()==ApplyEnum.dingdanjinE.getValue()){
				todoConfirmFeeResult(fwtr,ecList,errorList,model); //修改金额时的最终结算部分操作
				zhiFuApplyDao.updateApplyResult(Integer.parseInt(applyid));
				return "{\"errorCode\":0,\"msg\":\"true1\"}";
				//return "editcwb/XiuGaiJinEResult";
			}else if (zfav.getApplyway()==ApplyEnum.zhifufangshi.getValue()){
				todoConfirmWayResult(fwtr,ecList,errorList,model);
				zhiFuApplyDao.updateApplyResult(Integer.parseInt(applyid));
				return "{\"errorCode\":0,\"msg\":\"true2\"}";
				//return "editcwb/XiuGaiZhiFuFangShiResult";
			}else if (zfav.getApplyway()==ApplyEnum.dingdanleixing.getValue()){
				todoConfirmTypeResult(fwtr,ecList,errorList,model);
				zhiFuApplyDao.updateApplyResult(Integer.parseInt(applyid));	
				return "{\"errorCode\":0,\"msg\":\"true3\"}";
				//return "editcwb/XiuGaiDingDanLeiXingResult";
			}
		}
		
		return "";
	}
	//对订单类型进行修改的确认lx
	public void todoConfirmTypeResult(FeeWayTypeRemark fwtr,List<EdtiCwb_DeliveryStateDetail> ecList,List<String> errorList,Model model){
		String cwb = fwtr.getCwb();
		int cwbordertypeid = fwtr.getCwbordertypeid();
		int newcwbordertypeid = fwtr.getNewcwbordertypeid();
		long requestUser = fwtr.getRequestUser();
		try {
			EdtiCwb_DeliveryStateDetail ec_dsd = editCwbService.analysisAndSaveByXiuGaiDingDanLeiXing(cwb, cwbordertypeid, newcwbordertypeid, requestUser, getSessionUser().getUserid());
			ecList.add(ec_dsd);
		} catch (ExplinkException ee) {
			errorList.add(cwb + "_" + ee.getMessage());
		} catch (Exception e) {
			errorList.add(cwb + "_不确定_系统内部报错！");
			e.printStackTrace();
		}
		model.addAttribute("ecList", ecList);
		model.addAttribute("errorList", errorList);
	}
	//对支付方式进行修改的确认lx
	public void todoConfirmWayResult(FeeWayTypeRemark fwtr,List<EdtiCwb_DeliveryStateDetail> ecList,List<String> errorList,Model model){
		String cwb = fwtr.getCwb();
		int paywayid = fwtr.getPaywayid();
		int newpaywayid = fwtr.getNewpaywayid();
		long requestUser = fwtr.getRequestUser();
		try {
			EdtiCwb_DeliveryStateDetail ec_dsd = editCwbService.analysisAndSaveByXiuGaiZhiFuFangShi(cwb, paywayid, newpaywayid, requestUser, getSessionUser().getUserid());
			//added by jiangyu begin
			adjustmentRecordService.createAdjustmentRecordByPayType(cwb, paywayid, newpaywayid);
			orgBillAdjustmentRecordService.createAdjustmentRecordByPayType(cwb,paywayid,newpaywayid);
			//修改支付方式,判断是否生成调整单
			//added by jiangyu end
			ecList.add(ec_dsd);
		} catch (ExplinkException ee) {
			errorList.add(cwb + "_" + ee.getMessage());
		} catch (Exception e) {
			errorList.add(cwb + "_不确定_系统内部报错！");
			e.printStackTrace();
		}
		model.addAttribute("ecList", ecList);
		model.addAttribute("errorList", errorList);
	}
	
	//对金额更改的最终确认方法lx
	public void todoConfirmFeeResult(FeeWayTypeRemark fwtr,List<EdtiCwb_DeliveryStateDetail> ecList,List<String> errorList,Model model){
		String cwb = fwtr.getCwb();
		String isDeliveryState = fwtr.getIsDeliveryState();
		BigDecimal receivablefee = fwtr.getReceivablefee();
		BigDecimal cash = fwtr.getCash();
		BigDecimal pos = fwtr.getPos();
		BigDecimal checkfee = fwtr.getCheckfee();
		BigDecimal otherfee = fwtr.getOtherfee();
		BigDecimal paybackfee = fwtr.getPaybackfee();
		long requestUser = fwtr.getRequestUser();
		List<User> userList = userDAO.getUserByid(requestUser);
		
		CwbOrder cwbOrder = new CwbOrder();
		cwbOrder = this.cwbDAO.getCwbByCwb(cwb);
		User user = new User();
		if (userList.size() > 0) {
			user = userList.get(0);
		}

		// 先判断是有账单 获取到修改订单金额的值,进行判断插入到数据库中
		if ((receivablefee != null) && !receivablefee.equals(cwbOrder.getReceivablefee())) {
//			this.adjustmentRecordService.createAdjustmentRecode(cwb, cwbOrder.getCustomerid(), cwbOrder.getReceivablefee(), paybackfee, receivablefee, "", user.getUsername(),cwbOrder.getCwbordertypeid());
			//客户调整单逻辑入口
			this.adjustmentRecordService.processAdjusRecordByMoney(cwbOrder, paybackfee, receivablefee, "", user.getUsername());
			//站内调整单逻辑入口
			this.orgBillAdjustmentRecordService.createOrgBillAdjustRecord(cwbOrder,user,receivablefee,paybackfee);
		}
		
		try {
			EdtiCwb_DeliveryStateDetail ec_dsd = editCwbService.analysisAndSaveByXiuGaiJinE(cwb, isDeliveryState, receivablefee, cash, pos, checkfee, otherfee, paybackfee, requestUser,
					getSessionUser().getUserid());
			ecList.add(ec_dsd);
		} catch (ExplinkException ee) {
			errorList.add(cwb + "_" + ee.getMessage());
		} catch (Exception e) {
			errorList.add(cwb + "_" + FlowOrderTypeEnum.YiShenHe.getValue() + "_系统内部报错！");
			e.printStackTrace();
		}
		model.addAttribute("ecList", ecList);
		model.addAttribute("errorList", errorList);
	}
	
	//未通过结算部分确认
	@RequestMapping("/editPaywayInfoModifyConfirmnopass")
	public @ResponseBody String editPaywayInfoModifyConfirmnopass(HttpServletRequest request){
		String applyids = request.getParameter("applyids");
		for(String applyid:applyids.split(",")){
			zhiFuApplyDao.updateStateConfirmNopassByCwb(Integer.parseInt(applyid));//更改状态为确认不通过
			ZhiFuApplyView zfav = zhiFuApplyDao.getZhiFuViewByApplyid(applyid);
			if(zfav.getApplyway()==ApplyEnum.dingdanjinE.getValue()){
				return "{\"errorCode\":0,\"msg\":\"true1\"}";
			}else if (zfav.getApplyway()==ApplyEnum.zhifufangshi.getValue()){
				return "{\"errorCode\":0,\"msg\":\"true2\"}";
			}else if (zfav.getApplyway()==ApplyEnum.dingdanleixing.getValue()){
				return "{\"errorCode\":0,\"msg\":\"true3\"}";
			}
		}
		return null;
	}
	
	
	//机构部分
	/**
	 * 进入申请修改订单配送结果页面
	 * 
	 * @param page
	 * @param model
	 * @param cwb
	 * @return
	 */

	@RequestMapping("/toCreateApplyEditDeliverystate/{page}")
	public String toCreateApplyEditDeliverystate(@PathVariable("page") long page, Model model, @RequestParam(value = "cwb", defaultValue = "", required = false) String cwb
			) {
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
					CwbOrder co = cwbDAO.getCwbByCwbLock(cwbStr);
					List<ApplyEditDeliverystate> aedsList = applyEditDeliverystateDAO.getApplyEditDeliverystateByCwb(cwbStr, ApplyEditDeliverystateIshandleEnum.WeiChuLi.getValue());

					DeliveryState ds = deliveryStateDAO.getDeliveryByCwb(cwbStr);
					ApplyEditDeliverystate aeds = new ApplyEditDeliverystate();
					if (co != null && aedsList.size() == 0) {
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
						aeds.setCwbstate(co.getCwbstate());
						aeds.setApplyuserid(getSessionUser().getUserid());
						aeds.setApplybranchid(getSessionUser().getBranchid());
						aeds.setApplytime(nowtime);
						aeds.setShenhestate(1);//待审核状态
						applyEditDeliverystateDAO.creApplyEditDeliverystate(aeds);
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

	@RequestMapping("/toCreateApplyEditDeliverystateAgin")
	public @ResponseBody String toCreateApplyEditDeliverystateAgin(Model model,
			//处理在修改时应该
			@RequestParam(value="cwbss",defaultValue = "", required = false) String cwbss,
			@RequestParam(value = "editnowdeliverystate", defaultValue = "0", required = false) long editnowdeliverystate,
			@RequestParam(value = "editreason", defaultValue = "", required = false) String editreason
			
			) {
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date date = new Date();
			String nowtime = df.format(date);
			StringBuffer errorCwbs = new StringBuffer();

				// 判断是否符合申请条件：1.未反馈给电商 2.未交款
				DeliveryState deliverystate = deliveryStateDAO.getActiveDeliveryStateByCwb(cwbss);
				if (deliverystate == null || deliverystate.getDeliverystate() == 0) {
					errorCwbs.append(cwbss + ":未反馈的订单不能申请修改反馈状态！");
				} else if (deliverystate != null && deliverystate.getPayupid() == 0 && deliverystate.getIssendcustomer() == 0) {
					CwbOrder co = cwbDAO.getCwbByCwbLock(cwbss);
					List<ApplyEditDeliverystate> aedsLists = applyEditDeliverystateDAO.getAppliedEditDeliverystateByCwb(cwbss);
					DeliveryState ds = deliveryStateDAO.getDeliveryByCwb(cwbss);
					ApplyEditDeliverystate aeds = new ApplyEditDeliverystate();
					if(co!=null&&aedsLists!=null&&!"".equals(cwbss)){//如果订单已经被修改过则直接添加字段插入表中(实现多次存储已修改数据)
						aeds.setCwb(cwbss);
						aeds.setOpscwbid(co.getOpscwbid());
						aeds.setDeliverystateid(ds.getId());
						aeds.setCwbordertypeid(co.getCwbordertypeid());
						aeds.setNowdeliverystate(ds.getDeliverystate());
						aeds.setNopos(ds.getCash().add(ds.getCheckfee()).add(ds.getOtherfee()));
						aeds.setPos(ds.getPos());
						aeds.setDeliverid(ds.getDeliveryid());
						aeds.setCwbstate(co.getCwbstate());
						aeds.setApplyuserid(getSessionUser().getUserid());
						aeds.setApplybranchid(getSessionUser().getBranchid());
						aeds.setApplytime(nowtime);
						aeds.setShenhestate(1);
						aeds.setEditreason(editreason);
						aeds.setState(1);
						applyEditDeliverystateDAO.creApplyEditDeliverystateNew(aeds);
					}
						
				} else {
					if (deliverystate.getPayupid() > 0) {
						errorCwbs.append(cwbss + ":已上交款不能申请修改反馈状态！");
					} else {
						errorCwbs.append(cwbss + ":状态已推送给电商不能申请修改反馈状态！");
					}
				}
			
		return "{\"errorCode\":0,\"error\":\"再次提交申请\"}";
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
	 * 重置反馈
	 */
	@RequestMapping("/resetFeedback")
	public String resetFeedback(Model model, @RequestParam(value = "cwbs", required = false, defaultValue = "") String cwbs, @RequestParam(value = "type", required = false, defaultValue = "0") int type) {
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
			return "applyeditdeliverystate/resetFeedback";
		}
		// 整理sql要读取的cwb end
		model.addAttribute("cwbArray", cwbArray);
		String cwbsSql = cwbsSqlBuffer.substring(0, cwbsSqlBuffer.length() - 1);
		List<CwbOrder> cwbList = cwbDAO.getCwbByCwbs(cwbsSql);
		Map<String, AccountCwbFareDetail> accountCwbFareDetailMap = accountCwbFareDetailDAO.getAccountCwbFareDetailMapByCwbs(cwbsSql);

		// 做重置审核状态更改的操作 start
		if (type == EditCwbTypeEnum.ChongZhiShenHeZhuangTai.getValue()) {
			List<CwbOrder> allowCwb = new ArrayList<CwbOrder>();// 允许更改订单
			List<CwbOrder> prohibitedCwb = new ArrayList<CwbOrder>(); // 禁止更改的订单
			for (CwbOrder co : cwbList) {

				// 判断订单当前状态为36 已审核状态的订单才能重置审核状态
				if (co.getFlowordertype() == FlowOrderTypeEnum.YiShenHe.getValue()) {
					// 判断订单号是否为POS刷卡 posremark=POS刷卡 POS刷卡的订单不允许重置审核状态
					DeliveryState ds = deliveryStateDAO.getDeliveryStateByCwb(co.getCwb()).get(0);
					if (co.getInfactfare().compareTo(BigDecimal.ZERO) > 0 && (accountCwbFareDetailMap.get(co.getCwb()) == null ? 0 : accountCwbFareDetailMap.get(co.getCwb()).getFareid()) > 0) {
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
				cods.setDeliveryState(deliveryStateDAO.getDeliveryByCwbAndDeliverystate(co.getCwb()));
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
				cods.setDeliveryState(deliveryStateDAO.getDeliveryByCwbAndDeliverystate(co.getCwb()));
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
				cods.setDeliveryState(deliveryStateDAO.getDeliveryByCwbAndDeliverystate(co.getCwb()));
				// 已经归班的订单不能修改订单类型，必须充值审核状态才能修改
				if (cods.getDeliveryState() != null && cods.getDeliveryState().getGcaid() > 0) {
					cods.setError("已审核的订单不允许修改订单类型，若要修改，请重置审核状态后再试");
					prohibitedCods.add(cods);
				} else if (cods.getDeliveryState() != null && co.getCwbordertypeid() == CwbOrderTypeIdEnum.Shangmentui.getValue()
						&& cods.getDeliveryState().getInfactfare().compareTo(BigDecimal.ZERO) > 0) {
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

		return "applyeditdeliverystate/resetFeedback";
	}
	/**
	 * 重置反馈列表
	 */
	@RequestMapping("/resetFeedbackList")
	public String resetFeedbackList(){

		return "applyeditdeliverystate/resetFeedbackList";
	}
	/**
	 * 支付信息修改审核
	 */
	@SuppressWarnings("unused")
	@RequestMapping("/paywayInfoModifyCheck")
	public String paywayInfoModifyCheck(Model model,HttpServletRequest request,
			@RequestParam(value = "exportmould", defaultValue = "", required = true) String exportmould,
			@RequestParam(value = "cwb", defaultValue = "", required = true) String cwbs,
			@RequestParam(value = "cwbtypeid", defaultValue = "0", required = true) int cwbtypeid,
			@RequestParam(value = "applypeople", defaultValue = "0", required = true) long applypeople,
			@RequestParam(value = "applytype", defaultValue = "0", required = true) int applytype,
			@RequestParam(value = "userid", defaultValue = "0", required = true) int userid,
			@RequestParam(value = "shenhestate", defaultValue = "0", required = true) int shenhestate,
			@RequestParam(value = "shenheresult", defaultValue = "0", required = true) int shenheresult
			) {
		List<ZhiFuApplyView> zflist = zhiFuApplyDao.getAllZFAVBycwbs();
		List<User> uslist = userDAO.getAllUser();
		Map<Long,String> userMap = new HashMap<Long, String>();
		for(ZhiFuApplyView zf:zflist){
			for(User us:uslist){
				if(us.getUserid()==zf.getUserid()){
					userMap.put(us.getUserid(), us.getUsername());
				}
			}
		}
		model.addAttribute("applytype",applytype);
		model.addAttribute("userMap", userMap);
		List<Customer> customerList = customerDao.getAllCustomers();
		Map<Long, String> customermap = new HashMap<Long, String>();
		for(Customer cus:customerList){
			customermap.put(cus.getCustomerid(),cus.getCustomername());
		}
		model.addAttribute("customermap",customermap);
		List<Branch> branchList = branchDAO.getQueryBranchByBranchsiteAndUserid(getSessionUser().getUserid(), String.valueOf(BranchEnum.ZhanDian.getValue()));
		Map<Long, String> bramap = new HashMap<Long, String>();
		for(Branch bra : branchList){
			bramap.put(bra.getBranchid(), bra.getBranchname());
		}
		model.addAttribute("bramap",bramap);
		
		List<Exportmould> exportmouldlist = exportmouldDAO.getAllExportmouldByUser(getSessionUser().getRoleid());
		model.addAttribute("exportmouldlist", exportmouldlist);
		model.addAttribute("customerList", customerList);
		model.addAttribute("branchList", branchList);
		List<ZhiFuApplyView> zhifulist = null;
		if (cwbs.length() > 0) {
			StringBuffer sb = new StringBuffer("");
			for (String cwbStr : cwbs.split("\r\n")) {
				if (cwbStr.trim().length() == 0) {
					continue;
				}
				sb.append("'"+cwbStr+"',");
			}
			zhifulist = zhiFuApplyDao.getZFAVBycwbs(sb.toString().substring(0,sb.lastIndexOf(",")));
			//request.getSession().setAttribute("exportcwbs", cwbs.substring(0, cwbs.length() - 1));
			model.addAttribute("zhifulist", zhifulist);
		}else if(cwbs==null||"".equals(cwbs.trim())){
			zhifulist = zhiFuApplyDao.getapplycwbs(cwbtypeid,applytype,userid,shenhestate,shenheresult);
			model.addAttribute("zhifulist", zhifulist);
		}
		return "applyeditdeliverystate/paywayInfoModifyCheck";
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
		logger.info("web-agreeEditDeliveryState-进入单票反馈cwb={}",applyEditDeliverystate.getCwb());
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
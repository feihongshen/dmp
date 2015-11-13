package cn.explink.controller;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
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
import cn.explink.dao.ApplyEditDeliverystateDAO;
import cn.explink.dao.BranchDAO;
import cn.explink.dao.CustomerDAO;
import cn.explink.dao.CwbDAO;
import cn.explink.dao.DeliveryStateDAO;
import cn.explink.dao.ExceptionCwbDAO;
import cn.explink.dao.ExportmouldDAO;
import cn.explink.dao.ReasonDao;
import cn.explink.dao.UserDAO;
import cn.explink.dao.ZhiFuApplyDao;
import cn.explink.domain.ApplyEditDeliverystate;
import cn.explink.domain.Branch;
import cn.explink.domain.Customer;
import cn.explink.domain.CwbOrder;
import cn.explink.domain.DeliveryState;
import cn.explink.domain.EdtiCwb_DeliveryStateDetail;
import cn.explink.domain.FeeWayTypeRemark;
import cn.explink.domain.Reason;
import cn.explink.domain.User;
import cn.explink.domain.ZhiFuApplyView;
import cn.explink.enumutil.ApplyEditDeliverystateIshandleEnum;
import cn.explink.enumutil.ApplyEnum;
import cn.explink.enumutil.BranchEnum;
import cn.explink.enumutil.DeliveryStateEnum;
import cn.explink.enumutil.ReasonTypeEnum;
import cn.explink.exception.CwbException;
import cn.explink.pos.tools.SignTypeEnum;
import cn.explink.service.AdjustmentRecordService;
import cn.explink.service.CwbOrderService;
import cn.explink.service.CwbOrderWithDeliveryState;
import cn.explink.service.EditCwbService;
import cn.explink.service.ExplinkUserDetail;
import cn.explink.service.ExportService;
import cn.explink.service.OrgBillAdjustmentRecordService;
import cn.explink.util.DateTimeUtil;
import cn.explink.util.ExcelUtilsHandler;
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
	@Autowired
	ExportService exportService;

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	private User getSessionUser() {
		ExplinkUserDetail userDetail = (ExplinkUserDetail) securityContextHolderStrategy.getContext().getAuthentication().getPrincipal();
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
	@RequestMapping("/resetFeedbackList/{page}")
	public String resetFeedbackList(Model model,HttpServletRequest request,
			@PathVariable(value = "page") long page, 
			@RequestParam(value = "cwb",defaultValue = "",required = false) String cwbs,
			@RequestParam(value = "cwbtypeid",defaultValue = "0",required = false) Integer cwbtypeid,
			@RequestParam(value = "cwbresultid",defaultValue = "0",required = false) Long cwbresultid,
			@RequestParam(value = "isdo",defaultValue = "0",required = false) Integer isdo,
			@RequestParam(value = "cwbstate",defaultValue = "0",required = false) Long cwbstate,
			@RequestParam(value = "feedbackbranchid",defaultValue = "0",required = false) Long feedbackbranchid,
			@RequestParam(value = "begindate",defaultValue = "",required = false) String begindate,
			@RequestParam(value = "enddate",defaultValue = "",required = false) String enddate
			){
		Page pag = new Page();
		List<User> uslist = userDAO.getAllUser();
		List<Branch> branchList = branchDAO.getQueryBranchByBranchsiteAndUserid(getSessionUser().getUserid(), String.valueOf(BranchEnum.ZhanDian.getValue()));
		List<Branch> branchViewList = branchDAO.getAllEffectBranches();
		
		List<Reason> reasonList = reasonDAO.getAllReason();
		Map<String, String> reasonMap = new HashMap<String, String>();
		for(Reason reason : reasonList){
			reasonMap.put(reason.getReasonid()+"", reason.getReasoncontent());
		}		
		
		StringBuffer sb = new StringBuffer("");
		if(!cwbs.equals("")){
			for(String str:cwbs.split("\r\n")){
				sb.append("'").append(str).append("',");
			}
		}
		String cwbss = "";
		if(sb.length()>0){
			cwbss = sb.substring(0,sb.length()-1);
		}
		List<CwbOrderView> covList = new ArrayList<CwbOrderView>();
		List<ApplyEditDeliverystate> applyeditlist = new ArrayList<ApplyEditDeliverystate>();
		if(!(cwbs.equals("")&&begindate.equals(""))){
			applyeditlist = this.applyEditDeliverystateDAO.getAppliedEditDeliverystateByOthers(page,cwbss,cwbtypeid,cwbresultid,isdo,cwbstate,feedbackbranchid );
			long count = this.applyEditDeliverystateDAO.getAppliedEditDeliverystateCount(cwbss, cwbtypeid, cwbresultid, isdo, cwbstate, feedbackbranchid);
			pag = new Page(count,page,Page.ONE_PAGE_NUMBER);
			covList = this.cwborderService.getResetCwbOrderView(applyeditlist,uslist,branchViewList,reasonMap);
		}
		model.addAttribute("page",page);
		model.addAttribute("page_obj",pag);
		model.addAttribute("branchList", branchList);
		model.addAttribute("applyeditlist",covList);
		return "applyeditdeliverystate/resetFeedbackList";
	}
	
	
	/**
	 * 重置反馈并显示查询列表
	 */
	@RequestMapping("/rfbExportExcel")
	public void rfbExportExcel(Model model,HttpServletRequest request,HttpServletResponse response,
			@RequestParam(value = "cwb",defaultValue = "",required = false) String cwbs,
			@RequestParam(value = "cwbtypeid",defaultValue = "0",required = false) Integer cwbtypeid,
			@RequestParam(value = "cwbresultid",defaultValue = "0",required = false) Long cwbresultid,
			@RequestParam(value = "isdo",defaultValue = "0",required = false) Integer isdo,
			@RequestParam(value = "cwbstate",defaultValue = "0",required = false) Long cwbstate,
			@RequestParam(value = "feedbackbranchid",defaultValue = "0",required = false) Long feedbackbranchid,
			@RequestParam(value = "begindate",defaultValue = "",required = false) String begindate,
			@RequestParam(value = "enddate",defaultValue = "",required = false) String enddate
			){
		List<User> uslist = userDAO.getAllUser();
		List<Branch> branchList = branchDAO.getQueryBranchByBranchsiteAndUserid(getSessionUser().getUserid(), String.valueOf(BranchEnum.ZhanDian.getValue()));
		List<Reason> reasonList = reasonDAO.getAllReason();
		Map<String, String> reasonMap = new HashMap<String, String>();
		for(Reason reason : reasonList){
			reasonMap.put(reason.getReasonid()+"", reason.getReasoncontent());
		}		
		String cwbss = getCwbs(cwbs);
		List<ApplyEditDeliverystate> applyeditlist =  this.applyEditDeliverystateDAO.getAppliedEditDeliverystate(cwbss,cwbtypeid,cwbresultid,isdo,cwbstate,feedbackbranchid );
		List<CwbOrderView> covList = this.cwborderService.getResetCwbOrderView(applyeditlist,uslist,branchList,reasonMap);
		String[] cloumnName1 = new String[14]; // 导出的列名
		String[] cloumnName2 = new String[14]; // 导出的英文列名
		this.exportService.SetResetFeedbackOrderFields(cloumnName1, cloumnName2);
		String sheetName = "重置反馈状态"; // sheet的名称
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
		String fileName = "Reset_Feed_Back_" + df.format(new Date()) + ".xlsx"; // 文件名  
		ExcelUtilsHandler.exportExcelHandler(response, cloumnName1, cloumnName2, sheetName, fileName, covList);
	}
	
	
	
	//处理为审核通过状态
	@RequestMapping("/getCheckboxDealPass")
	public @ResponseBody String getCheckboxDealPass(
			@RequestParam(value="cwbdata",defaultValue="",required = false) String cwbdata
			){
			long errorcount=0;
			String cwbStr = "已做过重置审核订单:";
			if(cwbdata.length()>0){
				for(String cwb:cwbdata.split(",")){
					try {
					long edituserid =  this.getSessionUser().getUserid();
					String edittime = DateTimeUtil.getNowTime();
					ApplyEditDeliverystate aeds = this.applyEditDeliverystateDAO.getApplyED(cwb);
					if(aeds!=null){
						//重置审核的最终方法
						EdtiCwb_DeliveryStateDetail ec_dsd = editCwbService.analysisAndSaveByChongZhiShenHe(cwb, aeds.getApplyuserid(), getSessionUser().getUserid());
						this.applyEditDeliverystateDAO.updateShenheStatePass(cwb,edituserid,edittime);//更改审核状态
						
						//重置反馈状态调整单逻辑(若原先配送结果为上门退、换、送成功则处理调整单逻辑)
						if( DeliveryStateEnum.ShangMenHuanChengGong.getValue() == ec_dsd.getDs().getDeliverystate()
								||
							DeliveryStateEnum.ShangMenTuiChengGong.getValue() == ec_dsd.getDs().getDeliverystate()
								||
							DeliveryStateEnum.PeiSongChengGong.getValue() == ec_dsd.getDs().getDeliverystate()
						){
							//客户调整单逻辑入口
							this.adjustmentRecordService.createAdjustment4ReFeedBack(cwb);
							//站内调整单逻辑入口
							this.orgBillAdjustmentRecordService.createAdjustment4ReFeedBack(cwb,ec_dsd);
							
							String auditingTime = ec_dsd.getDs().getAuditingtime();
							if (StringUtils.isNotEmpty(auditingTime)) {
								String todayStr = DateTimeUtil.formatDate(new Date(), DateTimeUtil.DEF_DATE_FORMAT);
								String autitingDateStr = DateTimeUtil.translateFormatDate(auditingTime, DateTimeUtil.DEF_DATETIME_FORMAT, DateTimeUtil.DEF_DATE_FORMAT);
								if (!todayStr.equals(autitingDateStr)) {//归班审核时间与重置反馈时间不在同一天生成订单调整记录
									//重置反馈状态生成调整记录(目前是为了站点签收余额报表增加的方法)
									this.editCwbService.createFnOrgOrderAdjustRecord(cwb,ec_dsd);
								}
							}
						}
						
					}else{
						cwbStr += cwb+",";
					}
				} catch (Exception e) {
					this.logger.error("订单号:"+cwb+"--产生异常原因:",e);
					e.printStackTrace();
					errorcount++;
				}
				}
			}
		if(cwbStr.length()>10){
			String cwbstr = cwbStr.substring(0, cwbStr.length()-1);
			return "{\"errorCode\":2,\"error\":\""+cwbstr+"\"}";
		}
		if (errorcount==0) {
			return "{\"errorCode\":0,\"error\":\"审核为通过\"}";
		}else if (errorcount==cwbdata.split(",").length) {
			return "{\"errorCode\":1,\"error\":\"审核单全部失败(有些订单已经审核或不是审核状态)\"}";
		}else {
			return "{\"errorCode\":1,\"error\":\"审核单部分失败(可能有些订单已经审核或不是审核状态)\"}";
		}
		

	}
	//处理为审核不通过状态
	@RequestMapping("/getCheckboxDealNoPass")
	public @ResponseBody String getCheckboxDealNoPass(
			@RequestParam(value="cwbdata",defaultValue="",required = false) String cwbdata
			){
		String cwbStr = "已做过重置审核订单:";
		try{
			if(cwbdata.length()>0){
				for(String cwb:cwbdata.split(",")){
					long edituserid =  this.getSessionUser().getUserid();
					String edittime = DateTimeUtil.getNowTime();
					ApplyEditDeliverystate aeds = this.applyEditDeliverystateDAO.getApplyED(cwb);
					if(aeds!=null){
						applyEditDeliverystateDAO.updateShenheStateNoPass(cwb,edituserid,edittime);
					}else{
						cwbStr += cwb+",";
					}
				}
				if(cwbStr.length()>10){
					String cwbstr = cwbStr.substring(0, cwbStr.length()-1);
					return "{\"errorCode\":2,\"error\":\""+cwbstr+"\"}";
				}
				return "{\"errorCode\":0,\"error\":\"审核为不通过\"}";
			}
		}catch(Exception e){
			e.printStackTrace();
			return "{\"errorCode\":1,\"error\":\"审核为不通过失败\"}";
		}
		return "";
	}
	
	/**
	 * 支付信息修改审核
	 */
	@SuppressWarnings("unused")
	@RequestMapping("/paywayInfoModifyCheck/{page}")
	public String paywayInfoModifyCheck(Model model,HttpServletRequest request,
			@PathVariable(value = "page") long page,
			@RequestParam(value = "cwb", defaultValue = "", required = false) String cwbs,
			@RequestParam(value = "cwbtypeid", defaultValue = "0", required = false) int cwbtypeid,
			@RequestParam(value = "applytype", defaultValue = "0", required = false) int applytype,
			@RequestParam(value = "applypeople", defaultValue = "0", required = false) int userid,
			@RequestParam(value = "applystate", defaultValue = "0", required = false) int applystate,
			@RequestParam(value = "applyresult", defaultValue = "0", required = false) int applyresult,
			@RequestParam(value = "isnow", defaultValue = "0", required = false) int isnow
			) {
		
		Page pag = new Page();
		List<User> uslist = userDAO.getAllUser();
		List<Customer> customerList = customerDao.getAllCustomers();
		List<Branch> branchList = branchDAO.getQueryBranchByBranchsiteAndUserid(getSessionUser().getUserid(), String.valueOf(BranchEnum.ZhanDian.getValue()));
		String cwbss = getCwbs(cwbs);
		List<ZhiFuApplyView> zavlist = new ArrayList<ZhiFuApplyView>();
		long count = 0;
		List<CwbOrderView> covList = new ArrayList<CwbOrderView>();
		if(isnow!=0){
			zavlist = zhiFuApplyDao.getapplycwbsForpage(page,cwbss,cwbtypeid,applytype,userid,applystate,applyresult);
			count = zhiFuApplyDao.getapplycwbsForCount(cwbss,cwbtypeid,applytype,userid,applystate,applyresult);
			pag = new Page(count,page,Page.ONE_PAGE_NUMBER);
			covList = this.cwborderService.getZhifuApplyCwbOrderView(zavlist,customerList,branchList,uslist);
		}
		
		model.addAttribute("page",page);
		model.addAttribute("page_obj",pag);
		model.addAttribute("uslist",uslist);
		model.addAttribute("customerList", customerList);
		model.addAttribute("branchList", branchList);
		model.addAttribute("zhifulist", covList);
		return "applyeditdeliverystate/paywayInfoModifyCheck";
	}
	
	
	//支付信息修改审核-导出
	@RequestMapping("/checkExportExcel")
	public void checkExportExcel(Model model,HttpServletRequest request,HttpServletResponse response,
			@RequestParam(value = "cwb", defaultValue = "", required = false) String cwbs,
			@RequestParam(value = "cwbtypeid", defaultValue = "0", required = false) int cwbtypeid,
			@RequestParam(value = "applytype", defaultValue = "0", required = false) int applytype,
			@RequestParam(value = "applypeople", defaultValue = "0", required = false) int userid,
			@RequestParam(value = "applystate", defaultValue = "0", required = false) int applystate,
			@RequestParam(value = "applyresult", defaultValue = "0", required = false) int applyresult
			) {

		List<Customer> customerList = customerDao.getAllCustomers();
		List<Branch> branchList = branchDAO.getQueryBranchByBranchsiteAndUserid(getSessionUser().getUserid(), String.valueOf(BranchEnum.ZhanDian.getValue()));
		List<User> uslist = userDAO.getAllUser();
		String cwbss = getCwbs(cwbs);
		List<ZhiFuApplyView> zavlist = zhiFuApplyDao.getapplycwbs(cwbss,cwbtypeid,applytype,userid,applystate,applyresult);
		List<CwbOrderView> covList = this.cwborderService.getZhifuApplyCwbOrderView(zavlist,customerList,branchList,uslist);
		
		String[] cloumnName1 = new String[12]; // 导出的列名
		String[] cloumnName2 = new String[12]; // 导出的英文列名
		this.exportService.setEditPIMCheckOrderFields(cloumnName1, cloumnName2);
		String sheetName = "支付信息修改审核"; // sheet的名称
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
		String fileName = "ZhiFU_Info_Check_" + df.format(new Date()) + ".xlsx"; // 文件名  
		ExcelUtilsHandler.exportExcelHandler(response, cloumnName1, cloumnName2, sheetName, fileName, covList);
	}

	//支付信息修改确认-导出
	@RequestMapping("/confirmExportExcel")
	public void confirmExportExcel(Model model,HttpServletRequest request,HttpServletResponse response,
			@RequestParam(value = "cwb", defaultValue = "", required = true) String cwbs,
			@RequestParam(value = "cwbtypeid", defaultValue = "0", required = true) int cwbtypeid,
			@RequestParam(value = "applytype", defaultValue = "0", required = false) int applytype,
			@RequestParam(value = "userid", defaultValue = "0", required = true) int userid,
			@RequestParam(value = "confirmstate", defaultValue = "0", required = true) int confirmstate,
			@RequestParam(value = "confirmresult", defaultValue = "0", required = true) int confirmresult
			) {

		List<Customer> customerList = customerDao.getAllCustomers();
		List<Branch> branchList = branchDAO.getQueryBranchByBranchsiteAndUserid(getSessionUser().getUserid(), String.valueOf(BranchEnum.ZhanDian.getValue()));
		List<User> userList = userDAO.getAllUser();
		String cwbss = getCwbs(cwbs);
		List<ZhiFuApplyView> zavlist = zhiFuApplyDao.getConfirmCwbs(cwbss,cwbtypeid,applytype,userid,confirmstate,confirmresult);
		List<CwbOrderView> covList = this.cwborderService.getZhifuConfirmCwbOrderView(zavlist,customerList,branchList,userList);
		
		String[] cloumnName1 = new String[13]; // 导出的列名
		String[] cloumnName2 = new String[13]; // 导出的英文列名
		this.exportService.setEditPIMConfirmFields(cloumnName1, cloumnName2);
		String sheetName = "支付信息修改确认"; // sheet的名称
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
		String fileName = "ZhiFU_Info_Confirm_" + df.format(new Date()) + ".xlsx"; // 文件名  
		ExcelUtilsHandler.exportExcelHandler(response, cloumnName1, cloumnName2, sheetName, fileName, covList);
	}

	private String getCwbs(String cwbs) {
		StringBuffer strs = new StringBuffer("");
		if(!cwbs.equals("")){
			for(String str:cwbs.split("\r\n")){
				strs.append("'").append(str).append("',");
			}
		}
		String cwbss = "";
		if(strs.length()>0){
			cwbss = strs.substring(0, strs.length()-1);
		}
		return cwbss;
	}	
	
	//获取需要审核的订单并进行处理成通过审核
	@RequestMapping("/editPaywayInfoModifyCheckpass")
	public  @ResponseBody String editPaywayInfoModifyCheckpass(HttpServletRequest request){
		String applyids = request.getParameter("applyids");
		long auditnum = 0;
		StringBuffer sb = new StringBuffer("");
		try{
			for(String applyid:applyids.split(",")){
				int applyidint = Integer.parseInt(applyid);
				ZhiFuApplyView zhifu = zhiFuApplyDao.getCheckstate(applyidint,2);
				if(zhifu!=null){
					sb.append(zhifu.getCwb()+",");
				}else{
					String auditname = getSessionUser().getRealname();
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					String dateStr = sdf.format(new Date());
					zhiFuApplyDao.updateStatePassByCwb(applyidint,auditname,dateStr);//更改状态为通过审核
					auditnum+=1;
				}
			}
		}catch(Exception e){
			return "{\"code\":1,\"msg\":\"审核通过出现异常!\"}";
		}
		if(sb.length()>0){
			String str = sb.substring(0, sb.length()-1);
			return "{\"code\":0,\"msg\":\"审核通过成功:"+auditnum+"单,已审核且不能再审核订单:"+str+"\"}";
		}
		return "{\"code\":0,\"msg\":\"审核通过成功:"+auditnum+"单\"}";
	}
	//获取需要审核的订单并进行处理成未通过审核
	@RequestMapping("/editPaywayInfoModifyChecknopass")
	public @ResponseBody String editPaywayInfoModifyChecknopass(HttpServletRequest request){
		String applyids = request.getParameter("applyids");
		long auditnum = 0;
		StringBuffer sb = new StringBuffer("");
		try{
			for(String applyid:applyids.split(",")){
				int applyidint = Integer.parseInt(applyid);
				ZhiFuApplyView zhifu = zhiFuApplyDao.getCheckstate(applyidint,2);
				if(zhifu!=null){
					sb.append(zhifu.getCwb()+",");
				}else{
					String auditname = getSessionUser().getRealname();
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					String dateStr = sdf.format(new Date());
					zhiFuApplyDao.updateStateNopassByCwb(applyidint,auditname,dateStr);//更改状态为未通过审核
					auditnum+=1;
				}
			}
		}catch(Exception e){
			e.printStackTrace();
			return "{\"code\":1,\"msg\":\"审核不通过出现异常!\"}";
		}
		if(sb.length()>0){
			String str = sb.substring(0, sb.length()-1);
			return "{\"code\":0,\"msg\":\"审核不通过成功:"+auditnum+"单,已审核且不能再审核订单:"+str+"\"}";
		}
		return "{\"code\":0,\"msg\":\"审核不通过成功:"+auditnum+"单\"}";
	}
	/**
	 * 支付信息修改确认
	 */
	@RequestMapping("/paywayInfoModifyConfirm/{page}")
	public String paywayInfoModifyConfirm(Model model,HttpServletRequest request,
		@PathVariable(value = "page") long page,	
		@RequestParam(value = "exportmould", defaultValue = "", required = true) String exportmould,
		@RequestParam(value = "cwb", defaultValue = "", required = true) String cwbs,
		@RequestParam(value = "cwbtypeid", defaultValue = "0", required = true) int cwbtypeid,
		@RequestParam(value = "applytype", defaultValue = "0", required = false) int applytype,
		@RequestParam(value = "userid", defaultValue = "0", required = true) int userid,
		@RequestParam(value = "confirmstate", defaultValue = "0", required = true) int confirmstate,
		@RequestParam(value = "confirmresult", defaultValue = "0", required = true) int confirmresult,
		@RequestParam(value = "isnow", defaultValue = "0", required = true) int isnow
		
			) {
		
		Page pag = new Page();
		List<User> uslist = userDAO.getAllUser();
		List<Customer> customerList = customerDao.getAllCustomers();
		List<Branch> branchList = branchDAO.getQueryBranchByBranchsiteAndUserid(getSessionUser().getUserid(), String.valueOf(BranchEnum.ZhanDian.getValue()));
		String cwbss = getCwbs(cwbs);
		
		List<ZhiFuApplyView> zavlist= new ArrayList<ZhiFuApplyView>();
		long count = 0;
		List<CwbOrderView> covList = new ArrayList<CwbOrderView>();
		if(isnow!=0){
			zavlist = zhiFuApplyDao.getConfirmCwbsForpage(page,cwbss,cwbtypeid,applytype,userid,confirmstate,confirmresult);
			count = zhiFuApplyDao.getConfirmCwbsForCount(cwbss,cwbtypeid,applytype,userid,confirmstate,confirmresult);
			pag = new Page(count,page,Page.ONE_PAGE_NUMBER);
			covList = this.cwborderService.getZhifuConfirmCwbOrderView(zavlist,customerList,branchList,uslist);
		}
		
		model.addAttribute("page",page);
		model.addAttribute("page_obj",pag);
		model.addAttribute("uslist",uslist);
		model.addAttribute("customerList", customerList);
		model.addAttribute("branchList", branchList);
		model.addAttribute("zhifulist", covList);
		return "applyeditdeliverystate/paywayInfoModifyConfirm";
	}
	
	//通过最终结算确认
	@RequestMapping("/editPaywayInfoModifyConfirmpass")
	public @ResponseBody String editPaywayInfoModifyConfirmpass(Model model,HttpServletRequest request) throws Exception{
		String applyids = request.getParameter("applyids");
		
		List<EdtiCwb_DeliveryStateDetail> ecList = new ArrayList<EdtiCwb_DeliveryStateDetail>();
		List<String> errorList = new ArrayList<String>();
		long cwbpricerevisenum=0;//金额修改单量
		long applywayrevisenum=0;//支付方式修改单量
		long cwbtyperevisenum=0;//支付方式修改单量
		StringBuffer sb = new StringBuffer("");
		StringBuffer cwbStr = new StringBuffer("");
		for(String applyid:applyids.split(",")){
			//zhiFuApplyDao.updateStateConfirmPassByCwb(Integer.parseInt(applyid));//更改状态为确认通过
			ZhiFuApplyView zfav = zhiFuApplyDao.getZhiFuViewByApplyid(applyid);
			FeeWayTypeRemark fwtr = JsonUtil.readValue(zfav.getFeewaytyperemark(),FeeWayTypeRemark.class);
			String cofirmname = this.getSessionUser().getRealname();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String confirmtime = sdf.format(new Date());
			try {
				if(zfav.getConfirmstate()==2){
					sb.append(zfav.getCwb()+",");
				}else{
					if(zfav.getApplyway()==ApplyEnum.dingdanjinE.getValue()){
						long lon = zhiFuApplyDao.getApplystateCount(zfav.getCwb(),1);
						if(lon>0){
							cwbStr.append(zfav.getCwb());//添加订单有待确认并且有待审核的订单单号(确认通过时。。。)
						}
						todoConfirmFeeResult(fwtr,ecList,errorList,model); //修改金额时的最终结算部分操作
						zhiFuApplyDao.updateStateConfirmPassByCwb(Integer.parseInt(applyid),cofirmname,confirmtime);//更改状态为确认通过
						cwbpricerevisenum+=1;
						//return "{\"errorCode\":0,\"msg\":\"true1\"}";
					}else if (zfav.getApplyway()==ApplyEnum.zhifufangshi.getValue()){
						todoConfirmWayResult(fwtr,ecList,errorList,model);
						zhiFuApplyDao.updateStateConfirmPassByCwb(Integer.parseInt(applyid),cofirmname,confirmtime);//更改状态为确认通过
						applywayrevisenum+=1;
						//return "{\"errorCode\":0,\"msg\":\"true2\"}";
					}else if (zfav.getApplyway()==ApplyEnum.dingdanleixing.getValue()){
						todoConfirmTypeResult(fwtr,ecList,errorList,model);
						zhiFuApplyDao.updateStateConfirmPassByCwb(Integer.parseInt(applyid),cofirmname,confirmtime);//更改状态为确认通过	
						cwbtyperevisenum+=1;
						//return "{\"errorCode\":0,\"msg\":\"true3\"}";
					}	
				}
			} catch (Exception e) {
				e.printStackTrace();
				return "{\"code\":1,\"msg\":\"支付信息修改异常!\"}";
			}
		}
		if(sb.length()>0){
			String str = sb.substring(0,sb.length()-1);
			return "{\"code\":0,\"msg\":\"订单金额修改:"+cwbpricerevisenum+"单,订单支付方式修改:"+applywayrevisenum+"单,订单类型修改:"+cwbtyperevisenum+"单,已审核订单:"+str+"\"}";
		}
		return "{\"code\":0,\"msg\":\"订单金额修改:"+cwbpricerevisenum+"单,订单支付方式修改:"+applywayrevisenum+"单,订单类型修改:"+cwbtyperevisenum+"单\"}";
	}
	//对订单类型进行修改的确认lx
	public void todoConfirmTypeResult(FeeWayTypeRemark fwtr,List<EdtiCwb_DeliveryStateDetail> ecList,List<String> errorList,Model model) throws Exception{
		String cwb = fwtr.getCwb();
		int cwbordertypeid = fwtr.getCwbordertypeid();
		int newcwbordertypeid = fwtr.getNewcwbordertypeid();
		long requestUser = fwtr.getRequestUser();
		EdtiCwb_DeliveryStateDetail ec_dsd = editCwbService.analysisAndSaveByXiuGaiDingDanLeiXing(cwb, cwbordertypeid, newcwbordertypeid, requestUser, getSessionUser().getUserid());
		ecList.add(ec_dsd);
		model.addAttribute("ecList", ecList);
		model.addAttribute("errorList", errorList);
	}
	//对支付方式进行修改的确认lx
	public void todoConfirmWayResult(FeeWayTypeRemark fwtr,List<EdtiCwb_DeliveryStateDetail> ecList,List<String> errorList,Model model) throws Exception{
		String cwb = fwtr.getCwb();
		int paywayid = fwtr.getPaywayid();
		int newpaywayid = fwtr.getNewpaywayid();
		long requestUser = fwtr.getRequestUser();
		EdtiCwb_DeliveryStateDetail ec_dsd = editCwbService.analysisAndSaveByXiuGaiZhiFuFangShi(cwb, paywayid, newpaywayid, requestUser, getSessionUser().getUserid());
		//added by jiangyu begin
		adjustmentRecordService.createAdjustmentRecordByPayType(cwb, paywayid, newpaywayid);
		//deleted by zhouguoting 2015/11/06 修改订单支付方式不再需要生成站内调整单（归班审核后的订单不再允许修改订单支付方式）
		//orgBillAdjustmentRecordService.createAdjustmentRecordByPayType(cwb,paywayid,newpaywayid);
		//修改支付方式,判断是否生成调整单
		//added by jiangyu end
		ecList.add(ec_dsd);
		model.addAttribute("ecList", ecList);
		model.addAttribute("errorList", errorList);
	}
	
	//对金额更改的最终确认方法lx
	public void todoConfirmFeeResult(FeeWayTypeRemark fwtr,List<EdtiCwb_DeliveryStateDetail> ecList,List<String> errorList,Model model) throws Exception{
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
			//deleted by zhuoguoting 2015/11/06 修改订单支付金额不再生成站内调整记录（归班审核后的订单不再允许修改订单金额）
			//this.orgBillAdjustmentRecordService.createOrgBillAdjustRecord(cwbOrder,user,receivablefee,paybackfee);
		}
		
		EdtiCwb_DeliveryStateDetail ec_dsd = editCwbService.analysisAndSaveByXiuGaiJinE(cwb, isDeliveryState, receivablefee, cash, pos, checkfee, otherfee, paybackfee, requestUser,
				getSessionUser().getUserid());
		ecList.add(ec_dsd);
		model.addAttribute("ecList", ecList);
		model.addAttribute("errorList", errorList);
	}
	
	//未通过结算部分确认
	@RequestMapping("/editPaywayInfoModifyConfirmnopass")
	public @ResponseBody String editPaywayInfoModifyConfirmnopass(HttpServletRequest request){
		String applyids = request.getParameter("applyids");
		long cwbpricerevisenum=0;//金额修改单量
		long applywayrevisenum=0;//支付方式修改单量
		long cwbtyperevisenum=0;//支付方式修改单量
		StringBuffer sb = new StringBuffer("");
		try{
			for(String applyid:applyids.split(",")){
				ZhiFuApplyView zhifu = zhiFuApplyDao.getConfirmstate(Integer.parseInt(applyid),2);
				if(zhifu!=null){
					sb.append(zhifu.getCwb()+",");
				}else{
					String cofirmname = this.getSessionUser().getRealname();
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					String confirmtime = sdf.format(new Date());
					zhiFuApplyDao.updateStateConfirmNopassByCwb(Integer.parseInt(applyid),cofirmname,confirmtime);//更改状态为确认不通过
					ZhiFuApplyView zfav = zhiFuApplyDao.getZhiFuViewByApplyid(applyid);
					if(zfav.getApplyway()==ApplyEnum.dingdanjinE.getValue()){
						cwbpricerevisenum+=1;
					}else if (zfav.getApplyway()==ApplyEnum.zhifufangshi.getValue()){
						applywayrevisenum+=1;
					}else if (zfav.getApplyway()==ApplyEnum.dingdanleixing.getValue()){
						cwbtyperevisenum+=1;
					}
				}
			}
		}catch(Exception e){
			e.printStackTrace();
			return "{\"code\":1,\"msg\":\"支付信息修改确认出现异常!\"}";
		}
		if(sb.length()>0){
			String str = sb.substring(0,sb.length()-1);
			return "{\"code\":0,\"msg\":\"订单金额修改不通过:"+cwbpricerevisenum+"单,订单支付方式修改不通过:"+applywayrevisenum+"单,订单类型修改不通过:"+cwbtyperevisenum+"单,已审核订单:"+str+"\"}";
		}
		return "{\"code\":0,\"msg\":\"订单金额修改不通过:"+cwbpricerevisenum+"单,订单支付方式修改不通过:"+applywayrevisenum+"单,订单类型修改不通过:"+cwbtyperevisenum+"单\"}";
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
				CwbOrder corder = cwbDAO.getCwborder(cwbStr);
				Customer customer = customerDao.getCustomerById(corder.getCustomerid());
				DeliveryState deliverystate = deliveryStateDAO.getActiveDeliveryStateByCwb(cwbStr);
				if (corder!=null&&deliverystate!=null) {
					if (deliverystate.getDeliverybranchid()!=this.getSessionUser().getBranchid()) {
						errorCwbs.append(cwbStr + ":非本站点反馈订单，不能操作此订单!");
						continue;
					}
				}
				if(corder == null){
					errorCwbs.append(cwbStr + ":无此单号!");
					continue;
				//deliverystate.getGcaid() == 0	(审核状态字段)！
				}else if (deliverystate == null || deliverystate.getDeliverystate() == 0 ||deliverystate.getGcaid() == 0 ) {
					errorCwbs.append(cwbStr + ":未反馈的订单不能申请修改反馈状态！");
					continue;
				}else if(deliverystate.getDeliverystate()==DeliveryStateEnum.JuShou.getValue()&&customer.getNeedchecked()==1){
					errorCwbs.append(cwbStr + ":拒收的订单已开启退货出站审核，不能申请修改反馈状态！");
					continue;
				} else if (deliverystate != null && deliverystate.getPayupid() == 0) {//&& deliverystate.getIssendcustomer() == 0
					cwbs = cwbs.append(quot).append(cwbStr).append(quotAndComma);
					CwbOrder co = cwbDAO.getCwbByCwbLock(cwbStr);
					long aedsLong = applyEditDeliverystateDAO.getApplyEditCount(cwbStr, 1);
					DeliveryState ds = deliveryStateDAO.getDeliveryByCwb(cwbStr);
					ApplyEditDeliverystate aeds = new ApplyEditDeliverystate();
					if (co != null && aedsLong == 0) {
						aeds.setCwb(cwbStr);
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
						aeds.setShenhestate(1);//待审核状态
						aeds.setDeliverpodtime(ds.getDeliverytime());//反馈时间
						applyEditDeliverystateDAO.creApplyEditDeliverystate(aeds);
					}
				} /*else {
					if (deliverystate.getPayupid() > 0) {
						errorCwbs.append(cwbStr + ":已上交款不能申请修改反馈状态！");
					} else {
						errorCwbs.append(cwbStr + ":状态已推送给电商不能申请修改反馈状态！");
					}
				}*/
			}
			String cwbs1 = cwbs.length() > 0 ? cwbs.substring(0, cwbs.length() - 1) : "'--'";
			count = cwbDAO.getCwbOrderCwbsCount(cwbs1);
			List<CwbOrder> coList =cwbDAO.getCwbByCwbsPage(page, cwbs1);
			List<ApplyEditDeliverystate> aedsList = applyEditDeliverystateDAO.getApplyEditDeliverystateByCwbsPage(page, cwbs1, ApplyEditDeliverystateIshandleEnum.WeiChuLi.getValue());
			/*StringBuffer sb = new StringBuffer();
			for(ApplyEditDeliverystate aeds:aedsList){
				sb.append("'").append(aeds.getCwb()).append("',");
			}
			String cwbss = "";
			if(sb.length()>0){
				cwbss = sb.substring(0,sb.length()-1);
				
			}
			coList = cwbDAO.getCwbByCwbsPage(page, cwbss);*/
			List<Reason> reasonList = reasonDAO.getAllReason();
			List<User> userList = userDAO.getAllUser();
			List<Branch> branchList = branchDAO.getAllEffectBranches();
			List<CwbOrderView> covList = this.cwborderService.getCwborderviewList(coList,aedsList,userList,branchList);
			model.addAttribute("cwbList",coList);
			model.addAttribute("applyEditDeliverystateList",aedsList);
			model.addAttribute("branchList", branchList);
			model.addAttribute("userList", userList);
			model.addAttribute("reasonList",reasonList);
			model.addAttribute("errorCwbs", errorCwbs.toString());
			model.addAttribute("covList",covList);
		}
		model.addAttribute("page_obj", new Page(count, page, Page.ONE_PAGE_NUMBER));

		return "applyeditdeliverystate/createApplyEditDeliverystate";
	}

	
	/*@RequestMapping("/toCreateApplyEditDeliverystateAgin")
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
	}*/
	
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
	public @ResponseBody String submitCreateApplyEditDeliverystate(@PathVariable("id") long id, Model model,
			@RequestParam(value = "editnowdeliverystate", defaultValue = "0", required = true) long editnowdeliverystate,
			@RequestParam(value = "reasonid", defaultValue = "0", required = true) int reasonid,
			@RequestParam(value = "editreason", defaultValue = "", required = true) String editreason) {
		try {
			// 判断是否符合申请条件：1.未反馈给电商 2.未交款
			ApplyEditDeliverystate applyEditDeliverystate = applyEditDeliverystateDAO.getApplyEditDeliverystateById(id);
			DeliveryState deliverystate = deliveryStateDAO.getActiveDeliveryStateByCwb(applyEditDeliverystate.getCwb());
			if (deliverystate != null ) {
				applyEditDeliverystateDAO.saveApplyEditDeliverystateById(id,editnowdeliverystate,reasonid, editreason);
				return "{\"errorCode\":0,\"error\":\"提交成功\"}";
			} else {
				/*if (deliverystate == null || deliverystate.getPayupid() > 0) {
					return "{\"errorCode\":1,\"error\":\"提交失败,此单已经上交款，不能再申请修改状态\"}";
				} else if (deliverystate == null || deliverystate.getIssendcustomer() > 0) {
					return "{\"errorCode\":1,\"error\":\"提交失败,此单状态已推送给电商，不能再申请修改状态\"}";
				}*/
				return "{\"errorCode\":1,\"error\":\"提交失败，没有做反馈的订单不能申请修改\"}";
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
			@RequestParam(value = "enddate", required = false, defaultValue = "") String enddate, @RequestParam(value = "ishandle", defaultValue = "-1", required = false) long ishandle,
			@RequestParam(value = "isnow", defaultValue = "0", required = false) int isnow
			) {
			List<Reason> reasonList = reasonDAO.getAllReason();
			Map<String, String> reasonMap = new HashMap<String, String>();
			for(Reason reason : reasonList){
				reasonMap.put(reason.getReasonid()+"", reason.getReasoncontent());
			}
			List<ApplyEditDeliverystate> applyEditDeliverystateList = new ArrayList<ApplyEditDeliverystate>();
			long count = 0;
			if(isnow>0){
			//List<ApplyEditDeliverystate> applyEditDeliverystateLists = applyEditDeliverystateDAO.getApplyEditDeliverystateByWherePage(page, begindate, enddate, getSessionUser().getBranchid(), ishandle, "");
			long branchid = this.getSessionUser().getBranchid();
			List<ApplyEditDeliverystate> applyEditDeliverystateLists = applyEditDeliverystateDAO.getApplyEditDeliverys(page,begindate,enddate,ishandle,branchid);
			applyEditDeliverystateList = this.getapplyeditdeliverysate(applyEditDeliverystateLists, reasonMap);
			count = applyEditDeliverystateDAO.getApplyEditDeliveryCount(begindate, enddate,ishandle,branchid);
			}
			model.addAttribute("page",page);
			model.addAttribute("applyEditDeliverystateList", applyEditDeliverystateList);
			model.addAttribute("branchList", branchDAO.getAllEffectBranches());
			model.addAttribute("userList", userDAO.getAllUser());
			model.addAttribute("page_obj", new Page(count, page,
					Page.ONE_PAGE_NUMBER));

		return "applyeditdeliverystate/applyEditDeliverystatelist";
	}
	
	//反馈状态修改申请导出
	@RequestMapping("/createApplyeditExportExcel")
	public void createApplyeditExportExcel(HttpServletRequest request,HttpServletResponse response,
		@RequestParam(value = "begindate", required = false, defaultValue = "") String begindate,
		@RequestParam(value = "enddate", required = false, defaultValue = "") String enddate, 
		@RequestParam(value = "ishandle", defaultValue = "-1", required = false) long ishandle
	){
		List<Reason> reasonList = reasonDAO.getAllReason();
		Map<String, String> reasonMap = new HashMap<String, String>();
		for(Reason reason : reasonList){
			reasonMap.put(reason.getReasonid()+"", reason.getReasoncontent());
		}
		List<Branch> branchlist = branchDAO.getAllEffectBranches();
		List<User> userList = userDAO.getAllUser();
		List<ApplyEditDeliverystate> applyEditDeliverystateList = new ArrayList<ApplyEditDeliverystate>();
		List<ApplyEditDeliverystate> applyEditDeliverystateLists = applyEditDeliverystateDAO.getApplyEditDeliverys(begindate,enddate,ishandle,this.getSessionUser().getBranchid());
		List<String> strList = new ArrayList<String>();
		if(applyEditDeliverystateLists!=null&&!applyEditDeliverystateLists.isEmpty()){
			for(ApplyEditDeliverystate aeds : applyEditDeliverystateLists){
				CwbOrder co = cwbDAO.getCwbByCwb(aeds.getCwb());
				strList.add(co.getCwb());
			}
		}
		applyEditDeliverystateList = cwborderService.getapplyeditdeliverysates(applyEditDeliverystateLists, reasonMap,branchlist,userList,strList);
		
		String[] cloumnName1 = new String[11]; // 导出的列名
		String[] cloumnName2 = new String[11]; // 导出的英文列名
		this.exportService.SetResetFeedBackFields(cloumnName1, cloumnName2);
		String sheetName = "反馈状态修改申请信息"; // sheet的名称
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
		String fileName = "fankuizhuangtai_order_" + sdf.format(new Date()) + ".xlsx"; // 文件名
		ExcelUtilsHandler.exportExcelHandler(response, cloumnName1, cloumnName2, sheetName, fileName, applyEditDeliverystateList);
	}
	
	public List<ApplyEditDeliverystate> getapplyeditdeliverysate(List<ApplyEditDeliverystate> applyEditDeliverystateLists,Map<String, String> reasonMap){
		if(applyEditDeliverystateLists!=null&&applyEditDeliverystateLists.size()>0&&reasonMap.size()>0){
			for(ApplyEditDeliverystate aeds : applyEditDeliverystateLists){
				aeds.setReasoncontent(reasonMap.get(aeds.getReasonid()+"")==null?"":reasonMap.get(aeds.getReasonid()+"").toString());
			}
		}
		return applyEditDeliverystateLists;
	}
	
	//客服部分lx
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
			@RequestParam(value = "applybranchid", defaultValue = "0", required = false) long applybranchid, @RequestParam(value = "ishandle", defaultValue = "-1", required = false) long ishandle) {

		model.addAttribute("applyEditDeliverystateList", applyEditDeliverystateDAO.getApplyEditDeliverystateByWherePage(page, begindate, enddate, applybranchid, ishandle, cwb));
		model.addAttribute("branchList", branchDAO.getAllEffectBranches());
		model.addAttribute("userList", userDAO.getAllUser());
		model.addAttribute("page_obj", new Page(applyEditDeliverystateDAO.getApplyEditDeliverystateByWhereCount(begindate, enddate, applybranchid, ishandle, cwb), page, Page.ONE_PAGE_NUMBER));

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
		ApplyEditDeliverystate applyEditDeliverystate = applyEditDeliverystateDAO.getApplyEditDeliverystateById(id);
		DeliveryState ds = deliveryStateDAO.getActiveDeliveryStateByCwb(applyEditDeliverystate.getCwb());
		List<Reason> backreasonlist = reasonDAO.getAllReasonByReasonType(ReasonTypeEnum.ReturnGoods.getValue());
		List<Reason> leavedreasonlist = reasonDAO.getAllReasonByReasonType(ReasonTypeEnum.BeHelpUp.getValue());
		List<Reason> podremarkreasonlist = reasonDAO.getAllReasonByReasonType(ReasonTypeEnum.GiveResult.getValue());

		CwbOrder co = cwbDAO.getCwbByCwb(applyEditDeliverystate.getCwb());

		List<Customer> customerList = customerDAO.getAllCustomers();
		List<User> userList = userDAO.getAllUser();

		model.addAttribute("cwborder", co);
		model.addAttribute("deliverystate", getDeliveryStateView(ds, customerList, userList, null));

		if (co.getBackreason() != null && co.getBackreason().length() > 0)
			model.addAttribute("backreasonid", reasonDAO.getReasonByReasonid(co.getBackreasonid()) == null ? 0 : reasonDAO.getReasonByReasonid(co.getBackreasonid()).getReasonid());
		if (co.getLeavedreason() != null && co.getLeavedreason().length() > 0)
			model.addAttribute("leavedreasonid", reasonDAO.getReasonByReasonid(co.getLeavedreasonid()) == null ? 0 : reasonDAO.getReasonByReasonid(co.getLeavedreasonid()).getReasonid());

		model.addAttribute("backreasonlist", backreasonlist);
		model.addAttribute("leavedreasonlist", leavedreasonlist);
		model.addAttribute("podremarkreasonlist", podremarkreasonlist);
		model.addAttribute("applyEditDeliverystate", applyEditDeliverystate);
		model.addAttribute("userList", userDAO.getAllUser());

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
	public @ResponseBody String agreeEditDeliveryState(Model model, @PathVariable("id") long id, @PathVariable("deliveryid") long deliveryid,
			@RequestParam(value = "podresultid", required = false, defaultValue = "0") long podresultid, @RequestParam(value = "backreasonid", required = false, defaultValue = "0") long backreasonid,
			@RequestParam(value = "leavedreasonid", required = false, defaultValue = "0") long leavedreasonid,
			@RequestParam(value = "podremarkid", required = false, defaultValue = "0") long podremarkid, @RequestParam("returnedfee") BigDecimal returnedfee,
			@RequestParam("receivedfeecash") BigDecimal receivedfeecash, @RequestParam("receivedfeepos") BigDecimal receivedfeepos, @RequestParam("posremark") String posremark,
			@RequestParam("receivedfeecheque") BigDecimal receivedfeecheque, @RequestParam("receivedfeeother") BigDecimal receivedfeeother,
			@RequestParam(value = "checkremark", required = false, defaultValue = "") String checkremark,
			@RequestParam(value = "deliverstateremark", required = false, defaultValue = "") String deliverstateremark) {

		
		ApplyEditDeliverystate applyEditDeliverystate = applyEditDeliverystateDAO.getApplyEditDeliverystateById(id);
		logger.info("web-agreeEditDeliveryState-进入单票反馈cwb={}",applyEditDeliverystate.getCwb());
		try {
			// 判断是否符合申请条件：1.未反馈给电商 2.未交款
			DeliveryState deliverystate = deliveryStateDAO.getActiveDeliveryStateByCwb(applyEditDeliverystate.getCwb());
			if (deliverystate != null && deliverystate.getPayupid() == 0 && deliverystate.getIssendcustomer() == 0) {
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
				parameters.put("sessionuserid", getSessionUser().getUserid());
				parameters.put("sign_typeid", SignTypeEnum.BenRenQianShou.getValue());
				parameters.put("sign_man", "");
				parameters.put("sign_time", DateTimeUtil.getNowTime());
				cwborderService.deliverStatePod(getSessionUser(), applyEditDeliverystate.getCwb(), applyEditDeliverystate.getCwb(), parameters);
				CwbOrder cwbOrder = cwbDAO.getCwbByCwb(applyEditDeliverystate.getCwb());
				DeliveryState deliveryState = deliveryStateDAO.getActiveDeliveryStateByCwb(applyEditDeliverystate.getCwb());

				CwbOrderWithDeliveryState cwbOrderWithDeliveryState = new CwbOrderWithDeliveryState();
				cwbOrderWithDeliveryState.setCwbOrder(cwbOrder);
				cwbOrderWithDeliveryState.setDeliveryState(deliveryState);

				try {
					applyEditDeliverystateDAO.agreeSaveApplyEditDeliverystateById(id, receivedfeecash.add(receivedfeecheque).add(receivedfeeother), receivedfeepos, getSessionUser().getUserid(),
							DateTimeUtil.getNowTime(), new ObjectMapper().writeValueAsString(cwbOrderWithDeliveryState).toString());
				} catch (Exception e) {
					logger.error("error while saveing applyEditDeliverystate", e);
				}
			} else {
				if (deliverystate != null && deliverystate.getPayupid() > 0) {
					return "{\"errorCode\":1,\"error\":\"订单已经上交款，不能修改状态\"}";
				} else {
					return "{\"errorCode\":1,\"error\":\"订单状态已经推送给电商，不能修改状态\"}";
				}
			}

		} catch (CwbException ce) {
			CwbOrder cwbOrder = cwbDAO.getCwbByCwb(applyEditDeliverystate.getCwb());
			exceptionCwbDAO.createExceptionCwbScan(applyEditDeliverystate.getCwb(), ce.getFlowordertye(), ce.getMessage(), getSessionUser().getBranchid(), getSessionUser().getUserid(),
					cwbOrder == null ? 0 : cwbOrder.getCustomerid(), 0, 0, 0, "",applyEditDeliverystate.getCwb());

			return "{\"errorCode\":1,\"error\":\"" + ce.getMessage() + "\"}";
		}
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
			cwbOrder = cwbDAO.getCwbByCwb(ds.getCwb());
		} else {
			for (CwbOrder c : clist) {
				if (c.getCwb().equals(ds.getCwb())) {
					cwbOrder = c;
					break;
				}
			}
		}
		if (cwbOrder == null) {
			logger.warn("cwborder {} not exist" + ds.getCwb());
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
	@RequestMapping("/finddeliveryreason")
	public @ResponseBody List<Reason> finddeliveryreason(@RequestParam(value="deliverytype",defaultValue="0",required=false)long deliverytype){
		List<Reason> reasons=new ArrayList<Reason>();
		if ((deliverytype==DeliveryStateEnum.PeiSongChengGong.getValue())||(deliverytype==DeliveryStateEnum.ShangMenHuanChengGong.getValue())||deliverytype==DeliveryStateEnum.HuoWuDiuShi.getValue()) {
			reasons=reasonDAO.getAllReasonByReasonType(ReasonTypeEnum.GiveResult.getValue());
		}
		if (deliverytype==DeliveryStateEnum.ShangMenTuiChengGong.getValue()||deliverytype==DeliveryStateEnum.JuShou.getValue()||deliverytype==DeliveryStateEnum.BuFenTuiHuo.getValue()||deliverytype==DeliveryStateEnum.ShangMenJuTui.getValue()) {
			reasons=reasonDAO.getAllReasonByReasonType(ReasonTypeEnum.ReturnGoods.getValue());
		}
		if (deliverytype==DeliveryStateEnum.FenZhanZhiLiu.getValue()||deliverytype==DeliveryStateEnum.ZhiLiuZiDongLingHuo.getValue()) {
			reasons=reasonDAO.getAllReasonByReasonType(ReasonTypeEnum.BeHelpUp.getValue());
		}
		if (deliverytype==DeliveryStateEnum.DaiZhongZhuan.getValue()) {
			reasons=reasonDAO.getAllReasonByReasonType(ReasonTypeEnum.ChangeTrains.getValue());
		}
		return reasons==null?new ArrayList<Reason>():reasons;
	}
}
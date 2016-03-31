package cn.explink.controller;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ColumnMapRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.explink.dao.BranchDAO;
import cn.explink.dao.CommonDAO;
import cn.explink.dao.ComplaintDAO;
import cn.explink.dao.CustomWareHouseDAO;
import cn.explink.dao.CustomerDAO;
import cn.explink.dao.CwbDAO;
import cn.explink.dao.DeliveryStateDAO;
import cn.explink.dao.ExportmouldDAO;
import cn.explink.dao.GotoClassAuditingDAO;
import cn.explink.dao.OperationDetailDAO;
import cn.explink.dao.OperationSetTimeDAO;
import cn.explink.dao.OperationTimeDAO;
import cn.explink.dao.OrderFlowDAO;
import cn.explink.dao.ReasonDao;
import cn.explink.dao.RemarkDAO;
import cn.explink.dao.SystemInstallDAO;
import cn.explink.dao.TuihuoRecordDAO;
import cn.explink.dao.UserDAO;
import cn.explink.domain.Branch;
import cn.explink.domain.Common;
import cn.explink.domain.Complaint;
import cn.explink.domain.CustomWareHouse;
import cn.explink.domain.Customer;
import cn.explink.domain.CwbOrder;
import cn.explink.domain.DeliveryState;
import cn.explink.domain.OperationSetTime;
import cn.explink.domain.Reason;
import cn.explink.domain.Remark;
import cn.explink.domain.SetExportField;
import cn.explink.domain.TuihuoRecord;
import cn.explink.domain.User;
import cn.explink.enumutil.BranchEnum;
import cn.explink.enumutil.DeliveryStateEnum;
import cn.explink.enumutil.FlowOrderTypeEnum;
import cn.explink.enumutil.OperationTimeEnum;
import cn.explink.exception.CwbException;
import cn.explink.service.DataStatisticsService;
import cn.explink.service.ExplinkUserDetail;
import cn.explink.service.ExportService;
import cn.explink.util.ExcelUtils;
import cn.explink.util.Page;
import cn.explink.util.StreamingStatementCreator;

@Controller
@RequestMapping("/ExceptionMonitor")
public class ExceptionMonitor {
	@Autowired
	ReasonDao reasonDao;
	@Autowired
	BranchDAO branchDAO;

	@Autowired
	OperationTimeDAO operationTimeDAO;

	@Autowired
	SystemInstallDAO systemInstallDAO;

	@Autowired
	CwbDAO cwbDAO;

	@Autowired
	CustomerDAO customerDAO;

	@Autowired
	CustomWareHouseDAO customWareHouseDAO;

	@Autowired
	SecurityContextHolderStrategy securityContextHolderStrategy;

	@Autowired
	UserDAO userDAO;

	@Autowired
	ExportmouldDAO exportmouldDAO;

	@Autowired
	RemarkDAO remarkDAO;
	@Autowired
	CommonDAO commonDAO;
	@Autowired
	JdbcTemplate jdbcTemplate;
	@Autowired
	ExportService exportService;
	@Autowired
	DeliveryStateDAO deliveryStateDAO;
	@Autowired
	OrderFlowDAO orderFlowDAO;
	@Autowired
	ReasonDao reasonDAO;
	@Autowired
	TuihuoRecordDAO tuihuoRecordDAO;
	@Autowired
	GotoClassAuditingDAO gotoClassAuditingDAO;

	@Autowired
	DataStatisticsService dataStatisticsService;
	@Autowired
	OperationSetTimeDAO operationSetTimeDAO;
	@Autowired
	ComplaintDAO complaintDAO;
	@Autowired
	OperationDetailDAO operationDetailDAO;

	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private User getSessionUser() {
		ExplinkUserDetail userDetail = (ExplinkUserDetail) securityContextHolderStrategy.getContext().getAuthentication().getPrincipal();
		return userDetail.getUser();
	}

	/**
	 * 超期异常监控
	 * 
	 * @param model
	 * @param baleno
	 * @return
	 */
	@RequestMapping("/operationTimeOut")
	public String operationTimeOut(
			Model model,
			// @RequestParam(value="sitetype" ,required=false
			// ,defaultValue="0")long sitetype,
			@RequestParam(value = "modelid", defaultValue = "0", required = false) long modelid,// 超期异常名称
			@RequestParam(value = "modelname", defaultValue = "", required = false) String modelname, @RequestParam(value = "customerid", defaultValue = "0", required = false) long customerid,
			@RequestParam(value = "begindate", defaultValue = "", required = false) String begindate, @RequestParam(value = "enddate", defaultValue = "", required = false) String enddate,
			@RequestParam(value = "branchids", required = false, defaultValue = "") String[] branchids) {
		// ====超期异常名称下拉框===
		String branchdataids = "";
		String sitetypes = BranchEnum.KuFang.getValue() + "," + BranchEnum.ZhanDian.getValue() + "," + BranchEnum.TuiHuo.getValue() + "," + BranchEnum.ZhongZhuan.getValue();
		// 获得用户区域权限下的站点
		List<Branch> userbranch = branchDAO.getQueryBranchByBranchsiteAndUserid(getSessionUser().getUserid(), sitetypes);
		if (userbranch != null && !userbranch.isEmpty()) {
			String[] userbranchids = new String[userbranch.size()];
			for (int i = 0; i < userbranch.size(); i++) {
				userbranchids[i] = String.valueOf(userbranch.get(i).getBranchid());
				branchdataids += userbranch.get(i).getBranchid() + ",";
			}
			branchdataids = branchdataids.substring(0, branchdataids.lastIndexOf(","));
			model.addAttribute("modelList", operationSetTimeDAO.getOperationSetTimeList(0, 0, "", userbranchids, 1));
		}

		if (modelid > 0) {// 选了超期异常名称
			OperationSetTime operationSetTime = operationSetTimeDAO.getOperationSetTimeById(modelid);
			model.addAttribute("sitetype", operationSetTime.getSitetype());
			// ====机构名称下拉框====
			model.addAttribute("branchList", branchDAO.getQueryBranchByBranchidAndUserid(getSessionUser().getUserid(), operationSetTime.getSitetype()));

			// 查询 超期异常模板&&用户区域权限下机构 的站点
			if (branchids != null && branchids.length > 0) {
				branchdataids = "";
				for (String branchid : branchids) {
					branchdataids += branchid + ",";
				}
				branchdataids = branchdataids.substring(0, branchdataids.lastIndexOf(","));
			}
			List<Map<String, Object>> branchdata = operationDetailDAO.getDetailBranch(operationSetTime.getId(), branchdataids);
			model.addAttribute("branchdata", branchdata);

			JSONObject saveOutTimeJson = new JSONObject();
			saveOutTimeJson = JSONObject.fromObject(operationSetTime.getValue());
			long A1 = saveOutTimeJson.get("A1") == null ? 0 : saveOutTimeJson.getLong("A1");
			long A2 = saveOutTimeJson.get("A2") == null ? 0 : saveOutTimeJson.getLong("A2");
			long A3 = saveOutTimeJson.get("A3") == null ? 0 : saveOutTimeJson.getLong("A3");
			long A4 = saveOutTimeJson.get("A4") == null ? 0 : saveOutTimeJson.getLong("A4");
			long A5 = saveOutTimeJson.get("A5") == null ? 0 : saveOutTimeJson.getLong("A5");
			long A6 = saveOutTimeJson.get("A6") == null ? 0 : saveOutTimeJson.getLong("A6");
			long A7 = saveOutTimeJson.get("A7") == null ? 0 : saveOutTimeJson.getLong("A7");
			long A8 = saveOutTimeJson.get("A8") == null ? 0 : saveOutTimeJson.getLong("A8");
			long A9 = saveOutTimeJson.get("A9") == null ? 0 : saveOutTimeJson.getLong("A9");
			long A10 = saveOutTimeJson.get("A10") == null ? 0 : saveOutTimeJson.getLong("A10");
			long A11 = saveOutTimeJson.get("A11") == null ? 0 : saveOutTimeJson.getLong("A11");
			model.addAttribute("A1", A1);
			model.addAttribute("A2", A2);
			model.addAttribute("A3", A3);
			model.addAttribute("A4", A4);
			model.addAttribute("A5", A5);
			model.addAttribute("A6", A6);
			model.addAttribute("A7", A7);
			model.addAttribute("A8", A8);
			model.addAttribute("A9", A9);
			model.addAttribute("A10", A10);
			model.addAttribute("A11", A11);
			A1 = System.currentTimeMillis() - (A1 * 60 * 60 * 1000);
			A2 = System.currentTimeMillis() - (A2 * 60 * 60 * 1000);
			A3 = System.currentTimeMillis() - (A3 * 60 * 60 * 1000);
			A4 = System.currentTimeMillis() - (A4 * 60 * 60 * 1000);
			A5 = System.currentTimeMillis() - (A5 * 60 * 60 * 1000);
			A6 = System.currentTimeMillis() - (A6 * 60 * 60 * 1000);
			A7 = System.currentTimeMillis() - (A7 * 60 * 60 * 1000);
			A8 = System.currentTimeMillis() - (A8 * 60 * 60 * 1000);
			A9 = System.currentTimeMillis() - (A9 * 60 * 60 * 1000);
			A10 = System.currentTimeMillis() - (A10 * 60 * 60 * 1000);
			// 入库超时
			Map<Long, Long> mapforruku = operationTimeDAO.getOperationTimeByCredateAndFlowordertypeGroupBranch(A1, FlowOrderTypeEnum.RuKu.getValue(), customerid, begindate, enddate);
			model.addAttribute("ruku_time_out", mapforruku);
			// 出库超时
			Map<Long, Long> mapforchuku = operationTimeDAO.getOperationTimeByCredateAndFlowordertypesGroupNextbranch(A6, FlowOrderTypeEnum.ChuKuSaoMiao.getValue() + ","
					+ FlowOrderTypeEnum.KuDuiKuChuKuSaoMiao.getValue(), customerid, begindate, enddate);
			model.addAttribute("chuku_time_out", mapforchuku);
			// 到站超时
			Map<Long, Long> mapfordaozhan = operationTimeDAO
					.getOperationTimeByCredateAndFlowordertypeGroupBranch(A3, FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue(), customerid, begindate, enddate);
			model.addAttribute("daozhan_time_out", mapfordaozhan);
			// 滞留超时
			Map<Long, Long> mapforzhiliu = operationTimeDAO.getOperationTimeByCredateAndFlowordertypeAndDeliverystateGroupNextbranch(A9, FlowOrderTypeEnum.YiFanKui.getValue() + ","
					+ FlowOrderTypeEnum.YiShenHe.getValue(), DeliveryStateEnum.FenZhanZhiLiu.getValue(), customerid, begindate, enddate);
			model.addAttribute("zhiliu_time_out", mapforzhiliu);
			// 领货超时
			Map<Long, Long> mapforlinghuo = operationTimeDAO.getOperationTimeByCredateAndFlowordertypeGroupBranch(A4, FlowOrderTypeEnum.FenZhanLingHuo.getValue(), customerid, begindate, enddate);
			model.addAttribute("linghuo_time_out", mapforlinghuo);
			// 中转站入库超时
			Map<Long, Long> mapforzhongzhuanruku = operationTimeDAO.getZhongZhuanOperationTimeByCredateAndFlowordertypeGroupBranch(A2, FlowOrderTypeEnum.RuKu.getValue() + ","
					+ FlowOrderTypeEnum.ZhongZhuanZhanRuKu.getValue(), customerid, begindate, enddate);
			model.addAttribute("zhongzhuanzhan_ruku_time_out", mapforzhongzhuanruku);
			// 退货站入库超时
			Map<Long, Long> mapfortuihuoruku = operationTimeDAO.getOperationTimeByCredateAndFlowordertypeGroupBranch(A5, FlowOrderTypeEnum.TuiHuoZhanRuKu.getValue(), customerid, begindate, enddate);
			model.addAttribute("tuihuozhanruku_time_out", mapfortuihuoruku);
			// 中转站出库超时
			Map<Long, Long> mapforzhongzhuanchuku = operationTimeDAO.getOperationTimeByCredateAndFlowordertypeGroupNextbranch(A7, FlowOrderTypeEnum.ChuKuSaoMiao.getValue(), customerid, begindate,
					enddate);
			model.addAttribute("zhongzhuanzhan_chuku_time_out", mapforzhongzhuanchuku);
			// 退货出站超时
			Map<Long, Long> mapfortuihuochuzhan = operationTimeDAO.getOperationTimeByCredateAndFlowordertypeGroupNextbranch(A8, FlowOrderTypeEnum.TuiHuoChuZhan.getValue(), customerid, begindate,
					enddate);
			model.addAttribute("tuihuo_chuzhan_time_out", mapfortuihuochuzhan);
			// 超期未退货(拒收、上门退成功、上门换成功超时)
			Map<Long, Long> mapforjushou = operationTimeDAO.getOperationTimeByCredateAndFlowordertypeAndDeliverystateGroupNextbranch(A10, FlowOrderTypeEnum.YiFanKui.getValue() + ","
					+ FlowOrderTypeEnum.YiShenHe.getValue(), DeliveryStateEnum.JuShou.getValue(), customerid, begindate, enddate);
			model.addAttribute("jushou_time_out", mapforjushou);
		}
		model.addAttribute("customerlist", customerDAO.getAllCustomers());
		return "exceptionmonitor/operationTimeOut";
	}

	// @RequestMapping("/operationTimeOut_back")
	// public String operationTimeOut_back( Model model,
	// @RequestParam(value="modelid",defaultValue="0",required=false) long
	// modelid,
	// @RequestParam(value="modelname",defaultValue="",required=false) String
	// modelname,
	// @RequestParam(value="isUpdate",defaultValue="0",required=false) long
	// isUpdate,
	// @RequestParam(value="customerid",defaultValue="0",required=false) long
	// customerid,
	// @RequestParam(value="begindate",defaultValue="",required=false) String
	// begindate,
	// @RequestParam(value="enddate",defaultValue="",required=false) String
	// enddate,
	// @RequestParam(value="A1",defaultValue="0",required=false) long A1,
	// @RequestParam(value="A2",defaultValue="0",required=false) long A2,
	// @RequestParam(value="A3",defaultValue="0",required=false) long A3,
	// @RequestParam(value="A4",defaultValue="0",required=false) long A4,
	// @RequestParam(value="A5",defaultValue="0",required=false) long A5,
	// @RequestParam(value="A6",defaultValue="0",required=false) long A6,
	// @RequestParam(value="A7",defaultValue="0",required=false) long A7,
	// @RequestParam(value="A8",defaultValue="0",required=false) long A8,
	// @RequestParam(value="A9",defaultValue="0",required=false) long A9,
	// @RequestParam(value="A10",defaultValue="0",required=false) long A10){
	//
	// List<Branch> bAllList = branchDAO.getAllEffectBranches();
	// List<Branch> bList = new ArrayList<Branch>();
	// for(Branch b : bAllList){
	// if(b.getSitetype()==BranchEnum.KuFang.getValue()
	// ||b.getSitetype()==BranchEnum.ZhanDian.getValue()
	// ||b.getSitetype()==BranchEnum.TuiHuo.getValue()
	// ||b.getSitetype()==BranchEnum.ZhongZhuan.getValue()){
	// bList.add(b);
	// }
	// }
	//
	// model.addAttribute("branchList",bList);
	//
	// JSONObject saveOutTimeJson = new JSONObject();
	//
	// // SystemInstall cs = systemInstallDAO.getSystemInstallByName("outTime");
	// List<OperationSetTime> oList =
	// operationSetTimeDAO.getOperationSetTimeByUserid(getSessionUser().getUserid());
	// if(modelid > 0){
	// if(isUpdate == 1){
	// saveOutTimeJson.put("A1", A1);
	// saveOutTimeJson.put("A2", A2);
	// saveOutTimeJson.put("A3", A3);
	// saveOutTimeJson.put("A4", A4);
	// saveOutTimeJson.put("A5", A5);
	// saveOutTimeJson.put("A6", A6);
	// saveOutTimeJson.put("A7", A7);
	// saveOutTimeJson.put("A8", A8);
	// saveOutTimeJson.put("A9", A9);
	// saveOutTimeJson.put("A10",A10);
	// if(modelname.length()>0){
	// OperationSetTime option =
	// operationSetTimeDAO.getOperationSetTimeByIdAndName(
	// getSessionUser().getUserid(), modelname);
	// if(option != null ){
	// operationSetTimeDAO.updateOperationTime(option.getId(),
	// saveOutTimeJson.toString(), new
	// SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
	// modelid = option.getId();
	// }else{
	// modelid = operationSetTimeDAO.creOperationSetTime(modelname,
	// saveOutTimeJson.toString(), getSessionUser().getUserid(), new
	// SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()),0,1,"");
	// }
	// }else{
	// operationSetTimeDAO.updateOperationTime(modelid,
	// saveOutTimeJson.toString(), new
	// SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
	// }
	// }else{
	// OperationSetTime option =
	// operationSetTimeDAO.getOperationSetTimeById(modelid);
	// saveOutTimeJson = JSONObject.fromObject(option.getValue());
	// A1 = saveOutTimeJson.getLong("A1");
	// A2 = saveOutTimeJson.getLong("A2");
	// A3 = saveOutTimeJson.getLong("A3");
	// A4 = saveOutTimeJson.getLong("A4");
	// A5 = saveOutTimeJson.getLong("A5");
	// A6 = saveOutTimeJson.getLong("A6");
	// A7 = saveOutTimeJson.getLong("A7");
	// A8 = saveOutTimeJson.getLong("A8");
	// A9 = saveOutTimeJson.getLong("A9");
	// A10 = saveOutTimeJson.getLong("A10");
	// }
	// }else{
	// try{
	// if(oList==null || oList.size()==0){//如果系统没有设置默认超期时间 则默认为24小时
	// //A1 = System.currentTimeMillis()-(24*60*60*1000);
	// A1 = A2 = A3 = A4 = A5 = A6 = A7 = A8 = A9 = A10 = 24;
	// saveOutTimeJson.put("A1", A1);
	// saveOutTimeJson.put("A2", A2);
	// saveOutTimeJson.put("A3", A3);
	// saveOutTimeJson.put("A4", A4);
	// saveOutTimeJson.put("A5", A5);
	// saveOutTimeJson.put("A6", A6);
	// saveOutTimeJson.put("A7", A7);
	// saveOutTimeJson.put("A8", A8);
	// saveOutTimeJson.put("A9", A9);
	// saveOutTimeJson.put("A10",A10);
	// operationSetTimeDAO.creOperationSetTime("默认", saveOutTimeJson.toString(),
	// getSessionUser().getUserid(), new
	// SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()),0,1,"");
	// }else if(A1==0){//A1 等于0 代表没有传入任何参数
	// saveOutTimeJson = JSONObject.fromObject(oList.get(0).getValue());
	// A1 = saveOutTimeJson.getLong("A1");
	// A2 = saveOutTimeJson.getLong("A2");
	// A3 = saveOutTimeJson.getLong("A3");
	// A4 = saveOutTimeJson.getLong("A4");
	// A5 = saveOutTimeJson.getLong("A5");
	// A6 = saveOutTimeJson.getLong("A6");
	// A7 = saveOutTimeJson.getLong("A7");
	// A8 = saveOutTimeJson.getLong("A8");
	// A9 = saveOutTimeJson.getLong("A9");
	// A10 = saveOutTimeJson.getLong("A10");
	// }else{
	// saveOutTimeJson.put("A1", A1);
	// saveOutTimeJson.put("A2", A2);
	// saveOutTimeJson.put("A3", A3);
	// saveOutTimeJson.put("A4", A4);
	// saveOutTimeJson.put("A5", A5);
	// saveOutTimeJson.put("A6", A6);
	// saveOutTimeJson.put("A7", A7);
	// saveOutTimeJson.put("A8", A8);
	// saveOutTimeJson.put("A9", A9);
	// saveOutTimeJson.put("A10",A10);
	// }
	//
	// }catch (Exception e) {
	// A1 = A2 = A3 = A4 = A5 = A6 = A7 = A8 = A9 = A10 = 24;
	// //outTime = System.currentTimeMillis()-(24*60*60*1000);
	// }
	// }
	// model.addAttribute("A1",A1 );
	// model.addAttribute("A2",A2 );
	// model.addAttribute("A3",A3 );
	// model.addAttribute("A4",A4 );
	// model.addAttribute("A5",A5 );
	// model.addAttribute("A6",A6 );
	// model.addAttribute("A7",A7 );
	// model.addAttribute("A8",A8 );
	// model.addAttribute("A9",A9 );
	// model.addAttribute("A10",A10);
	//
	//
	// A1 = System.currentTimeMillis()-(A1 *60*60*1000);
	// A2 = System.currentTimeMillis()-(A2 *60*60*1000);
	// A3 = System.currentTimeMillis()-(A3 *60*60*1000);
	// A4 = System.currentTimeMillis()-(A4 *60*60*1000);
	// A5 = System.currentTimeMillis()-(A5 *60*60*1000);
	// A6 = System.currentTimeMillis()-(A6 *60*60*1000);
	// A7 = System.currentTimeMillis()-(A7 *60*60*1000);
	// A8 = System.currentTimeMillis()-(A8 *60*60*1000);
	// A9 = System.currentTimeMillis()-(A9 *60*60*1000);
	// A10 = System.currentTimeMillis()-(A10*60*60*1000);
	//
	// //入库超时
	// Map<Long,Long> mapforruku
	// =operationTimeDAO.getOperationTimeByCredateAndFlowordertypeGroupBranch(A1,FlowOrderTypeEnum.RuKu.getValue(),customerid,begindate,enddate);
	// model.addAttribute("ruku_time_out",mapforruku);
	// //中转站入库超时
	// Map<Long, Long> mapforzhongzhuanruku
	// =operationTimeDAO.getOperationTimeByCredateAndFlowordertypeGroupBranch(A2,FlowOrderTypeEnum.RuKu.getValue(),customerid,begindate,enddate);
	// model.addAttribute("zhongzhuanzhan_ruku_time_out",mapforzhongzhuanruku);
	// //到站超时
	// Map<Long,Long>
	// mapfordaozhan=operationTimeDAO.getOperationTimeByCredateAndFlowordertypeGroupBranch(A3,FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue(),customerid,begindate,enddate);
	// model.addAttribute("daozhan_time_out",mapfordaozhan);
	// //领货超时
	// Map<Long,Long>
	// mapforlinghuo=operationTimeDAO.getOperationTimeByCredateAndFlowordertypeGroupBranch(A4,FlowOrderTypeEnum.FenZhanLingHuo.getValue(),customerid,begindate,enddate);
	// model.addAttribute("linghuo_time_out",mapforlinghuo);
	// //退货站入库超时
	// Map<Long,Long>
	// mapfortuihuoruku=operationTimeDAO.getOperationTimeByCredateAndFlowordertypeGroupBranch(A5,FlowOrderTypeEnum.TuiHuoZhanRuKu.getValue(),customerid,begindate,enddate);
	// model.addAttribute("tuihuozhanruku_time_out",mapfortuihuoruku);
	// //出库超时
	// Map<Long,Long>
	// mapforchuku=operationTimeDAO.getOperationTimeByCredateAndFlowordertypesGroupNextbranch(A6,FlowOrderTypeEnum.ChuKuSaoMiao.getValue()+","+FlowOrderTypeEnum.KuDuiKuChuKuSaoMiao.getValue(),customerid,begindate,enddate);
	// model.addAttribute("chuku_time_out",mapforchuku);
	// //中转站出库超时
	// Map<Long,Long>
	// mapforzhongzhuanchuku=operationTimeDAO.getOperationTimeByCredateAndFlowordertypeGroupNextbranch(A7,FlowOrderTypeEnum.ChuKuSaoMiao.getValue(),customerid,begindate,enddate);
	// model.addAttribute("zhongzhuanzhan_chuku_time_out",mapforzhongzhuanchuku);
	// //退货出站超时
	// Map<Long,Long>
	// mapfortuihuochuzhan=operationTimeDAO.getOperationTimeByCredateAndFlowordertypeGroupNextbranch(A8,FlowOrderTypeEnum.TuiHuoChuZhan.getValue(),customerid,begindate,enddate);
	// model.addAttribute("tuihuo_chuzhan_time_out",mapfortuihuochuzhan);
	// //滞留超时
	//
	// Map<Long, Long>
	// mapforzhiliu=operationTimeDAO.getOperationTimeByCredateAndFlowordertypeAndDeliverystateGroupNextbranch(A9,
	// FlowOrderTypeEnum.YiFanKui.getValue()+","+FlowOrderTypeEnum.YiShenHe.getValue(),
	// DeliveryStateEnum.FenZhanZhiLiu.getValue(),customerid,begindate,enddate);
	//
	//
	// model.addAttribute("zhiliu_time_out",mapforzhiliu);
	// //超期未退货(拒收、上门退成功、上门换成功超时)
	//
	// Map<Long,Long>
	// mapforjushou=operationTimeDAO.getOperationTimeByCredateAndFlowordertypeAndDeliverystateGroupNextbranch(A10,
	// FlowOrderTypeEnum.YiFanKui.getValue()+","+FlowOrderTypeEnum.YiShenHe.getValue(),
	// DeliveryStateEnum.JuShou.getValue(),customerid,begindate,enddate);
	//
	// model.addAttribute("jushou_time_out",mapforjushou);
	//
	// model.addAttribute("modelList",
	// operationSetTimeDAO.getOperationSetTimeByUserid(getSessionUser().getUserid()));
	// model.addAttribute("customerlist", customerDAO.getAllCustomers());
	// model.addAttribute("customerid", customerid);
	// model.addAttribute("begindate", begindate);
	// model.addAttribute("enddate", enddate);
	// model.addAttribute("modelid", modelid);
	//
	// return "exceptionmonitor/operationTimeOut_back";
	// }

	// @RequestMapping("/del")
	// public String del( Model model,
	// @RequestParam(value="modelid",defaultValue="0",required=false) long
	// modelid,
	// @RequestParam(value="modelname",defaultValue="",required=false) String
	// modelname,
	// @RequestParam(value="isUpdate",defaultValue="0",required=false) long
	// isUpdate,
	// @RequestParam(value="customerid",defaultValue="0",required=false) long
	// customerid,
	// @RequestParam(value="begindate",defaultValue="",required=false) String
	// begindate,
	// @RequestParam(value="enddate",defaultValue="",required=false) String
	// enddate){
	// if(modelid >0 ){
	// OperationSetTime option =
	// operationSetTimeDAO.getOperationSetTimeById(modelid);
	// if(option == null || "默认".equals(option.getName()) ){
	// return operationTimeOut(model, 0, modelname, isUpdate, customerid,
	// begindate, enddate, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
	// }
	// operationSetTimeDAO.deleteOperationTimebyid(modelid);
	// }
	// return operationTimeOut(model, 0, modelname, isUpdate, customerid,
	// begindate, enddate, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
	// }

	@RequestMapping("/showTimeOut/{modelid}/{outhour}/{branchid}/{flowordertype}/{deliverystate}/{nextbranchid}/{customerid}/{begindate}/{enddate}/{page}")
	public String showTimeOut(Model model, @PathVariable("modelid") long modelid, @PathVariable("outhour") long outhour, @PathVariable("branchid") long branchid,
			@PathVariable("flowordertype") String flowordertype, @PathVariable("deliverystate") long deliverystate, @PathVariable("nextbranchid") long nextbranchid,
			@PathVariable("customerid") long customerid, @PathVariable("begindate") String begindate, @PathVariable("enddate") String enddate, @PathVariable("page") long page) {
		long outTime = System.currentTimeMillis() - (outhour * 60 * 60 * 1000);

		model.addAttribute("customerMap", customerDAO.getAllCustomersToMap());

		Map<Long, CustomWareHouse> customerWarehouseMap = new HashMap<Long, CustomWareHouse>();
		for (CustomWareHouse customWareHouse : customWareHouseDAO.getAllCustomWareHouse()) {
			customerWarehouseMap.put(customWareHouse.getWarehouseid(), customWareHouse);
		}
		model.addAttribute("customerWarehouseMap", customerWarehouseMap);
		Map<Long, Branch> branchMap = new HashMap<Long, Branch>();
		for (Branch branch : branchDAO.getAllBranches()) {
			branchMap.put(branch.getBranchid(), branch);
		}
		model.addAttribute("branchMap", branchMap);
		begindate = begindate.equals("-") ? "" : begindate;
		enddate = enddate.equals("-") ? "" : enddate;
		long count = cwbDAO.getCwbOrderForOperationtimeoutCount(outTime, flowordertype, branchid, deliverystate, nextbranchid, customerid, begindate, enddate);

		List<CwbOrder> cwbList = new ArrayList<CwbOrder>();
		cwbList.addAll(cwbDAO.getCwbOrderForOperationtimeout(page, outTime, flowordertype, branchid, deliverystate, nextbranchid, customerid, begindate, enddate));
		List<String> cwbForAll = cwbDAO.getCwbOrderForOperationtimeout(outTime, flowordertype, branchid, deliverystate, nextbranchid, customerid, begindate, enddate);

		String cwbs = "";
		if (cwbForAll != null && cwbForAll.size() > 0) {
			for (String s : cwbForAll) {
				cwbs += "'" + s + "',";
			}
			cwbs = cwbs.substring(0, cwbs.length() - 1);
		}

		model.addAttribute("cwbs", cwbs);
		model.addAttribute("cwbList", cwbList);
		model.addAttribute("page_obj", new Page(count, page, Page.ONE_PAGE_NUMBER));
		model.addAttribute("page", page);
		model.addAttribute("modelid", modelid);
		model.addAttribute("outhour", outhour);
		model.addAttribute("branchid", branchid);
		model.addAttribute("flowordertype", flowordertype);
		model.addAttribute("deliverystate", deliverystate);
		model.addAttribute("nextbranchid", nextbranchid);
		model.addAttribute("customerid", customerid);
		model.addAttribute("begindate", begindate);
		model.addAttribute("enddate", enddate);

		return "exceptionmonitor/show";
	}

	@RequestMapping("/showTimeOutByBranchid/{branchid}/{page}")
	public String showTimeOutByBranchid(
			Model model,
			// @RequestParam(value="A1",defaultValue="0",required=false) long
			// A1,
			// @RequestParam(value="A2",defaultValue="0",required=false) long
			// A2,
			// @RequestParam(value="A3",defaultValue="0",required=false) long
			// A3,
			// @RequestParam(value="A4",defaultValue="0",required=false) long
			// A4,
			// @RequestParam(value="A5",defaultValue="0",required=false) long
			// A5,
			// @RequestParam(value="A6",defaultValue="0",required=false) long
			// A6,
			// @RequestParam(value="A7",defaultValue="0",required=false) long
			// A7,
			// @RequestParam(value="A8",defaultValue="0",required=false) long
			// A8,
			// @RequestParam(value="A9",defaultValue="0",required=false) long
			// A9,
			// @RequestParam(value="A10",defaultValue="0",required=false) long
			// A10,
			@PathVariable("branchid") long branchid, @RequestParam(value = "modelid", defaultValue = "0", required = false) long modelid,
			@RequestParam(value = "customerid", defaultValue = "0", required = false) long customerid, @RequestParam(value = "begindate", defaultValue = "", required = false) String begindate,
			@RequestParam(value = "enddate", defaultValue = "", required = false) String enddate, @PathVariable("page") long page) {
		OperationSetTime operationSetTime = operationSetTimeDAO.getOperationSetTimeById(modelid);

		JSONObject saveOutTimeJson = new JSONObject();
		saveOutTimeJson = JSONObject.fromObject(operationSetTime.getValue());
		long A1 = saveOutTimeJson.get("A1") == null ? 0 : saveOutTimeJson.getLong("A1");
		long A2 = saveOutTimeJson.get("A2") == null ? 0 : saveOutTimeJson.getLong("A2");
		long A3 = saveOutTimeJson.get("A3") == null ? 0 : saveOutTimeJson.getLong("A3");
		long A4 = saveOutTimeJson.get("A4") == null ? 0 : saveOutTimeJson.getLong("A4");
		long A5 = saveOutTimeJson.get("A5") == null ? 0 : saveOutTimeJson.getLong("A5");
		long A6 = saveOutTimeJson.get("A6") == null ? 0 : saveOutTimeJson.getLong("A6");
		long A7 = saveOutTimeJson.get("A7") == null ? 0 : saveOutTimeJson.getLong("A7");
		long A8 = saveOutTimeJson.get("A8") == null ? 0 : saveOutTimeJson.getLong("A8");
		long A9 = saveOutTimeJson.get("A9") == null ? 0 : saveOutTimeJson.getLong("A9");
		long A10 = saveOutTimeJson.get("A10") == null ? 0 : saveOutTimeJson.getLong("A10");
		long A11 = saveOutTimeJson.get("A11") == null ? 0 : saveOutTimeJson.getLong("A11");
		A1 = System.currentTimeMillis() - (A1 * 60 * 60 * 1000);
		A2 = System.currentTimeMillis() - (A2 * 60 * 60 * 1000);
		A3 = System.currentTimeMillis() - (A3 * 60 * 60 * 1000);
		A4 = System.currentTimeMillis() - (A4 * 60 * 60 * 1000);
		A5 = System.currentTimeMillis() - (A5 * 60 * 60 * 1000);
		A6 = System.currentTimeMillis() - (A6 * 60 * 60 * 1000);
		A7 = System.currentTimeMillis() - (A7 * 60 * 60 * 1000);
		A8 = System.currentTimeMillis() - (A8 * 60 * 60 * 1000);
		A9 = System.currentTimeMillis() - (A9 * 60 * 60 * 1000);
		A10 = System.currentTimeMillis() - (A10 * 60 * 60 * 1000);
		A11 = System.currentTimeMillis() - (A11 * 60 * 60 * 1000);

		List<String> cwbForAll = new ArrayList<String>();
		Branch b = branchDAO.getBranchByBranchid(branchid);
		begindate = begindate.equals("-") ? "" : begindate;
		enddate = enddate.equals("-") ? "" : enddate;
		if (b != null) {
			if (b.getSitetype() == BranchEnum.KuFang.getValue()) {
				List<String> cwbForA1 = cwbDAO.getCwbOrderForOperationtimeout(A1, FlowOrderTypeEnum.RuKu.getValue() + "", branchid, 0, 0, customerid, begindate, enddate);
				List<String> cwbForA6 = cwbDAO.getCwbOrderForOperationtimeout(A6, FlowOrderTypeEnum.ChuKuSaoMiao.getValue() + "", 0, 0, branchid, customerid, begindate, enddate);
				cwbForAll.addAll(cwbForA1);
				cwbForAll.addAll(cwbForA6);
			}
			if (b.getSitetype() == BranchEnum.ZhanDian.getValue()) {

				List<String> cwbForA6 = cwbDAO.getCwbOrderForOperationtimeout(A6, FlowOrderTypeEnum.ChuKuSaoMiao.getValue() + "", 0, 0, branchid, customerid, begindate, enddate);
				List<String> cwbForA3 = cwbDAO.getCwbOrderForOperationtimeout(A3, FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue() + "", branchid, 0, 0, customerid, begindate, enddate);
				List<String> cwbForA4 = cwbDAO.getCwbOrderForOperationtimeout(A4, FlowOrderTypeEnum.FenZhanLingHuo.getValue() + "", branchid, 0, 0, customerid, begindate, enddate);
				List<String> cwbForA9 = cwbDAO.getCwbOrderForOperationtimeout(A9, FlowOrderTypeEnum.YiFanKui.getValue() + "," + FlowOrderTypeEnum.YiShenHe.getValue(), branchid,
						DeliveryStateEnum.FenZhanZhiLiu.getValue(), 0, customerid, begindate, enddate);
				List<String> cwbForA10 = cwbDAO.getCwbOrderForOperationtimeout(A10, FlowOrderTypeEnum.YiFanKui.getValue() + "," + FlowOrderTypeEnum.YiShenHe.getValue(), branchid,
						DeliveryStateEnum.JuShou.getValue(), 0, customerid, begindate, enddate);

				cwbForAll.addAll(cwbForA6);
				cwbForAll.addAll(cwbForA3);
				cwbForAll.addAll(cwbForA4);
				cwbForAll.addAll(cwbForA9);
				cwbForAll.addAll(cwbForA10);
			}
			if (b.getSitetype() == BranchEnum.ZhongZhuan.getValue()) {

				List<String> cwbForA7 = cwbDAO.getCwbOrderForOperationtimeout(A7, FlowOrderTypeEnum.ChuKuSaoMiao.getValue() + "", 0, 0, branchid, customerid, begindate, enddate);
				List<String> cwbForA2 = cwbDAO.getCwbOrderForOperationtimeout(A2, FlowOrderTypeEnum.RuKu.getValue() + "," + FlowOrderTypeEnum.ZhongZhuanZhanRuKu.getValue(), branchid, 0, 0,
						customerid, begindate, enddate);

				cwbForAll.addAll(cwbForA7);
				cwbForAll.addAll(cwbForA2);
			}
			if (b.getSitetype() == BranchEnum.TuiHuo.getValue()) {
				List<String> cwbForA8 = cwbDAO.getCwbOrderForOperationtimeout(A8, FlowOrderTypeEnum.TuiHuoChuZhan.getValue() + "", 0, 0, branchid, customerid, begindate, enddate);
				List<String> cwbForA5 = cwbDAO.getCwbOrderForOperationtimeout(A5, FlowOrderTypeEnum.TuiHuoZhanRuKu.getValue() + "", branchid, 0, 0, customerid, begindate, enddate);
				cwbForAll.addAll(cwbForA8);
				cwbForAll.addAll(cwbForA5);
			}

		}
		model.addAttribute("customerMap", customerDAO.getAllCustomersToMap());
		Map<Long, CustomWareHouse> customerWarehouseMap = new HashMap<Long, CustomWareHouse>();
		for (CustomWareHouse customWareHouse : customWareHouseDAO.getAllCustomWareHouse()) {
			customerWarehouseMap.put(customWareHouse.getWarehouseid(), customWareHouse);
		}
		model.addAttribute("customerWarehouseMap", customerWarehouseMap);
		Map<Long, Branch> branchMap = new HashMap<Long, Branch>();
		for (Branch branch : branchDAO.getAllBranches()) {
			branchMap.put(branch.getBranchid(), branch);
		}
		model.addAttribute("branchMap", branchMap);
		String cwbs = "";
		if (cwbForAll != null && cwbForAll.size() > 0) {
			for (String s : cwbForAll) {
				cwbs += "'" + s + "',";
			}
			cwbs = cwbs.substring(0, cwbs.length() - 1);
			model.addAttribute("cwbList", cwbDAO.getCwbByCwbsPage(page, cwbs));
		}
		model.addAttribute("branchid", branchid);
		model.addAttribute("cwbs", cwbs);
		model.addAttribute("page_obj", new Page(cwbForAll.size(), page, Page.ONE_PAGE_NUMBER));
		model.addAttribute("page", page);
		model.addAttribute("customerid", customerid);
		model.addAttribute("begindate", begindate);
		model.addAttribute("enddate", enddate);
		return "exceptionmonitor/showbybranchid";

	}

	@RequestMapping("/exportException")
	public void exportExcle(Model model, HttpServletResponse response, HttpServletRequest request, @RequestParam(value = "cwbs", required = false, defaultValue = "") final String cwbs) {
		String[] cloumnName1 = {}; // 导出的列名
		String[] cloumnName2 = {}; // 导出的英文列名
		String[] cloumnName3 = {}; // 导出的数据类型

		List<SetExportField> listSetExportField = exportmouldDAO.getSetExportFieldByStrs("0");
		cloumnName1 = new String[listSetExportField.size()];
		cloumnName2 = new String[listSetExportField.size()];
		cloumnName3 = new String[listSetExportField.size()];
		for (int k = 0, j = 0; j < listSetExportField.size(); j++, k++) {
			cloumnName1[k] = listSetExportField.get(j).getFieldname();
			cloumnName2[k] = listSetExportField.get(j).getFieldenglishname();
			cloumnName3[k] = listSetExportField.get(j).getExportdatatype();
		}
		final String[] cloumnName4 = cloumnName1;
		final String[] cloumnName5 = cloumnName2;
		final String[] cloumnName6 = cloumnName3;
		String sheetName = "订单信息"; // sheet的名称
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
		String fileName = "Order_" + df.format(new Date()) + ".xlsx"; // 文件名
		try {
			// 查询出数据

			final String sql = cwbDAO.getSQLExportKeFu(cwbs);

			ExcelUtils excelUtil = new ExcelUtils() { // 生成工具类实例，并实现填充数据的抽象方法
				@Override
				public void fillData(final Sheet sheet, final CellStyle style) {
					final List<User> uList = userDAO.getAllUser();
					final Map<Long, Customer> cMap = customerDAO.getAllCustomersToMap();
					final List<Branch> bList = branchDAO.getAllBranches();
					final List<Common> commonList = commonDAO.getAllCommons();
					final List<CustomWareHouse> cWList = customWareHouseDAO.getAllCustomWareHouse();
					List<Remark> remarkList = remarkDAO.getRemarkByCwbs(cwbs);
					final Map<String, Map<String, String>> remarkMap = exportService.getInwarhouseRemarks(remarkList);
					final List<Reason> reasonList = reasonDAO.getAllReason();
					jdbcTemplate.query(new StreamingStatementCreator(sql), new ResultSetExtractor<Object>() {
						private int count = 0;
						ColumnMapRowMapper columnMapRowMapper = new ColumnMapRowMapper();
						private List<Map<String, Object>> recordbatch = new ArrayList<Map<String, Object>>();

						public void processRow(ResultSet rs) throws SQLException {

							Map<String, Object> mapRow = columnMapRowMapper.mapRow(rs, count);
							recordbatch.add(mapRow);
							count++;
							if (count % 100 == 0) {
								writeBatch();
							}

						}

						private void writeSingle(Map<String, Object> mapRow, TuihuoRecord tuihuoRecord, DeliveryState ds, Map<String, String> allTime, int rownum, Map<String, String> cwbspayupidMap,
								Map<String, String> complaintMap) throws SQLException {
							Row row = sheet.createRow(rownum + 1);
							row.setHeightInPoints((float) 15);
							for (int i = 0; i < cloumnName4.length; i++) {
								Cell cell = row.createCell((short) i);
								cell.setCellStyle(style);
								// sheet.setColumnWidth(i, (short) (5000));
								// //设置列宽
								Object a = exportService.setObjectA(cloumnName5, mapRow, i, uList, cMap, bList, commonList, tuihuoRecord, ds, allTime, cWList, remarkMap, reasonList, cwbspayupidMap,
										complaintMap);
								if (cloumnName6[i].equals("double")) {
									cell.setCellValue(a == null ? BigDecimal.ZERO.doubleValue() : a.equals("") ? BigDecimal.ZERO.doubleValue() : Double.parseDouble(a.toString()));
								} else {
									cell.setCellValue(a == null ? "" : a.toString());
								}
							}
						}

						@Override
						public Object extractData(ResultSet rs) throws SQLException, DataAccessException {
							while (rs.next()) {
								this.processRow(rs);
							}
							writeBatch();
							return null;
						}

						public void writeBatch() throws SQLException {
							if (recordbatch.size() > 0) {
								List<String> cwbs = new ArrayList<String>();
								for (Map<String, Object> mapRow : recordbatch) {
									cwbs.add(mapRow.get("cwb").toString());
								}
								Map<String, DeliveryState> deliveryStates = getDeliveryListByCwbs(cwbs);
								Map<String, TuihuoRecord> tuihuorecoredMap = getTuihuoRecoredMap(cwbs);
								Map<String, String> cwbspayupMsp = getcwbspayupidMap(cwbs);
								Map<String, String> complaintMap = getComplaintMap(cwbs);
								Map<String, Map<String, String>> orderflowList = dataStatisticsService.getOrderFlowByCredateForDetailAndExportAllTime(cwbs, bList);
								int size = recordbatch.size();
								for (int i = 0; i < size; i++) {
									String cwb = recordbatch.get(i).get("cwb").toString();
									writeSingle(recordbatch.get(i), tuihuorecoredMap.get(cwb), deliveryStates.get(cwb), orderflowList.get(cwb), count - size + i, cwbspayupMsp, complaintMap);
								}
								recordbatch.clear();
							}
						}

						private Map<String, TuihuoRecord> getTuihuoRecoredMap(List<String> cwbs) {
							Map<String, TuihuoRecord> map = new HashMap<String, TuihuoRecord>();
							for (TuihuoRecord tuihuoRecord : tuihuoRecordDAO.getTuihuoRecordByCwbs(cwbs)) {
								map.put(tuihuoRecord.getCwb(), tuihuoRecord);
							}
							return map;
						}

						private Map<String, DeliveryState> getDeliveryListByCwbs(List<String> cwbs) {
							Map<String, DeliveryState> map = new HashMap<String, DeliveryState>();
							for (DeliveryState deliveryState : deliveryStateDAO.getActiveDeliveryStateByCwbs(cwbs)) {
								map.put(deliveryState.getCwb(), deliveryState);
							}
							return map;
						}

						private Map<String, String> getComplaintMap(List<String> cwbs) {
							Map<String, String> complaintMap = new HashMap<String, String>();
							for (Complaint complaint : complaintDAO.getActiveComplaintByCwbs(cwbs)) {
								complaintMap.put(complaint.getCwb(), complaint.getContent());
							}
							return complaintMap;
						}

						private Map<String, String> getcwbspayupidMap(List<String> cwbs) {
							Map<String, String> cwbspayupidMap = new HashMap<String, String>();
							/*
							 * for(DeliveryState deliveryState:deliveryStateDAO.
							 * getActiveDeliveryStateByCwbs(cwbs)){ String
							 * ispayup = "否"; GotoClassAuditing goclass =
							 * gotoClassAuditingDAO
							 * .getGotoClassAuditingByGcaid(deliveryState
							 * .getGcaid());
							 * 
							 * if(goclass!=null&&goclass.getPayupid()!=0){
							 * ispayup = "是"; }
							 * cwbspayupidMap.put(deliveryState.getCwb(),
							 * ispayup); }
							 */
							return cwbspayupidMap;
						}
					});
					/*
					 * jdbcTemplate.query(new StreamingStatementCreator(sql),
					 * new RowCallbackHandler(){ private int count=0;
					 * 
					 * @Override public void processRow(ResultSet rs) throws
					 * SQLException { Row row = sheet.createRow(count + 1);
					 * row.setHeightInPoints((float) 15);
					 * 
					 * DeliveryState ds = getDeliveryByCwb(rs.getString("cwb"));
					 * Map<String,String> allTime =
					 * getOrderFlowByCredateForDetailAndExportAllTime
					 * (rs.getString("cwb"));
					 * 
					 * for (int i = 0; i < cloumnName4.length; i++) { Cell cell
					 * = row.createCell((short) i); cell.setCellStyle(style);
					 * Object a = exportService.setObjectA(cloumnName5, rs, i ,
					 * uList
					 * ,cMap,bList,commonList,ds,allTime,cWList,remarkMap,reasonList
					 * ); if(cloumnName6[i].equals("double")){
					 * cell.setCellValue(a == null ?
					 * BigDecimal.ZERO.doubleValue() :
					 * a.equals("")?BigDecimal.ZERO
					 * .doubleValue():Double.parseDouble(a.toString())); }else{
					 * cell.setCellValue(a == null ? "" : a.toString()); } }
					 * count++;
					 * 
					 * }});
					 */
				}
			};
			excelUtil.excel(response, cloumnName4, sheetName, fileName);

		} catch (Exception e) {
			logger.error("", e);
		}
	}

	/**
	 * 查询订单的配送结果
	 * 
	 * @param cwb
	 * @return
	 */
	public DeliveryState getDeliveryByCwb(String cwb) {
		List<DeliveryState> delvieryList = deliveryStateDAO.getDeliveryStateByCwb(cwb);
		return delvieryList.size() > 0 ? delvieryList.get(delvieryList.size() - 1) : new DeliveryState();
	}

	/**
	 * 超期异常时效设置List
	 * 
	 * @return
	 */
	@RequestMapping("/system")
	public String system(Model model, @RequestParam(value = "sitetype", required = false, defaultValue = "0") long sitetype,
			@RequestParam(value = "modelname", defaultValue = "", required = false) String modelname, @RequestParam(value = "branchids", required = false, defaultValue = "") String[] branchids) {
		if (sitetype > 0) {
			model.addAttribute("branchList", branchDAO.getQueryBranchByBranchidAndUserid(getSessionUser().getUserid(), sitetype));
		}
		List<OperationSetTime> list = operationSetTimeDAO.getOperationSetTimeList(getSessionUser().getUserid(), sitetype, modelname.trim(), branchids, 0);
		List<Branch> bList = branchDAO.getAllBranches();
		List<OperationSetTime> oList = new ArrayList<OperationSetTime>();
		JSONObject saveOutTimeJson = new JSONObject();
		if (list != null && !list.isEmpty()) {
			for (int i = 0; i < list.size(); i++) {
				OperationSetTime o = list.get(i);
				saveOutTimeJson = JSONObject.fromObject(o.getValue());

				// 拼接显示超期异常时效
				StringBuffer sb = new StringBuffer();
				for (int j = 1; j <= 11; j++) {
					String text = "";
					String texthour = "";
					for (OperationTimeEnum e : OperationTimeEnum.values()) {
						if (j == e.getValue()) {
							text = e.getText();
						}
					}
					if (saveOutTimeJson.get("A" + j) != null) {
						if (saveOutTimeJson.getLong("A" + j) >= 24) {
							texthour = saveOutTimeJson.getLong("A" + j) / 24 + "天；";
						} else {
							texthour = saveOutTimeJson.getLong("A" + j) + "小时；";
						}
						sb.append(text + texthour);
					}

				}
				o.setValue(sb.toString());

				// 机构类型
				for (BranchEnum be : BranchEnum.values()) {
					if (o.getSitetype() == be.getValue()) {
						o.setSitetypename(be.getText());
					}
				}

				// 机构名称
				StringBuffer sbBranch = new StringBuffer();
				if (o != null && o.getBranchids() != null && o.getBranchids().length() > 0) {
					for (int j = 0; j < o.getBranchids().split(",").length; j++) {
						long branchid = Long.parseLong(o.getBranchids().split(",")[j]);
						sbBranch.append(dataStatisticsService.getQueryBranchName(bList, branchid) + ",");
					}
				}
				o.setBranchname(sbBranch.toString());
				oList.add(o);
			}
		}
		model.addAttribute("oList", oList);
		return "/exceptionmonitor/system";
	}

	/**
	 * 跳转至超期异常时效设置新增、修改
	 * 
	 * @return
	 */
	@RequestMapping("/add")
	public String add(Model model, @RequestParam(value = "id", defaultValue = "0", required = false) long id, @RequestParam(value = "sitetype", required = false, defaultValue = "0") long sitetype) {
		if (id > 0) {// 更新
			OperationSetTime o = operationSetTimeDAO.getOperationSetTimeById(id);
			JSONObject saveOutTimeJson = new JSONObject();
			saveOutTimeJson = JSONObject.fromObject(o.getValue());
			long A1 = saveOutTimeJson.get("A1") == null ? 0 : saveOutTimeJson.getLong("A1");
			long A2 = saveOutTimeJson.get("A2") == null ? 0 : saveOutTimeJson.getLong("A2");
			long A3 = saveOutTimeJson.get("A3") == null ? 0 : saveOutTimeJson.getLong("A3");
			long A4 = saveOutTimeJson.get("A4") == null ? 0 : saveOutTimeJson.getLong("A4");
			long A5 = saveOutTimeJson.get("A5") == null ? 0 : saveOutTimeJson.getLong("A5");
			long A6 = saveOutTimeJson.get("A6") == null ? 0 : saveOutTimeJson.getLong("A6");
			long A7 = saveOutTimeJson.get("A7") == null ? 0 : saveOutTimeJson.getLong("A7");
			long A8 = saveOutTimeJson.get("A8") == null ? 0 : saveOutTimeJson.getLong("A8");
			long A9 = saveOutTimeJson.get("A9") == null ? 0 : saveOutTimeJson.getLong("A9");
			long A10 = saveOutTimeJson.get("A10") == null ? 0 : saveOutTimeJson.getLong("A10");
			long A11 = saveOutTimeJson.get("A11") == null ? 0 : saveOutTimeJson.getLong("A11");
			model.addAttribute("A1", A1);
			model.addAttribute("A2", A2);
			model.addAttribute("A3", A3);
			model.addAttribute("A4", A4);
			model.addAttribute("A5", A5);
			model.addAttribute("A6", A6);
			model.addAttribute("A7", A7);
			model.addAttribute("A8", A8);
			model.addAttribute("A9", A9);
			model.addAttribute("A10", A10);
			model.addAttribute("A11", A11);

			sitetype = o.getSitetype();
			String modelname = o.getName();
			model.addAttribute("modelname", modelname);
			model.addAttribute("oBranchids", o.getBranchids());
		}
		model.addAttribute("branchList", branchDAO.getQueryBranchByBranchidAndUserid(getSessionUser().getUserid(), sitetype));
		model.addAttribute("sitetype", sitetype);
		return "exceptionmonitor/add";
	}

	/**
	 * 超期异常时效设置新增、修改
	 * 
	 * @return
	 */
	@RequestMapping("/systemsave")
	public @ResponseBody String systemsave(Model model, @RequestParam(value = "id", defaultValue = "0", required = false) long id,
			@RequestParam(value = "sitetype", defaultValue = "", required = false) int sitetype, @RequestParam(value = "modelname", defaultValue = "", required = false) String modelname,
			@RequestParam(value = "branchids", required = false, defaultValue = "") String[] branchids, @RequestParam(value = "A1", defaultValue = "0", required = false) long A1,
			@RequestParam(value = "A2", defaultValue = "0", required = false) long A2, @RequestParam(value = "A3", defaultValue = "0", required = false) long A3,
			@RequestParam(value = "A4", defaultValue = "0", required = false) long A4, @RequestParam(value = "A5", defaultValue = "0", required = false) long A5,
			@RequestParam(value = "A6", defaultValue = "0", required = false) long A6, @RequestParam(value = "A7", defaultValue = "0", required = false) long A7,
			@RequestParam(value = "A8", defaultValue = "0", required = false) long A8, @RequestParam(value = "A9", defaultValue = "0", required = false) long A9,
			@RequestParam(value = "A10", defaultValue = "0", required = false) long A10, @RequestParam(value = "A11", defaultValue = "0", required = false) long A11) {
		try {
			if (id > 0) {// 先删除
				operationSetTimeDAO.deleteOperationTimebyid(id);
				operationDetailDAO.deleteOperationDetailByTimeid(id);
			}

			JSONObject saveOutTimeJson = new JSONObject();
			if (sitetype == BranchEnum.KuFang.getValue()) {
				saveOutTimeJson.put("A1", A1);
				saveOutTimeJson.put("A6", A6);
			} else if (sitetype == BranchEnum.ZhanDian.getValue()) {
				saveOutTimeJson.put("A6", A6);
				saveOutTimeJson.put("A3", A3);
				saveOutTimeJson.put("A4", A4);
				saveOutTimeJson.put("A9", A9);
				saveOutTimeJson.put("A10", A10);
			} else if (sitetype == BranchEnum.ZhongZhuan.getValue()) {
				saveOutTimeJson.put("A7", A7);
				saveOutTimeJson.put("A2", A2);
				saveOutTimeJson.put("A11", A11);
			} else if (sitetype == BranchEnum.TuiHuo.getValue()) {
				saveOutTimeJson.put("A8", A8);
				saveOutTimeJson.put("A5", A5);
			}
			// 获得机构ids
			String ids = "";
			if (branchids != null && branchids.length > 0) {
				for (String branchid : branchids) {
					ids += branchid + ",";
				}
			}
			if (!"".equals(ids)) {
				ids = ids.substring(0, ids.lastIndexOf(","));
			}
			// 插入超期异常方案
			long timeid = operationSetTimeDAO.creOperationSetTime(modelname, saveOutTimeJson.toString(), getSessionUser().getUserid(), new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()),
					sitetype, 1, ids);
			// 插入超期异常方案明细
			if (branchids != null && branchids.length > 0) {
				for (String branchid : branchids) {
					operationDetailDAO.creOperationDetail(timeid, Long.parseLong(branchid));
				}
			}
			return "{\"errorCode\":0,\"error\":\"保存成功\"}";
		} catch (CwbException e) {
			return "{\"errorCode\":1,\"error\":\"" + e.getMessage() + "\"}";
		}
	}

	/**
	 * 超期异常时效设置启用、停用
	 * 
	 * @return
	 */
	@RequestMapping("/updatestate")
	public @ResponseBody String updatestate(Model model, @RequestParam(value = "id", defaultValue = "", required = false) long id) {
		try {
			operationSetTimeDAO.updateOperationTimeState(id);
			return "{\"errorCode\":0}";
		} catch (CwbException e) {
			return "{\"errorCode\":1}";
		}
	}

}

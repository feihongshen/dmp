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

import cn.explink.core.pager.Pager;
import cn.explink.dao.BranchDAO;
import cn.explink.dao.CommonDAO;
import cn.explink.dao.ComplaintDAO;
import cn.explink.dao.CustomWareHouseDAO;
import cn.explink.dao.CustomerDAO;
import cn.explink.dao.CwbDAO;
import cn.explink.dao.DeliveryStateDAO;
import cn.explink.dao.ExportmouldDAO;
import cn.explink.dao.MonitorDAO;
import cn.explink.dao.MonitorKucunDAO;
import cn.explink.dao.ReasonDao;
import cn.explink.dao.RemarkDAO;
import cn.explink.dao.TuihuoRecordDAO;
import cn.explink.dao.UserDAO;
import cn.explink.domain.Branch;
import cn.explink.domain.Common;
import cn.explink.domain.Complaint;
import cn.explink.domain.CustomWareHouse;
import cn.explink.domain.Customer;
import cn.explink.domain.DeliveryState;
import cn.explink.domain.Reason;
import cn.explink.domain.Remark;
import cn.explink.domain.SetExportField;
import cn.explink.domain.TuihuoRecord;
import cn.explink.domain.User;
import cn.explink.enumutil.BranchEnum;
import cn.explink.service.DataStatisticsService;
import cn.explink.service.ExplinkUserDetail;
import cn.explink.service.ExportService;
import cn.explink.service.MonitorLogService;
import cn.explink.util.ExcelUtils;
import cn.explink.util.Page;
import cn.explink.util.StreamingStatementCreator;

@Controller
@RequestMapping("/monitorlog")
public class MonitorLogController {

	@Autowired
	MonitorLogService monitorLogService;
	@Autowired
	UserDAO userDAO;
	@Autowired
	CwbDAO cwbDAO;
	@Autowired
	BranchDAO branchDAO;
	@Autowired
	CustomerDAO customerDAO;
	@Autowired
	ExportService exportService;
	@Autowired
	ExportmouldDAO exportmouldDAO;
	@Autowired
	CommonDAO commonDAO;
	@Autowired
	CustomWareHouseDAO customWareHouseDAO;
	@Autowired
	RemarkDAO remarkDAO;
	@Autowired
	ReasonDao reasonDao;
	@Autowired
	TuihuoRecordDAO tuihuoRecordDAO;
	@Autowired
	DeliveryStateDAO deliveryStateDAO;
	@Autowired
	ComplaintDAO complaintDAO;
	@Autowired
	MonitorDAO monitorDAO;
	@Autowired
	MonitorKucunDAO monitorKucunDAO;
	@Autowired
	SecurityContextHolderStrategy securityContextHolderStrategy;
	@Autowired
	DataStatisticsService dataStatisticsService;
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	/**
	 * 控制查询条件线程变量
	 */
	public static final ThreadLocal<String[]> QUERY_CONDITION = new ThreadLocal<String[]>();
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	private User getSessionUser() {
		ExplinkUserDetail userDetail = (ExplinkUserDetail) this.securityContextHolderStrategy.getContext().getAuthentication().getPrincipal();
		return userDetail.getUser();
	}

	/**
	 * 查看订单生命周期监控
	 *
	 * @param model
	 * @param cwb
	 * @param abnormaltypeid
	 * @return
	 */
	@RequestMapping("/monitorloglist")
	public String monitorloglist(Model model,@RequestParam(value = "customerid", required = false, defaultValue = "") String[] customeridStr,
			HttpServletRequest request) {
		
		List<Customer> clist =  this.customerDAO.getAllCustomers();//获取所有供货商属性值
		model.addAttribute("customerlist",clist); 
		Map<Long,String> cmap =new HashMap<Long , String>();   //把供货商id 和 供货商名称对应存入map集合
		for (Customer cut : clist) {                             
			cmap.put(cut.getCustomerid(), cut.getCustomername());
		}
		
		List<String> customeridList = this.dataStatisticsService.getList(customeridStr); //把前台传入的供货商ID字符串数组储存成集合形式
		model.addAttribute("customeridStr", customeridList);
		List<Branch>  blist = branchDAO.getBranchAllzhandian(BranchEnum.KuFang.getValue()+""); //获取所有库房的信息
		String branchids ="-1";
		if(blist != null && blist.size()>0){               //把库房的站点id存储成in()所需格式
			for (Branch branch : blist) {
				branchids += ","+branch.getBranchid();
			}
		}
		List<Branch>  blist1 = branchDAO.getBranchAllzhandian(BranchEnum.ZhongZhuan.getValue()+""); //获取所有库房的信息
		String branchids1 ="-1";
		if(blist1 != null && blist1.size()>0){               //把中转库的站点id存储成in()所需格式
			for (Branch branch : blist1) {
				branchids1 += ","+branch.getBranchid();
			}
		}
		List<Branch>  blist2 = branchDAO.getBranchAllzhandian(BranchEnum.TuiHuo.getValue()+""); //获取所有库房的信息
		String branchids2 ="-1";
		if(blist2 != null && blist2.size()>0){               //把退货库的站点id存储成in()所需格式
			for (Branch branch : blist2) {
				branchids2 += ","+branch.getBranchid();
			}
		}
		Map<Long ,MonitorLogSim> weidaohuoMap = new HashMap<Long,MonitorLogSim>();
		Map<Long ,MonitorLogSim> tihuoMap = new HashMap<Long,MonitorLogSim>();
		Map<Long ,MonitorLogSim> rukuMap = new HashMap<Long,MonitorLogSim>();
		Map<Long ,MonitorLogSim> chukuMap = new HashMap<Long,MonitorLogSim>();
		Map<Long ,MonitorLogSim> daozhanMap = new HashMap<Long,MonitorLogSim>();
		Map<Long ,MonitorLogSim> tuihuoyichuzhanMap = new HashMap<Long,MonitorLogSim>();
		Map<Long ,MonitorLogSim> zhongzhuanyichuzhanMap = new HashMap<Long,MonitorLogSim>();
		Map<Long ,MonitorLogSim> zhongzhanrukuMap = new HashMap<Long,MonitorLogSim>();
		Map<Long ,MonitorLogSim> tuihuorukuMap = new HashMap<Long,MonitorLogSim>();
		Map<Long ,MonitorLogSim> tuigonghuoshangMap = new HashMap<Long,MonitorLogSim>();
		Map<Long ,MonitorLogSim> zhandianzaizhanzijinMap = new HashMap<Long,MonitorLogSim>();
		Map<Long ,MonitorLogSim> zhongzhuankuyichuweidaozhanMap = new HashMap<Long,MonitorLogSim>();
		Map<Long ,MonitorLogSim> tuihuokutuihuozaitouweidaozhanMap = new HashMap<Long,MonitorLogSim>();
		Map<Long ,MonitorLogSim> tuikehuweishoukuanMap = new HashMap<Long,MonitorLogSim>();
		
		String customerids = "";
		
		if(request.getParameter("isnow") != null && request.getParameter("isnow").equals("1") ){
			
			
//			if(customeridStr.length > 0 || customeridStr != null ){
//				QUERY_CONDITION.set(customeridStr);
//			}
			if( customeridStr.length > 0 ){
				QUERY_CONDITION.set(customeridStr);
			}
			
			customeridStr = QUERY_CONDITION.get() == null? customeridStr : QUERY_CONDITION.get() ;
			
			customerids =getStrings(customeridStr); //把供货商id存储成in()所需格式
			if(customerids.length()>0){
				List<Customer> cPlist =   customerDAO.getCustomerByIds(customerids); //获取所有选中的供货商信息
				cmap =new HashMap<Long , String>();
				for (Customer cut : cPlist) {
					cmap.put(cut.getCustomerid(), cut.getCustomername());  //把所有选中的供货商id 和 供货商名称对应存入map集合
				}
			}
			
			//未到货
			List<MonitorLogSim> weidaohuoList =   monitorLogService.getMonitorLogByBranchid(branchids,customerids," flowordertype=1 ");
			if(weidaohuoList != null && weidaohuoList.size()>0){    //获取订单是该供货商总数以及receivablefee paybackfee代收货款应收金额和上门退应退金额的总和
				for (MonitorLogSim mon: weidaohuoList) {
					weidaohuoMap.put(mon.getCustomerid(), mon);
				}
			}
			//提货
			List<MonitorLogSim> tihuoList =   monitorLogService.getMonitorLogByBranchid(branchids,customerids," flowordertype=2 ");
			if(tihuoList != null && tihuoList.size()>0){
				for (MonitorLogSim mon: tihuoList) {
					tihuoMap.put(mon.getCustomerid(), mon);
				}
			}
			//入库
			List<MonitorLogSim> rukuList =   monitorLogService.getMonitorLogByBranchid(branchids,customerids," flowordertype = 4 AND currentbranchid IN("+branchids+") ");
			if(rukuList != null && rukuList.size()>0){
				for (MonitorLogSim mon: rukuList) {
					rukuMap.put(mon.getCustomerid(), mon);
				}
			}
			//出库
			List<MonitorLogSim> chukuList =   monitorLogService.getMonitorLogByBranchid(branchids,customerids," flowordertype = 6 AND `startbranchid` IN("+branchids+") ");
			if(chukuList != null && chukuList.size()>0){
				for (MonitorLogSim mon: chukuList) {
					chukuMap.put(mon.getCustomerid(), mon);
				}
			}
			//到站
			List<MonitorLogSim> daozhanList =   monitorDAO.getMonitorLogByExpBranchid(branchids,customerids," flowordertype IN(7,8,9,35,36) ");
			if(daozhanList != null && daozhanList.size()>0){
				for (MonitorLogSim mon: daozhanList) {
					daozhanMap.put(mon.getCustomerid(), mon);
				}
			}
			//退货已出站
				List<MonitorLogSim> tuihuoyichuzhanList =   monitorLogService.getMonitorLogByBranchid(branchids,customerids," flowordertype IN(40) AND startbranchid NOT IN("+branchids+") ");
				if(tuihuoyichuzhanList != null && tuihuoyichuzhanList.size()>0){
					for (MonitorLogSim mon: tuihuoyichuzhanList) {
						tuihuoyichuzhanMap.put(mon.getCustomerid(), mon);
					}
			}
			//中转已出站
			List<MonitorLogSim> zhongzhuanyichuzhanList =   monitorLogService.getMonitorLogByBranchid(branchids,customerids," flowordertype IN(6,14) AND startbranchid NOT IN("+branchids+") ");
			if(zhongzhuanyichuzhanList != null && zhongzhuanyichuzhanList.size()>0){
				for (MonitorLogSim mon: zhongzhuanyichuzhanList) {
					zhongzhuanyichuzhanMap.put(mon.getCustomerid(), mon);
				}
			}
			//中转入库
			List<MonitorLogSim> zhongzhanrukuList =   monitorLogService.getMonitorLogByBranchid(branchids,customerids," flowordertype=12 ");
			if(zhongzhanrukuList != null && zhongzhanrukuList.size()>0){
				for (MonitorLogSim mon: zhongzhanrukuList) {
					zhongzhanrukuMap.put(mon.getCustomerid(), mon);
				}
			}
			//未到货
			List<MonitorLogSim> tuihuorukuList =   monitorLogService.getMonitorLogByBranchid(branchids,customerids," flowordertype=15 ");
			if(tuihuorukuList != null && tuihuorukuList.size()>0){
				for (MonitorLogSim mon: tuihuorukuList) {
					tuihuorukuMap.put(mon.getCustomerid(), mon);
				}
			}
			//未到货 
			List<MonitorLogSim> tuigonghuoshangList =   monitorLogService.getMonitorLogByBranchid(branchids,customerids," flowordertype=27 ");
			if(tuigonghuoshangList != null && tuigonghuoshangList.size()>0){
				for (MonitorLogSim mon: tuigonghuoshangList) {
					tuigonghuoshangMap.put(mon.getCustomerid(), mon);
				}
			}
		/*	//站点在站资金
			List<MonitorLogSim> zhandianzaizhanzijinList =   monitorLogService.getMonitorLogByBranchidWithZhanDianZaiZhanZiJin(branchids,customerids," flowordertype IN(7,8,9,35,36) ");
			if(zhandianzaizhanzijinList != null && zhandianzaizhanzijinList.size()>0){
				for (MonitorLogSim mon: zhandianzaizhanzijinList) {
					zhandianzaizhanzijinMap.put(mon.getCustomerid(), mon);
				}
			}*/
			//中转库已出未到站
			List<MonitorLogSim> zhongzhuankuyichuweidaozhanList =   monitorLogService.getMonitorLogByBranchid(branchids1,customerids," flowordertype=6 and startbranchid IN("+branchids1+")  or flowordertype=14");
			if(zhongzhuankuyichuweidaozhanList != null && zhongzhuankuyichuweidaozhanList.size()>0){
				for (MonitorLogSim mon: zhongzhuankuyichuweidaozhanList) {
					zhongzhuankuyichuweidaozhanMap.put(mon.getCustomerid(), mon);
				}
			}
			//退货库退货再投未到站
			List<MonitorLogSim> tuihuokutuihuozaitouweidaozhanList =   monitorLogService.getMonitorLogByBranchid(branchids2,customerids,"flowordertype=6 and startbranchid IN("+branchids2+")");
			if(tuihuokutuihuozaitouweidaozhanList != null && tuihuokutuihuozaitouweidaozhanList.size()>0){
				for (MonitorLogSim mon: tuihuokutuihuozaitouweidaozhanList) {
					tuihuokutuihuozaitouweidaozhanMap.put(mon.getCustomerid(), mon);
				}
			}
			//退客户未收款
			List<MonitorLogSim> tuikehuweishoukuanList =   monitorLogService.getMonitorLogByBranchid(branchids,customerids," fncustomerbillverifyflag=0 and flowordertype=34");
			if(tuikehuweishoukuanList    != null && tuikehuweishoukuanList.size()>0){
				for (MonitorLogSim mon: tuikehuweishoukuanList) {
					tuikehuweishoukuanMap.put(mon.getCustomerid(), mon);
				}
			}
		}else{
			QUERY_CONDITION.set(null);
			cmap =new HashMap<Long , String>();
		}
		model.addAttribute("tuihuoyichuzhanMap", tuihuoyichuzhanMap);
		model.addAttribute("zhongzhuanyichuzhanMap", zhongzhuanyichuzhanMap);
		model.addAttribute("zhandianzaizhanzijinMap",zhandianzaizhanzijinMap);
		model.addAttribute("zhongzhuankuyichuweidaozhanMap",zhongzhuankuyichuweidaozhanMap);
		model.addAttribute("tuihuokutuihuozaitouweidaozhanMap",tuihuokutuihuozaitouweidaozhanMap);
		model.addAttribute("tuikehuweishoukuanMap",tuikehuweishoukuanMap);
		model.addAttribute("customerids",customerids);
		model.addAttribute("customerMap",cmap);
		model.addAttribute("weidaohuoMap", weidaohuoMap);
		model.addAttribute("tihuoMap", tihuoMap);
		model.addAttribute("rukuMap", rukuMap);
		model.addAttribute("chukuMap", chukuMap);
		model.addAttribute("daozhanMap", daozhanMap);
		model.addAttribute("zhongzhanrukuMap", zhongzhanrukuMap);
		model.addAttribute("tuihuorukuMap", tuihuorukuMap);
		model.addAttribute("tuigonghuoshangMap", tuigonghuoshangMap);
		return "/monitor/monitorlog";
	}
	
	
	@RequestMapping("/show/{customerid}/{type}/{page}")
	public String showMonitor(Model model,@PathVariable("customerid") String customerid,
			@PathVariable("type") String type,
			@PathVariable("page") long page,
			HttpServletRequest request) {
		List<Customer> clist =  this.customerDAO.getAllCustomers();
		model.addAttribute("customerlist",clist);
		Map<Long,String> cmap =new HashMap<Long , String>();
		for (Customer cut : clist) {
			cmap.put(cut.getCustomerid(), cut.getCustomername());
		}
		model.addAttribute("customerMap",cmap);
		List<Branch>  blist = branchDAO.getBranchAllzhandian(BranchEnum.KuFang.getValue()+"");
		String branchids ="-1";
		if(blist != null && blist.size()>0){
			for (Branch branch : blist) {
				branchids += ","+branch.getBranchid();
			}
		}
		List<Branch>  blist1 = branchDAO.getBranchAllzhandian(BranchEnum.ZhongZhuan.getValue()+""); //获取所有库房的信息
		String branchids1 ="-1";
		if(blist1 != null && blist1.size()>0){               //把中转库的站点id存储成in()所需格式
			for (Branch branch : blist1) {
				branchids1 += ","+branch.getBranchid();
			}
		}
		List<Branch>  blist2 = branchDAO.getBranchAllzhandian(BranchEnum.TuiHuo.getValue()+""); //获取所有库房的信息
		String branchids2 ="-1";
		if(blist2 != null && blist2.size()>0){               //把退货库的站点id存储成in()所需格式
			for (Branch branch : blist2) {
				branchids2 += ","+branch.getBranchid();
			}
		}
		if(customerid.equals("-3")){
			customerid ="";
		}
		List<CwbOrderView>  cwborderList =   monitorLogService.getMonitorLogByType(branchids,branchids1,branchids2,customerid,type,page);
		
		model.addAttribute("cwborderList", cwborderList);
		
		long count = monitorLogService.getMonitorLogByTypeCount(branchids,branchids1,branchids2,customerid,type);
/*		long count=0;
		List<CwbOrderView>  cwborderListCount =   monitorLogService.getMonitorLogByType(branchids,branchids1,branchids2,customerid,type,-9);
		*//*if (cwborderListCount.size()>0) {
			count=cwborderListCount.size();
		}*/
		Page pageparm = new Page(count, page, Page.ONE_PAGE_NUMBER);
		
		model.addAttribute("page_obj", pageparm);
		model.addAttribute("page", page);
		model.addAttribute("customerid", customerid.equals("")?"-3":customerid);
		model.addAttribute("type", type);
		model.addAttribute("expType", 1);
		model.addAttribute("exportmouldlist", this.exportmouldDAO.getAllExportmouldByUser(this.getSessionUser().getRoleid()));
		return "/monitor/monitorlogshow";
	}
	//存货监控
	@RequestMapping("/monitorcunhuolist")
	public String monitorcunhuolist(Model model,@RequestParam(value = "dispatchbranchid", required = false, defaultValue = "") String[] dispatchbranchidStr,
			HttpServletRequest request) {
		
		List<Branch> branchList = this.branchDAO.getQueryBranchByBranchsiteAndUserid(this.getSessionUser().getUserid(),BranchEnum.KuFang.getValue()+","+BranchEnum.ZhanDian.getValue()+","+BranchEnum.TuiHuo.getValue()+","+BranchEnum.ZhongZhuan.getValue());
		List<String> dispatchbranchidlist = this.dataStatisticsService.getList(dispatchbranchidStr);
		model.addAttribute("dispatchbranchidStr", dispatchbranchidlist);
		model.addAttribute("branchList", branchList);
		
		Map<Long ,MonitorKucunSim> rukuMap = new HashMap<Long,MonitorKucunSim>();
		Map<Long ,MonitorKucunSim> weidaohuoMap = new HashMap<Long,MonitorKucunSim>();
		Map<Long ,MonitorKucunSim> chukuMap = new HashMap<Long,MonitorKucunSim>();
		
		String branchidsPram =getStrings(dispatchbranchidStr);
		String branchids ="-1";
		if(branchidsPram.length() >0 ){
			branchids = branchidsPram;
		}else{
			if(branchList != null && branchList.size()>0){
				for (Branch branch : branchList) {
					branchids += ","+branch.getBranchid();
				}
			}
		}
		Map<Long ,String> branchMap = new HashMap<Long,String>();
		if(branchidsPram.length() >0 ){
			List<Branch> branchList2 = this.branchDAO.getBranchByBranchidsNoType(branchids);
			for (Branch branch : branchList2) {
				branchMap.put(branch.getBranchid(), branch.getBranchname());
			}
		}
		else if(branchList != null && branchList.size()>0){
			for (Branch branch : branchList) {
				branchMap.put(branch.getBranchid(), branch.getBranchname());
			}
		}
		List<MonitorKucunDTO> monitorList =  new ArrayList<MonitorKucunDTO>();
		if(request.getParameter("isnow") != null && request.getParameter("isnow").equals("1") && branchList != null && branchList.size()>0 ){
			//未到货
			List<MonitorKucunSim> weidaohuoList =   monitorKucunDAO.getMonitorLogByBranchid(branchids," flowordertype in(1,2) "," nextbranchid");
			if(weidaohuoList != null && weidaohuoList.size()>0){
				for (MonitorKucunSim mon: weidaohuoList) {
					weidaohuoMap.put(mon.getBranchid(), mon);
				}
			}
			//入库
			List<MonitorKucunSim> rukuList =   monitorKucunDAO.getMonitorLogByBranchid(branchids," (flowordertype IN( 4,12,15,7,8,9,35) OR (flowordertype =36 AND deliverystate NOT IN(1,2,3)) ) ");
			if(rukuList != null && rukuList.size()>0){
				for (MonitorKucunSim mon: rukuList) {
					rukuMap.put(mon.getBranchid(), mon);
				}
			}
			//出库
			List<MonitorKucunSim> chukuList =   monitorKucunDAO.getMonitorLogByBranchid(branchids," flowordertype IN( 6,14,40,27) ");
			if(chukuList != null && chukuList.size()>0){
				for (MonitorKucunSim mon: chukuList) {
					chukuMap.put(mon.getBranchid(), mon);
				}
			}
			
			
		}else{
			branchMap = new HashMap<Long,String>();
		}		
		
		model.addAttribute("branchids", branchids);
		model.addAttribute("branchMap", branchMap);
		
		model.addAttribute("weidaohuoMap", weidaohuoMap);
		model.addAttribute("rukuMap", rukuMap);
		model.addAttribute("chukuMap", chukuMap);
		model.addAttribute("monitorList", monitorList);
		return "/monitor/monitorcunhuo";
	}
	//存货监控明细
	@RequestMapping("/showkucun/{branchid}/{type}/{page}")
	public String showkucun(Model model,@PathVariable("branchid") String branchid,
			@PathVariable("type") String type,
			@PathVariable("page") long page,
			HttpServletRequest request) {
		List<Customer> clist =  this.customerDAO.getAllCustomers();
		model.addAttribute("customerlist",clist);
		Map<Long,String> cmap =new HashMap<Long , String>();
		for (Customer cut : clist) {
			cmap.put(cut.getCustomerid(), cut.getCustomername());
		}
		model.addAttribute("customerMap",cmap);
		List<Branch>  blist = this.branchDAO.getQueryBranchByBranchsiteAndUserid(this.getSessionUser().getUserid(),BranchEnum.KuFang.getValue()+","+BranchEnum.ZhanDian.getValue()+","+BranchEnum.TuiHuo.getValue()+","+BranchEnum.ZhongZhuan.getValue());
		String branchids ="-1";
		if(blist != null && blist.size()>0){
			for (Branch branch : blist) {
				branchids += ","+branch.getBranchid();
			}
		}
		List<CwbOrderView>  cwborderList =   monitorLogService.getMonitorKucunByType(branchids,branchid,type,page);
		
		model.addAttribute("cwborderList", cwborderList);
		
		long count = monitorLogService.getMonitorKucunByTypeCount(branchids,branchid,type);
		Page pageparm = new Page(count, page, Page.ONE_PAGE_NUMBER);
		
		model.addAttribute("page_obj", pageparm);
		model.addAttribute("page", page);
		model.addAttribute("branchid", branchid);
		model.addAttribute("type", type);
		model.addAttribute("expType", 2);
		model.addAttribute("exportmouldlist", this.exportmouldDAO.getAllExportmouldByUser(this.getSessionUser().getRoleid()));
		return "/monitor/monitorcunhuoshow";
	}
	
	@RequestMapping("/exportExcel")
	public void exportExcel(Model model, HttpServletResponse response, @RequestParam(value = "customerid", required = false, defaultValue = "") String customerid,
			 @RequestParam(value = "branchid", required = false, defaultValue = "-3") String branchid,
			@RequestParam(value = "type", required = false, defaultValue = "") String type,
			@RequestParam(value = "expType", required = false, defaultValue = "1") long expType,
			@RequestParam(value = "exportmould", required = false, defaultValue = "0") String exportEume) {
		String[] cloumnName1 = {}; // 导出的列名
		String[] cloumnName2 = {}; // 导出的英文列名
		String[] cloumnName3 = {}; // 导出的数据类型

		List<SetExportField> listSetExportField = this.exportmouldDAO.getSetExportFieldByStrs(exportEume);
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
		String sheetName = "订单明细"; // sheet的名称
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
		String fileName = "monitor_" + df.format(new Date()) + ".xlsx"; // 文件名
		try {
			List<Branch>  blist = branchDAO.getBranchAllzhandian(BranchEnum.KuFang.getValue()+"");
			String branchids ="-1";
			if(blist != null && blist.size()>0){
				for (Branch branch : blist) {
					branchids += ","+branch.getBranchid();
				}
			}
			if(customerid.equals("-3")){
				customerid = "";
			}
			String sql1 = "";
			if(expType ==1 ){
				if("weidaohuo".equals(type)){
					sql1 =   monitorDAO.getMonitorLogByTypeSql(" flowordertype=1 ", customerid);
				}
				
				if("tihuo".equals(type)){
					sql1 =   monitorDAO.getMonitorLogByTypeSql(" flowordertype=2 ", customerid);
				}
				if("ruku".equals(type)){
					sql1 =   monitorDAO.getMonitorLogByTypeSql(" flowordertype = 4 AND currentbranchid IN("+branchids+") ", customerid);
				}
				if("chuku".equals(type)){
					sql1 =   monitorDAO.getMonitorLogByTypeSql(" flowordertype = 6 AND `startbranchid` IN("+branchids+") ",customerid);
				}
				if("daozhan".equals(type)){
					sql1 =   monitorDAO.getMonitorLogByTypeAndNotInSql("7,8,9,35,36", branchids,customerid);
				}
				
				if("yichuzhan".equals(type)){
					sql1 =  monitorDAO.getMonitorLogByTypeSql(" flowordertype IN(6,14,40) AND startbranchid NOT IN("+branchids+") ",customerid);
				}
				if("Zhongzhanruku".equals(type)){
					sql1 =   monitorDAO.getMonitorLogByTypeSql(" flowordertype=12 ",customerid);
				}
				if("tuihuoruku".equals(type)){
					sql1 =   monitorDAO.getMonitorLogByTypeSql(" flowordertype=15 ", customerid);
				}
				if("tuigonghuoshang".equals(type)){
					sql1 =   monitorDAO.getMonitorLogByTypeSql(" flowordertype=27 ",customerid);
				}
				if("all".equals(type)){
					List<String> cwbList =   monitorDAO.getMonitorLogByTypeAndNotIn("7,8,9,35,36", branchids,customerid);
					String cwbs ="";
					if (cwbList.size() > 0) {
						cwbs = this.dataStatisticsService.getOrderFlowCwbs(cwbList);
					} else {
						cwbs = "'--'";
					}
					
					StringBuffer wheresql = new StringBuffer("(flowordertype IN(1,2,6,12,15,27) " +
							" OR (flowordertype = 4 AND currentbranchid IN("+branchids+" ) ) " +
							" OR (flowordertype IN(14,40)  AND  startbranchid NOT IN("+branchids+"))" +
							" OR cwb IN("+cwbs+")" +
							") ");
					
					
					sql1 =   monitorDAO.getMonitorLogByTypeSql(wheresql.toString(),customerid);
				}
			}else{
				List<Branch>  branchlist = this.branchDAO.getQueryBranchByBranchsiteAndUserid(this.getSessionUser().getUserid(),BranchEnum.KuFang.getValue()+","+BranchEnum.ZhanDian.getValue()+","+BranchEnum.TuiHuo.getValue()+","+BranchEnum.ZhongZhuan.getValue());
				String lbranchids ="-1";
				if(branchlist != null && branchlist.size()>0){
					for (Branch branch : branchlist) {
						lbranchids += ","+branch.getBranchid();
					}
				}
				if("-3".equals(branchid)){
					branchid ="";
				}
				if("kucun".equals(type)){
					sql1 =   monitorKucunDAO.getMonitorKucunByTypeSql("1", branchid,lbranchids);
				}
				
				if("yichukuzaitu".equals(type)){
					sql1 =   monitorKucunDAO.getMonitorLogByTypeSql("6,14,40,27", branchid,lbranchids);
				}
				
				if("weiruku".equals(type)){
					sql1 =   cwbDAO.getMonitorLogByTypeSql(" flowordertype in(1,2) ", branchid, branchids);
				}
				
				
				if("all".equals(type)){
					List<String>   cwbList =   monitorKucunDAO.getMonitorKucunByTypeNoPage("1", branchid,branchids);
						String cwbs1 ="";
						if (cwbList.size() > 0) {
							cwbs1 = this.dataStatisticsService.getOrderFlowCwbs(cwbList);
						} else {
							cwbs1 = "'--'";
						}
						cwbList =   monitorKucunDAO.getMonitorLogByTypeNoPage( " flowordertype in(6,14,40,27) ", branchid,branchids);
						String cwbs2 ="";
						if (cwbList.size() > 0) {
							cwbs2 = this.dataStatisticsService.getOrderFlowCwbs(cwbList);
						} else {
							cwbs2 = "'--'";
						}
						String wheresql =" ( " +
						 		" cwb in("+cwbs1+")" +
						 		" or cwb in("+cwbs2+")) or  ";
					sql1 =   cwbDAO.getMonitorLogByTypeSql(wheresql,branchid,lbranchids);
				}
			}
			final String sql =sql1;

			ExcelUtils excelUtil = new ExcelUtils() { // 生成工具类实例，并实现填充数据的抽象方法
				@Override
				public void fillData(final Sheet sheet, final CellStyle style) {
					final List<User> uList = userDAO.getAllUser();
					final Map<Long, Customer> cMap = customerDAO.getAllCustomersToMap();
					final List<Branch> bList = branchDAO.getAllBranches();
					final List<Common> commonList = commonDAO.getAllCommons();
					final List<CustomWareHouse> cWList = customWareHouseDAO.getAllCustomWareHouse();
					List<Remark> remarkList = remarkDAO.getAllRemark();
					final Map<String, Map<String, String>> remarkMap = exportService.getInwarhouseRemarks(remarkList);
					final List<Reason> reasonList = reasonDao.getAllReason();
					jdbcTemplate.query(new StreamingStatementCreator(sql), new ResultSetExtractor<Object>() {
						private int count = 0;
						ColumnMapRowMapper columnMapRowMapper = new ColumnMapRowMapper();
						private List<Map<String, Object>> recordbatch = new ArrayList<Map<String, Object>>();

						public void processRow(ResultSet rs) throws SQLException {
							Map<String, Object> mapRow = this.columnMapRowMapper.mapRow(rs, this.count);
							this.recordbatch.add(mapRow);
							this.count++;
							if ((this.count % 100) == 0) {
								this.writeBatch();
							}
						}

						private void writeSingle(Map<String, Object> mapRow, TuihuoRecord tuihuoRecord, DeliveryState ds, Map<String, String> allTime, int rownum, Map<String, String> cwbspayupidMap,
								Map<String, String> complaintMap) throws SQLException {
							Row row = sheet.createRow(rownum + 1);
							row.setHeightInPoints(15);
							for (int i = 0; i < cloumnName4.length; i++) {
								Cell cell = row.createCell((short) i);
								cell.setCellStyle(style);
								// sheet.setColumnWidth(i, (short) (5000));
								// //设置列宽
								Object a = exportService.setObjectA(cloumnName5, mapRow, i, uList, cMap, bList, commonList, tuihuoRecord, ds, allTime, cWList, remarkMap,
										reasonList, cwbspayupidMap, complaintMap);
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
							this.writeBatch();
							return null;
						}

						public void writeBatch() throws SQLException {
							if (this.recordbatch.size() > 0) {
								List<String> cwbs = new ArrayList<String>();
								for (Map<String, Object> mapRow : this.recordbatch) {
									cwbs.add(mapRow.get("cwb").toString());
								}
								Map<String, DeliveryState> deliveryStates = this.getDeliveryListByCwbs(cwbs);
								Map<String, TuihuoRecord> tuihuorecoredMap = this.getTuihuoRecoredMap(cwbs);
								Map<String, String> cwbspayupMsp = this.getcwbspayupidMap(cwbs);
								Map<String, String> complaintMap = this.getComplaintMap(cwbs);
								Map<String, Map<String, String>> orderflowList = dataStatisticsService.getOrderFlowByCredateForDetailAndExportAllTime(cwbs, bList);
								int size = this.recordbatch.size();
								for (int i = 0; i < size; i++) {
									String cwb = this.recordbatch.get(i).get("cwb").toString();
									this.writeSingle(this.recordbatch.get(i), tuihuorecoredMap.get(cwb), deliveryStates.get(cwb), orderflowList.get(cwb), (this.count - size) + i, cwbspayupMsp,
											complaintMap);
								}
								this.recordbatch.clear();
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
							for (DeliveryState deliveryState :deliveryStateDAO.getActiveDeliveryStateByCwbs(cwbs)) {
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

				}
			};
			excelUtil.excel(response, cloumnName4, sheetName, fileName);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	private String getStrings(String[] strArr) {
		String strs = "";
		if (strArr.length > 0) {
			for (String str : strArr) {
				strs += str + ",";
			}
		}

		if (strs.length() > 0) {
			strs = strs.substring(0, strs.length() - 1);
		}
		return strs;
	}
	
	
}
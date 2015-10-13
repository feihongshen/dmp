package cn.explink.service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Service;

import cn.explink.controller.EmaildateTDO;
import cn.explink.controller.MonitorDTO;
import cn.explink.controller.MonitorView;
import cn.explink.dao.GetDmpDAO;
import cn.explink.dao.MonitorHouseDAO;
import cn.explink.domain.Branch;
import cn.explink.domain.CwbOrder;
import cn.explink.util.DateDayUtil;

@Service
public class MonitorHouseService {
	@Autowired
	MonitorHouseDAO monitorDAO;

	// 库房监控
	public List<MonitorView> getHouseViewByNowBranchList(List<Branch> branchList) {
		List<MonitorView> monitorViewList = new ArrayList<MonitorView>();
		if (branchList.size() > 0) {
			for (Branch b : branchList) {
				MonitorView mv = monitorDAO.getMonitorView(b.getBranchid());
				if (mv != null) {
					monitorViewList.add(mv);
				}
			}
		}
		return monitorViewList;
	}

	public List<MonitorView> getHouseViewList(List<Branch> branchList) {
		List<MonitorView> monitorViewList = new ArrayList<MonitorView>();
		if (branchList.size() > 0) {
			for (Branch b : branchList) {
				MonitorView mv = monitorDAO.getMonitorView(b.getBranchid());
				if (mv != null) {
					monitorViewList.add(mv);
				} else {
					MonitorView monitorView = new MonitorView();
					monitorView.setCarwarehouse(b.getBranchid());
					monitorViewList.add(monitorView);
				}

			}
		}
		return monitorViewList;
	}

	/*
	 * public List<MonitorDTO> getHouseDate(String type){ //处理时间数据 //
	 * if(crateStartdate.length() == 0 && crateEnddate.length()>0){ //
	 * crateStartdate = crateEnddate; // }else if(crateStartdate.length() > 0 &&
	 * crateEnddate.length()==0){ // SimpleDateFormat sdf=new
	 * SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); // crateEnddate = sdf.format(new
	 * Date()); // }else if(crateStartdate.length() == 0 &&
	 * crateEnddate.length()==0){ // SimpleDateFormat sdf=new
	 * SimpleDateFormat("yyyy-MM-dd"); // crateEnddate = sdf.format(new Date())+
	 * " 23:59:59"; // crateStartdate = DateDayUtil.getDayCum(crateEnddate,
	 * 0)+" 00:00:00"; // }
	 * 
	 * List<MonitorDTO> monList = null; if("rkzs".equals(type)){ monList =
	 * monitorDAO.getHouse(); } // else if("wrk".equals(type)){ // mon =
	 * monitorDAO.getWeiruku(crateStartdate, crateEnddate, customerid,branchid);
	 * // }else if("yrk".equals(type)){ // mon =
	 * monitorDAO.getYiruku(crateStartdate, crateEnddate, customerid,branchid);
	 * // }else if("yhwd".equals(type)){ // mon =
	 * monitorDAO.getYouhuowudan(crateStartdate, crateEnddate,
	 * customerid,branchid); // }else if("ydwh".equals(type)){//有单无货 // mon =
	 * new MonitorDTO(); // List<EmaildateTDO> list =
	 * monitorDAO.getEmailDateAndBrandId(crateStartdate, crateEnddate,
	 * branchid); // if(list != null && list.size()>0){ // String
	 * emaildateidStr=""; // for(EmaildateTDO e : list){ // long count =
	 * monitorDAO.getYoudanwuhuoCountCheck(crateStartdate, crateEnddate,
	 * customerid, branchid, e.getEmaildateid()); // if(count > 0){ //
	 * emaildateidStr += e.getEmaildateid()+","; // } // } //
	 * if(emaildateidStr.length()>0){ // emaildateidStr =
	 * emaildateidStr.substring(0, emaildateidStr.length()-1); // } // mon =
	 * monitorDAO.getYoudanwuhuo(crateStartdate, crateEnddate, customerid,
	 * branchid, emaildateidStr); // }else{ // mon.setCountsum(0); //
	 * mon.setCaramountsum(BigDecimal.ZERO); // } // // }else
	 * if("ckzt".equals(type)){ // mon =
	 * monitorDAO.getChukuzaitu(crateStartdate, crateEnddate,
	 * customerid,branchid); // }else if("kc".equals(type)){ // mon =
	 * monitorDAO.getKucun(crateStartdate, crateEnddate, customerid,branchid);
	 * // } return monList; }
	 */
	/*
	 * //中转监控 public MonitorDTO getZhongzhuanDate(String crateStartdate,String
	 * crateEnddate, String customerid,long branchid,String type){ //处理时间数据
	 * if(crateStartdate.length() == 0 && crateEnddate.length()>0){
	 * crateStartdate = crateEnddate; }else if(crateStartdate.length() > 0 &&
	 * crateEnddate.length()==0){ SimpleDateFormat sdf=new
	 * SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); crateEnddate = sdf.format(new
	 * Date()); }else if(crateStartdate.length() == 0 &&
	 * crateEnddate.length()==0){ SimpleDateFormat sdf=new
	 * SimpleDateFormat("yyyy-MM-dd"); crateEnddate = sdf.format(new Date())+
	 * " 23:59:59"; crateStartdate = DateDayUtil.getDayCum(crateEnddate,
	 * 0)+" 00:00:00"; }
	 * 
	 * MonitorDTO mon = null; if("rkzs".equals(type)){ mon =
	 * monitorDAO.getzhongzhuanAll(crateStartdate, crateEnddate,
	 * customerid,branchid); }else if("wrk".equals(type)){ mon =
	 * monitorDAO.getzhongzhuanWeiruku(crateStartdate, crateEnddate,
	 * customerid,branchid); }else if("yrk".equals(type)){ mon =
	 * monitorDAO.getZhongzhuanYiruku(crateStartdate, crateEnddate,
	 * customerid,branchid); }else if("yhwd".equals(type)){ mon =
	 * monitorDAO.getzhongzhuanYouhuowudan(crateStartdate, crateEnddate,
	 * customerid,branchid); }else if("ydwh".equals(type)){//有单无货 mon = new
	 * MonitorDTO(); List<EmaildateTDO> list =
	 * monitorDAO.getEmailDateAndBrandId(crateStartdate, crateEnddate,
	 * branchid); if(list != null && list.size()>0){ String emaildateidStr="";
	 * for(EmaildateTDO e : list){ long count =
	 * monitorDAO.getYoudanwuhuoCountCheck(crateStartdate, crateEnddate,
	 * customerid, branchid, e.getEmaildateid()); if(count > 0){ emaildateidStr
	 * += e.getEmaildateid()+","; } } if(emaildateidStr.length()>0){
	 * emaildateidStr = emaildateidStr.substring(0, emaildateidStr.length()-1);
	 * } mon = monitorDAO.getYoudanwuhuo(crateStartdate, crateEnddate,
	 * customerid, branchid, emaildateidStr); }else{ mon.setCountsum(0);
	 * mon.setCaramountsum(BigDecimal.ZERO); }
	 * 
	 * }else if("ckzt".equals(type)){ mon =
	 * monitorDAO.getzhongzhuanChukuzaitu(crateStartdate, crateEnddate,
	 * customerid,branchid); }else if("kc".equals(type)){ mon =
	 * monitorDAO.getzhongzhuanKucun(crateStartdate, crateEnddate,
	 * customerid,branchid); } return mon; } //退货监控 public MonitorDTO
	 * getTuihuoDate(String crateStartdate,String crateEnddate, String
	 * customerid,long branchid,String type){ //处理时间数据
	 * if(crateStartdate.length() == 0 && crateEnddate.length()>0){
	 * crateStartdate = crateEnddate; }else if(crateStartdate.length() > 0 &&
	 * crateEnddate.length()==0){ SimpleDateFormat sdf=new
	 * SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); crateEnddate = sdf.format(new
	 * Date()); }else if(crateStartdate.length() == 0 &&
	 * crateEnddate.length()==0){ SimpleDateFormat sdf=new
	 * SimpleDateFormat("yyyy-MM-dd"); crateEnddate = sdf.format(new Date())+
	 * " 23:59:59"; crateStartdate = DateDayUtil.getDayCum(crateEnddate,
	 * 0)+" 00:00:00"; } MonitorDTO mon = null; if("rkzs".equals(type)){ mon =
	 * monitorDAO.gettuihuoAll(crateStartdate, crateEnddate,
	 * customerid,branchid); }else if("wrk".equals(type)){ mon =
	 * monitorDAO.gettuihuoWeiruku(crateStartdate, crateEnddate,
	 * customerid,branchid); }else if("yrk".equals(type)){ mon =
	 * monitorDAO.gettuihuoYiruku(crateStartdate, crateEnddate,
	 * customerid,branchid); }else if("yhwd".equals(type)){ mon =
	 * monitorDAO.gettuihuoYouhuowudan(crateStartdate, crateEnddate,
	 * customerid,branchid); }else if("ydwh".equals(type)){//有单无货 mon = new
	 * MonitorDTO(); List<EmaildateTDO> list =
	 * monitorDAO.getEmailDateAndBrandId(crateStartdate, crateEnddate,
	 * branchid); if(list != null && list.size()>0){ String emaildateidStr="";
	 * for(EmaildateTDO e : list){ long count =
	 * monitorDAO.getYoudanwuhuoCountCheck(crateStartdate, crateEnddate,
	 * customerid, branchid, e.getEmaildateid()); if(count > 0){ emaildateidStr
	 * += e.getEmaildateid()+","; } } if(emaildateidStr.length()>0){
	 * emaildateidStr = emaildateidStr.substring(0, emaildateidStr.length()-1);
	 * } mon = monitorDAO.getYoudanwuhuo(crateStartdate, crateEnddate,
	 * customerid, branchid, emaildateidStr); }else{ mon.setCountsum(0);
	 * mon.setCaramountsum(BigDecimal.ZERO); }
	 * 
	 * }else if("ckzt".equals(type)){ mon =
	 * monitorDAO.gettuihuoChukuzaitu(crateStartdate, crateEnddate,
	 * customerid,branchid); }else if("kc".equals(type)){ mon =
	 * monitorDAO.gettuihuoKucun(crateStartdate, crateEnddate,
	 * customerid,branchid); } return mon; } //库房监控查看 public List<CwbOrder>
	 * getHouseDateList(String crateStartdate,String crateEnddate, String
	 * customerid,long branchid,String type,long page){ //处理时间数据
	 * if(crateStartdate.length() == 0 && crateEnddate.length()>0){
	 * crateStartdate = crateEnddate; }else if(crateStartdate.length() > 0 &&
	 * crateEnddate.length()==0){ SimpleDateFormat sdf=new
	 * SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); crateEnddate = sdf.format(new
	 * Date()); }else if(crateStartdate.length() == 0 &&
	 * crateEnddate.length()==0){ SimpleDateFormat sdf=new
	 * SimpleDateFormat("yyyy-MM-dd"); crateEnddate = sdf.format(new Date())+
	 * " 23:59:59"; crateStartdate = DateDayUtil.getDayCum(crateEnddate,
	 * 0)+" 00:00:00"; }
	 * 
	 * List<CwbOrder> list = new ArrayList<CwbOrder>(); if("rkzs".equals(type)){
	 * list = monitorDAO.getHouseList(crateStartdate, crateEnddate,
	 * customerid,branchid,page); }else if("wrk".equals(type)){ list =
	 * monitorDAO.getWeirukuList(crateStartdate, crateEnddate,
	 * customerid,branchid,page); }else if("yrk".equals(type)){ list =
	 * monitorDAO.getYirukuList(crateStartdate, crateEnddate,
	 * customerid,branchid,page); }else if("yhwd".equals(type)){ list =
	 * monitorDAO.getYouhuowudanList(crateStartdate, crateEnddate,
	 * customerid,branchid,page); }else if("ydwh".equals(type)){//有单无货
	 * List<EmaildateTDO> emaillist =
	 * monitorDAO.getEmailDateAndBrandId(crateStartdate, crateEnddate,
	 * branchid); if(emaillist != null && emaillist.size()>0){ String
	 * emaildateidStr=""; for(EmaildateTDO e : emaillist){ long count =
	 * monitorDAO.getYoudanwuhuoCountCheck(crateStartdate, crateEnddate,
	 * customerid, branchid, e.getEmaildateid()); if(count > 0){ emaildateidStr
	 * += e.getEmaildateid()+","; } }
	 * 
	 * if(emaildateidStr.length()>0){ emaildateidStr =
	 * emaildateidStr.substring(0, emaildateidStr.length()-1); } list =
	 * monitorDAO.getYoudanwuhuoList(crateStartdate, crateEnddate,
	 * customerid,branchid,emaildateidStr,page);
	 * 
	 * } return list;
	 * 
	 * }else if("ckzt".equals(type)){ list =
	 * monitorDAO.getChukuzaituList(crateStartdate, crateEnddate,
	 * customerid,branchid,page); }else if("kc".equals(type)){ list =
	 * monitorDAO.getKucunList(crateStartdate, crateEnddate,
	 * customerid,branchid,page); } return list; } //库房监控查看 public String
	 * getHouseDateSql(String crateStartdate,String crateEnddate, String
	 * customerid,long branchid,String type,long page){ //处理时间数据
	 * if(crateStartdate.length() == 0 && crateEnddate.length()>0){
	 * crateStartdate = crateEnddate; }else if(crateStartdate.length() > 0 &&
	 * crateEnddate.length()==0){ SimpleDateFormat sdf=new
	 * SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); crateEnddate = sdf.format(new
	 * Date()); }else if(crateStartdate.length() == 0 &&
	 * crateEnddate.length()==0){ SimpleDateFormat sdf=new
	 * SimpleDateFormat("yyyy-MM-dd"); crateEnddate = sdf.format(new Date())+
	 * " 23:59:59"; crateStartdate = DateDayUtil.getDayCum(crateEnddate,
	 * 0)+" 00:00:00"; }
	 * 
	 * String sql =""; if("rkzs".equals(type)){ sql =
	 * monitorDAO.getHouseSql(crateStartdate, crateEnddate,
	 * customerid,branchid,page); }else if("wrk".equals(type)){ sql =
	 * monitorDAO.getWeirukuSql(crateStartdate, crateEnddate,
	 * customerid,branchid,page); }else if("yrk".equals(type)){ sql =
	 * monitorDAO.getYirukuSql(crateStartdate, crateEnddate,
	 * customerid,branchid,page); }else if("yhwd".equals(type)){ sql =
	 * monitorDAO.getYouhuowudanSql(crateStartdate, crateEnddate,
	 * customerid,branchid,page); }else if("ydwh".equals(type)){//有单无货
	 * List<EmaildateTDO> emaillist =
	 * monitorDAO.getEmailDateAndBrandId(crateStartdate, crateEnddate,
	 * branchid); if(emaillist != null && emaillist.size()>0){ String
	 * emaildateidStr=""; for(EmaildateTDO e : emaillist){ long count =
	 * monitorDAO.getYoudanwuhuoCountCheck(crateStartdate, crateEnddate,
	 * customerid, branchid, e.getEmaildateid()); if(count > 0){ emaildateidStr
	 * += e.getEmaildateid()+","; } }
	 * 
	 * if(emaildateidStr.length()>0){ emaildateidStr =
	 * emaildateidStr.substring(0, emaildateidStr.length()-1); } sql =
	 * monitorDAO.getYoudanwuhuoSql(crateStartdate, crateEnddate,
	 * customerid,branchid,emaildateidStr,page);
	 * 
	 * } return sql;
	 * 
	 * }else if("ckzt".equals(type)){ sql =
	 * monitorDAO.getChukuzaituSql(crateStartdate, crateEnddate,
	 * customerid,branchid,page); }else if("kc".equals(type)){ sql =
	 * monitorDAO.getKucunSql(crateStartdate, crateEnddate,
	 * customerid,branchid,page); } return sql; } //库房监控查看分页总数 public long
	 * getHouseDateCount(String crateStartdate,String crateEnddate, String
	 * customerid,long branchid,String type){ //处理时间数据
	 * if(crateStartdate.length() == 0 && crateEnddate.length()>0){
	 * crateStartdate = crateEnddate; }else if(crateStartdate.length() > 0 &&
	 * crateEnddate.length()==0){ SimpleDateFormat sdf=new
	 * SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); crateEnddate = sdf.format(new
	 * Date()); }else if(crateStartdate.length() == 0 &&
	 * crateEnddate.length()==0){ SimpleDateFormat sdf=new
	 * SimpleDateFormat("yyyy-MM-dd"); crateEnddate = sdf.format(new Date())+
	 * " 23:59:59"; crateStartdate = DateDayUtil.getDayCum(crateEnddate,
	 * 0)+" 00:00:00"; }
	 * 
	 * long count = 0; if("rkzs".equals(type)){ count =
	 * monitorDAO.getHouseCount(crateStartdate, crateEnddate,
	 * customerid,branchid); }else if("wrk".equals(type)){ count =
	 * monitorDAO.getWeirukuCount(crateStartdate, crateEnddate,
	 * customerid,branchid); }else if("yrk".equals(type)){ count =
	 * monitorDAO.getYirukuCount(crateStartdate, crateEnddate,
	 * customerid,branchid); }else if("yhwd".equals(type)){ count =
	 * monitorDAO.getYouhuowudanCount(crateStartdate, crateEnddate,
	 * customerid,branchid); }else if("ydwh".equals(type)){//有单无货
	 * 
	 * List<EmaildateTDO> emaillist =
	 * monitorDAO.getEmailDateAndBrandId(crateStartdate, crateEnddate,
	 * branchid); if(emaillist != null && emaillist.size()>0){ String
	 * emaildateidStr=""; for(EmaildateTDO e : emaillist){ long countCheck =
	 * monitorDAO.getYoudanwuhuoCountCheck(crateStartdate, crateEnddate,
	 * customerid, branchid, e.getEmaildateid()); if(countCheck > 0){
	 * emaildateidStr += e.getEmaildateid()+","; } }
	 * if(emaildateidStr.length()>0){ emaildateidStr =
	 * emaildateidStr.substring(0, emaildateidStr.length()-1); } count =
	 * monitorDAO.getYoudanwuhuoCount(crateStartdate, crateEnddate, customerid,
	 * branchid, emaildateidStr); }
	 * 
	 * }else if("ckzt".equals(type)){ count =
	 * monitorDAO.getChukuzaituCount(crateStartdate, crateEnddate,
	 * customerid,branchid); }else if("kc".equals(type)){ count =
	 * monitorDAO.getKucunCount(crateStartdate, crateEnddate,
	 * customerid,branchid); } return count; } //中转监控查看 public List<CwbOrder>
	 * getzhongzhuanDateList(String crateStartdate,String crateEnddate, String
	 * customerid,long branchid,String type,long page){ //处理时间数据
	 * if(crateStartdate.length() == 0 && crateEnddate.length()>0){
	 * crateStartdate = crateEnddate; }else if(crateStartdate.length() > 0 &&
	 * crateEnddate.length()==0){ SimpleDateFormat sdf=new
	 * SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); crateEnddate = sdf.format(new
	 * Date()); }else if(crateStartdate.length() == 0 &&
	 * crateEnddate.length()==0){ SimpleDateFormat sdf=new
	 * SimpleDateFormat("yyyy-MM-dd"); crateEnddate = sdf.format(new Date())+
	 * " 23:59:59"; crateStartdate = DateDayUtil.getDayCum(crateEnddate,
	 * 0)+" 00:00:00"; }
	 * 
	 * List<CwbOrder> list = new ArrayList<CwbOrder>(); if("rkzs".equals(type)){
	 * list = monitorDAO.getzhongzhuanAllList(crateStartdate, crateEnddate,
	 * customerid,branchid,page); }else if("wrk".equals(type)){ list =
	 * monitorDAO.getzhongzhuanWeirukuList(crateStartdate, crateEnddate,
	 * customerid,branchid,page); }else if("yrk".equals(type)){ list =
	 * monitorDAO.getzhongzhuanYirukuList(crateStartdate, crateEnddate,
	 * customerid,branchid,page); }else if("yhwd".equals(type)){ list =
	 * monitorDAO.getzhongzhuanYouhuowudanList(crateStartdate, crateEnddate,
	 * customerid,branchid,page); }else if("ydwh".equals(type)){//有单无货
	 * List<EmaildateTDO> emaillist =
	 * monitorDAO.getEmailDateAndBrandId(crateStartdate, crateEnddate,
	 * branchid); if(emaillist != null && emaillist.size()>0){ String
	 * emaildateidStr=""; for(EmaildateTDO e : emaillist){ long count =
	 * monitorDAO.getYoudanwuhuoCountCheck(crateStartdate, crateEnddate,
	 * customerid, branchid, e.getEmaildateid()); if(count > 0){ emaildateidStr
	 * += e.getEmaildateid()+","; } }
	 * 
	 * if(emaildateidStr.length()>0){ emaildateidStr =
	 * emaildateidStr.substring(0, emaildateidStr.length()-1); } list =
	 * monitorDAO.getYoudanwuhuoList(crateStartdate, crateEnddate,
	 * customerid,branchid,emaildateidStr,page);
	 * 
	 * } return list;
	 * 
	 * }else if("ckzt".equals(type)){ list =
	 * monitorDAO.getzhongzhuanChukuzaituList(crateStartdate, crateEnddate,
	 * customerid,branchid,page); }else if("kc".equals(type)){ list =
	 * monitorDAO.getzhongzhuanKucunList(crateStartdate, crateEnddate,
	 * customerid,branchid,page); } return list; } public String
	 * getzhongzhuanSql(String crateStartdate,String crateEnddate, String
	 * customerid,long branchid,String type,long page){ //处理时间数据
	 * if(crateStartdate.length() == 0 && crateEnddate.length()>0){
	 * crateStartdate = crateEnddate; }else if(crateStartdate.length() > 0 &&
	 * crateEnddate.length()==0){ SimpleDateFormat sdf=new
	 * SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); crateEnddate = sdf.format(new
	 * Date()); }else if(crateStartdate.length() == 0 &&
	 * crateEnddate.length()==0){ SimpleDateFormat sdf=new
	 * SimpleDateFormat("yyyy-MM-dd"); crateEnddate = sdf.format(new Date())+
	 * " 23:59:59"; crateStartdate = DateDayUtil.getDayCum(crateEnddate,
	 * 0)+" 00:00:00"; }
	 * 
	 * String sql = ""; if("rkzs".equals(type)){ sql =
	 * monitorDAO.getSql(crateStartdate, crateEnddate,
	 * customerid,branchid,page); }else if("wrk".equals(type)){ sql =
	 * monitorDAO.getzhongzhuanWeirukuSql(crateStartdate, crateEnddate,
	 * customerid,branchid,page); }else if("yrk".equals(type)){ sql =
	 * monitorDAO.getzhongzhuanYirukuSql(crateStartdate, crateEnddate,
	 * customerid,branchid,page); }else if("yhwd".equals(type)){ sql =
	 * monitorDAO.getzhongzhuanYouhuowudanSql(crateStartdate, crateEnddate,
	 * customerid,branchid,page); }else if("ydwh".equals(type)){//有单无货
	 * List<EmaildateTDO> emaillist =
	 * monitorDAO.getEmailDateAndBrandId(crateStartdate, crateEnddate,
	 * branchid); if(emaillist != null && emaillist.size()>0){ String
	 * emaildateidStr=""; for(EmaildateTDO e : emaillist){ long count =
	 * monitorDAO.getYoudanwuhuoCountCheck(crateStartdate, crateEnddate,
	 * customerid, branchid, e.getEmaildateid()); if(count > 0){ emaildateidStr
	 * += e.getEmaildateid()+","; } }
	 * 
	 * if(emaildateidStr.length()>0){ emaildateidStr =
	 * emaildateidStr.substring(0, emaildateidStr.length()-1); } sql =
	 * monitorDAO.getYoudanwuhuoSql(crateStartdate, crateEnddate,
	 * customerid,branchid,emaildateidStr,page);
	 * 
	 * } return sql;
	 * 
	 * }else if("ckzt".equals(type)){ sql =
	 * monitorDAO.getzhongzhuanChukuzaituSql(crateStartdate, crateEnddate,
	 * customerid,branchid,page); }else if("kc".equals(type)){ sql =
	 * monitorDAO.getzhongzhuanKucunSql(crateStartdate, crateEnddate,
	 * customerid,branchid,page); } return sql; } //中转监控查看分页总数 public long
	 * getzhongzhuanDateCount(String crateStartdate,String crateEnddate, String
	 * customerid,long branchid,String type){ //处理时间数据
	 * if(crateStartdate.length() == 0 && crateEnddate.length()>0){
	 * crateStartdate = crateEnddate; }else if(crateStartdate.length() > 0 &&
	 * crateEnddate.length()==0){ SimpleDateFormat sdf=new
	 * SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); crateEnddate = sdf.format(new
	 * Date()); }else if(crateStartdate.length() == 0 &&
	 * crateEnddate.length()==0){ SimpleDateFormat sdf=new
	 * SimpleDateFormat("yyyy-MM-dd"); crateEnddate = sdf.format(new Date())+
	 * " 23:59:59"; crateStartdate = DateDayUtil.getDayCum(crateEnddate,
	 * 0)+" 00:00:00"; }
	 * 
	 * long count = 0; if("rkzs".equals(type)){ count =
	 * monitorDAO.getzhongzhuanAllCount(crateStartdate, crateEnddate,
	 * customerid,branchid); }else if("wrk".equals(type)){ count =
	 * monitorDAO.getzhongzhuanWeirukuCount(crateStartdate, crateEnddate,
	 * customerid,branchid); }else if("yrk".equals(type)){ count =
	 * monitorDAO.getzhongzhuanYirukuCount(crateStartdate, crateEnddate,
	 * customerid,branchid); }else if("yhwd".equals(type)){ count =
	 * monitorDAO.getzhongzhuanYouhuowudanCount(crateStartdate, crateEnddate,
	 * customerid,branchid); }else if("ydwh".equals(type)){//有单无货
	 * 
	 * List<EmaildateTDO> emaillist =
	 * monitorDAO.getEmailDateAndBrandId(crateStartdate, crateEnddate,
	 * branchid); if(emaillist != null && emaillist.size()>0){ String
	 * emaildateidStr=""; for(EmaildateTDO e : emaillist){ long countCheck =
	 * monitorDAO.getYoudanwuhuoCountCheck(crateStartdate, crateEnddate,
	 * customerid, branchid, e.getEmaildateid()); if(countCheck > 0){
	 * emaildateidStr += e.getEmaildateid()+","; } }
	 * if(emaildateidStr.length()>0){ emaildateidStr =
	 * emaildateidStr.substring(0, emaildateidStr.length()-1); } count =
	 * monitorDAO.getYoudanwuhuoCount(crateStartdate, crateEnddate, customerid,
	 * branchid, emaildateidStr); }
	 * 
	 * }else if("ckzt".equals(type)){ count =
	 * monitorDAO.getzhongzhuanChukuzaituCount(crateStartdate, crateEnddate,
	 * customerid,branchid); }else if("kc".equals(type)){ count =
	 * monitorDAO.getzhongzhuanKucunCount(crateStartdate, crateEnddate,
	 * customerid,branchid); } return count; } //退货监控查看 public List<CwbOrder>
	 * gettuihuoDateList(String crateStartdate,String crateEnddate, String
	 * customerid,long branchid,String type,long page){ //处理时间数据
	 * if(crateStartdate.length() == 0 && crateEnddate.length()>0){
	 * crateStartdate = crateEnddate; }else if(crateStartdate.length() > 0 &&
	 * crateEnddate.length()==0){ SimpleDateFormat sdf=new
	 * SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); crateEnddate = sdf.format(new
	 * Date()); }else if(crateStartdate.length() == 0 &&
	 * crateEnddate.length()==0){ SimpleDateFormat sdf=new
	 * SimpleDateFormat("yyyy-MM-dd"); crateEnddate = sdf.format(new Date())+
	 * " 23:59:59"; crateStartdate = DateDayUtil.getDayCum(crateEnddate,
	 * 0)+" 00:00:00"; }
	 * 
	 * List<CwbOrder> list = new ArrayList<CwbOrder>(); if("rkzs".equals(type)){
	 * list = monitorDAO.gettuihuoAllList(crateStartdate, crateEnddate,
	 * customerid,branchid,page); }else if("wrk".equals(type)){ list =
	 * monitorDAO.gettuihuoWeirukuList(crateStartdate, crateEnddate,
	 * customerid,branchid,page); }else if("yrk".equals(type)){ list =
	 * monitorDAO.gettuihuoYirukuList(crateStartdate, crateEnddate,
	 * customerid,branchid,page); }else if("yhwd".equals(type)){ list =
	 * monitorDAO.gettuihuoYouhuowudanList(crateStartdate, crateEnddate,
	 * customerid,branchid,page); }else if("ydwh".equals(type)){//有单无货
	 * List<EmaildateTDO> emaillist =
	 * monitorDAO.getEmailDateAndBrandId(crateStartdate, crateEnddate,
	 * branchid); if(emaillist != null && emaillist.size()>0){ String
	 * emaildateidStr=""; for(EmaildateTDO e : emaillist){ long count =
	 * monitorDAO.getYoudanwuhuoCountCheck(crateStartdate, crateEnddate,
	 * customerid, branchid, e.getEmaildateid()); if(count > 0){ emaildateidStr
	 * += e.getEmaildateid()+","; } }
	 * 
	 * if(emaildateidStr.length()>0){ emaildateidStr =
	 * emaildateidStr.substring(0, emaildateidStr.length()-1); } list =
	 * monitorDAO.getYoudanwuhuoList(crateStartdate, crateEnddate,
	 * customerid,branchid,emaildateidStr,page);
	 * 
	 * } return list;
	 * 
	 * }else if("ckzt".equals(type)){ list =
	 * monitorDAO.gettuihuoChukuzaituList(crateStartdate, crateEnddate,
	 * customerid,branchid,page); }else if("kc".equals(type)){ list =
	 * monitorDAO.gettuihuoKucunList(crateStartdate, crateEnddate,
	 * customerid,branchid,page); } return list; } //退货监控查看 public String
	 * gettuihuoSql(String crateStartdate,String crateEnddate, String
	 * customerid,long branchid,String type,long page){ //处理时间数据
	 * if(crateStartdate.length() == 0 && crateEnddate.length()>0){
	 * crateStartdate = crateEnddate; }else if(crateStartdate.length() > 0 &&
	 * crateEnddate.length()==0){ SimpleDateFormat sdf=new
	 * SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); crateEnddate = sdf.format(new
	 * Date()); }else if(crateStartdate.length() == 0 &&
	 * crateEnddate.length()==0){ SimpleDateFormat sdf=new
	 * SimpleDateFormat("yyyy-MM-dd"); crateEnddate = sdf.format(new Date())+
	 * " 23:59:59"; crateStartdate = DateDayUtil.getDayCum(crateEnddate,
	 * 0)+" 00:00:00"; }
	 * 
	 * String sql = ""; if("rkzs".equals(type)){ sql =
	 * monitorDAO.gettuihuoAllSql(crateStartdate, crateEnddate,
	 * customerid,branchid,page); }else if("wrk".equals(type)){ sql =
	 * monitorDAO.gettuihuoWeirukuSql(crateStartdate, crateEnddate,
	 * customerid,branchid,page); }else if("yrk".equals(type)){ sql =
	 * monitorDAO.gettuihuoYirukuSql(crateStartdate, crateEnddate,
	 * customerid,branchid,page); }else if("yhwd".equals(type)){ sql =
	 * monitorDAO.gettuihuoYouhuowudanSql(crateStartdate, crateEnddate,
	 * customerid,branchid,page); }else if("ydwh".equals(type)){//有单无货
	 * List<EmaildateTDO> emaillist =
	 * monitorDAO.getEmailDateAndBrandId(crateStartdate, crateEnddate,
	 * branchid); if(emaillist != null && emaillist.size()>0){ String
	 * emaildateidStr=""; for(EmaildateTDO e : emaillist){ long count =
	 * monitorDAO.getYoudanwuhuoCountCheck(crateStartdate, crateEnddate,
	 * customerid, branchid, e.getEmaildateid()); if(count > 0){ emaildateidStr
	 * += e.getEmaildateid()+","; } }
	 * 
	 * if(emaildateidStr.length()>0){ emaildateidStr =
	 * emaildateidStr.substring(0, emaildateidStr.length()-1); } sql =
	 * monitorDAO.getYoudanwuhuoSql(crateStartdate, crateEnddate,
	 * customerid,branchid,emaildateidStr,page);
	 * 
	 * } return sql;
	 * 
	 * }else if("ckzt".equals(type)){ sql =
	 * monitorDAO.gettuihuoChukuzaituSql(crateStartdate, crateEnddate,
	 * customerid,branchid,page); }else if("kc".equals(type)){ sql =
	 * monitorDAO.gettuihuoKucunSql(crateStartdate, crateEnddate,
	 * customerid,branchid,page); } return sql; } //中转监控查看分页总数 public long
	 * gettuihuoDateCount(String crateStartdate,String crateEnddate, String
	 * customerid,long branchid,String type){ //处理时间数据
	 * if(crateStartdate.length() == 0 && crateEnddate.length()>0){
	 * crateStartdate = crateEnddate; }else if(crateStartdate.length() > 0 &&
	 * crateEnddate.length()==0){ SimpleDateFormat sdf=new
	 * SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); crateEnddate = sdf.format(new
	 * Date()); }else if(crateStartdate.length() == 0 &&
	 * crateEnddate.length()==0){ SimpleDateFormat sdf=new
	 * SimpleDateFormat("yyyy-MM-dd"); crateEnddate = sdf.format(new Date())+
	 * " 23:59:59"; crateStartdate = DateDayUtil.getDayCum(crateEnddate,
	 * 0)+" 00:00:00"; }
	 * 
	 * long count = 0; if("rkzs".equals(type)){ count =
	 * monitorDAO.gettuihuoAllCount(crateStartdate, crateEnddate,
	 * customerid,branchid); }else if("wrk".equals(type)){ count =
	 * monitorDAO.gettuihuoWeirukuCount(crateStartdate, crateEnddate,
	 * customerid,branchid); }else if("yrk".equals(type)){ count =
	 * monitorDAO.gettuihuoYirukuCount(crateStartdate, crateEnddate,
	 * customerid,branchid); }else if("yhwd".equals(type)){ count =
	 * monitorDAO.gettuihuoYouhuowudanCount(crateStartdate, crateEnddate,
	 * customerid,branchid); }else if("ydwh".equals(type)){//有单无货
	 * 
	 * List<EmaildateTDO> emaillist =
	 * monitorDAO.getEmailDateAndBrandId(crateStartdate, crateEnddate,
	 * branchid); if(emaillist != null && emaillist.size()>0){ String
	 * emaildateidStr=""; for(EmaildateTDO e : emaillist){ long countCheck =
	 * monitorDAO.getYoudanwuhuoCountCheck(crateStartdate, crateEnddate,
	 * customerid, branchid, e.getEmaildateid()); if(countCheck > 0){
	 * emaildateidStr += e.getEmaildateid()+","; } }
	 * if(emaildateidStr.length()>0){ emaildateidStr =
	 * emaildateidStr.substring(0, emaildateidStr.length()-1); } count =
	 * monitorDAO.getYoudanwuhuoCount(crateStartdate, crateEnddate, customerid,
	 * branchid, emaildateidStr); }
	 * 
	 * }else if("ckzt".equals(type)){ count =
	 * monitorDAO.gettuihuoChukuzaituCount(crateStartdate, crateEnddate,
	 * customerid,branchid); }else if("kc".equals(type)){ count =
	 * monitorDAO.gettuihuoKucunCount(crateStartdate, crateEnddate,
	 * customerid,branchid); } return count; }
	 */

}

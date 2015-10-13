package cn.explink.service;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cn.explink.controller.MonitorDTO;
import cn.explink.dao.MonitorSiteNewDAO;
import cn.explink.domain.CwbOrder;

@Service
public class MonitorSiteService {
	@Autowired
	MonitorSiteNewDAO monitorDAO;

	// 站点监控
	public MonitorDTO getSiteDate(String crateStartdate, String crateEnddate, String customerid, long branchid, String type, String startinSitetime, String endinSitetime) {

		MonitorDTO mon = null;
		if ("rkzs".equals(type)) {
			mon = monitorDAO.getSite(crateStartdate, crateEnddate, customerid, branchid, startinSitetime, endinSitetime);
		} else if ("weidaohuo".equals(type)) {
			mon = monitorDAO.getWeidaohuo(crateStartdate, crateEnddate, customerid, branchid, startinSitetime, endinSitetime);
		} else if ("rukuweiling".equals(type)) {
			mon = monitorDAO.getRukuweiling(crateStartdate, crateEnddate, customerid, branchid, startinSitetime, endinSitetime);
		} else if ("youhuowudan".equals(type)) {
			mon = monitorDAO.getYouhuowudan(crateStartdate, crateEnddate, customerid, branchid, startinSitetime, endinSitetime);
		} else if ("youdanwuhuo".equals(type)) {
			mon = monitorDAO.getYoudanwuhuo(crateStartdate, crateEnddate, customerid, branchid, startinSitetime, endinSitetime);
		} else if ("yilinghuo".equals(type)) {
			mon = monitorDAO.getYilinghuo(crateStartdate, crateEnddate, customerid, branchid, startinSitetime, endinSitetime);
		} else if ("yichangdan".equals(type)) {
			mon = monitorDAO.getYichangdan(crateStartdate, crateEnddate, customerid, branchid, startinSitetime, endinSitetime);
		} else if ("yiliudan".equals(type)) {
			mon = monitorDAO.getYiliudan(crateStartdate, crateEnddate, customerid, branchid, startinSitetime, endinSitetime);
		} else if ("kucuntuihuo".equals(type)) {
			mon = monitorDAO.getTuihuokuncun(crateStartdate, crateEnddate, customerid, branchid, startinSitetime, endinSitetime);
		} else if ("kucunzhiliu".equals(type)) {
			mon = monitorDAO.getZhiliukucun(crateStartdate, crateEnddate, customerid, branchid, startinSitetime, endinSitetime);
		} else if ("zaituzhongzhuan".equals(type)) {
			mon = monitorDAO.getZhongzhuanzaitu(crateStartdate, crateEnddate, customerid, branchid, startinSitetime, endinSitetime);
		} else if ("zaitutuihuo".equals(type)) {
			mon = monitorDAO.getTuihuozaitu(crateStartdate, crateEnddate, customerid, branchid, startinSitetime, endinSitetime);
		} else if ("weijiaokuan".equals(type)) {
			mon = monitorDAO.getWeijiaokuan(crateStartdate, crateEnddate, customerid, branchid, startinSitetime, endinSitetime);
		} else if ("qiankuan".equals(type)) {
			mon = monitorDAO.getQiankuan(crateStartdate, crateEnddate, customerid, branchid, startinSitetime, endinSitetime);
		} else if ("tuotou".equals(type)) {
			mon = monitorDAO.getTuotou(crateStartdate, crateEnddate, customerid, branchid, startinSitetime, endinSitetime);
		} else if ("weituotou".equals(type)) {
			mon = monitorDAO.getWeituotou(crateStartdate, crateEnddate, customerid, branchid, startinSitetime, endinSitetime);
		} else if ("qita".equals(type)) {
			mon = monitorDAO.getQita(crateStartdate, crateEnddate, customerid, branchid, startinSitetime, endinSitetime);
		}
		return mon;
	}

	// 站点监控查看
	public List<CwbOrder> getSiteDateList(String crateStartdate, String crateEnddate, String customerid, long branchid, String type, long page, String startinSitetime, String endinSitetime) {
		// 处理时间数据
		/*
		 * if(crateStartdate.length() == 0 && crateEnddate.length()>0){
		 * crateStartdate = crateEnddate; }else if(crateStartdate.length() > 0
		 * && crateEnddate.length()==0){ SimpleDateFormat sdf=new
		 * SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); crateEnddate =
		 * sdf.format(new Date()); }else if(crateStartdate.length() == 0 &&
		 * crateEnddate.length()==0){ SimpleDateFormat sdf=new
		 * SimpleDateFormat("yyyy-MM-dd"); crateEnddate = sdf.format(new
		 * Date())+ " 23:59:59"; crateStartdate =
		 * DateDayUtil.getDayCum(crateEnddate, 0)+" 00:00:00"; }
		 * if(startinSitetime.length() == 0 && endinSitetime.length()>0){
		 * startinSitetime = endinSitetime; }else if(startinSitetime.length() >
		 * 0 && endinSitetime.length()==0){ SimpleDateFormat sdf=new
		 * SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); endinSitetime =
		 * sdf.format(new Date()); }else if(startinSitetime.length() == 0 &&
		 * endinSitetime.length()==0){ SimpleDateFormat sdf=new
		 * SimpleDateFormat("yyyy-MM-dd"); endinSitetime = sdf.format(new
		 * Date())+ " 23:59:59"; startinSitetime =
		 * DateDayUtil.getDayCum(endinSitetime, 0)+" 00:00:00"; }
		 */
		List<CwbOrder> list = new ArrayList<CwbOrder>();
		if ("rkzs".equals(type)) {
			list = monitorDAO.getSiteList(crateStartdate, crateEnddate, customerid, branchid, page, "", "");
		} else if ("weidaohuo".equals(type)) {
			list = monitorDAO.getWeidaohuoList(crateStartdate, crateEnddate, customerid, branchid, page, "", "");
		} else if ("rukuweiling".equals(type)) {
			list = monitorDAO.getRukuweilingList("", "", customerid, branchid, page, startinSitetime, endinSitetime);
		} else if ("youhuowudan".equals(type)) {
			list = monitorDAO.getYouhuowudanList("", "", customerid, branchid, page, startinSitetime, endinSitetime);
		} else if ("youdanwuhuo".equals(type)) {
			list = monitorDAO.getYoudanwuhuoList("", "", customerid, branchid, page, startinSitetime, endinSitetime);
		} else if ("yilinghuo".equals(type)) {
			list = monitorDAO.getYilinghuoList("", "", customerid, branchid, page, startinSitetime, endinSitetime);
		} else if ("yichangdan".equals(type)) {
			list = monitorDAO.getYichangdanList("", "", customerid, branchid, page, startinSitetime, endinSitetime);
		} else if ("yiliudan".equals(type)) {
			list = monitorDAO.getYiliudanList("", "", customerid, branchid, page, startinSitetime, endinSitetime);
		} else if ("kuncuntuihuo".equals(type)) {
			list = monitorDAO.getTuihuokuncunList("", "", customerid, branchid, page, startinSitetime, endinSitetime);
		} else if ("kucunzhiliu".equals(type)) {
			list = monitorDAO.getZhiliukucunList("", "", customerid, branchid, page, startinSitetime, endinSitetime);
		} else if ("zaituzhongzhuan".equals(type)) {
			list = monitorDAO.getZhongzhuanzaituList("", "", customerid, branchid, page, startinSitetime, endinSitetime);
		} else if ("zaitutuihuo".equals(type)) {
			list = monitorDAO.getTuihuozaituList("", "", customerid, branchid, page, startinSitetime, endinSitetime);
		} else if ("weijiaokuan".equals(type)) {
			list = monitorDAO.getWeijiaokuanList("", "", customerid, branchid, page, startinSitetime, endinSitetime);
		} else if ("qiankuan".equals(type)) {
			list = monitorDAO.getQiankuanList("", "", customerid, branchid, page, startinSitetime, endinSitetime);
		} else if ("tuotou".equals(type)) {
			list = monitorDAO.getTuotouList("", "", customerid, branchid, page, startinSitetime, endinSitetime);
		} else if ("weituotou".equals(type)) {
			list = monitorDAO.getWeituotouList("", "", customerid, branchid, page, startinSitetime, endinSitetime);
		} else if ("qita".equals(type)) {
			list = monitorDAO.getQitaList("", "", customerid, branchid, page, startinSitetime, endinSitetime);
		}

		return list;
	}

	// 站点监控查看
	public String getSiteDateSql(String crateStartdate, String crateEnddate, String customerid, long branchid, String type, long page, String startinSitetime, String endinSitetime) {
		String sql = "";
		if ("rkzs".equals(type)) {
			sql = monitorDAO.getSiteSql(crateStartdate, crateEnddate, customerid, branchid, page, "", "");
		} else if ("weidaohuo".equals(type)) {
			sql = monitorDAO.getWeidaohuoSql(crateStartdate, crateEnddate, customerid, branchid, page, "", "");
		} else if ("rukuweiling".equals(type)) {
			sql = monitorDAO.getRukuweilingSql("", "", customerid, branchid, page, startinSitetime, endinSitetime);
		} else if ("youhuowudan".equals(type)) {
			sql = monitorDAO.getYouhuowudanSql("", "", customerid, branchid, page, startinSitetime, endinSitetime);
		} else if ("youdanwuhuo".equals(type)) {
			sql = monitorDAO.getYoudanwuhuoSql("", "", customerid, branchid, page, startinSitetime, endinSitetime);
		} else if ("yilinghuo".equals(type)) {
			sql = monitorDAO.getYilinghuoSql("", "", customerid, branchid, page, startinSitetime, endinSitetime);
		} else if ("yichangdan".equals(type)) {
			sql = monitorDAO.getYichangdanSql("", "", customerid, branchid, page, startinSitetime, endinSitetime);
		} else if ("yiliudan".equals(type)) {
			sql = monitorDAO.getYiliudanSql("", "", customerid, branchid, page, startinSitetime, endinSitetime);
		} else if ("kuncuntuihuo".equals(type)) {
			sql = monitorDAO.getTuihuokuncunSql("", "", customerid, branchid, page, startinSitetime, endinSitetime);
		} else if ("kucunzhiliu".equals(type)) {
			sql = monitorDAO.getZhiliukucunSql("", "", customerid, branchid, page, startinSitetime, endinSitetime);
		} else if ("zaituzhongzhuan".equals(type)) {
			sql = monitorDAO.getZhongzhuanzaituSql("", "", customerid, branchid, page, startinSitetime, endinSitetime);
		} else if ("zaitutuihuo".equals(type)) {
			sql = monitorDAO.getTuihuozaituSql("", "", customerid, branchid, page, startinSitetime, endinSitetime);
		} else if ("weijiaokuan".equals(type)) {
			sql = monitorDAO.getWeijiaokuanSql("", "", customerid, branchid, page, startinSitetime, endinSitetime);
		} else if ("qiankuan".equals(type)) {
			sql = monitorDAO.getQiankuanSql("", "", customerid, branchid, page, startinSitetime, endinSitetime);
		} else if ("tuotou".equals(type)) {
			sql = monitorDAO.getTuotouSql("", "", customerid, branchid, page, startinSitetime, endinSitetime);
		} else if ("weituotou".equals(type)) {
			sql = monitorDAO.getWeituotouSql("", "", customerid, branchid, page, startinSitetime, endinSitetime);
		} else if ("qita".equals(type)) {
			sql = monitorDAO.getQitaSql("", "", customerid, branchid, page, startinSitetime, endinSitetime);
		}

		return sql;
	}

	// 站点监控查看分页总数
	public long getSiteDateCount(String crateStartdate, String crateEnddate, String customerid, long branchid, String type, String startinSitetime, String endinSitetime) {
		// 处理时间数据
		/*
		 * if(crateStartdate.length() == 0 && crateEnddate.length()>0){
		 * crateStartdate = crateEnddate; }else if(crateStartdate.length() > 0
		 * && crateEnddate.length()==0){ SimpleDateFormat sdf=new
		 * SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); crateEnddate =
		 * sdf.format(new Date()); }else if(crateStartdate.length() == 0 &&
		 * crateEnddate.length()==0){ SimpleDateFormat sdf=new
		 * SimpleDateFormat("yyyy-MM-dd"); crateEnddate = sdf.format(new
		 * Date())+ " 23:59:59"; crateStartdate =
		 * DateDayUtil.getDayCum(crateEnddate, 0)+" 00:00:00"; }
		 * if(startinSitetime.length() == 0 && endinSitetime.length()>0){
		 * startinSitetime = endinSitetime; }else if(startinSitetime.length() >
		 * 0 && endinSitetime.length()==0){ SimpleDateFormat sdf=new
		 * SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); endinSitetime =
		 * sdf.format(new Date()); }else if(startinSitetime.length() == 0 &&
		 * endinSitetime.length()==0){ SimpleDateFormat sdf=new
		 * SimpleDateFormat("yyyy-MM-dd"); endinSitetime = sdf.format(new
		 * Date())+ " 23:59:59"; startinSitetime =
		 * DateDayUtil.getDayCum(endinSitetime, 0)+" 00:00:00"; }
		 */
		long count = 0;
		if ("rkzs".equals(type)) {
			count = monitorDAO.getSiteCount(crateStartdate, crateEnddate, customerid, branchid, "", "");
		} else if ("weidaohuo".equals(type)) {
			count = monitorDAO.getWeidaohuoCount(crateStartdate, crateEnddate, customerid, branchid, "", "");
		} else if ("rukuweiling".equals(type)) {
			count = monitorDAO.getRukuweilingCount("", "", customerid, branchid, startinSitetime, endinSitetime);
		} else if ("youhuowudan".equals(type)) {
			count = monitorDAO.getYouhuowudanCount("", "", customerid, branchid, startinSitetime, endinSitetime);
		} else if ("youdanwuhuo".equals(type)) {
			count = monitorDAO.getYoudanwuhuoCount("", "", customerid, branchid, startinSitetime, endinSitetime);
		} else if ("yichangdan".equals(type)) {
			count = monitorDAO.getYichangdanCount("", "", customerid, branchid, startinSitetime, endinSitetime);
		} else if ("yilinghuo".equals(type)) {
			count = monitorDAO.getYilinghuoCount("", "", customerid, branchid, startinSitetime, endinSitetime);
		} else if ("yiliudan".equals(type)) {
			count = monitorDAO.getYiliudanCount("", "", customerid, branchid, startinSitetime, endinSitetime);
		} else if ("kuncuntuihuo".equals(type)) {
			count = monitorDAO.getTuihuokuncunCount("", "", customerid, branchid, startinSitetime, endinSitetime);
		} else if ("kucunzhiliu".equals(type)) {
			count = monitorDAO.getZhiliukucunCount("", "", customerid, branchid, startinSitetime, endinSitetime);
		} else if ("zaituzhongzhuan".equals(type)) {
			count = monitorDAO.getZhongzhuanzaituCount("", "", customerid, branchid, startinSitetime, endinSitetime);
		} else if ("zaitutuihuo".equals(type)) {
			count = monitorDAO.getTuihuozaituCount("", "", customerid, branchid, startinSitetime, endinSitetime);
		} else if ("weijiaokuan".equals(type)) {
			count = monitorDAO.getWeijiaokuanCount("", "", customerid, branchid, startinSitetime, endinSitetime);
		} else if ("qiankuan".equals(type)) {
			count = monitorDAO.getQiankuanCount("", "", customerid, branchid, startinSitetime, endinSitetime);
		} else if ("tuotou".equals(type)) {
			count = monitorDAO.getTuotouCount("", "", customerid, branchid, startinSitetime, endinSitetime);
		} else if ("weituotou".equals(type)) {
			count = monitorDAO.getWeituotouCount("", "", customerid, branchid, startinSitetime, endinSitetime);
		} else if ("qita".equals(type)) {
			count = monitorDAO.getQitaCount("", "", customerid, branchid, startinSitetime, endinSitetime);
		}

		return count;
	}

}

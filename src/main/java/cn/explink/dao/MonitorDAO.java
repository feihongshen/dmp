package cn.explink.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import cn.explink.controller.MonitorLogDTO;
import cn.explink.controller.MonitorLogSim;
import cn.explink.domain.CwbOrder;
import cn.explink.util.Page;


@Component
public class MonitorDAO {
	@Autowired
	CwbDAO cwbDAO;
	private final class MonitorlogDTOMapper implements RowMapper<MonitorLogDTO> {
		
		@Override
		public MonitorLogDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
			MonitorLogDTO menu = new MonitorLogDTO();
			menu.setCustomerid(rs.getLong("customerid"));
			menu.setWeidaohuoCountsum(rs.getLong("weidaohuoCountsum"));
			menu.setWeidaohuoCaramountsum(rs.getBigDecimal("weidaohuoCaramountsum"));
			menu.setTihuoCountsum(rs.getLong("tihuoCountsum"));
			menu.setTihuoCaramountsum(rs.getBigDecimal("tihuoCaramountsum"));
			menu.setRukuCountsum(rs.getLong("rukuCountsum"));
			menu.setRukuCaramountsum(rs.getBigDecimal("rukuCaramountsum"));
			menu.setChukuCountsum(rs.getLong("chukuCountsum"));
			menu.setChukuCaramountsum(rs.getBigDecimal("chukuCaramountsum"));
			menu.setDaozhanCountsum(rs.getLong("daozhanCountsum"));
			menu.setDaozhanCaramountsum(rs.getBigDecimal("daozhanCaramountsum"));
			menu.setZaizhanzijiCountsum(rs.getLong("zaizhanzijiCountsum"));
			menu.setZaizhanzijiCaramountsum(rs.getBigDecimal("zaizhanzijiCaramountsum"));
			menu.setYichuzhanCountsum(rs.getLong("yichuzhanCountsum"));
			menu.setYichuzhanCaramountsum(rs.getBigDecimal("yichuzhanCaramountsum"));
			menu.setZhongzhanrukuCountsum(rs.getLong("zhongzhanrukuCountsum"));
			menu.setZhongzhuanrukuCaramountsum(rs.getBigDecimal("zhongzhuanrukuCaramountsum"));
			menu.setTuihuorukuCountsum(rs.getLong("tuihuorukuCountsum"));
			menu.setTuihuorukuCaramountsum(rs.getBigDecimal("tuihuorukuCaramountsum"));
			menu.setTuigonghuoshangCountsum(rs.getLong("tuigonghuoshangCountsum"));
			menu.setTuigonghuoshangCaramountsum(rs.getBigDecimal("tuigonghuoshangCaramountsum"));
			menu.setTuikehuweishoukuanCountsum(rs.getLong("tuikehuweishoukuanCountsum"));
			menu.setTuikehuweishoukuanCaramountsum(rs.getBigDecimal("tuikehuweishoukuanCaramountsum"));
			return menu;
		}
	}
	private final class MonitorlogSimMapper implements RowMapper<MonitorLogSim> {
		@Override
		public MonitorLogSim mapRow(ResultSet rs, int rowNum) throws SQLException {
			MonitorLogSim menu = new MonitorLogSim();
			menu.setCustomerid(rs.getLong("customerid"));
			menu.setDcount(rs.getLong("dcount"));
			menu.setDsum(rs.getBigDecimal("dsum"));
			return menu;
		}
	}
	private final class StringTypeMapper implements RowMapper<String>{

		@Override
		public String mapRow(ResultSet rs, int rowNum) throws SQLException {
			// TODO Auto-generated method stub
			String cwbString=rs.getString(1);
			return cwbString;
		}
		
	}
	@Autowired
	JdbcTemplate jdbcTemplate;

	/**
	 * 订单生命周期监控 
	 * @param branchids 
	 * @return 
	 * @throws Exception
	 */
	public List<MonitorLogSim> getMonitorLogByBranchid(String branchids,String customerids,String wheresql) {
		String noteffectCwbsString=this.getEffectCwbsAdd(branchids, customerids, wheresql);
		StringBuffer sql = new StringBuffer("SELECT customerid,COUNT(1) as dcount, SUM(receivablefee+paybackfee) as dsum FROM  `express_ops_cwb_detail` WHERE  "+wheresql+" AND state=1  " + (customerids.length()>0? (" and customerid in("+customerids+") "):" ")+"  GROUP BY customerid");
		//receivablefee paybackfee代收货款应收金额和上门退应退金额的总和
		System.out.println("-- 生命周期监控:\n"+sql);
		List<MonitorLogSim> list = jdbcTemplate.query(sql.toString(), new MonitorlogSimMapper());
		return list;
	}
	public String getEffectCwbsAdd(String branchids,String customerids,String wheresql){
		StringBuffer buffer=new StringBuffer();
		String zhuiString="'";
		StringBuffer sql = new StringBuffer("SELECT cwb FROM  `express_ops_cwb_detail` WHERE  "+wheresql+" AND state=1  " + (customerids.length()>0? (" and customerid in("+customerids+") "):" ")+" and deliverystate=1 and newpaywayid='2'  GROUP BY customerid");
		List<String> calculateNumList=this.jdbcTemplate.query(sql.toString(), new StringTypeMapper());
		for (String string : calculateNumList) {
			buffer.append(zhuiString).append(string).append(zhuiString).append(",");
		}
		if (buffer.indexOf(",")>=0) {
			return buffer.substring(0, buffer.length()-1).toString();
		}else {
			return "''";
		}
	}
	/**
	 * 站点在站资金的情况(订单生命周期监控)
	 * @param branchids
	 * @param customerids
	 * @param wheresql
	 * @return
	 */
	public List<MonitorLogSim> getMonitorLogByBranchidWithZhandianzaiZhanZiJin(String branchids,String customerids,String wheresql) {
		String cwbsString=this.getExpectZhandianZaiZhanZiJinCwb(branchids,customerids,wheresql);
		StringBuffer sql = new StringBuffer("SELECT customerid,COUNT(1) as dcount, SUM(receivablefee+paybackfee) as dsum FROM  `express_ops_cwb_detail` WHERE  "+wheresql+" AND state=1  " + (customerids.length()>0? (" and customerid in("+customerids+") "):" ")+" AND cwb NOT IN("+cwbsString+") GROUP BY customerid");
		//receivablefee paybackfee代收货款应收金额和上门退应退金额的总和
		System.out.println("-- 生命周期监控:\n"+sql);
		List<MonitorLogSim> list = jdbcTemplate.query(sql.toString(), new MonitorlogSimMapper());
		return list;
	}
	public String getExpectZhandianZaiZhanZiJinCwb(String branchids,String customerids,String wheresql){
		String suffer="'";
		StringBuffer buffer=new StringBuffer();
		StringBuffer sql = new StringBuffer("SELECT cwb FROM  `express_ops_cwb_detail` WHERE  "+wheresql+" AND state=1  " + (customerids.length()>0? (" and customerid in("+customerids+") "):" ")+" AND deliverystate=1 AND newpaywayid='2' GROUP BY customerid");
		System.out.println("-- 生命周期监控--->>:\n"+sql);
		List<String> stringlist=jdbcTemplate.query(sql.toString(),new StringTypeMapper());
		for (String string : stringlist) {
			buffer.append(suffer).append(string).append(suffer).append(",");
		}
		if (buffer.indexOf(",")>=0) {
			return buffer.substring(0, buffer.length()-1).toString();
		}else {
			return "''";
		}
	}
	public List<MonitorLogSim> getMonitorLogByExpBranchid(String branchids,String customerids,String wheresql) {
		StringBuffer sql = new StringBuffer("SELECT customerid,COUNT(1) as dcount, SUM(receivablefee+paybackfee) as dsum FROM  `express_ops_operation_time` WHERE  "+wheresql+" " + (customerids.length()>0? (" and customerid in("+customerids+") "):" ")+" and branchid not in("+branchids+")  GROUP BY customerid");
		
		System.out.println("-- 生命周期监控:\n"+sql);
		List<MonitorLogSim> list = jdbcTemplate.query(sql.toString(), new MonitorlogSimMapper());
		return list;
	}

	public List<String> getMonitorLogByType(String flowordertypes ,String customerid,long page) {
		StringBuffer sql = new StringBuffer(
				"SELECT cwb  FROM `express_ops_operation_time` where  "+(customerid.length()>0?("customerid in("+customerid+")  and"):"")+"   flowordertype in("+flowordertypes+")" +
						" limit " + ((page - 1) * Page.ONE_PAGE_NUMBER) + " ," + Page.ONE_PAGE_NUMBER);

		List<String> list = jdbcTemplate.queryForList(sql.toString(), String.class);

		return list;
	}

	public List<String> getMonitorLogByType(String flowordertypes ,String branchids,long customerid,long page) {
		StringBuffer sql = new StringBuffer(
				"SELECT cwb  FROM `express_ops_operation_time` where "+(customerid>0?("customerid ="+customerid+"  and"):"")+"  flowordertype in("+flowordertypes+") and branchid in("+branchids+")" +
						" limit " + ((page - 1) * Page.ONE_PAGE_NUMBER) + " ," + Page.ONE_PAGE_NUMBER);

		List<String> list = jdbcTemplate.queryForList(sql.toString(), String.class);

		return list;
	}
	public List<String> getMonitorLogByTypeAndNotIn(String flowordertypes ,String branchids,String customerid,long page) {
		String effectCwbs=this.getEffectCwbs(flowordertypes, branchids, customerid, page);
		StringBuffer sql = new StringBuffer(
				"SELECT cwb  FROM `express_ops_operation_time` where  "+(customerid.length()>0?("customerid in("+customerid+")  and"):"")+"   flowordertype in("+flowordertypes+") and branchid not in("+branchids+")" );
		if (page!=-9) {
			sql.append(" limit " + ((page - 1) * Page.ONE_PAGE_NUMBER) + " ," + Page.ONE_PAGE_NUMBER);
		}

		List<String> list = jdbcTemplate.queryForList(sql.toString(), String.class);

		return list;
	}
	public String getEffectCwbs(String flowordertypes ,String branchids,String customerid,long page){
		StringBuffer buffer=new StringBuffer();
		String zhuiString="'";
		StringBuffer sql = new StringBuffer(
				"SELECT cwb  FROM `express_ops_operation_time` where  "+(customerid.length()>0?("customerid in("+customerid+")  and"):"")+"   flowordertype in("+flowordertypes+") and branchid not in("+branchids+")"+" and deliverystate=1" );
		if (page!=-9) {
			sql.append(" limit " + ((page - 1) * Page.ONE_PAGE_NUMBER) + " ," + Page.ONE_PAGE_NUMBER);
		}
		List<String> calculateNumList=this.jdbcTemplate.query(sql.toString(), new StringTypeMapper());
		for (String string : calculateNumList) {
			CwbOrder cwbOrder=cwbDAO.getCwbByCwb(string);
			if (cwbOrder!=null) {
				String newpaywayId=cwbOrder.getNewpaywayid();
				if (newpaywayId.equals("2")) {
					continue;
				}
			}
			buffer.append(zhuiString).append(string).append(zhuiString).append(",");
		}
		if (buffer.indexOf(",")>=0) {
			return buffer.substring(0, buffer.length()-1).toString();
		}else {
			return "''";
		}
	}
	public List<String> getMonitorLogByTypeAndNotIn(String flowordertypes ,String branchids,String customerid) {
		StringBuffer sql = new StringBuffer(
				"SELECT cwb  FROM `express_ops_operation_time` where  "+(customerid.length()>0?("customerid in("+customerid+")  and"):"")+"   flowordertype in("+flowordertypes+") and branchid not in("+branchids+")");
		
		List<String> list = jdbcTemplate.queryForList(sql.toString(), String.class);
		
		return list;
	}
	
	//==============条数===========
	public long getMonitorLogByTypeCount(String flowordertypes ,String customerid) {
		StringBuffer sql = new StringBuffer(
				"SELECT count(1)  FROM `express_ops_operation_time` where  "+(customerid.length()>0?("customerid in("+customerid+")  and"):"")+"    flowordertype in("+flowordertypes+")");
				long count = jdbcTemplate.queryForLong(sql.toString());
				return count;
	}

	public long getMonitorLogByTypeCount(String flowordertypes ,String branchids,long customerid) {
		StringBuffer sql = new StringBuffer(
				"SELECT count(1)  FROM `express_ops_operation_time` where  "+(customerid>0?("customerid ="+customerid+"  and"):"")+"   flowordertype in("+flowordertypes+") and branchid in("+branchids+")");
		long count = jdbcTemplate.queryForLong(sql.toString());

		return count;
	}
	public long getMonitorLogByTypeAndNotInCount(String flowordertypes ,String branchids,String customerid) {
		String noteffectcwbsString=this.getEffectCwbs(flowordertypes, branchids, customerid, -9);
		StringBuffer sql = new StringBuffer(
				"SELECT count(1)  FROM `express_ops_operation_time` where  "+(customerid.length()>0?("customerid in("+customerid+")  and"):"")+"    flowordertype in("+flowordertypes+") and branchid not in("+branchids+") ") ;

		long count = jdbcTemplate.queryForLong(sql.toString());

		return count;
	}

	
	///============导出=============
	
	
	
	public String getMonitorLogByTypeSql(String wheresql ,String customerids) {
		

		StringBuffer sql = new StringBuffer("SELECT * FROM  `express_ops_cwb_detail` WHERE  "+wheresql+" AND state=1  " + (customerids.length()>0? (" and customerid in("+customerids+") "):" ")+"");

		System.out.println("-- 生命周期监控:\n"+sql);
		
		return sql.toString();
	}

	public String getMonitorLogByTypeSql(String flowordertypes ,String branchids,String customerid) {
		String sql = "SELECT de.*  FROM `express_ops_operation_time` as ot  left join express_ops_cwb_detail as de on ot.cwb=de.cwb where "+(customerid.length()>0?("ot.customerid ="+customerid+"  and"):"")+" ot.flowordertype in("+flowordertypes+") and ot.branchid in("+branchids+")  and de.state=1  " ;


		return sql;
	}
	public String getMonitorLogByTypeAndNotInSql(String flowordertypes ,String branchids,String customerid) {
		String sql = "SELECT de.*  FROM `express_ops_operation_time` as ot  left join express_ops_cwb_detail as de on ot.cwb=de.cwb where "+(customerid.length()>0?("ot.customerid ="+customerid+"  and"):"")+" ot.flowordertype in("+flowordertypes+") and ot.branchid not in("+branchids+")  and de.state=1  ";


		return sql;
	}

}

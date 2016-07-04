package cn.explink.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import com.pjbest.splitting.aspect.DataSource;
import com.pjbest.splitting.routing.DatabaseType;

import cn.explink.controller.MonitorKucunDTO;
import cn.explink.controller.MonitorKucunSim;
import cn.explink.controller.MonitorLogSim;
import cn.explink.util.Page;


@Component
public class MonitorKucunDAO {

	private final class MonitorKucunDTOMapper implements RowMapper<MonitorKucunDTO> {
		@Override
		public MonitorKucunDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
			MonitorKucunDTO menu = new MonitorKucunDTO();
			menu.setBranchid(rs.getLong("branchid"));
			menu.setKucunCountsum(rs.getLong("kucunCountsum"));
			menu.setKucunCaramountsum(rs.getBigDecimal("kucunCaramountsum"));
			menu.setYichukuzaituCountsum(rs.getLong("yichukuzaituCountsum"));
			menu.setYichukuzaituCaramountsum(rs.getBigDecimal("yichukuzaituCaramountsum"));
			menu.setWeirukuCountsum(rs.getLong("weirukuCountsum"));
			menu.setWeirukuCaramountsum(rs.getBigDecimal("weirukuCaramountsum"));
			menu.setYituikehuweifankuanCountsum(rs.getLong("yituikehuweifankuanCountsum"));
			menu.setYituikehuweifankuanCaramountsum(rs.getBigDecimal("yituikehuweifankuanCaramountsum"));
			return menu;
		}
	}

	private final class MonitorKucunSimMapper implements RowMapper<MonitorKucunSim> {
		@Override
		public MonitorKucunSim mapRow(ResultSet rs, int rowNum) throws SQLException {
			MonitorKucunSim menu = new MonitorKucunSim();
			menu.setBranchid(rs.getLong("branchid"));
			menu.setDcount(rs.getLong("dcount"));
			menu.setDsum(rs.getBigDecimal("dsum"));
			return menu;
		}
	}
	@Autowired
	JdbcTemplate jdbcTemplate;

	/**
	 * 存货监控 
	 * @param branchids 
	 * @return 
	 * @throws Exception
	 */
	@DataSource(DatabaseType.REPLICA)
	public List<MonitorKucunSim> getMonitorLogByBranchid(String branchids ,String wheresql,String branchname) {
		StringBuffer sql = new StringBuffer("SELECT "+branchname+" as op.branchid,COUNT(1) as dcount, SUM(op.receivablefee+op.paybackfee) as dsum FROM  `express_ops_cwb_detail` op left join express_ops_cwb_detail as de on op.cwb=de.cwb   WHERE de.state=1 and  "+wheresql+" GROUP BY "+branchname+"");

		System.out.println("-- 生命周期监控:\n"+sql);
		List<MonitorKucunSim> list = jdbcTemplate.query(sql.toString(), new MonitorKucunSimMapper());
		return list;
		
		
	}
	/**
	 * 存货监控 
	 * @param branchids 
	 * @return 
	 * @throws Exception
	 */
	@DataSource(DatabaseType.REPLICA)
	public List<MonitorKucunSim> getMonitorLogByBranchid(String branchids ,String wheresql) {
		
		
//		StringBuffer sql = new StringBuffer(
//				"SELECT branchid," +
//						"SUM(CASE WHEN (flowordertype IN( 4,12,15,7,8,9,35) OR (flowordertype =36 AND deliverystate NOT IN(1,2,3)) ) THEN 1 ELSE 0 END) AS kucunCountsum," +
//						"SUM(CASE WHEN (flowordertype IN( 4,12,15,7,8,9,35) OR (flowordertype =36 AND deliverystate NOT IN(1,2,3)) ) THEN receivablefee - paybackfee ELSE 0 END) AS kucunCaramountsum," +
//						"SUM(CASE WHEN (flowordertype IN( 6,14,40,27) ) THEN 1 ELSE 0 END) AS yichukuzaituCountsum," +
//						"SUM(CASE WHEN (flowordertype IN( 6,14,40,27) ) THEN receivablefee+paybackfee ELSE 0 END) AS yichukuzaituCaramountsum," +
//						"SUM(CASE WHEN (flowordertype = 1 ) THEN 1 ELSE 0 END) AS weirukuCountsum," +
//						"SUM(CASE WHEN (flowordertype = 1 ) THEN receivablefee+paybackfee ELSE 0 END) AS weirukuCaramountsum," +
//						"SUM(0) AS yituikehuweifankuanCountsum," +
//						"SUM(0) AS yituikehuweifankuanCaramountsum" +
//						" FROM `express_ops_operation_time` WHERE branchid IN("+branchids+") GROUP BY branchid");
//		
//		
		
		StringBuffer sql = new StringBuffer("SELECT ot.branchid as branchid,COUNT(1) as dcount, SUM(ot.receivablefee+ot.paybackfee) as dsum FROM  `express_ops_operation_time` ot LEFT JOIN express_ops_cwb_detail AS de ON ot.cwb = de.cwb WHERE  "+wheresql+" and ot.branchid IN("+branchids+") and de.state=1  GROUP BY branchid");

		System.out.println("-- 生命周期监控:\n"+sql);
		
		List<MonitorKucunSim> list = jdbcTemplate.query(sql.toString(), new MonitorKucunSimMapper());
		
		return list;
	}

	@DataSource(DatabaseType.REPLICA)
	public List<String> getMonitorLogByType(String wheresql ,String branchid,long page,String branchids) {
		
		StringBuffer sql = new StringBuffer("SELECT op.cwb FROM  `express_ops_operation_time` op LEFT JOIN express_ops_cwb_detail AS de ON op.cwb = de.cwb WHERE  de.state=1 and "+wheresql+" and "+(branchid.length()>0?("op.branchid in("+branchid+") "):" op.branchid IN("+branchids+") ")+"  " +
				" limit " + ((page - 1) * Page.ONE_PAGE_NUMBER) + " ," + Page.ONE_PAGE_NUMBER);

		List<String> list = jdbcTemplate.queryForList(sql.toString(), String.class);

		return list;
	}
	
	@DataSource(DatabaseType.REPLICA)
	public List<String> getMonitorLogByTypeNoPage(String wheresql ,String branchid,String branchids) {
		
		StringBuffer sql = new StringBuffer("SELECT op.cwb FROM  `express_ops_operation_time` op left join express_ops_cwb_detail as de on op.cwb=de.cwb  WHERE  de.state=1 and "+wheresql+" and "+(branchid.length()>0?("op.branchid in("+branchid+") "):" op.branchid IN("+branchids+") ")+"  " +
				"");
		
		List<String> list = jdbcTemplate.queryForList(sql.toString(), String.class);
		
		return list;
	}
	
	@DataSource(DatabaseType.REPLICA)
	public List<String> getMonitorKucunByType(String flowordertypes ,String branchid,long page,String branchids) {
		StringBuffer sql = new StringBuffer(
				"SELECT op.cwb  FROM `express_ops_operation_time` op LEFT JOIN express_ops_cwb_detail AS de on op.cwb=de.cwb where  de.state=1 and "+(branchid.length()>0?("op.branchid in("+branchid+")  and"):" op.branchid IN("+branchids+") and ")+"  (op.flowordertype IN( 4,12,15,7,8,9,35) OR (op.flowordertype =36 AND op.deliverystate NOT IN(1,2,3)))" +
						" limit " + ((page - 1) * Page.ONE_PAGE_NUMBER) + " ," + Page.ONE_PAGE_NUMBER);
		
		List<String> list = jdbcTemplate.queryForList(sql.toString(), String.class);
		
		return list;
	}
	
	@DataSource(DatabaseType.REPLICA)
	public List<String> getMonitorKucunByTypeNoPage(String flowordertypes ,String branchid,String branchids) {
		StringBuffer sql = new StringBuffer(
				"SELECT op.cwb  FROM `express_ops_operation_time` op left join express_ops_cwb_detail as de on op.cwb=de.cwb  where  de.state=1 and "+(branchid.length()>0?("op.branchid in("+branchid+")  and"):" op.branchid IN("+branchids+") and ")+"  (op.flowordertype IN( 4,12,15,7,8,9,35) OR (op.flowordertype =36 AND op.deliverystate NOT IN(1,2,3)))" +
						" ");
		
		List<String> list = jdbcTemplate.queryForList(sql.toString(), String.class);
		
		return list;
	}
	
	@DataSource(DatabaseType.REPLICA)
	public List<String> getMonitorKucunByTypeAll(String flowordertypes ,long branchid,long page,String branchids) {
		StringBuffer sql = new StringBuffer(
				"SELECT op.cwb  FROM `express_ops_operation_time` op left join express_ops_cwb_detail as de on op.cwb=de.cwb  where  de.state=1 and "+(branchid>0?("op.branchid ="+branchid+"  and"):" op.branchid IN("+branchids+") and ")
				+" ( (op.flowordertype IN( 4,12,15,7,8,9,35) OR (op.flowordertype =36 AND op.deliverystate NOT IN(1,2,3))) or op.flowordertype in(1,6,14,40,27))" +
						" limit " + ((page - 1) * Page.ONE_PAGE_NUMBER) + " ," + Page.ONE_PAGE_NUMBER);
		
		List<String> list = jdbcTemplate.queryForList(sql.toString(), String.class);
		
		return list;
	}

	
	
	//==============条数===========
	@DataSource(DatabaseType.REPLICA)
	public long getMonitorLogByTypeCount(String flowordertypes ,String branchid,String branchids) {
		StringBuffer sql = new StringBuffer(
				"SELECT count(1)  FROM `express_ops_operation_time` op left join express_ops_cwb_detail as de on op.cwb=de.cwb  where de.state=1 and  "+(branchid.length()>0?("op.branchid in("+branchid+")  and"):" op.branchid IN("+branchids+") and ")+"    op.flowordertype in("+flowordertypes+")");
				long count = jdbcTemplate.queryForLong(sql.toString());
				return count;
	}
	
	@DataSource(DatabaseType.REPLICA)
	public long getMonitorKucunByTypeCount(String flowordertypes ,String branchid,String branchids) {
		StringBuffer sql = new StringBuffer(
				"SELECT count(1)  FROM `express_ops_operation_time` op left join express_ops_cwb_detail as de on op.cwb=de.cwb   where de.state=1 and  "+(branchid.length()>0?("op.branchid in("+branchid+")  and"):" op.branchid IN("+branchids+") and ")+ "(op.flowordertype IN( 4,12,15,7,8,9,35) OR (op.flowordertype =36 AND op.deliverystate NOT IN(1,2,3)))" );
		long count = jdbcTemplate.queryForLong(sql.toString());
		return count;
	}
	
	@DataSource(DatabaseType.REPLICA)
	public long getMonitorKucunByTypeCountAll(String flowordertypes ,long branchid,String branchids) {
		StringBuffer sql = new StringBuffer(
				"SELECT count(1)  FROM `express_ops_operation_time` op left join express_ops_cwb_detail as de on op.cwb=de.cwb  where de.state=1 and  "+(branchid>0?("op.branchid ="+branchid+"  and"):" op.branchid IN("+branchids+") and ")+
				"( (op.flowordertype IN( 4,12,15,7,8,9,35) OR (op.flowordertype =36 AND op.deliverystate NOT IN(1,2,3))) or op.flowordertype in(1,6,14,40,27))" );
		long count = jdbcTemplate.queryForLong(sql.toString());
		return count;
	}

	

	
	///============导出=============
	public String getMonitorLogByTypeSql(String flowordertypes ,String branchid,String branchids) {
		
		String sql = "SELECT de.*  FROM `express_ops_operation_time` as ot  left join express_ops_cwb_detail as de on ot.cwb=de.cwb where "+(branchid.length()>0?("ot.branchid in("+branchid+")  and"):" ot.branchid IN("+branchids+") and ")+"   ot.flowordertype in("+flowordertypes+")  and de.state=1  ";


		return sql;
	}
	public String getMonitorKucunByTypeSql(String flowordertypes ,String branchid,String branchids) {
		
		String sql = "SELECT de.*  FROM `express_ops_operation_time` as ot  left join express_ops_cwb_detail as de on ot.cwb=de.cwb where "+(branchid.length()>0?("ot.branchid in("+branchid+")  and"):" ot.branchid IN("+branchids+") and ")
				+"  (ot.flowordertype IN( 4,12,15,7,8,9,35) OR (ot.flowordertype =36 AND ot.deliverystate NOT IN(1,2,3)))  and de.state=1  ";
		
		
		return sql;
	}
	public String getMonitorKucunAllByTypeSql(String flowordertypes ,long branchid,String branchids) {
		
		String sql = "SELECT de.*  FROM `express_ops_operation_time` as ot  left join express_ops_cwb_detail as de on ot.cwb=de.cwb where "+(branchid>0?("ot.branchid ="+branchid+"  and"):" ot.branchid IN("+branchids+") and ")
				+" ( (ot.flowordertype IN( 4,12,15,7,8,9,35) OR (ot.flowordertype =36 AND ot.deliverystate NOT IN(1,2,3))) or ot.flowordertype in(1,6,14,40,27)) and de.state=1  ";
		
		
		return sql;
	}

	

}

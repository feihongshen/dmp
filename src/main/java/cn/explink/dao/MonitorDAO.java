package cn.explink.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import cn.explink.controller.MonitorLogDTO;
import cn.explink.util.Page;


@Component
public class MonitorDAO {

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

	@Autowired
	JdbcTemplate jdbcTemplate;

	/**
	 * 订单生命周期监控 
	 * @param branchids 
	 * @return 
	 * @throws Exception
	 */
	public List<MonitorLogDTO> getMonitorLogByBranchid(String branchids,String customerids) {
		StringBuffer sql = new StringBuffer(
				"SELECT customerid," +
						"SUM(CASE WHEN (flowordertype = 1 ) THEN 1 ELSE 0 END) AS weidaohuoCountsum," +
						"SUM(CASE WHEN (flowordertype = 1 ) THEN receivablefee+paybackfee ELSE 0 END) AS weidaohuoCaramountsum," +
						"SUM(CASE WHEN (flowordertype = 2 ) THEN 1 ELSE 0 END) AS tihuoCountsum," +
						"SUM(CASE WHEN (flowordertype = 2 ) THEN receivablefee+paybackfee ELSE 0 END) AS tihuoCaramountsum," +
						"SUM(CASE WHEN (flowordertype = 4 AND branchid IN("+branchids+") ) THEN 1 ELSE 0 END) AS rukuCountsum," +
						"SUM(CASE WHEN (flowordertype = 4 AND branchid IN("+branchids+") ) THEN receivablefee+paybackfee ELSE 0 END) AS rukuCaramountsum," +
						"SUM(CASE WHEN (flowordertype = 6 AND branchid IN("+branchids+") ) THEN 1 ELSE 0 END) AS chukuCountsum," +
						"SUM(CASE WHEN (flowordertype = 6 AND branchid IN("+branchids+") ) THEN receivablefee+paybackfee ELSE 0 END) AS chukuCaramountsum," +
						"SUM(CASE WHEN (flowordertype IN(7,8,9,35,36) ) THEN 1 ELSE 0 END) AS daozhanCountsum," +
						"SUM(CASE WHEN (flowordertype IN(7,8,9,35,36) ) THEN receivablefee+paybackfee ELSE 0 END) AS daozhanCaramountsum," +
						"SUM(0) AS zaizhanzijiCountsum," +
						"SUM(0) AS zaizhanzijiCaramountsum," +
						"SUM(CASE WHEN (flowordertype IN(6,14,40) AND branchid NOT IN("+branchids+") ) THEN 1 ELSE 0 END) AS yichuzhanCountsum," +
						"SUM(CASE WHEN (flowordertype IN(6,14,40) AND branchid NOT IN("+branchids+") ) THEN receivablefee+paybackfee ELSE 0 END) AS yichuzhanCaramountsum," +
						"SUM(CASE WHEN (flowordertype = 12 ) THEN 1 ELSE 0 END) AS zhongzhanrukuCountsum," +
						"SUM(CASE WHEN (flowordertype = 12 ) THEN receivablefee+paybackfee ELSE 0 END) AS zhongzhuanrukuCaramountsum," +
						"SUM(CASE WHEN (flowordertype = 15 ) THEN 1 ELSE 0 END) AS tuihuorukuCountsum," +
						"SUM(CASE WHEN (flowordertype = 15 ) THEN receivablefee+paybackfee ELSE 0 END) AS tuihuorukuCaramountsum," +
						"SUM(CASE WHEN (flowordertype = 27 ) THEN 1 ELSE 0 END) AS tuigonghuoshangCountsum," +
						"SUM(CASE WHEN (flowordertype = 27 ) THEN receivablefee+paybackfee ELSE 0 END) AS tuigonghuoshangCaramountsum," +
						"SUM(0) AS tuikehuweishoukuanCountsum," +
						"SUM(0) AS tuikehuweishoukuanCaramountsum" +
						" FROM `express_ops_operation_time` " + (customerids.length()>0? (" where customerid in("+customerids+") "):"")+"GROUP BY customerid");

		List<MonitorLogDTO> list = jdbcTemplate.query(sql.toString(), new MonitorlogDTOMapper());

		return list;
	}

	public List<String> getMonitorLogByType(String flowordertypes ,long customerid,long page) {
		StringBuffer sql = new StringBuffer(
				"SELECT cwb  FROM `express_ops_operation_time` where  "+(customerid>0?("customerid ="+customerid+"  and"):"")+"   flowordertype in("+flowordertypes+")" +
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
	public List<String> getMonitorLogByTypeAndNotIn(String flowordertypes ,String branchids,long customerid,long page) {
		StringBuffer sql = new StringBuffer(
				"SELECT cwb  FROM `express_ops_operation_time` where  "+(customerid>0?("customerid ="+customerid+"  and"):"")+"   flowordertype in("+flowordertypes+") and branchid not in("+branchids+")" +
						" limit " + ((page - 1) * Page.ONE_PAGE_NUMBER) + " ," + Page.ONE_PAGE_NUMBER);

		List<String> list = jdbcTemplate.queryForList(sql.toString(), String.class);

		return list;
	}
	
	//==============条数===========
	public long getMonitorLogByTypeCount(String flowordertypes ,long customerid) {
		StringBuffer sql = new StringBuffer(
				"SELECT count(1)  FROM `express_ops_operation_time` where  "+(customerid>0?("customerid ="+customerid+"  and"):"")+"    flowordertype in("+flowordertypes+")");
				long count = jdbcTemplate.queryForLong(sql.toString());
				return count;
	}

	public long getMonitorLogByTypeCount(String flowordertypes ,String branchids,long customerid) {
		StringBuffer sql = new StringBuffer(
				"SELECT count(1)  FROM `express_ops_operation_time` where  "+(customerid>0?("customerid ="+customerid+"  and"):"")+"   flowordertype in("+flowordertypes+") and branchid in("+branchids+")");
		long count = jdbcTemplate.queryForLong(sql.toString());

		return count;
	}
	public long getMonitorLogByTypeAndNotInCount(String flowordertypes ,String branchids,long customerid) {
		StringBuffer sql = new StringBuffer(
				"SELECT count(1)  FROM `express_ops_operation_time` where  "+(customerid>0?("customerid ="+customerid+"  and"):"")+"    flowordertype in("+flowordertypes+") and branchid not in("+branchids+")") ;

		long count = jdbcTemplate.queryForLong(sql.toString());

		return count;
	}

	
	///============导出=============
	public String getMonitorLogByTypeSql(String flowordertypes ,long customerid) {
		
		String sql = "SELECT de.*  FROM `express_ops_operation_time` as ot  left join express_ops_cwb_detail as de on ot.cwb=de.cwb where "+(customerid>0?("ot.customerid ="+customerid+"  and"):"")+"   ot.flowordertype in("+flowordertypes+")  and de.state=1  ";


		return sql;
	}

	public String getMonitorLogByTypeSql(String flowordertypes ,String branchids,long customerid) {
		String sql = "SELECT de.*  FROM `express_ops_operation_time` as ot  left join express_ops_cwb_detail as de on ot.cwb=de.cwb where "+(customerid>0?("ot.customerid ="+customerid+"  and"):"")+" ot.flowordertype in("+flowordertypes+") and ot.branchid in("+branchids+")  and de.state=1  " ;


		return sql;
	}
	public String getMonitorLogByTypeAndNotInSql(String flowordertypes ,String branchids,long customerid) {
		String sql = "SELECT de.*  FROM `express_ops_operation_time` as ot  left join express_ops_cwb_detail as de on ot.cwb=de.cwb where "+(customerid>0?("ot.customerid ="+customerid+"  and"):"")+" ot.flowordertype in("+flowordertypes+") and ot.branchid not in("+branchids+")  and de.state=1  ";


		return sql;
	}

}

package cn.explink.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import cn.explink.domain.TuihuoSiteTodaylog;
import cn.explink.util.Page;

@Component
public class LogTodayByTuihuoDAO {
	private final class TuihuoSiteTodaylogRowMapper implements RowMapper<TuihuoSiteTodaylog> {
		@Override
		public TuihuoSiteTodaylog mapRow(ResultSet rs, int rowNum) throws SQLException {
			TuihuoSiteTodaylog tuihuoLog = new TuihuoSiteTodaylog();
			tuihuoLog.setId(rs.getLong("id"));
			tuihuoLog.setUserid(rs.getLong("userid"));
			tuihuoLog.setTuihuoid(rs.getLong("tuihuoid"));
			tuihuoLog.setCustomerid(rs.getLong("customerid"));
			tuihuoLog.setCteatetime(rs.getString("cteatetime"));
			tuihuoLog.setStarttime(rs.getString("starttime"));
			tuihuoLog.setEndtime(rs.getString("endtime"));
			tuihuoLog.setZhandiantuihuochukuzaitu(rs.getLong("zhandiantuihuochukuzaitu"));
			tuihuoLog.setZhandianyingtui(rs.getLong("zhandianyingtui"));
			tuihuoLog.setTuigonghuoshangchuku(rs.getLong("tuigonghuoshangchuku"));
			tuihuoLog.setTuihuozhanruku(rs.getLong("tuihuozhanruku"));
			tuihuoLog.setTuihuozhantuihuochukuzaitu(rs.getLong("tuihuozhantuihuochukuzaitu"));
			tuihuoLog.setGonghuoshangjushoufanku(rs.getLong("gonghuoshangjushoufanku"));
			tuihuoLog.setGonghuoshangshouhuo(rs.getLong("gonghuoshangshouhuo"));
			return tuihuoLog;
		}
	}

	private final class WareHouseCwbMapper implements RowMapper<String> {
		@Override
		public String mapRow(ResultSet rs, int rowNum) throws SQLException {
			String cwb = rs.getString("cwb");
			return cwb;
		}
	}

	@Autowired
	private JdbcTemplate jdbcTemplate;

	// ==============库房日志统计==begin=============
	/**
	 * 站点应退数据
	 * 
	 * @param branchid
	 * @param flowordertype
	 *            = 36
	 * @param deliverystate
	 *            = 4
	 * @return
	 */
	public long getZhanDianYingtuiCount(long flowordertype, String deliverystates) {
		String sql = "SELECT COUNT(1) FROM  express_ops_cwb_detail WHERE " + " flowordertype =? AND deliverystate in(" + deliverystates + ") AND state=1";
		return jdbcTemplate.queryForLong(sql, flowordertype);
	}

	/**
	 * 退货站退货出站统计数量
	 * 
	 * @param flowordertype
	 * @param credate
	 * @return
	 */
	public long getFlowTypeCountByTuihuozhan(long flowordertype, String branchids, String startTime) {
		String sql = "SELECT COUNT(1) FROM  express_ops_order_flow WHERE  flowordertype =? AND branchid in(" + branchids + ") AND isnow=1 AND credate>?";
		return jdbcTemplate.queryForLong(sql, flowordertype, startTime);
	}

	/**
	 * 退货站退货出站获取订单号
	 * 
	 * @param flowordertype
	 * @param deliverystate
	 * @param page
	 * @return
	 */
	public List<String> getFlowTypeCwbsByTuihuozhan(long flowordertype, String branchids, String startTime, long page) {
		String sql = "SELECT cwb  FROM  express_ops_order_flow WHERE  " + "flowordertype =?  AND branchid in(" + branchids + ")  AND isnow=1 AND credate>? LIMIT ?,?";
		return jdbcTemplate.query(sql, new WareHouseCwbMapper(), flowordertype, startTime, ((page - 1) * Page.ONE_PAGE_NUMBER), Page.ONE_PAGE_NUMBER);
	}

	/**
	 * 导出使用
	 * 
	 * @param flowordertype
	 * @param branchid
	 * @param startTime
	 * @return
	 */
	public List<String> getFlowTypeCwbsByTuihuozhan(long flowordertype, String branchids, String startTime) {
		String sql = "SELECT cwb  FROM  express_ops_order_flow WHERE  " + "flowordertype =?  AND branchid in(" + branchids + ")  AND isnow=1 AND credate>? ";
		return jdbcTemplate.query(sql, new WareHouseCwbMapper(), flowordertype, startTime);
	}

	/**
	 * 按状态和时间统计数量
	 * 
	 * @param flowordertype
	 * @param credate
	 * @return
	 */
	public long getFlowTypeCount(long flowordertype, String startTime) {
		String sql = "SELECT COUNT(1) FROM  express_ops_order_flow WHERE  flowordertype =?  AND isnow=1 AND credate>?";
		return jdbcTemplate.queryForLong(sql, flowordertype, startTime);
	}

	/**
	 * 获取订单号
	 * 
	 * @param flowordertype
	 * @param deliverystate
	 * @param page
	 * @return
	 */
	public List<String> getFlowTypeCwbs(long flowordertype, String startTime, long page) {
		String sql = "SELECT cwb  FROM  express_ops_order_flow WHERE  " + "flowordertype =?  AND isnow=1 AND credate>? LIMIT ?,?";
		return jdbcTemplate.query(sql, new WareHouseCwbMapper(), flowordertype, startTime, ((page - 1) * Page.ONE_PAGE_NUMBER), Page.ONE_PAGE_NUMBER);
	}

	/**
	 * 导出使用
	 * 
	 * @param flowordertype
	 * @param startTime
	 * @return
	 */
	public List<String> getFlowTypeCwbs(long flowordertype, String startTime) {
		String sql = "SELECT cwb  FROM  express_ops_order_flow WHERE  " + "flowordertype =?  AND isnow=1 AND credate>? ";
		return jdbcTemplate.query(sql, new WareHouseCwbMapper(), flowordertype, startTime);
	}

	// ==============库房日志统计==end=============
	/**
	 * 获取最新一个日志
	 * 
	 * @param warehouseid
	 * @return
	 */
	public TuihuoSiteTodaylog getLastBranchTodayLogByWarehouseid(long tuihuoid) {
		String sql = "SELECT * FROM express_ops_tuihuo_todaylog WHERE  tuihuoid=? ORDER BY cteatetime DESC LIMIT 0,1";
		try {
			return jdbcTemplate.queryForObject(sql, new TuihuoSiteTodaylogRowMapper(), tuihuoid);
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	public List<TuihuoSiteTodaylog> getHistoryLog(long tuihuoid, String startTime, String endTime) {
		String sql = "SELECT id,userid,cteatetime,starttime,endtime,tuihuoid,customerid,"
				+ "zhandianyingtui,zhandiantuihuochukuzaitu,tuihuozhanruku,sum(tuihuozhantuihuochukuzaitu) as tuihuozhantuihuochukuzaitu"
				+ ",tuigonghuoshangchuku,gonghuoshangshouhuo,gonghuoshangjushoufanku FROM express_ops_tuihuo_todaylog WHERE   cteatetime>=? and cteatetime<=? GROUP BY endtime";
		try {
			return jdbcTemplate.query(sql, new TuihuoSiteTodaylogRowMapper(), startTime, endTime);
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	public List<TuihuoSiteTodaylog> getLogByid(long id) {

		String sql = " SELECT * FROM  express_ops_tuihuo_todaylog where " + " id=?  ";
		try {
			return jdbcTemplate.query(sql, new TuihuoSiteTodaylogRowMapper(), id);
		} catch (DataAccessException e) {
			return null;
		}

	}

	public long creBranchTodayLog(final TuihuoSiteTodaylog wTodaylog) {

		KeyHolder key = new GeneratedKeyHolder();
		jdbcTemplate.update(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(java.sql.Connection con) throws SQLException {
				PreparedStatement ps = null;
				ps = con.prepareStatement("INSERT INTO express_ops_tuihuo_todaylog" + "(userid,cteatetime,starttime,endtime,tuihuoid,customerid,"
						+ "zhandianyingtui,zhandiantuihuochukuzaitu,tuihuozhanruku,tuihuozhantuihuochukuzaitu" + ",tuigonghuoshangchuku,gonghuoshangshouhuo,gonghuoshangjushoufanku) " + "VALUES "
						+ "(?,?,?,?,?,?,?,?,?,?," + " ?,?,?)", new String[] { "id" });
				ps.setLong(1, wTodaylog.getUserid());
				ps.setString(2, wTodaylog.getCteatetime());
				ps.setString(3, wTodaylog.getStarttime());
				ps.setString(4, wTodaylog.getEndtime());
				ps.setLong(5, wTodaylog.getTuihuoid());
				ps.setLong(6, wTodaylog.getCustomerid());
				ps.setLong(7, wTodaylog.getZhandianyingtui());
				ps.setLong(8, wTodaylog.getZhandiantuihuochukuzaitu());
				ps.setLong(9, wTodaylog.getTuihuozhanruku());
				ps.setLong(10, wTodaylog.getTuihuozhantuihuochukuzaitu());
				ps.setLong(11, wTodaylog.getTuigonghuoshangchuku());
				ps.setLong(12, wTodaylog.getGonghuoshangshouhuo());
				ps.setLong(13, wTodaylog.getGonghuoshangjushoufanku());

				return ps;
			}
		}, key);
		return key.getKey().longValue();
	}

}

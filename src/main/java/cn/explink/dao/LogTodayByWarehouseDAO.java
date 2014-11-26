package cn.explink.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import cn.explink.domain.WarehouseTodaylog;
import cn.explink.util.Page;

@Component
public class LogTodayByWarehouseDAO {
	private final class WarehouseTodaylogRowMapper implements RowMapper<WarehouseTodaylog> {
		@Override
		public WarehouseTodaylog mapRow(ResultSet rs, int rowNum) throws SQLException {
			WarehouseTodaylog warehouseTodaylog = new WarehouseTodaylog();
			warehouseTodaylog.setId(rs.getLong("id"));
			warehouseTodaylog.setUserid(rs.getLong("userid"));
			warehouseTodaylog.setWarehouseid(rs.getLong("warehouseid"));
			warehouseTodaylog.setCustomerid(rs.getLong("customerid"));
			warehouseTodaylog.setCteatetime(rs.getString("cteatetime"));
			warehouseTodaylog.setStarttime(rs.getString("starttime"));
			warehouseTodaylog.setEndtime(rs.getString("endtime"));
			warehouseTodaylog.setZuori_kucun(rs.getLong("zuori_kucun"));
			warehouseTodaylog.setJinri_weiruku(rs.getLong("jinri_weiruku"));
			warehouseTodaylog.setJinri_yiruku(rs.getLong("jinri_yiruku"));
			warehouseTodaylog.setJinri_daocuohuo(rs.getLong("jinri_daocuohuo"));
			warehouseTodaylog.setJinri_daocuohuo(rs.getLong("jinri_daocuohuo"));
			warehouseTodaylog.setJinri_chukuzaitu(rs.getLong("jinri_chukuzaitu"));
			warehouseTodaylog.setJinri_chukuyidaozhan(rs.getLong("jinri_chukuyidaozhan"));
			warehouseTodaylog.setJinri_weichukuyidaozhan(rs.getLong("jinri_weichukuyidaozhan"));
			warehouseTodaylog.setJinri_kucun(rs.getLong("jinri_kucun"));
			warehouseTodaylog.setZuori_chukuzaitu(rs.getLong("zuori_chukuzaitu"));

			return warehouseTodaylog;
		}
	}

	private final class WareHouseLogMapper implements RowMapper<JSONObject> {
		@Override
		public JSONObject mapRow(ResultSet rs, int rowNum) throws SQLException {
			JSONObject obj = new JSONObject();
			obj.put("customerid", rs.getString("customerid") == null ? 0 : rs.getString("customerid"));
			obj.put("num", rs.getString("num"));
			return obj;
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
	 * 统计未入库的数量
	 * 
	 * @param branchid
	 * @param flowordertype
	 *            = 1
	 * @return
	 */
	public List<JSONObject> getWeirukuCount(long branchid, String flowordertypes) {

		String sql = "SELECT COUNT(cwb) as num,customerid FROM express_ops_cwb_detail " + "WHERE nextbranchid=? and flowordertype in(" + flowordertypes + ") AND state=1 GROUP BY customerid";

		return jdbcTemplate.query(sql, new WareHouseLogMapper(), branchid);
	}

	/**
	 * 统计已入库的订单
	 * 
	 * @param branchid
	 * @param flowordertype
	 *            = 4
	 * @param startTime
	 *            = 2013-01-19 00:00:00
	 * @param endTime
	 *            = 2013-01-20 00:00:00
	 * @return
	 */
	public List<JSONObject> getYirukuCount(long branchid, long flowordertype, String startTime, String endTime) {

		String sql = "SELECT COUNT(DISTINCT of.cwb) as num,cd.customerid FROM `express_ops_order_flow` of FORCE INDEX(FlowCredateIdx) " + "LEFT JOIN express_ops_cwb_detail cd ON of.cwb=cd.cwb "
				+ "WHERE of.branchid=? AND of.flowordertype=?  AND of.credate>=? " + "AND of.credate<? and cd.state=1 " + "GROUP BY cd.customerid";

		return jdbcTemplate.query(sql, new WareHouseLogMapper(), branchid, flowordertype, startTime, endTime);
	}

	/**
	 * 获取已入库的订单号
	 * 
	 * @param customerid
	 * @param branchid
	 * @param flowordertype
	 *            = 4
	 * @param startTime
	 *            = 2013-01-19 00:00:00
	 * @param endTime
	 *            = 2013-01-20 00:00:00
	 * @param page
	 *            页数
	 * @return
	 */
	public List<String> getYirukuCwb(long customerid, long branchid, long flowordertype, String startTime, String endTime, long page) {
		String sql = "SELECT DISTINCT of.cwb FROM `express_ops_order_flow` of FORCE INDEX(FlowCredateIdx) LEFT JOIN express_ops_cwb_detail cd ON of.cwb=cd.cwb"
				+ " WHERE of.flowordertype=? AND of.branchid=? AND of.credate>= ? " + " AND of.credate< ? and cd.state=1 AND cd.customerid=? " + " LIMIT ?,?";

		return jdbcTemplate.query(sql, new WareHouseCwbMapper(), flowordertype, branchid, startTime, endTime, customerid, ((page - 1) * Page.ONE_PAGE_NUMBER), Page.ONE_PAGE_NUMBER);
	}

	/**
	 * 获取已入库的订单号 不限制供货商
	 * 
	 * @param branchid
	 * @param flowordertype
	 *            = 4
	 * @param startTime
	 *            = 2013-01-19 00:00:00
	 * @param endTime
	 *            = 2013-01-20 00:00:00
	 * @param page
	 *            页数
	 * @return
	 */
	public List<String> getYirukuCwb(long branchid, long flowordertype, String startTime, String endTime, long page) {
		String sql = "SELECT DISTINCT of.cwb FROM `express_ops_order_flow` of FORCE INDEX(FlowCredateIdx) LEFT JOIN express_ops_cwb_detail cd ON of.cwb=cd.cwb"
				+ " WHERE of.flowordertype=? AND of.branchid=? AND of.credate>= ? " + " AND of.credate< ? and cd.state=1 " + " LIMIT ?,?";

		return jdbcTemplate.query(sql, new WareHouseCwbMapper(), flowordertype, branchid, startTime, endTime, ((page - 1) * Page.ONE_PAGE_NUMBER), Page.ONE_PAGE_NUMBER);
	}

	/**
	 * 获取所有已入库的单号
	 * 
	 * @param customerid
	 * @param branchid
	 * @param flowordertype
	 *            = 4
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	public List<String> getYirukuCwbAll(long customerid, long branchid, long flowordertype, String startTime, String endTime) {
		String sql = "SELECT DISTINCT of.cwb FROM `express_ops_order_flow` of FORCE INDEX(FlowCredateIdx) LEFT JOIN express_ops_cwb_detail cd ON of.cwb=cd.cwb"
				+ " WHERE of.flowordertype=? AND of.branchid=? AND of.credate>= ? " + " AND of.credate< ? and cd.state=1 AND cd.customerid=? ";

		return jdbcTemplate.query(sql, new WareHouseCwbMapper(), flowordertype, branchid, startTime, endTime, customerid);
	}

	/**
	 * 获取所有供货商所有已入库的单号
	 * 
	 * @param branchid
	 * @param flowordertype
	 *            = 4
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	public List<String> getYirukuCwbAll(long branchid, long flowordertype, String startTime, String endTime) {
		String sql = "SELECT DISTINCT of.cwb FROM `express_ops_order_flow` of FORCE INDEX(FlowCredateIdx) LEFT JOIN express_ops_cwb_detail cd ON of.cwb=cd.cwb"
				+ " WHERE of.flowordertype=? AND of.branchid=? AND of.credate>= ? " + " AND of.credate< ? and cd.state=1 ";

		return jdbcTemplate.query(sql, new WareHouseCwbMapper(), flowordertype, branchid, startTime, endTime);
	}

	/**
	 * 统计到错的数量
	 * 
	 * @param currentbranchid
	 * @param flowordertype
	 *            = 8
	 * @return
	 */
	public List<JSONObject> getDaocuohuoCount(long currentbranchid, long flowordertype) {

		String sql = "SELECT COUNT(cwb) as num,customerid FROM express_ops_cwb_detail " + "WHERE currentbranchid=? AND flowordertype=? AND state=1 GROUP BY customerid";

		return jdbcTemplate.query(sql, new WareHouseLogMapper(), currentbranchid, flowordertype);
	}

	/**
	 * 统计昨日出库在途
	 * 
	 * @param currentbranchid
	 * @param flowordertype
	 *            =6
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	public List<JSONObject> getZuorichukuzaituCount(long currentbranchid, long flowordertype, String startTime) {

		String sql = "SELECT COUNT(DISTINCT cd.cwb) as num,cd.customerid "
				+ "FROM  express_ops_cwb_detail cd FORCE INDEX(detail_startbranchid_idx) LEFT JOIN  express_ops_order_flow of ON cd.`cwb`=of.`cwb` "
				+ "WHERE cd.flowordertype=? AND cd.startbranchid=? AND of.flowordertype=? AND cd.state=1 AND of.isnow=1 AND of.`credate`<? " + " GROUP BY cd.customerid";

		return jdbcTemplate.query(sql, new WareHouseLogMapper(), flowordertype, currentbranchid, flowordertype, startTime);
	}

	/**
	 * 统计今日出库 合并所有出库的
	 * 
	 * @param currentbranchid
	 * @param flowordertype
	 *            =6
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	public List<JSONObject> getJinrichukuCount(long currentbranchid, String flowordertypes, String startTime) {

		String sql = "SELECT COUNT(DISTINCT cd.cwb) as num,cd.customerid " + "FROM  express_ops_cwb_detail cd LEFT JOIN  express_ops_order_flow of FORCE INDEX(FlowCredateIdx) ON cd.`cwb`=of.`cwb` "
				+ "WHERE of.branchid=? AND of.flowordertype in(" + flowordertypes + ") AND cd.state=1 AND of.`credate`>=? " + " GROUP BY cd.customerid";

		return jdbcTemplate.query(sql, new WareHouseLogMapper(), currentbranchid, startTime);
	}

	/**
	 * 统计今日出库在途
	 * 
	 * @param currentbranchid
	 * @param flowordertype
	 *            =6
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	public List<JSONObject> getJinrichukuzaituCount(long currentbranchid, long flowordertype, String startTime) {

		String sql = "SELECT COUNT(DISTINCT cd.cwb) as num,cd.customerid " + "FROM  express_ops_cwb_detail cd LEFT JOIN  express_ops_order_flow of ON cd.`cwb`=of.`cwb` "
				+ "WHERE cd.flowordertype=? AND cd.startbranchid=? AND of.flowordertype=? AND cd.state=1 AND  of.isnow=1 AND of.`credate`>=? " + " GROUP BY cd.customerid";

		return jdbcTemplate.query(sql, new WareHouseLogMapper(), flowordertype, currentbranchid, flowordertype, startTime);
	}

	/**
	 * 统计今日库存
	 * 
	 * @param currentbranchid
	 * @return
	 */
	public List<JSONObject> getJinrikucunCount(long currentbranchid) {

		String sql = "SELECT COUNT(cwb) as num,customerid FROM express_ops_cwb_detail " + " WHERE currentbranchid=? AND state=1 GROUP BY customerid";

		return jdbcTemplate.query(sql, new WareHouseLogMapper(), currentbranchid);
	}

	/**
	 * 获取所有 已到站 的统计
	 * 
	 * @param flowordertype
	 *            7,8
	 * @param startTime
	 * @return
	 */
	public List<JSONObject> getYidaozhanCount(String flowordertype, String startTime) {
		String sql = "SELECT  COUNT(DISTINCT cd.cwb) as num ,cd.customerid FROM `express_set_branch` b "
				+ " RIGHT JOIN  express_ops_order_flow of FORCE INDEX(FlowCredateIdx) ON of.branchid=b.branchid " + " LEFT JOIN express_ops_cwb_detail cd ON cd.cwb=of.cwb"
				+ " WHERE of.flowordertype IN (" + flowordertype + ") AND  of.credate>?  AND cd.state=1 AND b.`sitetype`=2 " + " GROUP BY cd.customerid ";
		return jdbcTemplate.query(sql, new WareHouseLogMapper(), startTime);
	}

	/**
	 * 获取漏扫已到站数据
	 * 
	 * @param flowordertype
	 *            =6
	 * @param branchid
	 * @param startTime
	 * @return
	 */
	public List<JSONObject> getLousaoyidaozhanCount(long flowordertype, long branchid, String startTime) {
		String sql = "SELECT COUNT(DISTINCT cd.cwb) as num ,cd.customerid " + "FROM  express_ops_cwb_detail cd LEFT JOIN  express_ops_order_flow of FORCE INDEX(FlowCredateIdx) ON cd.`cwb`=of.`cwb` "
				+ "WHERE  cd.state=1 AND of.branchid=? " + "AND of.flowordertype=?  AND of.`credate`>? " + "AND of.`comment` = '系统自动处理' GROUP BY cd.customerid";
		return jdbcTemplate.query(sql, new WareHouseLogMapper(), branchid, flowordertype, startTime);
	}

	// ==============库房日志统计==end=============
	/**
	 * 获取最新一个日志
	 * 
	 * @param warehouseid
	 * @return
	 */
	public WarehouseTodaylog getLastBranchTodayLogByWarehouseid(long warehouseid) {
		String sql = "SELECT * FROM express_ops_warehouse_todaylog WHERE  warehouseid=? ORDER BY cteatetime DESC LIMIT 0,1";
		try {
			return jdbcTemplate.queryForObject(sql, new WarehouseTodaylogRowMapper(), warehouseid);
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	public List<WarehouseTodaylog> getLogByWarehouseidAndDate(long warehouseid, String statetime, String endtime) {

		String sql = " SELECT * FROM  express_ops_warehouse_todaylog where " + " warehouseid=? and cteatetime >= ? and cteatetime <= ? group by customerid";

		try {
			return jdbcTemplate.query(sql, new WarehouseTodaylogRowMapper(), warehouseid, statetime, endtime);
		} catch (DataAccessException e) {
			return null;
		}

	}

	public long creBranchTodayLog(final WarehouseTodaylog wTodaylog) {

		KeyHolder key = new GeneratedKeyHolder();
		jdbcTemplate.update(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(java.sql.Connection con) throws SQLException {
				PreparedStatement ps = null;
				ps = con.prepareStatement("INSERT INTO express_ops_warehouse_todaylog" + "(userid,cteatetime,starttime,endtime,warehouseid,customerid,"
						+ "zuori_kucun,jinri_weiruku,jinri_yiruku,jinri_daocuohuo" + ",jinri_rukuheji,jinri_chukuzaitu,jinri_chukuyidaozhan,jinri_kucun,jinri_weichukuyidaozhan,zuori_chukuzaitu) "
						+ "VALUES " + "(?,?,?,?,?,?,?,?,?,?," + " ?,?,?,?,?,?)", new String[] { "id" });
				ps.setLong(1, wTodaylog.getUserid());
				ps.setString(2, wTodaylog.getCteatetime());
				ps.setString(3, wTodaylog.getStarttime());
				ps.setString(4, wTodaylog.getEndtime());
				ps.setLong(5, wTodaylog.getWarehouseid());
				ps.setLong(6, wTodaylog.getCustomerid());
				ps.setLong(7, wTodaylog.getZuori_kucun());
				ps.setLong(8, wTodaylog.getJinri_weiruku());
				ps.setLong(9, wTodaylog.getJinri_yiruku());
				ps.setLong(10, wTodaylog.getJinri_daocuohuo());
				ps.setLong(11, wTodaylog.getJinri_rukuheji());
				ps.setLong(12, wTodaylog.getJinri_chukuzaitu());
				ps.setLong(13, wTodaylog.getJinri_chukuyidaozhan());
				ps.setLong(14, wTodaylog.getJinri_kucun());
				ps.setLong(15, wTodaylog.getJinri_weichukuyidaozhan());
				ps.setLong(16, wTodaylog.getZuori_chukuzaitu());

				return ps;
			}
		}, key);
		return key.getKey().longValue();
	}

}

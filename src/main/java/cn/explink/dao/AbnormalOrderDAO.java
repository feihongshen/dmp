package cn.explink.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import com.jcraft.jsch.Logger;

import cn.explink.domain.AbnormalOrder;
import cn.explink.util.Page;

@Component
public class AbnormalOrderDAO {

	private final class AbnormalOrderRowMapper implements RowMapper<AbnormalOrder> {
		@Override
		public AbnormalOrder mapRow(ResultSet rs, int rowNum) throws SQLException {
			AbnormalOrder abnormalOrder = new AbnormalOrder();

			abnormalOrder.setId(rs.getLong("id"));
			abnormalOrder.setDescribe(rs.getString("describe"));
			abnormalOrder.setCreuserid(rs.getLong("creuserid"));
			abnormalOrder.setCredatetime(rs.getString("credatetime"));
			abnormalOrder.setAbnormaltypeid(rs.getLong("abnormaltypeid"));
			abnormalOrder.setOpscwbid(rs.getLong("opscwbid"));
			abnormalOrder.setCustomerid(rs.getLong("customerid"));
			abnormalOrder.setIshandle(rs.getLong("ishandle"));
			abnormalOrder.setBranchid(rs.getLong("branchid"));
			abnormalOrder.setIsnow(rs.getLong("isnow"));
			return abnormalOrder;
		}
	}

	private final class AbnormalOrderJsonRowMapper implements RowMapper<JSONObject> {
		@Override
		public JSONObject mapRow(ResultSet rs, int rowNum) throws SQLException {
			JSONObject obj = new JSONObject();

			obj.put("id", rs.getLong("id"));
			obj.put("describe", rs.getString("describe"));
			obj.put("creuserid", rs.getLong("creuserid"));
			obj.put("credatetime", rs.getString("credatetime"));
			obj.put("abnormaltypeid", rs.getLong("abnormaltypeid"));
			obj.put("opscwbid", rs.getLong("opscwbid"));
			obj.put("customerid", rs.getLong("customerid"));
			obj.put("ishandle", rs.getLong("ishandle"));
			obj.put("branchid", rs.getLong("branchid"));
			obj.put("isnow", rs.getLong("isnow"));
			obj.put("cwb", rs.getString("cwb"));
			obj.put("customerid", rs.getLong("customerid"));
			obj.put("emaildate", rs.getString("emaildate"));
			obj.put("flowordertype", rs.getLong("flowordertype"));
			obj.put("deliverybranchid", rs.getLong("deliverybranchid"));
			return obj;
		}
	}

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public AbnormalOrder getAbnormalOrderById(long id) {
		try {
			String sql = "select * from express_ops_abnormal_order where `id`=?  ";
			return jdbcTemplate.queryForObject(sql, new AbnormalOrderRowMapper(), id);
		} catch (Exception e) {
			return null;
		}
	}

	public List<AbnormalOrder> getAbnormalOrderByOpscwbid(long opscwbid) {
		String sql = "select * from express_ops_abnormal_order where `opscwbid`=?  ";
		return jdbcTemplate.query(sql, new AbnormalOrderRowMapper(), opscwbid);
	}

	public AbnormalOrder getAbnormalOrderByOpscwbidForObject(long opscwbid) {
		String sql = "select * from express_ops_abnormal_order where `opscwbid`=? ";
		AbnormalOrder ab = new AbnormalOrder();
		try {
			ab = jdbcTemplate.queryForObject(sql, new AbnormalOrderRowMapper(), opscwbid);
		} catch (Exception e) {
			ab = null;
		}
		return ab;
	}

	/*
	 * public List<String> getAbnormalOrderGetIds(long abnormaltypeid){ String
	 * sql =
	 * "select id from express_ops_abnormal_order where `abnormaltypeid`="+
	 * abnormaltypeid+" and `isnow`=1 "; return jdbcTemplate.queryForList(sql,
	 * String.class); }
	 */
	public void saveAbnormalOrderByid(long id, long abnormaltypeid, String describe) {
		String sql = "update express_ops_abnormal_order set `abnormaltypeid`=?,`describe`=? where `id`=?";
		jdbcTemplate.update(sql, abnormaltypeid, describe, id);
	}

	public void saveAbnormalOrderByOpscwbid(long opscwbid) {
		jdbcTemplate.update("update express_ops_abnormal_order set `isnow`='0' where `opscwbid`=?", opscwbid);
	}

	public void creAbnormalOrder(long opscwbid, long customerid, String describe, long creuserid, long branchid, long abnormaltypeid, String credatetime) {
		saveAbnormalOrderByOpscwbid(opscwbid);

		String sql = "insert into express_ops_abnormal_order(`opscwbid`,`customerid`,`describe`,`creuserid`,`branchid`,`abnormaltypeid`,`credatetime`,`isnow`) values(?,?,?,?,?,?,?,?)";
		jdbcTemplate.update(sql, opscwbid, customerid, describe, creuserid, branchid, abnormaltypeid, credatetime, 1);
	}

	public long creAbnormalOrderLong(final long opscwbid, final long customerid, final String describe, final long creuserid, final long branchid, final long abnormaltypeid, final String credatetime) {
		saveAbnormalOrderByOpscwbid(opscwbid);
		final String sql = "insert into express_ops_abnormal_order(`opscwbid`,`customerid`,`describe`,`creuserid`,`branchid`,`abnormaltypeid`,`credatetime`,`isnow`) values(?,?,?,?,?,?,?,?)";
		KeyHolder key = new GeneratedKeyHolder();
		jdbcTemplate.update(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(java.sql.Connection con) throws SQLException {
				PreparedStatement ps = null;
				ps = con.prepareStatement(sql, new String[] { "id" });
				ps.setLong(1, opscwbid);
				ps.setLong(2, customerid);
				ps.setString(3, describe);
				ps.setLong(4, creuserid);
				ps.setLong(5, branchid);
				ps.setLong(6, abnormaltypeid);
				ps.setString(7, credatetime);
				ps.setLong(8, 1);
				return ps;
			}
		}, key);
		return key.getKey().longValue();
	}

	public List<AbnormalOrder> getAbnormalOrderByWhere(long page, String begindate, String enddate, long ishandle, long abnormaltypeid, long creuserid) {
		String sql = "select * from express_ops_abnormal_order where creuserid=? " + " and credatetime >= '" + begindate + "' and credatetime <= '" + enddate + "' ";
		if (ishandle > -1) {
			sql += " and ishandle =" + ishandle;
		}
		if (abnormaltypeid > 0) {
			sql += " and abnormaltypeid =" + abnormaltypeid;
		}

		sql += " limit " + (page - 1) * Page.ONE_PAGE_NUMBER + " ," + Page.ONE_PAGE_NUMBER;

		return jdbcTemplate.query(sql, new AbnormalOrderRowMapper(), creuserid);
	}

	public long getAbnormalOrderCount(String begindate, String enddate, long ishandle, long abnormaltypeid, long creuserid) {
		String sql = "select count(1) from express_ops_abnormal_order where creuserid=? " + " and credatetime >= ?  and credatetime <=? ";
		if (ishandle > -1) {
			sql += " and ishandle =" + ishandle;
		}
		if (abnormaltypeid > 0) {
			sql += " and abnormaltypeid =" + abnormaltypeid;
		}
		return jdbcTemplate.queryForLong(sql, creuserid, begindate, enddate);
	}

	public List<JSONObject> getAbnormalOrderAndCwbdetailByCwbAndBranchidAndAbnormaltypeidAndIshandle(String opscwbids, String cwb, long branchid, long abnormaltypeid, long ishandle) {
		String sql = "SELECT ao.*,cd.`cwb`,cd.`customerid`,cd.`emaildate`,cd.`flowordertype`,cd.`deliverybranchid` FROM `express_ops_abnormal_order` ao "
				+ "LEFT JOIN `express_ops_cwb_detail` cd  ON ao.`opscwbid`=cd.`opscwbid`  WHERE  cd.state=1" + " and ao.`opscwbid` in(" + opscwbids + ")";
		if (cwb.length() > 0) {
			sql += " AND cd.`cwb` IN(" + cwb + ")";
		}
		if (branchid > 0) {
			sql += " AND ao.`branchid`=" + branchid;
		}
		if (abnormaltypeid > 0) {
			sql += " AND ao.`abnormaltypeid`=" + abnormaltypeid;
		}
		if (ishandle > -1) {
			sql += " AND ao.`ishandle`=" + ishandle;
		}

		return jdbcTemplate.query(sql, new AbnormalOrderJsonRowMapper());

	}

	public List<JSONObject> getAbnormalOrderAndCwbdetailByCwbAndBranchidAndAbnormaltypeidAndIshandleExport(String opscwbids, String cwb, long branchid, long abnormaltypeid, long ishandle) {
		String sql = "SELECT ao.*,cd.`cwb`,cd.`customerid`,cd.`emaildate`,cd.`flowordertype`,cd.`deliverybranchid` FROM `express_ops_abnormal_order` ao "
				+ "LEFT JOIN `express_ops_cwb_detail` cd  ON ao.`opscwbid`=cd.`opscwbid`  WHERE ao.isnow=1 and cd.state=1" + " and ao.`opscwbid` in(" + opscwbids + ")";
		if (cwb.length() > 0) {
			sql += " AND cd.`cwb` IN(" + cwb + ")";
		}
		if (branchid > 0) {
			sql += " AND ao.`branchid`=" + branchid;
		}
		if (abnormaltypeid > 0) {
			sql += " AND ao.`abnormaltypeid`=" + abnormaltypeid;
		}
		if (ishandle > -1) {
			sql += " AND ao.`ishandle`=" + ishandle;
		}

		return jdbcTemplate.query(sql, new AbnormalOrderJsonRowMapper());

	}

	public long getAbnormalOrderAndCwbdetailCount(String cwb, long branchid, long abnormaltypeid, long ishandle) {
		String sql = "SELECT count(1) FROM `express_ops_abnormal_order` ao " + "LEFT JOIN `express_ops_cwb_detail` cd  ON ao.`opscwbid`=cd.`opscwbid` WHERE  1=1 ";
		if (cwb.length() > 0) {
			sql += " AND cd.`cwb` IN(" + cwb + ")";
		}
		if (branchid > 0) {
			sql += " AND ao.`branchid`=" + branchid;
		}
		if (abnormaltypeid > 0) {
			sql += " AND ao.`abnormaltypeid`=" + abnormaltypeid;
		}
		if (ishandle > -1) {
			sql += " AND ao.`ishandle`=" + ishandle;
		}

		return jdbcTemplate.queryForLong(sql);

	}

	public void saveAbnormalOrderForIshandle(long id, long ishandle) {
		String sql = "update express_ops_abnormal_order set `ishandle`=?  where `id`=? ";
		jdbcTemplate.update(sql, ishandle, id);
	}

	public void saveAbnormalOrderForNohandle(long id, long ishandle) {
		String sql = "update express_ops_abnormal_order set `ishandle`= ? where `id`=?  ";
		jdbcTemplate.update(sql, ishandle, id);
	}

	public List<String> getAbnormalOrderByCredatetime(String chuangjianbegindate, String chuangjianenddate) {
		String sql = "select opscwbid from express_ops_abnormal_order where " + "  credatetime >= '" + chuangjianbegindate + "' and credatetime <= '" + chuangjianenddate + "' ";

		return jdbcTemplate.queryForList(sql, String.class);
	}

	/**
	 * 用于迁移
	 * 
	 * @return
	 */
	public List<AbnormalOrder> getAllAbnormalIsnow() {
		String sql = "SELECT * FROM express_ops_abnormal_order WHERE isnow=1 ORDER BY credatetime ";
		return jdbcTemplate.query(sql, new AbnormalOrderRowMapper());

	}

	/**
	 * 根据id 得到
	 * 
	 * @param id
	 * @return
	 */
	public AbnormalOrder getAbnormalOrderByOId(long id) {
		String sql = "select * from express_ops_abnormal_order where id=? ";
		AbnormalOrder ab = new AbnormalOrder();
		try {
			ab = jdbcTemplate.queryForObject(sql, new AbnormalOrderRowMapper(), id);
		} catch (Exception e) {
			ab = null;
		}
		return ab;
	}

	public List<JSONObject> getAbnormalOrderByCredatetimeofpage(long page, String chuangjianbegindate, String chuangjianenddate, String cwbs, long branchid, long abnormaltypeid, long ishandle) {
		String sql = "SELECT ao.*,cd.`cwb`,cd.`customerid`,cd.`emaildate`,cd.`flowordertype`,cd.`deliverybranchid`  FROM `express_ops_abnormal_order` ao LEFT JOIN `express_ops_cwb_detail` cd  "
				+ "ON ao.`opscwbid`=cd.`opscwbid`  " + "WHERE   " + "ao.credatetime >= '" + chuangjianbegindate + "' " + "and ao.credatetime <= '" + chuangjianenddate + "' "
				// + "AND ao.isnow=1 "
				+ "AND cd.state=1 ";
		if (cwbs.length() > 0) {
			sql += " AND cd.`cwb` IN(" + cwbs + ")";
		}
		if (branchid > 0) {
			sql += " AND ao.`branchid`=" + branchid;
		}
		if (abnormaltypeid > 0) {
			sql += " AND ao.`abnormaltypeid`=" + abnormaltypeid;
		}
		if (ishandle > -1) {
			sql += " AND ao.`ishandle`=" + ishandle;
		}
		sql += " limit " + (page - 1) * Page.ONE_PAGE_NUMBER + " ," + Page.ONE_PAGE_NUMBER;

		return jdbcTemplate.query(sql, new AbnormalOrderJsonRowMapper());
	}

	public int getAbnormalOrderByCredatetimeCount(String chuangjianbegindate, String chuangjianenddate, String cwbs, long branchid, long abnormaltypeid, long ishandle) {
		String sql = "SELECT count(1) FROM `express_ops_abnormal_order` ao LEFT JOIN `express_ops_cwb_detail` cd  " + "ON ao.`opscwbid`=cd.`opscwbid`  " + "WHERE   " + "ao.credatetime >= '"
				+ chuangjianbegindate + "' " + "and ao.credatetime <= '" + chuangjianenddate + "' "
				// + "AND ao.isnow=1 "
				+ "AND cd.state=1 ";
		if (cwbs.length() > 0) {
			sql += " AND cd.`cwb` IN(" + cwbs + ")";
		}
		if (branchid > 0) {
			sql += " AND ao.`branchid`=" + branchid;
		}
		if (abnormaltypeid > 0) {
			sql += " AND ao.`abnormaltypeid`=" + abnormaltypeid;
		}
		if (ishandle > -1) {
			sql += " AND ao.`ishandle`=" + ishandle;
		}

		return jdbcTemplate.queryForInt(sql);
	}

	public int getAbnormalOrderAndCwbdetailByCwbAndBranchidAndAbnormaltypeidAndIshandleCount(String cwb, long branchid, long abnormaltypeid, long ishandle, String begindate, String enddate) {
		String opscwbidsql = "select opscwbid from express_ops_abnormal_write_back where " + " credatetime >= '" + begindate + "'  and credatetime <= '" + enddate + "' ";

		String sql = "SELECT count(1) FROM `express_ops_abnormal_order` ao " + "LEFT JOIN `express_ops_cwb_detail` cd  ON ao.`opscwbid`=cd.`opscwbid`  WHERE  cd.state=1" + " and ao.`opscwbid` in("
				+ opscwbidsql + ")";

		if (cwb.length() > 0) {
			sql += " AND cd.`cwb` IN(" + cwb + ")";
		}
		if (branchid > 0) {
			sql += " AND ao.`branchid`=" + branchid;
		}
		if (abnormaltypeid > 0) {
			sql += " AND ao.`abnormaltypeid`=" + abnormaltypeid;
		}
		if (ishandle > -1) {
			sql += " AND ao.`ishandle`=" + ishandle;
		}
		// sql +=" AND ao.isnow=1 ";
		return jdbcTemplate.queryForInt(sql);
	}

	public List<JSONObject> getAbnormalOrderAndCwbdetailByCwbAndBranchidAndAbnormaltypeidAndIshandle(long page, String cwb, long branchid, long abnormaltypeid, long ishandle, String begindate,
			String enddate) {
		String opscwbidsql = "select opscwbid from express_ops_abnormal_write_back where " + " credatetime >= '" + begindate + "'  and credatetime <= '" + enddate + "' ";
		String sql = "SELECT ao.*,cd.`cwb`,cd.`customerid`,cd.`emaildate`,cd.`flowordertype`,cd.`deliverybranchid` FROM `express_ops_abnormal_order` ao "
				+ "LEFT JOIN `express_ops_cwb_detail` cd  ON ao.`opscwbid`=cd.`opscwbid`  WHERE  cd.state=1" + " and ao.`opscwbid` in(" + opscwbidsql + ")";
		if (cwb.length() > 0) {
			sql += " AND cd.`cwb` IN(" + cwb + ")";
		}
		if (branchid > 0) {
			sql += " AND ao.`branchid`=" + branchid;
		}
		if (abnormaltypeid > 0) {
			sql += " AND ao.`abnormaltypeid`=" + abnormaltypeid;
		}
		if (ishandle > -1) {
			sql += " AND ao.`ishandle`=" + ishandle;
		}
		// sql +=" AND ao.isnow=1 ";
		sql += " limit " + (page - 1) * Page.ONE_PAGE_NUMBER + " ," + Page.ONE_PAGE_NUMBER;
		System.out.println("问题件sql：" + sql);
		return jdbcTemplate.query(sql, new AbnormalOrderJsonRowMapper());
	}
}

package cn.explink.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import cn.explink.domain.OrderArriveTime;
import cn.explink.util.Page;

@Component
public class OrderArriveTimeDAO {
	@Autowired
	private JdbcTemplate jdbcTemplate;

	private final class OrderArriveTimeRowMapper implements RowMapper<OrderArriveTime> {
		@Override
		public OrderArriveTime mapRow(ResultSet rs, int rowNum) throws SQLException {
			OrderArriveTime orderArriveTime = new OrderArriveTime();
			orderArriveTime.setId(rs.getLong("id"));
			orderArriveTime.setCwb(rs.getString("cwb"));
			orderArriveTime.setCustomerid(rs.getLong("customerid"));
			orderArriveTime.setOuttime(rs.getString("outtime"));
			orderArriveTime.setIntime(rs.getString("intime"));
			orderArriveTime.setArrivetime(rs.getString("arrivetime"));
			orderArriveTime.setSendcarnum(rs.getLong("sendcarnum"));
			orderArriveTime.setScannum(rs.getLong("scannum"));
			orderArriveTime.setCwbordertypeid(rs.getLong("cwbordertypeid"));
			orderArriveTime.setOutbranchid(rs.getLong("outbranchid"));
			orderArriveTime.setInbranchid(rs.getLong("inbranchid"));
			orderArriveTime.setUserid(rs.getLong("userid"));
			orderArriveTime.setSaveuserid(rs.getLong("saveuserid"));
			orderArriveTime.setSavetime(rs.getString("savetime"));
			return orderArriveTime;
		}
	}

	public void createOrderArriveTime(final OrderArriveTime orderArriveTime) {
		KeyHolder key = new GeneratedKeyHolder();
		jdbcTemplate.update(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(java.sql.Connection con) throws SQLException {
				PreparedStatement ps = null;
				ps = con.prepareStatement("insert into ops_order_arrive_time (id,cwb,customerid,outtime,intime," + "sendcarnum,scannum,cwbordertypeid,outbranchid,inbranchid,userid) "
						+ "values (?,?,?,?,?,?,?,?,?,?,?)", new String[] { "id" });
				ps.setLong(1, orderArriveTime.getId());
				ps.setString(2, orderArriveTime.getCwb());
				ps.setLong(3, orderArriveTime.getCustomerid());
				ps.setString(4, orderArriveTime.getOuttime());
				ps.setString(5, orderArriveTime.getIntime());
				ps.setLong(6, orderArriveTime.getSendcarnum());
				ps.setLong(7, orderArriveTime.getScannum());
				ps.setLong(8, orderArriveTime.getCwbordertypeid());
				ps.setLong(9, orderArriveTime.getOutbranchid());
				ps.setLong(10, orderArriveTime.getInbranchid());
				ps.setLong(11, orderArriveTime.getUserid());
				return ps;
			}
		}, key);
		orderArriveTime.setId(key.getKey().longValue());
		// return key.getKey().longValue();
	}

	public void deleteOrderArriveTimeByCwb(String cwb) {
		jdbcTemplate.update("delete from ops_order_arrive_time where cwb = ?", cwb);
	}

	public void updateScannum(String cwb, long scannum) {
		jdbcTemplate.update("update ops_order_arrive_time set scannum=? where cwb=? ", scannum, cwb);
	}

	public List<OrderArriveTime> getOrderArriveTimePage(long page, long inbranchid, String cwbs, String starttime, String endtime, long flag) {
		String sql = "SELECT * FROM ops_order_arrive_time WHERE inbranchid=? ";
		sql = this.getOrderArriveTimePageWhere(sql, cwbs, starttime, endtime, flag);
		sql += " limit " + (page - 1) * Page.ONE_PAGE_NUMBER + " ," + Page.ONE_PAGE_NUMBER;
		return jdbcTemplate.query(sql, new OrderArriveTimeRowMapper(), inbranchid);
	}

	public long getOrderArriveTimePageCount(long inbranchid, String cwbs, String starttime, String endtime, long flag) {
		String sql = "SELECT count(1) FROM ops_order_arrive_time WHERE inbranchid=? ";
		sql = this.getOrderArriveTimePageWhere(sql, cwbs, starttime, endtime, flag);
		return jdbcTemplate.queryForLong(sql, inbranchid);
	}

	private String getOrderArriveTimePageWhere(String sql, String cwbs, String starttime, String endtime, long flag) {
		StringBuffer sb = new StringBuffer(sql);
		if (!"".equals(cwbs)) {
			sb.append(" and cwb IN (" + cwbs + ") ");
		}
		if (!("").equals(starttime)) {
			sb.append(" and intime >='" + starttime + "' ");
		}
		if (!("").equals(endtime)) {
			sb.append(" and intime <='" + endtime + "' ");
		}
		if (2 == flag) {// 2:已提交 1:未提交
			sb.append(" and arrivetime is not null ");
		} else {
			sb.append(" and arrivetime is null ");
		}
		return sb.toString();
	}

	public void updateOrderArriveTimeById(String arrivetime, long id) {
		String sql = "update ops_order_arrive_time SET arrivetime=? where id =? ";
		jdbcTemplate.update(sql, arrivetime, id);
	}

	public List<OrderArriveTime> getOrderArriveTimeList(long inbranchid, String cwbs, String starttime, String endtime, long flag) {
		String sql = "SELECT * FROM ops_order_arrive_time WHERE inbranchid=? ";
		sql = this.getOrderArriveTimePageWhere(sql, cwbs, starttime, endtime, flag);
		return jdbcTemplate.query(sql, new OrderArriveTimeRowMapper(), inbranchid);
	}

	public String getOrderArriveTimeListSql(long inbranchid, String cwbs, String starttime, String endtime, long flag) {
		String sql = "SELECT * FROM ops_order_arrive_time WHERE inbranchid=" + inbranchid;
		sql = this.getOrderArriveTimePageWhere(sql, cwbs, starttime, endtime, flag);
		return sql;
	}
}

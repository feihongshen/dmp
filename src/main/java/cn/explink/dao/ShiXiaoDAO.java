package cn.explink.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import cn.explink.domain.ShiXiao;
import cn.explink.util.Page;

@Component
public class ShiXiaoDAO {

	private final class ShiXiaoRowMapper implements RowMapper<ShiXiao> {
		@Override
		public ShiXiao mapRow(ResultSet rs, int rowNum) throws SQLException {
			ShiXiao shiXiao = new ShiXiao();

			shiXiao.setId(rs.getLong("id"));
			shiXiao.setCretime(rs.getString("cretime"));
			shiXiao.setCurrentbranchid(rs.getLong("currentbranchid"));
			shiXiao.setCustomerid(rs.getLong("customerid"));
			shiXiao.setCwb(rs.getString("cwb"));
			shiXiao.setDeliverybranchid(rs.getLong("deliverybranchid"));
			shiXiao.setFlowordertype(rs.getLong("flowordertype"));
			shiXiao.setNextbranchid(rs.getLong("nextbranchid"));
			shiXiao.setOpscwbid(rs.getLong("opscwbid"));
			shiXiao.setStartbranchid(rs.getLong("startbranchid"));
			shiXiao.setUserid(rs.getLong("userid"));

			return shiXiao;
		}
	}

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public List<ShiXiao> getShiXiaoByCwbsAndCretime(long page, String cwbs, String begindate, String enddate) {
		String sql = "select * from edit_shixiao ";
		if (cwbs.length() > 0 || begindate.length() > 0 || enddate.length() > 0) {
			sql += " where ";
			StringBuffer str = new StringBuffer();

			if (begindate.length() > 0) {
				str.append(" and cretime >= '" + begindate + "' ");
			}
			if (enddate.length() > 0) {
				str.append(" and cretime <= '" + enddate + "' ");
			}
			if (cwbs.length() > 0) {
				str.append(" and cwb in(" + cwbs + ")");
			}
			sql += str.substring(4, str.length());
		}
		sql += " limit " + (page - 1) * Page.ONE_PAGE_NUMBER + " ," + Page.ONE_PAGE_NUMBER;

		return jdbcTemplate.query(sql, new ShiXiaoRowMapper());
	}

	public long getShiXiaoByCwbsAndCretimeCount(String cwbs, String begindate, String enddate) {
		String sql = "select count(1) from edit_shixiao ";

		if (cwbs.length() > 0 || begindate.length() > 0 || enddate.length() > 0) {
			sql += " where ";
			StringBuffer str = new StringBuffer();

			if (begindate.length() > 0) {
				str.append(" and cretime >= '" + begindate + "' ");
			}
			if (enddate.length() > 0) {
				str.append(" and cretime <= '" + enddate + "' ");
			}
			if (cwbs.length() > 0) {
				str.append(" and cwb in(" + cwbs + ")");
			}
			sql += str.substring(4, str.length());
		}

		return jdbcTemplate.queryForLong(sql);
	}

	public void creAbnormalOrder(long opscwbid, String cretime, long currentbranchid, long customerid, String cwb, long deliverybranchid, long flowordertype, long nextbranchid, long startbranchid,
			long userid) {
		String sql = "insert into edit_shixiao(`opscwbid`,`cretime`,`currentbranchid`,`customerid`,`cwb`,`deliverybranchid`,`flowordertype`,`nextbranchid`,`startbranchid`,`userid`) values(?,?,?,?,?,?,?,?,?,?)";
		jdbcTemplate.update(sql, opscwbid, cretime, currentbranchid, customerid, cwb, deliverybranchid, flowordertype, nextbranchid, startbranchid, userid);
	}

}

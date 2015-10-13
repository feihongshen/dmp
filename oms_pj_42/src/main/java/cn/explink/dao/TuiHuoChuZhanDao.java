package cn.explink.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import cn.explink.domain.TuiHuoChuZhanOrder;
import cn.explink.util.Page;

@Component
public class TuiHuoChuZhanDao {
	@Autowired
	JdbcTemplate jdbcTemplate;

	public List<TuiHuoChuZhanOrder> checkIsExist(String cwb) {
		String sql = " select * from ops_delivery_tuihuochuzhan  where cwb=?";
		return jdbcTemplate.query(sql, new TuihuochuzhanMapper(), cwb);
	}

	private class TuihuochuzhanMapper implements RowMapper<TuiHuoChuZhanOrder> {

		@Override
		public TuiHuoChuZhanOrder mapRow(ResultSet rs, int rowNum) throws SQLException {
			TuiHuoChuZhanOrder to = new TuiHuoChuZhanOrder();
			to.setCustomerid(rs.getLong("customerid"));
			to.setCwb(rs.getString("cwb"));
			to.setCwbordertypeid(rs.getLong("cwbordertypeid"));
			to.setId(rs.getInt("id"));
			to.setTuihuobranchid(rs.getLong("tuihuobranchid"));
			to.setTuihuochuzhantime(rs.getString("tuihuochuzhantime"));
			to.setTuihuozhanrukutime(rs.getString("tuihuozhanrukutime"));
			return to;
		}
	}

	public void updateTuiHuoChuZhan(final TuiHuoChuZhanOrder to) {
		jdbcTemplate.update("update ops_delivery_tuihuochuzhan set customerid=?,cwbordertypeid=?,tuihuobranchid=?,tuihuochuzhantime=?,tuihuozhanrukutime=? where cwb=?", new PreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				ps.setLong(1, to.getCustomerid());
				ps.setLong(2, to.getCwbordertypeid());
				ps.setLong(3, to.getTuihuobranchid());
				ps.setString(4, to.getTuihuochuzhantime());
				ps.setString(5, to.getTuihuozhanrukutime());
				ps.setString(6, to.getCwb());
			}
		});
	}

	public void saveTuiHuoChuZhan(final TuiHuoChuZhanOrder to) {
		jdbcTemplate.update(" insert into ops_delivery_tuihuochuzhan (customerid,cwbordertypeid,tuihuobranchid,tuihuochuzhantime,tuihuozhanrukutime,cwb) values(?,?,?,?,?,?)",
				new PreparedStatementSetter() {

					@Override
					public void setValues(PreparedStatement ps) throws SQLException {
						ps.setLong(1, to.getCustomerid());
						ps.setLong(2, to.getCwbordertypeid());
						ps.setLong(3, to.getTuihuobranchid());
						ps.setString(4, to.getTuihuochuzhantime());
						ps.setString(5, to.getTuihuozhanrukutime());
						ps.setString(6, to.getCwb());
					}
				});
	}

	// 强制索引
	public List<TuiHuoChuZhanOrder> getTuiHuoChuZhanList(String begindate, String enddate, String branchstr, String customerstr, long istuihuozhanruku, long page) {
		String sql = "select * from ops_delivery_tuihuochuzhan FORCE INDEX(tuihuochuzhan_tuihuochuzhantime_idx) where tuihuochuzhantime >= '" + begindate + "'and tuihuochuzhantime <= '" + enddate
				+ "'";
		sql = getSql(sql, branchstr, customerstr, istuihuozhanruku, page);
		return jdbcTemplate.query(sql, new TuihuochuzhanMapper());
	}

	private String getSql(String sql, String branchstr, String customerstr, long istuihuozhanruku, long page) {

		if (branchstr.length() > 0 || customerstr.length() > 0 || istuihuozhanruku > 0) {
			if (branchstr.length() > 0) {
				sql += " and tuihuobranchid in(" + branchstr + ")";
			}
			if (customerstr.length() > 0) {
				sql += " and customerid in(" + customerstr + ")";
			}
			if (istuihuozhanruku != 0) {
				if (istuihuozhanruku == 1) {
					sql += " and tuihuozhanrukutime <>'' ";// yiruku
				} else {
					sql += " and tuihuozhanrukutime ='' ";
				}
			}
		}
		if (page > 0) {
			sql += " limit " + (page - 1) * Page.ONE_PAGE_NUMBER + "," + Page.ONE_PAGE_NUMBER;
		}
		return sql;
	}

	public long getCountTuihuoChuZhan(String begindate, String enddate, String branchstr, String customerstr, long istuihuozhanruku, int page) {
		String sql = " select count(1) from ops_delivery_tuihuochuzhan FORCE INDEX(tuihuochuzhan_tuihuochuzhantime_idx)   where tuihuochuzhantime >= '" + begindate + "'and tuihuochuzhantime <= '"
				+ enddate + "' ";
		sql = getSql(sql, branchstr, customerstr, istuihuozhanruku, page);
		return jdbcTemplate.queryForLong(sql);
	}

	public void updateXiuGaiDingDanLeiXing(String cwb, int cwbordertypeid) {
		jdbcTemplate.update("update ops_delivery_tuihuochuzhan set cwbordertypeid=? where cwb=?", cwbordertypeid, cwb);
	}

}

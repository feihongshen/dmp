package cn.explink.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import cn.explink.domain.CwbStateControl;

@Component
public class CwbStateControlDAO {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	private final class ExcelColumnSetRowMapper implements RowMapper<CwbStateControl> {
		@Override
		public CwbStateControl mapRow(ResultSet rs, int rowNum) throws SQLException {
			CwbStateControl cwbStateControl = new CwbStateControl();
			cwbStateControl.setFromstate(rs.getInt("fromstate"));
			cwbStateControl.setTostate(rs.getInt("tostate"));
			return cwbStateControl;
		}
	}

	public CwbStateControl getCwbStateControlByParam(int from, int tostates) {
		try {
			return jdbcTemplate.queryForObject("select * from express_set_cwb_state_control where fromstate=? and tostate in(" + tostates + ")", new ExcelColumnSetRowMapper(), from);
		} catch (Exception e) {
			return null;
		}
	}

	public void creCwbStateControl(int from, int to) {
		String sql = "insert into express_set_cwb_state_control (fromstate,tostate) values (?,?)";
		jdbcTemplate.update(sql, from, to);
	}

	public List<CwbStateControl> getCwbStateControlByWhere(int fromstate) {
		String sql = "SELECT * from express_set_cwb_state_control ";
		StringBuffer w = new StringBuffer();
		sql += " where ";
		if (fromstate > 0) {
			w.append(" and fromstate=" + fromstate);
			sql += w.substring(4, w.length());
		}

		List<CwbStateControl> cscList = jdbcTemplate.query(sql, new ExcelColumnSetRowMapper());
		return cscList;
	}

	public long getCwbStateControlCount(int fromstate) {
		String sql = "SELECT count(1) from express_set_cwb_state_control";
		StringBuffer w = new StringBuffer();
		sql += " where ";
		if (fromstate > 0) {
			w.append(" and fromstate=" + fromstate);
			sql += w.substring(4, w.length());
		}

		return jdbcTemplate.queryForLong(sql);
	}

	public void saveCwbStateControl(int oldfromstate, int fromstate, int oldtostate, int tostate) {
		String sql = "update express_set_cwb_state_control set fromstate=?,tostate=? where fromstate=? and tostate=?";
		jdbcTemplate.update(sql, fromstate, tostate, oldfromstate, oldtostate);
	}

	public void delCwbStateControl(int fromstate) {
		String sql = "delete from express_set_cwb_state_control where fromstate=?";
		jdbcTemplate.update(sql, fromstate);
	}

	public long getCountFromstateTostate(int fromstate, int tostate) {
		String sql = "select count(1) from express_set_cwb_state_control WHERE fromstate=? AND tostate =?";
		return jdbcTemplate.queryForLong(sql, fromstate, tostate);
	}

}

package cn.explink.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import cn.explink.domain.CwbALLStateControl;

@Component
public class CwbALLStateControlDAO {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	private final class CwbALLStateControlRowMapper implements RowMapper<CwbALLStateControl> {
		@Override
		public CwbALLStateControl mapRow(ResultSet rs, int rowNum) throws SQLException {
			CwbALLStateControl cwbALLStateControl = new CwbALLStateControl();
			cwbALLStateControl.setCwbstate(rs.getInt("cwbstate"));
			cwbALLStateControl.setToflowtype(rs.getInt("toflowtype"));
			return cwbALLStateControl;
		}
	}

	public List<CwbALLStateControl> getCwbAllStateControl(int cwbstate, String toflowtype) {
		return jdbcTemplate.query("select * from express_set_cwb_allstate_control where cwbstate=? and toflowtype in(" + toflowtype + ")", new CwbALLStateControlRowMapper(), cwbstate);
	}

	public CwbALLStateControl getCwbAllStateControlByParam(int cwbstate, int toflowtype) {
		try {
			return jdbcTemplate.queryForObject("select * from express_set_cwb_allstate_control where cwbstate=? and toflowtype = ?", new CwbALLStateControlRowMapper(), cwbstate, toflowtype);
		} catch (DataAccessException e) {
			return null;
		}
	}

	public void creCwbAllStateControl(int cwbstate, int toflowtype) {
		String sql = "insert into express_set_cwb_allstate_control (cwbstate,toflowtype) values (?,?)";
		jdbcTemplate.update(sql, cwbstate, toflowtype);
	}

	public List<CwbALLStateControl> getCwbAllStateControlByWhere(int cwbstate) {
		String sql = "SELECT * from express_set_cwb_allstate_control ";
		sql = this.getCwbAllStateControlByPageWhereSql(sql, cwbstate, 0);

		List<CwbALLStateControl> cscList = jdbcTemplate.query(sql, new CwbALLStateControlRowMapper());
		return cscList;
	}

	public long getCwbAllStateControlCount(int cwbstate, int toflowtype) {
		String sql = "SELECT count(1) from express_set_cwb_allstate_control";
		sql = this.getCwbAllStateControlByPageWhereSql(sql, cwbstate, toflowtype);
		return jdbcTemplate.queryForLong(sql);
	}

	private String getCwbAllStateControlByPageWhereSql(String sql, int cwbstate, int toflowtype) {
		if (cwbstate > -1 || toflowtype > 0) {
			StringBuffer w = new StringBuffer();
			sql += " where ";
			if (cwbstate > -1) {
				w.append(" and cwbstate=" + cwbstate);
			}
			if (toflowtype > 0) {
				w.append(" and toflowtype=" + toflowtype);
			}
			sql += w.substring(4, w.length());
		}
		return sql;
	}

	public void saveCwbAllStateControl(int oldcwbstate, int cwbstate, int oldtoflowtype, int toflowtype) {
		String sql = "update express_set_cwb_allstate_control set cwbstate=?,toflowtype=? where cwbstate=? and toflowtype=? ";
		jdbcTemplate.update(sql, cwbstate, toflowtype, oldcwbstate, oldtoflowtype);

	}

	public void delCwbAllStateControl(int cwbstate) {
		String sql = "delete from express_set_cwb_allstate_control where cwbstate=?";
		jdbcTemplate.update(sql, cwbstate);

	}

}

package cn.explink.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import cn.explink.domain.WindowShow;

@Component
public class AppearWindowDao {
	private final class WindowShowRowMapper implements RowMapper<WindowShow> {
		@Override
		public WindowShow mapRow(ResultSet rs, int rowNum) throws SQLException {
			WindowShow window = new WindowShow();
			window.setUserid(rs.getLong("userid"));
			window.setJsoninfo(rs.getString("jsoninfo"));
			window.setState(rs.getString("state"));
			window.setType(rs.getString("type"));
			window.setId(rs.getLong("id"));
			return window;
		}
	}

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public long creWindowTime(String jsoninfo, String type, long userid, String state) {
		try {
			// jdbcTemplate.update("insert into express_ops_window (jsoninfo,userid,type,state) values(?,?,?,?)",
			// jsoninfo, userid, type, state);
			return 1;
		} catch (Exception e) {
			return 0;
		}
	}

	public List<WindowShow> getCountWindowByState(long userid) {
		String sql = "select * from express_ops_window where state=1 and userid=?";
		try {
			return this.jdbcTemplate.query(sql, new WindowShowRowMapper(), userid);
		} catch (Exception e) {
			return null;
		}

	}

	public long getCountByStateAndUserid(long userid, String type) {
		String sql = "select count(1) from express_ops_window where type=? and userid=?";
		try {
			return this.jdbcTemplate.queryForLong(sql, type, userid);
		} catch (Exception e) {
			return 0;
		}
	}

	public long getStateAndUserid(long userid, String type) {
		String sql = "select count(1) from express_ops_window where type=? and userid=? and state=1";
		try {
			return this.jdbcTemplate.queryForLong(sql, type, userid);
		} catch (Exception e) {
			return 0;
		}
	}

	public void updateByStateAndUserid(String jsonInfo, long userid) {
		String sql = "update express_ops_window set state='1' , jsonInfo=? where userid=?";
		this.jdbcTemplate.update(sql, jsonInfo, userid);
	}

	public long updateByUserid(String timmer, long userid) {
		try {
			String sql = "update express_ops_window set state=?  where userid=?";
			this.jdbcTemplate.update(sql, timmer, userid);
			return 1;
		} catch (Exception e) {
			return 0;
		}
	}

	public WindowShow getObjectWindowByState(long userid) {
		String sql = "select * from express_ops_window where state=1 and userid=? limit 0,1";
		try {
			return this.jdbcTemplate.queryForObject(sql, new WindowShowRowMapper(), userid);
		} catch (Exception e) {
			return null;
		}

	}

	public List<WindowShow> getCountWindowByType(String type) {
		String sql = "select * from express_ops_window where state=1 and type in (" + type + ")";
		try {
			return this.jdbcTemplate.query(sql, new WindowShowRowMapper());
		} catch (Exception e) {
			return null;
		}

	}

	public long creWindowTime(String jsoninfo, int type, long userid, int state) {
		try {
			this.jdbcTemplate.update("insert into express_ops_window (jsoninfo,userid,type,state) values(?,?,?,?)", jsoninfo, userid, type, state);
			return 1;
		} catch (Exception e) {
			return 0;
		}
	}
}

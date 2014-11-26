package cn.explink.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import cn.explink.domain.MiddleAppear;

@Component
public class MiddleAppearDao {
	private final class MiddleAppearRowMapper implements RowMapper<MiddleAppear> {
		@Override
		public MiddleAppear mapRow(ResultSet rs, int rowNum) throws SQLException {
			MiddleAppear middleappear = new MiddleAppear();
			middleappear.setUserid(rs.getLong("userid"));
			middleappear.setCwb(rs.getString("cwb"));
			middleappear.setState(rs.getString("state"));
			middleappear.setType(rs.getString("type"));
			return middleappear;
		}
	}

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public void creMiddleTime(String cwb, String type, long userid, String state) {
		jdbcTemplate.update("insert into express_mid_opretion_user (cwb,userid,type,state) values(?,?,?,?)", cwb, userid, type, state);
	}

	public long getInfoCount(int type, long userid, String state, String cwb) {
		String sql = "select count(1) from express_mid_opretion_user where userid=? and type=? and state=?";
		if (cwb.length() > 0) {
			sql += " and cwb='" + cwb + "'";
		}
		return jdbcTemplate.queryForLong(sql, userid, type, state);
	}

	public long updateByUserid(String timmer, long userid, String type) {
		try {
			String sql = "update express_mid_opretion_user set state=?  where userid=?";
			if (Integer.parseInt(type) > 0) {
				sql += " and type= " + type;
			}
			jdbcTemplate.update(sql, timmer, userid);
			return 1;
		} catch (Exception e) {
			return 0;
		}
	}
}

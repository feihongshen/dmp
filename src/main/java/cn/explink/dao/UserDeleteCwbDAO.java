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

import cn.explink.domain.UserDeleteCwb;
import cn.explink.util.StringUtil;

@Component
public class UserDeleteCwbDAO {

	private final class UserDeleteCwbRowMapper implements RowMapper<UserDeleteCwb> {
		@Override
		public UserDeleteCwb mapRow(ResultSet rs, int rowNum) throws SQLException {
			UserDeleteCwb userDeleteCwb = new UserDeleteCwb();
			userDeleteCwb.setId(rs.getLong("id"));
			userDeleteCwb.setCheckcode(StringUtil.nullConvertToEmptyString(rs.getString("checkcode")));
			userDeleteCwb.setUserid(rs.getLong("userid"));
			return userDeleteCwb;
		}
	}

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public List<UserDeleteCwb> getUsersByUserid(long userid) {
		return jdbcTemplate.query("SELECT * from ops_userdeletecwb where userid=?", new UserDeleteCwbRowMapper(), userid);
	}

	public void creUserDeleteCwb(final UserDeleteCwb userDeleteCwb) {
		jdbcTemplate.update("insert into ops_userdeletecwb (userid,checkcode) values(?,?)", new PreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				ps.setLong(1, userDeleteCwb.getUserid());
				ps.setString(2, userDeleteCwb.getCheckcode());
			}
		});
	}
}

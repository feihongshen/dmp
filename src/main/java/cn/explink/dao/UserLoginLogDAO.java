package cn.explink.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import cn.explink.domain.Branch;
import cn.explink.domain.UserLoginLog;

/**
 * 用户登录日志
 * 
 * @author wangwei 2016年8月31日
 *
 */
@Component
public class UserLoginLogDAO {

	private final class UserLoginLogMapper implements RowMapper<UserLoginLog> {
		@Override
		public UserLoginLog mapRow(ResultSet rs, int rowNum)
				throws SQLException {
			UserLoginLog userLoginLog = new UserLoginLog();
			userLoginLog.setId(rs.getInt("id"));
			userLoginLog.setUsername(rs.getString("username")); // 用户登录名
			userLoginLog.setLastLoginState(rs.getInt("lastLoginState"));// 上次登录状态（1-成功，0-失败）
			userLoginLog.setLoginFailCount(rs.getInt("loginFailCount"));// 累计连续登录错误次数
			userLoginLog.setLastLoginTryTime(rs.getString("lastLoginTryTime"));// 上次尝试登录时间
			return userLoginLog;
		}
	}

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public void creUserLoginLog(final UserLoginLog userLoginLog) {
		KeyHolder key = new GeneratedKeyHolder();
		jdbcTemplate.update(new PreparedStatementCreator() {
			String sql = "insert into express_ops_user_login_log (username,lastLoginState,loginFailCount,lastLoginTryTime)  values(?,?,?,?)";

			@Override
			public PreparedStatement createPreparedStatement(Connection con)
					throws SQLException {
				PreparedStatement ps = null;
				ps = con.prepareStatement(sql, new String[] { "id" });
				ps.setString(1, userLoginLog.getUsername());
				ps.setInt(2, userLoginLog.getLastLoginState());// 上次登录状态（1-成功，0-失败）
				ps.setInt(3, userLoginLog.getLoginFailCount());// 累计连续登录错误次数
				ps.setString(4, userLoginLog.getLastLoginTryTime());// 上次尝试登录时间
				return ps;
			}
		}, key);
		userLoginLog.setId(key.getKey().longValue());
	}

	public void saveUserLoginLog(final UserLoginLog userLoginLog) {
		this.jdbcTemplate
				.update("update express_ops_user_login_log set username=?,lastLoginState=?,loginFailCount=?,lastLoginTryTime=?"
						+ " where id=? ", new PreparedStatementSetter() {
					@Override
					public void setValues(PreparedStatement ps)
							throws SQLException {
						ps.setString(1, userLoginLog.getUsername());
						ps.setInt(2, userLoginLog.getLastLoginState());// 上次登录状态（1-成功，0-失败）
						ps.setInt(3, userLoginLog.getLoginFailCount());// 累计连续登录错误次数
						ps.setString(4, userLoginLog.getLastLoginTryTime());// 上次尝试登录时间
						ps.setLong(5, userLoginLog.getId());
					}

				});
	}

	public List<UserLoginLog> getUserLoginLogById(long id) {
		String sql = "select * from express_ops_user_login_log where id =?";
		try {
			return this.jdbcTemplate.query(sql, new UserLoginLogMapper(), id);
		} catch (EmptyResultDataAccessException e) {
			return null;
		}

	}

	public UserLoginLog getUserLoginLogByUsername(String username) {
		String sql = "select *  from express_ops_user_login_log where username =?";
		try {
			return this.jdbcTemplate.queryForObject(sql,
					new UserLoginLogMapper(), username);
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

}

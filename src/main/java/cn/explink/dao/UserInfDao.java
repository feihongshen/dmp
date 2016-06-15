package cn.explink.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import cn.explink.domain.UserInf;

/**
 * 小件员接口
 * @author jian.xie
 *
 */
@Component
public class UserInfDao {
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	private final class UserInfRowMapper implements RowMapper<UserInf>{

		@Override
		public UserInf mapRow(ResultSet rs, int rowNum) throws SQLException {
			UserInf userInf = new UserInf();
			userInf.setInfId(rs.getLong("inf_id"));
			userInf.setUserid(rs.getLong("userid"));
			userInf.setUsername(rs.getString("username"));
			userInf.setRealname(rs.getString("realname"));
			userInf.setUsermobile(rs.getString("usermobile"));
			userInf.setPassword(rs.getString("password"));
			userInf.setCreateDate(rs.getDate("create_date"));
			userInf.setCreateUser(rs.getString("create_user"));
			userInf.setIsSync(rs.getBoolean("is_sync"));
			userInf.setStatus(rs.getByte("status"));
			userInf.setBranchid(rs.getLong("branchid"));
			userInf.setOldusername(rs.getString("oldusername"));
			userInf.setTimes(rs.getInt("times"));
			return userInf;
		}		
	}	
	
	public List<UserInf> getUserInfByIsSync(boolean isSync){
		String sql = " select * from express_set_user_inf where is_sync = ? order by inf_id";
		List<UserInf> list = jdbcTemplate.query(sql, new UserInfRowMapper(), isSync);
		return list;
	}
	
	public long saveUserInf(final UserInf userInf){
		KeyHolder key = new GeneratedKeyHolder();
		jdbcTemplate.update(new PreparedStatementCreator(){
			String sql = " insert into express_set_user_inf (inf_id, userid,username, realname, usermobile, password,is_sync,status,create_date,create_user,branchid,oldusername,times) values(?,?,?,?,?,?,?,?,?,?,?,?,?)";
			@Override
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				PreparedStatement ps = null;
				ps = con.prepareStatement(sql, new String[] { "inf_id" });
				ps.setLong(1,  userInf.getInfId());
				ps.setLong(2, userInf.getUserid());
				ps.setString(3, userInf.getUsername() == null ? "" : userInf.getUsername());
				ps.setString(4, userInf.getRealname() == null ? "" : userInf.getRealname());
				ps.setString(5, userInf.getUsermobile() == null ? "" : userInf.getUsermobile());
				ps.setString(6, userInf.getPassword() == null ? "" : userInf.getPassword());
				ps.setBoolean(7, userInf.getIsSync());
				ps.setByte(8, userInf.getStatus());
				ps.setTimestamp(9, new Timestamp(userInf.getCreateDate().getTime()));
				ps.setString(10, userInf.getCreateUser() == null ? "" : userInf.getCreateUser());
				ps.setLong(11, userInf.getBranchid());
				ps.setString(12, userInf.getOldusername() == null ? "" : userInf.getOldusername());
				ps.setInt(13, userInf.getTimes());
				return ps;
			}
		}, key);
		userInf.setInfId(key.getKey().longValue());
		return userInf.getInfId();
	}
	
	/**
	 * 根据主键更新记录为已同步
	 * @param infId
	 * @return
	 */
	public int updateUserInfForIssync(long infId){
		String sql = " update express_set_user_inf set is_sync = 1 where inf_id=? ";
		return jdbcTemplate.update(sql, infId);
	}
	
	/**
	 * 同步次数加1
	 * @param infId
	 * @return
	 */
	public int incrTimes(long infId){
		String sql = " update express_set_user_inf set times = times + 1 where inf_id=?";
		return jdbcTemplate.update(sql, infId); 
	}
	
}

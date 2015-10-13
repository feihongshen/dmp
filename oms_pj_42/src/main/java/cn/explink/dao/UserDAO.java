package cn.explink.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import cn.explink.domain.User;
import cn.explink.util.Page;
import cn.explink.util.StringUtil;

@Component
public class UserDAO {

	private final class UserRowMapper implements RowMapper<User> {
		@Override
		public User mapRow(ResultSet rs, int rowNum) throws SQLException {
			User user = new User();
			user.setUserid(rs.getInt("userid"));
			user.setUsername(rs.getString("username"));
			user.setRealname(StringUtil.nullConvertToEmptyString(rs.getString("realname")));
			user.setPassword(rs.getString("password"));
			user.setBranchid(rs.getLong("branchid"));
			user.setUsercustomerid(rs.getLong("usercustomerid"));
			user.setIdcardno(StringUtil.nullConvertToEmptyString(rs.getString("idcardno")));
			user.setEmployeestatus(rs.getInt("employeestatus"));
			user.setUserphone(StringUtil.nullConvertToEmptyString(rs.getString("userphone")));
			user.setUsermobile(StringUtil.nullConvertToEmptyString(rs.getString("usermobile")));
			user.setUseraddress(StringUtil.nullConvertToEmptyString(rs.getString("useraddress")));
			user.setUserremark(StringUtil.nullConvertToEmptyString(rs.getString("userremark")));
			user.setUsersalary(rs.getBigDecimal("usersalary"));
			user.setShowphoneflag(rs.getLong("showphoneflag"));
			user.setUseremail(StringUtil.nullConvertToEmptyString(rs.getString("useremail")));
			user.setUserwavfile(StringUtil.nullConvertToEmptyString(rs.getString("userwavfile")));
			user.setRoleid(rs.getLong("roleid"));
			user.setUserDeleteFlag(rs.getLong("userDeleteFlag"));
			return user;
		}

	}

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public List<User> getUsersByUsername(String username) {
		List<User> userList = this.jdbcTemplate.query("SELECT * from express_set_user where username=? and userDeleteFlag=1", new UserRowMapper(), username);
		return userList;
	}

	public List<User> getUsersByRealname(String realname) {
		List<User> userList = this.jdbcTemplate.query("SELECT * from express_set_user where realname=? and userDeleteFlag=1", new UserRowMapper(), realname);
		return userList;
	}

	public User getUserByUserid(long userid) {
		User user = this.jdbcTemplate.queryForObject("SELECT * from express_set_user where userid=" + userid + " and userDeleteFlag=1", new UserRowMapper());
		return user;
	}

	public List<User> getUsersByPage(long page, String username, String realname) {
		String sql = "SELECT * from express_set_user ";
		sql = this.getUsersByPageWhereSql(sql, username, realname);
		sql += " order by userid desc limit " + ((page - 1) * Page.ONE_PAGE_NUMBER) + " ," + Page.ONE_PAGE_NUMBER;

		List<User> userList = this.jdbcTemplate.query(sql, new UserRowMapper());
		return userList;
	}

	public long getUserCount(String username, String realname) {
		String sql = "SELECT count(1) from express_set_user";
		sql = this.getUsersByPageWhereSql(sql, username, realname);
		return this.jdbcTemplate.queryForLong(sql);
	}

	private String getUsersByPageWhereSql(String sql, String username, String realname) {
		if ((username.length() > 0) || (realname.length() > 0)) {
			sql += " where ";
			if ((username.length() > 0) && (realname.length() > 0)) {
				sql += " username='" + username + "' and  realname='" + realname + "' and userDeleteFlag=1";
			} else {
				if (username.length() > 0) {
					sql += " username='" + username + "' and userDeleteFlag=1";
				}
				if (realname.length() > 0) {
					sql += " realname='" + realname + "' and userDeleteFlag=1";
				}
			}
		}
		return sql;
	}

	public void creUser(final User user) {
		this.jdbcTemplate.update("insert into express_set_user (username,password,realname,idcardno," + "employeestatus,branchid,userphone,usermobile,useraddress,userremark,usersalary,"
				+ "usercustomerid,showphoneflag,useremail,userwavfile,roleid) " + "values(?,?,?,?,?,?,?,?,?,?, ?,?,?,?,?,? )", new PreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				ps.setString(1, user.getUsername());
				ps.setString(2, user.getPassword());
				ps.setString(3, user.getRealname());
				ps.setString(4, user.getIdcardno());
				ps.setInt(5, user.getEmployeestatus());
				ps.setLong(6, user.getBranchid());
				ps.setString(7, user.getUserphone());
				ps.setString(8, user.getUsermobile());
				ps.setString(9, user.getUseraddress());
				ps.setString(10, user.getUserremark());
				ps.setBigDecimal(11, user.getUsersalary());
				ps.setLong(12, user.getUsercustomerid());
				ps.setLong(13, user.getShowphoneflag());
				ps.setString(14, user.getUseremail());
				ps.setString(15, user.getUserwavfile());
				ps.setLong(16, user.getRoleid());
			}

		});
	}

	public void saveUser(final User user) {
		this.jdbcTemplate.update("update express_set_user set username=?,password=?,realname=?,idcardno=?,"
				+ "employeestatus=?,branchid=?,userphone=?,usermobile=?,useraddress=?,userremark=?,usersalary=?," + "usercustomerid=?,showphoneflag=?,useremail=?,userwavfile=?,roleid=? "
				+ "where userid=? and userDeleteFlag=1", new PreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				ps.setString(1, user.getUsername());
				ps.setString(2, user.getPassword());
				ps.setString(3, user.getRealname());
				ps.setString(4, user.getIdcardno());
				ps.setInt(5, user.getEmployeestatus());
				ps.setLong(6, user.getBranchid());
				ps.setString(7, user.getUserphone());
				ps.setString(8, user.getUsermobile());
				ps.setString(9, user.getUseraddress());
				ps.setString(10, user.getUserremark());
				ps.setBigDecimal(11, user.getUsersalary());

				ps.setLong(12, user.getUsercustomerid());
				ps.setLong(13, user.getShowphoneflag());
				ps.setString(14, user.getUseremail());
				ps.setString(15, user.getUserwavfile());
				ps.setLong(16, user.getRoleid());
				ps.setLong(17, user.getUserid());
			}

		});
	}

	public List<User> getUserByRole(int roleid) {
		String sql = "SELECT * FROM express_set_user WHERE roleid=" + roleid + " and userDeleteFlag=1";
		List<User> userList = this.jdbcTemplate.query(sql, new UserRowMapper());
		return userList;
	}

	public User getUsersByRealnameAndRole(String realname, int roleid) {
		User user = this.jdbcTemplate.queryForObject("SELECT * from express_set_user where realname=? and roleid=? and userDeleteFlag=1", new UserRowMapper(), realname, roleid);
		return user;
	}

	public List<User> getAllUser() {
		String sql = "select * from express_set_user where userDeleteFlag=1 ";
		List<User> userList = this.jdbcTemplate.query(sql, new UserRowMapper());
		return userList;
	}

	public List<User> getUserByRoleAndBranchid(int roleid, long branchid) {
		String sql = "SELECT * FROM express_set_user WHERE roleid=" + roleid + " and branchid=" + branchid + " and userDeleteFlag=1";
		return this.jdbcTemplate.query(sql, new UserRowMapper());
	}

	public User getSingelUsersByRealname(String realname) {
		try {
			String sql = "select * from express_set_user where realname='" + realname + "' and userDeleteFlag=1";
			return this.jdbcTemplate.queryForObject(sql, new UserRowMapper());
		} catch (EmptyResultDataAccessException ee) {
			return null;
		}

	}

}

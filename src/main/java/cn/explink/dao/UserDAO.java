package cn.explink.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import cn.explink.aspect.SystemInstallOperation;
import cn.explink.domain.User;
import cn.explink.enumutil.UserEmployeestatusEnum;
import cn.explink.util.Page;
import cn.explink.util.StringUtil;

@Component
public class UserDAO {

	private Logger logger = LoggerFactory.getLogger(UserDAO.class);

	private final class UserRowMapper implements RowMapper<User> {
		@Override
		public User mapRow(ResultSet rs, int rowNum) throws SQLException {
			User user = new User();
			user.setUserid(rs.getInt("userid"));
			user.setUsername(rs.getString("username"));
			user.setRealname(StringUtil.nullConvertToEmptyString(rs.getString("realname")));
			user.setPassword(rs.getString("password"));//POS登录密码
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
			user.setShownameflag(rs.getLong("shownameflag"));
			user.setShowmobileflag(rs.getLong("showmobileflag"));
			user.setUseremail(StringUtil.nullConvertToEmptyString(rs.getString("useremail")));
			user.setUserwavfile(StringUtil.nullConvertToEmptyString(rs.getString("userwavfile")));
			user.setRoleid(rs.getLong("roleid"));
			user.setUserDeleteFlag(rs.getLong("userDeleteFlag"));
			user.setDeliverManCode(rs.getString("deliverManCode"));
			user.setDeliverAccount(rs.getBigDecimal("deliverAccount"));
			user.setDeliverPosAccount(rs.getBigDecimal("deliverPosAccount"));
			user.setIsImposedOutWarehouse(rs.getInt("isImposedOutWarehouse"));
			user.setLastLoginIp(rs.getString("lastip"));
			user.setLastLoginTime(rs.getString("lasttime"));
			user.setPfruleid(rs.getLong("pfruleid"));
			user.setSex(rs.getInt("sex"));// 性别
			user.setStartworkdate(rs.getString("startworkdate"));// 入职时间
			user.setJobnum(StringUtil.nullConvertToEmptyString(rs.getString("jobnum")));// 工号
			user.setJiesuanstate(rs.getInt("jiesuanstate"));// 结算状态
			user.setMaxcutpayment(rs.getBigDecimal("maxcutpayment"));// 最高扣款额度
			user.setFixedadvance(rs.getBigDecimal("fixedadvance"));// 固定预付款
			user.setBasicadvance(rs.getBigDecimal("basicadvance"));// 基础预付款
			user.setFallbacknum(rs.getLong("fallbacknum"));// 保底单量
			user.setLateradvance(rs.getBigDecimal("lateradvance"));// 后期预付款
			user.setBasicfee(rs.getBigDecimal("basicfee"));// 基本派费
			user.setAreafee(rs.getBigDecimal("areafee"));// 区域派费
			user.setWebPassword(rs.getString("webPassword"));//网页登录密码
			user.setLastLoginState(rs.getInt("lastLoginState"));// 上次登录状态（1-成功，0-失败）
			user.setLoginFailCount(rs.getInt("loginFailCount"));// 累计连续登录错误次数
			user.setLastLoginTryTime(rs.getString("lastLoginTryTime"));// 上次尝试登录时间
			return user;
		}

	}

	/**
	 *
	 * @description
	 * @author 刘武强
	 * @data 2015年10月13日
	 */
	private final class ImportUserRowMapper implements RowMapper<User> {
		@Override
		public User mapRow(ResultSet rs, int rowNum) throws SQLException {
			User user = new User();
			user.setUserid(rs.getInt("userid"));
			user.setUsername(rs.getString("username"));
			user.setRealname(StringUtil.nullConvertToEmptyString(rs.getString("realname")));
			user.setBranchid(rs.getLong("branchid"));
			return user;
		}

	}

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public List<User> getUsersByUsername(String username) {
		List<User> userList = this.jdbcTemplate.query("SELECT * from express_set_user where username=? and userDeleteFlag=1", new UserRowMapper(), username);
		return userList;
	}

	public User getUserByUsername(String username) {
		try {
			return this.jdbcTemplate.queryForObject("SELECT * from express_set_user where username=? and userDeleteFlag=1", new UserRowMapper(), username);
		} catch (Exception e) {
			return null;
		}

	}

	public User getUserByJobnum(String username) {
		try {
			return this.jdbcTemplate.queryForObject("SELECT * from express_set_user where jobnum=? and userDeleteFlag=1", new UserRowMapper(), username);
		} catch (Exception e) {
			return null;
		}

	}

	public List<User> getUsersByUsernameLogin(String username) {
		List<User> userList = this.jdbcTemplate.query("SELECT * from express_set_user where username=? and userDeleteFlag=1 and employeestatus<>3", new UserRowMapper(), username);
		return userList;
	}

	public List<User> getUsersByRealname(String realname) {
		List<User> userList = this.jdbcTemplate.query("SELECT * from express_set_user where realname=? and userDeleteFlag=1 ", new UserRowMapper(), realname);
		return userList;
	}

	@Cacheable(value = "userCache", key = "#userid")
	public User getUserByUserid(long userid) {
		User user = new User();
		try {
			user = this.jdbcTemplate.queryForObject("SELECT * from express_set_user where userid=? and userDeleteFlag=1 ", new UserRowMapper(), userid);
		} catch (Exception e) {
			user = new User();
		}
		return user;
	}

	public List<User> getUserByid(long userid) {
		return this.jdbcTemplate.query("SELECT * from express_set_user where userid=? and userDeleteFlag=1 ", new UserRowMapper(), userid);
	}

	public User getAllUserByid(long userid) {
		try {
			return this.jdbcTemplate.queryForObject("SELECT * from express_set_user where userid=?", new UserRowMapper(), userid);
		} catch (DataAccessException e) {
			return new User();
		}
	}

	public User getUserByidAdd(long userid) {
		try {
			return this.jdbcTemplate.queryForObject("SELECT * from express_set_user where userid=?", new UserRowMapper(), userid);
		} catch (DataAccessException e) {
			return null;
		}
	}

	public List<User> getUsersByPage(long page, String username, String realname, long branchid, long roleid) {
		String sql = "SELECT * from express_set_user ";
		sql = this.getUsersByPageWhereSql(sql, username, realname, branchid, roleid);
		sql += " order by CONVERT( realname USING gbk ) COLLATE gbk_chinese_ci ASC limit " + ((page - 1) * Page.ONE_PAGE_NUMBER) + " ," + Page.ONE_PAGE_NUMBER;

		List<User> userList = this.jdbcTemplate.query(sql, new UserRowMapper());
		return userList;
	}

	public long getUserCount(String username, String realname, long branchid, long roleid) {
		String sql = "SELECT count(1) from express_set_user";
		sql = this.getUsersByPageWhereSql(sql, username, realname, branchid, roleid);
		return this.jdbcTemplate.queryForLong(sql);
	}

	public List<User> getUsersForUserBranchByPage(long page, String username, String realname, long branchid) {
		String sql = "SELECT * from express_set_user where roleid in(2) ";

		if ((username.length() > 0) || (realname.length() > 0)) {
			sql += " and ";
			if ((username.length() > 0) && (realname.length() > 0)) {
				sql += " username like '%" + username + "%' and  realname like '%" + realname + "%' and userDeleteFlag=1 ";
			} else {
				if (username.length() > 0) {
					sql += " username like '%" + username + "%' and userDeleteFlag=1 ";
				}
				if (realname.length() > 0) {
					sql += " realname like '%" + realname + "%' and userDeleteFlag=1 ";
				}
			}

		}
		if (branchid > -1) {
			sql += " and branchid=" + branchid;
		}
		sql += " order by CONVERT( realname USING gbk ) COLLATE gbk_chinese_ci ASC limit " + ((page - 1) * Page.ONE_PAGE_NUMBER) + " ," + Page.ONE_PAGE_NUMBER;

		List<User> userList = this.jdbcTemplate.query(sql, new UserRowMapper());
		return userList;
	}

	public long getUserForUserBranchCount(String username, String realname, long branchid) {
		String sql = "SELECT count(1) from express_set_user where roleid in(2) ";
		if ((username.length() > 0) || (realname.length() > 0)) {
			sql += " and ";
			if ((username.length() > 0) && (realname.length() > 0)) {
				sql += " username like '%" + username + "%' and  realname like '%" + realname + "%' and userDeleteFlag=1 ";
			} else {
				if (username.length() > 0) {
					sql += " username like '%" + username + "%' and userDeleteFlag=1 ";
				}
				if (realname.length() > 0) {
					sql += " realname like '%" + realname + "%' and userDeleteFlag=1 ";
				}
			}

		}
		if (branchid > -1) {
			sql += " and branchid=" + branchid;
		}
		return this.jdbcTemplate.queryForLong(sql);
	}

	private String getUsersByPageWhereSql(String sql, String username, String realname, long branchid, long roleid) {
		sql += " where 1=1 ";
		if ((username.length() > 0) || (realname.length() > 0)) {
			sql += " and ";
			if ((username.length() > 0) && (realname.length() > 0)) {
				sql += " username like '%" + username + "%' and  realname like '%" + realname + "%' and userDeleteFlag=1 ";
			} else {
				if (username.length() > 0) {
					sql += " username like '%" + username + "%' and userDeleteFlag=1 ";
				}
				if (realname.length() > 0) {
					sql += " realname like '%" + realname + "%' and userDeleteFlag=1 ";
				}
			}

		}
		if (branchid > -1) {
			sql += " and branchid=" + branchid;
		}

		if (roleid > -1) {
			sql += " and roleid=" + roleid;
		}
		return sql;
	}

	public List<User> findDeliveryManinfoBybranchid(int branchid) {
		String sql = "select * from express_set_user where branchid=?";
		return this.jdbcTemplate.query(sql, new UserRowMapper(), branchid);
	}

	@SystemInstallOperation
	public void creUser(final User user) {		
		KeyHolder key = new GeneratedKeyHolder();
		jdbcTemplate.update(new PreparedStatementCreator() {
			String sql = "insert into express_set_user (username,password,realname,idcardno,employeestatus,branchid,userphone,usermobile,useraddress,userremark,usersalary,usercustomerid,showphoneflag,useremail,userwavfile,roleid,isImposedOutWarehouse,shownameflag,showmobileflag,pfruleid,sex,startworkdate,jobnum,jiesuanstate,maxcutpayment,fixedadvance,basicadvance,fallbacknum,lateradvance,basicfee,areafee,webPassword,lastLoginState,loginFailCount,lastLoginTryTime)  values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			@Override
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				PreparedStatement ps = null;
				ps = con.prepareStatement(sql, new String[] { "userid"});
				ps.setString(1, user.getUsername());
				ps.setString(2, user.getPassword());//POS登录密码
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
				ps.setInt(17, user.getIsImposedOutWarehouse());
				ps.setLong(18, user.getShownameflag());
				ps.setLong(19, user.getShowmobileflag());
				ps.setLong(20, user.getPfruleid());
				ps.setInt(21, user.getSex());
				ps.setString(22, user.getStartworkdate());
				ps.setString(23, user.getJobnum());
				ps.setInt(24, user.getJiesuanstate());
				ps.setBigDecimal(25, user.getMaxcutpayment());
				ps.setBigDecimal(26, user.getFixedadvance());
				ps.setBigDecimal(27, user.getBasicadvance());
				ps.setLong(28, user.getFallbacknum());
				ps.setBigDecimal(29, user.getLateradvance());
				ps.setBigDecimal(30, user.getBasicfee());// 基本派费
				ps.setBigDecimal(31, user.getAreafee());// 区域派费
				ps.setString(32, user.getWebPassword());//网页登录密码
				ps.setInt(33, user.getLastLoginState());// 上次登录状态（1-成功，0-失败）
				ps.setInt(34, user.getLoginFailCount());// 累计连续登录错误次数
				ps.setString(35, user.getLastLoginTryTime());// 上次尝试登录时间
				return ps;
			}
		}, key);		
		user.setUserid(key.getKey().longValue());
		
//		this.jdbcTemplate
//				.update("insert into express_set_user (username,password,realname,idcardno," + "employeestatus,branchid,userphone,usermobile,useraddress,userremark,usersalary," + "usercustomerid,showphoneflag,useremail,userwavfile,roleid,isImposedOutWarehouse,shownameflag,showmobileflag,pfruleid,sex,startworkdate,jobnum,jiesuanstate,maxcutpayment," + "fixedadvance,basicadvance,fallbacknum,lateradvance,basicfee,areafee,webPassword) " + "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)", new PreparedStatementSetter() {
//					@Override
//					public void setValues(PreparedStatement ps) throws SQLException {
//						ps.setString(1, user.getUsername());
//						ps.setString(2, user.getPassword());//POS登录密码
//						ps.setString(3, user.getRealname());
//						ps.setString(4, user.getIdcardno());
//						ps.setInt(5, user.getEmployeestatus());
//						ps.setLong(6, user.getBranchid());
//						ps.setString(7, user.getUserphone());
//						ps.setString(8, user.getUsermobile());
//						ps.setString(9, user.getUseraddress());
//						ps.setString(10, user.getUserremark());
//						ps.setBigDecimal(11, user.getUsersalary());
//						ps.setLong(12, user.getUsercustomerid());
//						ps.setLong(13, user.getShowphoneflag());
//						ps.setString(14, user.getUseremail());
//						ps.setString(15, user.getUserwavfile());
//						ps.setLong(16, user.getRoleid());
//						ps.setInt(17, user.getIsImposedOutWarehouse());
//						ps.setLong(18, user.getShownameflag());
//						ps.setLong(19, user.getShowmobileflag());
//						ps.setLong(20, user.getPfruleid());
//						ps.setInt(21, user.getSex());
//						ps.setString(22, user.getStartworkdate());
//						ps.setString(23, user.getJobnum());
//						ps.setInt(24, user.getJiesuanstate());
//						ps.setBigDecimal(25, user.getMaxcutpayment());
//						ps.setBigDecimal(26, user.getFixedadvance());
//						ps.setBigDecimal(27, user.getBasicadvance());
//						ps.setLong(28, user.getFallbacknum());
//						ps.setBigDecimal(29, user.getLateradvance());
//						ps.setBigDecimal(30, user.getBasicfee());// 基本派费
//						ps.setBigDecimal(31, user.getAreafee());// 区域派费
//						ps.setString(32, user.getWebPassword());//网页登录密码
//					}
//
//				});
	}

	@SystemInstallOperation
	@CacheEvict(value = "userCache", key = "#user.userid")
	public void saveUser(final User user) {
		this.jdbcTemplate
				.update("update express_set_user set username=?,password=?,realname=?,idcardno=?," + "employeestatus=?,branchid=?,userphone=?,usermobile=?,useraddress=?,userremark=?,usersalary=?," + "usercustomerid=?,showphoneflag=?,useremail=?,userwavfile=?,roleid=?,isImposedOutWarehouse=?,shownameflag=?," + "showmobileflag=?,pfruleid=?,sex=?,startworkdate=?,jobnum=?,jiesuanstate=?,maxcutpayment=?,fixedadvance=?," + "basicadvance=?,fallbacknum=?,lateradvance=?,basicfee=?,areafee=?,webPassword=?,lastLoginState=?,loginFailCount=?,lastLoginTryTime=?" + " where userid=? and userDeleteFlag=1 ", new PreparedStatementSetter() {
					@Override
					public void setValues(PreparedStatement ps) throws SQLException {
						ps.setString(1, user.getUsername());
						ps.setString(2, user.getPassword());//POS登录密码
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
						ps.setInt(17, user.getIsImposedOutWarehouse());
						ps.setLong(18, user.getShownameflag());
						ps.setLong(19, user.getShowmobileflag());
						ps.setLong(20, user.getPfruleid());
						ps.setInt(21, user.getSex());
						ps.setString(22, user.getStartworkdate());
						ps.setString(23, user.getJobnum());
						ps.setInt(24, user.getJiesuanstate());
						ps.setBigDecimal(25, user.getMaxcutpayment());
						ps.setBigDecimal(26, user.getFixedadvance());
						ps.setBigDecimal(27, user.getBasicadvance());
						ps.setLong(28, user.getFallbacknum());
						ps.setBigDecimal(29, user.getLateradvance());
						ps.setBigDecimal(30, user.getBasicfee());// 基本派费
						ps.setBigDecimal(31, user.getAreafee());// 区域派费
						ps.setString(32, user.getWebPassword());//网页登录密码
						ps.setInt(33, user.getLastLoginState());// 上次登录状态（1-成功，0-失败）
						ps.setInt(34, user.getLoginFailCount());// 累计连续登录错误次数
						ps.setString(35, user.getLastLoginTryTime());// 上次尝试登录时间
						ps.setLong(36, user.getUserid());
					}

				});
	}

	public List<User> getUserByRole(int roleid) {
		String sql = "SELECT * FROM express_set_user WHERE roleid=? and userDeleteFlag=1 and employeestatus=1";
		List<User> userList = this.jdbcTemplate.query(sql, new UserRowMapper(), roleid);
		return userList;
	}
	
	public List<User> getAllUserByRole(List<Long> roleidList) {
		StringBuilder roleidsSb = new StringBuilder("");
		for(int i = 0; i < roleidList.size();i++) {
			Long roleid = roleidList.get(i);
			if(i != 0) {
				roleidsSb.append(",");
			}
			roleidsSb.append(roleid);
		}
		String roleids = roleidsSb.toString();
		String sql = "SELECT * FROM express_set_user WHERE roleid in(" + roleids + " )";
		List<User> userList = this.jdbcTemplate.query(sql, new UserRowMapper());
		return userList;
	}

	public List<User> getUserByRole(String roleids, long branchid) {
		String sql = "SELECT * FROM express_set_user WHERE  branchid=? and roleid in(" + roleids + " ) and userDeleteFlag=1 and employeestatus=1";
		List<User> userList = this.jdbcTemplate.query(sql, new UserRowMapper(), branchid);
		return userList;
	}

	public User getUsersByRealnameAndRole(String realname, int roleid) {
		try {
			User user = this.jdbcTemplate
					.queryForObject("SELECT * from express_set_user where realname=? and roleid=? and userDeleteFlag=1 and employeestatus=1", new UserRowMapper(), realname, roleid);
			return user;
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	public User getUsersByRealnameAndRole(String realname, String roleids) {
		try {
			String sql = "SELECT * from express_set_user where realname=? and roleid in(" + roleids + ") and userDeleteFlag=1 and employeestatus=1";
			User user = this.jdbcTemplate.queryForObject(sql, new UserRowMapper(), realname);
			return user;
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	public List<User> getAllUser() {
		String sql = "select * from express_set_user where userDeleteFlag=1 and employeestatus=1";
		List<User> userList = this.jdbcTemplate.query(sql, new UserRowMapper());
		return userList;
	}

	public List<User> getAllUsers() {
		String sql = "select * from express_set_user where userDeleteFlag=1";
		List<User> userList = this.jdbcTemplate.query(sql, new UserRowMapper());
		return userList;
	}

	public List<User> getAllUserByuserDeleteFlag() {
		String sql = "select * from express_set_user where userDeleteFlag=1 order by CONVERT( realname USING gbk ) COLLATE gbk_chinese_ci ASC ";
		List<User> userList = this.jdbcTemplate.query(sql, new UserRowMapper());
		return userList;
	}

	public List<User> getAllUserOrderByBranchid() {
		String sql = "select * from express_set_user where userDeleteFlag=1 and employeestatus=1 order by branchid ";
		List<User> userList = this.jdbcTemplate.query(sql, new UserRowMapper());
		return userList;
	}

	public List<User> getAllUserbybranchid(long branchid) {
		String sql = "select * from express_set_user where branchid=" + branchid + " and userDeleteFlag=1 and employeestatus=1";
		List<User> userList = this.jdbcTemplate.query(sql, new UserRowMapper());
		return userList;
	}

	public List<User> getAllUserbybranchidByUserDeleteFlag(long branchid) {
		String sql = "select * from express_set_user where branchid=" + branchid + " and userDeleteFlag=1 ";
		List<User> userList = this.jdbcTemplate.query(sql, new UserRowMapper());
		return userList;
	}

	public List<User> getAllUserbybranchids(String branchids) {
		String sql = "select * from express_set_user where branchid in(0" + branchids + ") and userDeleteFlag=1 and employeestatus=1";
		List<User> userList = this.jdbcTemplate.query(sql, new UserRowMapper());
		return userList;
	}

	public List<User> getUserByRoleAndBranchid(int roleid, long branchid) {
		try {
			String sql = "SELECT * FROM express_set_user WHERE roleid=? and branchid=? and userDeleteFlag=1 and employeestatus=1";
			return this.jdbcTemplate.query(sql, new UserRowMapper(), roleid, branchid);
		} catch (EmptyResultDataAccessException ee) {
			return null;
		}
	}
	
	public List<User> getUserByRoleAndBranchName(int roleid, String branchName) {
		try {
			String sql = "SELECT * FROM express_set_user u, express_set_branch b WHERE u.branchid = b.branchid and u.roleid=? and b.branchname=? and u.userDeleteFlag=1 and u.employeestatus=1";
			return this.jdbcTemplate.query(sql, new UserRowMapper(), roleid, branchName);
		} catch (EmptyResultDataAccessException ee) {
			return null;
		}
	}

	public List<User> getUserByRolesAndBranchid(String roleids, long branchid) {
		try {
			String sql = "SELECT * FROM express_set_user WHERE roleid in(" + roleids + ") and branchid=" + branchid + " and userDeleteFlag=1 and employeestatus=" + UserEmployeestatusEnum.GongZuo
					.getValue() + " ORDER BY CONVERT( realname USING gbk ) COLLATE gbk_chinese_ci ASC ";
			return this.jdbcTemplate.query(sql, new UserRowMapper());
		} catch (EmptyResultDataAccessException ee) {
			return null;
		}
	}

	/**
	 *
	 * @Title: getAllUserByRoleidsAndBranchid
	 * @description 通过角色编号和站点id获取小件员（工作、休假、离职的都查出来）
	 * @author 刘武强
	 * @date  2016年1月20日下午3:42:00
	 * @param  @param roleids
	 * @param  @param branchid
	 * @param  @return
	 * @return  List<User>
	 * @throws
	 */
	public List<User> getAllUserByRoleidsAndBranchid(String roleids, long branchid) {
		try {
			String sql = "SELECT * FROM express_set_user WHERE roleid in(" + roleids + ") and branchid=" + branchid + " and userDeleteFlag=1  ORDER BY CONVERT( realname USING gbk ) COLLATE gbk_chinese_ci ASC ";
			return this.jdbcTemplate.query(sql, new UserRowMapper());
		} catch (EmptyResultDataAccessException ee) {
			return null;
		}
	}

	/**
	 *
	 * @Title: getUserByRolesAndBranchidAndDeliveryManNames
	 * @description 查询某站点（branchid）下名字在DeliveryManNames字符串内的揽件员
	 * @author 刘武强
	 * @date 2015年10月12日下午1:20:08
	 * @param @param roleids
	 * @param @param branchid
	 * @param @return
	 * @return List<User>
	 * @throws
	 */
	public List<User> getUserByRolesAndBranchidAndDeliveryManNames(String roleids, long branchid, String DeliveryManNames) {
		try {
			String sql = "SELECT * FROM express_set_user WHERE realname in  " + DeliveryManNames + "  and roleid in(" + roleids + ") and branchid=" + branchid + " and userDeleteFlag=1 and employeestatus=" + UserEmployeestatusEnum.GongZuo
					.getValue() + " ORDER BY CONVERT( realname USING gbk ) COLLATE gbk_chinese_ci ASC ";
			return this.jdbcTemplate.query(sql, new ImportUserRowMapper());
		} catch (EmptyResultDataAccessException ee) {
			return null;
		}
	}

	public List<User> getDeliveryUserByRolesAndBranchid(String roleids, long branchid) {
		try {
			String sql = "SELECT * FROM express_set_user u LEFT JOIN express_set_role_new r ON u.roleid=r.roleid " + "WHERE (r.isdelivery=1 or u.roleid in(" + roleids + ")) and u.branchid=" + branchid + " and u.userDeleteFlag=1 " + "and u.employeestatus=" + UserEmployeestatusEnum.GongZuo
					.getValue() + " ORDER BY CONVERT( u.realname USING gbk ) COLLATE gbk_chinese_ci ASC ";
			return this.jdbcTemplate.query(sql, new UserRowMapper());
		} catch (EmptyResultDataAccessException ee) {
			return null;
		}
	}

	public List<User> getAllUserByRolesAndBranchids(String roleids, String branchids) {
		try {
			String sql = "SELECT * FROM express_set_user WHERE roleid in(" + roleids + ") and branchid in(" + branchids + ") and userDeleteFlag=1 ";
			return this.jdbcTemplate.query(sql, new UserRowMapper());
		} catch (EmptyResultDataAccessException ee) {
			return null;
		}
	}
	
	/**
	 * 获取非离职的该站上的小件员与站长
	 * @author jian.xie 
	 * @date 2016-07-13
	 * @param branchids
	 * @return
	 */
	public List<User> getAllUserByRolesAndBranchids(String branchids) {
		try {
			String sql = "SELECT * FROM express_set_user WHERE roleid in(2,4) and branchid in(" + branchids + ") and userDeleteFlag=1 and employeestatus != 3 ";
			return this.jdbcTemplate.query(sql, new UserRowMapper());
		} catch (EmptyResultDataAccessException ee) {
			return null;
		}
	}

	public List<User> getAllUserByRolesAndBranchid(String roleids, long branchid) {
		try {
			String sql = "SELECT * FROM express_set_user WHERE roleid in(" + roleids + ") and branchid=? and userDeleteFlag=1 ";
			return this.jdbcTemplate.query(sql, new UserRowMapper(), branchid);
		} catch (EmptyResultDataAccessException ee) {
			return null;
		}
	}

	public User getSingelUsersByRealname(String realname) {
		try {
			String sql = "select * from express_set_user where realname=? and userDeleteFlag=1 and employeestatus=1";
			return this.jdbcTemplate.queryForObject(sql, new UserRowMapper(), realname);
		} catch (EmptyResultDataAccessException ee) {
			return null;
		}

	}

	public List<User> getUserByBranchid(long branchid) {
		try {
			String sql = "SELECT * FROM express_set_user WHERE branchid=? and userDeleteFlag=1 and employeestatus=1";
			return this.jdbcTemplate.query(sql, new UserRowMapper(), branchid);
		} catch (EmptyResultDataAccessException ee) {
			return null;
		}
	}

	public List<User> getUserByBranchids(String branchids) {
		try {
			String sql = "SELECT * FROM express_set_user WHERE branchid in(" + branchids + ") and userDeleteFlag=1 and employeestatus=1";
			return this.jdbcTemplate.query(sql, new UserRowMapper());
		} catch (EmptyResultDataAccessException ee) {
			return null;
		}
	}

	public List<User> getAllUserByBranchid(long branchid) {
		try {
			String sql = "SELECT * FROM express_set_user WHERE branchid=? and userDeleteFlag=1 ";
			return this.jdbcTemplate.query(sql, new UserRowMapper(), branchid);
		} catch (EmptyResultDataAccessException ee) {
			return null;
		}
	}

	// 根据branchid逗号隔开查询。
	public List<User> getUserByBranchIds(String branchids) {
		List<User> list = new ArrayList<User>();
		String sql = "SELECT * FROM express_set_user WHERE branchid in (" + branchids + ") and userDeleteFlag=1 and employeestatus=1 ";
		try {
			list = this.jdbcTemplate.query(sql, new UserRowMapper());
		} catch (DataAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}

	// 根据branchid逗号隔开查询。
	public List<User> getAllUserByBranchIds(String branchids) {
		List<User> list = new ArrayList<User>();
		String sql = "SELECT * FROM express_set_user WHERE branchid in (" + branchids + ") and userDeleteFlag=1 ";
		try {
			list = this.jdbcTemplate.query(sql, new UserRowMapper());
		} catch (DataAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}

	/**
	 * 根据站点 查询该站点下所有的非离职的小件员
	 *
	 * @param branchids
	 * @return
	 */
	public List<User> queryAllUserByBranchId(long branchid) {
		List<User> list = new ArrayList<User>();
		String sql = "SELECT * FROM express_set_user WHERE branchid = '" + branchid + "' and employeestatus not in(" + UserEmployeestatusEnum.LiZhi.getValue() + ") and userDeleteFlag=1 and roleid in(2,4)";
		try {
			list = this.jdbcTemplate.query(sql, new UserRowMapper());
		} catch (DataAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}

	// 根据branchid逗号隔开查询。
	public List<User> getAllUserByBranchIdsAndRoleid(String branchids, long roleid) {
		String sql = "SELECT * FROM express_set_user WHERE branchid in (" + branchids + ") and userDeleteFlag=1 and roleid=?";
		return this.jdbcTemplate.query(sql, new UserRowMapper(), roleid);
	}

	public String getDeliveryUserNameByCwb(String cwb) {
		String username = "";
		long deliverid = 0;
		Map<String, Object> userMap = null;
		try {
			String sql = "select deliverid from express_ops_cwb_detail where  cwb=? and state=1 ";
			deliverid = this.jdbcTemplate.queryForLong(sql, cwb);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (deliverid != 0) {
			String usersql = "select username from express_set_user where userid=" + deliverid;
			userMap = this.jdbcTemplate.queryForMap(usersql);
			username = userMap.get("username").toString();
		}
		this.logger.info("POS部分查询订单号cwb:" + cwb + ",username:" + username);
		return username;

	}

	public Map<Long, String> getAllDeliverMap() {
		String sql = "select userid , realname from express_set_user where roleid in (2 , 4)";
		final Map<Long, String> deliverMap = new HashMap<Long, String>();
		this.jdbcTemplate.query(sql, new RowCallbackHandler() {

			@Override
			public void processRow(ResultSet rs) throws SQLException {
				deliverMap.put(rs.getLong("userid"), rs.getString("realname"));
			}
		});
		return deliverMap;
	}

	public List<User> getUserListByBranchid(long branchid, long deliveryid) {
		String sql = "SELECT * FROM express_set_user WHERE branchid=?";
		if (deliveryid > 0) {
			sql += " and userid =" + deliveryid;
		}
		sql += " and userDeleteFlag=1 and employeestatus=1 and roleid in (4,2)";
		return this.jdbcTemplate.query(sql, new UserRowMapper(), branchid);
	}

	/**
	 * 修改用户编码
	 *
	 * @param user
	 */
	@SystemInstallOperation
	@CacheEvict(value = "userCache", key = "#userid")
	public void updateUserCode(String deliverManCode, long userid) {
		String sql = "update express_set_user set deliverManCode=? where userid=?";
		this.jdbcTemplate.update(sql, deliverManCode, userid);
	}

	/**
	 * 获得用户信息，并锁住用户表的行
	 *
	 * @param userid
	 * @return
	 */
	public User getUserByUseridLock(long userid) {
		return this.jdbcTemplate.queryForObject("SELECT * from express_set_user where userid=? for update ", new UserRowMapper(), userid);
	}

	/**
	 * 修改小件员帐户余额
	 *
	 * @param userid
	 * @param deliverAccount
	 * @param deliverPosAccount
	 */
	@SystemInstallOperation
	@CacheEvict(value = "userCache", key = "#userid")
	public void updateUserAmount(long userid, BigDecimal deliverAccount, BigDecimal deliverPosAccount) {
		String sql = "update express_set_user set deliverAccount=?, deliverPosAccount=? where userid=?";
		this.jdbcTemplate.update(sql, deliverAccount, deliverPosAccount, userid);
	}

	/**
	 * 小件员帐户列表
	 *
	 * @param page
	 * @param branchid
	 * @param realname
	 * @param deliverAccountStartStr
	 * @param deliverAccountEndStr
	 * @param deliverPosAccountStartStr
	 * @param deliverPosAccountEndStr
	 */
	public List<User> getDeliverAccountList(long page, long branchid, String realname, String deliverAccountStartStr, String deliverAccountEndStr, String deliverPosAccountStartStr, String deliverPosAccountEndStr, int uee) {
		String sql = "SELECT * from express_set_user where roleid in(2,4)";
		sql += this.getDeliverAccountListWhere(branchid, realname, deliverAccountStartStr, deliverAccountEndStr, deliverPosAccountStartStr, deliverPosAccountEndStr, uee);
		sql += " order by deliverAccount asc , deliverPosAccount asc limit " + ((page - 1) * Page.ONE_PAGE_NUMBER) + " ," + Page.ONE_PAGE_NUMBER;
		return this.jdbcTemplate.query(sql, new UserRowMapper());

	}

	public JSONObject getAllGruopNumWorker() {
		String sql = "SELECT branchid ,count(*) as col2  from express_set_user where employeestatus <>3 and  userDeleteFlag=1 group by branchid";
		JSONObject us = (JSONObject) this.jdbcTemplate.query(sql, new ResultSetExtractor() {
			@Override
			public Object extractData(ResultSet rs) throws SQLException {
				JSONObject us = new JSONObject();
				while (rs.next()) {
					long col1 = rs.getLong("branchid");
					BigDecimal col2 = rs.getBigDecimal("col2");
					us.put(col1, col2);
				}
				return us;
			};
		});
		return us;
	}

	/**
	 * 小件员帐户列表
	 *
	 * @param page
	 * @param branchid
	 * @param realname
	 * @param deliverAccountStartStr
	 * @param deliverAccountEndStr
	 * @param deliverPosAccountStartStr
	 * @param deliverPosAccountEndStr
	 */
	public Long getDeliverAccountListCount(long branchid, String realname, String deliverAccountStartStr, String deliverAccountEndStr, String deliverPosAccountStartStr, String deliverPosAccountEndStr, int uee) {
		String sql = "SELECT count(1) from express_set_user where roleid in(2,4)";
		sql += this.getDeliverAccountListWhere(branchid, realname, deliverAccountStartStr, deliverAccountEndStr, deliverPosAccountStartStr, deliverPosAccountEndStr, uee);
		return this.jdbcTemplate.queryForLong(sql);

	}

	private String getDeliverAccountListWhere(long branchid, String realname, String deliverAccountStartStr, String deliverAccountEndStr, String deliverPosAccountStartStr, String deliverPosAccountEndStr, int uee) {
		String sql = "";
		if (branchid > -1) {
			sql += " and branchid=" + branchid;
		}
		if (realname.trim().length() > 0) {
			sql += " and realname like '%" + realname + "%'";
		}
		if ((deliverAccountStartStr != null) && (deliverAccountStartStr.trim().length() > 0)) {
			sql += " and deliverAccount>=" + deliverAccountStartStr;
		}
		if ((deliverAccountEndStr != null) && (deliverAccountEndStr.trim().length() > 0)) {
			sql += " and deliverAccount<=" + deliverAccountEndStr;
		}
		if ((deliverPosAccountStartStr != null) && (deliverPosAccountStartStr.trim().length() > 0)) {
			sql += " and deliverPosAccount>=" + deliverPosAccountStartStr;
		}
		if ((deliverPosAccountEndStr != null) && (deliverPosAccountEndStr.trim().length() > 0)) {
			sql += " and deliverPosAccount<=" + deliverPosAccountEndStr;
		}
		if (uee > -1) {
			sql += " and employeestatus=" + uee;
		}
		return sql;

	}

	public List<User> getUserForAutoComplete(String username) {
		String sql = "select * from express_set_user where  realname like '%" + username + "%' and  userDeleteFlag=1 order by CONVERT( realname USING gbk ) COLLATE gbk_chinese_ci ASC ";
		List<User> userList = this.jdbcTemplate.query(sql, new UserRowMapper());
		return userList;
	}

	public List<User> getUserByUsernameAndMobile(String mobile) {
		try {
			String sql = "SELECT * FROM express_set_user WHERE  usermobile=?  and userDeleteFlag=1 ";
			return this.jdbcTemplate.query(sql, new UserRowMapper(), mobile);
		} catch (EmptyResultDataAccessException ee) {
			return null;
		}
	}

	/**
	 * 存储 用户的最近登录信息
	 *
	 * @param user
	 */
	@SystemInstallOperation
	public void updateUserLastInfo(User user) {
		String sql = "update express_set_user set lastip=?, lasttime=? where userid=?";
		this.jdbcTemplate.update(sql, user.getLastLoginIp(), user.getLastLoginTime(), user.getUserid());
	}

	/**
	 * 根据userids 得到所有用户
	 *
	 * @param useridsStr
	 * @return
	 */
	public List<User> getAllUserByUserIds(String useridsStr) {
		String sql = "select * from express_set_user where userid in (" + useridsStr + ")";
		return this.jdbcTemplate.query(sql, new UserRowMapper());
	}

	/**
	 *
	 * @param sosString
	 * @param branchid
	 * @param roleid
	 * @param workstate
	 * @param page
	 * @return
	 */
	public List<User> getUserForContact(String sosString, long branchid, long roleid, long workstate, long page) {
		String sql = "select * from express_set_user where userDeleteFlag=1 ";
		sql = this.getUserForCountWhere(sql, sosString, branchid, roleid, workstate);
		sql += " limit " + ((page - 1) * Page.ONE_PAGE_NUMBER) + "," + Page.ONE_PAGE_NUMBER;
		return this.jdbcTemplate.query(sql, new UserRowMapper());
	}

	// 根据条件 查询 返回sql
	private String getUserForCountWhere(String sql, String sosString, long branchid, long roleid, long workstate) {
		if ((sosString.length() > 0) || (branchid > 0) || (roleid >= 0) || (workstate > 0)) {
			StringBuffer str = new StringBuffer();
			if (sosString.length() > 0) {
				str.append(" and username like '%" + sosString + "%' or realname like '%" + sosString + "%'");
			}
			if (branchid > 0) {
				str.append(" and branchid=" + branchid);
			}
			if (roleid >= 0) {
				str.append(" and roleid=" + roleid);
			}
			if (workstate > 0) {
				str.append(" and employeestatus=" + workstate);
			}
			if (str.length() > 0) {
				sql += str.toString();
			}
		}

		return sql;
	}

	/**
	 * 用于分页
	 *
	 * @param sosString
	 * @param branchid
	 * @param roleid
	 * @param workstate
	 * @return
	 */
	public long getUserCountForContact(String sosString, long branchid, long roleid, long workstate) {
		String sql = "select count(1) from express_set_user where userDeleteFlag=1  ";
		sql = this.getUserForCountWhere(sql, sosString, branchid, roleid, workstate);
		return this.jdbcTemplate.queryForLong(sql);
	}

	/**
	 * 根据用户的状态 查询 工作 离职 休假
	 *
	 * @param value
	 * @return
	 */
	public Object getAllUserByWorkState(int status) {
		return this.jdbcTemplate.queryForLong(" select count(1) from  express_set_user where userDeleteFlag=1 and employeestatus=" + status);
	}

	/**
	 * 通讯录 导出
	 *
	 * @param sosString
	 * @param branchid
	 * @param roleid
	 * @param workstate
	 * @return
	 */
	public List<User> getUserForContactExport(String sosString, long branchid, long roleid, long workstate) {
		String sql = "select * from express_set_user where userDeleteFlag=1 ";
		sql = this.getUserForCountWhere(sql, sosString, branchid, roleid, workstate);
		return this.jdbcTemplate.query(sql, new UserRowMapper());
	}

	/**
	 * 得到全部 user
	 *
	 * @return
	 */
	public List<User> getUserForALL() {
		String sql = "select * from express_set_user  ";
		return this.jdbcTemplate.query(sql, new UserRowMapper());
	}

	public User getUserByBranchName(long branchid) {
		return this.jdbcTemplate.queryForObject("SELECT * from express_set_user where branchid=? and userDeleteFlag=1 limit 1 ", new UserRowMapper(), branchid);
	}

	public User getUserByRealname(String realname) {
		try {
			String sql = "select * from express_set_user where realname=?   ";
			return this.jdbcTemplate.queryForObject(sql, new UserRowMapper(), realname);
		} catch (EmptyResultDataAccessException ee) {
			return null;
		}

	}

	public Map<Long, String> getUserNameMap(List<Long> idList) {
		String sqlInParam = this.getSqlInParam(idList);
		Map<Long, String> nameMap = new HashMap<Long, String>();
		String sql = "select userid , realname from express_set_user where userid in (" + sqlInParam + ")";
		this.jdbcTemplate.query(sql, new UserIdNameRCH(nameMap));

		return nameMap;
	}

	public Map<Long, String> getUserNameMap(Set<Long> idSet) {
		return this.getUserNameMap(new ArrayList<Long>(idSet));
	}

	private class UserIdNameRCH implements RowCallbackHandler {

		private Map<Long, String> nameMap = null;

		public UserIdNameRCH(Map<Long, String> nameMap) {
			this.nameMap = nameMap;
		}

		@Override
		public void processRow(ResultSet rs) throws SQLException {
			Long id = Long.valueOf(rs.getLong("userid"));
			String name = rs.getString("realname");
			this.getNameMap().put(id, name);
		}

		private Map<Long, String> getNameMap() {
			return this.nameMap;
		}

	}

	private <T extends Number> String getSqlInParam(Collection<T> idList) {
		StringBuilder sqlInParam = new StringBuilder();
		if ((idList == null) || idList.isEmpty()) {
			return sqlInParam.toString();
		}
		for (T id : idList) {
			if (id == null) {
				continue;
			}
			sqlInParam.append(id.toString());
			sqlInParam.append(",");
		}
		if (sqlInParam.length() == 0) {
			return sqlInParam.toString();
		}
		return sqlInParam.substring(0, sqlInParam.length() - 1);
	}

	public List<Long> getAllDeliverId() {
		String sql = "select userid from express_set_user where roleid in (2,4)";

		return this.jdbcTemplate.queryForList(sql, Long.class);
	}

	public List<Long> getBranchDeliverId(Set<Long> branchIdSet) {
		String sql = "select userid from express_set_user where roleid in (2,4) and branchid in (" + this.getSqlInParam(branchIdSet) + ")";

		return this.jdbcTemplate.queryForList(sql, Long.class);
	}

	public List<Long> getBranchDeliverId(long branchId) {
		String sql = "select userid from express_set_user where roleid in (2,4) and branchid = ? ";
		Object[] paras = new Object[] { branchId };
		return this.jdbcTemplate.queryForList(sql, paras, Long.class);
	}

	public Map<Long, String> getDeliverNameMapByBranch(Set<Long> branchIdSet) {
		String inPara = this.getSqlInParam(branchIdSet);

		return this.getDeliverNameMapByBranch(inPara);
	}

	public Map<Long, String> getDeliverNameMapByBranch(String inPara) {
		String sql = "select userid,realname from express_set_user where roleid in (2,4) and branchid in (" + inPara + ")";
		Map<Long, String> deliverNameMap = new HashMap<Long, String>();
		this.jdbcTemplate.query(sql, new UserIdNameRCH(deliverNameMap));

		return deliverNameMap;
	}

	public Map<Long, String> getDeliverNameMapByBranch(long branchId) {
		String sql = "select userid,realname from express_set_user where roleid in (2,4) and branchid = ?";
		Map<Long, String> deliverNameMap = new HashMap<Long, String>();
		Object[] paras = new Object[] { branchId };
		this.jdbcTemplate.query(sql, paras, new UserIdNameRCH(deliverNameMap));

		return deliverNameMap;
	}

	public Map<Long, String> getDeliverNameMapByBranchid(long branchId) {
		String sql = "select userid,realname from express_set_user where roleid in (2,4) and employeestatus not in(" + UserEmployeestatusEnum.LiZhi.getValue() + ") and branchid = ?";
		Map<Long, String> deliverNameMap = new HashMap<Long, String>();
		Object[] paras = new Object[] { branchId };
		this.jdbcTemplate.query(sql, paras, new UserIdNameRCH(deliverNameMap));

		return deliverNameMap;
	}

	public List<User> getAllDeliverUser(long branchId) {
		String sql = "select * from express_set_user where roleid in (2,4)";
		if (branchId != 0) {
			sql += " and branchid =" + branchId;
		}
		return this.jdbcTemplate.query(sql, new UserRowMapper());
	}

	public User getUserByUsernameToUpper(String username) {
		return this.jdbcTemplate.queryForObject("SELECT * from express_set_user where UPPER(username)=? and userDeleteFlag=1 limit 1", new UserRowMapper(), username);
	}

	public List<User> getUsersByUsernameToUpper(String username) {
		List<User> userList = this.jdbcTemplate.query("SELECT * from express_set_user where  UPPER(username)=? and userDeleteFlag=1", new UserRowMapper(), username.toUpperCase());
		return userList;
	}

	/**
	 * @param dutypersonname
	 * @param dutybranchid
	 * @return
	 */
	public User getUsersByRealnameAndBranchid(String dutypersonname, int dutybranchid) {
		try {
			String sql = "SELECT * from express_set_user where realname=? and branchid=? and userDeleteFlag=1 limit 1";
			return this.jdbcTemplate.queryForObject(sql, new UserRowMapper(), dutypersonname, dutybranchid);
		} catch (Exception e) {
			return null;
		}
	}

	public List<User> getBeiTouSuRen(long branchid) {

		try {
			String sql = "select * from express_set_user where branchid=?";
			return this.jdbcTemplate.query(sql, new UserRowMapper(), branchid);
		} catch (Exception e) {
			return null;
		}
	}

	public User getbranchidbyuserid(long userid) {
		String sql = "select * from express_set_user where userid=? ";
		return this.jdbcTemplate.queryForObject(sql, new UserRowMapper(), userid);
	}

	/**
	 * @param pfruleid
	 * @return
	 */
	public List<User> getUserByPFruleId(long pfruleid) {
		String sql = "select * from express_set_user where pfruleid=?  and userDeleteFlag=1";
		return this.jdbcTemplate.query(sql, new UserRowMapper(), pfruleid);
	}

	public List<User> getUsersByuserids(String userids) {
		String sql = "select * from express_set_user where userid in(" + userids + ")";
		return this.jdbcTemplate.query(sql, new UserRowMapper());
	}

	@SystemInstallOperation
	@CacheEvict(value = "userCache", key = "#userid")
	public long updatelateradvanceByuserid(long userid, BigDecimal add) {
		String sql = "update express_set_user set lateradvance=? where userid=?";
		return this.jdbcTemplate.update(sql, add, userid);
	}

	@CacheEvict(value = "userCache", allEntries = true)
	public void updateCache() {

	}

	public List<User> getMoHuUser(String userName) {

		String sql = "select * from express_set_user where realname like '%" + userName + "%'";
		return this.jdbcTemplate.query(sql, new UserRowMapper());

	}
	
	public List<User> getExportUserInfo(String username,String realname,long branchid,long roleid){
		String sql= "select * from express_set_user where 1=1";
		if ((username.length() > 0) || (realname.length() > 0)) {
			sql += " and ";
			if ((username.length() > 0) && (realname.length() > 0)) {
				sql += " username like '%" + username + "%' and  realname like '%" + realname + "%' and userDeleteFlag=1 ";
			} else {
				if (username.length() > 0) {
					sql += " username like '%" + username + "%' and userDeleteFlag=1 ";
				}
				if (realname.length() > 0) {
					sql += " realname like '%" + realname + "%' and userDeleteFlag=1 ";
				}
			}

		}
		if (branchid > -1) {
			sql += " and branchid=" + branchid;
		}

		if (roleid > -1) {
			sql += " and roleid=" + roleid;
		}
		sql += " order by CONVERT( realname USING gbk ) COLLATE gbk_chinese_ci ASC ";

		List<User> userList = this.jdbcTemplate.query(sql, new UserRowMapper());
		
		return userList;
	}
	
	public Map<Long, String> getAllUserRealNameMap() {
		String sql = "select userid, realname from express_set_user ";
		final Map<Long, String> deliverMap = new HashMap<Long, String>();
		this.jdbcTemplate.query(sql, new RowCallbackHandler() {

			@Override
			public void processRow(ResultSet rs) throws SQLException {
				deliverMap.put(rs.getLong("userid"), rs.getString("realname"));
			}
		});
		return deliverMap;
	}
	
	/**
	 * 根据姓名和所属站点id查询用户 add by vic.liang@pjbest.com 2016-08-03
	 * @param realname
	 * @param branchid
	 * @return
	 */
	public User getUserByRealNameBranchid(String realname, int branchid) {
		String sql = "select * from express_set_user where realname=? and branchid = ? and userDeleteFlag=1 ";
		try {
			return this.jdbcTemplate.queryForObject(sql,new UserRowMapper(),realname,branchid);
		} catch (DataAccessException e) {
			return null;
		}
	}
	
	/**
	 * 根据姓名查询用户 add by vic.liang@pjbest.com 2016-08-03
	 * @param realname
	 * @param branchid
	 * @return
	 */
	public User getUserByRealName(String realname) {
		String sql = "select * from express_set_user where realname=? and userDeleteFlag=1 ";
		try {
			return this.jdbcTemplate.queryForObject(sql,new UserRowMapper(),realname);
		} catch (DataAccessException e) {
			return null;
		}
	}
	
	
}

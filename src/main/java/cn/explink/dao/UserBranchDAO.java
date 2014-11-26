package cn.explink.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import cn.explink.domain.UserBranch;
import cn.explink.util.Page;

@Component
public class UserBranchDAO {

	private final class UserBranchRowMapper implements RowMapper<UserBranch> {
		@Override
		public UserBranch mapRow(ResultSet rs, int rowNum) throws SQLException {
			UserBranch userBranch = new UserBranch();
			userBranch.setBranchid(rs.getLong("branchid"));
			userBranch.setUserid(rs.getLong("userid"));
			userBranch.setId(rs.getLong("id"));

			return userBranch;
		}
	}

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public void creUserBranch(final UserBranch userBranch) {
		jdbcTemplate.update("insert into express_set_user_branch(branchid,userid) values(?,?)", new PreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				ps.setLong(1, userBranch.getBranchid());
				ps.setLong(2, userBranch.getUserid());
			}
		});
	}

	private String getUserBranchByPageWhereSql(String sql, long branchid, long userid) {

		if (branchid > 0 || userid > 0) {
			StringBuffer w = new StringBuffer();
			sql += " where";
			if (branchid > 0) {
				w.append(" and branchid=" + branchid);
			}
			if (userid > 0) {
				w.append(" and userid=" + userid);
			}
			sql += w.substring(4, w.length());
		}
		return sql;
	}

	public List<UserBranch> getUserBranchByWheresql(long branchid, long userid) {
		String sql = "select * from express_set_user_branch ";
		sql = this.getUserBranchByPageWhereSql(sql, branchid, userid);

		List<UserBranch> brList = jdbcTemplate.query(sql, new UserBranchRowMapper());
		return brList;
	}

	public List<UserBranch> getUserBranchByPage(long page, long branchid, long userid) {
		String sql = "select * from express_set_user_branch";
		sql = this.getUserBranchByPageWhereSql(sql, branchid, userid);
		sql += " order by branchid desc limit " + (page - 1) * Page.ONE_PAGE_NUMBER + " ," + Page.ONE_PAGE_NUMBER;
		List<UserBranch> userbranchlist = jdbcTemplate.query(sql, new UserBranchRowMapper());
		return userbranchlist;
	}

	public List<UserBranch> getUserBranchGroupByUserId(long page, long branchid, long userid) {
		String sql = "select * from express_set_user_branch";
		sql = this.getUserBranchByPageWhereSql(sql, branchid, userid);
		sql += " group by userid limit " + (page - 1) * Page.ONE_PAGE_NUMBER + " ," + Page.ONE_PAGE_NUMBER;
		List<UserBranch> userbranchlist = jdbcTemplate.query(sql, new UserBranchRowMapper());
		return userbranchlist;
	}

	public List<UserBranch> getUserBranchGroupByBranchid(long page, long branchid, long userid) {
		String sql = "select * from express_set_user_branch";
		sql = this.getUserBranchByPageWhereSql(sql, branchid, userid);
		sql += " group by branchid limit " + (page - 1) * Page.ONE_PAGE_NUMBER + " ," + Page.ONE_PAGE_NUMBER;
		List<UserBranch> userbranchlist = jdbcTemplate.query(sql, new UserBranchRowMapper());
		return userbranchlist;
	}

	public long getUserBranchCount(long branchid, long userid) {
		String sql = "select count(1) from express_set_user_branch";
		sql = this.getUserBranchByPageWhereSql(sql, branchid, userid);
		return jdbcTemplate.queryForInt(sql);
	}

	public long getUserBranchCountGroupByUserid(long branchid, long userid) {
		String sql = "select  COUNT(DISTINCT userid) from express_set_user_branch";
		sql = this.getUserBranchByPageWhereSql(sql, branchid, userid);
		return jdbcTemplate.queryForInt(sql);
	}

	public long getUserBranchCountGroupByBranchid(long branchid, long userid) {
		String sql = "select  COUNT(DISTINCT branchid) from express_set_user_branch";
		sql = this.getUserBranchByPageWhereSql(sql, branchid, userid);
		return jdbcTemplate.queryForInt(sql);
	}

	public UserBranch getUserBranchById(long id) {
		try {
			String sql = "select * from express_set_user_branch where id=? ";
			UserBranch userBranch = jdbcTemplate.queryForObject(sql, new UserBranchRowMapper(), id);
			return userBranch;
		} catch (DataAccessException e) {
			return null;
		}
	}

	public List<UserBranch> getUserBranchByUserId(long userid) {
		try {
			String sql = "select * from express_set_user_branch where userid=? ";
			return jdbcTemplate.query(sql, new UserBranchRowMapper(), userid);
		} catch (DataAccessException e) {
			return null;
		}
	}

	public List<UserBranch> getUserBranchByBranchid(long branchid) {
		try {
			String sql = "select * from express_set_user_branch where branchid=? ";
			return jdbcTemplate.query(sql, new UserBranchRowMapper(), branchid);
		} catch (DataAccessException e) {
			return null;
		}
	}

	public void saveUserBranchById(long id, long branchid, long userid) {
		String sql = "update express_set_user_branch set branchid=?,userid=? where id=?";
		jdbcTemplate.update(sql, branchid, userid, id);
	}

	public void deleteUserBranchById(long id) {
		String sql = "DELETE FROM express_set_user_branch WHERE id =? ";
		jdbcTemplate.update(sql, id);
	}

	public void deleteUserBranchByUserId(long userid) {
		String sql = "DELETE FROM express_set_user_branch WHERE userid = ?";
		jdbcTemplate.update(sql, userid);
	}

	public void deleteUserBranchByBranchid(long branchid) {
		String sql = "DELETE FROM express_set_user_branch WHERE branchid = ?";
		jdbcTemplate.update(sql, branchid);
	}
}

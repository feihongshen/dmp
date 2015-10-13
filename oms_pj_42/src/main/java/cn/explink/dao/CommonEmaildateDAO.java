package cn.explink.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import cn.explink.domain.CommonEmaildate;
import cn.explink.util.Page;

@Component
public class CommonEmaildateDAO {

	private final class CommonEmaildateRowMapper implements RowMapper<CommonEmaildate> {
		@Override
		public CommonEmaildate mapRow(ResultSet rs, int rowNum) throws SQLException {
			CommonEmaildate common = new CommonEmaildate();
			common.setEmaildateid(rs.getLong("emaildateid"));
			common.setCommoncode(rs.getString("commoncode"));
			common.setEmaildate(rs.getString("emaildate"));
			common.setCwbcount(rs.getLong("cwbcount"));
			return common;
		}
	}

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public List<CommonEmaildate> getAllCommonEmaildate() {
		return jdbcTemplate.query("select * from common_emaildate ", new CommonEmaildateRowMapper());
	}

	public List<CommonEmaildate> getAllCommonEmaildatePage(String commoncode, String startdate, String enddate, long page) {
		String sql = "select * from common_emaildate ";
		sql = getSqlByCommonCodeEmaildatePage(sql, commoncode, startdate, enddate);
		sql += " limit " + (page - 1) * Page.ONE_PAGE_NUMBER + " ," + Page.ONE_PAGE_NUMBER;
		return jdbcTemplate.query(sql, new CommonEmaildateRowMapper());
	}

	public long getAllCommonEmaildateCount(String commoncode, String startdate, String enddate) {
		String sql = "select count(1) from common_emaildate ";
		sql = getSqlByCommonCodeEmaildatePage(sql, commoncode, startdate, enddate);
		return jdbcTemplate.queryForLong(sql);
	}

	public long creCommonEmaildate(final CommonEmaildate comm) {
		KeyHolder key = new GeneratedKeyHolder();
		jdbcTemplate.update(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(java.sql.Connection con) throws SQLException {
				PreparedStatement ps = null;
				ps = con.prepareStatement("insert into common_emaildate (commoncode,emaildate) " + "values(?,?)", new String[] { "emaildateid" });
				ps.setString(1, comm.getCommoncode());
				ps.setString(2, comm.getEmaildate());
				return ps;
			}
		}, key);
		return key.getKey().longValue();
	}

	public void updateCommonEmaildateCount(long emaildateid) {
		jdbcTemplate.update("update common_emaildate set cwbcount=cwbcount+1 where emaildateid=?", emaildateid);
	}

	public void updateCommonEmaildateJianCount(long emaildateid) {
		jdbcTemplate.update("update common_emaildate set cwbcount=cwbcount-1 where emaildateid=? and cwbcount>0", emaildateid);
	}

	private String getSqlByCommonCodeEmaildatePage(String sql, String commoncode, String startdate, String enddate) {
		sql += " where cwbcount>0 ";
		if (startdate.length() > 0 || enddate.length() > 0 || commoncode.length() > 0) {
			if (startdate.length() > 0) {
				sql += " and emaildate>='" + startdate + "'";
			}
			if (enddate.length() > 0) {
				sql += " and emaildate<='" + enddate + "'";
			}
			if (commoncode.length() > 0) {
				sql += " and commoncode='" + commoncode + "'";
			}

		}
		return sql;
	}

	public CommonEmaildate getAllCommonEmaildateByEmaildateid(long emaildateid) {
		CommonEmaildate ce = new CommonEmaildate();
		try {
			ce = jdbcTemplate.queryForObject(" select * from common_emaildate where emaildateid=" + emaildateid, new CommonEmaildateRowMapper());
		} catch (Exception e) {
			ce = null;
		}
		return ce;
	}
}

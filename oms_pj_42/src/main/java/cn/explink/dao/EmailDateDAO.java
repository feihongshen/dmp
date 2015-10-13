package cn.explink.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import cn.explink.domain.EmailDate;
import cn.explink.util.Page;

@Component
public class EmailDateDAO {
	private final class EmailDateRowMapper implements RowMapper<EmailDate> {
		@Override
		public EmailDate mapRow(ResultSet rs, int rowNum) throws SQLException {
			EmailDate emaildate = new EmailDate();
			emaildate.setEmaildateid(rs.getLong("emaildateid"));
			emaildate.setEmaildatetime(rs.getString("emaildatetime"));
			emaildate.setUserid(rs.getLong("userid"));
			emaildate.setCustomerid(rs.getLong("customerid"));
			emaildate.setWarehouseid(rs.getLong("warehouseid"));
			emaildate.setState(rs.getInt("state"));
			return emaildate;
		}
	}

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public List<EmailDate> getAllEmailDate() {
		List<EmailDate> emailDate = jdbcTemplate.query("select * from express_ops_emaildate", new EmailDateRowMapper());
		return emailDate;
	}

	public void getCreateEmailDate(String emaildate, long userid, long customerid, long warehouseid) {
		jdbcTemplate.update("insert into express_ops_emaildate(emaildatetime,userid,customerid,warehouseid) values(?,?,?,?)", emaildate, userid, customerid, warehouseid);
	}

	public int getEmailDateCount(String emaildate) {
		return jdbcTemplate.queryForInt("select count(1) from express_ops_emaildate where emaildatetime=?", emaildate);
	}

	public List<EmailDate> getEmailDateByDate(String beginemaildate, String endemaildate) {
		return jdbcTemplate.query("select * from express_ops_emaildate where emaildatetime >'" + beginemaildate + "' and emaildatetime <='" + endemaildate + "' order by emaildatetime desc",
				new EmailDateRowMapper());
	}

	public long saveEmailDateToEmailDate(long driverid, String emaildatetime, long warehouseid) {
		return jdbcTemplate.update("update express_ops_emaildate set userid = '" + driverid + "',warehouseid='" + warehouseid + "',state=1 where emaildatetime='" + emaildatetime + "' ");
	}

	private String getEmaildateByPageWhereSql(String sql, long userid, String beginemaildate, String endemaildate) {

		if (userid > 0 || beginemaildate.length() > 0 || endemaildate.length() > 0) {
			StringBuffer w = new StringBuffer();
			sql += " where ";
			if (userid > 0) {
				w.append(" and userid=" + userid);
			}
			if (beginemaildate.length() > 0) {
				w.append(" and emaildatetime > '" + beginemaildate + "'");
			}
			if (endemaildate.length() > 0) {
				w.append(" and emaildatetime < '" + endemaildate + "'");
			}
			sql += w.substring(4, w.length());
		}
		return sql;
	}

	public List<EmailDate> getAllEmailDateByUseridAndEmaildate(long page, long userid, String beginemaildate, String endemaildate) {
		String sql = "select * from express_ops_emaildate";
		sql = this.getEmaildateByPageWhereSql(sql, userid, beginemaildate, endemaildate);
		sql += " limit " + (page - 1) * Page.ONE_PAGE_NUMBER + " ," + Page.ONE_PAGE_NUMBER;
		return jdbcTemplate.query(sql, new EmailDateRowMapper());
	}

	public long getEmaildateConutByUseridAndEmaildate(long userid, String beginemaildate, String endemaildate) {
		String sql = "SELECT COUNT(1) FROM express_ops_emaildate";
		sql = this.getEmaildateByPageWhereSql(sql, userid, beginemaildate, endemaildate);
		return jdbcTemplate.queryForLong(sql);
	}

}

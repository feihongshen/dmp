package cn.explink.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import cn.explink.domain.CwbError;
import cn.explink.util.Page;

@Component
public class CwbErrorDAO {

	private final class CwbErrorMapper implements RowMapper<CwbError> {
		@Override
		public CwbError mapRow(ResultSet rs, int rowNum) throws SQLException {
			CwbError cwbError = new CwbError();
			cwbError.setId(rs.getLong("id"));
			cwbError.setCwb(rs.getString("cwb"));
			cwbError.setCwbdetail(rs.getString("cwbdetail"));
			cwbError.setEmaildate(rs.getString("emaildate"));
			cwbError.setMessage(rs.getString("message"));
			cwbError.setState(rs.getInt("state"));

			return cwbError;
		}
	}

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public void creCwbError(String cwb, String detail, long emaildateid, String emaildate, String message) {
		jdbcTemplate.update("INSERT INTO express_ops_cwb_error(cwb,cwbdetail,emaildateid,emaildate,message)VALUES (?,?,?,?,?);", cwb, detail, emaildateid, emaildate, message);
	}

	public List<CwbError> getcwbOrderErrorByPage(int page, long emaildateid) {
		return jdbcTemplate.query("select * from express_ops_cwb_error where emaildateid=? limit ?,?", new CwbErrorMapper(), emaildateid, ((page - 1) * Page.ONE_PAGE_NUMBER), Page.ONE_PAGE_NUMBER);
	}

	public long getCwbOrderErrorCount(long emaildateid) {
		return jdbcTemplate.queryForLong("select count(1) from express_ops_cwb_error where emaildateid=?", emaildateid);
	}
}

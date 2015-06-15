package cn.explink.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import cn.explink.domain.PenalizeOutImportErrorRecord;
import cn.explink.util.StringUtil;

@Component
public class PenalizeOutImportErrorRecordDAO {
	@Autowired
	private JdbcTemplate jdbcTemplate;

	private final class PenalizeOutImportErrorRecordRowMapper implements RowMapper<PenalizeOutImportErrorRecord> {

		@Override
		public PenalizeOutImportErrorRecord mapRow(ResultSet rs, int rowNum) throws SQLException {
			PenalizeOutImportErrorRecord error = new PenalizeOutImportErrorRecord();
			error.setId(rs.getLong("id"));
			error.setImportFlag(rs.getLong("importFlag"));
			error.setCwb(StringUtil.nullConvertToEmptyString(rs.getString("cwb")));
			error.setError(StringUtil.nullConvertToEmptyString(rs.getString("error")));
			return error;
		}

	}

	public int crePenalizeOutImportErrorRecord(String cwb, long importFlag, String error) {
		try {
			String sql = "insert into express_ops_penalizeOutImportErrorRecord(cwb,importFlag,error) values(?,?,?)";
			return this.jdbcTemplate.update(sql, cwb, importFlag, error);
		} catch (Exception e) {
			return 0;
		}
	}
	public List<PenalizeOutImportErrorRecord> getPenalizeOutImportRecordByImportFlag(long importFlag) {
		try {
			String sql = "select * from  express_ops_penalizeOutImportErrorRecord where importFlag=?";
			//sql += " order by state desc limit " + ((page - 1) * Page.ONE_PAGE_NUMBER) + " ," + Page.ONE_PAGE_NUMBER;
			return this.jdbcTemplate.query(sql,new PenalizeOutImportErrorRecordRowMapper(), importFlag);
		} catch (Exception e) {
			return null;
		}
	}
	public int getPenalizeOutImportRecordByImportFlagCount(long importFlag) {
		try {
			String sql = "select count(1) from  express_ops_penalizeOutImportErrorRecord where importFlag=?";
			return this.jdbcTemplate.queryForInt(sql,importFlag);
		} catch (Exception e) {
			return 0;
		}
	}
}

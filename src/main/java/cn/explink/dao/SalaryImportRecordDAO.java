package cn.explink.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import cn.explink.domain.SalaryImportRecord;
import cn.explink.util.StringUtil;

@Component
public class SalaryImportRecordDAO {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	private final class SalaryImportRecordRowMapper implements RowMapper<SalaryImportRecord> {

		@Override
		public SalaryImportRecord mapRow(ResultSet rs, int rowNum) throws SQLException {
			SalaryImportRecord im = new SalaryImportRecord();
			im.setId(rs.getLong("id"));
			im.setImportFlag(rs.getLong("importFlag"));
			im.setUserid(rs.getLong("userid"));
			im.setStarttime(StringUtil.nullConvertToEmptyString(rs.getString("starttime")));
			im.setSuccessCounts(rs.getInt("successCounts"));
			im.setFailCounts(rs.getInt("failCounts"));
			im.setTotalCounts(rs.getInt("totalCounts"));
			return im;
		}

	}

	public int creSalaryImportRecord(SalaryImportRecord record) {
		try {
			String sql = "insert into express_set_salaryImportRecord (importFlag,userid,starttime,successCounts,failCounts,totalCounts) values(?,?,NOW(),?,?,?)";
			return this.jdbcTemplate.update(sql,record.getImportFlag(),record.getUserid(),record.getSuccessCounts(),record.getFailCounts(),record.getTotalCounts());
		} catch (Exception e) {
			return 0;
		}

	}
	public SalaryImportRecord getSalaryImportRecordByImportFlag(long importFlag) {
		try {
			String sql = "select * from  express_set_salaryImportRecord where importFlag=?";
			return this.jdbcTemplate.queryForObject(sql,new SalaryImportRecordRowMapper(),importFlag);
		} catch (Exception e) {
			return null;
		}
	}
}

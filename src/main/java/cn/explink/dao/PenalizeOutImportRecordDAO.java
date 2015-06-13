package cn.explink.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import cn.explink.domain.PenalizeOutImportRecord;
import cn.explink.util.StringUtil;

@Component
public class PenalizeOutImportRecordDAO {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	private final class PenalizeOutImportRecordRowMapper implements RowMapper<PenalizeOutImportRecord> {

		@Override
		public PenalizeOutImportRecord mapRow(ResultSet rs, int rowNum) throws SQLException {
			PenalizeOutImportRecord im = new PenalizeOutImportRecord();
			im.setId(rs.getLong("id"));
			im.setImportFlag(rs.getLong("importFlag"));
			im.setUserid(rs.getLong("userid"));
			im.setStarttime(StringUtil.nullConvertToEmptyString(rs.getString("starttime")));
			//im.setEndtime(StringUtil.nullConvertToEmptyString(rs.getString("endtime")));
			im.setSuccessCounts(rs.getInt("successCounts"));
			im.setFailCounts(rs.getInt("failCounts"));
			im.setTotalCounts(rs.getInt("totalCounts"));
			return im;
		}

	}

	public int crePenalizeOutImportRecord(PenalizeOutImportRecord record) {
		try {
			String sql = "insert into express_ops_penalizeOutImportRecord (importFlag,userid,starttime,successCounts,failCounts,totalCounts) values(?,?,NOW(),?,?,?)";
			return this.jdbcTemplate.update(sql,record.getImportFlag(),record.getUserid(),record.getSuccessCounts(),record.getFailCounts(),record.getTotalCounts());
		} catch (Exception e) {
			return 0;
		}

	}
}

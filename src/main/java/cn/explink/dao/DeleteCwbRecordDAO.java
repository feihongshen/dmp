package cn.explink.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import cn.explink.domain.DeleteCwbRecord;
import cn.explink.util.Page;
import cn.explink.util.StringUtil;

@Component
public class DeleteCwbRecordDAO {

	private final class DeleteCwbRecordRowMapper implements RowMapper<DeleteCwbRecord> {
		@Override
		public DeleteCwbRecord mapRow(ResultSet rs, int rowNum) throws SQLException {
			DeleteCwbRecord deleteCwbRecord = new DeleteCwbRecord();
			deleteCwbRecord.setId(rs.getLong("id"));
			deleteCwbRecord.setCustomerid(rs.getLong("customerid"));
			deleteCwbRecord.setCwb(StringUtil.nullConvertToEmptyString(rs.getString("cwb")));
			deleteCwbRecord.setDeletetime(StringUtil.nullConvertToEmptyString(rs.getString("deletetime")));
			deleteCwbRecord.setFlowordertype(rs.getLong("flowordertype"));
			deleteCwbRecord.setUserid(rs.getLong("userid"));
			return deleteCwbRecord;
		}
	}

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public List<DeleteCwbRecord> getDeleteCwbRecordByDeleteTimeAndCwbsPage(long page, String begindate, String enddate, String cwbs) {
		String sql = "SELECT * from ops_deletecwbrecord where ";
		if (cwbs.length() > 0) {
			sql = sql.replace("ops_deletecwbrecord", "ops_deletecwbrecord FORCE INDEX(DCR_Cwb_Idx)");
			sql += " cwb in(" + cwbs + ")";
		} else {
			sql = sql.replace("ops_deletecwbrecord", "ops_deletecwbrecord FORCE INDEX(DCR_Deletetime_Idx)");
			sql += " deletetime >='" + begindate + "' and deletetime <='" + enddate + "'";
		}

		sql += "  limit " + (page - 1) * Page.ONE_PAGE_NUMBER + " ," + Page.ONE_PAGE_NUMBER;
		return jdbcTemplate.query(sql, new DeleteCwbRecordRowMapper());
	}

	public long getDeleteCwbRecordByDeleteTimeAndCwbsCount(String begindate, String enddate, String cwbs) {
		String sql = "SELECT count(1) from ops_deletecwbrecord where ";
		if (cwbs.length() > 0) {
			sql = sql.replace("ops_deletecwbrecord", "ops_deletecwbrecord FORCE INDEX(DCR_Cwb_Idx)");
			sql += " cwb in(" + cwbs + ")";
		} else {
			sql = sql.replace("ops_deletecwbrecord", "ops_deletecwbrecord FORCE INDEX(DCR_Deletetime_Idx)");
			sql += " deletetime >='" + begindate + "' and deletetime <='" + enddate + "'";
		}

		return jdbcTemplate.queryForLong(sql);
	}

	public void creDeleteCwbRecord(final DeleteCwbRecord deleteCwbRecord) {
		jdbcTemplate.update("insert into ops_deletecwbrecord (cwb,customerid,deletetime,flowordertype,userid) values(?,?,?,?,?)", new PreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				ps.setString(1, deleteCwbRecord.getCwb());
				ps.setLong(2, deleteCwbRecord.getCustomerid());
				ps.setString(3, deleteCwbRecord.getDeletetime());
				ps.setLong(4, deleteCwbRecord.getFlowordertype());
				ps.setLong(5, deleteCwbRecord.getUserid());
			}
		});
	}

}

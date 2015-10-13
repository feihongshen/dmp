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
import cn.explink.domain.Reason;
import cn.explink.util.Page;

@Component
public class ReasonDao {

	private final class ReasonRowMapper implements RowMapper<Reason> {

		@Override
		public Reason mapRow(ResultSet rs, int rowNum) throws SQLException {
			Reason reason = new Reason();
			reason.setReasonid(rs.getLong("reasonid"));
			reason.setReasoncontent(rs.getString("reasoncontent"));
			reason.setReasontype(rs.getLong("reasontype"));
			return reason;
		}
	}

	@Autowired
	private JdbcTemplate jdbcTemplate;

	private String getReasonByPageWhereSql(String sql, long reasontype) {
		if (reasontype > 0) {
			sql += " where reasontype=" + reasontype;
		}
		return sql;
	}

	public List<Reason> getReasonByPage(long page, long reasontype) {
		String sql = "select * from express_set_reason";
		sql = this.getReasonByPageWhereSql(sql, reasontype);
		sql += " order by reasonid desc limit " + (page - 1) * Page.ONE_PAGE_NUMBER + " ," + Page.ONE_PAGE_NUMBER;
		List<Reason> reasonList = jdbcTemplate.query(sql, new ReasonRowMapper());
		return reasonList;
	}

	public long getReasonCount(long reasontype) {
		String sql = "select count(1) from express_set_reason";
		sql = this.getReasonByPageWhereSql(sql, reasontype);
		return jdbcTemplate.queryForInt(sql);
	}

	public Reason getReasonByReasonid(long reasonid) {
		return jdbcTemplate.queryForObject("select * from express_set_reason where reasonid=?", new ReasonRowMapper(), reasonid);
	}

	public List<Reason> getReasonByReasoncontent(String reasoncontent) {
		return jdbcTemplate.query("select * from express_set_reason where reasoncontent=?", new ReasonRowMapper(), reasoncontent);
	}

	public void saveReason(final Reason reason) {

		jdbcTemplate.update("update express_set_reason set reasoncontent=?,reasontype =? where reasonid=?", new PreparedStatementSetter() {

			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				ps.setString(1, reason.getReasoncontent());
				ps.setLong(2, reason.getReasontype());
				ps.setLong(3, reason.getReasonid());
			}
		});

	}

	public void creReason(final Reason reason) {

		jdbcTemplate.update("insert into express_set_reason(reasoncontent,reasontype) values(?,?)", new PreparedStatementSetter() {

			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				ps.setString(1, reason.getReasoncontent());
				ps.setLong(2, reason.getReasontype());
			}
		});
	}

	public List<Reason> getAllReasonByReasonType(long reasontype) {
		String sql = "select * from express_set_reason where reasontype=" + reasontype;
		return jdbcTemplate.query(sql, new ReasonRowMapper());
	}

}

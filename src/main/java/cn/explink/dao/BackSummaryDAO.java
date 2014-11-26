package cn.explink.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import cn.explink.domain.BackSummary;

@Component
public class BackSummaryDAO {
	@Autowired
	private JdbcTemplate jdbcTemplate;

	private final class BackSummaryRowMapper implements RowMapper<BackSummary> {
		@Override
		public BackSummary mapRow(ResultSet rs, int rowNum) throws SQLException {
			BackSummary o = new BackSummary();
			o.setSummaryid(rs.getLong("summaryid"));
			o.setNums24(rs.getLong("nums24"));
			o.setNums72(rs.getLong("nums72"));
			o.setNumsout(rs.getLong("numsout"));
			o.setNumsinto(rs.getLong("numsinto"));
			o.setCreatetime(rs.getString("createtime"));
			return o;
		}
	}

	public long createBackSummary(final BackSummary o) {
		KeyHolder key = new GeneratedKeyHolder();
		jdbcTemplate.update(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(java.sql.Connection con) throws SQLException {
				PreparedStatement ps = null;
				ps = con.prepareStatement("INSERT INTO `ops_back_summary` (nums24,nums72,numsout,numsinto,createtime) " + "values (?,?,?,?,?)", new String[] { "summaryid" });
				ps.setLong(1, o.getNums24());
				ps.setLong(2, o.getNums72());
				ps.setLong(3, o.getNumsout());
				ps.setLong(4, o.getNumsinto());
				ps.setString(5, o.getCreatetime());
				return ps;
			}
		}, key);
		o.setSummaryid(key.getKey().longValue());
		return key.getKey().longValue();
	}

	public BackSummary getBackSummary() {
		String sql = "SELECT * FROM ops_back_summary ORDER BY createtime DESC LIMIT 0,1";
		try {
			return jdbcTemplate.queryForObject(sql, new BackSummaryRowMapper());
		} catch (DataAccessException e) {
			return null;
		}
	}

	public List<BackSummary> getBackSummaryList(String starttime, String endtime) {
		String sql = "SELECT * FROM ops_back_summary WHERE createtime>=? and createtime<=? ORDER BY createtime DESC";
		return jdbcTemplate.query(sql, new BackSummaryRowMapper(), starttime, endtime);
	}
}

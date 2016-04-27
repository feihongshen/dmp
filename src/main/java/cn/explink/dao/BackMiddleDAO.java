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

import cn.explink.domain.BackMiddle;
import cn.explink.util.Page;

import com.pjbest.splitting.aspect.DataSource;
import com.pjbest.splitting.routing.DatabaseType;

@Component
public class BackMiddleDAO {
	@Autowired
	private JdbcTemplate jdbcTemplate;

	private final class BackMiddleRowMapper implements RowMapper<BackMiddle> {
		@Override
		public BackMiddle mapRow(ResultSet rs, int rowNum) throws SQLException {
			BackMiddle o = new BackMiddle();
			o.setId(rs.getLong("id"));
			o.setSummaryid(rs.getLong("summaryid"));
			o.setCwb(rs.getString("cwb"));
			o.setType(rs.getInt("type"));
			return o;
		}
	}

	public void createBackMiddle(final BackMiddle o) {
		KeyHolder key = new GeneratedKeyHolder();
		jdbcTemplate.update(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(java.sql.Connection con) throws SQLException {
				PreparedStatement ps = null;
				ps = con.prepareStatement("INSERT INTO `ops_back_middle` (`summaryid`,`cwb`,`type`) " + "VALUES (?,?,?);", new String[] { "id" });
				ps.setLong(1, o.getSummaryid());
				ps.setString(2, o.getCwb());
				ps.setInt(3, o.getType());
				return ps;
			}
		}, key);
		o.setId(key.getKey().longValue());
	}

	@DataSource(DatabaseType.REPLICA)
	public List<BackMiddle> getBackMiddleList(long page, long summaryid, int type) {
		String sql = " SELECT * FROM ops_back_middle  a LEFT JOIN express_ops_cwb_detail b ON a.`cwb`=b.`cwb` WHERE b.`state`=1 and a.summaryid=? and a.type=? ";
		sql += " limit " + (page - 1) * Page.ONE_PAGE_NUMBER + " ," + Page.ONE_PAGE_NUMBER;
		return jdbcTemplate.query(sql, new BackMiddleRowMapper(), summaryid, type);
	}

	@DataSource(DatabaseType.REPLICA)
	public long getBackMiddleCount(long summaryid, int type) {
		String sql = "SELECT count(1) FROM ops_back_middle a LEFT JOIN express_ops_cwb_detail b ON a.`cwb`=b.`cwb` WHERE b.`state`=1 AND a.summaryid=? AND a.type=?";
		return jdbcTemplate.queryForLong(sql, summaryid, type);
	}

	@DataSource(DatabaseType.REPLICA)
	public String getCwbByExcel(long page, long summaryid, int type) {
		String sql = "SELECT b.* FROM ops_back_middle a LEFT JOIN express_ops_cwb_detail b " + "ON a.`cwb`=b.`cwb` WHERE b.`state`=1 AND summaryid=" + summaryid + " AND TYPE=" + type + " ";
		sql += " limit " + page + " ," + Page.EXCEL_PAGE_NUMBER;
		return sql;
	}
}

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

import com.mysql.jdbc.Statement;

import cn.explink.domain.Remark;

@Component
public class RemarkDAO {
	private final class RemarkRowMapper implements RowMapper<Remark> {
		@Override
		public Remark mapRow(ResultSet rs, int rowNum) throws SQLException {
			Remark remark = new Remark();
			remark.setRemarkid(rs.getLong("remarkid"));
			remark.setRemarktype(rs.getString("remarktype"));
			remark.setRemark(rs.getString("remark"));
			remark.setCwb(rs.getString("cwb"));
			remark.setCreatetime(rs.getString("createtime"));
			remark.setUsername(rs.getString("username"));
			return remark;
		}
	}

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public long saveRemark(final Remark r) {
		KeyHolder key = new GeneratedKeyHolder();
		jdbcTemplate.update(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(java.sql.Connection con) throws SQLException {
				PreparedStatement ps = null;
				ps = con.prepareStatement("insert into express_set_remark(remarktype,remark,cwb,createtime,username) values(?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
				ps.setString(1, r.getRemarktype());
				ps.setString(2, r.getRemark());
				ps.setString(3, r.getCwb());
				ps.setString(4, r.getCreatetime());
				ps.setString(5, r.getUsername());
				return ps;
			}
		}, key);
		return key.getKey().longValue();
	}

	public List<Remark> getRemarkByTypeAndCwb(String remarktype, String cwb) {
		String sql = "select * from express_set_remark where remarktype =? and cwb =?";
		return jdbcTemplate.query(sql, new RemarkRowMapper(), remarktype, cwb);
	}

	public List<Remark> getRemarkByCwb(String cwb) {
		String sql = "select * from express_set_remark where  cwb =?";
		return jdbcTemplate.query(sql, new RemarkRowMapper(), cwb);
	}

	public List<Remark> getRemarkByCwbs(String cwbs) {
		String sql = "select * from express_set_remark ";
		if (cwbs.length() > 0) {
			sql += " where  cwb in(" + cwbs + ")";
		}
		return jdbcTemplate.query(sql, new RemarkRowMapper());
	}

	public List<Remark> getAllRemark() {
		String sql = "select * from express_set_remark ";
		return jdbcTemplate.query(sql, new RemarkRowMapper());
	}
}

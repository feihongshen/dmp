package cn.explink.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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
				ps = con.prepareStatement("insert into express_set_remark(remarktype,remark) values(?,?)", Statement.RETURN_GENERATED_KEYS);
				ps.setString(1, r.getRemarktype());
				ps.setString(2, r.getRemark());
				return ps;
			}
		}, key);
		return key.getKey().longValue();
	}
}

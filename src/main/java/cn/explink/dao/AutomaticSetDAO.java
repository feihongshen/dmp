package cn.explink.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import cn.explink.domain.AutomaticSet;

@Component
public class AutomaticSetDAO {
	private final class AutomaticSetRowMapper implements RowMapper<AutomaticSet> {
		@Override
		public AutomaticSet mapRow(ResultSet rs, int rowNum) throws SQLException {
			AutomaticSet automaticSet = new AutomaticSet();
			automaticSet.setIsauto(rs.getLong("isauto"));
			automaticSet.setNowlinkname(rs.getString("nowlinkname"));
			automaticSet.setNextlink(rs.getString("nextlink"));
			return automaticSet;
		}
	}

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public void saveAutomaticSetByNowlinkname(long isauto, String nowlinkname) {
		String sql = "update express_ops_automatic_set set isauto=? where nowlinkname=?";
		jdbcTemplate.update(sql, isauto, nowlinkname);
	}

	public List<AutomaticSet> getAllAutomaticSet() {
		String sql = "select * from express_ops_automatic_set";
		return jdbcTemplate.query(sql, new AutomaticSetRowMapper());
	}

	public void saveAutomaticSetToIsauto() {
		String sql = "update express_ops_automatic_set set isauto=0";
		jdbcTemplate.update(sql);
	}
}

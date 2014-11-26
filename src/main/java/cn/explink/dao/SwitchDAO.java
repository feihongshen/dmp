package cn.explink.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import cn.explink.domain.Switch;
import cn.explink.util.StringUtil;

@Component
public class SwitchDAO {

	private final class SwitchRowMapper implements RowMapper<Switch> {
		@Override
		public Switch mapRow(ResultSet rs, int rowNum) throws SQLException {
			Switch switchs = new Switch();
			switchs.setId(rs.getLong("id"));
			switchs.setSwitchname(StringUtil.nullConvertToEmptyString(rs.getString("switchname")));
			switchs.setState(StringUtil.nullConvertToEmptyString(rs.getString("state")));

			return switchs;
		}
	}

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public List<Switch> getAllSwitch() {
		String sql = "select * from express_set_switch";
		return jdbcTemplate.query(sql, new SwitchRowMapper());
	}

	public Switch getSwitchBySwitchname(String switchname) {
		try {
			String sql = "select * from express_set_switch where switchname=?";
			return jdbcTemplate.queryForObject(sql, new SwitchRowMapper(), switchname);
		} catch (Exception e) {
			return new Switch();
		}
	}

	public void delSwitch(String state, String switchname) {
		jdbcTemplate.update("update express_set_switch set state=? where switchname=?", state, switchname);
	}

}

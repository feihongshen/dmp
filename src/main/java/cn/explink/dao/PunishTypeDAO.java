package cn.explink.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import cn.explink.domain.PunishType;
import cn.explink.util.Page;

@Component
public class PunishTypeDAO {
	private final class PunishTypeRowMapper implements RowMapper<PunishType> {

		@Override
		public PunishType mapRow(ResultSet rs, int rowNum) throws SQLException {
			PunishType punishType = new PunishType();
			punishType.setId(rs.getLong("id"));
			punishType.setName(rs.getString("name"));
			punishType.setState(rs.getLong("state"));
			return punishType;
		}

	}

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public List<PunishType> getAllPunishTypeByName() {
		String sql = "select * from express_set_punish_type where state=1";
		return this.jdbcTemplate.query(sql, new PunishTypeRowMapper());
	}

	public List<PunishType> getAllPunishTypeByNameAll() {
		String sql = "select * from express_set_punish_type ";
		return this.jdbcTemplate.query(sql, new PunishTypeRowMapper());
	}

	public PunishType getPunishTypeByName(String name) {
		String sql = "select * from express_set_punish_type where name=? ";
		return this.jdbcTemplate.queryForObject(sql, new PunishTypeRowMapper(), name);
	}

	public void crePunishType(String name) {
		String sql = "insert into express_set_punish_type(name) values(?)";
		this.jdbcTemplate.update(sql, name);

	}

	public List<PunishType> getPunishTypeByWhere(long page, String name) {
		String sql = "select * from express_set_punish_type ";
		if (name.length() > 0) {
			sql += " where name='" + name + "'";
		}
		sql += " order by state desc limit " + ((page - 1) * Page.ONE_PAGE_NUMBER) + " ," + Page.ONE_PAGE_NUMBER;
		return this.jdbcTemplate.query(sql, new PunishTypeRowMapper());
	}

	public long getPunishTypeCount(String name) {
		String sql = "select count(1) from express_set_punish_type ";
		if (name.length() > 0) {
			sql += " where name='" + name + "'";
		}
		return this.jdbcTemplate.queryForLong(sql);
	}

	public PunishType getPunishTypeById(long id) {
		try {
			String sql = "select * from express_set_punish_type where id=? ";
			return this.jdbcTemplate.queryForObject(sql, new PunishTypeRowMapper(), id);
		} catch (Exception e) {
			return null;
		}
	}

	public void savePunishType(String name, long id) {
		String sql = "update express_set_punish_type set name=? where id=? ";
		this.jdbcTemplate.update(sql, name, id);
	}

	public void delPunishType(long id) {
		this.jdbcTemplate.update("update express_set_punish_type set state=(state+1)%2 where id=?", id);
	}

	public void delPunishTypeData(long id) {
		String sql = "delete from express_set_punish_type where `id`=?";
		this.jdbcTemplate.update(sql, id);

	}

}

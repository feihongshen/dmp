package cn.explink.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import cn.explink.domain.SystemInstall;
import cn.explink.util.Page;

@Component
public class SystemInstallDAO {
	private final class SystemInstallMapper implements RowMapper<SystemInstall> {
		@Override
		public SystemInstall mapRow(ResultSet rs, int rowNum) throws SQLException {
			SystemInstall systemInstall = new SystemInstall();
			systemInstall.setId(rs.getLong("id"));
			systemInstall.setName(rs.getString("name"));
			systemInstall.setValue(rs.getString("value"));
			systemInstall.setChinesename(rs.getString("chinesename"));
			return systemInstall;
		}
	}

	@Autowired
	private JdbcTemplate jdbcTemplate;

	/*
	 * public long create(final String name,final String value){ KeyHolder key =
	 * new GeneratedKeyHolder(); jdbcTemplate.update(new
	 * PreparedStatementCreator() { public PreparedStatement
	 * createPreparedStatement( java.sql.Connection con) throws SQLException {
	 * PreparedStatement ps = null; ps = con.prepareStatement(
	 * "insert into express_set_system_install(name,value) values(?,?)",new
	 * String[] { "id" }); ps.setString(1, name); ps.setString(2, value); return
	 * ps; } }, key); return key.getKey().longValue(); }
	 */

	public SystemInstall getSystemInstallByName(String name) {
		String sql = "select * from express_set_system_install where `name`=?";
		try {
			return this.jdbcTemplate.queryForObject(sql, new SystemInstallMapper(), name);
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	public SystemInstall getSystemInstallByChineseName(String chinesename) {
		String sql = "select * from express_set_system_install where `chinesename`=?";
		try {
			return this.jdbcTemplate.queryForObject(sql, new SystemInstallMapper(), chinesename);
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	public List<SystemInstall> getAllProperties() {
		String sql = "select * from express_set_system_install";
		return this.jdbcTemplate.query(sql, new SystemInstallMapper());
	}

	public SystemInstall getSystemInstall(String name) {
		try {
			String sql = "select * from express_set_system_install where name=?";
			return this.jdbcTemplate.queryForObject(sql, new SystemInstallMapper(), name);
		} catch (DataAccessException e) {
			return null;
		}
	}

	public SystemInstall getSystemInstallById(long id) {
		try {
			String sql = "select * from express_set_system_install where  `id`=? ";
			return this.jdbcTemplate.queryForObject(sql, new SystemInstallMapper(), id);
		} catch (DataAccessException e) {
			return null;
		}
	}

	public SystemInstall getSystemInstallByParam(String chinesename, String name, String value) {
		try {
			String sql = "select * from express_set_system_install where  `chinesename`=? and `name`=? and `value`=?";
			return this.jdbcTemplate.queryForObject(sql, new SystemInstallMapper(), chinesename, name, value);
		} catch (DataAccessException e) {
			return null;
		}
	}

	public SystemInstall getSystemInstallByName(String chinesename, String name) {
		try {
			String sql = "select * from express_set_system_install where  `chinesename`=? and `name`=? ";
			return this.jdbcTemplate.queryForObject(sql, new SystemInstallMapper(), chinesename, name);
		} catch (DataAccessException e) {
			return null;
		}
	}

	public void creSystemInstall(String chinesename, String name, String value) {
		String sql = "insert into express_set_system_install (`chinesename`,`name`,`value`) values (?,?,?)";
		this.jdbcTemplate.update(sql, chinesename, name, value);
	}

	public List<SystemInstall> getSystemInstallByWhere(long page, String chinesename, String value) {
		String sql = "SELECT * from express_set_system_install ";
		sql = this.getSystemInstallByPageWhereSql(sql, chinesename, value);
		sql += " limit " + ((page - 1) * Page.ONE_PAGE_NUMBER) + " ," + Page.ONE_PAGE_NUMBER;

		List<SystemInstall> cscList = this.jdbcTemplate.query(sql, new SystemInstallMapper());
		return cscList;
	}

	public long getSystemInstallCount(String chinesename, String value) {
		String sql = "SELECT count(1) from express_set_system_install";
		sql = this.getSystemInstallByPageWhereSql(sql, chinesename, value);
		return this.jdbcTemplate.queryForLong(sql);
	}

	private String getSystemInstallByPageWhereSql(String sql, String chinesename, String value) {
		if ((chinesename.length() > 0) || (value.length() > 0)) {
			StringBuffer w = new StringBuffer();
			sql += " where ";
			if (chinesename.length() > 0) {
				w.append(" and `chinesename`='" + chinesename + "'");
			}
			if (value.length() > 0) {
				w.append(" and `value`='" + value + "'");
			}
			sql += w.substring(4, w.length());
		}
		return sql;
	}

	public void saveSystemInstall(String chinesename, String name, String value, long id) {
		String sql = "update express_set_system_install set `name`=?,`value`=?,`chinesename`=? where `id`=?";
		this.jdbcTemplate.update(sql, name, value, chinesename, id);

	}

	public void delSystemInstallById(long id) {
		String sql = "delete from express_set_system_install where `id`=?";
		this.jdbcTemplate.update(sql, id);

	}
}

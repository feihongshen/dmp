package cn.explink.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import cn.explink.domain.AbnormalType;
import cn.explink.util.Page;

@Component
public class AbnormalTypeDAO {

	private final class AbnormalTypeRowMapper implements RowMapper<AbnormalType> {
		@Override
		public AbnormalType mapRow(ResultSet rs, int rowNum) throws SQLException {
			AbnormalType abnormalType = new AbnormalType();

			abnormalType.setId(rs.getLong("id"));
			abnormalType.setName(rs.getString("name"));
			abnormalType.setState(rs.getLong("state"));

			return abnormalType;
		}
	}

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public List<AbnormalType> getAllAbnormalTypeByName() {
		String sql = "select * from express_set_abnormal_type where state=1";
		return jdbcTemplate.query(sql, new AbnormalTypeRowMapper());
	}

	public List<AbnormalType> getAllAbnormalTypeByNameAll() {
		String sql = "select * from express_set_abnormal_type ";
		return jdbcTemplate.query(sql, new AbnormalTypeRowMapper());
	}

	public List<AbnormalType> getAbnormalTypeByName(String name) {
		String sql = "select * from express_set_abnormal_type where name=? ";
		return jdbcTemplate.query(sql, new AbnormalTypeRowMapper(), name);
	}

	public void creAbnormalType(String name) {
		String sql = "insert into express_set_abnormal_type(name) values(?)";
		jdbcTemplate.update(sql, name);

	}

	public List<AbnormalType> getAbnormalTypeByWhere(long page, String name) {
		String sql = "select * from express_set_abnormal_type ";
		if (name.length() > 0) {
			sql += " where name='" + name + "'";
		}
		sql += " order by state desc limit " + (page - 1) * Page.ONE_PAGE_NUMBER + " ," + Page.ONE_PAGE_NUMBER;
		return jdbcTemplate.query(sql, new AbnormalTypeRowMapper());
	}

	public long getAbnormalTypeCount(String name) {
		String sql = "select count(1) from express_set_abnormal_type ";
		if (name.length() > 0) {
			sql += " where name='" + name + "'";
		}
		return jdbcTemplate.queryForLong(sql);
	}

	public AbnormalType getAbnormalTypeById(long id) {
		try {
			String sql = "select * from express_set_abnormal_type where id=? ";
			return jdbcTemplate.queryForObject(sql, new AbnormalTypeRowMapper(), id);
		} catch (Exception e) {
			return null;
		}
	}

	public void saveAbnormalType(String name, long id) {
		String sql = "update express_set_abnormal_type set name=? where id=? ";
		jdbcTemplate.update(sql, name, id);
	}

	public void delAbnormalType(long id) {
		jdbcTemplate.update("update express_set_abnormal_type set state=(state+1)%2 where id=?", id);
	}

}

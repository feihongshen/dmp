package cn.explink.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import cn.explink.domain.Department;
import cn.explink.util.StringUtil;

@Component
public class DepartDAO {
	private final class DepartMapper implements RowMapper<Department> {
		@Override
		public Department mapRow(ResultSet rs, int rowNum) throws SQLException {
			Department department = new Department();
			department.setId(rs.getLong("departid"));
			department.setName(StringUtil.nullConvertToEmptyString(rs.getString("departname")));
			department.setRemark(StringUtil.nullConvertToEmptyString(rs.getString("departremark")));
			return department;
		}
	}

	@Autowired
	JdbcTemplate jdbcTemplate;

	public Department getDepartment(long id) {
		try {
			return jdbcTemplate.queryForObject("select * from express_set_depart where departid=?", new DepartMapper(), id);
		} catch (DataAccessException e) {
			return null;
		}
	}
}

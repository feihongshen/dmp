package cn.explink.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import cn.explink.domain.ServiceArea;

@Component
public class ServiceAreaDAO {

	private final class ServiceAreaRowMapper implements RowMapper<ServiceArea> {
		@Override
		public ServiceArea mapRow(ResultSet rs, int rowNum) throws SQLException {
			ServiceArea serviceArea = new ServiceArea();
			serviceArea.setServiceareaid(rs.getLong("serviceareaid"));
			serviceArea.setServiceareaname(rs.getString("serviceareaname"));
			serviceArea.setServicearearemark(rs.getString("servicearearemark"));
			serviceArea.setCustomerid(rs.getLong("customerid"));
			serviceArea.setServid(rs.getString("servid"));
			return serviceArea;
		}
	}

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public ServiceArea getServiceAreaByName(String housename) {
		String sql = "select * from express_set_servicearea where serviceareaname=?";
		return jdbcTemplate.queryForObject(sql, new ServiceAreaRowMapper(), housename);
	}

	public List<ServiceArea> getAllServiceArea(String housename) {
		String sql = "select * from express_set_servicearea";
		return jdbcTemplate.query(sql, new ServiceAreaRowMapper(), housename);
	}

	public List<ServiceArea> getAllServiceArea() {
		return jdbcTemplate.query("select * from express_set_servicearea", new ServiceAreaRowMapper());
	}

}

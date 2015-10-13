package cn.explink.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import cn.explink.domain.ServiceArea;

@Component
public class DeliverTypeDAO {

	private final class ServiceAreaRowMapper implements RowMapper<ServiceArea> {
		@Override
		public ServiceArea mapRow(ResultSet rs, int rowNum) throws SQLException {
			ServiceArea serviceArea = new ServiceArea();
			serviceArea.setCustomerid(rs.getLong("customerid"));
			return serviceArea;
		}
	}

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public ServiceArea getServiceAreaByName(String housename) {
		String sql = "select * from express_set_servicearea where serviceareaname=?";
		return jdbcTemplate.queryForObject(sql, new ServiceAreaRowMapper(), housename);
	}

}

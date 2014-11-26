package cn.explink.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
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
		try {
			String sql = "select * from express_set_servicearea where serviceareaname=?";
			return jdbcTemplate.queryForObject(sql, new ServiceAreaRowMapper(), housename);
		} catch (DataAccessException e) {
			// TODO Auto-generated catch block
			return null;
		}
	}

	public List<ServiceArea> getAllServiceArea() {
		return jdbcTemplate.query("select * from express_set_servicearea", new ServiceAreaRowMapper());
	}

	public long reateServiceArea(final ServiceArea serviceArea) {
		KeyHolder key = new GeneratedKeyHolder();
		jdbcTemplate.update(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(java.sql.Connection con) throws SQLException {
				PreparedStatement ps = null;
				ps = con.prepareStatement("insert into express_set_servicearea(customerid,serviceareaname) values(?,?) ", new String[] { "serviceareaid" });
				ps.setLong(1, serviceArea.getCustomerid());
				ps.setString(2, serviceArea.getServiceareaname());
				return ps;
			}
		}, key);
		serviceArea.setServiceareaid(key.getKey().longValue());
		return key.getKey().longValue();
	}

	public Map<String, ServiceArea> getAllServiceAreaNames() {
		List<ServiceArea> serviceAreaList = getAllServiceArea();
		Map<String, ServiceArea> serviceAreaMap = new HashMap<String, ServiceArea>();
		if (serviceAreaList != null) {
			for (ServiceArea serviceArea : serviceAreaList) {
				serviceAreaMap.put(serviceArea.getServiceareaname(), serviceArea);
			}
		}
		return serviceAreaMap;
	}

}

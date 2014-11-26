package cn.explink.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import cn.explink.domain.Shipper;

@Component
public class ShipperDAO {
	private final class ShipperRowMapper implements RowMapper<Shipper> {
		@Override
		public Shipper mapRow(ResultSet rs, int rowNum) throws SQLException {
			Shipper shipper = new Shipper();
			return shipper;
		}
	}

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public Shipper getShipperByname(String branchname) {
		return jdbcTemplate.queryForObject("SELECT * from express_set_shipper where shippername=?", new ShipperRowMapper(), branchname);
	}

}

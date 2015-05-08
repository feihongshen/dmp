package cn.explink.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import cn.explink.domain.DeliveryStations;


@Repository
public class FindLocationDao {
	@Autowired
	private JdbcTemplate jt;
	
	
	private final class DeliveryStationsRowMapper implements RowMapper<DeliveryStations>{

		@Override
		public DeliveryStations mapRow(ResultSet rs, int rowNum)
				throws SQLException {
			DeliveryStations ds = new DeliveryStations();
			ds.setId(rs.getInt("ID"));
			ds.setName(rs.getString("NAME"));
			ds.setCoordinate(rs.getString("COORDINATE"));
			ds.setMapcenter_lat(rs.getBigDecimal("MAPCENTER_LAT"));
			ds.setMapcenter_lng(rs.getBigDecimal("MAPCENTER_LNG"));
			ds.setAddress(rs.getString("ADDRESS"));
			ds.setTypeid(rs.getInt("typeid"));
			return ds;
		}
	
	}
	
	//查询所有地址
	public List<DeliveryStations> findALLDeliveryStations(){
		String sql="select * from delivery_stations";
		return this.jt.query(sql,new DeliveryStationsRowMapper());
	}	
}

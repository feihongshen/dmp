/**
 *
 */
package cn.explink.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import cn.explink.domain.Area;
import cn.explink.util.StringUtil;

/**
 * @author Administrator
 *
 */
@Component
public class AreaDAO {
	@Autowired
	private JdbcTemplate jdbcTemplate;

	final class AreaRowMapper implements RowMapper<Area> {
		@Override
		public Area mapRow(ResultSet rs, int rowNum) throws SQLException {
			Area ar = new Area();
			ar.setId(rs.getLong("id"));
			ar.setCity(StringUtil.nullConvertToEmptyString(rs.getString("city")));
			ar.setArea(StringUtil.nullConvertToEmptyString(rs.getString("area")));
			ar.setRemark(StringUtil.nullConvertToEmptyString(rs.getString("remark")));
			ar.setIsareafee(rs.getInt("isareafee"));
			ar.setIsoverbig(rs.getInt("isoverbig"));
			ar.setIsoverweight(rs.getInt("isoverweight"));
			return ar;
		}
	}



	public List<Area> getAllArea() {
		String sql = "select * from express_set_area ";
		try {
			return this.jdbcTemplate.query(sql, new AreaRowMapper());
		} catch (Exception e) {
			return null;
		}
	}
	public Area getAreaByCity(String city) {
		String sql = "select * from express_set_area where city=?";
		try {
			return this.jdbcTemplate.queryForObject(sql, new AreaRowMapper(),city);
		} catch (Exception e) {
			return null;
		}
	}
	public Area getAreaByArea(String area) {
		String sql = "select * from express_set_area where area=?";
		try {
			return this.jdbcTemplate.queryForObject(sql, new AreaRowMapper(),area);
		} catch (Exception e) {
			return null;
		}
	}

	public Area getAreaByCityAndArea(String city,String area) {
		String sql = "select * from express_set_area where city=? and area=?";
		try {
			return this.jdbcTemplate.queryForObject(sql, new AreaRowMapper(),city,area);
		} catch (Exception e) {
			return null;
		}
	}

}

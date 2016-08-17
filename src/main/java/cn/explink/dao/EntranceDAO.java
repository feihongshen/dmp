package cn.explink.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import cn.explink.domain.Entrance;
import cn.explink.util.StringUtil;

/**
 * 自动分拣线上货口dao
 * @author mali 2016年3月24日
 *
 */
@Component
public class EntranceDAO {
	
	private final class EntranceRowMapper implements RowMapper<Entrance> {
		@Override
		public Entrance mapRow(ResultSet rs, int rowNum) throws SQLException {
			Entrance entrance = new Entrance();
			entrance.setEntranceno(rs.getString("entranceno"));
			entrance.setEntranceip(StringUtil.nullConvertToEmptyString(rs.getString("entranceip")));
			entrance.setEnable(rs.getInt("enable"));
			return entrance;
		}
	}
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	public List<Entrance> getAllEnableEntrances(){
		String sql="select *  from express_set_entrance where enable=0 order by entranceno";
		return this.jdbcTemplate.query(sql,new EntranceRowMapper());
		
	}
	
	public Entrance getEntranceByNo(String EntranceNo){
		String sql="select *  from express_set_entrance where enable=0 and entranceno =?";
		return this.jdbcTemplate.queryForObject(sql,new EntranceRowMapper(),EntranceNo);
		
	}

}

package cn.explink.dao.express;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import cn.explink.domain.VO.express.AdressVO;

/**
 *
 * @description 对街道表（express_set_town）进行操作的dao
 * @author  刘武强
 * @data   2015年8月7日
 */

@Component
public class TownDAO {
	@Autowired
	private JdbcTemplate jdbcTemplate;

	private final class TownRowMapper implements RowMapper<AdressVO> {
		@Override
		public AdressVO mapRow(ResultSet rs, int rowNum) throws SQLException {
			AdressVO adressVO = new AdressVO();
			adressVO.setId(rs.getInt("id"));
			adressVO.setCode(rs.getString("code"));
			adressVO.setName(rs.getString("name"));
			adressVO.setParentCode(rs.getString("parentCode"));
			return adressVO;
		}
	}

	/**
	 *
	 * @Title: getProvince
	 * @description 根据区/县的code，获取该区下的说县/街道
	 * @author 刘武强
	 * @date  2015年8月7日下午1:52:34
	 * @param  @param parenCode
	 * @param  @return
	 * @return  List<AdressVO>
	 * @throws
	 */
	public List<AdressVO> getTownOfCounty(String parenCode) {
		StringBuffer sql = new StringBuffer();
		List<AdressVO> list = new ArrayList<AdressVO>();
		sql.append("select id,code,name,county_code as parentCode from express_set_town  where county_code=? order by code desc");
		list = this.jdbcTemplate.query(sql.toString(), new TownRowMapper(), parenCode);
		return list;
	}

	public List<AdressVO> getAllTown() {
		StringBuffer sql = new StringBuffer();
		List<AdressVO> list = new ArrayList<AdressVO>();
		sql.append("select id,code,name,county_code as parentCode from express_set_town order by code desc");
		list = this.jdbcTemplate.query(sql.toString(), new TownRowMapper());
		return list;
	}

	/**
	 *
	 * @Title: getTownByTownNames
	 * @description 通过街道的名字的集合（字符串），查询字符串内所有街道的信息
	 * @author 刘武强
	 * @date  2015年10月12日上午11:05:22
	 * @param  @param TownNames
	 * @param  @return
	 * @return  List<AdressVO>
	 * @throws
	 */
	public List<AdressVO> getTownByTownNames(String TownNames) {
		StringBuffer sql = new StringBuffer();
		List<AdressVO> list = new ArrayList<AdressVO>();
		sql.append("select id,code,name,county_code as parentCode from express_set_town where name in " + TownNames + " order by code asc");
		list = this.jdbcTemplate.query(sql.toString(), new TownRowMapper());
		return list;
	}
}

package cn.explink.dao.express;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import cn.explink.domain.VO.express.AdressVO;

/**
*
* @description 对区/县表（express_set_county）进行操作的dao
* @author  刘武强
* @data   2015年8月7日
*/

@Component
public class CountyDAO {
	@Autowired
	private JdbcTemplate jdbcTemplate;

	private final class CountyRowMapper implements RowMapper<AdressVO> {
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
	 * @description 根据市的code，获取该市下的说有区/县
	 * @author 刘武强
	 * @date  2015年8月7日下午1:52:34
	 * @param  @param parenCode
	 * @param  @return
	 * @return  List<AdressVO>
	 * @throws
	 */
	public List<AdressVO> getCountyOfCity(String parenCode) {
		StringBuffer sql = new StringBuffer();
		List<AdressVO> list = new ArrayList<AdressVO>();
		sql.append("select id,code,name,city_code as parentCode from express_set_county where city_code=? order by code desc");
		list = this.jdbcTemplate.query(sql.toString(), new CountyRowMapper(), parenCode);
		return list;
	}

	/**
	 * 根据市的id，获取该市下的说有区
	 *
	 * @param parentId
	 * @return
	 */
	public List<AdressVO> getCountyOfCity(Integer parentId) {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT c.id,c.code,c.name,city_code as parentCode FROM express_set_county c  LEFT JOIN express_set_city p ON c.city_code=p.code WHERE p.id=? order by code desc");
		return this.jdbcTemplate.query(sql.toString(), new CountyRowMapper(), parentId);
	}

	/**
	 * 获取所有的乡
	 * @return
	 */
	public List<AdressVO> getAllCounty() {
		StringBuffer sql = new StringBuffer();
		List<AdressVO> list = new ArrayList<AdressVO>();
		sql.append("select id,code,name,city_code as parentCode from express_set_county order by code desc");
		list = this.jdbcTemplate.query(sql.toString(), new CountyRowMapper());
		return list;
	}

	/**
	 *
	 * @Title: getCountysByCountyNames
	 * @description 通过区/县的名字的集合（字符串），查询字符串内所有区/县的信息
	 * @author 刘武强
	 * @date  2015年10月12日上午11:01:42
	 * @param  @param CountyNames
	 * @param  @return
	 * @return  List<AdressVO>
	 * @throws
	 */
	public List<AdressVO> getCountysByCountyNames(String CountyNames) {
		StringBuffer sql = new StringBuffer();
		List<AdressVO> list = new ArrayList<AdressVO>();
		sql.append("select id,code,name,city_code as parentCode from express_set_county where name in " + CountyNames);
		list = this.jdbcTemplate.query(sql.toString(), new CountyRowMapper());
		return list;
	}
	
	/**
	 * 根据ID查询
	 * @date 2016年5月17日 下午4:41:34
	 * @param id
	 * @return
	 */
	public AdressVO getCountyById(int id) {
		StringBuffer sql = new StringBuffer();
		AdressVO county = new AdressVO();
		sql.append("select id,code,name,city_code as parentCode from express_set_county where id=?");
		county = this.jdbcTemplate.queryForObject(sql.toString(), new CountyRowMapper(), id);
		return county;
	}
	public AdressVO getCountyByCode(String code) {
		StringBuffer sql = new StringBuffer();
		AdressVO county = new AdressVO();
		sql.append("select id,code,name,city_code as parentCode from express_set_county where code=?");
		county = this.jdbcTemplate.queryForObject(sql.toString(), new CountyRowMapper(), code);
		return county;
	}
	/**
	 * 通过名称及所属城市获取镇
	 * 
	 */
	public AdressVO getCountyByNameAndCity(String name, AdressVO vo){
		if(StringUtils.isEmpty(name) || vo == null){
			return null;
		}
		String cityCode = vo.getCode();
		String sql = "select id,code,name,city_code as parentCode from express_set_county where name=? and city_code = ?";
		List<AdressVO> voList = jdbcTemplate.query(sql, new CountyRowMapper(), name, cityCode);
		if(CollectionUtils.isEmpty(voList)){
			return null;
		}
		return voList.get(0);
	}
}
package cn.explink.dao.express;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import cn.explink.domain.VO.express.AdressVO;
import cn.explink.util.StringUtil;

/**
 *
 * @description 对市表（express_set_city）进行操作的dao
 * @author 刘武强
 * @data 2015年8月7日
 */
@Component
public class CityDAO {
	@Autowired
	private JdbcTemplate jdbcTemplate;

	private final class CityRowMapper implements RowMapper<AdressVO> {
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
	 * @description 根据省的code，获取该省下的说有市
	 * @author 刘武强
	 * @date 2015年8月7日下午1:52:34
	 * @param @param parenCode
	 * @param @return
	 * @return List<AdressVO>
	 * @throws
	 */
	public List<AdressVO> getCityOfProvince(String parentCode) {
		StringBuffer sql = new StringBuffer();
		List<AdressVO> list = new ArrayList<AdressVO>();
		sql.append("select id,code,name,province_code as parentCode from express_set_city where province_code=? order by code desc");
		list = this.jdbcTemplate.query(sql.toString(), new CityRowMapper(), parentCode);
		return list;
	}

	/**
	 *
	 * @Title: getCityByCityNames
	 * @description 通过市的名字的集合（字符串），查询字符串内所有市的信息
	 * @author 刘武强
	 * @date  2015年10月12日上午10:59:41
	 * @param  @param CityNames
	 * @param  @return
	 * @return  List<AdressVO>
	 * @throws
	 */
	public List<AdressVO> getCityByCityNames(String CityNames) {
		StringBuffer sql = new StringBuffer();
		List<AdressVO> list = new ArrayList<AdressVO>();
		sql.append("select id,code,name,province_code as parentCode  from express_set_city where name in " + CityNames + " order by code asc");
		list = this.jdbcTemplate.query(sql.toString(), new CityRowMapper());
		return list;
	}

	/**
	 * 根据省的id，获取该省下的说有市
	 *
	 * @param parentId
	 * @return
	 */
	public List<AdressVO> getCityOfProvince(Integer parentId) {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT c.id,c.code,c.name,c.province_code AS parentCode FROM express_set_city c " + "LEFT JOIN express_set_province p ON c.province_code=p.code WHERE p.id=? order by code desc");
		return this.jdbcTemplate.query(sql.toString(), new CityRowMapper(), parentId);
	}

	private final class CityRowMapper4Query implements RowMapper<AdressVO> {
		@Override
		public AdressVO mapRow(ResultSet rs, int rowNum) throws SQLException {
			AdressVO adressVO = new AdressVO();
			adressVO.setId(rs.getInt("id"));
			adressVO.setCode(rs.getString("code"));
			adressVO.setName(rs.getString("name"));
			return adressVO;
		}
	}

	public List<AdressVO> getAllCity() {
		StringBuffer sql = new StringBuffer();
		List<AdressVO> list = new ArrayList<AdressVO>();
		sql.append("select id,code,name from express_set_city order by code desc");
		list = this.jdbcTemplate.query(sql.toString(), new CityRowMapper4Query());
		return list;
	}

	public AdressVO getProvinceById(int id) {
		String sql = "select id,code,name,province_code AS parentCode from express_set_city where id=" + id;
		AdressVO adressVO = null;
		try {
			adressVO = this.jdbcTemplate.queryForObject(sql, new CityRowMapper());
		} catch (DataAccessException e) {
			return null;
		}
		return adressVO;
	}
	
	/**
	 * 通过名字及所属省份编码获取市
	 */
	public AdressVO getCityByNameAndProvice(String name, AdressVO vo){
		if(StringUtil.isEmpty(name) || vo == null){
			return null;
		}
		String proviceCode = vo.getCode();
		String sql = "select id,code,name,province_code AS parentCode from express_set_city where name= ? and province_code = ?";
		List<AdressVO> voList = jdbcTemplate.query(sql, new CityRowMapper(), name, proviceCode);
		if(CollectionUtils.isEmpty(voList)){
			return null;
		}
		return voList.get(0);
	}


    /**
     * @return AdressVO with parentCode
     */
    public List<AdressVO> getAllCityWithParentCode() {
        StringBuffer sql = new StringBuffer();
        List<AdressVO> list = new ArrayList<AdressVO>();
        sql.append("select id,code,name,province_code as parentCode from express_set_city order by code desc;");
        list = this.jdbcTemplate.query(sql.toString(), new CityRowMapper());
        return list;
    }

    public AdressVO getCityById(int id) {
        String sql = "select id,code,name,province_code as parentCode from express_set_city where id=?";
        AdressVO adressVO = null;
        try {
            adressVO = this.jdbcTemplate.queryForObject(sql, new CityRowMapper(), id);
        } catch (DataAccessException e) {
            return null;
        }
        return adressVO;
    }
    public AdressVO getCityByCode(String code) {
        String sql = "select id,code,name,province_code as parentCode from express_set_city where code=?";
        AdressVO adressVO = null;
        try {
            adressVO = this.jdbcTemplate.queryForObject(sql, new CityRowMapper(), code);
        } catch (DataAccessException e) {
            return null;
        }
        return adressVO;
    }
}

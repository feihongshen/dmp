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

import cn.explink.domain.VO.express.AdressVO;
import cn.explink.domain.express.NewAreaForm;

/**
 *
 * @description 对省表（express_set_province）进行操作的dao
 * @author 刘武强
 * @data 2015年8月7日
 */
@Component
public class ProvinceDAO {
	@Autowired
	private JdbcTemplate jdbcTemplate;

	private final class ProvinceRowMapper implements RowMapper<AdressVO> {
		@Override
		public AdressVO mapRow(ResultSet rs, int rowNum) throws SQLException {
			AdressVO adressVO = new AdressVO();
			adressVO.setId(rs.getInt("id"));
			adressVO.setCode(rs.getString("code"));
			adressVO.setName(rs.getString("name"));
			return adressVO;
		}
	}

	private final class newProvinceRowMapper implements RowMapper<NewAreaForm> {
		@Override
		public NewAreaForm mapRow(ResultSet rs, int rowNum) throws SQLException {
			NewAreaForm addressVO = new NewAreaForm();
			addressVO.setAddressId(rs.getInt("id"));
			addressVO.setAddressCode(rs.getString("code"));
			addressVO.setAddressName(rs.getString("name"));
			addressVO.setAddressLevel(1);
			addressVO.setIsDirectly(0);
			addressVO.setParentCode("101");
			addressVO.setParentName("中国");
			return addressVO;
		}
	}

	private final class NextAddressRowMapper implements RowMapper<NewAreaForm> {
		private int level;
		private String parentcodeName;;

		NextAddressRowMapper(String table, String parentcodeName) {
			this.parentcodeName = parentcodeName;
			if ("express_set_province".equals(table)) {
				this.level = 1;
			} else if ("express_set_city".equals(table)) {
				this.level = 2;
			} else if ("express_set_county".equals(table)) {
				this.level = 3;
			} else {
				this.level = 4;
			}
		}

		@Override
		public NewAreaForm mapRow(ResultSet rs, int rowNum) throws SQLException {
			NewAreaForm addressVO = new NewAreaForm();
			addressVO.setAddressId(rs.getInt("id"));
			addressVO.setAddressCode(rs.getString("code"));
			addressVO.setAddressName(rs.getString("name"));
			addressVO.setAddressLevel(this.level);
			addressVO.setIsDirectly(0);
			addressVO.setParentCode(rs.getString(this.parentcodeName));
			addressVO.setParentName("");
			return addressVO;
		}
	}

	/**
	 *
	 * @Title: getProvince
	 * @description 获取所有的省
	 * @author 刘武强
	 * @date 2015年8月7日下午1:53:04
	 * @param @return
	 * @return List<AdressVO>
	 * @throws
	 */
	public List<AdressVO> getProvince() {
		StringBuffer sql = new StringBuffer();
		List<AdressVO> list = new ArrayList<AdressVO>();
		sql.append("select * from express_set_province order by code asc");
		list = this.jdbcTemplate.query(sql.toString(), new ProvinceRowMapper());
		return list;
	}

	/**
	 *
	 * @Title: getProvincesByProviceNames
	 * @description 通过省的名字的集合（字符串），查询字符串内所有省的信息
	 * @author 刘武强
	 * @date  2015年10月12日上午10:53:49
	 * @param  @param ProvicneNames
	 * @param  @return
	 * @return  List<AdressVO>
	 * @throws
	 */

	public List<AdressVO> getProvincesByProviceNames(String ProvicneNames) {
		StringBuffer sql = new StringBuffer();
		List<AdressVO> list = new ArrayList<AdressVO>();
		sql.append("select id,code,name from express_set_province where name in " + ProvicneNames + "order by code asc");
		list = this.jdbcTemplate.query(sql.toString(), new ProvinceRowMapper());
		return list;
	}

	/**
	 *
	 * @Title: getNewProvinceInner
	 * @description 获取境内的省份
	 * @author 刘武强
	 * @date  2015年9月28日下午12:48:09
	 * @param  @return
	 * @return  List<NewAreaForm>
	 * @throws
	 */
	public List<NewAreaForm> getNewProvinceInner() {
		StringBuffer sql = new StringBuffer();
		List<NewAreaForm> list = new ArrayList<NewAreaForm>();
		sql.append("select * from express_set_province where code< 200000 order by code asc");
		list = this.jdbcTemplate.query(sql.toString(), new newProvinceRowMapper());
		return list;
	}

	/**
	 *
	 * @Title: getNewProvinceInner
	 * @description 获取境外的省份
	 * @author 刘武强
	 * @date  2015年9月28日下午12:48:09
	 * @param  @return
	 * @return  List<NewAreaForm>
	 * @throws
	 */
	public List<NewAreaForm> getNewProvinceOut() {
		StringBuffer sql = new StringBuffer();
		List<NewAreaForm> list = new ArrayList<NewAreaForm>();
		sql.append("select * from express_set_province where code>= 200000 order by code asc");
		list = this.jdbcTemplate.query(sql.toString(), new newProvinceRowMapper());
		return list;
	}

	public List<NewAreaForm> getNextAddress(String parentCode) {
		String table = "express_set_province";
		String parentcodeName = "province_code";
		if (parentCode.length() < 9) {
			table = "express_set_city";
			parentcodeName = "province_code";
		} else if (parentCode.length() < 12) {
			table = "express_set_county";
			parentcodeName = "city_code";
		} else {
			table = "express_set_town";
			parentcodeName = "county_code";
		}
		StringBuffer sql = new StringBuffer();
		List<NewAreaForm> list = new ArrayList<NewAreaForm>();
		sql.append("select * from ").append(table);
		sql.append(" where ").append(parentcodeName).append("=");
		sql.append(parentCode).append(" order by code asc");
		list = this.jdbcTemplate.query(sql.toString(), new NextAddressRowMapper(table, parentcodeName));
		return list;
	}

	private final class ProvinceRowMapper4Query implements RowMapper<AdressVO> {
		@Override
		public AdressVO mapRow(ResultSet rs, int rowNum) throws SQLException {
			AdressVO adressVO = new AdressVO();
			adressVO.setId(rs.getInt("id"));
			adressVO.setCode(rs.getString("code"));
			adressVO.setName(rs.getString("name"));
			return adressVO;
		}
	}

	public List<AdressVO> getAllProvince() {
		StringBuffer sql = new StringBuffer();
		List<AdressVO> list = new ArrayList<AdressVO>();
		sql.append("select id,code,name from express_set_province order by code desc");
		list = this.jdbcTemplate.query(sql.toString(), new ProvinceRowMapper4Query());
		return list;
	}

	public AdressVO getProvinceById(int id) {
		String sql = "select id,code,name from express_set_province where id=" + id;
		AdressVO adressVO = null;
		try {
			adressVO = this.jdbcTemplate.queryForObject(sql, new ProvinceRowMapper4Query());
		} catch (DataAccessException e) {
			return null;
		}
		return adressVO;
	}
	
	public AdressVO getProvinceByCode(String code) {
		String sql = "select id,code,name from express_set_province where code=?";
		AdressVO adressVO = null;
		try {
			adressVO = this.jdbcTemplate.queryForObject(sql, new ProvinceRowMapper4Query(), code);
		} catch (DataAccessException e) {
			e.printStackTrace();
			return null;
		}
		return adressVO;
	}
}

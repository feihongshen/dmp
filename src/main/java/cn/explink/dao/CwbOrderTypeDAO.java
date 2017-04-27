package cn.explink.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import cn.explink.domain.ImportCwbOrderType;
import cn.explink.util.Page;

@Component
public class CwbOrderTypeDAO {
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	private final class CwbOrderTypeMapper implements RowMapper<ImportCwbOrderType> {
		@Override
		public ImportCwbOrderType mapRow(ResultSet rs, int rowNum) throws SQLException {
			ImportCwbOrderType importCwbOrderType = new ImportCwbOrderType();
			importCwbOrderType.setImportid(rs.getLong("importid"));
			importCwbOrderType.setImporttypeid(rs.getLong("importtypeid"));
			importCwbOrderType.setImporttype(rs.getString("importtype"));
			return importCwbOrderType;
		}
	}

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public long getImporttypeidByImportType(String importtype) {
		try {
			return jdbcTemplate.queryForObject("SELECT * from express_set_importset where importsetflag=1 and importtype=?", new CwbOrderTypeMapper(), importtype).getImporttypeid();
		} catch (Exception e) {
			throw new RuntimeException("无法匹配到正确的订单类型!");
			// return 1;
		}
	}

	public List<ImportCwbOrderType> getImportType(String importtype) {
		return jdbcTemplate.query("SELECT * from express_set_importset where importsetflag=1 and importtype=?", new CwbOrderTypeMapper(), importtype);
	}

	public void delImportCwbOrderType(long importid) {
		jdbcTemplate.update("UPDATE express_set_importset SET importsetflag=0 WHERE importid=?", importid);
	}

	public List<ImportCwbOrderType> getImportCwbOrderTypeByimporttypeid(int importtypeid) {
		return jdbcTemplate.query("SELECT * FROM express_set_importset where importsetflag=1 and importtypeid=?", new CwbOrderTypeMapper(), importtypeid);
	}

	public ImportCwbOrderType getImportCwbOrderTypeByimportid(long importid) {
		ImportCwbOrderType importCwbOrderType = jdbcTemplate.queryForObject("SELECT * FROM express_set_importset where importsetflag=1 and importid=?", new CwbOrderTypeMapper(), importid);
		return importCwbOrderType;
	}

	public List<ImportCwbOrderType> getImportCwbOrderType() {
		return jdbcTemplate.query("SELECT * FROM express_set_importset where importsetflag=1 order by importtypeid", new CwbOrderTypeMapper());
	}

	public void creImportCwbOrderType(final ImportCwbOrderType importCwbOrderType) {
		jdbcTemplate.update("insert into express_set_importset(importtypeid,importtype,importsetflag)" + "values(?,?,1)", new PreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				ps.setLong(1, importCwbOrderType.getImporttypeid());
				ps.setString(2, importCwbOrderType.getImporttype());
			}
		});
	}

	public void saveImportCwbOrderType(final ImportCwbOrderType importCwbOrderType) {
		jdbcTemplate.update("update express_set_importset set importtypeid=?,importtype=? where importid=? ", new PreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				ps.setLong(1, importCwbOrderType.getImporttypeid());
				ps.setString(2, importCwbOrderType.getImporttype());
				ps.setLong(3, importCwbOrderType.getImportid());
			}
		});
	}

	public String getImportCwbOrderTypeByPageWhereSql(String sql, int cwbordertype) {
		if (cwbordertype <= 0) {
			sql += "where importsetflag=1 order by importtypeid";
		} else if (cwbordertype > 0) {
			sql += "where importsetflag=1 and importtypeid='" + cwbordertype + "' order by importtypeid";
		}
		return sql;
	}

	public long getCwbOrderTypeConut(int importtypeid) {
		String sql = "SELECT COUNT(1) FROM express_set_importset ";
		sql = this.getImportCwbOrderTypeByPageWhereSql(sql, importtypeid);
		return jdbcTemplate.queryForLong(sql);
	}

	public List<ImportCwbOrderType> getCwbOrderTypeByPage(long page, int importtypeid) {
		String sql = "SELECT * from express_set_importset ";
		sql = this.getImportCwbOrderTypeByPageWhereSql(sql, importtypeid);
		sql += " limit " + (page - 1) * Page.ONE_PAGE_NUMBER + "," + Page.ONE_PAGE_NUMBER;
		List<ImportCwbOrderType> importCwbOrderTypes = jdbcTemplate.query(sql, new CwbOrderTypeMapper());
		return importCwbOrderTypes;
	}

	public Map<String, Long> getOrderTypeMappings() {
		String sql = "SELECT * FROM express_set_importset WHERE "
				+ " = 1";
		List<ImportCwbOrderType> orderTypeList = jdbcTemplate.query(sql, new CwbOrderTypeMapper());
		Map<String, Long> orderTypeMapping = new HashMap<String, Long>();
		if (orderTypeList != null) {
			for (ImportCwbOrderType type : orderTypeList) {
				logger.info("导入数据OrderTypeMap == key:{},value:{}", type.getImporttype(), type.getImporttypeid());
				orderTypeMapping.put(type.getImporttype(), type.getImporttypeid());
			}
		}
		return orderTypeMapping;
	}

}

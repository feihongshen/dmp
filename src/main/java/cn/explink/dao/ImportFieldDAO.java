package cn.explink.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import cn.explink.domain.ImportField;

@Component
public class ImportFieldDAO {

	private final class ImportFieldRowMapper implements RowMapper<ImportField> {
		@Override
		public ImportField mapRow(ResultSet rs, int rowNum) throws SQLException {
			ImportField importField = new ImportField();
			importField.setId(rs.getLong("id"));
			importField.setFieldname(rs.getString("fieldname"));
			importField.setFieldenglishname(rs.getString("fieldenglishname"));
			return importField;
		}
	}

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public List<ImportField> getAllImportField() {
		return jdbcTemplate.query("select * from express_set_importfield", new ImportFieldRowMapper());
	}

}

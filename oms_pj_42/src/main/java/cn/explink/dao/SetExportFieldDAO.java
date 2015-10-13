package cn.explink.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import cn.explink.domain.SetExportField;

@Component
public class SetExportFieldDAO {

	private final class SetExportFieldMapper implements RowMapper<SetExportField> {
		@Override
		public SetExportField mapRow(ResultSet rs, int rowNum) throws SQLException {
			SetExportField setExportField = new SetExportField();
			setExportField.setId(rs.getInt("id"));
			setExportField.setFieldname(rs.getString("fieldname"));
			setExportField.setExportstate(rs.getLong("exportstate"));
			setExportField.setFieldenglishname(rs.getString("fieldenglishname"));
			return setExportField;
		}
	}

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public void saveSetExportFieldById(String fieldid) {
		jdbcTemplate.update("update express_ops_setexportfield set exportstate=1 where id = '" + fieldid + "'");
	}

	public void saveGetExportStateZero() {
		jdbcTemplate.update("update express_ops_setexportfield set exportstate=0");
	}

	public List<SetExportField> getSetExportFieldByExportstate() {
		return jdbcTemplate.query("select * from express_ops_setexportfield where exportstate=1 order by id", new SetExportFieldMapper());
	}

	public List<SetExportField> getAllSetExportField() {
		return jdbcTemplate.query("select * from express_ops_setexportfield order by id", new SetExportFieldMapper());
	}

	// ------------------默认模版------------------
	public List<SetExportField> getDefaultExportField() {
		return jdbcTemplate.query("select * from express_ops_setexportfield where exportstate=1 order by id", new SetExportFieldMapper());
	}

	public void saveDefaultExportFieldById(String ids) {
		jdbcTemplate.update("update express_ops_setexportfield set exportstate=1 where id in (" + ids + ")");
	}

}

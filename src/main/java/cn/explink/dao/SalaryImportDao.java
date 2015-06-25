package cn.explink.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import cn.explink.domain.SalaryImport;
import cn.explink.util.StringUtil;

@Component
public class SalaryImportDao {
	private final class SalaryImportRowMapper implements RowMapper<SalaryImport> {
		@Override
		public SalaryImport mapRow(ResultSet rs, int rowNum) throws SQLException {
			SalaryImport simport = new SalaryImport();
			simport.setFilename(StringUtil.nullConvertToEmptyString(rs.getString("filename")));
			simport.setFiletext(StringUtil.nullConvertToEmptyString(rs.getString("filetext")));
			simport.setWhichvalue(rs.getInt("whichvalue"));
			simport.setIschecked(rs.getInt("ischecked"));
			simport.setAddordeduct(rs.getInt("addordeduct"));
			return simport;
		}
	}
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	public List<SalaryImport> getAllSalaryImports(){
		String sql = "select * from express_set_salary ";
		return jdbcTemplate.query(sql, new SalaryImportRowMapper());
	}
	
	public void updateWhichvalue(int whichvalue,String filename){
		String sql = "update express_set_salary set whichvalue=? where filename=?";
		jdbcTemplate.update(sql,whichvalue,filename);
	}
	
}

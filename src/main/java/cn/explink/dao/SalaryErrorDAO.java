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

import cn.explink.domain.SalaryError;
import cn.explink.util.StringUtil;

/**
 * @author Administrator
 *
 */
@Component
public class SalaryErrorDAO {
	@Autowired
	JdbcTemplate jdbcTemplate;

	private final class SalaryErrorRowMapper implements RowMapper<SalaryError> {

		/*
		 * (non-Javadoc)
		 *
		 * @see
		 * org.springframework.jdbc.core.RowMapper#mapRow(java.sql.ResultSet,
		 * int)
		 */
		@Override
		public SalaryError mapRow(ResultSet rs, int rowNum) throws SQLException {
			SalaryError salary = new SalaryError();
			salary.setId(rs.getLong("id"));
			salary.setRealname(StringUtil.nullConvertToEmptyString(rs.getString("realname")));
			salary.setIdcard(StringUtil.nullConvertToEmptyString(rs.getString("idcard")));
			salary.setText(StringUtil.nullConvertToEmptyString(rs.getString("text")));
			salary.setImportflag(rs.getLong("importflag"));
			return salary;
		}
	}

	public List<SalaryError> getSalaryErrorByImportflag(long importflag) {
		String sql="select * from express_set_salaryError where importflag= "+importflag;
		return this.jdbcTemplate.query(sql, new SalaryErrorRowMapper());
	}
	public int creSalaryError(String realname,String idcard,String text,long importflag) {
		String sql="insert into express_set_salaryError(realname,idcard,text,importflag) values(?,?,?,?)";
		return this.jdbcTemplate.update(sql, realname,idcard,text,importflag);
	}


}

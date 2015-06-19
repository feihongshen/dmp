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

import cn.explink.domain.Salary;
import cn.explink.util.Page;
import cn.explink.util.StringUtil;

/**
 * @author Administrator
 *
 */
@Component
public class SalaryDAO {
	private final class SalaryRowMapper implements RowMapper<Salary> {
		@Override
		public Salary mapRow(ResultSet rs, int rowNum) throws SQLException {
			Salary salary = new Salary();
			salary.setId(rs.getLong("id"));
			salary.setBranchid(rs.getLong("branchid"));
			salary.setAgejob(rs.getLong("agejob"));
			salary.setRealname(StringUtil.nullConvertToEmptyString(rs.getString("realname")));
			salary.setIdcard(StringUtil.nullConvertToEmptyString(rs.getString("idcard")));
			salary.setAccountSingle(rs.getBigDecimal("accountSingle"));
			salary.setSalarybasic(rs.getBigDecimal("salarybasic"));
			salary.setSalaryjob(rs.getBigDecimal("salaryjob"));
			salary.setSalarypush(rs.getBigDecimal("salarypush"));
			salary.setBonusfuel(rs.getBigDecimal("bonusfuel"));
			salary.setBonusfixed(rs.getBigDecimal("bonusfixed"));
			salary.setBonusphone(rs.getBigDecimal("bonusphone"));
			salary.setBonusweather(rs.getBigDecimal("bonusweather"));
			salary.setPenalizecancel(rs.getBigDecimal("penalizecancel"));
			salary.setBonusother1(rs.getBigDecimal("bonusother1"));
			salary.setBonusother2(rs.getBigDecimal("bonusother2"));
			salary.setBonusother3(rs.getBigDecimal("bonusother3"));
			salary.setBonusother4(rs.getBigDecimal("bonusother4"));
			salary.setBonusother5(rs.getBigDecimal("bonusother5"));
			salary.setBonusother6(rs.getBigDecimal("bonusother6"));
			salary.setOvertimework(rs.getBigDecimal("overtimework"));
			salary.setAttendance(rs.getBigDecimal("attendance"));
			salary.setSecurity(rs.getBigDecimal("security"));
			salary.setGongjijin(rs.getBigDecimal("gongjijin"));
			salary.setFoul(rs.getBigDecimal("foul"));
			salary.setGoods(rs.getBigDecimal("goods"));
			salary.setDorm(rs.getBigDecimal("dorm"));
			salary.setPenalizeother1(rs.getBigDecimal("penalizeother1"));
			salary.setPenalizeother2(rs.getBigDecimal("penalizeother2"));
			salary.setPenalizeother3(rs.getBigDecimal("penalizeother3"));
			salary.setPenalizeother4(rs.getBigDecimal("penalizeother4"));
			salary.setPenalizeother5(rs.getBigDecimal("penalizeother5"));
			salary.setPenalizeother6(rs.getBigDecimal("penalizeother6"));
			salary.setImprestgoods(rs.getBigDecimal("imprestgoods"));
			salary.setImprestother1(rs.getBigDecimal("imprestother1"));
			salary.setImprestother2(rs.getBigDecimal("imprestother2"));
			salary.setImprestother3(rs.getBigDecimal("imprestother3"));
			salary.setImprestother4(rs.getBigDecimal("imprestother4"));
			salary.setImprestother5(rs.getBigDecimal("imprestother5"));
			salary.setImprestother6(rs.getBigDecimal("imprestother6"));
			salary.setSalaryaccrual(rs.getBigDecimal("salaryaccrual"));
			salary.setTax(rs.getBigDecimal("tax"));
			salary.setSalary(rs.getBigDecimal("salary"));
			return salary;
		}
	}
	@Autowired
	JdbcTemplate jdbcTemplate;

	public List<Salary> getSalaryByRealnameAndIdcard(long page,String realname,String idcard)
	{
		String sql="select * from express_ops_salary_detail where 1=1 ";
		sql +=this.creConditions(realname,idcard);
		sql += "  limit " + ((page - 1) * Page.ONE_PAGE_NUMBER) + " ," + Page.ONE_PAGE_NUMBER;
		return this.jdbcTemplate.query(sql, new SalaryRowMapper());
	}
	public int getSalaryByRealnameAndIdcardCounts(String realname,String idcard)
	{
		String sql="select count(1) from express_ops_salary_detail where 1=1 ";
		sql+=this.creConditions(realname,idcard);
		return this.jdbcTemplate.queryForInt(sql);
	}

	/**
	 * @param realname
	 * @param idcard
	 * @return
	 */
	private String creConditions(String realname, String idcard) {
		String sql="";
		if((null!=realname)&&(realname.length()>0))
		{
			sql+=" and realname like '%"+realname+"%'";
		}
		if((null!=idcard)&&(idcard.length()>0))
		{
			sql+=" and idcard like '%"+idcard+"%'";

		}
		return sql;
	}
	/**
	 * @param ids
	 * @return
	 */
	public long deleteSalaryByids(String ids) {
		String sql="delete from express_ops_salary_detail where id in("+ids+")";
		return this.jdbcTemplate.update(sql);
	}
}

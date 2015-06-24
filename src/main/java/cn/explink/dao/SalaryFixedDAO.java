/**
 *
 */
package cn.explink.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import cn.explink.domain.SalaryFixed;
import cn.explink.util.Page;
import cn.explink.util.StringUtil;

/**
 * @author Administrator
 *
 */
@Component
public class SalaryFixedDAO {
	private final class SalaryFixedRowMapper implements RowMapper<SalaryFixed> {
		@Override
		public SalaryFixed mapRow(ResultSet rs, int rowNum) throws SQLException {
			SalaryFixed salary = new SalaryFixed();
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
	public int creSalaryByRealname(final SalaryFixed salary){
		return this.jdbcTemplate.update("INSERT INTO `express_ops_salaryFixed_detail` "
				+ "( `branchid`, `realname`, `idcard`, `accountSingle`, `salarybasic`, `salaryjob`, `salarypush`, `agejob`, `bonusfuel`, `bonusfixed`, `bonusphone`, `bonusweather`, `penalizecancel`, `bonusother1`, `bonusother2`, `bonusother3`, `bonusother4`, `bonusother5`, `bonusother6`, `overtimework`, `attendance`, `security`, `gongjijin`, `foul`, `goods`, `dorm`, `penalizeother1`, `penalizeother2`, `penalizeother3`, `penalizeother4`, `penalizeother5`, `penalizeother6`, `imprestgoods`, `imprestother1`, `imprestother2`, `imprestother3`, `imprestother4`, `imprestother5`, `imprestother6`, `salaryaccrual`, `tax`, `salary`)"
				+ " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);",new PreparedStatementSetter(){

			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				ps.setLong(1, salary.getBranchid());
				ps.setString(2, salary.getRealname());
				ps.setString(3, salary.getIdcard());
				ps.setBigDecimal(4,salary.getAccountSingle());
				ps.setBigDecimal(5,salary.getSalarybasic());
				ps.setBigDecimal(6,salary.getSalaryjob());
				ps.setBigDecimal(7,salary.getSalarypush());
				ps.setLong(8,salary.getAgejob());
				ps.setBigDecimal(9,salary.getBonusfuel());
				ps.setBigDecimal(10,salary.getBonusfixed());
				ps.setBigDecimal(11,salary.getBonusphone());
				ps.setBigDecimal(12,salary.getBonusweather());
				ps.setBigDecimal(13,salary.getPenalizecancel());
				ps.setBigDecimal(14,salary.getBonusother1());
				ps.setBigDecimal(15,salary.getBonusother2());
				ps.setBigDecimal(16,salary.getBonusother3());
				ps.setBigDecimal(17,salary.getBonusother4());
				ps.setBigDecimal(18,salary.getBonusother5());
				ps.setBigDecimal(19,salary.getBonusother6());
				ps.setBigDecimal(20,salary.getOvertimework());
				ps.setBigDecimal(21,salary.getAttendance());
				ps.setBigDecimal(22,salary.getSecurity());
				ps.setBigDecimal(23,salary.getGongjijin());
				ps.setBigDecimal(24,salary.getFoul());
				ps.setBigDecimal(25,salary.getGoods());
				ps.setBigDecimal(26,salary.getDorm());
				ps.setBigDecimal(27,salary.getPenalizeother1());
				ps.setBigDecimal(28,salary.getPenalizeother2());
				ps.setBigDecimal(29,salary.getPenalizeother3());
				ps.setBigDecimal(30,salary.getPenalizeother4());
				ps.setBigDecimal(31,salary.getPenalizeother5());
				ps.setBigDecimal(32,salary.getPenalizeother6());
				ps.setBigDecimal(33,salary.getImprestgoods());
				ps.setBigDecimal(34,salary.getImprestother1());
				ps.setBigDecimal(35,salary.getImprestother2());
				ps.setBigDecimal(36,salary.getImprestother3());
				ps.setBigDecimal(37,salary.getImprestother4());
				ps.setBigDecimal(38,salary.getImprestother5());
				ps.setBigDecimal(39,salary.getImprestother6());
				ps.setBigDecimal(40,salary.getSalaryaccrual());
				ps.setBigDecimal(41,salary.getTax());
				ps.setBigDecimal(42,salary.getSalary());
			}

		});
	}
	public List<SalaryFixed> getSalaryByRealnameAndIdcard(long page,String realname,String idcard)
	{
		String sql="select * from express_ops_salaryFixed_detail where 1=1 ";
		sql +=this.creConditions(realname,idcard);
		sql += "  limit " + ((page - 1) * Page.ONE_PAGE_NUMBER) + " ," + Page.ONE_PAGE_NUMBER;
		return this.jdbcTemplate.query(sql, new SalaryFixedRowMapper());
	}
	public int getSalaryByRealnameAndIdcardCounts(String realname,String idcard)
	{
		String sql="select count(1) from express_ops_salaryFixed_detail where 1=1 ";
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
		String sql="delete from express_ops_salaryFixed_detail where id in("+ids+")";
		return this.jdbcTemplate.update(sql);
	}
}

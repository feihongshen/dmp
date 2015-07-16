package cn.explink.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import cn.explink.domain.SalaryGather;
import cn.explink.util.StringUtil;

public class SalaryGatherDao {
	private final class SalaryGatherRowMapper implements RowMapper<SalaryGather> {
		@Override
		public SalaryGather mapRow(ResultSet rs, int rowNum) throws SQLException {
			SalaryGather salary = new SalaryGather();
			salary.setId(rs.getLong("id"));
			salary.setAgejob(rs.getBigDecimal("agejob"));
			salary.setRealname(StringUtil.nullConvertToEmptyString(rs.getString("realname")));
			salary.setIdcard(StringUtil.nullConvertToEmptyString(rs.getString("idcard")));
			salary.setAccountSingle(rs.getBigDecimal("accountSingle"));
			salary.setSalarybasic(rs.getBigDecimal("salarybasic"));
			salary.setSalaryjob(rs.getBigDecimal("salaryjob"));
			salary.setSalarypush(rs.getBigDecimal("salarypush"));//绩效奖金
		//	salary.setBonusfixed(rs.getBigDecimal("bonusfixed"));
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
			//salary.setTax(rs.getBigDecimal("tax"));
			//salary.setSalary(rs.getBigDecimal("salary"));
			salary.setJobpush(rs.getBigDecimal("jobpush"));
			salary.setBonusroom(rs.getBigDecimal("bonusroom"));
			salary.setBonusallday(rs.getBigDecimal("bonusallday"));
			salary.setBonusfood(rs.getBigDecimal("bonusfood"));
			salary.setBonustraffic(rs.getBigDecimal("bonustraffic"));
			salary.setCarrent(rs.getBigDecimal("carrent"));
			salary.setCarmaintain(rs.getBigDecimal("carmaintain"));
			salary.setCarfuel(rs.getBigDecimal("carfuel"));
			salary.setPenalizecancel_import(rs.getBigDecimal("penalizecancel_import"));
			salary.setFoul_import(rs.getBigDecimal("foul_import"));
			salary.setSalaradd(rs.getBigDecimal("salaradd"));//提成
			return salary;
		}
	}
}

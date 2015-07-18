package cn.explink.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import cn.explink.domain.SalaryCount;
import cn.explink.domain.SalaryGather;
import cn.explink.util.Page;
import cn.explink.util.StringUtil;
@Component
public class SalaryGatherDao {
	private final class SalaryGatherRowMapper implements RowMapper<SalaryGather> {
		@Override
		public SalaryGather mapRow(ResultSet rs, int rowNum) throws SQLException {
			SalaryGather salary = new SalaryGather();
			salary.setId(rs.getLong("id"));
			salary.setAgejob(rs.getBigDecimal("agejob"));
			salary.setBatchid(StringUtil.nullConvertToEmptyString(rs.getString("batchid")));//批次编号
			salary.setRealname(StringUtil.nullConvertToEmptyString(rs.getString("realname")));
			salary.setIdcard(StringUtil.nullConvertToEmptyString(rs.getString("idcard")));
			salary.setAccountSingle(rs.getLong("accountSingle"));
			salary.setSalarybasic(rs.getBigDecimal("salarybasic"));
			salary.setSalaryjob(rs.getBigDecimal("salaryjob"));
			salary.setPushcash(rs.getBigDecimal("pushcash"));//绩效奖金
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
			salary.setSalaryaccrual(rs.getBigDecimal("salaryaccrual"));//应发金额
			salary.setTax(rs.getBigDecimal("tax"));//个税
			salary.setSalary(rs.getBigDecimal("salary"));//实发金额
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
			salary.setSalarypush(rs.getBigDecimal("salarpush"));//提成
			salary.setBranchid(rs.getLong("branchid"));
			salary.setBranchname(branchDAO.getbranchname(rs.getLong("branchid"))==null?"":branchDAO.getbranchname(rs.getLong("branchid")).getBranchname());
			return salary;
		}
	}
	@Autowired
	private JdbcTemplate jdbcTemplate;
	@Autowired
	BranchDAO branchDAO;
	private Logger logger = LoggerFactory.getLogger(this.getClass());	

	
	public int cresalaryGather(final SalaryCount salaryCount ) {
		return this.jdbcTemplate.update("INSERT INTO `express_ops_salarygather_detail` (batchid,branchid) VALUES (?,?)",
				new PreparedStatementSetter() {
					@Override
					public void setValues(PreparedStatement ps) throws SQLException {
						ps.setString(1, salaryCount.getBatchid());
						ps.setLong(2,salaryCount.getBranchid());
					}
				});
	}

	public SalaryGather getSalaryByIdcard(String idcard) {
		try {
			String sql = "select * from express_ops_salarygather_detail where idcard=" + idcard + " limit 1;";
			return this.jdbcTemplate.queryForObject(sql, new SalaryGatherRowMapper());
		} catch (Exception e) {
			return null;

		}
	}
	public List<SalaryGather> findSalaryGathersWithQuery(long page,String batchnum,String distributionmember,String idcard,long branchid){
		List<SalaryGather> salaryGathers=new ArrayList<SalaryGather>();
		String sql="select * from express_ops_salarygather_detail where 1=1 ";
		if (branchid>0) {
			sql+=" and branchid="+branchid;
		}
		if (!"".equals(batchnum)) {
			sql+=" and batchid  like '%"+batchnum+"%' ";
		}
		if (!"".equals(distributionmember)) {
			sql+=" and realname like '%"+distributionmember+"%'";
		}
		if (!"".equals(idcard)&&!"undefined".equals(idcard)) {
			sql+=" and idcard like '%"+idcard+"%'";
		}
		if (sql.indexOf("and")>=0) {
			try {
				if (page!=-9) {
					sql += " limit " + ((page - 1) * Page.ONE_PAGE_NUMBER) + " ," + Page.ONE_PAGE_NUMBER;
				}
				salaryGathers=this.jdbcTemplate.query(sql, new SalaryGatherRowMapper());
			} catch (DataAccessException e) {
				this.logger.error("工资查询出现异常", e);
			}
		}
		return salaryGathers;
		
	}
}

package cn.explink.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import cn.explink.domain.AccountFeeType;
import cn.explink.util.Page;

@Component
public class AccountFeeTypeDAO {
	@Autowired
	private JdbcTemplate jdbcTemplate;

	private final class AccountFeeTypeRowMapper implements RowMapper<AccountFeeType> {
		@Override
		public AccountFeeType mapRow(ResultSet rs, int rowNum) throws SQLException {
			AccountFeeType accountFeeType = new AccountFeeType();
			accountFeeType.setFeetypeid(rs.getLong("feetypeid"));
			accountFeeType.setFeetypename(rs.getString("feetypename"));
			accountFeeType.setFeetype(rs.getLong("feetype"));
			accountFeeType.setCreatetime(rs.getString("createtime"));
			accountFeeType.setUserid(rs.getLong("userid"));
			accountFeeType.setEffectflag(rs.getLong("effectflag"));
			return accountFeeType;
		}
	}

	public List<AccountFeeType> getAccountFeeTypeList(long page, long feetype) {
		String sql = "select * from set_account_customfee_type ";
		sql = this.getAccountFeeTypeByPageWhereSql(sql, feetype);
		sql += " limit " + (page - 1) * Page.ONE_PAGE_NUMBER + " ," + Page.ONE_PAGE_NUMBER;
		List<AccountFeeType> list = jdbcTemplate.query(sql, new AccountFeeTypeRowMapper());
		return list;
	}

	public long getAccountFeeTypeCount(long feetype) {
		String sql = "select count(1) from set_account_customfee_type";
		sql = this.getAccountFeeTypeByPageWhereSql(sql, feetype);
		return jdbcTemplate.queryForLong(sql);
	}

	public List<AccountFeeType> getAccountFeeTypeByName(String feetypename) {
		String sql = "select * from set_account_customfee_type where feetypename=?";
		List<AccountFeeType> list = jdbcTemplate.query(sql, new AccountFeeTypeRowMapper(), feetypename);
		return list;
	}

	public void createAccountFeeType(AccountFeeType accountFeeType) {
		String sql = "insert into set_account_customfee_type(feetypename,feetype,userid,createtime) values(?,?,?,?)";
		jdbcTemplate.update(sql, accountFeeType.getFeetypename(), accountFeeType.getFeetype(), accountFeeType.getUserid(), accountFeeType.getCreatetime());
	}

	private String getAccountFeeTypeByPageWhereSql(String sql, long feetype) {
		StringBuffer sb = new StringBuffer(sql).append(" where 1=1 ");
		if (feetype > 0) {
			sb.append(" and feetype =" + feetype);
		}
		sb.append(" order by createtime desc");
		return sb.toString();
	}

	public AccountFeeType getAccountFeeTypeById(long feetypeid) {
		String sql = "select * from set_account_customfee_type where feetypeid = ?";
		return jdbcTemplate.queryForObject(sql, new AccountFeeTypeRowMapper(), feetypeid);
	}

	public void saveAccountFeeType(long feetypeid, long feetype, String feetypename) {
		String sql = "update set_account_customfee_type set feetypename=?,feetype=? where feetypeid=?";
		jdbcTemplate.update(sql, feetypename, feetype, feetypeid);

	}

	public void getDelAccountFeeType(long feetypeid) {
		String sql = "update set_account_customfee_type set effectflag=(effectflag+1)%2 where feetypeid=?";
		jdbcTemplate.update(sql, feetypeid);
	}

	public List<AccountFeeType> getAccountFeeTypeList() {
		String sql = "select * from set_account_customfee_type ";
		List<AccountFeeType> list = jdbcTemplate.query(sql, new AccountFeeTypeRowMapper());
		return list;
	}
}

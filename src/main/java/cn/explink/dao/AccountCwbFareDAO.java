package cn.explink.dao;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import cn.explink.domain.AccountCwbFare;

@Component
public class AccountCwbFareDAO {
	@Autowired
	private JdbcTemplate jdbcTemplate;

	private final class AccountCwbFareRowMapper implements RowMapper<AccountCwbFare> {
		@Override
		public AccountCwbFare mapRow(ResultSet rs, int rowNum) throws SQLException {
			AccountCwbFare accountCwbFare = new AccountCwbFare();
			accountCwbFare.setId(rs.getLong("id"));
			accountCwbFare.setAuditremark(rs.getString("auditremark"));
			accountCwbFare.setAudittime(rs.getString("audittime"));
			accountCwbFare.setAudituserid(rs.getLong("audituserid"));
			accountCwbFare.setCashfee(rs.getBigDecimal("cashfee"));
			accountCwbFare.setCashuser(rs.getString("cashuser"));
			accountCwbFare.setGirocardno(rs.getString("girocardno"));
			accountCwbFare.setGirofee(rs.getBigDecimal("girofee"));
			accountCwbFare.setGirouser(rs.getString("girouser"));
			accountCwbFare.setPayremark(rs.getString("payremark"));
			accountCwbFare.setPayuptime(rs.getString("payuptime"));
			accountCwbFare.setUserid(rs.getLong("userid"));

			return accountCwbFare;
		}
	}

	public long createAccountCwbFare(final AccountCwbFare accountCwbFare) {
		KeyHolder key = new GeneratedKeyHolder();
		this.jdbcTemplate.update(new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(java.sql.Connection con) throws SQLException {
				PreparedStatement ps = null;
				ps = con.prepareStatement("insert into account_cwb_fare(cashfee,cashuser,girocardno,girofee,girouser,payuptime," + "userid) values (?,?,?,?,?,?,?)", new String[] { "accountcwbid" });
				ps.setBigDecimal(1, accountCwbFare.getCashfee());
				ps.setString(2, accountCwbFare.getCashuser());
				ps.setString(3, accountCwbFare.getGirocardno());
				ps.setBigDecimal(4, accountCwbFare.getGirofee());
				ps.setString(5, accountCwbFare.getGirouser());
				ps.setString(6, accountCwbFare.getPayuptime());
				ps.setLong(7, accountCwbFare.getUserid());
				return ps;
			}
		}, key);
		accountCwbFare.setId(key.getKey().longValue());
		return key.getKey().longValue();
	}

	public void saveAccountCwbFareById(BigDecimal cashfee, BigDecimal girofee, long id) {
		String sql = "update account_cwb_fare set cashfee = ?,girofee = ? where id =?";
		this.jdbcTemplate.update(sql, cashfee, girofee, id);
	}

	public AccountCwbFare getAccountCwbFareLocKById(long id) {
		String sql = "select * from account_cwb_fare where id=? for update";
		try {
			return this.jdbcTemplate.queryForObject(sql, new AccountCwbFareRowMapper(), id);
		} catch (DataAccessException de) {
			return null;
		}
	}

	public List<AccountCwbFare> getAccountCwbFareListByIds(String ids) {
		String sql = "select * from account_cwb_fare where id in (" + ids + ")";
		try {
			return this.jdbcTemplate.query(sql, new AccountCwbFareRowMapper());
		} catch (DataAccessException de) {
			return null;
		}
	}
}

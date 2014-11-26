package cn.explink.dao;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import cn.explink.domain.FinancePayUpAudit;

@Component
public class FinancePayUpAuditDAO {

	private final class FinancePayUpAuditMapper implements RowMapper<FinancePayUpAudit> {
		@Override
		public FinancePayUpAudit mapRow(ResultSet rs, int rowNum) throws SQLException {
			FinancePayUpAudit financePayUpAudit = new FinancePayUpAudit();
			financePayUpAudit.setId(rs.getLong("id"));
			financePayUpAudit.setCredate(rs.getString("credate"));
			financePayUpAudit.setUserid(rs.getLong("userid"));
			financePayUpAudit.setAmount(rs.getBigDecimal("amount"));
			financePayUpAudit.setAmountpos(rs.getBigDecimal("amountpos"));
			financePayUpAudit.setPayamount(rs.getBigDecimal("payamount"));
			financePayUpAudit.setPayamountpos(rs.getBigDecimal("payamountpos"));
			financePayUpAudit.setAuditpayupid(rs.getString("payamountpos"));
			financePayUpAudit.setUpdateTime(rs.getString("updateTime"));
			return financePayUpAudit;
		}
	}

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public long insert(final String credate, final long userid, final BigDecimal amount, final BigDecimal amountpos, final BigDecimal payamount, final BigDecimal payamountpos,
			final String auditpayupid) {
		KeyHolder key = new GeneratedKeyHolder();
		jdbcTemplate.update(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(java.sql.Connection con) throws SQLException {
				PreparedStatement ps = null;
				ps = con.prepareStatement("INSERT INTO `finance_pay_up_audit` (`credate`,`userid`,`amount`,`amountpos`,`payamount`,`payamountpos`,`auditpayupid`)" + " values(?,?,?,?,? ,?,?) ",
						new String[] { "id" });
				ps.setString(1, credate);
				ps.setLong(2, userid);
				ps.setBigDecimal(3, amount);
				ps.setBigDecimal(4, amountpos);
				ps.setBigDecimal(5, payamount);
				ps.setBigDecimal(6, payamountpos);
				ps.setString(7, auditpayupid);

				return ps;
			}
		}, key);
		return key.getKey().longValue();
	}

	public List<FinancePayUpAudit> getList(String auditpayupid) {
		String sql = "select * from finance_pay_up_audit where id in(" + auditpayupid + ")";
		return jdbcTemplate.query(sql, new FinancePayUpAuditMapper());
	}

	public FinancePayUpAudit getFinancePayUpAuditByIdLock(Long id) {
		String sql = "select * from finance_pay_up_audit where id =? for update";
		return jdbcTemplate.queryForObject(sql, new FinancePayUpAuditMapper(), id);
	}

	// ==============================修改订单使用的方法 start
	// ==================================
	/**
	 * 重置审核状态 修改站点交款审核表字段
	 * 
	 * @param id
	 *            站点交款记录id
	 * @param amount
	 *            站点交款审核POS金额 减去重置审核状态订单的非POS金额 的值
	 * @param amount_pos
	 *            站点交款审核POS金额 减去重置审核状态订单的POS金额 的值
	 * @param updateTime
	 *            修改订单时间
	 */
	public void updateForChongZhiShenHe(long id, BigDecimal amount, BigDecimal amount_pos, String updateTime) {
		jdbcTemplate.update("update  finance_pay_up_audit set " + "amount=?,amountpos=?,updatetime=? where id = ? ", amount, amount_pos, updateTime, id);

	}
	// ==============================修改订单使用的方法 End
	// ==================================
}

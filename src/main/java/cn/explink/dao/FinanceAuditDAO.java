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

import cn.explink.domain.CwbOrder;
import cn.explink.domain.FinanceAudit;
import cn.explink.enumutil.FinanceAuditTypeEnum;
import cn.explink.util.Page;

@Component
public class FinanceAuditDAO {

	private final class FinanceAuditMapper implements RowMapper<FinanceAudit> {
		@Override
		public FinanceAudit mapRow(ResultSet rs, int rowNum) throws SQLException {
			FinanceAudit financeAudit = new FinanceAudit();
			financeAudit.setId(rs.getLong("id"));
			financeAudit.setCustomerid(rs.getLong("customerid"));
			financeAudit.setCustomerwarehouseid(rs.getLong("customerwarehouseid"));
			financeAudit.setCwbcount(rs.getLong("cwbcount"));
			financeAudit.setPaydatetime(rs.getString("paydatetime"));
			financeAudit.setPaytype(rs.getInt("paytype"));
			financeAudit.setShouldpayamount(rs.getBigDecimal("shouldpayamount"));
			financeAudit.setPayamount(rs.getBigDecimal("payamount"));
			financeAudit.setPaynumber(rs.getString("paynumber"));
			financeAudit.setAuditdatetime(rs.getString("auditdatetime"));
			financeAudit.setPayremark(rs.getString("payremark"));
			financeAudit.setType(rs.getInt("type"));
			financeAudit.setQiankuanamount(rs.getBigDecimal("qiankuanamount"));
			financeAudit.setPayqiankuanamount(rs.getBigDecimal("payqiankuanamount"));
			financeAudit.setPayqiankuandatetime(rs.getString("payqiankuandatetime"));
			financeAudit.setUpdateTime(rs.getString("updateTime"));
			return financeAudit;
		}
	}

	@Autowired
	private JdbcTemplate jdbcTemplate;

	/**
	 * 创建一条操作记录
	 * 
	 * @param of
	 * @return key
	 */
	public long creFinanceAudit(final FinanceAudit fa) {
		KeyHolder key = new GeneratedKeyHolder();
		jdbcTemplate.update(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(java.sql.Connection con) throws SQLException {
				PreparedStatement ps = null;
				ps = con.prepareStatement("INSERT INTO `finance_audit`(`customerid`,`customerwarehouseid`,`cwbcount`"
						+ ",`paydatetime`,`paytype`,`shouldpayamount`,`payamount`,`paynumber`,`auditdatetime`,`payremark`" + ",`type`,`qiankuanamount`,`payqiankuanamount`,`payqiankuandatetime`)"
						+ " VALUES (?,?,?,?,? ,?,?,?,?,?  ,?,?,?,? )", new String[] { "id" });
				ps.setLong(1, fa.getCustomerid());
				ps.setLong(2, fa.getCustomerwarehouseid());
				ps.setLong(3, fa.getCwbcount());
				ps.setString(4, fa.getPaydatetime());
				ps.setInt(5, fa.getPaytype());

				ps.setBigDecimal(6, fa.getShouldpayamount());
				ps.setBigDecimal(7, fa.getPayamount());
				ps.setString(8, fa.getPaynumber());
				ps.setString(9, fa.getAuditdatetime());
				ps.setString(10, fa.getPayremark());

				ps.setInt(11, fa.getType());
				ps.setBigDecimal(12, fa.getShouldpayamount().subtract(fa.getPayamount()));
				ps.setBigDecimal(13, fa.getPayqiankuanamount());
				ps.setString(14, fa.getPayqiankuandatetime());
				return ps;
			}
		}, key);
		return key.getKey().longValue();
	}

	public void insertTemp(CwbOrder co, long auditid, String auditdatetime, int type) {
		String sql = "INSERT INTO `finance_audit_temp` (`cwb`,`auditid`,`emaildateid`,`cwbordertype`,`audittime`,`type`)" + " values(?,?,?,?,?,?)";
		jdbcTemplate.update(sql, co.getCwb(), auditid, co.getEmaildateid(), co.getCwbordertypeid(), auditdatetime, type);
	}

	public long isAuditCount(String cwbs) {
		String sql = "SELECT COUNT(1) from `finance_audit_temp` where cwb in (" + cwbs + ")";
		return jdbcTemplate.queryForLong(sql);
	}

	public List<FinanceAudit> getFinanceAuditByParams(long customerid, long type, String begindate, String enddate) {
		String sql = "select * from `finance_audit`";
		if (customerid > 0 || type > 0 || begindate.length() > 0 || enddate.length() > 0) {
			StringBuffer w = new StringBuffer();
			sql += " where ";
			if (customerid > 0) {
				w.append(" and customerid =" + customerid);
			}
			if (type > 0) {
				w.append(" and type =" + type);
			}
			if (begindate.length() > 0) {
				w.append(" and auditdatetime >='" + begindate + "'");
			}
			if (enddate.length() > 0) {
				w.append(" and auditdatetime <= '" + enddate + "'");
			}
			sql += w.substring(4, w.length());
		}
		return jdbcTemplate.query(sql, new FinanceAuditMapper());
	}

	public List<FinanceAudit> getFinanceAuditByParams(long page, long customerid, long type, String begindate, String enddate) {
		String sql = "select * from `finance_audit`";
		if (customerid > 0 || type > 0 || begindate.length() > 0 || enddate.length() > 0) {
			StringBuffer w = new StringBuffer();
			sql += " where ";
			if (customerid > 0) {
				w.append(" and customerid =" + customerid);
			}
			if (type > 0) {
				w.append(" and type =" + type);
			}
			if (begindate.length() > 0) {
				w.append(" and auditdatetime >='" + begindate + "'");
			}
			if (enddate.length() > 0) {
				w.append(" and auditdatetime <= '" + enddate + "'");
			}
			sql += w.substring(4, w.length());
		}
		sql += " limit " + (page - 1) * Page.ONE_PAGE_NUMBER + " ," + Page.ONE_PAGE_NUMBER;
		return jdbcTemplate.query(sql, new FinanceAuditMapper());
	}

	public long getFinanceAuditByParamsCount(long customerid, long type, String begindate, String enddate) {
		String sql = "select count(1) from `finance_audit`";
		if (customerid > 0 || type > 0 || begindate.length() > 0 || enddate.length() > 0) {
			StringBuffer w = new StringBuffer();
			sql += " where ";
			if (customerid > 0) {
				w.append(" and customerid =" + customerid);
			}
			if (type > 0) {
				w.append(" and type =" + type);
			}
			if (begindate.length() > 0) {
				w.append(" and auditdatetime >='" + begindate + "'");
			}
			if (enddate.length() > 0) {
				w.append(" and auditdatetime <= '" + enddate + "'");
			}
			sql += w.substring(4, w.length());
		}
		return jdbcTemplate.queryForLong(sql);
	}

	public FinanceAudit getFinanceAuditById(long id) {
		String sql = "select * from `finance_audit` where id=" + id;
		return jdbcTemplate.queryForObject(sql, new FinanceAuditMapper());
	}

	public FinanceAudit getFinanceAuditByIdLock(long id) {
		String sql = "select * from `finance_audit` where id=? for update";
		return jdbcTemplate.queryForObject(sql, new FinanceAuditMapper(), id);
	}

	/**
	 * 
	 * @param id
	 * @param payqiankuandatetime
	 *            上交欠款的时间
	 * @param payqiankuanamount
	 *            上交欠款的金额 目前没有使用
	 * @param qiankuanamount
	 *            //应交欠款的金额，在本次交易中将减去已交的金额
	 * @param payamount
	 *            总的交款金额
	 * @param payremark
	 *            交款备注
	 * @param paynumber
	 *            小票号
	 * @param paytype
	 *            交款类型
	 */
	public void saveFinanceAuditById(long id, String payqiankuandatetime, BigDecimal payqiankuanamount, BigDecimal qiankuanamount, BigDecimal payamount, String payremark, String paynumber, int paytype) {
		String sql = "update `finance_audit` set payqiankuandatetime=?,payqiankuanamount=?,qiankuanamount=?,payamount=?,payremark=?,paynumber=?,paytype=?  where id=?";
		jdbcTemplate.update(sql, payqiankuandatetime, payqiankuanamount, qiankuanamount, payamount, payremark, paynumber, paytype, id);
	}

	public List<String> getFinanceAuditTempByAuditid(long auditid) {
		String sql = "select cwb from `finance_audit_temp` where auditid=" + auditid;
		return jdbcTemplate.query(sql, new CwbStringMapper());
	}

	public Long getFinanceAuditTempByCwb(String cwb, FinanceAuditTypeEnum type) {
		try {
			String sql = "select auditid from `finance_audit_temp` where cwb=? and type =? limit 0,1";
			return jdbcTemplate.queryForLong(sql, cwb, type.getValue());
		} catch (Exception e) {
			return 0L;
		}
	}

	public Long getFinanceAuditTempByCwb(String cwb) {
		try {
			String sql = "select auditid from `finance_audit_temp` where cwb=? limit 0,1";
			return jdbcTemplate.queryForLong(sql, cwb);
		} catch (Exception e) {
			return 0L;
		}
	}

	private final class CwbStringMapper implements RowMapper<String> {
		@Override
		public String mapRow(ResultSet rs, int rowNum) throws SQLException {
			String cwb = rs.getString("cwb");
			return cwb;
		}
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
	 * @param updateTime
	 *            修改订单时间
	 */
	public void updateForChongZhiShenHe(long id, BigDecimal amount, String updateTime) {
		jdbcTemplate.update("update  finance_audit set shouldpayamount=?,updatetime=? where id = ? ", amount, updateTime, id);

	}
	// ==============================修改订单使用的方法 End
	// ==================================
}

package cn.explink.dao;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import cn.explink.domain.AccountDeductRecord;
import cn.explink.util.Page;

@Component
public class AccountDeductRecordDAO {
	
	private static Logger logger = LoggerFactory.getLogger(AccountDeductRecordDAO.class);
	
	@Autowired
	private JdbcTemplate jdbcTemplate;

	private final class AccountDeductRecordRowMapper implements RowMapper<AccountDeductRecord> {
		@Override
		public AccountDeductRecord mapRow(ResultSet rs, int rowNum) throws SQLException {
			AccountDeductRecord accountDeductRecord = new AccountDeductRecord();
			accountDeductRecord.setRecordid(rs.getLong("recordid"));
			accountDeductRecord.setBranchid(rs.getLong("branchid"));
			accountDeductRecord.setRecordtype(rs.getLong("recordtype"));
			accountDeductRecord.setFee(rs.getBigDecimal("fee"));
			accountDeductRecord.setBeforefee(rs.getBigDecimal("beforefee"));
			accountDeductRecord.setAfterfee(rs.getBigDecimal("afterfee"));
			accountDeductRecord.setMemo(rs.getString("memo"));
			accountDeductRecord.setCreatetime(rs.getString("createtime"));
			accountDeductRecord.setUserid(rs.getLong("userid"));
			accountDeductRecord.setBeforedebt(rs.getBigDecimal("beforedebt"));
			accountDeductRecord.setAfterdebt(rs.getBigDecimal("afterdebt"));
			accountDeductRecord.setCwb(rs.getString("cwb"));
			try {
				if (rs.getObject("username") != null) {
					accountDeductRecord.setUsername(rs.getString("username"));
				}
			} catch (Exception e) {

			}
			return accountDeductRecord;
		}
	}

	private final class AccountDeductRecordHuiZong implements RowMapper<AccountDeductRecord> {
		@Override
		public AccountDeductRecord mapRow(ResultSet rs, int rowNum) throws SQLException {
			AccountDeductRecord accountDeductRecord = new AccountDeductRecord();
			accountDeductRecord.setRecordtype(rs.getLong("recordtype"));
			accountDeductRecord.setCreatetime(rs.getString("createtime"));
			accountDeductRecord.setFee(rs.getBigDecimal("fee"));
			accountDeductRecord.setNums(rs.getLong("nums"));
			return accountDeductRecord;
		}
	}

	private final class HuiZong implements RowMapper<AccountDeductRecord> {
		@Override
		public AccountDeductRecord mapRow(ResultSet rs, int rowNum) throws SQLException {
			AccountDeductRecord accountDeductRecord = new AccountDeductRecord();
			accountDeductRecord.setRecordtype(rs.getLong("recordtype"));
			accountDeductRecord.setFee(rs.getBigDecimal("fee"));
			accountDeductRecord.setNums(rs.getLong("nums"));
			return accountDeductRecord;
		}
	}

	public long createAccountDeductRecord(final AccountDeductRecord accountDeductRecord) {
		KeyHolder key = new GeneratedKeyHolder();
		jdbcTemplate.update(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(java.sql.Connection con) throws SQLException {
				PreparedStatement ps = null;
				ps = con.prepareStatement("insert into ops_account_deduct_record(branchid,recordtype,fee,beforefee,afterfee,memo,userid,beforedebt,afterdebt,cwb) " + "values (?,?,?,?,?,?,?,?,?,?)",
						new String[] { "recordid" });
				ps.setLong(1, accountDeductRecord.getBranchid());
				ps.setLong(2, accountDeductRecord.getRecordtype());
				ps.setBigDecimal(3, accountDeductRecord.getFee());
				ps.setBigDecimal(4, accountDeductRecord.getBeforefee());
				ps.setBigDecimal(5, accountDeductRecord.getAfterfee());
				ps.setString(6, accountDeductRecord.getMemo());
				ps.setLong(7, accountDeductRecord.getUserid());
				ps.setBigDecimal(8, accountDeductRecord.getBeforedebt());
				ps.setBigDecimal(9, accountDeductRecord.getAfterdebt());
				ps.setString(10, accountDeductRecord.getCwb());
				return ps;
			}
		}, key);
		accountDeductRecord.setRecordid(key.getKey().longValue());
		return key.getKey().longValue();
	}

	/*
	 * public List<AccountDeductRecord> getAccountDeductRecordPage(long
	 * page,long branchid,String starttime,String endtime,long recordtype){ try{
	 * String sql ="SELECT * FROM ops_account_deduct_record where branchid=? ";
	 * sql = this.getAccountDeductRecordWhere(sql,starttime,endtime,recordtype);
	 * if(page>0){ sql +=
	 * " limit "+(page-1)*Page.ONE_PAGE_NUMBER+" ,"+Page.ONE_PAGE_NUMBER; }
	 * List<AccountDeductRecord> list = jdbcTemplate.query(sql,new
	 * AccountDeductRecordRowMapper(),branchid); return list; }catch(Exception
	 * e){ e.printStackTrace(); return null; } }
	 */
	/**
	 * 返回站点交易明细，带用户姓名
	 * 
	 * @param page
	 * @param branchid
	 * @param starttime
	 * @param endtime
	 * @param recordtype
	 * @return
	 */
	public List<AccountDeductRecord> getAccountDeductRecordAndUserNamePage(long page, long branchid, String starttime, String endtime, long recordtype) {
		try {
			String sql = "SELECT  u.`username` ,a.* FROM ops_account_deduct_record  a LEFT JOIN `express_set_user` u ON a.`userid`=u.`userid`   where  a.branchid=? ";
			if (!("").equals(starttime)) {
				sql += " and a.createtime >='" + starttime + "' ";
			}
			if (!("").equals(endtime)) {
				sql += " and a.createtime <='" + endtime + "' ";
			}
			if (recordtype > 0) {
				sql += " and a.recordtype =" + recordtype + " ";
			}
			sql += " order by a.recordid desc ";
			if (page > 0) {
				sql += " limit " + (page - 1) * Page.ONE_PAGE_NUMBER + " ," + Page.ONE_PAGE_NUMBER;
			}
			List<AccountDeductRecord> list = jdbcTemplate.query(sql, new AccountDeductRecordRowMapper(), branchid);
			return list;
		} catch (Exception e) {
			logger.error("", e);
			return null;
		}
	}

	public long getAccountDeductRecordCount(long branchid, String starttime, String endtime, long recordtype) {
		try {
			String sql = "SELECT count(1) FROM ops_account_deduct_record where branchid=? ";
			sql = this.getAccountDeductRecordWhere(sql, starttime, endtime, recordtype);
			return jdbcTemplate.queryForLong(sql, branchid);
		} catch (Exception e) {
			logger.error("", e);
			return 0;
		}
	}

	private String getAccountDeductRecordWhere(String sql, String starttime, String endtime, long recordtype) {
		StringBuffer sb = new StringBuffer(sql);
		if (!("").equals(starttime)) {
			sb.append(" and createtime >='" + starttime + "' ");
		}
		if (!("").equals(endtime)) {
			sb.append(" and createtime <='" + endtime + "' ");
		}
		if (recordtype > 0) {
			sb.append(" and recordtype =" + recordtype + " ");
		}
		sb.append("order by recordid desc ");
		return sb.toString();
	}

	public AccountDeductRecord getAccountDeductRecordByIdLock(long recordid) {
		String sql = "select * from ops_account_deduct_record where recordid = ? for update";
		return jdbcTemplate.queryForObject(sql, new AccountDeductRecordRowMapper(), recordid);
	}

	public void updateRecordforFee(long recordid, BigDecimal fee) {
		jdbcTemplate.update("UPDATE ops_account_deduct_record SET fee=? where recordid=?", fee, recordid);
	}

	public List<AccountDeductRecord> getAccountDeductRecordHuiZong(long branchid, String starttime, String endtime) {
		try {
			StringBuffer sb = new StringBuffer("SELECT recordtype,createtime,SUM(fee) as fee,COUNT(1) as nums FROM ops_account_deduct_record where branchid=" + branchid);
			if (!("").equals(starttime)) {
				sb.append(" and createtime >='" + starttime + "' ");
			}
			if (!("").equals(endtime)) {
				sb.append(" and createtime <='" + endtime + "' ");
			}
			sb.append(" GROUP BY recordtype,DATE_FORMAT(createtime,'%Y-%m-%d') ");
			List<AccountDeductRecord> list = jdbcTemplate.query(sb.toString(), new AccountDeductRecordHuiZong());
			return list;
		} catch (Exception e) {
			logger.error("", e);
			return new ArrayList<AccountDeductRecord>();
		}
	}

	public List<AccountDeductRecord> getHuiZong(long branchid, String starttime, String endtime) {
		try {
			StringBuffer sb = new StringBuffer("SELECT recordtype,SUM(fee) as fee,COUNT(1) as nums FROM ops_account_deduct_record where branchid=" + branchid);
			if (!("").equals(starttime)) {
				sb.append(" and createtime >='" + starttime + "' ");
			}
			if (!("").equals(endtime)) {
				sb.append(" and createtime <='" + endtime + "' ");
			}
			sb.append(" GROUP BY recordtype");
			List<AccountDeductRecord> list = jdbcTemplate.query(sb.toString(), new HuiZong());
			return list;
		} catch (Exception e) {
			logger.error("", e);
			return new ArrayList<AccountDeductRecord>();
		}
	}

	public String sumOfamount(long branchid) {
		String sql = "SELECT SUM(fee) as fee FROM ops_account_deduct_record where recordtype='10' and branchid=?";
		String s = jdbcTemplate.queryForObject(sql, new Object[] { branchid }, String.class);
		return s == null ? "0.00" : s;

	}
}

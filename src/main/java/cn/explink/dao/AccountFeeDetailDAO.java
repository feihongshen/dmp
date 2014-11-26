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

import cn.explink.domain.AccountFeeDetail;
import cn.explink.util.Page;

@Component
public class AccountFeeDetailDAO {
	@Autowired
	private JdbcTemplate jdbcTemplate;

	private final class AccountFeeDetailRowMapper implements RowMapper<AccountFeeDetail> {
		@Override
		public AccountFeeDetail mapRow(ResultSet rs, int rowNum) throws SQLException {
			AccountFeeDetail accountFeeDetail = new AccountFeeDetail();
			accountFeeDetail.setFeedetailid(rs.getLong("feedetailid"));
			accountFeeDetail.setFeetypeid(rs.getLong("feetypeid"));
			accountFeeDetail.setSummaryid(rs.getLong("summaryid"));
			accountFeeDetail.setBranchid(rs.getLong("branchid"));
			accountFeeDetail.setDetailname(rs.getString("detailname"));
			accountFeeDetail.setCheckoutstate(rs.getLong("checkoutstate"));
			accountFeeDetail.setCustomfee(rs.getBigDecimal("customfee"));
			accountFeeDetail.setCreatetime(rs.getString("createtime"));
			accountFeeDetail.setUserid(rs.getLong("userid"));
			return accountFeeDetail;
		}
	}

	private final class AccountFeeDetailBranchRowMapper implements RowMapper<AccountFeeDetail> {
		@Override
		public AccountFeeDetail mapRow(ResultSet rs, int rowNum) throws SQLException {
			AccountFeeDetail accountFeeDetail = new AccountFeeDetail();
			accountFeeDetail.setFeedetailid(rs.getLong("feedetailid"));
			accountFeeDetail.setFeetypeid(rs.getLong("feetypeid"));
			accountFeeDetail.setSummaryid(rs.getLong("summaryid"));
			accountFeeDetail.setBranchid(rs.getLong("branchid"));
			accountFeeDetail.setDetailname(rs.getString("detailname"));
			accountFeeDetail.setCheckoutstate(rs.getLong("checkoutstate"));
			accountFeeDetail.setCustomfee(rs.getBigDecimal("customfee"));
			accountFeeDetail.setCreatetime(rs.getString("createtime"));
			accountFeeDetail.setUserid(rs.getLong("userid"));
			accountFeeDetail.setBranchname(rs.getString("branchname"));
			accountFeeDetail.setFeetypename(rs.getString("feetypename"));
			accountFeeDetail.setUsername(rs.getString("username"));
			return accountFeeDetail;
		}
	}

	private final class AccountFeeByAddSubRowMapper implements RowMapper<AccountFeeDetail> {
		@Override
		public AccountFeeDetail mapRow(ResultSet rs, int rowNum) throws SQLException {
			AccountFeeDetail accountFeeDetail = new AccountFeeDetail();
			accountFeeDetail.setFeedetailid(rs.getLong("feedetailid"));
			accountFeeDetail.setFeetypeid(rs.getLong("feetypeid"));
			accountFeeDetail.setSummaryid(rs.getLong("summaryid"));
			accountFeeDetail.setBranchid(rs.getLong("branchid"));
			accountFeeDetail.setDetailname(rs.getString("detailname"));
			accountFeeDetail.setCheckoutstate(rs.getLong("checkoutstate"));
			accountFeeDetail.setCustomfee(rs.getBigDecimal("customfee"));
			accountFeeDetail.setCreatetime(rs.getString("createtime"));
			accountFeeDetail.setUserid(rs.getLong("userid"));
			accountFeeDetail.setFeetypename(rs.getString("feetypename"));
			accountFeeDetail.setFeetype(rs.getLong("feetype"));
			return accountFeeDetail;
		}
	}

	private final class AccountFeeDetailMapper implements RowMapper<AccountFeeDetail> {
		@Override
		public AccountFeeDetail mapRow(ResultSet rs, int rowNum) throws SQLException {
			AccountFeeDetail accountFeeDetail = new AccountFeeDetail();
			accountFeeDetail.setFeedetailid(rs.getLong("feedetailid"));
			accountFeeDetail.setFeetypeid(rs.getLong("feetypeid"));
			accountFeeDetail.setSummaryid(rs.getLong("summaryid"));
			accountFeeDetail.setBranchid(rs.getLong("branchid"));
			accountFeeDetail.setDetailname(rs.getString("detailname"));
			accountFeeDetail.setCheckoutstate(rs.getLong("checkoutstate"));
			accountFeeDetail.setCustomfee(rs.getBigDecimal("customfee"));
			accountFeeDetail.setCreatetime(rs.getString("createtime"));
			accountFeeDetail.setUserid(rs.getLong("userid"));
			accountFeeDetail.setFeetypename(rs.getString("feetypename"));
			// accountFeeDetail.setFeetype(rs.getLong("feetype"));
			accountFeeDetail.setUsername(rs.getString("username"));
			return accountFeeDetail;
		}
	}

	private final class AccounntCutomFeeMapper implements RowMapper<AccountFeeDetail> {
		@Override
		public AccountFeeDetail mapRow(ResultSet rs, int rowNum) throws SQLException {
			AccountFeeDetail accountFeeDetail = new AccountFeeDetail();
			accountFeeDetail.setCustomfee(rs.getBigDecimal("customfee"));
			return accountFeeDetail;
		}
	}

	public List<AccountFeeDetail> getAccountFeeDetailList(long page, long feetypeid, String branchname, String detailname) {
		String sql = "SELECT b.branchname,c.feetypename,d.`username`,a.* FROM set_account_customfee_detail a,express_set_branch b," + "set_account_customfee_type c,express_set_user d "
				+ "WHERE a.branchid=b.branchid AND  a.feetypeid=c.feetypeid AND a.userid=d.userid ";
		sql = this.getAccountFeeDetailByPageWhereSql(sql, feetypeid, branchname, detailname);
		sql += " limit " + (page - 1) * Page.ONE_PAGE_NUMBER + " ," + Page.ONE_PAGE_NUMBER;
		List<AccountFeeDetail> list = jdbcTemplate.query(sql, new AccountFeeDetailBranchRowMapper());
		return list;
	}

	private String getAccountFeeDetailByPageWhereSql(String sql, long feetypeid, String branchname, String detailname) {
		StringBuffer sb = new StringBuffer(sql);
		if (feetypeid > 0) {
			sb.append(" and a.feetypeid=" + feetypeid);
		}
		if (!("").equals(branchname)) {
			sb.append(" and b.branchname like '%" + branchname + "%' ");
		}
		if (!("").equals(detailname)) {
			sb.append(" and c.feetypename like '%" + detailname + "%' ");
		}
		sb.append(" order by a.createtime desc");
		return sb.toString();
	}

	public long getAccountFeeDetailCount(long feetypeid, String branchname, String detailname) {
		String sql = "SELECT count(1) FROM set_account_customfee_detail a,express_set_branch b," + "set_account_customfee_type c,express_set_user d "
				+ "WHERE a.branchid=b.branchid AND  a.feetypeid=c.feetypeid AND a.userid=d.userid ";
		sql = this.getAccountFeeDetailByPageWhereSql(sql, feetypeid, branchname, detailname);
		return jdbcTemplate.queryForLong(sql);
	}

	public void createAccountFeeDetail(AccountFeeDetail o) {
		String sql = "insert into set_account_customfee_detail(feetypeid,branchid,detailname,checkoutstate,customfee,userid,createtime,summaryid) values(?,?,?,?,?,?,?,0)";
		jdbcTemplate.update(sql, o.getFeetypeid(), o.getBranchid(), o.getDetailname(), o.getCheckoutstate(), o.getCustomfee(), o.getUserid(), o.getCreatetime());
	}

	public AccountFeeDetail getAccountFeeDetailById(long feedetailid) {
		String sql = "select * from set_account_customfee_detail where feedetailid = ?";
		return jdbcTemplate.queryForObject(sql, new AccountFeeDetailRowMapper(), feedetailid);
	}

	// public void saveAccountFeeDetail(AccountFeeDetail o){
	// String sql =
	// "update set_account_customfee_detail set feetypeid=?,branchid=?,detailname=?,customfee=? where feedetailid=?";
	// jdbcTemplate.update(sql,o.getFeetypeid(),o.getBranchid(),o.getDetailname(),o.getCustomfee(),o.getFeedetailid());
	// }

	public void saveAccountFeeDetail(final AccountFeeDetail o) {
		jdbcTemplate.update("update set_account_customfee_detail set feetypeid=?,branchid=?,detailname=?,customfee=? " + "where feedetailid = ?", new PreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				ps.setLong(1, o.getFeetypeid());
				ps.setLong(2, o.getBranchid());
				ps.setString(3, o.getDetailname());
				ps.setBigDecimal(4, o.getCustomfee());
				ps.setLong(5, o.getFeedetailid());
			}
		});
	}

	// public List<AccountFeeDetail> getAccountCustomFeeList(long branchid,long
	// checkoutstate,long feetype){
	// String sql =
	// "SELECT b.*,a.feetype FROM set_account_customfee_type a,set_account_customfee_detail b "
	// +
	// "WHERE a.`feetypeid`=b.`feetypeid` AND a.`effectflag`=0 AND b.`branchid`=? AND b.`checkoutstate`=? and a.feetype=?";
	// return jdbcTemplate.query(sql,new
	// AccountFeeByAddSubRowMapper(),branchid,checkoutstate,feetype);
	// }

	public void updateAccountFeeDetailByCheckout(long summaryid, String feedetailid) {
		String sql = "update set_account_customfee_detail set checkoutstate=1,summaryid=? where feedetailid in(" + feedetailid + ")";
		jdbcTemplate.update(sql, summaryid);
	}

	public AccountFeeDetail getAccountFeeByCustomfeeSum(String feedetailid) {
		String sql = "select sum(customfee) as customfee from set_account_customfee_detail where feedetailid in(" + feedetailid + ")";
		return jdbcTemplate.queryForObject(sql, new AccounntCutomFeeMapper());
	}

	public void updateAccountFeeBySummaryid(long summaryid) {
		String sql = "update set_account_customfee_detail set checkoutstate=2,summaryid=0 where summaryid=?";
		jdbcTemplate.update(sql, summaryid);
	}

	public List<AccountFeeDetail> getAccountCustomFee(long branchid) {
		String sql = "SELECT a.`feetypename`,a.`feetype`,b.* FROM set_account_customfee_type a,set_account_customfee_detail b "
				+ "WHERE a.`feetypeid`=b.`feetypeid` AND a.`effectflag`=0 AND b.`branchid`=? and checkoutstate=2";
		return jdbcTemplate.query(sql, new AccountFeeByAddSubRowMapper(), branchid);
	}

	public List<AccountFeeDetail> getAccountFeeDetailByIds(String feedetailid, long feetype) {
		try {
			String sql = "SELECT b.`feetypename`,c.`username`,a.* FROM set_account_customfee_detail a,set_account_customfee_type b,express_set_user c "
					+ "WHERE a.feetypeid=b.feetypeid  AND a.`userid`=c.`userid` AND a.feedetailid IN (" + feedetailid + ")  AND b.`feetype`=?";
			return jdbcTemplate.query(sql, new AccountFeeDetailMapper(), feetype);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	// public long getAccountFeeDetailByIdsCount(String feedetailid,long
	// feetype){
	// String sql =
	// "SELECT b.`feetypename`,c.`username`,a.* FROM set_account_customfee_detail a,set_account_customfee_type b,express_set_user c "+
	// "WHERE a.feetypeid=b.feetypeid  AND a.`userid`=c.`userid` AND feedetailid IN ("+feedetailid+")  AND b.`feetype`=?";
	// return jdbcTemplate.queryForLong(sql,feetype);
	// }

	public List<AccountFeeDetail> getDetailBySummaryidList(long summaryid, long feetype) {
		try {
			String sql = "SELECT b.`feetypename`,c.`username`,a.* FROM set_account_customfee_detail a,set_account_customfee_type b,express_set_user c "
					+ "WHERE a.feetypeid=b.feetypeid  AND a.`userid`=c.`userid` AND a.summaryid=? AND b.`feetype`=?";
			return jdbcTemplate.query(sql, new AccountFeeDetailMapper(), summaryid, feetype);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public long getAccountFeeBySummaryidSum(String feedetailid) {
		String sql = "select sum(summaryid) as summaryid from set_account_customfee_detail where feedetailid in(" + feedetailid + ")";
		return jdbcTemplate.queryForLong(sql);
	}

	public List<AccountFeeDetail> getAccountFeeDetailLock(String feedetailid) {
		String sql = "SELECT * FROM set_account_customfee_detail WHERE feedetailid in(" + feedetailid + ") FOR UPDATE";
		return jdbcTemplate.query(sql, new AccountFeeDetailRowMapper());
	}
}

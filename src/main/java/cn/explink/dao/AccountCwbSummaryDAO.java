package cn.explink.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import cn.explink.domain.AccountCwbSummary;
import cn.explink.util.Page;

import com.pjbest.splitting.aspect.DataSource;
import com.pjbest.splitting.routing.DatabaseType;

@Component
public class AccountCwbSummaryDAO {
	@Autowired
	private JdbcTemplate jdbcTemplate;

	private final class AccountCwbSummaryRowMapper implements RowMapper<AccountCwbSummary> {
		@Override
		public AccountCwbSummary mapRow(ResultSet rs, int rowNum) throws SQLException {
			AccountCwbSummary accountCwbSummary = new AccountCwbSummary();
			accountCwbSummary.setSummaryid(rs.getLong("summaryid"));
			accountCwbSummary.setCheckoutstate(rs.getInt("checkoutstate"));
			accountCwbSummary.setAccounttype(rs.getInt("accounttype"));
			accountCwbSummary.setMemo(rs.getString("memo"));
			accountCwbSummary.setCreatetime(rs.getString("createtime"));
			accountCwbSummary.setUserid(rs.getLong("userid"));
			accountCwbSummary.setSavecreatetime(rs.getString("savecreatetime"));
			accountCwbSummary.setSaveuserid(rs.getLong("saveuserid"));
			accountCwbSummary.setOtheraddfee(rs.getBigDecimal("otheraddfee"));
			accountCwbSummary.setOthersubtractfee(rs.getBigDecimal("othersubtractfee"));
			accountCwbSummary.setZznums(rs.getLong("zznums"));
			accountCwbSummary.setZzcash(rs.getBigDecimal("zzcash"));
			accountCwbSummary.setThnums(rs.getInt("thnums"));
			accountCwbSummary.setThcash(rs.getBigDecimal("thcash"));
			accountCwbSummary.setTonums(rs.getLong("tonums"));
			accountCwbSummary.setTocash(rs.getBigDecimal("tocash"));
			accountCwbSummary.setTopos(rs.getBigDecimal("topos"));
			accountCwbSummary.setWjnums(rs.getLong("wjnums"));
			accountCwbSummary.setWjcash(rs.getBigDecimal("wjcash"));
			accountCwbSummary.setYjnums(rs.getLong("yjnums"));
			accountCwbSummary.setYjcash(rs.getBigDecimal("yjcash"));
			accountCwbSummary.setYjpos(rs.getBigDecimal("yjpos"));
			accountCwbSummary.setHjnums(rs.getLong("hjnums"));
			accountCwbSummary.setHjfee(rs.getBigDecimal("hjfee"));
			accountCwbSummary.setFeetransfer(rs.getBigDecimal("feetransfer"));
			accountCwbSummary.setFeecash(rs.getBigDecimal("feecash"));
			accountCwbSummary.setFeepos(rs.getBigDecimal("feepos"));
			accountCwbSummary.setFeecheck(rs.getBigDecimal("feecheck"));
			accountCwbSummary.setUsertransfer(rs.getString("usertransfer"));
			accountCwbSummary.setUsercash(rs.getString("usercash"));
			accountCwbSummary.setUserpos(rs.getString("userpos"));
			accountCwbSummary.setUsercheck(rs.getString("usercheck"));
			accountCwbSummary.setCardtransfer(rs.getString("cardtransfer"));
			accountCwbSummary.setBranchid(rs.getLong("branchid"));
			accountCwbSummary.setYjcheck(rs.getBigDecimal("yjcheck"));
			accountCwbSummary.setYjother(rs.getBigDecimal("yjother"));
			accountCwbSummary.setWjpos(rs.getBigDecimal("wjpos"));
			accountCwbSummary.setWjcheck(rs.getBigDecimal("wjcheck"));
			accountCwbSummary.setWjother(rs.getBigDecimal("wjother"));
			accountCwbSummary.setTocheck(rs.getBigDecimal("tocheck"));
			accountCwbSummary.setToother(rs.getBigDecimal("toother"));
			accountCwbSummary.setQknums(rs.getLong("qknums"));
			accountCwbSummary.setQkcash(rs.getBigDecimal("qkcash"));
			accountCwbSummary.setTofee(rs.getBigDecimal("tofee"));
			accountCwbSummary.setYjfee(rs.getBigDecimal("yjfee"));
			accountCwbSummary.setWjfee(rs.getBigDecimal("wjfee"));
			accountCwbSummary.setOtheraddnums(rs.getLong("otheraddnums"));
			accountCwbSummary.setOthersubnums(rs.getLong("othersubnums"));
			accountCwbSummary.setPoscash(rs.getBigDecimal("poscash"));
			accountCwbSummary.setPosnums(rs.getLong("posnums"));
			return accountCwbSummary;
		}
	}

	public long createAccountCwbSummary(final AccountCwbSummary accountCwbSummary) {
		KeyHolder key = new GeneratedKeyHolder();
		jdbcTemplate.update(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(java.sql.Connection con) throws SQLException {
				PreparedStatement ps = null;
				ps = con.prepareStatement("insert into ops_account_cwb_summary(checkoutstate,memo,createtime,userid,savecreatetime,"
						+ "saveuserid,otheraddfee,othersubtractfee,zznums,zzcash,thnums,thcash,tonums,tocash,topos,wjnums,wjcash,yjnums,yjcash,yjpos,hjnums,"
						+ "hjfee,feetransfer,feecash,feepos,feecheck,usertransfer,usercash,userpos,usercheck,cardtransfer,accounttype,branchid,"
						+ "yjcheck,yjother,wjpos,wjcheck,wjother,tocheck,toother,qknums,qkcash,tofee,yjfee,wjfee,otheraddnums,othersubnums,poscash,posnums) "
						+ "values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)", new String[] { "summaryid" });
				ps.setLong(1, accountCwbSummary.getCheckoutstate());
				ps.setString(2, accountCwbSummary.getMemo());
				ps.setString(3, accountCwbSummary.getCreatetime());
				ps.setLong(4, accountCwbSummary.getUserid());
				ps.setString(5, accountCwbSummary.getSavecreatetime());
				ps.setLong(6, accountCwbSummary.getSaveuserid());
				ps.setBigDecimal(7, accountCwbSummary.getOtheraddfee());
				ps.setBigDecimal(8, accountCwbSummary.getOthersubtractfee());
				ps.setLong(9, accountCwbSummary.getZznums());
				ps.setBigDecimal(10, accountCwbSummary.getZzcash());
				ps.setLong(11, accountCwbSummary.getThnums());
				ps.setBigDecimal(12, accountCwbSummary.getThcash());
				ps.setLong(13, accountCwbSummary.getTonums());
				ps.setBigDecimal(14, accountCwbSummary.getTocash());
				ps.setBigDecimal(15, accountCwbSummary.getTopos());
				ps.setLong(16, accountCwbSummary.getWjnums());
				ps.setBigDecimal(17, accountCwbSummary.getWjcash());
				ps.setLong(18, accountCwbSummary.getYjnums());
				ps.setBigDecimal(19, accountCwbSummary.getYjcash());
				ps.setBigDecimal(20, accountCwbSummary.getYjpos());
				ps.setLong(21, accountCwbSummary.getYjnums());
				ps.setBigDecimal(22, accountCwbSummary.getHjfee());
				ps.setBigDecimal(23, accountCwbSummary.getFeetransfer());
				ps.setBigDecimal(24, accountCwbSummary.getFeecash());
				ps.setBigDecimal(25, accountCwbSummary.getFeepos());
				ps.setBigDecimal(26, accountCwbSummary.getFeecheck());
				ps.setString(27, accountCwbSummary.getUsertransfer());
				ps.setString(28, accountCwbSummary.getUsercash());
				ps.setString(29, accountCwbSummary.getUserpos());
				ps.setString(30, accountCwbSummary.getUsercheck());
				ps.setString(31, accountCwbSummary.getCardtransfer());
				ps.setLong(32, accountCwbSummary.getAccounttype());
				ps.setLong(33, accountCwbSummary.getBranchid());
				ps.setBigDecimal(34, accountCwbSummary.getYjcheck());
				ps.setBigDecimal(35, accountCwbSummary.getYjother());
				ps.setBigDecimal(36, accountCwbSummary.getWjpos());
				ps.setBigDecimal(37, accountCwbSummary.getWjcheck());
				ps.setBigDecimal(38, accountCwbSummary.getWjother());
				ps.setBigDecimal(39, accountCwbSummary.getTocheck());
				ps.setBigDecimal(40, accountCwbSummary.getToother());
				ps.setLong(41, accountCwbSummary.getQknums());
				ps.setBigDecimal(42, accountCwbSummary.getQkcash());
				ps.setBigDecimal(43, accountCwbSummary.getTofee());
				ps.setBigDecimal(44, accountCwbSummary.getYjfee());
				ps.setBigDecimal(45, accountCwbSummary.getWjfee());
				ps.setLong(46, accountCwbSummary.getOtheraddnums());
				ps.setLong(47, accountCwbSummary.getOthersubnums());
				ps.setBigDecimal(48, accountCwbSummary.getPoscash());
				ps.setLong(49, accountCwbSummary.getPosnums());
				return ps;
			}
		}, key);
		accountCwbSummary.setSummaryid(key.getKey().longValue());
		return key.getKey().longValue();
	}

	public void saveAccountCwbSummaryByCheck(final AccountCwbSummary accountCwbSummary) {
		jdbcTemplate.update("update ops_account_cwb_summary set checkoutstate=?,memo=?,savecreatetime=?,saveuserid=?,feetransfer=?,"
				+ "feecash=?,feepos=?,feecheck=?,usertransfer=?,usercash=?,userpos=?,usercheck=?,cardtransfer=?,createtime=?,userid=? " + "where summaryid =?", new PreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				ps.setLong(1, accountCwbSummary.getCheckoutstate());
				ps.setString(2, accountCwbSummary.getMemo());
				ps.setString(3, accountCwbSummary.getSavecreatetime());
				ps.setLong(4, accountCwbSummary.getSaveuserid());
				ps.setBigDecimal(5, accountCwbSummary.getFeetransfer());
				ps.setBigDecimal(6, accountCwbSummary.getFeecash());
				ps.setBigDecimal(7, accountCwbSummary.getFeepos());
				ps.setBigDecimal(8, accountCwbSummary.getFeecheck());
				ps.setString(9, accountCwbSummary.getUsertransfer());
				ps.setString(10, accountCwbSummary.getUsercash());
				ps.setString(11, accountCwbSummary.getUserpos());
				ps.setString(12, accountCwbSummary.getUsercheck());
				ps.setString(13, accountCwbSummary.getCardtransfer());
				ps.setString(14, accountCwbSummary.getCreatetime());
				ps.setLong(15, accountCwbSummary.getUserid());
				ps.setLong(16, accountCwbSummary.getSummaryid());
			}
		});
	}

	public void deleteAccountCwbSummarById(long summaryid) {
		jdbcTemplate.update("delete from ops_account_cwb_summary where summaryid = ?", summaryid);
	}

	public AccountCwbSummary getAccountCwbSummaryById(long summaryid) {
		String sql = "select * from ops_account_cwb_summary where summaryid = ?";
		return jdbcTemplate.queryForObject(sql, new AccountCwbSummaryRowMapper(), summaryid);
	}

	public AccountCwbSummary getAccountCwbSummaryByIdLock(long summaryid) {
		String sql = "select * from ops_account_cwb_summary where summaryid = ? for update";
		return jdbcTemplate.queryForObject(sql, new AccountCwbSummaryRowMapper(), summaryid);
	}

	@DataSource(DatabaseType.REPLICA)
	public List<AccountCwbSummary> getAccountCwbSummaryList(long page, long checkoutstate, String branchids, long accounttype, String starttime, String endtime) {
		String sql = "SELECT * FROM ops_account_cwb_summary WHERE checkoutstate=? and accounttype=? ";
		sql = this.getAccountCwbSummaryPageWhereSql(sql, checkoutstate, branchids, starttime, endtime);
		if (page > 0) {
			sql += " limit " + (page - 1) * Page.ONE_PAGE_NUMBER + " ," + Page.ONE_PAGE_NUMBER;
		}
		return jdbcTemplate.query(sql, new AccountCwbSummaryRowMapper(), checkoutstate, accounttype);
	}

	@DataSource(DatabaseType.REPLICA)
	public long getAccountCwbSummaryCount(long checkoutstate, String branchids, long accounttype, String starttime, String endtime) {
		String sql = "SELECT count(1) FROM ops_account_cwb_summary WHERE checkoutstate=? and accounttype=?";
		sql = this.getAccountCwbSummaryPageWhereSql(sql, checkoutstate, branchids, starttime, endtime);
		return jdbcTemplate.queryForLong(sql, checkoutstate, accounttype);
	}

	private String getAccountCwbSummaryPageWhereSql(String sql, long checkoutstate, String branchids, String starttime, String endtime) {
		StringBuffer sb = new StringBuffer(sql);
		if (!"".equals(branchids)) {
			sb.append(" and branchid IN (" + branchids + ") ");
		}

		if (checkoutstate == 0) {// 待审核
			if (!"".equals(starttime)) {
				sb.append(" and savecreatetime >='" + starttime + " 00:00:00'");
			}
			if (!"".equals(endtime)) {
				sb.append(" and savecreatetime <='" + endtime + " 59:59:59'");
			}
			sb.append(" order by savecreatetime desc ");
		} else {// 已审核
			if (!"".equals(starttime)) {
				sb.append(" and createtime >='" + starttime + " 00:00:00'");
			}
			if (!"".equals(endtime)) {
				sb.append(" and createtime <='" + endtime + " 59:59:59'");
			}
			sb.append(" order by createtime desc ");
		}

		return sb.toString();
	}

	public long getJiesuanchukuCount(long checkoutstate, long accounttype, long branchid) {
		String sql = "SELECT count(1) FROM ops_account_cwb_summary WHERE checkoutstate=? and accounttype=? and branchid=?";
		return jdbcTemplate.queryForLong(sql, checkoutstate, accounttype, branchid);
	}

	// public void updateAccountByPos(BigDecimal poscash,long posnums,long
	// summaryid){
	// String
	// sql="update ops_account_cwb_summary set poscash=?,posnums=? where summaryid=?";
	// jdbcTemplate.update(sql,poscash,posnums,summaryid);
	// }

	public void saveAccountEditCwb(final AccountCwbSummary accountCwbSummary) {
		jdbcTemplate.update("update ops_account_cwb_summary set wjnums=?,wjcash=?,wjpos=?,wjcheck=?,wjother=?,wjfee=?,tonums=?,tocash=?,topos=?,tocheck=?,"
				+ "toother=?,tofee=?,yjnums=?,yjcash=?,Yjpos=?,Yjcheck=?,Yjother=?,Yjfee=?,poscash=?,posnums=?,hjfee=?,qkcash=?,qknums=?," + "zzcash=?,thcash=? " + "where summaryid =?",
				new PreparedStatementSetter() {
					@Override
					public void setValues(PreparedStatement ps) throws SQLException {
						ps.setLong(1, accountCwbSummary.getWjnums());
						ps.setBigDecimal(2, accountCwbSummary.getWjcash());
						ps.setBigDecimal(3, accountCwbSummary.getWjpos());
						ps.setBigDecimal(4, accountCwbSummary.getWjcheck());
						ps.setBigDecimal(5, accountCwbSummary.getWjother());
						ps.setBigDecimal(6, accountCwbSummary.getWjfee());
						ps.setLong(7, accountCwbSummary.getTonums());
						ps.setBigDecimal(8, accountCwbSummary.getTocash());
						ps.setBigDecimal(9, accountCwbSummary.getTopos());
						ps.setBigDecimal(10, accountCwbSummary.getTocheck());
						ps.setBigDecimal(11, accountCwbSummary.getToother());
						ps.setBigDecimal(12, accountCwbSummary.getTofee());
						ps.setLong(13, accountCwbSummary.getYjnums());
						ps.setBigDecimal(14, accountCwbSummary.getYjcash());
						ps.setBigDecimal(15, accountCwbSummary.getYjpos());
						ps.setBigDecimal(16, accountCwbSummary.getYjcheck());
						ps.setBigDecimal(17, accountCwbSummary.getYjother());
						ps.setBigDecimal(18, accountCwbSummary.getYjfee());
						ps.setBigDecimal(19, accountCwbSummary.getPoscash());
						ps.setLong(20, accountCwbSummary.getPosnums());
						ps.setBigDecimal(21, accountCwbSummary.getHjfee());
						ps.setBigDecimal(22, accountCwbSummary.getQkcash());
						ps.setLong(23, accountCwbSummary.getQknums());

						ps.setBigDecimal(24, accountCwbSummary.getZzcash());
						ps.setBigDecimal(25, accountCwbSummary.getThcash());
						ps.setLong(26, accountCwbSummary.getSummaryid());
					}
				});
	}
}

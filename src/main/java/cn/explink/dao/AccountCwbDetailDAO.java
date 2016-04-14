package cn.explink.dao;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import cn.explink.domain.AccountCwbDetail;
import cn.explink.util.Page;

@Component
public class AccountCwbDetailDAO {
	
	private static Logger logger = LoggerFactory.getLogger(AccountCwbDetailDAO.class);
	
	@Autowired
	private JdbcTemplate jdbcTemplate;

	private final class AccountCwbDetailRowMapper implements RowMapper<AccountCwbDetail> {
		@Override
		public AccountCwbDetail mapRow(ResultSet rs, int rowNum) throws SQLException {
			AccountCwbDetail accountCwbDetail = new AccountCwbDetail();
			accountCwbDetail.setAccountcwbid(rs.getLong("accountcwbid"));
			accountCwbDetail.setBranchid(rs.getLong("branchid"));
			accountCwbDetail.setCurrentbranchid(rs.getLong("currentbranchid"));
			accountCwbDetail.setCustomerid(rs.getLong("customerid"));
			accountCwbDetail.setFlowordertype(rs.getLong("flowordertype"));
			accountCwbDetail.setDeliverystate(rs.getInt("deliverystate"));
			accountCwbDetail.setCheckoutstate(rs.getLong("checkoutstate"));
			accountCwbDetail.setDebetstate(rs.getLong("debetstate"));
			accountCwbDetail.setCwb(rs.getString("cwb"));
			accountCwbDetail.setCwbordertypeid(rs.getLong("cwbordertypeid"));
			accountCwbDetail.setSendcarnum(rs.getLong("sendcarnum"));
			accountCwbDetail.setScannum(rs.getLong("scannum"));
			accountCwbDetail.setCaramount(rs.getBigDecimal("caramount"));
			accountCwbDetail.setReceivablefee(rs.getBigDecimal("receivablefee"));
			accountCwbDetail.setPaybackfee(rs.getBigDecimal("paybackfee"));
			accountCwbDetail.setPos(rs.getBigDecimal("pos"));
			accountCwbDetail.setCash(rs.getBigDecimal("cash"));
			accountCwbDetail.setCheckfee(rs.getBigDecimal("checkfee"));
			accountCwbDetail.setOtherfee(rs.getBigDecimal("otherfee"));
			accountCwbDetail.setCreatetime(rs.getString("createtime"));
			accountCwbDetail.setUserid(rs.getLong("userid"));
			accountCwbDetail.setAccountbranch(rs.getLong("accountbranch"));

			return accountCwbDetail;
		}
	}

	private final class ReceivablefeeMapper implements RowMapper<AccountCwbDetail> {
		@Override
		public AccountCwbDetail mapRow(ResultSet rs, int rowNum) throws SQLException {
			AccountCwbDetail accountCwbDetail = new AccountCwbDetail();
			accountCwbDetail.setReceivablefee(rs.getBigDecimal("receivablefee"));
			accountCwbDetail.setCheckoutstate(rs.getLong("checkoutstate"));
			accountCwbDetail.setDebetstate(rs.getLong("debetstate"));
			accountCwbDetail.setCash(rs.getBigDecimal("cash"));
			accountCwbDetail.setPos(rs.getBigDecimal("pos"));
			accountCwbDetail.setCheckfee(rs.getBigDecimal("checkfee"));
			accountCwbDetail.setOtherfee(rs.getBigDecimal("otherfee"));
			accountCwbDetail.setPaybackfee(rs.getBigDecimal("paybackfee"));
			accountCwbDetail.setCaramount(rs.getBigDecimal("caramount"));
			return accountCwbDetail;
		}
	}

	private final class sumOutwarehouseMapper implements RowMapper<AccountCwbDetail> {
		@Override
		public AccountCwbDetail mapRow(ResultSet rs, int rowNum) throws SQLException {
			AccountCwbDetail accountCwbDetail = new AccountCwbDetail();
			accountCwbDetail.setReceivablefee(rs.getBigDecimal("receivablefee"));
			accountCwbDetail.setCaramount(rs.getBigDecimal("caramount"));
			accountCwbDetail.setPaybackfee(rs.getBigDecimal("paybackfee"));
			accountCwbDetail.setPos(rs.getBigDecimal("pos"));
			accountCwbDetail.setCash(rs.getBigDecimal("cash"));
			accountCwbDetail.setCheckfee(rs.getBigDecimal("checkfee"));
			accountCwbDetail.setOtherfee(rs.getBigDecimal("otherfee"));
			return accountCwbDetail;
		}
	}

	private final class DeliverytimeRowMapper implements RowMapper<AccountCwbDetail> {
		@Override
		public AccountCwbDetail mapRow(ResultSet rs, int rowNum) throws SQLException {
			AccountCwbDetail accountCwbDetail = new AccountCwbDetail();
			accountCwbDetail.setAccountcwbid(rs.getLong("accountcwbid"));
			accountCwbDetail.setBranchid(rs.getLong("branchid"));
			accountCwbDetail.setCurrentbranchid(rs.getLong("currentbranchid"));
			accountCwbDetail.setCustomerid(rs.getLong("customerid"));
			accountCwbDetail.setFlowordertype(rs.getLong("flowordertype"));
			accountCwbDetail.setDeliverystate(rs.getInt("deliverystate"));
			accountCwbDetail.setCheckoutstate(rs.getLong("checkoutstate"));
			accountCwbDetail.setDebetstate(rs.getLong("debetstate"));
			accountCwbDetail.setCwb(rs.getString("cwb"));
			accountCwbDetail.setCwbordertypeid(rs.getLong("cwbordertypeid"));
			accountCwbDetail.setSendcarnum(rs.getLong("sendcarnum"));
			accountCwbDetail.setScannum(rs.getLong("scannum"));
			accountCwbDetail.setCaramount(rs.getBigDecimal("caramount"));
			accountCwbDetail.setReceivablefee(rs.getBigDecimal("receivablefee"));
			accountCwbDetail.setPaybackfee(rs.getBigDecimal("paybackfee"));
			accountCwbDetail.setPos(rs.getBigDecimal("pos"));
			accountCwbDetail.setCash(rs.getBigDecimal("cash"));
			accountCwbDetail.setCheckfee(rs.getBigDecimal("checkfee"));
			accountCwbDetail.setOtherfee(rs.getBigDecimal("otherfee"));
			accountCwbDetail.setCreatetime(rs.getString("createtime"));
			accountCwbDetail.setUserid(rs.getLong("userid"));
			accountCwbDetail.setAccountbranch(rs.getLong("accountbranch"));
			accountCwbDetail.setDeliverytime(rs.getString("deliverytime"));
			return accountCwbDetail;
		}
	}

	public long getAccountCwbDetailCount(String cwb) {
		String sql = "select count(1) from ops_account_cwb_detail where cwb=?";
		return jdbcTemplate.queryForLong(sql, cwb);
	}

	public void createAccountCwbDetail(final AccountCwbDetail accountCwbDetail) {
		KeyHolder key = new GeneratedKeyHolder();
		jdbcTemplate.update(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(java.sql.Connection con) throws SQLException {
				PreparedStatement ps = null;
				ps = con.prepareStatement("insert into ops_account_cwb_detail(branchid,customerid,flowordertype,deliverystate,checkoutstate,debetstate,"
						+ "cwb,cwbordertypeid,sendcarnum,scannum,caramount,receivablefee,paybackfee,pos,cash,checkfee,otherfee,createtime,userid," + "currentbranchid,accountbranch) "
						+ "values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)", new String[] { "accountcwbid" });
				ps.setLong(1, accountCwbDetail.getBranchid());
				ps.setLong(2, accountCwbDetail.getCustomerid());
				ps.setLong(3, accountCwbDetail.getFlowordertype());
				ps.setLong(4, accountCwbDetail.getDeliverystate());
				ps.setLong(5, accountCwbDetail.getCheckoutstate());
				ps.setLong(6, accountCwbDetail.getDebetstate());
				ps.setString(7, accountCwbDetail.getCwb());
				ps.setLong(8, accountCwbDetail.getCwbordertypeid());
				ps.setLong(9, accountCwbDetail.getSendcarnum());
				ps.setLong(10, accountCwbDetail.getScannum());
				ps.setBigDecimal(11, accountCwbDetail.getCaramount());
				ps.setBigDecimal(12, accountCwbDetail.getReceivablefee());
				ps.setBigDecimal(13, accountCwbDetail.getPaybackfee());
				ps.setBigDecimal(14, accountCwbDetail.getPos());
				ps.setBigDecimal(15, accountCwbDetail.getCash());
				ps.setBigDecimal(16, accountCwbDetail.getCheckfee());
				ps.setBigDecimal(17, accountCwbDetail.getOtherfee());
				ps.setString(18, accountCwbDetail.getCreatetime());
				ps.setLong(19, accountCwbDetail.getUserid());
				ps.setLong(20, accountCwbDetail.getCurrentbranchid());
				ps.setLong(21, accountCwbDetail.getAccountbranch());
				return ps;
			}
		}, key);
		accountCwbDetail.setAccountcwbid(key.getKey().longValue());
		// return key.getKey().longValue();
	}

	public void saveAccountCwbDetailByCwb(final AccountCwbDetail accountCwbDetail) {
		jdbcTemplate.update("update ops_account_cwb_detail set branchid = ?,customerid = ?,flowordertype = ?,"
				+ "deliverystate =?,checkoutstate = ?,debetstate = ?,cwbordertypeid = ?,sendcarnum = ?,scannum = ?,caramount = ?,"
				+ "receivablefee = ?,paybackfee = ?, pos = ?,cash = ?,checkfee = ?,otherfee = ?,createtime = ?,userid = ?,currentbranchid=? " + "where cwb = ?", new PreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				ps.setLong(1, accountCwbDetail.getBranchid());
				ps.setLong(2, accountCwbDetail.getCustomerid());
				ps.setLong(3, accountCwbDetail.getFlowordertype());
				ps.setLong(4, accountCwbDetail.getDeliverystate());
				ps.setLong(5, accountCwbDetail.getCheckoutstate());
				ps.setLong(6, accountCwbDetail.getDebetstate());
				ps.setLong(7, accountCwbDetail.getCwbordertypeid());
				ps.setLong(8, accountCwbDetail.getSendcarnum());
				ps.setLong(9, accountCwbDetail.getScannum());
				ps.setBigDecimal(10, accountCwbDetail.getCaramount());
				ps.setBigDecimal(11, accountCwbDetail.getReceivablefee());
				ps.setBigDecimal(12, accountCwbDetail.getPaybackfee());
				ps.setBigDecimal(13, accountCwbDetail.getPos());
				ps.setBigDecimal(14, accountCwbDetail.getCash());
				ps.setBigDecimal(15, accountCwbDetail.getCheckfee());
				ps.setBigDecimal(16, accountCwbDetail.getOtherfee());
				ps.setString(17, accountCwbDetail.getCreatetime());
				ps.setLong(18, accountCwbDetail.getUserid());
				ps.setLong(19, accountCwbDetail.getCurrentbranchid());
				ps.setString(20, accountCwbDetail.getCwb());
			}
		});
	}

	public List<AccountCwbDetail> getAccountCwbDetailByFlow(long branchid, long flowordertype, long checkoutstate, long debetstate) {
		String sql = "select * from ops_account_cwb_detail where branchid=? and flowordertype=? and  checkoutstate=? and debetstate=?";
		return jdbcTemplate.query(sql, new AccountCwbDetailRowMapper(), branchid, flowordertype, checkoutstate, debetstate);
	}

	public List<AccountCwbDetail> getAccountCwbByYJList(long branchid, String flowordertype) {
		try {
			String sql = "select * from ops_account_cwb_detail where branchid=? and flowordertype in (" + flowordertype
					+ ") and checkoutstate=0 and debetstate=0 and accountbranch=0 ORDER BY createtime DESC LIMIT 3000";
			return jdbcTemplate.query(sql, new AccountCwbDetailRowMapper(), branchid);
		} catch (Exception e) {
			logger.error("", e);
			return null;
		}
	}

	public List<AccountCwbDetail> getDeliveryByQKList(long branchid, String flowordertype) {
		try {
			String sql = "select a.*,b.deliverytime from ops_account_cwb_detail a,express_ops_delivery_state b WHERE a.cwb=b.cwb and a.branchid=? and a.flowordertype in (" + flowordertype
					+ ") and a.checkoutstate=0 and a.debetstate>0 AND b.deliverystate in(1,2,3) and b.state=1 ";
			return jdbcTemplate.query(sql, new DeliverytimeRowMapper(), branchid);
		} catch (Exception e) {
			logger.error("", e);
			return null;
		}
	}

	public List<AccountCwbDetail> getAccountCwbByQKList(long branchid, String flowordertype) {
		try {
			String sql = "select * from ops_account_cwb_detail where branchid=? and flowordertype in (" + flowordertype + ") and checkoutstate=0 and debetstate>0";
			return jdbcTemplate.query(sql, new AccountCwbDetailRowMapper(), branchid);
		} catch (Exception e) {
			logger.error("", e);
			return null;
		}
	}

	public List<AccountCwbDetail> getAccountCwbDetailIds(long page, String ids) {
		String sql = "SELECT * FROM ops_account_cwb_detail  where accountcwbid in(" + ids + ") ";
		sql += " limit " + (page - 1) * Page.ONE_PAGE_NUMBER + " ," + Page.ONE_PAGE_NUMBER;
		return jdbcTemplate.query(sql, new AccountCwbDetailRowMapper());
	}

	public long getAccountCwbDetailIdsCount(String ids) {
		String sql = "SELECT count(1) FROM ops_account_cwb_detail where accountcwbid in(" + ids + ")";
		return jdbcTemplate.queryForLong(sql);
	}

	public AccountCwbDetail getReceivablefeeSum(String accountcwbid) {
		String sql = "select sum(receivablefee) as receivablefee,sum(paybackfee) as paybackfee,sum(caramount) as caramount," + "sum(checkoutstate) AS checkoutstate,sum(debetstate) AS debetstate,"
				+ "SUM(cash) AS cash,SUM(pos) AS pos,SUM(checkfee) AS checkfee,SUM(otherfee) AS otherfee " + "from ops_account_cwb_detail where accountcwbid in (" + accountcwbid + ") ";
		return jdbcTemplate.queryForObject(sql, new ReceivablefeeMapper());
	}

	public List<AccountCwbDetail> getAccountCwbByDeliveryList(String accountcwbid) {
		String sql = "select * from ops_account_cwb_detail where accountcwbid in(" + accountcwbid + ") ";
		return jdbcTemplate.query(sql, new AccountCwbDetailRowMapper());
	}

	public void updateAccountCwbDetailDelete(long summaryid) {
		String sql = "UPDATE ops_account_cwb_detail SET debetstate=(CASE WHEN checkoutstate=0 THEN 0 ELSE debetstate END),"
				+ "checkoutstate=0 WHERE checkoutstate=? OR ( checkoutstate=0 AND debetstate=?)";
		jdbcTemplate.update(sql, summaryid, summaryid);
	}

	public List<AccountCwbDetail> getAccountCwbByBranch(long branchid, long summaryid) {
		String sql = "select * from ops_account_cwb_detail where branchid=? and checkoutstate=?";
		return jdbcTemplate.query(sql, new AccountCwbDetailRowMapper(), branchid, summaryid);
	}

	public void updateAccountCwbDetailByCheckout(long checkoutstate, String accountcwbid) {
		String sql = "update ops_account_cwb_detail set checkoutstate=? where accountcwbid in(" + accountcwbid + ")";
		jdbcTemplate.update(sql, checkoutstate);
	}

	public void updateAccountCwbDetailByDebetstate(long debetstate, String accountcwbid) {
		String sql = "update ops_account_cwb_detail set debetstate=? where accountcwbid in(" + accountcwbid + ")";
		jdbcTemplate.update(sql, debetstate);
	}

	public List<AccountCwbDetail> getAccountCwbDetailByYTT(long branchid, long flowordertype, String selectDS) {
		// String sql =
		// "select * from ops_account_cwb_detail where branchid=? and flowordertype=? and  checkoutstate=0 and debetstate=0 and accountbranch=0 ORDER BY createtime DESC LIMIT 3000";
		// if(!"0".equals(selectDS)){
		// sql+=" and receivablefee>0 ";
		// }
		String sql = "select a.*,b.deliverytime FROM ops_account_cwb_detail a,express_ops_delivery_state b WHERE a.cwb=b.cwb "
				+ "and a.branchid=? and a.flowordertype=? and  a.checkoutstate=0 and a.debetstate=0 and a.accountbranch=0  AND b.deliverystate in(1,2,3) and b.state=1 ORDER BY b.deliverytime asc LIMIT 3000";
		if (!"0".equals(selectDS)) {
			sql += " and a.receivablefee>0 ";
		}
		return jdbcTemplate.query(sql, new DeliverytimeRowMapper(), branchid, flowordertype);
	}

	public List<AccountCwbDetail> getDeliveryByYTTEJ(long branchid, String flowordertype) {
		String sql = "select a.*,b.deliverytime from ops_account_cwb_detail a,express_ops_delivery_state b WHERE a.cwb=b.cwb and a.branchid=? " + "and a.flowordertype in(" + flowordertype
				+ ") and  a.checkoutstate=0 and a.debetstate=0 and a.accountbranch>0 AND b.deliverystate in(1,2,3) and b.state=1 ORDER BY b.deliverytime asc LIMIT 3000";
		return jdbcTemplate.query(sql, new DeliverytimeRowMapper(), branchid);
	}

	public List<AccountCwbDetail> getAccountCwbDetailByYTTEJ(long branchid, String flowordertype) {
		String sql = "select * from ops_account_cwb_detail where branchid=? and flowordertype in(" + flowordertype
				+ ") and  checkoutstate=0 and debetstate=0 and accountbranch>0 ORDER BY createtime DESC LIMIT 3000";
		return jdbcTemplate.query(sql, new AccountCwbDetailRowMapper(), branchid);
	}

	public List<AccountCwbDetail> getAccountByExportList(long branchid, String flowordertype) {
		String sql = "select * from ops_account_cwb_detail where branchid=? and flowordertype in (" + flowordertype + ") and checkoutstate=0";
		return jdbcTemplate.query(sql, new AccountCwbDetailRowMapper(), branchid);
	}

	public String getAccountByExport(long branchid, String flowordertype) {
		String sql = "select * from ops_account_cwb_detail where branchid=" + branchid + " and flowordertype in (" + flowordertype + ") and checkoutstate=0";
		return sql;
	}

	public List<AccountCwbDetail> getAccountCwbDetailByType(long page, String flowordertype, long summaryid) {
		String sql = "select * from ops_account_cwb_detail where flowordertype in(" + flowordertype + ") and  checkoutstate=? and debetstate=0";
		if (page > 0) {
			sql += " limit " + (page - 1) * Page.ONE_PAGE_NUMBER + " ," + Page.ONE_PAGE_NUMBER;
		}
		return jdbcTemplate.query(sql, new AccountCwbDetailRowMapper(), summaryid);
	}

	public long getCountAccountCwbDetailByType(String flowordertype, long summaryid) {
		String sql = "SELECT count(1) from ops_account_cwb_detail where flowordertype in(" + flowordertype + ") and  checkoutstate=? and debetstate=0";
		return jdbcTemplate.queryForLong(sql, summaryid);
	}

	public AccountCwbDetail getDetailByTypeSum(String flowordertype, long summaryid) {
		String sql = "select sum(receivablefee) as receivablefee,sum(paybackfee) as paybackfee,sum(caramount) as caramount,sum(pos) as pos,sum(cash) as cash,sum(checkfee) as checkfee,sum(otherfee) as otherfee from ops_account_cwb_detail where flowordertype in("
				+ flowordertype + ") and  checkoutstate=? and debetstate=0";
		try {
			return jdbcTemplate.queryForObject(sql, new sumOutwarehouseMapper(), summaryid);
		} catch (DataAccessException e) {
			return new AccountCwbDetail();
		}
	}

	public List<AccountCwbDetail> getAccountCwbDetailByQK(long page, String flowordertype, long summaryid) {
		String sql = "select * from ops_account_cwb_detail where flowordertype in (" + flowordertype + ") and checkoutstate=? and debetstate>0";
		if (page > 0) {
			sql += " limit " + (page - 1) * Page.ONE_PAGE_NUMBER + " ," + Page.ONE_PAGE_NUMBER;
		}
		return jdbcTemplate.query(sql, new AccountCwbDetailRowMapper(), summaryid);
	}

	public long getCountAccountCwbDetailByQK(String flowordertype, long summaryid) {
		String sql = "select count(1) from ops_account_cwb_detail where flowordertype in (" + flowordertype + ") and checkoutstate=? and debetstate>0";
		return jdbcTemplate.queryForLong(sql, summaryid);
	}

	public AccountCwbDetail getDetailByQKSum(String flowordertype, long summaryid) {
		String sql = "select sum(receivablefee) as receivablefee,sum(paybackfee) as paybackfee,sum(caramount) as caramount,"
				+ "sum(pos) as pos,sum(cash) as cash,sum(checkfee) as checkfee,sum(otherfee) as otherfee  from ops_account_cwb_detail where flowordertype in(" + flowordertype
				+ ") and  checkoutstate=? and debetstate>0";
		try {
			return jdbcTemplate.queryForObject(sql, new sumOutwarehouseMapper(), summaryid);
		} catch (DataAccessException e) {
			return new AccountCwbDetail();
		}
	}

	public List<AccountCwbDetail> getAccountCwbDetailByWj(long page, String flowordertype, long summaryid) {
		String sql = "select * from ops_account_cwb_detail where flowordertype in (" + flowordertype + ") and debetstate=?";
		if (page > 0) {
			sql += " limit " + (page - 1) * Page.ONE_PAGE_NUMBER + " ," + Page.ONE_PAGE_NUMBER;
		}
		return jdbcTemplate.query(sql, new AccountCwbDetailRowMapper(), summaryid);
	}

	public long getCountAccountCwbDetailByWj(String flowordertype, long summaryid) {
		String sql = "select count(1) from ops_account_cwb_detail where flowordertype in (" + flowordertype + ") and debetstate=?";
		return jdbcTemplate.queryForLong(sql, summaryid);
	}

	public AccountCwbDetail getDetailByWjSum(String flowordertype, long summaryid) {
		String sql = "select sum(receivablefee) as receivablefee,sum(paybackfee) as paybackfee,sum(caramount) as caramount,sum(pos) as pos,sum(cash) as cash,sum(checkfee) as checkfee,sum(otherfee) as otherfee from ops_account_cwb_detail where flowordertype in("
				+ flowordertype + ") and debetstate=?";
		try {
			return jdbcTemplate.queryForObject(sql, new sumOutwarehouseMapper(), summaryid);
		} catch (DataAccessException e) {
			return new AccountCwbDetail();
		}
	}

	public List<AccountCwbDetail> getAccountCwbDetailByTo(long page, String flowordertype, long summaryid) {
		String sql = "select * from ops_account_cwb_detail where flowordertype in (" + flowordertype + ") " + "and ((checkoutstate=0 and debetstate=?) or (checkoutstate=? and debetstate=0))";
		if (page > 0) {
			sql += " limit " + (page - 1) * Page.ONE_PAGE_NUMBER + " ," + Page.ONE_PAGE_NUMBER;
		}
		return jdbcTemplate.query(sql, new AccountCwbDetailRowMapper(), summaryid, summaryid);
	}

	public long getCountAccountCwbDetailByTo(String flowordertype, long summaryid) {
		String sql = "select count(1) from ops_account_cwb_detail where flowordertype in (" + flowordertype + ") " + "and ((checkoutstate=0 and debetstate=?) or (checkoutstate=? and debetstate=0))";
		return jdbcTemplate.queryForLong(sql, summaryid, summaryid);
	}

	public AccountCwbDetail getDetailByToSum(String flowordertype, long summaryid) {
		String sql = "select sum(receivablefee) as receivablefee,sum(paybackfee) as paybackfee,sum(caramount) as caramount,sum(pos) as pos,sum(cash) as cash,sum(checkfee) as checkfee,sum(otherfee) as otherfee from ops_account_cwb_detail where flowordertype in("
				+ flowordertype + ") " + "and ((checkoutstate=0 and debetstate=?) or (checkoutstate=? and debetstate=0))";
		try {
			return jdbcTemplate.queryForObject(sql, new sumOutwarehouseMapper(), summaryid, summaryid);
		} catch (DataAccessException e) {
			return new AccountCwbDetail();
		}
	}

	public List<AccountCwbDetail> getAccountCwbDetailLock(String accountcwbid) {
		String sql = "SELECT * from ops_account_cwb_detail where accountcwbid in (" + accountcwbid + ") for update";
		logger.info(sql);
		return jdbcTemplate.query(sql, new AccountCwbDetailRowMapper());
	}

	public void updateAccountCwbDetailScannum(String cwb, long scannum) {
		jdbcTemplate.update("update ops_account_cwb_detail set scannum=? where cwb=?", scannum, cwb);
	}

	public void deleteAccountCwbDetailById(long accountcwbid) {
		jdbcTemplate.update("delete from ops_account_cwb_detail where accountcwbid = ?", accountcwbid);
	}

	public List<AccountCwbDetail> getEditCwbByFlowordertype(String cwb, String flowordertype) {
		try {
			String sql = "select * from ops_account_cwb_detail where cwb=? and flowordertype in (" + flowordertype + ") ";
			return jdbcTemplate.query(sql, new AccountCwbDetailRowMapper(), cwb);
		} catch (Exception e) {
			logger.error("", e);
			return null;
		}
	}

	public void updateXiuGaiJinE(long accountcwbid, BigDecimal receivablefee, BigDecimal paybackfee) {
		jdbcTemplate.update("update ops_account_cwb_detail set receivablefee=?,paybackfee=? where accountcwbid=?", receivablefee, paybackfee, accountcwbid);
	}

	public void updateXiuGaiJinEByCwb(String cwb, BigDecimal receivablefee, BigDecimal paybackfee, BigDecimal cash, BigDecimal pos, BigDecimal checkfee, BigDecimal otherfee) {
		jdbcTemplate.update("update ops_account_cwb_detail set receivablefee=?,paybackfee=?,cash=?,pos=?,checkfee=?,otherfee=? where cwb=?", receivablefee, paybackfee, cash, pos, checkfee, otherfee,
				cwb);
	}

	public void updateXiuGaiZhiFuFangShi(String cwb, BigDecimal cash, BigDecimal pos, BigDecimal checkfee, BigDecimal otherfee) {
		jdbcTemplate.update("update ops_account_cwb_detail set cash=?,pos=?,checkfee=?,otherfee=? where cwb=?", cash, pos, checkfee, otherfee, cwb);
	}

	/**
	 * 修改订单类型 修改订单表字段
	 * 
	 * @param opscwbid
	 * @param newcwbordertypeid
	 *            新的订单类型
	 * @param deliverystate
	 *            配送结果随着订单类型而变
	 */
	public void updateXiuGaiDingDanLeiXing(long accountcwbid, int newcwbordertypeid, long deliverystate, BigDecimal paybackfee) {
		jdbcTemplate.update("update ops_account_cwb_detail set cwbordertypeid=?,deliverystate=?,paybackfee=? where accountcwbid=?", newcwbordertypeid, deliverystate, paybackfee, accountcwbid);

	}
}

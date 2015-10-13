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

import cn.explink.domain.BranchPayamount;

@Component
public class BranchPayamountDAO {

	private final class BranchPayamountRowMapper implements RowMapper<BranchPayamount> {
		@Override
		public BranchPayamount mapRow(ResultSet rs, int rowNum) throws SQLException {
			BranchPayamount branchPayamount = new BranchPayamount();
			branchPayamount.setBranchpayid(rs.getLong("branchpayid"));
			branchPayamount.setBranchid(rs.getLong("branchid"));
			branchPayamount.setBranchname(rs.getString("branchname"));
			branchPayamount.setDeliverpaydate(rs.getString("deliverpaydate"));
			branchPayamount.setCwbnum(rs.getInt("cwbnum"));
			branchPayamount.setCheckdate(rs.getString("checkdate"));
			branchPayamount.setBranchpaydatetime(rs.getString("branchpaydatetime"));
			branchPayamount.setBranchpaydate(rs.getString("branchpaydate"));
			branchPayamount.setReceivablefee(rs.getBigDecimal("receivablefee"));
			branchPayamount.setReceivedfee(rs.getBigDecimal("receivedfee"));
			branchPayamount.setPaybackedfee(rs.getBigDecimal("paybackfee"));
			branchPayamount.setPayedheadfee(rs.getBigDecimal("payedheadfee"));
			branchPayamount.setPayremark(rs.getString("payremark"));
			branchPayamount.setCheckremark(rs.getString("checkremark"));
			branchPayamount.setPaywayname(rs.getString("paywayname"));
			branchPayamount.setPayproveid(rs.getString("payproveid"));
			branchPayamount.setReceivedfeecash(rs.getBigDecimal("receivedfeecash"));
			branchPayamount.setReceivedfeepos(rs.getBigDecimal("receivedfeepos"));
			branchPayamount.setReceivedfeecheque(rs.getBigDecimal("receivedfeecheque"));
			branchPayamount.setReceivedfeepos_checked(rs.getBigDecimal("receivedfeepos_checked"));
			branchPayamount.setReceivedfeecheque_checked(rs.getBigDecimal("receivedfeecheque_checked"));
			branchPayamount.setOtherbranchfee(rs.getBigDecimal("otherbranchfee"));
			branchPayamount.setOtherbranchfee_checked(rs.getBigDecimal("otherbranchfee_checked"));
			branchPayamount.setReceivedfee_halfback(rs.getBigDecimal("receivedfee_halfback"));
			branchPayamount.setReceivedfee_success(rs.getBigDecimal("receivedfee_success"));
			branchPayamount.setTotaldebtfee(rs.getBigDecimal("totaldebtfee"));
			branchPayamount.setCheckposdate(rs.getString("checkposdate"));
			branchPayamount.setCashnum(rs.getInt("cashnum"));
			branchPayamount.setPosnum(rs.getInt("posnum"));
			branchPayamount.setUserid(rs.getInt("userid"));
			branchPayamount.setOrderStr(rs.getString("orderStr"));
			branchPayamount.setUsername(rs.getString("username"));
			branchPayamount.setPayUpId(rs.getLong("payUpId"));
			branchPayamount.setPayuprealname(rs.getString("payuprealname"));
			branchPayamount.setUpbranchid(rs.getLong("upbranchid"));
			branchPayamount.setPayup_type(rs.getLong("payup_type"));
			branchPayamount.setWay(rs.getLong("way"));
			return branchPayamount;
		}
	}

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public void creBranchPayamount(final BranchPayamount branchPayamount) {

		jdbcTemplate.update("insert into express_branch_payamount(branchpayid,branchid,branchname,deliverpaydate,cwbnum,checkdate,branchpaydatetime"
				+ ",branchpaydate,receivablefee,receivedfee,paybackfee,paybackedfee,payheadfee,payedheadfee,payremark,checkremark,paywayname,payproveid,receivedfeecash"
				+ ",receivedfeepos,receivedfeecheque,receivedfeepos_checked,receivedfeecheque_checked,otherbranchfee,otherbranchfee_checked,receivedfee_halfback"
				+ ",receivedfee_success,totaldebtfee,checkposdate,cashnum,posnum,upstate,ordercount,userid,orderStr,username,payUpId,payuprealname,upbranchid,payup_type,way) "
				+ "values(?,?,?,?,?,?,?,?,?,?" + ",?,?,?,?,?,?,?,?,?,?" + ",?,?,?,?,?,?,?,?,?,?" + ",?,?,?,?,?,?,?,?,?,?" + ",?)", new PreparedStatementSetter() {

			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				// TODO Auto-generated method stub

				ps.setLong(1, branchPayamount.getBranchpayid());
				ps.setLong(2, branchPayamount.getBranchid());
				ps.setString(3, branchPayamount.getBranchname());
				ps.setString(4, branchPayamount.getDeliverpaydate());
				ps.setInt(5, branchPayamount.getCwbnum());
				ps.setString(6, branchPayamount.getCheckdate());
				ps.setString(7, branchPayamount.getBranchpaydatetime());
				ps.setString(8, branchPayamount.getBranchpaydate());
				ps.setBigDecimal(9, branchPayamount.getReceivablefee());
				ps.setBigDecimal(10, branchPayamount.getReceivedfee());
				ps.setBigDecimal(11, branchPayamount.getPaybackfee());
				ps.setBigDecimal(12, branchPayamount.getPaybackedfee());
				ps.setBigDecimal(13, branchPayamount.getPayheadfee());
				ps.setBigDecimal(14, branchPayamount.getPayedheadfee());
				ps.setString(15, branchPayamount.getPayremark());
				ps.setString(16, branchPayamount.getCheckremark());
				ps.setString(17, branchPayamount.getPaywayname());
				ps.setString(18, branchPayamount.getPayproveid());
				ps.setBigDecimal(19, branchPayamount.getReceivedfeecash());
				ps.setBigDecimal(20, branchPayamount.getReceivedfeepos());
				ps.setBigDecimal(21, branchPayamount.getReceivedfeecheque());
				ps.setBigDecimal(22, branchPayamount.getReceivedfeepos_checked());
				ps.setBigDecimal(23, branchPayamount.getReceivedfeecheque_checked());
				ps.setBigDecimal(24, branchPayamount.getOtherbranchfee());
				ps.setBigDecimal(25, branchPayamount.getOtherbranchfee_checked());
				ps.setBigDecimal(26, branchPayamount.getReceivedfee_halfback());
				ps.setBigDecimal(27, branchPayamount.getReceivedfee_success());
				ps.setBigDecimal(28, branchPayamount.getTotaldebtfee());
				ps.setString(29, branchPayamount.getCheckposdate());
				ps.setInt(30, branchPayamount.getCashnum());
				ps.setInt(31, branchPayamount.getPosnum());
				ps.setInt(32, branchPayamount.getUpstate());
				ps.setInt(33, branchPayamount.getOrdercount());
				ps.setInt(34, branchPayamount.getUserid());
				ps.setString(35, branchPayamount.getOrderStr());
				ps.setString(36, branchPayamount.getUsername());
				ps.setLong(37, branchPayamount.getPayUpId());
				ps.setString(38, branchPayamount.getPayuprealname());
				ps.setLong(39, branchPayamount.getUpbranchid());
				ps.setLong(40, branchPayamount.getPayup_type());
				ps.setLong(41, branchPayamount.getWay());

			}
		});

	}

	public long save(final BranchPayamount branchPayamount) {

		return jdbcTemplate.update("update express_branch_payamount  set "
				+ "receivedfeecash=?,otherbranchfee_checked=?,receivedfeepos_checked=?,receivedfeecheque_checked=?,checkdate=?,upstate=?,userid=?,username=?,checkremark=?  where branchpayid=?",
				new PreparedStatementSetter() {
					@Override
					public void setValues(PreparedStatement ps) throws SQLException {
						// TODO Auto-generated method stub
						ps.setBigDecimal(1, branchPayamount.getReceivedfeecash());
						ps.setBigDecimal(2, branchPayamount.getOtherbranchfee_checked());
						ps.setBigDecimal(3, branchPayamount.getReceivedfeepos_checked());
						ps.setBigDecimal(4, branchPayamount.getReceivedfeecheque_checked());
						ps.setString(5, branchPayamount.getCheckdate());
						ps.setInt(6, branchPayamount.getUpstate());
						ps.setInt(7, branchPayamount.getUserid());
						ps.setString(8, branchPayamount.getUsername());
						ps.setString(9, branchPayamount.getCheckremark());
						ps.setLong(10, branchPayamount.getBranchpayid());
					}
				});

	}

	public List<BranchPayamount> getBranchPayamount(long brand, String month) {
		String sql = "SELECT * FROM express_branch_payamount where branchpaydatetime >='" + month + " 00:00:00' AND branchpaydatetime <='" + month + " 23:59:59' AND branchid = " + brand
				+ " AND upstate <> 1";
		return jdbcTemplate.query(sql, new BranchPayamountRowMapper());
	}

	public List<BranchPayamount> getBranchPayamountBack(long brand, String month) {
		String sql = "SELECT * FROM express_branch_payamount where branchpaydatetime >='" + month + " 00:00:00' AND branchpaydatetime <='" + month + " 23:59:59' AND branchid = " + brand
				+ " AND upstate = 1";
		return jdbcTemplate.query(sql, new BranchPayamountRowMapper());
	}

	public long getBranchPayamountCount(long payUpId) {
		String sql = "SELECT count(1) FROM express_branch_payamount where payUpId=" + payUpId;
		return jdbcTemplate.queryForLong(sql);
	}

	private String getWhereSql(String sql, long branchid, String strateBranchpaydatetime, String endBranchpaydatetime, String strateDeliverpaydate, String endDeliverpaydate, int upstate,
			long upbranchid) {

		if (branchid > 0 || strateBranchpaydatetime.length() > 0 || endBranchpaydatetime.length() > 0 || strateDeliverpaydate.length() > 0 || endDeliverpaydate.length() > 0 || upstate > -1
				|| upbranchid > 0) {
			StringBuffer w = new StringBuffer();
			if (upstate == 1) {
				w.append(" upstate =1 ");
				if (strateBranchpaydatetime.length() > 0) {
					w.append(" AND checkdate >='" + strateBranchpaydatetime + "' ");

				}
				if (endBranchpaydatetime.length() > 0) {
					w.append(" AND checkdate <='" + endBranchpaydatetime + "' ");
				}
				if (strateDeliverpaydate.length() > 0) {
					w.append(" AND checkdate>='" + strateDeliverpaydate + "' ");

				}
				if (endDeliverpaydate.length() > 0) {
					w.append(" AND checkdate<='" + endDeliverpaydate + "' ");
				}
				if (upbranchid > 0) {
					w.append(" AND upbranchid=" + upbranchid + " ");
				}
			} else {
				w.append(" upstate <> 1 ");
				if (strateBranchpaydatetime.length() > 0) {
					w.append(" AND branchpaydatetime >='" + strateBranchpaydatetime + "' ");

				}
				if (endBranchpaydatetime.length() > 0) {
					w.append(" AND branchpaydatetime <='" + endBranchpaydatetime + "' ");
				}
				if (strateDeliverpaydate.length() > 0) {
					w.append(" AND deliverpaydate>='" + strateDeliverpaydate + "' ");

				}
				if (endDeliverpaydate.length() > 0) {
					w.append(" AND deliverpaydate<='" + endDeliverpaydate + "' ");
				}
				if (upbranchid > 0) {
					w.append(" AND upbranchid=" + upbranchid + " ");
				}
			}
			if (branchid > 0) {
				w.append(" AND branchid=" + branchid);
			}
			sql += w;
		}
		return sql;
	}

	public List<BranchPayamount> getAllNochack(long branchid, String strateBranchpaydatetime, String endBranchpaydatetime, String strateDeliverpaydate, String endDeliverpaydate, int upstate,
			long upbranchid) {
		String sql = "SELECT branchpayid,branchid,branchname," + "SUM(cwbnum) AS cwbnum," + "SUM(receivedfee) AS receivedfee ," + "SUM(cwbnum) AS cwbcount," + "SUM(receivablefee) AS receivablefee,"
				+ "SUM(receivedfee) AS receivedfee," + "SUM(paybackfee) AS paybackfee," + "SUM(paybackedfee) AS paybackedfee," + "SUM(payheadfee) AS payheadfee,"
				+ "SUM(payedheadfee) AS payedheadfee," + "SUM(payedheadfee) AS payedheadfee," + "SUM(receivedfeecash) AS receivedfeecash," + "SUM(receivedfeepos) AS receivedfeepos,"
				+ "SUM(receivedfeecheque) AS receivedfeecheque," + "SUM(receivedfeepos_checked) AS receivedfeepos_checked," + "SUM(receivedfeecheque_checked) AS receivedfeecheque_checked,"
				+ "SUM(otherbranchfee) AS otherbranchfee," + "SUM(otherbranchfee_checked) AS otherbranchfee_checked," + "SUM(receivedfee_halfback) AS receivedfee_halfback,"
				+ "SUM(receivedfee_success) AS receivedfee_success," + "SUM(totaldebtfee) AS totaldebtfee," + "SUM(cashnum) AS cashnum," + "SUM(posnum) AS posnum,"
				+ "branchpaydatetime,deliverpaydate,payremark," + "checkremark," + "paywayname, " + "cashnum, posnum, upstate,  ordercount, userid,checkdate,branchpaydate,payproveid,checkposdate,"
				+ "orderStr,username,payUpId,payuprealname,upbranchid, payup_type,way  " + " FROM express_branch_payamount WHERE ";
		sql = this.getWhereSql(sql, branchid, strateBranchpaydatetime, endBranchpaydatetime, strateDeliverpaydate, endDeliverpaydate, upstate, upbranchid);
		sql += "  GROUP BY DAY(branchpaydatetime),branchid ORDER BY branchid";
		return jdbcTemplate.query(sql, new BranchPayamountRowMapper());
	}

}

package cn.explink.dao;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import cn.explink.domain.AccountCwbFareDetail;
import cn.explink.enumutil.CwbOrderTypeIdEnum;
import cn.explink.util.StreamingStatementCreator;

@Component
public class AccountCwbFareDetailDAO {
	@Autowired
	private JdbcTemplate jdbcTemplate;

	private final class AccountCwbFareDetailRowMapper implements RowMapper<AccountCwbFareDetail> {
		@Override
		public AccountCwbFareDetail mapRow(ResultSet rs, int rowNum) throws SQLException {
			AccountCwbFareDetail accountCwbFareDetail = new AccountCwbFareDetail();
			accountCwbFareDetail.setId(rs.getLong("id"));
			accountCwbFareDetail.setAudittime(rs.getString("audittime"));
			accountCwbFareDetail.setCustomerid(rs.getLong("customerid"));
			accountCwbFareDetail.setCwb(rs.getString("cwb"));
			accountCwbFareDetail.setCwbordertypeid(rs.getLong("cwbordertypeid"));
			accountCwbFareDetail.setDeliverybranchid(rs.getLong("deliverybranchid"));
			accountCwbFareDetail.setDeliverystate(rs.getLong("deliverystate"));
			accountCwbFareDetail.setFareid(rs.getLong("fareid"));
			accountCwbFareDetail.setInfactfare(rs.getBigDecimal("infactfare"));
			accountCwbFareDetail.setPayuptime(rs.getString("payuptime"));
			accountCwbFareDetail.setShouldfare(rs.getBigDecimal("shouldfare"));

			accountCwbFareDetail.setVerifyflag(rs.getInt("verifyflag"));
			accountCwbFareDetail.setVerifytime(rs.getString("verifytime"));

			accountCwbFareDetail.setUserid(rs.getInt("userid"));

			return accountCwbFareDetail;
		}
	}

	private final class AccountCwbFareDetailMOneyMapper implements RowMapper<AccountCwbFareDetail> {
		@Override
		public AccountCwbFareDetail mapRow(ResultSet rs, int rowNum) throws SQLException {
			AccountCwbFareDetail accountCwbFareDetail = new AccountCwbFareDetail();
			accountCwbFareDetail.setShouldfare(rs.getBigDecimal("shouldfare") == null ? BigDecimal.ZERO : rs.getBigDecimal("shouldfare"));
			accountCwbFareDetail.setInfactfare(rs.getBigDecimal("infactfare") == null ? BigDecimal.ZERO : rs.getBigDecimal("infactfare"));
			return accountCwbFareDetail;
		}
	}

	public void createAccountCwbFareDetail(final AccountCwbFareDetail accountCwbFareDetail) {
		KeyHolder key = new GeneratedKeyHolder();
		this.jdbcTemplate.update(new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(java.sql.Connection con) throws SQLException {
				PreparedStatement ps = null;
				ps = con.prepareStatement("insert into account_cwb_fare_detail(cwb,customerid,cwbordertypeid,deliverybranchid,audittime,"
						+ "deliverystate,shouldfare,infactfare,userid) values (?,?,?,?,?,?,?,?,?)", new String[] { "accountcwbid" });
				ps.setString(1, accountCwbFareDetail.getCwb());
				ps.setLong(2, accountCwbFareDetail.getCustomerid());
				ps.setLong(3, accountCwbFareDetail.getCwbordertypeid());
				ps.setLong(4, accountCwbFareDetail.getDeliverybranchid());
				ps.setString(5, accountCwbFareDetail.getAudittime());
				ps.setLong(6, accountCwbFareDetail.getDeliverystate());
				ps.setBigDecimal(7, accountCwbFareDetail.getShouldfare());
				ps.setBigDecimal(8, accountCwbFareDetail.getInfactfare());
				ps.setLong(9, accountCwbFareDetail.getUserid());
				return ps;
			}
		}, key);
		accountCwbFareDetail.setId(key.getKey().longValue());
		// return key.getKey().longValue();
	}

	public void saveAccountCwbFareDetailByCwb(final AccountCwbFareDetail accountCwbFareDetail) {
		this.jdbcTemplate.update("update account_cwb_fare_detail set customerid=?,cwbordertypeid=?,deliverybranchid=?," + "audittime=?,deliverystate=?,shouldfare=?,infactfare=? where cwb=?",
				new PreparedStatementSetter() {
					@Override
					public void setValues(PreparedStatement ps) throws SQLException {
						ps.setLong(1, accountCwbFareDetail.getCustomerid());
						ps.setLong(2, accountCwbFareDetail.getCwbordertypeid());
						ps.setLong(3, accountCwbFareDetail.getDeliverybranchid());
						ps.setString(4, accountCwbFareDetail.getAudittime());
						ps.setLong(5, accountCwbFareDetail.getDeliverystate());
						ps.setBigDecimal(6, accountCwbFareDetail.getShouldfare());
						ps.setBigDecimal(7, accountCwbFareDetail.getInfactfare());
						ps.setString(8, accountCwbFareDetail.getCwb());
					}
				});
	}

	public void saveAccountCwbFareDetailByCwb(String payuptime, long fareid, String cwbs) {
		String sql = "update account_cwb_fare_detail set payuptime = ?,fareid = ? where cwb in(" + cwbs + ")";
		this.jdbcTemplate.update(sql, payuptime, fareid);
	}

	public List<AccountCwbFareDetail> getAccountCwbFareDetailByQK(long page, String customerids, long ispay, long isaudittime, String begindate, String enddate, long deliverybranchid,
			long deliverystate, long shoulefarefeesign, long pageNumber) {
		String sql = "select * from account_cwb_fare_detail where cwbordertypeid=" + CwbOrderTypeIdEnum.Shangmentui.getValue();
		sql = this.getAccountCwbFareDetailByQKSql(sql, customerids, ispay, isaudittime, begindate, enddate, deliverybranchid, deliverystate, shoulefarefeesign);

		sql += " limit " + ((page - 1) * pageNumber) + " ," + pageNumber;
		return this.jdbcTemplate.query(sql, new AccountCwbFareDetailRowMapper());
	}

	/**
	 * 查询已交款的数据
	 *
	 * @param page
	 * @param customerids
	 * @param ispay
	 * @param verifytime
	 * @param begindate
	 * @param enddate
	 * @param deliverybranchid
	 * @param deliverystate
	 * @param shoulefarefeesign
	 * @param pageNumber
	 * @return
	 */
	public List<AccountCwbFareDetail> getAccountCwbFareDetailByQKVerify(long page, String customerids, int verifyflag, long verifytime, String begindate, String enddate, long deliverybranchid,
			long deliverystate, long shoulefarefeesign, long pageNumber, long userid) {
		String sql = "select * from account_cwb_fare_detail where cwbordertypeid=" + CwbOrderTypeIdEnum.Shangmentui.getValue();
		sql = this.getAccountCwbFareDetailByQKVerifySql(sql, customerids, verifyflag, verifytime, begindate, enddate, deliverybranchid, deliverystate, shoulefarefeesign);
		if (userid > 0) {
			sql += " and userid=" + userid;
		}
		sql += " limit " + ((page - 1) * pageNumber) + " ," + pageNumber;
		System.out.println(sql);
		return this.jdbcTemplate.query(sql, new AccountCwbFareDetailRowMapper());
	}

	public List<AccountCwbFareDetail> getExportAccountCwbFareDetailByQK(String customerids, long ispay, long isaudittime, String begindate, String enddate, long deliverybranchid, long deliverystate,
			long shoulefarefeesign) {
		String sql = "select * from account_cwb_fare_detail where cwbordertypeid=" + CwbOrderTypeIdEnum.Shangmentui.getValue();
		sql = this.getAccountCwbFareDetailByQKSql(sql, customerids, ispay, isaudittime, begindate, enddate, deliverybranchid, deliverystate, shoulefarefeesign);

		return this.jdbcTemplate.query(sql, new AccountCwbFareDetailRowMapper());
	}

	// 财务审核
	public List<AccountCwbFareDetail> getExportAccountCwbFareDetailByQKVerify(String customerids, int verifyflag, long verifytime, String begindate, String enddate, long deliverybranchid,
			long deliverystate, long shoulefarefeesign, long userid) {
		String sql = "select * from account_cwb_fare_detail where cwbordertypeid=" + CwbOrderTypeIdEnum.Shangmentui.getValue();
		sql = this.getAccountCwbFareDetailByQKVerifySql(sql, customerids, verifyflag, verifytime, begindate, enddate, deliverybranchid, deliverystate, shoulefarefeesign);
		if (userid > 0) {
			sql += " and userid=" + userid;
		}
		return this.jdbcTemplate.query(sql, new AccountCwbFareDetailRowMapper());
	}

	public long getAccountCwbFareDetailCountByQK(String customerids, long ispay, long isaudittime, String begindate, String enddate, long deliverybranchid, long deliverystate, long shoulefarefeesign) {
		String sql = "select count(1) from account_cwb_fare_detail where cwbordertypeid=" + CwbOrderTypeIdEnum.Shangmentui.getValue();
		sql = this.getAccountCwbFareDetailByQKSql(sql, customerids, ispay, isaudittime, begindate, enddate, deliverybranchid, deliverystate, shoulefarefeesign);

		try {
			return this.jdbcTemplate.queryForInt(sql);
		} catch (DataAccessException e) {
			return 0;
		}
	}

	/**
	 * 财务审核查询
	 *
	 * @param customerids
	 * @param verifyflag
	 * @param verifytime
	 * @param begindate
	 * @param enddate
	 * @param deliverybranchid
	 * @param deliverystate
	 * @param shoulefarefeesign
	 * @return
	 */
	public long getAccountCwbFareDetailCountByQKVerify(String customerids, int verifyflag, long verifytime, String begindate, String enddate, long deliverybranchid, long deliverystate,
			long shoulefarefeesign) {
		String sql = "select count(1) from account_cwb_fare_detail where cwbordertypeid=" + CwbOrderTypeIdEnum.Shangmentui.getValue();
		sql = this.getAccountCwbFareDetailByQKVerifySql(sql, customerids, verifyflag, verifytime, begindate, enddate, deliverybranchid, deliverystate, shoulefarefeesign);

		try {
			return this.jdbcTemplate.queryForInt(sql);
		} catch (DataAccessException e) {
			return 0;
		}
	}

	public AccountCwbFareDetail getAccountCwbFareDetailSumByQK(String customerids, long ispay, long isaudittime, String begindate, String enddate, long deliverybranchid, long deliverystate,
			long shoulefarefeesign) {
		String sql = "select sum(shouldfare) as shouldfare,sum(infactfare) as infactfare from account_cwb_fare_detail where cwbordertypeid=" + CwbOrderTypeIdEnum.Shangmentui.getValue();
		sql = this.getAccountCwbFareDetailByQKSql(sql, customerids, ispay, isaudittime, begindate, enddate, deliverybranchid, deliverystate, shoulefarefeesign);
		try {
			return this.jdbcTemplate.queryForObject(sql, new AccountCwbFareDetailMOneyMapper());
		} catch (DataAccessException e) {
			return new AccountCwbFareDetail();
		}
	}

	public AccountCwbFareDetail getAccountCwbFareDetailSumByQKVerify(String customerids, int verifyflag, long verifytime, String begindate, String enddate, long deliverybranchid, long deliverystate,
			long shoulefarefeesign, long userid) {
		String sql = "select sum(shouldfare) as shouldfare,sum(infactfare) as infactfare from account_cwb_fare_detail where cwbordertypeid=" + CwbOrderTypeIdEnum.Shangmentui.getValue();
		sql = this.getAccountCwbFareDetailByQKVerifySql(sql, customerids, verifyflag, verifytime, begindate, enddate, deliverybranchid, deliverystate, shoulefarefeesign);
		if (userid > 0) {
			sql += " and userid=" + userid;
		}
		try {
			return this.jdbcTemplate.queryForObject(sql, new AccountCwbFareDetailMOneyMapper());
		} catch (DataAccessException e) {
			return new AccountCwbFareDetail();
		}
	}

	public String getAccountCwbFareDetailByQKSql(String sql, String customerids, long ispay, long isaudittime, String begindate, String enddate, long deliverybranchid, long deliverystate,
			long shoulefarefeesign) {
		if (customerids.length() > 0) {
			sql += " and customerid in(" + customerids + ")";
		}
		if (ispay > 0) {
			sql += " and fareid>0";
			sql += " and payuptime >= '" + begindate + "' ";
			sql += " and payuptime <= '" + enddate + "' ";
		} else {
			sql += " and fareid=0";
			sql += " and audittime >= '" + begindate + "' ";
			sql += " and audittime <= '" + enddate + "' ";
		}
		if (deliverybranchid > 0) {
			sql += " and deliverybranchid=" + deliverybranchid;
		}
		if (deliverystate > 0) {
			sql += " and deliverystate=" + deliverystate;
		}
		if (shoulefarefeesign > 0) {
			sql += " and shouldfare>0";
		} else {
			sql += " and shouldfare=0";
		}
		return sql;
	}

	// 财务审核
	public String getAccountCwbFareDetailByQKVerifySql(String sql, String customerids, int verifyflag, long verifytime, String begindate, String enddate, long deliverybranchid, long deliverystate,
			long shoulefarefeesign) {
		if (customerids.length() > 0) {
			sql += " and customerid in(" + customerids + ")";
		}

		sql += " and verifyflag=" + verifyflag;

		if (verifyflag == 0) { // 未审核 是交款时间
			sql += " and payuptime >= '" + begindate + "' ";
			sql += " and payuptime <= '" + enddate + "' ";
		} else { // 已审核是审核时间

			sql += " and verifytime >= '" + begindate + "' ";
			sql += " and verifytime <= '" + enddate + "' ";
		}
		if (deliverybranchid > 0) {
			sql += " and deliverybranchid=" + deliverybranchid;
		}
		if (deliverystate > 0) {
			sql += " and deliverystate=" + deliverystate;
		}
		if (shoulefarefeesign > 0) {
			sql += " and shouldfare>0";
		} else {
			sql += " and shouldfare=0";
		}
		return sql;
	}

	public List<AccountCwbFareDetail> getAccountCwbFareDetailByCwb(String cwb) {
		String sql = "select * from account_cwb_fare_detail where cwb=? order by id desc";
		return this.jdbcTemplate.query(sql, new AccountCwbFareDetailRowMapper(), cwb);
	}

	public Map<String, AccountCwbFareDetail> getAccountCwbFareDetailMapByCwbs(String cwbs) {
		String sql = "SELECT * from account_cwb_fare_detail where cwb in(" + cwbs + ") ";
		final Map<String, AccountCwbFareDetail> cwbList = new HashMap<String, AccountCwbFareDetail>();
		this.jdbcTemplate.query(new StreamingStatementCreator(sql), new RowCallbackHandler() {
			@Override
			public void processRow(ResultSet rs) throws SQLException {
				AccountCwbFareDetail accountCwbFareDetail = new AccountCwbFareDetail();
				accountCwbFareDetail.setAudittime(rs.getString("audittime"));
				accountCwbFareDetail.setCustomerid(rs.getLong("customerid"));
				accountCwbFareDetail.setCwb(rs.getString("cwb"));
				accountCwbFareDetail.setCwbordertypeid(rs.getLong("cwbordertypeid"));
				accountCwbFareDetail.setDeliverybranchid(rs.getLong("deliverybranchid"));
				accountCwbFareDetail.setDeliverystate(rs.getLong("deliverystate"));
				accountCwbFareDetail.setFareid(rs.getLong("fareid"));
				accountCwbFareDetail.setId(rs.getLong("id"));
				accountCwbFareDetail.setInfactfare(rs.getBigDecimal("infactfare"));
				accountCwbFareDetail.setPayuptime(rs.getString("payuptime"));
				accountCwbFareDetail.setShouldfare(rs.getBigDecimal("shouldfare"));

				cwbList.put(rs.getString("cwb"), accountCwbFareDetail);
			}
		});
		return cwbList;
	}

	public void deleteAccountCwbFareDetailByCwb(String cwb) {
		String sql = "delete from account_cwb_fare_detail where cwb=?";
		this.jdbcTemplate.update(sql, cwb);
	}

	public void updateAccountCwbFareDetailByCwb(String cwbs, String verifytime) {
		String sql = "update  account_cwb_fare_detail set verifytime=?,verifyflag=1 where cwb in (" + cwbs + ")";
		this.jdbcTemplate.update(sql, verifytime);
	}

	public List<AccountCwbFareDetail> getAccountCwbFareDetailBySubmit(long branchid, String begindate, String enddate, long cwbordertypeid, long faretypeid, long userid) {

		String sql = "select * from account_cwb_fare_detail " + "where " + " cwbordertypeid=" + cwbordertypeid + " and deliverybranchid=" + branchid + " and audittime>='" + begindate
				+ "' and audittime<='" + enddate + "' and userid=" + userid;
		if (faretypeid == 1) {
			sql += " and fareid=0";
		}
		if (faretypeid == 2) {
			sql += " and fareid>0";
			sql += " and verifyflag=0";
		}
		if (faretypeid == 3) {
			sql += " and fareid>0";
			sql += " and verifyflag>0";
		}

		return this.jdbcTemplate.query(sql, new AccountCwbFareDetailRowMapper());
	}
}

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

import cn.explink.domain.AccountDeducDetail;
import cn.explink.util.Page;

@Component
public class AccountDeducDetailDAO {
	@Autowired
	private JdbcTemplate jdbcTemplate;

	private final class AccountDeducDetailRowMapper implements RowMapper<AccountDeducDetail> {
		@Override
		public AccountDeducDetail mapRow(ResultSet rs, int rowNum) throws SQLException {
			AccountDeducDetail accountDeducDetail = new AccountDeducDetail();
			accountDeducDetail.setId(rs.getLong("id"));
			accountDeducDetail.setRecordid(rs.getLong("recordid"));
			accountDeducDetail.setBranchid(rs.getLong("branchid"));
			accountDeducDetail.setCwb(rs.getString("cwb"));
			accountDeducDetail.setFlowordertype(rs.getLong("flowordertype"));
			accountDeducDetail.setFee(rs.getBigDecimal("fee"));
			accountDeducDetail.setMemo(rs.getString("memo"));
			accountDeducDetail.setCreatetime(rs.getString("createtime"));
			accountDeducDetail.setUserid(rs.getLong("userid"));
			accountDeducDetail.setCreatetimesecond(rs.getString("createtimesecond"));
			accountDeducDetail.setRecordidvirt(rs.getLong("recordidvirt"));
			return accountDeducDetail;
		}
	}

	private final class AccountDeducDetailCount implements RowMapper<AccountDeducDetail> {
		@Override
		public AccountDeducDetail mapRow(ResultSet rs, int rowNum) throws SQLException {
			AccountDeducDetail accountDeducDetail = new AccountDeducDetail();
			accountDeducDetail.setFee(rs.getBigDecimal("fee"));
			return accountDeducDetail;
		}
	}

	private final class AccountDeducDetailNewCount implements RowMapper<AccountDeducDetail> {
		@Override
		public AccountDeducDetail mapRow(ResultSet rs, int rowNum) throws SQLException {
			AccountDeducDetail accountDeducDetail = new AccountDeducDetail();
			accountDeducDetail.setBranchid(rs.getLong("branchid"));
			accountDeducDetail.setFee(rs.getBigDecimal("fee"));
			accountDeducDetail.setMemo(rs.getString("memo"));
			return accountDeducDetail;
		}
	}

	public long createAccountDeducDetail(final AccountDeducDetail accountDeducDetail) {
		KeyHolder key = new GeneratedKeyHolder();
		jdbcTemplate.update(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(java.sql.Connection con) throws SQLException {
				PreparedStatement ps = null;
				ps = con.prepareStatement("insert into ops_account_deduct_detail(`recordid`,`branchid`,`cwb`,`flowordertype`,`fee`,`memo`,`userid`,`createtimesecond`,recordidvirt) "
						+ "values (?,?,?,?,?,?,?,?,?)", new String[] { "id" });
				ps.setLong(1, accountDeducDetail.getRecordid());
				ps.setLong(2, accountDeducDetail.getBranchid());
				ps.setString(3, accountDeducDetail.getCwb());
				ps.setLong(4, accountDeducDetail.getFlowordertype());
				ps.setBigDecimal(5, accountDeducDetail.getFee());
				ps.setString(6, accountDeducDetail.getMemo());
				ps.setLong(7, accountDeducDetail.getUserid());
				ps.setString(8, accountDeducDetail.getCreatetimesecond());
				ps.setLong(9, accountDeducDetail.getRecordidvirt());
				return ps;
			}
		}, key);
		accountDeducDetail.setId(key.getKey().longValue());
		return key.getKey().longValue();
	}

	public List<AccountDeducDetail> getDeducDetailByRuKuKouKuan(String cwb, long branchid, String flowordertype) {
		String sql = "SELECT * FROM ops_account_deduct_detail WHERE cwb=? AND flowordertype IN(" + flowordertype + ") AND recordidvirt=0 AND branchid=? ORDER BY id DESC LIMIT 0,1";
		return jdbcTemplate.query(sql, new AccountDeducDetailRowMapper(), cwb, branchid);
	}

	public void updateDeducDetailRecordVirtIdById(String ids) {
		String sql = "UPDATE ops_account_deduct_detail SET recordidvirt=1 where id in (" + ids + ") ";
		jdbcTemplate.update(sql);
	}

	public List<AccountDeducDetail> getDeducDetailByList(long branchid, String flowordertype, String cwb, String starttime, String endtime, String ids) {
		String sql = "SELECT * FROM ops_account_deduct_detail where recordid=0 and branchid=?";
		sql = this.getDeducDetailByPageWhere(sql, flowordertype, cwb, starttime, endtime, ids);
		List<AccountDeducDetail> list = jdbcTemplate.query(sql, new AccountDeducDetailRowMapper(), branchid);
		return list;
	}

	private String getDeducDetailByPageWhere(String sql, String flowordertype, String cwb, String starttime, String endtime, String ids) {
		StringBuffer sb = new StringBuffer(sql);
		if (!"".equals(ids)) {
			sb.append(" and id in (" + ids + ") ");
		}
		if (!("0").equals(flowordertype)) {
			sb.append(" and flowordertype IN (" + flowordertype + ") ");
		}
		if (!("").equals(cwb)) {
			sb.append(" and cwb IN (" + cwb + ")  ");
		}

		if (!("").equals(starttime)) {
			sb.append(" and createtime >='" + starttime + "' ");
		}
		if (!("").equals(endtime)) {
			sb.append(" and createtime <='" + endtime + "' ");
		}

		sb.append(" order by createtime desc");
		return sb.toString();
	}

	public List<AccountDeducDetail> getDeducDetailRecordIdById(String ids, long branchid) {
		// String sql
		// ="SELECT SUM(fee) AS fee,branchid,group_concat(CAST(id as char)) AS memo,SUM(recordid) recordid FROM ops_account_deduct_detail  where id in ("+ids+") GROUP BY branchid";
		String sql = "SELECT * FROM ops_account_deduct_detail  where id in (" + ids + ") and branchid=?";
		List<AccountDeducDetail> list = jdbcTemplate.query(sql, new AccountDeducDetailRowMapper(), branchid);
		return list;
	}

	public List<AccountDeducDetail> getDeducDetailRecordIdByIdVirt(String ids, long branchid) {
		// String sql
		// ="SELECT SUM(fee) AS fee,branchid,group_concat(CAST(id as char)) AS memo,SUM(recordid) recordid FROM ops_account_deduct_detail  where id in ("+ids+") and recordidvirt=0 GROUP BY branchid";
		String sql = "SELECT sum(fee) as fee FROM ops_account_deduct_detail  where id in (" + ids + ") and recordidvirt=0 and branchid=?";
		List<AccountDeducDetail> list = jdbcTemplate.query(sql, new AccountDeducDetailCount(), branchid);
		return list;
	}

	public List<AccountDeducDetail> getDeducDetailRecordPage(long page, long recordid) {
		try {
			String sql = "SELECT * FROM ops_account_deduct_detail where recordid =? ";
			sql += " limit " + (page - 1) * Page.ONE_PAGE_NUMBER + " ," + Page.ONE_PAGE_NUMBER;
			List<AccountDeducDetail> list = jdbcTemplate.query(sql, new AccountDeducDetailRowMapper(), recordid);
			return list;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public long getDeducDetailRecordCount(long recordid) {
		try {
			String sql = "SELECT count(1) FROM ops_account_deduct_detail where recordid =? ";
			return jdbcTemplate.queryForLong(sql, recordid);
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}

	public List<AccountDeducDetail> getDeducDetailRecordList(long recordid) {
		try {
			String sql = "SELECT * FROM ops_account_deduct_detail where recordid =? ";
			List<AccountDeducDetail> list = jdbcTemplate.query(sql, new AccountDeducDetailRowMapper(), recordid);
			return list;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public List<AccountDeducDetail> selectJobKouKuan(String flowordertype, long createtimesecond) {
		String sql = "SELECT branchid,SUM(fee) AS fee,GROUP_CONCAT(CAST(id AS CHAR)) AS memo FROM ops_account_deduct_detail WHERE recordid=0 AND flowordertype IN(" + flowordertype + ") "
				+ "AND createtimesecond<=? GROUP BY branchid";
		return jdbcTemplate.query(sql, new AccountDeducDetailNewCount(), createtimesecond);
	}

	public void updateJobKouKuanVirt(String ids, long branchid) {
		String sql = "UPDATE ops_account_deduct_detail SET recordidvirt=1 where id in (" + ids + ") and branchid=?";
		jdbcTemplate.update(sql, branchid);
	}

	public void updateJobKouKuan(long recordid, String ids, long branchid) {
		String sql = "UPDATE ops_account_deduct_detail SET recordid=? where id in (" + ids + ") and branchid=?";
		jdbcTemplate.update(sql, recordid, branchid);
	}

	public List<AccountDeducDetail> selectJobKouKuanVirt(String flowordertype, long createtimesecond) {
		String sql = "SELECT branchid,SUM(fee) AS fee,GROUP_CONCAT(CAST(id AS CHAR)) AS memo FROM ops_account_deduct_detail WHERE recordidvirt=0 AND flowordertype IN(" + flowordertype + ") "
				+ "AND createtimesecond<=? GROUP BY branchid";
		return jdbcTemplate.query(sql, new AccountDeducDetailNewCount(), createtimesecond);
	}

	public List<AccountDeducDetail> getEditCwbByFlowordertype(String cwb, String flowordertype) {
		try {
			String sql = "select * from ops_account_deduct_detail where cwb=? and flowordertype in (" + flowordertype + ") ";
			return jdbcTemplate.query(sql, new AccountDeducDetailRowMapper(), cwb);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public void deleteAccountDeducDetailById(long id) {
		jdbcTemplate.update("delete from ops_account_deduct_detail where id = ?", id);
	}

	public List<AccountDeducDetail> getDetailByCwb(String cwb) {
		try {
			String sql = "select * from ops_account_deduct_detail where cwb=? ";
			return jdbcTemplate.query(sql, new AccountDeducDetailRowMapper(), cwb);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public void updateXiuGaiJinE(String cwb, BigDecimal fee) {
		jdbcTemplate.update("update ops_account_deduct_detail set fee=? where cwb=?", fee, cwb);
	}

	/**
	 * 修改订单类型 修改订单表字段
	 * 
	 * @param id
	 * @param newcwbordertypeid
	 *            新的订单类型
	 */
	public void updateXiuGaiDingDanLeiXing(long id, BigDecimal fee) {
		jdbcTemplate.update("update ops_account_deduct_detail set fee=? where id=?", fee, id);

	}

	public List<AccountDeducDetail> getDeducDetailByZhongZhaunRuKuKouKuan(String cwb, String flowordertype) {
		String sql = "SELECT * FROM ops_account_deduct_detail WHERE cwb=? AND flowordertype IN(" + flowordertype + ") AND recordidvirt=0 ORDER BY id DESC LIMIT 0,1";
		return jdbcTemplate.query(sql, new AccountDeducDetailRowMapper(), cwb);
	}
}

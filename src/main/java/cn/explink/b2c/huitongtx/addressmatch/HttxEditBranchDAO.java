package cn.explink.b2c.huitongtx.addressmatch;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import cn.explink.util.Page;

@Component
public class HttxEditBranchDAO {

	private final class EditBranchRowMapper implements RowMapper<HttxEditBranch> {
		@Override
		public HttxEditBranch mapRow(ResultSet rs, int rowNum) throws SQLException {
			HttxEditBranch httxEditBranch = new HttxEditBranch();
			httxEditBranch.setId(rs.getLong("id"));
			httxEditBranch.setReceiver_city(rs.getString("receiver_city"));
			httxEditBranch.setReceiver_address(rs.getString("receiver_address"));
			httxEditBranch.setCretime(rs.getString("cretime"));
			httxEditBranch.setDealtime(rs.getString("dealtime"));
			httxEditBranch.setTaskcode(rs.getString("taskcode"));
			httxEditBranch.setMatchtype(rs.getInt("matchtype"));
			httxEditBranch.setSendflag(rs.getLong("sendflag"));
			httxEditBranch.setMatchbranch(rs.getString("matchbranch"));
			httxEditBranch.setBranchid(rs.getLong("branchid"));

			return httxEditBranch;
		}
	}

	@Autowired
	private JdbcTemplate jdbcTemplate;

	/**
	 * 根据条件查询快行线匹配表信息
	 * 
	 * @param starttime
	 * @param endtime
	 * @param matchtype
	 *            匹配类型(1自动匹配，2人工匹配，0未匹配) -1不根据此条件查询
	 * @param page
	 * @param sendflag
	 *            0未推送 1已推送
	 * @return
	 */
	public List<HttxEditBranch> getHttxEditBranchList(String starttime, String endtime, int matchtype, long branchid, long sendflag, long page, long onePageNumber) {
		String sql = "select * from ops_branchmatch_adress " + " where cretime >='" + starttime + "' and cretime<='" + endtime + "' ";
		if (sendflag <= 2) {
			sql += " and sendflag=" + sendflag;
		} else {
			sql += " and sendflag>2";
		}
		sql = getWhereSql(matchtype, branchid, sql);
		if (page > 0) {
			sql += " limit " + (page - 1) * onePageNumber + " ," + onePageNumber;
		}

		return jdbcTemplate.query(sql, new EditBranchRowMapper());
	}

	/**
	 * 查询出所有未推送的订单数据进行推送。
	 * 
	 * @param matchtype
	 *            匹配类型(1自动匹配，2人工匹配，0未匹配) -1不根据此条件查询
	 * @param sendflag
	 *            0未推送 1已推送
	 * @return
	 */
	public List<HttxEditBranch> getHttxEditBranchSendList(long maxCount, int matchtype, String starttime, String endtime) {
		String sql = "select * from ops_branchmatch_adress  where  sendflag=0 and matchtype=" + matchtype + " and cretime between '" + starttime + "' and '" + endtime + "' order by cretime limit 0,"
				+ maxCount;

		return jdbcTemplate.query(sql, new EditBranchRowMapper());
	}

	public long getHttxCountByCwb(String taskcode) {
		String sql = "select count(1) from ops_branchmatch_adress  where  taskcode=?";

		return jdbcTemplate.queryForLong(sql, taskcode);
	}

	public long getHttxEditBranchCount(String starttime, String endtime, int matchtype, long branchid, long sendflag) {
		String sql = "select count(1) from ops_branchmatch_adress " + " where cretime >='" + starttime + "' and cretime<='" + endtime + "' ";
		if (sendflag <= 2) {
			sql += " and sendflag=" + sendflag;
		} else {
			sql += " and sendflag>2";
		}

		sql = getWhereSql(matchtype, branchid, sql);
		return jdbcTemplate.queryForLong(sql);
	}

	private String getWhereSql(int matchtype, long branchid, String sql) {
		if (matchtype != -1) {
			sql += " and matchtype=" + matchtype;
		}
		if (branchid > 0) {
			sql += " and branchid=" + branchid;
		}
		return sql;
	}

	public void creHttxEditBranchData(String taskcode, String receiver_city, String receiver_address, String cretime, String dealtime, long sendflag, int matchtype, String branchname, long branchid) {
		jdbcTemplate.update("insert into ops_branchmatch_adress (taskcode,receiver_city,receiver_address,cretime,dealtime,sendflag,matchtype,matchbranch,branchid) " + "values(?,?,?,?,?,?,?,?,? )",
				taskcode, receiver_city, receiver_address, cretime, dealtime, sendflag, matchtype, branchname, branchid);
	}

	public void updateHttxEditBranchData(String dealtime, String matchbranch, long branchid, int matchtype, long id) {
		jdbcTemplate.update("update ops_branchmatch_adress set dealtime=?,matchbranch=?,branchid=?,matchtype=? where id=?", dealtime, matchbranch, branchid, matchtype, id);
	}

	public void updateSendStaus(String taskcode, String remark, String dealtime, long sendflag) {
		jdbcTemplate.update("update ops_branchmatch_adress set sendflag=?,dealtime=?,remark=? where taskcode=?", sendflag, dealtime, remark, taskcode);
	}

	public HttxEditBranch getHttxEditBranchById(long id) {
		String sql = "select * from ops_branchmatch_adress where  id=?";
		return jdbcTemplate.queryForObject(sql, new EditBranchRowMapper(), id);
	}

	public long getHttxNoSendSelect(int matchtype, String starttime, String endtime) {
		String sql = "select count(1) from ops_branchmatch_adress where sendflag=0 and cretime between '" + starttime + "' and '" + endtime + "'   and  matchtype=" + matchtype;
		return jdbcTemplate.queryForLong(sql);
	}

	// public void delAccountArea(long customerid){
	// jdbcTemplate.update("update ops_branchmatch_adress set isEffectFlag=(isEffectFlag+1)%2 where customerid=? ",customerid);
	// }

}

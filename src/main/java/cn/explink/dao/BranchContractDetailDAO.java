package cn.explink.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.neo4j.cypher.internal.compiler.v2_1.docbuilders.internalDocBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import cn.explink.domain.ExpressSetBranchContractDetail;
import cn.explink.util.Page;

@Component
public class BranchContractDetailDAO {
	private final class BranchContractDetailMapper implements
			RowMapper<ExpressSetBranchContractDetail> {
		@Override
		public ExpressSetBranchContractDetail mapRow(ResultSet rs, int rowNum)
				throws SQLException {
			ExpressSetBranchContractDetail branchContractDetail = new ExpressSetBranchContractDetail();
			branchContractDetail.setId(rs.getInt("id"));
			branchContractDetail.setBranchId(rs.getInt("branchId"));
			branchContractDetail.setDepositReturnTime(rs
					.getString("depositReturnTime"));
			branchContractDetail.setDepositReturnAmount(rs
					.getBigDecimal("depositReturnAmount"));
			branchContractDetail.setDepositReturnPerson(rs.getString("depositReturnPerson"));
			branchContractDetail.setDepositCollector(rs.getString("depositCollector"));
			branchContractDetail.setRemark(rs.getString("remark"));
			branchContractDetail.setCreator(rs.getInt("creator"));
			branchContractDetail.setCreateTime(rs.getString("createTime"));
			branchContractDetail.setModifyPerson(rs.getInt("modifyPerson"));
			branchContractDetail.setModifyTime(rs.getString("modifyTime"));

			return branchContractDetail;
		}
	}

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	public long createBranchContractDetail(
			final ExpressSetBranchContractDetail branchContractDetail) {
		KeyHolder key = new GeneratedKeyHolder();
		jdbcTemplate.update(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(
					java.sql.Connection con) throws SQLException {
				PreparedStatement ps = null;
				ps = con.prepareStatement(
						"insert into express_set_branch_contract_detail("
								+ "branchId,depositReturnTime,"
								+ "depositReturnAmount,depositReturnPerson,"
								+ "depositCollector,remark,creator,createTime"
								+ ") values(?,?,?,?,?,?,?,?)",
						new String[] { "id" });
				int i = 1;
				ps.setInt(i++, branchContractDetail.getBranchId());
				ps.setString(i++, branchContractDetail.getDepositReturnTime());
				ps.setBigDecimal(i++, branchContractDetail.getDepositReturnAmount());
				ps.setString(i++, branchContractDetail.getDepositReturnPerson());
				ps.setString(i++, branchContractDetail.getDepositCollector());
				ps.setString(i++, branchContractDetail.getRemark());
				ps.setInt(i++, branchContractDetail.getCreator());
				ps.setString(i++, branchContractDetail.getCreateTime());

				return ps;
			}
		}, key);
		return key.getKey().longValue();
	}
	
	
	@CacheEvict(value = "branchContractDetailCache", key = "#branchContractDetail.id")
	public void updateBranchContractDetail(final ExpressSetBranchContractDetail branchContractDetail) {
		
		this.jdbcTemplate
		.update("update express_set_branch_contract_detail set "
				+ "depositReturnTime=?,depositReturnAmount=?,"
				+ "depositReturnPerson=?,depositCollector=?,remark=?,"
				+ "modifyPerson=?,modifyTime=? where id=?", new PreparedStatementSetter() {
					@Override
					public void setValues(PreparedStatement ps) throws SQLException {
						// TODO Auto-generated method stub
						int i = 1;
						ps.setString(i++, branchContractDetail.getDepositReturnTime());
						ps.setBigDecimal(i++, branchContractDetail.getDepositReturnAmount());
						ps.setString(i++, branchContractDetail.getDepositReturnPerson());
						ps.setString(i++, branchContractDetail.getDepositCollector());
						ps.setString(i++, branchContractDetail.getRemark());
						ps.setInt(i++, branchContractDetail.getModifyPerson());
						ps.setString(i++, branchContractDetail.getModifyTime());
						ps.setInt(i++, branchContractDetail.getId());
					}
				});
	}
	
	public int deleteBranchContractDetail(int id) {
		String sql = "delete from express_set_branch_contract_detail where id=" + id;
		return this.jdbcTemplate.update(sql);
	}
	
	public int deleteBranchContractDetailByBranchId(String branchIds) {
		String sql = "delete from express_set_branch_contract_detail where branchId in (" + branchIds + ")";
		return this.jdbcTemplate.update(sql);
	}
	
	public List<ExpressSetBranchContractDetail> getBranchContractDetailList() {
		String sql = "select * from express_set_branch_contract_detail";
		return jdbcTemplate.query(sql, new BranchContractDetailMapper());
	}
	
	public List<ExpressSetBranchContractDetail> getBranchContractDetailListByBranchId(int branchId) {
		String sql = "select * from express_set_branch_contract_detail where branchId=?";
		return jdbcTemplate.query(sql, new BranchContractDetailMapper(), branchId);
	}
	
//	public long create(final String baleno) {
//		KeyHolder key = new GeneratedKeyHolder();
//		jdbcTemplate.update(new PreparedStatementCreator() {
//			public PreparedStatement createPreparedStatement(
//					java.sql.Connection con) throws SQLException {
//				PreparedStatement ps = null;
//				ps = con.prepareStatement(
//						"insert into express_ops_bale(baleno) values(?)",
//						new String[] { "id" });
//				ps.setString(1, baleno);
//				return ps;
//			}
//		}, key);
//		return key.getKey().longValue();
//	}

	public ExpressSetBranchContractDetail getBranchContractByBranchContractstateAndBranchid(
			String baleno, long balestate, long branchid) {
		try {
			String sql = "select * from express_ops_bale where baleno=? and balestate=? and branchid=?";
			return jdbcTemplate.queryForObject(sql, new BranchContractDetailMapper(),
					baleno, balestate, branchid);
		} catch (Exception e) {
			return null;
		}
	}

	

	public List<ExpressSetBranchContractDetail> getBranchContractByBranchContractstate(
			String baleno, long balestate) {
		String sql = "select * from express_ops_bale where baleno=? and balestate=?";
		return jdbcTemplate.query(sql, new BranchContractDetailMapper(), baleno,
				balestate);
	}

	public List<ExpressSetBranchContractDetail> getBranchContractByBranchContractnoAndBranchContractstate(
			String baleno, String balestates) {
		String sql = "select * from express_ops_bale where baleno=? and balestate in("
				+ balestates + ")";
		return jdbcTemplate.query(sql, new BranchContractDetailMapper(), baleno);
	}

	public List<ExpressSetBranchContractDetail> getBranchContractByBranchContractstate(
			long balestate) {
		String sql = "select * from express_ops_bale where balestate=?";
		return jdbcTemplate.query(sql, new BranchContractDetailMapper(), balestate);
	}

	public void saveForBranchid(long id, long branchid, long groupid,
			long balestate) {
		String sql = "update express_ops_bale set branchid=?,groupid=?,balestate=? where id=?";
		jdbcTemplate.update(sql, branchid, groupid, balestate, id);
	}

	public ExpressSetBranchContractDetail getBranchContractByBranchContractno(
			String baleno, long balestate) {
		try {
			String sql = "select * from express_ops_bale where baleno=? and balestate=?";
			return jdbcTemplate.queryForObject(sql, new BranchContractDetailMapper(),
					baleno, balestate);
		} catch (DataAccessException e) {
			return null;
		}
	}

	/*
	 * public void saveForBranchContractState(long balestate,long groupid,long
	 * branchid){ String sql =
	 * "update express_ops_bale set balestate="+balestate+
	 * " where id in(select baleid from express_ops_groupdetail where groupid="
	 * +groupid
	 * +") and branchid="+branchid+" and balestate="+ContractStateEnum.SaoMiaoZhong
	 * .getValue(); jdbcTemplate.update(sql); }
	 */

//	public void saveForState(String baleno, long branchid, long balestate) {
//		String sql = "update express_ops_bale set balestate=? where branchid=? and baleno=? and balestate=? ";
//		jdbcTemplate.update(sql, balestate, branchid, baleno,
//				ContractStateEnum.WeiDaoZhan.getValue());
//	}

	public void saveForBranchContractstate(String baleno, long balestate,
			long oldbalestate) {
		String sql = "update express_ops_bale set balestate=? where baleno=? and balestate=? ";
		jdbcTemplate.update(sql, balestate, baleno, oldbalestate);
	}

//	public void saveForBranchidAndState(String baleno, long branchid,
//			long balestate) {
//		String sql = "update express_ops_bale set branchid=?,balestate=? where baleno=? and balestate=? ";
//		jdbcTemplate.update(sql, branchid, balestate, baleno,
//				ContractStateEnum.WeiDaoZhan.getValue());
//	}

//	public void saveForBranchidAndGroupid(long branchid, long balestate,
//			long groupid) {
//		String sql = "update express_ops_bale set balestate=? where branchid=? and groupid=? and balestate=?";
//		jdbcTemplate.update(sql, balestate, branchid, groupid,
//				ContractStateEnum.WeiDaoZhan.getValue());
//	}

	public void saveById(long balestate, long id) {
		String sql = "update express_ops_bale set balestate=? where id=?";
		jdbcTemplate.update(sql, balestate, id);
	}

	public List<ExpressSetBranchContractDetail> getBranchContractByBranchContractno(
			String baleno) {
		String sql = "select * from express_ops_bale where baleno=?";
		return jdbcTemplate.query(sql, new BranchContractDetailMapper(), baleno);

	}

	public void saveForBranchContractCount(long id, long cwbcount) {
		String sql = "update express_ops_bale set cwbcount=?  where id=? ";
		jdbcTemplate.update(sql, cwbcount, id);
	}

	public List<ExpressSetBranchContractDetail> getBranchContractByChukuDate(
			String begindate, String enddate, long page) {
		String sql = "select * from express_ops_bale where cretime>=? and cretime<=? ";
		sql += " limit " + (page - 1) * Page.ONE_PAGE_NUMBER + " ,"
				+ Page.ONE_PAGE_NUMBER;
		return jdbcTemplate.query(sql, new BranchContractDetailMapper(), begindate,
				enddate);
	}

	public long getBranchContractByChukuDateCount(String begindate,
			String enddate) {
		String sql = "select count(1) from express_ops_bale where cretime>=? and cretime<=? ";
		return jdbcTemplate.queryForLong(sql, begindate, enddate);
	}

	public ExpressSetBranchContractDetail getBranchContractByCwb(String cwb) {
		try {
			String sql = "select * from express_ops_bale where cwb=? ";
			return jdbcTemplate.queryForObject(sql, new BranchContractDetailMapper(),
					cwb);
		} catch (DataAccessException e) {
			return null;
		}
	}

	public ExpressSetBranchContractDetail getBranchContractOneByBranchContractno(
			String baleno) {
		try {
			String sql = "select * from express_ops_bale where baleno=? ";
			return jdbcTemplate.queryForObject(sql, new BranchContractDetailMapper(),
					baleno);
		} catch (DataAccessException e) {
			return null;
		}
	}

	public ExpressSetBranchContractDetail getBranchContractOneByBranchContractnoLock(
			String baleno) {
		try {
			String sql = "select * from express_ops_bale where baleno=? for update";
			return jdbcTemplate.queryForObject(sql, new BranchContractDetailMapper(),
					baleno);
		} catch (DataAccessException e) {
			return null;
		}
	}

	public void updateSubBranchContractCount(String baleno) {
		String sql = "update express_ops_bale set cwbcount=cwbcount-1 where baleno=? ";
		jdbcTemplate.update(sql, baleno);
	}

	public void updateAddBranchContractCount(String baleno) {
		String sql = "update express_ops_bale set cwbcount=cwbcount+1 where baleno=? ";
		jdbcTemplate.update(sql, baleno);
	}

	public void updateBranchContractsate(String baleno, long balestate) {
		String sql = "update express_ops_bale set balestate=? where baleno=? ";
		jdbcTemplate.update(sql, balestate, baleno);
	}

	public List<ExpressSetBranchContractDetail> getBranchContractByBranchContractPrint(
			long branchid, String baleno, String strtime, String endtime) {
		String sql = "select * from express_ops_bale where branchid=? and cwbcount>0";
		if (baleno.length() > 0) {
			sql += " and baleno='" + baleno + "'";
		}
		if (strtime.length() > 0) {
			sql += " and cretime>'" + strtime + "'";
		}
		if (endtime.length() > 0) {
			sql += " and cretime<'" + endtime + "'";
		}
		return jdbcTemplate.query(sql, new BranchContractDetailMapper(), branchid);
	}

}

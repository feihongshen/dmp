package cn.explink.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import cn.explink.domain.ExpressSetBranchContract;
import cn.explink.domain.VO.ExpressSetBranchContractVO;
import cn.explink.util.Page;

@Component
public class BranchContractDAO {
	private final class BranchContractMapper implements
			RowMapper<ExpressSetBranchContract> {
		@Override
		public ExpressSetBranchContract mapRow(ResultSet rs, int rowNum)
				throws SQLException {
			ExpressSetBranchContract branchContract = new ExpressSetBranchContract();
			branchContract.setId(rs.getInt("id"));
			branchContract.setContractNo(rs.getString("contractNo"));
			branchContract.setContractState(rs.getInt("contractState"));
			branchContract.setContractBeginDate(rs
					.getString("contractBeginDate"));
			branchContract.setContractEndDate(rs
					.getString("contractEndDate"));
			branchContract.setBranchName(rs.getString("branchName"));
			branchContract.setSiteChief(rs.getString("siteChief"));
			branchContract.setChiefIdentity(rs.getString("chiefIdentity"));
			branchContract.setAreaManager(rs.getString("areaManager"));
			branchContract.setIsDeposit(rs.getInt("isDeposit"));
			branchContract.setDepositCollectDate(rs
					.getString("depositCollectDate"));
			branchContract.setDepositCollectAmount(rs
					.getBigDecimal("depositCollectAmount"));
			branchContract
					.setDepositCollector(rs.getString("depositCollector"));
			branchContract.setDepositPayor(rs.getString("depositPayor"));
			branchContract.setContractDescription(rs
					.getString("contractDescription"));
			branchContract.setContractAttachment(rs
					.getString("contractAttachment"));
			branchContract.setCreator(rs.getInt("creator"));
			branchContract.setCreateTime(rs.getString("createTime"));
			branchContract.setModifyPerson(rs.getInt("modifyPerson"));
			branchContract.setModifyTime(rs.getString("modifyTime"));

			return branchContract;
		}
	}

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public long createBranchContract(
			final ExpressSetBranchContract branchContract) {
		KeyHolder key = new GeneratedKeyHolder();
		jdbcTemplate.update(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(
					java.sql.Connection con) throws SQLException {
				PreparedStatement ps = null;
				ps = con.prepareStatement(
						"insert into express_set_branch_contract("
								+ "contractNo,contractState,contractBeginDate,contractEndDate,branchName,"
								+ "siteChief,chiefIdentity,areaManager,isDeposit,depositCollectDate,"
								+ "depositCollectAmount,depositCollector,depositPayor,contractDescription,"
								+ "contractAttachment,creator,createTime) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)",
						new String[] { "id" });
				int i = 1;

				ps.setString(i++, branchContract.getContractNo());
				ps.setInt(i++, branchContract.getContractState());
				ps.setString(i++, branchContract.getContractBeginDate());
				ps.setString(i++, branchContract.getContractEndDate());
				ps.setString(i++, branchContract.getBranchName());
				ps.setString(i++, branchContract.getSiteChief());
				ps.setString(i++, branchContract.getChiefIdentity());
				ps.setString(i++, branchContract.getAreaManager());
				ps.setInt(i++, branchContract.getIsDeposit());
				ps.setString(i++, branchContract.getDepositCollectDate());
				ps.setBigDecimal(i++, branchContract.getDepositCollectAmount());
				ps.setString(i++, branchContract.getDepositCollector());
				ps.setString(i++, branchContract.getDepositPayor());
				ps.setString(i++, branchContract.getContractDescription());
				ps.setString(i++, branchContract.getContractAttachment());
				ps.setInt(i++, branchContract.getCreator());
				ps.setString(i++, branchContract.getCreateTime());

				return ps;
			}
		}, key);
		return key.getKey().longValue();
	}

	public void updateBranchContract(final ExpressSetBranchContract branchContract) {

		this.jdbcTemplate
				.update("update express_set_branch_contract set "
						+ "contractNo=?,contractState=?,contractBeginDate=?,"
						+ "contractEndDate=?,branchName=?,siteChief=?,chiefIdentity=?,"
						+ "areaManager=?,isDeposit=?,depositCollectDate=?,depositCollectAmount=?,"
						+ "depositCollector=?,depositPayor=?,contractDescription=?,contractAttachment=?,"
						+ "modifyPerson=?,modifyTime=? where id=?", new PreparedStatementSetter() {
					@Override
					public void setValues(PreparedStatement ps) throws SQLException {
						// TODO Auto-generated method stub
						int i = 1;

						ps.setString(i++, branchContract.getContractNo());
						ps.setInt(i++, branchContract.getContractState());
						ps.setString(i++, branchContract.getContractBeginDate());
						ps.setString(i++, branchContract.getContractEndDate());
						ps.setString(i++, branchContract.getBranchName());
						ps.setString(i++, branchContract.getSiteChief());
						ps.setString(i++, branchContract.getChiefIdentity());
						ps.setString(i++, branchContract.getAreaManager());
						ps.setInt(i++, branchContract.getIsDeposit());
						ps.setString(i++, branchContract.getDepositCollectDate());
						ps.setBigDecimal(i++, branchContract.getDepositCollectAmount());
						ps.setString(i++, branchContract.getDepositCollector());
						ps.setString(i++, branchContract.getDepositPayor());
						ps.setString(i++, branchContract.getContractDescription());
						ps.setString(i++, branchContract.getContractAttachment());
						ps.setInt(i++, branchContract.getModifyPerson());
						ps.setString(i++, branchContract.getModifyTime());
						ps.setInt(i++, branchContract.getId());
					}
				});
	}
	
	public int deleteBranchContract(int id) {
		String sql = "delete from express_set_branch_contract where id=" + id;
		return this.jdbcTemplate.update(sql);
	}
	
	public List<ExpressSetBranchContract> getBranchContractList() {
		String sql = "select * from express_set_branch_contract";
		return jdbcTemplate.query(sql, new BranchContractMapper());
	}
	
	public List<ExpressSetBranchContract> getBranchContractListById(int id) {
		String sql = "select * from express_set_branch_contract where id=?";
		return jdbcTemplate.query(sql, new BranchContractMapper(), id);
	}
	
	public List<ExpressSetBranchContract> queryBranchContract(ExpressSetBranchContractVO branchContractVO) {
		
		String sql = "select * from express_set_branch_contract where 1=1 ";
		if(branchContractVO != null){
			if(StringUtils.isNotBlank(branchContractVO.getContractNo())){
				sql += " and contractNo like '%" + branchContractVO.getContractNo() + "%' ";
			}
			if(branchContractVO.getContractState() != 0){
				sql += " and contractState= '" + branchContractVO.getContractState() + "' ";
			}
			if(StringUtils.isNotBlank(branchContractVO.getBranchName())){
				sql += " and branchName like '%" + branchContractVO.getBranchName() + "%' ";
			}
			if(StringUtils.isNotBlank(branchContractVO.getAreaManager())){
				sql += " and areaManager like '%" + branchContractVO.getAreaManager() + "%' ";
			}
			if(StringUtils.isNotBlank(branchContractVO.getSiteChief())){
				sql += " and siteChief like '%" + branchContractVO.getSiteChief() + "%' ";
			}
			if(StringUtils.isNotBlank(branchContractVO.getChiefIdentity())){
				sql += " and chiefIdentity like '%" + branchContractVO.getChiefIdentity() + "%' ";
			}
			if(StringUtils.isNotBlank(branchContractVO.getContractDescription())){
				sql += " and contractDescription like '%" + branchContractVO.getContractDescription() + "%' ";
			}
			if(branchContractVO.getIsDeposit() != 2){
				sql += " and isDeposit= '" + branchContractVO.getIsDeposit() + "' ";
			}
			if(StringUtils.isNotBlank(branchContractVO.getContractBeginDateFrom())){
				sql += " and contractBeginDate>= '" + branchContractVO.getContractBeginDateFrom() + "' ";
			}
			if(StringUtils.isNotBlank(branchContractVO.getContractBeginDateTo())){
				sql += " and contractBeginDate<= '" + branchContractVO.getContractBeginDateTo() + "' ";
			}
			if(StringUtils.isNotBlank(branchContractVO.getContractEndDateFrom())){
				sql += " and contractEndDate>= '" + branchContractVO.getContractEndDateFrom() + "' ";
			}
			if(StringUtils.isNotBlank(branchContractVO.getContractEndDateTo())){
				sql += " and contractEndDate<= '" + branchContractVO.getContractEndDateTo() + "' ";
			}
			if(StringUtils.isNotBlank(branchContractVO.getContractColumn())){
				sql += " order by " + branchContractVO.getContractColumn();
			}
			if(StringUtils.isNotBlank(branchContractVO.getContractColumnOrder())){
				sql += " " + branchContractVO.getContractColumnOrder();
			}
		}
		
		return jdbcTemplate.query(sql, new BranchContractMapper());
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

	public ExpressSetBranchContract getBranchContractByBranchContractstateAndBranchid(
			String baleno, long balestate, long branchid) {
		try {
			String sql = "select * from express_ops_bale where baleno=? and balestate=? and branchid=?";
			return jdbcTemplate.queryForObject(sql, new BranchContractMapper(),
					baleno, balestate, branchid);
		} catch (Exception e) {
			return null;
		}
	}

	

	public List<ExpressSetBranchContract> getBranchContractByBranchContractstate(
			String baleno, long balestate) {
		String sql = "select * from express_ops_bale where baleno=? and balestate=?";
		return jdbcTemplate.query(sql, new BranchContractMapper(), baleno,
				balestate);
	}

	public List<ExpressSetBranchContract> getBranchContractByBranchContractnoAndBranchContractstate(
			String baleno, String balestates) {
		String sql = "select * from express_ops_bale where baleno=? and balestate in("
				+ balestates + ")";
		return jdbcTemplate.query(sql, new BranchContractMapper(), baleno);
	}

	public List<ExpressSetBranchContract> getBranchContractByBranchContractstate(
			long balestate) {
		String sql = "select * from express_ops_bale where balestate=?";
		return jdbcTemplate.query(sql, new BranchContractMapper(), balestate);
	}

	public void saveForBranchid(long id, long branchid, long groupid,
			long balestate) {
		String sql = "update express_ops_bale set branchid=?,groupid=?,balestate=? where id=?";
		jdbcTemplate.update(sql, branchid, groupid, balestate, id);
	}

	public ExpressSetBranchContract getBranchContractByBranchContractno(
			String baleno, long balestate) {
		try {
			String sql = "select * from express_ops_bale where baleno=? and balestate=?";
			return jdbcTemplate.queryForObject(sql, new BranchContractMapper(),
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

	public List<ExpressSetBranchContract> getBranchContractByBranchContractno(
			String baleno) {
		String sql = "select * from express_ops_bale where baleno=?";
		return jdbcTemplate.query(sql, new BranchContractMapper(), baleno);

	}

	public void saveForBranchContractCount(long id, long cwbcount) {
		String sql = "update express_ops_bale set cwbcount=?  where id=? ";
		jdbcTemplate.update(sql, cwbcount, id);
	}

	public List<ExpressSetBranchContract> getBranchContractByChukuDate(
			String begindate, String enddate, long page) {
		String sql = "select * from express_ops_bale where cretime>=? and cretime<=? ";
		sql += " limit " + (page - 1) * Page.ONE_PAGE_NUMBER + " ,"
				+ Page.ONE_PAGE_NUMBER;
		return jdbcTemplate.query(sql, new BranchContractMapper(), begindate,
				enddate);
	}

	public long getBranchContractByChukuDateCount(String begindate,
			String enddate) {
		String sql = "select count(1) from express_ops_bale where cretime>=? and cretime<=? ";
		return jdbcTemplate.queryForLong(sql, begindate, enddate);
	}

	public ExpressSetBranchContract getBranchContractByCwb(String cwb) {
		try {
			String sql = "select * from express_ops_bale where cwb=? ";
			return jdbcTemplate.queryForObject(sql, new BranchContractMapper(),
					cwb);
		} catch (DataAccessException e) {
			return null;
		}
	}

	public ExpressSetBranchContract getBranchContractOneByBranchContractno(
			String baleno) {
		try {
			String sql = "select * from express_ops_bale where baleno=? ";
			return jdbcTemplate.queryForObject(sql, new BranchContractMapper(),
					baleno);
		} catch (DataAccessException e) {
			return null;
		}
	}

	public ExpressSetBranchContract getBranchContractOneByBranchContractnoLock(
			String baleno) {
		try {
			String sql = "select * from express_ops_bale where baleno=? for update";
			return jdbcTemplate.queryForObject(sql, new BranchContractMapper(),
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

	public List<ExpressSetBranchContract> getBranchContractByBranchContractPrint(
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
		return jdbcTemplate.query(sql, new BranchContractMapper(), branchid);
	}

}

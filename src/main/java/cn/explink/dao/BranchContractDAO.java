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
			branchContract.setBranchId(rs.getInt("branchId"));
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
			branchContract.setQualityControlClause(rs
					.getString("qualityControlClause"));
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
								+ "contractNo,contractState,contractBeginDate,contractEndDate,branchId,branchName,"
								+ "siteChief,chiefIdentity,areaManager,isDeposit,depositCollectDate,"
								+ "depositCollectAmount,depositCollector,depositPayor,contractDescription,"
								+ "contractAttachment,qualityControlClause,creator,createTime) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)",
						new String[] { "id" });
				int i = 1;

				ps.setString(i++, branchContract.getContractNo());
				ps.setInt(i++, branchContract.getContractState());
				ps.setString(i++, branchContract.getContractBeginDate());
				ps.setString(i++, branchContract.getContractEndDate());
				ps.setInt(i++, branchContract.getBranchId());
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
				ps.setString(i++, branchContract.getQualityControlClause());
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
						+ "contractState=?,areaManager=?,depositPayor=?,contractDescription=?,"
						+ "qualityControlClause=?,modifyPerson=?,modifyTime=? where id=?", new PreparedStatementSetter() {
					@Override
					public void setValues(PreparedStatement ps) throws SQLException {
						// TODO Auto-generated method stub
						int i = 1;

						ps.setInt(i++, branchContract.getContractState());
						ps.setString(i++, branchContract.getAreaManager());
						ps.setString(i++, branchContract.getDepositPayor());
						ps.setString(i++, branchContract.getContractDescription());
						ps.setString(i++, branchContract.getQualityControlClause());
						ps.setInt(i++, branchContract.getModifyPerson());
						ps.setString(i++, branchContract.getModifyTime());
						ps.setInt(i++, branchContract.getId());
					}
				});
	}
	
	public int deleteBranchContract(String ids) {
		String sql = "delete from express_set_branch_contract where id in (" + ids + ")";
		return this.jdbcTemplate.update(sql);
	}
	
	public int validateContractNo(String contractNo) {
		String sql = "select count(1) from express_set_branch_contract where contractNo='" + contractNo + "'";
		return this.jdbcTemplate.queryForInt(sql);
	}
	
	public List<ExpressSetBranchContract> getBranchContractList() {
		String sql = "select * from express_set_branch_contract";
		return jdbcTemplate.query(sql, new BranchContractMapper());
	}
	
	public List<ExpressSetBranchContract> getBranchContractListByBranchId(int branchId) {
		String sql = "select * from express_set_branch_contract where branchId = ?";
		return jdbcTemplate.query(sql, new BranchContractMapper(), branchId);
	}
	
	public List<ExpressSetBranchContract> getBranchContractListById(int id) {
		String sql = "select * from express_set_branch_contract where id=?";
		return jdbcTemplate.query(sql, new BranchContractMapper(), id);
	}
	
	public List<ExpressSetBranchContract> queryBranchContract(long page, ExpressSetBranchContractVO branchContractVO) {
		
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
			if(branchContractVO.getIsDeposit() != null && branchContractVO.getIsDeposit().intValue() != 2){
				sql += " and isDeposit= '" + branchContractVO.getIsDeposit().intValue() + "' ";
			}
			if(StringUtils.isNotBlank(branchContractVO.getContractBeginDateFrom())){
				sql += " and DATE_FORMAT(contractBeginDate,'%Y-%m-%d %H:%i:%s') >= DATE_FORMAT('"
						+ branchContractVO.getContractBeginDateFrom() + "','%Y-%m-%d %H:%i:%s')";
			}
			if(StringUtils.isNotBlank(branchContractVO.getContractBeginDateTo())){
				sql += " and DATE_FORMAT(contractBeginDate,'%Y-%m-%d %H:%i:%s') <= DATE_FORMAT('"
						+ branchContractVO.getContractBeginDateTo() + "','%Y-%m-%d %H:%i:%s')";
			}
			if(StringUtils.isNotBlank(branchContractVO.getContractEndDateFrom())){
				sql += " and DATE_FORMAT(contractEndDate,'%Y-%m-%d %H:%i:%s') >= DATE_FORMAT('"
						+ branchContractVO.getContractEndDateFrom() + "','%Y-%m-%d %H:%i:%s')";
			}
			if(StringUtils.isNotBlank(branchContractVO.getContractEndDateTo())){
				sql += " and DATE_FORMAT(contractEndDate,'%Y-%m-%d %H:%i:%s') <= DATE_FORMAT('"
						+ branchContractVO.getContractEndDateTo() + "','%Y-%m-%d %H:%i:%s')";
			}
			if(StringUtils.isNotBlank(branchContractVO.getContractColumn())){
				sql += " order by " + branchContractVO.getContractColumn();
			}
			if(StringUtils.isNotBlank(branchContractVO.getContractColumnOrder())){
				sql += " " + branchContractVO.getContractColumnOrder();
			}
		}
		if(sql.indexOf("order by") > -1) {
			sql += " limit " + ((page - 1) * Page.ONE_PAGE_NUMBER) + " ," + Page.ONE_PAGE_NUMBER;
		} else {
			sql += " order by id desc limit " + ((page - 1) * Page.ONE_PAGE_NUMBER) + " ," + Page.ONE_PAGE_NUMBER;
		}
		
		return jdbcTemplate.query(sql, new BranchContractMapper());
	}
	
	public int queryBranchContractCount(ExpressSetBranchContractVO branchContractVO) {
		
		String sql = "select count(1) from express_set_branch_contract where 1=1 ";
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
			if(branchContractVO.getIsDeposit() != null && branchContractVO.getIsDeposit().intValue() != 2){
				sql += " and isDeposit= '" + branchContractVO.getIsDeposit().intValue() + "' ";
			}
			if(StringUtils.isNotBlank(branchContractVO.getContractBeginDateFrom())){
				sql += " and DATE_FORMAT(contractBeginDate,'%Y-%m-%d %H:%i:%s') >= DATE_FORMAT('"
						+ branchContractVO.getContractBeginDateFrom() + "','%Y-%m-%d %H:%i:%s')";
			}
			if(StringUtils.isNotBlank(branchContractVO.getContractBeginDateTo())){
				sql += " and DATE_FORMAT(contractBeginDate,'%Y-%m-%d %H:%i:%s') <= DATE_FORMAT('"
						+ branchContractVO.getContractBeginDateTo() + "','%Y-%m-%d %H:%i:%s')";
			}
			if(StringUtils.isNotBlank(branchContractVO.getContractEndDateFrom())){
				sql += " and DATE_FORMAT(contractEndDate,'%Y-%m-%d %H:%i:%s') >= DATE_FORMAT('"
						+ branchContractVO.getContractEndDateFrom() + "','%Y-%m-%d %H:%i:%s')";
			}
			if(StringUtils.isNotBlank(branchContractVO.getContractEndDateTo())){
				sql += " and DATE_FORMAT(contractEndDate,'%Y-%m-%d %H:%i:%s') <= DATE_FORMAT('"
						+ branchContractVO.getContractEndDateTo() + "','%Y-%m-%d %H:%i:%s')";
			}
			if(StringUtils.isNotBlank(branchContractVO.getContractColumn())){
				sql += " order by " + branchContractVO.getContractColumn();
			}
			if(StringUtils.isNotBlank(branchContractVO.getContractColumnOrder())){
				sql += " " + branchContractVO.getContractColumnOrder();
			}
		}
		
		return jdbcTemplate.queryForInt(sql);
	}
	
	public List<ExpressSetBranchContract> getMaxContractNo(){
		String sql = "select * from express_set_branch_contract order by contractNo desc ";
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

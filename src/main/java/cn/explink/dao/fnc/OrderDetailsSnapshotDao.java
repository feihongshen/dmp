package cn.explink.dao.fnc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import cn.explink.dao.BranchDAO;
import cn.explink.domain.Branch;
import cn.explink.domain.CwbOrderSnapshot;
import cn.explink.enumutil.CwbOrderTypeIdEnum;
import cn.explink.enumutil.CwbStateEnum;
import cn.explink.enumutil.FlowOrderTypeEnum;
import cn.explink.util.StringUtil;

/**
 * 
 * OrderDetailsSnapshotDao
 * 
 * @author jinghui.pan
 *
 */
@Repository
public class OrderDetailsSnapshotDao {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	private BranchDAO branchDAO;
	
	private Logger logger = LoggerFactory
			.getLogger(OrderDetailsSnapshotDao.class);

	
	public List<CwbOrderSnapshot> listByFnrptlifecycleid(long fnrptlifecycleid, int page, int pageSize){
		
		final String sql = "select * from fn_order_details_snapshot where fnrptlifecycleid=? and state = 1 order by cwb  limit ?, ? ";
		
		return this.jdbcTemplate.query(sql, new CwbOrderSnapshotWithDescRowMapper(),fnrptlifecycleid,((page - 1) * pageSize), pageSize);
		
	}
	
	public int countByFnrptlifecycleid(long fnrptlifecycleid){
		
		final String sql = "select count(cwb) from fn_order_details_snapshot where fnrptlifecycleid=? and state = 1";
		
		return this.jdbcTemplate.queryForInt(sql,fnrptlifecycleid);
		
	}
	
	public List<CwbOrderSnapshot> listByFlowordertypeAndOrdertypeidAndCwbstate(int flowordertype, int cwbordertypeid, int cwbstate, int reportdate){
		
		return _listByFlowordertypeAndOrdertypeidAndCwbstate(flowordertype, cwbordertypeid, cwbstate, reportdate);
	}
	
	
	
	public List<CwbOrderSnapshot> listByFlowordertypeAndOrdertypeid(int flowordertype, int cwbordertypeid, int reportdate){
		
		return _listByFlowordertypeAndOrdertypeidAndCwbstate(flowordertype, cwbordertypeid, 0, reportdate);
		
	}
	
	public List<CwbOrderSnapshot> listByFlowordertypesAndOrdertypeid(String flowordertypes ,int cwbordertypeid, int reportdate){
		
		return this._listByFlowordertypesAndOrdertypeid(flowordertypes,cwbordertypeid,0,reportdate);
		
	}
	
	private List<CwbOrderSnapshot> _listByFlowordertypesAndOrdertypeid(String flowordertypes, int cwbordertypeid, int cwbstate, int reportdate){
		
		return this._listByFlowordertypeAndOrdertypeidAndCwbstate(0,flowordertypes,cwbordertypeid,cwbstate,reportdate);
		
	}
	
	private List<CwbOrderSnapshot> _listByFlowordertypeAndOrdertypeidAndCwbstate(int flowordertype, int cwbordertypeid, int cwbstate, int reportdate){
		
		return this._listByFlowordertypeAndOrdertypeidAndCwbstate(flowordertype,null,cwbordertypeid,cwbstate,reportdate);
		
	}
	
	private List<CwbOrderSnapshot> _listByFlowordertypeAndOrdertypeidAndCwbstate(int flowordertype, String flowordertypes, int cwbordertypeid, int cwbstate, int reportdate){
		
		StringBuilder sBuilder = new StringBuilder("select * from fn_order_details_snapshot where 1 = 1");
		
		if(flowordertype > 0){
			sBuilder.append(" and flowordertype=").append(flowordertype);
		}
		
		if(flowordertypes != null){
			sBuilder.append(" and flowordertype in(").append(flowordertypes).append(")");
		}
		
		if(cwbordertypeid > 0){
			sBuilder.append(" and cwbordertypeid=").append(cwbordertypeid);
		}
		
		if(cwbstate > 0){
			sBuilder.append(" and cwbstate=").append(cwbstate);
		}
		
		if(reportdate > 0){
			sBuilder.append(" and lifecycle_rpt_date=").append(reportdate);
		}
		
		sBuilder.append(" and state = 1");
		
		final String sql = sBuilder.toString();
		
		return this.jdbcTemplate.query(sql, new CwbOrderSnapshotRowMapper());
		
	}
	
	/**
	 * 站点在站货物 - “分站到货扫描”“到错货”“到错货处理”“分站领货”“反馈为分站滞留”“反馈为小件员滞留”“反馈为待中转”“反馈为拒收”“反馈为货物丢失”
	 * 
	 * @return
	 */
	public List<CwbOrderSnapshot> getListStockInStation(int cwbordertypeid){
		
		final String sql = "select * from fn_order_details_snapshot "
				+ " where flowordertype in(7,8,9,38) or (flowordertype in (35,36) and deliverystate in(6,10,4,8)) and cwbordertypeid = ? and state = 1 ";
		return this.jdbcTemplate.query(sql, new CwbOrderSnapshotRowMapper(),cwbordertypeid);
	}
	
	/**
	 * 站点在站货物 - “分站到货扫描”“到错货”“到错货处理”“分站领货”“反馈为分站滞留”“反馈为小件员滞留”“反馈为待中转”“反馈为拒收”“反馈为货物丢失”
	 * 
	 * @return
	 */
	public List<CwbOrderSnapshot> getListStockInStation(int cwbordertypeid, int reportdate){
		
		final String sql = "select * from fn_order_details_snapshot "
				+ " where flowordertype in(7,8,9,38) or (flowordertype in (35,36) and deliverystate in(6,10,4,8)) and cwbordertypeid = ? and lifecycle_rpt_date = ? and state = 1 ";
		return this.jdbcTemplate.query(sql, new CwbOrderSnapshotRowMapper(),cwbordertypeid,reportdate);
	}
	
	/**
	 * 站点未返代收货款 - 全部反馈为“配送成功”且结算状态为“未收款”
	 * 
	 * @return
	 */
	public List<CwbOrderSnapshot> getListFeeNotReturned(int cwbordertypeid, int reportdate){
		
		StringBuilder sqlBuilder = new StringBuilder( "select * from fn_order_details_snapshot")
		.append(" where deliverystate=1 and flowordertype in (35,36) ")
		.append(" and fnorgoffsetflag = 0 ")
		.append(" and cwbordertypeid = ? ")
		.append(" and receivablefee > 0  and lifecycle_rpt_date = ? and state =1");
		
		return this.jdbcTemplate.query(sqlBuilder.toString(), new CwbOrderSnapshotRowMapper(),cwbordertypeid,reportdate);
	}
	
	/**
	 * Quer list for “退供货商成功”操作状态但未做应收款账单核销的配送类型订单
	 * 
	 * @return
	 */
	public List<CwbOrderSnapshot> getListTuiKeHuNotReturned(int cwbordertypeid,int reportdate){
		
		StringBuilder sqlBuilder = new StringBuilder( "select * from fn_order_details_snapshot")
				.append(" where flowordertype = 34 ")
				.append(" and cwbordertypeid = ?  ")
				.append(" and fncustomerbillverifyflag < 1000 and lifecycle_rpt_date = ? and state = 1 ");
		
		return this.jdbcTemplate.query(sqlBuilder.toString(), new CwbOrderSnapshotRowMapper(),cwbordertypeid,reportdate);
	}
	
	/**
	 * 分拣出库未到站  - 只需要统计操作状态为出库扫描且上一站是退货库的订单
	 * 
	 * @return
	 */
	public List<CwbOrderSnapshot> getFengBoChukuWeiDaoZhan(int cwbordertypeid, int reportdate){
		StringBuilder sqlBuilder = new StringBuilder( "select * from fn_order_details_snapshot s")
		.append(" left join express_set_branch b on s.startbranchid = b.branchid ")
		.append(" where 1 = 1 ")
		.append(" and flowordertype = 6 ")
		.append(" and cwbstate = 1 and cwbordertypeid = ? ")
		.append(" and sitetype = 1 and lifecycle_rpt_date = ? and state =1 ");
		
		return this.jdbcTemplate.query(sqlBuilder.toString(), new CwbOrderSnapshotRowMapper(),cwbordertypeid,reportdate);
	}
	/**
	 * #退货再投未到站 -  退货再投未到站的只需要统计操作状态为出库扫描且上一站是退货库的订单
	 * 
	 * @return
	 */
	public List<CwbOrderSnapshot> getTuiHuoZaiTouWeiDaoZhan(int cwbordertypeid, int reportdate){
		StringBuilder sqlBuilder = new StringBuilder( "select * from fn_order_details_snapshot s")
				.append(" left join express_set_branch b on s.startbranchid = b.branchid ")
				.append(" where 1 = 1 ")
				.append(" and flowordertype = 6 ")
				.append(" and cwbstate = 1 and cwbordertypeid = ? ")
				.append(" and sitetype = 3 and lifecycle_rpt_date = ? and state =1 ");
		
		return this.jdbcTemplate.query(sqlBuilder.toString(), new CwbOrderSnapshotRowMapper(),cwbordertypeid,reportdate);
	}
	
	/**
	 * Quer list for 站点未返代收货款 - 全部反馈为“配送成功”且代收金额>0且结算状态为“未收款”的配送类型订单量之和
	 * 
	 * @return
	 */
	public List<CwbOrderSnapshot> getListFeeNotReturnedFromCwbDetailByPage(int cwbordertypeid, int page, int pageSize, long opscwbid){
		
		StringBuilder sqlBuilder = new StringBuilder(
				"select opscwbid,cwb,customerid,cwbordertypeid,cwbstate,flowordertype,emaildate,sendcarnum")
				.append(" ,backcarnum, receivablefee,paybackfee,shouldfare,currentbranchid, startbranchid")
				.append(" ,nextbranchid, deliverybranchid,deliverystate,state,infactfare,cwbstate, fnorgoffsetflag,fncustomerbillverifyflag")
				.append(" from express_ops_cwb_detail")
				.append(" where deliverystate=1 and flowordertype in (35,36) ")
				.append(" and fnorgoffsetflag = 0 and cwbordertypeid = ? and receivablefee > 0  ");
		
		if(opscwbid > 0){
			sqlBuilder.append(" and opscwbid < " + opscwbid );
		}
		
		sqlBuilder.append(" and state =1  order by opscwbid desc limit ?, ? ");
		
		return this.jdbcTemplate.query(sqlBuilder.toString(), new CwbOrderAdapteRowMapper(),cwbordertypeid,((page - 1) * pageSize), pageSize);
	}
	
	
	/**
	 * Quer list for “退供货商成功”操作状态但未做应收款账单核销的配送类型订单
	 * 
	 * @return
	 */
	public List<CwbOrderSnapshot> getListTuiKeHuWeiShouKuanFromCwbDetailByPage(int cwbordertypeid, int page, int pageSize, long opscwbid){
		
		StringBuilder sqlBuilder = new StringBuilder(
				"select opscwbid,cwb,customerid,cwbordertypeid,cwbstate,flowordertype,emaildate,sendcarnum")
				.append(" ,backcarnum, receivablefee,paybackfee,shouldfare,currentbranchid, startbranchid")
				.append(" ,nextbranchid, deliverybranchid,deliverystate,state,infactfare,cwbstate, fnorgoffsetflag,fncustomerbillverifyflag")
				.append(" from express_ops_cwb_detail")
				.append(" where flowordertype = 34 ")
				.append(" and cwbordertypeid = ?  and fncustomerbillverifyflag < 1000");
		
		if(opscwbid > 0){
			sqlBuilder.append(" and opscwbid < " + opscwbid );
		}
		
		sqlBuilder.append(" and state = 1 order by opscwbid desc limit ?, ? ");
		
		return this.jdbcTemplate.query(sqlBuilder.toString(), new CwbOrderAdapteRowMapper(),cwbordertypeid,((page - 1) * pageSize), pageSize);
	}
	
	/**
	 * count for 站点未返代收货款 - 全部反馈为“配送成功”且代收金额>0且结算状态为“未收款”的配送类型订单量之和
	 * 
	 * @return
	 */
	public long countFeeNotReturnedFromCwbDetail(int cwbordertypeid) {

		StringBuilder sqlBuilder = new StringBuilder(
				"select count(1)")
				.append(" from express_ops_cwb_detail")
				.append(" where deliverystate=1 and flowordertype in (35,36) ")
				.append(" and fnorgoffsetflag = 0 ")
				.append(" and cwbordertypeid = ? ")
				.append(" and receivablefee > 0 ").append(" and state =1");
		
		return this.jdbcTemplate.queryForLong(sqlBuilder.toString(), cwbordertypeid);
	}
	
	public long countTuiKeHuWeiShouKuanFromCwbDetail(int cwbordertypeid) {
		
		StringBuilder sqlBuilder = new StringBuilder( "select count(1)")
				.append(" from express_ops_cwb_detail")
				.append(" where flowordertype = 34 ")
				.append(" and cwbordertypeid = ?  and fncustomerbillverifyflag < 1000 and state = 1 ");
		
		return this.jdbcTemplate.queryForLong(sqlBuilder.toString(), cwbordertypeid);
	}
	
	
	
	/**
	 * @param lifecycleid
	 * @param cwb
	 *
	 * @author jinghui.pan@pjbest.com
	 */
	public void updateLifecycleReportIdByCwb(long lifecycleid, long cwb) {
		this.jdbcTemplate
				.update("update fn_order_details_snapshot set fnrptlifecycleid=? where cwb = ?",
						lifecycleid, cwb);

	}
	
	/**
	 * @param lifecycleid
	 * @param cwbs
	 *
	 * @author jinghui.pan@pjbest.com
	 */
	public void updateLifecycleReportIdByCwb(long lifecycleid, String cwbs) {
		this.jdbcTemplate
				.update("update fn_order_details_snapshot set fnrptlifecycleid=? where cwb in (" + cwbs +")",
						lifecycleid);

	}
	
	/**
	 * @param lifecycleid
	 * @param keys
	 *
	 * @author jinghui.pan@pjbest.com
	 */
	public void updateLifecycleReportIdByKeys(long lifecycleid, String keys) {
		this.jdbcTemplate
				.update("update fn_order_details_snapshot set fnrptlifecycleid=? where id in (" + keys +")",
						lifecycleid);

	}
	
	/**
	 * 更形订单快照表的state从1变成0
	 *
	 */
	public void updateStateToZero() {
		this.jdbcTemplate
		.update("update fn_order_details_snapshot set state = 0 where state = 1");
		
	}
	
	/**
	 * 软删除订单记录(state=0, fnrptlifecycleid = -1）
	 * 
	 * @param cwbordertypeid
	 * @param reportdate
	 *
	 */
	public void disableRowByCwbAndReportDate(String cwbs, int reportdate) {
		this.jdbcTemplate .update("update fn_order_details_snapshot set state = 0 and fnrptlifecycleid = -1 where cwb in (" + cwbs +") and lifecycle_rpt_date = ? and state = 1",reportdate);

	}
	
	/**
	 * 软删除订单记录(state=0, fnrptlifecycleid = -1）
	 * 
	 * @param cwbordertypeid
	 * @param reportdate
	 *
	 */
	public void disableRowByNotReturnedFeeByReportDate(int cwbordertypeid, int reportdate) {
		
		StringBuilder sqlBuilder = new StringBuilder( "update fn_order_details_snapshot set state = 0")
		.append(" where deliverystate=1 and flowordertype in (35,36) ")
		.append(" and fnorgoffsetflag = 0 ")
		.append(" and cwbordertypeid = ? ")
		.append(" and receivablefee > 0  and lifecycle_rpt_date = ? and state =1");
		
		this.jdbcTemplate.update(sqlBuilder.toString(),cwbordertypeid, reportdate);
	}
	
	/**
	 * 
	 * 更形订单快照表的state从1变成0
	 * 
	 * @param reportDate 
	 */
	public void updateStateToZeroByReportDate(int reportDate) {
		this.jdbcTemplate
				.update("update fn_order_details_snapshot set state = 0 where state = 1 and lifecycle_rpt_date = ?", reportDate);

	}
	

	public boolean isExistByCwb(String cwb) {
		
		int count = this.jdbcTemplate.queryForInt("select count(1) from fn_order_details_snapshot where cwb = ? and state = 1",cwb);
		
		return (count >= 1);
	}
	
	public boolean isExistByCwbAndReportdate(String cwb,int reportDate) {
		
		int count = this.jdbcTemplate.queryForInt("select count(1) from fn_order_details_snapshot where cwb = ? and lifecycle_rpt_date = ? and state = 1",cwb,reportDate);
		
		return (count >= 1);
	}
	
	
	public void batchInsertOrderDetailSnapshot(final List<CwbOrderSnapshot> cwbOrderList) {
		
		final int batchSize = cwbOrderList.size();
		
		final String insertsql = "insert into fn_order_details_snapshot"
				+ "( cwb,emaildate,receivablefee,paybackfee,customerid,cwbordertypeid,startbranchid,nextbranchid"
				+ ",sendcarnum,backcarnum,flowordertype,deliverybranchid,currentbranchid,cwbstate,deliverystate"
				+ ",shouldfare,infactfare,fnorgoffsetflag,lifecycle_rpt_date)"
				+ " values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		
		
		this.jdbcTemplate.batchUpdate(insertsql, new BatchPreparedStatementSetter() {
			
			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				
				CwbOrderSnapshot cwbOrder = cwbOrderList.get(i);
				
				ps.setString(1, cwbOrder .getCwb());
				ps.setString(2, cwbOrder.getEmaildate());
				ps.setBigDecimal(3, cwbOrder.getReceivablefee());
				ps.setBigDecimal(4, cwbOrder.getPaybackfee());
				ps.setLong(5, cwbOrder.getCustomerid());
				ps.setLong(6, cwbOrder.getCwbordertypeid());
				ps.setLong(7, cwbOrder.getStartbranchid());
				ps.setLong(8, cwbOrder.getNextbranchid());
				ps.setLong(9, cwbOrder.getSendcarnum());
				ps.setLong(10, cwbOrder.getBackcarnum());
				ps.setLong(11, cwbOrder.getFlowordertype());
				ps.setLong(12, cwbOrder.getDeliverybranchid());
				ps.setLong(13, cwbOrder.getCurrentbranchid());
				ps.setLong(14, cwbOrder.getCwbstate());
				ps.setLong(15, cwbOrder.getDeliverystate());
				ps.setBigDecimal(16, cwbOrder.getShouldfare());
				ps.setBigDecimal(17, cwbOrder.getInfactfare());
				ps.setLong(18, cwbOrder.getFnorgoffsetflag());
				ps.setInt(19, cwbOrder.getReportdate());
			}
			
			@Override
			public int getBatchSize() {
				return batchSize;
			}
		});
	}
	public void insertOrderDetailSnapshot(final CwbOrderSnapshot cwbOrder) {
		final String insertsql = "insert into fn_order_details_snapshot"
				+ "( cwb,emaildate,receivablefee,paybackfee,customerid,cwbordertypeid,startbranchid,nextbranchid"
				+ ",sendcarnum,backcarnum,flowordertype,deliverybranchid,currentbranchid,cwbstate,deliverystate"
				+ ",shouldfare,infactfare,fnorgoffsetflag,lifecycle_rpt_date)"
				+ " values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		KeyHolder keyHolder = new GeneratedKeyHolder();
		this.jdbcTemplate.update(new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(
					Connection connection) throws SQLException {
				PreparedStatement ps = connection.prepareStatement(insertsql,
						new String[] { "id" });
				ps.setString(1, cwbOrder.getCwb());
				ps.setString(2, cwbOrder.getEmaildate());
				ps.setBigDecimal(3, cwbOrder.getReceivablefee());
				ps.setBigDecimal(4, cwbOrder.getPaybackfee());
				ps.setLong(5, cwbOrder.getCustomerid());
				ps.setLong(6, cwbOrder.getCwbordertypeid());
				ps.setLong(7, cwbOrder.getStartbranchid());
				ps.setLong(8, cwbOrder.getNextbranchid());
				ps.setLong(9, cwbOrder.getSendcarnum());
				ps.setLong(10, cwbOrder.getBackcarnum());
				ps.setLong(11, cwbOrder.getFlowordertype());
				ps.setLong(12, cwbOrder.getDeliverybranchid());
				ps.setLong(13, cwbOrder.getCurrentbranchid());
				ps.setLong(14, cwbOrder.getCwbstate());
				ps.setLong(15, cwbOrder.getDeliverystate());
				ps.setBigDecimal(16, cwbOrder.getShouldfare());
				ps.setBigDecimal(17, cwbOrder.getInfactfare());
				ps.setLong(18, cwbOrder.getFnorgoffsetflag());
				ps.setInt(19, cwbOrder.getReportdate());
				return ps;
			}
		}, keyHolder);
	}

	private String getBranchById(long branchid){
		String branchName = "";
		Branch branch = branchDAO.getBranchById(branchid);
		if(branch != null){
			branchName = branch.getBranchname();
		}
		return branchName;
	}
	
	private class CwbOrderSnapshotWithDescRowMapper extends CwbOrderSnapshotRowMapper implements RowMapper<CwbOrderSnapshot>{

		@Override
		public CwbOrderSnapshot mapRow(ResultSet rs, int rowNum)
				throws SQLException {
			CwbOrderSnapshot orderSnapshot = super.mapRow(rs, rowNum);
			
			orderSnapshot.setCwbordertypeDesc(CwbOrderTypeIdEnum.getTextByValue(orderSnapshot.getCwbordertypeid()));
			orderSnapshot.setFlowordertypeDesc(FlowOrderTypeEnum.getText(orderSnapshot.getFlowordertype()).getText());
			orderSnapshot.setCwbstateDesc(CwbStateEnum.getByValue(orderSnapshot.getCwbstate()).getText());
			orderSnapshot.setCurrentbranchDesc(getBranchById(orderSnapshot.getCurrentbranchid()));
			orderSnapshot.setNextbranchDesc(getBranchById(orderSnapshot.getNextbranchid()));
			orderSnapshot.setStartbranchDesc(getBranchById(orderSnapshot.getStartbranchid()));
			return orderSnapshot;
		}
		
	}
	
	private class CwbOrderSnapshotRowMapper extends CwbOrderCommonRowMapper implements RowMapper<CwbOrderSnapshot> {
		@Override
		public CwbOrderSnapshot mapRow(ResultSet rs, int rowNum) throws SQLException {
			CwbOrderSnapshot orderSnapshot = super.mapRow(rs, rowNum);
			
			orderSnapshot.setId(rs.getLong("id"));
			orderSnapshot.setFnrptlifecycleid(rs.getLong("fnrptlifecycleid"));
			orderSnapshot.setReportdate(rs.getInt("lifecycle_rpt_date"));
			
			
			return orderSnapshot;
		}
	}
	
	private class CwbOrderAdapteRowMapper extends CwbOrderCommonRowMapper implements RowMapper<CwbOrderSnapshot> {
		@Override
		public CwbOrderSnapshot mapRow(ResultSet rs, int rowNum) throws SQLException {
			CwbOrderSnapshot orderSnapshot = super.mapRow(rs, rowNum);
			
			orderSnapshot.setOpscwbid(rs.getLong("opscwbid"));
			
			return orderSnapshot;
		}
	}
	
	
	private class CwbOrderCommonRowMapper implements RowMapper<CwbOrderSnapshot> {
		@Override
		public CwbOrderSnapshot mapRow(ResultSet rs, int rowNum) throws SQLException {
			CwbOrderSnapshot orderSnapshot = new CwbOrderSnapshot();
			
			orderSnapshot.setCwb(StringUtil.nullConvertToEmptyString(rs.getString("cwb")));
			orderSnapshot.setEmaildate(rs.getString("emaildate"));
			orderSnapshot.setReceivablefee(rs.getBigDecimal("receivablefee"));
			orderSnapshot.setPaybackfee(rs.getBigDecimal("paybackfee"));
			orderSnapshot.setCustomerid(rs.getLong("customerid"));
			orderSnapshot.setCwbordertypeid(rs.getInt("cwbordertypeid"));
			orderSnapshot.setStartbranchid(rs.getLong("startbranchid"));
			orderSnapshot.setNextbranchid(rs.getLong("nextbranchid"));
			orderSnapshot.setSendcarnum(rs.getLong("sendcarnum"));
			orderSnapshot.setBackcarnum(rs.getLong("backcarnum"));
			orderSnapshot.setFlowordertype(rs.getLong("flowordertype"));
			orderSnapshot.setState(rs.getLong("state"));
			orderSnapshot.setDeliverybranchid(rs.getLong("deliverybranchid"));
			orderSnapshot.setCwbstate(rs.getLong("cwbstate"));
			orderSnapshot.setCurrentbranchid(rs.getLong("currentbranchid"));
			orderSnapshot.setDeliverystate(rs.getInt("deliverystate"));
			orderSnapshot.setShouldfare(rs.getBigDecimal("shouldfare"));
			orderSnapshot.setInfactfare(rs.getBigDecimal("infactfare"));
			orderSnapshot.setFnorgoffsetflag(rs.getInt("fnorgoffsetflag"));
			orderSnapshot.setFncustomerbillverifyflag(rs.getInt("fncustomerbillverifyflag"));
			
			return orderSnapshot;
		}
	}


	
}

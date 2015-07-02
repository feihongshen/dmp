package cn.explink.dao;

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

import cn.explink.domain.OrderBackCheck;
import cn.explink.util.Page;
import cn.explink.util.StringUtil;

@Component
public class OrderBackCheckDAO {
	@Autowired
	private JdbcTemplate jdbcTemplate;

	private final class OrderBackCheckRowMapper implements RowMapper<OrderBackCheck> {
		@Override
		public OrderBackCheck mapRow(ResultSet rs, int rowNum) throws SQLException {
			OrderBackCheck orderBackCheck = new OrderBackCheck();
			orderBackCheck.setId(rs.getLong("id"));
			orderBackCheck.setCheckstate(rs.getLong("checkstate"));
			orderBackCheck.setCwb(rs.getString("cwb"));
			orderBackCheck.setCustomerid(rs.getLong("customerid"));
			orderBackCheck.setCwbordertypeid(rs.getInt("cwbordertypeid"));
			orderBackCheck.setFlowordertype(rs.getLong("flowordertype"));
			orderBackCheck.setCwbstate(rs.getLong("cwbstate"));
			orderBackCheck.setConsigneename(rs.getString("consigneename"));
			orderBackCheck.setConsigneephone(rs.getString("consigneephone"));
			orderBackCheck.setConsigneeaddress(rs.getString("consigneeaddress"));
			orderBackCheck.setBackreason(rs.getString("backreason"));
			orderBackCheck.setUserid(rs.getLong("userid"));
			orderBackCheck.setCreatetime(rs.getString("createtime"));
			orderBackCheck.setCheckuser(rs.getLong("checkuser"));
			orderBackCheck.setCheckcreatetime(rs.getString("checkcreatetime"));
			orderBackCheck.setBranchid(rs.getLong("branchid"));
			orderBackCheck.setCheckresult(rs.getLong("checkresult"));
			orderBackCheck.setAuditname(StringUtil.nullConvertToEmptyString(rs.getString("auditname")));//审核人
			orderBackCheck.setAudittime(StringUtil.nullConvertToEmptyString(rs.getString("audittime")));//审核时间
			return orderBackCheck;
		}
	}
	
	private final class CwbMapper implements RowMapper<String> {
		@Override
		public String mapRow(ResultSet rs, int rowNum) throws SQLException {
			return rs.getString("cwb");
		}
	}

	public void createOrderBackCheck(final OrderBackCheck orderBackCheck) {
		KeyHolder key = new GeneratedKeyHolder();
		jdbcTemplate.update(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(java.sql.Connection con) throws SQLException {
				PreparedStatement ps = null;
				ps = con.prepareStatement("insert into ops_order_back_check (checkstate,cwb,customerid,cwbordertypeid,flowordertype,"
						+ "cwbstate,consigneename,consigneephone,consigneeaddress,backreason,userid,createtime,branchid) " + "values (?,?,?,?,?,?,?,?,?,?,?,?,?)", new String[] { "recordid" });
				ps.setLong(1, orderBackCheck.getCheckstate());
				ps.setString(2, orderBackCheck.getCwb());
				ps.setLong(3, orderBackCheck.getCustomerid());
				ps.setInt(4, orderBackCheck.getCwbordertypeid());
				ps.setLong(5, orderBackCheck.getFlowordertype());
				ps.setLong(6, orderBackCheck.getCwbstate());
				ps.setString(7, orderBackCheck.getConsigneename());
				ps.setString(8, orderBackCheck.getConsigneephone());
				ps.setString(9, orderBackCheck.getConsigneeaddress());
				ps.setString(10, orderBackCheck.getBackreason());
				ps.setLong(11, orderBackCheck.getUserid());
				ps.setString(12, orderBackCheck.getCreatetime());
				ps.setLong(13, orderBackCheck.getBranchid());
				return ps;
			}
		}, key);
		orderBackCheck.setId(key.getKey().longValue());
		// return key.getKey().longValue();
	}

	public OrderBackCheck getOrderBackCheckById(long id) {
		String sql = "select * from ops_order_back_check where id = ? ";
		try {
			return jdbcTemplate.queryForObject(sql, new OrderBackCheckRowMapper(), id);
		} catch (Exception e) {
			return null;
		}
	}

	public void deleteOrderBackCheckByCwb(String cwb) {
		jdbcTemplate.update("delete from ops_order_back_check where cwb = ?", cwb);
	}

	public OrderBackCheck getOrderBackCheckByCwb(String cwb) {
		String sql = "select * from ops_order_back_check where cwb = ? and isstastics = 0  ";
		try {
			return jdbcTemplate.queryForObject(sql, new OrderBackCheckRowMapper(), cwb);
		} catch (Exception e) {
			return null;
		}
	}
	
	public OrderBackCheck getOrderBackCheckByCheckstate(String cwb) {
		String sql = "select * from ops_order_back_check where checkstate=1  and cwb = ? ";
		try {
			return jdbcTemplate.queryForObject(sql, new OrderBackCheckRowMapper(), cwb);
		} catch (Exception e) {
			return null;
		}
	}

	public OrderBackCheck getOrderBackCheckByCwbAndBranch(String cwb, String branchid) {
		String sql = "select * from ops_order_back_check where cwb = ? and branchid in(" + branchid + ") and checkstate=0 ";
		try {
			return jdbcTemplate.queryForObject(sql, new OrderBackCheckRowMapper(), cwb);
		} catch (Exception e) {
			return null;
		}
	}
	
	//全部
	public List<OrderBackCheck> getOrderBackChecksByCwbsAndBranch(String cwbs, String branchid) {
		String sql = "select * from ops_order_back_check where cwb in("+cwbs+") and branchid in(" + branchid + ") and checkstate=0 ";
		try {
			return jdbcTemplate.query(sql, new OrderBackCheckRowMapper());
		} catch (Exception e) {
			return null;
		}
	}
	//分页
	public List<OrderBackCheck> getOrderBackCheckByCwbsAndBranch(long page,String cwbs, String branchid) {
		String sql = "select * from ops_order_back_check where cwb in("+cwbs+") and branchid in(" + branchid + ") and checkstate=0 ";
		try {
			sql += " limit " + (page - 1) * Page.ONE_PAGE_NUMBER + " ," + Page.ONE_PAGE_NUMBER;
			return jdbcTemplate.query(sql, new OrderBackCheckRowMapper());
		} catch (Exception e) {
			return null;
		}
	}
	//审核为确认退货
	public void updateOrderBackCheck1(long checkresult,long id,String auditname,String audittime) {
		String sql = "UPDATE ops_order_back_check SET checkstate=2,checkresult=?,auditname=?,audittime=? where id =?";
		this.jdbcTemplate.update(sql, checkresult, auditname,audittime,id);
	}
	
	//审核为站点滞留
	public void updateOrderBackCheck2(long checkresult,String cwb,String auditname,String audittime) {
		String sql = "UPDATE ops_order_back_check SET checkstate=2,checkresult=?,auditname=?,audittime=? where cwb =?";
		this.jdbcTemplate.update(sql, checkresult, auditname, audittime, cwb);
	}

	
	public List<OrderBackCheck> getOrderBackCheckList(String id) {
		String sql = "select * from ops_order_back_check where id in (" + id + ")";
		try {
			return jdbcTemplate.query(sql, new OrderBackCheckRowMapper());
		} catch (Exception e) {
			return null;
		}
	}

	public List<OrderBackCheck> getOrderBackCheckListByBranch(String branchid) {
		String sql = "select * from ops_order_back_check where branchid in (" + branchid + ") and checkstate=0";
		try {
			return jdbcTemplate.query(sql, new OrderBackCheckRowMapper());
		} catch (Exception e) {
			return null;
		}
	}
	
	
	//退货出站根据条件查询(总量)
	public List<OrderBackCheck> getOrderBackChecks(String cwbs,int cwbtypeid,long customerid,long branchid,long checkstate,long checkresult,String begindate,String enddate) {
		String sql = "SELECT * from ops_order_back_check  ";
		if(!cwbs.equals("")){
			sql += " where cwb in("+cwbs+")";
		}else{
			sql += " where 1=1 ";
			StringBuffer sb = new StringBuffer();
			if (branchid>0) {
				sb.append(" and branchid="+branchid);
			}
			if(customerid>0){
				sb.append(" and customerid="+customerid);
			}
			if(cwbtypeid>0){
				sb.append(" and cwbordertypeid="+cwbtypeid);
			}
			if(checkstate>=0){
				sb.append(" and checkstate="+checkstate);
			}
			if(checkresult>0){
				sb.append(" and checkresult="+checkresult);
			}
			if(!"".equals(begindate)){
				sb.append(" and createtime>='"+begindate+"'");
			}
			if(!"".equals(enddate)){
				sb.append(" and createtime<='"+enddate+"'");
			}
			sql +=sb;
		}
		return this.jdbcTemplate.query(sql,new OrderBackCheckRowMapper());
	}
	//退货出站根据条件查询(分页)
	public List<OrderBackCheck> getOrderBackChecksForpage(long page,String cwbs,int cwbtypeid,long customerid,long branchid,long checkstate,long checkresult,String begindate,String enddate) {
		String sql = "SELECT * from ops_order_back_check where checkstate="+checkstate;
		if(!cwbs.equals("")){
			sql += " and cwb in("+cwbs+")";
		}else{
			StringBuffer sb = new StringBuffer();
			if (branchid>0) {
				sb.append(" and branchid="+branchid);
			}
			if(customerid>0){
				sb.append(" and customerid="+customerid);
			}
			if(cwbtypeid>0){
				sb.append(" and cwbordertypeid="+cwbtypeid);
			}
			if(checkresult>0){
				sb.append(" and checkresult="+checkresult);
			}
			if(!"".equals(begindate)){
				sb.append(" and createtime >= '"+begindate+"'");
			}
			if(!"".equals(enddate)){
				sb.append(" and createtime <= '"+enddate+"'");
			}
			sql +=sb;
		}
		if(page!=-9){
			sql += " limit " + (page - 1) * Page.ONE_PAGE_NUMBER + " ," + Page.ONE_PAGE_NUMBER;

		}
		return this.jdbcTemplate.query(sql,new OrderBackCheckRowMapper());
	}
	
	public long getOrderBackChecksCount(String cwbs,int cwbtypeid,long customerid,long branchid,long checkstate,long checkresult,String begindate,String enddate) {
		String sql = "SELECT count(1) from ops_order_back_check where checkstate="+checkstate;
		if(!cwbs.equals("")){
			sql += " and cwb in("+cwbs+")";
		}else{
			StringBuffer sb = new StringBuffer();
			if (branchid>0) {
				sb.append(" and branchid="+branchid);
			}
			if(customerid>0){
				sb.append(" and customerid="+customerid);
			}
			if(cwbtypeid>0){
				sb.append(" and cwbordertypeid="+cwbtypeid);
			}
			if(checkresult>0){
				sb.append(" and checkresult="+checkresult);
			}
			if(!"".equals(begindate)){
				sb.append(" and createtime >='"+begindate+"'");
			}
			if(!"".equals(enddate)){
				sb.append(" and createtime <='"+enddate+"'");
			}
			sql +=sb;
		}
		return this.jdbcTemplate.queryForLong(sql);
	}
	
	public List<OrderBackCheck> getOrderBackCheckByIds(String ids){
		String sql = "select * from ops_order_back_check where id in("+ids+")";
		return jdbcTemplate.query(sql, new OrderBackCheckRowMapper());
	}
	
	
		
		/**
		 * 查询退货申请 不通过记录，只统计=0的数据
		 * @param customerid
		 * @param branchid
		 * @param checkresult
		 * @param begindate
		 * @param enddate
		 * @return
		 */
		public List<String> getOrderBackChecksCwbs(long branchid,String begindate,String enddate) {
			String sql = "SELECT * from ops_order_back_check where checkresult=2 and isstastics=0 ";
			StringBuffer sb = new StringBuffer();
			if (branchid>0) {
				sb.append(" and branchid="+branchid);
			}
			if(!"".equals(begindate)){
				sb.append(" and createtime>="+"'"+begindate+"'");
			}
			if(!"".equals(enddate)){
				sb.append(" and createtime<"+"'"+enddate+"'");
			}
			sql +=sb;
			return this.jdbcTemplate.query(sql,new CwbMapper());
		}
	
		/**
		 * 领货之后修改此状态为已统计
		 * @param cwb
		 */
		public void updateStastics(String cwb) {
			String sql = "update ops_order_back_check set isstastics=1 where cwb=? and isstastics=0 ";
			jdbcTemplate.update(sql,  cwb);
		}
		
	
}

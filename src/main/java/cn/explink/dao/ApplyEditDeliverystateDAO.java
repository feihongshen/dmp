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

import cn.explink.domain.ApplyEditDeliverystate;
import cn.explink.util.Page;

@Component
public class ApplyEditDeliverystateDAO {

	private final class ApplyEditDeliverystateRowMapper implements RowMapper<ApplyEditDeliverystate> {
		@Override
		public ApplyEditDeliverystate mapRow(ResultSet rs, int rowNum) throws SQLException {
			ApplyEditDeliverystate applyEditDeliverystate = new ApplyEditDeliverystate();

			applyEditDeliverystate.setApplybranchid(rs.getLong("applybranchid"));
			applyEditDeliverystate.setApplytime(rs.getString("applytime"));
			applyEditDeliverystate.setApplyuserid(rs.getLong("applyuserid"));
			applyEditDeliverystate.setCwb(rs.getString("cwb"));
			applyEditDeliverystate.setCwbordertypeid(rs.getInt("cwbordertypeid"));
			applyEditDeliverystate.setDeliverid(rs.getLong("deliverid"));
			applyEditDeliverystate.setDeliverpodtime(rs.getString("deliverpodtime"));
			applyEditDeliverystate.setDeliverystateid(rs.getLong("deliverystateid"));
			applyEditDeliverystate.setEditdetail(rs.getString("editdetail"));
			applyEditDeliverystate.setEditnopos(rs.getBigDecimal("editnopos"));
			applyEditDeliverystate.setEditnowdeliverystate(rs.getLong("editnowdeliverystate"));
			applyEditDeliverystate.setEditpos(rs.getBigDecimal("editpos"));
			applyEditDeliverystate.setEditreason(rs.getString("editreason"));
			applyEditDeliverystate.setEdittime(rs.getString("edittime"));
			applyEditDeliverystate.setEdituserid(rs.getLong("edituserid"));
			applyEditDeliverystate.setId(rs.getLong("id"));
			applyEditDeliverystate.setIsauditpayup(rs.getLong("isauditpayup"));
			applyEditDeliverystate.setIshandle(rs.getLong("ishandle"));
			applyEditDeliverystate.setIssendcustomer(rs.getLong("issendcustomer"));
			applyEditDeliverystate.setNopos(rs.getBigDecimal("nopos"));
			applyEditDeliverystate.setNowdeliverystate(rs.getInt("nowdeliverystate"));
			applyEditDeliverystate.setOpscwbid(rs.getLong("opscwbid"));
			applyEditDeliverystate.setPayupid(rs.getLong("payupid"));
			applyEditDeliverystate.setPos(rs.getBigDecimal("pos"));
			applyEditDeliverystate.setState(rs.getLong("state"));
			
			applyEditDeliverystate.setCwbstate(rs.getLong("cwbstate"));
			applyEditDeliverystate.setShenhestate(rs.getInt("shenhestate"));
			applyEditDeliverystate.setReasonid(rs.getInt("reasonid"));
			
			return applyEditDeliverystate;
		}
	}

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public long creApplyEditDeliverystate(final ApplyEditDeliverystate aeds) {
		KeyHolder key = new GeneratedKeyHolder();
		jdbcTemplate.update(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(java.sql.Connection con) throws SQLException {
				PreparedStatement ps = null;
				ps = con.prepareStatement("insert into express_ops_applyeditdeliverystate (deliverystateid,opscwbid,cwb,"
						+ "cwbordertypeid,nowdeliverystate,nopos,pos,deliverid,applyuserid,applybranchid,applytime,cwbstate,shenhestate,deliverpodtime) " + "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?)", new String[] { "id" });
				ps.setLong(1, aeds.getDeliverystateid());
				ps.setLong(2, aeds.getOpscwbid());
				ps.setString(3, aeds.getCwb());
				ps.setLong(4, aeds.getCwbordertypeid());
				ps.setLong(5, aeds.getNowdeliverystate());
				ps.setBigDecimal(6, aeds.getNopos());
				ps.setBigDecimal(7, aeds.getPos());
				ps.setLong(8, aeds.getDeliverid());
				ps.setLong(9, aeds.getApplyuserid());
				ps.setLong(10, aeds.getApplybranchid());
				ps.setString(11, aeds.getApplytime());
				
				ps.setLong(12, aeds.getCwbstate());
				ps.setInt(13, aeds.getShenhestate());
				ps.setString(14, aeds.getDeliverpodtime());//反馈时间
				return ps;
			}
		}, key);
		aeds.setId(key.getKey().longValue());
		return key.getKey().longValue();
	}
	//在多次申请对一条数据的修改后产生新的数据
	public long creApplyEditDeliverystateNew(final ApplyEditDeliverystate aeds) {
		KeyHolder key = new GeneratedKeyHolder();
		jdbcTemplate.update(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(java.sql.Connection con) throws SQLException {
				PreparedStatement ps = null;
				ps = con.prepareStatement("insert into express_ops_applyeditdeliverystate (deliverystateid,opscwbid,cwb,"
						+ "cwbordertypeid,nowdeliverystate,nopos,pos,deliverid,applyuserid,applybranchid,applytime,editreason,state,cwbstate,shenhestate) " + "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)", new String[] { "id" });
				ps.setLong(1, aeds.getDeliverystateid());
				ps.setLong(2, aeds.getOpscwbid());
				ps.setString(3, aeds.getCwb());
				ps.setLong(4, aeds.getCwbordertypeid());
				ps.setLong(5, aeds.getNowdeliverystate());
				ps.setBigDecimal(6, aeds.getNopos());
				ps.setBigDecimal(7, aeds.getPos());
				ps.setLong(8, aeds.getDeliverid());
				ps.setLong(9, aeds.getApplyuserid());
				ps.setLong(10, aeds.getApplybranchid());
				ps.setString(11, aeds.getApplytime());
				//ps.setLong(12, aeds.getEditnowdeliverystate());
				ps.setString(12, aeds.getEditreason());
				ps.setLong(13, aeds.getState());
				
				ps.setLong(14, aeds.getCwbstate());
				ps.setInt(15, aeds.getShenhestate());
				return ps;
			}
		}, key);
		aeds.setId(key.getKey().longValue());
		return key.getKey().longValue();
	}

	public void saveApplyEditDeliverystateById(long id, long editnowdeliverystate,int reasonid, String editreason) {
		String sql = "update express_ops_applyeditdeliverystate set state=1, editnowdeliverystate=?,reasonid=?,editreason=? where id=?";
		jdbcTemplate.update(sql, editnowdeliverystate,reasonid, editreason, id);
	}

	public ApplyEditDeliverystate getApplyEditDeliverystateById(long id) {
		String sql = "SELECT * from express_ops_applyeditdeliverystate where id=?";
		return jdbcTemplate.queryForObject(sql, new ApplyEditDeliverystateRowMapper(), id);
	}

	public List<ApplyEditDeliverystate> getApplyEditDeliverystateByCwbsPage(long page, String cwbs, long ishandle) {
		String sql = "SELECT * from express_ops_applyeditdeliverystate where cwb in(" + cwbs + ") and ishandle=? ";
		sql += " limit " + (page - 1) * Page.ONE_PAGE_NUMBER + " ," + Page.ONE_PAGE_NUMBER;
		return jdbcTemplate.query(sql, new ApplyEditDeliverystateRowMapper(), ishandle);
	}

	public List<ApplyEditDeliverystate> getApplyEditDeliverystateByCwb(String cwb, long ishandle) {
		String sql = "SELECT * from express_ops_applyeditdeliverystate where cwb =? and ishandle=? ";
		return jdbcTemplate.query(sql, new ApplyEditDeliverystateRowMapper(), cwb, ishandle);
	}
	//查询未进行申请的数据
	public List<ApplyEditDeliverystate> getHavenotApplyEditDeliverystateByCwb(String cwb, long ishandle) {
		String sql = "SELECT * from express_ops_applyeditdeliverystate where cwb =? and ishandle=? and state=0";
		return jdbcTemplate.query(sql, new ApplyEditDeliverystateRowMapper(), cwb, ishandle);
	}
	
	//拿到已经修改过的申请数据
	public List<ApplyEditDeliverystate> getAppliedEditDeliverystateByCwb(String cwb) {
		String sql = "SELECT * from express_ops_applyeditdeliverystate where cwb =? and state=1 ";
		return jdbcTemplate.query(sql, new ApplyEditDeliverystateRowMapper(), cwb);
	}

	public List<ApplyEditDeliverystate> getApplyEditDeliverystateByWherePage(long page, String begindate, String enddate, long applybranchid, long ishandle, String cwb) {
		String sql = "SELECT * from express_ops_applyeditdeliverystate where state=1 ";
		if (cwb.length() > 0) {
			StringBuffer w = new StringBuffer();
			w.append(" and cwb = '" + cwb + "'");
			sql += w.toString();
		} else if (begindate.length() > 0 || enddate.length() > 0 || applybranchid > 0 || ishandle > -1) {

			StringBuffer w = new StringBuffer();

			if (begindate.length() > 0) {
				w.append(" and applytime >= '" + begindate + "' ");
			}
			if (enddate.length() > 0) {
				w.append(" and applytime <= '" + enddate + "' ");
			}
			if (applybranchid > 0) {
				w.append(" and applybranchid = " + applybranchid);
			}
			if (ishandle > -1) {
				w.append(" and ishandle = " + ishandle);
			}
			sql += w.toString();
		}
		sql += " limit " + (page - 1) * Page.ONE_PAGE_NUMBER + " ," + Page.ONE_PAGE_NUMBER;
		return jdbcTemplate.query(sql, new ApplyEditDeliverystateRowMapper());
	}

	
	
	
	public long getApplyEditDeliverystateByWhereCount(String begindate, String enddate, long applybranchid, long ishandle, String cwb) {
		String sql = "SELECT count(1) from express_ops_applyeditdeliverystate where state=1 ";
		if (cwb.length() > 0) {
			StringBuffer w = new StringBuffer();
			w.append(" and cwb = '" + cwb + "'");
			sql += w.toString();
		} else if (begindate.length() > 0 || enddate.length() > 0 || applybranchid > 0 || ishandle > -1) {
			StringBuffer w = new StringBuffer();

			if (begindate.length() > 0) {
				w.append(" and applytime >= '" + begindate + "' ");
			}
			if (enddate.length() > 0) {
				w.append(" and applytime <= '" + enddate + "' ");
			}
			if (applybranchid > 0) {
				w.append(" and applybranchid = " + applybranchid);
			}
			if (ishandle > -1) {
				w.append(" and ishandle = " + ishandle);
			}
			sql += w.toString();

		}
		return jdbcTemplate.queryForLong(sql);
	}

	public void agreeSaveApplyEditDeliverystateById(long id, BigDecimal editnopos, BigDecimal editpos, long edituserid, String edittime, String editdetail) {
		String sql = "update express_ops_applyeditdeliverystate set editnopos=?,editpos=?,edituserid=?,edittime=?,editdetail=?,ishandle=1 where id=?";
		jdbcTemplate.update(sql, editnopos, editpos, edituserid, edittime, editdetail, id);
	}

	public void updateState(long issendcustomer, String ids) {
		String sql = "update express_ops_applyeditdeliverystate set issendcustomer=? where deliverystateid in(" + ids + ")";
		jdbcTemplate.update(sql, issendcustomer);
	}
	//根据批量订单查询
	public List<ApplyEditDeliverystate> getApplyEditBycwbs(String cwbs){
		String sql = "select * from express_ops_applyeditdeliverystate where cwb in("+cwbs+")";
		return jdbcTemplate.query(sql, new ApplyEditDeliverystateRowMapper(),cwbs );
	}
	
	//根据条件查询获取信息-分页
	public List<ApplyEditDeliverystate> getAppliedEditDeliverystateByOthers(long page,String cwbs,int cwbordertypeid,long cwbresult,long shenhestate,long cwbstate,long feedbackbranch,String feedbackStartDate,String feedbackEndDate) {
		String sql = "select * from express_ops_applyeditdeliverystate where state=1";
		
			StringBuffer sb = new StringBuffer();
			if(!cwbs.equals("")){
				sql += " and cwb in("+cwbs+")";
			}
			if(cwbordertypeid>0){
				sb.append(" and cwbordertypeid="+cwbordertypeid);
			}
			if(cwbresult>0){
				sb.append(" and nowdeliverystate="+cwbresult);
			}
			if(shenhestate>0){
				sb.append(" and shenhestate="+shenhestate);
			}
			if(cwbstate>0){
				sb.append(" and cwbstate="+cwbstate);
			}
			if(feedbackbranch>0){
				sb.append(" and applybranchid="+feedbackbranch);
			}
			if(feedbackStartDate!=null&&feedbackStartDate.length()>0){
				sb.append(" and deliverpodtime>='"+feedbackStartDate+"'");
			}
			if(feedbackEndDate!=null&&feedbackEndDate.length()>0){
				sb.append(" and deliverpodtime<='"+feedbackEndDate+"'");
			}
			sql += sb;
		
		sql += " limit " +(page-1) * Page.ONE_PAGE_NUMBER + ","+Page.ONE_PAGE_NUMBER ;
		return jdbcTemplate.query(sql, new ApplyEditDeliverystateRowMapper());
	}
	//根据条件查询获取信息-分页
	public long getAppliedEditDeliverystateCount(String cwbs,int cwbordertypeid,long cwbresult,long shenhestate,long cwbstate,long feedbackbranch,String feedbackStartDate,String feedbackEndDate) {
		String sql = "select count(1) from express_ops_applyeditdeliverystate where state=1";
	
			StringBuffer sb = new StringBuffer();
			if(!cwbs.equals("")){
				sql += " and cwb in("+cwbs+")";
			}
			if(cwbordertypeid>0){
				sb.append(" and cwbordertypeid="+cwbordertypeid);
			}
			if(cwbresult>0){
				sb.append(" and nowdeliverystate="+cwbresult);
			}
			if(shenhestate>0){
				sb.append(" and shenhestate="+shenhestate);
			}
			if(cwbstate>0){
				sb.append(" and cwbstate="+cwbstate);
			}
			if(feedbackbranch>0){
				sb.append(" and applybranchid="+feedbackbranch);
			}
			if(feedbackStartDate!=null&&feedbackStartDate.length()>0){
				sb.append(" and deliverpodtime>='"+feedbackStartDate+"'");
			}
			if(feedbackEndDate!=null&&feedbackEndDate.length()>0){
				sb.append(" and deliverpodtime<='"+feedbackEndDate+"'");
			}
			sql += sb;
		return jdbcTemplate.queryForLong(sql);
	}
	//根据条件查询获取信息-
	public List<ApplyEditDeliverystate> getAppliedEditDeliverystate(String cwbs,int cwbordertypeid,long cwbresult,long shenhestate,long cwbstate,long feedbackbranch) {
		String sql = "select * from express_ops_applyeditdeliverystate where state=1";
		if(!cwbs.equals("")){
			sql += " and cwb in("+cwbs+")";
		}else{
			StringBuffer sb = new StringBuffer();
			if(cwbordertypeid>0){
				sb.append(" and cwbordertypeid="+cwbordertypeid);
			}
			if(cwbresult>0){
				sb.append(" and nowdeliverystate="+cwbresult);
			}
			if(shenhestate>0){
				sb.append(" and shenhestate="+shenhestate);
			}
			if(cwbstate>0){
				sb.append(" and cwbstate="+cwbstate);
			}
			if(feedbackbranch>0){
				sb.append(" and applybranchid="+feedbackbranch);
			}
			sql += sb;
		}
		return jdbcTemplate.query(sql, new ApplyEditDeliverystateRowMapper());
	}
	//审核为通过
	public void updateShenheStatePass(String cwb,long edituserid,String edittime) {
		// TODO Auto-generated method stub
		String sql = "update express_ops_applyeditdeliverystate set shenhestate=3,edituserid=?,edittime=?,ishandle=1 where cwb=? order by id desc limit 1";
		this.jdbcTemplate.update(sql,edituserid,edittime,cwb);
	}
	//审核为不通过
	public void updateShenheStateNoPass(String cwb,long edituserid,String edittime) {
		// TODO Auto-generated method stub
		String sql = "update express_ops_applyeditdeliverystate set shenhestate=2,edituserid=?,edittime=?,ishandle=1 where cwb=? order by id desc limit 1";
		this.jdbcTemplate.update(sql,edituserid,edittime,cwb);
	}
	//根据订单查询单条信息
	public ApplyEditDeliverystate getApplyED(String cwb){
		try{
			String sql = "select * from express_ops_applyeditdeliverystate where cwb=? and ishandle=0 order by id desc limit 1";
			return jdbcTemplate.queryForObject(sql, new ApplyEditDeliverystateRowMapper(),cwb);
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
		
	}
	
	public List<ApplyEditDeliverystate> getApplyEditDeliverystates(String cwbs, long ishandle) {
		String sql = "SELECT * from express_ops_applyeditdeliverystate where cwb in(" + cwbs + ") and ishandle=? and state=1";
		return jdbcTemplate.query(sql, new ApplyEditDeliverystateRowMapper(), ishandle);
	}
	
	//新加，导出已提交订单
	public List<ApplyEditDeliverystate> getApplyEditDeliverystatess(String cwbs) {
		String sql = "SELECT * from express_ops_applyeditdeliverystate where cwb in(" + cwbs + ")  and state=1";
		return jdbcTemplate.query(sql, new ApplyEditDeliverystateRowMapper());
	}
	
	public long getApplyEditDeliverystateCount(String cwbs, long ishandle) {
		String sql = "SELECT count(1) from express_ops_applyeditdeliverystate where cwb in(" + cwbs + ") and ishandle=? and state=0";
		return jdbcTemplate.queryForLong(sql, new ApplyEditDeliverystateRowMapper(), ishandle);
	}
	
	public long getApplyEditCount(String cwb, long shenhestate) {
		String sql = "SELECT count(1) from express_ops_applyeditdeliverystate where cwb =? and shenhestate=? ";
		return jdbcTemplate.queryForLong(sql, cwb, shenhestate);
	}
	public List<ApplyEditDeliverystate> getApplyEditDeliverys(long page,String begindate,
			String enddate, long ishandle,long branchid) {
		String sql = "SELECT * from express_ops_applyeditdeliverystate where applybranchid="+branchid;
		StringBuffer sb = new StringBuffer("");
		if(!begindate.equals("")){
			sb.append(" and applytime >='" + begindate + "' ");
		}
		if(!enddate.equals("")){
			sb.append(" and applytime <='" + enddate + "' ");
		}
		if(ishandle>-1){
			sb.append(" and ishandle="+ishandle );
		}
		sql += sb;
		sql += " limit " + (page - 1) * Page.ONE_PAGE_NUMBER + " ," + Page.ONE_PAGE_NUMBER;
		return jdbcTemplate.query(sql, new ApplyEditDeliverystateRowMapper());
	}
	
	public List<ApplyEditDeliverystate> getApplyEditDeliverys(String begindate,
			String enddate, long ishandle,long branchid) {
		String sql = "SELECT * from express_ops_applyeditdeliverystate where applybranchid="+branchid;
		StringBuffer sb = new StringBuffer("");
		if(!begindate.equals("")){
			sb.append(" and applytime >='" + begindate + "' ");
		}
		if(!enddate.equals("")){
			sb.append(" and applytime <='" + enddate + "' ");
		}
		if(ishandle>-1){
			sb.append(" and ishandle="+ishandle );
		}
		sql += sb;
		sql += " order by applytime desc";
		return jdbcTemplate.query(sql, new ApplyEditDeliverystateRowMapper());
	}
	
	public long getApplyEditDeliveryCount(String begindate,
			String enddate, long ishandle,long branchid) {
		String sql = "SELECT count(1) from express_ops_applyeditdeliverystate where applybranchid="+branchid;
		StringBuffer sb = new StringBuffer("");
		if(!begindate.equals("")){
			sb.append(" and applytime >='" + begindate + "' ");
		}
		if(!enddate.equals("")){
			sb.append(" and applytime <='" + enddate + "' ");
		}
		if(ishandle>-1){
			sb.append(" and ishandle="+ishandle );
		}
		sql += sb;
		return jdbcTemplate.queryForLong(sql);
	}
	
	public ApplyEditDeliverystate getApplyEditDeliverystateWithPass(String cwb){
		try{
			String sql = "select * from express_ops_applyeditdeliverystate where cwb=? and shenhestate=3 order by applytime desc limit 1";
			return jdbcTemplate.queryForObject(sql, new ApplyEditDeliverystateRowMapper(),cwb);
		}catch(Exception e){
			return null;
		}
		
	}

}

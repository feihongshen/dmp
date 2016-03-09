package cn.explink.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import cn.explink.domain.OrderbackRecord;
import cn.explink.util.Page;
import cn.explink.util.StringUtil;
import cn.explink.util.TuiGongHuoShangPage;

@Component
public class OrderbackRecordDao {
	private final  class OrderbackRecordMapper implements RowMapper<OrderbackRecord> {
		@Override
		public OrderbackRecord mapRow(ResultSet rs, int rowNum) throws SQLException {
			OrderbackRecord zfav = new OrderbackRecord();
			zfav.setId(rs.getInt("id"));
			zfav.setCwb(StringUtil.nullConvertToEmptyString(rs.getString("cwb")));
			zfav.setCustomerid(rs.getInt("customerid"));
			zfav.setCwbordertypeid(rs.getInt("cwbordertypeid"));
			zfav.setShenhestate(rs.getInt("shenhestate"));
			zfav.setCreatetime(StringUtil.nullConvertToEmptyString(rs.getString("createtime")));
			zfav.setReceivablefee(rs.getBigDecimal("receivablefee"));
			zfav.setEmaildate(StringUtil.nullConvertToEmptyString(rs.getString("emaildate")));//退货出库时间
			zfav.setAuditname(StringUtil.nullConvertToEmptyString(rs.getString("auditname")));//确认人
			zfav.setAudittime(StringUtil.nullConvertToEmptyString(rs.getString("audittime")));//确认时间
			return zfav;
		}
	}

	private final class RecordMapper implements RowMapper<JSONObject> {
		
		@Override
		public JSONObject mapRow(ResultSet rs, int rowNum) throws SQLException {
			JSONObject obj = new JSONObject();
			obj.put("id", rs.getInt(rs.getInt("id")));
			obj.put("cwb",rs.getString("cwb") );
			obj.put("customerid",rs.getInt("customerid") );
			obj.put("cwbordertypeid",rs.getInt("cwbordertypeid") );
			obj.put("shenhestate", rs.getInt("shenhestate"));
			obj.put("createtime", rs.getString("createtime"));
			obj.put("receivablefee",rs.getBigDecimal("receivablefee") );
			obj.put("emaildate", rs.getString("emaildate"));
			return obj;
		}
	}
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	/**
	 * 创建一条操作记录
	 *
	 * @param of
	 * @return key
	 */
	public long creOrderbackRecord(final OrderbackRecord zav) {
		return	this.jdbcTemplate.update("insert into express_ops_orderback_record (cwb,customerid,cwbordertypeid,createtime,receivablefee,emaildate) values(?,?,?,?,?,?)",
				zav.getCwb(),
				zav.getCustomerid(),
				zav.getCwbordertypeid(),
				zav.getCreatetime(),
				zav.getReceivablefee(),
				zav.getEmaildate());
	}
	//分页查询退供货商订单
	public List<OrderbackRecord> getCwbOrdersByCwbspage(long page,String cwb, int cwbtypeid,long customerid, long shenhestate, String begindate, String enddate) {
		String sql = " select * from express_ops_orderback_record where 1=1";
		if(!"".equals(cwb)){
			sql += " and cwb in("+cwb+")";
		}
		StringBuffer w = new StringBuffer("");
		if (cwbtypeid > 0) {
			w.append(" and cwbordertypeid=" + cwbtypeid);
		}
		if (customerid > 0) {
			w.append(" and customerid=" + customerid);
		}
		if (shenhestate > -1) {
			w.append(" and shenhestate=" + shenhestate);
		}
		if (begindate.length() > 0) {
			w.append(" and createtime >= '" + begindate + "' ");
		}
		if (enddate.length() > 0) {
			w.append(" and createtime <= '" + enddate + "' ");
		}
		sql += w;
		if (page!=-9) {
			 sql+=" limit " + (page - 1) * TuiGongHuoShangPage.ONE_PAGE_NUMBER + " ," + TuiGongHuoShangPage.ONE_PAGE_NUMBER;

		}
		return jdbcTemplate.query(sql, new OrderbackRecordMapper());
	}
	
	public long getCwbOrdersByCwbsCount(String cwb, int cwbtypeid,long customerid, long shenhestate, String begindate, String enddate) {
		String sql = " select count(1) from express_ops_orderback_record where 1=1";
		if(!"".equals(cwb)){
			sql += " and cwb in("+cwb+")";
		}
		StringBuffer w = new StringBuffer("");
		if (cwbtypeid > 0) {
			w.append(" and cwbordertypeid=" + cwbtypeid);
		}
		if (customerid > 0) {
			w.append(" and customerid=" + customerid);
		}
		if (shenhestate > -1) {
			w.append(" and shenhestate=" + shenhestate);
		}
		if (begindate.length() > 0) {
			w.append(" and createtime >= '" + begindate + "' ");
		}
		if (enddate.length() > 0) {
			w.append(" and createtime <= '" + enddate + "' ");
		}
			sql += w;
		return jdbcTemplate.queryForLong(sql);
	}
	public void updateShenheState(int shenhestate,String cwb,String auditname,String audittime) {
		String sql = "update express_ops_orderback_record set shenhestate=?,auditname=?,audittime=? where cwb=? order by id desc limit 1";
		jdbcTemplate.update(sql,shenhestate,auditname,audittime,cwb);
	}
	
	public OrderbackRecord getOBRecord(String cwb){
		try{
			String sql = "select * from express_ops_orderback_record where cwb=? and shenhestate!=0 order by id desc limit 1";
			return this.jdbcTemplate.queryForObject(sql, new OrderbackRecordMapper(), cwb);
		}catch(Exception e){
//			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * 供货商拒收返库校验条件
	 * @param cwb
	 * @return
	 */
	public OrderbackRecord getOBRecordForGongYingShangJuShouFanKu(String cwb){
		try{
			String sql = "select * from express_ops_orderback_record where cwb=? and shenhestate=0 order by id desc limit 1";
			return this.jdbcTemplate.queryForObject(sql, new OrderbackRecordMapper(), cwb);
		}catch(Exception e){
//			e.printStackTrace();
			return null;
		}
	}
}

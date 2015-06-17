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
			zfav.setEmaildate(StringUtil.nullConvertToEmptyString(rs.getString("emaildate")));
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
		String sql = " select * from express_ops_orderback_record ";
		if(!cwb.equals("")){
			sql += " where cwb in("+cwb+")";
		}else{
			sql += " where 1=1 ";
			StringBuffer w = new StringBuffer("");
			if (cwbtypeid > 0) {
				w.append(" and cwbordertypeid=" + cwbtypeid);
			}
			if (customerid > 0) {
				w.append(" and customerid=" + customerid);
			}
			if (customerid > 0) {
				w.append(" and customerid=" + customerid);
			}
			if (shenhestate > 0) {
				w.append(" and shenhestate=" + shenhestate);
			}
			if (begindate.length() > 0) {
				w.append(" and createtime >= '" + begindate + "' ");
			}
			if (enddate.length() > 0) {
				w.append(" and createtime <= '" + enddate + "' ");
			}
			sql += w;
		}
		 sql+=" limit " + (page - 1) * Page.ONE_PAGE_NUMBER + " ," + Page.ONE_PAGE_NUMBER;
		return jdbcTemplate.query(sql, new OrderbackRecordMapper());
	}
	
	public long getCwbOrdersByCwbsCount(String cwb, int cwbtypeid,long customerid, long shenhestate, String begindate, String enddate) {
		String sql = " select count(1) from express_ops_orderback_record ";
		if(!cwb.equals("")){
			sql += " where cwb in("+cwb+")";
		}else{
			sql += " where 1=1 ";
			StringBuffer w = new StringBuffer("");
			if (cwbtypeid > 0) {
				w.append(" and cwbordertypeid=" + cwbtypeid);
			}
			if (customerid > 0) {
				w.append(" and customerid=" + customerid);
			}
			if (customerid > 0) {
				w.append(" and customerid=" + customerid);
			}
			if (shenhestate > 0) {
				w.append(" and shenhestate=" + shenhestate);
			}
			if (begindate.length() > 0) {
				w.append(" and createtime >= '" + begindate + "' ");
			}
			if (enddate.length() > 0) {
				w.append(" and createtime <= '" + enddate + "' ");
			}
			sql += w;
		}
		return jdbcTemplate.queryForLong(sql);
	}
	public void updateShenheState(int shenhestate,String cwb) {
		String sql = "update express_ops_orderback_record set shenhestate=? where cwb=? ";
		jdbcTemplate.update(sql,shenhestate,cwb);
	}
}

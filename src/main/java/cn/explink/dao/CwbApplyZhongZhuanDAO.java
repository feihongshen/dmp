package cn.explink.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import cn.explink.domain.CwbApplyZhongZhuan;
import cn.explink.util.Page;
import cn.explink.util.StreamingStatementCreator;
import cn.explink.util.StringUtil;

@Component
public class CwbApplyZhongZhuanDAO {
	private final class CwbApplyZhongZhuanMapper implements RowMapper<CwbApplyZhongZhuan> {

		@Override
		public CwbApplyZhongZhuan mapRow(ResultSet rs, int rowNum) throws SQLException {
			CwbApplyZhongZhuan CwbApplyZhongZhuan = new CwbApplyZhongZhuan();

			CwbApplyZhongZhuan.setApplybranchid(rs.getLong("applybranchid"));
			CwbApplyZhongZhuan.setApplytime(StringUtil.nullConvertToEmptyString(rs.getString("applytime")));
			CwbApplyZhongZhuan.setApplyzhongzhuanbranchid(rs.getLong("applyzhongzhuanbranchid"));
			CwbApplyZhongZhuan.setApplyuserid(rs.getLong("applyuserid"));
			CwbApplyZhongZhuan.setCustomerid(rs.getLong("customerid"));
			CwbApplyZhongZhuan.setCwb(rs.getString("cwb"));
			CwbApplyZhongZhuan.setCwbordertypeid(rs.getLong("cwbordertypeid"));
			CwbApplyZhongZhuan.setHandletime(StringUtil.nullConvertToEmptyString(rs.getString("handletime")));
			CwbApplyZhongZhuan.setHandleuserid(rs.getLong("handleuserid"));
			CwbApplyZhongZhuan.setId(rs.getLong("id"));
			CwbApplyZhongZhuan.setIshandle(rs.getLong("ishandle"));
			CwbApplyZhongZhuan.setIsnow(rs.getLong("isnow"));
			CwbApplyZhongZhuan.setPaybackfee(rs.getBigDecimal("paybackfee"));
			CwbApplyZhongZhuan.setReceivablefee(rs.getBigDecimal("receivablefee"));
			CwbApplyZhongZhuan.setApplyzhongzhuanremark(StringUtil.nullConvertToEmptyString(rs.getString("applyzhongzhuanremark")));
			CwbApplyZhongZhuan.setHandleremark(StringUtil.nullConvertToEmptyString(rs.getString("handleremark")));
			CwbApplyZhongZhuan.setArrivebranchtime(StringUtil.nullConvertToEmptyString(rs.getString("arrivebranchtime")));

			return CwbApplyZhongZhuan;
		}
	}
	
	private final class CwbMapper implements RowMapper<String> {

		@Override
		public String mapRow(ResultSet rs, int rowNum) throws SQLException {

			return rs.getString("cwb");
		}
	}

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public CwbApplyZhongZhuan getCwbApplyZhongZhuanByCwb(String cwb) {
		try {
			return jdbcTemplate.queryForObject("SELECT * from op_cwbapplyzhongzhuan where cwb=? and state=1 limit 0,1", new CwbApplyZhongZhuanMapper(), cwb);
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	public long creCwbApplyZhongZhuan(final CwbApplyZhongZhuan CwbApplyZhongZhuan) {
		KeyHolder key = new GeneratedKeyHolder();
		jdbcTemplate.update(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(java.sql.Connection con) throws SQLException {
				PreparedStatement ps = null;
				ps = con.prepareStatement("insert into op_cwbapplyzhongzhuan (cwb,customerid,cwbordertypeid,applyzhongzhuanbranchid"
						+ ",applytime,applyuserid,applybranchid,applyzhongzhuanremark,receivablefee,paybackfee,isnow,arrivebranchtime) " + "values(?,?,?,?,?,?,?,?,?,?,1,?)", new String[] { "id" });
				ps.setString(1, CwbApplyZhongZhuan.getCwb());
				ps.setLong(2, CwbApplyZhongZhuan.getCustomerid());
				ps.setLong(3, CwbApplyZhongZhuan.getCwbordertypeid());
				ps.setLong(4, CwbApplyZhongZhuan.getApplyzhongzhuanbranchid());
				ps.setString(5, CwbApplyZhongZhuan.getApplytime());
				ps.setLong(6, CwbApplyZhongZhuan.getApplyuserid());
				ps.setLong(7, CwbApplyZhongZhuan.getApplybranchid());
				ps.setString(8, CwbApplyZhongZhuan.getApplyzhongzhuanremark());
				ps.setBigDecimal(9, CwbApplyZhongZhuan.getReceivablefee());
				ps.setBigDecimal(10, CwbApplyZhongZhuan.getPaybackfee());
				ps.setString(11, CwbApplyZhongZhuan.getArrivebranchtime());
				return ps;
			}
		}, key);
		CwbApplyZhongZhuan.setId(key.getKey().longValue());
		return key.getKey().longValue();
	}

	public long creAndUpdateCwbApplyZhongZhuan(CwbApplyZhongZhuan CwbApplyZhongZhuan) {
		jdbcTemplate.update("update op_cwbapplyzhongzhuan set isnow=0 where cwb=? and isnow=1", CwbApplyZhongZhuan.getCwb());
		return creCwbApplyZhongZhuan(CwbApplyZhongZhuan);
	}

	public void updateCwbApplyZhongZhuanForHandle(String handletime, long handleuserid, String handleremark, long ishandle, long applyzhongzhuanbranchid, String cwb) {
		String sql = "update op_cwbapplyzhongzhuan set handletime=?,handleuserid=?,handleremark=?,ishandle=?,applyzhongzhuanbranchid=? where cwb=? and isnow=1";
		jdbcTemplate.update(sql, handletime, handleuserid, handleremark, ishandle, applyzhongzhuanbranchid, cwb);
	}
	public void updateCwbApplyZhongZhuanForIds(String handletime, long handleuserid, long ishandle,String opscwbids,int isnow) {
		String sql = "update op_cwbapplyzhongzhuan as op ,express_ops_cwb_detail de set op.handletime=?,op.handleuserid=?,op.ishandle=? where op.cwb=de.cwb and de.opscwbid in (" + opscwbids + ") and op.isnow=? ";
		jdbcTemplate.update(sql, handletime, handleuserid,  ishandle, isnow);
	}

	public long getCwbApplyZhongZhuanByCwbCount(String cwb) {
		String sql = "select count(1) from op_cwbapplyzhongzhuan where cwb=? and ishandle=0 and isnow=1";
		return jdbcTemplate.queryForLong(sql, cwb);
	}

	public long getCwbApplyZhongZhuanYiChuLiByCwbCount(String cwb) {
		String sql = "select count(1) from op_cwbapplyzhongzhuan where cwb=? and ishandle=3 and isnow=1";
		return jdbcTemplate.queryForLong(sql, cwb);
	}

	public Map<String, CwbApplyZhongZhuan> getCwbApplyZhongZhuanByCwbs(String cwbs) {
		String sql = "select * from op_cwbapplyzhongzhuan where cwb in(" + cwbs + ") and ishandle=0 and isnow=1";
		final Map<String, CwbApplyZhongZhuan> cwbapplyList = new HashMap<String, CwbApplyZhongZhuan>();
		jdbcTemplate.query(new StreamingStatementCreator(sql), new RowCallbackHandler() {
			@Override
			public void processRow(ResultSet rs) throws SQLException {
				try {
					CwbApplyZhongZhuan CwbApplyZhongZhuan = new CwbApplyZhongZhuan();
					CwbApplyZhongZhuan.setApplyzhongzhuanbranchid(rs.getLong("applyzhongzhuanbranchid"));
					CwbApplyZhongZhuan.setCwb(rs.getString("cwb"));
					CwbApplyZhongZhuan.setApplyzhongzhuanremark(rs.getString("applyzhongzhuanremark"));
					cwbapplyList.put(rs.getString("cwb"), CwbApplyZhongZhuan);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		});
		return cwbapplyList;
	}

	public List<CwbApplyZhongZhuan> getCwbApplyZhongZhuanPage(long page, String begindate, String enddate, long ishandle) {
		String sql = "select * from op_cwbapplyzhongzhuan ";

		if (ishandle > -1 || begindate.length() > 0 || enddate.length() > 0) {
			StringBuffer w = new StringBuffer();
			sql += " where ";
			if (ishandle > -1) {
				w.append(" and ishandle=" + ishandle);
			}
			if (begindate.length() > 0) {
				w.append(" and applytime >= '" + begindate + "' ");
			}
			if (enddate.length() > 0) {
				w.append(" and applytime <= '" + enddate + "' ");
			}
			sql += w.substring(4, w.length());
		}
		sql += " limit " + (page - 1) * Page.ONE_PAGE_NUMBER + " ," + Page.ONE_PAGE_NUMBER;
		return jdbcTemplate.query(sql, new CwbApplyZhongZhuanMapper());
	}

	public long getCwbApplyZhongZhuanCount(String begindate, String enddate, long ishandle) {
		String sql = "select  count(1) from op_cwbapplyzhongzhuan ";
		if (ishandle > -1 || begindate.length() > 0 || enddate.length() > 0) {
			StringBuffer w = new StringBuffer();
			sql += " where ";
			if (ishandle > -1) {
				w.append(" and ishandle=" + ishandle);
			}
			if (begindate.length() > 0) {
				w.append(" and applytime >= '" + begindate + "' ");
			}
			if (enddate.length() > 0) {
				w.append(" and applytime <= '" + enddate + "' ");
			}
			sql += w.substring(4, w.length());
		}
		return jdbcTemplate.queryForInt(sql);
	}

	public List<CwbApplyZhongZhuan> getCwbApplyZhongZhuanNoPage(String begindate, String enddate, long ishandle) {
		String sql = "select * from op_cwbapplyzhongzhuan ";

		if (ishandle > -1 || begindate.length() > 0 || enddate.length() > 0) {
			StringBuffer w = new StringBuffer();
			sql += " where ";
			if (ishandle > -1) {
				w.append(" and ishandle=" + ishandle);
			}
			if (!"".equals(begindate)) {
				w.append(" and applytime >= '" + begindate + "' ");
			}
			if (!"".equals(enddate)) {
				w.append(" and applytime <= '" + enddate + "' ");
			}
			sql += w.substring(4, w.length());
		}
		return jdbcTemplate.query(sql, new CwbApplyZhongZhuanMapper());
	}
	
	//新加----lx
	public List<CwbApplyZhongZhuan> getCwbApplyZhongZhuans(long page,String cwbs) {
		String sql = "select * from op_cwbapplyzhongzhuan where cwb in("+cwbs+") and ishandle=0 and isnow=1";
		sql += " limit " + (page - 1) * Page.ONE_PAGE_NUMBER + " ," + Page.ONE_PAGE_NUMBER;
		return jdbcTemplate.query(sql, new CwbApplyZhongZhuanMapper());
	}
	public long getCwbApplyZhongZhuanCount(String cwbs) {
		String sql = "select count(1) from op_cwbapplyzhongzhuan where cwb in("+cwbs+") and ishandle=0 and isnow=1";
		return jdbcTemplate.queryForInt(sql);
	}
	public List<CwbApplyZhongZhuan> getCwbApplyZhongZhuanList(long page,String cwbs,int cwbordertype,long customerid,long applyzhongzhuanbranchid,int ishandle,String begindate, String enddate) {
		String sql = " select * from op_cwbapplyzhongzhuan where ishandle=" + ishandle;
		if(!cwbs.equals("")){
			sql += " and cwb in("+cwbs+")";
		}else{
			StringBuffer w = new StringBuffer("");
			if (cwbordertype > 0) {
				w.append(" and cwbordertypeid=" + cwbordertype);
			}
			if (customerid > 0) {
				w.append(" and customerid=" + customerid);
			}
			if (applyzhongzhuanbranchid > 0) {
				w.append(" and applyzhongzhuanbranchid=" + applyzhongzhuanbranchid);
			}
			if (!"".equals(begindate)) {
				w.append(" and applytime >= '" + begindate + "' ");
			}
			if (!"".equals(enddate)) {
				w.append(" and applytime <= '" + enddate + "' ");
			}
			sql += w;
		}
		if (page!=-9) {
			 sql+=" limit " + (page - 1) * Page.ONE_PAGE_NUMBER + " ," + Page.ONE_PAGE_NUMBER;

		}
		return jdbcTemplate.query(sql, new CwbApplyZhongZhuanMapper());
	}
	public long getCwbApplyZhongZhuanCount(String cwbs,int cwbordertype,long customerid,long applyzhongzhuanbranchid,int ishandle, String begindate, String enddate) {
		String sql = "select count(1) from op_cwbapplyzhongzhuan where ishandle=" + ishandle;
		if(!cwbs.equals("")){
			sql += " and cwb in("+cwbs+")";
		}else{
			StringBuffer w = new StringBuffer("");
			if (cwbordertype > 0) {
				w.append(" and cwbordertypeid=" + cwbordertype);
			}
			if (customerid > 0) {
				w.append(" and customerid=" + customerid);
			}
			if (applyzhongzhuanbranchid > 0) {
				w.append(" and applyzhongzhuanbranchid=" + applyzhongzhuanbranchid);
			}
			if (!"".equals(begindate)) {
				w.append(" and applytime >= '" + begindate + "' ");
			}
			if (!"".equals(enddate)) {
				w.append(" and applytime <= '" + enddate + "' ");
			}
			sql += w;
		}
		return jdbcTemplate.queryForInt(sql);
	}
	
	
	
	public List<CwbApplyZhongZhuan> getCwbApplyZhongZhuanByids(String ids) {
		String sql = "select * from op_cwbapplyzhongzhuan where id in("+ids+") and ishandle=0 and isnow=1";
		return jdbcTemplate.query(sql, new CwbApplyZhongZhuanMapper());
	}
	public void updateCwbApplyZhongZhuanResultSuc(String handletime, long handleuserid, long ishandle, long applyzhongzhuanbranchid, String cwb) {
		String sql = "update op_cwbapplyzhongzhuan set handletime=?,handleuserid=?,ishandle=?,applyzhongzhuanbranchid=? where cwb=?";
		jdbcTemplate.update(sql, handletime, handleuserid, ishandle, applyzhongzhuanbranchid, cwb);
	}
	
	
	public List<String> getCwbApplyZhongZhuanList(String begindate, String enddate, long ishandle) {
		String sql = "select * from op_cwbapplyzhongzhuan ";

		if (ishandle > -1 || begindate.length() > 0 || enddate.length() > 0) {
			StringBuffer w = new StringBuffer();
			sql += " where ";
			if (ishandle > -1) {
				w.append(" and ishandle=" + ishandle);
			}
			if (begindate.length() > 0) {
				w.append(" and applytime >= '" + begindate + "' ");
			}
			if (enddate.length() > 0) {
				w.append(" and applytime < '" + enddate + "' ");
			}
			sql += w.substring(4, w.length());
		}
		return jdbcTemplate.query(sql, new CwbMapper());
	}
	
}

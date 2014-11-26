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

import cn.explink.domain.CwbApplyTuiHuo;
import cn.explink.util.Page;
import cn.explink.util.StreamingStatementCreator;
import cn.explink.util.StringUtil;

@Component
public class CwbApplyTuiHuoDAO {
	private final class CwbApplyTuiHuoMapper implements RowMapper<CwbApplyTuiHuo> {

		@Override
		public CwbApplyTuiHuo mapRow(ResultSet rs, int rowNum) throws SQLException {
			CwbApplyTuiHuo cwbApplyTuiHuo = new CwbApplyTuiHuo();

			cwbApplyTuiHuo.setApplybranchid(rs.getLong("applybranchid"));
			cwbApplyTuiHuo.setApplytime(StringUtil.nullConvertToEmptyString(rs.getString("applytime")));
			cwbApplyTuiHuo.setApplytuihuobranchid(rs.getLong("applytuihuobranchid"));
			cwbApplyTuiHuo.setApplyuserid(rs.getLong("applyuserid"));
			cwbApplyTuiHuo.setCustomerid(rs.getLong("customerid"));
			cwbApplyTuiHuo.setCwb(rs.getString("cwb"));
			cwbApplyTuiHuo.setCwbordertypeid(rs.getLong("cwbordertypeid"));
			cwbApplyTuiHuo.setHandletime(StringUtil.nullConvertToEmptyString(rs.getString("handletime")));
			cwbApplyTuiHuo.setHandleuserid(rs.getLong("handleuserid"));
			cwbApplyTuiHuo.setId(rs.getLong("id"));
			cwbApplyTuiHuo.setIshandle(rs.getLong("ishandle"));
			cwbApplyTuiHuo.setIsnow(rs.getLong("isnow"));
			cwbApplyTuiHuo.setPaybackfee(rs.getBigDecimal("paybackfee"));
			cwbApplyTuiHuo.setReceivablefee(rs.getBigDecimal("receivablefee"));
			cwbApplyTuiHuo.setApplytuihuoremark(StringUtil.nullConvertToEmptyString(rs.getString("applytuihuoremark")));
			cwbApplyTuiHuo.setHandleremark(StringUtil.nullConvertToEmptyString(rs.getString("handleremark")));

			return cwbApplyTuiHuo;
		}
	}

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public CwbApplyTuiHuo getCwbApplyTuiHuoByCwb(String cwb) {
		try {
			return jdbcTemplate.queryForObject("SELECT * from op_cwbapplytuihuo where cwb=? and state=1 limit 0,1", new CwbApplyTuiHuoMapper(), cwb);
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	public long creCwbApplyTuiHuo(final CwbApplyTuiHuo cwbApplyTuiHuo) {
		KeyHolder key = new GeneratedKeyHolder();
		jdbcTemplate.update(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(java.sql.Connection con) throws SQLException {
				PreparedStatement ps = null;
				ps = con.prepareStatement("insert into op_cwbapplytuihuo (cwb,customerid,cwbordertypeid,applytuihuobranchid"
						+ ",applytime,applyuserid,applybranchid,applytuihuoremark,receivablefee,paybackfee,isnow) " + "values(?,?,?,?,?,?,?,?,?,?,1)", new String[] { "id" });
				ps.setString(1, cwbApplyTuiHuo.getCwb());
				ps.setLong(2, cwbApplyTuiHuo.getCustomerid());
				ps.setLong(3, cwbApplyTuiHuo.getCwbordertypeid());
				ps.setLong(4, cwbApplyTuiHuo.getApplytuihuobranchid());
				ps.setString(5, cwbApplyTuiHuo.getApplytime());
				ps.setLong(6, cwbApplyTuiHuo.getApplyuserid());
				ps.setLong(7, cwbApplyTuiHuo.getApplybranchid());
				ps.setString(8, cwbApplyTuiHuo.getApplytuihuoremark());
				ps.setBigDecimal(9, cwbApplyTuiHuo.getReceivablefee());
				ps.setBigDecimal(10, cwbApplyTuiHuo.getPaybackfee());
				return ps;
			}
		}, key);
		cwbApplyTuiHuo.setId(key.getKey().longValue());
		return key.getKey().longValue();
	}

	public long creAndUpdateCwbApplyTuiHuo(CwbApplyTuiHuo cwbApplyTuiHuo) {
		jdbcTemplate.update("update op_cwbapplytuihuo set isnow=0 where cwb=? and isnow=1", cwbApplyTuiHuo.getCwb());
		return creCwbApplyTuiHuo(cwbApplyTuiHuo);
	}

	public void updateCwbApplyTuiHuoForHandle(String handletime, long handleuserid, String handleremark, long ishandle, long applytuihuobranchid, String cwb) {
		String sql = "update op_cwbapplytuihuo set handletime=?,handleuserid=?,handleremark=?,ishandle=?,applytuihuobranchid=? where cwb=? and isnow=1";
		jdbcTemplate.update(sql, handletime, handleuserid, handleremark, ishandle, applytuihuobranchid, cwb);
	}

	public long getCwbApplyTuiHuoByCwbCount(String cwb) {
		String sql = "select count(1) from op_cwbapplytuihuo where cwb=? and ishandle=0 and isnow=1";
		return jdbcTemplate.queryForLong(sql, cwb);
	}

	public Map<String, CwbApplyTuiHuo> getCwbApplyTuiHuoByCwbs(String cwbs) {
		String sql = "select * from op_cwbapplytuihuo where cwb in(" + cwbs + ") and ishandle=0 and isnow=1";
		final Map<String, CwbApplyTuiHuo> cwbapplyList = new HashMap<String, CwbApplyTuiHuo>();
		jdbcTemplate.query(new StreamingStatementCreator(sql), new RowCallbackHandler() {
			@Override
			public void processRow(ResultSet rs) throws SQLException {
				try {
					CwbApplyTuiHuo cwbApplyTuiHuo = new CwbApplyTuiHuo();
					cwbApplyTuiHuo.setApplytuihuobranchid(rs.getLong("applytuihuobranchid"));
					cwbApplyTuiHuo.setCwb(rs.getString("cwb"));
					cwbApplyTuiHuo.setApplytuihuoremark(rs.getString("applytuihuoremark"));
					cwbapplyList.put(rs.getString("cwb"), cwbApplyTuiHuo);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		});
		return cwbapplyList;
	}

	public List<CwbApplyTuiHuo> getCwbApplyTuiHuoPage(long page, String begindate, String enddate, long ishandle) {
		String sql = "select * from op_cwbapplytuihuo ";

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
		return jdbcTemplate.query(sql, new CwbApplyTuiHuoMapper());
	}

	public long getCwbApplyTuiHuoCount(String begindate, String enddate, long ishandle) {
		String sql = "select  count(1) from op_cwbapplytuihuo ";
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

	public List<CwbApplyTuiHuo> getCwbApplyTuiHuoNoPage(String begindate, String enddate, long ishandle) {
		String sql = "select * from op_cwbapplytuihuo ";

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
		return jdbcTemplate.query(sql, new CwbApplyTuiHuoMapper());
	}
}

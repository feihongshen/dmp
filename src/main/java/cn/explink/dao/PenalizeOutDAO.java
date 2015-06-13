/**
 *
 */
package cn.explink.dao;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import cn.explink.domain.PenalizeOut;
import cn.explink.util.Page;
import cn.explink.util.StringUtil;

/**
 * @author Administrator
 *
 */
@Component
public class PenalizeOutDAO {
	@Autowired
	private JdbcTemplate jdbcTemplate;

	private final class PenalizeOutRowMapper implements RowMapper<PenalizeOut> {

		/*
		 * (non-Javadoc)
		 *
		 * @see
		 * org.springframework.jdbc.core.RowMapper#mapRow(java.sql.ResultSet,
		 * int)
		 */
		@Override
		public PenalizeOut mapRow(ResultSet rs, int rowNum) throws SQLException {
			PenalizeOut out = new PenalizeOut();
			out.setPenalizeOutId(rs.getInt("penalizeOutId"));
			out.setCwb(rs.getString("cwb"));
			out.setCustomerid(rs.getLong("customerid"));
			out.setFlowordertype(rs.getLong("flowordertype"));
			out.setReceivablefee(rs.getBigDecimal("receivablefee"));
			out.setPenalizeOutfee(rs.getBigDecimal("penalizeOutfee"));
			out.setPenalizeOutbig(rs.getInt("penalizeOutbig"));
			out.setPenalizeOutsmall(rs.getInt("penalizeOutsmall"));
			out.setPenalizeOutContent(StringUtil.nullConvertToEmptyString(rs.getString("penalizeOutContent")));
			out.setCanceluser(rs.getInt("canceluser"));
			out.setCanceldate(this.getTime(rs.getString("canceldate")));
			out.setCancelContent(StringUtil.nullConvertToEmptyString(rs.getString("cancelContent")));
			out.setCreateruser(rs.getInt("createruser"));
			out.setCreaterdate(this.getTime(rs.getString("createrdate")));
			out.setPenalizeOutstate(rs.getInt("penalizeOutstate"));
			out.setState(rs.getInt("state"));
			return out;
		}

		/**
 *
 */
		private String getTime(String date) {
			String str = StringUtil.nullConvertToEmptyString(date);
			if (str.length() > 0) {
				return str.substring(0, str.lastIndexOf("."));
			}
			return str;
		}
	}

	private final class PenalizeOutfeeSumRowMapper implements RowMapper<BigDecimal> {
		@Override
		public BigDecimal mapRow(ResultSet rs, int rowNum) throws SQLException {

			return rs.getBigDecimal("sum");
		}
	}

	public List<PenalizeOut> getPenalizeOutByCwbs(String cwbs) {
		try {
		String sql = "select * from express_ops_penalizeOut_detail where cwb in (?)";
		return this.jdbcTemplate.query(sql, new PenalizeOutRowMapper(), cwbs);
		}
		catch(Exception e)
		{
			return null;
		}
	}

	public List<PenalizeOut> getPenalizeOutByList(String cwbs, long flowordertype, long customerid, int penalizeOutbig, int penalizeOutsmall, int penalizeState, String starttime, String endtime,
			long page) {
		try {
			String sql = "select * from express_ops_penalizeOut_detail where state=1 ";
			sql += this.creConditions(cwbs, flowordertype, customerid, penalizeOutbig, penalizeOutsmall, penalizeState, starttime, endtime);
			sql += " order by createrdate desc limit " + ((page - 1) * Page.ONE_PAGE_NUMBER) + " ," + Page.ONE_PAGE_NUMBER;
			return this.jdbcTemplate.query(sql, new PenalizeOutRowMapper());
		} catch (Exception e) {
			return null;
		}

	}

	public int getPenalizeOutByListCount(String cwbs, long flowordertype, long customerid, int penalizeOutbig, int penalizeOutsmall, int penalizeState, String starttime, String endtime) {
		try {
			String sql = "select count(1) from express_ops_penalizeOut_detail where state=1 ";
			sql += this.creConditions(cwbs, flowordertype, customerid, penalizeOutbig, penalizeOutsmall, penalizeState, starttime, endtime);
			return this.jdbcTemplate.queryForInt(sql);
		} catch (Exception e) {
			return 0;
		}

	}

	public BigDecimal getPenalizeOutByPenalizeOutfeeSum(String cwbs, long flowordertype, long customerid, int penalizeOutbig, int penalizeOutsmall, int penalizeState, String starttime, String endtime) {
		try {
			String sql = "select sum(penalizeOutfee) as sum from express_ops_penalizeOut_detail where state=1 ";
			sql += this.creConditions(cwbs, flowordertype, customerid, penalizeOutbig, penalizeOutsmall, penalizeState, starttime, endtime);
			return this.jdbcTemplate.queryForObject(sql, new PenalizeOutfeeSumRowMapper());
		} catch (Exception e) {
			return new BigDecimal(0);
		}

	}

	public List<PenalizeOut> getPenalizeOutByListExport(String cwbs, long flowordertype, long customerid, int penalizeOutbig, int penalizeOutsmall, int penalizeState, String starttime, String endtime) {
		try {
			String sql = "select * from express_ops_penalizeOut_detail where state=1 ";
			sql += this.creConditions(cwbs, flowordertype, customerid, penalizeOutbig, penalizeOutsmall, penalizeState, starttime, endtime);
			return this.jdbcTemplate.query(sql, new PenalizeOutRowMapper());
		} catch (Exception e) {
			return null;
		}

	}

	public List<PenalizeOut> getPenalizeOutByPenalizeTypeId(int penalizeTypeid) {
		try {
			String sql = "select * from express_ops_penalizeOut_detail where penalizeOutbig =? or  penalizeOutsmall=?";
			return this.jdbcTemplate.query(sql, new PenalizeOutRowMapper(), penalizeTypeid, penalizeTypeid);
		} catch (Exception e) {
			return null;
		}
	}

	public int crePenalizeOut(PenalizeOut out) throws Exception {
		try {
			String sql = "insert into express_ops_penalizeOut_detail(cwb,customerid,flowordertype,receivablefee,penalizeOutfee,penalizeOutbig,penalizeOutsmall,penalizeOutContent,createruser,createrdate,penalizeOutstate,state)"
					+ "values(?,?,?,?,?,?,?,?,?,NOW(),?,?) ";
			return this.jdbcTemplate.update(sql, out.getCwb(), out.getCustomerid(), out.getFlowordertype(), out.getReceivablefee(), out.getPenalizeOutfee(), out.getPenalizeOutbig(),
					out.getPenalizeOutsmall(), out.getPenalizeOutContent(), out.getCreateruser(), out.getPenalizeOutstate(), 1);
		} catch (Exception e) {
			return 0;
		}
	}
	public PenalizeOut getPenalizeOutByIsNull(String cwb,int penalizeOutsmall,BigDecimal penalizeOutfee)  {
		try {
			String sql = "select * from express_ops_penalizeOut_detail where cwb =? and  penalizeOutsmall=? and penalizeOutfee=? and state=1 limit 1";
			return this.jdbcTemplate.queryForObject(sql, new PenalizeOutRowMapper(), cwb, penalizeOutsmall,penalizeOutfee);
		} catch (Exception e) {
			return null;
		}
	}
	/**
	 * @param penalizeOutId
	 * @return
	 */
	public PenalizeOut getPenalizeOutByPenalizeOutId(int penalizeOutId) {
		try {
			String sql = "select * from express_ops_penalizeOut_detail where penalizeOutId =?  and state=1 limit 1";
			return this.jdbcTemplate.queryForObject(sql, new PenalizeOutRowMapper(),penalizeOutId);
		} catch (Exception e) {
			return null;
		}
	}

	public String creConditions(String cwbs, long flowordertype, long customerid, int penalizeOutbig, int penalizeOutsmall, int penalizeState, String starttime, String endtime) {
		String sql = "";
		if (cwbs.length() > 0) {
			sql += " and cwb in (" + cwbs + ")";
		}
		if (flowordertype > 0) {
			sql += " and flowordertype=" + flowordertype;
		}
		if (customerid > 0) {
			sql += " and customerid=" + customerid;
		}
		if (penalizeOutbig > 0) {
			sql += " and penalizeOutbig=" + penalizeOutbig;
		}
		if (penalizeOutsmall > 0) {
			sql += " and penalizeOutsmall=" + penalizeOutsmall;
		}
		if (penalizeState > 0) {
			sql += " and penalizeOutstate=" + penalizeState;
		}
		if ((starttime.length() > 0) && (endtime.length() > 0)) {
			sql += " and createrdate>='" + starttime + "' and createrdate<='" + endtime + "'";
		}
		return sql;
	}

	/**
	 * @param penalizeOutId
	 * @param cancelContent
	 */
	public int cancelpenalizeOutDataById(int penalizeOutId,int penalizeOutState, String cancelContent,String canceldate,long userid) {
		try{
			String  sql=" update express_ops_penalizeOut_detail set cancelContent=?,penalizeOutState=?,canceluser=?,canceldate=? where penalizeOutId=? ";
			return this.jdbcTemplate.update(sql,cancelContent,penalizeOutState,userid,canceldate,penalizeOutId);
		}
	catch(Exception e)
	{
		return 0;
	}

	}


}

package cn.explink.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import cn.explink.domain.orderflow.TranscwbOrderFlow;
import cn.explink.util.StreamingStatementCreator;

@Component
public class TranscwbOrderFlowDAO {

	private final class TranscwbOrderFlowRowMapper implements RowMapper<TranscwbOrderFlow> {
		@Override
		public TranscwbOrderFlow mapRow(ResultSet rs, int rowNum) throws SQLException {
			TranscwbOrderFlow transcwborderFlow = new TranscwbOrderFlow();
			transcwborderFlow.setFloworderid(rs.getLong("floworderid"));
			transcwborderFlow.setCwb(rs.getString("cwb"));
			transcwborderFlow.setScancwb(rs.getString("scancwb"));
			transcwborderFlow.setBranchid(rs.getLong("branchid"));
			transcwborderFlow.setCredate(rs.getTimestamp("credate"));
			transcwborderFlow.setUserid(rs.getLong("userid"));
			transcwborderFlow.setFloworderdetail(rs.getString("floworderdetail"));
			transcwborderFlow.setFlowordertype(rs.getInt("flowordertype"));
			transcwborderFlow.setIsnow(rs.getInt("isnow"));
			transcwborderFlow.setComment(rs.getString("comment"));
			return transcwborderFlow;
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
	public long creTranscwbOrderFlow(final TranscwbOrderFlow tof) {
		KeyHolder key = new GeneratedKeyHolder();
		this.jdbcTemplate.update(new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(java.sql.Connection con) throws SQLException {
				PreparedStatement ps = null;
				ps = con.prepareStatement("insert into express_ops_transcwb_orderflow (cwb,scancwb,branchid,userid,floworderdetail,flowordertype,isnow,comment) " + "values(?,?,?,?,?,?,1,?)",
						new String[] { "floworderid" });
				ps.setString(1, tof.getCwb());
				ps.setString(2, tof.getScancwb());
				ps.setLong(3, tof.getBranchid());
				ps.setLong(4, tof.getUserid());
				ps.setString(5, tof.getFloworderdetail() == null ? "" : tof.getFloworderdetail().toString());
				ps.setInt(6, tof.getFlowordertype());
				ps.setString(7, tof.getComment());
				return ps;
			}
		}, key);
		tof.setFloworderid(key.getKey().longValue());
		return key.getKey().longValue();
	}

	public long creAndUpdateTranscwbOrderFlow(TranscwbOrderFlow tof) {
		this.jdbcTemplate.update("update express_ops_transcwb_orderflow set isnow='0' where scancwb=? and isnow='1'", tof.getScancwb());
		return this.creTranscwbOrderFlow(tof);
	}

	public TranscwbOrderFlow getTranscwbOrderFlowByCwbAndState(String scancwb, String cwb) {
		try {
			String sql = "select * from express_ops_transcwb_orderflow where scancwb=? and cwb=? and isnow=1";
			return this.jdbcTemplate.queryForObject(sql, new TranscwbOrderFlowRowMapper(), scancwb, cwb);
		} catch (Exception e) {
			return null;
		}
	}

	public List<TranscwbOrderFlow> getTranscwbOrderFlowByScanCwb(String scancwb, String cwb) {
		return this.jdbcTemplate.query("select * from express_ops_transcwb_orderflow where scancwb= ? and cwb=? order by  credate ASC", new TranscwbOrderFlowRowMapper(), scancwb, cwb);
	}

	public List<TranscwbOrderFlow> getTranscwbOrderFlowByCwb(String cwb) {
		return this.jdbcTemplate.query("select * from express_ops_transcwb_orderflow where  cwb=? order by scancwb, credate ASC", new TranscwbOrderFlowRowMapper(), cwb);
	}

	public List<TranscwbOrderFlow> getTranscwbOrderFlowByCwbAndFloworderdetail(String scancwb, String cwb, long flowordertype, final long currentbranchid, final long startbranchid,
			final long nextbranchid) {
		String sql = "select * from express_ops_transcwb_orderflow where scancwb='" + scancwb + "' and cwb='" + cwb + "' and flowordertype=" + flowordertype + " and isnow=1";
		final List<TranscwbOrderFlow> cwbList = new ArrayList<TranscwbOrderFlow>();
		this.jdbcTemplate.query(new StreamingStatementCreator(sql), new RowCallbackHandler() {
			@Override
			public void processRow(ResultSet rs) throws SQLException {
				try {
					boolean isTrue = true;
					if (currentbranchid > 0) {
						isTrue = false;
						String currentbranchidstr = "currentbranchid\":" + currentbranchid + ",";
						if (rs.getString("floworderdetail").indexOf(currentbranchidstr) > -1) {
							isTrue = true;
						}
					}
					if (nextbranchid > 0) {
						isTrue = false;
						String nextbranchidstr = "nextbranchid\":" + nextbranchid + ",";
						if (rs.getString("floworderdetail").indexOf(nextbranchidstr) > -1) {
							isTrue = true;
						}
					}
					if ((startbranchid > 0) && isTrue) {
						isTrue = false;
						String startbranchidstr = "startbranchid\":" + startbranchid + ",";
						if (rs.getString("floworderdetail").indexOf(startbranchidstr) > -1) {
							isTrue = true;
						}
					}

					if (isTrue) {
						TranscwbOrderFlow transcwbOrderFlow = new TranscwbOrderFlow();
						transcwbOrderFlow.setBranchid(rs.getLong("branchid"));
						transcwbOrderFlow.setComment(rs.getString("comment"));
						transcwbOrderFlow.setCredate(rs.getDate("credate"));
						transcwbOrderFlow.setCwb(rs.getString("cwb"));
						transcwbOrderFlow.setFloworderdetail(rs.getString("floworderdetail"));
						transcwbOrderFlow.setFloworderid(rs.getLong("floworderid"));
						transcwbOrderFlow.setFlowordertype(rs.getInt("flowordertype"));
						transcwbOrderFlow.setIsnow(rs.getInt("isnow"));
						transcwbOrderFlow.setUserid(rs.getLong("userid"));
						transcwbOrderFlow.setScancwb(rs.getString("scancwb"));
						cwbList.add(transcwbOrderFlow);
					}

				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		});
		return cwbList;
	}

	public long getTranscwbOrderFlowByScanCwbCount(String scancwb, String cwb, long flowordertype, long branchid, long nextbranchid) {
		String sql = "SELECT COUNT(1) FROM express_ops_transcwb_orderflow WHERE scancwb<>? AND cwb=? AND flowordertype=? AND branchid=? and floworderdetail LIKE '%\"nextbranchid\":" + nextbranchid
				+ ",%' AND isnow=1 ";
		return this.jdbcTemplate.queryForLong(sql, scancwb, cwb, flowordertype, branchid);
	}
}

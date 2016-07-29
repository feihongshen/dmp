package cn.explink.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import cn.explink.core.utils.JsonUtil;
import cn.explink.domain.orderflow.TranscwbOrderFlow;
import cn.explink.enumutil.IsmpsflagEnum;
import cn.explink.util.StreamingStatementCreator;

@Component
public class TranscwbOrderFlowDAO {
	
	private final Logger logger = LoggerFactory.getLogger(getClass());

	private static final String SCANNUM_MAP_SCANCWB = "scancwb";

	private static final String SCANNUM_MAP_COUNT = "scannum";
	
	private static final String SCANNUM_MAP_CWB = "cwb";

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
	
	@Autowired
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

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

	public List<TranscwbOrderFlow> getTranscwbOrderFlowByScanCwbReverseOrder(String scancwb, String cwb) {
		return this.jdbcTemplate.query("select * from express_ops_transcwb_orderflow where scancwb= ? and cwb=? order by  credate DESC", new TranscwbOrderFlowRowMapper(), scancwb, cwb);
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
		if (this.isMPSOrder(cwb) == IsmpsflagEnum.no.getValue()) {
			//Modified by leoliao at 2016-03-15 解决重新打开入库/出库界面时，下一站为空导致订单(或全部运单)扫描完后sendcarnum与scannum不一致的问题
			String sql = "SELECT COUNT(1) FROM express_ops_transcwb_orderflow WHERE scancwb<>? AND cwb=? AND flowordertype=? " + 
						 "AND branchid=? AND isnow=1 ";
			if(nextbranchid > 0){
				sql += " AND floworderdetail LIKE '%\"nextbranchid\":" + nextbranchid + ",%' ";
			}
			
			/**Commoented by leoliao at 2016-03-15
			String sql = "SELECT COUNT(1) FROM express_ops_transcwb_orderflow WHERE scancwb<>? AND cwb=? AND flowordertype=? " + "AND branchid=? and floworderdetail LIKE '%\"nextbranchid\":"
					+ nextbranchid + ",%' AND isnow=1 ";
			*/
			return this.jdbcTemplate.queryForLong(sql, scancwb, cwb, flowordertype, branchid);
		} else {
			return this.getScanNumByTranscwbOrderFlow(scancwb, cwb, flowordertype, branchid);
		}

	}

	private int isMPSOrder(String cwb) {
		String sql = "select ismpsflag from express_ops_cwb_detail where cwb = ? and state = 1 ";
		int ismpsflag = (int) IsmpsflagEnum.no.getValue();
		try {
			ismpsflag = this.jdbcTemplate.queryForInt(sql, cwb);
		} catch (DataAccessException e) {
		}
		return ismpsflag;
	}

	public int getScanNumByTranscwbOrderFlow(String scancwb, String cwb, long flowordertype, long branchid) {
		List<Map<String, Object>> resultList = this.getScanCwbCountMapByTranscwbOrderFlow(cwb, flowordertype, branchid);
		int scannum = 0;
		if(CollectionUtils.isEmpty(resultList)){
			return scannum;
		}
		Map<String, Object> resultMap = resultList.get(0);
		Object countObj = resultMap.get(TranscwbOrderFlowDAO.SCANNUM_MAP_COUNT);
		scannum = Integer.parseInt(countObj.toString());
//		// 当前运单扫描次数
//		Integer currentTransCwbCount = Integer.valueOf(0);
//		// 兄弟运单
//		Map<String, Integer> siblingTransCwbMap = new HashMap<String, Integer>();
//		for (Map<String, Object> resultMap : resultList) {
//			Object transcwbObj = resultMap.get(TranscwbOrderFlowDAO.SCANNUM_MAP_SCANCWB);
//			Object countObj = resultMap.get(TranscwbOrderFlowDAO.SCANNUM_MAP_COUNT);
//			Integer count = Integer.valueOf(0);
//			if (transcwbObj != null) {
//				String transcwb = (String) transcwbObj;
//				// 当前扫描的运单
//				if (scancwb.equals(transcwb)) {
//					currentTransCwbCount = Integer.parseInt(((Long) countObj).toString());
//					continue;
//				}
//				if (countObj != null) {
//					count = Integer.parseInt(((Long) countObj).toString());
//				}
//				siblingTransCwbMap.put(transcwb, count);
//			}
//		}
//
//		int scannum = 0;
//		Set<String> siblingTransCwbKeySet = siblingTransCwbMap.keySet();
//		for (String siblingTransCwbKey : siblingTransCwbKeySet) {
//			// 当前运单某个操作状态扫描次数比某个兄弟运单的小，说明兄弟运单扫描过，所以++
//			if (currentTransCwbCount.compareTo(siblingTransCwbMap.get(siblingTransCwbKey)) < 0) {
//				scannum++;
//			}
//		}
		return scannum;
	}
	
	/**
	 * 通过运单操作获取扫描次数
	 * @param scancwb 运单号
	 * @param cwb 订单号
	 * @param flowordertype flowordertype
	 * @param branchid 站点id
	 * @param isNow 是否为当前操作，0否，1是
	 * @return 返回扫描次数
	 * @author neo01.huang
	 * 2016-4-28
	 */
	public int getScanNumByTranscwbOrderFlow(String scancwb, String cwb, long flowordertype, long branchid, int isNow) {
		List<Map<String, Object>> resultList = this.getScanCwbCountMapByTranscwbOrderFlow(cwb, flowordertype, branchid, isNow);
		// 当前运单扫描次数
		Integer currentTransCwbCount = Integer.valueOf(0);
		// 兄弟运单
		Map<String, Integer> siblingTransCwbMap = new HashMap<String, Integer>();
		for (Map<String, Object> resultMap : resultList) {
			Object transcwbObj = resultMap.get(TranscwbOrderFlowDAO.SCANNUM_MAP_SCANCWB);
			Object countObj = resultMap.get(TranscwbOrderFlowDAO.SCANNUM_MAP_COUNT);
			Integer count = Integer.valueOf(0);
			if (transcwbObj != null) {
				String transcwb = (String) transcwbObj;
				// 当前扫描的运单
				if (scancwb.equals(transcwb)) {
					currentTransCwbCount = Integer.parseInt(((Long) countObj).toString());
					continue;
				}
				if (countObj != null) {
					count = Integer.parseInt(((Long) countObj).toString());
				}
				siblingTransCwbMap.put(transcwb, count);
			}
		}

		int scannum = 0;
		Set<String> siblingTransCwbKeySet = siblingTransCwbMap.keySet();
		for (String siblingTransCwbKey : siblingTransCwbKeySet) {
			// 当前运单某个操作状态扫描次数比某个兄弟运单的小，说明兄弟运单扫描过，所以++
			if (currentTransCwbCount.compareTo(siblingTransCwbMap.get(siblingTransCwbKey)) < 0) {
				scannum++;
			}
		}
		return scannum;
	}

	/**
	 *
	 * @param scancwb
	 * @param cwb
	 * @param flowordertype
	 * @param branchid
	 * @return key:运单号 value:扫描次数
	 */
	private List<Map<String, Object>> getScanCwbCountMapByTranscwbOrderFlow(String cwb, long flowordertype, long branchid) {
//		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
//		String sql = "SELECT " + TranscwbOrderFlowDAO.SCANNUM_MAP_SCANCWB + ",count(scancwb) as " + TranscwbOrderFlowDAO.SCANNUM_MAP_COUNT
//				+ " FROM express_ops_transcwb_orderflow WHERE cwb=? AND flowordertype=? AND branchid=? " + " group by scancwb ";
//		try {
//			result = this.jdbcTemplate.queryForList(sql, cwb, flowordertype, branchid);
//		} catch (DataAccessException e) {
//		}
		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
		String sql = "SELECT " + TranscwbOrderFlowDAO.SCANNUM_MAP_CWB + ",count(cwb) as " + TranscwbOrderFlowDAO.SCANNUM_MAP_COUNT
				+ " FROM express_ops_transcwb_orderflow WHERE cwb=? AND flowordertype=? AND branchid=? and isnow=1 ";
		try {
			result = this.jdbcTemplate.queryForList(sql, cwb, flowordertype, branchid);
		} catch (DataAccessException e) {
		}
		return result;
	}
	
	/**
	 * 获取运单号、扫描次数map
	 * @param cwb 订单号
	 * @param flowordertype flowordertype
	 * @param branchid 站点id
	 * @param isNow 是否为当前操作，0否，1是
	 * @return key:运单号 value:扫描次数
	 */
	private List<Map<String, Object>> getScanCwbCountMapByTranscwbOrderFlow(String cwb, long flowordertype, long branchid, int isNow) {
		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
		String sql = "SELECT " + TranscwbOrderFlowDAO.SCANNUM_MAP_SCANCWB + ",count(scancwb) as " + TranscwbOrderFlowDAO.SCANNUM_MAP_COUNT
				+ " FROM express_ops_transcwb_orderflow WHERE cwb=? AND flowordertype=? AND branchid=? AND isnow=? " + " group by scancwb ";
		try {
			result = this.jdbcTemplate.queryForList(sql, cwb, flowordertype, branchid, isNow);
		} catch (DataAccessException e) {
		}
		return result;
	}
	
	public long getTranscwbOrderFlowCount(String cwb, long flowordertype,  long nextbranchid) {
		String sql = "SELECT COUNT(1) FROM express_ops_transcwb_orderflow WHERE cwb=? AND flowordertype=? AND  floworderdetail LIKE '%\"nextbranchid\":" + nextbranchid
				+ ",%' AND isnow=1 ";
		return jdbcTemplate.queryForLong(sql, cwb, flowordertype);
	}
	
	public TranscwbOrderFlow getTranscwbOrderFlowByCwbAndFlowordertype(String scancwb, String cwb,long flowordertype) {
		try {
			String sql = "select * from express_ops_transcwb_orderflow where scancwb=? and cwb=? and flowordertype=? limit 1";
			return this.jdbcTemplate.queryForObject(sql, new TranscwbOrderFlowRowMapper(), scancwb, cwb,flowordertype);
		} catch (Exception e) {
			return null;
		}
	}
	
	/**
	 * 查询express_ops_transcwb_orderflow
	 * @param param 参数map
	 * @param orderBy 排序
	 * @return
	 */
	public List<TranscwbOrderFlow> queryTranscwbOrderFlow(Map<String, Object> paramMap, String orderBy) {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT * FROM express_ops_transcwb_orderflow t ");
		sql.append(" WHERE 1=1 ");
		
		if (paramMap.get("flowordertype") != null) {
			sql.append(" AND t.flowordertype = :flowordertype ");
		}
		if (paramMap.get("flowordertypeIn") != null) {
			sql.append(" AND t.flowordertype IN (" + paramMap.get("flowordertypeIn").toString() + ") ");
		}
		if (paramMap.get("isnow") != null) {
			sql.append(" AND t.isnow = :isnow ");
		}
		if (paramMap.get("cwb") != null) {
			sql.append(" AND t.cwb = :cwb ");
		}
		if (paramMap.get("branchid") != null) {
			sql.append(" AND t.branchid = :branchid ");
		}
		if (paramMap.get("scancwb") != null) {
			sql.append(" AND t.scancwb = :scancwb ");
		}
		if (orderBy != null) {
			sql.append(" ORDER BY ");
			sql.append(orderBy);
			sql.append(" ");
		}
		String sqlStr = sql.toString();
		logger.info("queryTranscwbOrderFlow->sqlStr:{}", sqlStr);
		logger.info("queryTranscwbOrderFlow->paramMap:{}", JsonUtil.translateToJson(paramMap));
		return namedParameterJdbcTemplate.query(sqlStr, paramMap, new TranscwbOrderFlowRowMapper());
	}
	
	/**
	 * 用过订单查询运单是否都是处于同一操作流程   add by vic.liang@pjbest.com 2016-07-26
	 * @return
	 */
	public int getTransScanTimeByCwbFlowordertype(String cwb, int flowordertype) {
		String sql = "select count(1) from express_ops_transcwb_orderflow where cwb = ? and flowordertype = ? and isnow = 1";
		return this.jdbcTemplate.queryForInt(sql, cwb, flowordertype);
	}
}
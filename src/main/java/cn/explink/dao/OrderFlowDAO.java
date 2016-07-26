package cn.explink.dao;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.pjbest.splitting.aspect.DataSource;
import com.pjbest.splitting.routing.DatabaseType;

import cn.explink.domain.orderflow.OrderFlow;
import cn.explink.enumutil.FlowOrderTypeEnum;
import cn.explink.util.Page;
import cn.explink.util.StreamingStatementCreator;
import cn.explink.util.StringUtil;

@Repository
public class OrderFlowDAO {

	private static Logger logger = LoggerFactory.getLogger(OrderFlowDAO.class);
	
	private final class OrderFlowRowMapper implements RowMapper<OrderFlow> {
		@Override
		public OrderFlow mapRow(ResultSet rs, int rowNum) throws SQLException {
			OrderFlow orderFlow = new OrderFlow();
			orderFlow.setFloworderid(rs.getLong("floworderid"));
			orderFlow.setCwb(rs.getString("cwb"));
			orderFlow.setBranchid(rs.getLong("branchid"));
			orderFlow.setCredate(rs.getTimestamp("credate"));
			orderFlow.setUserid(rs.getLong("userid"));
			orderFlow.setFloworderdetail(rs.getString("floworderdetail"));
			orderFlow.setFlowordertype(rs.getInt("flowordertype"));
			orderFlow.setIsnow(rs.getInt("isnow"));
			orderFlow.setComment(rs.getString("comment"));
			return orderFlow;
		}

	}

	private final class OrderFlowRealRowMapper implements RowMapper<OrderFlow> {
		@Override
		public OrderFlow mapRow(ResultSet rs, int rowNum) throws SQLException {
			OrderFlow orderFlow = new OrderFlow();
			orderFlow.setFloworderid(rs.getLong("floworderid"));
			orderFlow.setCwb(rs.getString("cwb"));
			orderFlow.setBranchid(rs.getLong("branchid"));
			orderFlow.setCredate(rs.getTimestamp("credate"));
			orderFlow.setUserid(rs.getLong("userid"));
			orderFlow.setFloworderdetail(rs.getString("floworderdetail"));
			orderFlow.setFlowordertype(rs.getInt("flowordertype"));
			orderFlow.setIsnow(rs.getInt("isnow"));
			orderFlow.setComment(rs.getString("comment"));
			return orderFlow;
		}

	}

	private final class OrderFlowRowMapperNotDetail implements RowMapper<OrderFlow> {
		@Override
		public OrderFlow mapRow(ResultSet rs, int rowNum) throws SQLException {
			OrderFlow orderFlow = new OrderFlow();
			orderFlow.setCwb(rs.getString("cwb"));
			orderFlow.setBranchid(rs.getLong("branchid"));
			orderFlow.setCredate(rs.getTimestamp("credate"));
			orderFlow.setUserid(rs.getLong("userid"));
			orderFlow.setFlowordertype(rs.getInt("flowordertype"));
			orderFlow.setIsnow(rs.getInt("isnow"));
			orderFlow.setComment(rs.getString("comment"));
			return orderFlow;
		}

	}

	private final class OrderFlowRowMapperCwbAndCredate implements RowMapper<OrderFlow> {
		@Override
		public OrderFlow mapRow(ResultSet rs, int rowNum) throws SQLException {
			OrderFlow orderFlow = new OrderFlow();
			orderFlow.setCwb(rs.getString("cwb"));
			orderFlow.setCredate(rs.getTimestamp("credate"));
			return orderFlow;
		}

	}

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	private OperationTimeDAO operationTimeDAO;

	/**
	 * 创建一条操作记录
	 *
	 * @param of
	 * @return key
	 */
	public long creOrderFlow(final OrderFlow of, final String credate) {
		KeyHolder key = new GeneratedKeyHolder();
		this.jdbcTemplate.update(new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(java.sql.Connection con) throws SQLException {
				PreparedStatement ps = null;
				ps = con.prepareStatement("insert into express_ops_order_flow (cwb,branchid,userid,floworderdetail,flowordertype,isnow,comment,credate) " + "values(?,?,?,?,?,1,?,?)",
						new String[] { "floworderid" });
				ps.setString(1, of.getCwb());
				ps.setLong(2, of.getBranchid());
				ps.setLong(3, of.getUserid());
				ps.setString(4, of.getFloworderdetail() == null ? "" : of.getFloworderdetail().toString());
				ps.setInt(5, of.getFlowordertype());
				ps.setString(6, of.getComment());
				ps.setString(7, credate);
				return ps;
			}
		}, key);
		of.setFloworderid(key.getKey().longValue());
		return key.getKey().longValue();
	}

	public long mehCreOrderFlow(final OrderFlow of, final String credate) {
		String sql ="update express_ops_order_flow set floworderdetail='0' where cwb='"+of.getCwb()+"' and flowordertype ="+of.getFlowordertype()+"";
		this.jdbcTemplate.update(sql);
		KeyHolder key = new GeneratedKeyHolder();
		this.jdbcTemplate.update(new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(java.sql.Connection con) throws SQLException {
				PreparedStatement ps = null;
				ps = con.prepareStatement("insert into express_ops_order_flow (cwb,branchid,userid,floworderdetail,flowordertype,isnow,comment,credate) " + "values(?,?,?,?,?,?,?,?)",
						new String[] { "floworderid" });
				ps.setString(1, of.getCwb());
				ps.setLong(2, of.getBranchid());
				ps.setLong(3, of.getUserid());
				ps.setString(4, of.getFloworderdetail() == null ? "" : of.getFloworderdetail().toString());
				ps.setInt(5, of.getFlowordertype());
				ps.setInt(6, of.getIsnow());
				ps.setString(7, of.getComment());
				ps.setString(8, credate);
				return ps;
			}
		}, key);
		of.setFloworderid(key.getKey().longValue());
		return key.getKey().longValue();
	}

	public long creAndUpdateOrderFlow(OrderFlow of) {

		this.jdbcTemplate.update("update express_ops_order_flow set isnow='0' where cwb=? and isnow='1'", of.getCwb());
		if ((of.getFlowordertype() == FlowOrderTypeEnum.RuKu.getValue()) || (of.getFlowordertype() == FlowOrderTypeEnum.ChuKuSaoMiao.getValue())
				|| (of.getFlowordertype() == FlowOrderTypeEnum.KuDuiKuChuKuSaoMiao.getValue()) || (of.getFlowordertype() == FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue())
				|| (of.getFlowordertype() == FlowOrderTypeEnum.FenZhanDaoHuoYouHuoWuDanSaoMiao.getValue()) || (of.getFlowordertype() == FlowOrderTypeEnum.FenZhanLingHuo.getValue())
				|| (of.getFlowordertype() == FlowOrderTypeEnum.PosZhiFu.getValue()) || (of.getFlowordertype() == FlowOrderTypeEnum.TuiHuoZhanRuKu.getValue())
				|| (of.getFlowordertype() == FlowOrderTypeEnum.TuiGongYingShangChuKu.getValue()) || (of.getFlowordertype() == FlowOrderTypeEnum.GongYingShangJuShouFanKu.getValue())
				|| (of.getFlowordertype() == FlowOrderTypeEnum.CheXiaoFanKui.getValue()) || (of.getFlowordertype() == FlowOrderTypeEnum.ShenHeWeiZaiTou.getValue())
				|| (of.getFlowordertype() == FlowOrderTypeEnum.TuiHuoChuZhan.getValue()) || (of.getFlowordertype() == FlowOrderTypeEnum.ZhongZhuanZhanRuKu.getValue())
				|| (of.getFlowordertype() == FlowOrderTypeEnum.ZhongZhuanZhanChuKu.getValue()) || (of.getFlowordertype() == FlowOrderTypeEnum.ChaoQu.getValue())
				|| (of.getFlowordertype() == FlowOrderTypeEnum.YiChangPiPeiYiChuLi.getValue()) || (of.getFlowordertype() == FlowOrderTypeEnum.YunDanLuRu.getValue())
				|| (of.getFlowordertype() == FlowOrderTypeEnum.LanJianRuZhan.getValue()) || (of.getFlowordertype() == FlowOrderTypeEnum.LanJianChuZhan.getValue())){
			JSONObject orderJson = JSONObject.fromObject(of.getFloworderdetail()).getJSONObject("cwbOrder");
			String outWarehouseTime = "";
			if (of.getFlowordertype() == FlowOrderTypeEnum.RuKu.getValue()) {
				outWarehouseTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(of.getCredate());
			}
			//modified by jiangyu begin【避免出现空指针异常】
			String receivablefee = "0.00";
			if ("null".equals(orderJson.getString("receivablefee"))||orderJson.getString("receivablefee")==null) {
				receivablefee = "0.00";
			}else {
				receivablefee = orderJson.getString("receivablefee");
			}
			String paybackfee = "0.00";
			if ("null".equals(orderJson.getString("paybackfee"))||orderJson.getString("paybackfee")==null) {
				paybackfee = "0.00";
			}else {
				paybackfee = orderJson.getString("paybackfee");
			}
			//modified by jiangyu end
			this.operationTimeDAO.creAndUpdateOperationTime(of.getCwb(), of.getBranchid(), of.getFlowordertype(), 0, orderJson.getLong("nextbranchid"), orderJson.getLong("customerid"),
					outWarehouseTime, orderJson.getString("emaildate"),orderJson.getInt("cwbordertypeid"),new BigDecimal(receivablefee),new BigDecimal(paybackfee));
		} else if (of.getFlowordertype() == FlowOrderTypeEnum.GongHuoShangTuiHuoChenggong.getValue()) {
			this.operationTimeDAO.delOperationTime(of.getCwb());
		}
		return this.creOrderFlow(of, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(of.getCredate()));
	}

	public List<OrderFlow> getOrderFlowListByCwb(String cwb) {
		List<OrderFlow> orderFlowList = this.jdbcTemplate.query("SELECT of_b.*,u.username FROM express_set_user u RIGHT JOIN " + "(SELECT of.*,b.branchname FROM "
				+ "(SELECT * FROM express_ops_order_flow WHERE cwb=?) " + "of LEFT JOIN express_set_branch b ON b.branchid=of.branchid )" + " of_b ON of_b.userid=u.userid ", new OrderFlowRowMapper(),
				cwb);
		return orderFlowList;
	}

	public OrderFlow getOrderCurrentFlowByCwb(String cwb) {
		String sql = "select * from express_ops_order_flow where cwb = ? and isnow = 1";
		Object[] paras = new Object[] { cwb };
		List<OrderFlow> orderFlowList = this.jdbcTemplate.query(sql, paras, new OrderFlowRowMapper());
		if (orderFlowList.size() == 0) {
			return null;
		}
		return orderFlowList.get(0);
	}
	public Map<String, String> getOrderFlowByCwbs(String cwbs) {
		Map<String, String> map = new HashMap<String, String>();
		String sql = "select * from express_ops_order_flow where cwb in("+cwbs+") and isnow = 1";
		List<OrderFlow> orderFlowList = this.jdbcTemplate.query(sql,  new OrderFlowRowMapper());
		if (orderFlowList.size() > 0) {
			for(int i=0;i<orderFlowList.size();i++){
				OrderFlow order = new OrderFlow();
				order = orderFlowList.get(i);
				map.put(order.getCwb(), order.getCredate().toString());
			}
		}
		return map;
	}

	/**
	 * 获取某种操作类型的最新流程记录
	 * @param cwb
	 * @param flowOrderType
	 * @return
	 */
	public OrderFlow findOrderFlow(String cwb , int flowOrderType){
		StringBuffer sql = new StringBuffer("select * from express_ops_order_flow flow where flow.cwb = '") ;
		sql.append(cwb).append("'") ;
		sql.append(" and flow.flowordertype = ").append(flowOrderType) ;
		sql.append(" order by flow.credate desc ") ;
		List<OrderFlow> orderFlowList = this.jdbcTemplate.query(sql.toString(),  new OrderFlowRowMapper());
		if(orderFlowList == null || orderFlowList.size() == 0){
			return null ;
		}
		return orderFlowList.get(0) ;
	}
	
	public List<OrderFlow> getOrderFlowByFlowordertypeAndCwb(long flowordertype, String cwb, long branchid) {
		String sql = "select * from express_ops_order_flow where flowordertype=? and cwb=? and branchid=?";
		return this.jdbcTemplate.query(sql, new OrderFlowRealRowMapper(), flowordertype, cwb, branchid);
	}

	public OrderFlow getOrderFlowByCwbAndState(String cwb, long isnow) {
		try {
			String sql = "select `cwb`,`branchid`,`credate`,`userid`,`flowordertype`,`isnow`,`outwarehouseid`,`comment` from express_ops_order_flow where cwb='" + cwb + "' and isnow=" + isnow;
			return this.jdbcTemplate.queryForObject(sql, new OrderFlowRowMapperNotDetail());
		} catch (Exception e) {
			return null;
		}
	}

	
	public OrderFlow getOrderFlowByCwbAndStateAll(String cwb, long isnow) {
		try {
			String sql = "select * from express_ops_order_flow where cwb='" + cwb + "' and isnow=" + isnow;
			return this.jdbcTemplate.queryForObject(sql, new OrderFlowRowMapper());
		} catch (Exception e) {
			return null;
		}
	}

	public OrderFlow getOrderFlowById(long id) {
		String sql = "select * from express_ops_order_flow where floworderid = ?";
		return this.jdbcTemplate.queryForObject(sql, new OrderFlowRealRowMapper(), id);
	}

	private final class IntoWarehousByBranchidAndCustomerMapper implements RowMapper<BigDecimal[]> {
		@Override
		public BigDecimal[] mapRow(ResultSet rs, int rowNum) throws SQLException {
			return new BigDecimal[] { rs.getBigDecimal("num"), StringUtil.nullConvertToBigDecimal(rs.getBigDecimal("receivablefee")),
					StringUtil.nullConvertToBigDecimal(rs.getBigDecimal("paybackfee")) };

		}
	}

	/**
	 * 获取今天状态为flowordertype的并且 属于branchid机构的customerid供货商的数量与金额的统计结果
	 *
	 * @param branchid
	 *            机构
	 * @param customerid
	 *            供货商
	 * @param toDay
	 *            时间
	 * @param flowordertype
	 *            状态 对应 FlowOrderTypeEnum
	 * @return
	 */
	public BigDecimal[] getOrderFlowJoinCwbByBranchidAndCustomerList(long branchid, long customerid, String toDay, long flowordertype) {
		String sql = "SELECT COUNT(1) as num,SUM(of_cd.receivablefee) as receivablefee,SUM(of_cd.paybackfee) as paybackfee FROM "
				+ "(SELECT DISTINCT of.`cwb`,of.`flowordertype`,cd.receivablefee,cd.paybackfee FROM `express_ops_order_flow` of "
				+ "LEFT JOIN `express_ops_cwb_detail` cd ON  of.cwb=cd.cwb WHERE of.credate >? and of.flowordertype=? and of.branchid=? and cd.customerid=? AND cd.state=1) of_cd ";
		return this.jdbcTemplate.queryForObject(sql, new IntoWarehousByBranchidAndCustomerMapper(), toDay, flowordertype, branchid, customerid);
	}

	/**
	 * 获取今天状态为flowordertype的并且 属于branchid机构的数量与金额的统计结果
	 *
	 * @param branchid
	 *            机构
	 * @param toDay
	 *            时间
	 * @param flowordertype
	 *            状态 对应 FlowOrderTypeEnum
	 * @return
	 */
	public BigDecimal[] getOrderFlowJoinCwbByBranchidAndCustomerList(long branchid, String toDay, long flowordertype) {
		String sql = "SELECT COUNT(1) as num,SUM(of_cd.receivablefee) as receivablefee,SUM(of_cd.paybackfee) as paybackfee FROM "
				+ "(SELECT DISTINCT of.`cwb`,of.`flowordertype`,cd.receivablefee,cd.paybackfee FROM `express_ops_order_flow` of "
				+ "LEFT JOIN `express_ops_cwb_detail` cd ON  of.cwb=cd.cwb WHERE of.credate >? and of.flowordertype=? and of.branchid=? AND cd.state=1) of_cd ";
		return this.jdbcTemplate.queryForObject(sql, new IntoWarehousByBranchidAndCustomerMapper(), toDay, flowordertype, branchid);
	}

	/**
	 * 获取今天“当前”状态为flowordertype的并且 属于branchid机构的customerid供货商的数量与金额的统计结果
	 *
	 * @param branchid
	 *            机构
	 * @param customerid
	 *            供货商
	 * @param toDay
	 *            时间
	 * @param flowordertype
	 *            当前(isnow 为1)状态 对应 FlowOrderTypeEnum
	 * @return
	 */
	public BigDecimal[] getOrderFlowJoinCwbByBranchidAndCustomerListIsNow(long branchid, long customerid, String toDay, long flowordertype) {
		String sql = "SELECT COUNT(1) as num,SUM(of_cd.receivablefee) as receivablefee,SUM(of_cd.paybackfee) as paybackfee FROM "
				+ "(SELECT DISTINCT of.`cwb`,of.`flowordertype`,cd.receivablefee,cd.paybackfee FROM `express_ops_order_flow` of "
				+ "LEFT JOIN `express_ops_cwb_detail` cd ON  of.cwb=cd.cwb WHERE of.credate >? and of.flowordertype=? and isnow=1 and of.branchid=? and cd.customerid=? AND cd.state=1) of_cd ";
		return this.jdbcTemplate.queryForObject(sql, new IntoWarehousByBranchidAndCustomerMapper(), toDay, flowordertype, branchid, customerid);
	}

	/**
	 * 获取toDay以后的环节为flowordertype的并且
	 * 属于branchid机构的结果中符合reacherrorflag状态的的数量与金额的统计结果
	 *
	 * @param branchid
	 *            机构
	 * @param toDay
	 *            时间
	 * @param flowordertype
	 *            状态 对应 FlowOrderTypeEnum
	 * @param reacherrorflag
	 *            对应ReachErrorFlagEnum
	 * @return
	 */
	public BigDecimal[] getOrderFlowJoinCwbByReacherrorflagAndBranchidList(int reacherrorflag, long branchid, String toDay, long flowordertype) {
		String sql = "SELECT COUNT(1) as num,SUM(of_cd.receivablefee) as receivablefee,SUM(of_cd.paybackfee) as paybackfee FROM "
				+ "(SELECT DISTINCT of.`cwb`,of.`flowordertype`,cd.receivablefee,cd.paybackfee FROM `express_ops_order_flow` of "
				+ "LEFT JOIN `express_ops_cwb_detail` cd ON  of.cwb=cd.cwb WHERE of.credate >? and cd.reacherrorflag=? and of.flowordertype=? and of.branchid=? AND cd.state=1) of_cd ";
		return this.jdbcTemplate.queryForObject(sql, new IntoWarehousByBranchidAndCustomerMapper(), toDay, reacherrorflag, flowordertype, branchid);
	}

	/**
	 * 快捷查询,调用订单流程的接口
	 *
	 * @param cwb
	 * @return
	 */
	public List<OrderFlow> getOrderFlowByCwb(String cwb) {
		return this.jdbcTemplate.query("select * from express_ops_order_flow where cwb= ?  order by  credate ASC,flowordertype asc", new OrderFlowRowMapper(), cwb);
	}
	
	/*public List<OrderFlow> getOrderFlowByCwbAndCustomerid(String cwb,int customerid) {
		return this.jdbcTemplate.query("select * from express_ops_order_flow where cwb= ?  order by  credate ASC,flowordertype asc", new OrderFlowRowMapper(), cwb);
	}*/

	public List<OrderFlow> getOrderFlowByWhere(long page, long branchid, long userid, long flowordertype, String beginemaildate, String endemaildate, long onePageNumber) {
		String sql = "select `cwb`,`branchid`,`credate`,`userid`,`flowordertype`,`isnow`,`outwarehouseid`,`comment` from express_ops_order_flow ";
		sql = this.getOrderFlowByWhereSql(sql, branchid, userid, flowordertype, beginemaildate, endemaildate);
		sql += " limit " + ((page - 1) * onePageNumber) + " ," + onePageNumber;
		return this.jdbcTemplate.query(sql, new OrderFlowRowMapperNotDetail());
	}

	public List<String> getOrderFlowLingHuoList(long branchid, String flowordertypes, String beginemaildate, String endemaildate) {
		String sql = "select cwb from express_ops_order_flow ";
		// 强制索引
		if ((beginemaildate.length() > 0) || (endemaildate.length() > 0)) {
			sql = sql.replace("express_ops_order_flow", "express_ops_order_flow FORCE INDEX(FlowCredateIdx)");
		}

		if ((branchid > 0) || (beginemaildate.length() > 0) || (endemaildate.length() > 0) || (flowordertypes.length() > 0)) {
			StringBuffer w = new StringBuffer();
			sql += " where ";
			if (branchid > 0) {
				w.append(" and branchid=" + branchid);
			}
			if (beginemaildate.length() > 0) {
				w.append(" and credate >= '" + beginemaildate + "'");
			}
			if (endemaildate.length() > 0) {
				w.append(" and credate <= '" + endemaildate + "'");
			}
			if (flowordertypes.length() > 0) {
				w.append(" and flowordertype in(" + flowordertypes + ")");
			}
			sql += w.substring(4, w.length());
		}
		return this.jdbcTemplate.queryForList(sql, String.class);
	}

	public List<String> getOrderFlowJinRiChuKuORRuKuList(final long branchid, String flowordertypes, String beginemaildate) {
		String sql = "select * from express_ops_order_flow FORCE INDEX(FlowCredateIdx) where credate >='" + beginemaildate + "' " + " and flowordertype in(" + flowordertypes + ")";

		final List<String> cwbList = new ArrayList<String>();
		this.jdbcTemplate.query(new StreamingStatementCreator(sql), new RowCallbackHandler() {
			@Override
			public void processRow(ResultSet rs) throws SQLException {
				try {
					boolean isTrue = true;
					if (branchid > 0) {
						isTrue = false;
						String dsate = "nextbranchid\":" + branchid + ",\"deliverybranchid";
						if (rs.getString("floworderdetail").indexOf(dsate) > -1) {
							isTrue = true;
						}
					}
					if (isTrue) {
						cwbList.add(rs.getString("cwb"));
					}

				} catch (Exception e) {
					logger.error("", e);
				}
			}
		});
		return cwbList;
	}

	public long getOrderFlowCount(long branchid, long userid, long flowordertype, String beginemaildate, String endemaildate) {
		String sql = "select  count(1) from express_ops_order_flow ";
		sql = this.getOrderFlowByWhereSql(sql, branchid, userid, flowordertype, beginemaildate, endemaildate);
		return this.jdbcTemplate.queryForInt(sql);
	}

	private String getOrderFlowByWhereSql(String sql, long branchid, long userid, long flowordertype, String beginemaildate, String endemaildate) {
		if ((branchid > 0) || (userid > 0) || (beginemaildate.length() > 0) || (endemaildate.length() > 0) || (flowordertype > 0)) {
			StringBuffer w = new StringBuffer();
			sql += " where ";
			if (branchid > 0) {
				w.append(" and branchid=" + branchid);
			}
			if (userid > 0) {
				w.append(" and userid=" + userid);
			}
			if (beginemaildate.length() > 0) {
				w.append(" and credate >= '" + beginemaildate + "'");
			}
			if (endemaildate.length() > 0) {
				w.append(" and credate <= '" + endemaildate + "'");
			}
			if (flowordertype > 0) {
				w.append(" and flowordertype=" + flowordertype);
			}
			sql += w.substring(4, w.length());
		}
		return sql;
	}

	public String getOrderFlowByCwbAndFlowordertypeWhereSql(String sql, String cwb, long flowordertype, String begindate, String enddate) {
		if ((cwb.length() > 0) || (flowordertype > 0) || (begindate.length() > 0) || (enddate.length() > 0)) {
			StringBuffer w = new StringBuffer();
			sql += " where ";
			if (cwb.trim().length() > 0) {
				w.append(" and cwb = '" + cwb + "'");
			}
			if (flowordertype > 0) {
				w.append(" and flowordertype=" + flowordertype);
			}
			if (begindate.length() > 0) {
				w.append(" and credate >= '" + begindate + "'");
			}
			if (enddate.length() > 0) {
				w.append(" and credate <= '" + enddate + "'");
			}
			if ((cwb.trim().length() == 0) && (flowordertype == 0) && (begindate.length() == 0) && (enddate.length() == 0)) {
				w.append(" and cwb = '" + cwb + "'");
			}
			w.append(" order by floworderid ");
			sql += w.substring(4, w.length());
		} else {
			sql += " where cwb = '" + cwb + "'  order by floworderid  ";
		}
		return sql;
	}

	public List<OrderFlow> getOrderFlowByCwbAndFlowordertype(String cwb, long flowordertype, String begindate, String enddate) {
		String sql = "select `cwb`,`branchid`,`credate`,`userid`,`flowordertype`,`isnow`,`outwarehouseid`,`comment` from express_ops_order_flow ";
		sql = this.getOrderFlowByCwbAndFlowordertypeWhereSql(sql, cwb, flowordertype, begindate, enddate);
		return this.jdbcTemplate.query(sql, new OrderFlowRowMapperNotDetail());
	}
	
	/**
	 * 
	 * 根据订单号集合，到流程表中查找符合条件的数据 
	 * @author 刘武强
	 * @date:2016年7月22日 下午6:06:33 
	 * @params:@param cwb
	 * @params:@param flowordertype
	 * @params:@param begindate
	 * @params:@param enddate
	 * @params:@return
	 */
	public List<OrderFlow> getOrderFlowByCwbsAndFlowordertype(String cwbs, long flowordertype, String begindate, String enddate) {
		String sql = "select `cwb`,`branchid`,`credate`,`userid`,`flowordertype`,`isnow`,`outwarehouseid`,`comment` from express_ops_order_flow ";
		sql = this.getOrderFlowByCwbsAndFlowordertypeWhereSql(sql, cwbs, flowordertype, begindate, enddate);
		return this.jdbcTemplate.query(sql, new OrderFlowRowMapperNotDetail());
	}
	/**
	 * 
	 * 拼接条件
	 * @author 刘武强
	 * @date:2016年7月22日 下午6:12:09 
	 * @params:@param sql
	 * @params:@param cwbs
	 * @params:@param flowordertype
	 * @params:@param begindate
	 * @params:@param enddate
	 * @params:@return
	 */
	public String getOrderFlowByCwbsAndFlowordertypeWhereSql(String sql, String cwbs, long flowordertype, String begindate, String enddate) {
		if ((cwbs.length() > 0) || (flowordertype > 0) || (begindate.length() > 0) || (enddate.length() > 0)) {
			StringBuffer w = new StringBuffer();
			sql += " where 1=1 ";
			if (cwbs.trim().length() > 0) {
				w.append(" and cwb in (" + cwbs + ")");
			}
			if (flowordertype > 0) {
				w.append(" and flowordertype=" + flowordertype);
			}
			if (begindate.length() > 0) {
				w.append(" and credate >= '" + begindate + "'");
			}
			if (enddate.length() > 0) {
				w.append(" and credate <= '" + enddate + "'");
			}
			
			w.append(" order by floworderid ");
			sql += w;
		} else {
			sql += " where cwb in (" + cwbs + ") order by floworderid  ";
		}
		return sql;
	}
	// 2013-10-09 5196版本临时去掉到货明细、到货批量页面中的“出库时间”
	/*
	 * public OrderFlow getOrderFlowByCwbAndFlowordertype(String cwb,long
	 * flowordertype){ try{ String
	 * sql="select * from express_ops_order_flow where cwb = '"+cwb+"'";
	 * if(flowordertype>0){ sql += " and flowordertype="+flowordertype; } sql +=
	 * " order by credate desc "; List<OrderFlow> orderFlowlist =
	 * jdbcTemplate.query(sql, new OrderFlowRealRowMapper()); return
	 * orderFlowlist.size()>0?orderFlowlist.get(0):new OrderFlow();
	 * }catch(DataAccessException e){ return new OrderFlow(); } }
	 */

	public List<OrderFlow> getOrderFlowByCwbAndFlowordertype(long flowordertype, String cwb) {
		String sql = "select `cwb`,`branchid`,`credate`,`userid`,`flowordertype`,`isnow`,`outwarehouseid`,`comment` from express_ops_order_flow  where flowordertype=? and cwb = ?";

		return this.jdbcTemplate.query(sql, new OrderFlowRowMapperNotDetail(), flowordertype, cwb);
	}

	@DataSource(DatabaseType.REPLICA)
	public List<String> getOrderFlowByCredateAndFlowordertype(String begindate, String enddate, long flowordertype, final String[] operationOrderResultTypes, final String[] dispatchbranchids,
			long nextbranchid, long deliverid) {
		String sql = "select * from express_ops_order_flow FORCE INDEX(FlowCredateIdx)  where flowordertype=" + flowordertype;
		StringBuilder deliverystatesql = new StringBuilder();
		sql += " and credate >= '" + begindate + "' ";
		sql += " and credate <= '" + enddate + "' ";

		if (nextbranchid > 0) {
			deliverystatesql.append(" and floworderdetail LIKE '%\"nextbranchid\":" + nextbranchid + ",%' ");
		}

		if (deliverid > 0) {
			deliverystatesql.append(" and floworderdetail LIKE '%\"deliverid\":" + deliverid + ",%' ");
		}

		sql += deliverystatesql.toString();
		final List<String> cwbList = new ArrayList<String>();
		this.jdbcTemplate.query(new StreamingStatementCreator(sql), new RowCallbackHandler() {
			@Override
			public void processRow(ResultSet rs) throws SQLException {
				try {
					boolean isTrue = true;
					// CwbOrderWithDeliveryState
					// cwbOrderWithDeliveryState=om.readValue(rs.getString("floworderdetail"),
					// CwbOrderWithDeliveryState.class);
					if (operationOrderResultTypes.length > 0) {
						isTrue = false;
						for (String operationOrderResultType : operationOrderResultTypes) {
							String dsate = "deliverystate\":" + operationOrderResultType + ",\"cash";
							String dsate2 = "deliveryState\":null";
							if ((rs.getString("floworderdetail").indexOf(dsate) > -1) || (rs.getString("floworderdetail").indexOf(dsate2) > -1)) {
								isTrue = true;
								break;
							}
						}
					}
					if ((dispatchbranchids.length > 0) && isTrue) {
						isTrue = false;
						for (String dispatchbranchid : dispatchbranchids) {
							String deliverybranchidstr = "deliverybranchid\":" + dispatchbranchid + ",";
							if (rs.getString("floworderdetail").indexOf(deliverybranchidstr) > -1) {
								isTrue = true;
								break;
							}
						}

					}
					if (isTrue) {
						cwbList.add(rs.getString("cwb"));
					}

				} catch (Exception e) {
					logger.error("", e);
				}
			}

		});
		return cwbList;
	}

	public List<String> getOrderByAuditTime(String begindate, String enddate, String[] operationOrderResultTypes, long branchid, long deliverid) {
		String sql = "select ds.cwb from express_ops_delivery_state as ds left join express_ops_goto_class_auditing as au on ds.gcaid = au.id  " + "  where ds.state=1 ";
		StringBuilder deliverystatesql = new StringBuilder();
		if (begindate.length() > 0) {
			deliverystatesql.append(" and au.auditingtime >= '" + begindate + "' ");
		}
		if (enddate.length() > 0) {
			deliverystatesql.append(" and au.auditingtime <= '" + enddate + "' ");
		}

		if (operationOrderResultTypes.length > 0) {
			String deliverystates = "";
			for (String deliverystate : operationOrderResultTypes) {
				deliverystates += deliverystate + ",";
			}
			deliverystates = deliverystates.substring(0, deliverystates.length() - 1);
			deliverystatesql.append(" and ds.deliverystate in(" + deliverystates + ") ");
		}
		if (branchid > 0) {
			deliverystatesql.append(" and ds.deliverybranchid=" + branchid + " ");
		}
		if (deliverid > 0) {
			deliverystatesql.append(" and ds.deliveryid=" + deliverid + " ");
		}
		sql += deliverystatesql.toString();

		return this.jdbcTemplate.queryForList(sql, String.class);
	}

	public List<String> getOrderFlowBySome(String begindate, String enddate, String flowordertypes, String currentBranchids, long isnowdata) {
		if(currentBranchids.equals("")){
			currentBranchids="''";
		}
		String sql = "select cwb from express_ops_order_flow FORCE INDEX(FlowCredateIdx)  where flowordertype in(" + flowordertypes + ") " + " and credate >= '" + begindate + "'  and credate <= '"
				+ enddate + "' and branchid in(" + currentBranchids + ")";

		if (isnowdata > 0) {
			sql += " and isnow =1 ";
		}
		return this.jdbcTemplate.queryForList(sql, String.class);
	}

	public List<String> getOrderFlowForOutwarehouse(String begindate, String enddate, long flowordertype, final String[] nextbranchids, final String[] startbranchids) {
		String sql = "select * from express_ops_order_flow FORCE INDEX(FlowCredateIdx)  where flowordertype =" + flowordertype;
		StringBuilder deliverystatesql = new StringBuilder();
		sql += " and credate >= '" + begindate + "' ";
		sql += " and credate <= '" + enddate + "' ";
		/*
		 * if(nextbranchids.length>0){ for(String nextbranchid:nextbranchids){
		 * deliverystatesql
		 * .append(" and floworderdetail LIKE '%\"nextbranchid\":"
		 * +nextbranchid+",%' "); } } if(startbranchids.length>0){ for(String
		 * startbranchid:startbranchids){
		 * deliverystatesql.append(" and floworderdetail LIKE '%\"startbranchid\":"
		 * +startbranchid+",%' "); } }
		 */
		sql += deliverystatesql.toString();
		final List<String> cwbList = new ArrayList<String>();
		this.jdbcTemplate.query(new StreamingStatementCreator(sql), new RowCallbackHandler() {
			@Override
			public void processRow(ResultSet rs) throws SQLException {
				try {
					boolean isTrue = true;
					// CwbOrderWithDeliveryState
					// cwbOrderWithDeliveryState=om.readValue(rs.getString("floworderdetail"),
					// CwbOrderWithDeliveryState.class);
					if (nextbranchids.length > 0) {
						isTrue = false;
						for (String nextbranchid : nextbranchids) {
							String nextbranchidstr = "nextbranchid\":" + nextbranchid + ",";
							if (rs.getString("floworderdetail").indexOf(nextbranchidstr) > -1) {
								isTrue = true;
								break;
							}
						}
					}
					if ((startbranchids.length > 0) && isTrue) {
						isTrue = false;
						for (String startbranchid : startbranchids) {
							String startbranchidstr = "startbranchid\":" + startbranchid + ",";
							if (rs.getString("floworderdetail").indexOf(startbranchidstr) > -1) {
								isTrue = true;
								break;
							}
						}

					}
					if (isTrue) {
						cwbList.add(rs.getString("cwb"));
					}

				} catch (Exception e) {
					logger.error("", e);
				}
			}

		});
		return cwbList;
		// jdbcTemplate.queryForList(sql, String.class);
	}

	@DataSource(DatabaseType.REPLICA)
	public List<String> getOrderFlowForZhongZhuan(String begindate, String enddate, long flowordertype, final String[] nextbranchids, final String[] startbranchids) {
		String sql = "select * from express_ops_order_flow FORCE INDEX(FlowCredateIdx)  where flowordertype =" + flowordertype;
		StringBuilder deliverystatesql = new StringBuilder();
		sql += " and credate >= '" + begindate + "' ";
		sql += " and credate <= '" + enddate + "' ";

		/*
		 * if(nextbranchids.length>0){ for(String nextbranchid:nextbranchids){
		 * deliverystatesql
		 * .append(" and floworderdetail LIKE '%\"nextbranchid\":"
		 * +nextbranchid+",%' "); } } if(startbranchids.length>0){ for(String
		 * startbranchid:startbranchids){
		 * deliverystatesql.append(" and floworderdetail LIKE '%\"startbranchid\":"
		 * +startbranchid+",%' "); } }
		 */

		sql += deliverystatesql.toString();
		final List<String> cwbList = new ArrayList<String>();
		this.jdbcTemplate.query(new StreamingStatementCreator(sql), new RowCallbackHandler() {
			@Override
			public void processRow(ResultSet rs) throws SQLException {
				try {
					boolean isTrue = true;
					// CwbOrderWithDeliveryState
					// cwbOrderWithDeliveryState=om.readValue(rs.getString("floworderdetail"),
					// CwbOrderWithDeliveryState.class);
					if (nextbranchids.length > 0) {
						isTrue = false;
						for (String nextbranchid : nextbranchids) {
							String nextbranchidstr = "nextbranchid\":" + nextbranchid + ",";
							if (rs.getString("floworderdetail").indexOf(nextbranchidstr) > -1) {
								isTrue = true;
								break;
							}
						}
					}
					if ((startbranchids.length > 0) && isTrue) {
						isTrue = false;
						for (String startbranchid : startbranchids) {
							String startbranchidstr = "startbranchid\":" + startbranchid + ",";
							if (rs.getString("floworderdetail").indexOf(startbranchidstr) > -1) {
								isTrue = true;
								break;
							}
						}

					}
					if (isTrue) {
						cwbList.add(rs.getString("cwb"));
					}

				} catch (Exception e) {
					logger.error("", e);
				}
			}

		});
		return cwbList;
	}

	@DataSource(DatabaseType.REPLICA)
	public List<OrderFlow> getOrderFlowForZhanDianChuZhan(String begindate, String enddate, final String[] startbranchids, final String[] nextbranchids, final long flowordertype) {
		String sql = "select * from express_ops_order_flow FORCE INDEX(FlowCredateIdx) where credate >= '" + begindate + "' ";
		StringBuilder deliverystatesql = new StringBuilder();
		sql += " and credate <= '" + enddate + "' ";

		sql += deliverystatesql.toString();
		final List<OrderFlow> cwbList = new ArrayList<OrderFlow>();
		this.jdbcTemplate.query(new StreamingStatementCreator(sql), new RowCallbackHandler() {
			@Override
			public void processRow(ResultSet rs) throws SQLException {
				try {
					boolean isTrue = true;
					if (nextbranchids.length > 0) {
						isTrue = false;
						for (String nextbranchid : nextbranchids) {
							String nextbranchidstr = "nextbranchid\":" + nextbranchid + ",";
							if (rs.getString("floworderdetail").indexOf(nextbranchidstr) > -1) {
								isTrue = true;
								break;
							}
						}
					}
					if ((startbranchids.length > 0) && isTrue) {
						isTrue = false;
						for (String startbranchid : startbranchids) {
							String startbranchidstr = "startbranchid\":" + startbranchid + ",";
							if (rs.getString("floworderdetail").indexOf(startbranchidstr) > -1) {
								isTrue = true;
								break;
							}
						}
					}
					if ((flowordertype > 0) && isTrue) {
						isTrue = false;
						String flowordertypestr = "flowordertype\":" + flowordertype + ",";
						if (rs.getString("floworderdetail").indexOf(flowordertypestr) > -1) {
							isTrue = true;
						}

					}

					if (isTrue) {
						OrderFlow of = new OrderFlow();
						of.setBranchid(rs.getLong("branchid"));
						of.setComment(rs.getString("comment"));
						of.setCredate(rs.getDate("credate"));
						of.setCwb(rs.getString("cwb"));
						of.setFloworderdetail(rs.getString("floworderdetail"));
						of.setFloworderid(rs.getLong("floworderid"));
						of.setFlowordertype(rs.getInt("flowordertype"));
						of.setIsnow(rs.getInt("isnow"));
						of.setUserid(rs.getLong("userid"));
						cwbList.add(of);
					}

				} catch (Exception e) {
					logger.error("", e);
				}
			}

		});
		return cwbList;
	}

	public List<OrderFlow> getAdvanceOrderFlowByCwb(String cwb) {
		return this.jdbcTemplate.query("select `cwb`,`branchid`,`credate`,`userid`,`flowordertype`,`isnow`,`outwarehouseid`,`comment` from express_ops_order_flow where cwb= '" + cwb
				+ "'  order by  credate ASC", new OrderFlowRowMapperNotDetail());

	}

	// modi by wangych 20140721 增加“系统自动处理”查询条件
	private String getOpreateOrderFlowByWhereSql(String sql, String branchids, long flowordertype, String beginemaildate, String endemaildate, String autodetailflag) {
		if ((branchids.length() > 0) || (beginemaildate.length() > 0) || (endemaildate.length() > 0) || (flowordertype > 0) || (autodetailflag.length() > 0)) {
			StringBuffer w = new StringBuffer();
			sql += " where ";
			if (branchids.length() > 0) {
				w.append(" and branchid in(" + branchids + ")");
			}
			if (beginemaildate.length() > 0) {
				w.append(" and credate >= '" + beginemaildate + "'");
			}
			if (endemaildate.length() > 0) {
				w.append(" and credate <= '" + endemaildate + "'");
			}
			if (flowordertype > 0) {
				w.append(" and flowordertype=" + flowordertype);
			}
			if ("1".equals(autodetailflag)) {
				w.append("");
			} else if ("2".equals(autodetailflag)) {
				w.append(" and comment = '系统自动处理'");
			} else if ("3".equals(autodetailflag)) {
				w.append(" and comment != '系统自动处理'");
			}
			sql += w.substring(4, w.length());
		}
		return sql;
	}

	// modi by wangych 20140721 增加“系统自动处理”查询条件
	public List<OrderFlow> getOperateOrderFlowByWhere(long page, String branchids, long flowordertype, String beginemaildate, String endemaildate, String autodetailflag) {
		String sql = "select * from express_ops_order_flow ";
		sql = this.getOpreateOrderFlowByWhereSql(sql, branchids, flowordertype, beginemaildate, endemaildate, autodetailflag);
		sql += " limit " + ((page - 1) * Page.ONE_PAGE_NUMBER) + " ," + Page.ONE_PAGE_NUMBER;
		return this.jdbcTemplate.query(sql, new OrderFlowRealRowMapper());
	}

	// modi by wangych 20140721 增加“系统自动处理”查询条件
	public long getOperateOrderFlowByWhereCount(String branchids, long flowordertype, String beginemaildate, String endemaildate, String autodetailflag) {
		String sql = "select  count(1) from express_ops_order_flow ";
		sql = this.getOpreateOrderFlowByWhereSql(sql, branchids, flowordertype, beginemaildate, endemaildate, autodetailflag);
		return this.jdbcTemplate.queryForInt(sql);
	}

	// modi by wangych 20140721 增加“系统自动处理”查询条件
	public List<OrderFlow> downloadOperateOrderFlowByWhere(String branchids, long flowordertype, String beginemaildate, String endemaildate, String autodetailflag) {
		String sql = "select * from express_ops_order_flow ";
		sql = this.getOpreateOrderFlowByWhereSql(sql, branchids, flowordertype, beginemaildate, endemaildate, autodetailflag);
		return this.jdbcTemplate.query(sql, new OrderFlowRealRowMapper());
	}

	// ============统计到站无结果的订单===五步===begin===============
	@DataSource(DatabaseType.REPLICA)
	public List<String> getOneCwbs(String flowordertypes, String begindate, String enddate, String kufangids, String branchids) {
		String sql = "SELECT DISTINCT d.cwb FROM `express_ops_order_flow` AS f FORCE INDEX(FlowCredateIdx) LEFT JOIN `express_ops_cwb_detail` AS d ON f.cwb=d.cwb " + "WHERE f.flowordertype in("
				+ flowordertypes + ") and d.state=1  AND f.credate>=? AND f.credate<=?";

		StringBuilder deliverystatesql = new StringBuilder();
		if ((kufangids.length() > 0) || (branchids.length() > 0)) {
			if (branchids.length() > 0) {
				sql += " AND d.startbranchid IN(" + branchids + ") ";
			}
			if (kufangids.length() > 0) {
				sql += " AND d.`carwarehouse` IN(" + kufangids + ")";
			}
		}
		sql += deliverystatesql.toString();
		return this.jdbcTemplate.queryForList(sql, String.class, begindate, enddate);
	}

	@DataSource(DatabaseType.REPLICA)
	public List<String> getTwoCwbs(String flowordertypes, String cwbs, String enddate) {
		String sql = "SELECT DISTINCT cwb FROM express_ops_order_flow WHERE cwb IN(" + cwbs + ")" + " AND flowordertype in(" + flowordertypes + ") AND credate>? ";

		return this.jdbcTemplate.queryForList(sql, String.class, enddate);
	}

	@DataSource(DatabaseType.REPLICA)
	public List<String> getThreeCwbs(String flowordertypes, String cwbs, String noIncwbs) {
		String sql = "SELECT DISTINCT cwb FROM express_ops_order_flow WHERE cwb IN(" + cwbs + ")" + " AND cwb NOT IN(" + noIncwbs + ") AND flowordertype in(" + flowordertypes + ") ";
		return this.jdbcTemplate.queryForList(sql, String.class);
	}

	@DataSource(DatabaseType.REPLICA)
	public List<String> getFourCwbs(String cwbs) {
		String sql = "SELECT DISTINCT cwb  FROM `express_ops_delivery_state` WHERE cwb IN(" + cwbs + ") " + "AND state=1 AND `deliverystate`>0";
		return this.jdbcTemplate.queryForList(sql, String.class);
	}

	@DataSource(DatabaseType.REPLICA)
	public List<String> getFiveCwbs(String flowordertypes, String cwbs, String noIncwbs, long page) {
		String sql = "SELECT DISTINCT cwb FROM express_ops_order_flow WHERE cwb IN(" + cwbs + ")  " + "AND cwb NOT IN(" + noIncwbs + ") AND flowordertype in(" + flowordertypes + ") ";
		if (page > 0) {
			sql += " limit " + ((page - 1) * Page.ONE_PAGE_NUMBER) + " ," + Page.ONE_PAGE_NUMBER;
		}
		return this.jdbcTemplate.queryForList(sql, String.class);
	}

	@DataSource(DatabaseType.REPLICA)
	public long getFiveCwbsCount(String flowordertypes, String cwbs, String noIncwbs) {
		String sql = "SELECT count(DISTINCT cwb) FROM express_ops_order_flow WHERE cwb IN(" + cwbs + ")  " + "AND cwb NOT IN(" + noIncwbs + ") AND flowordertype in(" + flowordertypes + ") ";
		return this.jdbcTemplate.queryForLong(sql);
	}

	// ============统计到站无结果的订单===五步===end===============

	public long getOrderFlowByCredateAndFlowordertypeCount(long branchid, String begindate, String enddate, long flowordertype, long deliverid) {
		String sql = "select count(1) from express_ops_order_flow  where flowordertype=" + flowordertype + " and isnow=1";
		StringBuilder deliverystatesql = new StringBuilder();

		if (branchid > 0) {
			deliverystatesql.append(" and branchid=" + branchid);
		}
		// 强制索引
		if ((begindate.length() > 0) || (enddate.length() > 0)) {
			sql = sql.replace("express_ops_order_flow", "express_ops_order_flow FORCE INDEX(FlowCredateIdx)");
		}
		if (begindate.length() > 0) {
			deliverystatesql.append(" and credate >= '" + begindate + "' ");
		}
		if (enddate.length() > 0) {
			deliverystatesql.append(" and credate <= '" + enddate + "' ");
		}

		if (deliverid > 0) {
			deliverystatesql.append(" and floworderdetail LIKE '%\"deliverid\":" + deliverid + ",%' ");
		}

		sql += deliverystatesql.toString();

		return this.jdbcTemplate.queryForLong(sql);
	}

	public List<OrderFlow> getCwbsByFlowordertype(FlowOrderTypeEnum flowordertype, String cwbs) {
		String sql = "SELECT `cwb`,`branchid`,`credate`,`userid`,`flowordertype`,`isnow`,`outwarehouseid`,`comment` FROM express_ops_order_flow WHERE cwb IN(" + cwbs + ")" + " AND flowordertype="
				+ flowordertype.getValue() + " ORDER BY floworderid ";
		return this.jdbcTemplate.query(sql, new OrderFlowRowMapperNotDetail());
	}

	public List<OrderFlow> getCwbsByDateAndFlowtype(String emailStartTime, String eamilEndTime, long flowordertype) {
		String sql = "SELECT  `cwb`,`branchid`,`credate`,`userid`,`flowordertype`,`isnow`,`outwarehouseid`,`comment` FROM express_ops_order_flow FORCE INDEX(FlowCredateIdx) WHERE flowordertype =? "
				+ " AND credate>=? AND credate<=? and isnow=1 ";

		return this.jdbcTemplate.query(sql, new OrderFlowRowMapperNotDetail(), flowordertype, emailStartTime, eamilEndTime);
	}

	public List<String> getZaituCwbsByDateAndFlowtype(String emailStartTime, String eamilEndTime, long flowordertype, String branchids) {
		String sql = "SELECT  cwb FROM express_ops_order_flow FORCE INDEX(FlowCredateIdx) WHERE flowordertype =? " + " AND credate>=? AND credate<=? and branchid in (" + branchids + ") and isnow=1 ";

		return this.jdbcTemplate.queryForList(sql, String.class, flowordertype, emailStartTime, eamilEndTime);
	}

	@DataSource(DatabaseType.REPLICA)
	public List<String> getIntoCwbDetailAndOrderFlow(String begindate, String enddate, long flowordertype, long branchid) {
		String sql = "select cwb from express_ops_order_flow where flowordertype=" + flowordertype + " and credate >= '" + begindate + "' and credate <= '" + enddate + "' ";

		if (branchid > 0) {
			sql += " and branchid =" + branchid;
		}
		return this.jdbcTemplate.queryForList(sql, String.class);
	}

	public String getCwbDetailAndOrderFlowByParamSQL(long page, String customers, String emaildatebegin, String emaildateend, String begindate, String enddate, long flowordertype, long branchid,
			long cwbordertypeid) {
		String sql = "select cd.* from express_ops_cwb_detail cd left join express_ops_order_flow of on cd.cwb=of.cwb" + " where cd.state=1 and of.flowordertype=" + flowordertype
				+ " and of.credate >= '" + begindate + "'" + " and of.credate <= '" + enddate + "'";
		StringBuilder deliverystatesql = new StringBuilder();
		if (cwbordertypeid > -2) {
			deliverystatesql.append(" and cd.cwbordertypeid=" + cwbordertypeid);
		}
		if (customers.length() > 0) {
			deliverystatesql.append(" and cd.customerid in(" + customers + ")");
		}
		if (branchid > 0) {
			deliverystatesql.append(" and of.branchid =" + branchid);
		}
		if (emaildatebegin.length() > 0) {
			deliverystatesql.append(" and cd.emaildate>='" + emaildatebegin + "'");
		}
		if (emaildateend.length() > 0) {
			deliverystatesql.append(" and cd.emaildate<='" + emaildateend + "'");
		}

		sql += deliverystatesql.toString();
		sql += " GROUP BY cd.cwb";
		sql += " limit " + page + " ," + Page.EXCEL_PAGE_NUMBER;

		return sql;
	}

	public OrderFlow getOrderFlowByParam(long flowordertype, String cwb) {
		try {
			String sql = "select * from express_ops_order_flow where flowordertype=? and cwb=? order by  credate DESC";
			return this.jdbcTemplate.query(sql, new OrderFlowRealRowMapper(), flowordertype, cwb).get(0);
		} catch (DataAccessException e) {
			return null;
		}
	}

	public OrderFlow getOrderFlowByIsnow(String cwb) {
		try {
			String sql = "select * from express_ops_order_flow where isnow=1 and cwb='" + cwb + "' ";
			return this.jdbcTemplate.query(sql, new OrderFlowRealRowMapper()).get(0);
		} catch (DataAccessException e) {
			return null;
		}
	}

	public List<OrderFlow> getOrderFlowByCwbs(List<String> cwbs) {
		StringBuilder stringBuilder = new StringBuilder("select `cwb`,`branchid`,`credate`,`userid`,`flowordertype`,`isnow`,`outwarehouseid`,`comment` from express_ops_order_flow where cwb in  (");
		for (String cwb : cwbs) {
			stringBuilder.append("'").append(cwb).append("',");
		}
		if (cwbs.size() > 0) {
			stringBuilder.deleteCharAt(stringBuilder.length() - 1);
		}
		stringBuilder.append(") ");
		return this.jdbcTemplate.query(stringBuilder.toString(), new OrderFlowRowMapperNotDetail());
	}

	// 站点到货汇总查询
	public List<String> getDaohuoOrderFlow(String begindate, String enddate, String flowordertypes, final long currentBranchid) {
		String sql = "select cwb from express_ops_order_flow FORCE INDEX(FlowCredateIdx)  where flowordertype in(" + flowordertypes + ")" + " and credate >= '" + begindate + "' and credate <= '"
				+ enddate + "' ";

		if (currentBranchid > 0) {
			sql += " and branchid = " + currentBranchid;
		}
		final List<String> cwbList = new ArrayList<String>();
		this.jdbcTemplate.query(new StreamingStatementCreator(sql), new RowCallbackHandler() {
			private StringBuffer cwbs = new StringBuffer();

			@Override
			public void processRow(ResultSet rs) throws SQLException {
				try {
					if (this.cwbs.indexOf("'" + rs.getString("cwb") + "'") > -1) {
						return;
					}
					this.cwbs = this.cwbs.append("'" + rs.getString("cwb") + "'");

					cwbList.add(rs.getString("cwb"));

				} catch (Exception e) {
					logger.error("", e);
				}
			}

		});
		return cwbList;
	}

	public List<OrderFlow> getCwbByFlowordertypeAndCwbs(int value, String cwbs) {
		if (cwbs.trim().length() > 0) {
			String sql = " select `cwb`,`branchid`,`credate`,`userid`,`flowordertype`,`isnow`,`outwarehouseid`,`comment` from express_ops_order_flow where flowordertype=?  and cwb in(" + cwbs
					+ ")  order by credate desc";
			return this.jdbcTemplate.query(sql, new OrderFlowRowMapperNotDetail(), value);
		} else {
			return new ArrayList<OrderFlow>();
		}
	}

	public OrderFlow getOrderFlowByCwbAndFlowtype(String cwb, String flowordertypes) {
		try {
			String sql = "select * from express_ops_order_flow where  cwb=? and flowordertype in(" + flowordertypes + ") order by credate desc limit 0,1";
			return this.jdbcTemplate.queryForObject(sql, new OrderFlowRowMapperCwbAndCredate(), cwb);
		} catch (DataAccessException e) {
			return null;
		}
	}

	public OrderFlow queryFlow(String cwb, FlowOrderTypeEnum flowType) {
		try {
			String sql = "select * from express_ops_order_flow where cwb= ? and flowordertype = ? order by credate desc limit 1";
			return this.jdbcTemplate.queryForObject(sql, new OrderFlowRowMapper(), cwb, flowType.getValue());
		} catch (DataAccessException e) {
			return null;
		}
	}

	public void updateCommentByCwb(String cwb, String comment) {
		String sql = "update express_ops_order_flow set comment=? where cwb=? and isnow=1 ";
		this.jdbcTemplate.update(sql, comment, cwb);

	}

	/**
	 * 批量插入订单超区流程.
	 */
	public void batchOutArea(String[] cwbs, long reportOutAreaBranchId, long reportOutAreaUserId, Map<String, Long> branchMap) {
		this.batchInsertOutAreaFlow(cwbs, reportOutAreaBranchId, reportOutAreaUserId, branchMap);
	}

	private void batchInsertOutAreaFlow(String[] cwbs, long reportOutAreaBranchId, long reportOutAreaUserId, Map<String, Long> branchMap) {
		this.batchRestNowFlow(cwbs);
		this.batchInsertOrderFlow(cwbs, reportOutAreaBranchId, reportOutAreaUserId);
		this.batchUpdateOperationTime(cwbs, reportOutAreaBranchId, branchMap);
	}

	private void batchInsertOrderFlow(String[] cwbs, long reportOutAreaBranchId, long reportOutAreaUserId) {
		
		String cwbstr = getStrings(cwbs);
		String sql1 = "UPDATE express_ops_order_flow SET  floworderdetail ='0' WHERE  flowordertype="+FlowOrderTypeEnum.ChaoQu.getValue()+" and cwb in("+cwbstr+") ";
		this.jdbcTemplate.update(sql1);
		StringBuilder sql = new StringBuilder();
		sql.append("insert into express_ops_order_flow(cwb , branchid , credate , userid,flowordertype ,isnow,floworderdetail)values(? , ");
		sql.append(Long.toString(reportOutAreaBranchId) + " , ");
		sql.append("'" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + " ', ");
		sql.append(Long.toString(reportOutAreaUserId) + " , ");
		sql.append(Integer.toString(FlowOrderTypeEnum.ChaoQu.getValue()) + ",");
		sql.append(Integer.toString(1) + ",'1')");

		this.jdbcTemplate.batchUpdate(sql.toString(), this.getOutAreaOrderParaList(cwbs));
	}

	private  String getStrings(String[] strArr) {
		String strs = "";
		if (strArr.length > 0) {
			for (String str : strArr) {
				strs += "'"+str + "',";
			}
		}

		if (strs.length() > 0) {
			strs = strs.substring(0, strs.length() - 1);
		}
		return strs;
	}
	private void batchRestNowFlow(String[] cwbs) {
		String sql = "update express_ops_order_flow set isnow = 0 where cwb in (" + this.getInPara(cwbs) + ")";
		this.jdbcTemplate.execute(sql);
	}

	private void batchUpdateOperationTime(String[] cwbs, long reportOutAreaBranchId, Map<String, Long> branchMap) {
		String sql = "update express_ops_operation_time set flowordertype = 60,branchid = ?,nextbranchid=? where cwb = ?";
		this.jdbcTemplate.batchUpdate(sql, this.getUpdateOptTimePara(cwbs, reportOutAreaBranchId, branchMap));
	}

	private List<Object[]> getUpdateOptTimePara(String[] cwbs, long reportOutAreaBranchId, Map<String, Long> branchMap) {
		List<Object[]> paraList = new ArrayList<Object[]>();
		for (String cwb : cwbs) {
			Object[] para = new Object[3];
			para[0] = reportOutAreaBranchId;
			para[1] = branchMap.get(cwb);
			para[2] = cwb;
			paraList.add(para);
		}
		return paraList;
	}

	private String getInPara(String[] cwbs) {
		StringBuilder inPara = new StringBuilder();
		for (String cwb : cwbs) {
			inPara.append("'" + cwb + "',");
		}
		return inPara.substring(0, inPara.length() - 1);
	}

	private List<Object[]> getOutAreaOrderParaList(String[] cwbs) {
		List<Object[]> paraList = new ArrayList<Object[]>();
		for (String cwb : cwbs) {
			Object[] objs = new Object[1];
			objs[0] = cwb.replace("'", "");
			paraList.add(objs);
		}
		return paraList;
	}

	public Map<String, Object> queryOrderFlowAndCredate(String cwb) {
		String sql = "select credate,flowordertype from express_ops_order_flow where cwb = '" + cwb + "' and isnow = 1";
		Map<String, Object> orderAndFlowMapping = new HashMap<String, Object>();
		this.jdbcTemplate.query(sql, new OrderFlowAndCredateHandler(orderAndFlowMapping));

		return orderAndFlowMapping;
	}

	private class OrderFlowAndCredateHandler implements RowCallbackHandler {

		Map<String, Object> orderAndFlowMapping = null;

		public OrderFlowAndCredateHandler(Map<String, Object> orderAndFlowMapping) {
			this.orderAndFlowMapping = orderAndFlowMapping;
		}

		@Override
		public void processRow(ResultSet rs) throws SQLException {
			String credate = rs.getString("credate");
			int flowType = rs.getInt("flowordertype");
			this.getOrderAndFlowMapping().put("credate", credate);
			this.getOrderAndFlowMapping().put("flowType", Integer.valueOf(flowType));
		}

		private Map<String, Object> getOrderAndFlowMapping() {
			return this.orderAndFlowMapping;
		}

	}

	public void deleteOrderFlow(String cwb, FlowOrderTypeEnum... flows) {
		if (cwb == null) {
			return;
		}
		if ((flows == null) || (flows.length == 0)) {
			return;
		}
		String sql = "delete from express_ops_order_flow where cwb = ? and " + this.getFlowOrderTypeCond(flows);
		this.jdbcTemplate.update(sql, cwb);
	}

	private String getFlowOrderTypeCond(FlowOrderTypeEnum... flows) {
		StringBuilder cond = new StringBuilder();
		if (flows.length == 1) {
			cond.append("flowordertype = ");
			cond.append(flows[0].getValue());
		} else {
			cond.append("flowordertype in (");
			cond.append(this.getInPara(flows));
			cond.append(")");
		}
		return cond.toString();
	}

	private String getInPara(FlowOrderTypeEnum... flows) {
		StringBuilder inPara = new StringBuilder();
		for (FlowOrderTypeEnum flow : flows) {
			inPara.append(flow.getValue());
			inPara.append(",");
		}
		return inPara.substring(0, inPara.length() - 1);
	}

	public void setOrderCurrentFlow(String cwb, FlowOrderTypeEnum flow) {
		String resetSql = "update express_ops_order_flow set isnow = 0 where cwb = ? and isnow = 1";
		this.jdbcTemplate.update(resetSql, cwb);
		String setSql = "update express_ops_order_flow set isnow = 1 where cwb = ? and flowordertype = " + flow.getValue();
		this.jdbcTemplate.update(setSql, cwb);
	}
	
	public void deleteOrderFlowByCwb(String cwb) {
		
		String sql = "delete from express_ops_order_flow where cwb = ? ";
		this.jdbcTemplate.update(sql, cwb);
	}
	
	public OrderFlow getRuKuTimeByCwb(String cwb) {
		
		String sql = "select * from express_ops_order_flow where cwb = ? and flowordertype="+4;
		OrderFlow lf=null;
		List<OrderFlow> lof=this.jdbcTemplate.query(sql,new OrderFlowRowMapper(),cwb);
		if(lof.size()>0&&lof!=null){
			lf=lof.get(0);
		}
		return lf;
	}

	public List<OrderFlow> getOrderByCredate(int flowordertype, String begindate,String enddate){
		String sql = "select * from express_ops_order_flow where flowordertype=? and credate>=? and credate<=?";
		return this.jdbcTemplate.query(sql, new OrderFlowRowMapperNotDetail(),flowordertype,begindate,enddate);
	}
	
	public OrderFlow getOrderFlowCwb(String cwb) {
		try {
			String sql = "select * from express_ops_order_flow where cwb=? and flowordertype IN(7,8) order by floworderid desc limit 1" ;
			return this.jdbcTemplate.queryForObject(sql, new OrderFlowRowMapper(),cwb);
		} catch (Exception e) {
			return null;
		}
	}
	

	/**
	 * 获取时间范围内订单流程记录数
	 * 
	 * @param begindate 开始日期
	 * @param enddate 结束日期
	 * @return
	 *
	 * @author jinghui.pan@pjbest.com
	 */
	public long getOrderFlowCountByCredate(String begindate, String enddate,FlowOrderTypeEnum... flows) {
		
		
		StringBuilder sqlBuilder = new StringBuilder();
		sqlBuilder.append("select count(1) from express_ops_order_flow FORCE INDEX(FlowCredateIdx)")
		.append(" where 1 = 1")
		.append(" and credate >= ? ")
		.append(" and credate <= ? ");
		
		if (flows != null && flows.length > 0) {
			sqlBuilder.append(" and " + this.getFlowOrderTypeCond(flows));
		}
		
		
		return this.jdbcTemplate.queryForLong(sqlBuilder.toString(), begindate,enddate);
	}
	/**
	 * 获取时间范围内订单流程记录数
	 * 
	 * @param begindate 开始日期
	 * @param enddate 结束日期
	 * @return
	 *
	 * @author jinghui.pan@pjbest.com
	 */
	public long getOrderFlowCountByCredate(String begindate, String enddate) {

//		StringBuilder sqlBuilder = new StringBuilder();
//		sqlBuilder.append("select count(1) from express_ops_order_flow FORCE INDEX(FlowCredateIdx)")
//		.append(" where 1 = 1")
//		.append(" and credate >= ? ")
//		.append(" and credate <= ? ");
		
		return getOrderFlowCountByCredate(begindate, enddate, null);
	}
	
	/**
	 * 获取以floworderid排倒序订单流程列表
	 * 
	 * @param begindate 开始日期
	 * @param enddate 结束日期
	 * @param page 页码
	 * @param pageSize 页数
	 * @return
	 *
	 * @author jinghui.pan@pjbest.com
	 */
	public List<OrderFlow> getOrderFlowByCredateAndPage(String begindate, String enddate, int page, int pageSize) {
		StringBuilder sqlBuilder = new StringBuilder();
		sqlBuilder.append("select * from express_ops_order_flow FORCE INDEX(FlowCredateIdx)")
		.append(" where 1 = 1")
		.append(" and credate >= ? ")
		.append(" and credate <= ?")
		.append(" order by floworderid desc ")
		.append(" limit ?, ? ");
		
		return this.jdbcTemplate.query(sqlBuilder.toString(), new OrderFlowRowMapper(), begindate,enddate,((page - 1) * pageSize), pageSize  );
	}
	
	
	public List<OrderFlow> getOrderFlowByCredateAndPage(String begindate, String enddate, int pageSize,long lastFloworderid) {
		
		return this.getOrderFlowByCredateAndPage(begindate, enddate, pageSize, lastFloworderid, null);
	}
	
	/**
	 * 获取以floworderid排倒序订单流程列表
	 * 
	 * @param begindate 开始日期
	 * @param enddate 结束日期
	 * @param page 页码
	 * @param pageSize 页数
	 * @return
	 *
	 * @author jinghui.pan@pjbest.com
	 */
	public List<OrderFlow> getOrderFlowByCredateAndPage(String begindate, String enddate, int pageSize,long lastFloworderid,FlowOrderTypeEnum... flows) {
		StringBuilder sqlBuilder = new StringBuilder();
		sqlBuilder.append("select * from express_ops_order_flow")
		.append(" where 1 = 1")
		.append(" and credate >= ? ")
		.append(" and credate <= ?");
		
		if(lastFloworderid > 0){
			sqlBuilder.append(" and floworderid < " ).append(lastFloworderid);
		}
		
		if (flows != null && flows.length > 0) {
			sqlBuilder.append(" and " + this.getFlowOrderTypeCond(flows));
		}
		
		
		
		sqlBuilder.append(" order by floworderid desc limit ?");
		
		return this.jdbcTemplate.query(sqlBuilder.toString(), new OrderFlowRowMapper(), begindate,enddate, pageSize  );
	}
	
	/**
	 * 查询指定客户的订单轨迹
	 */
	public List<OrderFlow> getOrderFlowByCwbAndCustomerid(String cwb,long customerid) {
		String sql="select * from express_ops_order_flow of WHERE of.cwb=? "
				+" AND EXISTS(select 1 from express_ops_cwb_detail cd where of.cwb=cd.cwb and cd.customerid=? )"
				+" order by  of.credate ASC,of.flowordertype asc";
		return this.jdbcTemplate.query(sql, new OrderFlowRowMapper(), cwb , customerid);
	}
}

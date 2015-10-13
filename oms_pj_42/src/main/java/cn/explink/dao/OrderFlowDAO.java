package cn.explink.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import cn.explink.domain.orderflow.OrderFlow;
import cn.explink.enumutil.DeliveryStateEnum;
import cn.explink.enumutil.FlowOrderTypeEnum;
import cn.explink.util.Page;
import cn.explink.util.StringUtil;

@Component
public class OrderFlowDAO {

	private final class OrderFlowRowMapper implements RowMapper<OrderFlow> {
		@Override
		public OrderFlow mapRow(ResultSet rs, int rowNum) throws SQLException {
			OrderFlow orderFlow = new OrderFlow();
			orderFlow.setFloworderid(rs.getLong("floworderid"));
			orderFlow.setCwb(StringUtil.nullConvertToEmptyString(rs.getString("cwb")));
			orderFlow.setBranchid(rs.getLong("branchid"));
			orderFlow.setCredate(rs.getTimestamp("credate"));
			orderFlow.setUserid(rs.getLong("userid"));
			orderFlow.setFlowordertype(rs.getInt("flowordertype"));
			orderFlow.setIsnow(rs.getInt("isnow"));
			orderFlow.setCustomerid(rs.getInt("customerid"));
			orderFlow.setEmaildate(rs.getTimestamp("emaildate"));
			orderFlow.setShiptime(rs.getTimestamp("shiptime"));
			orderFlow.setCaramount(rs.getBigDecimal("caramount"));
			orderFlow.setIsGo(rs.getInt("isGo"));
			orderFlow.setOutwarehouseid(rs.getInt("outwarehouseid"));
			orderFlow.setState(rs.getInt("state"));
			orderFlow.setEmaildateid(rs.getInt("emaildateid"));
			orderFlow.setCustomername(StringUtil.nullConvertToEmptyString(rs.getString("customername")));
			orderFlow.setStartbranchid(rs.getLong("startbranchid"));
			orderFlow.setNextbranchid(rs.getLong("nextbranchid"));
			orderFlow.setExcelbranchid(rs.getLong("excelbranchid"));
			orderFlow.setCarwarehouse(StringUtil.nullConvertToEmptyString(rs.getString("carwarehouse")));
			orderFlow.setStartbranchname(StringUtil.nullConvertToEmptyString(rs.getString("startbranchname")));
			orderFlow.setNextbranchname(StringUtil.nullConvertToEmptyString(rs.getString("nextbranchname")));
			orderFlow.setExcelbranchname(StringUtil.nullConvertToEmptyString(rs.getString("excelbranchname")));
			orderFlow.setDeliverid(rs.getLong("deliverid"));
			orderFlow.setDelivername(StringUtil.nullConvertToEmptyString(rs.getString("delivername")));
			orderFlow.setUsername(StringUtil.nullConvertToEmptyString(rs.getString("username")));
			orderFlow.setSigninman(StringUtil.nullConvertToEmptyString(rs.getString("signinman")));
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
			orderFlow.setFlowordertype(rs.getInt("flowordertype"));
			orderFlow.setIsnow(rs.getInt("isnow"));
			return orderFlow;
		}

	}

	private final class OrderFlowRealRowMapperB2C implements RowMapper<OrderFlow> {
		@Override
		public OrderFlow mapRow(ResultSet rs, int rowNum) throws SQLException {
			OrderFlow orderFlow = new OrderFlow();
			orderFlow.setFloworderid(rs.getLong("floworderid"));
			orderFlow.setCwb(rs.getString("cwb"));
			orderFlow.setBranchid(rs.getLong("branchid"));
			orderFlow.setCredate(rs.getTimestamp("credate"));
			orderFlow.setUserid(rs.getLong("userid"));
			orderFlow.setFlowordertype(rs.getInt("flowordertype"));
			orderFlow.setIsnow(rs.getInt("isnow"));
			orderFlow.setUsername(rs.getString("username"));
			return orderFlow;
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
	public long creOrderFlow(final OrderFlow of) {
		KeyHolder key = new GeneratedKeyHolder();
		jdbcTemplate.update(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(java.sql.Connection con) throws SQLException {
				PreparedStatement ps = null;
				ps = con.prepareStatement("insert into express_ops_order_flow (cwb,branchid,credate,userid,floworderdetail," + "flowordertype,isnow,customerid,emaildate,shiptime,"
						+ "caramount,isGo,emaildateid,customername,startbranchid," + "nextbranchid,excelbranchid,carwarehouse,startbranchname,excelbranchname,"
						+ "nextbranchname,deliverid,delivername,username) " + "values(?,?,?,?,?,?,?,?,?,?," + "?,?,?,?,?,?,?,?,?,?," + "?,?,?,?)", new String[] { "floworderid" });
				ps.setString(1, of.getCwb());
				ps.setLong(2, of.getBranchid());
				ps.setTimestamp(3, of.getCredate());
				ps.setLong(4, of.getUserid());
				ps.setString(5, of.getFloworderdetail() == null ? "" : of.getFloworderdetail().toString());
				ps.setInt(6, of.getFlowordertype());
				ps.setInt(7, of.getIsnow());
				ps.setInt(8, of.getCustomerid());
				ps.setTimestamp(9, of.getEmaildate());
				ps.setTimestamp(10, of.getShiptime());
				ps.setBigDecimal(11, of.getCaramount());
				ps.setInt(12, of.getIsGo());
				// ps.setLong(13, of.getFloworderid());
				ps.setLong(13, of.getEmaildateid());
				ps.setString(14, of.getCustomername());
				ps.setLong(15, of.getStartbranchid());
				ps.setLong(16, of.getNextbranchid());
				ps.setLong(17, of.getExcelbranchid());
				ps.setString(18, of.getCarwarehouse());
				ps.setString(19, of.getStartbranchname());
				ps.setString(20, of.getExcelbranchname());
				ps.setString(21, of.getNextbranchname());
				ps.setLong(22, of.getDeliverid());
				ps.setString(23, of.getDelivername());
				ps.setString(24, of.getUsername());
				return ps;
			}
		}, key);
		return key.getKey().longValue();
	}

	public long check(String cwb, int flowordertype, String credate) {
		String sql = "select count(1)  from express_ops_order_flow where cwb='" + cwb + "' and flowordertype=" + flowordertype + " and  credate ='" + credate + "'  ";
		return jdbcTemplate.queryForLong(sql);
	}

	public long creAndUpdateOrderFlow(OrderFlow of) {
		jdbcTemplate.update("update express_ops_order_flow set isnow='0' where cwb='" + of.getCwb() + "' and isnow='1'");
		return creOrderFlow(of);
	}

	public List<OrderFlow> getOrderFlowListByCwb(String cwb) {
		List<OrderFlow> orderFlowList = jdbcTemplate.query("SELECT of_b.*,u.username FROM express_set_user u RIGHT JOIN " + "(SELECT of.*,b.branchname FROM "
				+ "(SELECT * FROM express_ops_order_flow WHERE cwb=?) " + "of LEFT JOIN express_set_branch b ON b.branchid=of.branchid )" + " of_b ON of_b.userid=u.userid ", new OrderFlowRowMapper(),
				cwb);
		return orderFlowList;
	}

	public List<OrderFlow> getOrderFlowByFlowordertypeAndCwbAndIsgo(long flowordertype, String cwb) {
		String sql = "select * from express_ops_order_flow where flowordertype=" + flowordertype + " and cwb='" + cwb + "' ";
		return jdbcTemplate.query(sql, new OrderFlowRealRowMapperB2C());
	}

	public List<OrderFlow> getOrderFlowByFlowordertypeAndCwb(long flowordertype, String cwb, long branchid) {
		String sql = "select * from express_ops_order_flow where flowordertype=" + flowordertype + " and cwb='" + cwb + "' and branchid=" + branchid;
		return jdbcTemplate.query(sql, new OrderFlowRealRowMapper());
	}

	public List<OrderFlow> getOrderFlowByFlowordertype(long flowordertype) {
		String sql = "select * from express_ops_order_flow where flowordertype=" + flowordertype + " and isnow=0";
		return jdbcTemplate.query(sql, new OrderFlowRealRowMapper());
	}

	public OrderFlow getOrderFlowByCwbAndState(String cwb, long isnow) {
		String sql = "select * from express_ops_order_flow where cwb=" + cwb + " and isnow=" + isnow;
		return jdbcTemplate.queryForObject(sql, new OrderFlowRealRowMapper());
	}

	public long save(String cwb, long exportType) {

		return jdbcTemplate.update("update express_ops_order_flow set exportType=" + exportType + " where cwb ='" + cwb + "'");

	}

	public long saveBack(String cwb, long exportType) {

		return jdbcTemplate.update("update express_ops_order_flow set exportBackType=" + exportType + " where cwb ='" + cwb + "'");

	}

	public List<OrderFlow> getOrderFlowByCwb(String cwb) {
		return jdbcTemplate.query("select * from express_ops_order_flow where cwb= '" + cwb + "' and state=1 order by  credate ASC", new OrderFlowRowMapper());

	}

	public OrderFlow getOrderFlowById(long floworderid) {
		try {
			return jdbcTemplate.queryForObject("select * from express_ops_order_flow where floworderid= ?", new OrderFlowRowMapper(), floworderid);
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	public OrderFlow getOrderFlowByCwbIsnow(String cwb) {
		return jdbcTemplate.queryForObject("select * from express_ops_order_flow where cwb= '" + cwb + "' and isnow =1 and state=1", new OrderFlowRowMapper());

	}

	public long getOrderFlowByCwbCount(String cwb, String type) {
		return jdbcTemplate.queryForLong("select count(1) from express_ops_order_flow where cwb= '" + cwb + "' and flowordertype in(" + type + ")");
	}

	public long getOrderFlowDeliveryByCwbCount(String cwb, String type) {
		return jdbcTemplate.queryForLong("select count(1) from express_ops_order_flow where cwb= '" + cwb + "' and orderResultType in(" + type + ")");
	}

	public OrderFlow getOrderFlowByCwbO(String cwb) {
		try {
			return jdbcTemplate.queryForObject("select * from express_ops_order_flow where isGo=1 and state=1 and  cwb= '" + cwb + "' order by credate desc limit 1", new OrderFlowRowMapper());
		} catch (DataAccessException e) {
			// TODO Auto-generated catch block
			return null;
		}
	}

	// 配送成功时 ，修改签收人
	public void updateSignman(String signinman, String cwb, int flowordertype) {
		String sql = "update express_ops_order_flow set signinman=? where cwb=? and flowordertype=?";
		jdbcTemplate.update(sql, signinman, cwb, flowordertype);
	}

	public void updateSignmanDelivery(String signinman, String cwb, int orderResultType) {
		String sql = "update express_ops_order_flow set signinman=? where cwb=? and orderResultType=?";
		jdbcTemplate.update(sql, signinman, cwb, orderResultType);
	}

	// Jms数据失效
	public void updateLoseOrderFlow(int emaildateid) {
		String sql = "update express_ops_order_flow set state=0 where emaildateid=?";
		jdbcTemplate.update(sql, emaildateid);
	}

	// Jms修改站点
	public void updateOrderFlowBranchAndDeliverid(int branchid, String cwb) {
		String sql = "update express_ops_order_flow set branchid=? where state=1 and isnow = 1 and  cwb=? ";
		jdbcTemplate.update(sql, branchid, cwb);
	}

	/**
	 * 当当，查询跟踪日志反馈信息
	 * 
	 * @param conStr
	 * @return
	 */
	public List<OrderFlow> getTrackInfoListToSend(String customerids, int maxCount) {
		try {
			String sql = "select * from express_ops_order_flow where customerid in (" + customerids + ") and isgo=1 and send_b2c_flag in (0,2) " + " and flowordertype>1  LIMIT 0," + maxCount;

			return jdbcTemplate.query(sql, new OrderFlowRowMapper());
		} catch (DataAccessException e) {
			e.printStackTrace();
		}
		return new ArrayList<OrderFlow>();
	}

	// ---------------------------操作记录查询-------------------------
	public List<OrderFlow> getOrderFlowByPage(long page, long customerid, long startbranchid, String begindate, String enddate) {
		String sql = "select * from express_ops_order_flow ";
		sql = this.getOrderFlowByPageWhereSql(sql, customerid, startbranchid, begindate, enddate);
		sql += "limit " + (page - 1) * Page.ONE_PAGE_NUMBER + " ," + Page.ONE_PAGE_NUMBER;
		return jdbcTemplate.query(sql, new OrderFlowRowMapper());
	}

	private String getOrderFlowByPageWhereSql(String sql, long customerid, long startbranchid, String begindate, String enddate) {
		if (customerid > 0 || startbranchid > 0 || begindate.length() > 0 || enddate.length() > 0) {
			StringBuffer str = new StringBuffer();
			StringBuffer s = new StringBuffer();
			sql += " where ";
			if (customerid > 0) {
				str.append(" and  customerid =" + customerid);
			}
			if (startbranchid > 0) {
				str.append(" and  branchid =" + startbranchid);
			}
			if (begindate.length() > 0) {
				str.append(" and  credate > '" + begindate + "' ");
			}
			if (enddate.length() > 0) {
				str.append(" and  credate <'" + enddate + "' ");
			}
			str.append(" and flowordertype='" + FlowOrderTypeEnum.YiFanKui.getValue() + "' ");
			sql += str.substring(4, str.length());
		}
		return sql;
	}

	public long getOrderFlowCount(long customerid, long startbranchid, String begindate, String enddate) {
		String sql = "SELECT count(1) from express_ops_order_flow";
		sql = this.getOrderFlowByPageWhereSql(sql, customerid, startbranchid, begindate, enddate);
		return jdbcTemplate.queryForLong(sql);
	}

}

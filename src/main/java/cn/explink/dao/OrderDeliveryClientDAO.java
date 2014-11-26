package cn.explink.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import cn.explink.domain.OrderDeliveryClient;
import cn.explink.util.Page;

@Component
public class OrderDeliveryClientDAO {
	@Autowired
	private JdbcTemplate jdbcTemplate;

	private final class OrderDeliveryClientRowMapper implements RowMapper<OrderDeliveryClient> {
		@Override
		public OrderDeliveryClient mapRow(ResultSet rs, int rowNum) throws SQLException {
			OrderDeliveryClient orderDeliveryClient = new OrderDeliveryClient();
			orderDeliveryClient.setId(rs.getLong("id"));
			orderDeliveryClient.setCwb(rs.getString("cwb"));
			orderDeliveryClient.setClientid(rs.getLong("clientid"));
			orderDeliveryClient.setDeliveryid(rs.getLong("deliveryid"));
			orderDeliveryClient.setBranchid(rs.getLong("branchid"));
			orderDeliveryClient.setCreatetime(rs.getString("createtime"));
			orderDeliveryClient.setUserid(rs.getLong("userid"));
			orderDeliveryClient.setState(rs.getLong("state"));
			orderDeliveryClient.setDeleteuserid(rs.getLong("deleteuserid"));
			orderDeliveryClient.setDeletetime(rs.getString("deletetime"));
			return orderDeliveryClient;
		}
	}

	public void createOrderDeliveryClient(final OrderDeliveryClient orderDeliveryClient) {
		KeyHolder key = new GeneratedKeyHolder();
		jdbcTemplate.update(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(java.sql.Connection con) throws SQLException {
				PreparedStatement ps = null;
				ps = con.prepareStatement("INSERT INTO ops_order_delivery_client (id,cwb,clientid,deliveryid,branchid,createtime,userid,state) " + "values (?,?,?,?,?,?,?,?)",
						new String[] { "accountcwbid" });
				ps.setLong(1, orderDeliveryClient.getId());
				ps.setString(2, orderDeliveryClient.getCwb());
				ps.setLong(3, orderDeliveryClient.getClientid());
				ps.setLong(4, orderDeliveryClient.getDeliveryid());
				ps.setLong(5, orderDeliveryClient.getBranchid());
				ps.setString(6, orderDeliveryClient.getCreatetime());
				ps.setLong(7, orderDeliveryClient.getUserid());
				ps.setLong(8, orderDeliveryClient.getState());
				return ps;
			}
		}, key);
		orderDeliveryClient.setId(key.getKey().longValue());
		// return key.getKey().longValue();
	}

	public void deleteOrderDeliveryClientByCwb(String cwb) {
		jdbcTemplate.update("delete from ops_order_delivery_client where cwb = ?", cwb);
	}

	public List<OrderDeliveryClient> getOrderDeliveryClientList(long branchid, long state, long page) {
		String sql = "SELECT * FROM ops_order_delivery_client WHERE branchid=? and state=? order by id desc limit ?,? ";
		return jdbcTemplate.query(sql, new OrderDeliveryClientRowMapper(), branchid, state, (page - 1) * Page.DETAIL_PAGE_NUMBER, Page.DETAIL_PAGE_NUMBER);
	}

	public List<OrderDeliveryClient> getOrderDeliveryClientPage(long page, long branchid, long state, String cwb, String starttime, String endtime) {
		String sql = "SELECT * FROM ops_order_delivery_client WHERE branchid=? and state=?";
		sql = this.getOrderDeliveryClientPageWhere(sql, cwb, starttime, endtime);
		sql += " limit " + (page - 1) * Page.ONE_PAGE_NUMBER + " ," + Page.ONE_PAGE_NUMBER;
		return jdbcTemplate.query(sql, new OrderDeliveryClientRowMapper(), branchid, state);
	}

	public long getOrderDeliveryClientCount(long branchid, long state, String cwb, String starttime, String endtime) {
		String sql = "SELECT count(1) FROM ops_order_delivery_client WHERE branchid=? and state=?";
		sql = this.getOrderDeliveryClientPageWhere(sql, cwb, starttime, endtime);
		return jdbcTemplate.queryForLong(sql, branchid, state);
	}

	private String getOrderDeliveryClientPageWhere(String sql, String cwb, String starttime, String endtime) {
		StringBuffer sb = new StringBuffer(sql);
		if (!("").equals(cwb)) {
			sb.append(" and cwb IN (" + cwb + ")  ");
		}
		if (!("").equals(starttime)) {
			sb.append(" and deletetime >='" + starttime + "' ");
		}
		if (!("").equals(endtime)) {
			sb.append(" and deletetime <='" + endtime + "' ");
		}
		sb.append(" order by deletetime desc");
		return sb.toString();
	}

	public OrderDeliveryClient getOrderDeliveryClientByCwbLock(String cwb) {
		try {
			return jdbcTemplate.queryForObject("SELECT * from ops_order_delivery_client where cwb=? for update", new OrderDeliveryClientRowMapper(), cwb);
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	public void updateOrderDeliveryClientByCwb(long userid, String cwb, String deletetime) {
		jdbcTemplate.update("update ops_order_delivery_client set state=0,deletetime =?,deleteuserid=? where cwb = ?", deletetime, userid, cwb);
	}

	public List<OrderDeliveryClient> getOrderDeliveryClientByCwb(String cwb, long state) {
		String sql = "SELECT * FROM ops_order_delivery_client WHERE cwb=? AND state=?";
		return jdbcTemplate.query(sql, new OrderDeliveryClientRowMapper(), cwb, state);
	}

	public long getCountYiWeiPai(long branchid, long state, long clientid) {
		String sql = "SELECT count(1) FROM ops_order_delivery_client WHERE branchid=? and state=? ";
		if (clientid > 0) {
			sql += " and clientid=" + clientid;
		}

		return jdbcTemplate.queryForLong(sql, branchid, state);
	}

	public void updateFanKun(String cwb) {
		jdbcTemplate.update("update ops_order_delivery_client set state=2 where cwb = ?", cwb);
	}
}

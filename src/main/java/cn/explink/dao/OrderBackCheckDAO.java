package cn.explink.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import cn.explink.domain.OrderBackCheck;

@Component
public class OrderBackCheckDAO {
	@Autowired
	private JdbcTemplate jdbcTemplate;

	private final class OrderBackCheckRowMapper implements RowMapper<OrderBackCheck> {
		@Override
		public OrderBackCheck mapRow(ResultSet rs, int rowNum) throws SQLException {
			OrderBackCheck orderBackCheck = new OrderBackCheck();
			orderBackCheck.setId(rs.getLong("id"));
			orderBackCheck.setCheckstate(rs.getLong("checkstate"));
			orderBackCheck.setCwb(rs.getString("cwb"));
			orderBackCheck.setCustomerid(rs.getLong("customerid"));
			orderBackCheck.setCwbordertypeid(rs.getInt("cwbordertypeid"));
			orderBackCheck.setFlowordertype(rs.getLong("flowordertype"));
			orderBackCheck.setCwbstate(rs.getLong("cwbstate"));
			orderBackCheck.setConsigneename(rs.getString("consigneename"));
			orderBackCheck.setConsigneephone(rs.getString("consigneephone"));
			orderBackCheck.setConsigneeaddress(rs.getString("consigneeaddress"));
			orderBackCheck.setBackreason(rs.getString("backreason"));
			orderBackCheck.setUserid(rs.getLong("userid"));
			orderBackCheck.setCreatetime(rs.getString("createtime"));
			orderBackCheck.setCheckuser(rs.getLong("checkuser"));
			orderBackCheck.setCheckcreatetime(rs.getString("checkcreatetime"));
			orderBackCheck.setBranchid(rs.getLong("branchid"));
			return orderBackCheck;
		}
	}

	public void createOrderBackCheck(final OrderBackCheck orderBackCheck) {
		KeyHolder key = new GeneratedKeyHolder();
		jdbcTemplate.update(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(java.sql.Connection con) throws SQLException {
				PreparedStatement ps = null;
				ps = con.prepareStatement("insert into ops_order_back_check (checkstate,cwb,customerid,cwbordertypeid,flowordertype,"
						+ "cwbstate,consigneename,consigneephone,consigneeaddress,backreason,userid,createtime,branchid) " + "values (?,?,?,?,?,?,?,?,?,?,?,?,?)", new String[] { "recordid" });
				ps.setLong(1, orderBackCheck.getCheckstate());
				ps.setString(2, orderBackCheck.getCwb());
				ps.setLong(3, orderBackCheck.getCustomerid());
				ps.setInt(4, orderBackCheck.getCwbordertypeid());
				ps.setLong(5, orderBackCheck.getFlowordertype());
				ps.setLong(6, orderBackCheck.getCwbstate());
				ps.setString(7, orderBackCheck.getConsigneename());
				ps.setString(8, orderBackCheck.getConsigneephone());
				ps.setString(9, orderBackCheck.getConsigneeaddress());
				ps.setString(10, orderBackCheck.getBackreason());
				ps.setLong(11, orderBackCheck.getUserid());
				ps.setString(12, orderBackCheck.getCreatetime());
				ps.setLong(13, orderBackCheck.getBranchid());
				return ps;
			}
		}, key);
		orderBackCheck.setId(key.getKey().longValue());
		// return key.getKey().longValue();
	}

	public OrderBackCheck getOrderBackCheckById(long id) {
		String sql = "select * from ops_order_back_check where id = ? ";
		try {
			return jdbcTemplate.queryForObject(sql, new OrderBackCheckRowMapper(), id);
		} catch (Exception e) {
			return null;
		}
	}

	public void deleteOrderBackCheckByCwb(String cwb) {
		jdbcTemplate.update("delete from ops_order_back_check where cwb = ?", cwb);
	}

	public OrderBackCheck getOrderBackCheckByCwb(String cwb) {
		String sql = "select * from ops_order_back_check where cwb = ? ";
		try {
			return jdbcTemplate.queryForObject(sql, new OrderBackCheckRowMapper(), cwb);
		} catch (Exception e) {
			return null;
		}
	}

	public OrderBackCheck getOrderBackCheckByCwbAndBranch(String cwb, String branchid) {
		String sql = "select * from ops_order_back_check where cwb = ? and branchid in(" + branchid + ") and checkstate=0 ";
		try {
			return jdbcTemplate.queryForObject(sql, new OrderBackCheckRowMapper(), cwb);
		} catch (Exception e) {
			return null;
		}
	}

	public void updateOrderBackCheck(long id) {
		String sql = "UPDATE ops_order_back_check SET checkstate=1 where id =? ";
		jdbcTemplate.update(sql, id);
	}

	public List<OrderBackCheck> getOrderBackCheckList(String id) {
		String sql = "select * from ops_order_back_check where id in (" + id + ")";
		try {
			return jdbcTemplate.query(sql, new OrderBackCheckRowMapper());
		} catch (Exception e) {
			return null;
		}
	}

	public List<OrderBackCheck> getOrderBackCheckListByBranch(String branchid) {
		String sql = "select * from ops_order_back_check where branchid in (" + branchid + ") and checkstate=0";
		try {
			return jdbcTemplate.query(sql, new OrderBackCheckRowMapper());
		} catch (Exception e) {
			return null;
		}
	}
}

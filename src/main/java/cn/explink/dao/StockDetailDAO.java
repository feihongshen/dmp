package cn.explink.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import cn.explink.domain.StockDetail;

@Component
public class StockDetailDAO {
	private final class StockDetailRowMapper implements RowMapper<StockDetail> {
		@Override
		public StockDetail mapRow(ResultSet rs, int rowNum) throws SQLException {
			StockDetail stockDetail = new StockDetail();
			stockDetail.setBranchid(rs.getLong("branchid"));
			stockDetail.setUserid(rs.getLong("userid"));
			stockDetail.setCwb(rs.getString("cwb"));
			stockDetail.setId(rs.getLong("id"));
			stockDetail.setOrderflowid(rs.getLong("orderflowid"));
			stockDetail.setResultid(rs.getLong("resultid"));
			stockDetail.setType(rs.getLong("type"));
			stockDetail.setStocktype(rs.getLong("stocktype"));
			stockDetail.setState(rs.getString("state"));
			stockDetail.setTranscwb(rs.getString("transcwb"));
			return stockDetail;
		}
	}

	private final class StockAndCwbDetailRowMapper implements RowMapper<JSONObject> {
		@Override
		public JSONObject mapRow(ResultSet rs, int rowNum) throws SQLException {
			JSONObject obj = new JSONObject();
			obj.put("branchid", rs.getLong("branchid"));
			obj.put("userid", rs.getLong("userid"));
			obj.put("cwb", rs.getString("cwb"));
			obj.put("transcwb", rs.getString("transcwb"));
			obj.put("id", rs.getLong("id"));
			obj.put("orderflowid", rs.getLong("orderflowid"));
			obj.put("resultid", rs.getLong("resultid"));
			obj.put("type", rs.getLong("type"));
			obj.put("stocktype", rs.getLong("stocktype"));
			obj.put("state", rs.getString("state"));
			obj.put("customerid", rs.getLong("customerid"));
			obj.put("emaildate", rs.getString("emaildate"));
			obj.put("consigneename", rs.getString("consigneename"));
			obj.put("receivablefee", rs.getString("receivablefee"));
			obj.put("consigneeaddress", rs.getString("consigneeaddress"));
			return obj;
		}
	}

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public List<StockDetail> getAllStockDetail() {
		String sql = "select * from express_ops_stock_detail";
		return jdbcTemplate.query(sql, new StockDetailRowMapper());
	}

	public List<JSONObject> getAllStockDetailByWhere(long branchid, long type, String state, String stocktypes) {
		String sql = "SELECT sd.*,cd.`customerid`,cd.`emaildate`,cd.`consigneename`,cd.`receivablefee`,cd.`consigneeaddress` FROM express_ops_stock_detail sd LEFT JOIN express_ops_cwb_detail cd ON sd.`cwb`=cd.`cwb` WHERE sd.`branchid`=？ AND sd.`type`=? AND sd.`state`=? AND sd.`stocktype` IN("
				+ stocktypes + ") AND cd.`state`=1";
		return jdbcTemplate.query(sql, new StockAndCwbDetailRowMapper(), branchid, type, state);
	}

	public void saveStockDetailById(long type, long userid, long id) {
		String sql = "update express_ops_stock_detail set type=?,userid=? where id=?";
		jdbcTemplate.update(sql, type, userid, id);
	}

	public void saveStockDetailForResultId(long resultid, long branchid) {
		String sql = "update express_ops_stock_detail set resultid=? where branchid=? and state='1'";
		jdbcTemplate.update(sql, resultid, branchid);
	}

	public StockDetail getStockDetailByCwbAndBranchid(String cwb, String scancwb, long branchid, long type) {
		try {
			String sql = "select * from express_ops_stock_detail where cwb=? and branchid=? and state='1'";
			if (scancwb.length() == 0) {
				sql += " and type=" + type;
			} else {
				sql += " and transcwb='" + scancwb + "'";
			}
			List<StockDetail> stockDetaillist = jdbcTemplate.query(sql, new StockDetailRowMapper(), cwb, branchid);
			return stockDetaillist.isEmpty() ? null : stockDetaillist.get(0);
		} catch (DataAccessException e) {
			return null;
		}

	}

	public void creStockDetail(long branchid, String cwb, long orderflowid, long resultid, long type) {
		String sql = "insert into express_ops_stock_detail(branchid,cwb,orderflowid,resultid,type) values(?,?,?,?,?)";
		jdbcTemplate.update(sql, branchid, cwb, orderflowid, resultid, type);
	}

	public void deleteStockDetail(long resultid) {
		String sql = "delete from express_ops_stock_detail where resultid=?";
		jdbcTemplate.update(sql, resultid);
	}

	public void deleteStockDetailByType(int type) {
		String sql = "delete from express_ops_stock_detail where type=?";
		jdbcTemplate.update(sql, type);
	}

	public List<StockDetail> getStockDetailByParm(String cwb, long branchid, long orderflowid) {
		String sql = "select * from express_ops_stock_detail where cwb=? and branchid=? and orderflowid=?";
		return jdbcTemplate.query(sql, new StockDetailRowMapper(), cwb, branchid, orderflowid);
	}

	public void creStockDetailByParm(String cwb, String transcwb, long branchid, long orderflowid, long stocktype, long type) {
		String sql = "insert into express_ops_stock_detail(cwb,transcwb,branchid,orderflowid,stocktype,type) values(?,?,?,?,?,?)";
		jdbcTemplate.update(sql, cwb, transcwb, branchid, orderflowid, stocktype, type);
	}

	public void saveStockDetailStateByBranchid(String state, long branchid) {
		String sql = "update express_ops_stock_detail set state=? where branchid=?";
		jdbcTemplate.update(sql, state, branchid);
	}
}

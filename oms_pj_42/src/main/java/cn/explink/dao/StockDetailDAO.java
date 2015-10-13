package cn.explink.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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
			stockDetail.setCwb(rs.getString("cwb"));
			stockDetail.setId(rs.getLong("id"));
			stockDetail.setOrderflowid(rs.getLong("orderflowid"));
			stockDetail.setResultid(rs.getLong("resultid"));
			stockDetail.setType(rs.getLong("type"));
			return stockDetail;
		}
	}

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public List<StockDetail> getAllStockDetail() {
		String sql = "select * from express_ops_stock_detail";
		return jdbcTemplate.query(sql, new StockDetailRowMapper());
	}

	public void saveStockDetailByCwb(long type, String cwb) {
		String sql = "update express_ops_stock_detail set type=? where cwb=?";
		jdbcTemplate.update(sql, type, cwb);
	}

	public void saveStockDetailForResultId(long resultid, long branchid) {
		String sql = "update express_ops_stock_detail set resultid=? where branchid=?";
		jdbcTemplate.update(sql, resultid, branchid);
	}

	public List<StockDetail> getStockDetailByCwb(String cwb) {
		String sql = "select * from express_ops_stock_detail where cwb=" + cwb;
		return jdbcTemplate.query(sql, new StockDetailRowMapper());
	}

	public void creStockDetail(long branchid, String cwb, long orderflowid, long resultid, long type) {
		String sql = "insert into express_ops_stock_detail(branchid,cwb,orderflowid,resultid,type) values(?,?,?,?,?)";
		jdbcTemplate.update(sql, branchid, cwb, orderflowid, resultid, type);
	}

	public void deleteStockDetail(long resultid) {
		String sql = "delete from express_ops_stock_detail where resultid=" + resultid;
		jdbcTemplate.update(sql);
	}

	public void deleteStockDetailByType(int type) {
		String sql = "delete from express_ops_stock_detail where type=" + type;
		jdbcTemplate.update(sql);
	}
}

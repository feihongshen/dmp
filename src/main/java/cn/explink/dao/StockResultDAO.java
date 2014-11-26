package cn.explink.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import com.mysql.jdbc.Statement;

import cn.explink.domain.StockResult;

@Component
public class StockResultDAO {
	private final class StockResultRowMapper implements RowMapper<StockResult> {
		@Override
		public StockResult mapRow(ResultSet rs, int rowNum) throws SQLException {
			StockResult stockResult = new StockResult();
			stockResult.setId(rs.getLong("id"));
			stockResult.setBranchid(rs.getLong("branchid"));
			stockResult.setCheckcount(rs.getLong("checkcount"));
			stockResult.setRealcount(rs.getLong("realcount"));
			stockResult.setState(rs.getLong("state"));
			stockResult.setCreatetime(rs.getTimestamp("createtime"));

			return stockResult;
		}
	}

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public long creStockResult(final StockResult sr) {
		saveStockResultByBranchidAndState(sr.getBranchid(), 0);
		KeyHolder key = new GeneratedKeyHolder();
		jdbcTemplate.update(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(java.sql.Connection con) throws SQLException {
				PreparedStatement ps = null;
				ps = con.prepareStatement("insert into express_ops_stock_result(branchid,state,realcount) values(?,?,?)", Statement.RETURN_GENERATED_KEYS);
				ps.setLong(1, sr.getBranchid());
				ps.setLong(2, sr.getState());
				ps.setLong(3, sr.getRealcount());
				return ps;
			}
		}, key);
		return key.getKey().longValue();
	}

	public StockResult getAllStockResultByBranchidAndState(long branchid, long state) {
		String sql = "select * from express_ops_stock_result where branchid=? and state=?";
		try {
			return jdbcTemplate.queryForObject(sql, new StockResultRowMapper(), branchid, state);
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	public void saveStockResultByBranchidAndState(long branchid, long state) {
		String sql = "update express_ops_stock_result set state=1 where branchid=? and state=?";
		jdbcTemplate.update(sql, branchid, state);
	}

	public void saveStockResultById(long checkcount, long id) {
		String sql = "update express_ops_stock_result set checkcount=? where id=?";
		jdbcTemplate.update(sql, checkcount, id);
	}

	public void saveStockResultForRealcount(long checkcount, long realcount, long branchid) {
		String sql = "update express_ops_stock_result set checkcount=?,realcount=?,state=0 where branchid=? and state=1";
		jdbcTemplate.update(sql, checkcount, realcount, branchid);
	}

	public void deleteStockResult(long branchid) {
		String sql = "delete from express_ops_stock_result WHERE branchid=?";
		jdbcTemplate.update(sql, branchid);
	}
}

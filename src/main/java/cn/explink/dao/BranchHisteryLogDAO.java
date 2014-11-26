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

import cn.explink.domain.BranchHisteryLog;

@Component
public class BranchHisteryLogDAO {
	private final class BranchHisteryLogRowMapper implements RowMapper<BranchHisteryLog> {
		@Override
		public BranchHisteryLog mapRow(ResultSet rs, int rowNum) throws SQLException {
			BranchHisteryLog branchlog = new BranchHisteryLog();
			branchlog.setId(rs.getLong("id"));
			branchlog.setBranchlogid(rs.getLong("branchlogid"));
			branchlog.setLogtype(rs.getLong("logtype"));
			branchlog.setCwb(rs.getString("cwb"));
			return branchlog;
		}
	}

	@Autowired
	private JdbcTemplate jdbcTemplate;

	/**
	 * 按站点日志id和统计类型查询订单号
	 * 
	 * @param branchlogid
	 * @param type
	 * @return
	 */
	public List<BranchHisteryLog> getBranchHisteryLogBybranchlogidAndType(long branchlogid, long type) {
		String sql = "select * from express_ops_branch_histerylog where branchlogid=? and logtype=?";
		return jdbcTemplate.query(sql, new BranchHisteryLogRowMapper(), branchlogid, type);
	}

	/**
	 * 创建一条历史订单对应站点日志
	 * 
	 * @param bHisterylog
	 * @return
	 */
	public long creBranchHisteryLog(final BranchHisteryLog bHisterylog) {
		KeyHolder key = new GeneratedKeyHolder();
		jdbcTemplate.update(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(java.sql.Connection con) throws SQLException {
				PreparedStatement ps = null;
				ps = con.prepareStatement("INSERT INTO express_ops_branch_histerylog" + "(branchlogid,logtype,cwb) " + "VALUES " + "(?,?,?)", new String[] { "id" });
				ps.setLong(1, bHisterylog.getBranchlogid());
				ps.setLong(2, bHisterylog.getLogtype());
				ps.setString(3, bHisterylog.getCwb());
				return ps;
			}
		}, key);
		return key.getKey().longValue();
	}

}

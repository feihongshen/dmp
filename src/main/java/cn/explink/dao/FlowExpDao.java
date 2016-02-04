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

import cn.explink.domain.FlowExp;

@Component
public class FlowExpDao {
	private final class FlowExpMapper implements RowMapper<FlowExp> {
		@Override
		public FlowExp mapRow(ResultSet rs, int rowNum) throws SQLException {
			FlowExp flowexp = new FlowExp();
			flowexp.setId(rs.getLong("id"));	
			flowexp.setCwb(rs.getString("cwb"));	
			flowexp.setCretime(rs.getString("cretime"));	
			return flowexp;
		}
	}

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public long createFlowExp(final String cwb,final String cretime) {
		KeyHolder key = new GeneratedKeyHolder();
		this.jdbcTemplate.update(new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(java.sql.Connection con) throws SQLException {
				PreparedStatement ps = null;
				ps = con.prepareStatement("insert into express_ops_flowexp(cwb,cretime) values(?,?)", new String[] { "id" });
				ps.setString(1,cwb);
				ps.setString(2,cretime);
				return ps;
			}
		}, key);
		return key.getKey().longValue();
	}
	
	public List<FlowExp> getFlowExpByCwbs(String cwbs) {
		String sql = "select * from express_ops_flowexp where cwb in("+cwbs+")";
		return this.jdbcTemplate.query(sql, new FlowExpMapper());
	}
	
	public List<FlowExp> getFlowExpList() {
		String sql = "select * from express_ops_flowexp limit 0,1000";
		return this.jdbcTemplate.query(sql, new FlowExpMapper());
	}
	
	public void deteleFlowExp(String cwb) {
		String sql = "delete from express_ops_flowexp where cwb =?";
		this.jdbcTemplate.update(sql,cwb);
	}
	
	

}

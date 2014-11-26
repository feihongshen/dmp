package cn.explink.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import cn.explink.domain.WarehouseToBranch;

@Component
public class WarehouseToBranchDAO {

	private final class WarehouseToBranchRowMapper implements RowMapper<WarehouseToBranch> {
		@Override
		public WarehouseToBranch mapRow(ResultSet rs, int rowNum) throws SQLException {
			WarehouseToBranch warehtbranch = new WarehouseToBranch();
			warehtbranch.setId(rs.getLong("id"));
			warehtbranch.setCwb(rs.getString("cwb"));
			warehtbranch.setStartbranchid(rs.getLong("startbranchid"));
			warehtbranch.setNextbranchid(rs.getLong("nextbranchid"));
			warehtbranch.setCredate(rs.getString("credate"));
			warehtbranch.setType(rs.getInt("type"));
			return warehtbranch;
		}
	}

	@Autowired
	JdbcTemplate jdbcTemplate;

	public void creWarehouseToBranch(String cwb, long startbranchid, long nextbranchid, String credate, int type) {
		jdbcTemplate.update("insert into express_ops_warehouse_to_branch(cwb,startbranchid,nextbranchid,credate,type) " + " values(?,?,?,?,?)", cwb, startbranchid, nextbranchid, credate, type);
	}

	public void updateWarehouseToBranch(String cwb, long startbranchid, long nextbranchid, String credate, int type) {
		jdbcTemplate.update("update express_ops_warehouse_to_branch set startbranchid=?,nextbranchid=?,credate=? " + " where cwb=? and type=? and startbranchid=?", startbranchid, nextbranchid,
				credate, cwb, type, startbranchid);
	}

	public long getcwb(String cwb, long startbranchid) {
		return jdbcTemplate.queryForLong("select count(1) from  express_ops_warehouse_to_branch  " + " where cwb=? and startbranchid=? ", cwb, startbranchid);
	}

}

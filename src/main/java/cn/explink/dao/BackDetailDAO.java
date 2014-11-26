package cn.explink.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import cn.explink.domain.BackDetail;

@Component
public class BackDetailDAO {
	@Autowired
	private JdbcTemplate jdbcTemplate;

	private final class BackDetailRowMapper implements RowMapper<BackDetail> {
		@Override
		public BackDetail mapRow(ResultSet rs, int rowNum) throws SQLException {
			BackDetail o = new BackDetail();
			o.setId(rs.getLong("id"));
			o.setBranchid(rs.getLong("branchid"));
			o.setCwb(rs.getString("cwb"));
			o.setTime24(rs.getLong("time24"));
			o.setTime72(rs.getLong("time72"));
			o.setType(rs.getInt("type"));
			o.setIntoflag(rs.getInt("intoflag"));
			o.setCreatetime(rs.getString("createtime"));
			return o;
		}
	}

	public long createBackDetail(final BackDetail o) {
		KeyHolder key = new GeneratedKeyHolder();
		jdbcTemplate.update(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(java.sql.Connection con) throws SQLException {
				PreparedStatement ps = null;
				ps = con.prepareStatement("INSERT INTO `ops_back_detail` (branchid,cwb,time24,time72,type,intoflag,createtime) " + "VALUES (?,?,?,?,?,?,?);", new String[] { "id" });
				ps.setLong(1, o.getBranchid());
				ps.setString(2, o.getCwb());
				ps.setLong(3, o.getTime24());
				ps.setLong(4, o.getTime72());
				ps.setInt(5, o.getType());
				ps.setInt(6, o.getIntoflag());
				ps.setString(7, o.getCreatetime());
				return ps;
			}
		}, key);
		o.setId(key.getKey().longValue());
		return key.getKey().longValue();
	}

	public void updateBackDetail(long branchid, long time24, long time72, int intoflag, long id, String nowtime) {
		String sql = "UPDATE ops_back_detail set branchid=?,time24=?,time72=?,intoflag=?,createtime=? where id=?";
		jdbcTemplate.update(sql, branchid, time24, time72, intoflag, nowtime, id);
	}

	public BackDetail getBackDetail(String cwb, long branchid, int type) {
		String sql = "select * from ops_back_detail where cwb=? and branchid=? and type=?";
		try {
			return jdbcTemplate.queryForObject(sql, new BackDetailRowMapper(), cwb, branchid, type);
		} catch (DataAccessException e) {
			return null;
		}
	}

	public BackDetail getBackDetailByCwb(String cwb, int type) {
		String sql = "select * from ops_back_detail where cwb=? and type=?";
		try {
			return jdbcTemplate.queryForObject(sql, new BackDetailRowMapper(), cwb, type);
		} catch (DataAccessException e) {
			return null;
		}
	}

	public void updateTuiHuoZhanRuKu(String cwb, int type) {
		String sql = "update ops_back_detail set intoflag=1 where cwb=? and type=? and intoflag=0";
		jdbcTemplate.update(sql, cwb, type);
	}

	public List<BackDetail> getBackDetailListByInOrOut(String starttime, String endtime) {
		StringBuffer sb = new StringBuffer(" select a.* from ops_back_detail a left join express_ops_cwb_detail b on a.cwb = b.cwb where a.type in (2,3) and b.state = 1 ");
		if (!"".equals(starttime)) {
			sb.append(" and a.createtime>='" + starttime + "' ");
		}
		if (!"".equals(endtime)) {
			sb.append(" and a.createtime<='" + endtime + "' ");
		}
		return jdbcTemplate.query(sb.toString(), new BackDetailRowMapper());
	}

	public List<BackDetail> getBackDetailListByBack(long time) {
		String sql = " SELECT a.* FROM ops_back_detail a left join express_ops_cwb_detail b on a.cwb = b.cwb WHERE a.type=1 AND a.intoflag=0 AND b.state = 1 " + "AND ("
				+ "(a.time72<? AND a.time24=0)" + "OR" + "(a.time24<? AND a.time72=0)" + ")";
		return jdbcTemplate.query(sql, new BackDetailRowMapper(), time, time);
	}
}

package cn.explink.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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

import cn.explink.domain.OperationSetTime;

@Component
public class OperationSetTimeDAO {

	private final class OperationSetTimeRowMapper implements RowMapper<OperationSetTime> {
		@Override
		public OperationSetTime mapRow(ResultSet rs, int rowNum) throws SQLException {
			OperationSetTime operationTime = new OperationSetTime();
			operationTime.setId(rs.getLong("id"));
			operationTime.setCreattime(rs.getString("creattime"));
			operationTime.setName(rs.getString("name"));
			operationTime.setUpdatetime(rs.getString("updatetime"));
			operationTime.setUserid(rs.getLong("userid"));
			operationTime.setValue(rs.getString("value"));
			operationTime.setSitetype(rs.getInt("sitetype"));
			operationTime.setState(rs.getInt("state"));
			operationTime.setBranchids(rs.getString("branchids"));
			return operationTime;
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
	public long creOperationSetTime(final String name, final String value, final long userid, final String creattime, final int sitetype, final int state, final String branchids) {
		KeyHolder key = new GeneratedKeyHolder();
		jdbcTemplate.update(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(java.sql.Connection con) throws SQLException {
				PreparedStatement ps = null;
				ps = con.prepareStatement("insert into express_set_operation_time (name,value,userid,creattime,sitetype,state,branchids) values(?,?,?,?,?,?,?)", new String[] { "id" });
				ps.setString(1, name);
				ps.setString(2, value);
				ps.setLong(3, userid);
				ps.setString(4, creattime);
				ps.setInt(5, sitetype);
				ps.setInt(6, state);
				ps.setString(7, branchids);
				return ps;
			}
		}, key);
		return key.getKey().longValue();
	}

	// public void updateOperationTime(long id,String name,String value,String
	// updatetime){
	// String sql =
	// "update express_set_operation_time set name=?,value=?,updatetime=? where id=?";
	// jdbcTemplate.update(sql,name,value,updatetime,id);
	// }

	public void updateOperationTime(long id, String value, String updatetime) {
		String sql = "update express_set_operation_time  set value=?,updatetime=? where id=?";
		jdbcTemplate.update(sql, value, updatetime, id);
	}

	public List<OperationSetTime> getOperationSetTimeByUserid(long userid) {
		String sql = "select * from express_set_operation_time where userid=?";
		return jdbcTemplate.query(sql, new OperationSetTimeRowMapper(), userid);
	}

	public OperationSetTime getOperationSetTimeById(long id) {
		try {
			String sql = "select * from express_set_operation_time where id=?";
			return jdbcTemplate.queryForObject(sql, new OperationSetTimeRowMapper(), id);
		} catch (DataAccessException e) {
			// TODO Auto-generated catch block
			return null;
		}
	}

	public OperationSetTime getOperationSetTimeByIdAndName(long userid, String name) {
		try {
			String sql = "select * from express_set_operation_time where userid=? and name=?";
			return jdbcTemplate.queryForObject(sql, new OperationSetTimeRowMapper(), userid, name);
		} catch (DataAccessException e) {
			// TODO Auto-generated catch block
			return null;
		}
	}

	public List<OperationSetTime> getOperationSetTime() {
		String sql = "select * from express_set_operation_time ";
		return jdbcTemplate.query(sql, new OperationSetTimeRowMapper());
	}

	public void deleteOperationTimebyid(long id) {
		String sql = "delete from  express_set_operation_time  where id=?";
		jdbcTemplate.update(sql, id);
	}

	public List<OperationSetTime> getOperationSetTimeList(long userid, long sitetype, String modelname, String[] branchids, long state) {
		StringBuffer sb = new StringBuffer();
		sb.append("select * from express_set_operation_time where 1=1 ");
		if (userid > 0) {
			sb.append(" and userid=" + userid);
		}
		if (state > 0) {
			sb.append(" and state=" + state);
		}
		if (sitetype > 0) {
			sb.append(" and sitetype=" + sitetype);
		}
		if (!"".equals(modelname)) {
			sb.append(" and name like '%" + modelname + "%' ");
		}
		if (branchids != null && branchids.length > 0) {
			for (int i = 0; i < branchids.length; i++) {
				if (i == 0) {
					sb.append(" and (branchids like '%" + branchids[i] + "%' ");
				} else {
					sb.append(" or branchids like '%" + branchids[i] + "%'");
				}
			}
			sb.append(" ) ");
		}
		sb.append(" order by creattime desc ");
		return jdbcTemplate.query(sb.toString(), new OperationSetTimeRowMapper());
	}

	public void updateOperationTimeState(long id) {
		String sql = "update express_set_operation_time set state=(state+1)%2 where id=?";
		jdbcTemplate.update(sql, id);
	}

	public OperationSetTime getOperationTimeById(long id) {
		try {
			return jdbcTemplate.queryForObject("SELECT * from express_set_operation_time where id=? ", new OperationSetTimeRowMapper(), id);
		} catch (EmptyResultDataAccessException ee) {
			return new OperationSetTime();
		}
	}
}

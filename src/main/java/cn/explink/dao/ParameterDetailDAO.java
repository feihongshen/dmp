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

import cn.explink.domain.ParameterDetail;
import cn.explink.util.Page;

@Component
public class ParameterDetailDAO {

	private final class ParameterDetailRowMapper implements RowMapper<ParameterDetail> {
		@Override
		public ParameterDetail mapRow(ResultSet rs, int rowNum) throws SQLException {
			ParameterDetail parameterDetail = new ParameterDetail();
			parameterDetail.setId(rs.getLong("id"));
			parameterDetail.setFiled(rs.getString("filed"));
			parameterDetail.setFlowordertype(rs.getLong("flowordertype"));
			parameterDetail.setName(rs.getString("name"));
			return parameterDetail;
		}
	}

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public ParameterDetail getParameterDetailById(long id) {
		try {
			String sql = "select * from express_set_parameter_detail where id=?";
			return jdbcTemplate.queryForObject(sql, new ParameterDetailRowMapper(), +id);
		} catch (DataAccessException e) {
			return null;
		}
	}

	public List<ParameterDetail> getParameterDetail(int flowordertype, String filed) {
		try {
			String sql = "select * from express_set_parameter_detail ";
			sql = getParameterDetailByPageWhereSql(sql, flowordertype, filed);
			return jdbcTemplate.query(sql, new ParameterDetailRowMapper());
		} catch (DataAccessException e) {
			return null;
		}
	}

	public long creParameterDetail(final ParameterDetail pd) {
		KeyHolder key = new GeneratedKeyHolder();
		jdbcTemplate.update(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(java.sql.Connection con) throws SQLException {
				PreparedStatement ps = null;
				ps = con.prepareStatement("insert into express_set_parameter_detail (filed,flowordertype,name) " + "values(?,?,?)", new String[] { "id" });
				ps.setString(1, pd.getFiled());
				ps.setLong(2, pd.getFlowordertype());
				ps.setString(3, pd.getName());
				return ps;
			}
		}, key);
		/*
		 * String sql =
		 * "insert into express_set_parameter_detail (filed,flowordertype,name) values ('"
		 * +filed+"',"+flowordertype+"',name='"+name+"')";
		 * jdbcTemplate.update(sql);
		 */return key.getKey().longValue();
	}

	public List<ParameterDetail> getParameterDetailByWhere(long page, int flowordertype, String filed) {
		String sql = "SELECT * from express_set_parameter_detail ";
		sql = this.getParameterDetailByPageWhereSql(sql, flowordertype, filed);
		sql += " limit " + (page - 1) * Page.ONE_PAGE_NUMBER + " ," + Page.ONE_PAGE_NUMBER;

		List<ParameterDetail> pdList = jdbcTemplate.query(sql, new ParameterDetailRowMapper());
		return pdList;
	}

	public long getParameterDetailCount(int flowordertype, String filed) {
		String sql = "SELECT count(1) from express_set_parameter_detail";
		sql = this.getParameterDetailByPageWhereSql(sql, flowordertype, filed);
		return jdbcTemplate.queryForLong(sql);
	}

	private String getParameterDetailByPageWhereSql(String sql, int flowordertype, String filed) {
		if (flowordertype > 0 || filed.length() > 0) {
			StringBuffer w = new StringBuffer();
			sql += " where ";
			if (flowordertype > 0) {
				w.append(" and flowordertype=" + flowordertype);
			}
			if (filed.length() > 0) {
				w.append(" and filed='" + filed + "'");
			}
			sql += w.substring(4, w.length());
		}
		return sql;
	}

	public void saveParameterDetail(long id, long flowordertype, String filed, String name) {
		String sql = "update express_set_parameter_detail set flowordertype=?,filed=?,name=? where id=?";
		jdbcTemplate.update(sql, flowordertype, filed, name, id);

	}

	public void delParameterDetail(long id) {
		String sql = "delete from express_set_parameter_detail where id=?";
		jdbcTemplate.update(sql, id);

	}

}

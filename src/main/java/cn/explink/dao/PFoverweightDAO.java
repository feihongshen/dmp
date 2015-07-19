/**
 *
 */
package cn.explink.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import cn.explink.domain.PFoverweight;

/**
 * @author Administrator
 *
 */
@Component
public class PFoverweightDAO {
	@Autowired
	JdbcTemplate jdbcTemplate;

	final class PFoverweightRowMapper implements RowMapper<PFoverweight> {

		@Override
		public PFoverweight mapRow(ResultSet rs, int rowNum) throws SQLException {
			PFoverweight pf = new PFoverweight();
			pf.setId(rs.getLong("id"));
			pf.setAreaid(rs.getLong("areaid"));
			pf.setMaxcount(rs.getLong("maxcount"));
			pf.setMincount(rs.getLong("mincount"));
			pf.setSubsidyfee(rs.getBigDecimal("subsidyfee"));
			pf.setRemark(rs.getString("remark"));
			return pf;
		}
	}

	/**
	 * @param pfruleid
	 * @param typeid
	 * @param tabid
	 * @param mincount
	 * @param maxcount
	 * @return
	 */
	public PFoverweight getPFoverweightByRTC(long areaid, long mincount, long maxcount) {
		String sql = "select * from paifeirule_overweight where mincount=? and maxcount=? and areaid=? ";
		try {
			return this.jdbcTemplate.queryForObject(sql, new PFoverweightRowMapper(), mincount, maxcount, areaid);
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * @param pf
	 */
	public int updatePFoverweight(final PFoverweight pf) {

		String sql = "update `paifeirule_overweight` set `mincount`=?, `maxcount`=?,`subsidyfee`=?, `remark`=? ,`areaid`=? where id=?";
		return this.jdbcTemplate.update(sql, new PreparedStatementSetter() {

			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				ps.setLong(1, pf.getMincount());
				ps.setLong(2, pf.getMaxcount());
				ps.setBigDecimal(3, pf.getSubsidyfee());
				ps.setString(4, pf.getRemark());
				ps.setLong(5, pf.getAreaid());
				ps.setLong(6, pf.getId());

			}
		});

	}

	/**
	 * @param pf
	 */
	public int credata(final PFoverweight pf) {
		String sql = "insert  INTO `paifeirule_overweight` (`mincount`, `maxcount`, `subsidyfee`, `remark`,`areaid`) VALUES (?, ?, ?, ?, ?); ";
		return this.jdbcTemplate.update(sql, new PreparedStatementSetter() {

			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				ps.setLong(1, pf.getMincount());
				ps.setLong(2, pf.getMaxcount());
				ps.setBigDecimal(3, pf.getSubsidyfee());
				ps.setString(4, pf.getRemark());
				ps.setLong(5, pf.getAreaid());

			}
		});
	}

	/**
	 * @param areaid
	 * @param i
	 * @return
	 */
	public PFoverweight getPFoverweightByAreaidAndCount(long areaid, int count) {
		String sql = "select * from paifeirule_overweight where mincount=<? and maxcount>=? and areaid=? limit 1";
		try {
			return this.jdbcTemplate.queryForObject(sql, new PFoverweightRowMapper(), count, count, areaid);
		} catch (Exception e) {
			return null;
		}
	}
}

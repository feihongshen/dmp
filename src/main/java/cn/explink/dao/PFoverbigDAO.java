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

import cn.explink.domain.PFoverbig;

/**
 * @author Administrator
 *
 */
@Component
public class PFoverbigDAO {
	@Autowired
	JdbcTemplate jdbcTemplate;

	final class PFoverbigRowMapper implements RowMapper<PFoverbig> {

		@Override
		public PFoverbig mapRow(ResultSet rs, int rowNum) throws SQLException {
			PFoverbig pf = new PFoverbig();
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
	public PFoverbig getPFoverbigByRTC(long areaid, long mincount, long maxcount) {
		String sql = "select * from paifeirule_overbig where mincount=? and maxcount=? and areaid=? ";
		try {
			return this.jdbcTemplate.queryForObject(sql, new PFoverbigRowMapper(), mincount, maxcount, areaid);
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * @param pf
	 */
	public int updatePFoverbig(final PFoverbig pf) {

		String sql = "update `paifeirule_overbig` set `mincount`=?, `maxcount`=?, `subsidyfee`=?,`remark`=? ,`areaid`=? where id=? ";
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
	public int credata(final PFoverbig pf) {
		String sql = "insert  INTO `paifeirule_overbig` (`mincount`, `maxcount`, `subsidyfee`, `remark`,`areaid`) VALUES (?, ?, ?, ?, ?); ";
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
	public PFoverbig getPFoverbigByAreaidAndCount(long areaid, int count) {
		String sql = "select * from paifeirule_overbig where mincount=<? and maxcount>=? and areaid=? limit 1";
		try {
			return this.jdbcTemplate.queryForObject(sql, new PFoverbigRowMapper(), count, count, areaid);
		} catch (Exception e) {
			return null;
		}
	}
}

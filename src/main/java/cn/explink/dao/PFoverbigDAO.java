/**
 *
 */
package cn.explink.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
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

	public long credataOfID(final PFoverbig pf) {
		final String sql = "insert  INTO `paifeirule_overbig` (`mincount`, `maxcount`, `subsidyfee`, `remark`,`areaid`) VALUES (?, ?, ?, ?, ?); ";
		KeyHolder keyHolder = new GeneratedKeyHolder();
		this.jdbcTemplate.update(new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
				ps.setLong(1, pf.getMincount());
				ps.setLong(2, pf.getMaxcount());
				ps.setBigDecimal(3, pf.getSubsidyfee());
				ps.setString(4, pf.getRemark());
				ps.setLong(5, pf.getAreaid());
				return ps;
			}
		}, keyHolder);
		return keyHolder.getKey().longValue();
	}

	/**
	 * @param areaid
	 * @param i
	 * @return
	 */
	public PFoverbig getPFoverbigByAreaidAndCount(long areaid, long count) {
		String sql = "select * from paifeirule_overbig where mincount=<? and maxcount>? and areaid=? limit 1";
		try {
			return this.jdbcTemplate.queryForObject(sql, new PFoverbigRowMapper(), count, count, areaid);
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * @param id
	 * @return
	 */
	public List<PFoverbig> getPFoverbigByAreaidAndTabid(long areaid) {
		String sql = "select * from paifeirule_overbig where  areaid=? ";
		try {
			return this.jdbcTemplate.query(sql, new PFoverbigRowMapper(), areaid);
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * @param areaid
	 * @return
	 */
	public int deletePFoverbigByAreaid(long areaid) {
		String sql = "delete from paifeirule_overbig where  areaid=? ";
		try {
			return this.jdbcTemplate.update(sql, areaid);
		} catch (Exception e) {
			return 0;
		}
	}
}

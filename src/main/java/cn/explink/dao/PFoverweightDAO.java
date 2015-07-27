/**
 *
 */
package cn.explink.dao;

import java.math.BigDecimal;
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
			pf.setMaxcount(rs.getBigDecimal("maxcount"));
			pf.setMincount(rs.getBigDecimal("mincount"));
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
	public PFoverweight getPFoverweightByRTC(long areaid, BigDecimal mincount, BigDecimal maxcount) {
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
				ps.setBigDecimal(1, pf.getMincount());
				ps.setBigDecimal(2, pf.getMaxcount());
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
				ps.setBigDecimal(1, pf.getMincount());
				ps.setBigDecimal(2, pf.getMaxcount());
				ps.setBigDecimal(3, pf.getSubsidyfee());
				ps.setString(4, pf.getRemark());
				ps.setLong(5, pf.getAreaid());

			}
		});
	}
	/**
	 * @param pf
	 */
	public long credataOfID(final PFoverweight pf) {
		final String sql = "insert  INTO `paifeirule_overweight` (`mincount`, `maxcount`, `subsidyfee`, `remark`,`areaid`) VALUES (?, ?, ?, ?, ?); ";
		KeyHolder keyHolder = new GeneratedKeyHolder();
		this.jdbcTemplate.update(new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				PreparedStatement ps=con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
				ps.setBigDecimal(1, pf.getMincount());
				ps.setBigDecimal(2, pf.getMaxcount());
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
	public PFoverweight getPFoverweightByAreaidAndCount(long areaid, BigDecimal count) {
		String sql = "select * from paifeirule_overweight where mincount=<? and maxcount>? and areaid=? limit 1";
		try {
			return this.jdbcTemplate.queryForObject(sql, new PFoverweightRowMapper(), count, count, areaid);
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * @param id
	 * @return
	 */
	public List<PFoverweight> getPFoverweightByAreaidAndTabid(long areaid) {
		String sql = "select * from paifeirule_overweight where  areaid=? ";
		try {
			return this.jdbcTemplate.query(sql, new PFoverweightRowMapper(), areaid);
		} catch (Exception e) {
			return null;
		}
	}
	/**
	 * @param id
	 * @return
	 */
	public int deletePFoverweightByAreaid(long areaid) {
		String sql = "delete from paifeirule_overweight where  areaid=? ";
		try {
			return this.jdbcTemplate.update(sql, areaid);
		} catch (Exception e) {
			return 0;
		}
	}
}

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

import cn.explink.domain.PFinsertion;

/**
 * @author Administrator
 *
 */
@Component
public class PFinsertionDAO {
	@Autowired
	JdbcTemplate jdbcTemplate;

	final class PFinsertionRowMapper implements RowMapper<PFinsertion> {

		@Override
		public PFinsertion mapRow(ResultSet rs, int rowNum) throws SQLException {
			PFinsertion pf = new PFinsertion();
			pf.setId(rs.getLong("id"));
			pf.setPfruleid(rs.getLong("pfruleid"));
			pf.setTypeid(rs.getInt("typeid"));
			pf.setMaxcount(rs.getLong("maxcount"));
			pf.setMincount(rs.getLong("mincount"));
			pf.setInsertionfee(rs.getBigDecimal("insertionfee"));
			pf.setTabid(rs.getInt("tabid"));
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
	public PFinsertion getPFinsertionByRTC(long pfruleid, int typeid, int tabid, long mincount, long maxcount) {
		String sql = "select * from paifeirule_insertion where mincount=? and maxcount=? and pfruleid=? and typeid=? and tabid=?";
		try {
			return this.jdbcTemplate.queryForObject(sql, new PFinsertionRowMapper(), mincount, maxcount, pfruleid, typeid, tabid);
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * @param pf
	 */
	public int updatePFinsertion(final PFinsertion pf) {

		String sql = "update `paifeirule_insertion` set `mincount`=?, `maxcount`=?, `insertionfee`=?, `typeid`=?, `pfruleid`=?,`tabid`=?,`remark`=? where id=?";
		return this.jdbcTemplate.update(sql, new PreparedStatementSetter() {

			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				ps.setLong(1, pf.getMincount());
				ps.setLong(2, pf.getMaxcount());
				ps.setBigDecimal(3, pf.getInsertionfee());
				ps.setInt(4, pf.getTypeid());
				ps.setLong(5, pf.getPfruleid());
				ps.setInt(6, pf.getTabid());
				ps.setString(7, pf.getRemark());
				ps.setLong(8, pf.getId());

			}
		});

	}

	/**
	 * @param pf
	 */
	public int credata(final PFinsertion pf) {
		String sql = "insert  INTO `paifeirule_insertion` (`mincount`, `maxcount`, `insertionfee`, `typeid`, `pfruleid`,`tabid`,`remark`) VALUES (?, ?, ?, ?, ?, ?,?); ";
		return this.jdbcTemplate.update(sql, new PreparedStatementSetter() {

			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				ps.setLong(1, pf.getMincount());
				ps.setLong(2, pf.getMaxcount());
				ps.setBigDecimal(3, pf.getInsertionfee());
				ps.setInt(4, pf.getTypeid());
				ps.setLong(5, pf.getPfruleid());
				ps.setInt(6, pf.getTabid());
				ps.setString(7, pf.getRemark());

			}
		});
	}
	/**
	 * @param pf
	 */
	public long credataOfID(final PFinsertion pf) {
		final String sql = "insert  INTO `paifeirule_insertion` (`mincount`, `maxcount`, `insertionfee`, `typeid`, `pfruleid`,`tabid`,`remark`) VALUES (?, ?, ?, ?, ?, ?,?); ";
		KeyHolder keyHolder = new GeneratedKeyHolder();
		this.jdbcTemplate.update(new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				PreparedStatement ps=con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
				ps.setLong(1, pf.getMincount());
				ps.setLong(2, pf.getMaxcount());
				ps.setBigDecimal(3, pf.getInsertionfee());
				ps.setInt(4, pf.getTypeid());
				ps.setLong(5, pf.getPfruleid());
				ps.setInt(6, pf.getTabid());
				ps.setString(7, pf.getRemark());
				return ps;
			}
		}, keyHolder);
		return keyHolder.getKey().longValue();
	}

	/**
	 * @param pfruleid
	 * @param value
	 * @param i
	 * @return
	 */
	public PFinsertion getPFinsertionByPfruleidAndCount(long pfruleid, int tabid, long count) {
		String sql = "select * from paifeirule_insertion where mincount<=? and maxcount>? and pfruleid=? and tabid=?";
		try {
			return this.jdbcTemplate.queryForObject(sql, new PFinsertionRowMapper(), count, count, pfruleid, tabid);
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * @param pfruleid
	 * @param value
	 * @return
	 */
	public List<PFinsertion> getPFinsertionByPfruleidAndTabid(long pfruleid, int tabid) {
		String sql = "select * from paifeirule_insertion where pfruleid=? and tabid=?";
		try {
			return this.jdbcTemplate.query(sql, new PFinsertionRowMapper(), pfruleid, tabid);
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * @param id
	 * @param value
	 */
	public int deletePFinsertionByPfRuleidAndTabid(long pfruleid, int tabid) {
		String sql="delete from paifeirule_insertion where pfruleid=? and tabid=?";
		return this.jdbcTemplate.update(sql,pfruleid,tabid);

	}

	/**
	 * @param pfruleid
	 */
	public void deletePFinsertionByPfruleid(long pfruleid) {
		String sql="delete from paifeirule_insertion where pfruleid=? ";
		this.jdbcTemplate.update(sql,pfruleid);

	}
}

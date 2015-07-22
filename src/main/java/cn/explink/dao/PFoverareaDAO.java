/**
 *
 */
package cn.explink.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import cn.explink.domain.PFoverarea;

/**
 * @author Administrator
 *
 */
@Component
public class PFoverareaDAO {
	@Autowired
	JdbcTemplate jdbcTemplate;
	final class PFoverareaRowMapper implements RowMapper<PFoverarea> {

		@Override
		public PFoverarea mapRow(ResultSet rs, int rowNum) throws SQLException {
			PFoverarea pf = new PFoverarea();
			pf.setId(rs.getLong("id"));
			pf.setState(rs.getInt("state"));
			pf.setPfruleid(rs.getLong("pfruleid"));
			pf.setTypeid(rs.getInt("typeid"));
			pf.setTabid(rs.getInt("tabid"));
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
	public PFoverarea getPFoverareaByRTC(long pfruleid, int typeid, int tabid,int state) {
		String sql = "select * from paifeirule_overarea where pfruleid=? and typeid=? and tabid=? and state=? limit 1";
		try {
			return this.jdbcTemplate.queryForObject(sql, new PFoverareaRowMapper(), pfruleid, typeid, tabid,state);
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * @param pf
	 */
	public int updatePFoverarea(final PFoverarea pf) {

		String sql = "update `paifeirule_overarea` set `typeid`=?, `pfruleid`=?,`tabid`=?,`state`=? where id=? ";
		return this.jdbcTemplate.update(sql, new PreparedStatementSetter() {

			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				ps.setLong(1, pf.getTypeid());
				ps.setLong(2, pf.getPfruleid());
				ps.setLong(3, pf.getTabid());
				ps.setLong(4, pf.getState());
				ps.setLong(6, pf.getId());

			}
		});

	}
	/**
	 * @param pf
	 */
	public int updatePFoverareaByPfruleidAndTabid(final PFoverarea pf) {

		String sql = "update `paifeirule_overarea` set `state`=? where `typeid`=? and `pfruleid`=? and `tabid`=?";
		return this.jdbcTemplate.update(sql, new PreparedStatementSetter() {

			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				ps.setLong(1, pf.getState());
				ps.setLong(2, pf.getTypeid());
				ps.setLong(3, pf.getPfruleid());
				ps.setLong(4, pf.getTabid());

			}
		});

	}

	/**
	 * @param pf
	 */
	public int credata(final PFoverarea pf) {
		String sql = "insert  INTO `paifeirule_overarea` (`typeid`, `pfruleid`,`tabid`,`state`) VALUES (?, ?, ?, ?); ";
		return this.jdbcTemplate.update(sql, new PreparedStatementSetter() {

			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				ps.setLong(1, pf.getTypeid());
				ps.setLong(2, pf.getPfruleid());
				ps.setLong(3, pf.getTabid());
				ps.setLong(4, pf.getState());

			}
		});
	}

	/**
	 * @param pf
	 */
	public long credataOfID(final PFoverarea pf) {
		final String sql = "insert  INTO `paifeirule_overarea` (`typeid`, `pfruleid`,`tabid`,`state`) VALUES (?, ?, ?, ?); ";
		KeyHolder keyHolder = new GeneratedKeyHolder();
		this.jdbcTemplate.update(new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				PreparedStatement ps=con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
				ps.setLong(1, pf.getTypeid());
				ps.setLong(2, pf.getPfruleid());
				ps.setLong(3, pf.getTabid());
				ps.setLong(4, pf.getState());
				return ps;
			}
		}, keyHolder);
		return keyHolder.getKey().longValue();
	}

	/**
	 * @param pfruleid
	 * @param value
	 * @return
	 */
	public PFoverarea getPFoverareaByPaifeiruleAndtabid(long pfruleid, int tabid) {
		String sql = "select * from paifeirule_overarea where pfruleid=? and tabid=?  ";
		try {
			return this.jdbcTemplate.queryForObject(sql, new PFoverareaRowMapper(), pfruleid,  tabid);
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * @param id
	 * @param tabid
	 */
	public int deletePFoverareaByPfRuleidAndTabid(long pfruleid, int tabid) {
		String sql="delete from paifeirule_overarea where pfruleid=? and tabid=?  ";
		return this.jdbcTemplate.update(sql,pfruleid,tabid);

	}
}

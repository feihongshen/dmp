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

import cn.explink.domain.PFbusiness;

/**
 * @author Administrator
 *
 */
@Component
public class PFbusinessDAO {
	@Autowired
	JdbcTemplate jdbcTemplate;

	final class PFbusinessRowMapper implements RowMapper<PFbusiness> {

		/*
		 * (non-Javadoc)
		 *
		 * @see
		 * org.springframework.jdbc.core.RowMapper#mapRow(java.sql.ResultSet,
		 * int)
		 */
		@Override
		public PFbusiness mapRow(ResultSet rs, int rowNum) throws SQLException {
			PFbusiness pf = new PFbusiness();
			pf.setId(rs.getLong("id"));
			pf.setSubsidyfee(rs.getBigDecimal("subsidyfee"));
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
	 * @return
	 */
	public PFbusiness getPFbusinessByRTC(long pfruleid, int typeid, int tabid) {

		String sql = "select * from paifeirule_business where  pfruleid=? and typeid=? and tabid=?";
		try {
			return this.jdbcTemplate.queryForObject(sql, new PFbusinessRowMapper(), pfruleid, typeid, tabid);
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * @param pf
	 */
	public int updatePFbusiness(final PFbusiness pf) {

		String sql = "update `paifeirule_business` set `subsidyfee`=?,  `typeid`=?, `pfruleid`=?,`tabid`=? where id=?";
		return this.jdbcTemplate.update(sql, new PreparedStatementSetter() {

			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				ps.setBigDecimal(1, pf.getSubsidyfee());
				ps.setInt(2, pf.getTypeid());
				ps.setLong(3, pf.getPfruleid());
				ps.setInt(4, pf.getTabid());
				ps.setLong(5, pf.getId());
			}
		});

	}

	/**
	 * @param pf
	 */
	public int credata(final PFbusiness pf) {

		String sql = "insert  INTO `paifeirule_business` (`subsidyfee`,`typeid`,`pfruleid`,`tabid`) VALUES (?, ?, ?, ?); ";
		return this.jdbcTemplate.update(sql, new PreparedStatementSetter() {

			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				ps.setBigDecimal(1, pf.getSubsidyfee());
				ps.setInt(2, pf.getTypeid());
				ps.setLong(3, pf.getPfruleid());
				ps.setInt(4, pf.getTabid());
			}
		});
	}

	/**
	 * @param pfruleid
	 * @param value
	 * @return
	 */
	public PFbusiness getPFbusinessByRTC(long pfruleid, int tabid) {
		String sql = "select * from paifeirule_business where  pfruleid=?  and tabid=?";
		try {
			return this.jdbcTemplate.queryForObject(sql, new PFbusinessRowMapper(), pfruleid, tabid);
		} catch (Exception e) {
			return null;
		}
	}

}

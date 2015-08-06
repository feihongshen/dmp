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

import cn.explink.domain.PFarea;
import cn.explink.util.StringUtil;

/**
 * @author Administrator
 *
 */
@Component
public class PFAreaDAO {
	@Autowired
	JdbcTemplate jdbcTemplate;

	final class PFareaRowMapper implements RowMapper<PFarea> {

		@Override
		public PFarea mapRow(ResultSet rs, int rowNum) throws SQLException {
			PFarea pf = new PFarea();
			pf.setId(rs.getLong("id"));
			pf.setAreaid(rs.getLong("areaid"));
			pf.setAreaname(StringUtil.nullConvertToEmptyString(rs.getString("areaname")));
			pf.setAreafee(rs.getBigDecimal("areafee"));
			pf.setOverbigflag(rs.getInt("overbigflag"));
			pf.setPfruleid(rs.getLong("pfruleid"));
			pf.setTypeid(rs.getInt("typeid"));
			pf.setTabid(rs.getInt("tabid"));
			pf.setRemark(StringUtil.nullConvertToEmptyString(rs.getString("remark")));
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
	public PFarea getPFareaByRTC(long pfruleid, int typeid, int tabid, long areaid) {
		String sql = "select * from paifeirule_area where  pfruleid=? and typeid=? and tabid=? and areaid=? limit 1";
		try {
			return this.jdbcTemplate.queryForObject(sql, new PFareaRowMapper(), pfruleid, typeid, tabid, areaid);
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * @param pf
	 */
	public int updatePFarea(final PFarea pf) {

		String sql = "update `paifeirule_area` set `areaid`=?, `areaname`=?, `overbigflag`=? ,`areafee`=? where id=?";
		return this.jdbcTemplate.update(sql, new PreparedStatementSetter() {

			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				ps.setLong(1, pf.getAreaid());
				ps.setString(2, pf.getAreaname());
				ps.setInt(3, pf.getOverbigflag());
				ps.setBigDecimal(4, pf.getAreafee());
				ps.setLong(5, pf.getId());

			}
		});

	}

	/**
	 * @param pf
	 */
	public int credata(final PFarea pf) {
		String sql = "insert  INTO `paifeirule_area` (`areaid`, `areaname`, `areafee`, `overbigflag`,`typeid`,`pfruleid`,`tabid`) VALUES (?, ?, ?, ? ,? ,? ,?); ";
		return this.jdbcTemplate.update(sql, new PreparedStatementSetter() {

			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				ps.setLong(1, pf.getAreaid());
				ps.setString(2, pf.getAreaname());
				ps.setBigDecimal(3, pf.getAreafee());
				ps.setInt(4, pf.getOverbigflag());
				ps.setInt(5, pf.getTypeid());
				ps.setLong(6, pf.getPfruleid());
				ps.setInt(7, pf.getTabid());

			}
		});
	}

	/**
	 * @param pf
	 */
	public long credataOfID(final PFarea pf) {
		final String sql = "insert  INTO `paifeirule_area` (`areaid`, `areaname`, `areafee`, `overbigflag`,`typeid`,`pfruleid`,`tabid`) VALUES (?, ?, ?, ? ,? ,? ,?); ";
		KeyHolder keyHolder = new GeneratedKeyHolder();
		this.jdbcTemplate.update(new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
				ps.setLong(1, pf.getAreaid());
				ps.setString(2, pf.getAreaname());
				ps.setBigDecimal(3, pf.getAreafee());
				ps.setInt(4, pf.getOverbigflag());
				ps.setInt(5, pf.getTypeid());
				ps.setLong(6, pf.getPfruleid());
				ps.setInt(7, pf.getTabid());
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
	public List<PFarea> getPFareaByPfruleidAndTabid(long pfruleid, int tabid) {

		String sql = "select * from paifeirule_area where  pfruleid=?  and tabid=? ";
		try {
			return this.jdbcTemplate.query(sql, new PFareaRowMapper(), pfruleid, tabid);
		} catch (Exception e) {
			return null;
		}
	}

	public PFarea getPFareaBypfruleidAndTabidAndAreaid(long pfruleid, int tabid, long areaid) {

		String sql = "select * from paifeirule_area where  pfruleid=?  and tabid=? and areaid=? ";
		try {
			return this.jdbcTemplate.queryForObject(sql, new PFareaRowMapper(), pfruleid, tabid, areaid);
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * @param pfruleid
	 * @return
	 */
	public List<PFarea> getPFareaByPfruleid(long pfruleid) {
		String sql = "select * from paifeirule_area where  pfruleid=?   ";
		try {
			return this.jdbcTemplate.query(sql, new PFareaRowMapper(), pfruleid);
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * @param pfruleid
	 */
	public void deleteAreaByPfruleid(long pfruleid) {
		String sql = "delete from paifeirule_area where  pfruleid=?   ";
		this.jdbcTemplate.update(sql, pfruleid);

	}
	public int deleteAreaByPfruleidAndTabid(long pfruleid,int tabid) {
		String sql = "delete from paifeirule_area where  pfruleid=? and tabid=?  ";
		return this.jdbcTemplate.update(sql, pfruleid,tabid);

	}

	public PFarea getPFareaById(long id) {

		String sql = "select * from paifeirule_area where  id=?  ";
		try {
			return this.jdbcTemplate.queryForObject(sql, new PFareaRowMapper(), id);
		} catch (Exception e) {
			return null;
		}
	}
}

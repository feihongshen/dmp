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

import cn.explink.domain.PFbasic;
import cn.explink.util.StringUtil;

/**
 * @author Administrator
 *
 */
@Component
public class PFbasicDAO {
	@Autowired
	JdbcTemplate jdbcTemplate;

	final class PFbasicRowMapper implements RowMapper<PFbasic> {

		/*
		 * (non-Javadoc)
		 *
		 * @see
		 * org.springframework.jdbc.core.RowMapper#mapRow(java.sql.ResultSet,
		 * int)
		 */
		@Override
		public PFbasic mapRow(ResultSet rs, int rowNum) throws SQLException {
			PFbasic pf = new PFbasic();
			pf.setId(rs.getLong("id"));
			pf.setCustomerid(rs.getLong("customerid"));
			pf.setBasicPFfee(rs.getBigDecimal("basicPFfee"));
			pf.setPfruleid(rs.getLong("pfruleid"));
			pf.setTypeid(rs.getInt("typeid"));
			pf.setTabid(rs.getInt("tabid"));
			pf.setShowflag(rs.getInt("showflag"));
			pf.setRemark(StringUtil.nullConvertToEmptyString(rs.getString("remark")));
			return pf;
		}
	}

	/**
	 * @param pfb
	 */
	public int credata(final PFbasic pfb) {
		String sql = "insert  INTO `paifeirule_basic` (`customerid`, `basicPFfee`, `remark`, `typeid`, `pfruleid`,`tabid`,`showflag`) VALUES (?,?, ?, ?, ?, ?, ?); ";
		return this.jdbcTemplate.update(sql, new PreparedStatementSetter() {

			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				ps.setLong(1, pfb.getCustomerid());
				ps.setBigDecimal(2, pfb.getBasicPFfee());
				ps.setString(3, pfb.getRemark());
				ps.setInt(4, pfb.getTypeid());
				ps.setLong(5, pfb.getPfruleid());
				ps.setInt(6, pfb.getTabid());
				ps.setInt(7, pfb.getShowflag());

			}
		});
	}

	/**
	 * @param pfb
	 */
	public long credataOfID(final PFbasic pfb) {//插入新数据并且获取主键
		final String sql = "insert  INTO `paifeirule_basic` (`customerid`, `basicPFfee`, `remark`, `typeid`, `pfruleid`,`tabid`,`showflag`) VALUES (?,?, ?, ?, ?, ?, ?); ";
		KeyHolder keyHolder = new GeneratedKeyHolder();
		this.jdbcTemplate.update(new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				PreparedStatement ps=con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
				ps.setLong(1, pfb.getCustomerid());
				ps.setBigDecimal(2, pfb.getBasicPFfee());
				ps.setString(3, pfb.getRemark());
				ps.setInt(4, pfb.getTypeid());
				ps.setLong(5, pfb.getPfruleid());
				ps.setInt(6, pfb.getTabid());
				ps.setInt(7, pfb.getShowflag());
				return ps;
			}
		}, keyHolder);
		return keyHolder.getKey().longValue();
	}

	/**
	 * @param pfruleid
	 * @param typeid
	 * @param tabid
	 * @param customerid
	 * @return
	 */
	public PFbasic getPFbasicByRTC(long pfruleid, int typeid, int tabid, long customerid) {
		String sql = "select * from paifeirule_basic where pfruleid=? and typeid=? and tabid=? and customerid=?";

		try {
			return this.jdbcTemplate.queryForObject(sql, new PFbasicRowMapper(), pfruleid, typeid, tabid, customerid);
		} catch (Exception e) {
			return null;
		}
	}

	public int updatePFbasic(final PFbasic pfb) {
		String sql = "update `paifeirule_basic` set `customerid`=?, `basicPFfee`=?, `remark`=?, `typeid`=?, `pfruleid`=?,`tabid`=? ,`showflag`=? where id=?";
		return this.jdbcTemplate.update(sql, new PreparedStatementSetter() {

			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				ps.setLong(1, pfb.getCustomerid());
				ps.setBigDecimal(2, pfb.getBasicPFfee());
				ps.setString(3, pfb.getRemark());
				ps.setInt(4, pfb.getTypeid());
				ps.setLong(5, pfb.getPfruleid());
				ps.setInt(6, pfb.getTabid());
				ps.setInt(7, pfb.getShowflag());
				ps.setLong(8, pfb.getId());
			}
		});
	}

	/**
	 * @param id
	 */
	public void deletePFbasicById(long id) {
		String sql = "delete from paifeirule_basic where id=? ";
		this.jdbcTemplate.update(sql, id);
	}
	public void deletePFbasicByPfruleid(long pfruleid) {
		String sql = "delete from paifeirule_basic where pfruleid=? ";
		this.jdbcTemplate.update(sql, pfruleid);
	}

	public int deletePFbasicByPfRuleidAndTabid(long pfruleid, int tabid) {
		String sql = "delete from paifeirule_basic where pfruleid=? and tabid=? ";
		return this.jdbcTemplate.update(sql, pfruleid, tabid);
	}

	public List<PFbasic> getPFbasicByPfruleidAndTabid(long pfruleid, long tabid) {
		String sql = "select * from paifeirule_basic where pfruleid=? and tabid=? ";

		try {
			return this.jdbcTemplate.query(sql, new PFbasicRowMapper(), pfruleid, tabid);
		} catch (Exception e) {
			return null;
		}
	}

	public PFbasic getPFbasicByPfruleidAndtabidAndCustomerid(long pfruleid, int tabid, long customerid) {
		String sql = "select * from paifeirule_basic where pfruleid=? and tabid=? and customerid=?";

		try {
			return this.jdbcTemplate.queryForObject(sql, new PFbasicRowMapper(), pfruleid, tabid, customerid);
		} catch (Exception e) {
			return null;
		}
	}
}

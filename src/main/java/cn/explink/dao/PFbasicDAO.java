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
			pf.setRemark(StringUtil.nullConvertToEmptyString(rs.getString("remark")));
			return pf;
		}
	}

	/**
	 * @param pfb
	 */
	public int credata(final PFbasic pfb) {
		String sql = "insert  INTO `paifeirule_basic` (`customerid`, `basicPFfee`, `remark`, `typeid`, `pfruleid`,`tabid`) VALUES (?, ?, ?, ?, ?, ?); ";
		return this.jdbcTemplate.update(sql, new PreparedStatementSetter() {

			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				ps.setLong(1, pfb.getCustomerid());
				ps.setBigDecimal(2, pfb.getBasicPFfee());
				ps.setString(3, pfb.getRemark());
				ps.setInt(4, pfb.getTypeid());
				ps.setLong(5, pfb.getPfruleid());
				ps.setInt(6, pfb.getTabid());

			}
		});
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
		String sql = "update `paifeirule_basic` set `customerid`=?, `basicPFfee`=?, `remark`=?, `typeid`=?, `pfruleid`=?,`tabid`=? where id=?";
		return this.jdbcTemplate.update(sql, new PreparedStatementSetter() {

			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				ps.setLong(1, pfb.getCustomerid());
				ps.setBigDecimal(2, pfb.getBasicPFfee());
				ps.setString(3, pfb.getRemark());
				ps.setInt(4, pfb.getTypeid());
				ps.setLong(5, pfb.getPfruleid());
				ps.setInt(6, pfb.getTabid());
				ps.setLong(7, pfb.getId());
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
}

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

import cn.explink.domain.PFcollection;
import cn.explink.util.StringUtil;

/**
 * @author Administrator
 *
 */
@Component
public class PFcollectionDAO {
	@Autowired
	JdbcTemplate jdbcTemplate;
	final class PFcollectionRowMapper implements RowMapper<PFcollection>{

		/* (non-Javadoc)
		 * @see org.springframework.jdbc.core.RowMapper#mapRow(java.sql.ResultSet, int)
		 */
		@Override
		public PFcollection mapRow(ResultSet rs, int rowNum) throws SQLException {
			PFcollection pf=new PFcollection();
			pf.setId(rs.getLong("id"));
			pf.setCustomerid(rs.getLong("customerid"));
			pf.setCollectionPFfee(rs.getBigDecimal("collectionPFfee"));
			pf.setPfruleid(rs.getLong("pfruleid"));
			pf.setTypeid(rs.getInt("typeid"));
			pf.setTabid(rs.getInt("tabid"));
			pf.setRemark(StringUtil.nullConvertToEmptyString(rs.getString("remark")));
			return pf;
		}}

	/**
	 * @param pfb
	 */
	public int credata(final PFcollection pfb) {
		String sql = "insert  INTO `paifeirule_collection` (`customerid`, `collectionPFfee`, `remark`, `typeid`, `pfruleid`,`tabid`) VALUES (?, ?, ?, ?, ?, ?); ";
		return this.jdbcTemplate.update(sql, new PreparedStatementSetter() {

			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				ps.setLong(1, pfb.getCustomerid());
				ps.setBigDecimal(2, pfb.getCollectionPFfee());
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
	public PFcollection getPFcollectionByRTC(long pfruleid, int typeid, int tabid, long customerid) {
		String sql = "select * from paifeirule_collection where pfruleid=? and typeid=? and tabid=? and customerid=?";

		try {
			return this.jdbcTemplate.queryForObject(sql, new PFcollectionRowMapper(), pfruleid, typeid, tabid, customerid);
		} catch (Exception e) {
			return null;
		}
	}
	public int updatePFcollection(final PFcollection pfb) {
		String sql = "update `paifeirule_collection` set `customerid`=?, `collectionPFfee`=?, `remark`=?, `typeid`=?, `pfruleid`=?,`tabid`=? where id=?";
		return this.jdbcTemplate.update(sql, new PreparedStatementSetter() {

			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				ps.setLong(1, pfb.getCustomerid());
				ps.setBigDecimal(2, pfb.getCollectionPFfee());
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
		String sql = "delete from paifeirule_collection where id=? ";
		this.jdbcTemplate.update(sql, id);
	}

	/**
	 * @param pfruleid
	 * @param value
	 * @return
	 */
	public PFcollection getPFcollectionByPfruleidAndtabid(long pfruleid, int tabid) {
		String sql = "select * from paifeirule_collection where pfruleid=? and tabid=?";

		try {
			return this.jdbcTemplate.queryForObject(sql, new PFcollectionRowMapper(), pfruleid, tabid);
		} catch (Exception e) {
			return null;
		}
	}
}

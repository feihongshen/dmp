package cn.explink.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import cn.explink.domain.CwbTemp;

@Component
public class CwbTempDAO {
	private final class CwbTempMapper implements RowMapper<CwbTemp> {
		@Override
		public CwbTemp mapRow(ResultSet rs, int rowNum) throws SQLException {
			CwbTemp cwbTemp = new CwbTemp();

			cwbTemp.setId(rs.getLong("id"));
			cwbTemp.setCwb(rs.getString("cwb"));
			cwbTemp.setCreatetime(rs.getString("createtime"));
			cwbTemp.setCwbtype(rs.getString("cwbtype"));
			cwbTemp.setState(rs.getInt("state"));
			cwbTemp.setSoCwb(rs.getString("soCwb"));
			cwbTemp.setAsnCwb(rs.getString("asnCwb"));

			return cwbTemp;
		}
	}

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public long createCwbTemp(final CwbTemp cwbTemp) {
		KeyHolder key = new GeneratedKeyHolder();
		jdbcTemplate.update(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(java.sql.Connection con) throws SQLException {
				PreparedStatement ps = null;
				ps = con.prepareStatement("insert into express_ops_cwb_temp(cwb,createtime,cwbtype,state,asnCwb,soCwb) values(?,?,?,?,?,?)", new String[] { "id" });
				ps.setString(1, cwbTemp.getCwb());
				ps.setString(2, cwbTemp.getCreatetime());
				ps.setString(3, cwbTemp.getCwbtype());
				ps.setInt(4, cwbTemp.getState());
				ps.setString(5, cwbTemp.getAsnCwb());
				ps.setString(6, cwbTemp.getSoCwb());
				return ps;
			}
		}, key);
		return key.getKey().longValue();
	}

	public List<CwbTemp> getCwbTemp(String states, String cwbtype) {
		String sql = "select * from express_ops_cwb_temp where state in (" + states + ") and cwbtype=? order by state ASC";
		return jdbcTemplate.query(sql, new CwbTempMapper(), cwbtype);
	}

	public List<CwbTemp> getCwbTempByCwbs(String cwbs) {
		String sql = "select * from express_ops_cwb_temp where cwb in (" + cwbs + ")";
		return jdbcTemplate.query(sql, new CwbTempMapper());
	}

	public void updateCwbtemp(String cwb, int state) {
		String sql = "update express_ops_cwb_temp set state=? where cwb=?";
		jdbcTemplate.update(sql, state, cwb);
	}

	public long checkCwb(String cwb) {
		String sql = "select count(1) from express_ops_cwb_temp  where cwb=?";
		return jdbcTemplate.queryForLong(sql, cwb);
	}

	public long checkSOCwb(String soCwb) {
		String sql = "select count(1) from express_ops_cwb_temp  where soCwb=? and cwbtype='SO' and state=1";
		return jdbcTemplate.queryForLong(sql, soCwb);
	}

	public void updateAsnCwbBySoCwb(String soCwb, String asnCwb) {
		String sql = "update express_ops_cwb_temp set asnCwb=? where soCwb=? and cwbtype='SO' ";
		jdbcTemplate.update(sql, asnCwb, soCwb);
	}

	public void updateSoCwbByCwb(String soCwb, String cwb) {
		String sql = "update express_ops_cwb_temp set soCwb=? where cwb=? and cwbtype='SO' ";
		jdbcTemplate.update(sql, soCwb, cwb);
	}

	public void updateAsnCwbByCwb(String asnCwb, String cwb) {
		String sql = "update express_ops_cwb_temp set asnCwb=? where cwb=? and cwbtype='ASN' ";
		jdbcTemplate.update(sql, asnCwb, cwb);
	}

}

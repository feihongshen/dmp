package cn.explink.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import cn.explink.domain.NoPiPeiCwbDetail;

@Component
public class NoPiPeiCwbDetailDAO {
	private Logger logger = LoggerFactory.getLogger(NoPiPeiCwbDetailDAO.class);

	private final class NoPiPeiCwbDetailMapper implements RowMapper<NoPiPeiCwbDetail> {

		@Override
		public NoPiPeiCwbDetail mapRow(ResultSet rs, int rowNum) throws SQLException {
			NoPiPeiCwbDetail noPiPeiCwbDetail = new NoPiPeiCwbDetail();
			noPiPeiCwbDetail.setId(rs.getLong("id"));
			noPiPeiCwbDetail.setCreatetime(rs.getString("createtime"));
			noPiPeiCwbDetail.setCwb(rs.getString("cwb"));
			noPiPeiCwbDetail.setCarwarehouseid(rs.getLong("carwarehouseid"));

			return noPiPeiCwbDetail;
		}
	}

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public void creNoPiPeiCwbDetail(final NoPiPeiCwbDetail noPiPeiCwbDetail) {
		jdbcTemplate.update("INSERT INTO `ops_nopipeicwbdetail`(`cwb`,`createtime`,`carwarehouseid`) VALUES ( ?,NOW(),?)", new PreparedStatementSetter() {

			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				ps.setString(1, noPiPeiCwbDetail.getCwb());
				ps.setLong(2, noPiPeiCwbDetail.getCarwarehouseid());
			}
		});
	}

	public void saveNoPiPeiCwbDetail(final NoPiPeiCwbDetail noPiPeiCwbDetail) {
		jdbcTemplate.update("update `ops_nopipeicwbdetail` set `carwarehouseid`=? where `cwb`=?", new PreparedStatementSetter() {

			@Override
			public void setValues(PreparedStatement ps) throws SQLException {

				ps.setLong(1, noPiPeiCwbDetail.getCarwarehouseid());
				ps.setString(2, noPiPeiCwbDetail.getCwb());
			}
		});
	}

	public List<String> getNoPiPeiCwbDetailByCarwarehouseid(long carwarehouseid) {
		String sql = "SELECT cwb from ops_nopipeicwbdetail where carwarehouseid=?";
		return jdbcTemplate.queryForList(sql, String.class, carwarehouseid);
	}

	public NoPiPeiCwbDetail getNoPiPeiCwbDetailByCwb(String cwb, long carwarehouseid) {
		try {
			return jdbcTemplate.queryForObject("SELECT * from ops_nopipeicwbdetail where cwb=? and carwarehouseid=? limit 0,1", new NoPiPeiCwbDetailMapper(), cwb, carwarehouseid);
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	public long getNoPiPeiCwbDetailByCwbCount(String cwb) {
		return jdbcTemplate.queryForLong("SELECT count(1) from ops_nopipeicwbdetail where cwb=? ", cwb);
	}

	public void deleteNoPiPeiCwbDetailByCwb(String cwb) {
		String sql = "DELETE FROM `ops_nopipeicwbdetail` WHERE `cwb` = ? ";
		jdbcTemplate.update(sql, cwb);
	}

}

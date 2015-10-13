package cn.explink.dao;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import cn.explink.domain.CwbTiHuo;
import cn.explink.util.Page;
import cn.explink.util.StringUtil;

@Component
public class CwbTiHuoDAO {
	private final class CwbTiHuoMapper implements RowMapper<CwbTiHuo> {
		@Override
		public CwbTiHuo mapRow(ResultSet rs, int rowNum) throws SQLException {
			CwbTiHuo cwbTiHuo = new CwbTiHuo();

			cwbTiHuo.setId(rs.getLong("id"));
			cwbTiHuo.setCwb(rs.getString("cwb"));
			cwbTiHuo.setEmaildate(StringUtil.nullConvertToEmptyString(rs.getString("emaildate")));
			cwbTiHuo.setCurrentbranchid(rs.getLong("currentbranchid"));
			cwbTiHuo.setTihuotime(StringUtil.nullConvertToEmptyString(rs.getString("tihuotime")));
			cwbTiHuo.setCustomerid(rs.getLong("customerid"));
			cwbTiHuo.setCwbordertypeid(rs.getLong("cwbordertypeid"));
			cwbTiHuo.setReceivablefee(rs.getBigDecimal("receivablefee"));
			cwbTiHuo.setPaybackfee(rs.getBigDecimal("paybackfee"));
			cwbTiHuo.setUserid(rs.getLong("userid"));

			return cwbTiHuo;
		}
	}

	private final class CwbTiHuoSumMapper implements RowMapper<CwbTiHuo> {
		@Override
		public CwbTiHuo mapRow(ResultSet rs, int rowNum) throws SQLException {
			CwbTiHuo cwbTiHuo = new CwbTiHuo();
			cwbTiHuo.setId(rs.getLong("id"));
			cwbTiHuo.setReceivablefee(rs.getBigDecimal("receivablefee"));
			cwbTiHuo.setPaybackfee(rs.getBigDecimal("paybackfee"));

			return cwbTiHuo;
		}
	}

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public void creCwbTiHuo(final CwbTiHuo cwbTiHuo) {

		jdbcTemplate.update("INSERT INTO `ops_cwb_tihuo`(`cwb`,`emaildate`,`currentbranchid`,`tihuotime`," + "`cwbordertypeid`,`userid`,`customerid`,`receivablefee`,`paybackfee`) "
				+ " VALUES ( ?,?,?,?,?,?,?,?,?)", new PreparedStatementSetter() {

			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				ps.setString(1, cwbTiHuo.getCwb());
				ps.setString(2, cwbTiHuo.getEmaildate());
				ps.setLong(3, cwbTiHuo.getCurrentbranchid());
				ps.setString(4, cwbTiHuo.getTihuotime());
				ps.setLong(5, cwbTiHuo.getCwbordertypeid());
				ps.setLong(6, cwbTiHuo.getUserid());
				ps.setLong(7, cwbTiHuo.getCustomerid());
				ps.setBigDecimal(8, cwbTiHuo.getReceivablefee());
				ps.setBigDecimal(9, cwbTiHuo.getPaybackfee());
			}
		});

	}

	public void saveCwbTiHuoById(final CwbTiHuo cwbTiHuo) {
		jdbcTemplate.update("UPDATE `ops_cwb_tihuo` SET " + "`emaildate`=?,`currentbranchid`=?,`tihuotime`=?,`cwbordertypeid`=?,"
				+ "`userid`=?,`customerid`=?,`receivablefee`=?,`paybackfee`=? WHERE `id`=?", new PreparedStatementSetter() {

			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				ps.setString(1, cwbTiHuo.getEmaildate());
				ps.setLong(2, cwbTiHuo.getCurrentbranchid());
				ps.setString(3, cwbTiHuo.getTihuotime());
				ps.setLong(4, cwbTiHuo.getCwbordertypeid());
				ps.setLong(5, cwbTiHuo.getUserid());
				ps.setLong(6, cwbTiHuo.getCustomerid());
				ps.setBigDecimal(7, cwbTiHuo.getReceivablefee());
				ps.setBigDecimal(8, cwbTiHuo.getPaybackfee());
				ps.setLong(9, cwbTiHuo.getId());
			}
		});
	}

	public List<CwbTiHuo> getCwbTiHuoList(long page, String begindate, String enddate, String customerids) {
		String sql = "SELECT * FROM ops_cwb_tihuo FORCE INDEX(TiHuo_TiHuoTime_Idx) where tihuotime >='" + begindate + "' and tihuotime <='" + enddate + "'";
		if (customerids.length() > 0) {
			sql += " and customerid in(" + customerids + ") ";
		}
		sql += "  limit " + (page - 1) * Page.ONE_PAGE_NUMBER + " ," + Page.ONE_PAGE_NUMBER;
		System.out.println(sql);
		return jdbcTemplate.query(sql, new CwbTiHuoMapper());
	}

	public CwbTiHuo getCwbTiHuoSum(String begindate, String enddate, String customerids) {
		String sql = "SELECT count(1) as id,sum(receivablefee) as receivablefee,sum(paybackfee) as paybackfee FROM ops_cwb_tihuo FORCE INDEX(TiHuo_TiHuoTime_Idx) where tihuotime >='" + begindate
				+ "' and tihuotime <='" + enddate + "'";
		if (customerids.length() > 0) {
			sql += " and customerid in(" + customerids + ") ";
		}
		return jdbcTemplate.queryForObject(sql, new CwbTiHuoSumMapper());
	}

	public List<CwbTiHuo> checkCwbTiHuo(String cwb) {
		String sql = "SELECT * FROM ops_cwb_tihuo  WHERE cwb=? limit 0,1";
		return jdbcTemplate.query(sql, new CwbTiHuoMapper(), cwb);
	}

	public void deleteCwbTiHuo(String cwb) {
		String sql = "DELETE FROM ops_cwb_tihuo WHERE cwb=? ";
		jdbcTemplate.update(sql, cwb);
	}

	public void updateXiuGaiJinE(String cwb, BigDecimal receivablefee, BigDecimal paybackfee) {
		jdbcTemplate.update("update ops_cwb_tihuo set receivablefee=?,paybackfee=? where cwb=?", receivablefee, paybackfee, cwb);
	}

	public void updateXiuGaiDingDanLeiXing(String cwb, int cwbordertypeid) {
		jdbcTemplate.update("update ops_cwb_tihuo set cwbordertypeid=? where cwb=?", cwbordertypeid, cwb);
	}
}

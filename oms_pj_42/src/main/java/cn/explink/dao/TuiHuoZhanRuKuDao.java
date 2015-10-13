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

import cn.explink.domain.TuiHuoZhanRuKuOrder;
import cn.explink.util.Page;

@Component
public class TuiHuoZhanRuKuDao {
	@Autowired
	JdbcTemplate jdbcTemplate;

	private class TuiHuoZhanRuKuMapper implements RowMapper<TuiHuoZhanRuKuOrder> {

		@Override
		public TuiHuoZhanRuKuOrder mapRow(ResultSet rs, int rowNum) throws SQLException {
			TuiHuoZhanRuKuOrder to = new TuiHuoZhanRuKuOrder();
			to.setCustomerid(rs.getLong("customerid"));
			to.setCwb(rs.getString("cwb"));
			to.setCwbordertypeid(rs.getLong("cwbordertypeid"));
			to.setDeliverystateid(rs.getLong("deliverystateid"));
			to.setId(rs.getLong("id"));
			to.setPaybackfee(rs.getBigDecimal("paybackfee"));
			to.setReceivablefee(rs.getBigDecimal("receivablefee"));
			to.setRukutime(rs.getString("rukutime"));
			to.setTuihuobranchid(rs.getLong("tuihuobranchid"));
			return to;
		}
	}

	public List<TuiHuoZhanRuKuOrder> checkIsExist(String cwb) {
		String sql = " select * from ops_tuihuozhanruku where cwb=? ";
		return jdbcTemplate.query(sql, new TuiHuoZhanRuKuMapper(), cwb);
	}

	public void updateTuiHuoZhanRuKu(final TuiHuoZhanRuKuOrder tuiHuoZhanRuKuOrder) {

		String sql = "update ops_tuihuozhanruku set cwbordertypeid=?,tuihuobranchid=?,deliverystateid=?,rukutime=?,customerid=?,receivablefee=?,paybackfee=? where cwb=?";
		jdbcTemplate.update(sql, new PreparedStatementSetter() {

			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				ps.setLong(1, tuiHuoZhanRuKuOrder.getCwbordertypeid());
				ps.setLong(2, tuiHuoZhanRuKuOrder.getTuihuobranchid());
				ps.setLong(3, tuiHuoZhanRuKuOrder.getDeliverystateid());
				ps.setString(4, tuiHuoZhanRuKuOrder.getRukutime());
				ps.setLong(5, tuiHuoZhanRuKuOrder.getCustomerid());
				ps.setBigDecimal(6, tuiHuoZhanRuKuOrder.getReceivablefee());
				ps.setBigDecimal(7, tuiHuoZhanRuKuOrder.getPaybackfee());
				ps.setString(8, tuiHuoZhanRuKuOrder.getCwb());
			}
		});

	}

	public void creTuiHuoZhanRuKu(final TuiHuoZhanRuKuOrder tuiHuoZhanRuKuOrder) {
		String sql = " INSERT INTO `ops_tuihuozhanruku`(`cwb`,`cwbordertypeid`,`tuihuobranchid`,`deliverystateid`,`rukutime`,`customerid`,`receivablefee`,`paybackfee`) VALUES ( ?,?,?,?,?,?,?,?) ";
		jdbcTemplate.update(sql, new PreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				ps.setString(1, tuiHuoZhanRuKuOrder.getCwb());
				ps.setLong(2, tuiHuoZhanRuKuOrder.getCwbordertypeid());
				ps.setLong(3, tuiHuoZhanRuKuOrder.getTuihuobranchid());
				ps.setLong(4, tuiHuoZhanRuKuOrder.getDeliverystateid());
				ps.setString(5, tuiHuoZhanRuKuOrder.getRukutime());
				ps.setLong(6, tuiHuoZhanRuKuOrder.getCustomerid());
				ps.setBigDecimal(7, tuiHuoZhanRuKuOrder.getReceivablefee());
				ps.setBigDecimal(8, tuiHuoZhanRuKuOrder.getPaybackfee());
			}
		});

	}

	/**
	 * 根据条件 查询
	 * 
	 * @param begindate
	 * @param enddate
	 * @param branchids
	 * @param customerids
	 * @param cwbordertypeids
	 * @param page
	 * @return
	 */
	public List<TuiHuoZhanRuKuOrder> getTuiHuoRecordByTuihuozhanruku(String begindate, String enddate, String branchids, String customerids, String cwbordertypeids, long page) {
		String sql = " select * from ops_tuihuozhanruku  FORCE INDEX(tuihuozhanruku_rukutime_idx ) where rukutime>='" + begindate + "' and rukutime<='" + enddate + "'";
		sql = this.getSqlByWhere(sql, branchids, customerids, cwbordertypeids);
		sql += " limit " + (page - 1) * Page.ONE_PAGE_NUMBER + "," + Page.ONE_PAGE_NUMBER;
		return jdbcTemplate.query(sql, new TuiHuoZhanRuKuMapper());
	}

	private String getSqlByWhere(String sql, String branchids, String customerids, String cwbordertypeids) {
		if (branchids.length() > 0) {
			sql += " and tuihuobranchid in(" + branchids + ")";
		}
		if (customerids.length() > 0) {
			sql += " and customerid in(" + customerids + ")";
		}
		if (cwbordertypeids.length() > 0) {
			sql += " and cwbordertypeid in(" + cwbordertypeids + ")";
		}
		return sql;
	}

	public long getTuiHuoRecordByTuihuozhanrukuCount(String begindate, String enddate, String branchids, String customerids, String cwbordertypeids) {
		String sql = " select count(1) from ops_tuihuozhanruku  FORCE INDEX(tuihuozhanruku_rukutime_idx ) where rukutime>='" + begindate + "' and rukutime<='" + enddate + "'";
		sql = this.getSqlByWhere(sql, branchids, customerids, cwbordertypeids);
		return jdbcTemplate.queryForLong(sql);
	}

	public void updateXiuGaiJinE(String cwb, BigDecimal receivablefee, BigDecimal paybackfee) {
		jdbcTemplate.update("update ops_tuihuozhanruku set receivablefee=?,paybackfee=? where cwb=?", receivablefee, paybackfee, cwb);
	}

	public void updateXiuGaiDingDanLeiXing(String cwb, int cwbordertypeid) {
		jdbcTemplate.update("update ops_tuihuozhanruku set cwbordertypeid=? where cwb=?", cwbordertypeid, cwb);
	}
}

package cn.explink.dao;

import java.math.BigDecimal;
import java.security.interfaces.RSAKey;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import cn.explink.domain.KuFangZaiTuOrder;
import cn.explink.util.Page;

@Component
public class KuFangZaiTuDao {
	@Autowired
	JdbcTemplate jdbcTemplate;

	private class KuFangZaiTuMapper implements RowMapper<KuFangZaiTuOrder> {

		@Override
		public KuFangZaiTuOrder mapRow(ResultSet rs, int rowNum) throws SQLException {
			KuFangZaiTuOrder kfo = new KuFangZaiTuOrder();
			kfo.setCwb(rs.getString("cwb"));
			kfo.setCwbordertypeid(rs.getLong("cwbordertypeid"));
			kfo.setDeliverybranchid(rs.getLong("deliverybranchid"));
			kfo.setEmaildate(rs.getString("emaildate"));
			kfo.setId(rs.getLong("id"));
			kfo.setNextbranchid(rs.getLong("nextbranchid"));
			kfo.setOutbranchid(rs.getLong("outbranchid"));
			kfo.setOutwarehousetime(rs.getString("outwarehousetime"));
			kfo.setPaybackfee(rs.getBigDecimal("paybackfee"));
			kfo.setReceivablefee(rs.getBigDecimal("receivablefee"));
			kfo.setCustomerid(rs.getLong("customerid"));
			return kfo;
		}
	}

	public List<KuFangZaiTuOrder> getKuFangZaiTu(long page, String begindate, String enddate, String kufangids, String nextbranchids, String cwbordertypeids, long datetype, String orderName) {
		String sql = " select * from ops_kufangzaitu where ";
		sql = this.getSqlByWhere(sql, datetype, begindate, enddate, kufangids, nextbranchids, cwbordertypeids);
		sql += "order by " + orderName;
		sql += " limit " + (page - 1) * Page.ONE_PAGE_NUMBER + "," + Page.ONE_PAGE_NUMBER;

		return jdbcTemplate.query(sql, new KuFangZaiTuMapper());
	}

	private String getSqlByWhere(String sql, long datetype, String begindate, String enddate, String kufangids, String nextbranchids, String cwbordertypeids) {
		if (datetype == 1) {
			sql = sql.replace("ops_kufangzaitu", "ops_kufangzaitu FORCE INDEX(kufangzaitu_emaildate_idx )");
			sql += " emaildate >= '" + begindate + "' ";
			sql += " and emaildate <= '" + enddate + "' ";
		} else {
			sql = sql.replace("ops_kufangzaitu", "ops_kufangzaitu  FORCE INDEX(kufangzaitu_outwarehousetime_idx)");
			sql += " outwarehousetime >= '" + begindate + "' ";
			sql += " and outwarehousetime <= '" + enddate + "' ";
		}
		if (kufangids.length() > 0 || nextbranchids.length() > 0 || cwbordertypeids.length() > 0) {
			if (kufangids.length() > 0) {
				sql += " and outbranchid in(" + kufangids + ")";
			}
			if (nextbranchids.length() > 0) {
				sql += " and nextbranchid in(" + nextbranchids + ")";
			}
			if (cwbordertypeids.length() > 0) {
				sql += " and cwbordertypeid in(" + cwbordertypeids + ")";
			}

		}
		return sql;
	}

	public List<KuFangZaiTuOrder> checkCwbIsExist(String cwb) {
		String sql = " select * from ops_kufangzaitu where cwb=? ";
		return jdbcTemplate.query(sql, new KuFangZaiTuMapper(), cwb);
	}

	public void creKuFangZaiTu(final KuFangZaiTuOrder kf) {
		String sql = "INSERT INTO `ops_kufangzaitu`(`cwbordertypeid`,`deliverybranchid`,`emaildate`,`outwarehousetime`,`outbranchid`,`nextbranchid`,`receivablefee`,`paybackfee`,`customerid`,`cwb`) VALUES (?,?,?,?,?,?,?,?,?,?) ";
		jdbcTemplate.update(sql, new PreparedStatementSetter() {

			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				ps.setLong(1, kf.getCwbordertypeid());
				ps.setLong(2, kf.getDeliverybranchid());
				ps.setString(3, kf.getEmaildate());
				ps.setString(4, kf.getOutwarehousetime());
				ps.setLong(5, kf.getOutbranchid());
				ps.setLong(6, kf.getNextbranchid());
				ps.setBigDecimal(7, kf.getReceivablefee());
				ps.setBigDecimal(8, kf.getPaybackfee());
				ps.setLong(9, kf.getCustomerid());
				ps.setString(10, kf.getCwb());
			}
		});

	}

	public void saveKuFangZaiTu(final KuFangZaiTuOrder kf) {
		String sql = " update  ops_kufangzaitu set cwbordertypeid=?,deliverybranchid=?,emaildate=?,outwarehousetime=?,outbranchid=?,nextbranchid=?,receivablefee=?,paybackfee=?,customerid=? where cwb=?";
		jdbcTemplate.update(sql, new PreparedStatementSetter() {

			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				ps.setLong(1, kf.getCwbordertypeid());
				ps.setLong(2, kf.getDeliverybranchid());
				ps.setString(3, kf.getEmaildate());
				ps.setString(4, kf.getOutwarehousetime());
				ps.setLong(5, kf.getOutbranchid());
				ps.setLong(6, kf.getNextbranchid());
				ps.setBigDecimal(7, kf.getReceivablefee());
				ps.setBigDecimal(8, kf.getPaybackfee());
				ps.setLong(9, kf.getCustomerid());
				ps.setString(10, kf.getCwb());
			}
		});

	}

	public KuFangZaiTuOrder getKuFangZaiTuCount(String begindate, String enddate, String kufangids, String nextbranchids, String cwbordertypeids, long datetype) {
		String sql = "select count(1) as id,sum(receivablefee) as receivablefee,sum(paybackfee) as paybackfee from ops_kufangzaitu where";
		sql = this.getSqlByWhere(sql, datetype, begindate, enddate, kufangids, nextbranchids, cwbordertypeids);

		return jdbcTemplate.queryForObject(sql, new KuFangZaiTuSum());
	}

	private class KuFangZaiTuSum implements RowMapper<KuFangZaiTuOrder> {

		@Override
		public KuFangZaiTuOrder mapRow(ResultSet rs, int rowNum) throws SQLException {
			KuFangZaiTuOrder kf = new KuFangZaiTuOrder();
			kf.setId(rs.getLong("id"));
			kf.setReceivablefee(rs.getBigDecimal("receivablefee"));
			kf.setPaybackfee(rs.getBigDecimal("paybackfee"));

			return kf;
		}

	}

	/**
	 * 根据订单删除当前状态 不是在途的订单
	 * 
	 * @param cwb
	 */
	public void deleteByCwb(String cwb) {
		String sql = " delete from ops_kufangzaitu  where cwb=?";
		jdbcTemplate.update(sql, cwb);
	}

	public void updateXiuGaiJinE(String cwb, BigDecimal receivablefee, BigDecimal paybackfee) {
		jdbcTemplate.update("update ops_kufangzaitu set receivablefee=?,paybackfee=? where cwb=?", receivablefee, paybackfee, cwb);
	}

	public void updateXiuGaiDingDanLeiXing(String cwb, int cwbordertypeid) {
		jdbcTemplate.update("update ops_kufangzaitu set cwbordertypeid=? where cwb=?", cwbordertypeid, cwb);
	}
}

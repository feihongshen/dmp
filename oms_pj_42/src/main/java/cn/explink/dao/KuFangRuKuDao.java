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

import cn.explink.domain.CwbOrder;
import cn.explink.domain.KuFangRuKuOrder;
import cn.explink.util.Page;

@Component
public class KuFangRuKuDao {
	@Autowired
	JdbcTemplate jdbcTemplate;

	private class KuFangRuKuMapper implements RowMapper<KuFangRuKuOrder> {

		@Override
		public KuFangRuKuOrder mapRow(ResultSet rs, int rowNum) throws SQLException {
			KuFangRuKuOrder kf = new KuFangRuKuOrder();
			kf.setCustomerid(rs.getLong("customerid"));
			kf.setCwb(rs.getString("cwb"));
			kf.setCwbordertypeid(rs.getLong("cwbordertypeid"));
			kf.setDeliverybranchid(rs.getLong("deliverybranchid"));
			kf.setEmaildate(rs.getString("emaildate"));
			kf.setId(rs.getLong("id"));
			kf.setIntobranchid(rs.getLong("intobranchid"));
			kf.setIntowarehousetime(rs.getString("intowarehousetime"));
			kf.setIsruku(rs.getLong("isruku"));
			kf.setPaybackfee(rs.getBigDecimal("paybackfee"));
			kf.setReceivablefee(rs.getBigDecimal("receivablefee"));
			kf.setIntowarehouseuserid(rs.getLong("intowarehouseuserid"));
			return kf;
		}

	}

	private final class CwbGHSDL implements RowMapper<KuFangRuKuOrder> {
		@Override
		public KuFangRuKuOrder mapRow(ResultSet rs, int rowNum) throws SQLException {
			KuFangRuKuOrder kf = new KuFangRuKuOrder();
			kf.setId(rs.getLong("id"));
			kf.setCustomerid(rs.getLong("customerid"));
			return kf;
		}
	}

	public List<KuFangRuKuOrder> checkCwbIsExist(String cwb) {
		String sql = " select * from ops_kufangruku where cwb=?";
		return jdbcTemplate.query(sql, new KuFangRuKuMapper(), cwb);
	}

	public void creKuFangRuKu(final KuFangRuKuOrder kf) {
		String sql = "INSERT INTO `ops_kufangruku`(`cwb`,`customerid`,`intowarehousetime`," + "`intobranchid`,`cwbordertypeid`,`deliverybranchid`,`isruku`,`emaildate`,"
				+ "`paybackfee`,`receivablefee`,intowarehouseuserid) VALUES ( ?,?,?,?,?,?,?,?,?,?,?); ";
		jdbcTemplate.update(sql, new PreparedStatementSetter() {

			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				ps.setString(1, kf.getCwb());
				ps.setLong(2, kf.getCustomerid());
				ps.setString(3, kf.getIntowarehousetime());
				ps.setLong(4, kf.getIntobranchid());
				ps.setLong(5, kf.getCwbordertypeid());
				ps.setLong(6, kf.getDeliverybranchid());
				ps.setLong(7, kf.getIsruku());
				ps.setString(8, kf.getEmaildate());
				ps.setBigDecimal(9, kf.getPaybackfee());
				ps.setBigDecimal(10, kf.getReceivablefee());
				ps.setLong(11, kf.getIntowarehouseuserid());
			}

		});

	}

	public void saveKuFangRuKu(final KuFangRuKuOrder kf) {
		String sql = " UPDATE `ops_kufangruku` SET customerid=?,intowarehousetime=?,intobranchid=?,cwbordertypeid=?,deliverybranchid=?,"
				+ "isruku=?,emaildate=?,`paybackfee`=?,`receivablefee`=?,intowarehouseuserid=? WHERE cwb=? ";
		jdbcTemplate.update(sql, new PreparedStatementSetter() {

			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				ps.setLong(1, kf.getCustomerid());
				ps.setString(2, kf.getIntowarehousetime());
				ps.setLong(3, kf.getIntobranchid());
				ps.setLong(4, kf.getCwbordertypeid());
				ps.setLong(5, kf.getDeliverybranchid());
				ps.setLong(6, kf.getIsruku());
				ps.setString(7, kf.getEmaildate());
				ps.setBigDecimal(8, kf.getPaybackfee());
				ps.setBigDecimal(9, kf.getReceivablefee());
				ps.setLong(10, kf.getIntowarehouseuserid());
				ps.setString(11, kf.getCwb());
			}

		});

	}

	// isruku 1 入库
	public List<KuFangRuKuOrder> getKuKangRuKuOrders(long page, String begindate, String enddate, String emaildatebegin, String emaildateend, long kufangid, String customers, long cwbordertypeid,
			String isruku) {
		String sql = "select * from ops_kufangruku ";
		sql = this.getKuFangRuKuSql(sql, begindate, enddate, emaildatebegin, emaildateend, kufangid, customers, cwbordertypeid, isruku);
		sql += " limit " + (page - 1) * Page.ONE_PAGE_NUMBER + "," + Page.ONE_PAGE_NUMBER;
		return jdbcTemplate.query(sql, new KuFangRuKuMapper());
	}

	private String getKuFangRuKuSql(String sql, String begindate, String enddate, String emaildatebegin, String emaildateend, long kufangid, String customers, long cwbordertypeid, String isruku) {
		if ("false".equals(isruku)) {
			sql += "where emaildate>='" + emaildatebegin + "' and emaildate<='" + emaildateend + "'";
			sql += " and isruku=0";
		} else {
			sql += "where intowarehousetime>='" + begindate + "' and intowarehousetime<='" + enddate + "'";
			if (emaildatebegin.length() > 0) {
				sql += " and emaildate>='" + emaildatebegin + "'";
			}
			if (emaildateend.length() > 0) {
				sql += " and emaildate<='" + emaildateend + "'";
			}
			sql += " and isruku=1";
		}
		if (kufangid > 0) {
			sql += " and intobranchid=" + kufangid;
		}
		if (customers.length() > 0) {
			sql += " and customerid in(" + customers + ")";
		}
		if (cwbordertypeid > 0) {
			sql += " and cwbordertypeid=" + cwbordertypeid;
		}
		return sql;
	}

	public long getKuKangRuKuOrdersCount(String begindate, String enddate, String emaildatebegin, String emaildateend, long kufangid, String customers, long cwbordertypeid, String isruku) {
		String sql = "select count(1) from ops_kufangruku ";
		sql = this.getKuFangRuKuSql(sql, begindate, enddate, emaildatebegin, emaildateend, kufangid, customers, cwbordertypeid, isruku);
		return jdbcTemplate.queryForLong(sql);
	}

	public void deleteByCwb(String cwb) {
		String sql = " delete from ops_kufangruku where cwb=" + cwb;
		jdbcTemplate.update(sql);
	}

	/**
	 * 库房入库时间
	 * 
	 * @param emaildateStar
	 * @param emaildateEnd
	 * @param kufangid
	 * @return
	 */

	public List<KuFangRuKuOrder> getCwbByCustomeridAndEmaildate(String emaildateStar, String emaildateEnd, long kufangid) {
		String sql = "SELECT * FROM ops_kufangruku WHERE intowarehousetime>='" + emaildateStar + "' AND intowarehousetime<='" + emaildateEnd + " 23:59:59' ";
		if (kufangid > 0) {
			sql += "and intobranchid=" + kufangid;
		}
		sql += "   order BY intowarehousetime";
		return jdbcTemplate.query(sql, new KuFangRuKuMapper());
	}

	/**
	 * 库房入库时间
	 * 
	 * @param emaildateStar
	 * @param emaildateEnd
	 * @param kufangid
	 * @return
	 */

	public List<KuFangRuKuOrder> getEditByCustomeridAndEmaildate(String emaildateEnd, long kufangid, long customerid, long page) {
		String sql = "SELECT *  FROM ops_kufangruku WHERE intowarehousetime>='" + emaildateEnd + " 00:00:00' AND intowarehousetime<='" + emaildateEnd + " 23:59:59' ";
		if (kufangid > 0) {
			sql += "and intobranchid=" + kufangid;
		}
		if (customerid > 0) {
			sql += " and customerid=" + customerid;
		}
		sql += "  limit " + (page - 1) * Page.ONE_PAGE_NUMBER + " ," + Page.ONE_PAGE_NUMBER;
		return jdbcTemplate.query(sql, new KuFangRuKuMapper());
	}

	public long getCountByCustomeridAndEmaildate(String timer, long kufangid, long customerid) {

		String sql = "SELECT count(1)  FROM ops_kufangruku WHERE intowarehousetime>='" + timer + " 00:00:00' AND intowarehousetime<='" + timer + " 23:59:59' ";
		if (kufangid > 0) {
			sql += "and intobranchid=" + kufangid;
		}
		if (customerid > 0) {
			sql += " and customerid=" + customerid;
		}
		return jdbcTemplate.queryForLong(sql);
	}

	public void updateXiuGaiJinE(String cwb, BigDecimal receivablefee, BigDecimal paybackfee) {
		jdbcTemplate.update("update ops_kufangruku set receivablefee=?,paybackfee=? where cwb=?", receivablefee, paybackfee, cwb);
	}

	public void updateXiuGaiDingDanLeiXing(String cwb, int cwbordertypeid) {
		jdbcTemplate.update("update ops_kufangruku set cwbordertypeid=? where cwb=?", cwbordertypeid, cwb);
	}
}

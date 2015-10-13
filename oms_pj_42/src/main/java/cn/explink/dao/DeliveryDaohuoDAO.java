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

import cn.explink.domain.DeliveryDaohuo;
import cn.explink.util.Page;
import cn.explink.util.StringUtil;

@Component
public class DeliveryDaohuoDAO {
	private final class DeliveryDaohuoMapper implements RowMapper<DeliveryDaohuo> {
		@Override
		public DeliveryDaohuo mapRow(ResultSet rs, int rowNum) throws SQLException {
			DeliveryDaohuo deldaohuo = new DeliveryDaohuo();
			deldaohuo.setId(rs.getLong("id"));
			deldaohuo.setCwb(rs.getString("cwb"));
			deldaohuo.setCurrentbranchid(rs.getLong("currentbranchid"));
			deldaohuo.setEmaildate(StringUtil.nullConvertToEmptyString(rs.getString("emaildate")));
			deldaohuo.setInSitetime(StringUtil.nullConvertToEmptyString(rs.getString("inSitetime")));
			deldaohuo.setCustomerid(rs.getLong("customerid"));
			deldaohuo.setCwbordertypeid(rs.getString("cwbordertypeid"));
			deldaohuo.setIsnow(rs.getLong("isnow"));
			deldaohuo.setNextbranchid(rs.getLong("nextbranchid"));
			deldaohuo.setStartbranchid(rs.getLong("startbranchid"));
			deldaohuo.setReceivablefee(rs.getBigDecimal("receivablefee"));
			deldaohuo.setPaybackfee(rs.getBigDecimal("paybackfee"));
			return deldaohuo;
		}
	}

	private final class DeliveryDaohuoSumMapper implements RowMapper<DeliveryDaohuo> {
		@Override
		public DeliveryDaohuo mapRow(ResultSet rs, int rowNum) throws SQLException {
			DeliveryDaohuo deldaohuo = new DeliveryDaohuo();
			deldaohuo.setId(rs.getLong("id"));
			deldaohuo.setReceivablefee(rs.getBigDecimal("receivablefee"));
			deldaohuo.setPaybackfee(rs.getBigDecimal("paybackfee"));

			return deldaohuo;
		}
	}

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public void updateDeliveryDaohuoIsnow(String cwb) {
		jdbcTemplate.update("update ops_delivery_daohuo set isnow='0' where cwb=? and isnow='1'", cwb);
	}

	public void creDeliveryDaohuo(final DeliveryDaohuo del) {
		jdbcTemplate.update("INSERT INTO `ops_delivery_daohuo`(`cwb`,`currentbranchid`,`emaildate`,"
				+ "`inSitetime`,`customerid`,`cwbordertypeid`,`nextbranchid`,`startbranchid`,`receivablefee`,`paybackfee`,`isnow`) " + " VALUES ( ?,?,?,?,?,?,?,?,?,?,1)",
				new PreparedStatementSetter() {

					@Override
					public void setValues(PreparedStatement ps) throws SQLException {
						ps.setString(1, del.getCwb());
						ps.setLong(2, del.getCurrentbranchid());
						ps.setString(3, del.getEmaildate());
						ps.setString(4, del.getInSitetime());
						ps.setLong(5, del.getCustomerid());
						ps.setString(6, del.getCwbordertypeid());
						ps.setLong(7, del.getNextbranchid());
						ps.setLong(8, del.getStartbranchid());
						ps.setBigDecimal(9, del.getReceivablefee());
						ps.setBigDecimal(10, del.getPaybackfee());
					}
				});
	}

	public void saveDeliveryDaohuoByCwb(final DeliveryDaohuo del) {
		jdbcTemplate.update("UPDATE `ops_delivery_daohuo` SET " + "`currentbranchid`=?,`emaildate`=?,`inSitetime`=?,`customerid`=?," + "`cwbordertypeid`=?,`nextbranchid`=?,`startbranchid`=?,"
				+ "`receivablefee`=?,`paybackfee`=?,isnow=? WHERE `cwb`=?", new PreparedStatementSetter() {

			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				ps.setLong(1, del.getCurrentbranchid());
				ps.setString(2, del.getEmaildate());
				ps.setString(3, del.getInSitetime());
				ps.setLong(4, del.getCustomerid());
				ps.setString(5, del.getCwbordertypeid());
				ps.setLong(6, del.getNextbranchid());
				ps.setLong(7, del.getStartbranchid());
				ps.setBigDecimal(8, del.getReceivablefee());
				ps.setBigDecimal(9, del.getPaybackfee());
				ps.setLong(10, del.getIsnow());
				ps.setString(11, del.getCwb());
			}
		});
	}

	public List<DeliveryDaohuo> getDeliveryDaohuoList(String begindate, String enddate, long customerid, String kufangids, String cwbordertypeids, String currentBranchids, long isnow, long page) {
		String sql = "SELECT * FROM ops_delivery_daohuo FORCE INDEX(Delivery_Daohuo_InSitetime_dx) where inSitetime>='" + begindate + "'  and inSitetime<='" + enddate + "' and isnow = " + isnow;
		sql = setWhereSql(sql, customerid, kufangids, cwbordertypeids, currentBranchids);
		sql += "  limit " + (page - 1) * Page.ONE_PAGE_NUMBER + " ," + Page.ONE_PAGE_NUMBER;
		System.out.println(sql);
		return jdbcTemplate.query(sql, new DeliveryDaohuoMapper());
	}

	public DeliveryDaohuo getDeliveryDaohuoSum(String begindate, String enddate, long customerid, String kufangids, String cwbordertypeids, String currentBranchids, long isnow) {
		String sql = "SELECT count(1) as id,sum(receivablefee) as receivablefee,sum(paybackfee) as paybackfee FROM ops_delivery_daohuo  FORCE INDEX(Delivery_Daohuo_InSitetime_dx) where inSitetime>='"
				+ begindate + "'  and inSitetime<='" + enddate + "'  and isnow = " + isnow;
		sql = setWhereSql(sql, customerid, kufangids, cwbordertypeids, currentBranchids);
		return jdbcTemplate.queryForObject(sql, new DeliveryDaohuoSumMapper());
	}

	public List<DeliveryDaohuo> checkDeliveryDaohuo(String cwb) {
		String sql = "SELECT * FROM ops_delivery_daohuo  WHERE cwb=? limit 0,1";
		return jdbcTemplate.query(sql, new DeliveryDaohuoMapper(), cwb);
	}

	public List<DeliveryDaohuo> checkDeliveryDaohuoByCwbAndInSitetime(String cwb, String inSitetime) {
		String sql = "SELECT * FROM ops_delivery_daohuo  WHERE cwb=? and inSitetime<? limit 0,1";
		return jdbcTemplate.query(sql, new DeliveryDaohuoMapper(), cwb, inSitetime);
	}

	/**
	 * 
	 * @param sql
	 * @param customerid
	 * @param kufangids
	 * @param cwbordertypeids
	 * @param currentBranchids
	 * @param isnow
	 * @return
	 */
	public String setWhereSql(String sql, long customerid, String kufangids, String cwbordertypeids, String currentBranchids) {
		if (customerid > 0 || kufangids.length() > 0 || cwbordertypeids.length() > 0 || currentBranchids.length() > 0) {
			if (customerid > 0) {
				sql += " and customerid =" + customerid;
			}
			if (cwbordertypeids.length() > 0) {
				sql += " and cwbordertypeid in(" + cwbordertypeids + ") ";
			}
			if (kufangids.length() > 0) {
				sql += " and startbranchid in(" + kufangids + ") ";
			}
			if (currentBranchids.length() > 0) {
				sql += " and currentbranchid in(" + currentBranchids + ") ";
			}
		}
		return sql;
	}

	public List<String> getDaohuoDataByCurrentbranchidAndInSitetime(String begindate, String enddate, String flowordertypes, final long currentBranchid) {
		String sql = "select DISTINCT(cwb) from ops_delivery_daohuo FORCE INDEX(Delivery_Daohuo_InSitetime_dx)  where " + " inSitetime >= '" + begindate + "' and inSitetime <= '" + enddate + "' ";

		if (currentBranchid > 0) {
			sql += " and currentbranchid = " + currentBranchid;
		}
		return jdbcTemplate.queryForList(sql, String.class);
	}

	public List<DeliveryDaohuo> getDaohuoDataByCurrentbranchidAndInSitetimePage(long page, String begindate, String enddate, String flowordertypes, final long currentBranchid) {
		String sql = "select * from ops_delivery_daohuo FORCE INDEX(Delivery_Daohuo_InSitetime_dx)  where " + " inSitetime >= '" + begindate + "' and inSitetime <= '" + enddate + "' ";

		if (currentBranchid > 0) {
			sql += " and currentbranchid = " + currentBranchid;
		}

		sql += "  limit " + (page - 1) * Page.ONE_PAGE_NUMBER + " ," + Page.ONE_PAGE_NUMBER;
		return jdbcTemplate.query(sql, new DeliveryDaohuoMapper());
	}

	public long getDaohuoDataByCurrentbranchidAndInSitetimeCount(String begindate, String enddate, String flowordertypes, final long currentBranchid) {
		String sql = "select count(1) from ops_delivery_daohuo FORCE INDEX(Delivery_Daohuo_InSitetime_dx)  where " + " inSitetime >= '" + begindate + "' and inSitetime <= '" + enddate + "' ";

		if (currentBranchid > 0) {
			sql += " and currentbranchid = " + currentBranchid;
		}

		return jdbcTemplate.queryForLong(sql);
	}

	public void updateXiuGaiJinE(String cwb, BigDecimal receivablefee, BigDecimal paybackfee) {
		jdbcTemplate.update("update ops_delivery_daohuo set receivablefee=?,paybackfee=? where cwb=?", receivablefee, paybackfee, cwb);
	}

	public void updateXiuGaiDingDanLeiXing(String cwb, int cwbordertypeid) {
		jdbcTemplate.update("update ops_delivery_daohuo set cwbordertypeid=? where cwb=?", cwbordertypeid, cwb);
	}
}

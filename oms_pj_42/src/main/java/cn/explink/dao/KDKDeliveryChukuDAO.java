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

import cn.explink.domain.KDKDeliveryChuku;
import cn.explink.util.Page;
import cn.explink.util.StringUtil;

@Component
public class KDKDeliveryChukuDAO {
	private final class DeliveryMapper implements RowMapper<KDKDeliveryChuku> {
		@Override
		public KDKDeliveryChuku mapRow(ResultSet rs, int rowNum) throws SQLException {
			KDKDeliveryChuku del = new KDKDeliveryChuku();

			del.setId(rs.getLong("id"));
			del.setCwb(rs.getString("cwb"));
			del.setEmaildate(StringUtil.nullConvertToEmptyString(rs.getString("emaildate")));
			del.setStartbranchid(rs.getLong("startbranchid"));
			del.setOutstoreroomtime(StringUtil.nullConvertToEmptyString(rs.getString("outstoreroomtime")));
			del.setCustomerid(rs.getLong("customerid"));
			del.setCwbordertypeid(rs.getString("cwbordertypeid"));
			del.setNextbranchid(rs.getLong("nextbranchid"));
			del.setReceivablefee(rs.getBigDecimal("receivablefee"));
			del.setPaybackfee(rs.getBigDecimal("paybackfee"));

			return del;
		}
	}

	private final class DeliverySumMapper implements RowMapper<KDKDeliveryChuku> {
		@Override
		public KDKDeliveryChuku mapRow(ResultSet rs, int rowNum) throws SQLException {
			KDKDeliveryChuku del = new KDKDeliveryChuku();
			del.setId(rs.getLong("id"));
			del.setReceivablefee(rs.getBigDecimal("receivablefee"));
			del.setPaybackfee(rs.getBigDecimal("paybackfee"));

			return del;
		}
	}

	private final class DeliveryGroupNextbranchidMapper implements RowMapper<KDKDeliveryChuku> {
		@Override
		public KDKDeliveryChuku mapRow(ResultSet rs, int rowNum) throws SQLException {
			KDKDeliveryChuku del = new KDKDeliveryChuku();

			del.setId(rs.getLong("id"));
			del.setNextbranchid(rs.getLong("nextbranchid"));

			return del;
		}
	}

	private final class DeliveryGroupCustomeridMapper implements RowMapper<KDKDeliveryChuku> {
		@Override
		public KDKDeliveryChuku mapRow(ResultSet rs, int rowNum) throws SQLException {
			KDKDeliveryChuku del = new KDKDeliveryChuku();

			del.setId(rs.getLong("id"));
			del.setCustomerid(rs.getLong("customerid"));

			return del;
		}
	}

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public void creKDKDeliveryChuku(final KDKDeliveryChuku del) {

		jdbcTemplate.update("INSERT INTO `ops_delivery_kdkchuku`(`cwb`,`emaildate`,`startbranchid`,`nextbranchid`," + "`cwbordertypeid`,`outstoreroomtime`,`customerid`,`receivablefee`,`paybackfee`) "
				+ " VALUES ( ?,?,?,?,?,?,?,?,?)", new PreparedStatementSetter() {

			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				ps.setString(1, del.getCwb());
				ps.setString(2, del.getEmaildate());
				ps.setLong(3, del.getStartbranchid());
				ps.setLong(4, del.getNextbranchid());
				ps.setString(5, del.getCwbordertypeid());
				ps.setString(6, del.getOutstoreroomtime());
				ps.setLong(7, del.getCustomerid());
				ps.setBigDecimal(8, del.getReceivablefee());
				ps.setBigDecimal(9, del.getPaybackfee());
			}
		});

	}

	public void saveKDKDeliveryChuku(final KDKDeliveryChuku dechuku) {
		jdbcTemplate.update("UPDATE `ops_delivery_kdkchuku` SET " + "`emaildate`=?,`startbranchid`=?,`nextbranchid`=?,`cwbordertypeid`=?,"
				+ "`outstoreroomtime`=?,`customerid`=?,`receivablefee`=?,`paybackfee`=? WHERE `cwb`=?", new PreparedStatementSetter() {

			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				// TODO Auto-generated method stub
				ps.setString(1, dechuku.getEmaildate());
				ps.setLong(2, dechuku.getStartbranchid());
				ps.setLong(3, dechuku.getNextbranchid());
				ps.setString(4, dechuku.getCwbordertypeid());
				ps.setString(5, dechuku.getOutstoreroomtime());
				ps.setLong(6, dechuku.getCustomerid());
				ps.setBigDecimal(7, dechuku.getReceivablefee());
				ps.setBigDecimal(8, dechuku.getPaybackfee());
				ps.setString(9, dechuku.getCwb());
			}
		});
	}

	public List<KDKDeliveryChuku> getKDKDeliveryChukuList(long page, String begindate, String enddate, String customerids, String kufangids, String nextbranchids, String cwbordertypeids) {
		String sql = "SELECT * FROM ops_delivery_kdkchuku FORCE INDEX(Delivery_KDKChuku_Outstoreroomtime_Idx) where outstoreroomtime >='" + begindate + "' and outstoreroomtime <='" + enddate + "'";
		sql = setWhereSql(sql, customerids, kufangids, nextbranchids, cwbordertypeids);
		sql += " group by cwb";
		sql += "  limit " + (page - 1) * Page.ONE_PAGE_NUMBER + " ," + Page.ONE_PAGE_NUMBER;
		return jdbcTemplate.query(sql, new DeliveryMapper());
	}

	public KDKDeliveryChuku getKDKDeliveryChukuSum(String begindate, String enddate, String customerids, String kufangids, String nextbranchids, String cwbordertypeids) {
		String sql = "SELECT count(distinct(cwb)) as id,sum(receivablefee) as receivablefee,sum(paybackfee) as paybackfee FROM ops_delivery_kdkchuku FORCE INDEX(Delivery_KDKChuku_Outstoreroomtime_Idx) where outstoreroomtime >='"
				+ begindate + "' and outstoreroomtime <='" + enddate + "'";
		sql = setWhereSql(sql, customerids, kufangids, nextbranchids, cwbordertypeids);
		return jdbcTemplate.queryForObject(sql, new DeliverySumMapper());
	}

	public List<KDKDeliveryChuku> checkKDKDeliveryChuku(String cwb) {
		String sql = "SELECT * FROM ops_delivery_kdkchuku  WHERE cwb=? limit 0,1";
		return jdbcTemplate.query(sql, new DeliveryMapper(), cwb);
	}

	public void deleteKDKDeliveryChuku(String cwb) {
		String sql = "DELETE FROM ops_delivery_kdkchuku WHERE cwb=? ";
		jdbcTemplate.update(sql, cwb);
	}

	/**
	 * 
	 * @param sql
	 * @param customerids
	 * @param startbranchid
	 * @param nextbranchids
	 * @param cwbordertypeids
	 * @return
	 */
	public String setWhereSql(String sql, String customerids, String kufangids, String nextbranchids, String cwbordertypeids) {
		if (customerids.length() > 0 || cwbordertypeids.length() > 0 || nextbranchids.length() > 0 || kufangids.length() > 0) {
			if (customerids.length() > 0) {
				sql += " and customerid in(" + customerids + ") ";
			}
			if (cwbordertypeids.length() > 0) {
				sql += " and cwbordertypeid in(" + cwbordertypeids + ") ";
			}
			if (nextbranchids.length() > 0) {
				sql += " and nextbranchid in(" + nextbranchids + ") ";
			}
			if (kufangids.length() > 0) {
				sql += " and startbranchid in(" + kufangids + ")";
			}
		}
		return sql;
	}

	public List<KDKDeliveryChuku> getKDKDeliveryChukuCollectList(long page, long customerid, long kufangid, long nextbranchid, String begindate, String enddate) {
		String sql = "SELECT * FROM ops_delivery_kdkchuku FORCE INDEX(Delivery_KDKChuku_Outstoreroomtime_Idx) where outstoreroomtime>='" + begindate + "' and outstoreroomtime <= '" + enddate + "'";

		if (customerid > 0) {
			sql += " and customerid=" + customerid;
		}
		if (nextbranchid > 0) {
			sql += " and nextbranchid =" + nextbranchid;
		}
		if (kufangid > 0) {
			sql += " and startbranchid = " + kufangid;
		}
		sql += "  limit " + (page - 1) * Page.ONE_PAGE_NUMBER + " ," + Page.ONE_PAGE_NUMBER;
		System.out.println(sql);
		return jdbcTemplate.query(sql, new DeliveryMapper());
	}

	public KDKDeliveryChuku getKDKDeliveryChukuCollectSum(long customerid, long kufangid, long nextbranchid, String begindate, String enddate) {
		String sql = "SELECT count(1) as id,sum(receivablefee) as receivablefee,sum(paybackfee) as paybackfee FROM ops_delivery_kdkchuku FORCE INDEX(Delivery_KDKChuku_Outstoreroomtime_Idx) where outstoreroomtime>='"
				+ begindate + "' and outstoreroomtime <= '" + enddate + "'";

		if (customerid > 0) {
			sql += " and customerid=" + customerid;
		}
		if (nextbranchid > 0) {
			sql += " and nextbranchid =" + nextbranchid;
		}
		if (kufangid > 0) {
			sql += " and startbranchid = " + kufangid;
		}
		return jdbcTemplate.queryForObject(sql, new DeliverySumMapper());
	}

	public List<KDKDeliveryChuku> getKDKDeliveryChukuGroupByNextbranchid(long kufangid, String begindate, String enddate) {
		String sql = "select nextbranchid,count(1)as id from ops_delivery_kdkchuku FORCE INDEX(Delivery_KDKChuku_Outstoreroomtime_Idx) where "
				+ " outstoreroomtime >=? and outstoreroomtime <= ? and startbranchid = ? GROUP BY nextbranchid";

		return jdbcTemplate.query(sql, new DeliveryGroupNextbranchidMapper(), begindate, enddate, kufangid);
	}

	public List<KDKDeliveryChuku> getKDKDeliveryChukuGroupByCustomerid(long kufangid, String begindate, String enddate, String nextbranchids) {
		String sql = "select customerid,count(1)as id from ops_delivery_kdkchuku FORCE INDEX(Delivery_KDKChuku_Outstoreroomtime_Idx) where outstoreroomtime >=? and outstoreroomtime <=? and startbranchid=?  and nextbranchid in("
				+ nextbranchids + ") GROUP BY customerid";

		return jdbcTemplate.query(sql, new DeliveryGroupCustomeridMapper(), begindate, enddate, kufangid);
	}

	public List<KDKDeliveryChuku> getKDKDeliveryChukuByoutstoreroomtimeAndstartbranchid(long kufangid, String begindate, String enddate) {
		String sql = "select * from ops_delivery_kdkchuku FORCE INDEX(Delivery_KDKChuku_Outstoreroomtime_Idx) where outstoreroomtime >=? and outstoreroomtime <=? and startbranchid=? ";

		return jdbcTemplate.query(sql, new DeliveryMapper(), begindate, enddate, kufangid);
	}

	public void updateXiuGaiJinE(String cwb, BigDecimal receivablefee, BigDecimal paybackfee) {
		jdbcTemplate.update("update ops_delivery_kdkchuku set receivablefee=?,paybackfee=? where cwb=?", receivablefee, paybackfee, cwb);
	}

	public void updateXiuGaiDingDanLeiXing(String cwb, int cwbordertypeid) {
		jdbcTemplate.update("update ops_delivery_kdkchuku set cwbordertypeid=? where cwb=?", cwbordertypeid, cwb);
	}
}

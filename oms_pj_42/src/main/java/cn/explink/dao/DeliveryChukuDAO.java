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

import cn.explink.domain.DeliveryChuku;
import cn.explink.util.Page;
import cn.explink.util.StringUtil;

@Component
public class DeliveryChukuDAO {
	private final class DeliveryMapper implements RowMapper<DeliveryChuku> {
		@Override
		public DeliveryChuku mapRow(ResultSet rs, int rowNum) throws SQLException {
			DeliveryChuku del = new DeliveryChuku();

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
			del.setOutstoreroomtimeuserid(rs.getLong("outstoreroomtimeuserid"));
			return del;
		}
	}

	private final class DeliveryNotDetailMapper implements RowMapper<DeliveryChuku> {
		@Override
		public DeliveryChuku mapRow(ResultSet rs, int rowNum) throws SQLException {
			DeliveryChuku del = new DeliveryChuku();

			del.setId(rs.getLong("id"));
			del.setNextbranchid(rs.getLong("nextbranchid"));

			return del;
		}
	}

	private final class DeliverySumMapper implements RowMapper<DeliveryChuku> {
		@Override
		public DeliveryChuku mapRow(ResultSet rs, int rowNum) throws SQLException {
			DeliveryChuku del = new DeliveryChuku();
			del.setId(rs.getLong("id"));
			del.setReceivablefee(rs.getBigDecimal("receivablefee"));
			del.setPaybackfee(rs.getBigDecimal("paybackfee"));

			return del;
		}
	}

	private final class DeliveryGroupNextbranchidMapper implements RowMapper<DeliveryChuku> {
		@Override
		public DeliveryChuku mapRow(ResultSet rs, int rowNum) throws SQLException {
			DeliveryChuku del = new DeliveryChuku();

			del.setId(rs.getLong("id"));
			del.setNextbranchid(rs.getLong("nextbranchid"));

			return del;
		}
	}

	private final class CwbGHSDL implements RowMapper<DeliveryChuku> {
		@Override
		public DeliveryChuku mapRow(ResultSet rs, int rowNum) throws SQLException {
			DeliveryChuku del = new DeliveryChuku();

			del.setId(rs.getLong("id"));
			del.setCustomerid(rs.getLong("customerid"));

			return del;
		}
	}

	private final class DeliveryGroupCustomeridMapper implements RowMapper<DeliveryChuku> {
		@Override
		public DeliveryChuku mapRow(ResultSet rs, int rowNum) throws SQLException {
			DeliveryChuku del = new DeliveryChuku();

			del.setId(rs.getLong("id"));
			del.setCustomerid(rs.getLong("customerid"));

			return del;
		}
	}

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public void creDeliveryChuku(final DeliveryChuku del) {

		jdbcTemplate.update("INSERT INTO `ops_delivery_chuku`(`cwb`,`emaildate`,`startbranchid`,`nextbranchid`,"
				+ "`cwbordertypeid`,`outstoreroomtime`,`customerid`,`receivablefee`,`paybackfee`,outstoreroomtimeuserid) " + " VALUES ( ?,?,?,?,?,?,?,?,?,?)", new PreparedStatementSetter() {

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
				ps.setLong(10, del.getOutstoreroomtimeuserid());
			}
		});

	}

	public void saveDeliveryChuku(final DeliveryChuku dechuku) {
		jdbcTemplate.update("UPDATE `ops_delivery_chuku` SET " + "`emaildate`=?,`startbranchid`=?,`nextbranchid`=?,`cwbordertypeid`=?,"
				+ "`outstoreroomtime`=?,`customerid`=?,`receivablefee`=?,`paybackfee`=?,outstoreroomtimeuserid=? WHERE `cwb`=?", new PreparedStatementSetter() {

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
				ps.setLong(9, dechuku.getOutstoreroomtimeuserid());
				ps.setString(10, dechuku.getCwb());
			}
		});
	}

	public List<DeliveryChuku> getDeliveryChukuList(long page, String begindate, String enddate, String customerids, String kufangids, String nextbranchids, String cwbordertypeids) {
		String sql = "SELECT * FROM ops_delivery_chuku FORCE INDEX(Delivery_Chuku_Outstoreroomtime_Idx) where outstoreroomtime >='" + begindate + "' and outstoreroomtime <='" + enddate + "'";
		sql = setWhereSql(sql, customerids, kufangids, nextbranchids, cwbordertypeids);
		sql += " group by cwb";
		sql += "  limit " + (page - 1) * Page.ONE_PAGE_NUMBER + " ," + Page.ONE_PAGE_NUMBER;
		System.out.println("sql:" + sql);
		return jdbcTemplate.query(sql, new DeliveryMapper());
	}

	public DeliveryChuku getDeliveryChukuSum(String begindate, String enddate, String customerids, String kufangids, String nextbranchids, String cwbordertypeids) {
		String sql = "SELECT count(distinct(cwb)) as id,sum(receivablefee) as receivablefee,sum(paybackfee) as paybackfee FROM ops_delivery_chuku FORCE INDEX(Delivery_Chuku_Outstoreroomtime_Idx) where outstoreroomtime >='"
				+ begindate + "' and outstoreroomtime <='" + enddate + "'";
		sql = setWhereSql(sql, customerids, kufangids, nextbranchids, cwbordertypeids);
		return jdbcTemplate.queryForObject(sql, new DeliverySumMapper());
	}

	public List<DeliveryChuku> checkDeliveryChuku(String cwb) {
		String sql = "SELECT * FROM ops_delivery_chuku  WHERE cwb=? limit 0,1";
		return jdbcTemplate.query(sql, new DeliveryMapper(), cwb);
	}

	public void deleteDeliveryChuku(String cwb) {
		String sql = "DELETE FROM ops_delivery_chuku WHERE cwb=? ";
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

	public List<DeliveryChuku> getDeliveryChukuCollectList(long page, long customerid, long kufangid, long nextbranchid, String begindate, String enddate) {
		String sql = "SELECT * FROM ops_delivery_chuku FORCE INDEX(Delivery_Chuku_Outstoreroomtime_Idx) where outstoreroomtime>='" + begindate + "' and outstoreroomtime <= '" + enddate + "'";

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

	public DeliveryChuku getDeliveryChukuCollectSum(long customerid, long kufangid, long nextbranchid, String begindate, String enddate) {
		String sql = "SELECT count(1) as id,sum(receivablefee) as receivablefee,sum(paybackfee) as paybackfee FROM ops_delivery_chuku FORCE INDEX(Delivery_Chuku_Outstoreroomtime_Idx) where outstoreroomtime>='"
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

	public List<DeliveryChuku> getDeliveryChukuGroupByNextbranchid(long kufangid, String begindate, String enddate) {
		String sql = "select nextbranchid,count(1)as id from ops_delivery_chuku FORCE INDEX(Delivery_Chuku_Outstoreroomtime_Idx) where "
				+ " outstoreroomtime >=? and outstoreroomtime <= ? and startbranchid = ? GROUP BY nextbranchid";

		return jdbcTemplate.query(sql, new DeliveryGroupNextbranchidMapper(), begindate, enddate, kufangid);
	}

	public List<DeliveryChuku> getDeliveryChukuGroupByCustomerid(long kufangid, String begindate, String enddate, String nextbranchids) {
		String sql = "select customerid,count(1)as id from ops_delivery_chuku FORCE INDEX(Delivery_Chuku_Outstoreroomtime_Idx) where outstoreroomtime >=? and outstoreroomtime <=? and startbranchid=?  and nextbranchid in("
				+ nextbranchids + ") GROUP BY customerid";

		return jdbcTemplate.query(sql, new DeliveryGroupCustomeridMapper(), begindate, enddate, kufangid);
	}

	public List<DeliveryChuku> getDeliveryChukuByoutstoreroomtimeAndstartbranchid(long kufangid, String begindate, String enddate, long customerid, String nextbranchids) {
		String sql = "SELECT COUNT(1) as id,nextbranchid FROM ops_delivery_chuku " + "FORCE INDEX(Delivery_Chuku_Outstoreroomtime_Idx) WHERE outstoreroomtime >=? AND outstoreroomtime <=? "
				+ "AND startbranchid=? and customerid=? ";
		if (nextbranchids.length() > 0) {
			sql += " and nextbranchid in(" + nextbranchids + ")";
		}
		sql += " GROUP BY nextbranchid";
		return jdbcTemplate.query(sql, new DeliveryNotDetailMapper(), begindate, enddate, kufangid, customerid);
	}

	/**
	 * 库房出库时间
	 * 
	 * @param emaildateStar
	 * @param emaildateEnd
	 * @param kufangid
	 * @return
	 */

	public List<DeliveryChuku> getCwbByCustomeridAndEmaildate(String emaildateStar, String emaildateEnd, long kufangid) {
		String sql = "SELECT * FROM ops_delivery_chuku WHERE outstoreroomtime>='" + emaildateStar + " ' AND outstoreroomtime<='" + emaildateEnd + " 23:59:59' ";
		if (kufangid > 0) {
			sql += "and startbranchid=" + kufangid;
		}
		sql += "  order BY outstoreroomtime";
		return jdbcTemplate.query(sql, new DeliveryMapper());
	}

	/**
	 * 库房出库时间
	 * 
	 * @param emaildateStar
	 * @param emaildateEnd
	 * @param kufangid
	 * @return
	 */

	public List<DeliveryChuku> getEditByCustomeridAndEmaildate(String emaildateEnd, long kufangid, long customerid, long page) {
		String sql = "SELECT *  FROM ops_delivery_chuku WHERE outstoreroomtime>='" + emaildateEnd + " 00:00:00' AND outstoreroomtime<='" + emaildateEnd + " 23:59:59' ";
		if (kufangid > 0) {
			sql += "and startbranchid=" + kufangid;
		}
		if (customerid > 0) {
			sql += " and customerid=" + customerid;
		}
		sql += "  limit " + (page - 1) * Page.ONE_PAGE_NUMBER + " ," + Page.ONE_PAGE_NUMBER;
		return jdbcTemplate.query(sql, new DeliveryMapper());
	}

	public long getCountByCustomeridAndEmaildate(String timer, long kufangid, long customerid) {
		String sql = "SELECT count(1)  FROM ops_delivery_chuku WHERE outstoreroomtime>='" + timer + " 00:00:00' AND outstoreroomtime<='" + timer + " 23:59:59' ";
		if (kufangid > 0) {
			sql += "and startbranchid=" + kufangid;
		}
		if (customerid > 0) {
			sql += " and customerid=" + customerid;
		}
		return jdbcTemplate.queryForLong(sql);
	}

	public void updateXiuGaiJinE(String cwb, BigDecimal receivablefee, BigDecimal paybackfee) {
		jdbcTemplate.update("update ops_delivery_chuku set receivablefee=?,paybackfee=? where cwb=?", receivablefee, paybackfee, cwb);
	}

	public void updateXiuGaiDingDanLeiXing(String cwb, int cwbordertypeid) {
		jdbcTemplate.update("update ops_delivery_chuku set cwbordertypeid=? where cwb=?", cwbordertypeid, cwb);
	}
}

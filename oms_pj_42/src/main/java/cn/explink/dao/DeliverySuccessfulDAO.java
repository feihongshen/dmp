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

import cn.explink.domain.DeliverySuccessful;
import cn.explink.util.Page;
import cn.explink.util.StringUtil;

@Component
public class DeliverySuccessfulDAO {
	private final class DeliveryMapper implements RowMapper<DeliverySuccessful> {
		@Override
		public DeliverySuccessful mapRow(ResultSet rs, int rowNum) throws SQLException {
			DeliverySuccessful del = new DeliverySuccessful();
			del.setId(rs.getLong("id"));
			del.setAuditstate(rs.getLong("auditstate"));
			del.setAudittime(StringUtil.nullConvertToEmptyString(rs.getString("audittime")));
			del.setBranchid(rs.getLong("branchid"));
			del.setCreatetime(StringUtil.nullConvertToEmptyString(rs.getString("createtime")));
			del.setCustomerid(rs.getLong("customerid"));
			del.setCwb(rs.getString("cwb"));
			del.setCwbordertypeid(rs.getString("cwbordertypeid"));
			del.setDeliveryid(rs.getLong("deliveryid"));
			del.setDeliverystate(rs.getLong("deliverystate"));
			del.setDeliverystateid(rs.getLong("deliverystateid"));
			del.setDeliverytime(StringUtil.nullConvertToEmptyString(rs.getString("deliverytime")));
			del.setPaywayid(rs.getLong("paywayid"));
			del.setReceivablefee(rs.getBigDecimal("receivablefee"));
			del.setPaybackfee(rs.getBigDecimal("paybackfee"));

			return del;
		}
	}

	private final class DeliverySumMapper implements RowMapper<DeliverySuccessful> {
		@Override
		public DeliverySuccessful mapRow(ResultSet rs, int rowNum) throws SQLException {
			DeliverySuccessful del = new DeliverySuccessful();
			del.setId(rs.getLong("id"));
			del.setReceivablefee(rs.getBigDecimal("receivablefee"));
			del.setPaybackfee(rs.getBigDecimal("paybackfee"));

			return del;
		}
	}

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public void creDeliverySuccessful(final DeliverySuccessful del) {

		jdbcTemplate.update("INSERT INTO `ops_delivery_successful`(`cwb`,`createtime`,`branchid`,`deliveryid`,"
				+ "`deliverystate`,`cwbordertypeid`,`customerid`,`deliverystateid`,`paywayid`,`deliverytime`,`audittime`,`auditstate`,receivablefee,paybackfee) " + " VALUES ( ?,?,?,?,?,?,?,?,?,?"
				+ ",?,?,?,?)", new PreparedStatementSetter() {

			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				ps.setString(1, del.getCwb());
				ps.setString(2, del.getCreatetime());
				ps.setLong(3, del.getBranchid());
				ps.setLong(4, del.getDeliveryid());
				ps.setLong(5, del.getDeliverystate());
				ps.setString(6, del.getCwbordertypeid());
				ps.setLong(7, del.getCustomerid());
				ps.setLong(8, del.getDeliverystateid());
				ps.setLong(9, del.getPaywayid());
				ps.setString(10, del.getDeliverytime());
				ps.setString(11, del.getAudittime());
				ps.setLong(12, del.getAuditstate());
				ps.setBigDecimal(13, del.getReceivablefee());
				ps.setBigDecimal(14, del.getPaybackfee());
			}
		});

	}

	public void saveDeliverySuccessful(final DeliverySuccessful del) {

		jdbcTemplate.update("UPDATE `ops_delivery_successful` SET " + "`createtime`=?,`branchid`=?,`deliveryid`=?,`deliverystate`=?," + "`cwbordertypeid`=?,`customerid`=?,`deliverystateid`=?,"
				+ "`paywayid`=?,`deliverytime`=?,`audittime`=?,`auditstate`=? WHERE `cwb`=?", new PreparedStatementSetter() {

			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				// TODO Auto-generated method stub
				ps.setString(1, del.getCreatetime());
				ps.setLong(2, del.getBranchid());
				ps.setLong(3, del.getDeliveryid());
				ps.setLong(4, del.getDeliverystate());
				ps.setString(5, del.getCwbordertypeid());
				ps.setLong(6, del.getCustomerid());
				ps.setLong(7, del.getDeliverystateid());
				ps.setLong(8, del.getPaywayid());
				ps.setString(9, del.getDeliverytime());
				ps.setString(10, del.getAudittime());
				ps.setLong(11, del.getAuditstate());
				ps.setString(12, del.getCwb());

			}
		});

	}

	public List<DeliverySuccessful> getDeliverySuccessfulList(String begindate, String enddate, long isaudit, long isauditTime, String customerids, String cwbordertypeids, long paywayid,
			String dispatchbranchids, long deliverid, String operationOrderResultTypes, long page, Integer paybackfeeIsZero) {
		String sql = "SELECT * FROM ops_delivery_successful ";
		sql = setWhereSql(sql, begindate, enddate, isaudit, isauditTime, customerids, cwbordertypeids, paywayid, dispatchbranchids, deliverid, operationOrderResultTypes, paybackfeeIsZero);
		sql += "  limit " + (page - 1) * Page.ONE_PAGE_NUMBER + " ," + Page.ONE_PAGE_NUMBER;
		System.out.println(sql);
		return jdbcTemplate.query(sql, new DeliveryMapper());
	}

	public DeliverySuccessful getDeliverySuccessfulSum(String begindate, String enddate, long isaudit, long isauditTime, String customerids, String cwbordertypeids, long paywayid,
			String dispatchbranchids, long deliverid, String operationOrderResultTypes, long page, Integer paybackfeeIsZero) {
		String sql = "SELECT count(1) as id,  sum(receivablefee) as receivablefee,sum(paybackfee) as paybackfee FROM ops_delivery_successful  ";
		sql = setWhereSql(sql, begindate, enddate, isaudit, isauditTime, customerids, cwbordertypeids, paywayid, dispatchbranchids, deliverid, operationOrderResultTypes, paybackfeeIsZero);
		return jdbcTemplate.queryForObject(sql, new DeliverySumMapper());
	}

	public List<DeliverySuccessful> checkDeliverySuccessful(String cwb) {
		String sql = "SELECT * FROM ops_delivery_successful  WHERE cwb=? limit 0,1";
		return jdbcTemplate.query(sql, new DeliveryMapper(), cwb);
	}

	public long checkDeliveryByDeliverytime(String cwb, String createtime) {
		String sql = "SELECT count(1) FROM ops_delivery_successful   WHERE cwb=? and deliverytime=?  ";
		return jdbcTemplate.queryForLong(sql, cwb, createtime, createtime);
	}

	public long checkDeliveryByAudittime(String cwb, String createtime) {
		String sql = "SELECT count(1) FROM ops_delivery_successful  WHERE cwb=? and audittime=?  ";
		return jdbcTemplate.queryForLong(sql, cwb, createtime, createtime);
	}

	public void deleteDeliverySuccessful(String cwb) {
		String sql = "DELETE FROM ops_delivery_successful WHERE cwb=? ";
		jdbcTemplate.update(sql, cwb);
	}

	/**
	 * 改名
	 * 
	 * @param sql
	 * @param begindate
	 * @param enddate
	 * @param isaudit
	 * @param isauditTime
	 * @param customerids
	 * @param cwbordertypeids
	 * @param paywayid
	 * @param dispatchbranchids
	 * @param deliverid
	 * @param operationOrderResultTypes
	 * @param paybackfeeIsZero
	 * @return
	 */
	public String setWhereSql(String sql, String begindate, String enddate, long isaudit, long isauditTime, String customerids, String cwbordertypeids, long paywayid, String dispatchbranchids,
			long deliverid, String operationOrderResultTypes, Integer paybackfeeIsZero) {
		if (begindate.length() > 0 || enddate.length() > 0 || isaudit > 0 || isauditTime > 0 || customerids.length() > 0 || cwbordertypeids.length() > 0 || paywayid > 0
				|| dispatchbranchids.length() > 0 || deliverid > 0 || operationOrderResultTypes.length() > 0) {
			if (isauditTime == 0) {// 强制索引
				sql = sql.replace("ops_delivery_successful", "ops_delivery_successful FORCE INDEX(success_deliverytime_idx)");
				sql += " where deliverytime>='" + begindate + "'  and deliverytime<='" + enddate + "' ";
			} else {
				sql = sql.replace("ops_delivery_successful", "ops_delivery_successful FORCE INDEX(success_audittime_idx)");
				sql += " where audittime >='" + begindate + "'  and audittime <='" + enddate + "' ";
			}
			if (isaudit > 0) {
				sql += " and auditstate >0 ";
			} else if (isaudit == 0) {
				sql += " and auditstate=0 ";
			}
			if (customerids.length() > 0) {
				sql += " and customerid in(" + customerids + ") ";
			}
			if (cwbordertypeids.length() > 0) {
				sql += " and cwbordertypeid in(" + cwbordertypeids + ") ";
			}
			if (dispatchbranchids.length() > 0) {
				sql += " and branchid in(" + dispatchbranchids + ") ";
			}
			if (operationOrderResultTypes.length() > 0) {
				sql += " and deliverystate in(" + operationOrderResultTypes + ") ";
			}
			if (paywayid > 0) {
				sql += " and paywayid = " + paywayid;
			}
			if (deliverid > 0) {
				sql += " and deliveryid = " + deliverid;
			}
			if (paybackfeeIsZero > -1) {
				if (paybackfeeIsZero == 0) {
					sql += " and receivablefee=0 ";
				} else {
					sql += " and receivablefee>0 ";
				}
			}
		}

		return sql;

	}

	public void updateXiuGaiJinE(String cwb, BigDecimal receivablefee, BigDecimal paybackfee) {
		jdbcTemplate.update("update ops_delivery_successful set receivablefee=?,paybackfee=? where cwb=?", receivablefee, paybackfee, cwb);
	}

	public void updateXiuGaiZhiFuFangShi(String cwb, int newpaywayid) {
		jdbcTemplate.update("update ops_delivery_successful set paywayid=? where cwb=?", newpaywayid, cwb);
	}

	public void updateXiuGaiDingDanLeiXing(String cwb, int cwbordertypeid, long deliverystate) {
		jdbcTemplate.update("update ops_delivery_successful set cwbordertypeid=?,deliverystate=? where cwb=?", cwbordertypeid, deliverystate, cwb);
	}
}

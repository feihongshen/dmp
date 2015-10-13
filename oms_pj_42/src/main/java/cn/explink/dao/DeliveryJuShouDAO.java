package cn.explink.dao;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import cn.explink.domain.DeliveryJuShou;
import cn.explink.util.Page;

@Component
public class DeliveryJuShouDAO {
	@Autowired
	JdbcTemplate jdbcTemplate;
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	private final class DeliveryJuShouMapper implements RowMapper<DeliveryJuShou> {
		@Override
		public DeliveryJuShou mapRow(ResultSet rs, int rowNum) throws SQLException {
			DeliveryJuShou dj = new DeliveryJuShou();
			dj.setAudittime(rs.getString("audittime"));
			dj.setBranchid(rs.getLong("branchid"));
			dj.setCustomerid(rs.getLong("customerid"));
			dj.setCwb(rs.getString("cwb"));
			dj.setCwbordertypeid(rs.getLong("cwbordertypeid"));
			dj.setDeliveryid(rs.getLong("deliveryid"));
			dj.setDeliverystateid(rs.getLong("deliverystateid"));
			dj.setDeliverytime(rs.getString("deliverytime"));
			dj.setEmaildate(rs.getString("emaildate"));
			dj.setGcaid(rs.getLong("gcaid"));
			dj.setDeliverystate(rs.getLong("deliverystate"));
			dj.setId(rs.getInt("id"));
			dj.setPaybackfee(rs.getBigDecimal("paybackfee"));
			dj.setReceivablefee(rs.getBigDecimal("receivablefee"));
			return dj;
		}

	}

	private final class DeliveryJuShouSumMapper implements RowMapper<DeliveryJuShou> {
		@Override
		public DeliveryJuShou mapRow(ResultSet rs, int rowNum) throws SQLException {
			DeliveryJuShou dj = new DeliveryJuShou();
			dj.setId(rs.getLong("id"));
			dj.setReceivablefee(rs.getBigDecimal("receivablefee"));
			dj.setPaybackfee(rs.getBigDecimal("paybackfee"));
			return dj;
		}

	}

	// 检查是否 存在
	public List<DeliveryJuShou> checkIsExist(String cwb) {
		List<DeliveryJuShou> dList = new ArrayList<DeliveryJuShou>();
		String sql = "SELECT * FROM ops_delivery_jushou  WHERE cwb=? limit 0,1";
		dList = jdbcTemplate.query(sql, new DeliveryJuShouMapper(), cwb);
		return dList;
	}

	// 保存
	public void save(final DeliveryJuShou dj) {
		jdbcTemplate
				.update(" insert into ops_delivery_jushou ( cwb,emaildate,branchid,deliveryid,cwbordertypeid,customerid,deliverystateid,deliverytime,audittime,gcaid,receivablefee,paybackfee,deliverystate ) values(?,?,?,?,?,?,?,?,?,?,?,?,? )",
						new PreparedStatementSetter() {
							@Override
							public void setValues(PreparedStatement ps) throws SQLException {
								ps.setString(1, dj.getCwb());
								ps.setString(2, dj.getEmaildate());
								ps.setLong(3, dj.getBranchid());
								ps.setLong(4, dj.getDeliveryid());
								ps.setLong(5, dj.getCwbordertypeid());
								ps.setLong(6, dj.getCustomerid());
								ps.setLong(7, dj.getDeliverystateid());
								ps.setString(8, dj.getDeliverytime());
								ps.setString(9, dj.getAudittime());
								ps.setLong(10, dj.getGcaid());
								ps.setBigDecimal(11, dj.getReceivablefee());
								ps.setBigDecimal(12, dj.getPaybackfee());
								ps.setLong(13, dj.getDeliverystate());
							}
						});
	}

	// 更新

	public void update(final DeliveryJuShou dj) {
		jdbcTemplate.update(" update  ops_delivery_jushou set emaildate=?,branchid=?,deliveryid=?,cwbordertypeid=?,customerid=?,deliverystateid=?,"
				+ "deliverytime=?,audittime=?,gcaid=?,receivablefee=?,paybackfee=?,deliverystate=?  where cwb=? ", new PreparedStatementSetter() {

			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				ps.setString(1, dj.getEmaildate());
				ps.setLong(2, dj.getBranchid());
				ps.setLong(3, dj.getDeliveryid());
				ps.setLong(4, dj.getCwbordertypeid());
				ps.setLong(5, dj.getCustomerid());
				ps.setLong(6, dj.getDeliverystateid());
				ps.setString(7, dj.getDeliverytime());
				ps.setString(8, dj.getAudittime());
				ps.setLong(9, dj.getGcaid());
				ps.setBigDecimal(10, dj.getReceivablefee());
				ps.setBigDecimal(11, dj.getPaybackfee());
				ps.setLong(12, dj.getDeliverystate());
				ps.setString(13, dj.getCwb());

			}

		});

	}

	public List<DeliveryJuShou> getJuShouList(String begindate, String enddate, long isaudit, long isauditTime, String customeridStr, String cwbordertypeidStr, String dispatchbranchidStr,
			long deliverid, String operationOrderResultTypeStr, long page) {
		String sql = " select * from ops_delivery_jushou where ";
		sql = setSqlByWhere(sql, begindate, enddate, isaudit, isauditTime, customeridStr, cwbordertypeidStr, dispatchbranchidStr, deliverid, operationOrderResultTypeStr, page);
		logger.info("sql:{}", sql);

		return jdbcTemplate.query(sql, new DeliveryJuShouMapper());
	}

	private String setSqlByWhere(String sql, String begindate, String enddate, long isaudit, long isauditTime, String customeridStr, String cwbordertypeidStr, String dispatchbranchidStr,
			long deliverid, String operationOrderResultTypeStr, long page) {
		if (isauditTime == 0) {
			sql = sql.replace("ops_delivery_jushou", "ops_delivery_jushou FORCE INDEX(jushou_deliverytime_idx)");
			sql += " deliverytime >= '" + begindate + "' ";
			sql += " and deliverytime <= '" + enddate + "' ";
		} else {
			sql = sql.replace("ops_delivery_jushou", "ops_delivery_jushou  FORCE INDEX(jushou_audittime_idx)");
			sql += " audittime >= '" + begindate + "' ";
			sql += " and audittime <= '" + enddate + "' ";
		}
		if (dispatchbranchidStr.length() > 0 || operationOrderResultTypeStr.length() > 0 || deliverid > 0 || isaudit >= 0 || customeridStr.length() >= 0 || cwbordertypeidStr.length() >= 0) {
			StringBuffer w = new StringBuffer();
			if (dispatchbranchidStr.length() > 0) {
				w.append(" and branchid in (" + dispatchbranchidStr + ")");
			}
			if (customeridStr.length() > 0) {
				w.append(" and customerid in(" + customeridStr + ")");
			}

			if (operationOrderResultTypeStr.length() > 0) {
				w.append(" and deliverystate in (" + operationOrderResultTypeStr + ")");
			}
			if (deliverid > 0) {
				w.append(" and deliveryid=" + deliverid);
			}
			if (cwbordertypeidStr.length() > 0) {
				w.append(" and cwbordertypeid in(" + cwbordertypeidStr + ") ");
			}
			if (isaudit >= 0) {
				if (isaudit == 0) {
					w.append(" and gcaid<1");
				} else {
					w.append(" and gcaid>0");
				}
			}
			if (page > 0) {
				w.append(" limit " + (page - 1) * Page.ONE_PAGE_NUMBER + "," + Page.ONE_PAGE_NUMBER);
			}

			sql += w.toString();
		}
		return sql;
	}

	public DeliveryJuShou getDeliveryJuShouSum(String begindate, String enddate, long isaudit, long isauditTime, String customeridStr, String cwbordertypeidStr, String dispatchbranchidStr,
			long deliverid, String operationOrderResultTypeStr) {
		String sql = "SELECT count(1) as id,SUM(receivablefee) AS receivablefee ,SUM(paybackfee) AS paybackfee FROM ops_delivery_jushou where ";
		sql = setSqlByWhere(sql, begindate, enddate, isaudit, isauditTime, customeridStr, cwbordertypeidStr, dispatchbranchidStr, deliverid, operationOrderResultTypeStr, 0);
		return jdbcTemplate.queryForObject(sql, new DeliveryJuShouSumMapper());
	}

	public void delJuShouByCwb(String cwb) {
		String sql = "DELETE FROM ops_delivery_jushou WHERE cwb=? ";
		jdbcTemplate.update(sql, cwb);
	}

	public void updateXiuGaiJinE(String cwb, BigDecimal receivablefee, BigDecimal paybackfee) {
		jdbcTemplate.update("update ops_delivery_jushou set receivablefee=?,paybackfee=? where cwb=?", receivablefee, paybackfee, cwb);
	}

	public void updateXiuGaiDingDanLeiXing(String cwb, int cwbordertypeid, long deliverystate) {
		jdbcTemplate.update("update ops_delivery_jushou set cwbordertypeid=?,deliverystate=? where cwb=?", cwbordertypeid, deliverystate, cwb);
	}

}

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

import cn.explink.domain.DeliveryZhiLiu;
import cn.explink.util.Page;
import cn.explink.util.StringUtil;

@Component
public class DeliveryZhiLiuDAO {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	@Autowired
	JdbcTemplate jdbcTemplate;

	private final class DeliveryZhiLiuMapper implements RowMapper<DeliveryZhiLiu> {
		@Override
		public DeliveryZhiLiu mapRow(ResultSet rs, int rowNum) throws SQLException {
			DeliveryZhiLiu dzl = new DeliveryZhiLiu();
			dzl.setId(rs.getLong("id"));
			dzl.setCwb(rs.getString("cwb"));
			dzl.setAudittime(rs.getString("audittime"));
			dzl.setBranchid(rs.getLong("branchid"));
			dzl.setCustomerid(rs.getLong("customerid"));
			dzl.setCwbordertypeid(rs.getLong("cwbordertypeid"));
			dzl.setDeliveryid(rs.getLong("deliveryid"));
			dzl.setDeliverystateid(rs.getLong("deliverystateid"));
			dzl.setDeliverytime(rs.getString("deliverytime"));
			dzl.setGcaid(rs.getLong("gcaid"));
			dzl.setResendtime(rs.getString("resendtime"));
			dzl.setEmaildate(StringUtil.nullConvertToEmptyString(rs.getString("emaildate")));
			dzl.setReceivablefee(rs.getBigDecimal("receivablefee"));
			dzl.setPaybackfee(rs.getBigDecimal("paybackfee"));
			return dzl;
		}
	}

	private final class DeliveryZhiLiuSumMapper implements RowMapper<DeliveryZhiLiu> {
		@Override
		public DeliveryZhiLiu mapRow(ResultSet rs, int rowNum) throws SQLException {
			DeliveryZhiLiu dzl = new DeliveryZhiLiu();
			dzl.setId(rs.getLong("id"));
			dzl.setReceivablefee(rs.getBigDecimal("receivablefee"));
			dzl.setPaybackfee(rs.getBigDecimal("paybackfee"));
			return dzl;
		}
	}

	public List<DeliveryZhiLiu> getZhiliuList(String begindate, String enddate, long isaudit, long isauditTime, String customeridStr, String cwbordertypeidStr, String dispatchbranchidStr,
			long deliverid, long page) {
		String sql = "select * from ops_delivery_zhiliu where ";
		sql = setSqlByWhere(sql, begindate, enddate, isaudit, isauditTime, customeridStr, cwbordertypeidStr, dispatchbranchidStr, deliverid, page);
		logger.info("sql:{}", sql);
		return jdbcTemplate.query(sql, new DeliveryZhiLiuMapper());
	}

	private String setSqlByWhere(String sql, String begindate, String enddate, long isaudit, long isauditTime, String customeridStr, String cwbordertypeidStr, String dispatchbranchidStr,
			long deliverid, long page) {
		if (isauditTime == 0) {
			sql = sql.replace("ops_delivery_zhiliu", "ops_delivery_zhiliu FORCE INDEX(zhiliu_deliverytime_idx )");
			sql += " deliverytime >= '" + begindate + "' ";
			sql += " and deliverytime <= '" + enddate + "' ";
		} else {
			sql = sql.replace("ops_delivery_zhiliu", "ops_delivery_zhiliu  FORCE INDEX(zhiliu_audittime_idx)");
			sql += " audittime >= '" + begindate + "' ";
			sql += " and audittime <= '" + enddate + "' ";
		}
		if (dispatchbranchidStr.length() > 0 || deliverid > 0 || isaudit >= 0 || customeridStr.length() >= 0 || cwbordertypeidStr.length() >= 0) {
			StringBuffer w = new StringBuffer();
			if (dispatchbranchidStr.length() > 0) {
				w.append(" and branchid in (" + dispatchbranchidStr + ")");
			}
			if (customeridStr.length() > 0) {
				w.append(" and customerid in(" + customeridStr + ")");
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

	public DeliveryZhiLiu getDeliveryZhiLiuSum(String begindate, String enddate, long isaudit, long isauditTime, String customeridStr, String cwbordertypeidStr, String dispatchbranchidStr,
			long deliverid) {
		String sql = "select count(1) as id,sum(receivablefee) as receivablefee,sum(paybackfee) as paybackfee from  ops_delivery_zhiliu where";
		sql = setSqlByWhere(sql, begindate, enddate, isaudit, isauditTime, customeridStr, cwbordertypeidStr, dispatchbranchidStr, deliverid, 0);
		logger.info("sql:{}", sql);
		return jdbcTemplate.queryForObject(sql, new DeliveryZhiLiuSumMapper());
	}

	/**
	 * 检查 是否已经存在 根据 cwb
	 * 
	 * @param cwb
	 * @return
	 */
	public List<DeliveryZhiLiu> checkIsExist(String cwb) {
		List<DeliveryZhiLiu> dList = new ArrayList<DeliveryZhiLiu>();
		String sql = "SELECT * FROM ops_delivery_zhiliu  WHERE cwb=? limit 0,1";
		dList = jdbcTemplate.query(sql, new DeliveryZhiLiuMapper(), cwb);
		return dList;
	}

	/**
	 * 更新滞留信息
	 * 
	 * @param dzl
	 */

	public void updateZhiLiu(final DeliveryZhiLiu dzl) {
		jdbcTemplate.update("UPDATE `ops_delivery_zhiliu` SET " + "`emaildate`=?,`audittime`=?,`branchid`=?,`customerid`=?," + "`cwbordertypeid`=?,`deliveryid`=?,`deliverystateid`=?,"
				+ "`deliverytime`=?,`gcaid`=?,`resendtime`=?,`receivablefee`=?,`paybackfee`=? WHERE `cwb`=?", new PreparedStatementSetter() {

			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				ps.setString(1, dzl.getEmaildate());
				ps.setString(2, dzl.getAudittime());
				ps.setLong(3, dzl.getBranchid());
				ps.setLong(4, dzl.getCustomerid());
				ps.setLong(5, dzl.getCwbordertypeid());
				ps.setLong(6, dzl.getDeliveryid());
				ps.setLong(7, dzl.getDeliverystateid());
				ps.setString(8, dzl.getDeliverytime());
				ps.setLong(9, dzl.getGcaid());
				ps.setString(10, dzl.getResendtime());
				ps.setBigDecimal(11, dzl.getReceivablefee());
				ps.setBigDecimal(12, dzl.getPaybackfee());
				ps.setString(13, dzl.getCwb());
			}
		});

	}

	public void creDeliveryZhiLiu(final DeliveryZhiLiu dzl) {
		jdbcTemplate.update(" insert into  ops_delivery_zhiliu (emaildate,audittime,branchid,customerid,"
				+ "cwbordertypeid,deliveryid,deliverystateid,deliverytime,gcaid,resendtime,receivablefee,paybackfee,cwb ) values(?,?,?,?,?,?,?,?,?,?,?,?,?)", new PreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				ps.setString(1, dzl.getEmaildate());
				ps.setString(2, dzl.getAudittime());
				ps.setLong(3, dzl.getBranchid());
				ps.setLong(4, dzl.getCustomerid());
				ps.setLong(5, dzl.getCwbordertypeid());
				ps.setLong(6, dzl.getDeliveryid());
				ps.setLong(7, dzl.getDeliverystateid());
				ps.setString(8, dzl.getDeliverytime());
				ps.setLong(9, dzl.getGcaid());
				ps.setString(10, dzl.getResendtime());
				ps.setBigDecimal(11, dzl.getReceivablefee());
				ps.setBigDecimal(12, dzl.getPaybackfee());
				ps.setString(13, dzl.getCwb());
			}
		});
	}

	public void deleteDeliveryZhiLiu(String cwb) {
		String sql = "DELETE FROM ops_delivery_zhiliu WHERE cwb=? ";
		jdbcTemplate.update(sql, cwb);
	}

	public void updateXiuGaiJinE(String cwb, BigDecimal receivablefee, BigDecimal paybackfee) {
		jdbcTemplate.update("update ops_delivery_zhiliu set receivablefee=?,paybackfee=? where cwb=?", receivablefee, paybackfee, cwb);
	}

	public void updateXiuGaiDingDanLeiXing(String cwb, int cwbordertypeid) {
		jdbcTemplate.update("update ops_delivery_zhiliu set cwbordertypeid=? where cwb=?", cwbordertypeid, cwb);
	}
}

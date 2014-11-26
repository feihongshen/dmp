package cn.explink.dao;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import cn.explink.pos.tools.PosTradeDetail;
import cn.explink.service.CwbOrderService;
import cn.explink.util.Page;

/**
 * POS刷卡支付
 * 
 * @author Administrator
 *
 */
@Component
public class PosPayMoneyDAO {

	@Autowired
	JdbcTemplate jdbcTemplate;
	@Autowired
	UserDAO userDAO;
	@Autowired
	CwbOrderService cwbOrderService;

	private final class PosTradeDetailRowMapper implements RowMapper<PosTradeDetail> {
		@Override
		public PosTradeDetail mapRow(ResultSet rs, int rowNum) throws SQLException {
			PosTradeDetail posTradeDetail = new PosTradeDetail();
			posTradeDetail.setPayid(rs.getLong("payid"));
			posTradeDetail.setPos_code(rs.getString("pos_code"));
			posTradeDetail.setCwb(rs.getString("cwb"));
			posTradeDetail.setTradeTime(rs.getString("tradeTime"));
			posTradeDetail.setTradeDeliverId(rs.getLong("tradeDeliverId"));
			posTradeDetail.setTradeTypeId(rs.getInt("tradeTypeId"));
			posTradeDetail.setPayTypeId(rs.getInt("payTypeId"));
			posTradeDetail.setPayAmount(rs.getBigDecimal("payAmount") == null ? BigDecimal.ZERO : rs.getBigDecimal("payAmount"));
			posTradeDetail.setPayDetail(rs.getString("payDetail"));
			posTradeDetail.setPayRemark(rs.getString("payRemark"));
			posTradeDetail.setSignName(rs.getString("signName"));
			posTradeDetail.setSignTime(rs.getString("signTime"));
			posTradeDetail.setSignRemark(rs.getString("signRemark"));
			posTradeDetail.setSigntypeid(rs.getInt("signtypeid"));
			posTradeDetail.setIsSuccessFlag(rs.getInt("isSuccessFlag"));
			posTradeDetail.setAcq_type(rs.getString("acq_type"));
			posTradeDetail.setCustomerid(rs.getLong("customerid"));
			posTradeDetail.setEmaildate(rs.getString("emaildate"));
			posTradeDetail.setTerminal_no(rs.getString("terminal_no"));
			return posTradeDetail;
		}
	}

	public String getPosPayByPageWhereSql(String sql, String cwb, String pos_code, String starttime, String endtime, String deliveryids, long isSuccessFlag) {
		sql += " where 1=1 ";
		StringBuffer str = new StringBuffer();
		if (cwb.length() > 0) {
			str.append(" and cwb = '" + cwb + "'");
		} else {
			if (pos_code.length() > 0) {
				str.append(" and pos_code = '" + pos_code + "'");
			}
			if (starttime.length() > 0) {
				str.append(" and tradeTime >= '" + starttime + "'");
			}
			if (endtime.length() > 0) {
				str.append(" and tradeTime <= '" + endtime + "'");
			}
			if (deliveryids.length() > 2) {
				str.append(" and tradeDeliverId in (" + deliveryids + ")");
			}
			if (isSuccessFlag > 0) {
				str.append(" and isSuccessFlag = '" + isSuccessFlag + "'");
			}
		}

		return sql + str.toString();
	}

	public long getPosPayCount(String cwb, String pos_code, String starttime, String endtime, String deliveryids, long isSuccessFlag) {
		String sql = "select count(1) from express_ops_pos_paydetail";
		sql = this.getPosPayByPageWhereSql(sql, cwb, pos_code, starttime, endtime, deliveryids, isSuccessFlag);
		return jdbcTemplate.queryForInt(sql);
	}

	public List<PosTradeDetail> getPosPayByPage(long page, String cwb, String pos_code, String starttime, String endtime, String deliveryids, long isSuccessFlag) {
		String sql = "select * from express_ops_pos_paydetail ";
		sql = this.getPosPayByPageWhereSql(sql, cwb, pos_code, starttime, endtime, deliveryids, isSuccessFlag);
		sql += " limit " + (page - 1) * Page.ONE_PAGE_NUMBER + " ," + Page.ONE_PAGE_NUMBER;
		return jdbcTemplate.query(sql, new PosTradeDetailRowMapper());
	}

	public List<PosTradeDetail> getPosPayExpo(String cwb, String pos_code, String starttime, String endtime, String deliveryids, long isSuccessFlag) {
		String sql = "select * from express_ops_pos_paydetail ";
		sql = this.getPosPayByPageWhereSql(sql, cwb, pos_code, starttime, endtime, deliveryids, isSuccessFlag);
		return jdbcTemplate.query(sql, new PosTradeDetailRowMapper());
	}

	public String getPosPayExpoSql(String cwb, String pos_code, String starttime, String endtime, String deliveryids, long isSuccessFlag) {
		String sql = "select op.*,de.emaildate as deEmaildate,de.`paywayid`,de.`newpaywayid` from express_ops_pos_paydetail as op left join express_ops_cwb_detail AS de ON op.cwb=de.cwb ";

		sql += " where de.state=1 ";
		StringBuffer str = new StringBuffer();
		if (cwb.length() > 0) {
			str.append(" and op.cwb = '" + cwb + "'");
		} else {
			if (pos_code.length() > 0) {
				str.append(" and op.pos_code = '" + pos_code + "'");
			}
			if (starttime.length() > 0) {
				str.append(" and op.tradeTime >= '" + starttime + "'");
			}
			if (endtime.length() > 0) {
				str.append(" and op.tradeTime <= '" + endtime + "'");
			}
			if (deliveryids.length() > 2) {
				str.append(" and op.tradeDeliverId in (" + deliveryids + ")");
			}
			if (isSuccessFlag > 0) {
				str.append(" and op.isSuccessFlag = '" + isSuccessFlag + "'");
			}
		}
		sql = sql + str.toString();
		return sql;
	}

}

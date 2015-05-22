package cn.explink.dao;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import cn.explink.domain.ApplyEditDeliverystate;
import cn.explink.util.Page;

@Component
public class ApplyEditDeliverystateDAO {

	private final class ApplyEditDeliverystateRowMapper implements RowMapper<ApplyEditDeliverystate> {
		@Override
		public ApplyEditDeliverystate mapRow(ResultSet rs, int rowNum) throws SQLException {
			ApplyEditDeliverystate applyEditDeliverystate = new ApplyEditDeliverystate();

			applyEditDeliverystate.setApplybranchid(rs.getLong("applybranchid"));
			applyEditDeliverystate.setApplytime(rs.getString("applytime"));
			applyEditDeliverystate.setApplyuserid(rs.getLong("applyuserid"));
			applyEditDeliverystate.setCwb(rs.getString("cwb"));
			applyEditDeliverystate.setCwbordertypeid(rs.getInt("cwbordertypeid"));
			applyEditDeliverystate.setDeliverid(rs.getLong("deliverid"));
			applyEditDeliverystate.setDeliverpodtime(rs.getString("deliverpodtime"));
			applyEditDeliverystate.setDeliverystateid(rs.getLong("deliverystateid"));
			applyEditDeliverystate.setEditdetail(rs.getString("editdetail"));
			applyEditDeliverystate.setEditnopos(rs.getBigDecimal("editnopos"));
			applyEditDeliverystate.setEditnowdeliverystate(rs.getLong("editnowdeliverystate"));
			applyEditDeliverystate.setEditpos(rs.getBigDecimal("editpos"));
			applyEditDeliverystate.setEditreason(rs.getString("editreason"));
			applyEditDeliverystate.setEdittime(rs.getString("edittime"));
			applyEditDeliverystate.setEdituserid(rs.getLong("edituserid"));
			applyEditDeliverystate.setId(rs.getLong("id"));
			applyEditDeliverystate.setIsauditpayup(rs.getLong("isauditpayup"));
			applyEditDeliverystate.setIshandle(rs.getLong("ishandle"));
			applyEditDeliverystate.setIssendcustomer(rs.getLong("issendcustomer"));
			applyEditDeliverystate.setNopos(rs.getBigDecimal("nopos"));
			applyEditDeliverystate.setNowdeliverystate(rs.getInt("nowdeliverystate"));
			applyEditDeliverystate.setOpscwbid(rs.getLong("opscwbid"));
			applyEditDeliverystate.setPayupid(rs.getLong("payupid"));
			applyEditDeliverystate.setPos(rs.getBigDecimal("pos"));
			applyEditDeliverystate.setState(rs.getLong("state"));
			applyEditDeliverystate.setAudit(rs.getLong("audit"));

			return applyEditDeliverystate;
		}
	}

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public long creApplyEditDeliverystate(final ApplyEditDeliverystate aeds) {
		KeyHolder key = new GeneratedKeyHolder();
		this.jdbcTemplate.update(new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(java.sql.Connection con) throws SQLException {
				PreparedStatement ps = null;
				ps = con.prepareStatement("insert into express_ops_applyeditdeliverystate (deliverystateid,opscwbid,cwb,"
						+ "cwbordertypeid,nowdeliverystate,nopos,pos,deliverid,applyuserid,applybranchid,applytime) " + "values(?,?,?,?,?,?,?,?,?,?,?)", new String[] { "id" });
				ps.setLong(1, aeds.getDeliverystateid());
				ps.setLong(2, aeds.getOpscwbid());
				ps.setString(3, aeds.getCwb());
				ps.setLong(4, aeds.getCwbordertypeid());
				ps.setLong(5, aeds.getNowdeliverystate());
				ps.setBigDecimal(6, aeds.getNopos());
				ps.setBigDecimal(7, aeds.getPos());
				ps.setLong(8, aeds.getDeliverid());
				ps.setLong(9, aeds.getApplyuserid());
				ps.setLong(10, aeds.getApplybranchid());
				ps.setString(11, aeds.getApplytime());
				return ps;
			}
		}, key);
		aeds.setId(key.getKey().longValue());
		return key.getKey().longValue();
	}

	public void saveApplyEditDeliverystateById(long id, long editnowdeliverystate, String editreason) {
		String sql = "update express_ops_applyeditdeliverystate set state=1, editnowdeliverystate=?,editreason=? where id=?";
		this.jdbcTemplate.update(sql, editnowdeliverystate, editreason, id);
	}

	public ApplyEditDeliverystate getApplyEditDeliverystateById(long id) {
		String sql = "SELECT * from express_ops_applyeditdeliverystate where id=?";
		return this.jdbcTemplate.queryForObject(sql, new ApplyEditDeliverystateRowMapper(), id);
	}

	public List<ApplyEditDeliverystate> getApplyEditDeliverystateByCwbsPage(long page, String cwbs, long ishandle) {
		String sql = "SELECT * from express_ops_applyeditdeliverystate where cwb in(" + cwbs + ") and ishandle=? ";
		sql += " limit " + ((page - 1) * Page.ONE_PAGE_NUMBER) + " ," + Page.ONE_PAGE_NUMBER;
		return this.jdbcTemplate.query(sql, new ApplyEditDeliverystateRowMapper(), ishandle);
	}

	public List<ApplyEditDeliverystate> getApplyEditDeliverystateByCwb(String cwb, long ishandle) {
		String sql = "SELECT * from express_ops_applyeditdeliverystate where cwb =? and ishandle=? ";
		return this.jdbcTemplate.query(sql, new ApplyEditDeliverystateRowMapper(), cwb, ishandle);
	}

	public List<ApplyEditDeliverystate> getApplyEditDeliverystateByWherePage(long page, String begindate, String enddate, long applybranchid, long ishandle, String cwb, boolean isFinancial, long audit) {
		String sql = "SELECT * from express_ops_applyeditdeliverystate where state=1 ";
		if (cwb.length() > 0) {
			StringBuffer w = new StringBuffer();
			w.append(" and cwb = '" + cwb + "'");
			sql += w.toString();
		} else if ((begindate.length() > 0) || (enddate.length() > 0) || (applybranchid > 0) || (ishandle > -1) || (audit > -1)) {

			StringBuffer w = new StringBuffer();

			if (begindate.length() > 0) {
				w.append(" and applytime >= '" + begindate + "' ");
			}
			if (enddate.length() > 0) {
				w.append(" and applytime <= '" + enddate + "' ");
			}
			if (applybranchid > 0) {
				w.append(" and applybranchid = " + applybranchid);
			}
			if (ishandle > -1) {
				w.append(" and ishandle = " + ishandle);
			}
			if (audit > -1) {
				w.append(" and audit = " + audit);
			}
			sql += w.toString();
		}
		if (isFinancial) {
			sql += " and audit =1 ";
		}

		sql += " limit " + ((page - 1) * Page.ONE_PAGE_NUMBER) + " ," + Page.ONE_PAGE_NUMBER;
		return this.jdbcTemplate.query(sql, new ApplyEditDeliverystateRowMapper());
	}

	public long getApplyEditDeliverystateByWhereCount(String begindate, String enddate, long applybranchid, long ishandle, String cwb, boolean isFinancial, long audit) {
		String sql = "SELECT count(1) from express_ops_applyeditdeliverystate where state=1 ";
		if (cwb.length() > 0) {
			StringBuffer w = new StringBuffer();
			w.append(" and cwb = '" + cwb + "'");
			sql += w.toString();
		} else if ((begindate.length() > 0) || (enddate.length() > 0) || (applybranchid > 0) || (ishandle > -1) || (audit > -1)) {
			StringBuffer w = new StringBuffer();

			if (begindate.length() > 0) {
				w.append(" and applytime >= '" + begindate + "' ");
			}
			if (enddate.length() > 0) {
				w.append(" and applytime <= '" + enddate + "' ");
			}
			if (applybranchid > 0) {
				w.append(" and applybranchid = " + applybranchid);
			}
			if (ishandle > -1) {
				w.append(" and ishandle = " + ishandle);
			}
			if (audit > -1) {
				w.append(" and audit = " + audit);
			}

			sql += w.toString();

		}
		if (isFinancial) {
			sql += " and audit =1 ";
		}
		return this.jdbcTemplate.queryForLong(sql);
	}

	public void agreeSaveApplyEditDeliverystateById(long id, BigDecimal editnopos, BigDecimal editpos, long edituserid, String edittime, String editdetail) {
		String sql = "update express_ops_applyeditdeliverystate set editnopos=?,editpos=?,edituserid=?,edittime=?,editdetail=?,ishandle=1 where id=?";
		this.jdbcTemplate.update(sql, editnopos, editpos, edituserid, edittime, editdetail, id);
	}

	public void updateState(long issendcustomer, String ids) {
		String sql = "update express_ops_applyeditdeliverystate set issendcustomer=? where deliverystateid in(" + ids + ")";
		this.jdbcTemplate.update(sql, issendcustomer);
	}

	public int updateAudit(long id, long flag, long userid) {
		String sql = "update express_ops_applyeditdeliverystate set audit=" + flag + ",edituserid=" + userid + " where id =" + id;
		return this.jdbcTemplate.update(sql);
	}
}

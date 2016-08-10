package cn.explink.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import cn.explink.domain.OrgRechargesAdjustmentRecord;
import cn.explink.util.DateTimeUtil;

@Component
public class FnOrgRechargesAdjustRecordDAO {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	private final class OrgRechargesAdjustRecordRowMapper implements
			RowMapper<OrgRechargesAdjustmentRecord> {
		@Override
		public OrgRechargesAdjustmentRecord mapRow(ResultSet rs, int rowNum)
				throws SQLException {
			OrgRechargesAdjustmentRecord rechargesAdjustRecord = new OrgRechargesAdjustmentRecord();
			rechargesAdjustRecord.setId(rs.getLong("id"));
			rechargesAdjustRecord.setCwb(rs.getString("cwb"));
			rechargesAdjustRecord.setDeliverybranchid(rs
					.getLong("deliverybranchid"));
			rechargesAdjustRecord.setAdjustAmount(rs
					.getBigDecimal("adjust_amount"));
			rechargesAdjustRecord.setCreatedUser(rs.getString("created_user"));
			rechargesAdjustRecord.setCreatedTime(rs.getDate("created_time"));
			rechargesAdjustRecord.setPayMethod(rs.getInt("pay_method"));
			rechargesAdjustRecord.setDeliveryid(rs.getLong("deliveryid"));
			rechargesAdjustRecord.setSignTime(rs.getDate("sign_time"));
	
			return rechargesAdjustRecord;
		}
	}

	public void creRechargesAdjustmentRecord(
			final OrgRechargesAdjustmentRecord rechargesAdjustmentRecord) {
		String sql = "INSERT INTO fn_org_recharges_adjustment_record"
				+ " (cwb,pay_method,deliverybranchid,deliveryid,adjust_amount,sign_time,created_time,created_user)"
				+ " VALUES (?,?,?,?,?,?,?,?)";
		this.jdbcTemplate.update(sql,
				new PreparedStatementSetter() {
					@Override
					public void setValues(PreparedStatement ps)
							throws SQLException {
						ps.setString(1, rechargesAdjustmentRecord.getCwb());
						ps.setInt(2, rechargesAdjustmentRecord.getPayMethod());
						ps.setLong(3, rechargesAdjustmentRecord.getDeliverybranchid());
						ps.setLong(4, rechargesAdjustmentRecord.getDeliveryid());
						ps.setBigDecimal(5, rechargesAdjustmentRecord.getAdjustAmount());
						ps.setString(6, DateTimeUtil.formatDate(rechargesAdjustmentRecord.getSignTime()));
						ps.setString(7, DateTimeUtil.formatDate(rechargesAdjustmentRecord.getCreatedTime()));
						ps.setString(8, rechargesAdjustmentRecord.getCreatedUser());

					}
				});
	}

	/**
	 * 通过订单获取到对应的回款调整记录
	 * 
	 * @param cwbid
	 * @return
	 */
	public List<OrgRechargesAdjustmentRecord> getAdjustmentRecordByCwb(String cwb) {
		String sql = "select * from fn_org_recharges_adjustment_record where cwb=?  ";
		return this.jdbcTemplate.query(sql,
				new OrgRechargesAdjustRecordRowMapper(), cwb);
	}
}

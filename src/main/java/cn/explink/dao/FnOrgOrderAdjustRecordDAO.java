package cn.explink.dao;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import cn.explink.domain.OrgOrderAdjustmentRecord;
import cn.explink.enumutil.FnCwbStatusEnum;

@Component
public class FnOrgOrderAdjustRecordDAO {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	private final class OrgOrderAdjustRecordRowMapper implements
			RowMapper<OrgOrderAdjustmentRecord> {
		@Override
		public OrgOrderAdjustmentRecord mapRow(ResultSet rs, int rowNum)
				throws SQLException {
			OrgOrderAdjustmentRecord orderAdjustRecord = new OrgOrderAdjustmentRecord();
			orderAdjustRecord.setId(rs.getLong("id"));
			orderAdjustRecord.setOrderNo(rs.getString("order_no"));
			orderAdjustRecord.setBillId(rs.getLong("bill_id"));
			orderAdjustRecord.setBillNo(rs.getString("bill_no"));
			orderAdjustRecord.setDeliverybranchid(rs
					.getLong("deliverybranchid"));
			orderAdjustRecord.setAdjustBillNo(rs
					.getString("adjust_bill_no"));
			orderAdjustRecord.setCustomerId(rs.getLong("customer_id"));
			orderAdjustRecord
					.setReceiveFee(rs.getBigDecimal("receive_fee"));
			orderAdjustRecord.setRefundFee(rs.getBigDecimal("refund_fee"));
			orderAdjustRecord.setModifyFee(rs.getBigDecimal("modify_fee"));
			orderAdjustRecord.setGoodsAmount(rs
					.getBigDecimal("goods_amount"));
			orderAdjustRecord.setAdjustAmount(rs
					.getBigDecimal("adjust_amount"));
			orderAdjustRecord.setVerifyAmount(rs
					.getBigDecimal("verify_amount"));
			orderAdjustRecord.setRemark(rs.getString("remark"));
			orderAdjustRecord.setCreator(rs.getString("creator"));
			orderAdjustRecord.setCreateTime(rs.getDate("create_time"));
			orderAdjustRecord.setOrderType(rs.getInt("order_type"));
			orderAdjustRecord.setPayMethod(rs.getInt("pay_method"));
			orderAdjustRecord.setDeliverId(rs.getLong("deliver_id"));
			orderAdjustRecord.setSignTime(rs.getDate("sign_time"));
			orderAdjustRecord.setStatus(rs.getInt("status"));
			orderAdjustRecord.setPayWayChangeFlag(rs
					.getInt("pay_way_change_flag"));
			return orderAdjustRecord;
		}
	}

	public void creOrderAdjustmentRecord(
			final OrgOrderAdjustmentRecord orderAdjustmentRecord) {
		this.jdbcTemplate.update("insert into fn_org_order_adjustment_record "
				+ "(" + "order_no," + "bill_id," + "bill_no,"
				+ "adjust_bill_no," + "customer_id," + "receive_fee,"
				+ "refund_fee," + "modify_fee," + "adjust_amount," + "remark,"
				+ "creator," + "create_time," + "order_type," + "pay_method,"
				+ "deliver_id," + "sign_time," + "deliverybranchid,"
				+ "goods_amount," + "pay_way_change_flag," + "adjust_type,"
				+ "freight_amount, status" + ") "
				+ "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)",
				new PreparedStatementSetter() {
					@Override
					public void setValues(PreparedStatement ps)
							throws SQLException {
						ps.setString(1, orderAdjustmentRecord.getOrderNo());
						ps.setLong(2, orderAdjustmentRecord.getBillId());
						ps.setString(3, orderAdjustmentRecord.getBillNo());
						ps.setString(4, orderAdjustmentRecord.getAdjustBillNo());
						ps.setLong(5, orderAdjustmentRecord.getCustomerId());
						ps.setBigDecimal(6,
								orderAdjustmentRecord.getReceiveFee());
						ps.setBigDecimal(7,
								orderAdjustmentRecord.getRefundFee());
						ps.setBigDecimal(8,
								orderAdjustmentRecord.getModifyFee());
						ps.setBigDecimal(9,
								orderAdjustmentRecord.getAdjustAmount());
						ps.setString(10, orderAdjustmentRecord.getRemark());
						ps.setString(11, orderAdjustmentRecord.getCreator());
						ps.setDate(12, new Date(orderAdjustmentRecord
								.getCreateTime().getTime()));
						ps.setInt(13, orderAdjustmentRecord.getOrderType());
						ps.setInt(14, orderAdjustmentRecord.getPayMethod());
						ps.setLong(15, orderAdjustmentRecord.getDeliverId());
						ps.setDate(
								16,
								orderAdjustmentRecord.getSignTime() == null ? null
										: new Date(orderAdjustmentRecord
												.getSignTime().getTime()));
						ps.setLong(17,
								orderAdjustmentRecord.getDeliverybranchid());
						ps.setBigDecimal(18,
								orderAdjustmentRecord.getGoodsAmount());
						ps.setInt(19,
								orderAdjustmentRecord.getPayWayChangeFlag());
						ps.setInt(20, orderAdjustmentRecord.getAdjustType());
						ps.setBigDecimal(21,
								orderAdjustmentRecord.getFreightAmount());
						ps.setInt(22, orderAdjustmentRecord.getStatus() == null ? FnCwbStatusEnum.Unreceive.getIndex() : orderAdjustmentRecord.getStatus().intValue());
					}
				});
	}

	/**
	 * 通过订单获取到对应订单的调整单
	 * 
	 * @param cwbid
	 * @return
	 */
	public List<OrgOrderAdjustmentRecord> getAdjustmentRecordByCwb(String cwb) {
		String sql = "select * from fn_org_order_adjustment_record where `order_no`=?  ";
		return this.jdbcTemplate.query(sql,
				new OrgOrderAdjustRecordRowMapper(), cwb);
	}
}

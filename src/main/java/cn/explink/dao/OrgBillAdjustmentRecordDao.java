package cn.explink.dao;

import java.math.BigDecimal;
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

import cn.explink.domain.OrgBillAdjustmentRecord;

@Component
public class OrgBillAdjustmentRecordDao {
	
	private final class OrgBillAdjustmentRecordRowMapper implements RowMapper<OrgBillAdjustmentRecord> {
		@Override
		public OrgBillAdjustmentRecord mapRow(ResultSet rs, int rowNum) throws SQLException {
			OrgBillAdjustmentRecord adjustmentRecord = new OrgBillAdjustmentRecord();

			adjustmentRecord.setId(rs.getLong("id"));
			adjustmentRecord.setOrderNo(rs.getString("order_no"));
			adjustmentRecord.setBillNo(rs.getString("bill_no"));
			adjustmentRecord.setBillId(rs.getLong("bill_id"));
			adjustmentRecord.setAdjustBillNo(rs.getString("adjust_bill_no"));
			adjustmentRecord.setCustomerId(rs.getLong("customer_id"));
			adjustmentRecord.setReceiveFee(rs.getBigDecimal("receive_fee"));
			adjustmentRecord.setRefundFee(rs.getBigDecimal("refund_fee"));
			adjustmentRecord.setModifyFee(rs.getBigDecimal("modify_fee"));
			adjustmentRecord.setAdjustAmount(rs.getBigDecimal("adjust_amount"));
			adjustmentRecord.setRemark(rs.getString("remark"));
			adjustmentRecord.setCreator(rs.getString("creator"));
			adjustmentRecord.setCreateTime(rs.getDate("create_time"));
			adjustmentRecord.setStatus(rs.getInt("status"));
			adjustmentRecord.setCheckUser(rs.getString("check_user"));
			adjustmentRecord.setCheckTime(rs.getDate("check_time"));
			adjustmentRecord.setOrderType(rs.getInt("order_type"));
			adjustmentRecord.setPayMethod(rs.getInt("pay_method"));
			return adjustmentRecord;
		}
	}

	@Autowired
	private JdbcTemplate jdbcTemplate;
	public void creAdjustmentRecord(final OrgBillAdjustmentRecord adjustmentRecord) {
		this.jdbcTemplate.update("insert into fn_org_bill_adjustment_record "
				+ "("
				+ "order_no,"
				+ "bill_id,"
				+ "bill_no,"
				+ "adjust_bill_no,"
				+ "customer_id," 
				+ "receive_fee,"
				+ "refund_fee,"
				+ "modify_fee,"
				+ "adjust_amount,"
				+ "remark,"
				+ "creator,"
				+ "create_time,"
				+ "check_user,"
				+ "check_time,"
				+ "order_type,"
				+ "pay_method,"
				+ "status"
				+ ") " 
				+ "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)",
				new PreparedStatementSetter() {
					@Override
					public void setValues(PreparedStatement ps) throws SQLException {
						ps.setString(1, adjustmentRecord.getOrderNo());
						ps.setLong(2, adjustmentRecord.getBillId());
						ps.setString(3, adjustmentRecord.getBillNo());
						ps.setString(4, adjustmentRecord.getAdjustBillNo());
						ps.setLong(5, adjustmentRecord.getCustomerId());
						ps.setBigDecimal(6, adjustmentRecord.getReceiveFee());
						ps.setBigDecimal(7, adjustmentRecord.getRefundFee());
						ps.setBigDecimal(8, adjustmentRecord.getModifyFee());
						ps.setBigDecimal(9, adjustmentRecord.getAdjustAmount());
						ps.setString(10, adjustmentRecord.getRemark());
						ps.setString(11, adjustmentRecord.getCreator());
						ps.setDate(12, new Date(adjustmentRecord.getCreateTime().getTime()));
						ps.setString(13, adjustmentRecord.getCheckUser());
						ps.setDate(14, new Date(adjustmentRecord.getCheckTime().getTime()));
						ps.setInt(15, adjustmentRecord.getOrderType());
						ps.setInt(16, adjustmentRecord.getPayMethod());
						ps.setInt(17,adjustmentRecord.getStatus());
					}
				});
	}
	
	/**
	 * 通过订单获取到对应订单的调整单
	 * @param cwbid
	 * @return
	 */
	public List<OrgBillAdjustmentRecord> getAdjustmentRecordByCwb(String cwbid) {
		String sql = "select * from fn_org_bill_adjustment_record where `order_no`=?  ";
		return this.jdbcTemplate.query(sql, new OrgBillAdjustmentRecordRowMapper(), cwbid);
	}
	/**
	 * 修改调整单方法
	 * @param adjustmentRecord
	 */
	public void updateAdjustmentRecord(OrgBillAdjustmentRecord adjustmentRecord,long id){
		try {
			String sql="UPDATE fn_org_bill_adjustment_record SET receive_fee=?,"
					+ "refund_fee=?,modify_fee=?,adjust_amount=?,remark=?,"
					+ "creator=?,create_time=? WHERE id=?";
			
			BigDecimal receive_fee=adjustmentRecord.getReceiveFee();
			BigDecimal refund_fee=adjustmentRecord.getRefundFee();
			BigDecimal modify_fee=adjustmentRecord.getModifyFee();
			BigDecimal adjust_amount=adjustmentRecord.getAdjustAmount();
			String remark=adjustmentRecord.getRemark();
			String creator=adjustmentRecord.getCreator();
			java.util.Date create_time=adjustmentRecord.getCreateTime();
			this.jdbcTemplate.update(sql,receive_fee,refund_fee,modify_fee,adjust_amount,remark,creator,create_time,id);	
		} catch (Exception e) {
			e.printStackTrace();
		}

	}	
}

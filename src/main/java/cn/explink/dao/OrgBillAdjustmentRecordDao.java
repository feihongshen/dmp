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

import com.ctc.wstx.util.DataUtil;

import cn.explink.domain.OrgBillAdjustmentRecord;
/**
 * 结算 站内调整账单的生成
 * @author jiangyu 2015年4月1日
 *
 */
@Component
public class OrgBillAdjustmentRecordDao {
	
	private final class OrgBillAdjustmentRecordRowMapper implements RowMapper<OrgBillAdjustmentRecord> {
		@Override
		public OrgBillAdjustmentRecord mapRow(ResultSet rs, int rowNum) throws SQLException {
			OrgBillAdjustmentRecord adjustmentRecord = new OrgBillAdjustmentRecord();

			adjustmentRecord.setId(rs.getLong("id"));
			adjustmentRecord.setOrderNo(rs.getString("order_no"));
			adjustmentRecord.setBillId(rs.getLong("bill_id"));
			adjustmentRecord.setBillNo(rs.getString("bill_no"));
			adjustmentRecord.setDeliverybranchid(rs.getLong("deliverybranchid"));
			adjustmentRecord.setAdjustBillNo(rs.getString("adjust_bill_no"));
			adjustmentRecord.setCustomerId(rs.getLong("customer_id"));
			adjustmentRecord.setReceiveFee(rs.getBigDecimal("receive_fee"));
			adjustmentRecord.setRefundFee(rs.getBigDecimal("refund_fee"));
			adjustmentRecord.setModifyFee(rs.getBigDecimal("modify_fee"));
			adjustmentRecord.setGoodsAmount(rs.getBigDecimal("goods_amount"));
			adjustmentRecord.setAdjustAmount(rs.getBigDecimal("adjust_amount"));
			adjustmentRecord.setVerifyAmount(rs.getBigDecimal("verify_amount"));
			adjustmentRecord.setRemark(rs.getString("remark"));
			adjustmentRecord.setCreator(rs.getString("creator"));
			adjustmentRecord.setCreateTime(rs.getDate("create_time"));
			adjustmentRecord.setOrderType(rs.getInt("order_type"));
			adjustmentRecord.setPayMethod(rs.getInt("pay_method"));
			adjustmentRecord.setDeliverId(rs.getLong("deliver_id"));
			adjustmentRecord.setSignTime(rs.getDate("sign_time"));
			adjustmentRecord.setStatus(rs.getInt("status"));
			adjustmentRecord.setPayWayChangeFlag(rs.getInt("pay_way_change_flag"));
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
				+ "order_type,"
				+ "pay_method,"
				+ "deliver_id,"
				+ "sign_time,"
				+ "deliverybranchid,"
				+ "goods_amount,"
				+ "pay_way_change_flag,"
				+ "adjust_type,"
				+ "freight_amount,"
				+ ") " 
				+ "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)",
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
						ps.setInt(13, adjustmentRecord.getOrderType());
						ps.setInt(14, adjustmentRecord.getPayMethod());
						ps.setLong(15, adjustmentRecord.getDeliverId());
						ps.setDate(16, adjustmentRecord.getSignTime() == null ? null :new Date(adjustmentRecord.getSignTime().getTime()));
						ps.setLong(17, adjustmentRecord.getDeliverybranchid());
						ps.setBigDecimal(18, adjustmentRecord.getGoodsAmount());
						ps.setInt(19, adjustmentRecord.getPayWayChangeFlag());
						ps.setInt(20, adjustmentRecord.getAdjustType());
						ps.setBigDecimal(20, adjustmentRecord.getFreightAmount());
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

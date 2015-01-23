package cn.explink.dao;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import net.sf.json.JSONObject;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import cn.explink.domain.AbnormalOrder;
import cn.explink.domain.AbnormalWriteBack;
import cn.explink.domain.AdjustmentRecord;
import cn.explink.domain.CwbOrder;
import cn.explink.domain.FnOrgBillDetail;
import cn.explink.domain.User;
import cn.explink.enumutil.VerificationEnum;
import cn.explink.util.Page;
/**
 * 账单明细表DAO
 * @author 140624
 *
 */
@Component
public class FnOrgBillDetailDAO{
	private final class FnOrgBillDetailRowMapper implements RowMapper<FnOrgBillDetail> 	{
		/* (non-Javadoc)
		 * @see org.springframework.jdbc.core.RowMapper#mapRow(java.sql.ResultSet, int)
		 */
		@Override
		public FnOrgBillDetail mapRow(ResultSet rs, int rowNum) throws SQLException {
			FnOrgBillDetail fnOrgBillDetail=new FnOrgBillDetail();

			fnOrgBillDetail.setId(rs.getLong("id"));
			fnOrgBillDetail.setBillId(rs.getLong("bill_id"));
			fnOrgBillDetail.setOrderNo(rs.getString("order_no"));
			fnOrgBillDetail.setOrderId(rs.getString("order_id"));
			fnOrgBillDetail.setStatus(rs.getInt("status"));
			fnOrgBillDetail.setPayMethod(rs.getInt("pay_method"));
			fnOrgBillDetail.setPicker(rs.getString("picker"));
			fnOrgBillDetail.setGoodsAmount(rs.getBigDecimal("goods_amount"));
			fnOrgBillDetail.setVerifyAmount(rs.getBigDecimal("verify_amount"));
			fnOrgBillDetail.setRechargesId(rs.getLong("recharges_id"));
			fnOrgBillDetail.setSignTime(rs.getString("sign_time"));
			fnOrgBillDetail.setRemark(rs.getString("remark"));
			
			return fnOrgBillDetail;
		}
	}
	@Autowired
	private JdbcTemplate jdbcTemplate;
	public void creAdjustmentRecord(final AdjustmentRecord adjustmentRecord) {
		this.jdbcTemplate.update("insert into fn_adjustment_record (order_no,bill_no,adjust_bill_no,customer_id," + "receive_fee,refund_fee,modify_fee,adjust_amount,remark,creator,create_time,"
				+ "status,check_user,check_time,order_type) " + "values(?,?,?,?,?,?,?,?,?,?, ?,?,?,?,?)",
				new PreparedStatementSetter() {
					@Override
					public void setValues(PreparedStatement ps) throws SQLException {
						ps.setString(1, adjustmentRecord.getOrder_no());
						ps.setString(2, adjustmentRecord.getBill_no());
						ps.setString(3, adjustmentRecord.getAdjust_bill_no());
						ps.setLong(4, adjustmentRecord.getCustomer_id());
						ps.setBigDecimal(5, adjustmentRecord.getReceive_fee());
						ps.setBigDecimal(6, adjustmentRecord.getRefund_fee());
						ps.setBigDecimal(7, adjustmentRecord.getModify_fee());
						ps.setBigDecimal(8, adjustmentRecord.getAdjust_amount());
						ps.setString(9, adjustmentRecord.getRemark());
						ps.setString(10, adjustmentRecord.getCreator());
						ps.setString(11, adjustmentRecord.getCreate_time());
						ps.setInt(12, adjustmentRecord.getStatus());
						ps.setString(13, adjustmentRecord.getCheck_user());
						ps.setString(14, adjustmentRecord.getCheck_time());
						ps.setInt(15, adjustmentRecord.getOrder_type());
					}

				});
	}
	
	/**
	 * 通过订单获取到对应订单的调整单
	 * @param cwbid
	 * @return
	 */
	public List<FnOrgBillDetail> getFnOrgBillDetailByCwb(String cwbid) {
		String sql = "select * from fn_org_bill_detail where `order_no`=?  ";
		return this.jdbcTemplate.query(sql, new FnOrgBillDetailRowMapper(), cwbid);
	}
}

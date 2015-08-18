package cn.explink.dao;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import net.sf.json.JSONObject;

import org.junit.Test;
import org.neo4j.cypher.internal.compiler.v2_1.docbuilders.internalDocBuilder;
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
import cn.explink.domain.CwbOrder;
import cn.explink.domain.FnCustomerBill;
import cn.explink.domain.FnCustomerBillDetail;
import cn.explink.domain.User;
import cn.explink.enumutil.VerificationEnum;
import cn.explink.util.Page;

@Component
public class FnCustomerBillDetailDAO{
	private final class FnCustomerBillDetailRowMapper implements RowMapper<FnCustomerBillDetail> {
		@Override
		public FnCustomerBillDetail mapRow(ResultSet rs, int rowNum) throws SQLException {
			FnCustomerBillDetail fnCustomerBillDetail = new FnCustomerBillDetail();

			fnCustomerBillDetail.setId(rs.getLong("id"));
			fnCustomerBillDetail.setBillId(rs.getLong("bill_id"));
			fnCustomerBillDetail.setOrderNo(rs.getString("order_no"));
			fnCustomerBillDetail.setOrderId(rs.getLong("order_id"));
			fnCustomerBillDetail.setReceiveFee(rs.getBigDecimal("receive_fee"));
			fnCustomerBillDetail.setActualPay(rs.getBigDecimal("actual_pay"));
			fnCustomerBillDetail.setPayTime(rs.getString("pay_time"));
			fnCustomerBillDetail.setRefund(rs.getBigDecimal("refund"));
			fnCustomerBillDetail.setActualRefund(rs.getBigDecimal("actual_refund"));
			fnCustomerBillDetail.setRemark(rs.getString("remark"));
			fnCustomerBillDetail.setDiffAmount(rs.getBigDecimal("diff_amount"));
			fnCustomerBillDetail.setPayMethod(rs.getInt("pay_method"));
			fnCustomerBillDetail.setSigner(rs.getString("signer"));
			fnCustomerBillDetail.setSignTime(rs.getString("sign_time"));
			fnCustomerBillDetail.setArrivalTime(rs.getString("arrival_time"));
			return fnCustomerBillDetail;
		}
	}

	private final class FnCustomerBillRowMapper implements RowMapper<FnCustomerBill> {
		@Override
		public FnCustomerBill mapRow(ResultSet rs, int rowNum) throws SQLException {
			FnCustomerBill fnCustomerBill = new FnCustomerBill();

			fnCustomerBill.setId(rs.getLong("id"));
			fnCustomerBill.setBillNo(rs.getString("bill_no"));
			fnCustomerBill.setCustomerId(rs.getLong("customer_id"));
			fnCustomerBill.setBillType(rs.getInt("bill_type"));
			fnCustomerBill.setStatus(rs.getInt("status"));
			fnCustomerBill.setSettleTime(rs.getString("settle_time"));
			fnCustomerBill.setCreator(rs.getString("creator"));
			fnCustomerBill.setCheckTime(rs.getString("check_time"));
			fnCustomerBill.setCheckUser(rs.getString("check_user"));
			fnCustomerBill.setConfirmTime(rs.getDate("confirm_time"));
			fnCustomerBill.setConfirmUser(rs.getString("confirm_user"));
			fnCustomerBill.setVerifyTime(rs.getString("verify_time"));
			fnCustomerBill.setVerifyUser(rs.getString("verify_user"));
			fnCustomerBill.setDeliverOrg(rs.getString("deliver_org"));
			fnCustomerBill.setBillCount(rs.getInt("bill_count"));
			fnCustomerBill.setBillAmount(rs.getBigDecimal("bill_amount"));
			fnCustomerBill.setActualAmount(rs.getBigDecimal("actual_amount"));
			fnCustomerBill.setRemark(rs.getString("remark"));
			fnCustomerBill.setDateType(rs.getInt("date_type"));
			fnCustomerBill.setStartTime(rs.getString("start_time"));
			fnCustomerBill.setEndTime(rs.getString("end_time"));
			fnCustomerBill.setCreateType(rs.getString("create_type"));
			fnCustomerBill.setImportRemark(rs.getString("import_remark"));
			return fnCustomerBill;
		}
	}
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	

	public List<FnCustomerBill> getFnCustomerBillDetailByCwb(String cwb) {
		//String sql="SELECT * FROM `fn_customer_bill_detail` WHERE order_no=?";
		String sql = " SELECT a.* FROM fn_customer_bill AS a LEFT JOIN fn_customer_bill_detail AS b "
				   + " ON a.id = b.bill_id"
				   + " WHERE a.bill_type= 2 "
				   + " AND b.order_no=?";		
		return jdbcTemplate.query(sql, new FnCustomerBillRowMapper(),cwb);
	}
	
	
	
	
}

package main.java.cn.explink.dao;

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
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	

	public List<FnCustomerBillDetail> getFnCustomerBillDetailByCwb(String cwb) {
		String sql="SELECT * FROM `fn_customer_bill_detail` WHERE order_no=?";
				
		return jdbcTemplate.query(sql, new FnCustomerBillDetailRowMapper(),cwb);
	}
	
	
	
	
}

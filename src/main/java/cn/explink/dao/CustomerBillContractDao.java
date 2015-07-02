package cn.explink.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import cn.explink.domain.CustomerBillContract;

@Repository
public class CustomerBillContractDao {
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	private final class CustomerBillContractmapper implements RowMapper<CustomerBillContract>{

		@Override
		public CustomerBillContract mapRow(ResultSet rs, int rowNum)
				throws SQLException {
			CustomerBillContract c = new CustomerBillContract();
			c.setId(rs.getLong("id"));
			c.setBillBatches(rs.getLong("bill_batches"));
			c.setBillState(rs.getLong("bill_state"));
			c.setCustomerId(rs.getLong("customer_id"));
			c.setDateRange(rs.getString("date_range"));
			c.setCorrespondingCwbNum(rs.getLong("corresponding_cwb_num"));
			c.setDeliveryMoney(rs.getBigDecimal("delivery_money"));
			c.setDistributionMoney(rs.getBigDecimal("distribution_money"));
			c.setTransferMoney(rs.getBigDecimal("transfer_money"));
			c.setRefuseMoney(rs.getBigDecimal("transfer_money"));
			c.setTotalCharge(rs.getBigDecimal("total_charge"));
			c.setRemark(rs.getString("remark"));
			c.setDateCreateBill(rs.getString("date_create_bill"));
			c.setDateVerificationBill(rs.getString("date_verification_bill"));
			c.setCwbOrderType(rs.getLong("cwb_order_type"));
			
			return c;
		}
	}
	
	public CustomerBillContract findCustomerBillContract(){
		String sql="";
//		return jdbcTemplate.query();
		return null;
	}

}

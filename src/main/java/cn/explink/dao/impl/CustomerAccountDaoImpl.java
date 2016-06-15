package cn.explink.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import cn.explink.dao.interfac.ICustomerAccountDao;
import cn.explink.domain.CustomerAccountPO;

@Repository("customerAccountDao")
public class CustomerAccountDaoImpl implements ICustomerAccountDao {

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	private static final String TABLE_NAME = "fn_cust_pay_report_cfg" ;
	
	private final class CustomerAccountMapper  implements RowMapper<CustomerAccountPO>{

		@Override
		public CustomerAccountPO mapRow(ResultSet rs, int rowNum) throws SQLException {
			CustomerAccountPO customerAcc = new CustomerAccountPO() ;
			customerAcc.setCustomerId(rs.getInt("customer_id"));
			String customerNumber = rs.getString("customerno") ;
			if(StringUtils.isEmpty(customerNumber)){
				customerNumber = rs.getString("customercode") ;
			}
			customerAcc.setCustomerNumber(customerNumber);
			String customerName = rs.getString("customername") ;
			if(!StringUtils.isEmpty(customerName)){
				customerAcc.setCustomerName(customerName);
			}
			customerAcc.setSettleType(rs.getInt("settle_type"));
			customerAcc.setSettleTimeType(rs.getInt("settle_time_type"));
			customerAcc.setCreateTime(rs.getString("create_time"));
			customerAcc.setCreator(rs.getString("create_user"));
			return customerAcc;
		}
	}
	
	@Override
	public CustomerAccountPO findCustomer(String customerId) {
		StringBuffer sql = new StringBuffer();
		sql.append("select cfg.customer_id,info.customerno ,info.customercode ,info.customername,cfg.settle_type,cfg.settle_time_type,cfg.create_time,cfg.create_time,cfg.create_user from ") ;
		sql.append(TABLE_NAME) ;
		sql.append(" cfg inner join express_set_customer_info info") ;
		sql.append(" on cfg.customer_id = info.customerid") ;
		sql.append(" where 1 = 1").append(" and cfg.customer_id = ").append(customerId) ;
		sql.append(" order by cfg.customer_id ") ;
		List<CustomerAccountPO> list = this.jdbcTemplate.query(sql.toString(), new CustomerAccountMapper()) ;
		if(list == null || list.size() == 0){
			return null ;
		}
		return list.get(0) ;
	}

}

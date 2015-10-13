package cn.explink.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import cn.explink.domain.Customer;
import cn.explink.util.Page;

@Component
public class CustomerDAO {

	private final class CustomerRowMapper implements RowMapper<Customer> {
		@Override
		public Customer mapRow(ResultSet rs, int rowNum) throws SQLException {
			Customer customer = new Customer();
			customer.setCustomerid(rs.getLong("customerid"));
			customer.setCustomername(rs.getString("customername"));
			customer.setCustomeraddress(rs.getString("customeraddress"));
			customer.setCustomercontactman(rs.getString("customercontactman"));
			customer.setCustomerphone(rs.getString("customerphone"));
			customer.setIfeffectflag(rs.getLong("ifeffectflag"));
			return customer;
		}
	}

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public List<Customer> getAllCustomers() {
		return jdbcTemplate.query("select * from express_set_customer_info where ifeffectflag=1", new CustomerRowMapper());
	}

	public List<Customer> getAllCustomersByExistRules() {
		return jdbcTemplate.query("select * from (select customerid from express_set_excel_column ) co left join express_set_customer_info ci on co.customerid=ci.customerid", new CustomerRowMapper());
	}

	public Customer getCustomer(long id) {
		try {
			return jdbcTemplate.queryForObject("select * from express_set_customer_info where customerid=?", new Object[] { id }, new CustomerRowMapper());
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	public List<Customer> getCustomerByCustomername(String customername) {
		List<Customer> customerList = jdbcTemplate.query("SELECT * from express_set_customer_info where customername like '" + customername + "'", new CustomerRowMapper());
		return customerList;
	}

	public List<Customer> getCustomerByCustomernameCheck(String customername) {
		List<Customer> customerList = jdbcTemplate.query("SELECT * from express_set_customer_info where customername=?", new CustomerRowMapper(), customername);
		return customerList;
	}

	private String getCustomerByPageWhereSql(String sql, String customername) {
		if (customername.length() > 0) {
			sql += " where customername like '%" + customername + "%'";
		}
		return sql;
	}

	public List<Customer> getCustomerByPage(long page, String customername) {
		String sql = "select * from express_set_customer_info";
		sql = this.getCustomerByPageWhereSql(sql, customername);
		sql += " order by ifeffectflag desc limit " + (page - 1) * Page.ONE_PAGE_NUMBER + " ," + Page.ONE_PAGE_NUMBER;
		List<Customer> customerList = jdbcTemplate.query(sql, new CustomerRowMapper());
		return customerList;
	}

	public long getCustomerCount(String customer) {
		String sql = "select count(1) from express_set_customer_info";
		sql = this.getCustomerByPageWhereSql(sql, customer);
		return jdbcTemplate.queryForInt(sql);
	}

	public void creCustomer(final Customer customer) {

		jdbcTemplate.update("insert into express_set_customer_info(customername,customeraddress,customercontactman,customerphone) values(?,?,?,?)", new PreparedStatementSetter() {

			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				// TODO Auto-generated method stub
				ps.setString(1, customer.getCustomername());
				ps.setString(2, customer.getCustomeraddress());
				ps.setString(3, customer.getCustomercontactman());
				ps.setString(4, customer.getCustomerphone());
			}
		});

	}

	public Customer getCustomerById(long customerid) {

		try {
			return jdbcTemplate.queryForObject("select * from express_set_customer_info where customerid = ?", new CustomerRowMapper(), customerid);
		} catch (DataAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return new Customer();
		}

	}

	public void save(final Customer customer) {

		jdbcTemplate.update("update express_set_customer_info set customername=?,customeraddress=?,customercontactman=?,customerphone=? where customerid = ? ", new PreparedStatementSetter() {

			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				// TODO Auto-generated method stub
				ps.setString(1, customer.getCustomername());
				ps.setString(2, customer.getCustomeraddress());
				ps.setString(3, customer.getCustomercontactman());
				ps.setString(4, customer.getCustomerphone());
				ps.setLong(5, customer.getCustomerid());
			}
		});

	}

	public void delCustomer(long customerid) {
		jdbcTemplate.update("update express_set_customer_info set ifeffectflag=(ifeffectflag+1)%2 where customerid=" + customerid);
	}

}

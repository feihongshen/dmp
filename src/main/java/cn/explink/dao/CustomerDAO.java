package cn.explink.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import cn.explink.domain.Customer;
import cn.explink.util.Page;
import cn.explink.util.StringUtil;

@Component
public class CustomerDAO {

	private final class CustomerRowMapper implements RowMapper<Customer> {
		@Override
		public Customer mapRow(ResultSet rs, int rowNum) throws SQLException {
			Customer customer = new Customer();
			customer.setCustomerid(rs.getLong("customerid"));
			customer.setCustomername(StringUtil.nullConvertToEmptyString(rs.getString("customername")));
			customer.setCustomercode(StringUtil.nullConvertToEmptyString(rs.getString("customercode")));
			customer.setCustomeraddress(StringUtil.nullConvertToEmptyString(rs.getString("customeraddress")));
			customer.setCustomercontactman(StringUtil.nullConvertToEmptyString(rs.getString("customercontactman")));
			customer.setCustomerphone(rs.getString("customerphone"));
			customer.setIfeffectflag(rs.getLong("ifeffectflag"));
			customer.setB2cEnum(StringUtil.nullConvertToEmptyString(rs.getString("b2cEnum")));
			customer.setPaytype(rs.getLong("paytype"));
			customer.setIsypdjusetranscwb(rs.getLong("isypdjusetranscwb"));
			customer.setIsUsetranscwb(rs.getLong("isUsetranscwb"));
			customer.setIsAutoProductcwb(rs.getLong("isAutoProductcwb"));
			customer.setAutoProductcwbpre(rs.getString("autoProductcwbpre"));
			customer.setIsFeedbackcwb(rs.getInt("isFeedbackcwb"));
			customer.setCompanyname(StringUtil.nullConvertToEmptyString(rs.getString("companyname")));
			customer.setSmschannel(rs.getInt("smschannel"));
			customer.setIsqufendaxiaoxie(rs.getLong("isqufendaxiaoxie"));
			customer.setNeedchecked(rs.getInt("needchecked"));
			customer.setPfruleid(rs.getLong("pfruleid"));
			customer.setWavFilePath(rs.getString("wav_filepath"));
			return customer;
		}
	}

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public List<Customer> getAllCustomers() {
		return this.jdbcTemplate.query("select * from express_set_customer_info where ifeffectflag=1", new CustomerRowMapper());
	}

	public List<Customer> getAllCustomersWithDisable() {
		return this.jdbcTemplate.query("select * from express_set_customer_info where ifeffectflag = 1", new CustomerRowMapper());
	}

	public List<Customer> getAllIsAutoProductcwbCustomers() {
		return this.jdbcTemplate.query("select * from express_set_customer_info where ifeffectflag=1 and isAutoProductcwb=1", new CustomerRowMapper());
	}

	public List<Customer> getCustomersByQuanKai() {
		return this.jdbcTemplate.query("select * from express_set_customer_info where ifeffectflag=1 and isAutoProductcwb=1 and isUsetranscwb=0 and isypdjusetranscwb=1", new CustomerRowMapper());
	}

	public Map<Long, Customer> getAllCustomersToMap() {
		Map<Long, Customer> customerMap = new HashMap<Long, Customer>();
		for (Customer customer : this.getAllCustomersNew()) {
			customerMap.put(customer.getCustomerid(), customer);
		}
		return customerMap;
	}

	public List<Customer> getAllCustomersByExistRules() {
		return this.jdbcTemplate.query(
				"select * from (select customerid from express_set_excel_column ) co left join express_set_customer_info ci on co.customerid=ci.customerid where ci.ifeffectflag='1' ",
				new CustomerRowMapper());
	}

	public List<Customer> getCustomerByCustomername(String customername, String code, long customerid) {
		String sql = "SELECT * from express_set_customer_info where (customername = ? or customercode=? ) and customerid<>?";
		return this.jdbcTemplate.query(sql, new CustomerRowMapper(), customername, code, customerid);
	}

	public List<Customer> getCustomerByCustomername(String customername, String code) {
		String sql = "SELECT * from express_set_customer_info where customername = ? or customercode=? ";
		return this.jdbcTemplate.query(sql, new CustomerRowMapper(), customername, code);
	}

	public List<Customer> getCustomerByCustomernameCheck(String customername) {
		List<Customer> customerList = this.jdbcTemplate.query("SELECT * from express_set_customer_info where customername=?", new CustomerRowMapper(), customername);
		return customerList;
	}

	public List<Customer> getCustomerByPage(long page, String customername) {
		String sql = "select * from express_set_customer_info";
		if (customername.length() > 0) {
			sql += " where customername like '%" + customername + "%'";
		}
		sql += " order by ifeffectflag desc limit " + ((page - 1) * Page.ONE_PAGE_NUMBER) + " ," + Page.ONE_PAGE_NUMBER;
		List<Customer> customerList = this.jdbcTemplate.query(sql, new CustomerRowMapper());
		return customerList;
	}

	public long getCustomerCount(String customer) {
		String sql = "select count(1) from express_set_customer_info";
		if (customer.length() > 0) {
			sql += " where customername like '%" + customer + "%'";
		}
		return this.jdbcTemplate.queryForInt(sql);
	}

	public void creCustomer(final Customer customer) {
		final String insertsql = "insert into express_set_customer_info(customername,customercode,customeraddress,customercontactman,customerphone,b2cEnum,paytype,isypdjusetranscwb,isUsetranscwb,isAutoProductcwb,autoProductcwbpre,isFeedbackcwb,companyname,smschannel,isqufendaxiaoxie,wav_filepath,pfruleid) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		KeyHolder keyHolder = new GeneratedKeyHolder();
		this.jdbcTemplate.update(new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
				PreparedStatement ps = connection.prepareStatement(insertsql, new String[] { "id" });
				ps.setString(1, customer.getCustomername());
				ps.setString(2, customer.getCustomercode());
				ps.setString(3, customer.getCustomeraddress());
				ps.setString(4, customer.getCustomercontactman());
				ps.setString(5, customer.getCustomerphone());
				ps.setString(6, customer.getB2cEnum());
				ps.setLong(7, customer.getPaytype());
				ps.setLong(8, customer.getIsypdjusetranscwb());
				ps.setLong(9, customer.getIsUsetranscwb());
				ps.setLong(10, customer.getIsAutoProductcwb());
				ps.setString(11, customer.getAutoProductcwbpre());
				ps.setInt(12, customer.getIsFeedbackcwb());
				ps.setString(13, customer.getCompanyname());
				ps.setInt(14, customer.getSmschannel());
				ps.setLong(15, customer.getIsqufendaxiaoxie());
				ps.setString(16, customer.getWavFilePath());
				ps.setLong(17, customer.getPfruleid());
				return ps;
			}
		}, keyHolder);
		customer.setCustomerid(keyHolder.getKey().longValue());
	}

	public List<Customer> getCustomerByPaytype(long paytype) {
		return this.jdbcTemplate.query("select * from express_set_customer_info where paytype = ? and ifeffectflag='1'", new CustomerRowMapper(), paytype);
	}

	@Cacheable(value = "customerCache", key = "#customerid")
	public Customer getCustomerById(long customerid) {
		try {
			return this.jdbcTemplate.queryForObject("select * from express_set_customer_info where customerid = ? ", new CustomerRowMapper(), customerid);
		} catch (DataAccessException e) {
			// TODO Auto-generated catch block
			return new Customer();
		}
	}

	@CacheEvict(value = "customerCache", key = "#customer.customerid")
	public void save(final Customer customer) {

		this.jdbcTemplate
				.update("update express_set_customer_info set customername=?,customercode=?,customeraddress=?,customercontactman=?,customerphone=? ,paytype=?,isypdjusetranscwb=?,isUsetranscwb=?,isAutoProductcwb=?,autoProductcwbpre=?,isFeedbackcwb=?,companyname=?,smschannel=?,isqufendaxiaoxie=? ,needchecked=?,wav_filepath=? ,pfruleid=?"
						+ " where customerid = ? ", new PreparedStatementSetter() {

					@Override
					public void setValues(PreparedStatement ps) throws SQLException {
						// TODO Auto-generated method stub
						ps.setString(1, customer.getCustomername());
						ps.setString(2, customer.getCustomercode());
						ps.setString(3, customer.getCustomeraddress());
						ps.setString(4, customer.getCustomercontactman());
						ps.setString(5, customer.getCustomerphone());
						ps.setLong(6, customer.getPaytype());
						ps.setLong(7, customer.getIsypdjusetranscwb());
						ps.setLong(8, customer.getIsUsetranscwb());
						ps.setLong(9, customer.getIsAutoProductcwb());
						ps.setString(10, customer.getAutoProductcwbpre());
						ps.setInt(11, customer.getIsFeedbackcwb());
						ps.setString(12, customer.getCompanyname());
						ps.setInt(13, customer.getSmschannel());
						ps.setLong(14, customer.getIsqufendaxiaoxie());
						ps.setInt(15, customer.getNeedchecked());
						ps.setString(16, customer.getWavFilePath());
						ps.setLong(17, customer.getPfruleid());
						ps.setLong(18, customer.getCustomerid());
					}
				});

	}

	@CacheEvict(value = "customerCache", key = "#customerid")
	public void delCustomer(long customerid) {
		this.jdbcTemplate.update("update express_set_customer_info set ifeffectflag=(ifeffectflag+1)%2 where customerid=?", customerid);
	}

	public List<Customer> getCustomerByIds(String customerids) {
		if (customerids.length() > 0) {
			List<Customer> customer = this.jdbcTemplate.query("select * from express_set_customer_info where customerid in(" + customerids + ")", new CustomerRowMapper());
			return customer;
		} else {
			return null;
		}

	}

	public List<Customer> getCustomerByIdsAndId(String customerids, long customerid) {
		if (customerids.length() > 0) {
			List<Customer> customer = this.jdbcTemplate.query("select * from express_set_customer_info where customerid in(" + customerids + ") and customerid=" + customerid, new CustomerRowMapper());
			return customer;
		} else {
			return null;
		}

	}

	public List<Customer> getCustomerByNoInIds(String customerids) {
		if (customerids.length() > 0) {
			List<Customer> customer = this.jdbcTemplate.query("select * from express_set_customer_info where customerid not in(" + customerids + ")", new CustomerRowMapper());
			return customer;
		} else {
			return null;
		}

	}

	@CacheEvict(value = "customerCache", allEntries = true)
	public void updateB2cEnumByJoint_num(String customerids, String oldCustomerids, int joint_num) {
		if (!"".equals(oldCustomerids)) {
			String[] oldCustormeridstr = oldCustomerids.split(",");
			String oldCustormeridstrs = "";
			for (int i = 0; i < oldCustormeridstr.length; i++) {
				oldCustormeridstrs += oldCustormeridstr[i] + ",";
			}
			if ((oldCustormeridstr != null) && (oldCustormeridstr.length > 0)) {
				// 先清空原来的配置
				this.jdbcTemplate.update("update express_set_customer_info set b2cEnum=0 where customerid in(" + oldCustormeridstrs.substring(0, oldCustormeridstrs.length() - 1) + ")");
			}
		}
		if (!"".equals(customerids)) {
			String[] custormeridstr = customerids.split(",");
			String custormeridstrs = "";
			for (int i = 0; i < custormeridstr.length; i++) {
				custormeridstrs += custormeridstr[i] + ",";
			}
			if ((custormeridstr != null) && (custormeridstr.length > 0)) {
				// 更新前台传递过来的参数
				this.jdbcTemplate.update("update express_set_customer_info set b2cEnum=? where customerid in(" + custormeridstrs.substring(0, custormeridstrs.length() - 1) + ")", joint_num);
			}
		}
	}

	// 获取全部供货商 不管是否已经停用
	public List<Customer> getAllCustomersNew() {
		return this.jdbcTemplate.query("select * from express_set_customer_info", new CustomerRowMapper());
	}

	// 获取全部使用一票多件的供货商 不管是否已经停用
	public List<Customer> getAllCustomersByYPDJ() {
		return this.jdbcTemplate.query("select * from express_set_customer_info where isypdjusetranscwb=1 ", new CustomerRowMapper());
	}

	public List<Long> getAllCustomerId() {
		String sql = "select customerid from express_set_customer_info";
		return this.jdbcTemplate.queryForList(sql, Long.class);
	}

	public String getCustomerName(long customerId) {
		String sql = "select customername from express_set_customer_info where customerid = ?";
		Object[] paras = new Object[] { customerId };

		return this.jdbcTemplate.queryForObject(sql, paras, String.class);
	}

	public Map<Long, String> getCustomerNameMap(Set<Long> customerIdSet) {
		final Map<Long, String> customerNameMap = new HashMap<Long, String>();
		String inPara = this.getInPara(customerIdSet);
		String sql = "select customerid , customername from express_set_customer_info where customerid in (" + inPara + ")";

		this.jdbcTemplate.query(sql, new RowCallbackHandler() {

			@Override
			public void processRow(ResultSet rs) throws SQLException {
				customerNameMap.put(rs.getLong("customerid"), rs.getString("customername"));
			}
		});
		return customerNameMap;
	}

	private String getInPara(Set<Long> customerIdSet) {
		StringBuilder para = new StringBuilder();
		for (Long customerId : customerIdSet) {
			para.append(customerId);
			para.append(",");
		}
		return para.substring(0, para.length() - 1);
	}

	public Customer findcustomername(long customerid) {
		try {
			String sql = "select * from express_set_customer_info where customerid=? limit 1";

			return this.jdbcTemplate.queryForObject(sql, new CustomerRowMapper(), customerid);
		} catch (DataAccessException e) {
			// TODO Auto-generated catch block
			return null;
		}

	}

	public List<Customer> getAllCustomerss() {
		String sql = "select * from express_set_customer_info ";
		return this.jdbcTemplate.query(sql, new CustomerRowMapper());
	}
}

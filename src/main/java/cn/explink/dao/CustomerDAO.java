package cn.explink.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
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
import org.springframework.util.CollectionUtils;

import cn.explink.aspect.SystemInstallOperation;
import cn.explink.b2c.tools.B2cEnum;
import cn.explink.b2c.tools.JiontDAO;
import cn.explink.b2c.tools.JointEntity;
import cn.explink.domain.Customer;
import cn.explink.util.Page;
import cn.explink.util.StringUtil;

@Component
public class CustomerDAO {
	@Autowired
	JiontDAO jiontDAO;
	
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
			customer.setAutoArrivalBranchFlag(rs.getInt("autoarrivalbranchflag")); // 客户揽退单是否自动到货标志---刘武强20161026

			customer.setMpsswitch(rs.getInt("mpsswitch"));//供应商的集单开关
			return customer;
		}
	}

	/**
	 *
	 * @description
	 * @author 刘武强
	 * @data 2015年10月13日
	 */
	private final class ImportCustomerRowMapper implements RowMapper<Customer> {
		@Override
		public Customer mapRow(ResultSet rs, int rowNum) throws SQLException {
			Customer customer = new Customer();
			customer.setCustomerid(rs.getLong("customerid"));
			customer.setCustomername(StringUtil.nullConvertToEmptyString(rs.getString("customername")));
			customer.setCustomercode(StringUtil.nullConvertToEmptyString(rs.getString("customercode")));
			customer.setCompanyname(StringUtil.nullConvertToEmptyString(rs.getString("companyname")));
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

	/**
	 *
	 * @Title: getCustomerByCustomernames
	 * @description 通过客户名称查询所有的客户信息
	 * @author 刘武强
	 * @date 2015年10月12日上午10:21:18
	 * @param @param customernames
	 * @param @return
	 * @return List<Customer>
	 * @throws
	 */
	public List<Customer> getCustomerByCustomernames(String customernames) {
		String sql = "SELECT * from express_set_customer_info where ifeffectflag=1 and companyname in " + customernames;
		return this.jdbcTemplate.query(sql, new ImportCustomerRowMapper());
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
		return this.jdbcTemplate
				.query("select * from (select customerid from express_set_excel_column ) co left join express_set_customer_info ci on co.customerid=ci.customerid where ci.ifeffectflag='1' ", new CustomerRowMapper());
	}

	public List<Customer> getCustomerByCustomername(String customername, String code, long customerid) {
		String sql = "SELECT * from express_set_customer_info where (customername = ? or customercode=? ) and customerid<>?";
		return this.jdbcTemplate.query(sql, new CustomerRowMapper(), customername, code, customerid);
	}

	public List<Customer> getCustomerByCustomername(String customername, String code) {
		String sql = "SELECT * from express_set_customer_info where customername = ? or customercode=? ";
		return this.jdbcTemplate.query(sql, new CustomerRowMapper(), customername, code);
	}

	public List<Customer> getCustomerByCustomerid(String customerid) {
		String sql = "SELECT * from express_set_customer_info where customerid = ?";
		return this.jdbcTemplate.query(sql, new CustomerRowMapper(), customerid);
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

	@SystemInstallOperation
	public void creCustomer(final Customer customer) {
		final String insertsql = "insert into express_set_customer_info(customername,customercode,customeraddress,customercontactman,customerphone,b2cEnum,paytype,isypdjusetranscwb,isUsetranscwb,isAutoProductcwb,autoProductcwbpre,isFeedbackcwb,companyname,smschannel,isqufendaxiaoxie,wav_filepath,pfruleid,mpsswitch,autoarrivalbranchflag) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
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
				ps.setInt(18, customer.getMpsswitch());
				ps.setInt(19, customer.getAutoArrivalBranchFlag());
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

	@SystemInstallOperation
	@CacheEvict(value = "customerCache", key = "#customer.customerid")
	public void save(final Customer customer) {

		this.jdbcTemplate
				.update("update express_set_customer_info set customername=?,customercode=?,customeraddress=?,customercontactman=?,customerphone=? ,paytype=?,isypdjusetranscwb=?,isUsetranscwb=?,isAutoProductcwb=?,autoProductcwbpre=?,isFeedbackcwb=?,companyname=?,smschannel=?,isqufendaxiaoxie=? ,needchecked=?,wav_filepath=? ,pfruleid=? ,mpsswitch=? ,autoarrivalbranchflag=? " + " where customerid = ? ", new PreparedStatementSetter() {

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
						ps.setInt(18, customer.getMpsswitch());
						ps.setInt(19, customer.getAutoArrivalBranchFlag());
						ps.setLong(20, customer.getCustomerid());
					}
				});

	}

	@SystemInstallOperation
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
	
	/**
	 * 根据客户id查找已经绑定b2cEnum的记录
	 * @param customerids
	 * @return
	 */
	public List<Customer> getCustomerBoundB2cEnumByIds(String customerids) {
		if (customerids.length() > 0) {
			List<Customer> customer = this.jdbcTemplate.query("select * from express_set_customer_info where b2cEnum<>'0' and customerid in(" + customerids + ")", new CustomerRowMapper());
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

	@SystemInstallOperation
	@CacheEvict(value = "customerCache", allEntries = true)
	public void updateB2cEnumByJoint_num(String customerids, String oldCustomerids, int joint_num) {
		/*if (!"".equals(oldCustomerids)) {
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
		}*/
		
		String[] custormeridstr = null;//修改之后的客户id
		String[] oldCustomeridStr = null;//修改之前的客户id
		List<Long> customeridArray = new ArrayList<Long>();
		List<Long> oldCustomeridArray = new ArrayList<Long>();
		if(StringUtils.isNotBlank(customerids)){
			custormeridstr = customerids.split(",|，");
			for(String custStr : custormeridstr){
				try{
					customeridArray.add(Long.valueOf(custStr));
				}catch(Exception e){
					
				}
			}
		}
		
		if(StringUtils.isNotBlank(oldCustomerids)){
			oldCustomeridStr = oldCustomerids.split(",|，");
			for(String oldCustStr : oldCustomeridStr){
				try{
					oldCustomeridArray.add(Long.valueOf(oldCustStr));
				}catch(Exception e){
					
				}
			}
		}
		/**
		 * 如果新设置的客户id 设置过其他的对接，不允许设置当前的对接
		 */
		if(!customeridArray.isEmpty()){
			List<Customer> oldCustList = this.getCustomerBoundB2cEnumByIds(StringUtil.getStringsByLongList(customeridArray));
			if(oldCustList != null){
				for(Customer cust : oldCustList){
					if(!cust.getB2cEnum().equals(joint_num + "") && this.jiontDAO.getJointEntity(Integer.valueOf(cust.getB2cEnum())) != null){
						throw new RuntimeException("客户" + cust.getCustomerid() + "已在【" + B2cEnum.getEnumByKey(Integer.valueOf(cust.getB2cEnum()).intValue()).getText() + "】设置对接");
					}
					
				}
			}
		}
		/**
		 * 对修改前的客户id，需要解绑b2cEnum
		 */
		if(!oldCustomeridArray.isEmpty()){
			List<Customer> oldCustList = this.getCustomerBoundB2cEnumByIds(StringUtil.getStringsByLongList(oldCustomeridArray));
			if(oldCustList != null){
				for(Customer cust : oldCustList){
					if(cust.getB2cEnum().equals(joint_num + "")){
						// 兼容旧数据，此customer是否还存在其它开启的,不等于joint_num对接配置中，如果存在多条，默认取第一条
						int otherJointNum = getOtherJointNum(cust.getCustomerid(), joint_num);
						if(otherJointNum == 0){
							this.jdbcTemplate.update("update express_set_customer_info set b2cEnum=0 where customerid =?", cust.getCustomerid());
						} else {
							this.jdbcTemplate.update("update express_set_customer_info set b2cEnum=? where customerid =?", otherJointNum, cust.getCustomerid());
						}
					}
					
				}
			}
		}
		/**
		 * 绑定当前设置的客户的b2cEnum
		 */
		if(!customeridArray.isEmpty()){
			this.jdbcTemplate.update("update express_set_customer_info set b2cEnum=? where customerid in(" + StringUtil.getStringsByLongList(customeridArray) + ")", joint_num);
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

	public List<Customer> getCustomerByCustomerName(String customername) {
		String sql = "select * from express_set_customer_info";
		if (customername.length() > 0) {
			sql += " where customername like '%" + customername + "%'";
		}
		List<Customer> customerList = this.jdbcTemplate.query(sql, new CustomerRowMapper());
		return customerList;
	}

	/**
	 * @param pfruleid
	 * @return
	 */
	public List<Customer> getCustomerByPFruleId(long pfruleid) {
		String sql = "select * from express_set_customer_info where ifeffectflag=1 and pfruleid=?";
		return this.jdbcTemplate.query(sql, new CustomerRowMapper(), pfruleid);

	}

	@CacheEvict(value = "customerCache", allEntries = true)
	public void updateCache() {

	}

	/**
	 * 根据b2cenum获取供货商
	 */
	public Customer getCustomerbyB2cenum(String b2cEnum) {
		String sql = "select * from express_set_customer_info where b2cEnum=? limit 1";
		try {
			return this.jdbcTemplate.queryForObject(sql, new CustomerRowMapper(), b2cEnum);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * 获取客户对象
	 * @param name
	 * @return
	 */
	public Customer getCustomerByName(String name) {
		String sql = "select * from express_set_customer_info where customername=? limit 0,1";
		try {
			Customer customer = this.jdbcTemplate.queryForObject(sql, new CustomerRowMapper(), name);
			return customer;
		} catch (DataAccessException e) {
			return null;
		}
	}
	
	/**
	 * 返回其它的配置joint_num
	 * @author jian.xie
	 * @date 20160907
	 * @param customerid
	 * @param joint_num
	 * @return 
	 */
	private int getOtherJointNum(long customerid, int joint_num) {
		List<JointEntity> jointList = jiontDAO.getJointEntity(customerid, joint_num);
		if(CollectionUtils.isEmpty(jointList)){
			return 0;
		}
		String idStr = null;
		String[] id = null;
		Set<String> idSet = null;
		for(JointEntity entity : jointList){
			idStr = getCustomerStr(entity.getJoint_property());
			if(idStr.length() == 0){
				continue;
			}
			id = idStr.split(",");
			idSet = new HashSet<String>(Arrays.asList(id));
			if(idSet.contains(String.valueOf(customerid))){
				return entity.getJoint_num();
			}
		}			
		return 0;
	}
	
	/**
	 * 提取customerid和乐锋id
	 */
	private String getCustomerStr(String jointProperty){
		String pattern = "(?<=\"(customerids|lefengCustomerid)\":\")(\\d+[,，0-9]*)";
		// 创建 Pattern 对象
	    Pattern r = Pattern.compile(pattern);
	    String result = "";
	    // 现在创建 matcher 对象
	    Matcher m = r.matcher(jointProperty);
	    while(m.find()){
	    	if(result.length() > 0){
	    		result += ",";
	    	}
	    	result += m.group();
	    }		
		return result;
	}
}
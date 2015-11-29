package cn.explink.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import cn.explink.domain.addressvo.AddressCustomerStationVO;
import cn.explink.util.Page;

@Component
public class AddressCustomerStationDao {

	@Autowired
	JdbcTemplate jdbcTemplate;

	@Autowired
	BranchDAO branchDao;
	@Autowired
	CustomerDAO customerDao;

	private final class AddressCustomerStationMapper implements RowMapper<AddressCustomerStationVO> {

		@Override
		public AddressCustomerStationVO mapRow(ResultSet rs, int rowNum) throws SQLException {
			// TODO Auto-generated method stub
			AddressCustomerStationVO addressCustomerStation = new AddressCustomerStationVO();
			addressCustomerStation.setId(rs.getInt("id"));
			addressCustomerStation.setCustomerid(rs.getInt("customerid"));
			addressCustomerStation.setCustomerName(AddressCustomerStationDao.this.customerDao.getCustomerById(rs.getInt("customerid")).getCustomername());
			addressCustomerStation.setBranchid(rs.getInt("branchid"));
			addressCustomerStation.setBranchName(AddressCustomerStationDao.this.branchDao.getBranchById(rs.getInt("branchid")).getBranchname());
			return addressCustomerStation;
		}

	}

	private final class AddressCustomerStationBranchIDMapper implements RowMapper<AddressCustomerStationVO> {

		@Override
		public AddressCustomerStationVO mapRow(ResultSet rs, int rowNum) throws SQLException {
			// TODO Auto-generated method stub
			AddressCustomerStationVO addressCustomerStation = new AddressCustomerStationVO();
			// addressCustomerStation.setId(rs.getInt("id"));
			addressCustomerStation.setCustomerid(rs.getInt("customerid"));
			addressCustomerStation.setCustomerName(AddressCustomerStationDao.this.customerDao.getCustomerById(rs.getInt("customerid")).getCustomername());
			addressCustomerStation.setBranchid(rs.getInt("branchid"));
			addressCustomerStation.setBranchName(AddressCustomerStationDao.this.branchDao.getBranchById(rs.getInt("branchid")).getBranchname());
			return addressCustomerStation;
		}

	}

	// 获取全部记录
	public List<AddressCustomerStationVO> getAllCustomerStations() {
		String sql = "select * from express_set_customer_station";
		List<AddressCustomerStationVO> list = this.jdbcTemplate.query(sql, new AddressCustomerStationMapper());
		return list;
	}

	// 获取全部记录带分页
	public List<AddressCustomerStationVO> getAllCustomerStationsByPage(Long page, String customerid, String station) {
		String sql = "select * from express_set_customer_station where 1=1 ";
		List<Object> listParams = new ArrayList<Object>();
		if ((customerid != null) && !"".equals(customerid)) {
			sql += " and customerid=?";
			listParams.add(customerid);
		}
		if ((station != null) && !"".equals(station)) {
			sql += " and branchid=?";
			listParams.add(station);
		}
		Object[] obj = new Object[listParams.size()];
		for (int a = 0; a < listParams.size(); a++) {
			obj[a] = listParams.get(a);
		}
		sql += " limit " + ((page - 1) * Page.ONE_PAGE_NUMBER) + " ," + Page.ONE_PAGE_NUMBER;
		List<AddressCustomerStationVO> list = this.jdbcTemplate.query(sql, obj, new AddressCustomerStationMapper());
		return list;
	}

	// 获取按客户查询的分页
	public List<AddressCustomerStationVO> getCustomerStationsPage() {
		String sql = " SELECT * FROM express_set_customer_station GROUP BY customerid LIMIT 0,10";
		return this.jdbcTemplate.query(sql, new AddressCustomerStationMapper());
	}

	// 根据客户id获取所有站点
	public List<AddressCustomerStationVO> getCustomerStationByCustomerId(Integer customerid) {
		String sql = "SELECT customerid,branchid FROM express_set_customer_station WHERE customerid=?";
		return this.jdbcTemplate.query(sql, new AddressCustomerStationBranchIDMapper(), customerid);
	}

	// 根据客户id分组获取所有记录
	public int getAllCountByCustomerId() {
		String sql = "SELECT COUNT(1) FROM (SELECT DISTINCT(customerid) FROM express_set_customer_station) AS t";
		return this.jdbcTemplate.queryForInt(sql);
	}

	// 根据客户id分组获取所有记录
	public int getAllCount() {
		String sql = "SELECT COUNT(1) FROM express_set_customer_station";
		return this.jdbcTemplate.queryForInt(sql);
	}

	// 根据id获取一条记录
	public AddressCustomerStationVO getCustomerStationByid(Long id) {
		String sql = "select * from express_set_customer_station where id=?";
		return this.jdbcTemplate.queryForObject(sql, new AddressCustomerStationMapper(), id);
	}

	// 根据客户id获取一条记录
	public List<AddressCustomerStationVO> getCustomerStationByCustomerid(Long customerid) {
		String sql = "select * from express_set_customer_station where customerid=?";
		return this.jdbcTemplate.query(sql, new AddressCustomerStationMapper(), customerid);
	}

	// 插入一条记录
	public void create(String customerName, String stationName, Long userid, String userName, String dateTime) {
		String sql = "INSERT INTO express_set_customer_station (customerid,branchid,creatorid,creatorname,createtime) VALUES (?,?,?,?,?)";
		this.jdbcTemplate.update(sql, customerName, stationName, userid, userName, dateTime);
	}

	// 根据客户id删除记录
	public void delByCustomerId(Long customerId) {
		String sql = "delete from express_set_customer_station where customerid=?";
		this.jdbcTemplate.update(sql, customerId);
	}
}

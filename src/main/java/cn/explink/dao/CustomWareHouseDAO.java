package cn.explink.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import cn.explink.domain.CustomWareHouse;
import cn.explink.util.Page;

@Component
public class CustomWareHouseDAO {

	private final class CustomWarHouseRowMapper implements RowMapper<CustomWareHouse> {
		@Override
		public CustomWareHouse mapRow(ResultSet rs, int rowNum) throws SQLException {
			CustomWareHouse customWarHouse = new CustomWareHouse();
			customWarHouse.setWarehouseid(rs.getLong("warehouseid"));
			customWarHouse.setCustomerwarehouse(rs.getString("customerwarehouse"));
			customWarHouse.setWarehouseremark(rs.getString("warehouseremark"));
			customWarHouse.setCustomerid(rs.getLong("customerid"));
			customWarHouse.setIfeffectflag(rs.getInt("ifeffectflag"));
			customWarHouse.setWarehouse_no(rs.getString("warehouse_no"));
			return customWarHouse;
		}
	}

	@Autowired
	private JdbcTemplate jdbcTemplate;
	private int w;

	public List<CustomWareHouse> getAllCustomWareHouse() {
		String sql = "select * from express_set_customer_warehouse";
		return jdbcTemplate.query(sql, new CustomWarHouseRowMapper());
	}

	public CustomWareHouse getCustomWareHouseByHousename(String housename, long customerid) {
		try {
			String sql = "select * from express_set_customer_warehouse where  customerid=? and customerwarehouse=?";
			return jdbcTemplate.queryForObject(sql, new CustomWarHouseRowMapper(), customerid, housename);
		} catch (DataAccessException e) {
			// TODO Auto-generated catch block
			return null;
		}
	}

	public CustomWareHouse getCustomWareHouseByHousecode(String warehouse_no, long customerid) {
		try {
			String sql = "select * from express_set_customer_warehouse where  customerid=? and warehouse_no=?";
			return jdbcTemplate.queryForObject(sql, new CustomWarHouseRowMapper(), customerid, warehouse_no);
		} catch (DataAccessException e) {
			// TODO Auto-generated catch block
			return null;
		}
	}

	public List<CustomWareHouse> getCustomWareHouseByCustomerid(long customerid) {
		String sql = "select * from express_set_customer_warehouse where  customerid=? and ifeffectflag=1";
		return jdbcTemplate.query(sql, new CustomWarHouseRowMapper(), customerid);
	}

	public List<CustomWareHouse> getCustomWareHouseByCustomerids(String customerids) {
		String sql = "select * from express_set_customer_warehouse where  customerid in(0" + customerids + ") and ifeffectflag=1";
		return jdbcTemplate.query(sql, new CustomWarHouseRowMapper());
	}

	public List<CustomWareHouse> getWarehouseByCustomerid(String customerwarehouse, long customerid) {
		List<CustomWareHouse> warehouseList = jdbcTemplate.query("SELECT * from express_set_customer_warehouse where customerwarehouse=? and customerid=?", new CustomWarHouseRowMapper(),
				customerwarehouse, customerid);
		return warehouseList;
	}

	public List<CustomWareHouse> getCustomerWarehouse(String customerwarehouse) {
		List<CustomWareHouse> warehouseList = jdbcTemplate.query("SELECT * from express_set_customer_warehouse where customerwarehouse=?", new CustomWarHouseRowMapper(), customerwarehouse);
		return warehouseList;
	}

	public List<CustomWareHouse> getCustomerWarehouseByCode(String warehouse_no, long customerid, long warehouseid) {
		String sql = "SELECT * from express_set_customer_warehouse where warehouse_no=? and customerid=? ";
		if (warehouseid != 0) {
			sql += " and warehouseid <>" + warehouseid;
		}
		List<CustomWareHouse> warehouseList = jdbcTemplate.query(sql, new CustomWarHouseRowMapper(), warehouse_no, customerid);
		return warehouseList;
	}

	public List<CustomWareHouse> getWarehouseAll() {

		return jdbcTemplate.query("SELECT * from express_set_customer_warehouse", new CustomWarHouseRowMapper());
	}

	public List<CustomWareHouse> getWarehouseAllByCustomid(long customid) {

		return jdbcTemplate.query("SELECT * from express_set_customer_warehouse where customerid=?", new CustomWarHouseRowMapper(), customid);
	}

	public List<CustomWareHouse> getCustomerid(long customerid) {
		List<CustomWareHouse> warehouseList = jdbcTemplate.query("SELECT * from express_set_customer_warehouse where ifeffectflag=1 and customerid= ?", new CustomWarHouseRowMapper(), customerid);
		return warehouseList;
	}

	public void creCustomer(CustomWareHouse warehouse) {
		jdbcTemplate.update("insert into express_set_customer_warehouse(warehouseid,customerid,customerwarehouse,warehouseremark,ifeffectflag,warehouse_no) values(?,?,?,?,?,?)",
				warehouse.getWarehouseid(), warehouse.getCustomerid(), warehouse.getCustomerwarehouse(), warehouse.getWarehouseremark(), warehouse.getIfeffectflag(), warehouse.getWarehouse_no());
	}

	public long creCustomerGetId(final long customerid, final String customerwarehouse) {
		KeyHolder key = new GeneratedKeyHolder();
		jdbcTemplate.update(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(java.sql.Connection con) throws SQLException {
				PreparedStatement ps = null;
				ps = con.prepareStatement("insert into express_set_customer_warehouse(customerid,customerwarehouse,ifeffectflag) values(?,?,1)", new String[] { "warehouseid" });
				ps.setLong(1, customerid);
				ps.setString(2, customerwarehouse);
				return ps;
			}
		}, key);
		return key.getKey().longValue();
	}

	public long creCustomerGetId(final long customerid, final String customerwarehouse, final String warehouse_no) {
		KeyHolder key = new GeneratedKeyHolder();
		jdbcTemplate.update(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(java.sql.Connection con) throws SQLException {
				PreparedStatement ps = null;
				ps = con.prepareStatement("insert into express_set_customer_warehouse(customerid,customerwarehouse,warehouse_no,ifeffectflag) values(?,?,?,1)", new String[] { "warehouseid" });
				ps.setLong(1, customerid);
				ps.setString(2, customerwarehouse);
				ps.setString(3, warehouse_no);
				return ps;
			}
		}, key);
		return key.getKey().longValue();
	}

	public CustomWareHouse getWarehouseId(long warehouseid) {
		try {
			return jdbcTemplate.queryForObject("select * from express_set_customer_warehouse where warehouseid = ?", new CustomWarHouseRowMapper(), warehouseid);
		} catch (DataAccessException e) {
			// TODO Auto-generated catch block
			return null;
		}

	}

	public void getUpdateWarehouse(final CustomWareHouse warehouse) {

		jdbcTemplate.update("update express_set_customer_warehouse set customerid=?,customerwarehouse=?,warehouseremark=?,warehouse_no=? where warehouseid=?", new PreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				ps.setLong(1, warehouse.getCustomerid());
				ps.setString(2, warehouse.getCustomerwarehouse());
				ps.setString(3, warehouse.getWarehouseremark());
				ps.setString(4, warehouse.getWarehouse_no());
				ps.setLong(5, warehouse.getWarehouseid());
			}
		});
	}

	public void getDelCustomerWarehouse(long warehouseid) {
		jdbcTemplate.update("update express_set_customer_warehouse set ifeffectflag=(ifeffectflag+1)%2 where warehouseid=?", warehouseid);
	}

	private String getwarehouseByPageWhereSql(String sql, long customerid) {
		sql += " where cw.ifeffectflag is not null ";
		if (customerid > 0) {
			sql += " and cw.customerid=" + customerid;
			return sql;
		} else {
			return sql;
		}

	}

	public List<CustomWareHouse> getWarehouseByPage(long page, long customerid) {
		String sql = "select cw.* from express_set_customer_warehouse cw RIGHT JOIN (SELECT customerid,ifeffectflag FROM  express_set_customer_info WHERE ifeffectflag=1 ) ci ON ci.customerid=cw.customerid ";
		sql = this.getwarehouseByPageWhereSql(sql, customerid);
		sql += " order by cw.ifeffectflag desc limit " + (page - 1) * Page.ONE_PAGE_NUMBER + " ," + Page.ONE_PAGE_NUMBER;
		List<CustomWareHouse> warehouselist = jdbcTemplate.query(sql, new CustomWarHouseRowMapper());
		return warehouselist;
	}

	public long getWarehouseCount(long customerid) {
		String sql = "select count(1) from express_set_customer_warehouse cw RIGHT JOIN (SELECT customerid,ifeffectflag FROM  express_set_customer_info WHERE ifeffectflag=1 ) ci ON ci.customerid=cw.customerid ";
		sql = this.getwarehouseByPageWhereSql(sql, customerid);
		return jdbcTemplate.queryForInt(sql);
	}

	public List<CustomWareHouse> getWarehouseByIfeffectflag() {
		return jdbcTemplate.query("SELECT * from express_set_customer_warehouse where ifeffectflag =1", new CustomWarHouseRowMapper());
	}

	public void delWarehouse(long customerid) {
		jdbcTemplate.update("update express_set_customer_warehouse set ifeffectflag=(ifeffectflag+1)%2 where customerid=?", customerid);
	}

	// 根据customerid和仓库编号查询 warehouseid
	public CustomWareHouse getCustomWareHouseByNo(String warehouse_no, String customerid) {
		CustomWareHouse cc = null;
		try {
			String sql = "select * from express_set_customer_warehouse where  customerid=? and warehouse_no=? limit 0,1 ";
			cc = jdbcTemplate.queryForObject(sql, new CustomWarHouseRowMapper(), customerid, warehouse_no);
		} catch (DataAccessException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
		}
		return cc;
	}

	// 根据customerid和仓库编号查询 warehouseid
	public CustomWareHouse getCustomWareHouseByName(String warehouse_name, String customerid) {
		CustomWareHouse cc = null;
		try {
			String sql = "select * from express_set_customer_warehouse where  customerid=? and customerwarehouse=? limit 0,1 ";
			cc = jdbcTemplate.queryForObject(sql, new CustomWarHouseRowMapper(), customerid, warehouse_name);
		} catch (DataAccessException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
		}
		return cc;
	}

	// 是否存在仓库
	public boolean isExistsWarehouFlag(String warehouse_no, String customer_id) {
		boolean flag = false;

		if (warehouse_no != null && !"".equals(warehouse_no)) {
			int counts = 0;
			try {
				String sql = "select count(1) from express_set_customer_warehouse where ifeffectflag=1 and customerid in (" + customer_id + ") and warehouse_no=? ";
				counts = jdbcTemplate.queryForInt(sql, warehouse_no);
			} catch (DataAccessException e) {
				// TODO Auto-generated catch block
				// e.printStackTrace();
			}
			if (counts > 0) {
				flag = true;
			}
		}
		return flag;
	}

	public List<CustomWareHouse> getWareHouseByHousename(String housename) {
		String sql = "select * from express_set_customer_warehouse where ifeffectflag=1 and customerwarehouse=?";
		return jdbcTemplate.query(sql, new CustomWarHouseRowMapper(), housename);
	}

	public Map<String, Long> getCustomWareHouseIdMapByCustomerid(long customerId) {
		List<CustomWareHouse> customWareHouseList = getCustomWareHouseByCustomerid(customerId);
		Map<String, Long> customWareHouseIdMap = new HashMap<String, Long>();
		if (customWareHouseList != null) {
			for (CustomWareHouse wareHouse : customWareHouseList) {
				customWareHouseIdMap.put(wareHouse.getCustomerwarehouse(), wareHouse.getWarehouseid());
			}
		}
		return customWareHouseIdMap;
	}

}

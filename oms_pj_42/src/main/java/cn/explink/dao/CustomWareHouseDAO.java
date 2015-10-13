package cn.explink.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
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
			return customWarHouse;
		}
	}

	@Autowired
	private JdbcTemplate jdbcTemplate;

	// private int w;

	public CustomWareHouse getCustomWareHouseByHousename(String housename, long customerid) {
		try {
			String sql = "select * from express_set_customer_warehouse where  customerid=? and customerwarehouse=?";
			return jdbcTemplate.queryForObject(sql, new CustomWarHouseRowMapper(), customerid, housename);
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	public List<CustomWareHouse> getCustomWareHouseByCustomerid(long customerid) {
		String sql = "select * from express_set_customer_warehouse where  customerid=?";
		return jdbcTemplate.query(sql, new CustomWarHouseRowMapper(), customerid);
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

	public List<CustomWareHouse> getWarehouseAll() {

		return jdbcTemplate.query("SELECT * from express_set_customer_warehouse", new CustomWarHouseRowMapper());
	}

	public List<CustomWareHouse> getCustomerid(long customerid) {
		List<CustomWareHouse> warehouseList = jdbcTemplate.query("SELECT * from express_set_customer_warehouse where ifeffectflag=1 and customerid= ?", new CustomWarHouseRowMapper(), customerid);
		return warehouseList;
	}

	public void creCustomer(CustomWareHouse warehouse) {
		jdbcTemplate.update("insert into express_set_customer_warehouse(warehouseid,customerid,customerwarehouse,warehouseremark,ifeffectflag) values(?,?,?,?,?)", warehouse.getWarehouseid(),
				warehouse.getCustomerid(), warehouse.getCustomerwarehouse(), warehouse.getWarehouseremark(), warehouse.getIfeffectflag());
	}

	public CustomWareHouse getWarehouseId(long warehouseid) {
		return jdbcTemplate.queryForObject("select * from express_set_customer_warehouse where warehouseid = ?", new CustomWarHouseRowMapper(), warehouseid);

	}

	public void getUpdateWarehouse(final CustomWareHouse warehouse) {

		jdbcTemplate.update("update express_set_customer_warehouse set customerid=?,customerwarehouse=?,warehouseremark=? where warehouseid=?", new PreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				ps.setLong(1, warehouse.getCustomerid());
				ps.setString(2, warehouse.getCustomerwarehouse());
				ps.setString(3, warehouse.getWarehouseremark());
				ps.setLong(4, warehouse.getWarehouseid());
			}
		});
	}

	public void getDelCustomerWarehouse(long warehouseid) {
		jdbcTemplate.update("update express_set_customer_warehouse set ifeffectflag=(ifeffectflag+1)%2 where warehouseid=" + warehouseid);
	}

	private String getwarehouseByPageWhereSql(String sql, long customerid) {
		if (customerid > 0) {
			sql += " where customerid=" + customerid;
			return sql;
		} else {
			return sql;
		}

	}

	public List<CustomWareHouse> getWarehouseByPage(long page, long customerid) {
		String sql = "select * from express_set_customer_warehouse";
		sql = this.getwarehouseByPageWhereSql(sql, customerid);
		sql += " order by ifeffectflag desc limit " + (page - 1) * Page.ONE_PAGE_NUMBER + " ," + Page.ONE_PAGE_NUMBER;
		List<CustomWareHouse> warehouselist = jdbcTemplate.query(sql, new CustomWarHouseRowMapper());
		return warehouselist;
	}

	public long getWarehouseCount(long customerid) {
		String sql = "select count(1) from express_set_customer_warehouse ";
		sql = this.getwarehouseByPageWhereSql(sql, customerid);
		return jdbcTemplate.queryForInt(sql);
	}

}

package cn.explink.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import cn.explink.domain.CustomWarHouse;
import cn.explink.util.Page;

@Component
public class CustomWarHouseDAO {

	private final class CustomWarHouseRowMapper implements RowMapper<CustomWarHouse> {
		@Override
		public CustomWarHouse mapRow(ResultSet rs, int rowNum) throws SQLException {
			CustomWarHouse customWarHouse = new CustomWarHouse();
			customWarHouse.setWarhouseid(rs.getLong("warehouseid"));
			customWarHouse.setCustomerwarehouse(rs.getString("customerwarehouse"));
			customWarHouse.setWarehouseremark(rs.getString("warehouseremark"));
			customWarHouse.setCustomerid(rs.getLong("customerid"));
			customWarHouse.setIfeffectflag(rs.getInt("ifeffectflag") == 1);
			return customWarHouse;
		}
	}

	@Autowired
	private JdbcTemplate jdbcTemplate;
	private int w;

	public CustomWarHouse getCustomWarHouseByHousename(String housename, long customerid) {
		String sql = "select * from express_set_customer_warehouse where ifeffectflag=1 and customerid=? and customerwarhouse=?";
		return jdbcTemplate.queryForObject(sql, new CustomWarHouseRowMapper(), customerid, housename);
	}

	public List<CustomWarHouse> getCustomWarHouseByCustomerid(long customerid) {
		String sql = "select * from express_set_customer_warehouse where ifeffectflag=1 and customerid=?";
		return jdbcTemplate.query(sql, new CustomWarHouseRowMapper(), customerid);
	}

	public List<CustomWarHouse> getWarehouseByCustomerid(String customerwarehouse, long customerid) {
		List<CustomWarHouse> warehouseList = jdbcTemplate.query("SELECT * from express_set_customer_warehouse where customerwarehouse=? and customerid=?", new CustomWarHouseRowMapper(),
				customerwarehouse, customerid);
		return warehouseList;
	}

	public List<CustomWarHouse> getCustomerWarehouse(String customerwarehouse) {
		List<CustomWarHouse> warehouseList = jdbcTemplate.query("SELECT * from express_set_customer_warehouse where customerwarehouse=?", new CustomWarHouseRowMapper(), customerwarehouse);
		return warehouseList;
	}

	public List<CustomWarHouse> getWarehouseAll() {

		return jdbcTemplate.query("SELECT * from express_set_customer_warehouse", new CustomWarHouseRowMapper());
	}

	public List<CustomWarHouse> getCustomerid(long customerid) {
		List<CustomWarHouse> warehouseList = jdbcTemplate.query("SELECT * from express_set_customer_warehouse where ifeffectflag=1 and customerid= ?", new CustomWarHouseRowMapper(), customerid);
		return warehouseList;
	}

	public void creCustomer(CustomWarHouse warehouse) {
		jdbcTemplate.update("insert into express_set_customer_warehouse(warehouseid,customerid,customerwarehouse,warehouseremark,ifeffectflag) values(?,?,?,?,?)", warehouse.getWarhouseid(),
				warehouse.getCustomerid(), warehouse.getCustomerwarehouse(), warehouse.getWarehouseremark(), w = warehouse.isIfeffectflag() ? 1 : 0);
	}

	public CustomWarHouse getWarehouseId(long warehouseid) {
		return jdbcTemplate.queryForObject("select * from express_set_customer_warehouse where ifeffectflag=1 and warehouseid = ?", new CustomWarHouseRowMapper(), warehouseid);

	}

	public void getUpdateWarehouse(final CustomWarHouse warehouse) {

		jdbcTemplate.update("update express_set_customer_warehouse set customerid=?,customerwarehouse=?,warehouseremark=? where warehouseid=?", new PreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				ps.setLong(1, warehouse.getCustomerid());
				ps.setString(2, warehouse.getCustomerwarehouse());
				ps.setString(3, warehouse.getWarehouseremark());
				ps.setLong(4, warehouse.getWarhouseid());
			}
		});
	}

	public void getDelCustomerWarehouse(long warehouseid) {
		jdbcTemplate.update("update express_set_customer_warehouse set ifeffectflag=0 where warehouseid=" + warehouseid);
	}

	private String getwarehouseByPageWhereSql(String sql, long customerid) {
		if (customerid > 0) {
			sql += " and customerid=" + customerid;
			return sql;
		} else {
			return sql;
		}

	}

	public List<CustomWarHouse> getWarehouseByPage(long page, long customerid) {
		String sql = "select * from express_set_customer_warehouse where ifeffectflag=1 ";
		sql = this.getwarehouseByPageWhereSql(sql, customerid);
		sql += " limit " + (page - 1) * Page.ONE_PAGE_NUMBER + " ," + Page.ONE_PAGE_NUMBER;
		List<CustomWarHouse> warehouselist = jdbcTemplate.query(sql, new CustomWarHouseRowMapper());
		return warehouselist;
	}

	public long getWarehouseCount(long customerid) {
		String sql = "select count(1) from express_set_customer_warehouse where ifeffectflag=1 ";
		sql = this.getwarehouseByPageWhereSql(sql, customerid);
		return jdbcTemplate.queryForInt(sql);
	}

}

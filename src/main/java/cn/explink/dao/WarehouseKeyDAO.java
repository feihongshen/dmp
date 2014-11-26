package cn.explink.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import cn.explink.domain.WarehouseKey;
import cn.explink.util.Page;

@Component
public class WarehouseKeyDAO {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	private final class WarehouseKeyMapper implements RowMapper<WarehouseKey> {
		@Override
		public WarehouseKey mapRow(ResultSet rs, int rowNum) throws SQLException {
			WarehouseKey warehouseKey = new WarehouseKey();
			warehouseKey.setId(rs.getLong("id"));
			warehouseKey.setIfeffectflag(rs.getLong("ifeffectflag"));
			warehouseKey.setKeyname(rs.getString("keyname"));
			warehouseKey.setTargetcarwarehouseid(rs.getLong("targetcarwarehouseid"));
			return warehouseKey;
		}
	}

	public long getWarehouseKeyConut(long targetcarwarehouseid) {
		String sql = "SELECT COUNT(1) FROM express_set_warehouse_key ";
		sql = this.getWarehouseKeyByPageWhereSql(sql, targetcarwarehouseid);
		return jdbcTemplate.queryForLong(sql);
	}

	public List<WarehouseKey> getWarehouseKeyByPage(long page, long targetcarwarehouseid) {
		String sql = "SELECT * from express_set_warehouse_key ";
		sql = this.getWarehouseKeyByPageWhereSql(sql, targetcarwarehouseid);
		if (page > 1) {
			sql += " limit " + (page - 1) * Page.ONE_PAGE_NUMBER + "," + Page.ONE_PAGE_NUMBER;
		}
		List<WarehouseKey> warehouseKeys = jdbcTemplate.query(sql, new WarehouseKeyMapper());
		return warehouseKeys;
	}

	public List<WarehouseKey> getWarehouseKeyByKeyname(String keyname) {
		String sql = "SELECT * from express_set_warehouse_key where keyname=? and ifeffectflag=1";
		List<WarehouseKey> warehouseKeys = jdbcTemplate.query(sql, new WarehouseKeyMapper(), keyname);
		return warehouseKeys;
	}

	public List<WarehouseKey> getWarehouseKeyByKeynameAndTargetcarwarehouseid(String keyname, long targetcarwarehouseid) {
		String sql = "SELECT * from express_set_warehouse_key where keyname=? and targetcarwarehouseid=?  and ifeffectflag=1";
		List<WarehouseKey> warehouseKeys = jdbcTemplate.query(sql, new WarehouseKeyMapper(), keyname, targetcarwarehouseid);
		return warehouseKeys;
	}

	public void creWarehouseKey(long targetcarwarehouseid, String keyname) {
		String sql = "insert into express_set_warehouse_key(targetcarwarehouseid,keyname) values(?,?)";
		jdbcTemplate.update(sql, targetcarwarehouseid, keyname);
	}

	public String getWarehouseKeyByPageWhereSql(String sql, long targetcarwarehouseid) {
		if (targetcarwarehouseid > 0) {
			sql += "where targetcarwarehouseid=" + targetcarwarehouseid + " and ifeffectflag=1";
		}
		return sql;
	}

	public void delWarehouseKey(long id) {
		jdbcTemplate.update("UPDATE express_set_warehouse_key SET ifeffectflag=(ifeffectflag+1)%2 WHERE id=?", id);
	}

	public WarehouseKey getWarehouseKeyByid(long id) {
		WarehouseKey warehouseKey = jdbcTemplate.queryForObject("SELECT * FROM express_set_warehouse_key where ifeffectflag=1 and id=?", new WarehouseKeyMapper(), id);
		return warehouseKey;
	}

	public void saveWarehouseKeyByid(long id, long targetcarwarehouseid, String keyname) {
		String sql = "UPDATE express_set_warehouse_key SET targetcarwarehouseid=?,keyname=? WHERE id=? and ifeffectflag=1";
		jdbcTemplate.update(sql, targetcarwarehouseid, keyname, id);
	}

}

package cn.explink.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import cn.explink.domain.AccountArea;
import cn.explink.util.Page;

@Component
public class AccountAreaDAO {

	private final class AccountAreaRowMapper implements RowMapper<AccountArea> {
		@Override
		public AccountArea mapRow(ResultSet rs, int rowNum) throws SQLException {
			AccountArea accountArea = new AccountArea();
			accountArea.setAreaid(rs.getLong("areaid"));
			accountArea.setAreaname(rs.getString("areaname"));
			accountArea.setArearemark(rs.getString("arearemark"));
			accountArea.setCustomerid(rs.getLong("customerid"));
			accountArea.setEffectFlag(rs.getInt("isEffectFlag") == 1);
			return accountArea;
		}
	}

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public List<AccountArea> getAccountAreaByCustomerid(long customerid) {
		String sql = "select * from express_set_account_area where customerid=?";
		return jdbcTemplate.query(sql, new AccountAreaRowMapper(), customerid);
	}

	public List<AccountArea> getAccountAreaByPage(long page, long customerid) {
		String sql = "select * from express_set_account_area ";
		sql = this.getAccountAreaByPageWhereSql(sql, customerid);
		sql += " order by isEffectFlag desc limit " + (page - 1) * Page.ONE_PAGE_NUMBER + " ," + Page.ONE_PAGE_NUMBER;
		return jdbcTemplate.query(sql, new AccountAreaRowMapper());
	}

	public List<AccountArea> getAccountAreaByCustomeridAnd(long customerid, String areaname) {
		String sql = "SELECT * from express_set_account_area where customerid=? and areaname=? ";
		return jdbcTemplate.query(sql, new AccountAreaRowMapper(), customerid, areaname);
	}

	private String getAccountAreaByPageWhereSql(String sql, long customerid) {
		if (customerid >= 0) {
			sql += " where customerid=" + customerid;
		}
		return sql;
	}

	public long getAccountAreaCount(long customerid) {
		String sql = "SELECT count(1) from express_set_account_area";
		sql = this.getAccountAreaByPageWhereSql(sql, customerid);
		return jdbcTemplate.queryForLong(sql);
	}

	public void creAccountArea(long customerid, String areaname, String arearemark) {
		jdbcTemplate.update("insert into express_set_account_area (areaname,arearemark,customerid,isEffectFlag) " + "values(?,?,?,1 )", areaname, arearemark, customerid);
	}

	public AccountArea getAccountAreaById(long areaid) {
		String sql = "select * from express_set_account_area where  areaid=?";
		return jdbcTemplate.queryForObject(sql, new AccountAreaRowMapper(), areaid);
	}

	public void saveAccountArea(long customerid, String areaname, String arearemark, long areaid) {
		jdbcTemplate.update("update express_set_account_area set areaname=?,arearemark=?,customerid=? where areaid=? ", areaname, arearemark, customerid, areaid);

	}

	public void editAccountAreaIsEffectFlag(long areaid) {
		jdbcTemplate.update("update express_set_account_area set isEffectFlag=(isEffectFlag+1)%2 where areaid=? ", areaid);

	}

	public List<AccountArea> getAllAccountArea() {
		return jdbcTemplate.query("select * from express_set_account_area", new AccountAreaRowMapper());
	}

}

package cn.explink.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import cn.explink.domain.Common;
import cn.explink.util.Page;

@Component
public class CommonDAO {

	private final class CommonRowMapper implements RowMapper<Common> {
		@Override
		public Common mapRow(ResultSet rs, int rowNum) throws SQLException {
			Common common = new Common();
			common.setId(rs.getLong("id"));
			common.setCommonname(rs.getString("commonname"));
			common.setCommonnumber(rs.getString("commonnumber"));
			common.setOrderprefix(rs.getString("orderprefix"));
			common.setCommonstate(rs.getLong("commonstate"));
			common.setIsopenflag(rs.getLong("isopenflag"));
			return common;
		}
	}

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public List<Common> getAllCommon() {
		return jdbcTemplate.query("select * from express_set_common", new CommonRowMapper());
	}

	public String getCommonByPageWhereSql(String sql, String commonname, String commonnumber) {

		if (commonname.length() > 0 || commonnumber.length() > 0) {
			sql += " where ";
			StringBuffer str = new StringBuffer();
			if (commonname.length() > 0) {
				str.append(" and commonname like '%" + commonname + "%'");
			}
			if (commonnumber.length() > 0) {
				str.append(" and commonnumber = '" + commonnumber + "'");
			}
			sql += str.substring(4, str.length());
		}
		return sql;
	}

	public long getCommonCount(String commonname, String commonnumber) {
		String sql = "select count(1) from express_set_common";
		sql = this.getCommonByPageWhereSql(sql, commonname, commonnumber);
		return jdbcTemplate.queryForInt(sql);
	}

	public List<Common> getCommonByPage(long page, String commonname, String commonnumber) {
		String sql = "select * from express_set_common ";
		sql = this.getCommonByPageWhereSql(sql, commonname, commonnumber);
		sql += " order by commonstate desc limit " + (page - 1) * Page.ONE_PAGE_NUMBER + " ," + Page.ONE_PAGE_NUMBER;
		return jdbcTemplate.query(sql, new CommonRowMapper());
	}

	public void CreateCommon(String commonname, String commonnumber, String orderprefix) {
		jdbcTemplate.update("insert into express_set_common(commonname,commonnumber,orderprefix) values(?,?,?)", commonname, commonnumber, orderprefix);
	}

	public List<Common> getCommonByCommonname(String commonname) {
		return jdbcTemplate.query("select * from express_set_common where commonname = '" + commonname + "'", new CommonRowMapper());
	}

	public void saveCommon(String commonname, String commonnumber, String orderprefix, long id) {
		jdbcTemplate.update("update express_set_common set commonname=?,commonnumber=?,orderprefix=? where id =?", commonname, commonnumber, orderprefix, id);
	}

	public Common getCommonById(long id) {
		return jdbcTemplate.queryForObject("select * from express_set_common where id = " + id, new CommonRowMapper());
	}

	public void delCommon(long id) {
		jdbcTemplate.update("update express_set_common set commonstate=(commonstate+1)%2 where id=" + id);
	}

	public List<Common> getCommonByCommonnumber(String commonnumber) {
		return jdbcTemplate.query("select * from express_set_common where commonnumber = '" + commonnumber + "'", new CommonRowMapper());
	}

}

package cn.explink.print.template;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import cn.explink.util.Page;

@Component
public class PrintTemplateDAO {
	@Autowired
	JdbcTemplate jdbcTemplate;

	private final class PrintTemplateRowMapper implements RowMapper<PrintTemplate> {
		@Override
		public PrintTemplate mapRow(ResultSet rs, int rowNum) throws SQLException {
			PrintTemplate printTemplate = new PrintTemplate();
			printTemplate.setId(rs.getLong("id"));
			printTemplate.setDetail(rs.getString("detail"));
			printTemplate.setName(rs.getString("name"));
			printTemplate.setShownum(rs.getLong("shownum"));
			printTemplate.setOpertatetype(rs.getLong("opertatetype"));
			printTemplate.setCustomname(rs.getString("customname"));
			printTemplate.setTemplatetype(rs.getLong("templatetype"));
			return printTemplate;
		}
	}

	public List<PrintTemplate> getALLPrintTemplate() {
		String sql = "select * from express_set_print_template";
		return this.jdbcTemplate.query(sql, new PrintTemplateRowMapper());
	}

	public List<PrintTemplate> getPrintTemplateByOpreatetype(String opertatetypes) {
		String sql = "select * from express_set_print_template where opertatetype in(" + opertatetypes + ")";
		return this.jdbcTemplate.query(sql, new PrintTemplateRowMapper());
	}

	public PrintTemplate getPrintTemplate(long id) {
		return this.jdbcTemplate.queryForObject("select * from express_set_print_template where id=?", new PrintTemplateRowMapper(), id);
	}

	public long crePrintTemplate(final PrintTemplate pt) {
		KeyHolder key = new GeneratedKeyHolder();
		this.jdbcTemplate.update(new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(java.sql.Connection con) throws SQLException {
				PreparedStatement ps = null;
				ps = con.prepareStatement("insert into express_set_print_template(name,detail,shownum,opertatetype,customname,templatetype) values(?,?,?,?,?,?)", new String[] { "id" });
				ps.setString(1, pt.getName());
				ps.setString(2, pt.getDetail());
				ps.setLong(3, pt.getShownum());
				ps.setLong(4, pt.getOpertatetype());
				ps.setString(5, pt.getCustomname());
				ps.setLong(6, pt.getTemplatetype());
				return ps;
			}
		}, key);
		pt.setId(key.getKey().longValue());
		return key.getKey().longValue();
	}

	public List<PrintTemplate> getPrintTemplateByWhere(String name, String detail, long shownum, long opertatetype, String customname) {
		return this.jdbcTemplate.query("select * from express_set_print_template where name=? and detail=? and shownum=?  and opertatetype=? and customname=?", new PrintTemplateRowMapper(), name,
				detail, shownum, opertatetype, customname);
	}

	public List<PrintTemplate> getPrintTemplateByPage(long page, String name, String detail, long opertatetype) {
		String sql = "select * from express_set_print_template";
		sql = this.getPrintTemplateCwbOrderByPageWhereSql(sql, name, detail, opertatetype);
		sql += " limit " + ((page - 1) * Page.ONE_PAGE_NUMBER) + " ," + Page.ONE_PAGE_NUMBER;
		List<PrintTemplate> printTemplateList = this.jdbcTemplate.query(sql, new PrintTemplateRowMapper());
		return printTemplateList;
	}

	public long getPrintTemplateCount(String name, String detail, long opertatetype) {
		String sql = "select count(1) from express_set_print_template";
		sql = this.getPrintTemplateCwbOrderByPageWhereSql(sql, name, detail, opertatetype);
		return this.jdbcTemplate.queryForInt(sql);
	}

	private String getPrintTemplateCwbOrderByPageWhereSql(String sql, String name, String detail, long opertatetype) {
		if ((name.length() > 0) || (detail.length() > 0) || (opertatetype > 0)) {
			StringBuffer w = new StringBuffer();
			sql += " where ";
			if (name.length() > 0) {
				w.append(" and name = '" + name + "'");
			}
			if (detail.length() > 0) {
				w.append(" and detail = '" + detail + "'");
			}
			if (opertatetype > 0) {
				w.append(" and opertatetype = " + opertatetype);
			}
			sql += w.substring(4, w.length());
		}
		return sql;
	}

	public void savePrintTemplateById(String name, String detail, int id, long shownum, long opertatetype, String customname, long templatetype) {
		String sql = "update express_set_print_template set name='" + name + "',detail='" + detail + "',shownum=" + shownum + ",opertatetype=" + opertatetype + ",customname='" + customname
				+ "',templatetype=" + templatetype + " where id=" + id;
		this.jdbcTemplate.update(sql);
	}

	public void delPrintTemplateById(int id) {
		String sql = "DELETE FROM `express_set_print_template` where id=" + id;
		this.jdbcTemplate.update(sql);
	}
	
	
	public PrintTemplate getPrintTemplateByType(long type) {
		
		try {
			return this.jdbcTemplate.queryForObject("select * from express_set_print_template where  templatetype =? ORDER BY id DESC LIMIT 0,1", new PrintTemplateRowMapper(),type);
		} catch (DataAccessException e) {
			// TODO Auto-generated catch block
			return null;
		}
	
	}
}

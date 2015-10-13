package cn.explink.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.stereotype.Component;

import cn.explink.domain.Exportmould;
import cn.explink.domain.SetExportField;
import cn.explink.domain.User;
import cn.explink.service.ExplinkUserDetail;
import cn.explink.util.Page;

@Component
public class ExportmouldDAO {

	private final class ExportmouldRowMapper implements RowMapper<Exportmould> {
		@Override
		public Exportmould mapRow(ResultSet rs, int rowNum) throws SQLException {
			Exportmould exportmould = new Exportmould();
			exportmould.setId(rs.getLong("id"));
			exportmould.setRolename(rs.getString("rolename"));
			exportmould.setRoleid(rs.getLong("roleid"));
			exportmould.setMouldname(rs.getString("mouldname"));
			exportmould.setMouldfieldids(rs.getString("mouldfieldids"));
			exportmould.setStatus(rs.getLong("status"));
			return exportmould;
		}
	}

	private final class SetExportFieldMapper implements RowMapper<SetExportField> {
		@Override
		public SetExportField mapRow(ResultSet rs, int rowNum) throws SQLException {
			SetExportField setExportField = new SetExportField();
			setExportField.setId(rs.getInt("id"));
			setExportField.setFieldname(rs.getString("fieldname"));
			setExportField.setExportstate(rs.getLong("exportstate"));
			setExportField.setFieldenglishname(rs.getString("fieldenglishname"));
			return setExportField;
		}
	}

	@Autowired
	private JdbcTemplate jdbcTemplate;
	@Autowired
	SecurityContextHolderStrategy securityContextHolderStrategy;

	private User getSessionUser() {
		ExplinkUserDetail userDetail = (ExplinkUserDetail) securityContextHolderStrategy.getContext().getAuthentication().getPrincipal();
		return userDetail.getUser();
	}

	public List<Exportmould> getExportmouldByPage(long page) {
		return jdbcTemplate.query("select * from express_ops_exportmould where status=1 order by id limit " + (page - 1) * Page.ONE_PAGE_NUMBER + " ," + Page.ONE_PAGE_NUMBER,
				new ExportmouldRowMapper());
	}

	public long getExportmouldCount() {
		String sql = "select count(1) from express_ops_exportmould where status=1";
		return jdbcTemplate.queryForInt(sql);
	}

	public void creExportmould(long roleid, String rolename, String mouldname, String mouldfieldids) {
		jdbcTemplate.update("insert into express_ops_exportmould(roleid,rolename,mouldname,mouldfieldids) values(?,?,?,?)", roleid, rolename, mouldname, mouldfieldids);
	}

	public void delExportmould(long id) {
		jdbcTemplate.update("update express_ops_exportmould set status=(status+1)%2 where id=" + id);
	}

	public Exportmould getExportmouldById(long id) {
		return jdbcTemplate.queryForObject("select * from express_ops_exportmould where  status =1 and id = " + id + " order by id", new ExportmouldRowMapper());
	}

	public void editExportmould(long roleid, String rolename, String mouldname, String mouldfieldids, long id) {
		jdbcTemplate.update("update express_ops_exportmould set roleid=?,rolename=?,mouldname=?,mouldfieldids=? where id =?", roleid, rolename, mouldname, mouldfieldids, id);
	}

	public List<Exportmould> getAllExportmouldByUser(long roleid) {
		return jdbcTemplate.query("select * from express_ops_exportmould where status =1 and roleid=? order by id", new ExportmouldRowMapper(), roleid);
	}

	public List<SetExportField> getSetExportFieldByStrs(String strs) {
		if ("0".equals(strs)) {
			return jdbcTemplate.query("select * from express_ops_setexportfield where exportstate=1 ", new SetExportFieldMapper());
		} else {
			return jdbcTemplate.query("select * from express_ops_setexportfield where id in (" + strs + ")", new SetExportFieldMapper());
		}

	}

	public List<Exportmould> getExportmouldBymouldname(String mouldname) {
		return jdbcTemplate.query("select * from express_ops_exportmould where status =1 and mouldname= '" + mouldname + "'", new ExportmouldRowMapper());
	}

	public List<Exportmould> getExportmouldByRoleidNoId(long roleid, long id) {
		return jdbcTemplate.query("select * from express_ops_exportmould where  status =1 and roleid = " + roleid + " and id <> " + id + " order by id", new ExportmouldRowMapper());
	}

}

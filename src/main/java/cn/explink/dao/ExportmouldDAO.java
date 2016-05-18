package cn.explink.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import com.pjbest.splitting.aspect.DataSource;
import com.pjbest.splitting.routing.DatabaseType;

import cn.explink.domain.Exportmould;
import cn.explink.domain.SetExportField;
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
			setExportField.setOrderlevel(rs.getLong("orderlevel"));
			setExportField.setExportdatatype(rs.getString("exportdatatype"));
			return setExportField;
		}
	}

	@Autowired
	private JdbcTemplate jdbcTemplate;

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
		jdbcTemplate.update("update express_ops_exportmould set status=(status+1)%2 where id=?", id);
	}

	public Exportmould getExportmouldById(long id) {
		return jdbcTemplate.queryForObject("select * from express_ops_exportmould where  status =1 and id =? ", new ExportmouldRowMapper(), id);
	}

	public void editExportmould(long roleid, String rolename, String mouldname, String mouldfieldids, long id) {
		jdbcTemplate.update("update express_ops_exportmould set roleid=?,rolename=?,mouldname=?,mouldfieldids=? where id =?", roleid, rolename, mouldname, mouldfieldids, id);
	}

	@DataSource(DatabaseType.REPLICA)
	public List<Exportmould> getAllExportmouldByUser(long roleid) {
		return jdbcTemplate.query("select * from express_ops_exportmould where status =1 and roleid=? order by id", new ExportmouldRowMapper(), roleid);
	}

	public List<SetExportField> getSetExportFieldByStrs(String strs) {
		if ("0".equals(strs)) {
			return jdbcTemplate.query("select * from express_ops_setexportfield where exportstate=1 order by orderlevel", new SetExportFieldMapper());
		} else {
			return jdbcTemplate.query("select * from express_ops_setexportfield where id in (" + strs + ") order by orderlevel", new SetExportFieldMapper());
		}

	}

	public List<Exportmould> getExportmouldBymouldname(String mouldname) {
		return jdbcTemplate.query("select * from express_ops_exportmould where status =1 and mouldname=?", new ExportmouldRowMapper(), mouldname);
	}

	public List<Exportmould> getExportmouldByRoleidNoId(long roleid, long id) {
		return jdbcTemplate.query("select * from express_ops_exportmould where  status =1 and roleid =? and id <>? order by id", new ExportmouldRowMapper(), roleid, id);
	}

	public SetExportField getSetExportFieldByid(long id) {
		try {
			return jdbcTemplate.queryForObject("select * from express_ops_setexportfield where id=? ", new SetExportFieldMapper(), id);
		} catch (DataAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	public SetExportField getSetExportFieldMax(long level) {
		try {
			return jdbcTemplate.queryForObject("select * from express_ops_setexportfield where orderlevel > ? order by orderlevel ASC limit 1 ", new SetExportFieldMapper(), level);
		} catch (DataAccessException e) {
			return null;
		}
	}

	public SetExportField getSetExportFieldMin(long level) {
		try {
			return jdbcTemplate.queryForObject("select * from express_ops_setexportfield where orderlevel < ? order by orderlevel DESC limit 1 ", new SetExportFieldMapper(), level);
		} catch (DataAccessException e) {
			// TODO Auto-generated catch block
			return null;
		}
	}

	public long updateLevel(long id, long level) {
		return jdbcTemplate.update("update express_ops_setexportfield set orderlevel=? where id=? ", level, id);
	}

}

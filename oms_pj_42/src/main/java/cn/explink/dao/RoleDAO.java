package cn.explink.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import cn.explink.domain.Role;

@Component
public class RoleDAO {

	private final class RoleRowMapper implements RowMapper<Role> {
		@Override
		public Role mapRow(ResultSet rs, int rowNum) throws SQLException {
			Role role = new Role();
			role.setRoleid(rs.getLong("roleid"));
			role.setRolename(rs.getString("rolename"));
			role.setType(rs.getInt("type"));
			return role;
		}
	}

	@Autowired
	JdbcTemplate jdbcTemplate;

	public List<Role> getRoles() {
		return jdbcTemplate.query("select * from express_set_role_new ", new RoleRowMapper());
	}

	public void creRole(final Role role) {
		jdbcTemplate.update("insert into express_set_role_new (rolename,type) " + "values(?,1)", role.getRolename());
	}

	public List<Role> getRolesByRolename(String rolename) {
		return jdbcTemplate.query("select * from express_set_role_new where rolename=? ", new RoleRowMapper(), rolename);
	}

	public Role getRolesByRoleid(long roleid) {
		return jdbcTemplate.queryForObject("select * from express_set_role_new where roleid=? ", new RoleRowMapper(), roleid);
	}

	public List<Long> getRoleAndMenuByRoleid(long roleid) {
		return jdbcTemplate.queryForList("select menuid from express_set_role_menu_new where roleid=? ", Long.class, roleid);
	}

	public void saveRoleAndMenu(List<Long> menuids, long roleid) {
		if (menuids != null && menuids.size() > 0) {
			StringBuffer sql = new StringBuffer("insert into express_set_role_menu_new (roleid,menuid) values ");
			for (Long menuid : menuids) {
				sql.append("(").append(roleid).append(",").append(menuid).append("),");
			}
			jdbcTemplate.update(sql.substring(0, sql.length() - 1));
		}
	}

	public void delRoleAndMenu(long roleid) {
		jdbcTemplate.update("delete from express_set_role_menu_new where roleid=" + roleid);
	}

	public void save(long roleid, String rolename) {
		jdbcTemplate.update("update express_set_role_new set rolename=? where roleid=?", rolename, roleid);
	}

}

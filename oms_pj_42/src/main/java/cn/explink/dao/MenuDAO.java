package cn.explink.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import cn.explink.domain.Menu;
import cn.explink.util.StringUtil;

@Component
public class MenuDAO {

	private final class MenuMapper implements RowMapper<Menu> {
		@Override
		public Menu mapRow(ResultSet rs, int rowNum) throws SQLException {
			Menu menu = new Menu();
			menu.setId(rs.getLong("menuid"));
			menu.setName(StringUtil.nullConvertToEmptyString(rs.getString("menuname")));
			menu.setUrl(StringUtil.nullConvertToEmptyString(rs.getString("menuurl")));
			menu.setPosition(StringUtil.nullConvertToEmptyString(rs.getString("menuno")));
			menu.setParentid(rs.getLong("parentno"));
			menu.setImage(rs.getString("menuimage"));
			menu.setMenulevel(rs.getString("menulevel"));
			menu.setMenuno(rs.getString("menuno"));
			return menu;
		}
	}

	@Autowired
	JdbcTemplate jdbcTemplate;

	// 根据用户名查询顶级菜单
	public List<Menu> getTopLvelMenuByUserRoleid(long roleid) throws Exception {
		StringBuffer sql = new StringBuffer(
				"select m.* from (select * from express_set_role_menu_new where roleid=? ) rm left join express_set_menu_info_new m on rm.menuid=m.menuid where m.parentno=0  order by menuno");
		List<Menu> list = jdbcTemplate.query(sql.toString(), new MenuMapper(), roleid);
		return list;
	}

	public List<Menu> getSecondLvelMenuByUserRoleid(long roleid, String menulevel) throws Exception {
		StringBuffer sql = new StringBuffer(
				"select m.* from (select * from express_set_role_menu_new where roleid=? ) rm left join express_set_menu_info_new m on rm.menuid=m.menuid where m.menulevel=?  order by menuno");
		return jdbcTemplate.query(sql.toString(), new MenuMapper(), roleid, menulevel);
	}

	public List<Menu> getSecondLvelMenuByUserRoleidToWelcome(long roleid, String menulevel) throws Exception {
		StringBuffer sql = new StringBuffer(
				"select m.* from (select * from express_set_role_menu_new where roleid=? ) rm left join express_set_menu_info_new m on rm.menuid=m.menuid where m.menulevel=? order by menuno limit 0,6");
		return jdbcTemplate.query(sql.toString(), new MenuMapper(), roleid, menulevel);
	}

	public List<Menu> getMenus() {
		return jdbcTemplate.query("select * from express_set_menu_info_new order by parentno,menuno", new MenuMapper());

	}

	/**
	 * 获取所有PDA的菜单列表
	 * 
	 * @return
	 */
	public List<Menu> getMenusByUserRoleidToPDA(long roleid) {
		return jdbcTemplate.query(
				"select m.* from (select * from express_set_role_menu_new where roleid=? ) rm left join express_set_menu_info_new m on rm.menuid=m.menuid where m.menulevel='p' order by menuno",
				new MenuMapper(), roleid);
	}

	public List<Menu> getPDAMenus() {
		return jdbcTemplate.query("SELECT * FROM express_set_menu_info_new WHERE menulevel ='p'", new MenuMapper());
	}

}

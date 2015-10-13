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

import cn.explink.domain.ProxyConf;
import cn.explink.util.Page;
import cn.explink.util.StringUtil;

@Component
public class ProxyConfDAO {

	private final class ProxyConfMapper implements RowMapper<ProxyConf> {
		@Override
		public ProxyConf mapRow(ResultSet rs, int rowNum) throws SQLException {
			ProxyConf proxyConf = new ProxyConf();
			proxyConf.setId(rs.getInt("id"));
			proxyConf.setPort(rs.getInt("port"));
			proxyConf.setType(rs.getInt("type"));
			proxyConf.setState(rs.getInt("state"));
			proxyConf.setIp(StringUtil.nullConvertToEmptyString(rs.getString("ip")));
			proxyConf.setWith(rs.getInt("with"));
			proxyConf.setIsnoDefault(rs.getInt("isnoDefault"));
			return proxyConf;
		}
	}

	@Autowired
	private JdbcTemplate jdbcTemplate;

	// 获取一条代理
	public ProxyConf getProxyById(int id) {
		try {
			return jdbcTemplate.queryForObject("SELECT * from express_set_proxy where id=?", new ProxyConfMapper(), id);
		} catch (Exception e) {
			return null;
		}

	}

	// 获取一条当前使用的代理
	public ProxyConf getProxyNowUse(int type) {
		try {
			return jdbcTemplate.queryForObject("SELECT * from express_set_proxy where type=? and state=1 and isnoDefault=0 limit 1", new ProxyConfMapper(), type);
		} catch (Exception e) {
			return null;
		}

	}

	// 随机获取获取一条代理
	public ProxyConf getProxyRandom(int type) {
		try {
			return jdbcTemplate.queryForObject("SELECT * from express_set_proxy where type=? and state=1 and isnoDefault=0 order by rand() limit 1", new ProxyConfMapper(), type);
		} catch (Exception e) {
			return null;
		}

	}

	// 随机获取获取一条默认代理
	public ProxyConf getDefualtProxy() {
		try {
			return jdbcTemplate.queryForObject("SELECT * from express_set_proxy where  isnoDefault=1 order by rand() limit 1", new ProxyConfMapper());
		} catch (Exception e) {
			return null;
		}

	}

	// 获取所有代理
	public List<ProxyConf> getProxyAll(long page, int state) {
		List<ProxyConf> proxyConfList = jdbcTemplate.query("SELECT * from express_set_proxy " + (state > -1 ? (" where state=" + state) : "") + " order by state desc limit " + (page - 1)
				* Page.ONE_PAGE_NUMBER + " ," + Page.ONE_PAGE_NUMBER, new ProxyConfMapper());
		return proxyConfList;
	}

	// 获取所有代理数量
	public long getProxyAllConut(int state) {
		long count = jdbcTemplate.queryForLong("SELECT count(1) from express_set_proxy " + (state > -1 ? (" where state=" + state) : ""));
		return count;
	}

	// 修改类型
	public void updateProxyType(int id, int type) {

		jdbcTemplate.update("update express_set_proxy set type=?" + " where id=?", type, id);

	}

	// 修改可用状态
	public void updateProxyState(int id, int state) {

		jdbcTemplate.update("update express_set_proxy set state=?,type=0" + " where id=? and  isnoDefault=0", state, id);

	}

	// 创建代理
	public void creProxyConf(final ProxyConf proxyConf) {
		jdbcTemplate.update("insert into express_set_proxy(`port`,`type`,`state`,`ip`,`with`,`isnoDefault`) values(?,?,?,?,?,?)", new PreparedStatementSetter() {

			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				// TODO Auto-generated method stub
				ps.setInt(1, proxyConf.getPort());
				ps.setInt(2, proxyConf.getType());
				ps.setInt(3, proxyConf.getState());
				ps.setString(4, proxyConf.getIp());
				ps.setInt(5, proxyConf.getWith());
				ps.setInt(6, proxyConf.getIsnoDefault());
			}
		});
	}

	// 删除
	public void delProxyState(int id) {
		jdbcTemplate.update("DELETE FROM express_set_proxy where id=?", id);
	}

	// 修改代理
	public void editProxyType(String ip, int port, int with, int id, int state, int isnoDefault) {
		String sql = "update express_set_proxy set `ip`= '" + ip + "' ,`port`=" + port + ",`with`=" + with + ",`state`=" + state + ",`isnoDefault`=" + isnoDefault + " where id = " + id;
		jdbcTemplate.update(sql);
	}

	// 根据ip获取代理
	public ProxyConf getProxyByIp(String ip) {
		try {
			return jdbcTemplate.queryForObject("SELECT * from express_set_proxy where ip=?", new ProxyConfMapper(), ip);
		} catch (Exception e) {
			return null;
		}

	}

}

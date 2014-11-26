package cn.explink.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import cn.explink.domain.Notify;
import cn.explink.util.Page;

@Component
public class NotifyDao {

	@Autowired
	JdbcTemplate jdbcTemplate;

	public List<Notify> getAllNotify(long page) {
		String sql = " select * from set_notify where state=1 order by istop desc,cretime desc";
		sql += " limit " + (page - 1) * Page.ONE_PAGE_NUMBER + "," + Page.ONE_PAGE_NUMBER;
		return jdbcTemplate.query(sql, new notifyMapper());
	}

	private class notifyMapper implements RowMapper<Notify> {
		@Override
		public Notify mapRow(ResultSet rs, int rowNum) throws SQLException {
			Notify nf = new Notify();
			nf.setContent(rs.getString("content"));
			nf.setCretime(rs.getString("cretime"));
			nf.setCreuserid(rs.getLong("creuserid"));
			nf.setEdittime(rs.getString("edittime"));
			nf.setEdituserid(rs.getLong("edituserid"));
			nf.setId(rs.getInt("id"));
			nf.setIstop(rs.getInt("istop"));
			nf.setState(rs.getInt("state"));
			nf.setTitle(rs.getString("title"));
			nf.setType(rs.getInt("type"));
			return nf;
		}

	}

	/**
	 * 得到总数
	 * 
	 * @return
	 */
	public long getAllNotifyCount() {
		String sql = " select count(1) from  set_notify where state=1";
		return jdbcTemplate.queryForLong(sql);
	}

	public void creNotify(long userid, String title, long type, String content, String nowtime) {
		String sql = " INSERT INTO `set_notify`(`title`,`type`,`content`,`creuserid`,`cretime`) VALUES (?,?,?,?,?); ";
		jdbcTemplate.update(sql, title, type, content, userid, nowtime);
	}

	/**
	 * 根据 ids 删除
	 * 
	 * @param ids
	 */
	public void delByIds(String ids) {
		String sql = " update set_notify set state=0 where id in(" + ids + ")";
		jdbcTemplate.update(sql);
	}

	public Notify getNotifyById(long id) {
		Notify nf = new Notify();
		String sql = "select *  from set_notify where id=" + id;
		try {
			nf = jdbcTemplate.queryForObject(sql, new notifyMapper());
		} catch (Exception e) {
			nf = null;
		}
		return nf;
	}

	public void update(long id, String title, long type, String content, String edittime, long userid) {
		String sql = " update set_notify set title=?,type=?,content=?,edittime=?,edituserid=? where id=? ";
		jdbcTemplate.update(sql, title, type, content, edittime, userid, id);
	}

	/**
	 * 置顶
	 * 
	 * @param id
	 */
	public void updateToTop(long id) {
		String sql = " update set_notify set istop=1 where id=" + id;
		jdbcTemplate.update(sql);
	}

	/**
	 * 置顶前 把其他的istop 清零
	 */
	public void clearIstop() {
		String sql = " update set_notify set istop=0 ";
		jdbcTemplate.update(sql);
	}

	/**
	 * 得到置顶的公告
	 * 
	 * @return
	 */
	public Notify getIstopNotify() {
		Notify nf = new Notify();
		String sql = "select *  from set_notify where istop=1 and state=1";
		try {
			nf = jdbcTemplate.queryForObject(sql, new notifyMapper());
		} catch (Exception e) {
			nf = null;
		}
		return nf;
	}

	/**
	 * 首页 五条
	 * 
	 * @return
	 */
	public List<Notify> getIndexNotify() {
		String sql = "select * from set_notify where istop=0 and state=1  order by cretime desc limit 0,5";
		return jdbcTemplate.query(sql, new notifyMapper());
	}

}

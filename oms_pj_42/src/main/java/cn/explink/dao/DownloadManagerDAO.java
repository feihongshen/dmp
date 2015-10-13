package cn.explink.dao;

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

import cn.explink.domain.DownloadManager;
import cn.explink.enumutil.DownloadState;
import cn.explink.enumutil.ModelEnum;
import cn.explink.util.Page;

@Component
public class DownloadManagerDAO {
	private final class DownMapper implements RowMapper<DownloadManager> {
		@Override
		public DownloadManager mapRow(ResultSet rs, int rowNum) throws SQLException {
			DownloadManager down = new DownloadManager();

			down.setCreatetime(rs.getString("createtime"));
			down.setDatajson(rs.getString("datajson"));
			down.setFilename(rs.getString("filename"));
			down.setFileurl(rs.getString("fileurl"));
			down.setId(rs.getLong("id"));
			down.setModelid(rs.getInt("modelid"));
			down.setState(rs.getInt("state"));
			down.setTimeout(rs.getLong("timeout"));
			down.setUserid(rs.getLong("userid"));
			down.setEndtime(rs.getString("endtime"));
			down.setCnfilename(rs.getString("cnfilename"));
			return down;
		}

	}

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public long creDownloadManager(final DownloadManager down) {
		KeyHolder key = new GeneratedKeyHolder();
		jdbcTemplate.update(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(java.sql.Connection con) throws SQLException {
				PreparedStatement ps = null;
				ps = con.prepareStatement("INSERT INTO `set_download_manager`(`userid`,`datajson`,`modelid`,`createtime`,`state`,`filename`,`fileurl`,`timeout`,cnfilename) "
						+ "VALUES ( ?,?,?,?,?,?,?,?,?) ", new String[] { "id" });
				ps.setLong(1, down.getUserid());
				ps.setString(2, down.getDatajson());
				ps.setLong(3, down.getModelid());
				ps.setString(4, down.getCreatetime());
				ps.setLong(5, down.getState());
				ps.setString(6, down.getFilename());
				ps.setString(7, down.getFileurl());
				ps.setLong(8, down.getTimeout());
				ps.setString(9, down.getCnfilename());
				return ps;
			}
		}, key);
		return key.getKey().longValue();
	}

	public List<DownloadManager> getDownloadManagerListByUserId(long userid, String beginTime, String endTime, int state, long page) {
		// 不显示妥投率的下载请求
		// TODO should not hard code here
		String sql = "SELECT * FROM `set_download_manager` WHERE userid=? and modelid != " + ModelEnum.deliveryRate.getValue();
		if (beginTime.length() > 0 || endTime.length() > 0 || state > -2) {
			if (beginTime.length() > 0) {
				sql += " and createtime>='" + beginTime + "'";
			}
			if (endTime.length() > 0) {
				sql += " and createtime<='" + endTime + "'";
			}
			if (state > -2) {
				sql += " and state=" + state + " ";
			}
		}
		sql += " order by id desc  ";
		sql += "  limit " + (page - 1) * Page.DOWN_PAGE_NUMBER + " ," + Page.DOWN_PAGE_NUMBER;
		return jdbcTemplate.query(sql, new DownMapper(), userid);
	}

	public long getDownloadManagerCountByUserId(long userid, String beginTime, String endTime, int state) {
		// 不显示妥投率的下载请求
		// TODO should not hard code here
		String sql = "SELECT count(1) FROM `set_download_manager` WHERE userid=? and modelid != " + ModelEnum.deliveryRate.getValue();
		if (beginTime.length() > 0 || endTime.length() > 0 || state > -2) {
			if (beginTime.length() > 0) {
				sql += " and createtime>='" + beginTime + "'";
			}
			if (endTime.length() > 0) {
				sql += " and createtime<='" + endTime + "'";
			}
			if (state > -2) {
				sql += " and state=" + state + " ";
			}
		}
		return jdbcTemplate.queryForLong(sql, userid);
	}

	public List<DownloadManager> getDownloadManagerListByState(int state) {
		String sql = "SELECT * FROM `set_download_manager` WHERE state=? order by createtime ASC limit 0,1 ";
		return jdbcTemplate.query(sql, new DownMapper(), state);
	}

	public int getDownloadManagerCountByState(int state) {
		String sql = "SELECT count(1) FROM `set_download_manager` WHERE state=? ";
		return jdbcTemplate.queryForInt(sql, state);
	}

	public DownloadManager getDownloadManagerByid(long userid, long id) {
		String sql = "SELECT * FROM `set_download_manager` WHERE userid=? and id=? ";
		try {
			return jdbcTemplate.queryForObject(sql, new DownMapper(), userid, id);
		} catch (DataAccessException e) {
			// TODO Auto-generated catch block
			return null;
		}
	}

	public long getDownloadCountByUseridAndState(long userid, int state) {
		return jdbcTemplate.queryForLong("SELECT count(1) FROM `set_download_manager` WHERE userid=? and  state=? ", userid, state);
	}

	public List<DownloadManager> getUserList() {
		String sql = "SELECT * FROM `set_download_manager` WHERE state=0 group by userid";
		return jdbcTemplate.query(sql, new DownMapper());
	}

	public void updateXiazaiToZhongzhi() {
		String sql = "update `set_download_manager` set state=2  WHERE state=0 ";
		jdbcTemplate.update(sql);
	}

	public void updateStateById(int state, long id, String endtime) {
		String sql = "update `set_download_manager` set state=? " + (state == 1 ? " ,endtime='" + endtime + "'" : "") + " WHERE id=? ";
		jdbcTemplate.update(sql, state, id);
	}

	public void delStateById(long id) {
		String sql = "delete from `set_download_manager` WHERE id=? ";
		jdbcTemplate.update(sql, id);
	}

	public long checkDownload(String datajson) {
		String sql = "select count(1) from `set_download_manager`  WHERE datajson=? ";
		return jdbcTemplate.queryForLong(sql, datajson);
	}

	public DownloadManager getDownloadManagerByJson(String datajson) {
		String sql = "SELECT * FROM `set_download_manager` WHERE datajson=? and state in(-1,0) limit 0,1 ";
		try {
			return jdbcTemplate.queryForObject(sql, new DownMapper(), datajson);
		} catch (DataAccessException e) {
			// TODO Auto-generated catch block
			return null;
		}
	}

	public List<DownloadManager> getDownloadManagerByThreadCount(int count) {
		String sql = "SELECT * FROM `set_download_manager` WHERE state in(-1,0) order by state desc limit 0," + count;
		return jdbcTemplate.query(sql, new DownMapper());
	}

	public List<DownloadManager> getAllListByCreateTime(String createtime) {
		String sql = "SELECT * FROM `set_download_manager` WHERE createtime <=?  and state in(1,3)";
		return jdbcTemplate.query(sql, new DownMapper(), createtime);
	}

	public List<DownloadManager> getDownloadManager(long userId, ModelEnum modelId, DownloadState[] downloadStates) {
		StringBuilder sql = new StringBuilder("SELECT * FROM `set_download_manager` WHERE userid = ?  and modelid = ? and state in(");
		for (int i = 0; i < downloadStates.length; i++) {
			if (i != 0) {
				sql.append(", ");
			}
			sql.append(downloadStates[i].getValue());
		}
		sql.append(") order by id desc");
		return jdbcTemplate.query(sql.toString(), new DownMapper(), userId, modelId.getValue());
	}

	public DownloadManager getDownloadManagerById(long id) {
		String sql = "SELECT * FROM `set_download_manager` WHERE id = ?";
		return jdbcTemplate.queryForObject(sql, new DownMapper(), id);
	}

	public void updateState(Integer downloadRequestId, int state) {
		StringBuilder sql = new StringBuilder("update `set_download_manager` set state = ?");
		sql.append(" WHERE id= ?");
		jdbcTemplate.update(sql.toString(), state, downloadRequestId);
	}

	public List<DownloadManager> getDownloadManagerByUserAndModel(Long userId, ModelEnum model) {
		String sql = "SELECT * FROM `set_download_manager` WHERE userid = ? and modelid = ?";
		return jdbcTemplate.query(sql, new DownMapper(), userId, model.getValue());
	}

}

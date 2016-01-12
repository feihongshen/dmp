package cn.explink.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import cn.explink.domain.TransCwbDetail;
import cn.explink.util.Tools;

@Repository
public class TransCwbDetailDAO {

	@Autowired
	JdbcTemplate jdbcTemplate;

	private final class TransCwbRowMapper implements RowMapper<TransCwbDetail> {

		@Override
		public TransCwbDetail mapRow(ResultSet rs, int rowNum) throws SQLException {
			TransCwbDetail tcd = new TransCwbDetail();
			tcd.setId(rs.getInt("id"));
			tcd.setCwb(rs.getString("cwb"));
			tcd.setTranscwb(rs.getString("transcwb"));
			tcd.setTranscwbstate(rs.getInt("transcwbstate"));
			tcd.setTranscwboptstate(rs.getInt("transcwboptstate"));
			tcd.setCurrentbranchid(rs.getInt("currentbranchid"));
			tcd.setPreviousbranchid(rs.getInt("previousbranchid"));
			tcd.setNextbranchid(rs.getLong("nextbranchid"));
			tcd.setCreatetime(rs.getString("createtime"));
			tcd.setModifiedtime(rs.getString("modifiedtime"));
			tcd.setEmaildate(rs.getString("emaildate"));
			tcd.setCommonphraseid(rs.getInt("commonphraseid"));
			tcd.setCommonphrase(rs.getString("commonphrase"));
			return tcd;
		}
	}

	/**
	 * 添加TransCwbDetail
	 *
	 * @return
	 */
	public void addTransCwbDetail(final TransCwbDetail tc) {
		String sql = "insert into express_ops_transcwb_detail(cwb,transcwb,transcwbstate,transcwboptstate,currentbranchid,previousbranchid,nextbranchid,createtime,emaildate,commonphraseid,commonphrase) values(?,?,?,?,?,?,?,?,?,?,?)";
		this.jdbcTemplate.update(sql, new PreparedStatementSetter() {

			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				ps.setString(1, tc.getCwb());
				ps.setString(2, tc.getTranscwb());
				ps.setInt(3, tc.getTranscwbstate());
				ps.setInt(4, tc.getTranscwboptstate());
				ps.setLong(5, tc.getCurrentbranchid());
				ps.setLong(6, tc.getPreviousbranchid());
				ps.setLong(7, tc.getNextbranchid());
				ps.setString(8, tc.getCreatetime());
				ps.setString(9, tc.getEmaildate());
				ps.setLong(10, tc.getCommonphraseid());
				ps.setString(11, tc.getCommonphrase());

			}
		});
	}

	/**
	 * 修改TransCwbDetail
	 *
	 * @return
	 */
	public void updateTransCwbDetail(final TransCwbDetail tc) {
		String sql = "update express_ops_transcwb_detail set cwb=?,transcwb=?,transcwbstate=?,transcwboptstate=?,currentbranchid=?,previousbranchid=?,nextbranchid=?,createtime=?,modifiedtime=?,emaildate=?,commonphraseid=?,commonphrase=? where id=?";
		this.jdbcTemplate.update(sql, new PreparedStatementSetter() {

			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				ps.setString(1, tc.getCwb());
				ps.setString(2, tc.getTranscwb());
				ps.setInt(3, tc.getTranscwbstate());
				ps.setInt(4, tc.getTranscwboptstate());
				ps.setLong(5, tc.getCurrentbranchid());
				ps.setLong(6, tc.getPreviousbranchid());
				ps.setLong(7, tc.getNextbranchid());
				ps.setString(8, tc.getCreatetime());
				ps.setString(9, tc.getModifiedtime());
				ps.setString(10, tc.getEmaildate());
				ps.setLong(11, tc.getCommonphraseid());
				ps.setString(12, tc.getCommonphrase());
				ps.setInt(13, tc.getId());

			}
		});
	}

	/**
	 * 查找全部
	 *
	 * @return
	 */
	public List<TransCwbDetail> findTransCwbDetail() {
		String sql = "select * from express_ops_transcwb_detail";
		return this.jdbcTemplate.query(sql, new TransCwbRowMapper());
	}

	/**
	 * 查找通过运单号
	 *
	 * @return
	 */
	public TransCwbDetail findTransCwbDetailByTransCwb(String transcwb) {
		String sql = "select * from express_ops_transcwb_detail where transcwb=?";
		try {
			return this.jdbcTemplate.queryForObject(sql, new TransCwbRowMapper(), transcwb);
		} catch (DataAccessException e) {
			return null;
		}
	}

	/**
	 *
	 * 通过运单号列表查询运单
	 *
	 * @param transCwbList
	 * @return
	 */
	public List<TransCwbDetail> getTransCwbDetailListByTransCwbList(List<String> transCwbList) {
		List<TransCwbDetail> transCwbDetailList = new ArrayList<TransCwbDetail>();
		String sql = "select * from express_ops_transcwb_detail where transcwb " + Tools.assembleInByList(transCwbList);
		try {
			transCwbDetailList = this.jdbcTemplate.query(sql, new TransCwbRowMapper());
		} catch (DataAccessException e) {
		}
		return transCwbDetailList;
	}

	public String getCwbByTransCwb(String transcwb) {
		String sql = "select cwb from express_ops_transcwb_detail where transcwb=?";
		try {
			return this.jdbcTemplate.queryForObject(sql, String.class, transcwb);
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	public List<TransCwbDetail> getTransCwbDetailListByCwb(String cwb) {
		List<TransCwbDetail> transCwbDetailList = new ArrayList<TransCwbDetail>();
		String sql = "select * from express_ops_transcwb_detail where cwb=?";
		try {
			transCwbDetailList = this.jdbcTemplate.query(sql, new TransCwbRowMapper(), cwb);
		} catch (DataAccessException e) {
		}
		return transCwbDetailList;
	}

	/**
	 * 通过id删除实体
	 *
	 * @return
	 */
	public void removeTransCwbDetailById(int id) {
		String sql = "delete from express_ops_transcwb_detail where id=" + id;
		this.jdbcTemplate.update(sql);
	}

	/**
	 *
	 * @Title: getTransCwbDetailListByTransCwbs
	 * @description 根据输入的子单集合，获取所有的子单详情
	 * @author 刘武强
	 * @date 2016年1月8日下午5:17:19
	 * @param @param transcwbs
	 * @param @return
	 * @return List<TransCwbDetail>
	 * @throws
	 */
	public List<TransCwbDetail> getTransCwbDetailListByTransCwbs(String transcwbs) {
		String sql = "select * from express_ops_transcwb_detail where transcwb in " + transcwbs;
		try {
			return this.jdbcTemplate.query(sql, new TransCwbRowMapper());
		} catch (DataAccessException e) {
			return null;
		}
	}

	/**
	 *
	 * @Title: importEmbracedData
	 * @description TODO
	 * @author 刘武强
	 * @date 2016年1月11日下午8:56:29
	 * @param @param list
	 * @param @param userparam
	 * @param @param branch
	 * @return void
	 * @throws
	 */
	public void saveWithMount(List<TransCwbDetail> list) {
		final String sql = "update express_ops_transcwb_detail set cwb=?,transcwbstate=?,transcwboptstate=?,nextbranchid=?,commonphraseid=?,commonphrase=? where transcwb=?";
		int circleTimes = (list.size() / Tools.DB_OPERATION_MAX) + 1;

		for (int i = 0; i < circleTimes; i++) {
			int startIndex = Tools.DB_OPERATION_MAX * i;
			int endIndex = Tools.DB_OPERATION_MAX * (i + 1);
			if (endIndex > list.size()) {
				endIndex = list.size();
			}
			if (endIndex <= 0) {
				break;
			}
			final List<TransCwbDetail> tempList = list.subList(startIndex, endIndex);
			if ((null != tempList) && !tempList.isEmpty()) {
				this.jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
					@Override
					public void setValues(PreparedStatement ps, int i) throws SQLException {
						TransCwbDetail tc = tempList.get(i);
						ps.setString(1, tc.getCwb());
						ps.setInt(2, tc.getTranscwbstate());
						ps.setInt(3, tc.getTranscwboptstate());
						ps.setLong(4, tc.getNextbranchid());
						ps.setLong(5, tc.getCommonphraseid());
						ps.setString(6, tc.getCommonphrase());
						ps.setString(7, tc.getTranscwb());
					}

					@Override
					public int getBatchSize() {

						return tempList.size();
					}
				});
			}
		}
	}

}

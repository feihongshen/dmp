package cn.explink.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
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
			tcd.setNextbranchid(rs.getInt("nextbranchid"));
			tcd.setCreatetime(rs.getDate("createtime") + "");
			tcd.setModifiedtime(rs.getTimestamp("modifiedtime") + "");
			tcd.setEmaildate(rs.getDate("emaildate") + "");
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
		String sql = "insert into express_ops_transcwb_detail(cwb,transcwb,transcwbstate,transcwboptstate,currentbranchid,previousbranchid,nextbranchid,createtime,modifiedtime,emaildate,commonphraseid,commonphrase) values(?,?,?,?,?,?,?,?,?,?,?,?)";
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
				ps.setInt(11, tc.getCommonphraseid());
				ps.setString(12, tc.getCommonphrase());

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
				ps.setInt(11, tc.getCommonphraseid());
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
			// TODO Auto-generated catch block
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
		String sql = "select * from express_ops_transcwb_detail where transcwb " + Tools.assembleInByList(transCwbList);
		try {
			return this.jdbcTemplate.query(sql, new TransCwbRowMapper());
		} catch (DataAccessException e) {
			return null;
		}
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

}

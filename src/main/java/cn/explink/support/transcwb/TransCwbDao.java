package cn.explink.support.transcwb;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

@Component
public class TransCwbDao {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	private final class TranscwbRowMapper implements RowMapper<TranscwbView> {
		@Override
		public TranscwbView mapRow(ResultSet rs, int rowNum) throws SQLException {
			TranscwbView transcwbView = new TranscwbView();
			transcwbView.setCwb(rs.getString("cwb"));
			transcwbView.setTranscwb(rs.getString("transcwb"));
			return transcwbView;
		}

	}

	private final class TranscwbForcwbRowMapper implements RowMapper<String> {
		@Override
		public String mapRow(ResultSet rs, int rowNum) throws SQLException {
			return rs.getString("cwb");
		}
	}

	private final class TranscwbForTransRowMapper implements RowMapper<String> {
		@Override
		public String mapRow(ResultSet rs, int rowNum) throws SQLException {
			return rs.getString("transcwb");
		}
	}

	public String getCwbByTransCwb(String transcwb) {
		String sql = "select cwb from express_ops_transcwb where transcwb=?";
		try {
			return this.jdbcTemplate.queryForObject(sql, String.class, transcwb);
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	public List<TranscwbView> getTransCwbByCwb(String cwb) {
		String sql = "select * from express_ops_transcwb where cwb=?";
		try {
			return this.jdbcTemplate.query(sql, new TranscwbRowMapper(), cwb);
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	public List<TranscwbView> getcwbBytranscwb(String transcwb) {
		String sql = "select * from express_ops_transcwb where transcwb=?";
		try {
			return this.jdbcTemplate.query(sql, new TranscwbRowMapper(), transcwb);
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	public void saveTranscwb(String transcwb, String cwb) {
		try {
			this.jdbcTemplate.update("INSERT INTO express_ops_transcwb (transcwb, cwb) VALUES (?, ?)", transcwb, cwb);
		} catch (DataAccessException e) {
		}
	}

	public void deleteTranscwb(String cwb) {
		try {
			this.jdbcTemplate.update("DELETE FROM express_ops_transcwb where cwb=?", cwb);
		} catch (DataAccessException e) {
		}
	}

	public void deleteTranscwbByCwbs(String cwbs) {
		try {
			this.jdbcTemplate.update("DELETE FROM express_ops_transcwb where cwb in(" + cwbs + ")");
		} catch (DataAccessException e) {
		}
	}

	public void delTranscwb() {
		// 这真是一条超牛的sql JM
		// jdbcTemplate.update("DELETE FROM express_ops_transcwb where cwb in(select cwb from express_ops_cwb_detail where state=0)");
	}

	public List<TranscwbView> getTransCwbByTranscwb(String temporders) {
		String sql = " select * from express_ops_transcwb where transcwb in(" + temporders + ")";
		return this.jdbcTemplate.query(sql, new TranscwbRowMapper());
	}

	/**
	 * @Title: getCwbsByCwbList
	 * @description 根据输入的单号集合（子单号或主单号），查询所对应的TranscwbView集合
	 * @author 刘武强
	 * @date  2016年1月8日下午4:01:16
	 * @param  @param cwbs
	 * @param  @return
	 * @return  List<String>
	 * @throws
	 */
	public List<TranscwbView> getTransCwbByOrderNoList(String cwbs) {
		List<TranscwbView> list = new ArrayList<TranscwbView>();
		String sql = "select distinct * from express_ops_transcwb where cwb in " + cwbs + " or transcwb in  " + cwbs;
		list = this.jdbcTemplate.query(sql, new TranscwbRowMapper());
		return list;
	}

	/**
	 *
	 * @Title: getTransCwbByTransCwbList
	 * @description 根据输入的主单号集合，查询所对应的TranscwbView集合
	 * @author 刘武强
	 * @date  2016年1月11日上午9:24:46
	 * @param  @param cwbs
	 * @param  @return
	 * @return  List<TranscwbView>
	 * @throws
	 */
	public List<TranscwbView> getTransCwbByCwbNoList(String cwbs) {
		List<TranscwbView> list = new ArrayList<TranscwbView>();
		String sql = "select distinct * from express_ops_transcwb where  cwb in  " + cwbs;
		list = this.jdbcTemplate.query(sql, new TranscwbRowMapper());
		return list;
	}

}

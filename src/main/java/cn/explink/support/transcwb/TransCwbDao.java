package cn.explink.support.transcwb;

import java.sql.ResultSet;
import java.sql.SQLException;
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

	public String getCwbByTransCwb(String transcwb) {
		String sql = "select cwb from express_ops_transcwb where transcwb=?";
		try {
			return jdbcTemplate.queryForObject(sql, String.class, transcwb);
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	public List<TranscwbView> getTransCwbByCwb(String cwb) {
		String sql = "select * from express_ops_transcwb where cwb=?";
		try {
			return jdbcTemplate.query(sql, new TranscwbRowMapper(), cwb);
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}
	
	public List<TranscwbView> getcwbBytranscwb(String transcwb) {
		String sql = "select * from express_ops_transcwb where transcwb=?";
		try {
			return jdbcTemplate.query(sql, new TranscwbRowMapper(), transcwb);
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	public void saveTranscwb(String transcwb, String cwb) {
		try {
			jdbcTemplate.update("INSERT INTO express_ops_transcwb (transcwb, cwb) VALUES (?, ?)", transcwb, cwb);
		} catch (DataAccessException e) {
		}
	}

	public void deleteTranscwb(String cwb) {
		try {
			jdbcTemplate.update("DELETE FROM express_ops_transcwb where cwb=?", cwb);
		} catch (DataAccessException e) {
		}
	}

	public void deleteTranscwbByCwbs(String cwbs) {
		try {
			jdbcTemplate.update("DELETE FROM express_ops_transcwb where cwb in(" + cwbs + ")");
		} catch (DataAccessException e) {
		}
	}
	
	

	public void delTranscwb() {
		// 这真是一条超牛的sql JM
		// jdbcTemplate.update("DELETE FROM express_ops_transcwb where cwb in(select cwb from express_ops_cwb_detail where state=0)");
	}

	public List<TranscwbView> getTransCwbByTranscwb(String temporders) {
		String sql = " select * from express_ops_transcwb where transcwb in(" + temporders + ")";
		return jdbcTemplate.query(sql, new TranscwbRowMapper());
	}
	
}

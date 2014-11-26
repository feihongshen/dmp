package cn.explink.support.transcwb;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class OneTransToMoreCwbDao {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public List<String> getCwbByTransCwb(String transcwb) {
		String sql = "select cwb from set_onetranscwb_to_morecwbs where transcwb=?";
		try {
			return jdbcTemplate.queryForList(sql, String.class, transcwb);
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	public void saveTranscwb(String transcwb, String cwb) {
		jdbcTemplate.update("INSERT INTO set_onetranscwb_to_morecwbs (transcwb, cwb) VALUES (?, ?)", transcwb, cwb);
	}

	public void updateTranscwb(String transcwb, String cwb) {
		jdbcTemplate.update("update set_onetranscwb_to_morecwbs set transcwb=? where  cwb=?", transcwb, cwb);
	}

	public void delTranscwb() {
		jdbcTemplate.update("DELETE FROM set_onetranscwb_to_morecwbs where cwb in(select cwb from express_ops_cwb_detail where state=0)");
	}
}

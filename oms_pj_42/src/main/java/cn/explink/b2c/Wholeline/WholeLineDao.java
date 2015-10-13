package cn.explink.b2c.Wholeline;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

@Component
public class WholeLineDao {
	@Autowired
	private JdbcTemplate jdbcTemplate;

	private final class WholelineSearchMap implements RowMapper<WholelineSearch> {
		@Override
		public WholelineSearch mapRow(ResultSet rs, int rowNum) throws SQLException {
			WholelineSearch en = new WholelineSearch();
			en.setCwb(rs.getString("cwb"));
			en.setState(rs.getInt("state"));
			en.setRemark(rs.getString("remark"));
			en.setTranscwb(rs.getString("transcwb"));
			en.setCretime(rs.getString("cretime"));
			en.setCustomerid(rs.getString("customerid"));
			return en;
		}
	}

	public long getUpdateByCwb(String cwb, String statetime) {
		String sql = "update express_b2cdatadown_wholeline set state =? where cwb=?";
		return jdbcTemplate.update(sql, statetime, cwb);

	}

	public void saveWholeline(WholelineSearch lt) {
		String sql = "insert into express_b2cdatadown_wholeline (cwb,remark,cretime,transcwb,customerid) values(?,?,?,?,?) ";
		jdbcTemplate.update(sql, lt.getCwb(), lt.getRemark(), lt.getCretime(), lt.getTranscwb(), lt.getCustomerid());
	}

	public long getCountWholeline(String cwb) {
		String sql = "select count(1) from express_b2cdatadown_wholeline where cwb='" + cwb + "' ";
		return jdbcTemplate.queryForLong(sql);
	}

	public List<WholelineSearch> getWholeLinesCwBForList(long countfirst, long count) {
		String sql = "select * from express_b2cdatadown_wholeline where  state='' limit " + countfirst + "," + count;
		return jdbcTemplate.query(sql, new WholelineSearchMap());

	}

	public int getCountWholeLines() {
		String sql = "select count(1) from express_b2cdatadown_wholeline where  state=''";
		return jdbcTemplate.queryForInt(sql);

	}
}

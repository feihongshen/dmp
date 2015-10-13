package cn.explink.b2c.saohuobang;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

@Component
public class SaohuobangDao {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	private final class SaohuobangMap implements RowMapper<SaohuobangXMLNote> {
		@Override
		public SaohuobangXMLNote mapRow(ResultSet rs, int rowNum) throws SQLException {
			SaohuobangXMLNote en = new SaohuobangXMLNote();
			en.setCwb(rs.getString("cwb"));
			en.setOptiontime(rs.getString("posttime"));
			en.setRemark(rs.getString("remark"));
			en.setTranscwb(rs.getString("transcwb"));
			en.setFlowordertype(rs.getString("flowordertype"));
			en.setAddress(rs.getString("address"));
			return en;
		}
	}

	public long getDataByCwbs(String cwb) {
		String sql = "select COUNT(1) from express_b2cdata_search_saohuobang where cwb in(" + cwb + ") ";
		return jdbcTemplate.queryForLong(sql);

	}

	public List<SaohuobangXMLNote> getDataByCwbAndFlowordertype(String transcwb, String type) {
		String sql = "select * from express_b2cdata_search_saohuobang where transcwb=? and flowordertype=?";
		return jdbcTemplate.query(sql, new SaohuobangMap(), transcwb, type);

	}

	public void save(SaohuobangXMLNote lt, long flow) {
		String sql = "insert into express_b2cdata_search_saohuobang (cwb,flowordertype,posttime,remark,transcwb,address) values(?,?,?,?,?,?) ";
		jdbcTemplate.update(sql, lt.getCwb(), flow, lt.getOptiontime(), lt.getStatus(), lt.getTranscwb(), lt.getAddress());
	}

	public long CountSaohuobang(String cwb) {
		String sql = "select count(*) from express_b2cdata_search_saohuobang where transcwb=?";
		return jdbcTemplate.update(sql, cwb);
	}

	public List<SaohuobangXMLNote> getCwBForList(String cwb) {
		String sql = "select * from express_b2cdata_search_saohuobang where cwb=" + cwb + " order by flowordertype and posttime";
		return jdbcTemplate.query(sql, new SaohuobangMap());

	}

}

package cn.explink.b2c.sfexpress;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import cn.explink.b2c.Wholeline.WholelineSearch;

@Component
public class SfexpressDAO {
	@Autowired
	private JdbcTemplate jdbcTemplate;

	private final class WholelineSearchMap implements RowMapper<SfexpressBean> {
		@Override
		public SfexpressBean mapRow(ResultSet rs, int rowNum) throws SQLException {
			SfexpressBean en = new SfexpressBean();
			en.setCwb(rs.getString("cwb"));
			en.setTranscwb(rs.getString("transcwb"));
			en.setCretime(rs.getString("cretime"));
			en.setUpdatetime(rs.getString("updatetime"));
			en.setStatus(rs.getInt("status"));
			en.setRemark(rs.getString("remark"));
			en.setId(rs.getLong("id"));
			en.setIsok(rs.getInt("isok"));

			return en;
		}
	}

	public long getUpdateByCwb(String cwb, int status, String updatetime, String remark) {
		String sql = "update express_b2cdatadown_sf set status =?,updatetime=?,remark=? where cwb=?";
		return jdbcTemplate.update(sql, status, updatetime, remark, cwb);

	}

	public long getUpdateIsOKByCwb(String cwb, int isok_staus) {
		String sql = "update express_b2cdatadown_sf set isok=? where cwb=?";
		return jdbcTemplate.update(sql, isok_staus, cwb);

	}

	public void saveSfexpress(SfexpressBean sf) {
		String sql = "insert into express_b2cdatadown_sf (cwb,transcwb,cretime,status,remark) values(?,?,?,?,?) ";
		jdbcTemplate.update(sql, sf.getCwb(), sf.getTranscwb(), sf.getCretime(), 0, sf.getRemark());
	}

	public long getCountWholeline(String cwb) {
		String sql = "select count(1) from express_b2cdatadown_sf where cwb='" + cwb + "' ";
		return jdbcTemplate.queryForLong(sql);
	}

	public List<SfexpressBean> getSfexpressOrderList(long page, long onePageNumber) {
		String sql = "select * from express_b2cdatadown_sf where  isok=0 ";

		if (page > 0) {
			sql += " limit " + (page - 1) * onePageNumber + " ," + onePageNumber;
		}
		return jdbcTemplate.query(sql, new WholelineSearchMap());

	}

	public int getCountWholeLines() {
		String sql = "select count(1) from express_b2cdatadown_sf where  status=''";
		return jdbcTemplate.queryForInt(sql);

	}
}

package cn.explink.b2c.liantong;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

@Component
public class LiantongDAO {
	@Autowired
	private JdbcTemplate jdbcTemplate;

	private final class powerMapper implements RowMapper<LiantongEntity> {
		@Override
		public LiantongEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
			LiantongEntity en = new LiantongEntity();
			en.setB2cid(rs.getLong("b2cid"));
			en.setAcceptAction(rs.getString("acceptAction"));
			en.setAcceptAddress(rs.getString("acceptAddress"));
			en.setAcceptName(rs.getString("acceptName"));
			en.setAcceptTime(rs.getString("acceptTime"));
			en.setRemark(rs.getString("remark"));
			en.setCwb(rs.getString("cwb"));
			en.setTranscwb(rs.getString("transcwb"));
			en.setFlowordertype(rs.getLong("flowordertype"));

			return en;
		}
	}

	public List<LiantongEntity> getDataByCwb(String cwb) {
		String sql = "select * from express_b2cdata_liantong where cwb=? ";

		return jdbcTemplate.query(sql, new powerMapper(), cwb);

	}

	public void save(LiantongEntity lt) {
		String sql = "insert into express_b2cdata_liantong (cwb,transcwb,acceptTime,acceptAddress,acceptAction,acceptName,remark,flowordertype) values(?,?,?,?,?,?,?,?) ";
		jdbcTemplate.update(sql, lt.getCwb(), lt.getTranscwb(), lt.getAcceptTime(), lt.getAcceptAddress(), lt.getAcceptAction(), lt.getAcceptName(), lt.getRemark(), lt.getFlowordertype());
	}

	public void update(long flowordertype, long deliverystate, String cretime, String desc, String operator, String cwb) {
		String sql = "update express_b2cdata_liantong set flowordertype=?,deliverystate=?,cretime=?,content=?,operatorname=? where cwb=? ";
		jdbcTemplate.update(sql, flowordertype, deliverystate, cretime, desc, operator, cwb);
	}

}

package cn.explink.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import cn.explink.b2c.weisuda.WeisudaCwb;
import cn.explink.util.StringUtil;

@Component
public class WeisudaDAO {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	private final class WSMapper implements RowMapper<WeisudaCwb> {
		@Override
		public WeisudaCwb mapRow(ResultSet rs, int rowNum) throws SQLException {
			WeisudaCwb weisudaCwb = new WeisudaCwb();
			weisudaCwb.setId(StringUtil.nullConvertToEmptyString(rs.getString("id")));
			weisudaCwb.setCwb(StringUtil.nullConvertToEmptyString(rs.getString("cwb")));
			weisudaCwb.setBound_time(StringUtil.nullConvertToEmptyString(rs.getString("bound_time")));
			weisudaCwb.setCourier_code(StringUtil.nullConvertToEmptyString(rs.getString("courier_code")));
			weisudaCwb.setIsqianshou(StringUtil.nullConvertToEmptyString(rs.getString("isqianshou")));
			weisudaCwb.setIstuisong(StringUtil.nullConvertToEmptyString(rs.getString("istuisong")));
			weisudaCwb.setOperationTime(StringUtil.nullConvertToEmptyString(rs.getString("operationTime")));
			return weisudaCwb;
		}

	}

	public void insertWeisuda(WeisudaCwb weisudaCwb) {
		jdbcTemplate.update("INSERT INTO express_b2cdata_weisuda (cwb,courier_code,bound_time,operationTime)" + " VALUES(?,?,?,?)", weisudaCwb.getCwb(), weisudaCwb.getCourier_code(),
				weisudaCwb.getBound_time(), weisudaCwb.getOperationTime());
	}

	public List<WeisudaCwb> getWeisudaCwb(String istuisong) {
		try {
			return jdbcTemplate.query("select * from express_b2cdata_weisuda  where istuisong=? limit 0,500", new WSMapper(), istuisong);
		} catch (Exception e) {
			return null;
		}
	}

	public WeisudaCwb getWeisudaCwbIstuisong(String cwb) {
		try {

			return jdbcTemplate.queryForObject("select * from express_b2cdata_weisuda  where cwb=?  limit 1", new WSMapper(), cwb);

		} catch (Exception e) {
			return null;
		}
	}

	public WeisudaCwb getWeisudaCwb(String cwb, String orderTime) {
		try {

			return jdbcTemplate.queryForObject("select * from express_b2cdata_weisuda  where cwb=? and operationTime=? limit 1", new WSMapper(), cwb, orderTime);

		} catch (Exception e) {
			return null;
		}
	}

	public WeisudaCwb getWeisudaCwbByOrder(String cwb) {
		try {

			return jdbcTemplate.queryForObject("select * from express_b2cdata_weisuda  where cwb=?  limit 1", new WSMapper(), cwb);

		} catch (Exception e) {
			return null;
		}
	}

	public void updateWeisuda(String cwb, String flag, String remark) {

		jdbcTemplate.update("update express_b2cdata_weisuda set remark='" + remark + "',istuisong='" + flag + "' where cwb='" + cwb + "'");
	}

	public void updataWeisudaCwbIsqianshou(String cwb, String flag, String remark) {
		jdbcTemplate.update("update express_b2cdata_weisuda set isqianshou='" + flag + "' ,remark='" + remark + "' where  cwb='" + cwb + "'");

	}
}

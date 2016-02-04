package cn.explink.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

@Component
public class BankDao {
	@Autowired
	private JdbcTemplate jdbcTemplate;

	private final class BankJsonMapper implements RowMapper<JSONObject> {
		@Override
		public JSONObject mapRow(ResultSet rs, int rowNum) throws SQLException {
			JSONObject reJson = new JSONObject();
			reJson.put("id", rs.getLong("id"));
			reJson.put("tlBankCode", rs.getString("tl_bank_code"));
			reJson.put("bankName", rs.getString("bank_name"));
			reJson.put("cftBankCode", rs.getString("cft_bank_code"));
			return reJson;
		}
	}

	/**
	 *
	 * @Title: getTlBank
	 * @description 获取通联的银行列表
	 * @author 刘武强
	 * @date  2015年12月22日上午8:57:31
	 * @param  @return
	 * @return  List<JSONObject>
	 * @throws
	 */
	public List<JSONObject> getTlBank() {
		try {
			String sql = "select * from express_set_bank where tl_flag=1";
			return this.jdbcTemplate.query(sql, new BankJsonMapper());
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 *
	 * @Title: getCftBank
	 * @description 获取财付通的银行列表
	 * @author 刘武强
	 * @date  2015年12月22日上午8:57:35
	 * @param  @return
	 * @return  List<JSONObject>
	 * @throws
	 */
	public List<JSONObject> getCftBank() {
		try {
			String sql = "select * from express_set_bank where cft_flag=1";
			return this.jdbcTemplate.query(sql, new BankJsonMapper());
		} catch (Exception e) {
			return null;
		}
	}
}

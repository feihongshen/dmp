package cn.explink.dao;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class AddressStartDAO {

	@Autowired
	JdbcTemplate jdbcTemplate;

	public boolean isAddressStart() {
		List<Map<String, Object>> list = jdbcTemplate.queryForList("select addressdbflag from express_set_address_db_start");
		if (list.size() > 0) {
			int flag = (Integer) list.get(0).get("addressdbflag");
			return flag == 1;
		}
		return false;
	}
}

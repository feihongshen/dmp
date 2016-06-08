package cn.explink.util.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import cn.explink.util.HealthCheck;

public class HealthCheckImpl implements HealthCheck {

	private static HealthCheck singleton = null;

	public static HealthCheck get_instance() {
		return singleton;
	}

	public HealthCheckImpl() {
		super();
		if (singleton == null) {
			singleton = this;
		}
	}

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Override
	public int doCheck(int level) {
		try {
			if (level > 0) {
				int total = jdbcTemplate.queryForInt("SELECT 1 + 1 FROM DUAL");
				return total;
			}
		} catch (Exception e) {
			// e.printStackTrace();
		}
		return 0;
	}

}

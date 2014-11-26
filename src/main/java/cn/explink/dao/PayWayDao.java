package cn.explink.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import cn.explink.domain.PayWay;
import cn.explink.util.Page;

@Component
public class PayWayDao {
	private final class PayWayMapper implements RowMapper<PayWay> {
		@Override
		public PayWay mapRow(ResultSet rs, int rowNum) throws SQLException {
			PayWay payWay = new PayWay();
			payWay.setId(rs.getLong("id"));
			payWay.setPayway(rs.getString("payway"));
			payWay.setPaywayid(rs.getLong("paywayid"));
			payWay.setIssetflag(rs.getLong("issetflag"));
			return payWay;
		}
	}

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public void create(final String payway, final long paywayid) {
		jdbcTemplate.update("insert into express_set_payway(payway,paywayid) values(?,?)", new PreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				ps.setString(1, payway);
				ps.setLong(2, paywayid);
			}
		});
	}

	public List<PayWay> getAllPayWay() {
		String sql = "select * from express_set_payway where issetflag=1";
		return jdbcTemplate.query(sql, new PayWayMapper());
	}

	public List<PayWay> getPayWayByPayway(String payway) {
		String sql = "select * from express_set_payway where payway=? and issetflag=1";
		return jdbcTemplate.query(sql, new PayWayMapper(), payway);
	}

	public List<PayWay> getPayWayByParm(String payway, long paywayid) {
		String sql = "select * from express_set_payway where payway=? and paywayid=? and issetflag=1";
		return jdbcTemplate.query(sql, new PayWayMapper(), payway, paywayid);
	}

	public List<PayWay> getPayWayByPage(long page, long paywayid) {
		String sql = "SELECT * from express_set_payway ";
		sql = this.getPayWayByPageWhereSql(sql, paywayid);
		sql += " limit " + (page - 1) * Page.ONE_PAGE_NUMBER + "," + Page.ONE_PAGE_NUMBER;
		List<PayWay> payWays = jdbcTemplate.query(sql, new PayWayMapper());
		return payWays;
	}

	public long getPayWayByConut(long paywayid) {
		String sql = "SELECT COUNT(1) FROM express_set_payway ";
		sql = this.getPayWayByPageWhereSql(sql, paywayid);
		return jdbcTemplate.queryForLong(sql);
	}

	public String getPayWayByPageWhereSql(String sql, long paywayid) {
		if (paywayid <= 0) {
			sql += "where issetflag=1 order by paywayid";
		} else if (paywayid > 0) {
			sql += "where issetflag=1 and paywayid='" + paywayid + "' order by paywayid";
		}
		return sql;
	}

	public void delPayWayById(long id) {
		jdbcTemplate.update("UPDATE express_set_payway SET issetflag=0 WHERE id=?", id);
	}

	public void savePayWayById(long id, String payway, long paywayid) {
		String sql = "UPDATE express_set_payway SET  payway=? ,paywayid=? WHERE id=? and issetflag=1";
		jdbcTemplate.update(sql, payway, paywayid, id);
	}

	public PayWay getPayWayByid(long id) {
		return jdbcTemplate.queryForObject("SELECT * FROM express_set_payway where issetflag=1 and id=?", new PayWayMapper(), id);
	}

	public long getPayWayByPayWay(String payway) {
		String sql = "SELECT * from express_set_payway where issetflag=1 and payway=?";
		List<PayWay> paywayList = jdbcTemplate.query(sql, new PayWayMapper(), payway);
		if (paywayList.size() > 0) {
			return paywayList.get(0).getId();
		} else {
			return 0;
		}

	}

	public long getPayWayByPayWayForPaywayid(String payway) {
		String sql = "SELECT * from express_set_payway where issetflag=1 and payway=?";
		List<PayWay> paywayList = jdbcTemplate.query(sql, new PayWayMapper(), payway);
		if (paywayList.size() > 0) {
			return paywayList.get(0).getPaywayid();
		} else {
			return 0;
		}

	}

	public Map<String, Long> getPayWayIdMapping() {
		List<PayWay> payWayList = getAllPayWay();
		Map<String, Long> payWayIdMap = new HashMap<String, Long>();
		if (payWayList != null) {
			for (PayWay payWay : payWayList) {
				payWayIdMap.put(payWay.getPayway(), payWay.getPaywayid());
			}
		}
		return payWayIdMap;
	}
}

package cn.explink.dao.monitor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import cn.explink.domain.monitor.MonitorSendB2cData;

@Component
public class MonitorSendB2cDataDAO {

	private final class MonitorSendB2cDataRowMapper implements RowMapper<MonitorSendB2cData> {
		@Override
		public MonitorSendB2cData mapRow(ResultSet rs, int rowNum) throws SQLException {
			MonitorSendB2cData monitorSendB2cData = new MonitorSendB2cData();
			monitorSendB2cData.setId(rs.getLong("id"));
			monitorSendB2cData.setRequesttime(rs.getString("requesttime"));
			monitorSendB2cData.setResultcount(rs.getLong("resultcount"));
			return monitorSendB2cData;
		}
	}

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public List<MonitorSendB2cData> getMonitorSendB2cDataByCustomerid(long customerid) {
		String sql = "select * from monitor_send_b2c_data where customerid=?";
		return jdbcTemplate.query(sql, new MonitorSendB2cDataRowMapper(), customerid);
	}

	public long getMonitorSendB2cDataCount(long customerid) {
		String sql = "SELECT count(1) from monitor_send_b2c_data";
		return jdbcTemplate.queryForLong(sql);
	}

	public void creMonitorSendB2cData(String requesttime, long resultcount) {
		jdbcTemplate.update("insert into monitor_send_b2c_data (requesttime,resultcount) values(?,?)", requesttime, resultcount);
	}

	public MonitorSendB2cData getMonitorSendB2cDataById(long areaid) {
		String sql = "select * from monitor_send_b2c_data where  areaid=?";
		return jdbcTemplate.queryForObject(sql, new MonitorSendB2cDataRowMapper(), areaid);
	}

	public long getMonitorSendB2cDataCountByRequesttime(String begintime) {
		return jdbcTemplate.queryForLong("select count(1) from monitor_send_b2c_data where requesttime>=?", begintime);
	}

}

package cn.explink.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class KeHuFaHuoDataDao {
	@Autowired
	JdbcTemplate jdbcTemplate;

	public void creDate(String begindate, String enddate, Long kufangid, long customerid, long num, long downid) {
		String sql = "INSERT INTO `ops_fahuohuizongdata`(`begindate`,`enddate`,`kufangid`,`customerid`,`result`,`exportid`) VALUES ( ?,?,?,?,?,?)";
		jdbcTemplate.update(sql, begindate, enddate, kufangid, customerid, num, downid);
	}
}

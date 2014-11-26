package cn.explink.b2c.amazon;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class AmazonDAO {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public void saveFile(String flieName, int state, String createtime) {
		String sql = "insert into express_ops_file_temp (flieName,state,createtime) VALUES (?,?,?)";
		jdbcTemplate.update(sql, flieName, state, createtime);
	}

	public void updateFile(String flieName, int state) {
		String sql = "update express_ops_file_temp set state=? where flieName=?";
		jdbcTemplate.update(sql, state, flieName);
	}

	public long checkFile(String flieName) {
		String sql = "select count(1) from  express_ops_file_temp  where flieName=?";
		return jdbcTemplate.queryForLong(sql, flieName);
	}

}

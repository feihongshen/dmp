package cn.explink.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import cn.explink.domain.Operatelog;
import cn.explink.util.StringUtil;

@Component
public class OperatelogDAO {

	private final class OperatelogRowMapper implements RowMapper<Operatelog> {
		@Override
		public Operatelog mapRow(ResultSet rs, int rowNum) throws SQLException {
			Operatelog operatelog = new Operatelog();
			operatelog.setId(rs.getLong("id"));
			operatelog.setOperateman(StringUtil.nullConvertToEmptyString(rs.getString("operateman")));
			operatelog.setOperatetime(StringUtil.nullConvertToEmptyString(rs.getString("operatetime")));
			operatelog.setCwb(StringUtil.nullConvertToEmptyString(rs.getString("cwb")));
			operatelog.setTranscwb(StringUtil.nullConvertToEmptyString(rs.getString("transcwb")));
			operatelog.setOperateremarks(StringUtil.nullConvertToEmptyString(rs.getString("operateremarks")));
			return operatelog;
		}
	}

	@Autowired
	private JdbcTemplate jdbcTemplate;

	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	public List<Operatelog> getAllOperatelog() {
		return jdbcTemplate.query("select * from express_ops_operatelog", new OperatelogRowMapper());
	}

	public List<Operatelog> getOperatelogByCwb(String cwb) {
		try {
			String sql = "select * from express_ops_operatelog where cwb = '" + cwb + "' or transcwb = '" + cwb + "' order by operatetime desc";
			return jdbcTemplate.query(sql, new OperatelogRowMapper());
		} catch (DataAccessException e) {
			e.printStackTrace();
		}
		return null;
	}

	public void creOperatelog(String operateman, String cwb, String transcwb, String operateramarks) {
		jdbcTemplate.update("insert into express_ops_operatelog(operateman,operatetime,cwb,transcwb,operateremarks) values(?,?,?,?,?)", operateman, sdf.format(new Date()), cwb, transcwb,
				operateramarks);
	}

}

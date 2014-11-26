package cn.explink.b2c.happyGo;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import cn.explink.util.DateTimeUtil;

@Component
public class HappyGoUsedDao {
	private Logger logger = LoggerFactory.getLogger(HappyGoUsedDao.class);

	private final class HappyRowMapper implements RowMapper<HappyMethod> {
		@Override
		public HappyMethod mapRow(ResultSet rs, int rowNum) throws SQLException {
			HappyMethod happy = new HappyMethod();
			happy.setAmount(rs.getInt("amount"));
			happy.setBatchname(rs.getString("batchname"));
			happy.setBatchno(rs.getString("batchno"));
			happy.setId(rs.getInt("id"));
			happy.setState(rs.getString("state"));
			happy.setType(rs.getInt("type"));
			happy.setWarehousecode(rs.getString("warehousecode"));
			happy.setSendtime(rs.getString("sendtime"));
			return happy;
		}

	}

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public long getbatchBybatchname(String name) {

		return jdbcTemplate.queryForLong("SELECT count(1) from express_ops_batch_gathered where batchname='" + name + "'");
	}

	public void getupdatebatchBybatchname(String name) {

		try {
			jdbcTemplate.update("update express_ops_batch_gathered set state='" + DateTimeUtil.getNowTime() + "' where batchname='" + name + "' ");
		} catch (Exception e) {
			logger.error("快乐购出库详情查询update新表时报错," + e.getMessage());
		}
	}

	/*
	 * 失效
	 */
	public void getupdateStateBybatchname(String name) {

		try {
			jdbcTemplate.update("update express_ops_batch_gathered set state=0 where batchname='" + name + "' ");
		} catch (Exception e) {
			logger.error("快乐购出库详情失效update新表时报错," + e.getMessage());
		}
	}

	public List<HappyMethod> getbatchByTypeAndState(int type) {
		List<HappyMethod> list = jdbcTemplate.query("SELECT * from express_ops_batch_gathered where type=? and state=1", new HappyRowMapper(), type);
		return list;
	}

	public void getInsertBatch(final Map<String, Object> list, final int type) {
		jdbcTemplate.update("insert into express_ops_batch_gathered (state,batchname,batchno,type," + "warehousecode,amount,sendtime)" + "values(?,?,?,?,?,?,?)", new PreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				ps.setString(1, "1");
				ps.setString(2, (String) list.get("filename"));
				ps.setString(3, (String) list.get("send_degree"));
				ps.setInt(4, type);
				ps.setString(5, (String) list.get("wh_code"));
				String a = (String) list.get("cnt");
				int b = Integer.parseInt(a);
				ps.setInt(6, b);
				ps.setString(7, (String) list.get("send_date"));
				logger.info("快乐购导入一条{}的数据,批次号是{}", type, list.get("filename"));
			}

		});
	}

	public List<HappyMethod> getbatchByType(int type) {
		List<HappyMethod> list = jdbcTemplate.query("SELECT * from express_ops_batch_gathered where type=? ", new HappyRowMapper(), type);
		return list;
	}
}

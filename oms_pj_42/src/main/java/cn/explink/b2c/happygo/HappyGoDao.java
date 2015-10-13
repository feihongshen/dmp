package cn.explink.b2c.happygo;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import cn.explink.util.DateTimeUtil;

@Component
public class HappyGoDao {

	private Logger logger = LoggerFactory.getLogger(HappyGoDao.class);

	private final class HappyRowMapper implements RowMapper<SerchHappy> {
		@Override
		public SerchHappy mapRow(ResultSet rs, int rowNum) throws SQLException {
			SerchHappy happy = new SerchHappy();
			happy.setCredate(rs.getString("credate"));
			happy.setCwb(rs.getString("cwb"));
			happy.setFlowordertype(rs.getInt("flowordertype"));
			happy.setOrderinfo(rs.getString("orderinfo"));
			happy.setState(rs.getInt("state"));
			happy.setPosttime(rs.getString("posttime"));
			happy.setId(rs.getInt("id"));
			return happy;
		}

	}

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public long getbatchBybatchname(String name) {

		return jdbcTemplate.queryForLong("SELECT count(1) from express_b2cdata_search_happygo where batchname='" + name + "'");
	}

	public void getupdatebatchBybatchname(String name) {

		try {
			jdbcTemplate.update("update express_b2cdata_search_happygo set state='" + DateTimeUtil.getNowTime() + "' where batchname='" + name + "' ");
		} catch (Exception e) {
			logger.error("快乐购出库详情查询update新表时报错," + e.getMessage());
		}
	}

	/*
	 * 失效
	 */
	public void getupdateStateBybatchname(String name) {

		try {
			jdbcTemplate.update("update express_b2cdata_search_happygo set state=0 where batchname='" + name + "' ");
		} catch (Exception e) {
			logger.error("快乐购出库详情失效update新表时报错," + e.getMessage());
		}
	}

	public List<SerchHappy> getSearchBytranscwb(String cwb) {
		List<SerchHappy> list = jdbcTemplate.query("SELECT * from express_b2cdata_search_happygo where cwb=? and state=0 order by flowordertype", new HappyRowMapper(), cwb);
		return list;
	}

	public void getSaveorder(final SerchHappy s, final long type) {
		jdbcTemplate.update("insert into express_b2cdata_search_happygo (state,flowordertype,orderinfo," + "cwb,credate,posttime)" + "values(?,?,?,?,?,?)", new PreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				ps.setString(1, "0");// weituisong
				ps.setLong(2, type);
				ps.setString(3, s.getOrderinfo());
				ps.setString(4, s.getCwb());
				ps.setString(5, s.getCredate());
				ps.setString(6, s.getPosttime());
				logger.info("快乐购一条{}的数据,批次号是{}", type, s.getCwb());
			}

		});
	}

}

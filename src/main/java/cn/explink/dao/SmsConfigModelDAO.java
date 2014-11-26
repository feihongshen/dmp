package cn.explink.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import cn.explink.domain.SmsConfigModel;

@Component
public class SmsConfigModelDAO {

	private final class SmsConfigModelRowMapper implements RowMapper<SmsConfigModel> {
		@Override
		public SmsConfigModel mapRow(ResultSet rs, int rowNum) throws SQLException {
			SmsConfigModel smsConfig = new SmsConfigModel();
			smsConfig.setId(rs.getLong("id"));
			smsConfig.setBranchids(rs.getString("branchids"));
			smsConfig.setCreateTime(rs.getString("createTime"));
			smsConfig.setCustomerids(rs.getString("customerids"));
			smsConfig.setFlowordertype(rs.getLong("flowordertype"));
			smsConfig.setMoney(rs.getBigDecimal("money"));
			smsConfig.setTemplatecontent(rs.getString("templatecontent"));
			smsConfig.setUsername(rs.getString("username"));
			return smsConfig;
		}
	}

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public List<SmsConfigModel> getAllSmsConfig() {
		return jdbcTemplate.query("select * from express_set_smsconfigmodel order by flowordertype ", new SmsConfigModelRowMapper());
	}

	public SmsConfigModel getSmsConfigById(long id) {
		try {
			return jdbcTemplate.queryForObject("select * from express_set_smsconfigmodel where id=? ", new SmsConfigModelRowMapper(), id);
		} catch (DataAccessException e) {
		}
		return null;
	}

	public SmsConfigModel getSmsConfigByFlowtype(long flowordertype) {
		try {
			return jdbcTemplate.queryForObject("select * from express_set_smsconfigmodel where flowordertype=? limit 1", new SmsConfigModelRowMapper(), flowordertype);
		} catch (DataAccessException e) {
		}
		return null;
	}

	public long delSmsConfigByFlowtype(long flowordertype) {
		try {
			return jdbcTemplate.update("delete from express_set_smsconfigmodel where flowordertype=? ", flowordertype);
		} catch (DataAccessException e) {
		}
		return 0;
	}

	public long creSmsConfigModel(final SmsConfigModel sms) {

		KeyHolder key = new GeneratedKeyHolder();
		jdbcTemplate.update(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(java.sql.Connection con) throws SQLException {
				PreparedStatement ps = null;
				ps = con.prepareStatement("INSERT INTO express_set_smsconfigmodel" + "( branchids,createTime,customerids," + "flowordertype,money,templatecontent,username) " + "VALUES "
						+ "(?,?,?,?,?,?,?)", new String[] { "id" });
				ps.setString(1, sms.getBranchids());
				ps.setString(2, sms.getCreateTime());
				ps.setString(3, sms.getCustomerids());
				ps.setLong(4, sms.getFlowordertype());
				ps.setBigDecimal(5, sms.getMoney());
				ps.setString(6, sms.getTemplatecontent());
				ps.setString(7, sms.getUsername());
				return ps;
			}
		}, key);
		return key.getKey().longValue();
	}

	public void updateSmsConfigModel(final SmsConfigModel sms) {
		jdbcTemplate.update("update express_set_smsconfigmodel set branchids=?,createTime=?,customerids=?," + "flowordertype=?,money=?,templatecontent=?,username=?" + "  where  id =?",
				new PreparedStatementSetter() {
					@Override
					public void setValues(PreparedStatement ps) throws SQLException {
						ps.setString(1, sms.getBranchids());
						ps.setString(2, sms.getCreateTime());
						ps.setString(3, sms.getCustomerids());
						ps.setLong(4, sms.getFlowordertype());
						ps.setBigDecimal(5, sms.getMoney());
						ps.setString(6, sms.getTemplatecontent());
						ps.setString(7, sms.getUsername());

						ps.setLong(8, sms.getId());
					}
				});

	}

}

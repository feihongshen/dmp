package cn.explink.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import cn.explink.domain.AutoIntowarehouseMessage;
import cn.explink.util.DateTimeUtil;

/**
 * 自动分拣线的消息记录dao
 * 
 * @author wangwei 2016年7月14日
 *
 */
@Component
public class AutoIntowarehouseMessageDAO {

	private final class AutoIntowarehouseMessageRowMapper implements
			RowMapper<AutoIntowarehouseMessage> {
		@Override
		public AutoIntowarehouseMessage mapRow(ResultSet rs, int rowNum)
				throws SQLException {
			AutoIntowarehouseMessage autoIntowarehouseMessage = new AutoIntowarehouseMessage();
			autoIntowarehouseMessage.setId(rs.getLong("id"));
			autoIntowarehouseMessage.setSerialNo(rs.getString("serialNo"));
			autoIntowarehouseMessage.setCwb(rs.getString("cwb"));
			autoIntowarehouseMessage
					.setSendContent(rs.getString("sendContent"));
			autoIntowarehouseMessage.setSendTime(rs.getString("sendTime"));
			autoIntowarehouseMessage.setReceiveContent(rs
					.getString("receiveContent"));
			autoIntowarehouseMessage
					.setReceiveTime(rs.getString("receiveTime"));
			autoIntowarehouseMessage.setHandleStatus(rs
					.getString("handleStatus"));

			autoIntowarehouseMessage.setCreatedByUser(rs
					.getString("created_by_user"));
			autoIntowarehouseMessage.setCreatedDtmLoc(DateTimeUtil
					.parseStringToDate(rs.getString("created_dtm_loc")));
			autoIntowarehouseMessage.setUpdatedByUser(rs
					.getString("updated_by_user"));
			autoIntowarehouseMessage.setUpdatedDtmLoc(DateTimeUtil
					.parseStringToDate(rs.getString("updated_dtm_loc")));

			return autoIntowarehouseMessage;
		}
	}

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public long creAutoIntowarehouseMessage(
			final AutoIntowarehouseMessage autoIntowarehouseMessage) {
		KeyHolder key = new GeneratedKeyHolder();
		this.jdbcTemplate.update(new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(
					java.sql.Connection con) throws SQLException {
				PreparedStatement ps = null;
				ps = con.prepareStatement(
						"insert into express_ops_auto_intowarehouse_message(id,serialNo,cwb,sendContent,sendTime,receiveContent,receiveTime,handleStatus,"
								+ "created_by_user,created_dtm_loc,updated_by_user,updated_dtm_loc) "
								+ "values(?,?,?,?,?,?,?,?," + "?,?,?,?)",
						new String[] { "id" });
				ps.setLong(1, autoIntowarehouseMessage.getId());
				ps.setString(2, autoIntowarehouseMessage.getSerialNo());
				ps.setString(3, autoIntowarehouseMessage.getCwb());
				ps.setString(4, autoIntowarehouseMessage.getSendContent());
				ps.setString(5, autoIntowarehouseMessage.getSendTime());
				ps.setString(6, autoIntowarehouseMessage.getReceiveContent());
				ps.setString(7, autoIntowarehouseMessage.getReceiveTime());
				ps.setString(8, autoIntowarehouseMessage.getHandleStatus());

				ps.setString(9, autoIntowarehouseMessage.getCreatedByUser());
				ps.setString(10, DateTimeUtil
						.parseDateToString(autoIntowarehouseMessage
								.getCreatedDtmLoc()));
				ps.setString(11, autoIntowarehouseMessage.getUpdatedByUser());
				ps.setString(12, DateTimeUtil
						.parseDateToString(autoIntowarehouseMessage
								.getUpdatedDtmLoc()));
				return ps;
			}
		}, key);
		autoIntowarehouseMessage.setId(key.getKey().longValue());
		return key.getKey().longValue();
	}

	public void saveAutoIntowarehouseMessage(
			final AutoIntowarehouseMessage autoIntowarehouseMessage) {
		this.jdbcTemplate
				.update("update express_ops_auto_intowarehouse_message "
						+ "set serialNo=?,cwb=?,sendContent=?,sendTime=?,receiveContent=?,receiveTime=?,handleStatus=?,"
						+ "created_by_user=?,created_dtm_loc=?,updated_by_user=?,updated_dtm_loc=? "
						+ "where id =? ", new PreparedStatementSetter() {

					@Override
					public void setValues(PreparedStatement ps)
							throws SQLException {
						ps.setString(1, autoIntowarehouseMessage.getSerialNo());
						ps.setString(2, autoIntowarehouseMessage.getCwb());
						ps.setString(3,
								autoIntowarehouseMessage.getSendContent());
						ps.setString(4, autoIntowarehouseMessage.getSendTime());
						ps.setString(5,
								autoIntowarehouseMessage.getReceiveContent());
						ps.setString(6,
								autoIntowarehouseMessage.getReceiveTime());
						ps.setString(7,
								autoIntowarehouseMessage.getHandleStatus());

						ps.setString(8,
								autoIntowarehouseMessage.getCreatedByUser());
						ps.setString(9, DateTimeUtil
								.parseDateToString(autoIntowarehouseMessage
										.getCreatedDtmLoc()));
						ps.setString(10,
								autoIntowarehouseMessage.getUpdatedByUser());
						ps.setString(11, DateTimeUtil
								.parseDateToString(autoIntowarehouseMessage
										.getUpdatedDtmLoc()));

						ps.setLong(12, autoIntowarehouseMessage.getId());
					}
				});
	}

	public AutoIntowarehouseMessage getAutoIntowarehouseMessageById(Long id) {
		String sql = "select * from express_ops_auto_intowarehouse_message where id =?";
		return this.jdbcTemplate.queryForObject(sql,
				new AutoIntowarehouseMessageRowMapper(), id);
	}

	public AutoIntowarehouseMessage getAutoIntowarehouseMessageBySerialNo(
			String serialNo) {
		String sql = "select * from express_ops_auto_intowarehouse_message where serialNo =?";
		return this.jdbcTemplate.queryForObject(sql,
				new AutoIntowarehouseMessageRowMapper(), serialNo);
	}

}

package cn.explink.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import cn.explink.domain.UpdateMessage;

@Component
public class UpdateMessageDAO {

	private final class UpdateMessageRowMapper implements RowMapper<UpdateMessage> {
		@Override
		public UpdateMessage mapRow(ResultSet rs, int rowNum) throws SQLException {
			UpdateMessage updatemessage = new UpdateMessage();
			updatemessage.setId(rs.getLong("id"));
			updatemessage.setMenuname(rs.getString("menuname"));
			updatemessage.setLastupdatetime(rs.getString("lastupdatetime"));
			updatemessage.setMenunvalue(rs.getLong("menunvalue"));
			return updatemessage;
		}
	}

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public void creUpdateMessage(String menuname, String lastupdatetime, long menunvalue) {
		String sql = "insert into set_updatemessage(menuname,lastupdatetime,menunvalue) values(?,?,?)";
		jdbcTemplate.update(sql, menuname, lastupdatetime, menunvalue);
	}

	public void saveUpdateMessageByMenunvalue(long menunvalue, String lastupdatetime) {
		String sql = "update set_updatemessage set lastupdatetime=? where menunvalue=?";
		jdbcTemplate.update(sql, lastupdatetime, menunvalue);
	}

	public UpdateMessage getUpdateMessageByMenunvalue(long menunvalue) {
		try {
			String sql = "select * from set_updatemessage where menunvalue=?";
			return jdbcTemplate.queryForObject(sql, new UpdateMessageRowMapper(), menunvalue);
		} catch (DataAccessException e) {
			return new UpdateMessage();
		}
	}

	public List<UpdateMessage> getUpdateMessageListByMenunvalue(long menunvalue) {
		String sql = "select * from set_updatemessage where menunvalue=?";
		return jdbcTemplate.query(sql, new UpdateMessageRowMapper(), menunvalue);
	}
}

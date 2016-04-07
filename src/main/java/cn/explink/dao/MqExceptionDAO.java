package cn.explink.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import cn.explink.dao.support.BasicJdbcTemplateDaoSupport;
import cn.explink.domain.MqException;

/**
 * 
 * MQ异常表处理DAO
 * @author jeef.fang
 *
 */
@Repository("mqExceptionDAO")
public class MqExceptionDAO extends BasicJdbcTemplateDaoSupport<MqException, Long> {
	public static final String pattern = "yyyy-MM-dd HH:mm:ss";
	public static final long nm = 1000 * 60;

	private Logger logger = LoggerFactory.getLogger(MqExceptionDAO.class);
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	public MqExceptionDAO() {
		super(MqException.class);
	}

	private final class MqExceptionRowMapper implements RowMapper<MqException> {
		@Override
		public MqException mapRow(ResultSet rs, int rowNum) throws SQLException {
			MqException mqException = new MqException();
			mqException.setId(rs.getLong("ID"));
			mqException.setExceptionCode(rs.getString("EXCEPTION_CODE"));
			mqException.setExceptionInfo(rs.getString("EXCEPTION_INFO"));
			mqException.setTopic(rs.getString("TOPIC"));
			mqException.setMessageBody(rs.getString("MESSAGE_BODY"));
			mqException.setMessageHeaderName(rs.getString("MESSAGE_HEADER_NAME"));
			mqException.setMessageHeader(rs.getString("MESSAGE_HEADER"));
			mqException.setHandleCount(rs.getInt("HANDLE_COUNT"));
			mqException.setRemarks(rs.getString("REMARKS"));
			mqException.setHandleTime(rs.getDate("HANDLE_TIME"));
			mqException.setCreatedByUser(rs.getString("CREATED_BY_USER"));
			mqException.setCreatedOffice(rs.getString("CREATED_OFFICE"));
			mqException.setCreatedDtmLoc(rs.getDate("CREATED_DTM_LOC"));
			mqException.setCreatedTimeZone(rs.getString("CREATED_TIME_ZONE"));
			mqException.setUpdatedByUser(rs.getString("UPDATED_BY_USER"));
			mqException.setUpdatedOffice(rs.getString("UPDATED_OFFICE"));
			mqException.setUpdatedDtmLoc(rs.getTimestamp("UPDATED_DTM_LOC"));
			mqException.setUpdatedTimeZone(rs.getString("UPDATED_TIME_ZONE"));
			mqException.setRouteingKey(rs.getString("ROUTEING_KEY"));
			return mqException;
		}
	}

	public List<MqException> listMqException() {
		List<MqException> resultList = null;
		try {
			String sql = "select * from mq_exception where handle_count > -1 and handle_count < 5 and is_deleted=0 limit 0,1000";
			resultList = getJdbcTemplate().query(sql, new MqExceptionRowMapper());
		}catch (Exception e) {
			this.logger.error("查询MQ异常列表", e);
			return resultList;
		}
		return resultList;
	}
}

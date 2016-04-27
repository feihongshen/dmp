package cn.explink.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import cn.explink.dao.support.BasicJdbcTemplateDaoSupport;
import cn.explink.domain.MqException;
import cn.explink.util.Page;

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
	
	public MqExceptionDAO() {
		super(MqException.class);
	}

	/**
	 * 重写save方法，如果uuid对应的记录存在，则说明是重发消息出错，则找到原来的消息，置为失败
	 */
	@Override
	public void save(MqException entity) {
		String uuid = entity.getMessageHeaderUUID();
		if(null != uuid && !"".equals(uuid)){
			MqException oldMqException = this.getMqExceptionByUUID(uuid);
			if(null != oldMqException){
				oldMqException.setHandleFlag(false);//失败
				oldMqException.setHandleTime(null);
				super.update(oldMqException);//更新
				return;
			}
		}
		
		super.save(entity);
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
			mqException.setMessageHeader(rs.getString("MESSAGE_HEADER"));
			mqException.setMessageHeaderUUID(rs.getString("MESSAGE_HEADER_UUID"));
			mqException.setHandleCount(rs.getInt("HANDLE_COUNT"));
			mqException.setHandleFlag(rs.getBoolean("HANDLE_FLAG"));
			mqException.setRemarks(rs.getString("REMARKS"));
			mqException.setHandleTime(rs.getTimestamp("HANDLE_TIME"));
			mqException.setMessageSource(rs.getString("MESSAGE_SOURCE"));
			mqException.setAutoResend(rs.getBoolean("IS_AUTO_RESEND"));
			mqException.setCreatedByUser(rs.getString("CREATED_BY_USER"));
			mqException.setCreatedOffice(rs.getString("CREATED_OFFICE"));
			mqException.setCreatedDtmLoc(rs.getTimestamp("CREATED_DTM_LOC"));
			mqException.setCreatedTimeZone(rs.getString("CREATED_TIME_ZONE"));
			mqException.setUpdatedByUser(rs.getString("UPDATED_BY_USER"));
			mqException.setUpdatedOffice(rs.getString("UPDATED_OFFICE"));
			mqException.setUpdatedDtmLoc(rs.getTimestamp("UPDATED_DTM_LOC"));
			mqException.setUpdatedTimeZone(rs.getString("UPDATED_TIME_ZONE"));
			mqException.setRouteingKey(rs.getString("ROUTEING_KEY"));
			return mqException;
		}
	}

	/**
	 * 查询需要重推的MQ异常 列表  (重发次数升序排列)
	 * @param executeCount
	 * @return
	 */
	public List<MqException> listMqException(int executeCount) {
		List<MqException> resultList = null;
		try {
			String sql = "select * from mq_exception where handle_flag=0 and is_auto_resend=1 and handle_count<5 and is_deleted=0"
					+ " order by created_dtm_loc desc,handle_count asc limit 0," + executeCount;
			resultList = getJdbcTemplate().query(sql, new MqExceptionRowMapper());
		}catch (Exception e) {
			this.logger.error("查询MQ异常列表", e);
			return resultList;
		}
		return resultList;
	}
	
	/**
	 * 根据条件查询 mq异常 列表 
	 * @param page
	 * @param exceptionCode
	 * @param topic
	 * @param handleFlag
	 * @return
	 */
	public List<MqException> getMqExceptionByWhere(long page, String exceptionCode, String topic, String handleFlag, String messageSource, 
			String isAutoResend, String createdDtmLocStart, String createdDtmLocEnd) {
		String sql = "SELECT * from mq_exception where 1=1";
		if (null != exceptionCode && !"".equals(exceptionCode.trim())) {
			sql += " and exception_code = '" + exceptionCode+ "'";
		}
		if (null != topic && !"".equals(topic.trim())) {
			sql += " and topic like '" + topic+ "%'";
		}
		if (null != handleFlag && !"".equals(handleFlag.trim())) {
			sql += " and handle_flag = " + handleFlag;
		}
		if (null != messageSource && !"".equals(messageSource.trim())) {
			sql += " and message_source = '" + messageSource + "'";
		}
		if (null != isAutoResend && !"".equals(isAutoResend.trim())) {
			sql += " and is_auto_resend = " + isAutoResend;
		}
		if (null != createdDtmLocStart && !"".equals(createdDtmLocStart.trim())) {
			sql += " and created_dtm_loc > '" + createdDtmLocStart + "'";
		}
		if (null != createdDtmLocEnd && !"".equals(createdDtmLocEnd.trim())) {
			sql += " and created_dtm_loc < '" + createdDtmLocEnd + "'";
		}

		sql += " and is_deleted=0 order by created_dtm_loc desc limit " + ((page - 1) * Page.ONE_PAGE_NUMBER) + " ," + Page.ONE_PAGE_NUMBER;

		List<MqException> cscList = getJdbcTemplate().query(sql, new MqExceptionRowMapper());
		return cscList;
	}
	
	/**
	 * 根据条件查询 mq异常 列表 (查总记录数)
	 * @param exceptionCode
	 * @param topic
	 * @param handleFlag
	 * @return
	 */
	public long getSystemInstallCount(String exceptionCode, String topic, String handleFlag, String messageSource, 
			String isAutoResend, String createdDtmLocStart, String createdDtmLocEnd) {
		String sql = "SELECT count(1) from mq_exception where 1=1";
		if (null != exceptionCode && !"".equals(exceptionCode.trim())) {
			sql += " and exception_code = '" + exceptionCode+ "'";
		}
		if (null != topic && !"".equals(topic.trim())) {
			sql += " and topic like '" + topic+ "%'";
		}
		if (null != handleFlag && !"".equals(handleFlag.trim())) {
			sql += " and handle_flag = " + handleFlag;
		}
		if (null != messageSource && !"".equals(messageSource.trim())) {
			sql += " and message_source = '" + messageSource + "'";
		}
		if (null != isAutoResend && !"".equals(isAutoResend.trim())) {
			sql += " and is_auto_resend = " + isAutoResend;
		}
		if (null != createdDtmLocStart && !"".equals(createdDtmLocStart.trim())) {
			sql += " and created_dtm_loc > '" + createdDtmLocStart + "'";
		}
		if (null != createdDtmLocEnd && !"".equals(createdDtmLocEnd.trim())) {
			sql += " and created_dtm_loc < '" + createdDtmLocEnd + "'";
		}
		sql += " and is_deleted=0";
		return getJdbcTemplate().queryForLong(sql);
	}
	
	/**
	 * 根据ID加载 对应的MQ 异常记录
	 * @param id
	 * @return
	 */
	public MqException getMqExceptionById(long id) {
		try {
			String sql = "select * from mq_exception where id=? ";
			return getJdbcTemplate().queryForObject(sql, new MqExceptionRowMapper(), id);
		} catch (DataAccessException e) {
			this.logger.error("根据ID加载MQ异常记录失败", e);
			return null;
		}
	}
	
	/**
	 * 根据MESSAGE_HEADER_UUID加载 对应的MQ 异常记录
	 * @param id
	 * @return
	 */
	private MqException getMqExceptionByUUID(String messageHeaderUUID) {
		try {
			String sql = "select * from mq_exception where message_header_uuid=? ";
			return getJdbcTemplate().queryForObject(sql, new MqExceptionRowMapper(), messageHeaderUUID);
		} catch (DataAccessException e) {
			this.logger.error("根据MESSAGE_HEADER_UUID加载MQ异常记录失败", e);
			return null;
		}
	}
	
	/**
	 * 加载所有的异常编码
	 * @param id
	 * @return
	 */
	public List<String> loadExceptionCode() {
		try {
			String sql = "select EXCEPTION_CODE from mq_exception group by EXCEPTION_CODE";
			return getJdbcTemplate().queryForList(sql, String.class);
		} catch (DataAccessException e) {
			this.logger.error("加载所有的异常编码", e);
			return null;
		}
	}
}

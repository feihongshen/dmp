package cn.explink.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import cn.explink.domain.SmsSend;
import cn.explink.enumutil.SmsSendManageEnum;
import cn.explink.util.Page;

@Component
public class SmsManageDao {

	@Autowired
	JdbcTemplate jdbcTemplate;

	public int getNumByCwbAndDeliverid(String cwb, long deliverid) {
		String sql = "SELECT COUNT(1) FROM sms_manage WHERE cwb=? AND deliverid=? ";
		return jdbcTemplate.queryForInt(sql, cwb, deliverid);
	}

	public void createInfo(String cwb, long deliverid, long flowordertype, long currentbranchid) {

		String sql = "INSERT INTO `sms_manage`(`cwb`,`deliverid`,`flowordertype`,`branchid`) VALUES ( ?,?,?,?) ";
		jdbcTemplate.update(sql, cwb, deliverid, flowordertype, currentbranchid);

	}

	public class SmsSendMangeMapper implements RowMapper<SmsSend> {

		@Override
		public SmsSend mapRow(ResultSet rs, int rowNum) throws SQLException {
			SmsSend smsSend = new SmsSend();
			smsSend.setId(rs.getLong("id"));
			smsSend.setSenddate(rs.getString("senddate"));
			smsSend.setRecipients(rs.getString("recipients"));
			smsSend.setConsigneemobile(rs.getString("consigneemobile"));
			smsSend.setSenddetail(rs.getString("senddetail"));
			smsSend.setNum(rs.getString("num"));
			smsSend.setSendstate(rs.getInt("sendstate"));
			smsSend.setErrorDetail(rs.getString("errorDetail"));
			smsSend.setUserid(rs.getLong("userid"));
			smsSend.setIp(rs.getString("ip"));
			return smsSend;
		}

	}

	/**
	 * 获取历史发送短信列表
	 * 
	 * @param startSenddate
	 *            开始时间
	 * @param stopSenddate
	 *            结束时间
	 * @param consigneemobile
	 *            手机号
	 * @param sendstate
	 *            发送状态 0为成功 1为失败 -1为全部
	 * @param page
	 *            当前第几页
	 * @return
	 */
	public List<SmsSend> getSendSmsList(String startSenddate, String stopSenddate, String consigneemobile, int sendstate, long page, int channel) {
		String sql = "SELECT * FROM sms_send_manage WHERE ";
		if (consigneemobile.trim().length() == 0) {
			sql += " senddate>='" + startSenddate + "' AND senddate<='" + stopSenddate + "' ";
		} else {
			sql += " consigneemobile='" + consigneemobile + "' ";
		}
		if (sendstate != SmsSendManageEnum.ALL.getValue()) {
			sql += " AND sendstate='" + sendstate + "'";
		}
		if (channel != -1) {
			sql += " AND channel='" + channel + "'";
		}
		sql += " ORDER BY senddate DESC LIMIT " + (page - 1) * Page.ONE_PAGE_NUMBER + " ," + Page.ONE_PAGE_NUMBER;
		return jdbcTemplate.query(sql, new SmsSendMangeMapper());
	}

	/**
	 * 获取历史发送短信列表总页数
	 * 
	 * @param startSenddate
	 *            开始时间
	 * @param stopSenddate
	 *            结束时间
	 * @param consigneemobile
	 *            手机号
	 * @param sendstate
	 *            发送状态 0为成功 1为失败 -1为全部
	 * @return
	 */
	public Long getSendSmsListCount(String startSenddate, String stopSenddate, String consigneemobile, int sendstate, int channel) {
		String sql = "SELECT count(1) FROM sms_send_manage WHERE ";
		if (consigneemobile.trim().length() == 0) {
			sql += " senddate>='" + startSenddate + "' AND senddate<='" + stopSenddate + "' ";
		} else {
			sql += " consigneemobile='" + consigneemobile + "' ";
		}
		if (sendstate != SmsSendManageEnum.ALL.getValue()) {
			sql += " AND sendstate='" + sendstate + "'";
		}
		if (channel != -1) {
			sql += " AND channel='" + channel + "'";
		}
		return jdbcTemplate.queryForLong(sql);
	}

	/**
	 * 创建一条短信发送记录
	 * 
	 * @param recipients
	 *            收信人
	 * @param consigneemobile
	 *            收信人手机
	 * @param senddetail
	 *            发送内容
	 * @param num
	 *            格式为 x/x 供几条 成功几条
	 * @param sendstate
	 *            发送状态
	 * @param errorDetail
	 *            错误详情
	 * @param userid
	 *            发送人ID
	 * @param ip
	 *            发送人IP
	 * @param changeSign
	 *            短信渠道签名
	 * @return id
	 */
	public Long creSendSms(final String recipients, final String consigneemobile, final String senddetail, final String num, final Long userid, final String ip, final int channel) {

		KeyHolder key = new GeneratedKeyHolder();
		jdbcTemplate.update(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(java.sql.Connection con) throws SQLException {
				PreparedStatement ps = null;
				ps = con.prepareStatement("INSERT INTO sms_send_manage  (`senddate`,`recipients`,`consigneemobile`,`senddetail`,`num`,`sendstate`,`errorDetail`,`userid`,`ip`,`channel`) "
						+ "values(now(),?,?,?,?, ?,?,?,?,?)", new String[] { "id" });
				ps.setString(1, recipients);
				ps.setString(2, consigneemobile);
				ps.setString(3, senddetail);
				ps.setString(4, num);
				ps.setInt(5, SmsSendManageEnum.Sending.getValue());
				ps.setString(6, "");
				ps.setLong(7, userid);
				ps.setString(8, ip);
				ps.setInt(9, channel);
				return ps;
			}
		}, key);
		return key.getKey().longValue();

	}

	/**
	 * 更改短信发送状态
	 * 
	 * @param sendstate
	 *            发送状态
	 * @param errorDetail
	 *            错误详情
	 * @param ids
	 *            短信发送记录ids
	 */
	public void updateSendSmsState(int sendstate, String errorDetail, String ids) {
		String sql = "UPDATE sms_send_manage SET `sendstate`=?,`errorDetail`=? where id IN " + ids;
		jdbcTemplate.update(sql, sendstate, errorDetail);
	}
}

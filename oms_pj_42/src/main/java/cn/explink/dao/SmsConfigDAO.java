package cn.explink.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import cn.explink.domain.SmsConfig;
import cn.explink.util.StringUtil;

@Component
public class SmsConfigDAO {

	private final class SmsConfigRowMapper implements RowMapper<SmsConfig> {
		@Override
		public SmsConfig mapRow(ResultSet rs, int rowNum) throws SQLException {
			SmsConfig smsConfig = new SmsConfig();
			smsConfig.setId(rs.getLong("id"));
			smsConfig.setName(StringUtil.nullConvertToEmptyString(rs.getString("name")));
			smsConfig.setPassword(StringUtil.nullConvertToEmptyString(rs.getString("password")));
			smsConfig.setWarningcount(rs.getLong("warningcount"));
			smsConfig.setPhone(StringUtil.nullConvertToEmptyString(rs.getString("phone")));
			smsConfig.setTemplet(StringUtil.nullConvertToEmptyString(rs.getString("templet")));
			smsConfig.setMonitor(rs.getLong("monitor"));
			smsConfig.setWarningcontent(StringUtil.nullConvertToEmptyString(rs.getString("warningcontent")));
			smsConfig.setIsOpen(rs.getLong("isOpen"));
			smsConfig.setTemplatecontent(StringUtil.nullConvertToEmptyString(rs.getString("templatecontent")));
			return smsConfig;
		}
	}

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public SmsConfig getAllSmsConfig() {
		try {
			return jdbcTemplate.queryForObject("select * from express_set_smsconfig", new SmsConfigRowMapper());
		} catch (DataAccessException e) {
			// TODO Auto-generated catch block
		}
		return null;
	}

	public void creSmsConfig(String name, String password, long warningcount, String phone, String templet, long monitor, String warningcontent, long isOpen, String templatecontent) {
		jdbcTemplate.update("insert into express_set_smsconfig(name,password,warningcount,phone,templet,monitor,warningcontent,isOpen,templatecontent) values(?,?,?,?,?,?,?,?,?)", name, password,
				warningcount, phone, templet, monitor, warningcontent, isOpen, templatecontent);
	}

	public void saveSmsConfig(String name, String password, long warningcount, String phone, String templet, long monitor, String warningcontent, long isOpen, String templatecontent, long id) {
		jdbcTemplate.update("update express_set_smsconfig set name=?,password=?,warningcount=?,phone=?,templet=?,monitor=?,warningcontent=?,isOpen=? ,templatecontent=? where id =?", name, password,
				warningcount, phone, templet, monitor, warningcontent, isOpen, templatecontent, id);
	}

}

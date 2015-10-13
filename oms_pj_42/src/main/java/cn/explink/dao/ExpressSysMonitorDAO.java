package cn.explink.dao;

import java.text.ParseException;
import java.util.Date;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import cn.explink.dao.support.BasicJdbcTemplateDaoSupport;
import cn.explink.domain.ExpressSysMonitor;
import cn.explink.enumutil.ExpressSysMonitorEnum;

@Repository("expressSysMonitorDAO")
public class ExpressSysMonitorDAO extends BasicJdbcTemplateDaoSupport<ExpressSysMonitor, Long> {
	public static final String pattern = "yyyy-MM-dd HH:mm:ss";
	public static final long nm = 1000 * 60;

	public ExpressSysMonitorDAO() {
		super(ExpressSysMonitor.class);
	}

	public ExpressSysMonitor getMaxOpt(String type) {
		ExpressSysMonitor m = new ExpressSysMonitor();
		String sql = "SELECT * FROM express_sys_monitor  a WHERE a.dealflag=0 and a.type=? order by a.optime desc limit 0,1 ";
		try {
			m = getJdbcTemplate().queryForObject(sql.toString(), new Object[] { type }, createRowMapper());
		} catch (DataAccessException e) {
			m.setOptime(DateFormatUtils.format(new Date(-1), pattern));
			e.printStackTrace();
		}
		return m;
	}

	public void chooise(ExpressSysMonitorEnum type, long endtime) {
		ExpressSysMonitor m = getMaxOpt(type.getText());
		try {
			Date nextExecuteTime = DateUtils.parseDate(m.getOptime(), pattern);
			long delay = nextExecuteTime.getTime() - endtime;
			if (delay <= type.getValue()) {
				ExpressSysMonitor expressSysMonitor = new ExpressSysMonitor();
				expressSysMonitor.setType(type.getText());
				expressSysMonitor.setOptime(DateFormatUtils.format(new Date(), pattern));
				save(expressSysMonitor);
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public String minutesBetween(ExpressSysMonitorEnum type) throws ParseException {
		ExpressSysMonitor m = getMaxOpt(type.getText());
		long diff = new Date().getTime() - DateUtils.parseDate(m.getOptime(), pattern).getTime();
		return String.valueOf(diff / nm);
	}

	public void updateDealFlag(ExpressSysMonitor expressSysMonitor) {
		String sql = "UPDATE express_sys_monitor SET dealflag = 1 WHERE dealflag = 0 AND type = '" + expressSysMonitor.getType() + "' AND optime <= '" + expressSysMonitor.getOptime() + "'";
		try {
			getJdbcTemplate().update(sql);
		} catch (DataAccessException e) {
			e.printStackTrace();
		}
	}

}

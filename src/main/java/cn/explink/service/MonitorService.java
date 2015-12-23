package cn.explink.service;

import java.util.Calendar;

import javax.mail.MessagingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.explink.dao.ScheduledTaskDAO;
import cn.explink.dao.SystemInstallDAO;
import cn.explink.domain.SystemInstall;
import cn.explink.schedule.Constants;
import cn.explink.util.EmailUtil;
import cn.explink.util.ResourceBundleUtil;

@Service
public class MonitorService {

	private static Logger logger = LoggerFactory.getLogger(MonitorService.class);

	@Autowired
	private ScheduledTaskDAO scheduledTaskDao;

	@Autowired
	private SystemInstallDAO systemInstallDao;

	public void monitorScheduledTasks() {
		if (ResourceBundleUtil.LABEL != null && (ResourceBundleUtil.LABEL.equals("local") || ResourceBundleUtil.LABEL.equals("ExecelImportAndValidateUtilTest") || ResourceBundleUtil.LABEL.startsWith("version"))) {
			logger.info("ignore monitor in local/ExecelImportAndValidateUtilTest/version env.");
			return;
		}
		String[] taskTypes = new String[] { Constants.TASK_TYPE_ORDER_FLOW_EDIT_SHOW_INFO, Constants.TASK_TYPE_ORDER_FLOW_OMS1, Constants.TASK_TYPE_ORDER_FLOW_OMSB2C,
				Constants.TASK_TYPE_ORDER_FLOW_RECIEVE_GOODS, Constants.TASK_TYPE_ORDER_FLOW_SMS, Constants.TASK_TYPE_ORDER_FLOW_TRANSCWB };
		Calendar time = Calendar.getInstance();
		time.add(Calendar.HOUR, -1);
		int timeOutTasks = scheduledTaskDao.countTimeOutTasks(taskTypes, time.getTime());
		if (timeOutTasks > 0) {
			SystemInstall mailAddressConfig = systemInstallDao.getSystemInstallByName("taskMonitorMailAddress");
			if (mailAddressConfig == null || mailAddressConfig.getValue() == null || mailAddressConfig.getValue().trim().isEmpty()) {
				logger.warn("no monitor mail address config. please set system param taskMonitorMailAddress");
				return;
			}
			String[] mailAddress = mailAddressConfig.getValue().split(",");
			String message = ResourceBundleUtil.LABEL + "共" + timeOutTasks + "个任务超时1小时以上未完成";
			try {
				logger.info("sending alarm mail to {}", mailAddressConfig.getValue());
				EmailUtil.sendMail(mailAddress, "易普联科任务超时报警", message);
			} catch (MessagingException e) {
				logger.error("alarm to {} failed.", mailAddressConfig.getValue(), e);
			}
		}
	}

}

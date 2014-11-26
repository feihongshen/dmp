package cn.explink.schedule.worker;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.explink.dao.SystemInstallDAO;
import cn.explink.domain.ScheduledTask;
import cn.explink.domain.SystemInstall;
import cn.explink.schedule.ScheduledWorker;
import cn.explink.service.ScheduledTaskService;
import cn.explink.util.DateTimeUtil;

@Service("scheduledTaskCleanUpWorker")
public class ScheduledTaskCleanUpWorker extends ScheduledWorker {

	private static final int DEFAULT_OLD_TASK_KEEP_DAY = 15;

	private Logger logger = LoggerFactory.getLogger(ScheduledTaskCleanUpWorker.class);

	@Autowired
	private ScheduledTaskService scheduledTaskService;

	@Autowired
	private SystemInstallDAO systemInstallDAO;

	@Override
	protected boolean doJob(ScheduledTask scheduledTask) throws Exception {
		try {
			SystemInstall oldTaskKeepDayConfig = systemInstallDAO.getSystemInstallByName("oldTaskKeepDay");
			int day = DEFAULT_OLD_TASK_KEEP_DAY;
			if (oldTaskKeepDayConfig != null) {
				day = Integer.parseInt(oldTaskKeepDayConfig.getValue());
			}
			scheduledTaskService.cleanUpOldTasks(day);

			Date nextJobTime = DateTimeUtil.getDateAfterToday(1);
			scheduleNextTask(scheduledTask, nextJobTime);
			return true;
		} catch (Exception e) {
			logger.error("clean up old tasks failed due to " + e.getMessage(), e);
			return false;
		}
	}

}

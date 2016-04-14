package cn.explink.schedule.worker;

import java.util.Calendar;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.explink.domain.ScheduledTask;
import cn.explink.schedule.ScheduledWorker;

@Service("synAddressTaskWorker")
public class SynAddressTaskWorker extends ScheduledWorker {

	private Logger logger = LoggerFactory.getLogger(SynAddressTaskWorker.class);

	@Autowired
	private SynAddressTaskWrapper synaddresswrapper;

	@Override
	protected boolean doJob(ScheduledTask scheduledTask) throws Exception {
		try {
			String result = (String) synaddresswrapper.doSomething(scheduledTask);
			if (result == null || !result.trim().equals("success")) {
				logger.warn("doJob failed, result = {}", result);
				return false;
			}
			return true;
		} catch (Exception e) {
			logger.error("syn address failed du to " + e.getMessage(), e);
			return false;
		}
	}

	/**
	 * 覆盖父类默认的失败处理机制，增加重试延迟递增算法。
	 */
	protected void handleFailedTask(ScheduledTask scheduledTask) {
		super.handleFailedTask(scheduledTask);
		// 延迟时间 = 10 minutes * 2 ^ 重试次数
		// int delayMinutes = (int) (Math.pow(2, scheduledTask.getTryCount() -
		// 1) * 10);
		int delayMinutes = 10;
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.MINUTE, delayMinutes);
		scheduledTask.setFireTime(calendar.getTime());
	}
}

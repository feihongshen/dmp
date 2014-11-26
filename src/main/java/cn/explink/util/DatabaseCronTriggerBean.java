package cn.explink.util;

import java.io.Serializable;
import java.text.ParseException;

import org.quartz.CronTrigger;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.Trigger;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.scheduling.quartz.CronTriggerBean;

public class DatabaseCronTriggerBean extends CronTriggerBean implements Serializable {

	private static final long serialVersionUID = 4980168080456291527L;

	private static SchedulerFactory sf = new StdSchedulerFactory();

	private static String TRIGGER_GROUP_NAME = "trigger";

	public static void modifyJobTime(JobDetail jobDetail, String time) throws SchedulerException, ParseException {
		Scheduler sched = sf.getScheduler();
		Trigger trigger = sched.getTrigger(jobDetail.getName(), TRIGGER_GROUP_NAME);
		if (trigger != null) {
			CronTrigger ct = (CronTrigger) trigger;
			// 移除当前进程的Job
			sched.deleteJob(jobDetail.getName(), jobDetail.getGroup());
			// 修改Trigger
			ct.setCronExpression(time);
			// 重新调度jobDetail
			sched.scheduleJob(jobDetail, ct);
		}
	}

	@Override
	public void afterPropertiesSet() throws ParseException {
		try {

			System.out.println("自定义");

			super.afterPropertiesSet();

			// this.setCronExpression("0 0 10,14,16 * * ?");

			// this.setCronExpression("0 45 11 ? * *");

			// this.setCronExpression("0/50 * * * * ? *");
			this.setCronExpression("0 45 11 ? * *");

			System.out.println(this.getCronExpression());

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (this.getCronExpression() == null) {
			this.setCronExpression("");
		}
		System.out.println(this.getCronExpression());
	}

}

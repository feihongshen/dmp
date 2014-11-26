package cn.explink.schedule;

import java.util.HashSet;
import java.util.Set;

/**
 * 任务调度运行环境
 */
public class ScheduledTaskEnv {

	private static ScheduledTaskEnv instance = new ScheduledTaskEnv();

	/**
	 * 已经提交到线程池的任务id
	 */
	private Set<Long> taskIds = new HashSet<Long>();

	private ScheduledTaskEnv() {
	}

	public static ScheduledTaskEnv getInstance() {
		return instance;
	}

	public void addTask(Long taskId) {
		taskIds.add(taskId);
	}

	public void removeTask(Long taskId) {
		taskIds.remove(taskId);
	}

	public boolean hasTask(Long taskId) {
		return taskIds.contains(taskId);
	}

}

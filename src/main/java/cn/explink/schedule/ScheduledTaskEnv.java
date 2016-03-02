package cn.explink.schedule;

import java.util.HashSet;
import java.util.Set;

import cn.explink.util.RedisMap;
import cn.explink.util.impl.RedisMapImpl;

/**
 * 任务调度运行环境，引入CacheManager改造成分布式缓存，后续待完善。
 */
public class ScheduledTaskEnv {

	private static ScheduledTaskEnv instance = new ScheduledTaskEnv();

	/**
	 * 已经提交到线程池的任务id
	 */
	// private Set<Long> taskIds = new HashSet<Long>();
	private RedisMap<Long, Long> taskIds = new RedisMapImpl<Long, Long>("ScheduledTaskEnv");

	private ScheduledTaskEnv() {
	}

	public static ScheduledTaskEnv getInstance() {
		return instance;
	}

	public void addTask(Long taskId) {
		taskIds.put(taskId, taskId);
	}

	public void removeTask(Long taskId) {
		taskIds.remove(taskId);
	}

	public boolean hasTask(Long taskId) {
		Long id = taskIds.get(taskId);
		if (id != null) {
			return true;
		}
		return false;
	}

}

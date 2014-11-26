package cn.explink.schedule;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * thread pool manager
 */
public class ExecutorManager {

	private static ExecutorManager instance;

	private Map<String, ExecutorService> executors = new HashMap<String, ExecutorService>();

	private ExecutorManager() {
	}

	public static ExecutorManager getInstance() {
		if (instance == null) {
			synchronized (ExecutorManager.class) {
				if (instance == null) {
					instance = new ExecutorManager();
					instance.init();
				}
			}
		}
		return instance;
	}

	/**
	 * 初始化各种任务的线程池
	 */
	private void init() {
		// order flow任务专用线程池
		ExecutorService executor = Executors.newFixedThreadPool(Constants.TASK_THREAD_POOL_SIZE_ORDER_FLOW);
		executors.put(Constants.TASK_TYPE_ORDER_FLOW_EDIT_SHOW_INFO, executor);
		executors.put(Constants.TASK_TYPE_ORDER_FLOW_OMS1, executor);
		executors.put(Constants.TASK_TYPE_ORDER_FLOW_OMSB2C, executor);
		executors.put(Constants.TASK_TYPE_ORDER_FLOW_RECIEVE_GOODS, executor);
		executors.put(Constants.TASK_TYPE_ORDER_FLOW_SMS, executor);
		executors.put(Constants.TASK_TYPE_ORDER_FLOW_TRANSCWB, executor);

		// 公共线程池
		executor = Executors.newFixedThreadPool(Constants.COMMON_TASK_THREAD_POOL_SIZE);
		executors.put(Constants.COMMON_TASK_THREAD_POOL_NAME, executor);
	}

	public ExecutorService getExecutorService(String name) {
		return executors.get(name);
	}
}

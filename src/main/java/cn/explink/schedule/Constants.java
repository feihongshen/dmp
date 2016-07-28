package cn.explink.schedule;

public class Constants {

	/**
	 * 任务状态 - 初始化
	 */
	public static final int TASK_STATUS_INITIALIZED = 0;

	/**
	 * 任务状态 - 已完成
	 */
	public static final int TASK_STATUS_COMPLETED = 1;

	/**
	 * 任务状态 - 已取消
	 */
	public static final int TASK_STATUS_CANCELED = 2;

	/**
	 * 执行调度任务的线程池名字
	 */
	public static final String COMMON_TASK_THREAD_POOL_NAME = "scheduledTaskThreadPool";

	/**
	 * 执行调度任务的线程池大小
	 */
	public static final int COMMON_TASK_THREAD_POOL_SIZE = 15;

	/**
	 * Order flow 任务的线程池大小
	 */
	public static final int TASK_THREAD_POOL_SIZE_ORDER_FLOW = 10;

	/**
	 * 任务类型 - order flow task, SAVE
	 */
	public static final String TASK_TYPE_ORDER_FLOW_OMS1 = "save.orderFlow";

	/**
	 * 任务类型 - order flow task, OMSB2C
	 */
	public static final String TASK_TYPE_ORDER_FLOW_OMSB2C = "omsb2c.orderFlow";

	/**
	 * 任务类型 - order flow task, editshowinfo
	 */
	public static final String TASK_TYPE_ORDER_FLOW_EDIT_SHOW_INFO = "editshowinfo.orderFlow";

	/**
	 * 任务类型 - order flow task, receivegoods
	 */
	public static final String TASK_TYPE_ORDER_FLOW_RECIEVE_GOODS = "receivegoods.orderFlow";

	/**
	 * 任务类型 - order flow task, sms
	 */
	public static final String TASK_TYPE_ORDER_FLOW_SMS = "sms.orderFlow";

	/**
	 * 任务类型 - order flow task, transcwb
	 */
	public static final String TASK_TYPE_ORDER_FLOW_TRANSCWB = "transcwb.orderFlow";

	/**
	 * 任务类型 - order flow task, account
	 */
	public static final String TASK_TYPE_ORDER_FLOW_ACCOUNT = "account.orderFlow";

	/**
	 * 任务类型 - 清除旧的已完成任务 cleanUpOldTask
	 */
	public static final String TASK_TYPE_CLEAN_UP_OLD_TASK = "cleanUpOldTask";

	/**
	 * 引用数据类型 - orderFlowId
	 */
	public static final String REFERENCE_TYPE_ORDER_FLOW_ID = "orderFlowId";

	/**
	 * ADD BY WANGYCH START
	 */
	/**
	 * 任务类型 - address syn task, user
	 */
	public static final String TASK_TYPE_SYN_ADDRESS_USER_CREATE = "synaddressusercreat";

	/**
	 * 任务类型 - address syn task, user
	 */
	public static final String TASK_TYPE_SYN_ADDRESS_USER_MODIFY = "synaddressuserrmodify";

	/**
	 * 任务类型 - address syn task, user
	 */
	public static final String TASK_TYPE_SYN_ADDRESS_USER_DELETE = "synaddressuserdelete";

	/**
	 * 任务类型 - address syn task, branch
	 */
	public static final String TASK_TYPE_SYN_ADDRESS_BRANCH_CREATE = "synaddressbranchcreat";

	/**
	 * 任务类型 - address syn task, branch
	 */
	public static final String TASK_TYPE_SYN_ADDRESS_BRANCH_MODIFY = "synaddressbranchrmodify";

	/**
	 * 任务类型 - address syn task, branch
	 */
	public static final String TASK_TYPE_SYN_ADDRESS_BRANCH_DELETE = "synaddressbranchdelete";

	/**
	 * 任务类型 - address syn task, customer
	 */
	public static final String TASK_TYPE_SYN_ADDRESS_CUSTOMER_CREATE = "synaddresscustomercreat";

	/**
	 * 任务类型 - address syn task, customer
	 */
	public static final String TASK_TYPE_SYN_ADDRESS_CUSTOMER_MODIFY = "synaddresscustomermodify";

	/**
	 * 任务类型 - address syn task, customer
	 */
	public static final String TASK_TYPE_SYN_ADDRESS_CUSTOMER_DELETE = "synaddresscustomerdelete";

	/**
	 * 引用数据类型 - userId
	 */
	public static final String REFERENCE_TYPE_USER_ID = "userId";

	/**
	 * 引用数据类型 - branchId
	 */
	public static final String REFERENCE_TYPE_BRANCH_ID = "branchId";

	/**
	 * 引用数据类型 - customerId
	 */
	public static final String REFERENCE_TYPE_CUSTOMER_ID = "customerId";
	/**
	 * ADD BY WANGYCH END
	 */
	
	/*
	 * 登录来源是否为PDA
	 */
	public static final String IS_ACCESS_SOURCE_FROM_PDA = "isAccessSourceFromPDA";
	
	/*
	 * 入库类型
	 */
	public static final byte INTOWAHOUSE_TYPE_INTOWAHOUSE = 1;			//分拣库入库
	public static final byte INTOWAHOUSE_TYPE_CHANGE_INTOWAHOUSE = 2;	//中转库入库
	
	/*
	 * 武汉自动化分拨报文处理状态
	 */
	public static final byte AUTO_ALLOC_STATUS_UNSENT = 0;			//未发送
	public static final byte AUTO_ALLOC_STATUS_SENT = 1;			//已发送
	public static final byte AUTO_ALLOC_STATUS_HANDLE_SUCCESS = 2;	//处理成功
	public static final byte AUTO_ALLOC_STATUS_HANDLE_FAIL = 3;		//处理失败
}

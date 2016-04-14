package cn.explink.service.addressmatch;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.explink.dao.AddressMatchLogDAO;
import cn.explink.domain.AddressMatchLog;

/**
 * 匹配地址日志收集器
 * @author neo01.huang
 * 2016-4-13
 */
public class AddressMatchLogCollector extends Thread {

	private static final Logger logger = LoggerFactory.getLogger(AddressMatchLogCollector.class);

	/**扫描时间间隔*/
	public static final int SCAN_INTERVAL = 5000;
	
	/**线程池 core size*/
	public static final int POOL_CORE_SIZE = 3;
	
	/**线程池 max size*/
	public static final int POOL_MAX_SIZE = 10;
	
	/**线程池 queue size*/
	public static final int POOL_QUEUE_SIZE = 100000;
	
	/**缓冲区最大size*/
	public static final int BUFFER_MAX_SIZE = 5000;
	
	/**内部实例*/
	private static AddressMatchLogCollector innerThread;
	/**
	 * 批量操作线程池
	 */
	private static ThreadPoolExecutor pool;
	
	/**
	 * 批量操作缓冲区
	 */
	private static final BlockingQueue<AddressMatchLog> buffer = new LinkedBlockingQueue<AddressMatchLog>();
	
	/**
	 * 是否初始化 
	 */
	private static volatile Boolean isInit = false;
	
	/**随机数*/
	private static final Random RANDOM = new Random();
	
	private static AddressMatchLogDAO addressMatchLogDAO;
	
	/**缓冲区检测器*/
	private static Thread addressMatchLogCollectorBufferDetector = new Thread(new Runnable() {
		@Override
		public void run() {
			try {
				while (true) {
					
					if (buffer.size() > BUFFER_MAX_SIZE) {
						//消费
						consume();
					}
					
					try {
						Thread.sleep(207);
					} catch (InterruptedException e) {
						throw new RuntimeException(e);
					}
					
				}
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			}
		}
	}); 
	
	/**
	 * 初始化
	 * @param innerAddressMatchLogDAO
	 */
	public static void init(AddressMatchLogDAO innerAddressMatchLogDAO) {
		if (!isInit) {
			synchronized (isInit) {
				if (!isInit) {
					
					addressMatchLogDAO = innerAddressMatchLogDAO;
					pool = new ThreadPoolExecutor(POOL_CORE_SIZE, 
							POOL_MAX_SIZE, 120, 
							TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(POOL_QUEUE_SIZE));
					
					innerThread = new AddressMatchLogCollector();
					//启动收集器
					innerThread.start();
					//启动缓冲区检测器
					addressMatchLogCollectorBufferDetector.start();
					
					isInit = true;
				}
			}
		}
	}
	
	
	@Override
	public void run() {
		try {
			while (true) {
				
				//消费
				consume();
				
				try {
					Thread.sleep(SCAN_INTERVAL);
				} catch (InterruptedException e) {
					throw new RuntimeException(e);
				}
				
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		
	}
	
	/**
	 * 消费
	 */
	public static synchronized void consume() {
		int bufferSize = buffer.size();
		
		List<AddressMatchLog> list = new ArrayList<AddressMatchLog>(bufferSize);
		
		for (int i = 0; i < bufferSize; i++) {
			AddressMatchLog record = null;
			try {
				record = buffer.take();
			} catch (InterruptedException e) {
				throw new RuntimeException(e);
			}
			list.add(record);
		}
		
		if (bufferSize != 0) {
			
			//批量操作
			batchOperation(list);
		}
	}
	
	/**
	 * 批量操作
	 * @param list
	 */
	private static void batchOperation(final List<AddressMatchLog> list) {
		//logger.info("consume address match log size:{}", list.size());
		pool.submit(new Runnable() {
			public void run() {
				try {
					addressMatchLogDAO.insertBatch(list);
				} catch (Exception e) {
					logger.error(e.getMessage(), e);
				}
			}
		});
		
	}
	
	/**
	 * 生产 地址匹配日志
	 * @param addressMatchLog 地址匹配日志
	 * @param addressMatchLogDAO 地址匹配日志DAO
	 */
	public static void produce(AddressMatchLog addressMatchLog, AddressMatchLogDAO addressMatchLogDAO) {
		try {
			Thread.sleep(RANDOM.nextInt(10) + 5);
			buffer.put(addressMatchLog);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}
	
}

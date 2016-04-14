package cn.explink.b2c.auto.order.mq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * author : yuxin.he
 * desc : 系统启动自动加载消费者
 * */
public class AutoInitProcessor {

	private static Logger logger = LoggerFactory.getLogger(AutoInitProcessor.class);

	cn.explink.b2c.auto.order.mq. ConsumerStarter consumerStarter;
	
	public ConsumerStarter getConsumerStarter() {
		return consumerStarter;
	}

	public void setConsumerStarter(ConsumerStarter consumerStarter) {
		this.consumerStarter = consumerStarter;
	}

	public void autoInit()
	{
		try {
			consumerStarter.start();
		} catch (Exception e) {
			logger.error("", e);
		}
		
	}
	
	
	public void autoDestroy(){
		try {
			consumerStarter.destroy();
		} catch (Exception e) {
			logger.error("", e);
		}
	}
	
}

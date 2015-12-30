package cn.explink.b2c.auto.order.mq;

import javax.annotation.Resource;

/**
 * author : yuxin.he
 * desc : 系统启动自动加载消费者
 * */
public class AutoInitProcessor {



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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
	public void autoDestroy(){
		try {
			consumerStarter.destroy();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}

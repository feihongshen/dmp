package cn.explink.b2c.auto.order.mq;



import com.vip.platform.middleware.vms.SubQoS;
import com.vip.platform.middleware.vms.VMSClient;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class ConsumerStarter {
    /**
     * 建议connectionFactory交由spring做单例管理，保证只通过一个connection连接rabbitmq server。
     */
     
	//@Resource
   

    Logger logger = LoggerFactory.getLogger(ConsumerStarter.class);
    
    
	public List<ConsumerTemplate> getCallBackList() {
		return callBackList;
	}

	public void setCallBackList(List<ConsumerTemplate> callBackList) {
		this.callBackList = callBackList;
	}

	private List<ConsumerTemplate> callBackList;
	

	
    //private ConsumerContainer consumerContainer;
 
//    @PostConstruct
    public void start() throws Exception{
    	
        try {
			//consumerContainer = new ConsumerContainer(connectionFactory);
			VMSClient client= new VMSClient();
			/*
			  可同时启动多个consumer实例并行执行。每个实例默认的prefetch account是5，显式ack。
			     如果需要修改prefetch account或自动ack，请调用相应的重载接口。建议采取默认需要显式ack，这样，消息消费出错还能后续处理
			     请注意显式ack已由fastrabbit 的框架代码实现，业务代码中不需要实现
			 */
			for(ConsumerTemplate rabbitTest: callBackList){
				client.subscribe(rabbitTest.getQueueName(), SubQoS.build().prefetchCount(rabbitTest.getFetchCount()).autoCommit(rabbitTest.getAutoCommit()), rabbitTest.getCallBack());
				
				//?????????
				logger.info("consumer started sucess,queueName={},prefetchCount={},callback={}:"+rabbitTest.getQueueName()+","+rabbitTest.getFetchCount()+","+rabbitTest.getCallBack().toString());
			}
			//consumerContainer.startAllConsumers();
		} catch (Exception e) {
			logger.error("rabbit mq start error:",e);
			throw e;
		}
    }

    /**
     * 容器退出时释放channel资源
     * @throws Exception
     */
//    @PreDestroy
    public void destroy() throws Exception{
      

    }
}

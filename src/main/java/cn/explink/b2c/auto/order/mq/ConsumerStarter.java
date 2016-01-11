package cn.explink.b2c.auto.order.mq;



import com.vip.platform.middleware.vms.ISubscriber;
import com.vip.platform.middleware.vms.SubQoS;
import com.vip.platform.middleware.vms.VMSClient;

import cn.explink.b2c.tools.JointService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;


public class ConsumerStarter implements ApplicationListener<ContextRefreshedEvent> {
    /**
     * 建议connectionFactory交由spring做单例管理，保证只通过一个connection连接rabbitmq server。
     */
     
	//@Resource
   

    private Logger logger = LoggerFactory.getLogger(ConsumerStarter.class);
    
	
	@Autowired
	private MqConfigService mqConfigService;
    
	public List<ConsumerTemplate> getCallBackList() {
		return callBackList;
	}

	public void setCallBackList(List<ConsumerTemplate> callBackList) {
		this.callBackList = callBackList;
	}

	public List<AutoExceptionSender> getSenderList() {
		return senderList;
	}

	public void setSenderList(List<AutoExceptionSender> senderList) {
		this.senderList = senderList;
	}

	private List<ConsumerTemplate> callBackList;
	
	private List<AutoExceptionSender> senderList;
	
	private Map<String,ISubscriber> subscriberMap=new HashMap<String,ISubscriber>();
	
	private static final String MQ_OPEN_KEY="MQ_OPEN_FLAG";
	
    //private ConsumerContainer consumerContainer;
	
	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		try {
			logger.info("Spring context event start");
			if(event.getApplicationContext().getParent() == null){
				logger.info("Spring onApplicationEvent start");
				start();
				logger.info("Spring onApplicationEvent end");
			}
		} catch (Exception e) {
			logger.error("Spring onApplicationEvent start() error:",e);
		}
	}
 
//    @PostConstruct
    public void start() throws Exception{
    	
        try {
        	//int state=this.jointService.getStateForJoint(B2cEnum.VipShop_TPSAutomate.getKey());//
        	int state=0;
        	
        	try{
	        	String value=mqConfigService.getValue(MQ_OPEN_KEY);
	        	if(value!=null&&value.length()>0){
	        		state=Integer.parseInt(value);
	        	}
	        } catch (Exception e) {
	        	logger.error("get MQ_OPEN_FLAG parameter error:",e);
	        }
        	
        	if(state==0){
        		logger.info("do NOT connect to rabbit mq,state={}",state);
        		throw new Exception("连接MQ服务器的开关未打开");
        		//return;//
        	}
        	
        	logger.info("start to load mq config...");
        	initMqConfig();
        	logger.info("completed to load mq config.");
        	
        	logger.info("start to connect to rabbit mq...");
        	

			//VMSClient client= VMSClient.getDefault();
			VMSClient client= new VMSClient();
			
			/*
			  可同时启动多个consumer实例并行执行。每个实例默认的prefetch account是5，显式ack。
			     如果需要修改prefetch account或自动ack，请调用相应的重载接口。建议采取默认需要显式ack，这样，消息消费出错还能后续处理
			     请注意显式ack已由fastrabbit 的框架代码实现，业务代码中不需要实现
			 */

			for(ConsumerTemplate template: callBackList){
				ISubscriber subscriber=client.subscribe(template.getQueueName(), SubQoS.build().prefetchCount(template.getFetchCount()).autoCommit(template.getAutoCommit()), template.getCallBack());
				subscriberMap.put(template.getQueueKey(), subscriber);
				logger.info("MQ subscriber started sucessfully! queueName="+template.getQueueName()+",prefetchCount="+template.getFetchCount()+",autoCommit="+template.getAutoCommit());
			}

			logger.info("Completed to connect to rabbit mq.");
			
		} catch (Exception e) {
			logger.error("rabbit mq start error:",e);
			throw e;
		}
    }
    
    public void stop() throws Exception{
    	boolean error=false;
    	Iterator it=subscriberMap.keySet().iterator();
        while(it.hasNext()){
        	try{
        		String key=(String)it.next();
        		ISubscriber subscriber=subscriberMap.get(key);
        		logger.info("start to stop MQ subscriber...");
        		if(subscriber!=null){
        			subscriber.shutdown();
        		}
        		logger.info("completed to stop MQ subscriber");
	        } catch (Exception e) {
	        	error=true;
				logger.error("MQ subscriber stop error:",e);
			}
        }
        subscriberMap.clear();
        
        logger.info("Completed to stop all MQ subscribers");

        if(error){
        	throw new Exception("Stopping MQ subscriber have error,adivse restart whole web application.");
        }
    }
    
    public Map<String,String> getMqSubscribers(){
    	Map<String,String> queueMap=new HashMap<String,String>();
    	Iterator it=subscriberMap.keySet().iterator();
        while(it.hasNext()){
        	String key=(String)it.next();
        	queueMap.put(key, key);
        }
        return queueMap;
    }

    /**
     * 容器退出时释放channel资源
     * @throws Exception
     */
//    @PreDestroy
    public void destroy() throws Exception{
      

    }

    //从DB读取MQ配置
    private void initMqConfig(){
    	mqConfigService.initMqConfig(this.callBackList, this.senderList);
    }
}

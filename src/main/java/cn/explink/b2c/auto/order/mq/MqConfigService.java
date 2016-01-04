package cn.explink.b2c.auto.order.mq;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class MqConfigService {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private JdbcTemplate jdbcTemplate;

	private static final String MQ_CONFIG_SQL="select text from express_auto_param_config where name=?";
	
	public void initMqConfig(List<ConsumerTemplate> callBackList, List<AutoExceptionSender> senderList){
		for(ConsumerTemplate callBack:callBackList){
			//channel
			String channel=getValue(callBack.getExchangeName());
			if(channel==null){
				throw new RuntimeException("MQ parameter "+callBack.getExchangeName()+"is not found");
			}else{
				callBack.setExchangeName(channel);
			}
			
			//queue
			String queue=getValue(callBack.getQueueName());
			if(queue==null){
				throw new RuntimeException("MQ parameter "+callBack.getQueueName()+"is not found");
			}else{
				callBack.setQueueName(queue);
			}
		}
		
		for(AutoExceptionSender sender:senderList){
			//channel
			String channel=getValue(sender.getChannel());
			if(channel==null){
				throw new RuntimeException("MQ parameter "+sender.getChannel()+"is not found");
			}else{
				sender.setChannel(channel);
			}
		}
	}
	
	private String getValue(String name){
		String value=jdbcTemplate.queryForObject(MQ_CONFIG_SQL,new Object[]{name}, String.class);
		logger.info("mq parameter name={},value={}",name,value);
		return value;
	}
}

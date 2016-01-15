package cn.explink.b2c.auto.order.mq;

import com.vip.platform.middleware.vms.IVMSCallback;

public class ConsumerTemplate {
	private IVMSCallback callBack;
	
	private String exchangeName;
	
	private String exchangeKey;
	
	private String queueKey;
	
	private String queueName;
	
	private Integer fetchCount;
	
	private Boolean autoCommit;
	
	private MqConfigService mqConfigService;
	
	public void init(){
		mqConfigService.initConsumer(this);
	}

	public IVMSCallback getCallBack() {
		return callBack;
	}

	public void setCallBack(IVMSCallback callBack) {
		this.callBack = callBack;
	}

	public String getQueueName() {
		return queueName;
	}

	public void setQueueName(String queueName) {
		this.queueName = queueName;
	}

	public Integer getFetchCount() {
		return fetchCount;
	}

	public void setFetchCount(Integer fetchCount) {
		this.fetchCount = fetchCount;
	}

	public Boolean getAutoCommit() {
		return autoCommit;
	}

	public void setAutoCommit(Boolean autoCommit) {
		this.autoCommit = autoCommit;
	}

	public String getExchangeName() {
		return exchangeName;
	}

	public void setExchangeName(String exchangeName) {
		this.exchangeName = exchangeName;
	}

	public String getExchangeKey() {
		return exchangeKey;
	}

	public void setExchangeKey(String exchangeKey) {
		this.exchangeKey = exchangeKey;
	}

	public String getQueueKey() {
		return queueKey;
	}

	public void setQueueKey(String queueKey) {
		this.queueKey = queueKey;
	}
	
	public MqConfigService getMqConfigService() {
		return mqConfigService;
	}

	public void setMqConfigService(MqConfigService mqConfigService) {
		this.mqConfigService = mqConfigService;
	}
}

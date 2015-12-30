package cn.explink.b2c.auto.order.mq;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@XmlRootElement(name = "root")
public class AutoMQExceptionDto {
	private String system_name;
	private String queue_name;
	private String exchange_name;
	private String routing_key;
	private String exception_info;
	private String business_id;
	private String create_time;
	private String remark;
	private String message;
	
	private long refid;//不输出到xml中
	
	public String getSystem_name() {
		return system_name;
	}
	public void setSystem_name(String system_name) {
		this.system_name = system_name;
	}
	
	public String getQueue_name() {
		return queue_name;
	}
	public void setQueue_name(String queue_name) {
		this.queue_name = queue_name;
	}
	public String getExchange_name() {
		return exchange_name;
	}
	public void setExchange_name(String exchange_name) {
		this.exchange_name = exchange_name;
	}
	public String getRouting_key() {
		return routing_key;
	}
	public void setRouting_key(String routing_key) {
		this.routing_key = routing_key;
	}
	public String getException_info() {
		return exception_info;
	}
	public void setException_info(String exception_info) {
		this.exception_info= exception_info;
	}
	public String getBusiness_id() {
		return business_id;
	}
	public void setBusiness_id(String business_id) {
		this.business_id = business_id;
	}
	public String getCreate_time() {
		return create_time;
	}
	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
	@XmlTransient
	public long getRefid() {
		return refid;
	}
	public void setRefid(long refid) {
		this.refid = refid;
	}
	
	
}

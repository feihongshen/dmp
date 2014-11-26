package cn.explink.controller;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DeliveryPercentForEmaildateDTO {
	private Logger logger = LoggerFactory.getLogger(DeliveryPercentForEmaildateDTO.class);
	private String cwb;
	private Long emaildateid;
	private long customerid;
	private Date deliverytime; // 反馈时间
	private Date starttime; // 投递率开始计算的时间

	public Logger getLogger() {
		return logger;
	}

	public void setLogger(Logger logger) {
		this.logger = logger;
	}

	public String getCwb() {
		return cwb;
	}

	public void setCwb(String cwb) {
		this.cwb = cwb;
	}

	public Long getEmaildateid() {
		return emaildateid;
	}

	public void setEmaildateid(Long emaildateid) {
		this.emaildateid = emaildateid;
	}

	public Date getDeliverytime() {
		return deliverytime;
	}

	public void setDeliverytime(Date deliverytime) {
		this.deliverytime = deliverytime;
	}

	public Date getStarttime() {
		return starttime;
	}

	public void setStarttime(Date starttime) {
		this.starttime = starttime;
	}

	public long getCustomerid() {
		return customerid;
	}

	public void setCustomerid(long customerid) {
		this.customerid = customerid;
	}

}

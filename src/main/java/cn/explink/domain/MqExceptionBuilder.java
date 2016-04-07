package cn.explink.domain;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSON;

/**
 * MQ异常model builder类
 * @author jeef.fang
 *
 */
public final class MqExceptionBuilder{

	private MqException mqException;

    public MqException getMqException() {
        return this.mqException;
    }

    private MqExceptionBuilder() {

    }

    public static MqExceptionBuilder getInstance() {
    	MqExceptionBuilder builder = new MqExceptionBuilder();
        builder.buildBase();
        return builder;
    }

    private void buildBase() {
    	MqException po = new MqException();
    	po.setExceptionCode("");
    	po.setMessageBody("");
    	po.setMessageHeader("");
    	po.setRemarks("");
    	po.setCreatedByUser("");
    	po.setCreatedOffice("");
    	po.setCreatedDtmLoc(new Date());
    	po.setCreatedTimeZone("");
    	po.setUpdatedByUser("");
    	po.setUpdatedOffice("");
    	po.setUpdatedTimeZone("");
    	po.setRouteingKey("");
        this.mqException = po;
    }
    
    public MqExceptionBuilder buildExceptionCode(String exceptionCode) {
        this.mqException.setExceptionCode(exceptionCode);
        return this;
    }
    
    public MqExceptionBuilder buildExceptionInfo(String exceptionInfo) {
        this.mqException.setExceptionInfo(exceptionInfo);
        return this;
    }
    
    public MqExceptionBuilder buildTopic(String topic) {
        this.mqException.setTopic(topic);
        return this;
    }
    
    public MqExceptionBuilder buildMessageBody(String messageBody) {
        this.mqException.setMessageBody(messageBody);
        return this;
    }
    
    public MqExceptionBuilder buildMessageHeader(Map<String,String> headerMap) {
        this.mqException.setMessageHeader(JSON.toJSONString(headerMap));
        
        return this;
    }
    
    public MqExceptionBuilder buildMessageHeader(String headerName, String headerValue) {
    	Map<String,String> map = new HashMap<String,String>();
    	map.put(headerName, headerValue);
        buildMessageHeader(map);
        return this;
    }
    
    public MqExceptionBuilder buildHandleCount(int handleCount) {
        this.mqException.setHandleCount(handleCount);
        return this;
    }
	
    public MqExceptionBuilder buildRemarks(String remarks) {
        this.mqException.setRemarks(remarks);
        return this;
    }
    
    public MqExceptionBuilder buildHandleTime(Date handleTime) {
        this.mqException.setHandleTime(handleTime);
        return this;
    }
	
    public MqExceptionBuilder buildCreatedByUser(String createdByUser) {
        this.mqException.setCreatedByUser(createdByUser);
        return this;
    }
	
    public MqExceptionBuilder buildCreatedOffice(String createdOffice) {
        this.mqException.setCreatedOffice(createdOffice);
        return this;
    }

    public MqExceptionBuilder buildCreatedDtmLoc(Date createdDtmLoc) {
        this.mqException.setCreatedDtmLoc(createdDtmLoc);
        return this;
    }
	
    public MqExceptionBuilder buildCreatedTimeZone(String createdTimeZone) {
        this.mqException.setCreatedTimeZone(createdTimeZone);
        return this;
    }
    
    public MqExceptionBuilder buildUpdatedByUser(String updatedByUser) {
        this.mqException.setUpdatedByUser(updatedByUser);
        return this;
    }
    
    public MqExceptionBuilder buildUpdatedOffice(String updatedOffice) {
        this.mqException.setUpdatedOffice(updatedOffice);
        return this;
    }
	
    public MqExceptionBuilder buildUpdatedDtmLoc(Date updatedDtmLoc) {
        this.mqException.setUpdatedDtmLoc(updatedDtmLoc);
        return this;
    }
	
    public MqExceptionBuilder buildUpdatedTimeZone(String updatedTimeZone) {
        this.mqException.setUpdatedTimeZone(updatedTimeZone);
        return this;
    }
    
    public MqExceptionBuilder buildIsDeleted(boolean isDeleted) {
        this.mqException.setIsDeleted(isDeleted);
        return this;
    }
    
    public MqExceptionBuilder buildRouteingKey(String routeingKey) {
        this.mqException.setRouteingKey(routeingKey);
        return this;
    }
}

package cn.explink.service;


import org.apache.camel.CamelContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import cn.explink.dao.MqExceptionDAO;
import cn.explink.domain.MqException;

/**
 * MQ异常处理服务
 * @author jeef.fang
 *
 */
@Component
@DependsOn({ "mqExceptionDAO" })
public class MqExceptionService {

	@Autowired
	private CamelContext camelContext;

	@Autowired
	MqExceptionDAO mqExceptionDAO;

	public void createMqException(MqException mqException) {
		mqExceptionDAO.save(mqException);
	}
	
	public void updateMqException(MqException mqException){
		mqExceptionDAO.update(mqException);
	}
}

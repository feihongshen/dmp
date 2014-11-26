package cn.explink.service;

import groovy.lang.Binding;
import groovy.lang.GroovyShell;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.explink.dao.OperationRuleDao;
import cn.explink.domain.CwbOrder;
import cn.explink.domain.OperationRule;
import cn.explink.domain.User;
import cn.explink.enumutil.FlowOrderTypeEnum;
import cn.explink.exception.CwbException;

@Service
public class OperationRuleService {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	OperationRuleDao operationRuleDao;

	List<OperationRule> allOperationRule = new ArrayList<OperationRule>();

	@PostConstruct
	public void reload() {
		allOperationRule = operationRuleDao.getAllOperationRule();
	}

	public void validate(User user, CwbOrder cwbOrder, Map<String, Object> parameters, FlowOrderTypeEnum orderTypeEnum) {
		logger.info("start validate cwb {} for floworder {}", cwbOrder.getCwb(), orderTypeEnum.getText());
		reload();
		List<OperationRule> operationRules = getRules(user, cwbOrder, parameters, orderTypeEnum);
		Binding binding = new Binding();
		binding.setProperty("cwborder", cwbOrder);
		binding.setProperty("user", user);
		binding.setProperty("parameters", parameters);
		binding.setProperty("operation", orderTypeEnum);
		GroovyShell groovyShell = new GroovyShell(binding);
		for (OperationRule operationRule : operationRules) {
			boolean match = (Boolean) groovyShell.evaluate(operationRule.getExpression());
			if (match) {
				switch (operationRule.getResult()) {
				case Success:
					return;
				case Fail:
					throw new CwbException(cwbOrder.getCwb(), orderTypeEnum.getValue(), groovyShell.evaluate(operationRule.getErrormessage()).toString());
				default:
					break;
				}
			}
		}
		logger.info("end validate cwb {} for floworder {}", cwbOrder.getCwb(), orderTypeEnum.getText());
	}

	public List<OperationRule> getRules(User user, CwbOrder cwbOrder, Map<String, Object> parameters, FlowOrderTypeEnum orderTypeEnum) {
		List<OperationRule> result = new ArrayList<OperationRule>();
		for (OperationRule operationRule : allOperationRule) {
			if (operationRule.getFlowordertype() == 0) {
				result.add(operationRule);
				continue;
			}
			if (orderTypeEnum.getValue() == operationRule.getFlowordertype()) {
				result.add(operationRule);
			}
		}
		return result;
	}
}

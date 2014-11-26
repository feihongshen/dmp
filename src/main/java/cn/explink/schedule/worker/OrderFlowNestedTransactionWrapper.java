package cn.explink.schedule.worker;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import cn.explink.client.AccountClient;
import cn.explink.client.OmsClient;
import cn.explink.dao.OrderFlowDAO;
import cn.explink.domain.ScheduledTask;
import cn.explink.domain.orderflow.OrderFlow;
import cn.explink.exception.ExplinkRuntimeException;
import cn.explink.schedule.Constants;
import cn.explink.service.CwbOrderService;
import cn.explink.service.EditService;
import cn.explink.service.SmsSendService;
import cn.explink.support.transcwb.TransCwbService;

@Service
public class OrderFlowNestedTransactionWrapper {

	@Autowired
	private OrderFlowDAO orderFlowDAO;

	@Autowired
	private CwbOrderService cwbOrderService;

	@Autowired
	private SmsSendService smsSendService;

	@Autowired
	private EditService editService;

	@Autowired
	private TransCwbService transCwbService;

	@Autowired
	private OmsClient omsClient;

	@Autowired
	private AccountClient accountClient;

	@Transactional(propagation = Propagation.NESTED)
	public Object doSomething(Object param) throws Exception {
		ScheduledTask scheduledTask = (ScheduledTask) param;
		long orderFlowId = Long.parseLong(scheduledTask.getReferenceId());
		OrderFlow orderFlow = orderFlowDAO.getOrderFlowById(orderFlowId);
		String taskType = scheduledTask.getTaskType();

		String result = "success";
		if (Constants.TASK_TYPE_ORDER_FLOW_EDIT_SHOW_INFO.equals(taskType)) {
			editService.saveEdit(orderFlow);
		} else if (Constants.TASK_TYPE_ORDER_FLOW_RECIEVE_GOODS.equals(taskType)) {
			cwbOrderService.autoReceiveGoods(orderFlow);
		} else if (Constants.TASK_TYPE_ORDER_FLOW_SMS.equals(taskType)) {
			smsSendService.sendSms(orderFlow);
		} else if (Constants.TASK_TYPE_ORDER_FLOW_TRANSCWB.equals(taskType)) {
			transCwbService.saveTransCwb(orderFlow);
		} else if (Constants.TASK_TYPE_ORDER_FLOW_OMS1.equals(taskType)) {
			result = omsClient.saveFlow(orderFlow);
		} else if (Constants.TASK_TYPE_ORDER_FLOW_OMSB2C.equals(taskType)) {
			result = omsClient.saveFlowB2cSend(orderFlow);
		} else if (Constants.TASK_TYPE_ORDER_FLOW_ACCOUNT.equals(taskType)) {
			result = accountClient.accountOrderFlow(orderFlow);
		} else {
			throw new ExplinkRuntimeException(scheduledTask.getTaskType() + " is not a valid order flow task");
		}
		return result;
	}

}
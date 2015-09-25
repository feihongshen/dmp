package cn.explink.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;

import cn.explink.dao.ExceptionCwbDAO;
import cn.explink.domain.User;
import cn.explink.enumutil.FlowOrderTypeEnum;

@Aspect
@Configurable
public class OperationRecord {

	@Autowired
	ExceptionCwbDAO exceptionCwbDAO;

	@Before("execution(void cn.explink.service.CwbOrderService.*(cn.explink.domain.User,String, ..) )&&args(user,cwb,..)&&@annotation(OrderFlowOperation(operation))")
	public void operation(User user, String cwb, FlowOrderTypeEnum operation, JoinPoint jp) {
		exceptionCwbDAO.createExceptionCwbScan(cwb, operation.getValue(), "", user.getBranchid(), user.getUserid(), 0, 0, 0, 0, "",cwb);
	}

}

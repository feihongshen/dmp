package cn.explink.aspect;

import java.util.List;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.util.StringUtils;

import cn.explink.domain.User;
import cn.explink.enumutil.FlowOrderTypeEnum;
import cn.explink.service.CwbTranslator;

@Aspect
@Configurable
public class CwbOperationValidateAspect {

	@Autowired
	List<CwbTranslator> cwbTranslators;

	@Before("execution(void cn.explink.service.CwbOrderService.*(cn.explink.domain.User,String, ..) )&&args(user,cwb,..)&&@annotation(OrderFlowOperation(operation))")
	public void operation(User user, String cwb, FlowOrderTypeEnum operation, JoinPoint jp) throws Throwable {
		Object[] args = jp.getArgs();
		args[1] = translateCwb(cwb);
	}

	public String translateCwb(String cwb) {
		for (CwbTranslator cwbTranslator : cwbTranslators) {
			String translateCwb = cwbTranslator.translate(cwb);
			if (StringUtils.hasLength(translateCwb)) {
				cwb = translateCwb;
			}
		}
		return cwb;
	}
}

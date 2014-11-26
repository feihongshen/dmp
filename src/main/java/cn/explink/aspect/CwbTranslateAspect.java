package cn.explink.aspect;

import java.util.List;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.util.StringUtils;

import cn.explink.domain.User;
import cn.explink.enumutil.FlowOrderTypeEnum;
import cn.explink.service.CwbTranslator;

@Aspect
@Configurable
public class CwbTranslateAspect {

	@Autowired
	List<CwbTranslator> cwbTranslators;

	@Around("execution(void cn.explink.service.CwbOrderService.*(cn.explink.domain.User,String, ..) )&&args(user,cwb,..)&&@annotation(OrderFlowOperation(operation))")
	public Object operation(User user, String cwb, FlowOrderTypeEnum operation, ProceedingJoinPoint pjp) throws Throwable {
		Object[] args = pjp.getArgs();
		args[1] = translateCwb(cwb);
		return pjp.proceed(args);
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

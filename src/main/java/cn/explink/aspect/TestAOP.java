package cn.explink.aspect;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Component;

@Aspect
@Component
@EnableAspectJAutoProxy(proxyTargetClass = true)
public class TestAOP {

	@Before("execution(* cn.explink.dao.TimeEffectiveDAO.getAllTimeEffectiveVO(..))")
	public void before() {
		System.out.println("before");
	}

}

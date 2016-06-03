package com.pjbest.splitting.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import com.pjbest.splitting.routing.DatabaseContextHolder;
import com.pjbest.splitting.routing.DatabaseType;

/**
 * 
 * @author pengfei.wan
 * @date 2016-03-15
 *
 */
@Aspect
@Component
public class DataSourceInterceptor implements Ordered {

	private static final Logger logger = LoggerFactory.getLogger(DataSourceInterceptor.class);

	private int order;

	@Value("20")
	public void setOrder(int order) {
		logger.info("DataSourceInterceptor >>> order = 20");
		this.order = order;
	}

	@Override
	public int getOrder() {
		return order;
	}

	@Around(value = "@within(dataSource) || @annotation(dataSource)")
	public Object proceed(ProceedingJoinPoint pjp, DataSource dataSource) throws Throwable {
		boolean flag = false;
		try {
			try {
				if (dataSource == null) {
					dataSource = pjp.getTarget().getClass().getAnnotation(DataSource.class);
				}
			} catch (Exception e) {
				logger.error("DataSourceInterceptor.proceed exception 1 : " + e.toString());
			}
			DatabaseContextHolder.pushDatabaseType(dataSource == null || dataSource.value() == null ? DatabaseType.MASTER : dataSource.value());
			flag = true;
			try {
				String info = "DataSourceInterceptor - class : " + pjp.getTarget().getClass().getName();
				info += ", method : " + pjp.getSignature().getName();
				info += ", DatabaseType : " + DatabaseContextHolder.peekDatabaseType();
				logger.info(info);
				System.out.println(info);
			} catch (Exception e) {
				logger.error("DataSourceInterceptor.proceed exception 2 : " + e.toString());
			}
			Object result = pjp.proceed();
			return result;
		} finally {
			if (flag) {
				DatabaseContextHolder.popDatabaseType();
			}
		}
	}

}

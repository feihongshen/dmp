package cn.explink.core.utils;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * Spring 容器工具类
 * @author gaoll
 *
 */
@Service
public class SpringContextUtils implements ServletContextListener, ApplicationContextAware{

	/**
	 * 注入spring容器
	 */
	private static ApplicationContext context;
	
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		context = applicationContext;
	}

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		context = WebApplicationContextUtils.getWebApplicationContext(sce.getServletContext());
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		context = null;
	}

	/**
	 * 获取当前Spring容器
	 * @return
	 */
	public static ApplicationContext getContext() {
		return context;
	}

	/**
	 * 手动注入Spring容器
	 * @param springContext
	 */
	public static void setContext(ApplicationContext springContext) {
		SpringContextUtils.context = springContext;
	}
}
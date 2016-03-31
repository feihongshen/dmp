package cn.explink.util;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.WebApplicationContext;

public class QuartzContextListener implements ServletContextListener {

	private static Logger logger = LoggerFactory.getLogger(QuartzContextListener.class);
	
	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		WebApplicationContext webApplicationContext = (WebApplicationContext) arg0.getServletContext().getAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE);
		org.quartz.impl.StdScheduler startQuertz = (org.quartz.impl.StdScheduler) webApplicationContext.getBean("schedulerFactory");
		if (startQuertz != null) {
			startQuertz.shutdown();
		}
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			logger.error("", e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.servlet.ServletContextListener#contextInitialized(javax.servlet
	 * .ServletContextEvent)
	 */
	@Override
	public void contextInitialized(ServletContextEvent arg0) {

	}

}
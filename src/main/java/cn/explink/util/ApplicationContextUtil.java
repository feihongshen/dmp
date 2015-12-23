package cn.explink.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

@Service
public class ApplicationContextUtil implements ApplicationContextAware {

	private static ApplicationContext applicationContext;

	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		synchronized (ApplicationContextUtil.class) {
			ApplicationContextUtil.applicationContext = applicationContext;
			ApplicationContextUtil.class.notifyAll();
		}
	}

	public static ApplicationContext getApplicationContext() {
		synchronized (ApplicationContextUtil.class) {
			while (applicationContext == null) {
				try {
					ApplicationContextUtil.class.wait(60000);
				} catch (InterruptedException ex) {
				}
			}
			return applicationContext;
		}
	}

	public static <T> Object getBean(String name) {
		return getApplicationContext().getBean(name);
	}
	
	public static <T> T getService(Class<T> serviceClass) {
		return ApplicationContextUtil.getApplicationContext().getBean(serviceClass);
	}
}
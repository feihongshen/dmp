package cn.explink.util;

import java.io.Serializable;
import java.text.ParseException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.quartz.CronTriggerBean;

public class DatabaseInitCronTriggerBean extends CronTriggerBean implements Serializable {

	private static final long serialVersionUID = 4980168080456291527L;

	private static Logger logger = LoggerFactory.getLogger(DatabaseInitCronTriggerBean.class);
	
	@Override
	public void afterPropertiesSet() throws ParseException {
		try {
			super.afterPropertiesSet();
		} catch (Exception e) {
			logger.error("", e);
		}
		if (this.getCronExpression() == null) {
			this.setCronExpression("");
		}
	}

}

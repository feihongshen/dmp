package cn.explink.util;

import java.io.Serializable;
import java.text.ParseException;

import org.springframework.scheduling.quartz.CronTriggerBean;

public class DatabaseInitCronTriggerBean extends CronTriggerBean implements Serializable {

	private static final long serialVersionUID = 4980168080456291527L;

	@Override
	public void afterPropertiesSet() throws ParseException {
		try {
			super.afterPropertiesSet();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (this.getCronExpression() == null) {
			this.setCronExpression("");
		}
	}

}

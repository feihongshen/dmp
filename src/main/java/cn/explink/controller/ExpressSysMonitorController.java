package cn.explink.controller;

import java.text.ParseException;
import java.util.Date;

import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.explink.dao.ExpressSysMonitorDAO;
import cn.explink.domain.ExpressSysMonitor;
import cn.explink.enumutil.ExpressSysMonitorEnum;

@Controller
@RequestMapping("/expressSysMonitor")
public class ExpressSysMonitorController {

	@Autowired
	ExpressSysMonitorDAO expressSysMonitorDAO;
	@Autowired
	SecurityContextHolderStrategy securityContextHolderStrategy;

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@RequestMapping("/jobTimer/{requestparam}")
	public @ResponseBody JSONObject jobTimer(@PathVariable("requestparam") String requestparam) {
		JSONObject jo = new JSONObject();

		ExpressSysMonitorEnum esm = ExpressSysMonitorEnum.fromString(requestparam);

		try {
			String threshold = expressSysMonitorDAO.minutesBetween(esm);
			jo.put("threshold", threshold);
			jo.put("resultCode", 0);
		} catch (ParseException e) {
			jo.put("resultCode", 1);
			logger.error(e.getMessage());
		}
		return jo;
	}

	@RequestMapping("/JMSDmpFlow")
	public @ResponseBody JSONObject returnMonitoerJMSDmpFlow() {
		JSONObject jo = new JSONObject();
		ExpressSysMonitor expressSysMonitor = expressSysMonitorDAO.getMaxOpt("JMSDmpFlow");
		try {
			String threshold = "0";
			if (expressSysMonitor == null) {
				threshold = "0";
			} else {
				threshold = "1";
			}

			jo.put("threshold", threshold);
			jo.put("resultCode", 0);
			expressSysMonitorDAO.updateDealFlag(expressSysMonitor);
		} catch (Exception e) {
			jo.put("resultCode", 1);
			logger.error(e.getMessage());
		}
		return jo;
	}

}

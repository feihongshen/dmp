package cn.explink.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.stereotype.Controller;
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
	public static final long nm = 1000 * 60;

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

	@RequestMapping("/JMSOMSFlow")
	public @ResponseBody JSONObject returnMonitoerJMSOMSFlow() {
		JSONObject jobj = new JSONObject();
		ExpressSysMonitor expressSysMonitor = expressSysMonitorDAO.getMaxOpt("JMSOMSFlow");
		try {
			if (expressSysMonitor != null) {
				SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				Date optime = df.parse(expressSysMonitor.getOptime());
				Long diff = new Date().getTime() - optime.getTime();
				String threshold = String.valueOf(diff / nm);
				jobj.put("threshold", threshold);
				jobj.put("resultCode", 0);
				expressSysMonitorDAO.updateDealFlag(expressSysMonitor);
			} else {
				jobj.put("resultCode", 1);
			}
		} catch (Exception e) {
			jobj.put("resultCode", 1);
			logger.error(e.getMessage());
		}
		return jobj;
	}

	@RequestMapping("/JMSB2CFlow")
	public @ResponseBody JSONObject returnMonitoerJMSB2CFlow() {
		JSONObject jobj = new JSONObject();
		ExpressSysMonitor expressSysMonitor = expressSysMonitorDAO.getMaxOpt("JMSB2CFlow");
		try {
			if (expressSysMonitor != null) {
				SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				Date optime = df.parse(expressSysMonitor.getOptime());
				Long diff = new Date().getTime() - optime.getTime();
				String threshold = String.valueOf(diff / nm);
				jobj.put("threshold", threshold);
				jobj.put("resultCode", 0);
				expressSysMonitorDAO.updateDealFlag(expressSysMonitor);
			} else {
				jobj.put("resultCode", 1);
			}
		} catch (Exception e) {
			jobj.put("resultCode", 1);
			logger.error(e.getMessage());
		}
		return jobj;
	}

}

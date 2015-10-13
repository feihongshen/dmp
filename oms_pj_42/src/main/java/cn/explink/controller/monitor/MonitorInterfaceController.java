package cn.explink.controller.monitor;

import java.text.SimpleDateFormat;
import java.util.Date;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.explink.b2c.tools.B2CDataDAO;
import cn.explink.dao.monitor.MonitorSendB2cDataDAO;

@RequestMapping("/monitorInterface")
@Controller
public class MonitorInterfaceController {

	@Autowired
	B2CDataDAO b2cDataDAO;
	@Autowired
	MonitorSendB2cDataDAO monitorSendB2cDataDAO;

	@RequestMapping("/monitorSendB2CData")
	public @ResponseBody JSONObject monitorSendB2CData() {
		JSONObject jo = new JSONObject();
		try {
			long currentMillis = System.currentTimeMillis();
			long beforeSS = currentMillis / 1000 - (10 * 60);
			String begintime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(beforeSS * 1000));

			String requesttime = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date(currentMillis));
			long resultcount = 0;
			long count = monitorSendB2cDataDAO.getMonitorSendB2cDataCountByRequesttime(begintime);
			if (count == 0) {
				resultcount = b2cDataDAO.getDataCountBySendB2cFlagAndPosttime();
				monitorSendB2cDataDAO.creMonitorSendB2cData(requesttime, resultcount);
			}
			jo.put("threshold", resultcount);
			jo.put("resultCode", 0);
		} catch (Exception e) {
			jo.put("resultCode", 1);
		}
		return jo;
	}

}

package cn.explink.service;

import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;
import cn.explink.domain.SmsConfig;

@Service
public class SmsConfigService {

	public SmsConfig loadFormForSmsConfig(HttpServletRequest request) {
		SmsConfig smsConfig = new SmsConfig();
		smsConfig.setId(Long.parseLong((request.getParameter("id") == null || "".equals(request.getParameter("id"))) ? "0" : request.getParameter("id")));
		smsConfig.setName(request.getParameter("name"));
		smsConfig.setPassword(request.getParameter("pass"));
		smsConfig.setWarningcount(Long.parseLong((request.getParameter("warningcount") == null || "".equals(request.getParameter("warningcount"))) ? "0" : request.getParameter("warningcount")));
		smsConfig.setPhone(request.getParameter("phone"));
		smsConfig.setTemplet(request.getParameter("templet"));
		smsConfig.setWarningcontent(request.getParameter("warningcontent"));
		smsConfig.setTemplatecontent(request.getParameter("templatecontent"));
		smsConfig.setMonitor(Long.parseLong((request.getParameter("monitor") == null || "".equals(request.getParameter("monitor"))) ? "0" : request.getParameter("monitor")));
		smsConfig.setIsOpen(Long.parseLong((request.getParameter("isOpen") == null || "".equals(request.getParameter("isOpen"))) ? "0" : request.getParameter("isOpen")));
		return smsConfig;
	}

}

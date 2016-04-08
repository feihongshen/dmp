package cn.explink.controller;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.explink.dao.MqExceptionDAO;
import cn.explink.dao.UserDAO;
import cn.explink.domain.MqException;
import cn.explink.domain.User;
import cn.explink.service.ExplinkUserDetail;
import cn.explink.service.MqExceptionService;
import cn.explink.service.UserService;
import cn.explink.util.Page;

@Controller
@RequestMapping("/mqexception")
public class MqExceptionController {

	@Autowired
	UserService userService;
	@Autowired
	UserDAO userDAO;
	
	@Autowired
	private MqExceptionDAO mqExceptionDAO; 
	
	@Autowired
	private MqExceptionService mqExceptionService; 
	
	@Autowired
	SecurityContextHolderStrategy securityContextHolderStrategy;

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	private User getSessionUser() {
		ExplinkUserDetail userDetail = (ExplinkUserDetail) securityContextHolderStrategy.getContext().getAuthentication().getPrincipal();
		return userDetail.getUser();
	}

	@RequestMapping("/list/{page}")
	public String list(@PathVariable("page") long page, Model model, HttpServletRequest request, 
			@RequestParam(value = "exceptionCode", required = false, defaultValue = "") String exceptionCode,
			@RequestParam(value = "topic", required = false, defaultValue = "") String topic,
			@RequestParam(value = "handleFlag", required = false, defaultValue = "") String handleFlag,
			@RequestParam(value = "messageSource", required = false, defaultValue = "") String messageSource,
			@RequestParam(value = "isAutoResend", required = false, defaultValue = "") String isAutoResend) throws Exception {
		exceptionCode = exceptionCode.replace("'", "");
		topic = topic.replace("'", "");
		model.addAttribute("siList", mqExceptionDAO.getMqExceptionByWhere(page, exceptionCode, topic, handleFlag, messageSource, isAutoResend));
		model.addAttribute("page_obj", new Page(mqExceptionDAO.getSystemInstallCount(exceptionCode, topic, handleFlag, messageSource, isAutoResend), page, Page.ONE_PAGE_NUMBER));
		model.addAttribute("page", page);
		model.addAttribute("exceptionCode", exceptionCode);
		model.addAttribute("topic", topic);
		model.addAttribute("handleFlag", handleFlag);
		model.addAttribute("messageSource", messageSource);
		model.addAttribute("isAutoResend", isAutoResend);
		return "/mqexception/list";
	}

	@RequestMapping("/edit/{id}")
	public String edit(Model model, @PathVariable("id") long id) {
		MqException mqException = mqExceptionDAO.getMqExceptionById(id);
		mqException.setMessageBody(mqException.getMessageBody().trim());//去空格
		mqException.setMessageHeader(mqException.getMessageHeader().trim());//去空格
		mqException.setRemarks(mqException.getRemarks().trim());//去空格
		model.addAttribute("mqException", mqException);
		return "/mqexception/edit";
	}

	@RequestMapping("/save/{id}")
	public @ResponseBody String save(Model model, @PathVariable("id") long id, 
			@RequestParam(value = "messageBody", required = false, defaultValue = "") String messageBody,
			@RequestParam(value = "messageHeader", required = false, defaultValue = "") String messageHeader,
			@RequestParam(value = "handleCount", required = false, defaultValue = "0") int handleCount,
			@RequestParam(value = "remarks", required = false, defaultValue = "") String remarks,
			@RequestParam(value = "isAutoResend", required = false, defaultValue = "0") int isAutoResend)
			throws Exception {
		MqException mqException = mqExceptionDAO.getMqExceptionById(id);
		if (mqException == null) {
			return "{\"errorCode\":1,\"error\":\"该MQ异常不存在\"}";
		} else {
			mqException.setMessageBody(messageBody.trim());
			mqException.setMessageHeader(messageHeader.trim());
			mqException.setHandleCount(handleCount);
			mqException.setRemarks(remarks.trim());
			mqException.setIsAutoResend(isAutoResend == 0 ? false : true);
			mqException.setUpdatedByUser(getSessionUser().getUsername());//更新人
			mqException.setUpdatedOffice(getSessionUser().getBranchid() + "");//更新机构
			mqExceptionService.updateMqException(mqException);
			logger.info("operatorUser={},mq异常记录修改 设置->save", getSessionUser().getUsername());
			return "{\"errorCode\":0,\"error\":\"保存成功\"}";
		}
	}
}
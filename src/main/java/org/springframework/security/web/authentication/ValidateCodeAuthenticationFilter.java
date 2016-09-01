package org.springframework.security.web.authentication;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.httpclient.util.DateUtil;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.org.eclipse.jdt.core.dom.ThisExpression;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.util.TextEscapeUtils;

import cn.explink.dao.SystemInstallDAO;
import cn.explink.dao.UserDAO;
import cn.explink.dao.UserLoginLogDAO;
import cn.explink.domain.SystemInstall;
import cn.explink.domain.User;
import cn.explink.domain.UserLoginLog;
import cn.explink.util.DateTimeUtil;
import cn.explink.util.StringUtil;

public class ValidateCodeAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

	private boolean postOnly = true;
	private boolean allowEmptyValidateCode = false;
	private String sessionvalidateCodeField = DEFAULT_SESSION_VALIDATE_CODE_FIELD;
	private String validateCodeParameter = DEFAULT_VALIDATE_CODE_PARAMETER;
	public static final String DEFAULT_SESSION_VALIDATE_CODE_FIELD = "validateCode";
	public static final String DEFAULT_VALIDATE_CODE_PARAMETER = "validateCode";
	
	public static final int LAST_LOGIN_STATE_SUCCESS = 1;
	public static final int LAST_LOGIN_STATE_FAIL = 0;
	
	//add by huangzh 2016-8-3 添加登录日志
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	UserDAO userDAO;
	
	@Autowired
	private SystemInstallDAO systemInstallDAO;
	
	@Autowired
	UserLoginLogDAO userLoginLogDAO;

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
		if (postOnly && !request.getMethod().equals("POST")) {
			throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
		}

		String username = StringUtils.trimToEmpty(obtainUsername(request));
		String password = obtainPassword(request);
		if (password == null) {
			password = StringUtils.EMPTY;
		}

		if (!isAllowEmptyValidateCode())
			checkValidateCode(request);
		request.getSession().removeAttribute(sessionvalidateCodeField);
		// 验证用户账号与密码是否对应
		username = username.trim();

		// modified by wangwei, 20150830, 登录次数限制， start
		/*
		List<User> users = userDAO.getUsersByUsername(username);
		if (users.size() == 0 || users.size() > 1 || !users.get(0).getWebPassword().equals(password)) {
			//add by huangzh 2016-8-3  添加登录失败日志
			this.logger.error("用户名:"+username+",登录IP："+getIpAddr(request)+",登录时间："+DateTimeUtil.getNowTime()+"，登录标识:登录失败，用户名或者密码错误");
			throw new AuthenticationServiceException("用户名或者密码错误");
		}
		*/
		UserLoginLog userLoginLog = getUserLoginLogByUsername(username); 
		if (userLoginLog != null) {
			int lastLoginState = userLoginLog.getLastLoginState();	// 上次登录状态（1-成功，0-失败）
			int loginFailCount = userLoginLog.getLoginFailCount();	// 累计连续登录错误次数
			String lastLoginTryTime = userLoginLog.getLastLoginTryTime();	// 上次尝试登录时间
			
			int loginFailMaxTryTimeLimit = getLoginFailMaxTryTimeLimit();	// 登录失败尝试最大次数
			int loginForbiddenIntervalInMinutes = getLoginForbiddenIntervalInMinutes();	// 登录失败禁止时间长度（分钟）
			
			String nowTimeInString = DateTimeUtil.getNowTime();
			
			if(lastLoginState != LAST_LOGIN_STATE_SUCCESS){
				if(loginFailCount >= loginFailMaxTryTimeLimit){
					long loginForbiddenPleaseWaitMinutes = loginForbiddenIntervalInMinutes - getDateDiffInMinutes(nowTimeInString, lastLoginTryTime); 
					if (loginForbiddenPleaseWaitMinutes > 0){
						String message = "连续登录失败超过" + loginFailMaxTryTimeLimit + "次，\n"
								+ "请 " + loginForbiddenPleaseWaitMinutes + "分钟后再试，或联系管理员解禁。";
						this.logger.error(message);
						throw new AuthenticationServiceException(message);
					} else {
						// 封禁时间到，解除禁止，重置loginFailCount为0
						userLoginLog.setLoginFailCount(0);
						userLoginLogDAO.saveUserLoginLog(userLoginLog);
					}
				}
			}
		}
		
		List<User> users = userDAO.getUsersByUsername(username);
		
		if (users.size() == 0 || users.size() > 1 || users.get(0) == null) {
			// 登录失败
			handleFailUserLoginLog(username);
			
			//add by huangzh 2016-8-3  添加登录失败日志
			this.logger.error("用户名:"+username+",登录IP："+getIpAddr(request)+",登录时间："+DateTimeUtil.getNowTime()+"，登录标识:登录失败，用户名或者密码错误");
			throw new AuthenticationServiceException("用户名或者密码错误");
		} else {
			User user = users.get(0);
			if (!user.getWebPassword().equals(password)) {
				// 登录失败
				handleFailUserLoginLog(username);
				
				this.logger.error("用户名:"+username+",登录IP："+getIpAddr(request)+",登录时间："+DateTimeUtil.getNowTime()+"，登录标识:登录失败，用户名或者密码错误");
				throw new AuthenticationServiceException("用户名或者密码错误");
			} else {
				// 登录成功
				handleSuccessUserLoginLog(username);
			 }
		}
		// modified by wangwei, 20150830, 登录次数限制， end

		UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(username, password);

		// Place the last username attempted into HttpSession for views
		HttpSession session = request.getSession(false);

		if (session != null || getAllowSessionCreation()) {
			request.getSession().setAttribute(SPRING_SECURITY_LAST_USERNAME_KEY, TextEscapeUtils.escapeEntities(username));
		}

		// Allow subclasses to set the "details" property
		setDetails(request, authRequest);
		// check validate code

		users.get(0).setLastLoginIp(getIpAddr(request));
		users.get(0).setLastLoginTime(DateTimeUtil.getNowTime());
		userDAO.updateUserLastInfo(users.get(0));
		
		//add by huangzh 2016-8-3  添加登录成功日志
		this.logger.info("用户名:"+username+",登录IP："+getIpAddr(request)+",登录时间："+DateTimeUtil.getNowTime()+"，登录标识：登录成功。");

		return this.getAuthenticationManager().authenticate(authRequest);
	}
	
	private UserLoginLog getUserLoginLogByUsername(String username) {
		UserLoginLog userLoginLog = userLoginLogDAO.getUserLoginLogByUsername(username);
		if(userLoginLog == null) {
			userLoginLog = new UserLoginLog();
			userLoginLog.setUsername(username);
			userLoginLog.setLastLoginState(0);
			userLoginLog.setLoginFailCount(0);
			userLoginLog.setLastLoginTryTime("0000-00-00 00:00:00");
			userLoginLogDAO.creUserLoginLog(userLoginLog);
			userLoginLog = userLoginLogDAO.getUserLoginLogByUsername(username);
		}
		return userLoginLog;
	}
	
	private void handleFailUserLoginLog(String username) {
		UserLoginLog userLoginLog = getUserLoginLogByUsername(username);
		if(userLoginLog!=null) {
			userLoginLog.setLastLoginState(LAST_LOGIN_STATE_FAIL);
			userLoginLog.setLoginFailCount(userLoginLog.getLoginFailCount() + 1);
			userLoginLog.setLastLoginTryTime(DateTimeUtil.getNowTime());
			userLoginLogDAO.saveUserLoginLog(userLoginLog);
		}
	}
	
	private void handleSuccessUserLoginLog(String username) {
		UserLoginLog userLoginLog = getUserLoginLogByUsername(username);
		if(userLoginLog!=null) {
			userLoginLog.setLastLoginState(LAST_LOGIN_STATE_SUCCESS);
			userLoginLog.setLoginFailCount(0);
			userLoginLog.setLastLoginTryTime(DateTimeUtil.getNowTime());
			userLoginLogDAO.saveUserLoginLog(userLoginLog);
		}
		
	}
	
	public String getIpAddr(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		return ip;
	}

	/**
	 * 
	 * <li>比较session中的验证码和用户输入的验证码是否相等</li>
	 * 
	 */
	protected void checkValidateCode(HttpServletRequest request) {
		String sessionValidateCode = obtainSessionValidateCode(request);
		String validateCodeParameter = obtainValidateCodeParameter(request);
		String username = StringUtils.trimToEmpty(obtainUsername(request));
		if(StringUtils.isEmpty(sessionValidateCode)) {
			//add by huangzh 2016-8-3  添加登录失败日志
			this.logger.error("用户名:"+username+",登录IP："+getIpAddr(request)+",登录时间："+DateTimeUtil.getNowTime()+"，登录标识:登录失败，验证码已过期");
			
			throw new AuthenticationServiceException("验证码已过期");
		}
		if (StringUtils.isEmpty(validateCodeParameter) || !sessionValidateCode.equalsIgnoreCase(validateCodeParameter)) {// &&!"TTTT".equals(validateCodeParameter)
			//add by huangzh 2016-8-3  添加登录失败日志
			this.logger.error("用户名:"+username+",登录IP："+getIpAddr(request)+",登录时间："+DateTimeUtil.getNowTime()+"，登录标识:登录失败，验证码不正确");
			
			throw new AuthenticationServiceException("验证码不正确");
		}
	}

	private String obtainValidateCodeParameter(HttpServletRequest request) {
		return request.getParameter(validateCodeParameter);
	}

	protected String obtainSessionValidateCode(HttpServletRequest request) {
		Object obj = request.getSession().getAttribute(sessionvalidateCodeField);
		return null == obj ? "" : obj.toString();
	}

	public boolean isPostOnly() {
		return postOnly;
	}

	@Override
	public void setPostOnly(boolean postOnly) {
		this.postOnly = postOnly;
	}

	public String getValidateCodeName() {
		return sessionvalidateCodeField;
	}

	public void setValidateCodeName(String validateCodeName) {
		this.sessionvalidateCodeField = validateCodeName;
	}

	public boolean isAllowEmptyValidateCode() {
		return allowEmptyValidateCode;
	}

	public void setAllowEmptyValidateCode(boolean allowEmptyValidateCode) {
		this.allowEmptyValidateCode = allowEmptyValidateCode;
	}
	
	// 登录失败尝试最大次数
	private int getLoginFailMaxTryTimeLimit() {
		int loginFailMaxTryTimeLimit;
		SystemInstall loginFailMaxTryTimeLimitSystemInstall = systemInstallDAO
				.getSystemInstall("loginFailMaxTryTimeLimit");
		loginFailMaxTryTimeLimit = (loginFailMaxTryTimeLimitSystemInstall == null ? 0
				: Integer.valueOf(loginFailMaxTryTimeLimitSystemInstall.getValue()));
		return loginFailMaxTryTimeLimit;
	}
	
	// 登录失败禁止时间长度（分钟）
	private int getLoginForbiddenIntervalInMinutes() {
		int loginForbiddenIntervalInMinutes;
		SystemInstall loginForbiddenIntervalInMinutesSystemInstall = systemInstallDAO
				.getSystemInstall("loginForbiddenIntervalInMinutes");
		loginForbiddenIntervalInMinutes = (loginForbiddenIntervalInMinutesSystemInstall == null ? 60
				: Integer.valueOf(loginForbiddenIntervalInMinutesSystemInstall.getValue()));
		return loginForbiddenIntervalInMinutes;
	}
	
	private long getDateDiffInMinutes(String dateStr1, String dateStr2) {
		if (StringUtil.isEmpty(dateStr1)) {
			dateStr1 = "0000-00-00 00:00:00";
		}
		if (StringUtil.isEmpty(dateStr2)) {
			dateStr2 = "0000-00-00 00:00:00";
		}
		return Math.abs(DateTimeUtil.dateDiff("minute", dateStr1, dateStr2));
	}
}

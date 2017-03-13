package cn.explink.pos.chinaums;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import cn.explink.domain.Role;
import cn.explink.domain.User;
import cn.explink.pos.chinaums.xml.Transaction;
import cn.explink.pos.tools.EmployeeInfo;
import cn.explink.util.DateTimeUtil;
import cn.explink.util.MD5.MD5Util;

@Service
public class ChinaUmsService_toLogin extends ChinaUmsService {
	private Logger logger = LoggerFactory.getLogger(ChinaUmsService_toLogin.class);

	/**
	 * 派送员登录
	 * 
	 * @return
	 */
	public String tologin(Transaction rootnote, ChinaUms chinaUms) {
		EmployeeInfo employee = new EmployeeInfo();
		String resp_code = ""; // 验证消息编码
		String resp_msg = ""; // 返回信息描述
		String username = ""; // 登录用户账号
		String password = ""; // 登录用户密钥
		String resp_time = DateTimeUtil.getNowTime("yyyyMMddHHmmss"); // 请求时间

		username = rootnote.getTransaction_Header().getEmployno();
		password = rootnote.getTransaction_Body().getPasswd();
		employee = jiontDAO.getEmployeeInfo(username);
		if (employee == null) {
			resp_code = "08";
			resp_msg = "登陆失败,无此用户名";
			logger.error("登陆失败,没有此用户名!" + username);
		} else {
			String user_pass = employee.getPassword(); // 数据库获取的password
			// 转换密码,改为md5加密,去掉奇数位0
			String md5password = MD5Util.md5(user_pass).toUpperCase();
			if (password.equalsIgnoreCase(md5password)) {
				resp_code = ChinaUmsExptMessageEnum.Success.getResp_code();
				resp_msg = ChinaUmsExptMessageEnum.Success.getResp_msg();
				logger.info("移动POS(chinaums)登录成功，当前用户名{}", username);

			} else {
				resp_code = ChinaUmsExptMessageEnum.DengLuFaild.getResp_code();
				resp_msg = ChinaUmsExptMessageEnum.DengLuFaild.getResp_msg();
				logger.info("移动POS(chinaums):用户名为{}登录失败，密码验证错误!", username);
			}

		}
		rootnote.getTransaction_Header().setResponse_code(resp_code);
		rootnote.getTransaction_Header().setResponse_msg(resp_msg);
		// 生成派送员登陆响应报文
		Map<String, String> retMap = convertMapType_Login(rootnote, chinaUms, resp_time, employee, resp_code);
		String responseXml = ChinaUmsXMLHandler.createXMLMessage_Login(retMap, employee, resp_code,chinaUms.getVersion());
		logger.info(rootnote.getTransaction_Header().getTranstype() + "-登录-返回的xml:" + responseXml);

		return responseXml;
	}

	private Map<String, String> convertMapType_Login(Transaction transaction, ChinaUms chinaUms, String resp_time, EmployeeInfo user, String resp_code) {
		Map<String, String> retMap = new HashMap<String, String>();
		// 放入map
		retMap.put("version", transaction.getTransaction_Header().getVersion());
		retMap.put("transtype", transaction.getTransaction_Header().getTranstype());
		retMap.put("employno", transaction.getTransaction_Header().getEmployno());
		retMap.put("termid", transaction.getTransaction_Header().getTermid());
		retMap.put("response_time", resp_time);
		retMap.put("response_code", transaction.getTransaction_Header().getResponse_code());
		retMap.put("response_msg", transaction.getTransaction_Header().getResponse_msg());
		if (user != null && resp_code.equals(ChinaUmsExptMessageEnum.Success.getResp_code())) {
			retMap.put("employname", user.getRealname());
			retMap.put("netcode", user.getBranchid());
			retMap.put("netname", user.getBranchname());
			try {
				User user1 = userDAO.getUserByUsername(user.getUsername());
				retMap.put("mobile", user1.getUsermobile());
				Role role = roleDAO.getRolesByRoleid(user1.getRoleid());
				retMap.put("rolename", role.getRolename());
				retMap.put("exceptioncodeversion", transaction.getTransaction_Header().getVersion());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		String str = ChinaUmsXMLHandler.createMACXML_Login(retMap, user,chinaUms.getVersion());
		String MAC = CreateRespSign(chinaUms, str);
		retMap.put("mac", MAC);
		return retMap;
	}

}

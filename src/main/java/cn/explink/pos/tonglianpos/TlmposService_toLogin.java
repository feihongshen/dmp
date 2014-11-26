package cn.explink.pos.tonglianpos;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import cn.explink.domain.User;
import cn.explink.pos.tonglianpos.xmldto.Transaction;
import cn.explink.pos.tools.EmployeeInfo;
import cn.explink.util.DateTimeUtil;
import cn.explink.util.pos.RSACoder;

@Service
public class TlmposService_toLogin extends TlmposService {
	private Logger logger = LoggerFactory.getLogger(TlmposService_toLogin.class);

	/**
	 * 派送员登录
	 * 
	 * @return
	 */
	public String tologin(Transaction rootnote, Tlmpos tlmpos) {
		EmployeeInfo employee = new EmployeeInfo();
		String resp_code = ""; // 验证消息编码
		String resp_msg = ""; // 返回信息描述
		String username = ""; // 登录用户账号
		String password = ""; // 登录用户密钥
		String resp_time = DateTimeUtil.getNowTime("yyyyMMddHHmmss"); // 请求时间

		User user = null;
		username = rootnote.getTransaction_Body().getDelivery_man();
		password = rootnote.getTransaction_Body().getPassword();
		employee = jiontDAO.getEmployeeInfo(username);
		if (employee == null) {
			resp_code = "08";
			resp_msg = "登陆失败,无此用户名";
			logger.error("登陆失败,没有此用户名!" + username);
		} else {
			String user_pass = employee.getPassword(); // 数据库获取的password

			String md5password = new String(RSACoder.byteToHexString(RSACoder.md5(user_pass.getBytes())));
			if (password.equalsIgnoreCase(md5password)) {
				resp_code = TlmposExptMsgEnum.Success.getResp_code();
				resp_msg = TlmposExptMsgEnum.Success.getResp_msg();
				logger.info("移动POS(tlmpos)登录成功，当前用户名{}", username);
			} else {
				resp_code = TlmposExptMsgEnum.DengLuFaild.getResp_code();
				resp_msg = TlmposExptMsgEnum.DengLuFaild.getResp_msg();
				logger.info("移动POS(tlmpos):用户名为{}登录失败，密码验证错误!", username);
			}
		}
		// 生成派送员登陆响应报文
		Map<String, String> retMap = convertMapType_Login(rootnote.getTransaction_Header().getTransaction_id(), tlmpos, resp_code, resp_msg, username, resp_time, user);
		String responseXml = TlmposXMLHandler.createXMLMessage_Login(retMap);
		logger.info(rootnote.getTransaction_Header().getTransaction_id() + "返回的xml:" + responseXml);

		return responseXml;
	}

	private Map<String, String> convertMapType_Login(String transaction_id, Tlmpos tlmpos, String resp_code, String resp_msg, String username, String resp_time, User user) {
		if (user == null) {
			user = new User();
		}
		Map<String, String> retMap = new HashMap<String, String>();
		// 放入map
		retMap.put("transaction_id", transaction_id);
		retMap.put("delivery_dept_no", tlmpos.getDeliver_dept_no()); // 员工所属单位编码
		retMap.put("delivery_dept", tlmpos.getDeliver_dept()); // 员工所属单位
		retMap.put("resp_code", resp_code);
		retMap.put("resp_msg", resp_msg);
		retMap.put("resp_time", resp_time);
		retMap.put("requester", tlmpos.getRequester());
		retMap.put("target", tlmpos.getTargeter());
		retMap.put("delivery_name", user.getRealname());
		retMap.put("delivery_man", username);
		retMap.put("delivery_zone", user.getUseraddress());

		String str = TlmposXMLHandler.createMACXML_Login(retMap);
		String MAC = CreateRespSign(tlmpos, str);
		retMap.put("MAC", MAC);
		return retMap;
	}

}

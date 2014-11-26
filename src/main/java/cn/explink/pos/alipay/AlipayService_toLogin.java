package cn.explink.pos.alipay;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import cn.explink.domain.User;
import cn.explink.pos.alipay.xml.Transaction;
import cn.explink.pos.tools.EmployeeInfo;
import cn.explink.util.DateTimeUtil;
import cn.explink.util.pos.RSACoder;

@Service
public class AlipayService_toLogin extends AlipayService {
	private Logger logger = LoggerFactory.getLogger(AlipayService_toLogin.class);

	public static void main(String[] args) {
		String user_pass = "123";
		String md5password = RSACoder.filterZeroOdd(new String(RSACoder.byteToHexString(RSACoder.md5(user_pass.getBytes()))));
		System.out.println(md5password);
		// 202CB962AC59075B964B07152D234B70
		// 202CB962AC5975B964B7152D234B70
	}

	/**
	 * 派送员登录
	 * 
	 * @return
	 */
	public String tologin(Transaction rootnote, AliPay alipay) {
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
			// 转换密码,改为md5加密,去掉奇数位0
			// String md5password = RSACoder.filterZeroOdd(new
			// String(RSACoder.byteToHexString(RSACoder.md5(user_pass.getBytes()))));
			String md5password = new String(RSACoder.byteToHexString(RSACoder.md5(user_pass.getBytes())));
			if (password.equalsIgnoreCase(md5password)) {
				resp_code = AliPayExptMessageEnum.Success.getResp_code();
				resp_msg = AliPayExptMessageEnum.Success.getResp_msg();
				logger.info("移动POS(alipay)登录成功，当前用户名{}", username);
			} else {
				resp_code = AliPayExptMessageEnum.DengLuFaild.getResp_code();
				resp_msg = AliPayExptMessageEnum.DengLuFaild.getResp_msg();
				logger.info("移动POS(alipay):用户名为{}登录失败，密码验证错误!", username);
			}
		}
		// 生成派送员登陆响应报文
		Map<String, String> retMap = convertMapType_Login(rootnote.getTransaction_Header().getTransaction_id(), alipay, resp_code, resp_msg, username, resp_time, user);
		String responseXml = AlipayXMLHandler.createXMLMessage_Login(retMap);
		logger.info(rootnote.getTransaction_Header().getTransaction_id() + "返回的xml:" + responseXml);

		return responseXml;
	}

	private Map<String, String> convertMapType_Login(String transaction_id, AliPay alipay, String resp_code, String resp_msg, String username, String resp_time, User user) {
		if (user == null) {
			user = new User();
		}
		Map<String, String> retMap = new HashMap<String, String>();
		// 放入map
		retMap.put("transaction_id", transaction_id);
		retMap.put("delivery_dept_no", alipay.getDeliver_dept_no()); // 员工所属单位编码
		retMap.put("delivery_dept", alipay.getDeliver_dept()); // 员工所属单位
		retMap.put("resp_code", resp_code);
		retMap.put("resp_msg", resp_msg);
		retMap.put("resp_time", resp_time);
		retMap.put("requester", alipay.getRequester());
		retMap.put("target", alipay.getTargeter());
		retMap.put("delivery_name", user.getRealname());
		retMap.put("delivery_man", username);
		retMap.put("delivery_zone", user.getUseraddress());

		String str = AlipayXMLHandler.createMACXML_Login(retMap);
		String MAC = CreateRespSign(alipay, str);
		retMap.put("MAC", MAC);
		return retMap;
	}

}

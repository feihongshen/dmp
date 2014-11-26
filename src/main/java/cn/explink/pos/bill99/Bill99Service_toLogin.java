package cn.explink.pos.bill99;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import cn.explink.pos.bill99.xml.Transaction;
import cn.explink.pos.tools.EmployeeInfo;
import cn.explink.util.DateTimeUtil;
import cn.explink.util.StringUtil;
import cn.explink.util.pos.CertificateCoderUtil;
import cn.explink.util.pos.RSACoder;

@Service
public class Bill99Service_toLogin extends Bill99Service {
	private Logger logger = LoggerFactory.getLogger(Bill99Service_toLogin.class);

	/**
	 * 派送员登录
	 * 
	 * @return
	 */
	public String tologin(Transaction rootnote, Bill99 bill99) {
		EmployeeInfo employee = new EmployeeInfo();
		String resp_code = ""; // 验证消息编码
		String resp_msg = ""; // 返回信息描述
		String username = ""; // 登录用户账号
		String password = ""; // 登录用户密钥
		username = StringUtil.nullConvertToEmptyString(rootnote.getTransaction_Body().getDelivery_man());
		password = StringUtil.nullConvertToEmptyString(rootnote.getTransaction_Body().getPassword());
		employee = jiontDAO.getEmployeeInfo(username);
		if (employee == null) {
			resp_code = "08";
			resp_msg = "登陆失败,无此用户名";
			logger.info("登陆失败,没有此用户名!{}", username);
		} else {
			String user_pass = employee.getPassword(); // 数据库获取的password
			// 转换密码,改为md5加密
			String md5password = new String(RSACoder.byteToHexString(RSACoder.md5(user_pass.getBytes())));
			if (password.equalsIgnoreCase(md5password)) {
				resp_code = "00";
				resp_msg = "登陆成功";
				logger.info("移动POS(bill99)登录成功，当前用户名{}", username);
			} else {
				resp_code = "01";
				resp_msg = "登陆失败,密码验证错误";
				logger.error("移动POS(bill99):用户名为{}登录失败，密码验证错误!", username);
			}
		}

		Map<String, String> retMap = convertMapType_Login(rootnote, bill99, resp_code, resp_msg, employee);
		// 生成派送员登陆响应报文
		String responseXml = Bill99XMLHandler.createXMLMessage_Login(retMap);
		logger.info(rootnote.getTransaction_Header().getTransaction_id() + "返回的xml:" + responseXml);
		return responseXml;
	}

	private Map<String, String> convertMapType_Login(Transaction rootnote, Bill99 bill99, String resp_code, String resp_msg, EmployeeInfo employee) {
		CertificateCoderUtil cfcu = new CertificateCoderUtil(bill99);
		if (employee == null) {
			employee = new EmployeeInfo();
		}
		String resp_time = DateTimeUtil.getNowTime("yyyyMMddHHmmss"); // 请求时间
		Map<String, String> retMap = new HashMap<String, String>();
		// 放入map
		retMap.put("version", bill99.getVersion());
		retMap.put("transaction_sn", rootnote.getTransaction_Header().getTransaction_sn());
		retMap.put("transaction_id", rootnote.getTransaction_Header().getTransaction_id());
		retMap.put("resp_code", resp_code);
		retMap.put("resp_msg", resp_msg);
		retMap.put("resp_time", resp_time);
		retMap.put("requester", bill99.getRequester());
		retMap.put("target", bill99.getTargeter());
		retMap.put("delivery_name", "00".equals(resp_code) ? employee.getRealname() : "");
		retMap.put("delivery_man", employee.getUsername());
		retMap.put("delivery_dept_no", "00".equals(resp_code) ? employee.getBranchid() : "");
		retMap.put("delivery_dept", "00".equals(resp_code) ? employee.getBranchname() : "");

		String str = Bill99XMLHandler.createMACXML_Login(retMap);
		String MAC = "";
		try {
			MAC = cfcu.sign(str.getBytes("utf-8"));
		} catch (Exception e) {
			resp_code = "01";
			resp_msg = "返回签名加密异常";
			logger.info("移动POS(bill99):用户名为{}登录失败，返回签名加密异常!", employee.getUsername());
			e.printStackTrace();
		}
		retMap.put("MAC", MAC);
		return retMap;
	}

}

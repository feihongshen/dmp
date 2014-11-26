package cn.explink.pos.yeepay;

import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.stereotype.Service;

import cn.explink.pos.tools.EmployeeInfo;
import cn.explink.pos.yeepay.xml.YeepayRequest;
import cn.explink.util.DateTimeUtil;

@Service
public class YeePayService_toLogin extends YeePayService {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	private Md5PasswordEncoder md5 = new Md5PasswordEncoder();

	public String toLogin(String service_code, YeepayRequest rootnote, YeePay yeePay) {
		String result_code = "";
		String result_msg = "";
		String Employee_ID = rootnote.getSessionBody().getEmployee_ID();
		String Password = rootnote.getSessionBody().getPassword();
		EmployeeInfo employee = jiontDAO.getEmployeeInfo(Employee_ID);
		if (employee == null) {
			result_code = "11";
			result_msg = "登录失败,没有该用户";
			logger.warn("没有该用户,当前用户名={}", Employee_ID);
		} else {
			// 获取数据库中的密码
			String passwordforLocal = employee.getPassword();
			String md5password = md5.encodePassword(passwordforLocal, null);
			if (!Password.equalsIgnoreCase(md5password)) {
				employee = new EmployeeInfo(); // 清空基本数据。
				result_code = "10";
				result_msg = "用户名或者密码错误";
				logger.error("密码错误,当前用户名=" + Employee_ID);
			} else {
				result_code = "2";
				result_msg = "登陆成功";
				logger.info("yeepay登录成功,当前用户名={}", Employee_ID);
			}
		}
		// 生成返回的xml字符串
		Map<String, String> respMap = convertMapType_Login(service_code, rootnote, yeePay, result_code, result_msg, employee);
		String responseXml = YeePayXMLHandler.createResponseXML_Login(respMap);
		logger.info("返回yeepay数据，业务编码{}，返回XML:{}", service_code, responseXml);
		return responseXml;
	}

	private Map<String, String> convertMapType_Login(String service_code, YeepayRequest rootnote, YeePay yeePay, String result_code, String result_msg, EmployeeInfo employee) {
		if (employee == null) {
			employee = new EmployeeInfo();
		}
		Map<String, String> respMap = new HashMap<String, String>();
		respMap.put("version", rootnote.getSessionHead().getVersion()); // 版本号
		respMap.put("servicecode", service_code); // 请求的业务编号
		respMap.put("transactionid", rootnote.getSessionHead().getTransactionID()); // 交易流水号
		respMap.put("srcsysid", yeePay.getRequester()); // 请求方
		respMap.put("dstsysid", yeePay.getTargeter()); // 响应方
		respMap.put("result_code", result_code); // 响应结果编码
		respMap.put("result_msg", result_msg); // 响应结果消息
		respMap.put("result_time", DateTimeUtil.getNowTime("yyyyMMddHHmmss")); // 响应时间
		respMap.put("employee_id", rootnote.getSessionBody().getEmployee_ID()); // 登录用户名
		respMap.put("employee_name", employee.getRealname()); // 登录用户名称
		respMap.put("company_code", employee.getBranchid()); // 单位编码
		respMap.put("company_name", employee.getBranchname()); // 单位名称
		respMap.put("company_addr", employee.getBranchaddress()); // 单位地址
		respMap.put("company_tel", employee.getBranchmobile() + "/" + employee.getBranchphone()); // 单位联系方式
		// 生成待加密的字符串
		String HMacXml = YeePayXMLHandler.createResponseXMLHMAC_Login(respMap);
		// 加密后的字符串
		String hmac = md5.encodePassword(HMacXml + yeePay.getPrivatekey(), null);
		// 把签名后的数据放在map中用来返回最后的数据
		respMap.put("hmac", hmac);
		return respMap;
	}

}

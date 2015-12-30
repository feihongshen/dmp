package cn.explink.pos.chinaums;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import cn.explink.pos.chinaums.xml.Transaction;
import cn.explink.pos.tools.EmployeeInfo;
import cn.explink.util.DateTimeUtil;

@Service
public class ChinaUmsService_toLogout extends ChinaUmsService {
	private Logger logger = LoggerFactory.getLogger(ChinaUmsService_toLogout.class);

	/**
	 * 派送员登录
	 * 
	 * @return
	 */
	public String tologout(Transaction rootnote, ChinaUms chinaUms) {
		EmployeeInfo employee = new EmployeeInfo();
		String resp_code = ""; // 验证消息编码
		String resp_msg = ""; // 返回信息描述
		String username = ""; // 登录用户账号
		username = rootnote.getTransaction_Header().getEmployno();
		employee = jiontDAO.getEmployeeInfo(username);
		if (employee == null) {
			resp_code = "08";
			resp_msg = "登出失败,无此用户名";
			logger.error("登出失败,没有此用户名!" + username);
		} else {
			resp_code = ChinaUmsExptMessageEnum.Success.getResp_code();
			resp_msg = ChinaUmsExptMessageEnum.Success.getResp_msg();
			logger.info("移动POS(chinaums)登录成功，当前用户名{}", username);
		}
		rootnote.getTransaction_Header().setResponse_code(resp_code);
		rootnote.getTransaction_Header().setResponse_msg(resp_msg);
		// 生成派送员登陆响应报文
		Map<String, String> retMap = convertMapType_Login(rootnote, chinaUms);
		String responseXml = ChinaUmsXMLHandler.createXMLMessage_toExptFeedBack(retMap);
		logger.info(rootnote.getTransaction_Header().getTranstype() + "-登出-返回的xml:" + responseXml);
		return responseXml;
	}

	private Map<String, String> convertMapType_Login(Transaction rootnote, ChinaUms chinaUms) {
		Map<String, String> retMap = new HashMap<String, String>();
		// 放入map
		retMap.put("version", rootnote.getTransaction_Header().getVersion());
		retMap.put("transtype", rootnote.getTransaction_Header().getTranstype());
		retMap.put("employno", rootnote.getTransaction_Header().getEmployno());
		retMap.put("termid", rootnote.getTransaction_Header().getTermid());
		retMap.put("response_time", DateTimeUtil.getNowTimeNo());
		retMap.put("response_code", rootnote.getTransaction_Header().getResponse_code());
		retMap.put("response_msg", rootnote.getTransaction_Header().getResponse_msg());

		// 生成待加密的字符串
		String str = ChinaUmsXMLHandler.createMACXML_cwbSign(retMap);
		String r = CreateRespSign(chinaUms, str);
		retMap.put("mac", r.toUpperCase());
		return retMap;
	}

}

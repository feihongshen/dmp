package cn.explink.pos.chinaums;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import cn.explink.pos.chinaums.xml.Transaction;
import cn.explink.pos.tools.EmployeeInfo;
import cn.explink.util.DateTimeUtil;
import cn.explink.util.MD5.MD5Util;

@Service
public class ChinaUmsService_public {
	private Logger logger = LoggerFactory.getLogger(ChinaUmsService_public.class);
	/**
	 * 将原来的报文加上MAC后生成的报文
	 * @param chinaUms
	 * @param rootnote
	 * @return
	 */
	public String createXML_toExptFeedBack(ChinaUms chinaUms, Transaction rootnote) {
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
		return createXML_toExptFeedBack(retMap);
	}

	private String createXML_toExptFeedBack(Map cwbmap) {
		StringBuffer str = new StringBuffer("");
		str.append("<?xml version='1.0' encoding='UTF-8' ?>" + "<transaction><transaction_header>" + "<version>").append(cwbmap.get("version")).append("</version>" + "<transtype>")
				.append(cwbmap.get("transtype")).append("</transtype>" + "<employno>").append(cwbmap.get("employno")).append("</employno>" + "<termid>").append(cwbmap.get("termid"))
				.append("</termid>" + "<response_time>").append(cwbmap.get("response_time")).append("</response_time>" + "<response_code>").append(cwbmap.get("response_code"))
				.append("</response_code>" + "<response_msg>").append(cwbmap.get("response_msg")).append("</response_msg>" + "<mac>").append(cwbmap.get("mac"))
				.append("</mac>" + "</transaction_header>" + "<transaction_body>").append("</transaction_body></transaction>");
		return str.toString().replaceAll("null", "");
	}
	/**
	 * 拼接签名
	 * @param chinaUms
	 * @param str
	 * @return
	 */
	private String CreateRespSign(ChinaUms chinaUms, String str) {
		String MAC = "";
		try {
			MAC = MD5Util.md5(str.toString() + MD5Util.md5(chinaUms.getPrivate_key()),"UTF-8");
		} catch (Exception e) {
			logger.error("移动POS(chinaums):返回签名加密异常!", e);
			e.printStackTrace();
		}
		return MAC;
	}

}

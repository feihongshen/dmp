package cn.explink.b2c.vipshop;

import java.security.MessageDigest;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

public class VipShopMD5Util {

	/**
	 * 对字符串进行签名
	 */
	public static String MD5(String parameter) {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			byte[] digestResult = md.digest(StringUtils.defaultString(parameter).getBytes("UTF-8"));
			StringBuffer hexValue = new StringBuffer();
			for (int i = 0; i < digestResult.length; i++) {
				int val = ((int) digestResult[i]) & 0xff;
				hexValue.append(StringUtils.leftPad(Integer.toHexString(val), 2, '0'));
			}
			return hexValue.toString().toUpperCase();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return parameter;
	}

	/**
	 * 返回的xml信息解析拼接。 20120514
	 * 
	 * @param orderlist
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static String parseJonitMD5Str(Map<String, Object> parseXmlMap, String private_key) {
		String version = parseXmlMap.get("version") != null ? parseXmlMap.get("version").toString() : null;
		String response_time = parseXmlMap.get("response_time") != null ? parseXmlMap.get("response_time").toString() : null;
		String cust_code = parseXmlMap.get("cust_code") != null ? parseXmlMap.get("cust_code").toString() : null;
		String sys_response_code = parseXmlMap.get("sys_response_code") != null ? parseXmlMap.get("sys_response_code").toString() : null;
		String sys_response_msg = parseXmlMap.get("sys_response_msg") != null ? parseXmlMap.get("sys_response_msg").toString() : null;

		List<Map<String, Object>> orderlist = (List<Map<String, Object>>) parseXmlMap.get("orderlist");

		String returnStrs = private_key + version + response_time + cust_code + sys_response_code + sys_response_msg;
		StringBuffer sub = new StringBuffer(returnStrs);
		if (orderlist != null && orderlist.size() > 0) {
			for (Map<String, Object> datamap : orderlist) {
				String id = convertNullString("id", datamap);
				String seq = convertNullString("seq", datamap);
				String order_sn = convertNullString("order_sn", datamap);
				String box_id = convertNullString("box_id", datamap);
				String buyer_name = convertNullString("buyer_name", datamap);
				String buyer_address = convertNullString("buyer_address", datamap);
				String tel = convertNullString("tel", datamap);
				String mobile = convertNullString("mobile", datamap);
				String post_code = convertNullString("post_code", datamap);
				String transport_day = convertNullString("transport_day", datamap);
				String money = convertNullString("money", datamap);
				sub.append(id + seq + order_sn + box_id + buyer_name + buyer_address + tel + mobile + post_code + transport_day + money);
			}
		}
		return sub.toString();
	}

	private static String convertNullString(String str, Map<String, Object> m) {
		String returnStr = m.get(str) == null ? null : m.get(str).toString();
		return returnStr;
	}

}

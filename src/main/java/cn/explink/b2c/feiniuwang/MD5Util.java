package cn.explink.b2c.feiniuwang;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
public class MD5Util {
	/**
     * 验证签名
     * 
     * @param data_digest
     * @param logistics_interface
     * @return
     */
	public static boolean checkSign(String data_digest, String logistics_interface, String key, String charset) {
		if (StringUtils.isEmpty(data_digest)) {
			return false;
		}
		try {
			String sign = new String(Base64.encodeBase64(MD5Util.code32(
					(logistics_interface + key), charset).getBytes(charset)));
			if (sign.equals(data_digest)) {
				return true;
			} else {
				return false;
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return false;
		}
	}
	public static void main(String[] args) {
		String json = "{\"eccompanyid\":\"飞牛\",\"logisticproviderid\":\"SYZXR\",\"customerid\":\"飞牛\",\"txlogisticid\":\"FN001\",\"tradeno\":\"\",\"mailno\":\"YWH201503181657001\",\"returnno\":\"\",\"totalservicefee\":\"\",\"codsplitfee\":\"\",\"buyservicefee\":\"\",\"ordertype\":\"1\",\"servicetype\":\"\",\"sender\":{\"name\":\"\",\"postcode\":\"\",\"phone\":\"\",\"mobile\":\"\",\"prov\":\"上海\",\"city\":\"上海市,闸北区\",\"address\":\"\"},\"receiver\":{\"name\":\"众人行\",\"postcode\":\"\",\"phone\":\"\",\"mobile\":\"138xxxx\",\"prov\":\"沈阳\",\"city\":\"沈阳,\",\"address\":\"沈阳市天津路\"},\"createordertime\":\"2015-03-18 16:57:58\",\"sendstarttime\":\"\",\"sendendtime\":\"\",\"goodsvalue\":\"0\",\"itemsvalue\":\"0.1\",\"items\":[{\"itemname\":\"测试\",\"number\":\"1\",\"itemvalue\":\"\",\"packagenumber\":\"1\",\"itemservicetype\":\"\"}],\"special\":\"\",\"stockname\":\"\",\"paytype\":\"\",\"weight\":\"0\",\"dispatcharea\":\"\",\"remark\":\"\"}";
		String key = "test";
		System.out.println(doSign(json, key, "UTF-8"));
		
		
	}
	/**
	 * 生成签名
	 * 
	 * @param logistics_interface
	 * @param key
	 * @param charset
	 * @return
	 */
	public static String doSign(String logistics_interface, String key, String charset) {
		try {
			String sign = new String(Base64.encodeBase64(code32(
					(logistics_interface + key), charset).getBytes(charset)));
			return sign;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * MD5 32位加密
	 * 
	 * @param origin
	 *            字符源
	 * @param charset
	 *            字符编码
	 * @return 32位密文
	 */
	public static String code32(String origin, String charset) {
		String resultString = null;
		try {
			resultString = new String(origin);
			MessageDigest md = MessageDigest.getInstance("MD5");
			if (charset == null) {
				resultString = byteArrayToHexString(md.digest(resultString
						.getBytes()));
			} else {
				resultString = byteArrayToHexString(md.digest(resultString
						.getBytes(charset)));
			}
		} catch (Exception exception) {
		}
		return resultString;
	}

	private static String byteArrayToHexString(byte b[]) {
		StringBuffer resultSb = new StringBuffer();
		for (int i = 0; i < b.length; i++) {
			resultSb.append(byteToHexString(b[i]));
		}

		return resultSb.toString();
	}

	private static String byteToHexString(byte b) {
		int n = b;
		if (n < 0)
			n += 256;
		int d1 = n / 16;
		int d2 = n % 16;
		return hexDigits[d1] + hexDigits[d2];
	}

	private static final String hexDigits[] = { "0", "1", "2", "3", "4", "5",
			"6", "7", "8", "9", "a", "b", "c", "d", "e", "f" };
	
	
}

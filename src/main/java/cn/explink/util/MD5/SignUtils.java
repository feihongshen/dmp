package cn.explink.util.MD5;

import java.net.URLEncoder;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * 签名算法
 * <p/>
 * 将请求中所有的参数取出来，包括url和post体（不包含sign） 按照参数名排序，结果为字符串
 */
public class SignUtils {

	public static String sign(LinkedHashMap<String, String> params, String app_secret) {
		try {
			String signSource = "";
			for (Map.Entry entry : params.entrySet()) {
				String key = (String) entry.getKey();
				String value = (String) entry.getValue();
				signSource += key + value;
			}
			signSource += app_secret;
			byte[] signbytes = SignUtils.md5Encrypt(signSource);
			String signStr = SignUtils.byteArrayToHexString(signbytes);
			return signStr;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}

	public static String sign(Map params, String app_secret) {

		List<String> paramNames = new ArrayList<String>(params.size());
		// add and sort key
		paramNames.addAll(params.keySet());
		Collections.sort(paramNames);
		try {
			String signStr = "";
			for (String paramName : paramNames) {
				signStr = signStr + "&" + paramName + "=" + params.get(paramName);
			}
			byte[] signbytes = SignUtils.hmacSHA1Encrypt(signStr.substring(1), app_secret);
			return URLEncoder.encode(SignUtils.encryptBASE64(signbytes), "UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String encryptBASE64(byte[] key) throws Exception {
		return new String(SignUtils.base64Encode(key));
	}

	public static byte[] md5Encrypt(String encryptText) throws Exception {
		MessageDigest digest = MessageDigest.getInstance("MD5");
		digest.update(encryptText.getBytes("utf-8"));
		byte[] md = digest.digest();
		return md;
	}

	public static byte[] hmacSHA1Encrypt(String encryptText, String encryptKey) throws Exception {
		String MAC_NAME = "HmacSHA1";
		String ENCODING = "UTF-8";
		SecretKey secretKey = new SecretKeySpec(encryptKey.getBytes(ENCODING), MAC_NAME);
		Mac mac = Mac.getInstance(secretKey.getAlgorithm());
		mac.init(secretKey);
		byte[] text = encryptText.getBytes(ENCODING);
		return mac.doFinal(text);
	}

	// 十六进制下数字到字符的映射数组
	private final static String[] hexDigits = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f" };

	private final static char[] base64EncodeChars = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c',
			'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '+', '/' };

	private static String base64Encode(byte[] data) {
		StringBuffer sb = new StringBuffer();
		int len = data.length;
		int i = 0;
		while (i < len) {
			int b1 = data[(i++)] & 0xFF;
			if (i == len) {
				sb.append(SignUtils.base64EncodeChars[(b1 >>> 2)]);
				sb.append(SignUtils.base64EncodeChars[((b1 & 0x3) << 4)]);
				sb.append("==");
				break;
			}
			int b2 = data[(i++)] & 0xFF;
			if (i == len) {
				sb.append(SignUtils.base64EncodeChars[(b1 >>> 2)]);
				sb.append(SignUtils.base64EncodeChars[(((b1 & 0x3) << 4) | ((b2 & 0xF0) >>> 4))]);
				sb.append(SignUtils.base64EncodeChars[((b2 & 0xF) << 2)]);
				sb.append("=");
				break;
			}
			int b3 = data[(i++)] & 0xFF;
			sb.append(SignUtils.base64EncodeChars[(b1 >>> 2)]);
			sb.append(SignUtils.base64EncodeChars[(((b1 & 0x3) << 4) | ((b2 & 0xF0) >>> 4))]);
			sb.append(SignUtils.base64EncodeChars[(((b2 & 0xF) << 2) | ((b3 & 0xC0) >>> 6))]);
			sb.append(SignUtils.base64EncodeChars[(b3 & 0x3F)]);
		}
		return sb.toString();
	}

	/**
	 * 转换字节数组为十六进制字符串
	 *
	 * @param b
	 *            byte[]
	 * @return 十六进制字符串
	 */
	public static String byteArrayToHexString(byte[] b) {
		StringBuffer resultSb = new StringBuffer();
		for (int i = 0; i < b.length; i++) {
			resultSb.append(SignUtils.byteToHexString(b[i]));
		}
		return resultSb.toString();
	}

	/**
	 * 将一个字节转化成十六进制形式的字符串 - 32位
	 */
	private static String byteToHexString(byte b) {
		int n = b;
		if (n < 0) {
			n = 256 + n;
		}
		int d1 = n / 16;
		int d2 = n % 16;
		return SignUtils.hexDigits[d1] + SignUtils.hexDigits[d2];
	}

	// 消息体签名
	public static String signVeryfy(String messageBody, String app_secret) {
		String message = messageBody + app_secret;
		try {
			byte[] signbytes = SignUtils.md5Encrypt(message);
			return SignUtils.encryptBASE64(signbytes);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	// 消息体签名
	public static String getMd5Hex16Base64(String messageBody, String app_secret) {
		String message = messageBody + app_secret;
		try {
			byte[] signbytes = SignUtils.md5Encrypt(message);
			String str = SignUtils.byteArrayToHexString(signbytes);
			String str16 = str.substring(8, 24);
			return SignUtils.encryptBASE64(str16.getBytes());

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static void main2(String[] args) {
		LinkedHashMap map = new LinkedHashMap();
		map.put("APP_KEY", "1000014720161102160719K8KS");
		map.put("TIMESTAMP", "2016-11-12 10:06:06");
		map.put("TRANS_ID", "20001112221");
		String token = SignUtils.sign(map, "C06591AA5FC7B0C03541FD0C5A7BB52D");

		System.out.println(token);
	}

	public static void main(String[] args) throws Exception {
		String source = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><Request service=\"OrderService\" lang=\"zh-CN\"><Body><Extra e1=\"abc\" e2=\"abc\"/><Order pay_method=\"1\" d_tel=\"15819050\" orderid=\"TE20150104\" j_mobile=\"13111744\" j_tel=\"13810744\" d_address=\"北京市海淀区中关村\" cargo_height=\"33\" express_type=\"1\" d_contact=\"小邱\" j_province=\"广东省\" j_county=\"福田区\" j_address=\"罗湖火车站东区调度室\" parcel_quantity=\"1\" cargo_length=\"33\" d_company=\"顺丰速运\" cargo_width=\"33\" d_mobile=\"15539050\" j_company=\"罗湖火车站\" j_contact=\"小雷\" j_city=\"深圳\"><Cargo unit=\"a\" name=\"LV1\" count=\"3\"/><Cargo unit=\"a\" name=\"LV2\" count=\"3\"/><AddedService value=\"3000\" name=\"COD\" value1=\"0123456789\"/><AddedService value=\"2304.23\" name=\"INSURE\"/><AddedService value=\"20150612\" name=\"TDELIVERY\" value1=\"1\"/><AddedService name=\"URGENT\"/></Order></Body><Head>BSPdevelop</Head></Request>";
		String appSecret = "hyyt2014";
		System.out.println(SignUtils.getMd5Hex16Base64(source, appSecret));
		// System.out.println(MD5Util.getMd5Hex16Base64(source, appSecret));

		System.out.println(source.trim());
	}
}

package cn.explink.util.MD5;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MD5Util {

	private static String byteArrayToHexString(byte b[]) {
		StringBuffer resultSb = new StringBuffer();
		for (int i = 0; i < b.length; i++)
			resultSb.append(byteToHexString(b[i]));

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

	public static String MD5Encode(String origin, String charsetname) {
		String resultString = null;
		try {
			resultString = new String(origin);
			MessageDigest md = MessageDigest.getInstance("MD5");
			if (charsetname == null || "".equals(charsetname))
				resultString = byteArrayToHexString(md.digest(resultString.getBytes()));
			else
				resultString = byteArrayToHexString(md.digest(resultString.getBytes(charsetname)));
		} catch (Exception exception) {
		}
		return resultString;
	}

	public static void main(String[] args) {
		String str = "LDP00067uvu13a1";
		System.out.println(md5(str));
	}

	public static String md5(String params) {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(params.getBytes());
			byte[] b = md.digest();
			String result = "";
			String temp = "";

			for (int i = 0; i < 16; i++) {
				temp = Integer.toHexString(b[i] & 0xFF);
				if (temp.length() == 1)
					temp = "0" + temp;
				result += temp;
			}
			return result;

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * MD5按照编码方式来 加密
	 * 
	 * @param params
	 * @param charset
	 * @return
	 */
	public static String md5(String params, String charset) {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(params.getBytes(charset));
			byte[] b = md.digest();
			String result = "";
			String temp = "";

			for (int i = 0; i < 16; i++) {
				temp = Integer.toHexString(b[i] & 0xFF);
				if (temp.length() == 1)
					temp = "0" + temp;
				result += temp;
			}
			return result;

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	private static final String hexDigits[] = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f" };

	// 获取MD5 只能使用静态方法 非静态方法获取的值不一样
	public static String getMD5String32Bytes(String text, String dangdangkey) {
		String hex = "";
		text = getRealString(text);
		try {
			try {
				MessageDigest md;
				md = MessageDigest.getInstance("MD5");
				byte[] md5hash = new byte[32];
				md.update(text.getBytes("UTF-8"));
				md5hash = md.digest(dangdangkey.getBytes());
				hex = convertToHex(md5hash);
			} catch (NoSuchAlgorithmException nsae) {
				nsae.printStackTrace();
			}
		} catch (UnsupportedEncodingException nsae) {
			nsae.printStackTrace();
		}
		return hex;
	}

	// 获取替换回车后的值
	public static String getRealString(String s) {
		String reg = "[\n-\r]";
		Pattern p = Pattern.compile(reg);
		Matcher m = p.matcher(s);
		String beizhu = m.replaceAll("");
		return beizhu;
	}

	private static String convertToHex(byte[] data) {
		StringBuffer buf = new StringBuffer();
		for (int i = 0; i < data.length; i++) {
			int halfbyte = (data[i] >>> 4) & 0x0F;
			int two_halfs = 0;
			do {
				if ((0 <= halfbyte) && (halfbyte <= 9))
					buf.append((char) ('0' + halfbyte));
				else
					buf.append((char) ('a' + (halfbyte - 10)));
				halfbyte = data[i] & 0x0F;
			} while (two_halfs++ < 1);
		}
		return buf.toString();
	}

}

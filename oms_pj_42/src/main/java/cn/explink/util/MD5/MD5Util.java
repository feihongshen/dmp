package cn.explink.util.MD5;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.bind.DatatypeConverter;

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

	public static String md5(String params) {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(params.getBytes("UTF-8"));
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

	public static String md5_liantong(String sourceWithSalt) {
		// String sourceWithSalt = signSecurity + "$" + source + "$" +
		// signSecurity;

		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(sourceWithSalt.getBytes("UTF-8"));
			byte[] digest = md.digest();
			return DatatypeConverter.printBase64Binary(digest);
		} catch (Exception e) {
			// Ignore
		}
		return null;
	}

	public static String md5Code(String params) {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(params.getBytes("UTF-8"));
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

	public static void main(String[] args) {
		String strs = "swUiuzms+bAqgNOhTaJAyaTFVdAhGaG2aPCUJ0o72P/dSFj8aRbFDOXjPTBB5oja9plfRNWgTKP1OFWBF2F85A==$appkey$com.aop.app.hyapp$apptx$OPEN12221111031$busitype$02$method$com.aop.method.wlpush$reqxml$<Orders><Order><MailNo>106119552844</MailNo><Steps><Step><AcceptState>ORDE</AcceptState><AcceptTime>2014-03-11 17:51:49</AcceptTime><AcceptAddress>从[北京库房]导入数据</AcceptAddress><AcceptName></AcceptName></Step></Steps></Order></Orders>$timestamp$2014-03-18 17:42:02$wlcompanycode$HY$swUiuzms+bAqgNOhTaJAyaTFVdAhGaG2aPCUJ0o72P/dSFj8aRbFDOXjPTBB5oja9plfRNWgTKP1OFWBF2F85A==";
		System.out.println(md5_liantong(strs));

	}

	public final static String md5forNet(String s) {
		char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
		try {
			byte[] strTemp = s.getBytes("UTF-8");
			MessageDigest mdTemp = MessageDigest.getInstance("MD5");
			mdTemp.update(strTemp);
			byte[] md = mdTemp.digest();
			int j = md.length;
			char str[] = new char[j * 2];
			int k = 0;
			for (int i = 0; i < j; i++) {
				byte byte0 = md[i];
				str[k++] = hexDigits[byte0 >>> 4 & 0xf];
				str[k++] = hexDigits[byte0 & 0xf];
			}
			return new String(str);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 生成32位MD5消息摘要
	 * 
	 * @param info
	 *            String 消息原文
	 * @return
	 */
	public static String getDigestStr(String info) {
		try {
			byte[] res = info.getBytes();
			MessageDigest md = MessageDigest.getInstance("MD5");
			byte[] result = md.digest(res);
			for (int i = 0; i < result.length; i++) {
				md.update(result[i]);
			}
			byte[] hash = md.digest();
			StringBuffer d = new StringBuffer("");
			for (int i = 0; i < hash.length; i++) {
				int v = hash[i] & 0xFF;
				if (v < 16) {
					d.append("0");
				}
				d.append(Integer.toString(v, 16).toUpperCase());
			}
			return d.toString();
		} catch (Exception e) {
			return null;
		}
	}
}

package cn.explink.util.MD5;

public class Base64Utils {

	/**
	 * base64编码
	 * 
	 * @param str
	 * @return
	 * @throws Exception
	 */
	public static String base64(String str, String charset) throws Exception {
		return (new sun.misc.BASE64Encoder()).encode(str.getBytes(charset));
	}

	public static void main(String[] args) throws Exception {
		String strs = "a005c38551a2a0e9b3729ed6f1ecba18";
		System.out.println(base64(strs, "utf-8"));
	}

}
package cn.explink.b2c.zhongliang;

import java.security.MessageDigest;

public class VerifyDataUtil {
	/*
	 * //验证数据 public static boolean VerifyData(String verifyData, String
	 * clientid, String CLIENTFLAG, String CLIENTKEY, String CLIENTCONST, String
	 * CLIENTIP) { if (verifyData.length() <= 5) return false; String rand1 =
	 * verifyData.substring(0,4); String rand2 =
	 * verifyData.substring(verifyData.length() - 4, verifyData.length());
	 * String str = rand2 + CLIENTFLAG + CLIENTKEY + CLIENTCONST + rand1; String
	 * strMd5=""; try { // strMd5 = MD5Util.md5(new
	 * String(str.getBytes("UTF8"))); strMd5 = getMD5(str, "UTF8"); } catch
	 * (Exception e) { e.printStackTrace(); } String CreateVerify = rand1 +
	 * strMd5.substring(7, 21) + rand2; return CreateVerify .equals(verifyData);
	 * }
	 * 
	 * /// 生成加密串 public static String encryptData( String clientid, String
	 * CLIENTFLAG, String CLIENTKEY, String CLIENTCONST) { int N1 = (int)
	 * (Math.random()*9000+1000) ; int N2 = (int) (Math.random()*9000+1000) ;
	 * String str = N2+CLIENTFLAG+CLIENTKEY+CLIENTCONST+N1; String strMd5="";
	 * try { // strMd5 = MD5Util.md5(new String(str.getBytes("UTF8"))); strMd5 =
	 * getMD5(str, "UTF8"); } catch (Exception e) { e.printStackTrace(); }
	 * return N1 + strMd5.substring(7, 21) + N2; }
	 * 
	 * public static String getMD5(String str, String encoding) throws Exception
	 * { MessageDigest md = MessageDigest.getInstance("MD5");
	 * md.update(str.getBytes(encoding)); byte[] result = md.digest();
	 * StringBuffer sb = new StringBuffer(32); for (int i = 0; i <
	 * result.length; i++) { int val = result[i] & 0xff; if (val < 0xf) {
	 * sb.append("0"); } sb.append(Integer.toHexString(val)); } return
	 * sb.toString().toUpperCase(); }
	 */

	public static String getMD5(String str, String encoding) throws Exception {
		MessageDigest md = MessageDigest.getInstance("MD5");
		md.update(str.getBytes(encoding));
		byte[] result = md.digest();
		StringBuffer sb = new StringBuffer(32);
		for (int i = 0; i < result.length; i++) {
			int val = result[i] & 0xff;
			if (val < 0xf) {
				sb.append("0");
			}
			sb.append(Integer.toHexString(val));
		}
		return sb.toString().toUpperCase();
	}

	// / 生成加密串
	public static String encryptData(String clientid, String CLIENTFLAG, String CLIENTKEY, String CLIENTCONST) {
		// /Random rd = new Random();
		int N1 = (int) (Math.random() * 9000 + 1000);
		int N2 = (int) (Math.random() * 9000 + 1000);
		String str = N2 + CLIENTFLAG + CLIENTKEY + CLIENTCONST + N1;
		String strMd5 = "";
		try {

			// strMd5 = MD5Util.md5(new String(str.getBytes("UTF8")));
			strMd5 = getMD5(str, "UTF8").toLowerCase().replace("-", "");
		} catch (Exception e) {
			e.printStackTrace();
		}

		return N1 + strMd5.substring(7, 7 + 21) + N2;
	}
}

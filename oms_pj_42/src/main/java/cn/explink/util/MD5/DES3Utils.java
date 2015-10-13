package cn.explink.util.MD5;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class DES3Utils {

	private static final String Algorithm = "DESede"; // 定义 加密算法,可用

	// public static final String key
	// ="DFSIojslsk;Ffjlk;}@{+nmjdsf$#f@ds45461*#&(()";

	private static byte[] CreateMD5Base64CodeKey(String key) throws NoSuchAlgorithmException, UnsupportedEncodingException {
		MessageDigest md5 = MessageDigest.getInstance("MD5");
		byte[] md5key = md5.digest(key.getBytes("GB2312"));
		BASE64Encoder base64Encoder = new BASE64Encoder();
		String MD5Codekey = base64Encoder.encode(md5key);
		return MD5Codekey.getBytes("GB2312");
	}

	public static String encryptMode(String painText, String key) throws NoSuchAlgorithmException, UnsupportedEncodingException {
		byte[] keybyte = CreateMD5Base64CodeKey(key);
		String enCode = encryptMode(keybyte, painText.getBytes("GB2312"));
		return enCode;
	}

	public static String decryptMode(String enCode, String key) throws NoSuchAlgorithmException, IOException {
		byte[] keybyte = CreateMD5Base64CodeKey(key);
		String deCode = decryptMode(keybyte, enCode);
		return deCode;

	}

	// DES,DESede,Blowfish

	// keybyte为加密密钥，长度为24字节
	// src为被加密的数据缓冲区（源）
	private static String encryptMode(byte[] keybyte, byte[] src) {

		try {

			SecretKey deskey = new SecretKeySpec(keybyte, Algorithm);
			// 加密
			Cipher c1 = Cipher.getInstance(Algorithm);
			c1.init(Cipher.ENCRYPT_MODE, deskey);
			byte[] b = c1.doFinal(src);
			BASE64Encoder base64Encoder = new BASE64Encoder();
			String DesBase64Code = base64Encoder.encode(b);
			return new String(DesBase64Code);
		} catch (java.security.NoSuchAlgorithmException e1) {
			e1.printStackTrace();
		} catch (javax.crypto.NoSuchPaddingException e2) {
			e2.printStackTrace();
		} catch (java.lang.Exception e3) {
			e3.printStackTrace();
		}
		return null;
	}

	// keybyte为加密密钥，长度为24字节
	// src为加密后的缓冲区
	private static String decryptMode(byte[] keybyte, String enCode) throws IOException {
		BASE64Decoder base64Decoder = new BASE64Decoder();
		byte[] src = base64Decoder.decodeBuffer(enCode);
		// byte[] keybyte = key.getBytes();
		try {
			// 生成密钥
			SecretKey deskey = new SecretKeySpec(keybyte, Algorithm);
			// 解密
			Cipher c1 = Cipher.getInstance(Algorithm);
			c1.init(Cipher.DECRYPT_MODE, deskey);
			return new String(c1.doFinal(src));
		} catch (java.security.NoSuchAlgorithmException e1) {
			e1.printStackTrace();
		} catch (javax.crypto.NoSuchPaddingException e2) {
			e2.printStackTrace();
		} catch (java.lang.Exception e3) {
			e3.printStackTrace();
		}
		return null;
	}

	// 转换成十六进制字符串
	// public static String byte2hex(byte[] b) {
	// String hs = "";
	// String stmp = "";
	// for (int n = 0; n < b.length; n++) {
	// stmp = (java.lang.Integer.toHexString(b[n] & 0XFF));
	// if (stmp.length() == 1)
	// hs = hs + "0" + stmp;
	// else
	// hs = hs + stmp;
	// if (n < b.length - 1)
	// hs = hs + ":";
	// }
	// return hs.toUpperCase();
	// }

}
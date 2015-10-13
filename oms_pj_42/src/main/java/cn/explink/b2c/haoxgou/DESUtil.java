package cn.explink.b2c.haoxgou;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.axis.encoding.Base64;

/**
 * 通过DES加密解密实现一个String字符串的加密和解密.
 */
public class DESUtil {

	public static String keyForD2D = "12345678";

	public static String encrypt(String data, String key) throws Exception {
		IvParameterSpec zeroIv = new IvParameterSpec(key.getBytes("UTF-8"));
		SecretKeySpec desKeySpec = new SecretKeySpec(key.getBytes("UTF-8"), "DES");
		Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
		cipher.init(Cipher.ENCRYPT_MODE, desKeySpec, zeroIv);
		byte[] encryptedData = cipher.doFinal(data.getBytes("GBK"));

		// return org.apache.ws.commons.util.Base64.encode(encryptedData);
		return Base64.encode(encryptedData);
	}

	public static String decrypt(String data, String key) throws Exception {
		byte[] byteMi = Base64.decode(data);
		IvParameterSpec zeroIv = new IvParameterSpec(key.getBytes("UTF-8"));
		SecretKeySpec desKeySpec = new SecretKeySpec(key.getBytes("UTF-8"), "DES");
		Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
		cipher.init(Cipher.DECRYPT_MODE, desKeySpec, zeroIv);
		byte decryptedData[] = cipher.doFinal(byteMi);

		return new String(decryptedData, "GBK");
	}
}

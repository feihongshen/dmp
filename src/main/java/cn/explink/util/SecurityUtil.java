package cn.explink.util;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vip.logistics.memberencrypt.Decryption;
import com.vip.logistics.memberencrypt.Encryption;

public class SecurityUtil {

	private static Logger logger = LoggerFactory.getLogger(SecurityUtil.class);

	private static SecurityUtil instance = null;

	public static SecurityUtil getInstance() {
		if (instance == null) {
			instance = new SecurityUtil();
		}
		return instance;
	}

	private SecurityUtil() {
	}

	public String decrypt(String cipherText) {
		try {
			String plainText = Decryption.decrypt(cipherText);
			return plainText;
		} catch (Exception e) {
			logger.error(e.getMessage());
			return cipherText;
		}

	}

	public List<String> decryptMulti(List<String> cipherTexts) {
		try {
			List<String> plainTexts = Decryption.decrypt(cipherTexts);
			return plainTexts;
		} catch (Exception e) {
			logger.error(e.getMessage());
			return cipherTexts;
		}

	}

	public String encrypt(String plainText) {
		try {
//			String cipherText = Encryption.encrypt(plainText);
//			return cipherText;
			return plainText;
		} catch (Exception e) {
			logger.error(e.getMessage());
			return plainText;
		}
	}

	public List<String> encryptMulti(List<String> plainTexts) {
		try {
//			List<String> cipherTexts = Decryption.decrypt(plainTexts);
//			return cipherTexts;
			return plainTexts;
		} catch (Exception e) {
			logger.error(e.getMessage());
			return plainTexts;
		}

	}

}

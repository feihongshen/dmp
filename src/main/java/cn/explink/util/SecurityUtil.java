package cn.explink.util;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cn.explink.dao.SystemInstallDAO;
import cn.explink.service.SystemInstallValueService;

import com.vip.logistics.memberencrypt.Decryption;
import com.vip.logistics.memberencrypt.Encryption;

@Component
public class SecurityUtil {

	private static Logger logger = LoggerFactory.getLogger(SecurityUtil.class);

//	@Autowired
//	private SystemInstallDAO systemInstallDAO;
	
	private final static String DECRYPT_FAIL_RESULT = "******"; 

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
			return DECRYPT_FAIL_RESULT;
		}

	}

	public List<String> decryptMulti(List<String> cipherTexts) {
		try {
			List<String> plainTexts = Decryption.decrypt(cipherTexts);
			return plainTexts;
		} catch (Exception e) {
			logger.error(e.getMessage());
			
			List<String> starStrings = new ArrayList<String>();
			for(int i = 0; i<cipherTexts.size(); i++) {
				starStrings.add(DECRYPT_FAIL_RESULT);
			}
			return starStrings;
		}

	}

	public String encrypt(String plainText) {
		try {
			if (isEncryptionOpen()) {
				String cipherText = Encryption.encrypt(plainText);
				return cipherText;
			} else {
				return plainText;
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
			return plainText;
		}
	}

	private boolean isEncryptionOpen() {
		boolean result = false;
//		result = (systemInstallDAO.getSystemInstall("isEncryptionOpen") == null ? false
//				: Boolean.valueOf(this.systemInstallDAO.getSystemInstall(
//						"isEncryptionOpen").getValue()));
		result = (SystemInstallValueService.getSystemInstallByName("isEncryptionOpen") == null ? false
				: Boolean.valueOf(SystemInstallValueService.getSystemInstallByName("isEncryptionOpen").getValue()));
		
		return result;
	}

	public List<String> encryptMulti(List<String> plainTexts) {
		try {
			if (isEncryptionOpen()) {
				List<String> cipherTexts = Decryption.decrypt(plainTexts);
				return cipherTexts;
			} else {
				return plainTexts;
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
			return plainTexts;
		}

	}
}

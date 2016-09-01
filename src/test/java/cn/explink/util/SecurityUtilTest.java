package cn.explink.util;

import java.util.Arrays;
import java.util.List;

import junit.framework.Assert;

import org.junit.Test;

public class SecurityUtilTest {

	@Test
	public void testEncrypt1() {
		String plainText = "13333333333";
		String actual = SecurityUtil.getInstance().encrypt(plainText);
		String expected = "10$ePEG5zjwsc4GqwkDvTEVpQ==";
		Assert.assertEquals(expected, actual);
	}
	@Test
	public void testEncrypt2() {
		String plainText = "06$RZDkWv4Gq/9RXpEimf+ILw==";
		String actual = SecurityUtil.getInstance().encrypt(plainText);
		String expected = "06$RZDkWv4Gq/9RXpEimf+ILw==";
		Assert.assertEquals(expected, actual);
	}

	@Test
	public void testDecrypt() {
		String cipherText = "07$9KzilwcH/4mBvCgAKprb9Q==";
		String actual = SecurityUtil.getInstance().decrypt(cipherText);
		String expected = "13533123433";
		Assert.assertEquals(expected, actual);
	}	
	@Test
	public void testDecrypt2() {
		String cipherText = "13533123433";
		String actual = SecurityUtil.getInstance().decrypt(cipherText);
		String expected = "13533123433";
		Assert.assertEquals(expected, actual);
	}	
	@Test
	public void testDecrypt3() {
		String cipherText = "01$2Qy+PndY/dCnGLNbdKzqPA==";
		String actual = SecurityUtil.getInstance().decrypt(cipherText);
		String expected = "******";
		Assert.assertEquals(expected, actual);
	}
	
	@Test
	public void testDecryptMulti() {
		String[] tempCipherTexts = {"06$RZDkWv4Gq/9RXpEimf+ILw", "10$ePEG5zjwsc4GqwkDvTEVpQ"};
		List<String> cipherTexts = Arrays.asList(tempCipherTexts);
		List<String> actual = SecurityUtil.getInstance().decryptMulti(cipherTexts);
		String[] tempExpected = {"******", "******"};
		List<String> expected = Arrays.asList(tempExpected);
		Assert.assertEquals(expected, actual);
	}
}

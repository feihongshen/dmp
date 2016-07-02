package cn.explink.util;

import junit.framework.Assert;

import org.junit.Test;

public class SecurityUtilTest {

	@Test
	public void testEncrypt() {
		String plainText = "13333333333";
		String actual = SecurityUtil.getInstance().encrypt(plainText);
		String expected = "06$RZDkWv4Gq/9RXpEimf+ILw==";
		Assert.assertEquals(expected, actual);
	}

	@Test
	public void testDecrypt() {
		String cipherText = "06$RZDkWv4Gq/9RXpEimf+ILw==";
		String actual = SecurityUtil.getInstance().decrypt(cipherText);
		String expected = "13333333333";
		Assert.assertEquals(expected, actual);
	}

}

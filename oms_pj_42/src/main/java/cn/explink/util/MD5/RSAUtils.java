package cn.explink.util.MD5;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;
import cn.explink.b2c.rufengda.Rufengda;

import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;

public class RSAUtils {

	/**
	 * modulePuk 公钥module节点 exponentPublic 公钥Exponent节点
	 * 
	 * modulePrk 私钥module节点 exponetPrivate 公钥 D节点
	 */

	// private static final String modulePuk =
	// "2cuxmVHFfWTI4EXEGOZRnUaCFhcC4x5uL+gMk8V8g6Rp4p64TWHx/ykUr18Ma6fkGU9xLd24UNgUPazhnIJ8PzFiSvSU6AWU1wO0DDsYB5o9b3QPYD4I9ZUwz6XOyE9oaWIh20sfWf0iQ5lcjcVpwcvteKI2e1Q5pzJKwUAD5hc=";
	//
	// private static final String exponentPublic = "AQAB";
	//
	// private static final String modulePrk
	// ="pJjg4pxqgboQsKLZITEsgGBKDKh+3kJfQggKRf37UPv3w+aeZ2IAuH0qVQnaW1tsFMYB0f6YZL0DjlFrXWaMv2oqh72AEMwbaEKeRY1upmSkd5NNpm2NJoKjjySaaxIKQji3ZN5YLtIAFrhqzROf/0tmpnMG9ZdwBns4xoO1HWs=";
	//
	// private static final String exponetPrivate =
	// "Dp8+KyN2Grwqy2ZMH7S8nVLwgRte2ePPjpAkSX98mf8oTZDpNYnxQnJsFk3fxgnjGh1VABC7/QcFj9kPzqZTjnEJPBfKgOZxeVeNg8pi5iPcXggz0qcQ56I9CtfLeL/U0/tV0wNgQBeUlzY1gH/XOG3PxazVb2xmBojK5fKwNwE=";
	//

	/**
	 * 公钥Key获取方法
	 * 
	 * @return
	 * @throws Base64DecodingException
	 */
	public static PublicKey getPublicKey(Rufengda rfd) throws Exception {
		byte[] modulusBytes;
		PublicKey pubKey = null;
		try {
			modulusBytes = Base64.decode(rfd.getModulePuk());
			byte[] exponentBytes = Base64.decode(rfd.getExponentPublic());
			BigInteger modulus = new BigInteger(1, modulusBytes);
			BigInteger exponent = new BigInteger(1, exponentBytes);
			RSAPublicKeySpec rsaPubKey = new RSAPublicKeySpec(modulus, exponent);
			KeyFactory fact = KeyFactory.getInstance("RSA");
			pubKey = fact.generatePublic(rsaPubKey);
		} catch (InvalidKeySpecException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return pubKey;
	}

	/**
	 * 私钥Key获取方法
	 * 
	 * @return
	 * @throws Base64DecodingException
	 */
	public static PrivateKey getPrivateKey(Rufengda rfd) throws Exception {
		byte[] modulusBytes;
		PrivateKey priKey = null;
		try {
			modulusBytes = Base64.decode(rfd.getModulePrk());
			byte[] exponentBytes = Base64.decode(rfd.getExponetPrivate());
			BigInteger modulus = new BigInteger(1, modulusBytes);
			BigInteger exponent = new BigInteger(1, exponentBytes);
			RSAPrivateKeySpec rsaPriKey = new RSAPrivateKeySpec(modulus, exponent);
			KeyFactory fact = KeyFactory.getInstance("RSA");
			priKey = fact.generatePrivate(rsaPriKey);
		} catch (InvalidKeySpecException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return priKey;
	}

	/**
	 * RSA 签名方法 私钥签名
	 * 
	 * @param plainText
	 *            要签名的数据
	 * @return 签名后的密文
	 * @throws NoSuchAlgorithmException
	 * @throws UnsupportedEncodingException
	 * @throws Base64DecodingException
	 */
	public static String sign(String plainText, Rufengda rfd) throws Exception {
		// 明文MD5加密转Base64再签名
		MessageDigest md5 = MessageDigest.getInstance("MD5");
		BASE64Encoder base64en = new BASE64Encoder();
		plainText = base64en.encode(md5.digest(plainText.getBytes("GBK")));
		PrivateKey key = getPrivateKey(rfd);
		java.security.Signature sign;
		try {
			sign = java.security.Signature.getInstance("SHA1withRSA");
			sign.initSign(key);
			sign.update(plainText.getBytes());
			return base64en.encode(sign.sign());
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (SignatureException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * RSA验证方法，公钥验证
	 * 
	 * @param plainText
	 *            明文
	 * @param signed
	 * @return
	 * @throws IOException
	 * @throws Base64DecodingException
	 * @throws NoSuchAlgorithmException
	 */
	public static boolean verify(String plainText, String signed, Rufengda rfd) throws Exception {

		// 明文MD5加密转Base64再签名
		MessageDigest md5 = MessageDigest.getInstance("MD5");
		BASE64Encoder base64en = new BASE64Encoder();
		plainText = base64en.encode(md5.digest(plainText.getBytes("GBK")));
		PublicKey key = getPublicKey(rfd);
		boolean ret = false;
		java.security.Signature signatureChecker;
		try {
			signatureChecker = java.security.Signature.getInstance("SHA1withRSA");
			signatureChecker.initVerify(key);
			BASE64Decoder base64Decoder = new BASE64Decoder();
			byte[] decodebase64Sign = base64Decoder.decodeBufferToByteBuffer(signed).array();
			signatureChecker.update(plainText.getBytes());
			if (signatureChecker.verify(decodebase64Sign)) {
				ret = true;
			}
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (SignatureException e) {
			e.printStackTrace();
		}

		return ret == false ? true : ret;
	}

}

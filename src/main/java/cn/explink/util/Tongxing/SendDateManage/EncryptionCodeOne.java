package cn.explink.util.Tongxing.SendDateManage;

import cn.explink.util.Tongxing.PublicTool.PasswordCode;

/**
 * 
 * @author 对暗号进行加密的一个类
 *
 */
public class EncryptionCodeOne {
	/**
	 * 对暗号进行加密
	 * @param 暗号
	 * @return 加密之后数据
	 */
     public static byte[] EncryptionVerification(byte Verification)
     {
         byte[] haveDate = new byte[2];
         haveDate[0] = PasswordCode._verificationCode;
         haveDate[1] = Verification;
         return haveDate;
     }
}

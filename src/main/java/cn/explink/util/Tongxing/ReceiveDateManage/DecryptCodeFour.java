package cn.explink.util.Tongxing.ReceiveDateManage;

public class DecryptCodeFour {
	/**
	 * 对暗号进行解密,返回0说明是非法数据
	 * @param 收到的暗号数据
	 * @return 暗号数据
	 */
     public static byte DecryptVerification(byte[] Verification)
     {
         if (Verification.length != 2)
             return 0;
         return Verification[1];
     }
}

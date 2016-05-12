package cn.explink.util.Tongxing.SendDateManage;

import cn.explink.util.Tongxing.PublicTool.ByteToDate;
import cn.explink.util.Tongxing.PublicTool.PasswordCode;


public class EncryptionTcpTwo {
	/**
	 * 对TCP发送进行粘包加密；
	 * @param 要加密的数据
	 * @return 加密之后的数据
	 */
    public static byte[] EncryptionPackage(byte[] sendDate)
    {
        byte[] dateAll = new byte[sendDate.length + 8];
        ByteToDate.IntToByte(PasswordCode._stickPackageCode, 0, dateAll);
        ByteToDate.IntToByte(sendDate.length, 4, dateAll);
        ByteToDate.BytesToBytes(sendDate, 8, dateAll);
        return dateAll;
    }
}

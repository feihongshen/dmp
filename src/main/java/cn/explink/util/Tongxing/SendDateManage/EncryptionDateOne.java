package cn.explink.util.Tongxing.SendDateManage;

import java.io.UnsupportedEncodingException;

import cn.explink.util.Tongxing.ClientState;
import cn.explink.util.Tongxing.PublicTool.ByteToDate;
import cn.explink.util.Tongxing.PublicTool.EngineToDate;
import cn.explink.util.Tongxing.PublicTool.PasswordCode;
import cn.explink.util.Tongxing.PublicTool.RandomPublic;
/**
 * 
 * @author 对数据进行第一次加密
 *
 */
public class EncryptionDateOne {
	/**
	 * 对文本和图片数据进行加密;如果长度超过限制，直接抛给文件处理中心
	 * @param EngineToDate
	 * @param ClientState
	 * @return 加密之后的数据
	 */
    public static byte[] encryption(EngineToDate stateCode,ClientState state)
    {
	byte[] returnByte = null;
    if (stateCode.State == PasswordCode._textCode)
    {
        byte textCode = PasswordCode._textCode;
        byte[] date=null;
		try {
			date = stateCode.Datestring.getBytes("UTF-8");
		} catch (UnsupportedEncodingException e) {
		}
        returnByte = encryptionTemporary(date, textCode, state);
    }
    else if (stateCode.State == PasswordCode._photographCode)
    {
        byte photographCode = stateCode.State;
        returnByte = encryptionTemporary(stateCode.DateByte, photographCode, state);
    }
    return returnByte;
}
    /**
     * 一个普通的对数据主体部分进行加密；
     * @param 要加密的数据
     * @param textCode
     * @param ClientState
     * @return 数据
     */
private static byte[] encryptionTemporary(byte[] date, byte textCode,ClientState state)
{
    state.SendDateLabel = RandomPublic.RandomNumber(16787);//给发送的数据进行编号
    byte[] dateOverall = ByteToDate.OffsetEncryption(date, state.SendDateLabel, 2);
    dateOverall[0] = PasswordCode._commonCode; dateOverall[1] = textCode;
    return dateOverall;
}
}

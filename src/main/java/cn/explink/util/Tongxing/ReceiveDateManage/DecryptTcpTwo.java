package cn.explink.util.Tongxing.ReceiveDateManage;

import java.util.ArrayList;
import java.util.Arrays;

import cn.explink.util.Tongxing.ClientState;
import cn.explink.util.Tongxing.PublicTool.ByteToDate;
import cn.explink.util.Tongxing.PublicTool.PasswordCode;
/**
 * 
 * @author 粘包处理类
 *
 */
public class DecryptTcpTwo {
	/**
	 * 对TCP粘包数据进行解密；把所有完整的包通过集合形式返回给客户
	 * @param 接收到的数据
	 * @param 上次残留的数据
	 * @return 返回的数据集合;
	 */
    public static ArrayList<byte[]> DecryptPackage(byte[] receiveDate, ClientState residualpackage)
    {
    	ArrayList<byte[]> listDate = new ArrayList<byte[]>();
        if (receiveDate.length < 4)
            return listDate;
        while (true)
        {
            byte[] haveDate = DecryptByte(receiveDate, residualpackage);
            if (haveDate != null)
                listDate.add(haveDate);
            if (haveDate == null || residualpackage.BufferBackup == null)
                break;
            if (residualpackage.BufferBackup.length < 4)
                break;
            receiveDate = residualpackage.BufferBackup; residualpackage.BufferBackup = null;
        }
        return listDate;
    }
    private static byte[] DecryptByte(byte[] receiveDate, ClientState residualpackage)
    {
    	byte[] BufferBackup=residualpackage.BufferBackup;
        byte[] haveDate = null;
        boolean ddd=false;
        int stickPackageCode = ByteToDate.ByteToInt(0, receiveDate);
        if (stickPackageCode != PasswordCode._stickPackageCode)
        {//第一个与暗号不相同；说明数据有可能是前面一个接下去的
            ddd = true;
            if (BufferBackup != null)
            { //说明真的是前面一个延续下来的
                byte[] addDate = new byte[receiveDate.length + BufferBackup.length];
                ByteToDate.BytesToBytes(BufferBackup, 0, addDate);
                ByteToDate.BytesToBytes(receiveDate, BufferBackup.length, addDate);
                receiveDate = addDate; residualpackage.BufferBackup = null;
            }
            else//不知道是什么数据，直接扔掉
            { return null; }
        }
        if (ddd)
            stickPackageCode = ByteToDate.ByteToInt(0, receiveDate);
        if (stickPackageCode == PasswordCode._stickPackageCode)
        { //第一个暗号相同，说明是一个新包开始了；不会是一个前面的残留包
            if (receiveDate.length < 9)
            { BufferBackup = receiveDate; return null; }
            int datelenth = ByteToDate.ByteToInt(4, receiveDate);
            residualpackage.BufferBackup = null;//对残留数据进行初始化
            if (datelenth == receiveDate.length - 8)
            { //说明整个收到的数据就是一个完整包
                haveDate = Arrays.copyOfRange(receiveDate, 8, datelenth+8);
            }
            else if (datelenth > receiveDate.length - 8)
            { //说明数据没有收完全,把数据放在残留包里进行下一轮接收
            	BufferBackup = receiveDate;
            }
            else
            {//说明有至少二个包粘在一起
                haveDate =Arrays.copyOfRange(receiveDate, 8, datelenth+8);
                residualpackage.BufferBackup = Arrays.copyOfRange(receiveDate, 8 + datelenth, receiveDate.length);
                //把剩下的扔然放在残留里
            }
        }
        return haveDate;
    }
}
package cn.explink.util.Tongxing.ReceiveDateManage;

import cn.explink.util.Tongxing.ClientState;
import cn.explink.util.Tongxing.PublicTool.ByteToDate;

/**
 * 
 * @author 对接收到的数据进行第一次处理；找出需要的数据；把空的去掉
 *
 */
public class ReceiveDateOne {
	/**
	 * 把缓冲区的数据拿出来；并且把缓冲区清空；
	 * @param ClientState
	 * @param 需要的长度
	 * @return 正式接收到的数据
	 */
   public static byte[] DateOneManage(ClientState stateOne,int insert)
   {
       byte[] receiveByte = null;
       if (stateOne.Buffer[0] == 0 && stateOne.BufferBackup != null && stateOne.BufferBackup.length >= insert)
       { receiveByte = stateOne.BufferBackup; stateOne.BufferBackup = null;}//主要用于缓冲区有扩大缩小
       else { receiveByte = stateOne.Buffer;}
       byte[] haveDate = ByteToDate.ByteToByte(receiveByte, insert,0);
       for(byte buffer : stateOne.Buffer)
    	   buffer=0;
       return haveDate;
   }
}

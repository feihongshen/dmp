package cn.explink.util.Tongxing.ReceiveDateManage;


import java.io.UnsupportedEncodingException;

import cn.explink.util.Tongxing.ClientState;
import cn.explink.util.Tongxing.PublicTool.ByteToDate;
import cn.explink.util.Tongxing.PublicTool.EngineToDate;
import cn.explink.util.Tongxing.PublicTool.PasswordCode;

public class DecryptDateFour {
	/**
	 * 对文本和图片数据进行解密;
	 * @param 接收到的数据
	 * @param ClientState
	 * @return EngineToDate
	 */
    public static EngineToDate deciphering(byte[] date, ClientState state)
    {
    	EngineToDate stateCode = null;
        if (date.length < 6)
            return stateCode;//收到的数据不正确
        byte headDate = date[1];
        if (headDate == PasswordCode._textCode || headDate == PasswordCode._photographCode)//解密到的是文本数据
        {
            int SendDateLabel = 0;
            EngineToDate toDate=ByteToDate.OffsetDecrypt(date, 2);
            byte[] dateAll=toDate.DateByte;
            SendDateLabel=toDate.SendDateLabel;
            byte[] ReplyDate = ByteToDate.CombinationTwo(PasswordCode._commonCode, PasswordCode._dateSuccess, SendDateLabel);//直接回复发送成功
            if (headDate == PasswordCode._textCode)
                    {
                        String str="";
						try {
							str = new String(dateAll,"UTF-8");
						} catch (UnsupportedEncodingException e) {
						}
                        stateCode = new EngineToDate(PasswordCode._textCode, str, ReplyDate);//解析出来是文本数据
                    }
            else
                    {
                        stateCode = new EngineToDate(PasswordCode._photographCode, dateAll, ReplyDate);//解释出来是图片数据
                    }
                
        }
        else if (headDate == PasswordCode._dateSuccess)//数据成功或重发
        {
            int SendDateLabel = ByteToDate.ByteToInt(2, date);//找出已发数据的标签
            if (headDate == PasswordCode._dateSuccess)
            {
                stateCode = new EngineToDate(headDate);
                if (SendDateLabel == state.SendDateLabel)
                { state.SendDate = null; }//已经成功对已发数据进行删除
            }
        }
        return stateCode;
    }
}

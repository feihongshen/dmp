package cn.explink.util.Tongxing.ReceiveDateManage;

import cn.explink.util.Tongxing.PublicTool.EngineToDate;
import cn.explink.util.Tongxing.PublicTool.PasswordCode;


/**
 * 
 * @author 对数据进行全方位的分配和过滤；把他们归类成普通数据，文件数据等
 *
 */
public class DecryptDateThree {
	/**
	 * 对数据进行全方位的分配和过滤；把他们归类成普通数据，文件数据等返回null表示违法数据
	 * @param 数据
	 * @return EngineToDate
	 */
    public static EngineToDate Distribution(byte[] date)
    {
    	EngineToDate statecode = null;
        if(date.length<2)
            return statecode;
        byte headcode=date[0];
        if (headcode == PasswordCode._fileCode || headcode == PasswordCode._bigDateCode || headcode == PasswordCode._commonCode || headcode == PasswordCode._verificationCode)
            statecode = new EngineToDate(headcode, date);
        return statecode;
    }
}

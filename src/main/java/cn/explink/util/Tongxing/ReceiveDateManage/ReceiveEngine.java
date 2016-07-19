package cn.explink.util.Tongxing.ReceiveDateManage;

import java.util.ArrayList;
import java.util.Date;

import cn.explink.util.Tongxing.ClientState;
import cn.explink.util.Tongxing.SocketClient;
import cn.explink.util.Tongxing.PublicTool.EngineToDate;
import cn.explink.util.Tongxing.PublicTool.PasswordCode;
import cn.explink.util.Tongxing.SendDateManage.EncryptionCodeOne;


/**
 * 
 * @author 接收的数据全部这里来处理;然后解出需要的数据给调用方!
 * 
 */
public class ReceiveEngine {
	private SocketClient sc;
private ClientState state;
	public ReceiveEngine(SocketClient socketClient) {
		this.sc = socketClient;
		this.state=sc.Clientstate;
	}
/**
 * 接收数据的引擎；当接收到数据之后全部到这个方法里来处理；
 * @param 数据的有效长度
 */
	public void ReceiveDecrypt(int insert) {
		if (insert < 1)
			return;
		byte[] ReceiveDate = ReceiveDateOne.DateOneManage(state,insert);
		// 把真正的有用数据拿出来；然后对缓存区进行重置
		if (ReceiveDate == null)
			return;
		state.NowTime = new Date();
		ArrayList<byte[]> listDate = DecryptTcpTwo.DecryptPackage(ReceiveDate,state);
        //把TCP数据全部解决出来;放到下面的循环里面去处理
        for (byte[] date : listDate)
        {
        	EngineToDate statecode =DecryptDateThree.Distribution(date);
        	if(statecode==null)
        		continue;
            TcpCodeManage(statecode);
        }
	}
	/**
	 * 把分析出来的数据进行归类；如暗号；普通数据等
	 * @param EngineToDate
	 */
    private void TcpCodeManage(EngineToDate statecode)
    {
        if (statecode.State == PasswordCode._verificationCode)//说明是暗号；抛给暗号处理中心
        {
            byte haveDate = DecryptCodeFour.DecryptVerification(statecode.DateByte);
            VerificationCodeManage(haveDate);
        }
        else
        {
            codeManage(statecode); }
    }
    /**
     * 把暗号翻译成机器语言
     * @param 暗号
     */
    private void VerificationCodeManage(byte haveDate)
    {
        switch (haveDate)
        {
            case 0://不是需要的数据
                break;
            case PasswordCode._heartbeatCode://是心跳信息
                //stateOne.HeartTime = DateTime.Now;
                break;
            case PasswordCode._serverToClient://客户端和服务端暗号正确；已登录;
            	sc.Sendmessage.SendDate(EncryptionCodeOne.EncryptionVerification(PasswordCode._clientToServer));
                sc.loginSuccess();//完全登录成功触发此方法
                break;
            case PasswordCode._clientCloseCode://服务器要求客户端关掉;
                sc.CloseEngine("强制关闭");
                break;
        }
    }
  /**
   * 把非暗号数据进行归类；分成普通数据，文件数据等;
   * @param EngineToDate
   */
    private void codeManage(EngineToDate statecode)
    {
    	EngineToDate stateCode = null;
        switch (statecode.State)
        {
            case PasswordCode._commonCode://普通数据信息;抛给普通Code去处理
                stateCode = DecryptDateFour.deciphering(statecode.DateByte, state);
                if(stateCode!=null)
                CommonCodeManage(stateCode);
                break;
            /*case PasswordCode._bigDateCode://抛给分包Code去处理
                stateCode = EncryptionDecryptSeparateDate.FileDecrypt(statecode.DateByte, stateOne);
                CommonCodeManage(stateOne, stateCode);
                break;
            case PasswordCode._fileCode://抛给文件处理器去处理；如果返回null就不用发送了
                byte[] haveDate = FileStart.ReceiveDateTO(statecode.DateByte, stateOne);
                if (haveDate != null)
                    Send(stateOne, haveDate);
                break;*/
        }
    }
    private void CommonCodeManage(EngineToDate stateCode)
    {
        switch (stateCode.State)
        {
            case PasswordCode._textCode://文本信息
            	sc.Sendmessage.SendDate(stateCode.ReplyDate);
            	//System.out.println(stateCode.Datestring);
            	//sc.iTxClient.AcceptString(stateCode.Datestring);//接收到文本信息给外部调用
            	// added by wangwei, 20160713, start
            	sc.AcceptString(stateCode.Datestring);//接收到文本信息给外部调用
            	// added by wangwei, 20160713, end
                break;
            case PasswordCode._photographCode://图片信息
            	sc.Sendmessage.SendDate(stateCode.ReplyDate);
                //接收到图片信息;给外部调用
                break;
            case PasswordCode._dateSuccess://数据发送成功
            	state.SendDate = null;
            	//System.out.println("发送成功");
                //我的数据已经成功收到,给外部调用
                break;
            case 0://说明这个数据只要直接回复给对方就可以了
            	sc.Sendmessage.SendDate(stateCode.ReplyDate);
                break;
        }
    }
}

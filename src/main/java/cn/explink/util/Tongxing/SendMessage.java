package cn.explink.util.Tongxing;

import java.io.IOException;

import cn.explink.util.Tongxing.PublicTool.EngineToDate;
import cn.explink.util.Tongxing.PublicTool.PasswordCode;
import cn.explink.util.Tongxing.SendDateManage.EncryptionDateOne;
import cn.explink.util.Tongxing.SendDateManage.EncryptionTcpTwo;

/**
 * 
 * @author 一个发送数据的类
 *
 */
public class SendMessage implements Runnable {
	private SocketClient sc;
	private Thread thread;
	private byte[] Date;
	private ClientState State;
	SendMessage(SocketClient socketClient) {
		this.sc = socketClient;
		this.State=sc.Clientstate;
	}
	/**
	 * 向服务器发送数据；最基础和原始的
	 * @param 字节集数据
	 */
	public void SendDate(byte[] HaveDate) {
		this.Date=EncryptionTcpTwo.EncryptionPackage(HaveDate);
		thread = new Thread(this);
		thread.setDaemon(true);
		thread.start();
	}
	@Override
	public void run() {
		// TODO 自动生成的方法存根
		try {
			sc.Clientstate.out.write(Date);
		} catch (IOException e) {
			
		}
	}
	/**
	 * 向服务器发送文本数据
	 * @param 文本数据
	 */
    public void sendMessage(String data)
    {
    	EngineToDate stateCode = new EngineToDate(PasswordCode._textCode, data);
        byte[] sendDate = EncryptionDateOne.encryption(stateCode, State);
        State.SendDate = sendDate;
        SendDate(sendDate);
    }
}


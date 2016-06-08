/**
 * 
 */
package cn.explink.util.Tongxing;

import java.util.Date;

import cn.explink.util.Tongxing.PublicTool.PasswordCode;
import cn.explink.util.Tongxing.PublicTool.RandomPublic;
import cn.explink.util.Tongxing.SendDateManage.EncryptionCodeOne;


/**
 * @author 心跳类
 * 
 */
public class HeartBeat implements Runnable {
	private SocketClient sc;
	private Thread thread;
	private ClientState State;
	HeartBeat(SocketClient socketClient) {
		this.sc = socketClient;
		this.State = sc.Clientstate;
		this.StartHeart();
	}
	/**
	 * 启动心跳线程
	 */
	public void StartHeart() {
		thread = new Thread(this);
		thread.setDaemon(true);
		thread.start();
	}

	/**
	 * 心跳线程
	 */
	@Override
	public void run() {
		// TODO 自动生成的方法存根
		while (sc.Clientstate.State == 2) {
			RandomPublic.TheadSleep(10000);
			long a=new Date().getTime()/1000 - State.NowTime.getTime()/1000 ;
			if(a>40)
			{this.HeartOut();
			break;
			}
			sc.Sendmessage.SendDate(EncryptionCodeOne.EncryptionVerification(PasswordCode._heartbeatCode));
			//发送心跳数据给服务器的方法
			RandomPublic.TheadSleep(1000);
		}
	}
	/**
	 * 当客户端心跳超时
	 */
	private void HeartOut()
	{
		sc.lostClient("心跳超时");
	}
}

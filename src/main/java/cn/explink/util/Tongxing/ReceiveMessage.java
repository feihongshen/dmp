/**
 * 
 */
package cn.explink.util.Tongxing;

import java.io.IOException;

import cn.explink.util.Tongxing.ReceiveDateManage.ReceiveEngine;


/**
 * @author 一个接收数据的工厂;所有与接收相关的工作由这里管理
 * 
 */
public class ReceiveMessage implements Runnable {
	private SocketClient sc;
	private Thread thread;
	private ClientState State;
    private ReceiveEngine Re;
	ReceiveMessage(SocketClient socketClient) {
		this.sc = socketClient;
		this.State = sc.Clientstate;
		Re=new ReceiveEngine(sc);
		this.StartReceive();
	}

	/**
	 * 接收数据
	 */
	public void StartReceive() {
		thread = new Thread(this);
		thread.setDaemon(true);
		thread.start();
	}

	@Override
	public void run() {
		try {
			int i = sc.Clientstate.input.read(State.Buffer);
			this.StartReceive();
			Re.ReceiveDecrypt(i);//把数据去处理
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			sc.lostClient("突然掉线");
		}
	}
}

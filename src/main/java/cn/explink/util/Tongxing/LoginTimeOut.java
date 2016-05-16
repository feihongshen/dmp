package cn.explink.util.Tongxing;

import cn.explink.util.Tongxing.PublicTool.RandomPublic;


/**
 * 
 * @author 判断登录超时的类;随着连接的启动而启动
 * 
 */
public class LoginTimeOut implements Runnable {
	private SocketClient sc;
	private Thread thread;
	private int Time = 10;
	private ClientState State;
	LoginTimeOut(SocketClient socketClient) {
		this.sc = socketClient;
		this.State = sc.Clientstate;
	}

	/**
	 * 启动超时判断
	 * 
	 * @param 多久之后认为超时
	 *            ,单位为秒
	 */
	public void StartTime(int time) {
		this.Time = time;
		thread = new Thread(this);
		thread.setDaemon(true);
		thread.start();
	}

	/**
	 * 超时判断线程,当有结果的时候，会自动跳出线程
	 */
	@Override
	public void run() {
		// TODO 自动生成的方法存根
		int timeout = 0;
		while (State.State < 2)// 在没有登录状态或关闭状态一直循环
		{
			RandomPublic.TheadSleep(1000);
			timeout++;
			if (timeout >= Time) {
				this.LoginOut();
				break;
			}
		}
	}
	/**
	 * 登录超时
	 */
	private void LoginOut() {
		if(State.State!=2)//如果在这时刚好他们已经登录成功了;就不用关闭了
		sc.loginFailure("登录超时");
	}
}

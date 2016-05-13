package cn.explink.util.Tongxing;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * 
 * @author 连接服务器的类
 *
 */
public class ClientConnect  {

	private Thread thread;
	private SocketClient sc;
	private ClientState State;
	private String ip;
	private int port;
	ClientConnect(SocketClient socketClient,String Ip,int Port) {
		this.ip=Ip;
		this.port=Port;
		this.sc = socketClient;
		this.State = sc.Clientstate;
		this.StartConnect();
	}
	/**
	 * 启动连接
	 */
	public void StartConnect() {
		try {
			State.WorkSocket = new Socket(this.ip, this.port);
			this.ConnectSuccess();
		} catch (Exception e) {
            this.ConnectFailure();
		}
	}
	/**
	 * 连接成功
	 */
	private void ConnectSuccess()
	{
        try {
        	//读取服务器端数据    
        	State.input = new DataInputStream(State.WorkSocket.getInputStream());
			//向服务器端发送数据    
        	State.out = new DataOutputStream(State.WorkSocket.getOutputStream());
        	State.State=1;//已经连接
        	sc.Receivemessage=new ReceiveMessage(sc);
        	sc.Sendmessage=new SendMessage(sc);
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			sc.loginFailure("开启流失败");
		}
	}
	/**
	 * 连接失败
	 */
	private void ConnectFailure()
	{
		sc.loginFailure("连接服务器失败");
	}
	/**
	 * @return the ip
	 */
	public String getIp() {
		return ip;
	}
	
}
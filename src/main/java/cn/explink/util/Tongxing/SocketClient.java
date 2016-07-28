package cn.explink.util.Tongxing;

import java.io.IOException;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import cn.explink.service.docking.AutoAllocationService;
import cn.explink.util.ApplicationContextUtil;
import cn.explink.util.ItTxFace.ISendTx;
import cn.explink.util.ItTxFace.ITxClient;

/**
 * 
 * @author TCP客户端通信引擎;
 *
 */
public class SocketClient implements ISendTx{
	public ClientConnect Clientconnect;
	public ReceiveMessage Receivemessage;
	public SendMessage Sendmessage;
	public HeartBeat Heartbeat;
	public LoginTimeOut LoginTimeout;
	public ClientState Clientstate;
	public SendClient SendClient;
	/**
	 * 启动连接
	 */
	public void StartEngine(String Ip,int Port)
	{
	    Clientstate=new ClientState();
		Clientconnect=new ClientConnect(this,Ip,Port);//启动连接
		LoginTimeout=new LoginTimeOut(this);//启动超时判断
		LoginTimeout.StartTime(30);
	}
	/**
	 * 当突然掉线的时候触发这个方法
	 */
	public void lostClient(String Reason)
	{
		if(Clientstate.State==3)
			return;
		if(Clientstate.State!=2)
			loginFailure("服务器已满");
		else
		CloseEngine("突然掉线");//不重连了就关闭客户端，释放资源
	}
	/**
	 * 关闭这个通信引擎；释放所有的资源
	 */
	public void CloseEngine(String Reason)
	{
		if(Clientstate.State==3)
			return;//如果已经触发了关闭引擎就返回
		Clientstate.State=3;
			try {
				if(Clientstate.WorkSocket!=null)
				{Clientstate.WorkSocket.close();}
			} catch (IOException e) {
			}//关闭socket
	}
	
	/**
	 * 登录失败之后要处理的一些事情
	 */
	public void loginFailure(String Reason)
	{
		if(Clientstate.State==3)
			return;
    	CloseEngine(Reason);//不重连了就关闭客户端，释放资源
	}
	/**
	 * 当完全登录成功之后要处理的一些事情
	 */
	public void loginSuccess()
	{
		Clientstate.State=2;
		Heartbeat=new HeartBeat(this);
	}
	/**
	 * 发送文本信息
	 * @param 文本数据
	 */
	public void sendMessage(String data)
	{
		if(Clientstate==null || Clientstate.State!=2)
			return;
		this.Sendmessage.sendMessage(data);
	}

	// added by wangwei, 20160714, start
	public void AcceptString(String Message) {
		SendClient=new SendClient();
		this.SendClient.AcceptString(Message);
	}
	// added by wangwei, 20160714, end
}

package cn.explink.util.Tongxing;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.Date;

public class ClientState {
	public Socket WorkSocket;
	public byte[] Buffer=new byte[1024];// 缓冲区
	public byte[] SendDate = null;// 已发送的数据
	public int SendDateLabel = 0;// 发送数据的标签
	public byte[] BufferBackup = null;// 备份缓冲区;主要是缓冲区有时候需要增大或缩小的时候用到；
	public Date NowTime = new Date();
	public DataInputStream input;
	public DataOutputStream out;
	public int State=0;//客户端状态，0代表没任何登录，1已连接未登录，2表示已登录,3表示已触发关闭引擎
}

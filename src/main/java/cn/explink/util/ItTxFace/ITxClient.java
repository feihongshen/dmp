package cn.explink.util.ItTxFace;
/**
 * 
 * @author 与服务器联动接口中心
 *
 */
public interface ITxClient {
	/**
	 * 引擎完全关闭触发此方法
	 * @param 引擎被关闭的原因
	 */
	void CloseEngineEnd(String Reason);
	/**
	 * 当从服务器接收到文本信息的时候触发此方法
	 * @param 接收到的文本数据
	 */
	void AcceptString(String Message);
	/**
	 * 连接成功的时候触发此方法
	 */
	void loginSuccess();
}

package cn.explink.util.ItTxFace;
/**
 * 
 * @author 外部类调用的一个接口；
 *
 */
public interface ISendTx {
	/**
	 * 启动引擎
	 */
    void StartEngine(String ip,int port);
    /**
     * 关闭引擎
     */
    void CloseEngine(String Reason);
  /**
   * 客户端向服务器发送文本数据
   * @param 文本数据
   */
    void sendMessage(String data);
}

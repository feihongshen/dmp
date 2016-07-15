package cn.explink.util.Tongxing;

import cn.explink.service.docking.AutoAllocationHelper;
import cn.explink.util.ItTxFace.ITxClient;

/**
 * 发送端接收返回消息用
 * 
 * @author wangwei 2016年7月14日
 *
 */
public class SendClient implements ITxClient {

	@Override
	public void CloseEngineEnd(String Reason) {
		// TODO Auto-generated method stub

	}

	@Override
	public void AcceptString(String Message) {
		AutoAllocationHelper.handleResult(Message);
	}

	@Override
	public void loginSuccess() {
		// TODO Auto-generated method stub

	}

}

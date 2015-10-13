package cn.explink.b2c.gztlfeedback.webservice;

import javax.jws.WebService;

@WebService
public interface GztlFeedbackWebservice {
	/**
	 * 写接口定义的方法
	 */

	public String orderAndFeedbackApi(String code, String invokeMethod, String sign, String xml);

}

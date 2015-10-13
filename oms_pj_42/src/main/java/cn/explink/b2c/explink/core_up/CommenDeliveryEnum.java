package cn.explink.b2c.explink.core_up;

import cn.explink.enumutil.FlowOrderTypeEnum;

/**
 * 允许被查询到的状态
 * 
 * @author Administrator
 *
 */
public enum CommenDeliveryEnum {

	ChengGong("SIGN", "成功"), JuShou("REFUSE", "拒收"), FenZhanZhiLiu("RETENTION", "分站滞留"), ;

	private String request_code; // 请求的指令
	private String text;

	private CommenDeliveryEnum(String request_code, String text) {

		this.request_code = request_code;
		this.text = text;

	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getRequest_code() {
		return request_code;
	}

	public void setRequest_code(String request_code) {
		this.request_code = request_code;
	}

}

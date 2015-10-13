package cn.explink.b2c.explink.code_down;

/**
 * epai 回传配送结果
 * 
 * @author Administrator
 *
 */
public enum EPaiDeliverStateEnum {

	ChengGong("SIGN", "成功"), JuShou("REFUSE", "拒收"), FenZhanZhiLiu("RETENTION", "分站滞留"), ;

	private String request_code; // 请求的指令
	private String text;

	private EPaiDeliverStateEnum(String request_code, String text) {

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

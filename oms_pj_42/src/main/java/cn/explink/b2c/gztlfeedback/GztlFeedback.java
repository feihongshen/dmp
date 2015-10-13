package cn.explink.b2c.gztlfeedback;

public class GztlFeedback {
	/**
	 * 广州通路外发单订单反馈推送接口需要的基础设置的字段
	 */
	private String customerids; // 在系统中的ids

	private String search_url; // 提供给乐峰查询的URL

	private long search_number;// 要查询的数量

	private String password;// 密码

	private String private_key;// 私匙

	private String sign;// MD5加密（详细见7.1MD5数字验签）

	private String invokeMethod;// 接口名称（FBI）

	private String code;// 客户编码（由飞远提供）

	private String receive_url;// 推送接收url

	private long warehouseid; // 订单入库库房

	private int loopCount;// 重发次数

	public long getWarehouseid() {
		return this.warehouseid;
	}

	public void setWarehouseid(long warehouseid) {
		this.warehouseid = warehouseid;
	}

	public String getReceive_url() {
		return this.receive_url;
	}

	public void setReceive_url(String receive_url) {
		this.receive_url = receive_url;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getCustomerids() {
		return this.customerids;
	}

	public void setCustomerids(String customerids) {
		this.customerids = customerids;
	}

	public String getSearch_url() {
		return this.search_url;
	}

	public void setSearch_url(String search_url) {
		this.search_url = search_url;
	}

	public long getSearch_number() {
		return this.search_number;
	}

	public void setSearch_number(long search_number) {
		this.search_number = search_number;
	}

	public String getPrivate_key() {
		return this.private_key;
	}

	public void setPrivate_key(String private_key) {
		this.private_key = private_key;
	}

	public String getSign() {
		return this.sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public String getInvokeMethod() {
		return this.invokeMethod;
	}

	public void setInvokeMethod(String invokeMethod) {
		this.invokeMethod = invokeMethod;
	}

	public String getCode() {
		return this.code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public int getLoopCount() {
		return this.loopCount;
	}

	public void setLoopCount(int loopCount) {
		this.loopCount = loopCount;
	}

}

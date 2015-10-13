package cn.explink.b2c.yixun;

public class YiXun {

	private int count; // 每次最大发送数量
	private String apikey;
	private String feedback_url; // 反馈地址
	private String secret; // //加密密钥
	private String customerids; // 配送公司中的id。可逗号隔开
	private String request_url; // 提供给易迅网的接口
	private long warehouseid; // 订单导入库房Id

	public String getRequest_url() {
		return request_url;
	}

	public void setRequest_url(String request_url) {
		this.request_url = request_url;
	}

	public long getWarehouseid() {
		return warehouseid;
	}

	public void setWarehouseid(long warehouseid) {
		this.warehouseid = warehouseid;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public String getApikey() {
		return apikey;
	}

	public void setApikey(String apikey) {
		this.apikey = apikey;
	}

	public String getFeedback_url() {
		return feedback_url;
	}

	public void setFeedback_url(String feedback_url) {
		this.feedback_url = feedback_url;
	}

	public String getSecret() {
		return secret;
	}

	public void setSecret(String secret) {
		this.secret = secret;
	}

	public String getCustomerids() {
		return customerids;
	}

	public void setCustomerids(String customerids) {
		this.customerids = customerids;
	}

}

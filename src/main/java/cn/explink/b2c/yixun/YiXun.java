package cn.explink.b2c.yixun;

public class YiXun {

	private String customerids; // 易迅网在系统中的customerid
	private String request_url; // 提供给易迅的url，用于接收订单明细数据
	private String feedback_url; // 反馈信息，易迅网URL

	private int count; // 每次推送给 易迅的个数
	private long warehouseid; // 易迅的仓库id
	private String apikey; // 配送公司在易迅网那边的唯一标识。
	private String secret; // 密钥信息

	public long getWarehouseid() {
		return warehouseid;
	}

	public void setWarehouseid(long warehouseid) {
		this.warehouseid = warehouseid;
	}

	public String getCustomerids() {
		return customerids;
	}

	public void setCustomerids(String customerids) {
		this.customerids = customerids;
	}

	public String getRequest_url() {
		return request_url;
	}

	public void setRequest_url(String request_url) {
		this.request_url = request_url;
	}

	public String getFeedback_url() {
		return feedback_url;
	}

	public void setFeedback_url(String feedback_url) {
		this.feedback_url = feedback_url;
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

	public String getSecret() {
		return secret;
	}

	public void setSecret(String secret) {
		this.secret = secret;
	}

}

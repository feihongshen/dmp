package cn.explink.b2c.homegobj;

public class Homegobj {

	private String express_id;
	private String private_key; // 密钥
	private long maxCount; // 每次查询数量

	public long getMaxCount() {
		return maxCount;
	}

	public void setMaxCount(long maxCount) {
		this.maxCount = maxCount;
	}

	private String getOrder_url; // 订单下载url
	private String feedback_url; // 状态反馈url
	private long pageSize;

	public long getPageSize() {
		return pageSize;
	}

	public void setPageSize(long pageSize) {
		this.pageSize = pageSize;
	}

	public String getFeedback_url() {
		return feedback_url;
	}

	public void setFeedback_url(String feedback_url) {
		this.feedback_url = feedback_url;
	}

	private long warehouseid; // 库房
	private String customerid; // 供货商 id
	private int beforhours; // 提前的小时数
	private int loopcount; // 循环次数

	public String getExpress_id() {
		return express_id;
	}

	public void setExpress_id(String express_id) {
		this.express_id = express_id;
	}

	public String getPrivate_key() {
		return private_key;
	}

	public void setPrivate_key(String private_key) {
		this.private_key = private_key;
	}

	public String getGetOrder_url() {
		return getOrder_url;
	}

	public void setGetOrder_url(String getOrder_url) {
		this.getOrder_url = getOrder_url;
	}

	public long getWarehouseid() {
		return warehouseid;
	}

	public void setWarehouseid(long warehouseid) {
		this.warehouseid = warehouseid;
	}

	public String getCustomerid() {
		return customerid;
	}

	public void setCustomerid(String customerid) {
		this.customerid = customerid;
	}

	public int getBeforhours() {
		return beforhours;
	}

	public void setBeforhours(int beforhours) {
		this.beforhours = beforhours;
	}

	public int getLoopcount() {
		return loopcount;
	}

	public void setLoopcount(int loopcount) {
		this.loopcount = loopcount;
	}

}

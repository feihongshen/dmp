package cn.explink.b2c.yihaodian.addressmatch;

public class YihaodianAddMatch {
	private String userCode; // 用户代码，用来标识哪个承运商
	private String private_key; // 密钥信息
	private String receiver_url;// 接收地址

	public String getReceiver_url() {
		return receiver_url;
	}

	public void setReceiver_url(String receiver_url) {
		this.receiver_url = receiver_url;
	}

	private long maxCount; // 每次允许最大限制数

	public String getUserCode() {
		return userCode;
	}

	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}

	public String getPrivate_key() {
		return private_key;
	}

	public void setPrivate_key(String private_key) {
		this.private_key = private_key;
	}

	public long getMaxCount() {
		return maxCount;
	}

	public void setMaxCount(long maxCount) {
		this.maxCount = maxCount;
	}

}

package cn.explink.b2c.smile;

/**
 * 广州思迈速递接口 属性 设置
 * 
 * @author Administrator
 *
 */
public class Smile {

	private String customerids; // 在系统中的id
	private String secretKey; // 加密字符串 双方约定
	private String feedback_url; // 反馈给供方的地址
	private int maxCount; // 每次查询的最大数据量
	private String sendClientLoge; // 发送客户标识（公司名称）-跟踪接口
	private String actionName; // 订单下单指令 固定等于 RequestOrder

	public String getActionName() {
		return actionName;
	}

	public void setActionName(String actionName) {
		this.actionName = actionName;
	}

	public String getSendClientLoge() {
		return sendClientLoge;
	}

	public void setSendClientLoge(String sendClientLoge) {
		this.sendClientLoge = sendClientLoge;
	}

	public String getCustomerids() {
		return customerids;
	}

	public void setCustomerids(String customerids) {
		this.customerids = customerids;
	}

	public String getSecretKey() {
		return secretKey;
	}

	public void setSecretKey(String secretKey) {
		this.secretKey = secretKey;
	}

	public String getFeedback_url() {
		return feedback_url;
	}

	public void setFeedback_url(String feedback_url) {
		this.feedback_url = feedback_url;
	}

	public int getMaxCount() {
		return maxCount;
	}

	public void setMaxCount(int maxCount) {
		this.maxCount = maxCount;
	}

}

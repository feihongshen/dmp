package cn.explink.b2c.shenzhoushuma;

/**
 * 神州数码 配置信息实体
 * @author yurong.liang 2016-04-29
 */
public class ShenZhouShuMa {
	
	private String logisticProvider; // 代运简称
	private String logisticProviderId; // 代运编码
	private int customerId;//客户id
	private int maxCount;//每次推送轨迹最大数量
	private String feedBackUrl;//反馈URL
	private String privateKey;// 秘钥
	
	public String getFeedBackUrl() {
		return feedBackUrl;
	}
	public void setFeedBackUrl(String feedBackUrl) {
		this.feedBackUrl = feedBackUrl;
	}
	public int getCustomerId() {
		return customerId;
	}
	public void setCustomerId(int customerId) {
		this.customerId = customerId;
	}
	public int getMaxCount() {
		return maxCount;
	}
	public void setMaxCount(int maxCount) {
		this.maxCount = maxCount;
	}
	public String getLogisticProvider() {
		return logisticProvider;
	}
	public String getLogisticProviderId() {
		return logisticProviderId;
	}
	public void setLogisticProvider(String logisticProvider) {
		this.logisticProvider = logisticProvider;
	}
	public void setLogisticProviderId(String logisticProviderId) {
		this.logisticProviderId = logisticProviderId;
	}
	public String getPrivateKey() {
		return privateKey;
	}
	public void setPrivateKey(String privateKey) {
		this.privateKey = privateKey;
	}

}

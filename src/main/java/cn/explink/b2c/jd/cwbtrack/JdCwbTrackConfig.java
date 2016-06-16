package cn.explink.b2c.jd.cwbtrack;
/**
 * 京东_订单跟踪接口 配置
 * @author yurong.liang 2016/03/07
 */
public class JdCwbTrackConfig {
	
	private int maxCount;//每次最多请求单号个数
	private long customerId;//客户ID
	private String privateKey;//私钥

	public int getMaxCount() {
		return maxCount;
	}

	public void setMaxCount(int maxCount) {
		this.maxCount = maxCount;
	}

	public long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(long customerId) {
		this.customerId = customerId;
	}

	public String getPrivateKey() {
		return privateKey;
	}

	public void setPrivateKey(String privateKey) {
		this.privateKey = privateKey;
	}


}

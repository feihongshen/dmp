package cn.explink.b2c.smiled;

/**
 * 广州思迈速递接口 属性 设置
 * 
 * @author Administrator
 *
 */
public class Smiled {

	private String express_id;
	private String private_key; // 加密字符串 双方约定
	private String send_url;
	private String feedback_url; // 反馈给供方的地址
	private long loopcount;
	private long resendcount; // 失败重发次数

	public long getResendcount() {
		return resendcount;
	}

	public void setResendcount(long resendcount) {
		this.resendcount = resendcount;
	}

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

	public String getSend_url() {
		return send_url;
	}

	public void setSend_url(String send_url) {
		this.send_url = send_url;
	}

	public String getFeedback_url() {
		return feedback_url;
	}

	public void setFeedback_url(String feedback_url) {
		this.feedback_url = feedback_url;
	}

	public long getLoopcount() {
		return loopcount;
	}

	public void setLoopcount(long loopcount) {
		this.loopcount = loopcount;
	}

	public long getMaxCount() {
		return maxCount;
	}

	public void setMaxCount(long maxCount) {
		this.maxCount = maxCount;
	}

	private long maxCount;

}

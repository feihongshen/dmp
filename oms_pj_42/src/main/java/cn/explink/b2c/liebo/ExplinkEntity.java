package cn.explink.b2c.liebo;

public class ExplinkEntity {

	private String express_id; // 快递公司的唯一标识
	private int maxcount; // 每次最大发送数量
	private String private_key; // 加密密钥
	private String cwbstate_url; // 订单状态反馈URL

	public String getExpress_id() {
		return express_id;
	}

	public void setExpress_id(String express_id) {
		this.express_id = express_id;
	}

	public int getMaxcount() {
		return maxcount;
	}

	public void setMaxcount(int maxcount) {
		this.maxcount = maxcount;
	}

	public String getPrivate_key() {
		return private_key;
	}

	public void setPrivate_key(String private_key) {
		this.private_key = private_key;
	}

	public String getCwbstate_url() {
		return cwbstate_url;
	}

	public void setCwbstate_url(String cwbstate_url) {
		this.cwbstate_url = cwbstate_url;
	}

	public String getTrackinfo_url() {
		return trackinfo_url;
	}

	public void setTrackinfo_url(String trackinfo_url) {
		this.trackinfo_url = trackinfo_url;
	}

	public String getCustomerids() {
		return customerids;
	}

	public void setCustomerids(String customerids) {
		this.customerids = customerids;
	}

	private String trackinfo_url; // 订单跟踪日志反馈接口
	private String customerids; // 当当在配送公司中的id。可逗号隔开

}

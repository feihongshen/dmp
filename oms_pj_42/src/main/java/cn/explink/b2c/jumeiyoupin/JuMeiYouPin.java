package cn.explink.b2c.jumeiyoupin;

public class JuMeiYouPin {

	private String express_id; // 快递公司的唯一标识
	private int count; // 每次最大发送数量
	private String private_key; // 加密密钥
	private String tuisong_url; // 推送的URL
	private String own_url; // 自己的接口url
	private String customerids; // 在配送公司中的id。可逗号隔开
	private String descirbe; // 导入数据的描述信息

	public String getDescirbe() {
		return descirbe;
	}

	public void setDescirbe(String descirbe) {
		this.descirbe = descirbe;
	}

	public String getCustomerids() {
		return customerids;
	}

	public void setCustomerids(String customerids) {
		this.customerids = customerids;
	}

	public String getExpress_id() {
		return express_id;
	}

	public void setExpress_id(String express_id) {
		this.express_id = express_id;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public String getPrivate_key() {
		return private_key;
	}

	public void setPrivate_key(String private_key) {
		this.private_key = private_key;
	}

	public String getTuisong_url() {
		return tuisong_url;
	}

	public void setTuisong_url(String tuisong_url) {
		this.tuisong_url = tuisong_url;
	}

	public String getOwn_url() {
		return own_url;
	}

	public void setOwn_url(String own_url) {
		this.own_url = own_url;
	}

}

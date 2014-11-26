package cn.explink.b2c.dangdang;

public class DangDang {

	private String express_id; // 快递公司的唯一标识
	private int count; // 每次最大发送数量
	private String private_key; // 加密密钥
	private String ruku_url; // 入库信息的URL
	private String chukuPaiSong_url; // 出库的url
	private String deliverystate_url; // 配送结果状态接口url
	private String trackinfo_url; // 订单跟踪日志反馈接口
	private String customerids; // 当当在配送公司中的id。可逗号隔开

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

	public String getRuku_url() {
		return ruku_url;
	}

	public void setRuku_url(String ruku_url) {
		this.ruku_url = ruku_url;
	}

	public String getChukuPaiSong_url() {
		return chukuPaiSong_url;
	}

	public void setChukuPaiSong_url(String chukuPaiSong_url) {
		this.chukuPaiSong_url = chukuPaiSong_url;
	}

	public String getDeliverystate_url() {
		return deliverystate_url;
	}

	public void setDeliverystate_url(String deliverystate_url) {
		this.deliverystate_url = deliverystate_url;
	}

	public String getTrackinfo_url() {
		return trackinfo_url;
	}

	public void setTrackinfo_url(String trackinfo_url) {
		this.trackinfo_url = trackinfo_url;
	}

}

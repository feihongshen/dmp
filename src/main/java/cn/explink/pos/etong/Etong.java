package cn.explink.pos.etong;

public class Etong {

	private String sender_url; // 发送URL
	private String receiver_url; // 接收URL
	private String express_id; // 唯一标示

	public String getExpress_id() {
		return express_id;
	}

	public void setExpress_id(String express_id) {
		this.express_id = express_id;
	}

	public String getSender_url() {
		return sender_url;
	}

	public void setSender_url(String sender_url) {
		this.sender_url = sender_url;
	}

	public String getReceiver_url() {
		return receiver_url;
	}

	public void setReceiver_url(String receiver_url) {
		this.receiver_url = receiver_url;
	}

}

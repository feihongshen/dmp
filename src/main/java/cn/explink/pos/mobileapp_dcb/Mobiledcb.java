package cn.explink.pos.mobileapp_dcb;

public class Mobiledcb {

	private String sender_url; // 发送URL
	private String sender_usersyn; // 配送员信息同步接口发送URL

	public String getSender_usersyn() {
		return sender_usersyn;
	}

	public void setSender_usersyn(String sender_usersyn) {
		this.sender_usersyn = sender_usersyn;
	}

	private String receiver_url; // 接收URL

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

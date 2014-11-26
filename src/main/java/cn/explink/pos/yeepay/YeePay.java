package cn.explink.pos.yeepay;

public class YeePay {

	private String version; // 版本号
	private String requester; // 请求方
	private String targeter; // 应答方
	private String privatekey; // 加密秘钥
	private int isotheroperator = 0; // 是否限制他人刷卡

	public int getIsotheroperator() {
		return isotheroperator;
	}

	public void setIsotheroperator(int isotheroperator) {
		this.isotheroperator = isotheroperator;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getRequester() {
		return requester;
	}

	public void setRequester(String requester) {
		this.requester = requester;
	}

	public String getTargeter() {
		return targeter;
	}

	public void setTargeter(String targeter) {
		this.targeter = targeter;
	}

	public String getPrivatekey() {
		return privatekey;
	}

	public void setPrivatekey(String privatekey) {
		this.privatekey = privatekey;
	}

}

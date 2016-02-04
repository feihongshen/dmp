package cn.explink.b2c.tonglian;

public class TongLian {
	/*
	 * 用户名
	 */
	private String userName;
	/*
	 * 用户密码
	 */
	private String userPass;
	/*
	 * 商户证书路径（生成【SIGNED_MSG】签名信息用到）
	 */
	private String pfxPath;
	/*
	 * 商户证书密码（生成【SIGNED_MSG】签名信息用到）
	 */
	private String pfxPassword;
	/*
	 * 安全证书（生成【SIGNED_MSG】签名信息用到）
	 */
	private String tltcerPath;
	/*
	 * 商户代码
	 */
	private String merchantId;

	public String getUserName() {
		return this.userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserPass() {
		return this.userPass;
	}

	public void setUserPass(String userPass) {
		this.userPass = userPass;
	}

	public String getPfxPath() {
		return this.pfxPath;
	}

	public void setPfxPath(String pfxPath) {
		this.pfxPath = pfxPath;
	}

	public String getPfxPassword() {
		return this.pfxPassword;
	}

	public void setPfxPassword(String pfxPassword) {
		this.pfxPassword = pfxPassword;
	}

	public String getMerchantId() {
		return this.merchantId;
	}

	public void setMerchantId(String merchantId) {
		this.merchantId = merchantId;
	}

	public String getTltcerPath() {
		return this.tltcerPath;
	}

	public void setTltcerPath(String tltcerPath) {
		this.tltcerPath = tltcerPath;
	}

}

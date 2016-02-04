package cn.explink.b2c.caifutong;

public class CaiFuTong {
	/*
	 * 商户号
	 */
	private String partner;
	/*
	 * 商户号密钥
	 */
	private String key;
	/*
	 * 操作员帐号
	 */
	private String opUser;
	/*
	 * 操作员密码
	 */
	private String opPasswd;
	/*
	 * CA证书路径
	 */
	private String caInfo;
	/*
	 * 商户证书
	 */
	private String certInfo;
	/*
	 * 商户证书密码
	 */
	private String certInfoPasswd;

	public String getPartner() {
		return this.partner;
	}

	public void setPartner(String partner) {
		this.partner = partner;
	}

	public String getKey() {
		return this.key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getOpUser() {
		return this.opUser;
	}

	public void setOpUser(String opUser) {
		this.opUser = opUser;
	}

	public String getOpPasswd() {
		return this.opPasswd;
	}

	public void setOpPasswd(String opPasswd) {
		this.opPasswd = opPasswd;
	}

	public String getCaInfo() {
		return this.caInfo;
	}

	public void setCaInfo(String caInfo) {
		this.caInfo = caInfo;
	}

	public String getCertInfo() {
		return this.certInfo;
	}

	public void setCertInfo(String certInfo) {
		this.certInfo = certInfo;
	}

	public String getCertInfoPasswd() {
		return this.certInfoPasswd;
	}

	public void setCertInfoPasswd(String certInfoPasswd) {
		this.certInfoPasswd = certInfoPasswd;
	}
}

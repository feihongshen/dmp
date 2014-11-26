package cn.explink.pos.alipay;

public class AliPay {

	private String requester;
	private String targeter;
	private String privateKey;
	private String publicKey;
	private String deliver_dept_no;// 员工所属单位编码
	private String deliver_dept; // 员工所属单名称
	private String request_url; // POS机请求的url

	// //////////财付通POS机对外查询接口配置////////////////////////////
	private String partner; // 商户号,由财付通统一分配的10位正整数(120XXXXXXX)号

	public String getPartner() {
		return partner;
	}

	public void setPartner(String partner) {
		this.partner = partner;
	}

	public String getSearchPosUrl() {
		return searchPosUrl;
	}

	public void setSearchPosUrl(String searchPosUrl) {
		this.searchPosUrl = searchPosUrl;
	}

	private String searchPosUrl; // 查询POS机交易结果URL

	public String getRequest_url() {
		return request_url;
	}

	public void setRequest_url(String request_url) {
		this.request_url = request_url;
	}

	private int isotherdeliverupdate = 0; // 他人刷卡是否更新派送员 1(更新) 0（不更新）
	private int isotheroperator = 0; // 是否限制他人刷卡

	public int getIsotheroperator() {
		return isotheroperator;
	}

	public void setIsotheroperator(int isotheroperator) {
		this.isotheroperator = isotheroperator;
	}

	public String getDeliver_dept_no() {
		return deliver_dept_no;
	}

	public int getIsotherdeliverupdate() {
		return isotherdeliverupdate;
	}

	public void setIsotherdeliverupdate(int isotherdeliverupdate) {
		this.isotherdeliverupdate = isotherdeliverupdate;
	}

	public void setDeliver_dept_no(String deliver_dept_no) {
		this.deliver_dept_no = deliver_dept_no;
	}

	public String getDeliver_dept() {
		return deliver_dept;
	}

	public void setDeliver_dept(String deliver_dept) {
		this.deliver_dept = deliver_dept;
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

	public String getPrivateKey() {
		return privateKey;
	}

	public void setPrivateKey(String privateKey) {
		this.privateKey = privateKey;
	}

	public String getPublicKey() {
		return publicKey;
	}

	public void setPublicKey(String publicKey) {
		this.publicKey = publicKey;
	}

}

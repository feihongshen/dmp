package cn.explink.b2c.gztl;

public class Gztl {
	private String customerids; // 在系统中的ids

	private String search_url; // 提供给乐峰查询的URL

	private long search_number;// 要查询的数量

	private String password;// 密码

	private String agentName;// agent名

	private String agentPhone;// agent电话

	private String agentWebsite;// agent网址

	private String agentId;// 代理id

	private String private_key;// 私匙

	private String exportbranchid;// 订单导入库房id

	private String sign;// MD5加密（详细见7.1MD5数字验签）

	private String invokeMethod;// 接口名称（FBI）

	private String code;// 客户编码（由飞远提供）

	public String getExportbranchid() {
		return this.exportbranchid;
	}

	public void setExportbranchid(String exportbranchid) {
		this.exportbranchid = exportbranchid;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getCustomerids() {
		return this.customerids;
	}

	public void setCustomerids(String customerids) {
		this.customerids = customerids;
	}

	public String getSearch_url() {
		return this.search_url;
	}

	public void setSearch_url(String search_url) {
		this.search_url = search_url;
	}

	public long getSearch_number() {
		return this.search_number;
	}

	public void setSearch_number(long search_number) {
		this.search_number = search_number;
	}

	public String getAgentName() {
		return this.agentName;
	}

	public void setAgentName(String agentName) {
		this.agentName = agentName;
	}

	public String getAgentPhone() {
		return this.agentPhone;
	}

	public void setAgentPhone(String agentPhone) {
		this.agentPhone = agentPhone;
	}

	public String getAgentWebsite() {
		return this.agentWebsite;
	}

	public void setAgentWebsite(String agentWebsite) {
		this.agentWebsite = agentWebsite;
	}

	public String getAgentId() {
		return this.agentId;
	}

	public void setAgentId(String agentId) {
		this.agentId = agentId;
	}

	public String getPrivate_key() {
		return this.private_key;
	}

	public void setPrivate_key(String private_key) {
		this.private_key = private_key;
	}

	public String getSign() {
		return this.sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public String getInvokeMethod() {
		return this.invokeMethod;
	}

	public void setInvokeMethod(String invokeMethod) {
		this.invokeMethod = invokeMethod;
	}

	public String getCode() {
		return this.code;
	}

	public void setCode(String code) {
		this.code = code;
	}

}

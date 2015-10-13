package cn.explink.b2c.gome;

/**
 * 国美对接设置entity
 * 
 * @author Administrator
 *
 */
public class Gome {
	private String tnt_url; // 获取订单和推送 的url
	private String full_url; // 获取订单详情 的url

	private String username; // 用户名
	private String password; // 密码

	private String customerid; // 在系统中的customerid,只能唯一一个
	private int maxCount; // 每次推送最大数量
	private int checkCount; // 获取订单数量

	private long warehouseid; // 订单入库库房

	private String businessCode;// 业务代码
	private String lspCode;// 物流公司编码
	private String lspAbbr;// 物流公司代码
	private String buid;// 渠道代码
	private String lspName;// 物流公司名称
	private String lspPhoneNumber;// 物流公司电话

	public String getTnt_url() {
		return tnt_url;
	}

	public void setTnt_url(String tnt_url) {
		this.tnt_url = tnt_url;
	}

	public String getFull_url() {
		return full_url;
	}

	public void setFull_url(String full_url) {
		this.full_url = full_url;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getCustomerid() {
		return customerid;
	}

	public void setCustomerid(String customerid) {
		this.customerid = customerid;
	}

	public int getMaxCount() {
		return maxCount;
	}

	public void setMaxCount(int maxCount) {
		this.maxCount = maxCount;
	}

	public int getCheckCount() {
		return checkCount;
	}

	public void setCheckCount(int checkCount) {
		this.checkCount = checkCount;
	}

	public long getWarehouseid() {
		return warehouseid;
	}

	public void setWarehouseid(long warehouseid) {
		this.warehouseid = warehouseid;
	}

	public String getBusinessCode() {
		return businessCode;
	}

	public void setBusinessCode(String businessCode) {
		this.businessCode = businessCode;
	}

	public String getLspCode() {
		return lspCode;
	}

	public void setLspCode(String lspCode) {
		this.lspCode = lspCode;
	}

	public String getLspAbbr() {
		return lspAbbr;
	}

	public void setLspAbbr(String lspAbbr) {
		this.lspAbbr = lspAbbr;
	}

	public String getBuid() {
		return buid;
	}

	public void setBuid(String buid) {
		this.buid = buid;
	}

	public String getLspName() {
		return lspName;
	}

	public void setLspName(String lspName) {
		this.lspName = lspName;
	}

	public String getLspPhoneNumber() {
		return lspPhoneNumber;
	}

	public void setLspPhoneNumber(String lspPhoneNumber) {
		this.lspPhoneNumber = lspPhoneNumber;
	}

}

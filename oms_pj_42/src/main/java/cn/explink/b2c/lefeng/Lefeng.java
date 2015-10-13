package cn.explink.b2c.lefeng;

/**
 * 乐蜂网接口 属性 设置
 *
 * @author Administrator
 *
 */
public class Lefeng {

	private String customerids; // 在系统中的ids

	private String search_url; // 提供给乐峰查询的URL

	private long search_number;// 要查询的数量

	private String password;// 密码

	private String agentName;

	private String agentPhone;

	private String agentWebsite;

	private String agentId;// 代理id

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

}

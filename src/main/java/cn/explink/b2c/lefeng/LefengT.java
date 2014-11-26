package cn.explink.b2c.lefeng;

/**
 * 乐蜂网接口 属性 设置
 * 
 * @author Administrator
 *
 */
public class LefengT {

	private String customerids; // 在系统中的ids
	private String code; // 乐蜂网给承运商提供的唯一标示
	private String appkey; // 密钥信息
	private String search_url; // 提供给乐峰查询的URL
	private int issignflag; // 是否开启签名验证 0不验证，1验证
	private String companyname; // 快递公司名称
	private String companyphone; // 快递公司电话
	private String website; // 网站地址

	public String getCompanyname() {
		return companyname;
	}

	public void setCompanyname(String companyname) {
		this.companyname = companyname;
	}

	public String getCompanyphone() {
		return companyphone;
	}

	public void setCompanyphone(String companyphone) {
		this.companyphone = companyphone;
	}

	public String getWebsite() {
		return website;
	}

	public void setWebsite(String website) {
		this.website = website;
	}

	public void setIssignflag(int issignflag) {
		this.issignflag = issignflag;
	}

	public int getIssignflag() {
		return issignflag;
	}

	public String getCustomerids() {
		return customerids;
	}

	public void setCustomerids(String customerids) {
		this.customerids = customerids;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getAppkey() {
		return appkey;
	}

	public void setAppkey(String appkey) {
		this.appkey = appkey;
	}

	public String getSearch_url() {
		return search_url;
	}

	public void setSearch_url(String search_url) {
		this.search_url = search_url;
	}

}

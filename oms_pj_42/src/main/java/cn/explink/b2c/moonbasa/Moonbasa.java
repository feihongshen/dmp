package cn.explink.b2c.moonbasa;

/**
 * 
 * @author Administrator 梦芭莎查询接口设置
 */
public class Moonbasa {

	private String custcode; // 快递公司的唯一标识
	private String pwd; // 查询密码
	private String customerid; // 在系统中的customerid
	private String search_url; // 提供查询url

	public String getSearch_url() {
		return search_url;
	}

	public void setSearch_url(String search_url) {
		this.search_url = search_url;
	}

	public String getCustomerid() {
		return customerid;
	}

	public void setCustomerid(String customerid) {
		this.customerid = customerid;
	}

	public String getCustcode() {
		return custcode;
	}

	public void setCustcode(String custcode) {
		this.custcode = custcode;
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

}

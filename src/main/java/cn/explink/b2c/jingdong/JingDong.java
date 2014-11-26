package cn.explink.b2c.jingdong;

/**
 * 京东对接 属性 设置
 * 
 * @author Administrator
 *
 */
public class JingDong {

	private String customerids; // 在系统中的ids
	private String search_url; // 提供给京东查询的URL

	public String getCustomerids() {
		return customerids;
	}

	public void setCustomerids(String customerids) {
		this.customerids = customerids;
	}

	public String getSearch_url() {
		return search_url;
	}

	public void setSearch_url(String search_url) {
		this.search_url = search_url;
	}

	public int getMaxcount() {
		return maxcount;
	}

	public void setMaxcount(int maxcount) {
		this.maxcount = maxcount;
	}

	private int maxcount;

}

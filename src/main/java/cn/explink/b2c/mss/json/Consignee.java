package cn.explink.b2c.mss.json;

/**
 * 收货信息
 *
 * @author zhaoshb
 * @since AR1.0
 */
public class Consignee {
	private String name;// 收货人姓名
	private String mobile;// 收货人手机号
	private String tel;// 收货人电话
	private String address;// 收货地址

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMobile() {
		return this.mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getTel() {
		return this.tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public String getAddress() {
		return this.address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

}

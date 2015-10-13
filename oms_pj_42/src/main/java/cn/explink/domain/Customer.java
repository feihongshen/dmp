package cn.explink.domain;

public class Customer {
	private long customerid;
	private String customername;
	private String customeraddress;
	private String customercontactman;
	private String customerphone;
	private long ifeffectflag;

	private String customercode; // 新增供货商编码（支付宝用到）

	private String b2cEnum;// 对应的枚举id

	public String getB2cEnum() {
		return b2cEnum;
	}

	public void setB2cEnum(String b2cEnum) {
		this.b2cEnum = b2cEnum;
	}

	public String getCustomercode() {
		return customercode;
	}

	public void setCustomercode(String customercode) {
		this.customercode = customercode;
	}

	public long getIfeffectflag() {
		return ifeffectflag;
	}

	public void setIfeffectflag(long ifeffectflag) {
		this.ifeffectflag = ifeffectflag;
	}

	public long getCustomerid() {
		return customerid;
	}

	public void setCustomerid(long customerid) {
		this.customerid = customerid;
	}

	public String getCustomername() {
		return customername;
	}

	public void setCustomername(String customername) {
		this.customername = customername;
	}

	public String getCustomeraddress() {
		return customeraddress;
	}

	public void setCustomeraddress(String customeraddress) {
		this.customeraddress = customeraddress;
	}

	public String getCustomercontactman() {
		return customercontactman;
	}

	public void setCustomercontactman(String customercontactman) {
		this.customercontactman = customercontactman;
	}

	public String getCustomerphone() {
		return customerphone;
	}

	public void setCustomerphone(String customerphone) {
		this.customerphone = customerphone;
	}

}

package cn.explink.domain;

public class CustomWarHouse {
	private long warhouseid;
	private long customerid;
	private String customerwarehouse;
	private String warehouseremark;
	private boolean ifeffectflag;

	public long getWarhouseid() {
		return warhouseid;
	}

	public void setWarhouseid(long warhouseid) {
		this.warhouseid = warhouseid;
	}

	public long getCustomerid() {
		return customerid;
	}

	public void setCustomerid(long customerid) {
		this.customerid = customerid;
	}

	public String getCustomerwarehouse() {
		return customerwarehouse;
	}

	public void setCustomerwarehouse(String customerwarehouse) {
		this.customerwarehouse = customerwarehouse;
	}

	public String getWarehouseremark() {
		return warehouseremark;
	}

	public void setWarehouseremark(String warehouseremark) {
		this.warehouseremark = warehouseremark;
	}

	public boolean isIfeffectflag() {
		return ifeffectflag;
	}

	public void setIfeffectflag(boolean ifeffectflag) {
		this.ifeffectflag = ifeffectflag;
	}

}

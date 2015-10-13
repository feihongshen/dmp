package cn.explink.domain;

public class AccountArea {
	private long areaid;
	private String areaname;
	private String arearemark;
	private long customerid;
	private boolean isEffectFlag;

	public long getAreaid() {
		return areaid;
	}

	public void setAreaid(long areaid) {
		this.areaid = areaid;
	}

	public String getAreaname() {
		return areaname;
	}

	public void setAreaname(String areaname) {
		this.areaname = areaname;
	}

	public String getArearemark() {
		return arearemark;
	}

	public void setArearemark(String arearemark) {
		this.arearemark = arearemark;
	}

	public long getCustomerid() {
		return customerid;
	}

	public void setCustomerid(long customerid) {
		this.customerid = customerid;
	}

	public boolean isEffectFlag() {
		return isEffectFlag;
	}

	public void setEffectFlag(boolean isEffectFlag) {
		this.isEffectFlag = isEffectFlag;
	}
}

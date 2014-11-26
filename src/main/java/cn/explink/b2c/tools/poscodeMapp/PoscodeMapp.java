package cn.explink.b2c.tools.poscodeMapp;

public class PoscodeMapp {
	private int poscodeid;
	private int posenum;
	private int customerid;
	private String customercode;

	public int getPoscodeid() {
		return poscodeid;
	}

	public void setPoscodeid(int poscodeid) {
		this.poscodeid = poscodeid;
	}

	public int getPosenum() {
		return posenum;
	}

	public void setPosenum(int posenum) {
		this.posenum = posenum;
	}

	public int getCustomerid() {
		return customerid;
	}

	public void setCustomerid(int customerid) {
		this.customerid = customerid;
	}

	public String getCustomercode() {
		return customercode;
	}

	public void setCustomercode(String customercode) {
		this.customercode = customercode;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	private String remark;

}

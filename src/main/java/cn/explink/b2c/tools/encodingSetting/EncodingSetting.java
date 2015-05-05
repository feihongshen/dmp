package cn.explink.b2c.tools.encodingSetting;

public class EncodingSetting {
	private int poscodeid; // 序号
	private int posenum;
	private int customerid; // 供货商ID
	private String customercode; // 供货商编码
	private String remark; // 备注
	
	
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

	
	

}

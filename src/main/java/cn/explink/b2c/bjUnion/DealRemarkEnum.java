package cn.explink.b2c.bjUnion;
//交易处理码
public enum DealRemarkEnum {
	login("MI0001"),//员工登陆
	sign("MI0006"),//派件签收登记
	query("MI0010"),//运单查询
	revoke("MI0007");//签收撤销
	private String remark;
	
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	private DealRemarkEnum(String remark){
		this.remark = remark;
	}
}

package cn.explink.domain;

public class CustomWareHouse {
	private long warehouseid;
	private long customerid;
	private String customerwarehouse;
	private String warehouseremark;
	private int ifeffectflag;
	private String warehouse_no; // 仓库编码

	public String getWarehouse_no() {
		return warehouse_no;
	}

	public void setWarehouse_no(String warehouse_no) {
		this.warehouse_no = warehouse_no;
	}

	public long getWarehouseid() {
		return warehouseid;
	}

	public void setWarehouseid(long warehouseid) {
		this.warehouseid = warehouseid;
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

	public int getIfeffectflag() {
		return ifeffectflag;
	}

	public void setIfeffectflag(int ifeffectflag) {
		this.ifeffectflag = ifeffectflag;
	}

}

package cn.explink.b2c.auto.order.service;

public class TpsBatchNoHeader {
	 private String custOrderNo;//客户订单号
	 private String transportNo;//运单号
	 private String warehouse;//仓库
	 private int dataType;//数据类型,101:更新箱托运单 102:更新箱批次号
	 private String dataBody;//数据报文
	 
	public String getCustOrderNo() {
		return custOrderNo;
	}
	public void setCustOrderNo(String custOrderNo) {
		this.custOrderNo = custOrderNo;
	}
	public String getTransportNo() {
		return transportNo;
	}
	public void setTransportNo(String transportNo) {
		this.transportNo = transportNo;
	}
	public String getWarehouse() {
		return warehouse;
	}
	public void setWarehouse(String warehouse) {
		this.warehouse = warehouse;
	}
	public int getDataType() {
		return dataType;
	}
	public void setDataType(int dataType) {
		this.dataType = dataType;
	}
	public String getDataBody() {
		return dataBody;
	}
	public void setDataBody(String dataBody) {
		this.dataBody = dataBody;
	}
	 
	 
}

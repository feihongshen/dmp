package cn.explink.b2c.auto.order.service;

public class TpsBatchNoDoBoxInfo {
	private String boxNo;//箱号
	private String transportNo;//运单号
	private String tmsBatchNo;//批次号 (即交接单号)
	private String tmsAttemperNo;//托运单
    private String tmsAttemperTime;// 托运单生成时间
	public String getBoxNo() {
		return boxNo;
	}
	public void setBoxNo(String boxNo) {
		this.boxNo = boxNo;
	}
	public String getTransportNo() {
		return transportNo;
	}
	public void setTransportNo(String transportNo) {
		this.transportNo = transportNo;
	}
	public String getTmsBatchNo() {
		return tmsBatchNo;
	}
	public void setTmsBatchNo(String tmsBatchNo) {
		this.tmsBatchNo = tmsBatchNo;
	}
	public String getTmsAttemperNo() {
		return tmsAttemperNo;
	}
	public void setTmsAttemperNo(String tmsAttemperNo) {
		this.tmsAttemperNo = tmsAttemperNo;
	}
	public String getTmsAttemperTime() {
		return tmsAttemperTime;
	}
	public void setTmsAttemperTime(String tmsAttemperTime) {
		this.tmsAttemperTime = tmsAttemperTime;
	}
    
    
}

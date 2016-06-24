package cn.explink.b2c.ems;

/**
 * EMS订单下发异常返回对象
 * @author huan.zhou
 */
public class ErrorDetail {
	private String dataID;
	private String dataError;
	private String dErrorCode;
	public String getDataID() {
		return dataID;
	}
	public void setDataID(String dataID) {
		this.dataID = dataID;
	}
	public String getDataError() {
		return dataError;
	}
	public void setDataError(String dataError) {
		this.dataError = dataError;
	}
	public String getdErrorCode() {
		return dErrorCode;
	}
	public void setdErrorCode(String dErrorCode) {
		this.dErrorCode = dErrorCode;
	}
	
}

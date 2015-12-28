package cn.explink.b2c.feiniuwang;


public class FeiNiuRequest {
	private CwbRequest logistics_interface;
	private String data_digest;
	private String logistic_proider_id;
	private String msg_type;
	public String getData_digest() {
		return data_digest;
	}
	public void setData_digest(String data_digest) {
		this.data_digest = data_digest;
	}
	public CwbRequest getLogistics_interface() {
		return logistics_interface;
	}
	public void setLogistics_interface(CwbRequest logistics_interface) {
		this.logistics_interface = logistics_interface;
	}
	
	
	public String getLogistic_proider_id() {
		return logistic_proider_id;
	}
	public void setLogistic_proider_id(String logistic_proider_id) {
		this.logistic_proider_id = logistic_proider_id;
	}
	public String getMsg_type() {
		return msg_type;
	}
	public void setMsg_type(String msg_type) {
		this.msg_type = msg_type;
	}
	
}

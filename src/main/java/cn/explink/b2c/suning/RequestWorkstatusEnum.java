package cn.explink.b2c.suning;

public enum RequestWorkstatusEnum {
	zhichang("T"),
	shixiao("F");
	private String msg;

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
	
	private RequestWorkstatusEnum(String msg){
		this.msg = msg;
	}
}

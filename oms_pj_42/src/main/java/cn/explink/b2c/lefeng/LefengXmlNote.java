package cn.explink.b2c.lefeng;

public class LefengXmlNote {
	/*
	 * <routes> <route code="N10"> <time>2013-07-01T22:50:35.000+08:00</time>
	 * <state>已进行柜台到件扫描,扫描员是CC</state> </route>
	 */
	private String code;
	private String time;
	private String state;
	private String transcwb;
	private String cwb;
	private String userName;
	private String phone;

	public String getTranscwb() {
		return this.transcwb;
	}

	public void setTranscwb(String transcwb) {
		this.transcwb = transcwb;
	}

	public String getCwb() {
		return this.cwb;
	}

	public void setCwb(String cwb) {
		this.cwb = cwb;
	}

	public String getCode() {
		return this.code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getTime() {
		return this.time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getState() {
		return this.state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getUserName() {
		return this.userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPhone() {
		return this.phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

}

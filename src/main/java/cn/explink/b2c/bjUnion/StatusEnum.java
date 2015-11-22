package cn.explink.b2c.bjUnion;

public enum StatusEnum {
	zaitu("01","途中"),
	peisonging("02","派件员手中"),
	sign("03","已签收"),
	question("04","问题件"),
	returns("05","退回"),
	inbranch("06","在站点"),
	zhiliu("07","滞留");
	
	private String intstr;
	private String msg;
	public String getIntstr() {
		return intstr;
	}

	public void setIntstr(String intstr) {
		this.intstr = intstr;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	private StatusEnum(String intstr,String msg){
		this.intstr = intstr;
		this.msg = msg;
	}
}

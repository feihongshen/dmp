package cn.explink.pos.unionpay;

public enum UnionPayEnum {

	Login("0100", "登录"), LoginOut("0101", "登出"), Search("0102", "运单查询"), Delivery("0103", "运单反馈"), SearchPos("01001", "查询POS");

	private String command;
	private String decribe;

	private UnionPayEnum(String key, String text) {
		this.command = key;
		this.decribe = text;
	}

	public String getCommand() {
		return command;
	}

	public void setCommand(String command) {
		this.command = command;
	}

	public String getDecribe() {
		return decribe;
	}

	public void setDecribe(String decribe) {
		this.decribe = decribe;
	}
}

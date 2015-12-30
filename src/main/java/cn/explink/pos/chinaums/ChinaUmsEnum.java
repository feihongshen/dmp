package cn.explink.pos.chinaums;

public enum ChinaUmsEnum {

	Login("P001", "登录"), LoginOut("P101", "登出"), Delivery("P003", "运单反馈"), Search("P004", "运单查询"), OrderRegistration("P005", "问题件登记"), DeliveryCancel("P006", "签收撤销");

	private String command;
	private String decribe;

	private ChinaUmsEnum(String key, String text) {
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

package cn.explink.enumutil;

public enum DeliverServerResultEnum {

	SUCCESS(Integer.valueOf(1),"派送成功"),
	REFUSE(Integer.valueOf(2),"收件人拒收"),
	REVERSE(Integer.valueOf(6),"恢复归班前状态"),
	HOLDUP(Integer.valueOf(7),"滞留");
	
	private Integer index;
	private String value;

	private DeliverServerResultEnum(Integer index, String value) {
		this.index = index;
		this.value = value;
	}

	public Integer getIndex() {
		return index;
	}

	public String getValue() {
		return value;
	}

}

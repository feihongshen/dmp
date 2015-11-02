package cn.explink.enumutil;

public enum DeliverServerPayEnum {

	CASH(Integer.valueOf(1),"现金"),
	POS(Integer.valueOf(6),"pos机"),
	ALIPAY(Integer.valueOf(7),"支付宝"),
	PAID(Integer.valueOf(8),"已付款");
	
	private Integer index;
	private String value;

	private DeliverServerPayEnum(Integer index, String value) {
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

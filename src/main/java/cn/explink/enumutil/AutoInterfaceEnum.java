package cn.explink.enumutil;

public enum AutoInterfaceEnum {
	dingdanxiafa(1, "订单下发"), fenjianzhuangtai(2, "分拣状态"),shangmenlantui(3, "上门揽退"),
	fankui_dingdan(4, "反馈订单异常到TPS"),fankui_fanjian(5, "反馈分拣异常到TPS"),
	insertTransportNo(6,"上门退成功录入运单号"),
	tpsbatchno(7,"TPS交接单号");
	
	private int value;
	private String text;

	private AutoInterfaceEnum(int value, String text) {
		this.value = value;
		this.text = text;
	}

	public int getValue() {
		return this.value;
	}

	public String getText() {
		return this.text;
	}
}

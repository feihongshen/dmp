package cn.explink.b2c.rufengda;

import cn.explink.enumutil.CwbOrderTypeIdEnum;

public enum RufengdaOrderTypeEnum {

	PuTongDingDan(0, CwbOrderTypeIdEnum.Peisong.getValue(), "普通订单"), Shangmenhuan(1, CwbOrderTypeIdEnum.Shangmenhuan.getValue(), "上门换货单"), Shangmentui(2, CwbOrderTypeIdEnum.Shangmentui.getValue(),
			"上门退货单"),
	// QianDanFanHui(3,CwbOrderTypeIdEnum.Peisong.getValue(),"签单返回业务")
	// //暂无此业务，如风达暂时没有上线
	;

	private int value;
	private int onwer_value;
	private String text;

	private RufengdaOrderTypeEnum(int value, int onwer_value, String text) {
		this.value = value;
		this.onwer_value = onwer_value;
		this.text = text;
	}

	public int getOnwer_value() {
		return onwer_value;
	}

	public void setOnwer_value(int onwer_value) {
		this.onwer_value = onwer_value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public void setText(String text) {
		this.text = text;
	}

	public int getValue() {
		return value;
	}

	public String getText() {
		return text;
	}

}

package cn.explink.enumutil.pos;

public enum PosEnum {
	AliPay(1001, "支付宝网络技术有限公司", "AliPay"), // 支付宝
	YeePay(1002, " 北京通融通信息技术有限公司(易宝支付)", "YeePay"), // 易宝支付
	// UnionPay(1003,"银联","UnionPay"),//银联
	Bill99(1004, "快钱支付清算信息有限公司", "Bill99"), // 快钱
	// HNAPay(1005,"海航新生支付","HNAPay"), //海航新生支付
	// EasyPay(1006,"海航易生支付","EasyPay"); //海航易生支付
	UnionPay(1005, "银联商务", "UnionPay"), ChinaUms(1006, "北京银联商务", "ChinaUms"), XitongFanKui(2001, "系统反馈", "系统反馈"), AliPayCodApp(1007, "支付宝CODAPP", "AliPayCodApp"), MobileEtong(1008, "物流E通（手机App）",
			"MobileEtong"),
	//
	YalianApp(1009, "亚联APP", "YalianApp"), MobileApp_dcb(1010, "新疆大晨报App", "MobileApp_dcb"), Weisuda(1013, "唯速达", "Weisuda"), ;

	private int key;
	private String text;
	private String method;

	private PosEnum(int key, String text, String method) {
		this.key = key;
		this.text = text;
		this.method = method;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public int getKey() {
		return key;
	}

	public void setKey(int key) {
		this.key = key;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

}

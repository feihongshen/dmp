package cn.explink.enumutil;

public enum AbnormalOrderHandleEnum {
	weichuli(1, "未处理"), kefuchuli(2, "客服处理"),chuangjianfangchuli(3,"创建方处理"),zerenfangchuli(4,"责任方处理"), jieanchuli(5, "结案处理"),yichuli(6,"已处理"),daichuli(7,"待处理"),xiugai(8,"修改");

	private int value;
	private String text;

	private AbnormalOrderHandleEnum(int value, String text) {
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

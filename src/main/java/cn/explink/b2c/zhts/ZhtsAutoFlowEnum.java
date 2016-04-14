package cn.explink.b2c.zhts;


public enum ZhtsAutoFlowEnum {
	RuKu(1, "自动入库"),
	ChuKu(4, "自动出库"), 
	ChengYunShangChuKu(3, "自动承运商出库确认");	
	private long value;
	private String text;

	

	private ZhtsAutoFlowEnum(int value, String text) {
		this.value = value;
		this.text = text;
	}

	public long getValue() {
		return this.value;
	}

	public String getText() {
		return this.text;
	}

	
}

package cn.explink.enumutil;

public enum DownLoadTypeEnum {
	Paiduizhong(-1, "排队中"), ZhengzaiDaochu(0, "导出中"), Wancheng(2, "完成"), YiXaizai(3, "已下载");

	private int value;
	private String text;

	private DownLoadTypeEnum(int value, String text) {
		this.value = value;
		this.text = text;
	}

	public int getValue() {
		return value;
	}

	public String getText() {
		return text;
	}

	public static ModelEnum getByValue(int value) {
		for (ModelEnum modelEnum : ModelEnum.values()) {
			if (value == modelEnum.getValue()) {
				return modelEnum;
			}
		}
		return null;
	}
}

package cn.explink.enumutil;

public enum BranchRouteEnum {
	// 1为双向，2为仅正向，3为仅逆向
	/* ShuangXiang(1,"双向"), */
	JinZhengXiang(2, "正向"), JinDaoXiang(3, "逆向");

	private int value;
	private String text;

	private BranchRouteEnum(int value, String text) {
		this.value = value;
		this.text = text;
	}

	public int getValue() {
		return value;
	}

	public String getText() {
		return text;
	}
}

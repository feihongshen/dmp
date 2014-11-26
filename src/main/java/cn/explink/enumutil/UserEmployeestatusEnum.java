package cn.explink.enumutil;

/**
 * 员工工作状态
 * 
 * @author Administrator
 *
 */
public enum UserEmployeestatusEnum {
	GongZuo(1, "工作"), XiuJia(2, "休假"), LiZhi(3, "离职");
	private int value;
	private String text;

	private UserEmployeestatusEnum(int value, String text) {
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

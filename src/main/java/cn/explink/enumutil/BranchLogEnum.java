package cn.explink.enumutil;

/**
 * 站点 日志统计类型枚举
 * 
 * @author Administrator
 *
 */
public enum BranchLogEnum {
	ZuoRiLinghuoWeiFankui(1, "昨日领货未反馈"), ZuoRiKuCun(2, "昨日库存");

	private long value;
	private String text;

	private BranchLogEnum(long value, String text) {
		this.value = value;
		this.text = text;
	}

	public long getValue() {
		return value;
	}

	public String getText() {
		return text;
	}
}

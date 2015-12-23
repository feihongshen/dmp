package cn.explink.enumutil.express;

/**
 * 快递揽件反馈统计的枚举
 * 
 * @author jiangyu 2015年7月30日
 *
 */
public enum ExpressFeedBackTongjiEnum {
	
	Picked(1, "已揽收"), 
	FeedBacked(2, "已反馈"), 
	TodayNotFeedBack(3, "今日未反馈"), 
	YestodayNotFeedBack(4, "昨日之前未反馈");
	
	private long value;
	private String text;

	private ExpressFeedBackTongjiEnum(long value, String text) {
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

package cn.explink.enumutil.express;
/**
 * 入站时处理状态的枚举
 * @author jiangyu 2015年8月10日
 *
 */
public enum ExpressProcessStateEnum {
	
	UnProcess(1,"未处理"),
	Processed(2,"已处理");
	
	private Integer value;
	
	private String text;

	private ExpressProcessStateEnum(Integer value, String text) {
		this.value = value;
		this.text = text;
	}

	
	public Integer getValue() {
		return value;
	}

	public void setValue(Integer value) {
		this.value = value;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
	
	
}

package cn.explink.util.express;

/**
 * 校验规则的枚举
 * 
 * @author jiangyu 2015年6月4日
 *
 */
public enum ValidatorTypeEnum {

	REGEX(Integer.valueOf(1), "正则表达式"),

	LOGICAL(Integer.valueOf(2), "业务逻辑"),

	MIN(Integer.valueOf(3), "最小值"),

	MAX(Integer.valueOf(4), "最大值"),

	PAGE(Integer.valueOf(5), "页面"),

	NOTNULL(Integer.valueOf(6), "非空");

	private Integer index;

	private String name;

	ValidatorTypeEnum(Integer index, String name) {
		this.index = index;
		this.name = name;
	}

	public Integer getIndex() {
		return index;
	}

	public void setIndex(Integer index) {
		this.index = index;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
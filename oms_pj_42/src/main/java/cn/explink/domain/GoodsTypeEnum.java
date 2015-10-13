package cn.explink.domain;

public enum GoodsTypeEnum {
	PJ("普件"), DJ("大件"), GP("贵品"), DJGP("大件贵品");

	String name;

	GoodsTypeEnum(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

}

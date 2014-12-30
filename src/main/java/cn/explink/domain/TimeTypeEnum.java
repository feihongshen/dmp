package cn.explink.domain;

public enum TimeTypeEnum {

	OrderCreateTime("订单生成时间", "-"), SystemAcceptTime("系统接收时间", "-"), StationAcceptTime("站点接收时间", "-"), ReportOutAreaTime("上报超区时间", ""), DispatchTime("订单分派时间", "");

	TimeTypeEnum(String name, String field) {
		this.name = name;
		this.field = field;
	}

	String name;

	String field;

	public String getName() {
		return this.name;
	}

	public String getField() {
		return this.field;
	}

}

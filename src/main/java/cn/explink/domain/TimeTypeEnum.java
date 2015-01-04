package cn.explink.domain;

public enum TimeTypeEnum {

	OrderCreateTime("订单生成时间", "create_time"), SystemAcceptTime("系统接收时间", "system_accept_time"), StationAcceptTime("站点接收时间", "station_accept_time"), ReportOutAreaTime("上报超区时间", "report_outarea_time"), DispatchTime(
			"订单分派时间", "dispatch_time");

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

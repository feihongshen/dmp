package cn.explink.vo.delivery;

public enum DeliveryRateQueryType {

	byBranch("按站点到货", "到货日期", "当天到货", "每日到货", "branchid", "substationgoodstime", "customerid"), byUser("按小件员领货", "领货日期", "当天领货", "每日领货", "branchid", "receivegoodstime", "customerid"), byWarehouse(
			"按库房入库", "入库日期", "当天入库", "每日入库", "customerid", "intowarehoustime", "branchid"), byOuthouse("按库房出库", "出库日期", "当天出库", "每日出库", "customerid", "outwarehousetime", "branchid"), byVendor(
			"按供货商发货", "发货日期", "当天发货", "每日发货", "customerid", "emaildatetime", "branchid");

	private String desc;

	private String title;

	private String title2;

	private String chartName;

	private String firstField;

	private String secondField;

	private String thirdField;

	private DeliveryRateQueryType(String desc, String title, String title2, String chartName, String firstField, String secondField, String thirdField) {
		this.desc = desc;
		this.title = title;
		this.title2 = title2;
		this.chartName = chartName;
		this.firstField = firstField;
		this.secondField = secondField;
		this.thirdField = thirdField;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getTitle2() {
		return title2;
	}

	public void setTitle2(String title2) {
		this.title2 = title2;
	}

	public String getFirstField() {
		return firstField;
	}

	public void setFirstField(String firstField) {
		this.firstField = firstField;
	}

	public String getSecondField() {
		return secondField;
	}

	public void setSecondField(String secondField) {
		this.secondField = secondField;
	}

	public String getChartName() {
		return chartName;
	}

	public void setChartName(String chartName) {
		this.chartName = chartName;
	}

	public String getThirdField() {
		return thirdField;
	}

	public void setThirdField(String thirdField) {
		this.thirdField = thirdField;
	}

}

package cn.explink.vo.delivery;

public enum CustomizedDeliveryDateType {

	day1(0, "1D(当日)"), day2(1, "2D(次日)"), day3(2, "3D(隔日)"), day4(3, "4D"), day5(4, "5D"), day6(5, "6D"), day7(6, "7D");

	private int day;

	private String desc;

	private CustomizedDeliveryDateType(int day, String desc) {
		this.day = day;
		this.desc = desc;
	}

	public String getDesc() {
		return desc;
	}

	public int getDay() {
		return day;
	}

}

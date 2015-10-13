package cn.explink.domain;

public enum CwbOrderType {
	Peisong(1), Shangmentui(2), Shangmenhuan(3);

	private int value;

	private CwbOrderType(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}
}

package cn.explink.enumutil;

public enum OperationTimeEnum {
	A1(1, "超期未出库"), A2(2, "超期未中转"), A3(3, "超期未领货"), A4(4, "超期未归班"), A5(5, "超期未退供货商"), A6(6, "超期未到货"), A7(7, "超期未到中转"), A8(8, "超期未到退货"), A9(9, "超期滞留"), A10(10, "超期未退货"), A11(11, "超期中转未到站");

	private int value;
	private String text;

	private OperationTimeEnum(int value, String text) {
		this.value = value;
		this.text = text;
	}

	public int getValue() {
		return value;
	}

	public String getText() {
		return text;
	}
}

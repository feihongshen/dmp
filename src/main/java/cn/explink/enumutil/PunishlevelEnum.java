package cn.explink.enumutil;

public enum PunishlevelEnum {
	Defalut(0, "未知");
	private long value;
	private String text;

	private PunishlevelEnum(long value, String text) {
		this.value = value;
		this.text = text;
	}

	public long getValue() {
		return this.value;
	}

	public String getText() {
		return this.text;
	}

	public static PunishlevelEnum getText(long value) {
		for (PunishlevelEnum pEnum : PunishlevelEnum.values()) {
			if (pEnum.getValue() == value) {
				return pEnum;
			}
		}
		return null;
	}

	public static PunishlevelEnum getText(String name) {
		for (PunishlevelEnum pEnum : PunishlevelEnum.values()) {
			if (pEnum.getText().equals(name.trim())) {
				return pEnum;
			}
		}
		return null;
	}
}

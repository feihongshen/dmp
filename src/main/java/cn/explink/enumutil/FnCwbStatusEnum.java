package cn.explink.enumutil;

public enum FnCwbStatusEnum {
	Received(1, "已收款"),
	Unreceive(0, "未收款"),
	SomeReceived(2, "部分收款");

	private int index;
	private String text;

	private FnCwbStatusEnum(int index, String text) {
		this.index = index;
		this.text = text;
	}

	public int getIndex() {
		return index;
	}

	public String getText() {
		return text;
	}

	public static FnCwbStatusEnum getByValue(int cwbordertypeid) {
		for (FnCwbStatusEnum cotie : FnCwbStatusEnum.values()) {
			if (cotie.getIndex() == cwbordertypeid)
				return cotie;
		}
		return null;
	}
	
	//根据index获得字符串
	public static String getTextByValue(Integer index) {
		for (FnCwbStatusEnum cotie : FnCwbStatusEnum.values()) {
			if (cotie.getIndex() == index)
				return cotie.getText();
		}
		return null;
	}
}

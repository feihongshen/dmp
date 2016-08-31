package cn.explink.enumutil;


public enum ApplyEditCartypeReviewStatusEnum {
	/**未处理*/
	unsolve(0, "未处理"), 
	/**审核通过*/
	pass   (1, "审核通过"),
	/**审核不通过*/
	denied (2, "审核不通过");

	private int value;
	private String text;

	private ApplyEditCartypeReviewStatusEnum(int value, String text) {
		this.value = value;
		this.text = text;
	}

	public int getValue() {
		return value;
	}

	public String getText() {
		return text;
	}
	
	public static ApplyEditCartypeReviewStatusEnum getByValue(int value) {
		for (ApplyEditCartypeReviewStatusEnum applyEditCartypeReviewStatusEnum : ApplyEditCartypeReviewStatusEnum.values()) {
			if (value == applyEditCartypeReviewStatusEnum.getValue()) {
				return applyEditCartypeReviewStatusEnum;
			}
		}
		return ApplyEditCartypeReviewStatusEnum.unsolve;
	}

}

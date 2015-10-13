package cn.explink.vo.delivery;

public enum DeliveryRateComputeType {

	bySubmitTime("按反馈时间", "couplebacktime"), byApproveTime("按审核时间", "checktime");

	private String desc;

	private String field;

	private DeliveryRateComputeType(String desc, String field) {
		this.desc = desc;
		this.field = field;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}

}

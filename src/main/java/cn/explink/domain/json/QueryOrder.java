package cn.explink.domain.json;

public class QueryOrder {
	private long floworderType;// 操作环节
	private String detail;// 描述
	private String datetime;//
	private String stateCode;

	public long getFloworderType() {
		return floworderType;
	}

	public void setFloworderType(long floworderType) {
		this.floworderType = floworderType;
	}

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}

	public String getDatetime() {
		return datetime;
	}

	public void setDatetime(String datetime) {
		this.datetime = datetime;
	}

	public String getStateCode() {
		return stateCode;
	}

	public void setStateCode(String stateCode) {
		this.stateCode = stateCode;
	}

}

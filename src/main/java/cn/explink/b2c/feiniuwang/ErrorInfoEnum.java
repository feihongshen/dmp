package cn.explink.b2c.feiniuwang;

public enum ErrorInfoEnum {
	
	geshi("S01","非法的 JSON 格式"),
	qianming("S02","非法的数字签名"),
	gongsi("S03","非法的物流公司"),
	leixing("S04","非法的通知类型"),
	neirong("S05","非法的通知内容"),
	chaoshi("S06","连接超时"),
	xitongyichang("S07","系统异常"),
	dianshangpingtai("S08","非法的电商平台"),
	wushuju("S09","没有任务数据反回");
	
	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getReasonInfo() {
		return reasonInfo;
	}

	public void setReasonInfo(String reasonInfo) {
		this.reasonInfo = reasonInfo;
	}

	private String reason;
	private String reasonInfo;
	
	private ErrorInfoEnum(String reason,String reasonInfo){
		this.reason = reason;
		this.reasonInfo = reasonInfo;
	}
}

package cn.explink.b2c.suning;

public enum FailedReasonEnum {
	signfail("签名验证失败"),
	spcodefail("苏宁合作商错误");
	
	private String failmsg;

	public String getFailmsg() {
		return failmsg;
	}

	public void setFailmsg(String failmsg) {
		this.failmsg = failmsg;
	}
	
	private FailedReasonEnum(String failmsg){
		this.failmsg = failmsg;
	}
	
	
	
}

package cn.explink.enumutil;

public enum UploadResponseCodeEnum {
	
	Chenggong("SUCCESS","成功"),OrderError("EC0101","订单信息错误或不完整"),weizhihzs("EC0012","未知或无合作关系的承运商"),NoExist("EC0103","订单不存在"),Noallow("EC0104","当前订单状态不允许做该操作"),Exception("EC0108","处理异常");
	
	private String Code;
	private String Meaning;
	public String getCode() {
		return Code;
	}
	public void setCode(String code) {
		Code = code;
	}
	public String getMeaning() {
		return Meaning;
	}
	public void setMeaning(String meaning) {
		Meaning = meaning;
	}
	private UploadResponseCodeEnum(String code, String meaning) {
		Code = code;
		Meaning = meaning;
	}
	
	

}

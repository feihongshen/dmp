package cn.explink.pos.tonglianpos.delivery;

public enum DeliveryEnum {
	
	Success("00","成功"),
	DingDanNull("01","订单不存在"),
	ChongFu("02","订单重复"),
	JuLing("03","状态为{0}的订单不能进行领货操作"),
	XieYiCuoWu("04","协议信息错误"),
	SignError("05","签名验证失败"),
	SystemError("06","未知异常"),
	
	;
	private String result_code;
	private String result_msg;
	public String getResult_code() {
		return result_code;
	}
	public void setResult_code(String result_code) {
		this.result_code = result_code;
	}
	public String getResult_msg() {
		return result_msg;
	}
	public void setResult_msg(String result_msg) {
		this.result_msg = result_msg;
	}
	
	private DeliveryEnum(String resp_code,String resp_msg){
		this.result_code=resp_code;
		this.result_msg=resp_msg;
		
	}
}

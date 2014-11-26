package cn.explink.b2c.jumei;

/**
 * 聚美优品的返回的异常信息码
 * 
 * @author Administrator
 *
 */
public enum JuMeiExptMessageEnum {

	// 系统错误原因
	IllegalXMLFormat("S1", "非法的XML格式"), IllegalSign("S2", "非法的数字签名"), IllegalCompany("S3", "非法的快递公司"), IllegalBiaoJiMing("S4", "非法的标记名"), IllegalContentFormat("S5", "通知内容格式非法"), ServerDealError("S6",
			"服务器处理错误"), OutOfRangeStringException("S7", "字符串超出指定长度"),
	// 业务错误信息
	R1("R1", "滞留或者拒收状态， 如果提交数据不是两列或者三列"), R2("R2", "其他状态，如果数据不是一列"), R3("R3", "滞留或者拒收状态的原因编码会做检查，如果无效提示此"), R4("R4", "输入不是以逗号分割"), R5("R5", "快递单号不存在"), R6("R6", "状态是<滞留>,<拒收>, 在客服回复之前快递公司再次提交异常状态"), R7(
			"R7", "原因类型非法"), R8("R8", "现有状态是”拒收”，客服答复可以退货，快递公司再次提交拒收"), R9("R9", "快递公司提交的快递单号属于另外一家快递公司"),

	;

	public String getResp_code() {
		return resp_code;
	}

	public void setResp_code(String resp_code) {
		this.resp_code = resp_code;
	}

	public String getResp_msg() {
		return resp_msg;
	}

	public void setResp_msg(String resp_msg) {
		this.resp_msg = resp_msg;
	}

	private String resp_code; // 异常码
	private String resp_msg; // 异常描述

	private JuMeiExptMessageEnum(String resp_code, String resp_msg) {
		this.resp_code = resp_code;
		this.resp_msg = resp_msg;

	}
}

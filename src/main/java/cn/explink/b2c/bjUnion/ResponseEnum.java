package cn.explink.b2c.bjUnion;

public enum ResponseEnum {
	success("00","交易成功"),
	havesign("35","此运单已签收"),
	SignValidateFailed("01", "签名验证失败"), 
	WangLuoYiChang("02", "网络异常"), 
	ChaXunYiChang("03", "没有检索到数据"), 
	DingDanYiQianShou("04", "订单已签收"), 
	BuNengCheXiao("05","不能撤销,针对现金支付，以及无货款签收的订单不允许撤销"), 
	YingShouJinEYiChang("06", "应收金额不正确"), 
	YiShouKuanWeiQianShou("07", "已收款未签收"), 
	DengLuFaild("08", "登录失败,密码错误"), 
	JiaoYiChongFu("09", "交易重复"), 
	QianShouBuChuLi("10", "签收不处理(对于未收款的金额不为0的运单签收时)"), 
	YunDanYiZuoFanHuo("11", "该运单已做返货"),
	DingDanYiFankui("12", "订单已反馈过，不能再次反馈"),
	DengLuFaildforname("13", "登录失败,登录名错误"), 
	QiTaShiBai("99", "其他失败"),
	jiaoyileixingma("14","交易类型码不存在");
	private String response_code;
	private String response_msg;
	public String getResponse_code() {
		return response_code;
	}
	public void setResponse_code(String response_code) {
		this.response_code = response_code;
	}
	public String getResponse_msg() {
		return response_msg;
	}
	public void setResponse_msg(String response_msg) {
		this.response_msg = response_msg;
	}
	
	private ResponseEnum(String response_code,String response_msg){
		this.response_code = response_code;
		this.response_msg = response_msg;
	}
}

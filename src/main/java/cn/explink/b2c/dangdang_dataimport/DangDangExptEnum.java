package cn.explink.b2c.dangdang_dataimport;

public enum DangDangExptEnum {

	Success("0", "全部接收/撤回成功"), ParamsExpt("11", "参数错误，包括必须字段无值等"), SignValidateFailed("12", "token验证失败"), BuFenJieShouExpt("21", "全部或部分接收/撤回失败"), CwbInfoExpt("101", "该单信息有误，包括必须字段缺失、类型错误等"), CwbExists(
			"102", "订单已经存在"), OtherExpt("103", "其他失败"), ;

	private String expt_code;
	private String expt_msg;

	private DangDangExptEnum(String expt_code, String expt_msg) {
		this.expt_code = expt_code;
		this.expt_msg = expt_msg;

	}

	public String getExpt_code() {
		return expt_code;
	}

	public void setExpt_code(String expt_code) {
		this.expt_code = expt_code;
	}

	public String getExpt_msg() {
		return expt_msg;
	}

	public void setExpt_msg(String expt_msg) {
		this.expt_msg = expt_msg;
	}
}

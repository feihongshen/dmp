package cn.explink.pos.bill99;

public enum Bill99ExptMessageEnum {

	Success("00", "成功"), SignValidateFailed("01", "验证失败"), WangLuoYiChang("02", "网络异常"), ChaXunYiChang("03", "没有检索到数据"), DingDanYiQianShou("04", "订单已签收"), BuNengCheXiao("05",
			"不能撤销,针对现金支付，以及无货款签收的订单不允许撤销"), YingShouJinEYiChang("06", "应收金额不正确"), YiShouKuanWeiQianShou("07", "已收款未签收"), DengLuFaild("08", "未收款（收款冲正未找到原收款交易时返回）"), CheXiaoJiaoYiBuCunZai("09",
			"撤销交易不存在"), JiaoYiChongFu("10", "交易重复"), QianShouBuChuLi("11", "签收不处理"), YunDanYiZuoFanHuo("12", "该运单已做返货"),

	QiTaShiBai("99", "其他失败"), ;
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

	private String resp_code;
	private String resp_msg;

	private Bill99ExptMessageEnum(String resp_code, String resp_msg) {
		this.resp_code = resp_code;
		this.resp_msg = resp_msg;

	}
}

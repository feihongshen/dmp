package cn.explink.pos.alipay;

import cn.explink.enumutil.DeliveryStateEnum;

public enum AliPayExptEnum {

	OperatorExpt("63", "无网络覆盖,超服务范围", DeliveryStateEnum.WeiFanKui.getValue()), DiZhiBuXiang("72", "地址不详,错误", DeliveryStateEnum.JuShou.getValue()), ShouJianRenError("73", "收件人错误",
			DeliveryStateEnum.JuShou.getValue()), HuoWuPoSun("81", "货物破损", DeliveryStateEnum.JuShou.getValue()), BuFenDiuShi("82", "部分丢失", DeliveryStateEnum.JuShou.getValue()), WeiDingGou("83",
			"未订购该货物", DeliveryStateEnum.JuShou.getValue()), unsatisfied("84", "货物不满意", DeliveryStateEnum.JuShou.getValue()), ReceivedFeeError("85", "到货款、代收款不符", DeliveryStateEnum.JuShou.getValue()), KaiXiangYanHuo(
			"86", "要求开箱验货", DeliveryStateEnum.JuShou.getValue()), NoHavePapers("87", "未提供相关票证", DeliveryStateEnum.JuShou.getValue()), FenZhanZhiLiu("91", "延期改派", DeliveryStateEnum.FenZhanZhiLiu
			.getValue()), OtherExpt("99", "其他异常原因(手工录入)", DeliveryStateEnum.JuShou.getValue());

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

	public int getExpt_orderType() {
		return expt_orderType;
	}

	public void setExpt_orderType(int expt_orderType) {
		this.expt_orderType = expt_orderType;
	}

	private String expt_code;
	private String expt_msg;
	private int expt_orderType;

	private AliPayExptEnum(String expt_code, String expt_msg, int expt_orderType) {
		this.expt_code = expt_code;
		this.expt_msg = expt_msg;
		this.expt_orderType = expt_orderType;
	}
}

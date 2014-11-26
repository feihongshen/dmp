package cn.explink.pos.yeepay;

import cn.explink.enumutil.DeliveryStateEnum;

/**
 * 易宝支付POS异常反馈信息编码枚举
 * 
 * @author Administrator
 *
 */
public enum YeePayExptEnum {

	JuShou("1", "拒收", DeliveryStateEnum.JuShou.getValue()), BanShouBanTui("2", "半收半退", DeliveryStateEnum.BuFenTuiHuo.getValue()), HuoWuSunHuai("3", "货物损坏", DeliveryStateEnum.JuShou.getValue()), ErCiTouDi(
			"4", "二次投递", DeliveryStateEnum.FenZhanZhiLiu.getValue()), CancelSign("5", "取消签收", DeliveryStateEnum.JuShou.getValue());

	private String expt_code;
	private String expt_msg;
	private int expt_orderType;

	private YeePayExptEnum(String expt_code, String expt_msg, int expt_orderType) {
		this.expt_code = expt_code;
		this.expt_msg = expt_msg;
		this.expt_orderType = expt_orderType;
	}

	public String getExpt_code() {
		return expt_code;
	}

	public String getExpt_msg() {
		return expt_msg;
	}

	public int getExpt_orderType() {
		return expt_orderType;
	}

}

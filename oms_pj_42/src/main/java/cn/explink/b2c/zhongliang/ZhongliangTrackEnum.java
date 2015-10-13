package cn.explink.b2c.zhongliang;

import cn.explink.enumutil.FlowOrderTypeEnum;

/**
 * 订单跟踪信息推送
 * 
 * @author Administrator
 *
 */

public enum ZhongliangTrackEnum {

	Daorushuju(FlowOrderTypeEnum.DaoRuShuJu.getValue(), "1", "接受"), RuKu(FlowOrderTypeEnum.RuKu.getValue(), "3", "产生运单号"), Chuku(FlowOrderTypeEnum.ChuKuSaoMiao.getValue(), "800", "出库"), Daohuo(
			FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue(), "800", "到货"), Linhuo(FlowOrderTypeEnum.FenZhanLingHuo.getValue(), "800", "领货"), QianShou(FlowOrderTypeEnum.YiShenHe.getValue(), "4",
			"归班成功"), JuShou(FlowOrderTypeEnum.YiShenHe.getValue(), "5", "现场拒收"), ;

	private long flowordertype;
	private String state;
	private String text;

	private ZhongliangTrackEnum(long flowordertype, String state, String text) {
		this.state = state;
		this.flowordertype = flowordertype;
		this.text = text;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public long getFlowordertype() {
		return flowordertype;
	}

	public void setFlowordertype(long flowordertype) {
		this.flowordertype = flowordertype;
	}

}

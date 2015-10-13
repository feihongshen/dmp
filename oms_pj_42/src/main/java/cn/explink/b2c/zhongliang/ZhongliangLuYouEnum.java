package cn.explink.b2c.zhongliang;

import cn.explink.enumutil.FlowOrderTypeEnum;

/**
 * 订单跟踪信息推送
 * 
 * @author Administrator
 *
 */

public enum ZhongliangLuYouEnum {

	Ruku(FlowOrderTypeEnum.RuKu.getValue(), "1", "接受"), Peisong(FlowOrderTypeEnum.RuKu.getValue(), "3", "产生运单号"), QianShou(FlowOrderTypeEnum.YiShenHe.getValue(), "4", "归班成功"), JuShou(
			FlowOrderTypeEnum.YiShenHe.getValue(), "5", "现场拒收"), ;

	private long flowordertype;
	private String state;
	private String text;

	private ZhongliangLuYouEnum(long flowordertype, String state, String text) {
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

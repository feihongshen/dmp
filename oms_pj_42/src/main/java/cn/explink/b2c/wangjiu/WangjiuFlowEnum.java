package cn.explink.b2c.wangjiu;

import cn.explink.enumutil.FlowOrderTypeEnum;

/**
 * 订单流转信息推送
 * 
 * @author Administrator
 *
 */
public enum WangjiuFlowEnum {
	LanShou(FlowOrderTypeEnum.RuKu.getValue(), "GOT", "揽收"), QianShou(FlowOrderTypeEnum.YiShenHe.getValue(), "SIGNED", "签收"),

	;

	private long flowordertype;
	private String state;
	private String text;

	private WangjiuFlowEnum(long flowordertype, String state, String text) {
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

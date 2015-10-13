package cn.explink.b2c.wangjiu;

import cn.explink.enumutil.FlowOrderTypeEnum;

/**
 * 订单跟踪信息推送
 * 
 * @author Administrator
 *
 */
public enum WangjiuTrackEnum {
	ACCEPT(FlowOrderTypeEnum.RuKu.getValue(), "ACCEPT", "揽收"), TRANSIT(FlowOrderTypeEnum.FenZhanLingHuo.getValue(), "TRANSIT", "派送中"), SIGNED(FlowOrderTypeEnum.YiShenHe.getValue(), "SIGNED", "送达成功"), FAILED(
			FlowOrderTypeEnum.YiShenHe.getValue(), "FAILED", "送达失败"),

	;

	private long flowordertype;
	private String state;
	private String text;

	private WangjiuTrackEnum(long flowordertype, String state, String text) {
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

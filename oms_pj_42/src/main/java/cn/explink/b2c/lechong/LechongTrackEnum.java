package cn.explink.b2c.lechong;

import cn.explink.enumutil.FlowOrderTypeEnum;

/**
 * 订单跟踪信息推送
 * 
 * @author Administrator
 *
 */
public enum LechongTrackEnum {

	Ruku(FlowOrderTypeEnum.RuKu.getValue(), "ACCEPT", "入库"), PaiSongZhong(FlowOrderTypeEnum.FenZhanLingHuo.getValue(), "DELIVER", "派送中"), QianShou(FlowOrderTypeEnum.YiShenHe.getValue(), "SIGNED",
			"签收"), JuShou(FlowOrderTypeEnum.YiShenHe.getValue(), "FAILED", "现场拒收"), ;

	private long flowordertype;
	private String state;
	private String text;

	private LechongTrackEnum(long flowordertype, String state, String text) {
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

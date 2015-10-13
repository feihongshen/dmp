package cn.explink.b2c.yangguang;

import cn.explink.enumutil.FlowOrderTypeEnum;

/**
 * 一号店推送状态反馈的枚举
 * 
 * @author Administrator 央广接口结果状态：1.配送结束(不传) 2拒收，3滞留
 */
public enum YangGuangFlowEnum {

	BuFenShiBai(2, FlowOrderTypeEnum.YiShenHe.getValue(), "部分失败"), FenZhanZhiLiu(3, FlowOrderTypeEnum.YiShenHe.getValue(), "分站滞留"), JuShou(2, FlowOrderTypeEnum.YiShenHe.getValue(), "配送失败"),

	;

	public int getFlowordertype() {
		return flowordertype;
	}

	public void setFlowordertype(int flowordertype) {
		this.flowordertype = flowordertype;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	private int state;

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	private int flowordertype;
	private String text;

	private YangGuangFlowEnum(int state, int flowordertype, String text) {
		this.state = state;
		this.flowordertype = flowordertype;
		this.text = text;
	}

}

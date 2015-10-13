package cn.explink.b2c.homegobj;

import cn.explink.enumutil.FlowOrderTypeEnum;

/**
 * 订单状态
 * 
 * @author Administrator
 *
 */
public enum OrderStatusEnum {
	Qianshou(FlowOrderTypeEnum.YiShenHe.getValue(), "30", "签收"), JuShou(FlowOrderTypeEnum.YiShenHe.getValue(), "40", "现场拒收"),
	// JuShou1(FlowOrderTypeEnum.YiShenHe.getValue(),"41","非现场拒收"),
	ZhiLiu(FlowOrderTypeEnum.YiShenHe.getValue(), "60", "异常-联系不到客户"), DiuShi(FlowOrderTypeEnum.YiShenHe.getValue(), "50", "托寄物品丢失"), DuanXinYuYue(FlowOrderTypeEnum.FenZhanLingHuo.getValue(), "短信预约",
			"分站领货-短信预约"), ;

	private long flowordertype;
	private String state;
	private String text;

	private OrderStatusEnum(long flowordertype, String state, String text) {
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

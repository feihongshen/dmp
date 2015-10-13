package cn.explink.b2c.tmall;

import cn.explink.enumutil.DeliveryStateEnum;
import cn.explink.enumutil.FlowOrderTypeEnum;

/**
 * tmall推送状态反馈的枚举
 * 
 * @author Administrator
 *
 */
public enum TmallActionCodeEnum {
	P00("P00", DeliveryStateEnum.FenZhanZhiLiu.getValue(), "将于'时间'派送"), P01("P01", DeliveryStateEnum.QuanBuTuiHuo.getValue(), "将于'时间'退回"),
	// P02("P02",DeliveryStateEnum.FenZhanZhiLiu.getValue(),"已转'物流公司'派送，'运单号'"),
	// P03("P03",DeliveryStateEnum.FenZhanZhiLiu.getValue(),"将于'时间'派送"),
	P99("P99", DeliveryStateEnum.HuoWuDiuShi.getValue(), "其他"),

	;

	private String actionCode;

	public String getActionCode() {
		return actionCode;
	}

	public void setActionCode(String actionCode) {
		this.actionCode = actionCode;
	}

	private int deliverystate;
	private String text;

	private TmallActionCodeEnum(String actionCode, int deliverystate, String text) {
		this.actionCode = actionCode;
		this.deliverystate = deliverystate;
		this.text = text;
	}

	public int getDeliverystate() {
		return deliverystate;
	}

	public void setDeliverystate(int deliverystate) {
		this.deliverystate = deliverystate;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

}

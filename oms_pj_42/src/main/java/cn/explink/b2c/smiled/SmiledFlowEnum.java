package cn.explink.b2c.smiled;

import cn.explink.enumutil.DeliveryStateEnum;
import cn.explink.enumutil.FlowOrderTypeEnum;

/**
 * 思迈速递 反馈允许反馈的状态 三种：签收、拒收，异常反馈
 * 
 * @author Administrator
 *
 */
public enum SmiledFlowEnum {

	RuKuSaoMiao(FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue(), 1, "提货入库"), PaiJianSaoMiao(FlowOrderTypeEnum.FenZhanLingHuo.getValue(), 10, "派件扫描"), QianShou(FlowOrderTypeEnum.YiFanKui.getValue(),
			4, "签收成功"), YiChangLuRu(FlowOrderTypeEnum.YiFanKui.getValue(), 5, "异常件录入"), TuiHuoSaoMiao(FlowOrderTypeEnum.YiFanKui.getValue(), 6, "退货发件扫描"), TuiHuoZhanRuKu(
			FlowOrderTypeEnum.TuiHuoZhanRuKu.getValue(), 6, "退货确认状态"), ;
	private long flowordertype;
	private long smile_code;

	private String text;

	private SmiledFlowEnum(long flowordertype, long smile_code, String text) {
		this.flowordertype = flowordertype;
		this.smile_code = smile_code;
		this.text = text;
	}

	public long getSmile_code() {
		return smile_code;
	}

	public void setSmile_code(long smile_code) {
		this.smile_code = smile_code;
	}

	public long getFlowordertype() {
		return flowordertype;
	}

	public void setFlowordertype(long flowordertype) {
		this.flowordertype = flowordertype;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

}

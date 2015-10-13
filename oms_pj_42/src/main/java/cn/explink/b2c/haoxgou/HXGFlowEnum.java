package cn.explink.b2c.haoxgou;

import cn.explink.enumutil.DeliveryStateEnum;
import cn.explink.enumutil.FlowOrderTypeEnum;

/**
 * 好享购 反馈允许反馈的状态 三种：签收、拒收，异常反馈
 * 
 * @author Administrator
 *
 */
public enum HXGFlowEnum {

	// 配送在途
	PeiSongZhong(FlowOrderTypeEnum.FenZhanLingHuo.getValue(), "01", "配送中", 0),

	// 配送结果
	FenZhanZhiLiu(DeliveryStateEnum.FenZhanZhiLiu.getValue(), "02", "分站滞留", 1), JuShou(DeliveryStateEnum.QuanBuTuiHuo.getValue(), "03", "全部拒收", 1), BufenJuShou(DeliveryStateEnum.BuFenTuiHuo
			.getValue(), "03", "部分拒收", 1), PeiSongChengGong(DeliveryStateEnum.PeiSongChengGong.getValue(), "04", "签收成功", 1), ShangMenHuanChengGong(DeliveryStateEnum.ShangMenHuanChengGong.getValue(),
			"04", "上门换成功", 1),

	// 退货流程
	TuiHuoTuZhong(FlowOrderTypeEnum.TuiGongYingShangChuKu.getValue(), "05", "退货供货商途中", 0), GongHuoShangTuiHuoChenggong(FlowOrderTypeEnum.GongHuoShangTuiHuoChenggong.getValue(), "07", "供货商确认退货-接收", 0), GongYingShangJuShouFanKu(
			FlowOrderTypeEnum.GongYingShangJuShouFanKu.getValue(), "06", "供货商确认退货-拒收返库", 0), ;

	private long delivery_state;

	private String request_code; // 请求的指令

	private String text;
	private int state; // 0 配送中， 1最终结果

	private HXGFlowEnum(long delivery_state, String request_code, String text, int state) {
		this.delivery_state = delivery_state;
		this.request_code = request_code;
		this.text = text;
		this.state = state;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public long getDelivery_state() {
		return delivery_state;
	}

	public void setDelivery_state(long delivery_state) {
		this.delivery_state = delivery_state;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getRequest_code() {
		return request_code;
	}

	public void setRequest_code(String request_code) {
		this.request_code = request_code;
	}

}

package cn.explink.b2c.dpfoss;

import cn.explink.enumutil.DeliveryStateEnum;
import cn.explink.enumutil.FlowOrderTypeEnum;

/**
 * 好享购 反馈允许反馈的状态 三种：签收、拒收，异常反馈
 * 
 * @author Administrator
 *
 */
public enum DpfossTrackFlowEnum {

	// 配送在途
	KuFangRuKu(FlowOrderTypeEnum.RuKu.getValue(), "1", "到达-落地配公司库房", 0), KuFangChuKu(FlowOrderTypeEnum.ChuKuSaoMiao.getValue(), "2", "离开-库房出库", 0), PaiSongZhong(FlowOrderTypeEnum.FenZhanLingHuo
			.getValue(), "3", "派件中", 0),
	// 配送结果
	JuShou(DeliveryStateEnum.QuanBuTuiHuo.getValue(), "4", "全部拒收", 1), BufenJuShou(DeliveryStateEnum.BuFenTuiHuo.getValue(), "4", "部分拒收", 1), ;

	private long delivery_state;

	private String request_code; // 请求的指令

	private String text;
	private int state; // 0 配送中， 1最终结果

	private DpfossTrackFlowEnum(long delivery_state, String request_code, String text, int state) {
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

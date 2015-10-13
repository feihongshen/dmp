package cn.explink.b2c.dpfoss;

/**
 * 德邦 反馈允许反馈的状态 三种：签收、拒收,部分签收
 * 
 * @author Administrator
 *
 */
public enum DpfossSignedEnum {

	PeiSongChengGong(1, "1", "配送成功"), ShangMenTuiChengGong(2, "1", "上门退成功"), ShangMenHuanChengGong(3, "1", "上门换成功"), QuanBuTuiHuo(4, "4", "全部退货"), BuFenTuiHuo(5, "3", "部分退货"), ShangMenJuTui(7, "4",
			"上门拒退"),

	;

	private long delivery_state;
	private String request_code; // 请求的指令
	private String text;

	private DpfossSignedEnum(long delivery_state, String request_code, String text) {
		this.delivery_state = delivery_state;
		this.request_code = request_code;
		this.text = text;

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

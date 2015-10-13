package cn.explink.b2c.explink.core_up;

import cn.explink.enumutil.FlowOrderTypeEnum;

/**
 * 允许被查询到的状态
 * 
 * @author Administrator
 *
 */
public enum CommenFlowtypeEnum {

	KuFangRuKu(FlowOrderTypeEnum.RuKu.getValue(), "ACCEPT", "库房入库"), PaiSongZhong(FlowOrderTypeEnum.FenZhanLingHuo.getValue(), "DELIVERING", "分站派送中"), Fankui(FlowOrderTypeEnum.YiFanKui.getValue(),
			"FEEDBACK", "配送结果反馈"), BufenJuShou(FlowOrderTypeEnum.YiShenHe.getValue(), "VERIFY", "配送结果确认审核"),

	TuihuoChuZhan(FlowOrderTypeEnum.TuiHuoChuZhan.getValue(), "RET_OUT_BRANCH", "分站退货出站"),

	;
	private long own_code; // 自己公司的code
	private String request_code; // 请求的指令
	private String text;

	private CommenFlowtypeEnum(long own_code, String request_code, String text) {
		this.own_code = own_code;
		this.request_code = request_code;
		this.text = text;

	}

	public long getOwn_code() {
		return own_code;
	}

	public void setOwn_code(long own_code) {
		this.own_code = own_code;
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

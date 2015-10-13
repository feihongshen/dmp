package cn.explink.b2c.maisike;

import cn.explink.enumutil.PaytypeEnum;

public enum MaisikePaytypeEmum {
	POS(PaytypeEnum.Pos.getValue(), "POS", "POS刷卡支付"), CASH(PaytypeEnum.Xianjin.getValue(), "CASH", "现金支付"), ONLINE(PaytypeEnum.Xianjin.getValue(), "ONLINE", "在线支付"), NOPAY(PaytypeEnum.Xianjin
			.getValue(), "NOPAY", "未支付"),

	;

	public int getPaytypeid() {
		return paytypeid;
	}

	public void setPaytypeid(int paytypeid) {
		this.paytypeid = paytypeid;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	private int paytypeid;
	private String code;
	private String text;

	private MaisikePaytypeEmum(int paytypeid, String code, String text) {
		this.paytypeid = paytypeid;
		this.code = code;
		this.text = text;
	}
}

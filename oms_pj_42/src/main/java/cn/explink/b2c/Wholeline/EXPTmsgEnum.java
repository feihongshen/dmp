package cn.explink.b2c.Wholeline;

public enum EXPTmsgEnum {

	peisongchenggong(13, "配送成功"), zhiliujianrucang(17, "滞留件入仓"), daohuosaomiao(32, "分站到货扫描"), linghuocaomiao(12, "领货扫描");

	private int value;
	private String text;

	private EXPTmsgEnum(int value, String text) {
		this.value = value;
		this.text = text;
	}

	public int getValue() {
		return value;
	}

	public String getText() {
		return text;
	}

}

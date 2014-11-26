package cn.explink.pos.tools;

/**
 * 签收人类型 本人签收、他人代签
 * 
 * @author Administrator
 *
 */
public enum SignTypeEnum {

	BenRenQianShou(1, "本人签收"), TaRenDaiQianShou(2, "他人代签收");

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public String getSign_text() {
		return sign_text;
	}

	public void setSign_text(String sign_text) {
		this.sign_text = sign_text;
	}

	private int value;
	private String sign_text;

	private SignTypeEnum(int value, String sign_text) {
		this.value = value;
		this.sign_text = sign_text;
	}
}

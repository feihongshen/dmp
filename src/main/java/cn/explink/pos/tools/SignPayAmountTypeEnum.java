package cn.explink.pos.tools;

/**
 * 签收人类型 本人签收、他人代签
 * 
 * @author Administrator
 *
 */
public enum SignPayAmountTypeEnum {

	LingKuanQianShou(1, "零款签收"), FeiLingKuanQianShou(2, "非零款签收");
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

	private SignPayAmountTypeEnum(int value, String sign_text) {
		this.value = value;
		this.sign_text = sign_text;
	}
}

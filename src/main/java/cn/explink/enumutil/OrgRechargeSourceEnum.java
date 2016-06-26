package cn.explink.enumutil;
/**
 * 缴款记录来源枚举类
 * @author gordon.zhou
 *
 */
public enum OrgRechargeSourceEnum {
	Import(1, "导入缴款"), 
	SMT(2, "应退金额缴款 "), 
	FreightAdjust(3, "运费调整缴款 "),
	Vpal(4, "代扣收款"),
	PosCodAuto(5, "POSCOD自动回款"),
	ReceiveFeeAdjust(6, "货款调整缴款");
	
	private int value;
	private String text;
	public int getValue() {
		return value;
	}
	public void setValue(int value) {
		this.value = value;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	private OrgRechargeSourceEnum(int value, String text) {
		this.value = value;
		this.text = text;
	}
	
	public static String getTextFromValue(int value){
	            for (OrgRechargeSourceEnum mode : OrgRechargeSourceEnum.values()) {
	                if (mode.getValue()==value) {
	                    return mode.getText();
	                }
	            }
	     return "";
	}

}

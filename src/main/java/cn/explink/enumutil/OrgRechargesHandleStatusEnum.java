package cn.explink.enumutil;

import org.apache.commons.lang.StringUtils;

/**
 * 
 * @author gordon.zhou
 * 站点充值记录 处理状态枚举类
 */
public enum OrgRechargesHandleStatusEnum {	
	Imported(0, "导入"), 
	JustHandled(1, "未冲抵"),
	ChargeAgainsted(2,"已冲抵"),
	Exception(3, "处理异常"),
	Invalid(-1,"已撤销");
	

	private Integer value;
	private String text;
	

	private OrgRechargesHandleStatusEnum(Integer value, String text) {
		this.value = value;
		this.text = text;
	}

	public Integer getValue() {
		return value;
	}

	public String getText() {
		return text;
	}

	public static OrgRechargesHandleStatusEnum getByValue(Integer handleStatus) {
		for (OrgRechargesHandleStatusEnum cotie : OrgRechargesHandleStatusEnum.values()) {
			if (cotie.getValue() == handleStatus)
				return cotie;
		}
		return null;
	}
	
	public static Integer convertText2Value(String text){
		if(StringUtils.isNotEmpty(text)){
			for (OrgRechargesHandleStatusEnum cotie : OrgRechargesHandleStatusEnum.values()) {
				if (cotie.getText().equals(text))
					return cotie.getValue();
			}
		}
		return 0;
	}
	
	public static String getTextByValue(int value) {
		for (OrgRechargesHandleStatusEnum status : OrgRechargesHandleStatusEnum.values()) {
			if (status.getValue() == value) {
				return status.getText();
			}
		}
		return "";
	}

}

package cn.explink.enumutil;

import cn.explink.util.Tools;
/**
 * 站点账单类型
 * @author JY
 *
 */
public enum OrgBillTypeEnum {
	All(2,"全部"),
	Normal(0, "正常"), 
	Adjust(1, "调整");

	private Integer value;
	private String text;
	

	private OrgBillTypeEnum(Integer value, String text) {
		this.value = value;
		this.text = text;
	}

	public Integer getValue() {
		return value;
	}

	public String getText() {
		return text;
	}

	public static OrgBillTypeEnum getByValue(Integer cwbordertypeid) {
		for (OrgBillTypeEnum cotie : OrgBillTypeEnum.values()) {
			if (cotie.getValue() == cwbordertypeid)
				return cotie;
		}
		return null;
	}
	
	public static Integer convertText2Value(String text){
		if(!Tools.isEmpty(text)){
			for (OrgBillTypeEnum cotie : OrgBillTypeEnum.values()) {
				if (cotie.getText().equals(text))
					return cotie.getValue();
			}
		}
		return 0;
	}
	
	//根据code获得字符串
	public static String getTextByValue(Integer cwbordertypeid) {
		for (OrgBillTypeEnum cotie : OrgBillTypeEnum.values()) {
			if (cotie.getValue() == cwbordertypeid)
				return cotie.getText();
		}
		return null;
	}
	
}

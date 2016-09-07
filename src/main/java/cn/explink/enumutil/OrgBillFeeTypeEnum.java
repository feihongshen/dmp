package cn.explink.enumutil;

import cn.explink.util.Tools;

/**
 * 站点账单费用类型
 * @author gordon.zhou
 *
 */
public enum OrgBillFeeTypeEnum {
	COD(0, "代收货款"), 
	freightOfReturn(1, "上门退运费");

	private Integer value;
	private String text;
	

	private OrgBillFeeTypeEnum(Integer value, String text) {
		this.value = value;
		this.text = text;
	}

	public Integer getValue() {
		return value;
	}

	public String getText() {
		return text;
	}

	public static OrgBillFeeTypeEnum getByValue(Integer cwbordertypeid) {
		for (OrgBillFeeTypeEnum cotie : OrgBillFeeTypeEnum.values()) {
			if (cotie.getValue() == cwbordertypeid)
				return cotie;
		}
		return null;
	}
	
	public static Integer convertText2Value(String text){
		if(!Tools.isEmpty(text)){
			for (OrgBillFeeTypeEnum cotie : OrgBillFeeTypeEnum.values()) {
				if (cotie.getText().equals(text))
					return cotie.getValue();
			}
		}
		return 0;
	}
	
	
	//根据code获得显示字符串
	public static String getTextByValue(Integer cwbordertypeid) {
		for (OrgBillFeeTypeEnum cotie : OrgBillFeeTypeEnum.values()) {
			if (cotie.getValue() == cwbordertypeid)
				return cotie.getText();
		}
		return null;
	}
}

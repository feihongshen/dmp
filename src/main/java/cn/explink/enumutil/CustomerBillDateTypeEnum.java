package cn.explink.enumutil;

import cn.explink.util.Tools;


/**
 * 客户返款账单日期类型
 * @author gordon.zhou
 *
 */
public enum CustomerBillDateTypeEnum {
	customerDeliver(1,"客户发货时间"),
	sortingInventory(2,"分拣入库时间"),
	audit(3,"归班审核时间"),
	adjust(4,"调整记录时间");
	
	private Integer value;
	private String text;
	
	private CustomerBillDateTypeEnum(Integer value, String text) {
		this.value = value;
		this.text = text;
	}

	
	public static CustomerBillDateTypeEnum getByValue(Integer code) {
		for (CustomerBillDateTypeEnum cotie : CustomerBillDateTypeEnum.values()) {
			if (cotie.getValue() == code)
				return cotie;
		}
		return null;
	}
	
	public static Integer convertText2Value(String text){
		if(!Tools.isEmpty(text)){
			for (CustomerBillDateTypeEnum cotie : CustomerBillDateTypeEnum.values()) {
				if (cotie.getText().equals(text))
					return cotie.getValue();
			}
		}
		return 0;
	}
	
	//根据code获得字符串
	public static String getTextByValue(Integer code) {
		for (CustomerBillDateTypeEnum cotie : CustomerBillDateTypeEnum.values()) {
			if (cotie.getValue() == code)
				return cotie.getText();
		}
		return null;
	}
	
	public Integer getValue() {
		return value;
	}

	public void setValue(Integer value) {
		this.value = value;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
	
}

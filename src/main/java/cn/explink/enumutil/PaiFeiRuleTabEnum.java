/**
 *
 */
package cn.explink.enumutil;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Administrator
 *
 */
public enum PaiFeiRuleTabEnum {
	Paisong(1, "配送规则费"), Tihuo(2,"提货规则费"),Zhongzhuan(3, "中转规则费");

	private int value;
	private String text;
	private static  Map<Integer, PaiFeiRuleTabEnum> map=new HashMap<Integer, PaiFeiRuleTabEnum>();
	static {
		for(PaiFeiRuleTabEnum type:PaiFeiRuleTabEnum.values())
		{
			PaiFeiRuleTabEnum.map.put(type.value, type);
		}

	}
	private PaiFeiRuleTabEnum(int value, String text) {
		this.value = value;
		this.text = text;
	}

	public int getValue() {
		return this.value;
	}

	public String getText() {
		return this.text;
	}
	public PaiFeiRuleTabEnum getVauleByID(int value)
	{
		return PaiFeiRuleTabEnum.map.get(value);
	}
}

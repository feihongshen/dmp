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
public enum PaiFeiRuleStateEnum {
	zaiyong(1, "在用"), tingyong(2, "停用");
	private static  Map<Integer, PaiFeiRuleStateEnum> map=new HashMap<Integer, PaiFeiRuleStateEnum>();
	static {
		for(PaiFeiRuleStateEnum type:PaiFeiRuleStateEnum.values())
		{
			PaiFeiRuleStateEnum.map.put(type.value, type);
		}

	}
	private int value;
	private String text;

	private PaiFeiRuleStateEnum(int value, String text) {
		this.value = value;
		this.text = text;
	}

	public int getValue() {
		return this.value;
	}

	public String getText() {
		return this.text;
	}
	public PaiFeiRuleStateEnum getVauleByID(int value)
	{
		return PaiFeiRuleStateEnum.map.get(value);
	}
}

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
public enum PaiFeiRuleTypeEnum {

	Franchisee(1, "落地配与加盟商"), Customer(2,"落地配与供货商"),Derlivery(3, "落地配与配送员");

	private int value;
	private String text;
	private static  Map<Integer, PaiFeiRuleTypeEnum> map=new HashMap<Integer, PaiFeiRuleTypeEnum>();
	static {
		for(PaiFeiRuleTypeEnum type:PaiFeiRuleTypeEnum.values())
		{
			PaiFeiRuleTypeEnum.map.put(type.value, type);
		}

	}
	private PaiFeiRuleTypeEnum(int value, String text) {
		this.value = value;
		this.text = text;
	}

	public int getValue() {
		return this.value;
	}

	public String getText() {
		return this.text;
	}
	public PaiFeiRuleTypeEnum getVauleByID(int value)
	{
		return PaiFeiRuleTypeEnum.map.get(value);
	}
}

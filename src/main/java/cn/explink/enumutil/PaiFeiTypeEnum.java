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
public enum PaiFeiTypeEnum {
	Basic(1, "基本派费"),
	Collection(2,"代收补助费"),
	Area(3,"区域属性助费"),
	Overarea(4, "超区补助"),
	Business(5, "业务补助"),
	Insertion(6, "拖单补助");

	private int value;
	private String text;
	private static  Map<Integer, PaiFeiTypeEnum> map=new HashMap<Integer, PaiFeiTypeEnum>();
	static {
		for(PaiFeiTypeEnum type:PaiFeiTypeEnum.values())
		{
			PaiFeiTypeEnum.map.put(type.value, type);
		}

	}
	private PaiFeiTypeEnum(int value, String text) {
		this.value = value;
		this.text = text;
	}

	public int getValue() {
		return this.value;
	}

	public String getText() {
		return this.text;
	}
	public PaiFeiTypeEnum getVauleByID(int value)
	{
		return PaiFeiTypeEnum.map.get(value);
	}
}

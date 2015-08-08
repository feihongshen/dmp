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
public enum PaiFeiBuZhuTypeEnum {
	Basic(1, "基本派费"),
	Collection(2,"代收补助费"),
	Area(3,"区域属性助费"),
	Overarea(4, "超区补助"),
	Business(5, "业务补助"),
	Insertion(6, "拖单补助");

	private int value;
	private String text;
	private static  Map<Integer, PaiFeiBuZhuTypeEnum> map=new HashMap<Integer, PaiFeiBuZhuTypeEnum>();
	static {
		for(PaiFeiBuZhuTypeEnum type:PaiFeiBuZhuTypeEnum.values())
		{
			PaiFeiBuZhuTypeEnum.map.put(type.value, type);
		}

	}
	private PaiFeiBuZhuTypeEnum(int value, String text) {
		this.value = value;
		this.text = text;
	}

	public int getValue() {
		return this.value;
	}

	public String getText() {
		return this.text;
	}
	public static PaiFeiBuZhuTypeEnum getVauleByID(int value)
	{
		return PaiFeiBuZhuTypeEnum.map.get(value);
	}
}

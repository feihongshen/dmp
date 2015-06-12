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
public enum PenalizeSateEnum {
	Successful(1, "赔付成立"),
	Cancel(2, "赔付撤销");
	private int value;
	private String text;
	static Map<Integer, PenalizeSateEnum> bigMap=new HashMap<Integer, PenalizeSateEnum>();
	static {
		for (PenalizeSateEnum big : PenalizeSateEnum.values()) {
			PenalizeSateEnum.bigMap.put(big.getValue(), big);
		}
	}

	private PenalizeSateEnum(int value, String text) {
		this.value = value;
		this.text = text;
	}

	public int getValue() {
		return this.value;
	}

	public String getText() {
		return this.text;
	}

	public static PenalizeSateEnum getPenalizebigEnum(int value) {
		return PenalizeSateEnum.bigMap.get(value);
	}

}

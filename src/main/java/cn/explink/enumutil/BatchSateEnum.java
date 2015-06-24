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
public enum BatchSateEnum {
	Yihexiao(1, "已核销"),
	Weihexiao(0, "未核销");
	private int value;
	private String text;
	static Map<Integer, BatchSateEnum> bigMap=new HashMap<Integer, BatchSateEnum>();
	static {
		for (BatchSateEnum big : BatchSateEnum.values()) {
			BatchSateEnum.bigMap.put(big.getValue(), big);
		}
	}

	private BatchSateEnum(int value, String text) {
		this.value = value;
		this.text = text;
	}

	public int getValue() {
		return this.value;
	}

	public String getText() {
		return this.text;
	}

	public static BatchSateEnum getBatchSateEnum(int value) {
		return BatchSateEnum.bigMap.get(value);
	}

}

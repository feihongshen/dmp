package cn.explink.enumutil;

import java.util.HashMap;
import java.util.Map;

public enum PaytypeEnum {
	// ================= sitetype ================
	Xianjin(1, "现金"), Pos(2, "POS刷卡"), Zhipiao(3, "支票"), Qita(4, "其他"), CodPos(5, "支付宝COD扫码支付");

	private int value;
	private String text;

	private PaytypeEnum(int value, String text) {
		this.value = value;
		this.text = text;
	}

	public int getValue() {
		return value;
	}

	public String getText() {
		return text;
	}

	public static PaytypeEnum getByValue(int value) {
		for (PaytypeEnum paytypeEnum : PaytypeEnum.values()) {
			if (value == paytypeEnum.getValue()) {
				return paytypeEnum;
			}
		}
		return PaytypeEnum.Qita;
	}
	public static String getTextByValue(int value) {
		for (PaytypeEnum paytypeEnum : PaytypeEnum.values()) {
			if (value == paytypeEnum.getValue()) {
				return paytypeEnum.getText();
			}
		}
		return "";
	}
	
	public static Map<Integer, String> getMap() {
		Map<Integer, String> map = new HashMap<Integer, String>();
		for (PaytypeEnum e : PaytypeEnum.values()) {
			map.put(e.value, e.text);
		}
		return map;
	}
	
}

package cn.explink.enumutil;


/**
 * 一票多件是否到齐（0：未到齐，1：到齐）
 *
 * @author songkaojun 2016年1月7日
 */
public enum MPSAllArrivedFlagEnum {
	NO(0, "未到齐"), YES(1, "到齐");

	private int value;
	private String text;

	private MPSAllArrivedFlagEnum(int value, String text) {
		this.value = value;
		this.text = text;
	}

	public int getValue() {
		return this.value;
	}

	public String getText() {
		return this.text;
	}

}

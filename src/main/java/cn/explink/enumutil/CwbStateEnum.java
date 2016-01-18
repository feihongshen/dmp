package cn.explink.enumutil;

import java.util.HashMap;
import java.util.Map;

import cn.explink.exception.ExplinkException;

public enum CwbStateEnum {
	WUShuju(0, "无数据"), PeiShong(1, "配送"), TuiHuo(2, "退货"), DiuShi(3, "全部丢失"), WuXiaoShuJu(4, "无效数据"), TuiGongYingShang(5, "退供货商"), ZhongZhuan(6, "中转"), OXO_JIT(7, "OXO_JIT"), BUFENDIUSHI(8, "部分丢失"), WANQUANPOSUN(
			9, "全部破损"), BUFENPOSUN(10, "部分破损");

	private int value;
	private String text;

	private CwbStateEnum(int value, String text) {
		this.value = value;
		this.text = text;
	}

	public int getValue() {
		return this.value;
	}

	public String getText() {
		return this.text;
	}

	public static CwbStateEnum getByValue(long value) {
		for (CwbStateEnum cc : CwbStateEnum.values()) {
			if (value == cc.getValue()) {
				return cc;
			}
		}
		throw new ExplinkException(ExceptionCwbErrorTypeEnum.PEi_SONG_LEI_XING_WEI_ZHAO_DAO, value);
	}

	public static Map<Integer, String> getMap() {
		Map<Integer, String> map = new HashMap<Integer, String>();
		for (CwbStateEnum e : CwbStateEnum.values()) {
			map.put(e.value, e.text);
		}
		return map;
	}

	/**
	 *
	 * @Title: getTextByValue
	 * @description 通过value获取枚举的text
	 * @author 刘武强
	 * @date  2016年1月12日下午8:24:58
	 * @param  @param value
	 * @param  @return
	 * @return  String
	 * @throws
	 */
	public static String getTextByValue(int value) {
		for (CwbStateEnum temp : CwbStateEnum.values()) {
			if (temp.getValue() == value) {
				return temp.getText();
			}
		}
		return "";
	}
}

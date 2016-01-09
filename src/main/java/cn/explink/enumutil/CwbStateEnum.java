package cn.explink.enumutil;

import java.util.HashMap;
import java.util.Map;

import cn.explink.exception.ExplinkException;

public enum CwbStateEnum {
	WUShuju(0, "无数据"), PeiShong(1, "配送"), TuiHuo(2, "退货"), DiuShi(3, "完全丢失"), WuXiaoShuJu(4, "无效数据"), TuiGongYingShang(5, "退供货商"), ZhongZhuan(6, "中转")
	,OXO_JIT(7,"OXO_JIT"),BUFENDIUSHI(8,"部分丢失"),WANQUANPOSUN(9,"完全破损"),BUFENPOSUN(10,"部分破损") ;

	private int value;
	private String text;

	private CwbStateEnum(int value, String text) {
		this.value = value;
		this.text = text;
	}

	public int getValue() {
		return value;
	}

	public String getText() {
		return text;
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
}

package cn.explink.enumutil;

/**
 * 订单生命周期类型id
 * 
 * @author jinghui.pan
 *
 */
public enum CwbOrderLifeCycleTypeIdEnum {

	Import(1, "导入未收货"), 
	TiHuo(2, "提货未入库"), 
	FengBoRuku(3, "分拣入库未出库"),
	FengBoChuku(4, "分拣出库未到站"),
	ZhanDianZaiZhan(5, "站点在站货物"),
	ZhanDianWeiFanKuan(6, "站点未返代收货款"),
	TuiHuoChuZhanZaiTu(7, "退货出站在途"),
	ZhongZhuanChuZhanZaiTu(8, "中转出站在途"),
	ZhongZhuanKuRuKu(9, "中转库入库未出库"),
	ZhongZhuanKuChuKu(10, "中转出库未到站"),
	TuiHuoKuRuKu(11, "退货入库未出库"),
	TuiHuoZaiTou(12, "退货再投未到站"),
	TuiKeHuZaiTu(13, "退客户在途"),
	TuiKeHuWeiShouKuan(14, "退客户未收款");

	private int value;
	private String text;

	private CwbOrderLifeCycleTypeIdEnum(int value, String text) {
		this.value = value;
		this.text = text;
	}

	public int getValue() {
		return value;
	}

	public String getText() {
		return text;
	}

	public static CwbOrderLifeCycleTypeIdEnum getByValue(int cwbordertypeid) {
		for (CwbOrderLifeCycleTypeIdEnum idEnum : CwbOrderLifeCycleTypeIdEnum.values()) {
			if (idEnum.getValue() == cwbordertypeid)
				return idEnum;
		}
		return null;
	}
	public static String getTextByValue(int value) {
		for (CwbOrderLifeCycleTypeIdEnum idEnum : CwbOrderLifeCycleTypeIdEnum.values()) {
			if (idEnum.getValue() == value){
				return idEnum.getText();
			}	
		}
		return "";
	}
	public static int getMaxValue() {
		int ret = 0;
		for (CwbOrderLifeCycleTypeIdEnum idEnum : CwbOrderLifeCycleTypeIdEnum.values()) {
			if (idEnum.getValue() > ret)
				ret = idEnum.getValue();
		}
		return ret;
	}
}

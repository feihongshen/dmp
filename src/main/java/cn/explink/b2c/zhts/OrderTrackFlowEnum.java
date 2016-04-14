package cn.explink.b2c.zhts;

import java.util.HashMap;
import java.util.Map;

public enum OrderTrackFlowEnum {
	RuKu(4, "入库"), 
	ChuKuSaoMiao(6, "出库"), 
	FenZhanDaoHuoSaoMiao(7, "分站到货扫描"), 
	FenZhanDaoHuoYouHuoWuDanSaoMiao(8, "到错货"), 
	FenZhanLingHuo(9, "领货"), 
	ZhongZhuanZhanRuKu(12, "中转站入库"), 
	ZhongZhuanZhanChuKu(14, "中转站出库"), 
	TuiHuoZhanRuKu(15, "退货站入库"), 
	TuiGongYingShangChuKu(27, "退供货商出库"), 
	GongYingShangJuShouFanKu(28, "供货商拒收返库"), 
	CheXiaoFanKui(29, "撤销反馈"),
	GongHuoShangTuiHuoChenggong(34, "供货商退货成功"), 
	YiFanKui(35, "反馈"), 
	YiShenHe(36, "审核"), 
	DaoCuoHuoChuLi(38, "到错货处理"), 
	TuiHuoChuZhan(40, "退货出站"), 
	DingDanLanJie(44, "订单拦截"), 
	ShenHeWeiZaiTou(45, "审核为退货再投"), 
	KuDuiKuChuKuSaoMiao(46, "库对库出库"),
	ChaoQu(60, "超区"), 
	YiChangPiPeiYiChuLi(61, "异常匹配已处理"),
	//退供货商审核(拒收退货)--新加
	GongYingShangJuShouTuiHuo(62, "退供应商拒收退货");

	private int value;
	private String text;

	private static Map<Integer, OrderTrackFlowEnum> indexMap = null;
	static {
		OrderTrackFlowEnum.indexMap = new HashMap<Integer, OrderTrackFlowEnum>();
		for (OrderTrackFlowEnum orderType : OrderTrackFlowEnum.values()) {
			OrderTrackFlowEnum.indexMap.put(Integer.valueOf(orderType.getValue()), orderType);
		}
	}

	private OrderTrackFlowEnum(int value, String text) {
		this.value = value;
		this.text = text;
	}

	public int getValue() {
		return this.value;
	}

	public String getText() {
		return this.text;
	}

	public static OrderTrackFlowEnum getText(int value) {
		return OrderTrackFlowEnum.indexMap.get(Integer.valueOf(value));
	}

	public static OrderTrackFlowEnum getText(long value) {
		return OrderTrackFlowEnum.indexMap.get(Integer.valueOf((int) value));
	}
	
	public static Map<Integer, String> getMap() {
		Map<Integer, String> map = new HashMap<Integer, String>();
		for (OrderTrackFlowEnum e : OrderTrackFlowEnum.values()) {
			map.put(e.value, e.text);
		}
		return map;
	}
}

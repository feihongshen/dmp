package cn.explink.enumutil;

import java.util.HashMap;
import java.util.Map;

public enum FlowOrderTypeEnum {
	DaoRuShuJu(1, "导入数据"),
	// YouHuoWuDanDaoRuShuJu(2,"有货无单导入数据","NoListImportCwb"),
	TiHuo(2, "提货"), TiHuoYouHuoWuDan(3, "提货有货无单"), RuKu(4, "入库"), ChuKuSaoMiao(6, "出库"), FenZhanDaoHuoSaoMiao(7, "分站到货扫描"), FenZhanDaoHuoYouHuoWuDanSaoMiao(8, "到错货"), FenZhanLingHuo(9, "领货"), ZhongZhuanZhanRuKu(
			12, "中转站入库"), ZhongZhuanZhanChuKu(14, "中转站出库"), TuiHuoZhanRuKu(15, "退货站入库"), TuiGongYingShangChuKu(27, "退供货商出库"), GongYingShangJuShouFanKu(28, "供货商拒收返库"), CheXiaoFanKui(29, "撤销反馈"),
	// ChongZhengJiaoYi(30,"冲正交易"),
	GongHuoShangTuiHuoChenggong(34, "供货商退货成功"), YiFanKui(35, "反馈"), YiShenHe(36, "审核"), UpdateDeliveryBranch(37, "更新配送站"), DaoCuoHuoChuLi(38, "到错货处理"), BeiZhu(39, "备注"), TuiHuoChuZhan(40, "退货出站"), ShouGongXiuGai(
			41, "手工修改"), PosZhiFu(42, "POS支付"), YiChangDingDanChuLi(43, "异常订单处理"), DingDanLanJie(44, "订单拦截"), ShenHeWeiZaiTou(45, "审核为退货再投"), KuDuiKuChuKuSaoMiao(46, "库对库出库"), BaoGuoweiDao(50, "包裹未到"), // 亚马逊对接使用
	ZhongZhuanyanwu(51, "中转延误"), // 亚马逊对接使用
	ShouGongdiushi(52, "货物丢失"), LanShouDaoHuo(53, "揽收到货"), LingHuoWeiTuo(54, "领货委托"), WeiPaiCheXiao(55, "委派撤销"), GaiDan(56, "改单"), ChaoQu(60, "超区"), YiChangPiPeiYiChuLi(61, "异常匹配已处理"),
	//退供货商审核(拒收退货)--新加
	GongYingShangJuShouTuiHuo(62, "退供应商拒收退货"),
	UpdatePickBranch(63,"更新提货站"),
	ChongZhiFanKui(64,"重置反馈"),
	
	//zhili01.liang，修改货物类型需求，
	/**修改货物类型流程：审核通过，*/
	XiuGaiHuoWuLeiXingTongGuo(67,"修改货物类型通过"),
	/**修改货物类型流程：审核不通过，*/
	XiuGaiHuoWuLeiXingBuTongGuo(66,"修改货物类型不通过"),
	
	/**
	 * 快递的三个状态
	 */
	YunDanLuRu(1000,"运单录入"),
	LanJianRuZhan(1001,"揽件入站"),
	LanJianChuZhan(1002,"揽件出站"),
	
	//add by vic.liang@pjbest.com 2016-08-20
	BingEmsTrans(65,"绑定邮政运单");
	
	private int value;
	private String text;

	private static Map<Integer, FlowOrderTypeEnum> indexMap = null;
	static {
		FlowOrderTypeEnum.indexMap = new HashMap<Integer, FlowOrderTypeEnum>();
		for (FlowOrderTypeEnum orderType : FlowOrderTypeEnum.values()) {
			FlowOrderTypeEnum.indexMap.put(Integer.valueOf(orderType.getValue()), orderType);
		}
	}

	private FlowOrderTypeEnum(int value, String text) {
		this.value = value;
		this.text = text;
	}

	public int getValue() {
		return this.value;
	}

	public String getText() {
		return this.text;
	}

	public static FlowOrderTypeEnum getText(int value) {
		return FlowOrderTypeEnum.indexMap.get(Integer.valueOf(value));
	}

	public static FlowOrderTypeEnum getText(long value) {
		return FlowOrderTypeEnum.indexMap.get(Integer.valueOf((int) value));
	}

	public static Map<Integer, String> getMap() {
		Map<Integer, String> map = new HashMap<Integer, String>();
		for (FlowOrderTypeEnum e : FlowOrderTypeEnum.values()) {
			map.put(e.value, e.text);
		}
		return map;
	}

	/**
	 *
	 * @Title: getByValue
	 * @description 根据value获取枚举对象
	 * @author 刘武强
	 * @date  2016年1月11日下午8:43:28
	 * @param  @param value
	 * @param  @return
	 * @return  FlowOrderTypeEnum
	 * @throws
	 */
	public static FlowOrderTypeEnum getByValue(int value) {
		for (FlowOrderTypeEnum temp : FlowOrderTypeEnum.values()) {
			if (temp.getValue() == value) {
				return temp;
			}
		}
		return null;
	}
}

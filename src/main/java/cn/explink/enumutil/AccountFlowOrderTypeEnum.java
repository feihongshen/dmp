package cn.explink.enumutil;

/**
 * 结算操作类型
 * 
 * @author zs
 *
 */
public enum AccountFlowOrderTypeEnum {
	KuFangChuKu(1, "库房出库扫描"), ZhongZhuanRuKu(2, "中转入库扫描"), TuiHuoRuKu(3, "退货入库扫描"), ZhongZhuanChuKu(4, "中转出库扫描"), // 出库给站点
	TuiHuoChuKu(5, "退货出库扫描"), // 出库给站点
	GuiBanShenHe(6, "归班审核"), GaiZhanChongKuan(7, "改站冲款"), // 强制出库

	KouKuan(8, "扣款"), ChongZheng(9, "冲正"), ChongZhi(10, "充值"), ZhongZhuan(11, "中转"), TuiHuo(12, "退货"), Pos(13, "POS"), TiaoZhang(14, "调账");

	private int value;
	private String text;

	private AccountFlowOrderTypeEnum(int value, String text) {
		this.value = value;
		this.text = text;
	}

	public int getValue() {
		return value;
	}

	public String getText() {
		return text;
	}

	public static AccountFlowOrderTypeEnum getText(int value) {
		for (AccountFlowOrderTypeEnum typeEnum : AccountFlowOrderTypeEnum.values()) {
			if (typeEnum.getValue() == value) {
				return typeEnum;
			}
		}
		return null;
	}

	public static AccountFlowOrderTypeEnum getText(long value) {
		for (AccountFlowOrderTypeEnum typeEnum : AccountFlowOrderTypeEnum.values()) {
			if (typeEnum.getValue() == value) {
				return typeEnum;
			}
		}
		return null;
	}
}

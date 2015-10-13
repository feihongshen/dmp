package cn.explink.enumutil;

public enum FlowOrderTypeZongheEnum {
	DaoRuShuJu(1, "导入数据", "emaildatetime"),
	// YouHuoWuDanDaoRuShuJu(2,"有货无单导入数据","NoListImportCwb"),
	TiHuo(2, "提货", "getgoodstime"), 
	RuKu(4, "入库", "intowarehoustime"), 
	ChuKuSaoMiao(6, "出库", "outwarehousetime"), 
	FenZhanDaoHuoSaoMiao(7, "分站到货","substationgoodstime"), 
	FenZhanDaoHuoYouHuoWuDanSaoMiao(8, "到错货", "substationgoodstime"), 
	FenZhanLingHuo(9, "领货", "receivegoodstime"), 
	ZhongZhuanChuKuSaoMiao(10, "站点中转出站","zhandianouttozhongzhuantime"), 
	ZhongZhuanZhanRuKu(12, "中转站入库", "changeintowarehoustime"), 
	ZhongZhuanZhanChuKuSaoMiao(14, "中转站出库", "changeouttowarehoustime"), 
	TuiHuoZhanRuKu(15, "退货站入库", "tuihuointowarehoustime"), 
	TuiHuoZhanZaiTouSaoMiao(17, "退货站再投出库", "tuihuoouttozhandiantime"), 
	TuiGongYingShangChuKu(27, "退供货商出库", "customerbacktime"), 
	GongYingShangJuShouFanKu(28, "供货商拒收返库", "gonghuoshangjushoutime"), 
	GongHuoShangTuiHuoChenggong(34, "供货商退货成功", "gonghuoshangchenggongtime"), 
	YiFanKui(35,"反馈", "couplebacktime"), 
	YiShenHe(36, "审核", "checktime"), 
	TuiHuoChuKuSaoMiao(40,"站点退货出站", "returngoodsoutwarehousetime"), 
	YiChangDingDanChuLi(43, "异常订单处理", "yichangchuli"), 
	DingDanLanJie(44, "订单拦截", "DingDanLanJie"), 
	ShenHeWeiZaiTou(45, "审核为退货再投","ShenHeWeiZaiTou"),
	KuDuiKuChuKuSaoMiao(46, "库对库出库", "housetohousetime"); 

	private int value;
	private String text;
	private String method;

	private FlowOrderTypeZongheEnum(int value, String text, String method) {
		this.value = value;
		this.text = text;
		this.method = method;
	}

	public int getValue() {
		return value;
	}

	public String getText() {
		return text;
	}

	public String getMethod() {
		return method;
	}

	public static FlowOrderTypeEnum getText(int value) {
		for (FlowOrderTypeEnum typeEnum : FlowOrderTypeEnum.values()) {
			if (typeEnum.getValue() == value) {
				return typeEnum;
			}
		}
		return null;
	}
}

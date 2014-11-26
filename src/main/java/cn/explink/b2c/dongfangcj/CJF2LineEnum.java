package cn.explink.b2c.dongfangcj;

/**
 * 存储 txt文件位置的枚举
 * 
 * @author Administrator 按字符串索引截取 type 上门退类型
 */
public enum CJF2LineEnum {

	PaiXuBianHao("排序编号", 2, 7), HuiShouQuFen("回收区分", 8, 8),

	PeiSongDiQu("配送地区", 12, 16), HuoYunDanHao("货运单号", 17, 28), DingGouBianHao("订购编号", 29, 51),
	// cargotype
	ZengPinYufou("赠品与否", 52, 52), JieNengBuTieYufou("节能补贴与否", 9, 9), SongHuoFeiYongQufen("送货费用区分", 10, 11),

	PeiSongShangPinQufen("配送商品区分", 53, 54), ShangPinZhongLiang("商品重量", 55, 61), ShouJianRen("收件人", 62, 81), DianHua("电话", 82, 106), ShouJi("手机", 107, 131), ShangPinBianHao("商品编号", 132, 141), ShangPinMingCheng(
			"商品名称", 142, 191), ShangPinShuLiang("商品数量", 192, 195), YouBian("邮编", 196, 201), GuKeDiZhi("退货顾客地址", 202, 301), BeiZhu("备注", 302, 401), TuiHuoCangKu("退货仓库", 402, 402),

	GongYingShang("供应商", 403, 452), GYSFuZeRen("供应商负责人", 453, 472), GYSDianHua("供应商电话号码", 473, 485), GYSYouBian("供应商邮编", 486, 491), GYSDiZhi("供应商地址", 492, 591),

	;

	private String lineName; // 列 名
	private int bIx; // 开始字符串位置
	private int eIx; // 结束字符串位置

	private CJF2LineEnum(String lineName, int beginIndex, int endIndex) {
		this.lineName = lineName;
		this.bIx = beginIndex;
		this.eIx = endIndex;
	}

	public String getLineName() {
		return lineName;
	}

	// java取索引从 0开始需 -1
	public int getbIx() {
		return bIx - 1;
	}

	public int geteIx() {
		return eIx;
	}

}

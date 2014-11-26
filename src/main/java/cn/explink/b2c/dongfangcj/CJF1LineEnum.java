package cn.explink.b2c.dongfangcj;

/**
 * 存储 txt文件位置的枚举
 * 
 * @author Administrator 按字符串索引截取 type 配送类型
 */
public enum CJF1LineEnum {

	PaiXuBianHao("排序编号", 2, 7), Cwb("货运单号", 8, 19), Shipcwb("订购编号", 46, 68),

	// Cargotype
	ZhiPeiSongYuFou("直配送与否", 20, 20), BuTieYuFou("节能补贴与否", 21, 21), ZengPingYuFou("赠品与否", 22, 22),

	// sendcargoname
	PeiSongShangPingQuFen("配送商品区分", 24, 25), SongHuoFeiYongQuFen("送货费用区分", 26, 27), ShangPingMingCheng("商品名称", 149, 198),

	Sendcargonum("商品数量", 199, 202), Cargoamount("商品金额", 203, 214), Receivablefee("应收金额", 203, 214), Cargorealweight("商品重量", 215, 221), Consigneename("收件人", 69, 88), Consigneephone("电话", 89, 113), ConsigneeMobile(
			"手机", 114, 138), Consigneepostcode("邮编", 228, 233),

	// consigneeaddress
	Consigneeaddress("送货地址", 234, 333), PeisongDiQu("配送地区", 30, 34),

	ShouXuFei("手续费", 222, 227), ShangPingBianHao("商品编号", 139, 148),

	// cwbremark
	DingDanBeiZhu("订单备注", 334, 433), ChuKuQuFen("出库区分", 23, 23),

	// customercommand
	XiuXiRiYuFou("休息日与否", 28, 28), GongZuoRiYuFou("工作与否", 29, 29), ZhiDingRiYuFou("指定日与否", 35, 35), ZhiDingRi("指定日", 36, 43), ZhiDingShiJian("指定时间", 44, 45),

	;

	private String lineName; // 列 名
	private int bIx; // 开始字符串位置
	private int eIx; // 结束字符串位置

	private CJF1LineEnum(String lineName, int beginIndex, int endIndex) {
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

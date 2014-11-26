package cn.explink.b2c.homegou;

/**
 * 存储 txt文件位置的枚举
 * 
 * @author Administrator 按字符串索引截取 type 配送类型
 */
public enum DOLineEnum {

	Hscode("HS生成编号", 1, 14), Shipptime("配送申请日", 15, 22), express_id("配送公司代码", 23, 24),

	Cwb("运单号", 25, 36), DingdanLeiXing("订购区分", 37, 39), DingGouBianHao("订购编号", 40, 62),

	// sendcargoname
	Consignee_no("顾客编号", 63, 74), Consigneename("顾客姓名", 75, 114), ConsigneepostCode("邮政代码", 115, 120), Consigneeaddress("邮政地址", 121, 320),

	ConsigneeMobile("手机", 321, 340), // 341 360
	Consigneephone("电话", 341, 360), // 321 340

	PayType("付款方法", 361, 362), ShangPingDaiMa("商品代码", 363, 372), sendcarnum("商品数量", 373, 374), receivablefee("商品金额", 375, 382), Cargoremark("商品配套", 383, 2382),

	;

	private String lineName; // 列 名
	private int bIx; // 开始字符串位置
	private int eIx; // 结束字符串位置

	private DOLineEnum(String lineName, int beginIndex, int endIndex) {
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

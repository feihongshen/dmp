package cn.explink.b2c.yangguang;

/**
 * 配送订单详情字段枚举
 * 
 * @author Administrator
 *
 */
public enum YgSE1Enum {

	SerialNo("序列", 2, 7), WarehouseCode("仓库编号", 8, 10), ShipperNo("运单编号", 11, 23), // 对应cwb
	KuChuQuFen("出库区分", 24, 24), // ①COD ②Mobile Pos ③Only
								// Delivery(一般送货)④Replacement delivery (交换) ⑤And
								// so on(其它)
	OrderNo("订购编号", 25, 47), // 对应 真实商品编号
	CustName("顾客姓名", 48, 67), // 对应 consigneename
	TelephoneNo("电话", 68, 80), // 对应 consigneephone
	MobilephoneNo("手机", 81, 93), // 对应 consigneemobile
	ZipCode("邮编", 94, 99), // 对应 consigneepostcode
	Address("地址", 100, 199), // 对应 consigneeaddress
	ProductCode("商品编号", 200, 209), ProductName("商品名称", 210, 259), UnitCode("单品CODE", 260, 262), UnitName("单品名称", 263, 312), ProductQty("商品数量", 313, 316), ProductAmount("COD金额", 317, 326), Comments(
			"其它", 327, 426), DateofDelivery("送货邀请日", 427, 434), Wb_I_No("运单识别编号", 435, 448), Weight("重量", 449, 454), ;

	private String lineName; // 列 名
	private int bIx; // 开始字符串位置
	private int eIx; // 结束字符串位置

	private YgSE1Enum(String lineName, int beginIndex, int endIndex) {
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

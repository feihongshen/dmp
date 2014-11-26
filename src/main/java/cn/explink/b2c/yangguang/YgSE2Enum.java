package cn.explink.b2c.yangguang;

/**
 * 上门退换订单详情字段枚举
 * 
 * @author Administrator
 *
 */
public enum YgSE2Enum {

	SerialNo("序列", 2, 7), WarehouseCode("仓库编号", 8, 10), pick_type("回收区分", 11, 11), // 对应订单类型，上门退和上门换
																					// 1退货，2换货
	OrderNo("退货接收编号", 12, 34), // 对应 真实商品编号
	CustName("顾客姓名", 35, 54), // 对应 consigneename
	TelephoneNo("电话", 55, 67), // 对应 consigneephone
	MobilephoneNo("手机", 68, 80), // 对应 consigneemobile
	ZipCode("邮编", 81, 86), // 对应 consigneepostcode
	Address("地址", 87, 186), // 对应 consigneeaddress
	ProductCode("商品编号", 187, 196), ProductName("商品名称", 197, 246), UnitCode("单品CODE", 247, 249), UnitName("单品名称", 250, 299), ProductQty("商品数量", 300, 303), Memo("其它", 304, 403), RETURN_NO("退货识别编号",
			404, 415), Weight("重量", 416, 421), ;

	private String lineName; // 列 名
	private int bIx; // 开始字符串位置
	private int eIx; // 结束字符串位置

	private YgSE2Enum(String lineName, int beginIndex, int endIndex) {
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

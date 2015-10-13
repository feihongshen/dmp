package cn.explink.b2c.homegou;

/**
 * 存储 txt文件位置的枚举
 * 
 * @author Administrator 按字符串索引截取 type 状态反馈-配送结束接口枚举
 */
public enum DCEnum {

	paixuCode("配送结束编号", 1, 10), company_code("配送公司代码", 11, 12), Cwb("运单号", 13, 24), deliveryResult("配送结束与否", 25, 25), deliverytime("配送结束日", 26, 33),

	deliveryReason("配送原因代码", 34, 35), CwbOrdertype("配送区分", 36, 37), // 10:不用付款,
																	// 20:货到付款，40：交换送货
	deliveryAmount("配送金额", 38, 45), payType1("付款方法1", 46, 47), payAmount1("付款金额1", 48, 55), payType2("付款方法2", 56, 57), payAmount2("付款方法2", 58, 65),

	EndLen("尾数位数", 1, 5) // 统计 总单量,6位数
	;

	private String lineName; // 列 名
	private int bIx; // 开始字符串位置
	private int eIx; // 结束字符串位置
	private int len; // 长度

	private DCEnum(String lineName, int beginIndex, int endIndex) {
		this.lineName = lineName;
		this.bIx = beginIndex;
		this.eIx = endIndex;
	}

	public String getLineName() {
		return lineName;
	}

	// java取索引从 0开始需 -1
	public int getbIx() {
		return bIx;
	}

	public int geteIx() {
		return eIx;
	}

	public int getLen() {
		return eIx - bIx == 0 ? 1 : (eIx - bIx + 1);
	}

}

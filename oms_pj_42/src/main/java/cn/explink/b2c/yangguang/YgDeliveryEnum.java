package cn.explink.b2c.yangguang;

/**
 * 存储 txt文件位置的枚举
 * 
 * @author Administrator 按字符串索引截取 type 状态反馈-配送结束接口枚举
 */
public enum YgDeliveryEnum {

	DataFlag("Data Flag", 1, 1), PaiXuBianHao("序列", 2, 7), OrderNo("订购编号", 8, 30), ShippNo("运单编号", 31, 43), DeliveryDate("送货日", 44, 51), CompletionFlag("送货与否", 52, 52), ExptReason("未送货原因", 53, 54), Express_ID(
			"快递公司ID", 55, 56), Wb_I_No("快递公司ID", 57, 70),

	EndLen("尾数位数", 1, 7) // 统计 总单量,6位数
	;

	private String lineName; // 列 名
	private int bIx; // 开始字符串位置
	private int eIx; // 结束字符串位置
	private int len; // 长度

	private YgDeliveryEnum(String lineName, int beginIndex, int endIndex) {
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

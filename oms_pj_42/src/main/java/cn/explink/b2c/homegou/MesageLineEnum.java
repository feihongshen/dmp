package cn.explink.b2c.homegou;

/**
 * 存储 txt文件位置的枚举
 * 
 * @author Administrator 按字符串索引截取 type 状态反馈-配送结束接口枚举
 */
public enum MesageLineEnum {

	consigneeCode("顾客代码", 1, 12), consigneeName("顾客姓名", 13, 52), mobilePhone("手机号", 53, 72), messageType("短信分类", 73, 74), // 21：订购出库
																															// 22：交换出库
																															// 23：回收
	messageContent("短信内容", 75, 574),

	EndLen("尾数位数", 1, 5) // 统计 总单量,5位数
	;

	private String lineName; // 列 名
	private int bIx; // 开始字符串位置
	private int eIx; // 结束字符串位置
	private int len; // 长度

	private MesageLineEnum(String lineName, int beginIndex, int endIndex) {
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

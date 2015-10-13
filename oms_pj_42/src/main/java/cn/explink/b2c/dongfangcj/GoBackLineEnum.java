package cn.explink.b2c.dongfangcj;

/**
 * 存储 txt文件位置的枚举
 * 
 * @author Administrator 按字符串索引截取 type 状态反馈-配送结束接口枚举
 */
public enum GoBackLineEnum {

	DataFlag("Data Flag", 1, 1), PaiXuBianHao("排序编号", 2, 9), HuoYunDanHao("货运单号", 10, 23), HuiShouShiJian("回收时间", 24, 37), HuiShouYuFou("回收与否", 38, 39), // 1.completed(完成)
																																							// 2.拒收/退货
	ShibaiYuanYin("失败原因", 40, 47), // 送货完成 ：00

	EndLen("尾数位数", 2, 9) // 统计 总单量,8位数
	;

	private String lineName; // 列 名
	private int bIx; // 开始字符串位置
	private int eIx; // 结束字符串位置
	private int len; // 长度

	private GoBackLineEnum(String lineName, int beginIndex, int endIndex) {
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

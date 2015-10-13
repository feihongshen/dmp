package cn.explink.b2c.dongfangcj;

/**
 * 存储 txt文件位置的枚举
 * 
 * @author Administrator 按字符串索引截取 type 状态反馈-配送结束接口枚举
 */
public enum DyLineEnum {

	DataFlag("Data Flag", 1, 1), PaiXuBianHao("排序编号", 2, 7), HuoYunDanHao("货运单号", 8, 17), SongHuoRi("送货日", 18, 25), PeiSongJieGuo("配送结果", 26, 26), // 1.completed(完成)
																																					// 2.拒收/退货
	YiChangYuanYin("拒收原因", 27, 28), // 送货完成 ：00
	FuKuanFangShi1("付款方式1", 29, 30), // 拒收/退货：00
	FuKuanJine1("付款金额1", 31, 42), // 拒收/退货：000000
	FuKuanFangShi2("付款方式2", 43, 44), // 拒收/退货：00
	FuKuanJine2("付款金额2", 45, 56), // 拒收/退货：000000

	EndLen("尾数位数", 1, 7) // 统计 总单量,6位数
	;

	private String lineName; // 列 名
	private int bIx; // 开始字符串位置
	private int eIx; // 结束字符串位置
	private int len; // 长度

	private DyLineEnum(String lineName, int beginIndex, int endIndex) {
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

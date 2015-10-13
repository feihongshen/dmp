package cn.explink.b2c.dongfangcj;

/**
 * 存储 txt文件位置的枚举
 * 
 * @author Administrator 按字符串索引截取 type 204接口枚举 外地POS
 */
public enum POSLineEnum {

	DataFlag("Data Flag", 1, 1), PaiXuBianHao("排序编号", 2, 7), HuoYunDanHao("货运单号", 8, 17), ZhiFuQuFen("支付区分", 18, 19), FuKuanJine("付款金额", 20, 31), ShouXuFei("手续费", 32, 37), terminal_no("终端号", 38, 47), card_bank(
			"银行卡", 48, 51), card_no("银行卡号", 52, 71), OK_DATE("完成日期", 72, 85), sys_no("系统编号", 86, 125), EndLen("尾数位数", 1, 7) // 统计
																															// 总单量,6位数
	;

	private String lineName; // 列 名
	private int bIx; // 开始字符串位置
	private int eIx; // 结束字符串位置
	private int len; // 长度

	private POSLineEnum(String lineName, int beginIndex, int endIndex) {
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

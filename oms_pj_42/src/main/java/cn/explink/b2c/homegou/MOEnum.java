package cn.explink.b2c.homegou;

/**
 * 存储 txt文件位置的枚举
 * 
 * @author Administrator 按字符串索引截取 type 204接口枚举 外地POS
 */
public enum MOEnum {

	Business_no("公司代码", 1, 15), terminal_no("终端编号", 16, 23), card_bank("银行代码", 24, 27), card_no("卡号", 28, 47), ok_date("授权时间", 48, 61), inamt_amt("授权金额", 62, 68), wb_no("运单号", 69, 80), batch_no(
			"batch号", 81, 86), sys_no("系统号", 87, 95), serial_no("serial号", 96, 106), inamt_date("系统号", 107, 114), type("授权区分", 115, 116),

	EndLen("尾数位数", 1, 7) // 统计 总单量,6位数
	;

	private String lineName; // 列 名
	private int bIx; // 开始字符串位置
	private int eIx; // 结束字符串位置
	private int len; // 长度

	private MOEnum(String lineName, int beginIndex, int endIndex) {
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

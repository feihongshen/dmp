package cn.explink.b2c.yangguang;

import cn.explink.enumutil.PaytypeEnum;

/**
 * 央广 出库区分
 * 
 * @author Administrator
 *
 */
public enum YgChuKuQuFenEnum {

	Cod("Cod货到付款", 1, PaytypeEnum.Xianjin.getValue()), MobilePos("POS刷卡", 2, PaytypeEnum.Pos.getValue()), OnlyDelivery("一般配送", 3, PaytypeEnum.Xianjin.getValue()), ReplacementDelivery("交换", 4,
			PaytypeEnum.Xianjin.getValue()), AndSoOn("其他", 5, PaytypeEnum.Xianjin.getValue()), ;

	public int getPaytype() {
		return paytype;
	}

	public void setPaytype(int paytype) {
		this.paytype = paytype;
	}

	private String text; // 列 名
	private int no; // 开始字符串位置
	private int paytype; // 对应的支付方式

	private YgChuKuQuFenEnum(String text, int no, int paytype) {
		this.text = text;
		this.no = no;
		this.paytype = paytype;
	}

	public static YgChuKuQuFenEnum getYgStatus(int no) {
		for (YgChuKuQuFenEnum ygenum : YgChuKuQuFenEnum.values()) {
			if (ygenum.getNo() == no) {
				return ygenum;
			}
		}
		return null;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public int getNo() {
		return no;
	}

	public void setNo(int no) {
		this.no = no;
	}

}

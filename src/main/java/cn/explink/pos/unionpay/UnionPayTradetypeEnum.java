package cn.explink.pos.unionpay;

public enum UnionPayTradetypeEnum {

	PosSuccess(00, "刷卡成功"), partOfPosSuccess(01, "刷卡部分成功"), CashSuccess(30, "现金成功"), partOfCashSuccess(31, "现金部分成功"), chequeSuccess(60, "支票成功"), JuShou(20, "拒收"), YiChang(40, "异常-滞留"), BackOutSuccess(
			70, "快递单撤销成功");

	private int type;
	private String decribe;

	private UnionPayTradetypeEnum(int key, String text) {
		this.type = key;
		this.decribe = text;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getDecribe() {
		return decribe;
	}

	public void setDecribe(String decribe) {
		this.decribe = decribe;
	}

}

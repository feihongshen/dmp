package cn.explink.enumutil;

public enum StockDetailStocktypeEnum {

	YiBanKuCun(1, "一般库存"), LingHuoKuCun(2, "领货库存");
	private int value;
	private String text;

	private StockDetailStocktypeEnum(int value, String text) {
		this.value = value;
		this.text = text;
	}

	public int getValue() {
		return value;
	}

	public String getText() {
		return text;
	}
}

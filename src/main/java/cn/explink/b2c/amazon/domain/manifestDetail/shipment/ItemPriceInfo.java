package cn.explink.b2c.amazon.domain.manifestDetail.shipment;

import javax.xml.bind.annotation.XmlElement;

public class ItemPriceInfo {
	private ItemUnitPrice itemUnitPrice;
	private ItemExtendedPrice itemExtendedPrice;

	@XmlElement(name = "itemUnitPrice")
	public ItemUnitPrice getItemUnitPrice() {
		return itemUnitPrice;
	}

	public void setItemUnitPrice(ItemUnitPrice itemUnitPrice) {
		this.itemUnitPrice = itemUnitPrice;
	}

	@XmlElement(name = "itemExtendedPrice")
	public ItemExtendedPrice getItemExtendedPrice() {
		return itemExtendedPrice;
	}

	public void setItemExtendedPrice(ItemExtendedPrice itemExtendedPrice) {
		this.itemExtendedPrice = itemExtendedPrice;
	}

}

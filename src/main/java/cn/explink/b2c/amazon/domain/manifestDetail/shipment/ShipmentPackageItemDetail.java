package cn.explink.b2c.amazon.domain.manifestDetail.shipment;

import javax.xml.bind.annotation.XmlElement;

public class ShipmentPackageItemDetail {
	private String orderingOrderId;// 条目订单号
	private String encryptedOrderingShipmentId;// XXXX
	private String itemID;// 条目代码
	private String itemTitle;// 条目名称
	private ItemQuantity itemQuantity;// 数量
	private ItemPriceInfo itemPriceInfo;// 条目价格信息
	private ItemWeight itemWeight;// 条目重量
	private String harmonizedTariffNumber;// 条目重量
	private String harmonizedTariffDescription;// 管制分类
	private String countryOfOrigin;// 国家
	private String eCCN;//
	// 很重要，是判断退换货
	// WarrantyReplacement，ChargedReplacement，FreeReplacement都是换相同件所以ReplacementType节点里的字符串只要包含Replacement，就认为是换相同件
	private String replacementType;

	@XmlElement(name = "orderingOrderId")
	public String getOrderingOrderId() {
		return orderingOrderId;
	}

	public void setOrderingOrderId(String orderingOrderId) {
		this.orderingOrderId = orderingOrderId;
	}

	@XmlElement(name = "encryptedOrderingShipmentId")
	public String getEncryptedOrderingShipmentId() {
		return encryptedOrderingShipmentId;
	}

	public void setEncryptedOrderingShipmentId(String encryptedOrderingShipmentId) {
		this.encryptedOrderingShipmentId = encryptedOrderingShipmentId;
	}

	@XmlElement(name = "itemID")
	public String getItemID() {
		return itemID;
	}

	public void setItemID(String itemID) {
		this.itemID = itemID;
	}

	@XmlElement(name = "itemTitle")
	public String getItemTitle() {
		return itemTitle;
	}

	public void setItemTitle(String itemTitle) {
		this.itemTitle = itemTitle;
	}

	@XmlElement(name = "itemQuantity")
	public ItemQuantity getItemQuantity() {
		return itemQuantity;
	}

	public void setItemQuantity(ItemQuantity itemQuantity) {
		this.itemQuantity = itemQuantity;
	}

	@XmlElement(name = "itemPriceInfo")
	public ItemPriceInfo getItemPriceInfo() {
		return itemPriceInfo;
	}

	public void setItemPriceInfo(ItemPriceInfo itemPriceInfo) {
		this.itemPriceInfo = itemPriceInfo;
	}

	@XmlElement(name = "itemWeight")
	public ItemWeight getItemWeight() {
		return itemWeight;
	}

	public void setItemWeight(ItemWeight itemWeight) {
		this.itemWeight = itemWeight;
	}

	@XmlElement(name = "harmonizedTariffNumber")
	public String getHarmonizedTariffNumber() {
		return harmonizedTariffNumber;
	}

	public void setHarmonizedTariffNumber(String harmonizedTariffNumber) {
		this.harmonizedTariffNumber = harmonizedTariffNumber;
	}

	@XmlElement(name = "harmonizedTariffDescription")
	public String getHarmonizedTariffDescription() {
		return harmonizedTariffDescription;
	}

	public void setHarmonizedTariffDescription(String harmonizedTariffDescription) {
		this.harmonizedTariffDescription = harmonizedTariffDescription;
	}

	@XmlElement(name = "countryOfOrigin")
	public String getCountryOfOrigin() {
		return countryOfOrigin;
	}

	public void setCountryOfOrigin(String countryOfOrigin) {
		this.countryOfOrigin = countryOfOrigin;
	}

	@XmlElement(name = "ECCN")
	public String getECCN() {
		return eCCN;
	}

	public void setECCN(String eCCN) {
		this.eCCN = eCCN;
	}

	@XmlElement(name = "ReplacementType")
	public String getReplacementType() {
		return replacementType;
	}

	public void setReplacementType(String replacementType) {
		this.replacementType = replacementType;
	}

}

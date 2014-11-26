package cn.explink.b2c.amazon.domain.header;

import javax.xml.bind.annotation.XmlElement;

public class ManifestHeader {
	private String warehouseLocationID;// 发货库房
	private String manifestCreateDateTime;// 发货单创建时间
	private String carrierInternalID;// 配送公司id
	private ShipmentMethod shipmentMethod;// 发货方法
	private String manifestNumber;// 发货单号
	private String carrierAccountID;// 配送公司ID
	private String carrierManifestID;// 配送方发货单号
	private String shipmentDate;// 预计发货时间
	private String currencyCode;// 货币代码
	private ShipFromAddress shipFromAddress;// 出货地信息
	private ShipperInformation shipperInformation;// 出货人信息
	private String prepaidCollect;// 亚马逊付款方式, 亚马逊如何支付配送公司

	@XmlElement(name = "warehouseLocationID")
	public String getWarehouseLocationID() {
		return warehouseLocationID;
	}

	public void setWarehouseLocationID(String warehouseLocationID) {
		this.warehouseLocationID = warehouseLocationID;
	}

	@XmlElement(name = "manifestCreateDateTime")
	public String getManifestCreateDateTime() {
		return manifestCreateDateTime;
	}

	public void setManifestCreateDateTime(String manifestCreateDateTime) {
		this.manifestCreateDateTime = manifestCreateDateTime;
	}

	@XmlElement(name = "carrierInternalID")
	public String getCarrierInternalID() {
		return carrierInternalID;
	}

	public void setCarrierInternalID(String carrierInternalID) {
		this.carrierInternalID = carrierInternalID;
	}

	@XmlElement(name = "shipmentMethod")
	public ShipmentMethod getShipmentMethod() {
		return shipmentMethod;
	}

	public void setShipmentMethod(ShipmentMethod shipmentMethod) {
		this.shipmentMethod = shipmentMethod;
	}

	@XmlElement(name = "manifestNumber")
	public String getManifestNumber() {
		return manifestNumber;
	}

	public void setManifestNumber(String manifestNumber) {
		this.manifestNumber = manifestNumber;
	}

	@XmlElement(name = "carrierAccountID")
	public String getCarrierAccountID() {
		return carrierAccountID;
	}

	public void setCarrierAccountID(String carrierAccountID) {
		this.carrierAccountID = carrierAccountID;
	}

	@XmlElement(name = "carrierManifestID")
	public String getCarrierManifestID() {
		return carrierManifestID;
	}

	public void setCarrierManifestID(String carrierManifestID) {
		this.carrierManifestID = carrierManifestID;
	}

	@XmlElement(name = "shipmentDate")
	public String getShipmentDate() {
		return shipmentDate;
	}

	public void setShipmentDate(String shipmentDate) {
		this.shipmentDate = shipmentDate;
	}

	@XmlElement(name = "currencyCode")
	public String getCurrencyCode() {
		return currencyCode;
	}

	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}

	@XmlElement(name = "shipFromAddress")
	public ShipFromAddress getShipFromAddress() {
		return shipFromAddress;
	}

	public void setShipFromAddress(ShipFromAddress shipFromAddress) {
		this.shipFromAddress = shipFromAddress;
	}

	@XmlElement(name = "shipperInformation")
	public ShipperInformation getShipperInformation() {
		return shipperInformation;
	}

	public void setShipperInformation(ShipperInformation shipperInformation) {
		this.shipperInformation = shipperInformation;
	}

	@XmlElement(name = "prepaidCollect")
	public String getPrepaidCollect() {
		return prepaidCollect;
	}

	public void setPrepaidCollect(String prepaidCollect) {
		this.prepaidCollect = prepaidCollect;
	}

}

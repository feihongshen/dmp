package cn.explink.b2c.amazon.domain.manifestDetail;

import javax.xml.bind.annotation.XmlElement;

public class ShipmentDetail {
	private String customerOrderNumber;// 委托方订单号
	private ConsigneeAddress consigneeAddress;// 收货人地址
	private ShipmentCostInfo shipmentCostInfo;// 货品金额信息
	private ShipmentPackageInfo shipmentPackageInfo;// 包裹信息
	private String reasonForExport;// 出口原因
	private String isFreeReplacementExchangeFlag;// 是否为替换货
	private String isExportChargePrepaid;
	private String addressId;
	private String preferredDeliveryTime;

	@XmlElement(name = "customerOrderNumber")
	public String getCustomerOrderNumber() {
		return customerOrderNumber;
	}

	public void setCustomerOrderNumber(String customerOrderNumber) {
		this.customerOrderNumber = customerOrderNumber;
	}

	@XmlElement(name = "consigneeAddress")
	public ConsigneeAddress getConsigneeAddress() {
		return consigneeAddress;
	}

	public void setConsigneeAddress(ConsigneeAddress consigneeAddress) {
		this.consigneeAddress = consigneeAddress;
	}

	@XmlElement(name = "shipmentCostInfo")
	public ShipmentCostInfo getShipmentCostInfo() {
		return shipmentCostInfo;
	}

	public void setShipmentCostInfo(ShipmentCostInfo shipmentCostInfo) {
		this.shipmentCostInfo = shipmentCostInfo;
	}

	@XmlElement(name = "shipmentPackageInfo")
	public ShipmentPackageInfo getShipmentPackageInfo() {
		return shipmentPackageInfo;
	}

	public void setShipmentPackageInfo(ShipmentPackageInfo shipmentPackageInfo) {
		this.shipmentPackageInfo = shipmentPackageInfo;
	}

	@XmlElement(name = "reasonForExport")
	public String getReasonForExport() {
		return reasonForExport;
	}

	public void setReasonForExport(String reasonForExport) {
		this.reasonForExport = reasonForExport;
	}

	@XmlElement(name = "isFreeReplacementExchangeFlag")
	public String getIsFreeReplacementExchangeFlag() {
		return isFreeReplacementExchangeFlag;
	}

	public void setIsFreeReplacementExchangeFlag(String isFreeReplacementExchangeFlag) {
		this.isFreeReplacementExchangeFlag = isFreeReplacementExchangeFlag;
	}

	@XmlElement(name = "isExportChargePrepaid")
	public String getIsExportChargePrepaid() {
		return isExportChargePrepaid;
	}

	public void setIsExportChargePrepaid(String isExportChargePrepaid) {
		this.isExportChargePrepaid = isExportChargePrepaid;
	}

	@XmlElement(name = "addressId")
	public String getAddressId() {
		return addressId;
	}

	public void setAddressId(String addressId) {
		this.addressId = addressId;
	}

	@XmlElement(name = "preferredDeliveryTime")
	public String getPreferredDeliveryTime() {
		return preferredDeliveryTime;
	}

	public void setPreferredDeliveryTime(String preferredDeliveryTime) {
		this.preferredDeliveryTime = preferredDeliveryTime;
	}

}

package cn.explink.b2c.amazon.domain.manifestDetail;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;

import cn.explink.b2c.amazon.domain.manifestDetail.shipment.CartonID;
import cn.explink.b2c.amazon.domain.manifestDetail.shipment.PackageShipmentMethod;
import cn.explink.b2c.amazon.domain.manifestDetail.shipment.ShipmentPackageActualGrossWeight;
import cn.explink.b2c.amazon.domain.manifestDetail.shipment.ShipmentPackageDeclaredGrossWeight;
import cn.explink.b2c.amazon.domain.manifestDetail.shipment.ShipmentPackageDimensions;
import cn.explink.b2c.amazon.domain.manifestDetail.shipment.ShipmentPackageItemDetail;
import cn.explink.b2c.amazon.domain.manifestDetail.shipment.ShipmentPackageItemQuantity;

public class ShipmentPackageInfo {
	private CartonID cartonID;// 包裹盒信息
	private PackageShipmentMethod packageShipmentMethod;// 包裹发货方法
	private String shipZone;// zone代码
	private String shipSort;// sort代码
	private String commercialInvoiceDate;// 商业发票创建时间
	private String scheduledDeliveryDate;// XXX时间
	private ShipmentPackageDeclaredGrossWeight shipmentPackageDeclaredGrossWeight;// 预计送达时间
	private String shipmentPackageDimWtCalcMethod;// 货品声报重量信息
	private ShipmentPackageActualGrossWeight shipmentPackageActualGrossWeight;// 货品实际重量信息
	private ShipmentPackageDimensions shipmentPackageDimensions;// 包裹尺寸
	private List<ShipmentPackageItemDetail> shipmentPackageItemDetail;// 包裹条目信息
	private String totalDeclaredValue;// 总金额
	private String pkgHarmonizedTariffDescription;// 包裹描述, 出关用
	private ShipmentPackageItemQuantity shipmentPackageItemQuantity;// 包裹数量

	@XmlElement(name = "cartonID")
	public CartonID getCartonID() {
		return cartonID;
	}

	public void setCartonID(CartonID cartonID) {
		this.cartonID = cartonID;
	}

	@XmlElement(name = "packageShipmentMethod")
	public PackageShipmentMethod getPackageShipmentMethod() {
		return packageShipmentMethod;
	}

	public void setPackageShipmentMethod(PackageShipmentMethod packageShipmentMethod) {
		this.packageShipmentMethod = packageShipmentMethod;
	}

	@XmlElement(name = "shipZone")
	public String getShipZone() {
		return shipZone;
	}

	public void setShipZone(String shipZone) {
		this.shipZone = shipZone;
	}

	@XmlElement(name = "shipSort")
	public String getShipSort() {
		return shipSort;
	}

	public void setShipSort(String shipSort) {
		this.shipSort = shipSort;
	}

	@XmlElement(name = "commercialInvoiceDate")
	public String getCommercialInvoiceDate() {
		return commercialInvoiceDate;
	}

	public void setCommercialInvoiceDate(String commercialInvoiceDate) {
		this.commercialInvoiceDate = commercialInvoiceDate;
	}

	@XmlElement(name = "scheduledDeliveryDate")
	public String getScheduledDeliveryDate() {
		return scheduledDeliveryDate;
	}

	public void setScheduledDeliveryDate(String scheduledDeliveryDate) {
		this.scheduledDeliveryDate = scheduledDeliveryDate;
	}

	@XmlElement(name = "shipmentPackageDeclaredGrossWeight")
	public ShipmentPackageDeclaredGrossWeight getShipmentPackageDeclaredGrossWeight() {
		return shipmentPackageDeclaredGrossWeight;
	}

	public void setShipmentPackageDeclaredGrossWeight(ShipmentPackageDeclaredGrossWeight shipmentPackageDeclaredGrossWeight) {
		this.shipmentPackageDeclaredGrossWeight = shipmentPackageDeclaredGrossWeight;
	}

	@XmlElement(name = "shipmentPackageDimWtCalcMethod")
	public String getShipmentPackageDimWtCalcMethod() {
		return shipmentPackageDimWtCalcMethod;
	}

	public void setShipmentPackageDimWtCalcMethod(String shipmentPackageDimWtCalcMethod) {
		this.shipmentPackageDimWtCalcMethod = shipmentPackageDimWtCalcMethod;
	}

	@XmlElement(name = "shipmentPackageActualGrossWeight")
	public ShipmentPackageActualGrossWeight getShipmentPackageActualGrossWeight() {
		return shipmentPackageActualGrossWeight;
	}

	public void setShipmentPackageActualGrossWeight(ShipmentPackageActualGrossWeight shipmentPackageActualGrossWeight) {
		this.shipmentPackageActualGrossWeight = shipmentPackageActualGrossWeight;
	}

	@XmlElement(name = "shipmentPackageDimensions")
	public ShipmentPackageDimensions getShipmentPackageDimensions() {
		return shipmentPackageDimensions;
	}

	public void setShipmentPackageDimensions(ShipmentPackageDimensions shipmentPackageDimensions) {
		this.shipmentPackageDimensions = shipmentPackageDimensions;
	}

	@XmlElement(name = "shipmentPackageItemDetail")
	public List<ShipmentPackageItemDetail> getShipmentPackageItemDetail() {
		return shipmentPackageItemDetail;
	}

	public void setShipmentPackageItemDetail(List<ShipmentPackageItemDetail> shipmentPackageItemDetail) {
		this.shipmentPackageItemDetail = shipmentPackageItemDetail;
	}

	@XmlElement(name = "totalDeclaredValue")
	public String getTotalDeclaredValue() {
		return totalDeclaredValue;
	}

	public void setTotalDeclaredValue(String totalDeclaredValue) {
		this.totalDeclaredValue = totalDeclaredValue;
	}

	@XmlElement(name = "pkgHarmonizedTariffDescription")
	public String getPkgHarmonizedTariffDescription() {
		return pkgHarmonizedTariffDescription;
	}

	public void setPkgHarmonizedTariffDescription(String pkgHarmonizedTariffDescription) {
		this.pkgHarmonizedTariffDescription = pkgHarmonizedTariffDescription;
	}

	@XmlElement(name = "shipmentPackageItemQuantity")
	public ShipmentPackageItemQuantity getShipmentPackageItemQuantity() {
		return shipmentPackageItemQuantity;
	}

	public void setShipmentPackageItemQuantity(ShipmentPackageItemQuantity shipmentPackageItemQuantity) {
		this.shipmentPackageItemQuantity = shipmentPackageItemQuantity;
	}

}

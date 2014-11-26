package cn.explink.b2c.amazon.domain.manifestDetail.shipment;

import javax.xml.bind.annotation.XmlElement;

public class CartonID {
	private String encryptedShipmentID;//
	private String packageID;//
	private String trackingID;//

	@XmlElement(name = "encryptedShipmentID")
	public String getEncryptedShipmentID() {
		return encryptedShipmentID;
	}

	public void setEncryptedShipmentID(String encryptedShipmentID) {
		this.encryptedShipmentID = encryptedShipmentID;
	}

	@XmlElement(name = "packageID")
	public String getPackageID() {
		return packageID;
	}

	public void setPackageID(String packageID) {
		this.packageID = packageID;
	}

	@XmlElement(name = "trackingID")
	public String getTrackingID() {
		return trackingID;
	}

	public void setTrackingID(String trackingID) {
		this.trackingID = trackingID;
	}

}

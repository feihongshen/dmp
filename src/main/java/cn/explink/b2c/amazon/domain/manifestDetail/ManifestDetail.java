package cn.explink.b2c.amazon.domain.manifestDetail;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;

public class ManifestDetail {
	private List<ShipmentDetail> shipmentDetail;// 发货条目明细(每个货品循环shipmentDetail)

	@XmlElement(name = "shipmentDetail")
	public List<ShipmentDetail> getShipmentDetail() {
		return shipmentDetail;
	}

	public void setShipmentDetail(List<ShipmentDetail> shipmentDetail) {
		this.shipmentDetail = shipmentDetail;
	}

}

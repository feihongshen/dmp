package cn.explink.b2c.amazon.domain.manifestSummary;

import javax.xml.bind.annotation.XmlElement;

/**
 * 发货单汇总
 * 
 * @author Administrator
 *
 */
public class ManifestSummary {
	private TotalShipmentQuantity totalShipmentQuantity;// 总数量
	private TotalShipmentValue totalShipmentValue;// 包裹总金额
	private TotalDeclaredGrossWeight totalDeclaredGrossWeight;// 包裹声报重量信息
	private TotalActualGrossWeight totalActualGrossWeight;// 包裹实际重量信息

	@XmlElement(name = "totalShipmentQuantity")
	public TotalShipmentQuantity getTotalShipmentQuantity() {
		return totalShipmentQuantity;
	}

	public void setTotalShipmentQuantity(TotalShipmentQuantity totalShipmentQuantity) {
		this.totalShipmentQuantity = totalShipmentQuantity;
	}

	@XmlElement(name = "totalShipmentValue")
	public TotalShipmentValue getTotalShipmentValue() {
		return totalShipmentValue;
	}

	public void setTotalShipmentValue(TotalShipmentValue totalShipmentValue) {
		this.totalShipmentValue = totalShipmentValue;
	}

	@XmlElement(name = "totalDeclaredGrossWeight")
	public TotalDeclaredGrossWeight getTotalDeclaredGrossWeight() {
		return totalDeclaredGrossWeight;
	}

	public void setTotalDeclaredGrossWeight(TotalDeclaredGrossWeight totalDeclaredGrossWeight) {
		this.totalDeclaredGrossWeight = totalDeclaredGrossWeight;
	}

	@XmlElement(name = "totalActualGrossWeight")
	public TotalActualGrossWeight getTotalActualGrossWeight() {
		return totalActualGrossWeight;
	}

	public void setTotalActualGrossWeight(TotalActualGrossWeight totalActualGrossWeight) {
		this.totalActualGrossWeight = totalActualGrossWeight;
	}

}

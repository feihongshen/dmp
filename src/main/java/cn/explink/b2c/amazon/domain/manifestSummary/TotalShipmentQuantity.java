package cn.explink.b2c.amazon.domain.manifestSummary;

import javax.xml.bind.annotation.XmlElement;

public class TotalShipmentQuantity {
	private String quantity;

	@XmlElement(name = "quantity")
	public String getQuantity() {
		return quantity;
	}

	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}

}

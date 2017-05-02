package cn.explink.b2c.liantongordercenter.xmlDto;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "Cargo")
public class CargoDto {
	private String name;// 货物名称
	private String count;// 货物数量
	private int unit;// 货物数量单位：个、件、台
	private String weight;// 货物重量，单位千克
	private String amount;// 货物单价
	private String currency;// 货物单价货币

	@XmlAttribute(name = "name")
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@XmlAttribute(name = "count")
	public String getCount() {
		return this.count;
	}

	public void setCount(String count) {
		this.count = count;
	}

	@XmlAttribute(name = "unit")
	public int getUnit() {
		return this.unit;
	}

	public void setUnit(int unit) {
		this.unit = unit;
	}

	@XmlAttribute(name = "weight")
	public String getWeight() {
		return this.weight;
	}

	public void setWeight(String weight) {
		this.weight = weight;
	}

	@XmlAttribute(name = "amount")
	public String getAmount() {
		return this.amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	@XmlAttribute(name = "currency")
	public String getCurrency() {
		return this.currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

}

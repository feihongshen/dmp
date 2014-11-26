package cn.explink.b2c.gome.domain;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;

public class ItemLineVO {

	private List<ItemLine> itemLine;

	@XmlElement(name = "itemLine")
	public List<ItemLine> getItemLine() {
		return itemLine;
	}

	public void setItemLine(List<ItemLine> itemLine) {
		this.itemLine = itemLine;
	}

}

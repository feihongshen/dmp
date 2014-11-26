package cn.explink.b2c.amazon.domain;

import javax.xml.bind.annotation.XmlElement;

public class Message {

	private AmazonManifest amazonManifest;// 亚马逊发货单

	@XmlElement(name = "amazonManifest")
	public AmazonManifest getAmazonManifest() {
		return amazonManifest;
	}

	public void setAmazonManifest(AmazonManifest amazonManifest) {
		this.amazonManifest = amazonManifest;
	}

}

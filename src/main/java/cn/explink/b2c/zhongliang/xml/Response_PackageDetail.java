package cn.explink.b2c.zhongliang.xml;

import javax.xml.bind.annotation.XmlElement;

public class Response_PackageDetail {
	private String PackageID;

	@XmlElement(name = "PackageID")
	public String getPackageID() {
		return PackageID;
	}

	public void setPackageID(String packageID) {
		PackageID = packageID;
	}
}

package cn.explink.b2c.zhongliang.xml;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;

public class Response_PackageDetail {
	private List<String> PackageID = new ArrayList<String>();

	@XmlElement(name = "PackageID")
	public List<String> getPackageID() {
		Collections.sort(this.PackageID);
		return this.PackageID;
	}

	public void setPackageID(List<String> packageID) {
		this.PackageID = packageID;
	}
}

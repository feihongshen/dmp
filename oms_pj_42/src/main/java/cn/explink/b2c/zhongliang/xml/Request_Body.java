package cn.explink.b2c.zhongliang.xml;

import javax.xml.bind.annotation.XmlElement;

public class Request_Body {
	private Request_UpdateStatus updateStatus = new Request_UpdateStatus();

	@XmlElement(name = "UpdateStatus")
	public Request_UpdateStatus getUpdateStatus() {
		return updateStatus;
	}

	public void setUpdateStatus(Request_UpdateStatus updateStatus) {
		this.updateStatus = updateStatus;
	}
}

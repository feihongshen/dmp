package cn.explink.b2c.zhongliang.xml;

import javax.xml.bind.annotation.XmlElement;

public class Response_CancleOrder_Order {
private String SendOrderID;
private String Remark;
@XmlElement(name = "SendOrderID")
public String getSendOrderID() {
	return SendOrderID;
}
public void setSendOrderID(String sendOrderID) {
	SendOrderID = sendOrderID;
}
@XmlElement(name = "Remark")
public String getRemark() {
	return Remark;
}
public void setRemark(String remark) {
	Remark = remark;
}
}

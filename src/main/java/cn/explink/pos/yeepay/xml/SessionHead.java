package cn.explink.pos.yeepay.xml;

import javax.xml.bind.annotation.XmlElement;

public class SessionHead {

	String version;
	String serviceCode;
	String transactionID;
	String srcSysID;

	String reqTime;
	ExtendAtt extendAtt;
	String hMAC;
	String dstSysID;

	@XmlElement(name = "Version")
	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	@XmlElement(name = "ServiceCode")
	public String getServiceCode() {
		return serviceCode;
	}

	public void setServiceCode(String serviceCode) {
		this.serviceCode = serviceCode;
	}

	@XmlElement(name = "TransactionID")
	public String getTransactionID() {
		return transactionID;
	}

	public void setTransactionID(String transactionID) {
		this.transactionID = transactionID;
	}

	@XmlElement(name = "SrcSysID")
	public String getSrcSysID() {
		return srcSysID;
	}

	public void setSrcSysID(String srcSysID) {
		this.srcSysID = srcSysID;
	}

	@XmlElement(name = "DstSysID")
	public String getDstSysID() {
		return dstSysID;
	}

	public void setDstSysID(String dstSysID) {
		this.dstSysID = dstSysID;
	}

	@XmlElement(name = "ReqTime")
	public String getReqTime() {
		return reqTime;
	}

	public void setReqTime(String reqTime) {
		this.reqTime = reqTime;
	}

	@XmlElement(name = "ExtendAtt")
	public ExtendAtt getExtendAtt() {
		return extendAtt;
	}

	public void setExtendAtt(ExtendAtt extendAtt) {
		this.extendAtt = extendAtt;
	}

	@XmlElement(name = "HMAC")
	public String gethMAC() {
		return hMAC;
	}

	public void sethMAC(String hMAC) {
		this.hMAC = hMAC;
	}

}

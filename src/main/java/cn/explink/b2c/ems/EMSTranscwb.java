package cn.explink.b2c.ems;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 获取dmp与ems运单对照关系报文对象
 * @author huan.zhou
 */
@XmlRootElement(name = "response")
public class EMSTranscwb {
    private String result;
    private String errorDesc;
    private String errorCode;
    private EmsAndDmpTranscwb qryData;
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	@XmlElement(name = "errorDesc")
	public String getErrorDesc() {
		return errorDesc;
	}
	public void setErrorDesc(String errorDesc) {
		this.errorDesc = errorDesc;
	}
	@XmlElement(name = "errorCode")
	public String getErrorCode() {
		return errorCode;
	}
	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}
	@XmlElement(name = "qryData")
	public EmsAndDmpTranscwb getQryData() {
		return qryData;
	}
	public void setQryData(EmsAndDmpTranscwb qryData) {
		this.qryData = qryData;
	}
    
}

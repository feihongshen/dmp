package cn.explink.b2c.ems;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * EMS订单下发返回异常信息对象
 * @author huan.zhou
 */
@XmlRootElement(name = "response")
public class EMSOrderResultBack {
    private String result;
    private String errorDesc;
    private String errorCode;
    private List<ErrorDetail> errorDetail;
    
    @XmlElement(name = "result")
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
	public List<ErrorDetail> getErrorDetail() {
		return errorDetail;
	}
	public void setErrorDetail(List<ErrorDetail> errorDetail) {
		this.errorDetail = errorDetail;
	}
	
}

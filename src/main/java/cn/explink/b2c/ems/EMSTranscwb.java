package cn.explink.b2c.ems;

/**
 * 获取dmp与ems运单对照关系报文对象
 * @author huan.zhou
 */
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
	public String getErrorDesc() {
		return errorDesc;
	}
	public void setErrorDesc(String errorDesc) {
		this.errorDesc = errorDesc;
	}
	public String getErrorCode() {
		return errorCode;
	}
	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}
	public EmsAndDmpTranscwb getQryData() {
		return qryData;
	}
	public void setQryData(EmsAndDmpTranscwb qryData) {
		this.qryData = qryData;
	}
    
}

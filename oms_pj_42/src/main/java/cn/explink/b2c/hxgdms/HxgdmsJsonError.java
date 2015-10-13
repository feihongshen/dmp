package cn.explink.b2c.hxgdms;

import org.codehaus.jackson.annotate.JsonProperty;

/**
 * 
 * @author Administrator 异常状态反馈bean 包括:拒收、货物丢失
 */
public class HxgdmsJsonError {
	@JsonProperty(value = "WorkCode")
	private String workCode; // 订单号cwb
	@JsonProperty(value = "ErrorCode")
	private String errorCode; // 异常编码
	@JsonProperty(value = "ErrorNote")
	private String errorNote; // 异常备注
	@JsonProperty(value = "DelveryCode")
	private String delveryCode; // 承运商编码

	public String getWorkCode() {
		return workCode;
	}

	public void setWorkCode(String workCode) {
		this.workCode = workCode;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public String getErrorNote() {
		return errorNote;
	}

	public void setErrorNote(String errorNote) {
		this.errorNote = errorNote;
	}

	public String getDelveryCode() {
		return delveryCode;
	}

	public void setDelveryCode(String delveryCode) {
		this.delveryCode = delveryCode;
	}

}

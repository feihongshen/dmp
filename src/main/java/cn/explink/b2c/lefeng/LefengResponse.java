package cn.explink.b2c.lefeng;

/**
 * 乐蜂网返回实体类
 * 
 * @author Administrator
 *
 */
public class LefengResponse {

	private String errorMessage;
	private String responseStatus;
	private result result;

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public String getResponseStatus() {
		return responseStatus;
	}

	public void setResponseStatus(String responseStatus) {
		this.responseStatus = responseStatus;
	}

	public result getResult() {
		return result;
	}

	public void setResult(result result) {
		this.result = result;
	}
}

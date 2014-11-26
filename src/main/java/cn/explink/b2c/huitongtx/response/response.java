package cn.explink.b2c.huitongtx.response;

import java.util.List;

public class response {

	private String code;
	private String message;
	private List<datadetail> data;

	public List<datadetail> getData() {
		return data;
	}

	public void setData(List<datadetail> data) {
		this.data = data;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}

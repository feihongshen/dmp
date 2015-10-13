package cn.explink.b2c.huitongtx.response;

import java.util.List;

public class response {

	private int code;
	private String message;
	private List<datadetail> data;

	public List<datadetail> getData() {
		return data;
	}

	public void setData(List<datadetail> data) {
		this.data = data;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}

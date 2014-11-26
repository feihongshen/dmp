package cn.explink.b2c.maikaolin.xml;

import javax.xml.bind.annotation.XmlElement;

public class Request_Header {
	String user_id;// 请求的用户ID
	String user_key;// Y 请求的用户key
	String method;// Y 请求的方法名称
	String response_time;// Y 请求时间

	@XmlElement(name = "user_id")
	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	@XmlElement(name = "user_key")
	public String getUser_key() {
		return user_key;
	}

	public void setUser_key(String user_key) {
		this.user_key = user_key;
	}

	@XmlElement(name = "method")
	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	@XmlElement(name = "response_time")
	public String getResponse_time() {
		return response_time;
	}

	public void setResponse_time(String response_time) {
		this.response_time = response_time;
	}

}

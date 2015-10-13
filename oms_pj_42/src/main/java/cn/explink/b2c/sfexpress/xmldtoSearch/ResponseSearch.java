package cn.explink.b2c.sfexpress.xmldtoSearch;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 下单至顺丰返回的bean
 * 
 * @author Administrator
 *
 */
@XmlRootElement(name = "Response")
public class ResponseSearch {

	private String head;// 头信息
	private Body body; // 报文体
	private String error; // 错误信息节点

	@XmlElement(name = "ERROR")
	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	@XmlElement(name = "Head")
	public String getHead() {
		return head;
	}

	public void setHead(String head) {
		this.head = head;
	}

	@XmlElement(name = "Body")
	public Body getBody() {
		return body;
	}

	public void setBody(Body body) {
		this.body = body;
	}

}

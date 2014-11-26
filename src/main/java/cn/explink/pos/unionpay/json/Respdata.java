package cn.explink.pos.unionpay.json;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

/**
 * 银联返回JSON报文
 * 
 * @author Administrator
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Respdata {

	private String response;// 应答码00-成功，其它失败
	private String message; // 应答消息
	private int totalrecord; // 总记录数

	private RespContent respContent;

	public String getResponse() {
		return response;
	}

	public void setResponse(String response) {
		this.response = response;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public int getTotalrecord() {
		return totalrecord;
	}

	public void setTotalrecord(int totalrecord) {
		this.totalrecord = totalrecord;
	}

	public RespContent getRespContent() {
		return respContent;
	}

	public void setRespContent(RespContent respContent) {
		this.respContent = respContent;
	}

}

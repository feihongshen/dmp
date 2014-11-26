package cn.explink.b2c.vipshop.addresspre.response;

import java.util.List;

public class Response {
	private String msg;// 返回消息头说明
	private List<ResponseItem> responseItems;// 消息列表

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public List<ResponseItem> getItems() {
		return responseItems;
	}

	public void setItems(List<ResponseItem> responseItems) {
		this.responseItems = responseItems;
	}

}

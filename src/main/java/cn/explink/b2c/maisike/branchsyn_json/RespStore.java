package cn.explink.b2c.maisike.branchsyn_json;

import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;

public class RespStore {
	@JsonProperty(value = "Code")
	private String code;
	@JsonProperty(value = "Msg")
	private String msg;
	@JsonProperty(value = "Stores")
	private List<Stores> stores;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public List<Stores> getStores() {
		return stores;
	}

	public void setStores(List<Stores> stores) {
		this.stores = stores;
	}
}

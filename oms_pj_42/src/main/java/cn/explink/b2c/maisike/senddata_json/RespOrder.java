package cn.explink.b2c.maisike.senddata_json;

import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;

public class RespOrder {

	@JsonProperty(value = "Code")
	private String code;
	@JsonProperty(value = "Msg")
	private String msg;

	@JsonProperty(value = "OSNList")
	private List<String> osnlist;
	@JsonProperty(value = "Err_OSNList")
	private List<String> err_osnlist;

	public List<String> getErr_osnlist() {
		return err_osnlist;
	}

	public void setErr_osnlist(List<String> err_osnlist) {
		this.err_osnlist = err_osnlist;
	}

	public String getCode() {
		return code;
	}

	public List<String> getOsnlist() {
		return osnlist;
	}

	public void setOsnlist(List<String> osnlist) {
		this.osnlist = osnlist;
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

}

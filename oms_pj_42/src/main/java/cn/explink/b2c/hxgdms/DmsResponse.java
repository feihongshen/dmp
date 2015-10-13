package cn.explink.b2c.hxgdms;

import org.codehaus.jackson.annotate.JsonProperty;

public class DmsResponse {

	@JsonProperty(value = "Rtn_Code")
	private String rtn_Code;
	@JsonProperty(value = "Rtn_Msg")
	private String rtn_Msg;

	public String getRtn_Code() {
		return rtn_Code;
	}

	public void setRtn_Code(String rtn_Code) {
		this.rtn_Code = rtn_Code;
	}

	public String getRtn_Msg() {
		return rtn_Msg;
	}

	public void setRtn_Msg(String rtn_Msg) {
		this.rtn_Msg = rtn_Msg;
	}

}

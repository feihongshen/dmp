package cn.explink.b2c.feiniuwang;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Responseitems {
	@JsonProperty(value = "txlogisticid")
	private String txlogisticid;
	@JsonProperty(value = "mailno")
	private String mailno;
	@JsonProperty(value = "success")
	private String success;
	@JsonProperty(value = "reason")
	private String reason;
	public String getTxlogisticid() {
		return txlogisticid;
	}
	public void setTxlogisticid(String txlogisticid) {
		this.txlogisticid = txlogisticid;
	}
	public String getMailno() {
		return mailno;
	}
	public void setMailno(String mailno) {
		this.mailno = mailno;
	}
	public String getSuccess() {
		return success;
	}
	public void setSuccess(String success) {
		this.success = success;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	
}

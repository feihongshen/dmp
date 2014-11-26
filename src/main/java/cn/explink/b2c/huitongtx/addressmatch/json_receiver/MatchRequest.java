package cn.explink.b2c.huitongtx.addressmatch.json_receiver;

import org.codehaus.jackson.annotate.JsonProperty;

public class MatchRequest {
	@JsonProperty(value = "taskcode")
	private String taskcode;
	@JsonProperty(value = "receiver_address")
	private String receiver_address;
	@JsonProperty(value = "receiver_city")
	private String receiver_city;

	public String getTaskcode() {
		return taskcode;
	}

	public void setTaskcode(String taskcode) {
		this.taskcode = taskcode;
	}

	public String getReceiver_address() {
		return receiver_address;
	}

	public void setReceiver_address(String receiver_address) {
		this.receiver_address = receiver_address;
	}

	public String getReceiver_city() {
		return receiver_city;
	}

	public void setReceiver_city(String receiver_city) {
		this.receiver_city = receiver_city;
	}

}

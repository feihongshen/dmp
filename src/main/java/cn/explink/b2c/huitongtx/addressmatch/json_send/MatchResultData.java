package cn.explink.b2c.huitongtx.addressmatch.json_send;

import org.codehaus.jackson.annotate.JsonProperty;

public class MatchResultData {
	@JsonProperty(value = "taskcode")
	private String taskcode; // 0成功， 非0 异常
	@JsonProperty(value = "match_status")
	private String match_status;
	@JsonProperty(value = "station_code")
	private String station_code;
	@JsonProperty(value = "station_name")
	private String station_name;
	@JsonProperty(value = "remark")
	private String remark;

	public String getTaskcode() {
		return taskcode;
	}

	public void setTaskcode(String taskcode) {
		this.taskcode = taskcode;
	}

	public String getMatch_status() {
		return match_status;
	}

	public void setMatch_status(String match_status) {
		this.match_status = match_status;
	}

	public String getStation_code() {
		return station_code;
	}

	public void setStation_code(String station_code) {
		this.station_code = station_code;
	}

	public String getStation_name() {
		return station_name;
	}

	public void setStation_name(String station_name) {
		this.station_name = station_name;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

}

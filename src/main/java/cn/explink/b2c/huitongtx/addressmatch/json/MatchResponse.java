package cn.explink.b2c.huitongtx.addressmatch.json;

import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;

public class MatchResponse {
	@JsonProperty(value = "code")
	private String code; // 0成功， 非0 异常
	@JsonProperty(value = "message")
	private String message;
	@JsonProperty(value = "data")
	private List<MatchData> matchData;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public List<MatchData> getMatchData() {
		return matchData;
	}

	public void setMatchData(List<MatchData> matchData) {
		this.matchData = matchData;
	}

}

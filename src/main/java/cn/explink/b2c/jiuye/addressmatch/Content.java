package cn.explink.b2c.jiuye.addressmatch;

import org.codehaus.jackson.annotate.JsonProperty;

public class Content {
	@JsonProperty(value = "GetProvice")
	private String getProvice;
	@JsonProperty(value = "GetCity")
	private String getCity;
	@JsonProperty(value = "GetCounty")
	private String getCounty;
	@JsonProperty(value = "GetAddress")
	private String getAddress;
	public String getGetProvice() {
		return getProvice;
	}
	public void setGetProvice(String getProvice) {
		this.getProvice = getProvice;
	}
	public String getGetCity() {
		return getCity;
	}
	public void setGetCity(String getCity) {
		this.getCity = getCity;
	}
	public String getGetCounty() {
		return getCounty;
	}
	public void setGetCounty(String getCounty) {
		this.getCounty = getCounty;
	}
	public String getGetAddress() {
		return getAddress;
	}
	public void setGetAddress(String getAddress) {
		this.getAddress = getAddress;
	}
	
}

package cn.explink.b2c.feiniuwang;

import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;
@JsonIgnoreProperties(ignoreUnknown = true)
public class CwbResponse {
	@JsonProperty(value = "logisticproviderid")
	private String logisticproviderid;
	@JsonProperty(value = "responseitems")
	private List<Responseitems> responseitems;
	public String getLogisticproviderid() {
		return logisticproviderid;
	}
	public void setLogisticproviderid(String logisticproviderid) {
		this.logisticproviderid = logisticproviderid;
	}
	public List<Responseitems> getResponseitems() {
		return responseitems;
	}
	public void setResponseitems(List<Responseitems> responseitems) {
		this.responseitems = responseitems;
	}
	
}

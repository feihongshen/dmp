package cn.explink.b2c.feiniuwang;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Items {
	@JsonProperty(value = "itemname")
	private String itemname;
	@JsonProperty(value = "number")
	private String number;
	@JsonProperty(value = "itemvalue")
	private String itemvalue;
	@JsonProperty(value = "packagenumber")
	private String packagenumber;
	@JsonProperty(value = "itemservicetype")
	private String itemservicetype;
	
	public String getItemname() {
		return itemname;
	}
	public void setItemname(String itemname) {
		this.itemname = itemname;
	}
	
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}
	public String getItemvalue() {
		return itemvalue;
	}
	public void setItemvalue(String itemvalue) {
		this.itemvalue = itemvalue;
	}
	public String getPackagenumber() {
		return packagenumber;
	}
	public void setPackagenumber(String packagenumber) {
		this.packagenumber = packagenumber;
	}
	public String getItemservicetype() {
		return itemservicetype;
	}
	public void setItemservicetype(String itemservicetype) {
		this.itemservicetype = itemservicetype;
	}
	
}

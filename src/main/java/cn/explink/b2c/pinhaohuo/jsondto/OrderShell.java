package cn.explink.b2c.pinhaohuo.jsondto;

import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class OrderShell {

	@JsonProperty(value = "data") 
	private List<PhhOrder> order; //商品名称
	@JsonProperty(value = "key") 
	private String key;

	public List<PhhOrder> getOrder() {
		return order;
	}

	public void setOrder(List<PhhOrder> order) {
		this.order = order;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}
	
	
	
	

	
}

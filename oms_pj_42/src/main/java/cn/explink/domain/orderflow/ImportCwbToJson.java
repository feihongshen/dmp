package cn.explink.domain.orderflow;

import net.sf.json.JSONObject;

/**
 * 数据导入环节
 */
public class ImportCwbToJson implements OrderFlowInterface {

	private String emaildate;
	private long customerid;

	public String getEmaildate() {
		return emaildate;
	}

	public void setEmaildate(String emaildate) {
		this.emaildate = emaildate;
	}

	public long getCustomerid() {
		return customerid;
	}

	public void setCustomerid(long customerid) {
		this.customerid = customerid;
	}

	public ImportCwbToJson() {
		super();
	}

	public String getBody(OrderFlow orderFlow) {
		loadFloworderdetailToProperty(orderFlow.getFloworderdetail());
		StringBuffer body = new StringBuffer();
		body.append("<ul>");
		body.append(orderFlow.getBody());
		body.append("<li><span>").append("供货商：</span><span id='Customer" + this.getCustomerid() + "'>" + this.getCustomerid()).append("</span></li>");
		body.append("<li><span>").append("Emial时间：</span><span>" + this.getEmaildate()).append("</span></li>");
		body.append("</ul>");
		return body.toString();
	}

	public void loadFloworderdetailToProperty(JSONObject floworderdetail) {

		if (!floworderdetail.isNullObject()) {
			if (floworderdetail.get("emaildate") != null) {
				this.emaildate = floworderdetail.getString("emaildate");
			}
			if (floworderdetail.get("customerid") != null) {
				this.customerid = floworderdetail.getLong("customerid");
			}
		}

	}
}

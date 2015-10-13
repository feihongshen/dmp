package cn.explink.domain.orderflow;

import net.sf.json.JSONObject;

public class StockToJson implements OrderFlowInterface {

	private Long owgid;

	public Long getOwgid() {
		return owgid;
	}

	public void setOwgid(Long owgid) {
		this.owgid = owgid;
	}

	@Override
	public String getBody(OrderFlow orderFlow) {
		loadFloworderdetailToProperty(orderFlow.getFloworderdetail());
		StringBuffer body = new StringBuffer();
		body.append("<ul>");
		body.append(orderFlow.getBody());
		if (this.getOwgid() > 0) {
			body.append("<li><span>").append("批次编号：</span><span>" + this.getOwgid()).append("</span></li>");
		}
		body.append("</ul>");
		return body.toString();
	}

	@Override
	public void loadFloworderdetailToProperty(JSONObject floworderdetail) {
		if (!floworderdetail.isNullObject()) {
			if (floworderdetail.get("owgid") != null) {
				this.owgid = floworderdetail.getLong("owgid");
			}
		}

	}
}

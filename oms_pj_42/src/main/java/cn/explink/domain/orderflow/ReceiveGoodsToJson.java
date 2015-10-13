package cn.explink.domain.orderflow;

import net.sf.json.JSONObject;

public class ReceiveGoodsToJson implements OrderFlowInterface {

	private long deliverid;

	public long getDeliverid() {
		return deliverid;
	}

	public void setDeliverid(long deliverid) {
		this.deliverid = deliverid;
	}

	@Override
	public String getBody(OrderFlow orderFlow) {
		loadFloworderdetailToProperty(orderFlow.getFloworderdetail());
		StringBuffer body = new StringBuffer();
		body.append("<ul>");
		body.append(orderFlow.getBody());
		if (this.getDeliverid() > 0) {
			body.append("<li><span>").append("分配至：</span><span id='Deliver" + this.getDeliverid() + "'>" + this.getDeliverid()).append("</span></li>");
		}

		body.append("</ul>");
		return body.toString();
	}

	@Override
	public void loadFloworderdetailToProperty(JSONObject floworderdetail) {
		if (!floworderdetail.isNullObject()) {
			if (floworderdetail.get("deliverid") != null) {
				this.deliverid = floworderdetail.getLong("deliverid");
			}
		}

	}

}

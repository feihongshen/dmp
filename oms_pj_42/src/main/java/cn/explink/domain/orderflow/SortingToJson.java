package cn.explink.domain.orderflow;

import net.sf.json.JSONObject;

public class SortingToJson implements OrderFlowInterface {

	private long nextbranchid;

	public long getNextbranchid() {
		return nextbranchid;
	}

	public void setNextbranchid(long nextbranchid) {
		this.nextbranchid = nextbranchid;
	}

	@Override
	public String getBody(OrderFlow orderFlow) {
		loadFloworderdetailToProperty(orderFlow.getFloworderdetail());
		StringBuffer body = new StringBuffer();
		body.append("<ul>");
		body.append(orderFlow.getBody());
		if (this.getNextbranchid() > 0) {
			body.append("<li><span>").append("分配至：</span><span id='NextBranch" + this.getNextbranchid() + "'>" + this.getNextbranchid()).append("</span></li>");
		}
		body.append("</ul>");
		return body.toString();
	}

	@Override
	public void loadFloworderdetailToProperty(JSONObject floworderdetail) {
		if (!floworderdetail.isNullObject()) {
			if (floworderdetail.get("nextbranchid") != null) {
				this.nextbranchid = floworderdetail.getLong("nextbranchid");
			}
		}

	}

}

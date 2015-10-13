package cn.explink.domain.orderflow;

import net.sf.json.JSONObject;

/**
 * 滞留环节
 */
public class StayGoodsToJson implements OrderFlowInterface {

	private long leavedreasonid;

	public long getLeavedreasonid() {
		return leavedreasonid;
	}

	public void setLeavedreasonid(long leavedreasonid) {
		this.leavedreasonid = leavedreasonid;
	}

	public StayGoodsToJson() {
		super();
	}

	public String getBody(OrderFlow orderFlow) {
		loadFloworderdetailToProperty(orderFlow.getFloworderdetail());
		StringBuffer body = new StringBuffer();
		body.append("<ul>");
		body.append(orderFlow.getBody());
		if (this.getLeavedreasonid() > 0) {
			body.append("<li><span>").append("滞留原因id：</span><span id='leavedreason" + this.getLeavedreasonid() + "'>" + this.getLeavedreasonid()).append("</span></li>");
		}
		body.append("</ul>");
		return body.toString();
	}

	public void loadFloworderdetailToProperty(JSONObject floworderdetail) {

		if (!floworderdetail.isNullObject()) {
			if (floworderdetail.get("leavedreasonid") != null) {
				this.leavedreasonid = floworderdetail.getLong("leavedreasonid");
			}
		}

	}
}

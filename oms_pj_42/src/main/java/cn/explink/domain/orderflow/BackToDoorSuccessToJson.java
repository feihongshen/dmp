package cn.explink.domain.orderflow;

import net.sf.json.JSONObject;

/**
 * 上门退成功环节
 */
public class BackToDoorSuccessToJson implements OrderFlowInterface {

	private long podresultid;

	public long getPodresultid() {
		return podresultid;
	}

	public void setPodresultid(long podresultid) {
		this.podresultid = podresultid;
	}

	public BackToDoorSuccessToJson() {
		super();
	}

	public String getBody(OrderFlow orderFlow) {
		loadFloworderdetailToProperty(orderFlow.getFloworderdetail());
		StringBuffer body = new StringBuffer();
		body.append("<ul>");
		body.append(orderFlow.getBody());
		if (this.getPodresultid() > 0) {
			body.append("<li><span>").append("配送结果id：</span><span id='podresult" + this.getPodresultid() + "'>" + this.getPodresultid()).append("</span></li>");
		}
		body.append("</ul>");
		return body.toString();
	}

	public void loadFloworderdetailToProperty(JSONObject floworderdetail) {

		if (!floworderdetail.isNullObject()) {
			if (floworderdetail.get("podresultid") != null) {
				this.podresultid = floworderdetail.getLong("podresultid");
			}
		}

	}
}

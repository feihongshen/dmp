package cn.explink.domain.orderflow;

import net.sf.json.JSONObject;

/**
 * 部分退货环节
 */
public class SomeReturnGoodsToJson implements OrderFlowInterface {

	private long backreasonid;

	public long getBackreasonid() {
		return backreasonid;
	}

	public void setBackreasonid(long backreasonid) {
		this.backreasonid = backreasonid;
	}

	public SomeReturnGoodsToJson() {
		super();
	}

	public String getBody(OrderFlow orderFlow) {
		loadFloworderdetailToProperty(orderFlow.getFloworderdetail());
		StringBuffer body = new StringBuffer();
		body.append("<ul>");
		body.append(orderFlow.getBody());
		if (this.getBackreasonid() > 0) {
			body.append("<li><span>").append("退货原因id：</span><span id='backreason" + this.getBackreasonid() + "'>" + this.getBackreasonid()).append("</span></li>");
		}
		body.append("</ul>");
		return body.toString();
	}

	public void loadFloworderdetailToProperty(JSONObject floworderdetail) {

		if (!floworderdetail.isNullObject()) {
			if (floworderdetail.get("backreasonid") != null) {
				this.backreasonid = floworderdetail.getLong("backreasonid");
			}
		}

	}
}

package cn.explink.domain.orderflow;

import net.sf.json.JSONObject;

/**
 * 中转出库扫描环节
 *
 */

public class ChangeGoodsOutwarehouseToJson implements OrderFlowInterface {
	private Long reasonid;
	private Long nextbranchid;

	public Long getReasonid() {
		return reasonid;
	}

	public void setReasonid(Long reasonid) {
		this.reasonid = reasonid;
	}

	public Long getNextbranchid() {
		return nextbranchid;
	}

	public void setNextbranchid(Long nextbranchid) {
		this.nextbranchid = nextbranchid;
	}

	@Override
	public String getBody(OrderFlow orderFlow) {
		loadFloworderdetailToProperty(orderFlow.getFloworderdetail());
		StringBuffer body = new StringBuffer();
		body.append("<ul>");
		body.append(orderFlow.getBody());
		if (this.getReasonid() > 0) {
			body.append("<li><span>").append("中转原因id：</span><span>" + this.getReasonid()).append("</span></li>");
		}
		if (this.getNextbranchid() > 0) {
			body.append("<li><span>").append("下一站id：</span><span>" + this.getNextbranchid()).append("</span></li>");
		}
		body.append("</ul>");
		return body.toString();
	}

	@Override
	public void loadFloworderdetailToProperty(JSONObject floworderdetail) {
		if (!floworderdetail.isNullObject()) {
			if (floworderdetail.get("reasonid") != null) {
				this.reasonid = floworderdetail.getLong("reasonid");
			}
			if (floworderdetail.get("nextbranchid") != null) {
				this.nextbranchid = floworderdetail.getLong("nextbranchid");
			}
		}
	}

}

package cn.explink.domain.orderflow;

import net.sf.json.JSONObject;

/**
 * 中转入库正常入库环节
 */
public class ChangeIntoWarehousToJson implements OrderFlowInterface {

	private long nextbranchid;
	private long deliverid;

	public long getNextbranchid() {
		return nextbranchid;
	}

	public void setNextbranchid(long nextbranchid) {
		this.nextbranchid = nextbranchid;
	}

	public long getDeliverid() {
		return deliverid;
	}

	public void setDeliverid(long deliverid) {
		this.deliverid = deliverid;
	}

	public ChangeIntoWarehousToJson() {
		super();
	}

	public String getBody(OrderFlow orderFlow) {
		loadFloworderdetailToProperty(orderFlow.getFloworderdetail());
		StringBuffer body = new StringBuffer();
		body.append("<ul>");
		body.append(orderFlow.getBody());
		if (this.getDeliverid() > 0) {
			body.append("<li><span>").append("分配至：</span><span id='Deliver" + this.getNextbranchid() + "'>" + this.getDeliverid()).append("</span></li>");
		} else if (this.getNextbranchid() > 0) {
			body.append("<li><span>").append("分配至：</span><span id='NextBranch" + this.getNextbranchid() + "'>" + this.getNextbranchid()).append("</span></li>");
		}

		body.append("</ul>");
		return body.toString();
	}

	public void loadFloworderdetailToProperty(JSONObject floworderdetail) {

		if (!floworderdetail.isNullObject()) {
			if (floworderdetail.get("nextbranchid") != null) {
				this.nextbranchid = floworderdetail.getLong("nextbranchid");
			}
			if (floworderdetail.get("customerid") != null) {
				this.deliverid = floworderdetail.getLong("deliverid");
			}
		}

	}
}

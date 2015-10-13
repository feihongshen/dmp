package cn.explink.domain.orderflow;

import net.sf.json.JSONObject;

/**
 * 出库扫描环节
 */
public class OutWarehouseToJson implements OrderFlowInterface {
	private Long driverid;

	public Long getDriverid() {
		return driverid;
	}

	public void setDriverid(Long driverid) {
		this.driverid = driverid;
	}

	@Override
	public String getBody(OrderFlow orderFlow) {
		loadFloworderdetailToProperty(orderFlow.getFloworderdetail());
		StringBuffer body = new StringBuffer();
		body.append("<ul>");
		body.append(orderFlow.getBody());
		if (this.getDriverid() > 0) {
			body.append("<li><span>").append("司机：</span><span>" + this.getDriverid()).append("</span></li>");
		}
		body.append("</ul>");
		return body.toString();
	}

	@Override
	public void loadFloworderdetailToProperty(JSONObject floworderdetail) {
		if (!floworderdetail.isNullObject()) {
			if (floworderdetail.get("driverid") != null) {
				this.driverid = floworderdetail.getLong("driverid");
			}
		}

	}

}

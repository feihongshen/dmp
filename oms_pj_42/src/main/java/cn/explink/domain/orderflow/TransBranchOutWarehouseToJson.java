package cn.explink.domain.orderflow;

import net.sf.json.JSONObject;

/**
 * 中转站出库环节
 */
public class TransBranchOutWarehouseToJson implements OrderFlowInterface {
	private long driverid;
	private long nextbranchid;
	private long truckid;

	public long getDriverid() {
		return driverid;
	}

	public void setDriverid(long driverid) {
		this.driverid = driverid;
	}

	public long getNextbranchid() {
		return nextbranchid;
	}

	public void setNextbranchid(long nextbranchid) {
		this.nextbranchid = nextbranchid;
	}

	public long getTruckid() {
		return truckid;
	}

	public void setTruckid(long truckid) {
		this.truckid = truckid;
	}

	@Override
	public String getBody(OrderFlow orderFlow) {
		loadFloworderdetailToProperty(orderFlow.getFloworderdetail());
		StringBuffer body = new StringBuffer();
		body.append("<ul>");
		body.append(orderFlow.getBody());
		if (this.getDriverid() > 0) {
			body.append("<li><span>").append("司机id：</span><span>" + this.getDriverid()).append("</span></li>");
		}
		if (this.getNextbranchid() > 0) {
			body.append("<li><span>").append("下一站id：</span><span>" + this.getNextbranchid()).append("</span></li>");
		}
		if (this.getTruckid() > 0) {
			body.append("<li><span>").append("车辆id：</span><span>" + this.getTruckid()).append("</span></li>");
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
			if (floworderdetail.get("nextbranchid") != null) {
				this.nextbranchid = floworderdetail.getLong("nextbranchid");
			}
			if (floworderdetail.get("truckid") != null) {
				this.truckid = floworderdetail.getLong("truckid");
			}
		}

	}

}

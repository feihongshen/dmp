package cn.explink.domain.orderflow;

import net.sf.json.JSONObject;

/**
 * 提货有货无单入库环节
 */
public class GetGoodsNoListIntoWarehousToJson implements OrderFlowInterface {
	private Long customerid;

	public Long getCustomerid() {
		return customerid;
	}

	public void setCustomerid(Long customerid) {
		this.customerid = customerid;
	}

	@Override
	public String getBody(OrderFlow orderFlow) {
		loadFloworderdetailToProperty(orderFlow.getFloworderdetail());
		StringBuffer body = new StringBuffer();
		body.append("<ul>");
		body.append(orderFlow.getBody());
		if (this.getCustomerid() > 0) {
			body.append("<li><span>").append("供货商：</span><span id='Customer" + this.getCustomerid() + "'>" + this.getCustomerid()).append("</span></li>");
		}
		body.append("</ul>");
		return body.toString();
	}

	@Override
	public void loadFloworderdetailToProperty(JSONObject floworderdetail) {
		if (!floworderdetail.isNullObject()) {
			if (floworderdetail.get("customerid") != null) {
				this.customerid = floworderdetail.getLong("customerid");
			}
		}

	}

}

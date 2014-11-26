package cn.explink.b2c.dpfoss.waybill;

import java.io.Serializable;

public class QueryWayBillRequest implements Serializable {
	private String handOverNo;
	private String waybillNo;

	public String getHandOverNo() {
		return handOverNo;
	}

	public void setHandOverNo(String handOverNo) {
		this.handOverNo = handOverNo;
	}

	public String getWaybillNo() {
		return waybillNo;
	}

	public void setWaybillNo(String waybillNo) {
		this.waybillNo = waybillNo;
	}

}

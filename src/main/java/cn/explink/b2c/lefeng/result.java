package cn.explink.b2c.lefeng;

import java.util.List;

/**
 * 乐蜂网result信息
 * 
 * @author Administrator
 *
 */
public class result {
	private String expressNumber;
	private String status;
	private List<TrackingItems> trackingItems;

	public String getExpressNumber() {
		return expressNumber;
	}

	public void setExpressNumber(String expressNumber) {
		this.expressNumber = expressNumber;
	}

	public List<TrackingItems> getTrackingItems() {
		return trackingItems;
	}

	public void setTrackingItems(List<TrackingItems> trackingItems) {
		this.trackingItems = trackingItems;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}

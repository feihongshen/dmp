package cn.explink.domain;

public class OverdueExMoDetailVO {

	private String cwb = null;

	private String venderName = null;

	private String createDate = null;

	private String deliverState = null;

	private long warehouseId = 0;

	private String warehouseName = null;

	private long deliverStationId = 0;

	private String deliverStationName = null;

	public String getCwb() {
		return this.cwb;
	}

	public void setCwb(String cwb) {
		this.cwb = cwb;
	}

	public String getVenderName() {
		return this.venderName;
	}

	public void setVenderName(String venderName) {
		this.venderName = venderName;
	}

	public String getCreateDate() {
		return this.createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	public String getDeliverState() {
		return this.deliverState;
	}

	public void setDeliverState(String deliverState) {
		this.deliverState = deliverState;
	}

	public String getWarehouseName() {
		return this.warehouseName;
	}

	public void setWarehouseName(String warehouseName) {
		this.warehouseName = warehouseName;
	}

	public String getDeliverStationName() {
		return this.deliverStationName;
	}

	public void setDeliverStationName(String deliverStationName) {
		this.deliverStationName = deliverStationName;
	}

	public long getDeliverStationId() {
		return this.deliverStationId;
	}

	public void setDeliverStationId(long deliverStationId) {
		this.deliverStationId = deliverStationId;
	}

	public long getWarehouseId() {
		return this.warehouseId;
	}

	public void setWarehouseId(long warehouseId) {
		this.warehouseId = warehouseId;
	}

}

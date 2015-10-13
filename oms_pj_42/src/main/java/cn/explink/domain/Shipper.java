package cn.explink.domain;

public class Shipper {
	private long shipperid;
	private String shipperno;
	private String shippername;
	private String shipperurl;
	private String shipperremark;
	private long paywayid;

	public long getShipperid() {
		return shipperid;
	}

	public void setShipperid(long shipperid) {
		this.shipperid = shipperid;
	}

	public String getShipperno() {
		return shipperno;
	}

	public void setShipperno(String shipperno) {
		this.shipperno = shipperno;
	}

	public String getShippername() {
		return shippername;
	}

	public void setShippername(String shippername) {
		this.shippername = shippername;
	}

	public String getShipperurl() {
		return shipperurl;
	}

	public void setShipperurl(String shipperurl) {
		this.shipperurl = shipperurl;
	}

	public String getShipperremark() {
		return shipperremark;
	}

	public void setShipperremark(String shipperremark) {
		this.shipperremark = shipperremark;
	}

	public long getPaywayid() {
		return paywayid;
	}

	public void setPaywayid(long paywayid) {
		this.paywayid = paywayid;
	}
}

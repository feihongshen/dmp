package cn.explink.b2c.jiuxian;

public class JiuxianWang {
	public String customerid;
	public String track_url;
	public long warehouseid;
	public String userkey;
	public String sShippedCode;
	public String Maxcount;

	public String getMaxcount() {
		return Maxcount;
	}

	public void setMaxcount(String maxcount) {
		Maxcount = maxcount;
	}

	public String getCustomerid() {
		return customerid;
	}

	public void setCustomerid(String customerid) {
		this.customerid = customerid;
	}

	public String getTrack_url() {
		return track_url;
	}

	public void setTrack_url(String track_url) {
		this.track_url = track_url;
	}

	public long getWarehouseid() {
		return warehouseid;
	}

	public void setWarehouseid(long warehouseid) {
		this.warehouseid = warehouseid;
	}

	public String getUserkey() {
		return userkey;
	}

	public void setUserkey(String userkey) {
		this.userkey = userkey;
	}

	public String getsShippedCode() {
		return sShippedCode;
	}

	public void setsShippedCode(String sShippedCode) {
		this.sShippedCode = sShippedCode;
	}

}

package cn.explink.b2c.benlaishenghuo;

public class Benlaishenghuo {
	public String customerid;
	public String post_url;
	public String track_url;
	public long warehouseid;
	public int Isopendownload;
	public String userkey;
	public String sShippedCode;

	public String getsShippedCode() {
		return sShippedCode;
	}

	public void setsShippedCode(String sShippedCode) {
		this.sShippedCode = sShippedCode;
	}

	public String getUserkey() {
		return userkey;
	}

	public void setUserkey(String userkey) {
		this.userkey = userkey;
	}

	public int getIsopendownload() {
		return Isopendownload;
	}

	public void setIsopendownload(int isopendownload) {
		Isopendownload = isopendownload;
	}

	public String getCustomerid() {
		return customerid;
	}

	public void setCustomerid(String customerid) {
		this.customerid = customerid;
	}

	public String getPost_url() {
		return post_url;
	}

	public void setPost_url(String post_url) {
		this.post_url = post_url;
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
}

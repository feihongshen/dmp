package cn.explink.domain;

public class TuiHuoChuZhanOrder {
	private int id;
	private String cwb;
	private String tuihuochuzhantime;
	private long tuihuobranchid;
	private long customerid;
	private long cwbordertypeid;
	private String tuihuozhanrukutime;

	public String getTuihuozhanrukutime() {
		return tuihuozhanrukutime;
	}

	public void setTuihuozhanrukutime(String tuihuozhanrukutime) {
		this.tuihuozhanrukutime = tuihuozhanrukutime;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCwb() {
		return cwb;
	}

	public void setCwb(String cwb) {
		this.cwb = cwb;
	}

	public String getTuihuochuzhantime() {
		return tuihuochuzhantime;
	}

	public void setTuihuochuzhantime(String tuihuochuzhantime) {
		this.tuihuochuzhantime = tuihuochuzhantime;
	}

	public long getTuihuobranchid() {
		return tuihuobranchid;
	}

	public void setTuihuobranchid(long tuihuobranchid) {
		this.tuihuobranchid = tuihuobranchid;
	}

	public long getCustomerid() {
		return customerid;
	}

	public void setCustomerid(long customerid) {
		this.customerid = customerid;
	}

	public long getCwbordertypeid() {
		return cwbordertypeid;
	}

	public void setCwbordertypeid(long cwbordertypeid) {
		this.cwbordertypeid = cwbordertypeid;
	}

}

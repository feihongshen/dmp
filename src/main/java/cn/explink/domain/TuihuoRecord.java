package cn.explink.domain;

public class TuihuoRecord {

	private long id; // 主键id
	private long branchid; // 站点id
	private long tuihuobranchid; // 退货站id
	private String tuihuochuzhantime; // 退货出站时间
	private String tuihuozhanrukutime; // 退货站入库时间
	private long userid; // 操作人id
	private long customerid;// 供货商id
	private String cwb;// 订单号
	private int cwbordertypeid;// 订单类型（1配送 2上门退 3上门换）

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getBranchid() {
		return branchid;
	}

	public void setBranchid(long branchid) {
		this.branchid = branchid;
	}

	public long getTuihuobranchid() {
		return tuihuobranchid;
	}

	public void setTuihuobranchid(long tuihuobranchid) {
		this.tuihuobranchid = tuihuobranchid;
	}

	public String getTuihuochuzhantime() {
		return tuihuochuzhantime;
	}

	public void setTuihuochuzhantime(String tuihuochuzhantime) {
		this.tuihuochuzhantime = tuihuochuzhantime;
	}

	public String getTuihuozhanrukutime() {
		return tuihuozhanrukutime;
	}

	public void setTuihuozhanrukutime(String tuihuozhanrukutime) {
		this.tuihuozhanrukutime = tuihuozhanrukutime;
	}

	public long getUserid() {
		return userid;
	}

	public void setUserid(long userid) {
		this.userid = userid;
	}

	public long getCustomerid() {
		return customerid;
	}

	public void setCustomerid(long customerid) {
		this.customerid = customerid;
	}

	public String getCwb() {
		return cwb;
	}

	public void setCwb(String cwb) {
		this.cwb = cwb;
	}

	public int getCwbordertypeid() {
		return cwbordertypeid;
	}

	public void setCwbordertypeid(int cwbordertypeid) {
		this.cwbordertypeid = cwbordertypeid;
	}
}

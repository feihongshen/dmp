package cn.explink.domain;

public class OrderArriveTime {
	private long id;
	private String cwb;
	private long customerid;
	private String outtime;
	private String intime;
	private String arrivetime;
	private long sendcarnum;
	private long scannum;
	private long cwbordertypeid;
	private long outbranchid;
	private long inbranchid;
	private long userid;
	private long saveuserid;
	private String savetime;

	// 不在数据库字段
	private String customername;
	private String cwbordertypename;
	private String outbranchname;
	private String inbranchname;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getCwb() {
		return cwb;
	}

	public void setCwb(String cwb) {
		this.cwb = cwb;
	}

	public long getCustomerid() {
		return customerid;
	}

	public void setCustomerid(long customerid) {
		this.customerid = customerid;
	}

	public String getOuttime() {
		return outtime;
	}

	public void setOuttime(String outtime) {
		this.outtime = outtime;
	}

	public String getIntime() {
		return intime;
	}

	public void setIntime(String intime) {
		this.intime = intime;
	}

	public String getArrivetime() {
		return arrivetime;
	}

	public void setArrivetime(String arrivetime) {
		this.arrivetime = arrivetime;
	}

	public long getSendcarnum() {
		return sendcarnum;
	}

	public void setSendcarnum(long sendcarnum) {
		this.sendcarnum = sendcarnum;
	}

	public long getScannum() {
		return scannum;
	}

	public void setScannum(long scannum) {
		this.scannum = scannum;
	}

	public long getCwbordertypeid() {
		return cwbordertypeid;
	}

	public void setCwbordertypeid(long cwbordertypeid) {
		this.cwbordertypeid = cwbordertypeid;
	}

	public long getOutbranchid() {
		return outbranchid;
	}

	public void setOutbranchid(long outbranchid) {
		this.outbranchid = outbranchid;
	}

	public long getInbranchid() {
		return inbranchid;
	}

	public void setInbranchid(long inbranchid) {
		this.inbranchid = inbranchid;
	}

	public long getUserid() {
		return userid;
	}

	public void setUserid(long userid) {
		this.userid = userid;
	}

	public long getSaveuserid() {
		return saveuserid;
	}

	public void setSaveuserid(long saveuserid) {
		this.saveuserid = saveuserid;
	}

	public String getSavetime() {
		return savetime;
	}

	public void setSavetime(String savetime) {
		this.savetime = savetime;
	}

	public String getCustomername() {
		return customername;
	}

	public void setCustomername(String customername) {
		this.customername = customername;
	}

	public String getCwbordertypename() {
		return cwbordertypename;
	}

	public void setCwbordertypename(String cwbordertypename) {
		this.cwbordertypename = cwbordertypename;
	}

	public String getOutbranchname() {
		return outbranchname;
	}

	public void setOutbranchname(String outbranchname) {
		this.outbranchname = outbranchname;
	}

	public String getInbranchname() {
		return inbranchname;
	}

	public void setInbranchname(String inbranchname) {
		this.inbranchname = inbranchname;
	}

}

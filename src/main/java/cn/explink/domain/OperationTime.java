package cn.explink.domain;

public class OperationTime {
	protected long id;
	protected String cwb;
	protected long branchid;
	protected long credate;
	protected int flowordertype;
	protected long nextbranchid;
	protected String isupdate;
	protected int cwbordertypeid;
	private int sitetype;
	private int state;
	private long customerid;
	public long getCustomerid() {
		return customerid;
	}
	public void setCustomerid(long customerid) {
		this.customerid = customerid;
	}
	public OperationTime() {
	}

	public OperationTime(int id, String cwb, long branchid, long credate, int flowordertype, long nextbranchid) {
		this.id = id;
		this.cwb = cwb;
		this.branchid = branchid;
		this.credate = credate;
		this.flowordertype = flowordertype;
		this.nextbranchid = nextbranchid;
	}

	public int getSitetype() {
		return sitetype;
	}

	public void setSitetype(int sitetype) {
		this.sitetype = sitetype;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public String getIsupdate() {
		return isupdate;
	}

	public void setIsupdate(String isupdate) {
		this.isupdate = isupdate;
	}

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

	public long getBranchid() {
		return branchid;
	}

	public void setBranchid(long branchid) {
		this.branchid = branchid;
	}

	public long getCredate() {
		return credate;
	}

	public void setCredate(long credate) {
		this.credate = credate;
	}

	public int getFlowordertype() {
		return flowordertype;
	}

	public void setFlowordertype(int flowordertype) {
		this.flowordertype = flowordertype;
	}

	public long getNextbranchid() {
		return nextbranchid;
	}

	public void setNextbranchid(long nextbranchid) {
		this.nextbranchid = nextbranchid;
	}

	public int getCwbordertypeid() {
		return cwbordertypeid;
	}

	public void setCwbordertypeid(int cwbordertypeid) {
		this.cwbordertypeid = cwbordertypeid;
	}

	
}

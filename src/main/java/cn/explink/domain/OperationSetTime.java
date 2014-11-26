package cn.explink.domain;

public class OperationSetTime {
	private long id;
	private String name;
	private String value;
	private long userid;
	private String creattime;
	private String updatetime;
	private int sitetype;
	private int state;
	private String branchids;

	// 不在数据库字段
	private String sitetypename;
	private String branchname;

	public String getBranchname() {
		return branchname;
	}

	public void setBranchname(String branchname) {
		this.branchname = branchname;
	}

	public String getBranchids() {
		return branchids;
	}

	public void setBranchids(String branchids) {
		this.branchids = branchids;
	}

	public String getSitetypename() {
		return sitetypename;
	}

	public void setSitetypename(String sitetypename) {
		this.sitetypename = sitetypename;
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

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public long getUserid() {
		return userid;
	}

	public void setUserid(long userid) {
		this.userid = userid;
	}

	public String getCreattime() {
		return creattime;
	}

	public void setCreattime(String creattime) {
		this.creattime = creattime;
	}

	public String getUpdatetime() {
		return updatetime;
	}

	public void setUpdatetime(String updatetime) {
		this.updatetime = updatetime;
	}

}

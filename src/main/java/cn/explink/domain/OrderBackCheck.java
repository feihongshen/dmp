package cn.explink.domain;

public class OrderBackCheck {
	private long id;
	private long checkstate;
	private String cwb;
	private long customerid;
	private int cwbordertypeid;
	private long flowordertype;
	private long cwbstate;
	private String consigneename;
	private String consigneephone;
	private String consigneeaddress;
	private String backreason;
	private long userid;
	private String createtime;
	private long checkuser;
	private String checkcreatetime;
	private long branchid;

	// 不在数据库字段
	private String customername;
	private String username;
	private String checkusername;
	private String flowordertypename;
	private String cwbordertypename;
	private String cwbstatename;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getCheckstate() {
		return checkstate;
	}

	public void setCheckstate(long checkstate) {
		this.checkstate = checkstate;
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

	public int getCwbordertypeid() {
		return cwbordertypeid;
	}

	public void setCwbordertypeid(int cwbordertypeid) {
		this.cwbordertypeid = cwbordertypeid;
	}

	public long getFlowordertype() {
		return flowordertype;
	}

	public void setFlowordertype(long flowordertype) {
		this.flowordertype = flowordertype;
	}

	public long getCwbstate() {
		return cwbstate;
	}

	public void setCwbstate(long cwbstate) {
		this.cwbstate = cwbstate;
	}

	public String getConsigneename() {
		return consigneename;
	}

	public void setConsigneename(String consigneename) {
		this.consigneename = consigneename;
	}

	public String getConsigneephone() {
		return consigneephone;
	}

	public void setConsigneephone(String consigneephone) {
		this.consigneephone = consigneephone;
	}

	public String getConsigneeaddress() {
		return consigneeaddress;
	}

	public void setConsigneeaddress(String consigneeaddress) {
		this.consigneeaddress = consigneeaddress;
	}

	public String getBackreason() {
		return backreason;
	}

	public void setBackreason(String backreason) {
		this.backreason = backreason;
	}

	public long getUserid() {
		return userid;
	}

	public void setUserid(long userid) {
		this.userid = userid;
	}

	public String getCreatetime() {
		return createtime;
	}

	public void setCreatetime(String createtime) {
		this.createtime = createtime;
	}

	public long getCheckuser() {
		return checkuser;
	}

	public void setCheckuser(long checkuser) {
		this.checkuser = checkuser;
	}

	public String getCheckcreatetime() {
		return checkcreatetime;
	}

	public void setCheckcreatetime(String checkcreatetime) {
		this.checkcreatetime = checkcreatetime;
	}

	public long getBranchid() {
		return branchid;
	}

	public void setBranchid(long branchid) {
		this.branchid = branchid;
	}

	public String getCustomername() {
		return customername;
	}

	public void setCustomername(String customername) {
		this.customername = customername;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getCheckusername() {
		return checkusername;
	}

	public void setCheckusername(String checkusername) {
		this.checkusername = checkusername;
	}

	public String getFlowordertypename() {
		return flowordertypename;
	}

	public void setFlowordertypename(String flowordertypename) {
		this.flowordertypename = flowordertypename;
	}

	public String getCwbordertypename() {
		return cwbordertypename;
	}

	public void setCwbordertypename(String cwbordertypename) {
		this.cwbordertypename = cwbordertypename;
	}

	public String getCwbstatename() {
		return cwbstatename;
	}

	public void setCwbstatename(String cwbstatename) {
		this.cwbstatename = cwbstatename;
	}

}

package cn.explink.domain;

public class OrderBackRuku {
	private long id;
	private String cwb;
	private String consigneename;//收件人名
	private String consigneeaddress;//收件人地址
	private int cwbordertypeid;//订单类型
	private long customerid;//客户id
	private long branchid;//配送站点id
	private String createtime;//退货入库时间
	private int auditstate;//审核状态(0 待审核 1已审核)
	private String remarkstr;//备注
	private String auditname;//审核人
	private String audittime;//审核时间
	private int cwbstate;//订单状态
	
	//不在数据库字段
	private String cwbordertypename;
	private String customername;
	private String branchname;
	private String auditstatename;
	
	public String getAuditstatename() {
		return auditstatename;
	}
	public void setAuditstatename(String auditstatename) {
		this.auditstatename = auditstatename;
	}
	public int getCwbstate() {
		return cwbstate;
	}
	public void setCwbstate(int cwbstate) {
		this.cwbstate = cwbstate;
	}
	public String getCwbordertypename() {
		return cwbordertypename;
	}
	public void setCwbordertypename(String cwbordertypename) {
		this.cwbordertypename = cwbordertypename;
	}
	public String getCustomername() {
		return customername;
	}
	public void setCustomername(String customername) {
		this.customername = customername;
	}
	public String getBranchname() {
		return branchname;
	}
	public void setBranchname(String branchname) {
		this.branchname = branchname;
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
	public String getConsigneename() {
		return consigneename;
	}
	public void setConsigneename(String consigneename) {
		this.consigneename = consigneename;
	}
	public String getConsigneeaddress() {
		return consigneeaddress;
	}
	public void setConsigneeaddress(String consigneeaddress) {
		this.consigneeaddress = consigneeaddress;
	}
	public int getCwbordertypeid() {
		return cwbordertypeid;
	}
	public void setCwbordertypeid(int cwbordertypeid) {
		this.cwbordertypeid = cwbordertypeid;
	}
	public long getCustomerid() {
		return customerid;
	}
	public void setCustomerid(long customerid) {
		this.customerid = customerid;
	}
	public long getBranchid() {
		return branchid;
	}
	public void setBranchid(long branchid) {
		this.branchid = branchid;
	}
	public String getCreatetime() {
		return createtime;
	}
	public void setCreatetime(String createtime) {
		this.createtime = createtime;
	}
	public int getAuditstate() {
		return auditstate;
	}
	public void setAuditstate(int auditstate) {
		this.auditstate = auditstate;
	}
	public String getRemarkstr() {
		return remarkstr;
	}
	public void setRemarkstr(String remarkstr) {
		this.remarkstr = remarkstr;
	}
	public String getAuditname() {
		return auditname;
	}
	public void setAuditname(String auditname) {
		this.auditname = auditname;
	}
	public String getAudittime() {
		return audittime;
	}
	public void setAudittime(String audittime) {
		this.audittime = audittime;
	}
	
	
}

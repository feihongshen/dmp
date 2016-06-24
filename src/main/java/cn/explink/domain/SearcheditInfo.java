package cn.explink.domain;

public class SearcheditInfo {
	public long id;
	public String cwb;
	public long deliverybranchid;
	public String oldconsigneename;
	public String newconsigneename;
	public String oldconsigneemobile;
	public String newconsigneemobile;
	public String oldconsigneeaddress;
	public String newconsigneeaddress;
	public String oldResendtime;
	public String newResendtime;
	public String oldcommand;
	public String newcommand;
	public String cretime;
	public long crename;
	public String oldremark;
	public String newremark;
	private long newbranchid;
	private long oldexceldeliverid;
	private String oldexceldeliver;
	private long newexceldeliverid;
	private String newexceldeliver;
	
	public long getOldexceldeliverid() {
		return oldexceldeliverid;
	}

	public void setOldexceldeliverid(long oldexceldeliverid) {
		this.oldexceldeliverid = oldexceldeliverid;
	}

	public String getOldexceldeliver() {
		return oldexceldeliver;
	}

	public void setOldexceldeliver(String oldexceldeliver) {
		this.oldexceldeliver = oldexceldeliver;
	}

	public long getNewexceldeliverid() {
		return newexceldeliverid;
	}

	public void setNewexceldeliverid(long newexceldeliverid) {
		this.newexceldeliverid = newexceldeliverid;
	}

	public String getNewexceldeliver() {
		return newexceldeliver;
	}

	public void setNewexceldeliver(String newexceldeliver) {
		this.newexceldeliver = newexceldeliver;
	}

	public long getNewbranchid() {
		return newbranchid;
	}

	public void setNewbranchid(long newbranchid) {
		this.newbranchid = newbranchid;
	}

	private String newbranchname;//修改后的站点信息
	
	public String getNewbranchname() {
		return newbranchname;
	}

	public void setNewbranchname(String newbranchname) {
		this.newbranchname = newbranchname;
	}

	public String getOldremark() {
		return oldremark;
	}

	public void setOldremark(String oldremark) {
		this.oldremark = oldremark;
	}

	public String getNewremark() {
		return newremark;
	}

	public void setNewremark(String newremark) {
		this.newremark = newremark;
	}

	public String state;

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

	public String getOldconsigneename() {
		return oldconsigneename;
	}

	public void setOldconsigneename(String oldconsigneename) {
		this.oldconsigneename = oldconsigneename;
	}

	public String getNewconsigneename() {
		return newconsigneename;
	}

	public void setNewconsigneename(String newconsigneename) {
		this.newconsigneename = newconsigneename;
	}

	public String getOldconsigneemobile() {
		return oldconsigneemobile;
	}

	public void setOldconsigneemobile(String oldconsigneemobile) {
		this.oldconsigneemobile = oldconsigneemobile;
	}

	public String getNewconsigneemobile() {
		return newconsigneemobile;
	}

	public void setNewconsigneemobile(String newconsigneemobile) {
		this.newconsigneemobile = newconsigneemobile;
	}

	public String getOldconsigneeaddress() {
		return oldconsigneeaddress;
	}

	public void setOldconsigneeaddress(String oldconsigneeaddress) {
		this.oldconsigneeaddress = oldconsigneeaddress;
	}

	public String getNewconsigneeaddress() {
		return newconsigneeaddress;
	}

	public void setNewconsigneeaddress(String newconsigneeaddress) {
		this.newconsigneeaddress = newconsigneeaddress;
	}

	public String getOldResendtime() {
		return oldResendtime;
	}

	public void setOldResendtime(String oldResendtime) {
		this.oldResendtime = oldResendtime;
	}

	public String getNewResendtime() {
		return newResendtime;
	}

	public void setNewResendtime(String newResendtime) {
		this.newResendtime = newResendtime;
	}

	public String getOldcommand() {
		return oldcommand;
	}

	public void setOldcommand(String oldcommand) {
		this.oldcommand = oldcommand;
	}

	public String getNewcommand() {
		return newcommand;
	}

	public void setNewcommand(String newcommand) {
		this.newcommand = newcommand;
	}

	public String getCretime() {
		return cretime;
	}

	public void setCretime(String cretime) {
		this.cretime = cretime;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public long getDeliverybranchid() {
		return deliverybranchid;
	}

	public void setDeliverybranchid(long deliverybranchid) {
		this.deliverybranchid = deliverybranchid;
	}

	public long getCrename() {
		return crename;
	}

	public void setCrename(long crename) {
		this.crename = crename;
	}

}

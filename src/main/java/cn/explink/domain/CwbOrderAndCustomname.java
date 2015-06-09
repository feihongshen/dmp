package cn.explink.domain;
/**
 * 
 * @author Administrator
 *				co.setCwb(c.getCwb());
				co.setTranscwb(c.getTranscwb());
				co.setCustomerid(c.getCustomerid());
				co.setEmaildate(c.getEmaildate());
				co.setConsigneename(c.getConsigneename());
				co.setConsigneeaddress(c.getConsigneeaddress());
				co.setConsigneemobile(c.getConsigneemobile());
				co.setCwbstate(c.getCwbstate());
 */
public class CwbOrderAndCustomname {
	public String getCwbstate() {
		return cwbstate;
	}
	public void setCwbstate(String cwbstate) {
		this.cwbstate = cwbstate;
	}
	private long id;
	private String cwb;
	private String transcwb;
	private String customername;
	private String emaildate;
	private String consigneename;
	private String consigneeaddress;
	private String consigneemobile;
	/*long cwbstate;*/
	String cwbstate;
	private long customerid;
	
	
	
	public long getCustomerid() {
		return customerid;
	}
	public void setCustomerid(long customerid) {
		this.customerid = customerid;
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
	public String getTranscwb() {
		return transcwb;
	}
	public void setTranscwb(String transcwb) {
		this.transcwb = transcwb;
	}


	public String getCustomername() {
		return customername;
	}
	public void setCustomername(String customername) {
		this.customername = customername;
	}
	public String getEmaildate() {
		return emaildate;
	}
	public void setEmaildate(String emaildate) {
		this.emaildate = emaildate;
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
	public String getConsigneemobile() {
		return consigneemobile;
	}
	public void setConsigneemobile(String consigneemobile) {
		this.consigneemobile = consigneemobile;
	}
	/*public long getCwbstate() {
		return cwbstate;
	}
	public void setCwbstate(long cwbstate) {
		this.cwbstate = cwbstate;
	}*/
	
	
	

}

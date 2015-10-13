package cn.explink.domain;

public class ServiceArea {
	private long serviceareaid;
	private String serviceareaname;
	private String servicearearemark;
	private long customerid;
	private String servid;

	public long getServiceareaid() {
		return serviceareaid;
	}

	public void setServiceareaid(long serviceareaid) {
		this.serviceareaid = serviceareaid;
	}

	public String getServiceareaname() {
		return serviceareaname;
	}

	public void setServiceareaname(String serviceareaname) {
		this.serviceareaname = serviceareaname;
	}

	public String getServicearearemark() {
		return servicearearemark;
	}

	public void setServicearearemark(String servicearearemark) {
		this.servicearearemark = servicearearemark;
	}

	public long getCustomerid() {
		return customerid;
	}

	public void setCustomerid(long customerid) {
		this.customerid = customerid;
	}

	public String getServid() {
		return servid;
	}

	public void setServid(String servid) {
		this.servid = servid;
	}

}

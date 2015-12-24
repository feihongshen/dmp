package cn.explink.domain.addressvo;

import java.util.Date;

public class AddressCustomerStationVO {

	/**
	 * id
	 */
	private Integer id;

	/**
	 * 客户id
	 */
	private Integer customerid;

	/**
	 * 客户名称
	 */
	private String customerName;

	/**
	 * 机构id
	 */
	private String branchid;

	/**
	 * 机构名称
	 */
	private String branchName;

	/**
	 * 创建人id
	 */
	private Integer creatorid;

	/**
	 * 创建人名称
	 */
	private String creatorname;

	/**
	 * 创建时间
	 */
	private Date createtime;

	private String execute_branchid;

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getCustomerid() {
		return this.customerid;
	}

	public void setCustomerid(Integer customerid) {
		this.customerid = customerid;
	}

	public String getExecute_branchid() {
		return execute_branchid;
	}

	public void setExecute_branchid(String execute_branchid) {
		this.execute_branchid = execute_branchid;
	}

	public String getBranchid() {
		return branchid;
	}

	public void setBranchid(String branchid) {
		this.branchid = branchid;
	}

	public Integer getCreatorid() {
		return this.creatorid;
	}

	public void setCreatorid(Integer creatorid) {
		this.creatorid = creatorid;
	}

	public String getCreatorname() {
		return this.creatorname;
	}

	public void setCreatorname(String creatorname) {
		this.creatorname = creatorname;
	}

	public Date getCreatetime() {
		return this.createtime;
	}

	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}

	public String getCustomerName() {
		return this.customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getBranchName() {
		return this.branchName;
	}

	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}

}

package cn.explink.domain.addressvo;

import java.util.Date;

public class AddressCustomerStation {

	/**
	 * id
	 */
	private Integer id;

	/**
	 * 客户id
	 */
	private Integer customerid;

	/**
	 * 机构id
	 */
	private Integer branchid;

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

	public Integer getBranchid() {
		return this.branchid;
	}

	public void setBranchid(Integer branchid) {
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

}

/**
 *
 */
package cn.explink.domain;

import java.math.BigDecimal;

/**
 * @author Administrator
 *
 */
public class PFcollection {
	private long id;
	private long customerid;
	private BigDecimal collectionPFfee;
	private String remark;
	private int typeid;
	private long pfruleid;
	private int tabid;
	/**
	 * @return the tabid
	 */
	public int getTabid() {
		return this.tabid;
	}
	/**
	 * @param tabid the tabid to set
	 */
	public void setTabid(int tabid) {
		this.tabid = tabid;
	}
	/**
	 * @return the id
	 */
	public long getId() {
		return this.id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(long id) {
		this.id = id;
	}
	/**
	 * @return the customerid
	 */
	public long getCustomerid() {
		return this.customerid;
	}
	/**
	 * @param customerid the customerid to set
	 */
	public void setCustomerid(long customerid) {
		this.customerid = customerid;
	}
	/**
	 * @return the collectionPFfee
	 */
	public BigDecimal getCollectionPFfee() {
		return this.collectionPFfee;
	}
	/**
	 * @param collectionPFfee the collectionPFfee to set
	 */
	public void setCollectionPFfee(BigDecimal collectionPFfee) {
		this.collectionPFfee = collectionPFfee;
	}
	/**
	 * @return the remark
	 */
	public String getRemark() {
		return this.remark;
	}
	/**
	 * @param remark the remark to set
	 */
	public void setRemark(String remark) {
		this.remark = remark;
	}
	/**
	 * @return the typeid
	 */
	public int getTypeid() {
		return this.typeid;
	}
	/**
	 * @param typeid the typeid to set
	 */
	public void setTypeid(int typeid) {
		this.typeid = typeid;
	}
	/**
	 * @return the pfruleid
	 */
	public long getPfruleid() {
		return this.pfruleid;
	}
	/**
	 * @param pfruleid the pfruleid to set
	 */
	public void setPfruleid(long pfruleid) {
		this.pfruleid = pfruleid;
	}

}

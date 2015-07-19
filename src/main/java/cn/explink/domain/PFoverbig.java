/**
 *
 */
package cn.explink.domain;

import java.math.BigDecimal;

/**
 * @author Administrator
 *
 */
public class PFoverbig {
	private long id;
	private long mincount;
	private long maxcount;
	private BigDecimal subsidyfee;
	private String remark;
	private long areaid;
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
	 * @return the mincount
	 */
	public long getMincount() {
		return this.mincount;
	}
	/**
	 * @param mincount the mincount to set
	 */
	public void setMincount(long mincount) {
		this.mincount = mincount;
	}
	/**
	 * @return the maxcount
	 */
	public long getMaxcount() {
		return this.maxcount;
	}
	/**
	 * @param maxcount the maxcount to set
	 */
	public void setMaxcount(long maxcount) {
		this.maxcount = maxcount;
	}
	/**
	 * @return the fee
	 */
	public BigDecimal getSubsidyfee() {
		return this.subsidyfee;
	}
	/**
	 * @param fee the fee to set
	 */
	public void setSubsidyfee(BigDecimal subsidyfee) {
		this.subsidyfee = subsidyfee;
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
	 * @return the areaid
	 */
	public long getAreaid() {
		return this.areaid;
	}
	/**
	 * @param areaid the areaid to set
	 */
	public void setAreaid(long areaid) {
		this.areaid = areaid;
	}

}

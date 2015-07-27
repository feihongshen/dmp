/**
 *
 */
package cn.explink.domain;

import java.math.BigDecimal;

/**
 * @author Administrator
 *
 */
public class PFoverweight {
	private long id;
	private BigDecimal mincount;
	private BigDecimal maxcount;
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
	public BigDecimal getMincount() {
		return this.mincount;
	}
	/**
	 * @param mincount the mincount to set
	 */
	public void setMincount(BigDecimal mincount) {
		this.mincount = mincount;
	}
	/**
	 * @return the maxcount
	 */
	public BigDecimal getMaxcount() {
		return this.maxcount;
	}
	/**
	 * @param maxcount the maxcount to set
	 */
	public void setMaxcount(BigDecimal maxcount) {
		this.maxcount = maxcount;
	}
	/**
	 * @return the fee
	 */
	/**
	 * @return the subsidyfee
	 */
	public BigDecimal getSubsidyfee() {
		return this.subsidyfee;
	}
	/**
	 * @param subsidyfee the subsidyfee to set
	 */
	public void setSubsidyfee(BigDecimal subsidyfee) {
		this.subsidyfee = subsidyfee;
	}
	/**
	 * @param fee the fee to set
	 */

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
